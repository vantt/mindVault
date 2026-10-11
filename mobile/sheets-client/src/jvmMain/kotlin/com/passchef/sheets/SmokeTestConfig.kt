package com.passchef.sheets

import java.io.File
import java.io.IOException
import java.util.Properties

/** Local configuration only; never print its values. */
data class SmokeTestConfig(
    val clientId: String,
    val clientSecret: String,
    val spreadsheetId: String,
    val range: String
)

sealed class ConfigLoadResult {
    data class Loaded(val config: SmokeTestConfig) : ConfigLoadResult()
    data class Missing(val message: String) : ConfigLoadResult()
}

private val requiredKeys = listOf("clientId", "clientSecret", "spreadsheetId", "range")

fun loadSmokeTestConfig(configFile: File = File("smoketest.local.properties")): ConfigLoadResult {
    if (!configFile.exists()) {
        return ConfigLoadResult.Missing(
            "No smoke-test config found at '${configFile.path}'. Create the gitignored file " +
                "with keys: ${requiredKeys.joinToString()}. See " +
                "docs/runbooks/google-oauth-sheets-setup.md Step 7."
        )
    }
    val props = Properties()
    try {
        configFile.reader(Charsets.UTF_8).use { props.load(it) }
    } catch (_: IOException) {
        return ConfigLoadResult.Missing("Cannot read local connection config. Check the file and permissions.")
    } catch (_: IllegalArgumentException) {
        return ConfigLoadResult.Missing("Invalid local connection config. Check the properties format.")
    } catch (_: SecurityException) {
        return ConfigLoadResult.Missing("Cannot read local connection config. Check the file permissions.")
    }
    val values = requiredKeys.map { props.getProperty(it).orEmpty() }
    val missing = requiredKeys.filterIndexed { index, _ -> values[index].isBlank() }
    if (missing.isNotEmpty()) {
        return ConfigLoadResult.Missing("Local connection config is missing required keys: ${missing.joinToString()}.")
    }
    return ConfigLoadResult.Loaded(SmokeTestConfig(values[0], values[1], values[2], values[3]))
}
