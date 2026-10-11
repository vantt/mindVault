package dev.passchef.mobile.proof

import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.passchef.mobile.vault.AtomicRecordStore
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.security.MessageDigest
import java.util.UUID

/** Tests the actual Android filesystem; owns only a unique synthetic proof file. */
@RunWith(AndroidJUnit4::class)
class AtomicRecordStorePhysicalTest {
    @Test fun replacesExistingRecordAtomicallyAndReopensExactBytes() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val file = File(instrumentation.targetContext.noBackupFilesDir, "atomic-replace-proof-${UUID.randomUUID()}.json")
        val old = ByteArray(4096) { (it % 251).toByte() }
        val replacement = ByteArray(6144) { ((it + 19) % 239).toByte() }
        var phase = "first-create"
        try {
            AtomicRecordStore(file).write(old)
            assertTrue("First record bytes must match", MessageDigest.isEqual(old, AtomicRecordStore(file).read()))
            phase = "existing-record-replacement"
            AtomicRecordStore(file).write(replacement)
            phase = "new-instance-reopen"
            assertTrue("Replacement bytes must match", MessageDigest.isEqual(replacement, AtomicRecordStore(file).read()))
            assertTrue("Completed transaction must remove temporary file", !File(file.parentFile, file.name + ".pending").exists())
            instrumentation.sendStatus(0, Bundle().apply {
                putString("passchefAtomicReplacement", "first-create=true; existing-replace=true; reopened-exact=true")
            })
        } catch (error: Exception) {
            val causes = generateSequence(error as Throwable?) { it.cause }.take(5).toList()
            instrumentation.sendStatus(0, Bundle().apply {
                putString("passchefAtomicPhase", phase)
                putString("passchefExceptionClasses", causes.joinToString(" -> ") { it.javaClass.name })
                putString("passchefSanitizedStack", causes.flatMap { it.stackTrace.take(12).toList() }
                    .joinToString("\n") { "${it.className}.${it.methodName}:${it.lineNumber}" })
                putString("passchefOldRecordPreserved", runCatching {
                    MessageDigest.isEqual(old, AtomicRecordStore(file).read())
                }.getOrDefault(false).toString())
            })
            // Do not chain the original exception: runner would print its unsanitized message.
            throw AssertionError("Android atomic record operation failed; sanitized phase/class/stack recorded")
        } finally {
            old.fill(0); replacement.fill(0)
            File(file.parentFile, file.name + ".pending").delete(); file.delete()
        }
    }
}
