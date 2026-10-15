package dev.passchef.mobile.proof

import android.graphics.Rect
import android.os.Bundle
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Read-only geometry for the existing sampled edge. No input/hit-routing or target acceptance. */
@RunWith(AndroidJUnit4::class)
class LanguageEdgeGeometryPhysicalTest {
    @get:Rule val ui = createAndroidComposeRule<CookbookProofActivity>()
    private fun report(phase: String, flags: Map<String, Boolean>, counts: Map<String, Int>) {
        InstrumentationRegistry.getInstrumentation().sendStatus(2, Bundle().apply {
            putString("passchefLanguageGeometryPhase", phase)
            flags.forEach { (key, value) -> putBoolean("passchefLanguageGeometry_$key", value) }
            counts.forEach { (key, value) -> putInt("passchefLanguageGeometry_$key", value) }
        })
    }
    @Test fun observeSampledLanguageEdgeAgainstScrollAndLayoutAncestors() {
        val activity = ui.activity
        val directory = activity.proofDirectory
        val guards = SecurityUiObservation(activity)
        try {
            ui.waitForIdle(); guards.foreground()
            hideTimerIme(activity, guards::foreground)
            val initialVi = ui.onAllNodesWithText("PassChef — Cookbook riêng").fetchSemanticsNodes().isNotEmpty()
            ui.onNodeWithText(if (initialVi) "PassChef — Cookbook riêng" else "PassChef — private Cookbook").assertExists()
            val controls = ui.onAllNodes(hasClickAction() and (hasText("VI") or hasText("EN"))).fetchSemanticsNodes()
            assertTrue("One actual language control is required", controls.size == 1)
            val scrolls = ui.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.ScrollBy),
                useUnmergedTree = true).fetchSemanticsNodes()
            val mergedScrolls = ui.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.ScrollBy)).fetchSemanticsNodes()
            assertTrue("Actual own locked scroll observation is bounded and nonempty", scrolls.size in 1..32)
            assertTrue("Merged scroll observation is bounded and nonempty", mergedScrolls.size in 1..32)
            val observed = timerMain {
                val node = controls.single()
                val visual = node.boundsInWindow; val root = node.boundsInRoot; val touch = node.touchBoundsInRoot
                val dx = visual.left - root.left; val dy = visual.top - root.top
                val left = touch.left + dx; val right = touch.right + dx
                val top = touch.top + dy; val bottom = touch.bottom + dy
                val bottomAvailable = bottom - visual.bottom > 2
                val y = when {
                    bottomAvailable -> (visual.bottom + bottom) / 2
                    visual.top - top > 2 -> (top + visual.top) / 2
                    else -> Float.NaN
                }
                val point = Offset((left + right) / 2, y)
                val decor = activity.window.decorView
                val offset = IntArray(2).also(decor::getLocationOnScreen)
                val screenX = point.x + offset[0]; val screenY = point.y + offset[1]
                val visible = Rect().also(decor::getWindowVisibleDisplayFrame)
                val density = activity.resources.displayMetrics.density
                val insets = ViewCompat.getRootWindowInsets(decor)
                val valid = mapOf(
                    "finite" to listOf(point.x, point.y, dx, dy, left, right, top, bottom, density).all { it.isFinite() },
                    "placed" to (visual.width > 0 && visual.height > 0),
                    "insideExpanded" to (point.x > left && point.x < right && point.y > top && point.y < bottom),
                    "outsideVisual" to !visual.contains(point),
                    "declaredTouch48dp" to (density > 0 && touch.width >= 48 * density && touch.height >= 48 * density),
                    "clearEdgeMidpoint" to (bottomAvailable || visual.top - top > 2),
                    "insideOwnWindow" to (screenX >= offset[0] && screenY >= offset[1] &&
                        screenX < offset[0] + decor.width && screenY < offset[1] + decor.height),
                    "insideVisibleFrame" to (screenX >= visible.left && screenY >= visible.top && screenX < visible.right && screenY < visible.bottom),
                    "insetsAvailable" to (insets != null), "imeHidden" to (insets?.isVisible(WindowInsetsCompat.Type.ime()) == false))
                assertTrue("Same sampled edge has valid fresh geometry", valid.values.all { it })
                var info: LayoutInfo? = node.layoutInfo
                var ancestors = 0; var outside = 0; var targetContains = false
                val lineage = mutableListOf<LayoutInfo>()
                while (info != null) {
                    assertTrue("Layout observation is bounded and attached/placed", ancestors < 32 && info.isAttached && info.isPlaced)
                    val local = info.coordinates.windowToLocal(point)
                    assertTrue("Actual local point is finite", local.x.isFinite() && local.y.isFinite())
                    val size = info.coordinates.size
                    val contains = local.x >= 0 && local.y >= 0 && local.x < size.width && local.y < size.height
                    if (ancestors == 0) targetContains = contains else if (!contains) outside++
                    lineage.add(info); ancestors++; info = info.parentInfo
                }
                var covers = 0; var ancestorCovers = 0; var disjointCovers = 0
                for (scroll in scrolls) {
                    assertTrue("Observed scroll layouts remain attached and placed", scroll.layoutInfo.isAttached && scroll.layoutInfo.isPlaced)
                    val bounds = scroll.boundsInWindow
                    assertTrue("Observed scroll bounds are finite", listOf(bounds.left, bounds.top, bounds.right, bounds.bottom).all { it.isFinite() })
                    if (bounds.contains(point)) {
                        covers++
                        val ancestor = lineage.any { it === scroll.layoutInfo }
                        var parent: LayoutInfo? = scroll.layoutInfo
                        var levels = 0; var descendant = false
                        while (parent != null) {
                            assertTrue("Scroll lineage observation is bounded", levels++ < 32)
                            if (parent === node.layoutInfo) descendant = true
                            parent = parent.parentInfo
                        }
                        if (ancestor) ancestorCovers++ else if (!descendant) disjointCovers++
                    }
                }
                (valid + mapOf("bottomSample" to bottomAvailable, "targetLayoutContains" to targetContains,
                    "scrollBoundsCoverSample" to (covers > 0), "observed" to true)) to mapOf(
                    "layoutNodes" to ancestors, "ancestorLayoutsOutside" to outside,
                    "scrollNodes" to scrolls.size, "mergedScrollNodes" to mergedScrolls.size,
                    "coveringScrollNodes" to covers, "coveringAncestorScrollNodes" to ancestorCovers,
                    "coveringDisjointScrollNodes" to disjointCovers,
                    "rawWidthDpX1000" to (visual.width / density * 1000).toInt(),
                    "rawHeightDpX1000" to (visual.height / density * 1000).toInt(),
                    "targetLayoutWidthDpX1000" to (node.layoutInfo.coordinates.size.width / density * 1000).toInt(),
                    "targetLayoutHeightDpX1000" to (node.layoutInfo.coordinates.size.height / density * 1000).toInt(),
                    "touchHeightDpX1000" to (touch.height / density * 1000).toInt())
            }
            ui.onNodeWithText(if (initialVi) "PassChef — Cookbook riêng" else "PassChef — private Cookbook").assertExists()
            guards.foreground()
            report("language-geometry-observed", observed.first, observed.second)
        } catch (failure: Throwable) {
            runCatching { report("language-geometry-failed", mapOf("failed" to true), emptyMap()) }
            throw AssertionError("Language geometry observation failed; safe metadata recorded")
        } finally { ui.activityRule.scenario.close(); directory.deleteRecursively() }
    }
}
