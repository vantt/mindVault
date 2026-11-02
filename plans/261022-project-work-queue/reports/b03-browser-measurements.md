# B03 — browser support/performance preparation

Date:2026-10-27;current update2026-10-30, Asia/Saigon. **Scoped Done:selected native
WindowsStable156.0.8078.76/CfT116.0.5845.96 P0–P4 observations/descriptive measurements**.
[Actual results](b03-measurements-20261030.md)/[independent raw review Passed](b03-measurements-20261030-independent-review.md),
final identity/cleanup verified. Sparse/fast sampler cadence flags and browser branding
remain explicit;no broader performance SLO/platform/Store/Sync acceptance inferred.
Original dated preparation/NotRun snapshots below are historical and remain retained.
Required cells:Windows11 x64 actual Google Chrome Stable and declared Chrome116.
Held2.3.1 artifact is not stable A13 acceptance. Original preparation involved no
GUI/download/install/input/clipboard/policy/account/phone/ADB/Gradle/publication action.

## TODO

- [x] Bind production Argon2 path/parameters and source/artifact identities.
- [x] Check current official branded-Chrome automation/loading and116 provenance.
- [x] Separate available metadata from MissingValidExercise/routes/owner gates.
- [x] Specify finite cold/warm/startup/memory/fail-closed evidence and cleanup.
- [x] Local source/docs/link/status verification and independent preparation review Passed;runtime still gated.

## Required cells and current resources

[C02 decision](c02-release-boundary.md) requires Windows11 x64 actual Google Chrome Stable
and Chrome116 while minimum116 is retained. [Held2.3.1 evidence](c02-version-231-local-verification.md)
binds74runtime files/ZIP SHA256
`278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`;
held record `0d7e97303bdd67040710a37bb514ca893e40fee30797364a9264556b9f0cd336`.
No stable A13 acceptance or fresh browser result from those hashes.

| Resource | Observed availability / limit |
|---|---|
| Windows host |Root's read-only CIM metadata:Windows11 Pro N10.0.26200/build26200/64-bit. CPU model/cores/RAM/power state still need run-specific selected metadata. |
| Installed actual Chrome |Root read-only File/ProductVersion156.0.8078.48 at standard64-bit ProgramFiles path; executor independently found file/size4,522,648bytes and hashed executable (resource JSON). Signature/channel/live version not yet bound; no launch. Root found system32/user Chrome absent in checked locations. |
| Existing Node |Root found Windows `Program Files/nodejs/node.exe`; no Windows Node run/helper in this preparation. Linux Node24.18.0 used only for documentation metadata reads. |
| Browser cache |Linux Playwright dirs1200/1243; read-only Windows dirs1200/1223/1228. Directory names are not browser versions; no116 executable identified by this non-exhaustive scan. |
| Existing B01 |Linux/WSLg ChromeTesting143,3selected cases/33.046s only. Neither a Windows support result nor an Argon2 latency sample. |
| Isolation |[A21](a21-browser-isolation.md) remains Unverified; previous desktop slot released. New GUI/input/DevTools/file picker requires applicable fresh owner handoff. |

Only this report and ignored [resource-proof JSON](../../../chrome-extension/build/b03-preparation/resource-proof.json) authored.
Resource JSON SHA256:`0b5ed3e20f0d8fe36d1ca44f64b5b1845634c9dfdee74c7422def4b141a78f19`.
**MissingPreparedRunner/MissingValidExercise:**Stable/116 load/control and trusted timing/memory
observers need review and a valid exercise first. No helper or harness bypass authored.

## Current primary guidance — checked2026-10-27

Docs-seeker returned no Context7 result; official sources used:

