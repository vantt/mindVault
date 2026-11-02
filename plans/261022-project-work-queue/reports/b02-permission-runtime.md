# B02 — permission variant runtime preparation

Date:2026-10-30, Asia/Saigon. Status:**Prepared/native inputs frozen;independent prelaunch review Passed;all runtime NotRun**.
Historical preparation snapshot. Current [2026-11-02 actual run](b02-permission-runtime-20261102.md)
and [runtime review](b02-runtime-20261102-independent-review.md):P0–P3 selected24cases Passed;
both owned workspaces removed and desktop returned;full B02 remains Partial.
Parent:[Track01](../phase-01-extension.md#task-b02),[A05 audit/protocol](a05-permission-audit.md).
Root owns browser/desktop/cleanup;executor prepares local ignored helpers only.

## TODO before implementation

- [x] Read current README/Track01/A05,manifest,actual toolbar helper,Menu receiver and Builder detector.
- [x] Prepare exact held278b74file variants P0/P1/P2/P3,only permissions array differs.
- [x] Prepare small native Windows Stable synthetic permission runner,eachfile<200lines;
  no product/46test/Golden/config or held-artifact edits.
- [x] Syntax/variant-diff/source-hash checks and independent prelaunch source review Passed.
- [x] Root bound actual browser/signature/variant paths and froze5native runtime helpers/4configs;empty profiles.
- [ ] Root obtains the fresh concrete B02 desktop handoff before any GUI runtime;actual grants inspected only during run.
- [ ] Root selected runs/raw results/cleanup→independent actual review→plan update.

## Reviewable next phase and exact files

New ignored `chrome-extension/build/b02-permissions-20261030/`:
`prepare-variants.cjs` verifies/extracts exact held ZIP and creates four manifest-only
copies with inventories;`native-driver.cjs`/`native-ui.cjs` reuse reviewed native
loader/genuine setup/Save/Lock;`toolbar-popup.cjs` inspects Chrome's real programmatic
action popover through CDP;`permission-smoke.cjs` runs selected synthetic cases.
Optional focused `synthetic-menus.cjs` keeps route/DOM/receiver work separate.
No native keyboard or OS clipboard helper is proposed in this phase.

| Variant | Exact permission removal from current8 | Other fields/resources |
|---|---|---|
| P0 control |None |Exact current held2.3.1 beta/74files |
| P1 |activeTab only |Other7permissions;tabs retained |
| P2 |tabs only |Other7permissions;activeTab retained |
| P3 |tabs and activeTab |Other6permissions |

Keep host_permissions/content_scripts/CSP/version/min116/commands byte-equivalent
as parsed values. Each variant gets a fresh private native Windows Stable profile
and distinct owned installed path;inspect runtime manifest+permissions.getAll and
record observed URL/title access/API errors. No inherited user grants;programmatic
popover opening is not proof of a physical gesture/activeTab grant.
Current base ZIP SHA256278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e.
Candidate manifest/resource hashes identify experiments,not new A13 candidates.

## Selected cases per variant — all NotRun

| Case | Concrete exercise / required observation | Budget |
|---|---|---|
| R00 identity/setup |Actual Stable/CDP ID+version+permissions match exact variant;genuine UI master/Spice Save;literal Builder control |130s per real derive;180s whole variant |
| R01 zero Menus |Synthetic non-Menu active,programmatic real action popover shows generic Home/no Dish;detector returns no supported Menus |15s assertion |
| R02 supported active Menu |Intercept synthetic HTTPS docs.google.com/spreadsheets route locally;automatic real receiver and real popover read selected Recipe/cook literal Dish;no provider/account access |15s assertion |
| R03 picker/background metadata |Two synthetic supported Menus in separate windows plus active non-Menu;actual Builder detector/picker labels/selection/manual input coherent;record tab metadata+grants |15s assertion |
| R04 receiver/lock |Programmatic TRIGGER_HOTKEY to actual content receiver gives literal Dish on each Menu;real Options Lock broadcasts,closes all content Dishes;locked repeat cannot cook |15s assertion |
| R05 bundled navigation |Actual Builder help button opens bundled Demo;Options remains accessible;no broader metadata permission inferred |15s assertion |

Only exact synthetic URLs are fulfilled by the local route;all other HTTP(S) is
aborted and recorded. Real HTTPS origin/Chrome permission enforcement applies to
fixtures,but Google DOM/provider login is not exercised. Content-message trigger
is not a native shortcut. Actual popover uses Chrome action API/CDP,not a fake popup
or ordinary popup tab;its programmatic invocation does not certify activeTab gesture
semantics. Any unsupported capability gets Failed/Blocked/NotExercised,not a fallback
or changed test. P0control failure stops dependent comparison;no automatic adoption.

Root budget:one180s block/variant (maximum4separate blocks,not silent retries),plus
30s host watchdog;normal lifecycle closes and proves native browser root PID ESRCH
≤15s. Root verifies all owned children/profile absence and removes only the recorded
workspace after evidence/review;no blanket Chrome kill/no owner profile. Retain all
failures,raw grants/tab metadata/blocked requests and exact hashes. Never restore by
editing canonical manifest;disposable profile removal leaves production unchanged.

## Explicit unselected acceptance / prerequisite

Native Ctrl+Shift+L,actual user action/temporary activeTab grant and cross-origin
revocation,OS Copy/30s/later-text ownership,real idle/absolute lifetime/worker wake,
HTTP/restricted/loading/closed-page edge matrix,Tag mismatch/navigation races and
real Google/provider remain NotExercised by this packet. Native/clipboard rows need
root-owned reviewed physical input/clipboard exercise and fresh desktop handoff;
no account is needed for synthetic coverage. Full B02 cannot be Done or minimum
permissions adopted from this subset. A later retain/remove decision must bind all
required supported paths and final support boundary,then separately review product
manifest/A04 declarations/A13 artifact changes. No such product choice is assumed.

## Actual local preparation delivery

Six ignored CJS files prepared:variant builder38lines,native driver136,unchanged
reviewed UI helper70,actual-toolbar CDP helper60,synthetic Menus64,selected runner92.
All are<200lines;actual `node --check` Passed all6. Variant builder ran **filesystem
only** into [prepared-variants](../../../chrome-extension/build/b02-permissions-20261030/prepared-variants/):
four74file trees and canonical base manifest;P0exact original bytes,P1/P2/P3 only
manifest permissions differ,all other73members byte-identical to278b. Exact merged
manifests have8/7/7/6permissions,all other parsed fields unchanged. No ZIP repackaging.
[Preparation/hash record](../../../chrome-extension/build/b02-permissions-20261030/preparation.json)
binds helpers/manifests/variant inventory. Source74/all46test hashes matched held
record;local report links and `git diff --check` Passed. No browser/test body ran.

Root config per variant:`ownedRoot,source,profile,output,inventory`(canonical held
inventory),`baseManifest`(canonical held manifest),`variant`P0–P3,`loader:'cdp'`,
`executable,expectedVersionPrefix,zipSha256`278b. Each fresh profile/output must be
absent;driver verifies canonical base manifest and exact permission-only overlay,
then all73other source hashes. Setup/cooking use actual UI/crypto;R02/R04 wait for
the real automatically injected receiver. Shadow DOM content Dish/error assertions
use Playwright piercing locators,not document-only queries. All HTTP(S) routes are
installed before extension loading;only exact3synthetic URLs fulfilled,other origins
aborted/recorded. Picker selects the intended Menu by title URL,not enumeration order.
Programmatic popup controls are real target DOM events,not native gestures.

Root invocation after source review/frozen inputs/fresh desktop gate:
`node.exe permission-smoke.cjs <owned-variant-config.json>`;P0first,then one explicit
candidate block at a time. Never run this command from the preparation agent or
represent source/collection checks as a browser result. [Independent preparation review Passed](b02-preparation-independent-review.md);
all actual permission case verdicts remain NotRun at this snapshot.

## Native frozen packet — no browser launched

Root [native workspace](../../../chrome-extension/build/b02-permissions-20261030/native-workspace.json)
and [frozen inputs](../../../chrome-extension/build/b02-permissions-20261030/native-freeze.json)
retain296variant files,5runtime helpers and pinned Playwright/core1.57.0 packages.
`prepare-variants.cjs` is a local filesystem preparation helper,not a native runtime input.
[Fresh host preflight](../../../chrome-extension/build/b02-permissions-20261030/host-preflight.json)
binds actual installed Windows ChromeStable156.0.8078.76,valid Google signer and
exeSHA66885d764fa17c7a7c53e1f94148e46959c5ec21fa5bc3ac22143eb2a1f7731a.
No browser launch or active test browser;all4profile paths remain absent;
profiles/results parent directories are empty.
Prepared workspace stays retained for the owner slot;this is not a cleanup claim.

Owned Windows root:
`C:\Users\van.tran_fgorg\AppData\Local\Temp\passchef-b02-oct30-8yT0uC`.
Runner:`runner\permission-smoke.cjs` beneath that root. Exact per-variant native
config paths are `runner\P0-config.json` through `runner\P3-config.json`;
frozen hashes match these retained copies:

| Variant | Retained config | Status |
|---|---|---|
| P0 |[P0-config.json](../../../chrome-extension/build/b02-permissions-20261030/P0-config.json)|NotRun |
| P1 |[P1-config.json](../../../chrome-extension/build/b02-permissions-20261030/P1-config.json)|NotRun |
| P2 |[P2-config.json](../../../chrome-extension/build/b02-permissions-20261030/P2-config.json)|NotRun |
| P3 |[P3-config.json](../../../chrome-extension/build/b02-permissions-20261030/P3-config.json)|NotRun |

Resource budget:180s per variant plus30s host-watchdog allowance;**15minutes overall**
for4separately numbered blocks and owned window/focus cleanup. Root requests the
specific B02 desktop slot after [independent preparation review](b02-preparation-independent-review.md);
the previously returned B03/B08 slot is not reused as authorization. No OS clipboard,
physical shortcut,account/real Google/Sync/policy/registry/publication action included.
Production manifest permissions remain unchanged;all variants are disposable.
Neither native staging nor preparation review selects/adopts a minimum permission set.

## Unresolved

Fresh scoped desktop handoff and actual selected runtime;unselected native/grant/clipboard/
edge cases and final retain/remove decision. Held A13/provisional verification is
not blanket stable-release or no-other-bugs acceptance. Sync stays owner-deferred.
