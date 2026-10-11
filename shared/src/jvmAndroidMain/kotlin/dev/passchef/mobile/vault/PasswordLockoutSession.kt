package dev.passchef.mobile.vault

/** Cache authentication already succeeded. Existing Cookbooks incur zero writes/deletions. */
object PasswordLockoutSession {
    fun open(repository: VaultRepository, prepared: PreparedVault, firstSetup: Boolean): VaultSession {
        try {
            if (firstSetup) repository.commitPasswordOnly(prepared)
            return prepared.session
        } catch (error: Exception) { prepared.close(); throw error }
    }
}
