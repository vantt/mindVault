package dev.passchef.mobile.cookbook

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test
import java.nio.file.Files

/** Real coordinator/files; immediate Main reaches deferred IO before readiness changes. */
@OptIn(ExperimentalCoroutinesApi::class)
class CookbookSourceCommitBoundaryTest {
    private val origin = MenuOrigin("Menu_0123456789", 0, "Offline")
    private val columns = mapOf(ColumnRole.RECIPE to 0)
    private fun initial(): MenuSnapshot {
        val mapping = MenuMapping(origin, columns)
        return MenuSnapshot(mapping = mapping,
            entries = MenuTable.project(listOf(listOf("abc#1")), mapping), refreshedAt = 1)
    }

    @Test fun readinessLossBeforeDispatchedConfirmationCommitLeavesNoCache() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val directory = Files.createTempDirectory("cookbook-confirm-boundary-").toFile()
        var ready = true
        val source = CookbookSources(directory, { ready }, StandardTestDispatcher(testScheduler))
        try {
            source.offline(origin.menuId, origin.title, "abc#1")
            assertEquals("CONFIRM", source.status)
            val staged = source.draft
            assertNotNull(staged)
            source.confirm(columns, 1, emptyMap())
            assertTrue(source.busy)
            assertNull(MenuSnapshotStore(directory).load(origin))
            ready = false // No invalidate: the commit itself must recheck readiness.
            advanceUntilIdle()
            assertNull(MenuSnapshotStore(directory).load(origin))
            assertTrue(source.snapshots.isEmpty())
            assertSame(staged, source.draft)
            source.invalidate(); ready = true
            source.offline(origin.menuId, origin.title, "def#1")
            source.confirm(columns, 1, emptyMap()); advanceUntilIdle()
            assertEquals("SYNCED", source.status)
            assertFalse(source.busy)
            assertEquals("def#1", MenuSnapshotStore(directory).load(origin)!!.entries.single().recipe)
        } finally { source.destroy(); Dispatchers.resetMain(); directory.deleteRecursively() }
    }

    @Test fun readinessLossBeforeDispatchedRemovalCannotDeleteLastGoodCache() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val directory = Files.createTempDirectory("cookbook-remove-boundary-").toFile()
        var ready = true
        val source = CookbookSources(directory, { ready }, StandardTestDispatcher(testScheduler))
        try {
            val original = initial(); MenuSnapshotStore(directory).save(original)
            source.reload(); advanceUntilIdle()
            val file = directory.listFiles()!!.single(); val before = file.readBytes()
            source.remove(origin)
            assertTrue(source.busy)
            assertTrue(file.exists())
            ready = false
            advanceUntilIdle()
            assertArrayEquals(before, file.readBytes())
            assertEquals(listOf(original), source.snapshots)
            source.invalidate(); ready = true; source.reload(); advanceUntilIdle()
            assertFalse(source.busy)
            assertEquals(original, MenuSnapshotStore(directory).load(origin))
            source.remove(origin); advanceUntilIdle()
            assertNull(MenuSnapshotStore(directory).load(origin))
            assertTrue(source.snapshots.isEmpty())
        } finally { source.destroy(); Dispatchers.resetMain(); directory.deleteRecursively() }
    }

    @Test fun invalidatedDispatchedRefreshCannotOverwriteAcceptedReplacement() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val directory = Files.createTempDirectory("cookbook-refresh-boundary-").toFile()
        val source = CookbookSources(directory, { true }, StandardTestDispatcher(testScheduler))
        try {
            val original = initial(); MenuSnapshotStore(directory).save(original)
            source.reload(); advanceUntilIdle()
            val file = directory.listFiles()!!.single(); val before = file.readBytes()
            source.offline(origin.menuId, origin.title, "def#1")
            assertTrue(source.busy)
            assertArrayEquals(before, file.readBytes())
            assertEquals(listOf(original), source.snapshots)
            source.invalidate()
            source.offline(origin.menuId, origin.title, "ghi#1")
            assertTrue(source.busy)
            advanceUntilIdle()
            assertFalse(source.busy)
            assertEquals("SYNCED", source.status)
            val saved = MenuSnapshotStore(directory).load(origin)!!
            assertEquals("ghi#1", saved.entries.single().recipe)
            assertEquals(listOf(saved), source.snapshots)
            assertNull(source.draft)
        } finally { source.destroy(); Dispatchers.resetMain(); directory.deleteRecursively() }
    }
}
