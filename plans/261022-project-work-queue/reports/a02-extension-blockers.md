# A02 — Extension blocker inventory

Date:2026-10-26, Asia/Saigon. Source:HEAD `039c76d17a2765bd753e09a7009e004dadfd6638`
+ preserved WIP; manifest2.3.0, package1.0.0. Audit only; no release candidate.
Status:Done — inventory/verification complete; [independent review Passed](a02-independent-review.md).
Product release gate stays closed.

## TODO

- [x] Read README, repository rules, Track01, commercialization and dated acceptance.
- [x] Separate owner observations, source-confirmed gaps and unexecuted acceptance.
- [x] Verify source/static reproductions and current unit baseline without GUI.
- [x] Complete ledger with expected/actual, source, scope, acceptance and routing.
- [x] Independent review Passed; root updates task/dispatch/master.

## Scope

Existing five browser slices remain historically accepted2026-10-12.
No browser/phone runtime, account mutation, Golden modification or repair here.
Current source inspection does not claim the owner's unspecified bugs were found.

## Ledger — implementation and owner observations

Common version for every row:source HEAD above, manifest2.3.0/package1.0.0;
no candidate ZIP. Priorities below are release triage, not a new security audit.

| ID / status | Expected / actual | Reproduction / source | Scope / acceptance / routing |
|---|---|---|---|
| OWN01 — MissingRepro; owner gate | Expected:owner's remaining bug/UI observations have identifiable flows and intended behavior. Actual:record says bugs/unfinished UI, without steps, screenshot, expected result or affected version. | [Commercialization](../../../docs/commercialization/plan.md), lines182–187,313–320. No new owner symptom supplied in A02. | Current release readiness; C01 supplies observations/version/repro/acceptance. Do not equate UI01 below with the owner's unspecified report, close gate or mark A23 N/A. |
| UI01 — Confirmed source gap; runtime NotExercised | Expected:Settings Display says “Per-profile setting; new profiles inherit these values”; three usable controls promise hint, default reveal and Builder Menu detection. Actual:only Settings reads/writes `displaySettings`; profile creation always starts `settings:{}`. Cook/Builder paths never consult `showPepperHint`, `revealPassword` or `autoDetectSheet`. | [Settings markup](../../../chrome-extension/src/options/options.html), lines298–351; [Settings logic](../../../chrome-extension/src/options/options-settings-tab.js), lines46–72; [new profile](../../../chrome-extension/src/options/profile-management.js), lines30–36. Static reproduction below finds only one production module referencing the four keys. | New-profile defaults, hint, reveal and Builder detection; source evidence establishes disconnected controls, not an observed Chrome run. Route A23 after C01 chooses completing intended behavior versus explicitly deferring controls. Acceptance:every enabled control honors a documented effect and new-profile inheritance, or UI clearly marks unavailable behavior; meaningful tests plus affected B01. Default reveal/security boundaries require a decision before changing behavior. |

UI01 does not reopen historically accepted profile-editor lifetime, Recipe Builder,
Tag or Popup A–J slices. Existing per-profile `pepperingHint` is separate and has
real consumers in [popup Home](../../../chrome-extension/src/popup/popup-home.js),
lines93–97 and [content presenter](../../../chrome-extension/src/content/content-presenter.js),
lines23–24. The orphan global defaults do not establish those consumers are broken.

## Ledger — preparation defects and acceptance gates

These have their own A/B/C owners; they are not newly diagnosed cooking defects.

