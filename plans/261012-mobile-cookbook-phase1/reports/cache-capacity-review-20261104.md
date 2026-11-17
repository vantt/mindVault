# Cache capacity and proposal UI independent review — 2026-11-04

Result: source review Passed; reproduced JVM failure resolved. Four new Android
UI cases compiled but runtime acceptance pending. No reviewer Gradle/device calls.

## Demonstrated production correction

`CookbookSources.confirm` previously handled mapping validation and durable save
inside one INVALID_MAPPING catch. A valid twenty-first Menu exceeds the existing
20-source cache limit; the Chef was incorrectly asked to change valid columns.
The narrow inner save catch now reports CACHE_FAILED. Mapping construction,
projection and snapshot validation retain INVALID_MAPPING. Success still removes
the draft only after durable commit and current ownership; failed save retains
the exact draft, accepted RAM snapshots and disk files. Ready/busy/current gates
and request ownership remain unchanged. No auth/token/storage-format changes.

Archived pre-fix source SHA256`581952b8…ae5c2` matches candidate16's source entry.
The new JVM test first checks unchanged draft/RAM/20-file inventory/all file bytes,
then checks CACHE_FAILED. Its original failed XML records exactly expected
CACHE_FAILED versus actual INVALID_MAPPING at that final assertion. Reproduction
history remains unchanged.

Build18 log: SUCCESSFUL32s,164 tasks,29 executed/135 up-to-date; debug/test/release
and lint tasks completed. Independently inspected copied post-fix XML:11 tests,
0 failures/errors/skips, including the new capacity test and existing mapping/
source/commit-boundary coverage. Reviewer did not rerun Gradle.

## Additive UI proof design

`menu-proposal-ui-fixture.kt` uses actual password setup, confirmed baseline cache,
favorite/recent and Cook through existing authenticated UI fixture. Controller
access is read-only. Every subsequent action requires actual secure authenticated
own foreground. Exact immutable RAM, encrypted jars, Menu file hashes and real
cache reloads guard preservation; successful accepts require one additional file,
unchanged prior files, exact literal row/origin/mapping and real freshness time.

Four new methods cover tied/sparse proposals and missing/duplicate roles; unique
Recipe proposal with explicit confirmation, header roles, zero-prefixed Unicode
username values; row/column/cell limits and refused oversized edit; and19 additional
real UI commits followed by a valid twenty-first mapping refused with CACHE_FAILED.
No direct cache/controller mutation, fake auth or clock injection in Android cases.
The JVM case seeds real bounded cache files to isolate the production failure.

Cleanup closes the owned Activity, reads only its unique record, independently
attempts each alias deletion and exact canonical UUID directory removal/absence.
Primary assertion failure is retained; cleanup errors are suppressed onto it or
raised separately. Owner default Cookbook is outside this fixture's directory.

## Scope and evidence

Against frozen source186, the sole changed existing path is
`app/src/main/kotlin/dev/passchef/mobile/cookbook/CookbookSources.kt`; the JVM test
and two Android files are additive. Existing test assertions remain unchanged.
External evidence: `2026-11-04-cache-capacity/reviewer-capacity-source18.json`,
`reproduction17.json`, immutable `unit-before17.xml` and copied `unit-after18` XML.

No remaining source-review finding. Existing Google host binds source186; this
production correction requires new source/APK freeze and explicit host rebinding.

## Candidate18 source189 and initial UI host review

Independent artifact audit Passed: all189 frozen current paths and exact ZIP
entry names/bytes match; all61 original Android test files remain byte-exact.
Three archived APKs match candidate18 manifest. Recorded actual installed app/test
digests in `installed-identity18.json` match those candidate bytes on owned
PassChefLegacy26/emulator-5582; this is root's saved device evidence, without new
reviewer device observations. Debug`c4f24726…ce454`,test`26b09a7e…5a5f5`,release
`87fc6d3e…45471`. Unlike prior proof-only changes, this correction affects release.

`run-proposal-ui.py` compiles and matches `ui-host-freeze18.json`. Four selected
methods and expected ordered phases/final source counts/statuses match source.
The runner requires fixed owned API26/AVD, real installed byte identity, inactive
native and initially empty namespace; each native selector requires exact JUnit
pass, one canonical fixture UUID/positive PID and final empty namespace.400s
timeout/unconfirmed inactivity stays Unknown; no automatic retry/UUID cleanup.

Two host completion items before runtime: verify source189 and archived APK bytes
before and after cases (currently only installed bytes are checked); reject a
trailing unfinished Proposal metadata bundle in final phase parsing. Root notified.
Evidence: external `reviewer-capacity-candidate18.json`. No reviewer device calls.

## Corrected UI host19 — Passed

Both completion items resolved. `provenance()` verifies189 canonical relative
current source paths, exact189 ZIP names/bytes and all3 archived APK bytes before
device preflight and after every case, before writing accepted evidence. Actual
pure invocation passed. Final phase parsing rejects unfinished Proposal metadata
and malformed Proposal prefix lines.

