# Mobile KMP Toolchain Setup

Track: `passchef-mobile-oauth-sheets` (D2 — Sheets API client scaffolded as a
new, minimal KMP/Gradle module). This doc records the exact toolchain versions
and install recipe used to bring up Kotlin Multiplatform (KMP) build tooling
in a sandbox that had zero Kotlin/Gradle/Java tooling installed and no
passwordless sudo — so a future session or CI can reproduce it without
re-deriving the steps.

## Versions

| Component | Version | Source |
|---|---|---|
| JDK | Temurin 21.0.12+8 (LTS) | Adoptium API (`api.adoptium.net`) |
| Gradle | 8.10 | `services.gradle.org` official binary distribution |
| Kotlin (Gradle plugin) | 1.9.24 | `org.jetbrains.kotlin.multiplatform` plugin, matches Gradle 8.10's bundled embedded Kotlin for DSL compatibility |
| Kotlin Gradle DSL (bundled) | 1.9.24 | Reported by `gradle -v` |

## Fixed install paths (repo-root-relative)

Both are installed **user-writable, no root needed**, at fixed paths so later
commands never have to hardcode a version:

```
.toolchain/jdk/       <- JDK 21 (Temurin), extracted tarball, renamed (version suffix stripped)
.toolchain/gradle/     <- Gradle 8.10, extracted zip, renamed (version suffix stripped)
```

Both directories are gitignored (`.toolchain/`) — they hold ~330MB of
downloaded binaries that must never be committed.

## Install recipe (reproducible from scratch)

1. **Download JDK 21 (Temurin):**
   ```sh
   curl -sL "https://api.adoptium.net/v3/assets/latest/21/hotspot?os=linux&architecture=x64&image_type=jdk" \
     | <parse JSON for the .tar.gz binary link, or follow the API's redirect directly>
   curl -sL -o jdk21.tar.gz "<resolved binary link>"
   ```
2. **Extract and pin the path:**
   ```sh
   tar xzf jdk21.tar.gz
   mv jdk-21.0.12+8 .toolchain/jdk   # fixed name, no version suffix
   ```
3. **Download Gradle 8.10:**
   ```sh
   curl -sL -o gradle.zip https://services.gradle.org/distributions/gradle-8.10-bin.zip
   ```
4. **Extract (no `unzip` binary in this sandbox — use Python's zipfile module):**
   ```sh
   python3 -m zipfile -e gradle.zip .
   mv gradle-8.10 .toolchain/gradle   # fixed name, no version suffix
   ```
5. **Fix executable bits (archives do not preserve them):**
   ```sh
   chmod +x .toolchain/jdk/bin/* .toolchain/gradle/bin/gradle
   ```
6. **Verify the toolchain:**
   ```sh
   JAVA_HOME="$(pwd)/.toolchain/jdk" PATH="$JAVA_HOME/bin:$PATH" java -version
   .toolchain/gradle/bin/gradle -v
   ```
7. **Generate the real Gradle wrapper for the module** (run once, from repo
   root, using the bootstrap Gradle above — after this, `.toolchain/gradle`
   is no longer needed; only `.toolchain/jdk` is, since the committed
   `gradlew` still needs a JDK on the machine running it):
   ```sh
   JAVA_HOME="$(pwd)/.toolchain/jdk" PATH="$JAVA_HOME/bin:$PATH" \
     .toolchain/gradle/bin/gradle -p mobile/sheets-client wrapper --gradle-version 8.10
   ```

## Running the module's own build

The generated `mobile/sheets-client/gradlew` is a standard Gradle wrapper
script. Like every Gradle wrapper, **it resolves the project directory from
the process's current working directory, not from the script's own
location** — so it must be invoked with `mobile/sheets-client` as the working
directory (a `cd` first, or `-p mobile/sheets-client` from elsewhere):

```sh
JAVA_HOME="$(pwd)/.toolchain/jdk"
PATH="$JAVA_HOME/bin:$PATH"
cd mobile/sheets-client && ./gradlew tasks    # or: build, check
```

Running `./mobile/sheets-client/gradlew tasks` directly from the repo root
(without `cd`-ing into the module dir first) fails with *"Directory ...
does not contain a Gradle build"* — the wrapper script never changes
directory on your behalf.

## Module scope (this track)

`mobile/sheets-client/` is a standalone Gradle Kotlin Multiplatform module
(own `settings.gradle.kts`, not part of a root multi-project build) with:

- `commonMain` / `commonTest` — shared Kotlin source sets.
- `jvmMain` / `jvmTest` — JVM target (`jvm()` in the `kotlin {}` block).
- `jvmToolchain(21)` pinned in `build.gradle.kts`.

The initial scaffold has been completed with the Sheets v4 REST client,
OAuth Console runbook, and JVM connection-check tool. The 2026-10-11 recovery
adds state/PKCE protection, UTF-8 ranges/configuration, literal recipe writes,
and 22 credential-free tests. See [worktree recovery](worktree-recovery.md)
and the [OAuth setup runbook](runbooks/google-oauth-sheets-setup.md).
It remains independent of app UI: no Android target/app module is added here
(`p-c46b217c` is separate). Real Google consent and a live Cookbook read remain
pending; the local build does not certify them.
