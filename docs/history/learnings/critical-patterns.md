# Critical Patterns

Mandatory pre-planning / pre-execution context for this repository.
bee-compounding appends hard-won patterns here; keep it short and current.

## bee-write-guard may falsely deny writes in a linked worktree (uncommitted fix, check it synced)

If a `bee-write-guard` denial's stated phase/feature does NOT match what
`node .bee/bin/bee.mjs status --json` shows for the CURRENT worktree, do not
assume the local gates are actually unapproved. `resolveWriteRecord` in
`.bee/bin/lib/guards.mjs` has a fixed bug where a session with no lane binding
(the normal case inside a `bee worktree new`-granted linked worktree) used to
fall back to reading MAIN's own unrelated default `state.json` instead of the
worktree's own already-correct state. The fix lives in
`/home/vantt/projects/passMan/.bee/bin/lib/guards.mjs` (uncommitted as of
2026-07-28) and must be synced into each linked worktree's own local
`.bee/bin` copy separately — check it's actually present before re-diagnosing
from scratch. Full root cause: `docs/history/learnings/20260728-passchef-mobile-oauth-sheets.md`.

## Kotlin/Gradle toolchain in this sandbox

No system Java/Gradle/Kotlin, no passwordless sudo, no `unzip`. A portable JDK
(Adoptium API) + Gradle (services.gradle.org) install works fine without root
— extract `.zip` with `python3 -m zipfile -e` (not `unzip`), `chmod +x`
afterward. A committed `./gradlew` wrapper MUST be invoked with
`cd <module-dir> && ./gradlew ...` (it resolves its project dir from cwd, not
its own script location) — never a relative path from repo root. A jvm-only
KMP module's real test task is `jvmTest`/`allTests`, not a plain `test` task.