Reviewer extracted only the two pure function definitions rather than executing
the device runner. Five probes passed: valid complete phase accepted; orphan tail,
malformed field, wrong status code and duplicate field rejected. Current host19
freeze exactly matches runner SHA256
`8339bef0c86c4cf9165476f0c0f731ebebd52659fa9aca5e75f9e90872e58c06`;
strict classifier unchanged. Original host91-line version is byte-exact inside
`ui-host-before19.zip` against retained freeze18. Candidate18/source189 unchanged.
Evidence: external `reviewer-capacity-host19.json`. No reviewer device calls.

No remaining preparation/source/host-review blocker. Runtime is still required.

## UI01 case03 failure — contract diagnosis

UI01 case01 Passed26.155s and case02 Passed23.224s per saved native transcripts.
Case03 Failed27.455s with exact JUnit failure expected CONFIRM, actual OFFLINE at
the new test line66/fixture proposal line64. Three actual INVALID_TABLE checkpoints
were emitted after preservation checks. Line65's accepted-field equality passed;
the failure precedes the next saved-data preservation call, so raw alone does not
establish that final post-oversize preservation check. Case04 did not run.

The new line66 assertion requires an existing unconfirmed MappingDraft/CONFIRM to
survive text replacement. Phase03 explicitly requires accepted field text and
saved data unchanged for the oversized edit, without that draft-retention rule.
Spec says edits invalidate prior work; `SourceScreen.changed()` invalidates source
drafts/pending work conservatively. Existing source guards over-limit text updates.
This evidence identifies an unmarked new-test overconstraint, without proving a
production cache-loss/input-limit defect. Exact callback sequence causing OFFLINE
is not inferred from the raw transcript alone.

Recommended bounded test correction: immediately retain accepted-field equality
and all baseline RAM/disk/cache checks, then click actual Preview columns again
on the same unchanged Menu ID/title/table. Require the same origin's valid draft,
expected proposal and no automatic cache change. Do not generate a new origin or
replace the input to bypass the boundary. Keep the failed candidate/transcript
and all earlier assertions; capacity CACHE_FAILED expectation stays mandatory.
No production change warranted by this demonstrated failure alone.

## Candidate20 and bounded retake — preparation Passed

The new test correction follows the documented contract: field equality remains,
all baseline RAM/files are checked immediately after oversized input, then
`repreview()` only clicks actual Preview columns and waits. The same captured
origin's proposal is checked afterward. No Menu ID/title/table replacement and no
controller mutation bypasses the boundary. Capacity CACHE_FAILED remains required.

Source189/ZIP exact names and bytes/current source all match frozen20. Relative
to18, only the two new proposal test/helper files changed by this bounded edit.
Production debug`c4f24726…ce454` and release`87fc6d3e…45471` are byte-identical18;
test APK`35a08698…aca14` matches candidate20's archived bytes. Three APKs match.

New `run-proposal-ui-retake.py` selects only the previously failed method03 and
not-yet-run method04, with separate ui02 outputs and candidate20/source20 binding.
Every guard function is AST-identical to reviewed host19 after only those explicit
artifact filename substitutions. Canonical source189/archive3APK provenance,
installed identity, phase/UUID/PID/count/status checks, inactive/empty namespace
and timeout Unknown behavior remain intact. Host20 freeze matches current bytes.
Failed ui01 history remains retained. No reviewer device/Gradle calls.

Evidence: external `reviewer-capacity-source-host20.json`. No remaining preparation
review blocker; this is not acceptance of the two pending native cases.

## Actual four selected UI acceptances — Passed

Independent raw reclassification confirms exact selected JUnit`[1,0]/[-1]/OK1` for
all4 accepted methods. Raw SHA/checkpoints match saved accepted records; each has
its own UUID/PID, completed phase order, final expected source count/status and
empty owned namespace after actual native inactivity. Source189/ZIP/APK provenance
passes before/after each accepted case. Root's initial failed03 audit also confirms
inactive/empty after failure; original ui01 overall remains Failed, without rewrite.

| Accepted case | Candidate | Native / host seconds | PID |
|---|---|---|---|
| Tied/sparse, missing/duplicate roles |18|26.155 /27.513|7574|
| Unique proposal, explicit confirm, literal cells |18|23.224 /24.454|7707|
| Table limits, oversized edit and same-origin repreview |20|29.497 /30.730|8527|
| Twenty real UI commits, valid twenty-first refused |20|97.776 /99.013|8645|

The last method emitted SourceCount20/SYNCED after actual commits and
SourceCount20/CACHE_FAILED after the valid refused mapping. Source-backed native
assertions verify immutable accepted RAM, all prior encrypted jars/cache bytes,
real disk reloads and absent refused origin. The corrected oversized case checks
field/data before same-origin preview; it does not claim stale draft retention.
Independent copied JVM XML remains11 tests/0 failures/errors/skips, including the
demonstrated pre-fix coordinator regression resolved by the narrow production catch.

Retake ui02 overall Passed with nativeMayStillLive=false. Candidate18 and20 use the
same production app/release bytes; only the reviewed test correction differs.
This accepts selected real offline proposal/mapping/limits/capacity UI on API26
password-fallback emulator, plus accurate storage failure classification. It does
not certify physical layout, Google, spoken TalkBack or whole-Mobile completion.
Evidence: external `reviewer-capacity-runtime20.json`. No reviewer device calls.

Unresolved: final release of owned emulator when remaining root-owned work ends;
separate broader physical/Google/plan acceptance gates remain outside this scope.
