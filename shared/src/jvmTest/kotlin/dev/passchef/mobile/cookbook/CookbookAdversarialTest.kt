package dev.passchef.mobile.cookbook

import java.io.File
import java.util.concurrent.CompletableFuture
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Independent hostile inputs/ownership boundaries; no production-generated Dish oracle. */
class CookbookAdversarialTest {
    private val header = listOf("Service", "Recipe", "Username")
    private val rows = listOf(header, listOf("Mail", "abc#1", "00123"), listOf("Bank", "xyz@1", "user"))
    private fun snapshot(id: String = "Menu_0123456789", tab: Int = 0): MenuSnapshot {
        val origin = MenuOrigin(id, tab, "Accounts")
        val mapping = MenuMapping(origin, mapOf(ColumnRole.LABEL to 0, ColumnRole.RECIPE to 1,
            ColumnRole.USERNAME to 2), firstDataRow = 2, confirmedHeader = header)
        return MenuSnapshot(mapping = mapping, entries = MenuTable.project(rows, mapping), refreshedAt = 100)
    }
    private fun temporary(block: (File) -> Unit) {
        val directory = createTempDirectory("passchef-adversarial-").toFile()
        try { block(directory) } finally { directory.deleteRecursively() }
    }

    @Test fun equalRecipeColumnsDoNotGuessAndSparseEvidenceCannotClaimConfidence() {
        val tied = listOf(listOf("a#1", "x@1"), listOf("b#1", "y@1"), listOf("c#1", "z@1"))
        assertNull(MenuTable.proposal(tied))
        assertNull(MenuTable.proposal(listOf(listOf("a#1"), listOf("b#1"))))
        assertFails { MenuMapping(snapshot().mapping.origin,
            mapOf(ColumnRole.RECIPE to 0, ColumnRole.USERNAME to 0)) }
    }

    @Test fun mappingDriftAndDisappearedHeaderPreserveLastAcceptedOfflineSnapshot() {
        val original = snapshot()
        val moved = listOf(listOf("Recipe", "Service", "Username"), listOf("abc#1", "Mail", "00123"))
        assertFails { MenuTable.refresh(moved, original, 200) }
        assertFails { MenuTable.refresh(emptyList(), original, 200) }
        assertFails { MenuTable.refresh(listOf(header, listOf("Mail", "not a Recipe", "00123")), original, 200) }
        assertEquals("abc#1", original.entries.first().recipe)
        assertEquals(100L, original.refreshedAt)
    }

    @Test fun projectionRetainsPhysicalRowsAndLiteralUsernameWithoutSearchingIngredients() {
        val original = snapshot()
        val mapping = original.mapping.copy(firstDataRow = 3, confirmedHeader = null)
        val entries = MenuTable.project(listOf(listOf("Ignored", "ignore#1"), header,
            listOf("Mail", "UniqueIngredient#1", "00123")), mapping)
        assertEquals(3, entries.single().row)
        assertEquals("00123", entries.single().username)
        assertEquals(entries, searchEntries(entries, "0012"))
        assertTrue(searchEntries(entries, "UniqueIngredient").isEmpty())
        assertFails { original.copy(entries = listOf(original.entries.first().copy(row = 1))) }
    }

    @Test fun hostileCacheCannotBindEntriesToAnotherMenuOrDuplicateRowIdentity() {
        val original = snapshot()
        val alien = original.entries.first().copy(origin = MenuOrigin("Alien_0123456789", 1, "Other"))
        assertFails { original.copy(entries = listOf(alien)) }
        assertFails { original.copy(entries = listOf(original.entries.first(), original.entries.first())) }
        assertFails { original.copy(favorites = setOf("missing-row")) }
        assertFails { MenuOrigin("../private-file", 0, "Bad") }
        assertFails { MenuTable.validate(List(MAX_ROWS + 1) { emptyList() }) }
        assertFails { MenuTable.validate(listOf(listOf("x".repeat(MAX_CELL + 1)))) }
    }

    @Test fun failedRealFilesystemWriteDoesNotReplaceGoodCache() = temporary { directory ->
        val store = MenuSnapshotStore(directory)
        val original = snapshot()
        store.save(original)
        val file = directory.listFiles()!!.single()
        val before = file.readBytes()
        // Force a real filesystem write failure without permissions dependent on test UID.
        val obstruction = File(directory, file.name + ".pending")
        assertTrue(obstruction.mkdir())
        File(obstruction, "keep").writeText("owned obstruction")
        assertFails { store.save(original.copy(refreshedAt = 200)) }
        assertTrue(before.contentEquals(file.readBytes()))
        assertEquals(original, store.load(original.mapping.origin))
    }

    @Test fun wrongFileOriginPayloadIsRefusedInsteadOfRebindingARecipe() = temporary { directory ->
        val store = MenuSnapshotStore(directory)
        val first = snapshot()
        val second = snapshot("Second_0123456789", 1)
        store.save(first)
        val firstFile = directory.listFiles()!!.single()
        store.save(second)
        val secondFile = directory.listFiles()!!.single { it != firstFile }
        firstFile.writeBytes(secondFile.readBytes())
        assertFails { store.load(first.mapping.origin) }
        assertEquals(listOf(second), store.inventory().snapshots)
        assertEquals(1, store.inventory().corruptFiles)
    }

