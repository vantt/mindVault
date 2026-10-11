# Critical Patterns

Durable project lessons. Current specifications and verification evidence take
precedence over older environment snapshots.

## Kotlin/Gradle toolchains

A portable JDK and Gradle installation can work without sudo. Python's
`zipfile` module can extract ZIP distributions when `unzip` is unavailable;
restore executable mode on the checked-in Gradle wrapper after extraction.

Run `./gradlew` from the appropriate project directory. Repository-root
`:shared`/`:app` use JDK17 and Android SDK platform36; the independent
`mobile/sheets-client` build uses JDK21. Keep those builds and dependencies
separate. A multiplatform module's JVM tests use `jvmTest`; the standalone
Menu client has its own `test` task.

Compilation/JVM tests do not establish physical Keystore or biometric behavior.
Use the [current reading map](../../specs/reading-map.md) and
[device proof runbook](../../runbooks/android-keystore-device-proof.md)
for supported scope, observed evidence and unexecuted physical coverage.
