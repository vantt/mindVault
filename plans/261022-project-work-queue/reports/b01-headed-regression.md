# B01 — selected FA01/UI01 headed regression preparation

Date:2026-10-26, Asia/Saigon. Status:**Prepared — independent preparation review Passed**;
[review](b01-preparation-independent-review.md). Dated execution followup:
[Run01 selected3cases Passed](b01-run-01.md), [independent raw review](b01-run-01-independent-review.md).
Final-candidate/full-suite B01 remains Pending; A21 isolation Unverified. Root used
the explicit owner desktop handoff and released it after cleanup. Preparation
checks/immutable freeze below retain their original pre-runtime NotRun state.

## TODO

- [x] Add focused genuine Options export/import→Builder Cook and Settings EN/VI cases.
- [x] Preserve all44existing tests/Golden and unchanged fixture/config/runtime.
- [x] Collect/transform only with Playwright `--list`; no browser execution.
- [x] Freeze current74-file source/ZIP/inventory/SHA and harness/new-test identities.
- [x] Record preflight/resource/checkpoint/timeout/end/cleanup protocol and limits.
- [x] Independent preparation review Passed; plan/dispatch updated before runtime.
- [x] Dated owner-authorized Run01:3selected browser cases Passed;raw independent review Passed;desktop released. Final full-suite scope remains Pending.

## Exact authored scope —3files

- New [Full Access regressions](../../../chrome-extension/test/e2e/full-access-import-regression.spec.ts):90lines,2cases.
- New [Settings regression](../../../chrome-extension/test/e2e/settings-defaults-deferral.spec.ts):56lines,1case.
- This report. No helper/config/fixture/runtime/manifest/locale/old-test change.

Reuses immutable [headed fixture](../../../chrome-extension/test/e2e/fixtures.ts)
and [current Cookbook setup](../../../chrome-extension/test/e2e/current-cookbook-fixture.ts).
The latter uses genuine AES solely to prepare its existing async readiness marker,
then actual profile-editor Save. New export/import/cooking assertions do not seed
an imported record, stub writer/core/crypto, call a direct cooking oracle or use
clipboard Copy. Actual popup HTML runs in a browser tab for Builder; this is not
toolbar popover/native shortcut acceptance. Test data is public synthetic only.

## Prepared runtime cases — original freeze states all NotRun

| Case | Intended actual assertions | Budget |
|---|---|---|
| FA01 with mapped Menu |Actual Options edit/save five jars including Unicode→Full Access export JSON/sharing password→paste/decrypt/import under unused own-profile name→reload→imported profile editor retains exact five values. Original ciphertext/salt/default unchanged; map routes to imported own profile. Actual popup Builder enforces that mapped profile, cooks literal `Basic*r4nd0m` and shows valid Tagged Recipe. |120s |
| FA01 without Menu |Same actual flow with no Menu metadata; original mapping/default retained. Builder explicitly selects imported own profile and cooks exact literal Dish with untagged `r4nd0m#1`. Own encrypted record exists, no legacy shared record. |120s |
| UI01 EN→VI→EN |Seed only legacy global preference metadata with opposite booleans/unknown field. Actual Settings and full language reloads show no three controls/card, truthful localized description, active language and real quota. Master panel expands/focuses/cancels/resets input; dismiss actual Reset confirmation. Exact Sync snapshot retained except chosen language; saved legacy object/session key retained and no page errors. |90s |

Five Spice values are entered through the actual editor. Menu ID is a synthetic
valid-format string used as metadata, never navigated to Google. Literal expected
Dish is independent of the production cooking implementation. Existing FA01 local
failure/repaired-handler evidence remains in its [separate report](a23-full-access-import-repair.md);
these new browser cases have **not** run against old or repaired source.

## Immutable preparation freeze

Ignored [`build/b01-selected/`](../../../chrome-extension/build/b01-selected/)
contains copied ZIP/inventory/SHA, all74exact shipped source bytes,50harness/input
files (46test files including2new, config/package/lock/contract), original44-test
hash snapshot and collection log. Existing browser fixture still loads working
`src`; the frozen source copy is evidence, not a modified runner/extracted-ZIP load.

[Freeze record](../../../chrome-extension/build/b01-selected/record.json) SHA256:
`b6e0669241095f2947360b8c98dc563ae74930f499ad9ad90d468c67cf865d94`.
Source baseline:HEAD039c76d plus preserved WIP/FA01/UI01; development manifest2.3.0,
tooling package1.0.0. C02 version/channel and stable A13 candidate remain Pending.
The frozen development [ZIP](../../../chrome-extension/build/b01-selected/artifact/passchef-extension-2.3.0-development.zip)
SHA256 is `8e921455f31f4ba6d749242c94d3b9f1b17f964656fdca2fc1d7dc449d92f7b7`,
74entries/1,102,447bytes. Every copied/working shipped source byte matched ZIP and
per-file inventory at freeze. No package was rebuilt or production source changed.

New spec SHA256:

```text
9f3849520e47d28f3f3f13ae5c3b424537eef564d98bf25aa8fd1b9d928cd812  full-access-import-regression.spec.ts
f6391f796517197da3e3dce05a8de59dceb100a22a8d3eec7ea85c3b4039ba2c  settings-defaults-deferral.spec.ts
```

## Local checks actually executed

Node24.18.0/pnpm10.24.0/Playwright1.57.0. From `chrome-extension`:

```sh
pnpm exec playwright test test/e2e/full-access-import-regression.spec.ts test/e2e/settings-defaults-deferral.spec.ts --list --reporter=line --workers=1 --retries=0 --global-timeout=420000
```

Exit0:exact3tests in2files collected/transformed, no fixture setup/browser execution.
[Retained collection output](../../../chrome-extension/build/b01-selected/collect-final.log).
Independent preparation reviewer also collected the same3cases; no runtime verdict.
Playwright collection transpiles TypeScript; no separate semantic `tsc` or browser
assertion success is claimed. Static checks resolved34literal ID selectors against
actual source/template IDs, checked dynamic group/profile selectors against source,
and confirmed both files<200lines. These checks do not prove timing/layout/actionability.

Read-only Python validated ZIP CRC/exact74entry inventory/current-source bytes,
all74source and50harness hashes and3NotRun states. Root's pre-task44-test SHA
snapshot passes; tracked WIP diff is byte-exact to the pre-B01 snapshot. Fixture,
config,Golden and runtime preservation therefore include existing dirty FA01/UI01
changes without confusing them with a clean HEAD. `git diff --check` exit0.
Prior151unit/49package results are retained evidence; no unchanged suite replay.
The whole-WIP comparison was at preparation handoff, before subsequent manager-only
plan/report edits. Future preflight binds the frozen runtime/harness hashes;manager
documentation updates do not relabel or change those frozen bytes.

## Prepared execution gates and exact command — historical pre-run protocol

Before asking owner for the desktop slot, finish independent preparation review,
resolve any findings and freeze any changed specs into a **new** numbered record;
never overwrite this freeze. Root then obtains the owner handoff or separately
demonstrated isolation (A21 remains Unverified). Recheck source baseline/hash,
ZIP/inventory/SHA and all74working source/50harness hashes against record; compare
the original44-test snapshot. If any byte differs, stop and review/refreeze, not
silently run a different patch. Root records exact browser runtime version after
authorized launch; cached executable presence/old Chromium143 is not fresh proof.

Use a unique run number and exclusive new output paths; verify they do not already
exist. No Google login/account or provider permission is needed for these local
synthetic cases; unchanged font CSS may request external font data. Default headed
browser still uses shared display/focus, so collection authorization is not a slot.
Do not alter fixture/config/headless/display to bypass the gate.

The following was proposed from `chrome-extension` after the gate. Root's dated
Run01 now owns actual execution/evidence; any future rerun needs a new run number,
applicable authorization and fresh preflight rather than reusing these output paths:

```sh
export PATH="/home/vantt/.nvm/versions/node/v24.18.0/bin:$PATH"
PLAYWRIGHT_JSON_OUTPUT_NAME=build/b01-selected/run-01-report.json pnpm exec playwright test test/e2e/full-access-import-regression.spec.ts test/e2e/settings-defaults-deferral.spec.ts --reporter=line,json --output=build/b01-selected/run-01-results --workers=1 --retries=0 --global-timeout=420000 > build/b01-selected/run-01.log 2>&1
```

Local installed reporter source confirms JSON output-name support. Config remains
immutable; per-case budgets total330s, global420s maximum (7minutes) plus explicit
root preflight/cleanup. No retries, broad full suite or unrelated native/clipboard
cases. Owner checkpoint is **before launch**:handoff agreed, exact selected run,
synthetic-only private profile, bounded slot and cleanup. No silent mid-run waiting
for owner input; unexpected prompts/desktop conflict stop classification safely.

Runtime success requires all3selected JSON results expected,0failures/skips, exit0,
complete terminal/log/artifact identity and cleanup evidence. Failure/timeout remains
Failed/Unknown as observed; no message/core oracle fallback or selector weakening.
Global host termination alone does not prove browser child exit. Fixture logs the
unique `passchef-e2e-` profile path; root records only that owned browser PID/profile,
checks context/child termination and exact profile-directory absence afterwards.
Existing fixture cleanup warnings must not be ignored as successful cleanup. Remove
only owned synthetic profile/download/test outputs as appropriate, retain raw evidence;
never kill shared Chrome, clear owner data/clipboard or restore by reading clipboard.

Selected success would verify source-patch B01 cases only. It would not complete
the final full suite, B04native/clipboard, actual toolbar/Google, B07extracted ZIP
smoke or B08upgrade/Sync. At preparation the runtime was NotRun; the linked dated
Run01 records the selected passes without changing the frozen preparation record.

## Unresolved

Future final-candidate/full-suite browser acceptance needs its own desktop handoff
or demonstrated isolation and exact candidate/harness preflight. Run01's three
selected assertions Passed; broader OWN01/C01/C02/A13/B07/B08 remain open.
