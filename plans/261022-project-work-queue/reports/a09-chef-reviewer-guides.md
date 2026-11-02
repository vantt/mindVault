# A09 — Chef/reviewer guides and release-note template

Date:2026-10-26, Asia/Saigon. Source:HEAD039c76d + preserved WIP;
manifest2.3.0, stable A13 candidate Pending. Status:**Done2026-10-26 — documentation preparation**;
[independent review Passed](a09-independent-review.md). FA01 product repair remains open.
No GUI, account/provider action, clipboard use, publication or product mutation.

## TODO

- [x] Inspect setup/profile/Cook/Builder/export/import/rotation source behavior.
- [x] Write practical Chef guide, synthetic Store reviewer guide and release/limit template.
- [x] Disclose incomplete restore/import behavior and source overwrite boundaries.
- [x] Verify literal cooking examples, source selectors/links and unchanged runtime/tests.
- [x] Independent review Passed; task/catalog/master/index updated; FA01 routed to separate A23 repair.

## Exact edited scope

| File | Delivery |
|---|---|
| [Chef guide](../../../docs/commercialization/operations/chef-guide.md) |Practical setup/unlock/profile jars/authorized selected Menu/actual Popup Builder/explicit Copy and hand-cooking example. Sharing/backup/import overwrite/recovery/three kinds of rotation separated. Current FA01 blocks working-restore claims. |
| [Reviewer guide](../../../docs/commercialization/operations/store-reviewer-guide.md) |Disposable public fixture, literal expected Dish, actual supported UI sequence, provider/access/clipboard/cleanup boundaries, current known limits and final packet fields still Pending. No production credentials or invented account/URL. |
| [Release-note/known-limit template](../../../docs/commercialization/operations/release-notes-template.md) |Artifact/source/hash/evidence/URLs/approval fields and known-limit register; no announcement, final version choice or fake acceptance. |
| [Commercialization index](../../../docs/commercialization/README.md) |Adds3guidance links and current draft revision date; existing WIP retained. |
| This report |Source facts, verification and FA01 repair routing. No registry Done before independent review. |

Scope5Markdown files only. Existing runtime/UI/scripts/tests/artifacts untouched;
no duplicated Privacy/Terms policy. Guides use actual UI labels where necessary
and the Recipe/Menu/Cookbook/Spices/Dish vocabulary for explanations.

## Source-bound behaviors

- [Options setup](../../../chrome-extension/src/options/options.js) checks matching
  master passwords/minimum8/backup confirmation, then shared background authority.
  [Profile editor](../../../chrome-extension/src/options/profile-editor.js) writes
  `{secrets,settings}` only while the current modal/session owns the save.
- [Popup/Home](../../../chrome-extension/src/popup/popup-home.js) handles selected
  cell/result routing; Generated is visibly readonly text. Builder preview differs
  from a standalone whole-Recipe manual screen. Hand cooking is a human own-profile
  method, not an accepted Android or Google/native UI run.
- [Full Access export](../../../chrome-extension/src/adapters/infrastructure/export-import-adapter.js)
  protects only selected original Spices with a separate sharing password; bundle
  headers include profile name/optional single Menu ID. It omits master password,
  other profiles, per-profile settings, default/global preferences and complete mappings.
- [Import](../../../chrome-extension/src/options/options-import-wizard.js):pasted/file
  JSON→validation→sharing-password decryption→local name→direct write. Current name
  collisions overwrite own/shared records without merge/overwrite prompt. Full Access
  can replace the specified Menu assignment; other assignments are retained, and
  import does not set default profile or restore global preferences.
- [Master rotation](../../../chrome-extension/src/options/options-password-change.js)
  re-encrypts existing profiles with new Cookbook protection. It does not change
  original Spices/account Dishes or revoke exported copies. Editing a Spice or Recipe
  affects account Dishes and requires deliberate account-password updates separately.
- UI01 Settings defaults and DEMO01 website illustration remain disclosed; Tag,
  clipboard, Sync/provider and broader platform limits follow current accepted scope.

