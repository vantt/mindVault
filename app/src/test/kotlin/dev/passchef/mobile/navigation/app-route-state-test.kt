package dev.passchef.mobile.navigation

import org.junit.Assert.*
import org.junit.Test

/** Spec §2 back rules and §8 lock survival (the holder is plain state; lock never touches it). */
class AppRouteStateTest {
    @Test fun backPopsPushedScreenThenReturnsToPasswordsThenFinishes() {
        val routes = AppRouteState()
        routes.select(AppTab.SETTINGS); routes.push(AppRoute.Connect)
        assertTrue(routes.back()); assertNull(routes.route); assertEquals(AppTab.SETTINGS, routes.tab)
        assertTrue(routes.back()); assertEquals(AppTab.PASSWORDS, routes.tab)
        assertFalse(routes.canGoBack); assertFalse(routes.back())
    }
    @Test fun eachTabKeepsItsOwnStackAndReselectPopsToRoot() {
        val routes = AppRouteState()
        routes.push(AppRoute.EntryDetail("id:1:2"))
        routes.select(AppTab.SETTINGS); routes.push(AppRoute.SecretParts)
        routes.select(AppTab.PASSWORDS); assertEquals(AppRoute.EntryDetail("id:1:2"), routes.route)
        routes.select(AppTab.PASSWORDS); assertNull(routes.route)
        routes.select(AppTab.SETTINGS); assertEquals(AppRoute.SecretParts, routes.route)
    }
    @Test fun pushIgnoresDuplicateTopAndRemoveWhereCleansStacks() {
        val routes = AppRouteState()
        routes.open(AppTab.SETTINGS, AppRoute.SheetDetail("k")); routes.push(AppRoute.SheetDetail("k"))
        assertTrue(routes.pop()); assertNull(routes.route)
        routes.push(AppRoute.SheetDetail("k")); routes.removeWhere { it == AppRoute.SheetDetail("k") }
        assertNull(routes.route)
    }
    @Test fun freshLaunchClearsHolderButSameProcessKeepsIt() {
        AppRouteStateHolder.fresh()
        AppRouteStateHolder.state.open(AppTab.SETTINGS, AppRoute.Connect)
        AppRouteStateHolder.state.connect = ConnectProgress(step = ConnectStep.MAP, fileId = "abcdefghij12")
        assertEquals(AppRoute.Connect, AppRouteStateHolder.state.route) // a lock keeps the same holder
        AppRouteStateHolder.fresh()
        assertNull(AppRouteStateHolder.state.route); assertNull(AppRouteStateHolder.state.connect.fileId)
    }
}
