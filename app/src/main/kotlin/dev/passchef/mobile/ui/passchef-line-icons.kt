package dev.passchef.mobile.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/** In-house 24dp line icons (no icon dependency). Tinted by the caller through Icon(tint). */
object PassChefIcons {
    private fun line(name: String, vararg paths: String): ImageVector =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f).apply {
            for (data in paths) addPath(addPathNodes(data), stroke = SolidColor(Color.Black), strokeLineWidth = 1.6f,
                strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round)
        }.build()

    val Lock = line("lock", "M6 11h12v9H6z", "M8.5 11V8a3.5 3.5 0 0 1 7 0v3", "M12 15v2")
    val Key = line("key", "M14.5 9.5a4 4 0 1 0 0.01 0", "M11.6 12.4L4 20", "M6 18l2 2", "M8 16l2 2")
    val Keypad = line("keypad", "M5 5h3v3H5z", "M10.5 5h3v3h-3z", "M16 5h3v3h-3z", "M5 10.5h3v3H5z",
        "M10.5 10.5h3v3h-3z", "M16 10.5h3v3h-3z", "M10.5 16h3v3h-3z")
    val Gear = line("gear", "M12 9a3 3 0 1 0 0.01 0",
        "M12 3v2.5M12 18.5V21M3 12h2.5M18.5 12H21M5.6 5.6l1.8 1.8M16.6 16.6l1.8 1.8M5.6 18.4l1.8-1.8M16.6 7.4l1.8-1.8")
    val Copy = line("copy", "M9 9h11v11H9z", "M5 15H4V4h11v1")
    val Eye = line("eye", "M2.5 12s3.5-6.5 9.5-6.5S21.5 12 21.5 12s-3.5 6.5-9.5 6.5S2.5 12 2.5 12z", "M12 9.5a2.5 2.5 0 1 0 0.01 0")
    val Star = line("star", "M12 3.5l2.6 5.4 5.9 0.8-4.3 4.1 1 5.8L12 16.8l-5.2 2.8 1-5.8-4.3-4.1 5.9-0.8z")
    val Refresh = line("refresh", "M20 12a8 8 0 1 1-2.4-5.7", "M20 4v4.5h-4.5")
    val Search = line("search", "M10.5 4a6.5 6.5 0 1 0 0.01 0", "M15.5 15.5L20 20")
    val Back = line("back", "M15 5l-7 7 7 7")
    val Chevron = line("chevron", "M9 5l7 7-7 7")
    val Down = line("down", "M5 9l7 7 7-7")
    val Plus = line("plus", "M12 5v14M5 12h14")
    val Drive = line("drive", "M8.5 4h7l5.5 9.5-3.5 6H6.5L3 13.5z", "M8.5 4L14 13.5H21", "M15.5 4L6.5 19.5")
    val Link = line("link", "M10 14a4 4 0 0 0 5.7 0l3-3a4 4 0 0 0-5.7-5.7l-1 1", "M14 10a4 4 0 0 0-5.7 0l-3 3a4 4 0 0 0 5.7 5.7l1-1")
    val Share = line("share", "M12 15V4", "M8 8l4-4 4 4", "M5 13v7h14v-7")
    val Table = line("table", "M4 5h16v14H4z", "M4 10h16M4 14.5h16M10 5v14")
    val Fingerprint = line("fingerprint", "M7 18c1.5-2 2-4 2-6a3 3 0 0 1 6 0c0 3-0.7 5.5-2 8",
        "M5 14.5c0.6-1 1-2 1-3.5a6 6 0 0 1 12 0c0 2-0.2 3.6-0.6 5", "M12 12c0 3-1 5.5-2.5 7.5")
    val Warning = line("warning", "M12 4l9 16H3z", "M12 10v4", "M12 17v0.5")
    val External = line("external", "M14 4h6v6", "M20 4l-9 9", "M18 14v6H4V6h6")
}
