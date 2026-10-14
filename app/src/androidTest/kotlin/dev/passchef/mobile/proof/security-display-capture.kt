package dev.passchef.mobile.proof

import android.app.UiAutomation
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.test.platform.app.InstrumentationRegistry

/** Read-only display capture; neither route enables secure layers or changes window policy. */
fun securityDisplayCapture(prefix: String): Bitmap? {
    val instrumentation = InstrumentationRegistry.getInstrumentation()
    val transport = InstrumentationRegistry.getArguments().getString("passchefSecureCaptureTransport") ?: "automation"
    require(transport == "automation" || transport == "shell") { "Unsupported proof capture transport" }
    val automation = instrumentation.getUiAutomation(UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES)
    instrumentation.sendStatus(2, Bundle().apply {
        putBoolean(prefix + "_captureViaShell", transport == "shell")
        putBoolean(prefix + "_captureViaUiAutomation", transport == "automation")
    })
    val bitmap = if (transport == "shell") {
        // Fixed Android command, streamed in memory. No screenshot file, secure-layer option or grant.
        ParcelFileDescriptor.AutoCloseInputStream(automation.executeShellCommand("screencap -p")).use { stream ->
            BitmapFactory.decodeStream(stream)
        }
    } else automation.takeScreenshot()
    instrumentation.sendStatus(2, Bundle().apply {
        putBoolean(prefix + "_captureBitmapAvailable", bitmap != null)
        bitmap?.let { putInt(prefix + "_captureBitmapWidth", it.width); putInt(prefix + "_captureBitmapHeight", it.height) }
    })
    return bitmap
}
