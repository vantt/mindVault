package com.passchef.sheets

import java.io.File
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The one behavior of oauth-sheets-4's smoke-test tool that is verifiable
 * without real Google credentials (per this cell's must_haves truth,
 * D4/plan.md): running the config-loading step with no config file present
 * must print/return a clear, actionable missing-config message instead of
 * throwing an uncaught exception.
 *
 * This never touches the real default config path (`smoketest.local.properties`)
 * -- it points [loadSmokeTestConfig] at a randomly-named file inside a fresh
 * temp directory, guaranteed not to exist, so the test is hermetic and never
 * reads a real developer's local credentials file if one happens to exist.
 */
class SheetsSmokeTestConfigTest {

    @Test
    fun loadSmokeTestConfig_noConfigFilePresent_returnsMissingWithClearMessage() {
        val nonExistentConfig = File(
            System.getProperty("java.io.tmpdir"),
            "sheets-smoketest-config-${UUID.randomUUID()}.properties"
        )
        assertTrue(!nonExistentConfig.exists(), "precondition: config file must not exist")

        val result = loadSmokeTestConfig(nonExistentConfig)

        val missing = assertIs<ConfigLoadResult.Missing>(
            result,
            "loadSmokeTestConfig must return Missing, not throw, when no config file is present"
        )
        assertTrue(
            missing.message.contains("clientId") &&
                missing.message.contains("clientSecret") &&
                missing.message.contains("spreadsheetId") &&
                missing.message.contains("range"),
            "missing-config message must name the required keys so the user knows what to add: ${missing.message}"
        )
        assertTrue(
            missing.message.contains(nonExistentConfig.path),
            "missing-config message must name the path it looked for: ${missing.message}"
        )
    }
}
