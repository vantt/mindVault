package dev.passchef.mobile.cookbook

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test
import java.nio.file.Files

/** Exercises coordinator error/reconfirmation state as well as durable last-good bytes. */
@OptIn(ExperimentalCoroutinesApi::class)
class CookbookSourceMappingRecoveryTest {
    private val origin = MenuOrigin("Menu_0123456789", 0, "Accounts")
    private fun original(): MenuSnapshot {
        val header = listOf("Label", "Recipe", "Username")
        val mapping = MenuMapping(origin, mapOf(ColumnRole.LABEL to 0,
            ColumnRole.RECIPE to 1, ColumnRole.USERNAME to 2), 2, header)
        val entries = MenuTable.project(listOf(header, listOf("Mail", "abc#1", "00123")), mapping)
        return MenuSnapshot(mapping = mapping, entries = entries, refreshedAt = 1,
            favorites = setOf(entries.single().id), recent = listOf(entries.single().id))
    }

    @Test fun driftAndInvalidConfirmationKeepLastGoodBytesUntilExplicitValidRemap() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler); Dispatchers.setMain(dispatcher)
        val directory = Files.createTempDirectory("cookbook-reconfirmation-").toFile()
        val source = CookbookSources(directory, { true }, dispatcher)
        try {
            val prior = original(); MenuSnapshotStore(directory).save(prior)
            source.reload(); advanceUntilIdle()
            val file = directory.listFiles()!!.single(); val before = file.readBytes()
            source.offline(origin.menuId, origin.title, "Recipe\tLabel\tUsername\ndef#1\tBank\t00123")
            advanceUntilIdle()
            assertEquals("RECONFIRM", source.status)
            assertFalse(source.busy)
            val staged = source.draft
            assertNotNull(staged)
            assertEquals(listOf(prior), source.snapshots)
            assertArrayEquals(before, file.readBytes())
            source.confirm(mapOf(ColumnRole.RECIPE to 0, ColumnRole.USERNAME to 0), 2, emptyMap())
            advanceUntilIdle()
            assertEquals("INVALID_MAPPING", source.status)
            assertFalse(source.busy)
            assertSame(staged, source.draft)
            assertEquals(listOf(prior), source.snapshots)
            assertArrayEquals(before, file.readBytes())
            assertEquals(prior, MenuSnapshotStore(directory).load(origin))
            source.confirm(mapOf(ColumnRole.RECIPE to 0, ColumnRole.LABEL to 1,
                ColumnRole.USERNAME to 2), 2, emptyMap()); advanceUntilIdle()
            assertEquals("SYNCED", source.status)
            assertFalse(source.busy)
            assertNull(source.draft)
            val saved = MenuSnapshotStore(directory).load(origin)!!
            assertEquals(listOf(saved), source.snapshots)
            assertEquals(listOf("Recipe", "Label", "Username"), saved.mapping.confirmedHeader)
            assertEquals(0, saved.mapping.columns[ColumnRole.RECIPE])
            assertEquals(2, saved.entries.single().row)
            assertEquals("def#1", saved.entries.single().recipe)
            assertEquals("Bank", saved.entries.single().label)
            assertEquals("00123", saved.entries.single().username)
        } finally { source.destroy(); Dispatchers.resetMain(); directory.deleteRecursively() }
    }

    @Test fun oversizedOfflineReplacementReportsInvalidTableWithoutReplacingGoodCache() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler); Dispatchers.setMain(dispatcher)
        val directory = Files.createTempDirectory("cookbook-invalid-replacement-").toFile()
        val source = CookbookSources(directory, { true }, dispatcher)
        try {
            val prior = original(); MenuSnapshotStore(directory).save(prior)
            source.reload(); advanceUntilIdle()
            val file = directory.listFiles()!!.single(); val before = file.readBytes()
            source.offline(origin.menuId, origin.title, "x".repeat(120_001))
            advanceUntilIdle()
            assertEquals("INVALID_TABLE", source.status)
            assertFalse(source.busy)
            assertNull(source.draft)
            assertNull(source.metadata)
            assertEquals(listOf(prior), source.snapshots)
            assertArrayEquals(before, file.readBytes())
            assertEquals(prior, MenuSnapshotStore(directory).load(origin))
            source.offline(origin.menuId, origin.title, "Label\tRecipe\tUsername\nMail\tdef#1\t00123")
            advanceUntilIdle()
            assertEquals("SYNCED", source.status)
            assertFalse(source.busy)
            val saved = MenuSnapshotStore(directory).load(origin)!!
            assertEquals("def#1", saved.entries.single().recipe)
            assertEquals(prior.favorites, saved.favorites)
            assertEquals(prior.recent, saved.recent)
            assertEquals(listOf(saved), source.snapshots)
        } finally { source.destroy(); Dispatchers.resetMain(); directory.deleteRecursively() }
    }

    @Test fun failedFavoriteCommitReportsCacheFailureAndCanRetryWithoutLosingSnapshot() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler); Dispatchers.setMain(dispatcher)
        val directory = Files.createTempDirectory("cookbook-favorite-write-failure-").toFile()
        val source = CookbookSources(directory, { true }, dispatcher)
        try {
            val prior = original(); MenuSnapshotStore(directory).save(prior)
            source.reload(); advanceUntilIdle()
            val file = directory.listFiles()!!.single(); val before = file.readBytes()
            val obstruction = java.io.File(directory, file.name + ".pending")
            assertTrue(obstruction.mkdir())
            java.io.File(obstruction, "keep").writeText("owned test obstruction")
            source.favorite(prior.entries.single()); assertTrue(source.busy)
            advanceUntilIdle()
            assertEquals("CACHE_FAILED", source.status)
            assertFalse(source.busy)
            assertEquals(listOf(prior), source.snapshots)
            assertArrayEquals(before, file.readBytes())
            assertEquals(prior, MenuSnapshotStore(directory).load(origin))
            assertTrue(obstruction.deleteRecursively())
            source.favorite(prior.entries.single()); advanceUntilIdle()
            assertFalse(source.busy)
            val saved = MenuSnapshotStore(directory).load(origin)!!
            assertTrue(saved.favorites.isEmpty())
            assertEquals(prior.entries, saved.entries)
            assertEquals(prior.recent, saved.recent)
            assertEquals(listOf(saved), source.snapshots)
        } finally { source.destroy(); Dispatchers.resetMain(); directory.deleteRecursively() }
    }
}
