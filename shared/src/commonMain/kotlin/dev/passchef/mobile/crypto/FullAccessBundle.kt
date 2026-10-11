package dev.passchef.mobile.crypto

import kotlinx.serialization.Serializable

@Serializable
data class FullAccessBundle(
    val type: String,
    val version: String,
    val encryptedData: List<Int>,
    val iv: List<Int>,
    val exportSalt: List<Int>
)

@Serializable
data class SpiceValue(val base: String)
