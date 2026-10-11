# Discovery: Android Keystore + Biometric Unlock

Depth: L2/L3 (external Android platform APIs, zero precedent in this repo; version-sensitive KMP tooling). Researched via `bee-xia` against current official docs (2026).

## 1. Wrapping the Argon2id-derived master key with a biometric-gated Keystore key

**Docs** (developer.android.com/identity/sign-in/biometric-auth): generate an AES/GCM Keystore key via `KeyGenParameterSpec.Builder(alias, PURPOSE_ENCRYPT|PURPOSE_DECRYPT).setBlockModes(BLOCK_MODE_GCM).setEncryptionPaddings(ENCRYPTION_PADDING_NONE).setUserAuthenticationRequired(true)`, gated with `setUserAuthenticationParameters(duration, authType)`.

- `duration=0` + `AUTH_BIOMETRIC_STRONG` → per-operation biometric auth (Class 3 only, no timeout reuse).
- **Hard constraint (Docs):** if `AUTH_DEVICE_CREDENTIAL` is included in the same key's auth types, `BiometricPrompt.authenticate()` cannot accept a `CryptoObject`. Since biometric must literally gate the crypto operation here (per CONTEXT.md D5/D6), the key must use `AUTH_BIOMETRIC_STRONG` only — no device-credential fallback baked into the same key.
- Binding: init a `Cipher` from the Keystore key, wrap in `BiometricPrompt.CryptoObject(cipher)`, pass to `authenticate()`. On success, `result.cryptoObject.cipher.doFinal()` wraps/unwraps the Argon2id-derived master key.

## 2. StrongBox

**Docs** (developer.android.com/privacy-and-security/keystore): `setIsStrongBoxBacked(true)` since **API 28** (confirmed — matches CONTEXT.md D4's guard). Not universal even on 28+ devices: check `PackageManager.hasSystemFeature(FEATURE_STRONGBOX_KEYSTORE)` first, still wrap generation in try/catch for `StrongBoxUnavailableException`. Docs explicitly state StrongBox is slower/more resource-constrained and "not necessary for most apps" — an explicit steer toward opt-in, not default. **Upstream**: scattered OEM-forum reports of StrongBox flakiness on some mid-tier chipsets (no specific device list found, `Inference`-level caution only).

## 3. setInvalidatedByBiometricEnrollment

**Docs**: min API **24** (confirmed — matches D4). Default `true`; irreversibly invalidates the key on new-biometric-enrollment or all-removed. Searched explicitly for a post-Android-14/15/16 replacement — **none found**. Android 15 QPR1/16 introduced *Identity Check* (biometric-only re-auth for sensitive settings, OS policy layer), which is unrelated to this KeyGenParameterSpec flag. **Inference**: keep the default `true`, catch `KeyPermanentlyInvalidatedException`, route to the already-locked master-password fallback (CONTEXT.md D3).

## 4. KMP project setup (2026)

**Docs** (kotlinlang.org AGP migration guide, JetBrains blog Jan 2026): AGP **9.0.0+** requires Gradle **9.1.0+**, JDK 17+. **Breaking change vs older setups:** `com.android.application` and `org.jetbrains.kotlin.multiplatform` can no longer share one module — the Android app entry point needs its own module; a KMP library module uses the new `com.android.kotlin.multiplatform.library` plugin (`androidLibrary { }` block replaces `androidTarget { }`). `android.enableLegacyVariantApi=true` exists as a temporary opt-out until AGP 10, relevant only to migrating legacy projects, not to us.

## 5. Bridging the Windows-host Android device's `adb` into WSL2

**Docs/Upstream**: two viable paths.
- **usbipd-win** (learn.microsoft.com/windows/wsl/connect-usb): Microsoft's own documented USB passthrough (`usbipd list` → `bind` → `attach --wsl`). Needs Windows 11 22000+ (or updated WSL on Win10), kernel ≥5.10.60.1. Device becomes WSL-exclusive while attached (Windows-side tools lose it); must re-attach after every unplug/reboot.
- **ADB_SERVER_SOCKET**: run `adb.exe` as the server on the Windows host, point the WSL2 `adb` client at it over TCP (`ADB_SERVER_SOCKET=tcp:<host>:5037`). No driver/service install, no re-bind dance, survives replug, and Windows-side tools (Android Studio) keep the device too.

## Evidence-gap note

No confirmed OEM-specific StrongBox failure list exists in public sources found — this stays an `Inference`-level caution, not a blocker; StrongBox is opportunistic/best-effort per CONTEXT.md D4 regardless. Exact Kotlin version for the AGP-9 version catalog was not pinned in the fetched docs (version-catalog-driven) — deferred to Prep as an implementation detail, not a plan-blocking unknown.