- [Chrome team announcement](https://groups.google.com/a/chromium.org/g/chromium-extensions/c/1-g8EFx2BBY):
  branded Chrome137+ removes `--load-extension`; Chromium/CfT retain it. Existing
  [Playwright fixture](../../../chrome-extension/test/e2e/fixtures.ts) flags are not a verified Stable156
  loader. CfT substitution cannot be reported as actual Google Chrome Stable.
- [Chrome Load unpacked guide](https://developer.chrome.com/docs/extensions/get-started/tutorial/hello-world#load-unpacked)
  documents Developer Mode→Load unpacked. Feasible proposed Stable route:launch
  a fresh synthetic profile, owner loads reviewed directory via that page/file picker,
  records installed ID/version, then reviewed observation. No production profile/policy/account/Sync edits.
- [Chrome136 debugging change](https://developer.chrome.com/blog/remote-debugging-port):
  debugging ports/pipes need nondefault user-data-dir. Do not attach to owner
  default profile. Experimental [CDP extension API](https://raw.githubusercontent.com/ChromeDevTools/devtools-protocol/master/pdl/domains/Extensions.pdl)
  exists, but tip-of-tree/API or old Canary examples do not prove Stable156 loading;
  matching protocol/flags/control canary must precede a helper.

For116, official [CfT metadata repository](https://github.com/GoogleChromeLabs/chrome-for-testing#json-api-endpoints)
describes archived version/platform endpoints. Small official
[`LATEST_RELEASE_116`](https://googlechromelabs.github.io/chrome-for-testing/LATEST_RELEASE_116)
text and [116.0.5845.96 JSON](https://googlechromelabs.github.io/chrome-for-testing/116.0.5845.96.json)
were read with Node fetch:revision1160321, win64 Chrome/ChromeDriver URLs listed;no binary request/download/install/launch/signature check.
This is a **CfT116 acquisition proposal**, not installed branded116/Windows11 proof.
Pin branding/version and agree whether it satisfies the116-floor cell before acquisition;
otherwise Blocked pending a legitimate approved branded116 route. No third-party installer.

## Production path and measurement boundaries

[Argon2 adapter](../../../chrome-extension/src/adapters/infrastructure/argon2_adapter.js)L26–48
uses Argon2id,time3,mem65536KiB,parallelism4,hash32bytes→AES-GCM JWK.
[Security contract](../../../docs/specs/extension-security.md):fail-closed,no vault PBKDF2 fallback/KDF reduction/storage migration.
The64MiB setting is not observed OS/process peak;sharing PBKDF2 is separate.

Actual unlock follows Options UI→trusted auth coordinator→
[offscreen client](../../../chrome-extension/src/security/offscreen-client.js)→
[offscreen document/worker](../../../chrome-extension/src/offscreen/offscreen.js)→
[crypto worker](../../../chrome-extension/src/offscreen/crypto-worker.js)→adapter/WASM.
One worker per offscreen document;idle notification may close it. Observe actual worker/WASM retention.
Warm browser/profile does not imply warm WASM;direct adapter loops are separate microbenchmarks.
Do not keep clipboard work pending to force warmth.

## Cases and sample plan — all NotRun

Each cell gets a fresh UUID profile,genuine synthetic master/Spices and UI-created encrypted Cookbook.
Never reuse newer-browser profile in116;no real account/Menu,key/token/password/Dish logs or clipboard.
Verify extension ID/version/source hash;retain raw samples,failures/timeouts/warmups and reasons,no silent retries/removal.

| Case | Proposed exercise and observations | Planned count / cap |
|---|---|---|
| P0 support/loader canary |Real Stable or exact116 on Win11, correct extension load, Options setup/unlock/lock, actual Worker/WASM/offscreen and exact synthetic Dish via Builder; required APIs/fail-closed errors. |1canary/cell,3minutes;any unavailable path→Blocked/Failed,not replacement browser. |
| P1 process-cold startup/unlock |After genuine setup and orderly owned-browser exit, reuse only disposable encrypted profile; fresh native PID each sample. Measure launch dispatch→Options locked ready, then actual unlock input/click→ready. Record process/worker/document identities and observed WASM initialization. |10samples/cell,10minute block cap;per-derive130s host cap includes existing120s production timeout. No OS cache flushing;label process-cold,not disk-cold. |
| P2 warm-browser unlock |Same process/profile, normal Lock between attempts;2recorded warmups plus20measured real unlocks. Observe offscreen/worker retention each time;split warm-browser/fresh-worker vs retained-worker groups. |20measured/cell,10minute block cap;130s per derivation maximum. Insufficient completion→partial N,not20Passed. |
| P3 sampled memory |Owned PID tree/extension target association, baseline before and samples during P1/P2 at200ms;record working set/private bytes and observed maxima/deltas by PID/type. |At least5complete unlock intervals/cell;5minute observation cap. Missed/unknown worker or sampling gaps→Incomplete memory result. |
| P4 fail-closed |Identity-preserving disposable fault exercise below:missing/corrupt WASM must refuse/no new session/Dish;retained salt/profile/map intact,error/no weaker fallback;restored control unlocks. |Missing+corrupt cases/cell;5minute cap,130s per derive. Auth counters follow existing policy. Fault driver MissingValidExercise. |

P4 requires **same owned installed path/extension ID/profile** throughout control→fault→restored control.
Manifest has no key:a newly pathed copy may change ID/storage and prove nothing about prior Cookbook preservation.
First create genuine synthetic state in one disposable working copy;close owned browser before mutating only its WASM.
Reopen same path/profile,record unchanged ID and exact retained salt/profile/map before attempted unlock;
close again,restore exact original WASM hash there,then reopen good control. Never mutate canonical/held files.
Until a reviewed identity-preserving fault driver exists,P4 remains MissingValidExercise,not executable Ready.

P1/P2 measure **end-to-end production unlock** including IPC/worker/WASM/AES,not pure Argon2 time.
Keep host/browser clocks separate;one monotonic clock per interval. Future reviewed worker observer
may report narrower KDF timing separately without changing parameters/shipped artifact. Current observers are missing.

Memory needs owned native PIDs/reviewed Windows WorkingSet64/PrivateMemorySize64 sampler or equivalent.
JS heap misses WASM/native/multi-process memory;200ms sampled maximum is not true peak.
No unqualified shared-working-set sum or64MiB-total claim. Startup/setup/KDF/test durations stay separate.

Report per cell:exact OS/build, CPU/cores/RAM, power/thermal/background-load notes,
binary path/signature/hash/live browser version/brand/argv, source/ZIP/helper hashes,
clock/observer method, raw N/timestamps/status, min/median/mean/max and exclusions.
Recheck executable hash/version after each block;an automatic update changes the
cell identity and requires explicit reassessment,not disabling updater/policies.
No p95 from these small planned samples; no latency/memory Pass threshold is
invented. Until an owner-approved target exists, metrics are descriptive; functional
parameter/fail-closed/support assertions get their own verdict. Time caps are safety
budgets, not performance SLOs. One approved bounded block per slot; don't silently
combine two browsers/all cases into a long owner wait.

## Future preflight/launch/end — proposed only

Resolve route/instrumentation,author/review/freeze valid helpers(<200lines each) and exact targets.
No measurement command exists:don't invent `pnpm measure` or run Linux fixture as Stable.
Root needs fresh desktop handoff/exclusive evidence directory. Proposed exact read-only Windows preflight:

```powershell
$chromePath = Join-Path $env:ProgramFiles 'Google\Chrome\Application\chrome.exe'
Get-Item $chromePath | Select-Object Length,@{n='FileVersion';e={$_.VersionInfo.FileVersion}},@{n='ProductVersion';e={$_.VersionInfo.ProductVersion}}
Get-FileHash -Algorithm SHA256 $chromePath
Get-AuthenticodeSignature $chromePath | Select-Object Status,StatusMessage
Get-CimInstance Win32_Processor | Select-Object Name,NumberOfCores,NumberOfLogicalProcessors
Get-CimInstance Win32_ComputerSystem | Select-Object Manufacturer,Model,TotalPhysicalMemory
```

Root creates a unique test profile,never owner default;proposed manual launch,not executed:

```powershell
$profilePath = Join-Path $env:TEMP ('passchef-b03-' + [guid]::NewGuid().ToString())
& $chromePath "--user-data-dir=$profilePath" '--no-first-run' 'chrome://extensions/'
```

Only after a reviewed debugging route may add loopback ephemeral remote debugging
to that same disposable profile. Owner Developer Mode/Load unpacked selects exact
reviewed2.3.1 source (or explicitly separately scoped extracted ZIP);confirm ID/
version/real browser brand and owned process before running any measurement helper.
If file picker/debugging/extension control unavailable, stop as MissingValidExercise;
do not change registry/policies/owner profile or substitute CfT silently.

End:normal owned-profile browser close;verify native children ended/profile absent or approved removal.
Never `taskkill /IM chrome.exe`/blanket Stop-Process. Retain raw/trace/status/exit/timeout/cleanup audit;
check source/tests/fault-copy restoration,then release desktop. Host watchdog is not child termination proof.
No OS/cache changes,owner profile/clipboard/account cleanup or real data write.

## Actual preparation checks and deliverable

Source/contract/fixture reads confirm KDF parameters,120s timer/worker ownership.
Read-only file/size/SHA/cache/held-record and primary docs/metadata checks;resource JSON keeps6categories NotRun.
Ten local targets/KDF+120s/6NotRun/resource hash/46test hashes and `git diff --check` Passed.
No executable run,policy change,tests replayed,browser acquisition or unproven performance/helper code.

## Scoped runner preparation followup —2026-10-27

Owner said “làm luôn đi” after the full-candidate/Windows/Sync next-step proposal.
Proceed with useful local preparation for those listed issues;C01's absence of other
bugs is unconfirmed and is not re-asked as a blocker to this preparation. No blanket
bug-list approval or new browser/input/clipboard/account action inferred here.

- [x] Inspect native Windows Node/pnpm/Playwright files and dependency platform availability.
- [x] Bind smallest unchanged full-suite lane plus correctly labelled extracted-ZIP/Stable lane.
- [x] Prepare bounded owned workspace helper;record identity/loader/run/cleanup gates.
- [x] Verify local links/source/test preservation;handoff before root runtime.

**Concrete next lane:**unchanged package script/full canonical suite against exact extracted2.3.1 ZIP,
using existing Linux Playwright/CfT143. This is correctly labelled Linux extracted-ZIP evidence,
not Windows Stable/116. Filesystem workspace does not prove desktop isolation;root owns the fresh
scoped desktop handoff,synthetic clipboard test and all browser lifecycle/resource checks.

Ignored [prepare-workspace.mjs](../../../chrome-extension/build/b07-run-01/prepare-workspace.mjs)
(<200lines,node syntax check Passed) verifies ZIP278b/inventory,extracts exactly74runtime files,
copies46unchanged canonical tests/fixtures plus original package/lock/Vitest/Playwright configs,
and symlinks installed Linux node_modules. Workspace `/tmp/passchef-b07-dQiFTQ/workspace` intentionally
has no `build` component,avoiding the artifact exclusion. No source/test/config override or install.
[Preparation record](../../../chrome-extension/build/b07-run-01/preparation.json) binds source74/harness50
and owned temp root;exclusive creation refuses overwrite. Root preflight rechecks all hashes.

Actual **collection only**:Vitest `list --filesOnly --json` exit0/exact21files;
Playwright `test --list --workers=1 --retries=0 --reporter=line --global-timeout=1200000`
exit0/exact28cases. Logs:[unit collection](../../../chrome-extension/build/b07-run-01/unit-collection.json),
[browser collection](../../../chrome-extension/build/b07-run-01/browser-collection.log).
Harmless [two-command shell probe](../../../chrome-extension/build/b07-run-01/argument-forwarding.json)
confirms pnpm forwards the four flags to the final command of `vitest run && playwright test`;
no test body/browser/network executed by this preparation. Proposed root runtime from workspace:

```sh
PLAYWRIGHT_JSON_OUTPUT_NAME=/home/vantt/projects/passMan/chrome-extension/build/b07-run-01/report.json pnpm test --workers=1 --retries=0 --reporter=line,json --global-timeout=1200000
```

Root retains combined unit/browser stdout,JSON/native exit/profile-child cleanup;1200s caps Playwright,
not the preceding Vitest phase,so use a separate bounded overall host watchdog and inspect child termination.
Expected151unit+28browser is a collection target,not an executed Pass here. No blanket Chrome kill.
After raw evidence/independent review,root removes only recorded workspace/temp profiles and releases desktop.

**Native Windows initial metadata:**read-only PowerShell command/file metadata found Node22.14.0 at
ProgramFiles, pnpm.CMD/Corepack shims there,and AppData global pnpm package10.27.0;no native version
command/install/browser ran. Project pnpm10.24 availability/shim routing still needs a non-network
runtime canary. Existing project dependency tree has no Win32 Rollup/esbuild packages;Linux bindings
cannot certify native full-suite readiness. Windows Playwright cache dirs are metadata only.
Next implementation,after review:separate ignored `build/b07-windows-01/` fresh-profile/manual-LoadUnpacked
controller+observer for actual Stable and exact ZIP extraction. Original46fixtures stay immutable;
do not reuse unsupported branded --load-extension flags. Resolve native package/control prerequisites
locally if possible,then the approved handoff;no silent CfT replacement or account/policy/download action.
Later direct WindowsNode `--version` returnedv22.14.0. Root's
[native module canary](../../../chrome-extension/build/b03-native-01/module-canary.json) Passed after copying
existing pinned Playwright/core pure-JS packages to its owned WinTemp preparation
([record](../../../chrome-extension/build/b03-native-01/preparation.json));UNC Linux-pnpm symlink resolution failed first.
This fixes browser-driver availability without Windows Vitest/Rollup/esbuild installation;no Windows
browser/support result follows. Root is separately reviewing native Stable/CDP load-unpacked P0.

**Separate WASM fail-closed probe prepared for review (runtime NotRun):**ignored
[`build/wasm-failclosed-01/`](../../../chrome-extension/build/wasm-failclosed-01/) contains123-line
spec and7-line config;one case collected/transformed without browser execution.
Runtime body creates a unique `/tmp/passchef-wasm-` lab from exact278bZIP,uses one installed
path/profile/ID for genuine UI setup→enabled editor Save→literal Dish baseline→missing WASM→
corrupt WASM→restored control. No injected vault/session/AES readiness marker or core oracle.
Each fault follows successful context close;assert unchanged encrypted Sync state,locked/no session,
no old Dish and explicit crypto-unavailable UI. Finally restore original WASM only after close;
remove own lab only on success,retain failure lab/raw state otherwise. Canonical/held bytes untouched.
Config budgets420s test/480s global,one worker/zero retries;root must review identity/native cleanup
and evidence paths before any separate run. Collection is not fail-closed acceptance.
[Preparation record](../../../chrome-extension/build/wasm-failclosed-01/preparation.json) SHA
`d62d1a0e1d3a0e8070002de5877081441d3dd71e31cdcede2b2087749291a7cf` binds final
spec/config/collection;spec SHA`15911053f7be2fd01bc40a6babce1ae134f9577f3f81628f2c218fe35286db7f`.
Future root command (not executed by preparer):

```sh
PLAYWRIGHT_JSON_OUTPUT_NAME=build/wasm-failclosed-01/run-01-report.json pnpm exec playwright test --config build/wasm-failclosed-01/playwright.config.ts --reporter=line,json
```

**Dated Linux fault execution followup:**root subsequently ran that separate probe,
1/1Passed248.666s/exit0. [B07 dated results/raw hashes](b07-zip-suite.md) record all
four baseline/missing/corrupt/restored stages on CfT143,unchanged installed path/profile/
ID`acbbpioacgekbichpmmbonihnellccpo`,encrypted-state preservation,no session/old Dish,
restored WASM hash and absent owned lab/processes. Original preparation JSON/NotRun
snapshots are retained;this Linux result does not execute required Windows Stable/116
P4 or provide KDF timing/memory metrics. Reporting agent launched no browser.

## Unresolved

Valid Stable/116 extension-loading and measurement routes, owner desktop handoff,
approved exact runtime/hardware/candidate, A13/OWN01/C01/A23 and actual B03/B07/B08.

## Current native execution and scheduling followup

[Actual Windows Stable156 P0](b03-native-stable-run-01.md) Passed5.315s with
[independent raw review](b03-native-stable-independent-review.md);three prior driver failures retained.
Source/ZIP/Golden unchanged;owned Linux/native workspaces removed,desktop released.
Original preparation resource/case NotRun records above are historical. Native
Windows116/P1–P4/full-matrix acceptance remains Pending;Linux fault proof is separate.
[Owner has second computer but defers Sync](c03-sync-owner-readiness.md) to a later slot;
mandatory before-publication Sync acceptance is retained.

## 2026-10-30 continuation — native measurements preparation

Owner: “em tiếp tục đi, nhớ cập nhật plan”. Root owns all browser/runtime/acquisition,
Windows host queries and cleanup. Executor prepares ignored helpers only;original46tests,
74source files,held ZIP and all earlier raw outcomes remain immutable. Sync remains
owner-deferred to a later slot and mandatory before publication.

- [x] Re-read README/rules/current B03/native Run04/source/provenance before implementation.
- [x] Prepare genuine UI helper using Run04 Settings navigation/locked-UI readiness;
  modern Stable CDP loader versus explicitly labelled CfT116 legacy flags.
- [x] Prepare bounded P0/P1 process-cold10/P2 warm-browser2warmups+20 samples and
  P3 native200ms WorkingSet/private-byte observer for at least5 measured intervals.
- [x] Prepare P4 same-sourcepath/profile/ID missing/corrupt/restored control on each
  separate browser cell;close before mutation and retain encrypted salt/profile/map.
- [x] Syntax/static proof,held/source/test hashes and independent helper source review Passed before root run.
- [x] Root bound actual Stable/CfT116 identities and executed reviewed P0–P4 blocks;
  results/independent review/immutable identity/final cleanup now linked in dated followup.

Minimal new ignored files under `chrome-extension/build/b03-measurements-20261030/`:
`native-ui.cjs` (real selectors/crypto UI),`native-driver.cjs` (loader/identity/closure),
`measure-unlock.cjs` (sample orchestration),`sample-memory.ps1` (owned PID tree),
`fault-smoke.cjs` (disposable identity-preserving fault/control). Each<200lines.
Root stages fresh owned Windows directory with exact ZIP278b59…f1e9e extracted74files,
inventory and pinned Playwright/core1.57.0 pure-JS packages;no dependency installation.
Each invocation has immutable JSON input/output and fresh owned profile per browser;
no cross-version profile reuse. Loader/candidate/API canary failure stops that cell.

P0 uses genuine UI setup+enabled Spice Save→Settings Lock→authoritative locked/no-key
and visible unlock UI→real unlock→literal `Basic*r4nd0m`. P1 closes/reopens the same
synthetic encrypted profile10times;record launch→locked-ready and click→dashboard
separately. P2 uses one browser process with normal Lock,2warmups+20 measured unlocks.
Observe CDP offscreen/worker targets before/after each interval;classify only observed
retention,otherwise warm-browser/worker-unverified. No direct adapter loop or forced
worker retention. P3 samples actual owned browser PID+descendants;each sample has
native UTC/PID/type/WorkingSet64/PrivateMemorySize64 and sampling timestamps. Require
at least5 complete observed intervals;200ms requested cadence/sample maxima≠true peak.
Keep all failures/warmups/raw samples;min/median/mean/max only,no p95/invented SLO.

Per block caps:P0 180s,P1/P2 600s each,P3 within P2,P4 360s;derive host130s retains
production120s timeout. Root uses a separate finite host watchdog and verifies actual
child/process termination;timer alone is not cleanup. Helpers fail safely/persist raw
on error,never blanket-kill Chrome. Only disposable P4 WASM bytes may change after
context closure;restore original bytes and positive control before cleanup. Root
retains result/log/input/helper hashes before removing its owned directories. Current
Oct30 runtime verdicts **NotRun**;previous27Oct Stable P0/Linux proofs remain historical.
Next after these cells:separately reviewed2.3.0→2.3.1 greater-version upgrade protocol;
no Store upgrade or Sync acceptance inferred from this preparation.

### Oct30 helper delivery and actual local checks

Six ignored helpers now prepared (each<200lines):the five above plus focused
`memory-observer.cjs` for sampler lifecycle,interval bracketing,baseline deltas and
during-only sampled group maxima. [Helper preparation/hash record](../../../chrome-extension/build/b03-measurements-20261030/helper-preparation.json)
binds all6files;`node --check` Passed all5CJS. PowerShell AST parsing/root Windows
execution is a separate operator check. Independent reviewer source review Passed
all6helpers;no product/test/config changes or runtime performed by preparer.

Driver records CDP targetCreated/Changed/Destroyed during each unlock;unknown worker
identity remains unverified. Normal closure requires native browser root PID ESRCH
within15s after context.close,records PID/wait and fails if missing/still live.
P1 navigation-only timing is separate:startup metric ends after actual AUTH_STATUS
locked/no session and visible Unlock/hidden Dashboard. P3 dynamically discovers new
PID descendants each sample,records actual cadence gaps and raw per-process memory;
requires5complete bracketed measured unlock intervals,otherwise P3 Incomplete even
if functional unlocks Pass. Baseline/post samples are excluded from during maxima.
P4 creates a nonempty synthetic Menu assignment through the real Add Assignment
prompt and verifies salt/encrypted profile/default/mapping preservation throughout.

Actual local proof:ZIP278b59…f1e9e/74members/CRC and all74source hashes matched held
record;all46canonical test hashes matched immutable held-record baseline. Old
`/tmp/passchef-c02-231-20261027/initial-tests.sha256` no longer exists on Oct30;
that unavailable temp-path check was replaced by the retained46hash record,not
reported as a successful checksum run. `git diff --check` Passed. All earlier raw
results/frozen helpers remain;root freezes each runtime revision separately.

Initial Oct30 preparation snapshot:root reported native CfT116 P0 Passed;that delivery did
not relabel P1/P2/P3/P4 as run or collapse CfT116 into branded Stable116. Runtime
case reports,actual version/signature/inputs/cadence/cleanup and independent raw
review follow root's separate numbered evidence. Root alone owns all GUI/runtime.

## Final Oct30 execution followup — selected B03 scoped Done

[Actual results](b03-measurements-20261030.md)/[independent raw review Passed](b03-measurements-20261030-independent-review.md)
supersede earlier Partial/NotRun status **only for these selected cases**:actual
WindowsStable156.0.8078.76 and explicitly branded CfT116.0.5845.96 P0,genuine UI
process-cold10,warm-browser2+20,original sparse+separate fast P3five intervals each,
and same-ID baseline/missing/corrupt/restored P4. Production KDF/74source/46tests
unchanged;native exits/final11profile+workspace absence/process count0 verified.
Desktop released. No p95/performance threshold/pure extension RAM/true peak claim;
fast sampling still flags degraded2/5CfT and1/5Stable intervals. Broader release/Store/
Sync gates remain separate tasks and do not keep completed selected B03 observations
open. [B08 local unpacked numeric upgrade](b08-artifact-acceptance.md) Passed/reviewed;
B08 overall Partial. Earlier Run01–03 failures/frozen NotRun records remain intact.

## Current unresolved

Broader platform/formal release acceptance and sampling/worker-identity limits;
Store delivery/full B08 and real2device Sync. Owner deferred Sync to later slot;
mandatory before-publication acceptance unchanged. No further owner question here.
