package dev.passchef.mobile

/** Gate screens (D3): first run, unlock with fingerprint, unlock with password, or the app. */
enum class VaultPhase { CHECKING, NEW, LOCKED_BIOMETRIC, LOCKED_PASSWORD, UNLOCKED }