| ID / status | Expected / actual | Reproduction / source | Scope / acceptance / routing |
|---|---|---|---|
| PKG01 — Confirmed local reproduction | Expected:assembly fails when either mandatory Argon2 loader/WASM is absent. Actual:copy script warns and exits0 even with both absent. | [copy_wasm.js](../../../chrome-extension/scripts/copy_wasm.js), lines17–29. Copy script alone into isolated temporary `scripts/`; run with no dependency directory. Fresh exit0, two warnings, no mandatory assets. | Packaging; A06 fail-closed assembly/validation, missing-one/missing-both fixtures, verified imports/resources/notices/ZIP root. Does not assert present source assets are missing. |
| CI01 — Confirmed static gap | Expected:validated PassChef development artifact with explicit runtime/retention/trigger scope. Actual:workflow installs frozen lockfile then directly ZIPs `src` as `mindvault-extension.zip`; no validation gate or explicit retention. Workflow-file-only edits are outside current push path filter. | [workflow](../../../.github/workflows/build_and_deploy.yml), lines3–9,35–57. No GitHub run executed. | A07 after A06; reviewed validation-before-ZIP, PassChef naming/runtime/retention/trigger. Website deployment remains C05. |
| COPY01 — Confirmed draft/source mismatch | Expected:valid Recipe, separate cooking/Cookbook protection, encrypted Sync and bounded clipboard/security claims. Actual:listing has `site+length32+symbols`, Argon2 Dish derivation, local-only/never-uploaded wording and “Nothing lingers”; README has “Hacker Proof”/“Unbreakable”. | [listing](../../../docs/commercialization/marketing/cws-listing-copy.md), lines22,51–52,70,82–112; [README](../../../README.md), lines36–55; [security contract](../../../docs/specs/extension-security.md). | A03 wording audit/correction; explicit draft status, correct examples/contracts and no absolute protection claims. No product code repair implied. |
| LEGAL01 — Confirmed draft/source mismatch | Expected:data/permissions/Terms match code and settled closed-source decision. Actual:Privacy lacks `offscreen`/`clipboardRead`, describes alarms cleanup and momentary tab-only access; Terms calls product free/open-source. | [Privacy](../../../landing-page/privacy.html), lines419–446; [Terms](../../../landing-page/terms.html), line385; [manifest](../../../chrome-extension/src/manifest.json); [permissions record](../../../docs/commercialization/legal/permissions-review.md). | A04 local draft correction and A05 permission audit; C04 final contact/legal owner; B02 for permission behavior; no publication in A02. |
| VER01 — Decision gate, not diagnosed bug | Expected:release channel/version has one approved record. Actual:manifest2.3.0/package1.0.0; earlier checklist explicitly says release version remains to confirm. | [manifest](../../../chrome-extension/src/manifest.json), line4; [package](../../../chrome-extension/package.json), line3; [commercialization](../../../docs/commercialization/plan.md), line195. | C02 chooses final channel/version/support; A13 binds exact candidate. Package/manifest difference alone does not establish Chrome load failure. |
| QA01 — Unexecuted acceptance, not diagnosed bug | Expected:chosen release boundary has artifact/upgrade, Sync/conflict, supported platform and measurements evidence. Actual:historical bounded source tests passed; those broader cells remain Pending. | [independent recheck](../../reports/261012-extension-recheck.md), remaining scope; [MVP status](../../../docs/prd/v1-mvp.md), lines1220–1237. | A10 protocol/A21 isolation preparation; C02/C03 and B03/B07/B08 runtime as applicable. Retain current Pending limits; no inference of failure or fresh pass. |

## Verification

Fresh2026-10-26, Linux Node24.18.0/pnpm10.24.0. Runtime path:
`/home/vantt/.nvm/versions/node/v24.18.0/bin` prepended to PATH.

| Command/check | Fresh result / practical limit |
|---|---|
| `cd chrome-extension; pnpm test:unit` | exit0;147/147 tests,20 files,4.75s; no skip/failure. Expected invalid-tag diagnostics only. These tests do not verify UI01's missing defaults or current Chrome runtime. |
| `node --check` over all `src/**/*.js` |55/55 passed; no source code changed. |
| JSON.parse manifest, EN/VI locale bundles, package |4/4 passed. |
| Isolated missing-dependency copy-script invocation |exit0 + both missing-file warnings; PKG01 reproduced. Temp directory removed in `finally`; real dependencies/assets untouched. |
| Production key-consumer scan + profile initializer assertion |UI01 confirmed statically:only `options-settings-tab.js` matches, initializer contains `settings:{}`. |
| `git diff --exit-code -- chrome-extension .github/workflows/build_and_deploy.yml` |exit0; all tracked extension/source/tests/workflow byte changes absent. |
| Golden SHA256 (`generate_password.spec.mjs`) |`5f9faa1e2bb5e3d38151250ffaa961a94efba459f41786f5ae90402ab07aae26`, matches prior accepted report. |
| `git diff --check` |exit0 on preserved WIP; report paths checked locally before handoff. |

