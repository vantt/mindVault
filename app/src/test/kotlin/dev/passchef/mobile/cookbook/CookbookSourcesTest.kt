package dev.passchef.mobile.cookbook

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Test
import org.junit.Assert.*
import java.nio.file.Files

/** Real production coordinator/cache with a deferred IO dispatcher; no Google/hardware mocks. */
@OptIn(ExperimentalCoroutinesApi::class)
class CookbookSourcesTest {
    @Test fun acceptedFavoriteWriteBlocksSecondMetadataClickUntilSnapshotIsPublished() = runTest {
        val dispatcher=StandardTestDispatcher(testScheduler); Dispatchers.setMain(dispatcher)
        val dir=Files.createTempDirectory("cookbook-metadata-").toFile()
        val origin=MenuOrigin("Menu_0123456789",0,"Offline")
        val mapping=MenuMapping(origin,mapOf(ColumnRole.RECIPE to 0))
        val initial=MenuSnapshot(mapping=mapping,entries=MenuTable.project(listOf(listOf("abc#1"),listOf("def#1")),mapping),refreshedAt=1)
        val source=CookbookSources(dir,{true},dispatcher)
        try {
            MenuSnapshotStore(dir).save(initial);source.reload();advanceUntilIdle()
            val first=initial.entries[0];val second=initial.entries[1]
            source.favorite(first); assertTrue(source.busy)
            source.favorite(second); source.recent(second) // disabled, not accepted into pending transaction
            advanceUntilIdle();assertFalse(source.busy)
            assertEquals(setOf(first.id),source.snapshots.single().favorites)
            source.favorite(second);advanceUntilIdle()
            source.recent(second);advanceUntilIdle()
            val saved=MenuSnapshotStore(dir).load(origin)!!
            assertEquals(setOf(first.id,second.id),saved.favorites)
            assertEquals(listOf(second.id),saved.recent)
        } finally { source.destroy();Dispatchers.resetMain();dir.deleteRecursively() }
    }
    @Test fun acceptedClearBlocksReplacementUntilDeletionCompletesAndLockCancelsQueuedWrite() = runTest {
        val dispatcher=StandardTestDispatcher(testScheduler);Dispatchers.setMain(dispatcher)
        val dir=Files.createTempDirectory("cookbook-clear-").toFile()
        val origin=MenuOrigin("Menu_0123456789",0,"Offline")
        val mapping=MenuMapping(origin,mapOf(ColumnRole.RECIPE to 0))
        val initial=MenuSnapshot(mapping=mapping,entries=MenuTable.project(listOf(listOf("abc#1")),mapping),refreshedAt=1)
        var ready=true;val source=CookbookSources(dir,{ready},dispatcher)
        try {
            MenuSnapshotStore(dir).save(initial);source.reload();advanceUntilIdle()
            source.favorite(initial.entries.single());ready=false;source.invalidate();advanceUntilIdle()
            assertTrue(MenuSnapshotStore(dir).load(origin)!!.favorites.isEmpty())
            ready=true;source.reload();advanceUntilIdle()
            source.remove(origin);assertTrue(source.busy)
            source.offline(origin.menuId,"Replacement","def#1")
            advanceUntilIdle();assertFalse(source.busy)
            assertNull(MenuSnapshotStore(dir).load(origin));assertNull(source.draft)
            source.offline(origin.menuId,"Replacement","def#1");advanceUntilIdle()
            source.confirm(mapOf(ColumnRole.RECIPE to 0),1,emptyMap());advanceUntilIdle()
            assertFalse(source.busy);assertEquals("def#1",source.snapshots.single().entries.single().recipe)
        } finally {source.destroy();Dispatchers.resetMain();dir.deleteRecursively()}
    }
    @Test fun existingOfflineRefreshOwnsPendingWriteAndGrantWaitBlocksCompetingIntents() = runTest {
        val dispatcher=StandardTestDispatcher(testScheduler);Dispatchers.setMain(dispatcher)
        val dir=Files.createTempDirectory("cookbook-refresh-").toFile()
        val origin=MenuOrigin("Menu_0123456789",0,"Offline")
        val mapping=MenuMapping(origin,mapOf(ColumnRole.RECIPE to 0))
        val prior=MenuSnapshot(mapping=mapping,entries=MenuTable.project(listOf(listOf("abc#1")),mapping),refreshedAt=1)
        val source=CookbookSources(dir,{true},dispatcher)
        try {
            MenuSnapshotStore(dir).save(prior);source.reload();advanceUntilIdle()
            source.offline(origin.menuId,origin.title,"def#1");assertTrue(source.busy)
            source.favorite(prior.entries.single());source.offline(origin.menuId,origin.title,"ghi#1")
            advanceUntilIdle();assertFalse(source.busy)
            assertEquals("def#1",MenuSnapshotStore(dir).load(origin)!!.entries.single().recipe)
            assertTrue(source.awaitGrant());assertTrue(source.busy)
            source.remove(origin);source.offline(origin.menuId,origin.title,"ghi#1")
            advanceUntilIdle();assertEquals("def#1",MenuSnapshotStore(dir).load(origin)!!.entries.single().recipe)
            source.disconnected();assertFalse(source.busy)
            source.offline(origin.menuId,origin.title,"ghi#1");advanceUntilIdle()
            assertEquals("ghi#1",source.snapshots.single().entries.single().recipe)
        } finally {source.destroy();Dispatchers.resetMain();dir.deleteRecursively()}
    }

}
