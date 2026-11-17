# Current mobile candidate review —10November2026 (Asia/Saigon)

Status: **No concrete P1/P2 found within reviewed source/evidence scope**.
This is a read-only candidate review; it does not close D1 while required G1/G2/L3
runtime gates remain open. No code/test, device, Google grant or Menu changed.

Reviewed current production diff and connected paths:

- `MainActivity`, `VaultController`, `VaultCooking`, `CookbookController`,
  `DishLifetime` and `SessionClock`: secure window, background/recreation locking,
  session/key ownership, stale cooking suppression, reveal and session deadlines.
- `AndroidSheetsAuthorization`, `CookbookSources`, `SourceOwnership` and
  `RejectedTokenCacheGate`: real callback/current-request checks, invalidation,
  guarded IO commit, current401 token rejection and explicit-Connect ordering.
  Eviction failure keeps authorization closed; obsolete completion cannot release
  a newer attempt. External consent locks the Cookbook by design; old replies do
  not publish a grant into the reopened session.
- `OwnedClipboard` and `ClipboardOwnership`: process-owned deadline/generation,
  foreground legal-read boundary, nullable foreign labels, label+digest comparison,
  bounded denied-read retries and API26/27 empty-clip fallback. A denied read gives
  no overwrite authority. Exact background30s/history/process-death remains unproved.
- Menu cache/mapping and new Compose split: same private `noBackupFilesDir` cache,
  confirmed columns, literal usernames, guarded mutation, preserved last-good bytes
  and capacity failure reporting. Manual/detail/source edits clear previous Dish;
  setup inputs use transient Compose state with clearing on stop; private Spice/
  session contents are not persisted through saved-state restoration or Menu snapshots.
- Main/debug manifests and packaged-release evidence: backup/data-transfer exclusion,
  debug proof isolation and unchanged production release identity.

Meaningful existing tests cover delayed/invalidated writes, readiness loss at the
commit boundary, failed real filesystem replacement, drift/reconfirmation, source
capacity, retired Dish/session/authorization generations, reveal/total/idle expiry,
clipboard replacement/read denial and rejected-token ordering/retry/late completion.
Pure tests remain explicitly distinct from actual Android Google callbacks.

Rechecked [fresh local verification](current-verification-20261110.md):28copied XML
files yield151tests/0failures/0errors/0skips; exact test-task log lines establish fresh
execution. All214source/config paths and current APKs remain frozen; baseline64test/
fixture paths are preserved. Root127s/standalone20s results match retained logs.
Compilation/APK assembly and main/unit lint analysis/report reused existing outputs;
Android-test lint analysis executed. No stronger fresh-analysis claim is made.

Current unsigned release SHA256
`f2a86210fa254f5050bebd1086092dcd53dd87b7bb63b346c49c9ac11f57f264`
matches the [9November packaged audit](release-package-audit-20261109.md): all3DEX,
25,535class definitions, no own proof/test classes, expected permission/component
guards and actual packaged backup exclusions. That static result is reused by exact
artifact identity; no new manifest/DEX execution or signed-runtime acceptance claimed.

Read-only review receipt:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-10-current-tree/candidate-review.json`.

Unresolved: human Google cancellation/resultCode, different-Menu stale callback and
actual OS-LMK recovery; then final source/candidate reconciliation and scoped delivery.