    @Test fun malformedUtf8InsideOtherwiseValidJsonMustNotSilentlyChangeAccountLabel() = temporary { directory ->
        val store = MenuSnapshotStore(directory)
        val original = snapshot()
        store.save(original)
        val file = directory.listFiles()!!.single()
        val bytes = file.readBytes()
        val index = bytes.toString(Charsets.UTF_8).indexOf("\"label\":\"Mail\"") + "\"label\":\"".length
        assertTrue(index >= "\"label\":\"".length)
        // Invalid two-byte sequence remains inside the JSON string, not JSON punctuation.
        bytes[index] = 0xC3.toByte(); bytes[index + 1] = 0x28.toByte()
        file.writeBytes(bytes)
        assertFails { store.load(original.mapping.origin) }
        assertEquals(1, store.inventory().corruptFiles)
        assertTrue(store.inventory().snapshots.isEmpty())
    }

    @Test fun sourceLimitRefusesNewSourceWithoutEvictingPreviouslyAcceptedCache() = temporary { directory ->
        val store = MenuSnapshotStore(directory)
        val first = snapshot()
        store.save(first)
        repeat(19) { store.save(snapshot("Extra_${it.toString().padStart(10, '0')}", it + 1)) }
        assertFails { store.save(snapshot("Overflow_0123456789", 30)) }
        assertEquals(20, store.inventory().snapshots.size)
        assertEquals(first, store.load(first.mapping.origin))
        store.save(first.copy(refreshedAt = 200))
        assertEquals(200L, store.load(first.mapping.origin)!!.refreshedAt)
    }

    @Test fun completionFromRetiredOpenCannotPublishIntoReopenedCookbook() {
        val lifetime = DishLifetime()
        val retired = lifetime.invalidate()
        lifetime.invalidate() // lock/background
        val reopened = lifetime.invalidate()
        assertFalse(lifetime.publish(retired, "RetiredDish", true))
        assertNull(lifetime.forCopy(true))
        assertFalse(lifetime.publish(reopened, "LockedDish", false))
        assertTrue(lifetime.publish(reopened, "CurrentDish", true))
        lifetime.reveal(1_000, true)
        assertEquals("CurrentDish", lifetime.visible(10_999, true))
        assertNull(lifetime.visible(11_000, true))
        assertNull(lifetime.forCopy(false))
        lifetime.invalidate()
        assertNull(lifetime.forCopy(true))
    }

    @Test fun oldClipboardTimerReadDenialAndReplacementDoNotAuthorizeOverwrite() {
        val policy = ClipboardOwnership()
        val old = policy.copied("FirstDish", 0)
        assertFalse(policy.shouldClear(old, 29_999, "FirstDish"))
        assertFalse(policy.shouldClear(old, 30_000, null))
        assertFalse(policy.shouldClear(old, 30_000, "User later text"))
        val current = policy.copied("SecondDish", 30_000)
        assertFalse(policy.shouldClear(old, 60_000, "SecondDish"))
        assertTrue(policy.shouldClear(current, 60_000, "SecondDish"))
        policy.forget() // separate username copy
        assertFalse(policy.shouldClear(current, 60_000, "SecondDish"))
    }

    @Test fun cancelledAuthorizationReplyCannotGrantIntoReopenedLifetime() {
        val owner = SourceOwnership()
        val old = owner.begin("authorization")
        val reply = CompletableFuture<String>()
        var grant: String? = null
        val delivered = reply.thenApply { if (owner.accepts(old, true)) grant = it }
        owner.invalidate()
        val reopened = owner.begin("authorization")
        reply.complete("RetiredSyntheticGrant"); delivered.join()
        assertNull(grant)
        assertTrue(owner.accepts(reopened, true))
    }

    @Test fun deferredRefreshAfterClearCannotRecreateRemovedPrivateCache() = temporary { directory ->
        val store = MenuSnapshotStore(directory); val saved = snapshot(); store.save(saved)
        val owner = SourceOwnership(); val refresh = owner.begin(saved.mapping.origin.key)
        val response = CompletableFuture<MenuSnapshot>()
        val lateCommit = response.thenApply { owner.commit(refresh, { true }) { store.save(it) } }
        owner.invalidate(); val removal = owner.begin(saved.mapping.origin.key)
        assertTrue(owner.commit(removal, { true }) { store.remove(saved.mapping.origin) })
        response.complete(saved.copy(refreshedAt = 200))
        assertFalse(lateCommit.join())
        assertNull(store.load(saved.mapping.origin))
    }

    @Test fun sourceSwitchAndLockedReadinessRejectDeferredWriteBeforeItRuns() {
        val owner = SourceOwnership(); val retired = owner.begin("Menu_A:0")
        val response = CompletableFuture<Unit>(); var writes = 0
        val lateCommit = response.thenApply { owner.commit(retired, { true }) { writes++ } }
        val current = owner.begin("Menu_B:1")
        response.complete(Unit)
        assertFalse(lateCommit.join())
        assertFalse(owner.commit(current, { false }) { writes++ })
        assertEquals(0, writes)
        assertTrue(owner.commit(current, { true }) { writes++ })
        assertEquals(1, writes)
    }
}