Reproduce the two inventory checks from repo root, Node24, without launching GUI:

```js
import fs from 'node:fs';
import path from 'node:path';
import os from 'node:os';
import { spawnSync } from 'node:child_process';
const temp = fs.mkdtempSync(path.join(os.tmpdir(), 'passchef-a02-audit-'));
try {
  fs.mkdirSync(path.join(temp, 'scripts'));
  const script = path.join(temp, 'scripts/copy_wasm.mjs');
  fs.copyFileSync('chrome-extension/scripts/copy_wasm.js', script);
  const run = spawnSync(process.execPath, [script], { encoding: 'utf8' });
  console.log(run.status, run.stderr);
} finally { fs.rmSync(temp, { recursive: true, force: true }); }
// Static UI01 scan: rg -n 'displaySettings|showPepperHint|revealPassword|autoDetectSheet' chrome-extension/src
// Initializer: chrome-extension/src/options/profile-management.js:34
```

Historical25 headed/8 native counts remain dated2026-10-12. No fresh headed E2E,
Google provider/native shortcut, clipboard or Sync run. Full `pnpm test` includes
headed E2E and was not run under this A-only authorization. No build/lint scripts
exist in current extension package; syntax/JSON checks are recorded as such.
All unrelated WIP preserved. This task changes only this report.

## Dated A09 amendment —2026-10-26

**FA01 — Confirmed P1 Full Access import shape defect**;source remains manifest2.3.0
at HEAD039c76d + preserved WIP. Expected:importing the exported original jars creates
an own profile that cooks the same Dish. Actual:the wizard reports completion but
encrypts the raw jar map instead of the own-profile envelope;reader cannot find
`profile.secrets`. Independent actual-handler/real-crypto isolated reproduction:
control cooks,import completes,restored Cook fails`Secret #1 not found`.
[A09 independent evidence](a09-independent-review.md#fa01--independent-actual-handler-reproduction).
No browser/provider proof or repair in A09. Route selected A23-FA01 local envelope
repair plus meaningful regression/review,then affected B01 when authorized;UI01's
C01 choice is separate. Original8-item A02 inventory/checks above retain their scope.

Follow-up2026-10-26:FA01 envelope [repair](a23-full-access-import-repair.md) and
[independent local review](a23-full-access-import-independent-review.md) Passed.
New imports locally cook correctly;older malformed records are not migrated.
Affected headed B01/candidate/upgrade restore acceptance stays Pending;A23 overall
Partial and UI01/OWN01 owner gate remain open. Original failed reproduction retained.

## UI01 follow-up —2026-10-26

Owner [approved temporary deferral](c01-owner-blocker-decisions.md). The card and
orphan handlers are now removed locally;stored values and existing hint/Builder/
Dish behavior are preserved. [Delivery](a23-display-defaults-deferral.md) and
[independent review Passed](a23-display-defaults-independent-review.md). This resolves
UI01's scope choice,not implementation of its preferences or actual B01 acceptance.
OWN01 MissingRepro/broader C01 and C02 remain open;the original inventory above is historical.

## Next — original A02 handoff

Inventory Done after independent review. Proceed A03 preparation;
UI01 repair waits C01 scope choice; A04/A05/A06 remain independently feasible.
A13 and owner product gate remain blocked; current inventory is not release acceptance.

## Unresolved

C01 owner bug/UI details; C02 version/support/Sync boundary; desktop slot for B cases.
