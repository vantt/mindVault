package dev.passchef.mobile.vault

import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/** A crash leaves either the prior complete record or its new complete replacement. */
class AtomicRecordStore(private val file: File) : RecordStore {
    @Synchronized override fun read(): ByteArray? {
        if (!file.exists()) return null
        require(file.length() in 1..200000)
        return file.inputStream().use { it.readBytes().also { bytes -> require(bytes.size <= 200000) } }
    }
    @Synchronized override fun write(bytes: ByteArray) {
        require(bytes.size in 1..200000)
        check(file.parentFile!!.isDirectory || file.parentFile!!.mkdirs())
        val temporary = File(file.parentFile, file.name + ".pending")
        try {
            FileOutputStream(temporary).use { output -> output.write(bytes); output.fd.sync() }
            // Never fall back to delete-then-rename: failed atomic replacement preserves the old record.
            Files.move(temporary.toPath(), file.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
        } finally { temporary.delete() }
    }
}