## FA01 — diagnosed Full Access import blocker; no repair in A09

Source:import handler encrypts `state.derivedSecrets` directly before storing
`profile:NAME`. The cooking reader expects `profile.secrets[index]`; legacy import
correctly wraps its map. The Full Access export/decrypt API returns the original jar
map, not a complete own-profile envelope. Thus UI success alone cannot prove recovery.

Independent reviewer ran actual openImportWizard handlers in JSDOM using real
export/encryption/decryption/cooking modules and isolated in-memory storage:
valid synthetic bundle imported, success/onComplete occurred, but decrypted stored
record had jar keys without `.secrets`; subsequent GeneratePassword failed with
“Secret #1 not found”. Properly enveloped control cooked the expected Dish.
This is an isolated handler/core reproduction, not Chrome/provider/Sync evidence.

Root will dispatch a separate A23-FA01 repair after A09 review/plan update. No import
code or overwrite/lifetime policy changed in this guide task. Expected narrow repair:
produce the own-profile envelope expected by the existing reader, preserve original
jar values, and prove exact subsequent cooking independently. Actual browser/upgrade/
restore acceptance remains separately gated. Current guides explicitly prohibit
relying on the broken import and preserve a working original/recovery record.

## Fresh local checks

Node24.18.0; actual production parser/cooking/export modules and existing JSDOM,
synthetic data only. No account/Google access, runtime UI, clipboard or provider write.

| Check | Result / limit |
|---|---|
| Literal Recipe/Dish |Current parser accepts`r4nd0m#1`; actual cooking with synthetic own Spice1=`Basic*` yields exact`Basic*r4nd0m`. |
| Full Access API roundtrip |Synthetic original jar map decrypts exactly; wrong sharing password rejects. Whole-Cookbook/settings/master/default/mapping fields are not present. This is bundle API proof, **not** successful browser import/restore. |
| Source/setup/rotation selectors |17/17referenced Options setup/unlock/profile/rotation selectors resolve in actual HTML. No GUI actions performed. |
| Import source assertions |Direct jar-map encryption, direct same-name write and assignment replacement confirmed; no overwrite/create-only guard. Independent handler reproduction above establishes FA01 beyond text inspection. |
| Local Markdown links |26valid before report expansion; final handoff checks all added report links. Final public contact/fixture URLs remain Pending, not fake links. |
| `git diff --check` |exit0. No unchanged unit-suite replay or browser/restore acceptance claimed. |
| Runtime/test preservation |Existing Options/manifest/WASM/tests have empty Git diff; Golden SHA256`5f9faa1e2bb5e3d38151250ffaa961a94efba459f41786f5ae90402ab07aae26` unchanged. |

No release acceptance or real recovery action follows from prose/API checks. A13/A14
must reconcile FA01 repair and all final support/contact/access/artifact evidence
before using these guides as approved Store instructions. Source-changing A23 work
must amend affected guide/known-limit wording without erasing the diagnosed failure.

Next:A23-FA01 local repair,then remaining A Ready work. A09 closed as preparation
only; UI01 intended security behavior still needs C01.

## Dated amendment —2026-10-26, selected A23-FA01 local repair

The historical failure and A09 verification above are retained. Subsequent selected
[A23-FA01 work](a23-full-access-import-repair.md) repairs the own-profile envelope;
new actual-handler/real-crypto/reader/cooking local regressions Passed4/4. Guides
now distinguish that repaired local path from still-Pending browser/candidate/
upgrade restore acceptance. Existing malformed records are not migrated, and
same-name/assignment overwrite, global settings/backup limits and UI01 remain.
Independent [repair review Passed](a23-full-access-import-independent-review.md);
affected B01 remains separate from A09's completed draft review.

## Unresolved

Full Access browser/candidate/upgrade recovery acceptance; C01/C02 release boundary; C04 identity/
contact/reviewer access; C05 URLs; A13/B07/B08 artifact/browser/upgrade/Sync acceptance.
