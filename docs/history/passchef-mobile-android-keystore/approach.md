# Approach: Android Keystore + Biometric Unlock

## Recommended path

Two Gradle modules from day one: `:app` (Android entry point, one Compose Activity/screen) and `:shared` (KMP library via the `com.android.kotlin.multiplatform.library` plugin, AGP 9.0+/Gradle 9.1+/JDK 17), per D2. `:shared` holds an `expect/actual` crypto boundary; the Android `actual` wraps the Argon2id-derived master key with a Keystore AES/GCM key using `setUserAuthenticationParameters(0, AUTH_BIOMETRIC_STRONG)` (per D5/D6, discovery.md §1) — no device-credential-inclusive key, since that would break `CryptoObject` binding. StrongBox is attempted opportunistically behind a feature check + try/catch, gated `Build.VERSION.SDK_INT >= 28` (per D4). `setInvalidatedByBiometricEnrollment(true)` stays default; `KeyPermanentlyInvalidatedException` and repeated-failure both route to the already-locked master-password re-derivation fallback (per D3). Verify runs on a real device bridged from the Windows host via `ADB_SERVER_SOCKET` (per D6, discovery.md §5) — no WSL2 emulator, no Robolectric substitute for the hardware-backed paths.

## Rejected alternatives

- Single-module legacy `com.android.library` + `androidTarget {}` KMP setup — inherits AGP-9 migration debt this greenfield project has no reason to take on (discovery.md §4).
- Keystore key with `AUTH_DEVICE_CREDENTIAL` included — breaks `CryptoObject` binding per Android's own API constraint; would silently downgrade the guarantee from "biometric" to "any screen lock."
- Mandating StrongBox — excludes API<28 devices and many real API28+ devices per Google's own "not necessary for most apps" guidance; contradicts D4's best-effort framing.
- `usbipd-win` USB passthrough as the primary adb bridge — more moving parts (driver install, per-reconnect re-bind, exclusive device lock) for no benefit here since only `adb` (not raw USB/fastboot) is needed.
- Custom NDK/Keymaster-level TEE access instead of AndroidKeystore — reinvents a capability the platform already exposes cleanly; no stated reason exists to bypass it.

## Risk map

| Component | Risk | Reason | Proof needed |
|---|---|---|---|
| AGP 9 / KMP plugin split (`:app` vs `:shared`) | MEDIUM | Brand-new plugin shape (`com.android.kotlin.multiplatform.library`) as of this research; no local precedent to fall back on | Feasibility spike: scaffold both modules, confirm a clean Gradle sync before any crypto code is written |
| BiometricPrompt + CryptoObject + Keystore wrap/unwrap round trip | HIGH | Core security guarantee of the whole feature; must be proven on real hardware, not assumed from docs | Instrumented run on the real device (per D6): wrap a test key, lock, unlock via biometric, unwrap, byte-compare |
| StrongBox opportunistic path | LOW | Explicitly best-effort (D4); failure degrades gracefully to non-StrongBox key | Try/catch path exercised on the actual test device (StrongBox-present or not — either outcome is a pass) |
| `KeyPermanentlyInvalidatedException` fallback flow | MEDIUM | Requires deliberately invalidating a key (re-enrolling a biometric) to observe — awkward to script, easy to skip | Manual induced-invalidation test on the real device: enroll a new fingerprint, confirm fallback to master-password re-derivation fires and re-wraps cleanly |
| ADB_SERVER_SOCKET bridge from Windows host | LOW | Well-documented mechanism, but new to this project's dev-loop | One-line `adb devices` proof from WSL2 before any cell claims to need it |
| Argon2id KMP implementation choice | LOW (scoped out) | Parity with the extension's `argon2_adapter.js` is owned by the separate spike PBI `p-c2ad072e`, not this feature | Not this feature's proof burden — cite the spike's result when available |

## Files and order

1. `settings.gradle.kts`, root `build.gradle.kts`, version catalog (`gradle/libs.versions.toml`) — AGP 9 / Gradle 9.1 / Kotlin pinned.
2. `shared/build.gradle.kts` + `shared/src/commonMain/.../KeystoreCrypto.kt` (expect) — the wrap/unwrap + biometric-gate contract.
3. `shared/src/androidMain/.../KeystoreCrypto.android.kt` (actual) — real Keystore/BiometricPrompt implementation.
4. `app/build.gradle.kts` + `app/src/main/.../MainActivity.kt` (Compose) — minimal screen driving unlock → wrap/unwrap demo.
5. Dev-loop: WSL2 `adb`/SDK install + `ADB_SERVER_SOCKET` bridge proof, then the first real instrumented verify run.

## Relevant learnings

- None yet in `docs/history/learnings/` (fresh repo area) — this feature's own close will seed the first Android-specific pattern entries via `bee-compounding`.

## Questions for validating

- Does `com.android.kotlin.multiplatform.library` sync cleanly in this exact environment (WSL2, no prior Android Studio/Gradle cache) on the first try, or does it need an escape hatch (`enableLegacyVariantApi`) here too? (discovery.md §4 risk)
- Does the real device actually support `FEATURE_STRONGBOX_KEYSTORE`, and does the try/catch fallback path get genuinely exercised either way?
- Is Windows-host `adb.exe` already installed (Android Studio / platform-tools), or does that also need installing before `ADB_SERVER_SOCKET` can be proven?
