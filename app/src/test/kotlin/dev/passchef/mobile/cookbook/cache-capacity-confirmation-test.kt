package dev.passchef.mobile.cookbook

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Test
import java.nio.file.Files

/** A valid Recipe mapping must not be blamed for a refused durable cache commit. */
@OptIn(ExperimentalCoroutinesApi::class)
class CacheCapacityConfirmationTest {
    @Test fun validTwentyFirstMenuPreservesCacheAndReportsStorageFailure() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        val directory = Files.createTempDirectory("cookbook-capacity-confirmation-").toFile()
        val source = CookbookSources(directory, { true }, dispatcher)
        try {
            val store = MenuSnapshotStore(directory)
            repeat(20) { index ->
                val origin = MenuOrigin("Capacity_Menu_${index.toString().padStart(2, '0')}", 0, "Menu $index")
                val mapping = MenuMapping(origin, mapOf(ColumnRole.RECIPE to 0))
                store.save(MenuSnapshot(mapping = mapping,
                    entries = listOf(MenuEntry(origin, 1, "abcd#1", "Row 1")), refreshedAt = 1))
            }
            source.reload(); advanceUntilIdle()
            val original = source.snapshots.toList()
            val bytes = directory.listFiles()!!.associate { it.name to it.readBytes() }
            assertEquals(20, original.size)
            val newId = "Capacity_Menu_21"
            source.offline(newId, "New Menu", "abcd#1"); advanceUntilIdle()
            assertEquals("CONFIRM", source.status)
            val staged = source.draft
            assertNotNull(staged)
            source.confirm(mapOf(ColumnRole.RECIPE to 0), 1, emptyMap()); advanceUntilIdle()
            assertFalse(source.busy)
            assertSame(staged, source.draft)
            assertEquals(original, source.snapshots)
            assertEquals(20, store.inventory().snapshots.size)
            val after = directory.listFiles()!!.associate { it.name to it.readBytes() }
            assertEquals(bytes.keys, after.keys)
            bytes.forEach { (name, content) -> assertArrayEquals(content, after.getValue(name)) }
            assertEquals("CACHE_FAILED", source.status)
        } finally {
            source.destroy(); Dispatchers.resetMain(); directory.deleteRecursively()
        }
    }
}
