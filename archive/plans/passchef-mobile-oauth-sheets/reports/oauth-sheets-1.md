# oauth-sheets-1 — Toolchain bring-up + KMP module scaffold

**Status:** [DONE]

**Outcome:** Bootstrapped a user-writable JDK 21 (Temurin) + Gradle 8.10 toolchain at fixed `.toolchain/jdk` and `.toolchain/gradle` paths (copied from the already-verified spike, no re-download), scaffolded a standalone Gradle KMP module at `mobile/sheets-client` (commonMain/jvmMain/commonTest/jvmTest source sets, `jvm()` target, `jvmToolchain(21)`), generated a real Gradle wrapper (8.10) committed at `mobile/sheets-client/gradlew`, and documented the exact recipe + versions in `docs/mobile-kmp-toolchain-setup.md`. `./gradlew tasks` and `./gradlew build` both succeed.

**Note on the two prior [BLOCKED] attempts in this same report file:** those were caused by a session-routing defect in `.bee/bin/lib/guards.mjs`'s `resolveWriteRecord`, which ignored this worktree's correctly-resolved state and fell back to the main checkout's unrelated state for gate checks. That defect has since been fixed and synced into this worktree; a direct test write succeeded before this cell's real work began, confirming the fix. This run proceeded normally with no further routing issues.

## Deviation

Cell's `verify` field was `JAVA_HOME="$(pwd)/.toolchain/jdk" PATH="$JAVA_HOME/bin:$PATH" ./mobile/sheets-client/gradlew tasks`, invoked from the repo root. This literal invocation fails (`Directory '...' does not contain a Gradle build`) because every standard Gradle wrapper script resolves the project directory from the process's current working directory, not the script's own location — confirmed by reading the generated `gradlew` (APP_HOME is computed in a subshell, used only for the wrapper-jar classpath; there is no `cd`). Auto-fixed per the worker-details blocking-issue rule: ran the corrected equivalent (`cd mobile/sheets-client && ... ./gradlew tasks`), same JDK, same intent — `BUILD SUCCESSFUL`. Documented the gotcha in `docs/mobile-kmp-toolchain-setup.md` so later cells' verify commands don't repeat it.

## Files touched

`.gitignore`, `mobile/sheets-client/settings.gradle.kts`, `mobile/sheets-client/build.gradle.kts`, `mobile/sheets-client/gradle.properties`, `mobile/sheets-client/gradle/wrapper/gradle-wrapper.jar`, `mobile/sheets-client/gradle/wrapper/gradle-wrapper.properties`, `mobile/sheets-client/gradlew`, `mobile/sheets-client/gradlew.bat`, `mobile/sheets-client/src/commonMain/kotlin/com/passchef/sheets/SheetsClientPlaceholder.kt`, `mobile/sheets-client/src/jvmMain/kotlin/com/passchef/sheets/JvmPlaceholder.kt`, `docs/mobile-kmp-toolchain-setup.md`.

Commit: `f7e0a54` — "feat(mobile): bring up KMP toolchain and scaffold sheets-client module" (cell: oauth-sheets-1).

## Reservations

Reserved then released (10 paths under `w-toolchain`/`oauth-sheets-1`); nothing left active.

## Full trace

`.bee/cells/oauth-sheets-1.json`
