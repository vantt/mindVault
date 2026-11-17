# Phase05 — Independent verification, honest acceptance and delivery

## Current execution —10November2026 (Asia/Saigon)

- [x] Read README/rules and reconcile remaining gates against existing evidence.
  Phases01–03 remain Done; no missing production feature identified. Remaining:
  G1 human Google cancellation, G2 different-Menu stale callback, L3 OS low-memory
  recovery, D1 final candidate validation and D2 scoped delivery.
- [x] Obtain current Xiaomi authentication/Android-Back readiness. Owner answered
  “Có, Xiaomi sẵn sàng”. This is readiness, not a new remote revocation approval.
- [x] Fresh local JVM/build/lint verification; preserve Golden tests and existing WIP.
  [10November verification](reports/current-verification-20261110.md):151 tests Passed,
  zero failures/errors/skips; root/standalone build Passed, lint0errors/7warnings/1hint.
  All214 source/config paths stable,64 baseline tests/fixtures exact; current3APKs
  match source205/VI-repair candidate. Main compilation/lint analysis reused;
  AndroidTest lint executed. No device acceptance inferred.
- [x] Review/reuse provider37 and obtain specific one-action approval/readiness.
  Current source205/25host hashes/16dependencies/APKs exact; owner explicitly approved
  the all-client impact, same account, Android Back and normal restoration scope.
- [x] Execute provider37 core01 once; retain the strict failure. Native Failed381.729s,
  host384.592s at unchanged120s marker assertion. Owner confirmed actual revocation
  only after checkpoint expiry (gesture time unknown); no stale marker published.
  One postfailure HOME allowed genuine JUnit/owned cleanup to complete; owner inventory,
  source/APKs preserved and native inactive. No human cancellation/G1/G2 acceptance.
- [x] Complete mandatory normal restoration after actual37 revocation. Restore01
  Failed16.119s/18.795s; requested-Menu CONNECT_REQUIRED/token absent and completed
  local rejected-token eviction independently reviewed. Source/owner/UUID preserved.
  Separate normal restore02 Failed182.232s/185.365s at genuine biometric setup;
  no Google request reached. Exact source/owner/UUID preserved, native inactive.
  Owner later reported biometric action; gesture time unknown. Restore03 NotStarted
  at an existing debug PID. A subsequent guarded inactive-package stop and rmdir of
  only its empty restore02 UUID restored exact original inventory. No new native
  run in03. Restore04 likewise NotStarted at an inactive debug PID; guarded recovery
  restored exact inventory before05. Actual fresh HOME readiness then restore05
  Passed120.493s native/122.331s host: original Menu/Test metadata/rows, mapping and
  durable cache; owner/source/APKs exact, native inactive, restorationRequired=false.
  [Current report](reports/google-android-back-cancellation-20261110.md). Independent
  closure audit Passed; readiness timestamp unchanged (82.741s old at dispatch).
  No human-cancellation/G1/G2 or fresh-consent claim.
- [x] Review/run prepared isolated userdebug VM observations; establish actual
  root/PSI/LMKD visibility before any bounded memory-workload decision.
  [Actual10November observations](reports/lmk-observable-runtime-20261110.md):
  BootedOwnedUserdebugIdle201.818s; UID0/Enforcing, PSI readable,
  stable LMKD PID221. Original monitor parser rejected actual95FDs at its64FD cap;
  preserve those Unavailable results. Separate reviewed max512 followup observed
  stable PSI/epoll links in both15s-separated samples. No APK/workload/L3 acceptance.
- [x] Prepare/review one bounded L3 workload adapter against current source205 and
  the exact owned guest;27 guards Passed, original native predicates unchanged.
  Install exact debug/test/helper APKs only in that guest (12 recorded operations).
  Initial setup stopped at absent-package `pm path` exit1 before any install; its
  result retained. Separate corrected setup verified all installed APK hashes.
- [ ] Prove L3 actual reason3 and locked saved-task/password/cache recovery.
  Reviewed attempt01 NotExercised: warm host16.041s Passed;17 responsive samples,
  MemAvailable626460→95476KiB, helper stopped at96MiB floor, target survived.
  Separate original cleanup Passed (host4.012s), guest namespace empty; independent
  runtime/cleanup audit Passed. Overall NotExercised retained. One changed
  slower-ramp helper/64MiB-floor variant reviewed:34 guards,115 frozen files,
  signed unique helper APK verified; actual owned-guest install5 operations Passed.
  Actual slow attempt01 NotExercised: native warm14.309s Passed;129 responsive
  samples, final60912KiB at64MiB floor, target survived/helper stopped. No exit/cold
  acceptance. Separate cleanup Passed4.011s host, independently reviewed; no further
  pressure escalation or L3 claim.
- [x] Prepare/review a fresh Google38 campaign using14 byte-identical reviewed
  helpers, source205/APKs and unchanged120s/180s assertions. Fresh47+48+41 checks,
  25host files/16dependencies Passed; five templates null and no actual authority.
  Prior37 scope consumed/restoration closed; new UUID and real-time coordination
  metadata only. Independent preapproval audit Passed.
- [x] Obtain new specific38 approval and actual PC-detail/HOME availability, then
  run one bounded core with normal restoration after any actual revocation.
  Actual38 core Failed111.687s native/114.175s host at the unchanged canceled-result
  conjunction. Real revoke acknowledgment/marker reached live120s; actual401/cache
  preservation, one Google request and automatic same-task cancel-only route6884
  observed. Owner explicitly confirmed Continue, not Back; request-bound DidNotCancel
  and aggregate Failed preserved. No G1/G2 credit or exact resultCode inference.
  Separate normal restore01 Passed (host19.805s), original Menu/Test mapping/cache,
  owner/source/APKs exact/native inactive, restorationRequired=false. Independent
  closure audit Passed; marker60.927s after live readiness, request-bound owner
  reply preserved. Restore native18.023s. No further Google revocation/core planned.
- [x] Finish both bounded guest trials and separate native fixture cleanup; slow
  cleanup Passed4.011s host/independent audit Passed, guest namespace empty. Both
  trials remain NotExercised; no further memory-pressure escalation planned.
- [x] Finish owned-VM disposal reconciliation. Original one actual emu-kill exited0,
  then5s host observation timed out: original cleanup Unknown retained. Separate
  read-only absence/source/protected-file audit and existing archive worker Passed:
  DisposedOwnedVmAfterRecoveredAbsence, exact owned Temp root removed, archive SHA256
  5b594194…54305; source205/APKs/118protected files/original AVD and physical owner
  Cookbook exact. No second kill. Independent final source/118protected-file,
  archive/absence and disposal audit Passed. No native/VM operation remains active.
- [x] Independently review the current production candidate and local evidence.
  [Candidate review](reports/candidate-review-20261110.md): no concrete P1/P2 in
  reviewed session/callback/token/clipboard/cache/backup boundaries;214 stable paths,
  151 tests and current APK identities checked. D1 final reconciliation remains open.
- [x] Reconcile10November runtime evidence, independent audits, current phase/plan
  status, owner runbook/spec, roadmap/changelog and mobile task registry. All actual
  grant restorations closed, original owner data/current candidate preserved,
  isolated guest disposed; retain core failures, both NotExercised LMK trials and
  original disposal Unknown alongside separate successful recovery. Whole plan
  remains Partial for G1/G2/L3 and final accepted-candidate integration/delivery.
- [ ] Complete independent candidate/evidence review and scoped delivery; synchronize
  statuses after each result. Keep historical failed/unknown outcomes unchanged.

## Resumed finite closure —9November2026 (Asia/Saigon)

- [x] Reconcile original finite phase03/C09 acceptance against actual reports,
  including full EN/VI200%landscape, spoken observations, clipboard/session timers,
  recovery and Recents. Planner and independent reviewer agree all named criteria
  have evidence. Mark phase03/C09 Done within their stated functional/sampled scope;
  keep historical failures and unmeasured combinations explicit.
- [x] Prepare/review a new Google packet requiring the actual PassChef connection
  detail page ready before native starts; preserve120s/180s assertions and require
  new specific approval before another one-time remote revocation. Prior35 is closed.
  [Provider36 independently reviewed](reports/google-prepositioned-cancellation-20261109.md):
  source205/allAPKs/22host hashes/16dependencies exact;47+46pure checks Passed,
  five actual templates null at preparation; actual operation is recorded below.
- [x] Obtain actual new one-action approval and prepared page/phone readiness,
  then execute G1/G2 once with mandatory normal restoration after any revocation.
  [Actual36](reports/google-prepositioned-cancellation-20261109.md): marker published
  live before120s, real401/external Google observed; owner chose Allow before route.
  Core Failed254.970s; separate normal restore Passed19.830s, obligation false,
  current9names/owner/source/APKs preserved. Independent audits Passed within scope.
- [x] Collect/review one fresh isolated idle emulator LMKD baseline before deciding
  whether any changed controlled-memory experiment is justified. Prior pressure1
  NotExercised/pressure2 Unknown remain unchanged; no blind pressure replay.
  [Actual diagnostic](reports/lmk-idle-diagnostic-20261109.md): initial launch Unknown;
  separately recovered same owned VM, CollectedForReview. Daemon stable/read latency
  bounded; PSI access PermissionDenied, monitoring unobserved. Exact VM disposed,
  owner/source/protected AVD preserved; no target/APK/pressure or L3 acceptance.
- [x] Prepare/review a new same-task automatic local route and explicit Android
  system-Back-on-cue protocol before proposing another G1 attempt; no new authority.
  [Provider37](reports/google-android-back-cancellation-20261109.md):25host hashes,
  16dependencies/source205/APKs exact,47+48+41pure checks independently Passed,
  five actual templates null; native120s/180s assertions unchanged.
- [x] Obtain fresh specific Back-only one-action approval and actual prepared
  PC-page/phone-HOME readiness, then run once and restore after any actual revoke.
  Resumed10November: [actual37](reports/google-android-back-cancellation-20261110.md)
  core Failed; separate normal restore05 Passed and obligation closed. No G1/G2 credit.
- [x] Verify/install one compatible API36 image and its existing license/checksum.
  [Actual SDK dependency](reports/lmk-observable-image-20261109.md) InstalledAndInspected:
  exact revision4/archive size+SHA1 before unzip, measured SHA256, static userdebug/
  debuggable1/API36,85old files/source205 preserved. No VM/phone/workload operation.
- [x] Verify actual owned-guest root/PSI/monitor observation capability before
  deciding whether a changed bounded memory workload is justified. Static build
  properties alone do not accept runtime eligibility or L3.
  Resumed10November: [actual diagnostic/followup](reports/lmk-observable-runtime-20261110.md)
  independently Passed within observation scope. L3 actual recovery remains open.
- [ ] Complete G1/G2 real Google callbacks and L3 genuine OS memory-reclamation
  recovery, then final source/candidate checks and scoped delivery.

## Additional execution —9November2026 (Asia/Saigon)

- [x] Re-read current plan/rules and independently investigate provider34's retained
  failure. Its topology snapshot was taken after native cleanup; it cannot establish
  the foreground task at the owner's Cancel action. No concrete source repair found.
- [x] Obtain current owner readiness: “Có, Xiaomi sẵn sàng”; attached physical device
  observed Awake. Existing source/tests and unrelated workspace changes preserved.
- [x] Run the existing authenticated EN/VI manual Cook/secure-capture case on the
  physical phone at200%font/landscape. Keep native assertions and real biometric
  authentication; verify owner files/APKs and restore original display settings.
  Native Passed11.949s/host14.058s; strict result, owner/source/APKs preserved,
  original display restored. [Independent review Passed](reports/bilingual-secure-landscape-20261109.md). This selected
  case does not certify the full VI source/mapping/layout matrix.
- [x] Add a separate VI offline source/mapping/detail runtime using existing genuine
  setup/cleanup fixture; preserve all existing tests. Compile/review/freeze before
  physical200%font/landscape execution and verify owner/display preservation.
  Initial native run02 Failed10.003s at new fixture's header scroll action after
  genuine setup/Cook succeeded. Owner/source/APKs preserved, display restored.
  Repair only the new header action to direct Click; retain original failed run.
  Separate repaired run02 [Passed61.969s](reports/vietnamese-offline-landscape-20261109.md):
  strict JUnit, source205/original204 preserved, main/release32 exact, new testAPK
  `e59af129…fa412f`; six own200%landscape samples, owner/display preserved/restored.
  Independent runtime review Passed; broader spoken/touch/LMK/Google stay open.
- [x] Freshly inspect current release manifest and every DEX for debug proof/test
  exclusion, permissions and backup boundaries. Static packaging Passed:3DEX/
  25,535class definitions; no own proof/test classes, actual packaged backup XML
  excludes cloud/transfer data. Release APK unchanged; signing/runtime acceptance
  remains separate. Evidence:`2026-11-09-release-package-audit/attempt02/summary.json`
  under the external mobile-evidence directory; [independent package review Passed](reports/release-package-audit-20261109.md).
- [x] Independently review new evidence and update each affected phase/status.
  Bilingual secure11.949s, release package audit and VI offline61.969s each received
  independent Passed review. New fixture's original10.003s failure stays Failed.
- [x] Prepare/review a new bounded Google topology diagnostic before requesting
  any fresh one-action revocation approval. Sample actual component/task/PID while
  native is pending and immediately after the owner reply; earlier post-cleanup
  topology cannot establish the navigation cause. [Packet35 independently Passed](reports/google-topology-diagnostic-20261109.md):
  39provider/46topology pure checks,source205/allAPKs/21host hashes/16dependencies
  exact; four actual templates null, device/native/account calls0. No Google action.
- [x] Obtain specific one-action approval and current readiness for the prepared
  diagnostic. Permission is separate from the successful offline authentication.
  Owner specifically approved “Đồng ý một lượt; chưa xóa; Xiaomi sẵn sàng”. Actual
  fresh preflight Passed.
- [x] Execute the approved core01 once and retain its exact result. Core01 Failed138.641s
  native/142.174s host at unchanged120s action-marker wait after
  real providerA/cache/Cook; host142.174s, owner/source/APKs preserved/native inactive.
  No action reply/record/marker or Google cancellation reached before that deadline.
  Root withdrew the old revoke instruction; original terminal obligation null is
  preserved as that historical snapshot. Independent runtime audit Passed for the
  strict Failed result and preserved state. Later action/restoration resolved below.
- [x] Resolve actual owner-side revocation status and complete mandatory normal
  restoration. Owner actually confirmed deletion after core ended; no stale marker
  published. Restore01 Failed182.301s initial setup/code15, retained; new actual
  immediate readiness and debugger review preceded separate restore02
  Passed43.846s/48.462s. Owner/source/APKs preserved/native inactive; obligation false.
  Independent final closure audit and root owner/display/own-PID audit Passed.
  Successful human Google cancellation/different-Menu remains unproved.

## Current execution checklist —9November2026 (Asia/Saigon)

Status: **Local verification/review Done; remaining physical/Google acceptance Pending**.
Use the10November execution section above for current work; this dated checklist
retains its original outcomes.
Update each item only after its evidence exists. Phases01/02 implementation remains Done;
phase03 finite acceptance is now Done; phases04/05 remain Partial for named G1/G2/L3
and final verification/delivery. Dated checklist entries below retain their scopes.

- [x] Audit README, repository rules, all five phases and latest6November evidence.
  No missing production implementation is identified by the remaining phase TODOs.
  Selected401/restoration, physical saved-task29, long-background functional return,
  EN/VI selected font observations and sampled language grid are already accepted.
- [x] Revalidate current mobile source with fresh root shared/bridge/app JVM tests,
  debug/test/release assembly and lint; count actual XML assertions and preserve
  Golden/vector bytes. Revalidate standalone Menu client independently with JDK21.
  [Actual9November evidence](reports/current-verification-20261109.md): fresh116+35
  tests, zero failures/errors/skips; build Passed. Compilation/lint analysis reused,
  lint0errors/7warnings/1hint; all3 APKs match32.207 sources stable; protected tests exact.
- [x] Identify available Android runtimes without changing owner data/settings:
  Xiaomi/API36 connected, actual power state Dozing, debug process absent. Existing
  SDK32 retry02 preparation matches all204 frozen files and3 current APKs; no native
  run started and no current installed-byte acceptance inferred from build outputs.
- [x] Before selected native execution, verify fresh actual owner availability,
  awake/unlocked foreground and installed APK identities against the frozen source.
  Owner confirmed immediate authentication/stay-in-app/PC chat. All204 frozen paths,
  installed debug/test APKs and original owner inventory passed preflight9November.
- [x] Execute the prepared final unchanged pre-SDK retry02 and complete its normal
  reconnect obligation. Core01 NotExercised129.990s host: genuine setup/provider read/
  cache/Cook and local SDK clear succeeded, but normal Connect silently reused grant.
  Separate reconnect01 Passed21.421s host; normalReconnectRequired=false, exact
  owner/fixture preservation and native inactivity independently reviewed Passed.
  [Actual results](reports/google-local-sdk-runtime-20261109.md); human cancellation remains NotExercised.
- [x] Execute unchanged full offline Cookbook UI on physical Xiaomi at200%font/
  landscape, with source/APK/owner preservation, actual own app configuration proof
  and exact stable display restoration. Separate from spoken/full-layout acceptance.
  Run01 Unknown: host's second exclusive config-sample write failed; detached
  native later logged1failure at setup120s/code15. Original owner/source/APKs exact,
  native inactive and original display restored with5continuous seconds of matching
  samples. Owner reports immediate successful first authentication; cause unknown.
  Host-only sample-write repair reviewed before one unchanged-native run02.
  Run02 Passed57.549s native/60.248s host, strict[1,0]/[-1]/OK1; own200%landscape
  configuration sampled, owner/source/APKs preserved, display restored. Runtime
  review Passed; [scope/history](reports/large-landscape-runtime-20261109.md).
- [x] Complete original finite phase03 phone coverage: Cookbook/source/mapping/detail,
  EN/VI sampled accessibility/layout, clipboard lifecycle, recovery/timers/Recents.
  Independent9November reconciliation found each original criterion has accepted
  evidence. No every-control/configuration matrix is inferred. Named L3 OS-LMK is
  tracked separately below and remains required; prior scoped results stay intact.
- [ ] Complete phase04 genuine human Google cancellation/resultCode with separate
  actual-owner action evidence and preserved offline Menu/jars; exercise provider-
  loaded cancellation and different-Menu stale callbacks under real conditions.
  Silent grant reuse stays NotExercised. Existing401/403 acceptance stays accepted.
- [x] Prepare/review a new provider33 campaign against unchanged source204/APK32.
  Owner currently sees PassChef on the PC connections page and has not revoked.
  New packet must not reuse provider31 approval; preserve its earlier failed/no-action case.
  Independent39 pure checks/16host hashes/14dependencies Passed; actual approval absent.
- [x] Obtain specific owner approval before provider33's one real connection
  revocation, then run only after fresh physical readiness/live retained-A checkpoint.
  [Concrete scope and status](reports/google-provider33-runtime-20261109.md).
  Actual approved revocation completed only at live retained-A. Core01 Failed106.553s
  native: owner confirms pressing Continue/Allow before route selection; no human
  Cancel proof. Separate normal restore01 Passed18.313s native; obligation false,
  owner/source/APKs preserved. This one-action scope is consumed; no automatic retry.
  Independent runtime review Passed; original Failed outcome retained.
- [x] Review prepared provider34 before requesting new specific one-action approval.
  New factual owner Continue/Allow reply explains the prior mismatch; unchanged
  native source/APKs,39 pure preparation checks and no actual records copied.
  Explicit hand-Cancel instruction precedes owner action in the new case.
  Independent39 pure checks/source/APK/host/dependency audit Passed; templates null.
- [x] Obtain provider34 specific approval and execute one bounded real cancellation
  attempt with mandatory normal restoration; preserve every earlier outcome.
  Core Failed316.896s native despite ready-bound owner HandCanceled reply: actual
  return/consumption was not observed within180s, terminal Pending=true. No Cancel
  acceptance. One extra empty proof directory made original preserved=false; original
  owner regular file stayed exact, separate inactive/rmdir-only recovery restored
  names. Restore01 Passed41.291s native; obligation false. Runtime review Passed.
  [Actual scope/results](reports/google-provider34-runtime-20261109.md).
- [ ] Investigate reproduced setup failures before another affected provider run.
  Latest unchanged SDK32/pre-SDK retry failed during initial Cookbook setup with
  observed code15; neither dispatched local SDK clearing nor created reconnect
  obligations. Earlier normal SDK restoration Passed. No new remote revocation is
  implied; follow actual immediate authentication prompts and retain unknown causes.
- [x] Independently review current ownership/auth/clipboard implementation and fresh
  local verification; no concrete P1/P2 found in reviewed scope. No source repair.
  Final evidence review Passed:28 copied XML files, fresh task logs,207 source hashes,
  baseline audit and copied/current APK identities checked independently.
- [x] Synchronize this checklist, phase statuses, plan, acceptance runbook, product
  spec, roadmap, changelog and parent task registry with demonstrated results.
  Correct stale Pending summaries for accepted401, grid and selected task/background
  cases while retaining original failed attempts and broader acceptance limits.
- [x] Independently review all new native evidence produced in this continuation
  and update affected status documents after each demonstrated completion.
  Actual9November localSDK/UI/provider33/provider34 runtime evidence has now been
  independently reviewed; native failures remain Failed, restores Passed. No source
  repair or successful human callback/different-Menu claim is inferred.
  Final root audit confirms original one owner regular file/eight names exact,
  source/APKs unchanged, native inactive/own PID absent, restorationRequired=false,
  font1.1/userRotation0/autoRotation1/WMfree/fixed-default restored. No further native
  or Google action is running; whole plan remains Partial for the unchecked gates.
- [ ] Finish required owner/device acceptance before final plan integration/delivery.
  Historical scoped main delivery `2748d0b` is Done; it does not certify current WIP
  runtime or release acceptance. Preserve unrelated extension/site work.

Completed autonomous work: fresh mobile checks, protected-byte audit, source review,
read-only runtime discovery and evidence-backed status reconciliation. Device/owner-dependent work: genuine authentication,
spoken/layout observations and real Google resolution/action; no fabricated native
reply, forced remote grant change or modified Golden assertion substitutes.

Unresolved: awake/unlocked runtime and fresh owner participation for the remaining
physical/Google cases; genuine external Google resolution; natural LMK; historical
setup/UI/wrap causes. See [latest actual evidence](reports/nov6-resume-runtime-20261106.md).

6November continuation ran on source195/candidate27 (production26 unchanged).
[Current runtime evidence](reports/nov6-resume-runtime-20261106.md) records actual
human/grid/font/physicalL1 results separately from preparation and earlier acceptance.
Owner resumed Google and200%/landscape; bilingual hearing/task restoration stay open.

4November: [proposal/limits/capacity UI](reports/cache-capacity-status-20261104.md)
and [API26](reports/api26-clipboard-runtime-20261104.md)/[API27](reports/api27-clipboard-runtime-20261104.md)
legacy clipboard4+4 independently Passed. Valid21st cache-save classification
repaired after a real20-file regression; fresh app JVM11 Passed. Candidate20/source189
reviewed; exact VMs/processes/serials/temp data cleaned. Selected physical TalkBack,
authenticated recreation and controlled emulator saved-task evidence below also
Passed. Broader physical matrix, genuine Google conditions and historical causes
remain owner-dependent; whole phase stays Partial.

Earlier4November Google preparation: native/source192/candidate23 built, independently
reviewed and installed with original owner inventory exact. Human runner04
reviewed/52 pure checks Passed;401 host01 reviewed/54 pure checks Passed. Final
same-account PassChef revoke/reconnect approval and fresh owner readiness received;
selected401 Run01 independently Passed105.047s with actual approved revocation,
exact preservation and fresh cached Cook. Restoration01 Failed11.014s at first
Connect; diagnostic recovery01 proved metadata401. Rejected-token repair26/JVM16
and subsequent restoration03 independently Passed33.115s; owner inventory/cleanup
verified. Original failures retained; human-cancellation runtime remains Pending.

Earlier21October [actual401 preparation](reports/google-http401-preparation.md): host
checkpoint/archive/raw-integrity audit Passed; no new device/build/test acceptance.
Permitted401 transition, reviewed native candidate and fresh owner readiness Pending.
Mobile stays Partial; historical outcomes retained.

Earlier21October: [provider-loaded403 + fresh cached Cook Run01](reports/google-provider403-preparation.md#actual-provider-loaded-http403-run01--passed21902s)
Passed21.902s; exact pre-Cook cache/jars/RAM preservation and fresh cached Cook,
independent runtime/owner/UUID/source/APK/history audits Passed. Mobile Partial.

Status: **Partial — local verification/delivery Done; owner acceptance Pending**. Priority:5.
Earlier21October: [actual host OS-Back/stale Google return](reports/google-cancellation-preparation.md#actual-host-android-system-back-run01--passed)
Passed17.949s,[1,0]/[-1]/OK1 on unchanged source86/APKs. Actual Google pending/lock/
Back/consumed callback, secure locked return and synthetic cache/jars/owner preserved;
independent runtime review Passed. Human cancellation/resultCode/401/different-Menu
scope Pending; prior manual Failed01/02 and later owner close report retained.
Owner/device acceptance below remains Pending.
Current [real session timer continuation](reports/session-timer-runtime.md): three
selective test-APK/lint builds Passed; idle Run01/02 pointer failures and Run03 setup
failure retained; Run04 setup Failed. Idle Run05 Passed1/1,341.722s; Total04 on14October
Passed1/1,609.969s, runner[1,0]/OK1,0 failures/errors/skips. Original ten-minute deadline
locked despite minute1–9 real touches/Cooks; Dish/session/Copy cleared and jars/digest
preserved. Same frozen03/APKs, no rebuild/install or fresh JVM. Selected5/10-minute
API36 foreground KEEP_SCREEN_ON accepted; all four idle/three total failures retained.
Selected [cache UI Run02](reports/google-cache-ui-runtime.md) Passed1/1,42.393s;
broader device/provider matrix remains Pending, whole phase Partial.
Latest [network-off Run04](reports/network-off-runtime.md) Passed1/1,31.477s:
actual offline UI/Cook/recreation and NETWORK_FAILED preservation;401/403/cancel,
process death/cold start/broader timer UX/UI and Run03 key-wrap cause remain Pending.
[Security/UI continuation](reports/security-ui-runtime.md): locked pixels01
Passed1/1,2.321s and authenticated02 Passed1/1,25.890s. Selected EN/VI setup/manual
Cook and actual secure shell display exclusion accepted on API36 foreground proof;
Locked01/Capture01/Auth01 retained Failed; Recents01 Failed206.052s without actual
post-checkpoint stop/resume, Recents02 Failed182.457s at setup before navigation.
Run03 Passed1/1,137.789s with same-Activity pause/stop/locked read and genuine reopening;
owner confirmed own card blank/covered. Same frozen04/APKs, no rebuild/install between
Recents attempts; earlier causes unconfirmed.
Six scoped test-APK/lint builds Passed; final06:42s,10 executed/84 UP-TO-DATE,
no production-source changes or fresh JVM/browser assertions. OS screenshot shortcut/
full UI/accessibility remain Pending. [Offline cold-process](reports/cold-process-runtime.md)
warm02 InterruptedByVerifiedSIGKILL and cold02 Passed1/1,5.993s,PID25682→25991:
initial locked/cleared state, genuine biometric reload/exact cache/Cook/jars preserved.
Build01/02 import/type failures retained; corrected build03 Passed54s13/146,
lint0/11/1 and release proof exclusion. Five new files and one intentional debug
manifest entry only; historical49 means48 unchanged+entry delta, protected208/23 intact.
Provider [cold02](reports/google-cold-runtime.md) Passed1/1,8.886s after verified
warm02 SIGKILL/PID13092→14408; real fingerprint/exact cache/Cook/jars/owned cleanup
preserved. Cold01 Failed181.588s reopening/cause unknown; diagnostic-only build03
Passed58s10/101, old54/17 assertions preserved, no production repair claim.
[VI OS-field01](reports/vi-accessibility-runtime.md) Passed1/1,1.5s, one own password
node with label descendants; prior same-node assertion remains Failed. Source59/
testb684/build55s9/102/lint0/11/1; no auth/capture/Google replay. TalkBack/LMK/task
restore/provider errors/full matrix Pending; whole mobile remains Partial.
[Language touch](reports/language-touch-runtime.md): center02 Passed1.852s, edge01/02
Failed1.408s/1.211s, no whole48dp target credit; cause unknown. Dwell04 built/reviewed
43s9/102/lint0/11/1/source60/testce61; USB initially empty14October. Reconnected15October:
exact candidate installed-r, dwell04 Failed1.709s/center04 Passed1.802s. Earlier
source/APK/failures retained; old59 unchanged. No production change/clock/input bypass.
Later geometry01 Passed1.505s, observed scroll coverage0. Paired pointer-kind finger01
Passed2.464s/source63/helper10/test9a64: UNKNOWN retained localized UI/FINGER switched
EN→VI→EN at equal guarded expanded midpoints/configured60ms, baseline constructor
Unknown/readback checked. New samples accepted; old failures/full-target limits retained.
[Manual shortcut](reports/manual-shortcut-runtime.md) Run03 Failed182.485s setup/
current wrap15; same-source/APK04 reached genuine secure masked Cook then Failed
236.235s owner-response completion wait. No observed reveal/stop/signal; hardware
shortcut Pending. Source64/helper12/test19b9/owner/exact UUID cleanup preserved.
Late04 missing-Reveal reply retained after terminal. Same-candidate05 genuine ready,
Failed188.013s completion wait/no sampled reveal/stop. Current screen requested before
further timed runtime; no production screenshot/owner-action cause inferred.
Run06 subsequently Failed193.721s completion wait despite a live owner reply/model
reveal; isolated hardlink permission rejection diagnosed, later portable publication
probe passed separately. New `ManualVisualPhysicalTest` build21s/lint0/11/1 and
actual07 Passed1/1,116.736s [1,0]/OK1:
normal Compose idle plus actual text/layout booleans is a materially new diagnostic,
not a fix or replay credited to original Run06. Old64 sources/assertions/deadlines
remain; human screenshot/visual evidence and JUnit checkpoint stay separate. Owner07
confirms Dish text then blocked Power+VolumeDown; selected hardware shortcut accepted.
Live publisher rc0; final owner/exact UUID cleanup/source66/helpers/protected208/23/
releaseefd3 preserved. Whole mobile Partial; prior failures and exact06cause retained.

## Unattended priority continuation —15October

[Unattended source coverage](reports/unattended-source-coverage.md) Passed: six new real-coordinator cases plus six common
stream-cancellation cases. Fresh app10/bridge22/standalone35, zero failures/errors/
skips; root9s/standalone20s/lint0/11/1. Common22 overlap across the two client roots.
All old code/Golden/assertions/source66/protected208/unrelated23/APKs unchanged;
no device interaction or new provider acceptance. Current stale summary statuses
corrected; broader phone/provider matrix and historical causes remain Pending.

## Context and requirements

[Google cancellation preparation](reports/google-cancellation-preparation.md): two new
selected fixture files preserve all68 prior app source/manifests and existing assertions.
Final compile/lint Passed26s10/84,lint0/11/1; frozen70/test669a8c9c, debug/release unchanged.
Natural-stop/pending-result gates independently reviewed; no fresh JVM/browser runs.
Actual external cancellation/stale acceptance remains Pending. After owner readiness,
selected Run01 NotExercised15.608s with1skip: silent SDK authorization/metadata return,
no observed external resolution. Exact cache/jars/owner/cleanup preserved; no new
cancellation/stale acceptance inferred from this completed skipped attempt.

[Overview](plan.md), [Core](../../docs/specs/mobile-core.md),
[device runbook](../../docs/runbooks/android-keystore-device-proof.md).
Prior Core/Keystore acceptance proves the foundation, not new Cookbook UI/OAuth.
Preserve original Golden/Unicode/vector files and unrelated Cave/skills-lock WIP.
Build/test source must be frozen before independent review and final validation.

## Autonomous local commands

From repo root, current installed JDK17 and SDK36:

```sh
export JAVA_HOME=/home/vantt/dev-tools/jdk-17.0.20+8
export ANDROID_HOME=/home/vantt/dev-tools/android-sdk
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew --no-daemon :shared:jvmTest :shared:assemble \
  :sheets-android:jvmTest :sheets-android:assemble :app:testDebugUnitTest \
  :app:assembleDebug :app:assembleDebugAndroidTest :app:assembleRelease :app:lintDebug
```

Use `--rerun-tasks` for the final independent run when fresh assertions are needed;
count XML tests/failures/errors/skips rather than treating UP-TO-DATE as new proof.
The root bridge has Android/JVM targets and reuses canonical portable client tests.
App unit tests exercise the actual source coordinator with deferred IO and real files.

Preserve standalone Menu client, using its existing local JDK21:

```sh
export JAVA_HOME=/home/vantt/projects/passMan/.toolchain/jdk
export PATH="$JAVA_HOME/bin:$PATH"
cd mobile/sheets-client
./gradlew --no-daemon build test
```

No live smoke command belongs to unattended tests. No emulator executable/runtime
is established. `assembleDebugAndroidTest` compiles tests; it does not run them.

## Implementation / verification TODO

- [x] Capture baseline source/Golden hashes in [baseline](reports/baseline-hashes.json);
  protect existing phone data, `.agents/` and `skills-lock.json` from scoped staging.
- [x] Author meaningful pure cache/mapping/search/session/reveal/clipboard policies
  and HTTP/auth callback regressions as described in phases01–04; actual engine/UI
  wiring must exist rather than shipping fixture-only replacements.
- [x] Independently review key ownership, source/Menu binding, atomic cache update,
  token lifecycle, stale results, permissions, masking/copy and app foreground rules.
- [x] Freeze source, run relevant local commands once final, report actual XML counts,
  lint result/warnings, debug/test/release IDs, APK hashes and unchanged Golden bytes.
- [x] Inspect release manifest/no backup/FLAG_SECURE and absence of debug proof/logging;
  no camera/Autofill/Drive permissions introduced for this bounded read/cook slice.
- [x] Record compiled Android test matrix separately from tests actually executed;
  keep no-device status visible in `reports/verification.md`.
- [x] Update product specs/roadmap and independent report with passed/failed/Pending
  columns. Overall mobile roadmap Phase1 remains Partial while the broader live/UI matrix is Pending.
- [x] Reviewed scoped Mobile delivery fast-forward pushed to main `2748d0b` on6November;
  remote HEAD readback matched. Required runtime gates remain; no archive/release claim.

## Owner/device acceptance — Partial

[Candidate and Android Google checklist](../../docs/runbooks/android-cookbook-acceptance.md).
Installation/digest checks and8 selected platform tests passed. Actual owner biometric
UI Run01 failed after reveal; Run02 failed after owner-confirmed interruption,
Run03 failed before setup; Run04 passed selected unchanged foreground flow. Run01
cause remains not reproduced; normal-policy Run05 passed. GoogleRun03 and cached
UI/Cook/authenticated same-process recreation Run02 Passed; wider acceptance Pending. The
[unattended continuation](reports/unattended-regression.md) adds shared78/app4 and
compiled diagnostics; it does not resolve the physical failure.

Remaining owner/device checks, after the unattended follow-up:

1. Human Google cancellation/resultCode and different-Menu/provider-loaded stale callbacks.
   Physical401 and usable grant restoration accepted4November; original failed recovery
   outcomes retained separately from successful repair26/restoration03.
   Selected provider-loaded403 preservation and fresh cached Cook Passed21.902s.
   Selected actual host OS-Back/stale reply after natural lock Passed17.949s.
   Selected grant/read/mapping/cache, actual network failure and controlled cold passed.
2. Broader physical mapping/layout remains; selected wrong-Menu tagged Recipe and irregular
   explicit mapping [Run01 Passed36.892s](reports/menu-mapping-runtime.md), plus four
   [actual emulator proposal/limits/capacity cases](reports/cache-capacity-status-20261104.md)
   now independently Passed. No physical breadth inferred from those new cases.
3. Long-background/natural LMK/saved-task restoration; authenticated recreation/
   process-death/cross-app clipboard. Selected owner-Copy35s screen-off/same-Activity
   legal-return clipboard observation [Passed121.836s](reports/owner-screen-off-clipboard-runtime.md).
   [Chrome diagnostics/focus repair](reports/cross-app-clipboard-runtime.md): actual
   replacement intact but expired claim stale; reviewed repair JVM10/adapter3+
   recreation1 Passed. Strict repair01 setupFailed before Copy; October16 run02
   terminal unknown after loss of old temporary evidence. October20 recovery/tail01
   Failed; actual public-marker probe confirmed null-label NPE. Minimal
   [null-safe repair](reports/clipboard-null-label-repair-runtime.md) build/JVM10
   and relevant physical5 Passed; unchanged strict Chrome return01 Passed99.870s.
   Owner reported proof close, then confirmed normal app stable; prior close cause unknown.
   [Resume](reports/clipboard-return-handoff.md).
   Later [authenticated physical recreation](reports/authenticated-recreation-runtime-20261104.md)
   and [controlled emulator saved-task/password restoration](reports/saved-task-runtime-20261104.md)
   Passed; natural LMK and broader physical saved-task/biometric scope remain open.
4. Larger fonts/orientation and full language touch-target coverage. Selected physical
   [TalkBack labels/focus/masking](reports/talkback-runtime-20261104.md) Passed.
   Secure shell capture/Recents/owner revealed-Dish shortcut07 already accepted.
5. Historical UI/setup/wrap causes remain open. Additional Keystore API/device cases
   are separately tracked; accepted current-Xiaomi scope is not reopened.

[Screen-off/clipboard](reports/screen-off-clipboard-runtime.md) selected01/02 Failed
37.492s/54.316s at copy+25s predeadline stop/noninteractive gate; no hold/return stages.
Both native0 but JUnit[1,-2]/[-1]/FAILURES; owner/source72/old71/helper28/protected208/23/
UUID cleanup retained. Owner02 response arrived after terminal, exact key time unknown.
No production correction or new background clipboard/lifecycle acceptance inferred.
Separate owner-controlled diagnostic01 later Passed121.836s with actual single-Copy
deadline/generation, sampled35s screen-off and legal locked return/clipboard absence.
See [accepted bounded scope](reports/owner-screen-off-clipboard-runtime.md); original
Failed01–03 remain Failed, broader matrix stays Pending and Mobile stays Partial.

Historical cache continuation build88s Passed fresh app4; shared78/bridge16/standalone29 retained.
Helper-only sync build40s Passed, lint0/11/1. Default storage recreation Passed1/1,
31.769s; cache UI Run01 Failed1/1,294.651s remains retained, corrected Run02 Passed1/1,
42.393s. Owner digest preserved. No production fix, actual network-off/process-death/
cold-start/cancellation/401/403/timer/UI matrix claim; no new commit claimed here.
Network continuation later build55s and diagnostic81s Passed; app4/shared78/bridge16/
standalone29 retained (no fresh JVM reruns). Network Run01/02/03 Failed history
retained; identical Run02/03 source/APKs Run04 Passed1/1,31.477s with strict actual
network absence and preserved cache/jars. No build/install repeated for Run03/04.

## Success and remaining dependencies

Code-complete means real offline/connection code + independent JVM/build/lint proof,
with owner-dependent acceptance explicitly Pending. Product acceptance additionally
requires the relevant real phone/OAuth evidence. QR camera/receiver, Autofill,
Drive/multi-profile and account authoring remain later plans, not overnight promises.


## Actual HTTP403 preparation —21October2026

[Selected candidate preparation](reports/google-http403-preparation.md) adds a separate UUID-owned genuine
biometric/production-provider attempt with synthetic offline cache preservation.
Final compile/JVM app10+bridge22/lint0errors and independent source/host review
Passed;43 host classifier cases do not certify Google. No new runtime/install;
denied owner-authorized test Menu URL and fresh readiness remain Pending.
Prior failures and selected system-Back acceptance retained; Mobile Partial.


## Actual HTTP403 Run01 —21October2026

[Selected runtime](reports/google-http403-preparation.md#actual-selected-run01--failed203052s) Failed203.052s,
selected[1,-2]/terminal[-1]/native0; pending external request and natural locked stop,
no actual403 or accepted return gate. Exact cause unpreserved by generic wrapper.
Owner/UUID cleanup/source/APK/history audits Passed. Owner later clicked webpage
Back to Safety and returned to Chrome/help; prior Google message Unknown.
No Android Back/Sheets403 credit; no active runtime or automatic unchanged retry.
Denied Menu supplied; authorization-page diagnosis and actual403 remain Pending.


## HTTP403 continuation Run02/03 —21October2026

[Runtime record](reports/google-http403-preparation.md): Run02 Failed234.38s, unresolved external return;
Run03 NotExercised17.223s, actual metadata returned, no403. Exact synthetic cache/
jars and stale Dish assertions retained; owner/cleanup/APK/source audits Passed.
Owner saw no account chooser; account identity Unknown. Owner close report is
separate from fixture terminal/normal Activity recovery. No active wait/retry;
identify actual Google account and inaccessible test Menu before next403 attempt.


## Actual Google HTTP403 Run04 — Passed27.530s

[Accepted selected runtime](reports/google-http403-preparation.md#actual-google-http403-run04--passed27530s):
real production metadata403→ACCESS_DENIED,selected[1,0]/[-1]/OK1, secure ready
foreground/RAM grant retained, no stop. Exact synthetic offline cache/jars/RAM
preserved; stale Dish/Copy absent; owner/UUID/APK/source/history audits Passed.
No provider-loaded cache or fresh post403 Cook/401/cancel acceptance. Earlier
Failed01/02 and NotExercised03 retained; no active wait. Mobile Partial.

## Provider-loaded HTTP403 delivery gates —21October2026 continuation

Status: **Run01 Passed21.902s; Mobile Partial**. Follow the
[phase04 continuation](phase-04-android-sheets-connection.md#provider-loaded-http403-and-fresh-cached-cook-21october2026-continuation).

- [x] Prepare separate selected fixture/provider observer and minimal shared UI
  reuse; preserve every old assertion. Independent source review before runtime.
- [x] Root-only compile/lint and relevant local checks; freeze fresh source/APK/
  helper identities and candidate archive under a new persistent evidence root.
  Historical frozen/candidate/helper/raw/result files remain immutable.
- [x] Host-only adversarial checks require selected JUnit start/end, unique terminal,
  anchored OK summary, UUID/phase binding, watchdog and failure handling. Require
  both pre-Cook403 preservation and fresh cached-Cook checkpoints; native0 alone
  cannot pass. Review host runner before any owner phone request.
- [x] After candidate build/review, obtain fresh owner readiness and root device
  serial/awake/keyguard/installed identities; preserve owner digest, source/APK/
  archive/history and preexisting directories. Run only the selected new case.
- [x] Genuine biometric setup completed during the live run before the next poll;
  no further biometric/Google checkpoint awaited guidance. No external Google page
  observed. Retain immediate poll-before-guidance and yield-at-live-checkpoint
  rules for future attempts; natural lock/stale callback policy remains unchanged.
- [x] Independently review raw runtime, both checkpoints, owner digest and exact
  UUID cleanup; classify Passed/Failed/NotExercised/Unknown by observed evidence.
- [x] Update acceptance reports/docs with bounded provider-loaded403 and new Cook
  results, retaining historical limits and Mobile Partial; commit/push authorized
  Mobile changes only after verification. No Done/archive or unrelated staging.

Missing schema/readiness blocks only dependent runtime; build/review can continue.
No broader401/cancellation/device acceptance inferred from this selected result.

[Actual Run01](reports/google-provider403-preparation.md#actual-provider-loaded-http403-run01--passed21902s):
selected[1,0]/[-1]/OK1; real provider baseline, exact pre-Cook403 preservation,
fresh cached Cook, owner/UUID/source/APK/history/archive audits Passed. No new
401/cancellation/broader matrix credit; historical outcomes retained. No active wait.

## Current-tree continuation —4November2026 (Asia/Saigon)

Status: **Local continuation Done; owner acceptance Partial**. Preserve the existing five phases and
every historical verdict. Current cross-platform UI changes are preexisting WIP;
verify the working tree without reverting or staging unrelated work.

- [x] Reconcile remaining deliverables: phases01/02 and phase03/04 implementation
  already Done locally; selected provider-loaded403 + fresh cached Cook accepted.
  Remaining acceptance is listed below, not a new Recipe/kitchen implementation.
- [x] Complete fresh root shared/bridge/app JVM assertions, debug/test/release
  assembly and lint: Passed335.520s,182 executed tasks; shared78/bridge22/app10,
  zero failures/errors/skips; lint0errors/10warnings/1hint. APK identities recorded
  in [current verification](reports/completion-verification-20261104.md).
- [x] Independently build/test the standalone JVM21 Menu client: Passed100.399s,
  fresh35 tests, zero failures/errors/skips. Common cases overlap with the bridge.
- [x] Verify protected Golden/vector/fixture bytes:54 baseline tests/fixtures and
  all170 frozen mobile paths unchanged. No phone/owner data was accessed; original
  runtime evidence and unrelated WIP retained.
- [x] Boot the separate disposable API36.1 `PassChefCookbook36`/port5580 and run
  existing locked-only diagnostics: selected JUnit1/1 Passed29.416s, actual stop/
  resume with no authentication or encrypted Cookbook. Exact frozen APKs installed;
  original Windows AVD and physical acceptance scopes preserved.
- [x] Execute a real password-only Cookbook UI flow on that isolated emulator;
  keep Google/account/biometric flows outside this unattended run and record
  emulator scope separately from physical-device evidence: unchanged Run02
  Passed111.247s native/116.292s host, selected JUnit1/1; all original assertions.
- [x] Preserve unchanged full-UI Run01 failure before input: selected JUnit[1,-2],
  old Espresso3.5.0 calls removed `InputManager.getInstance` on API36.1.
  Official Espresso3.7.0 release notes identify the upstream correction.
- [x] Pin compatible stable AndroidX instrumentation dependencies; source review
  Passed, compile/app APK/lint Build02 Passed194.447s,24/164 tasks executed.
  Freeze candidate02: only testAPK changed; debug/release retain original bytes.
  Preserve original APKs, native failure, all test assertions and Golden bytes.
- [x] Rerun unchanged selected UI against reviewed candidate02: Run02 Passed;
  old Run01 remains Failed. Corrected Espresso path now executes complete original
  setup/cache/Cook/reveal/clipboard/background assertions on the isolated emulator.
- [x] Record selected VI OS-field observation Passed20.781s and EN/VI secure pixels
  Passed12.496s; latter binds actual own Activity font2.0/landscape2400x1080 and
  excluded pixels. These do not establish full physical layout/spoken TalkBack.
- [x] Independently review source/artifacts and all selected native results; fix
  the demonstrated instrumentation compatibility failure, preserve old outcomes,
  and synchronize plan/spec/roadmap/changelog/acceptance evidence. No overall Done.
- [x] Close exact owned emulator, observe launcher exit0/QEMU PID and serial absent,
  verify synthetic noBackup directory empty, remove only task-created temporary
  AVD data and retain definitions/APKs/logs/reviews. Original Windows AVD retained;
  no physical phone currently attached. [Cleanup evidence](reports/completion-verification-20261104.md).
- [ ] With an available authorized phone/owner, finish remaining physical
  TalkBack/larger-font/orientation/language-touch coverage, natural LMK/saved-task
  restoration and broader authenticated clipboard lifecycle acceptance.
- [ ] With genuine provider conditions and owner readiness, exercise human Google
  cancellation/resultCode, different-Menu stale callback and actual401 preservation.
  Existing403 and cold/SIGKILL passes do not substitute for these cases.
- [ ] Retain unresolved historical UI/setup/wrap causes until their investigations
  have evidence; the separate Espresso correction does not explain earlier runs.

The earlier no-emulator statement describes its historical environment. Selected
Windows API36.1/WHPX boot/runtime evidence is now recorded above; fresh resource
readiness is required for any later runtime after the owned instance was removed.
The phone was absent at that cleanup; later owner-ready Xiaomi evidence appears
in the authenticated continuation below. Keep
phase03/04/05 Partial until their remaining required acceptance has real evidence.

Unresolved: fresh owner readiness per later run; genuine Google401 trigger and
cancellation/stale-callback conditions; remaining physical accessibility/lifecycle
matrix and historical failure causes. No emulator result closes those requirements.

## Authenticated lifecycle continuation —4November2026

Status: **Selected emulator Run01 and physical Run04 Passed; broader acceptance Pending**. Existing local/emulator passes
above remain complete; prioritize the uncovered authenticated clipboard boundary.

- [x] Audit existing fixtures: `ClipboardRecreationPhysicalTest` creates no
  encrypted Cookbook; `GoogleCacheUiContinuation` authenticates/recreates but has
  no clipboard assertion. `ColdStartProofActivity` rejects saved instance state
  and its launcher clears the task, so cold/SIGKILL proof is not saved-task proof.
- [x] Add a separate selected test using genuine password setup/reopening,
  with actual owner biometric prompts when available (password-only on emulator),
  confirmed offline Menu/cache and cached Cook, then actual UI Copy. Set
  `CookbookProofActivity.retainProofStorage=true` before real Activity recreation;
  require a different destroyed/replacement Activity, identical owned directory,
  locked/cleared session/Dish/Copy and unchanged encrypted jars/cache. Verify the
  original sensitive clipboard marker/content before30s and cleanup after the
  original deadline using real monotonic time; then genuinely reopen by password
  and Cook the cached row again. No session injection, timer reset or clock bypass.
  Two additive files compiled/reviewed; cleanup/old-controller proof corrections
  completed before final Build04 Passed2m33s. All old assertions unchanged.
- [x] Independently review/compile/freeze that new test and run only its selector
  on a fresh isolated emulator; retain all previous assertions/results and record
  actual JUnit/artifact/cleanup: [Run01](reports/authenticated-recreation-runtime-20261104.md)
  Passed125.698s native/137.635s host, exact selected1/1; same original deadline/
  generation, locked old/new controllers, genuine password reopen/cached Cook.
  Exact UUID cleanup/source173 and installed APK identities independently reviewed.
- [x] Run the same frozen candidate04 on Xiaomi/API36 after real OS unlock and
  actual biometric setup/reopening: physical Run04 Passed116.477s host, selected1/1.
  Original deadline1461316489/gen1 survived recreation, clear observed+557ms;
  old owner Cookbook894f73…a4d6/all protected files/old six proof directories
  preserved and current UUID removed. Temporary install-only screen policy
  restored0 before Copy; Prep01–03 launched no native case. Independent audit next.
- [x] Complete selected controlled emulator saved-task/password proof: record real stop/save-instance/task ID
  and owned UUID before verified background process death; restore the existing
  system task with nonnull saved state and a new PID, not `CLEAR_TASK`, a new
  cold intent or same-process `ActivityScenario.recreate()`. Require initial lock,
  exact disk preservation and genuine password reopen/cached Cook. Establish the
  host/observer route before runtime; instrumentation restart must not replace the
  task being proved. Keep natural LMK/physical biometric acceptance Pending.

Process-death clipboard timing/removal stays outside the documented guarantee;
do not add a failing requirement to persist Dish content or clear unknown copies.
Unresolved: natural LMK/physical task matrix; broader physical owner/Google
conditions above. Selected authenticated clipboard recreation is now evidenced.

## Saved-task host route —4November2026 preparation

Status: **Selected controlled emulator saved-task/password runtime Passed**. No new module or production
state persistence. Use only a disposable emulator and UUID-owned synthetic data.

- [x] Consult primary AOSP/AndroidX sources: `am instrument --no-restart` and
  `am task focus TASK_ID` are parsed despite omissions from shell help. The former
  needs an already-running target process and skips instrumentation force-stop;
  the latter focuses an existing attached task, not an arbitrary recent-only one.
- [x] Prove flag support with a bounded selected probe: bootstrap the ordinary
  locked app process, attach `--no-restart`, and verify unchanged PID/task on exit.
  Pass `-e waitForActivitiesToComplete false` so the standard AndroidX finisher
  does not close the stopped proof Activity. Check actual lifecycle/task records;
  a successful shell exit alone does not establish support or preservation.
- [x] Prepare one selected warm test: launch the isolated `CookbookProofActivity`
  and use native accessibility actions to genuinely set up/password-open,
  confirm offline cache/Cook, enable UUID-only saved-state retention, and freeze
  jars/cache/task/PID. Use real Home; require actual stop, lock and saved-state
  callback. Finish warm instrumentation without closing that retained scenario;
  require instrumentation absent, app still stopped and the same task present.
  Source/build acceptance only here; runtime assertions remain required below.
- [x] Kill only the verified background target process while no instrumentation
  is active; require old PID absent and original task retained. Focus that task
  by ID, or select its actual Recents card if it is no longer attached. Never
  substitute a fresh launch, force-stop, `CLEAR_TASK` or task-lock mode.
- [x] Implement a debug-only passive lifecycle journal for original stop/save and
  restored create/resume: owned UUID, task ID, PID, nonnull saved-state flag and
  locked/session/Dish booleans only. Assert same task/UUID/storage and new PID,
  exact encrypted jars/cache and cleared authentication before any new unlock.
- [x] Attach a cold selected `--no-restart` observer after the restored process
  exists; preserve its PID/task. Use existing native `UiAutomation` accessibility
  actions for real password entry and cached Cook, with strict own foreground
  guards; no new Compose Activity launch or assumed registration of old roots.
  Review/build/freeze before runtime; preserve exact outcomes and UUID-only cleanup.

The no-restart probe Passed with PID8020 unchanged. Candidate10 freezes180 source
paths and installed APK hashes; Build10 Passed1m20s. Attempt01 Failed0.105s before
the warm method (Compose coroutine ServiceLoader); Attempt02 Failed24.442s after
genuine setup at the offscreen mapping confirmation. Neither reached task death
or restoration. Native-only warm actions and bounded actual scrolling are now
independently reviewed. Attempt03 native warm Passed14.571s, but host Failed:
`am kill` left the previous-app PID alive; selected cleanup Passed. Attempt03 earns
no death/restoration credit. Reviewed host11 uses guarded own-UID/exact-PID/start
ticks SIGKILL; Attempt04 Passed warm16.242s/cold7.850s/cleanup0.294s. Same UUID/task17/
ActivityRecord, PID9840→9955, actual nonnull saved state and locked creation, exact
files before genuine password reopen/cache Cook; cleanup restores empty namespace.
Preserve all verdicts in the [runtime report](reports/saved-task-runtime-20261104.md).
See [independent saved-task review](reports/saved-task-review-20261104.md).
Primary references: [AOSP shell parser](https://android.googlesource.com/platform/frameworks/base/+/master/cmds/am/src/com/android/commands/am/Am.java),
[task focus implementation](https://android.googlesource.com/platform/frameworks/base.git/+/master/services/core/java/com/android/server/am/ActivityManagerShellCommand.java),
[Android16 instrumentation lifecycle](https://android.googlesource.com/platform/frameworks/base/+/refs/heads/android16-release/services/core/java/com/android/server/am/ActivityManagerService.java),
[AndroidX finisher gate](https://github.com/android/android-test/blob/main/runner/monitor/java/androidx/test/runner/MonitoringInstrumentation.java).

Unresolved: natural LMK and broader physical task/biometric matrix. Selected emulator
parser/runner/task/native UI compatibility is now evidenced; controlled background
death does not certify natural LMK, physical biometric or Google scope.

## Isolated source checkpoint —4November2026

- [x] Identify29 owned code paths against the initial170-source inventory and frozen
  candidate26/194 paths. All five Kotlin HEAD preimages and saved Gradle preimage
  match the initial baseline. Exclude preexisting theme/fonts/resources, extension,
  landing-page, skills and overlapping current documentation changes.
- [x] Create a separate worktree at352ffa9; copy only the frozen29 code paths.
  Fresh build Passed48s/182 tasks all executed; shared78/bridge22/app16 tests with
  zero failures/errors/skips; lint0 errors/8 warnings/1 hint. Original61 Android
  tests and54 protected/Golden files exact. Independent source/artifact audits Passed.
- [x] Record three focused source commits and one standalone handoff report on
  `codex/mobile-cookbook-checkpoint-20261104`, HEAD`b1ac8aec506dc1cd6091bb6f3d4ae203d0458b68`.
  Isolated checkout clean; original branch/index and all194 source bytes preserved.
  Root receives only an unstaged copy of the new report. No push/merge/archive.
- [ ] Finish required owner/device acceptance before final plan integration/delivery.

The isolated APKs contain the HEAD UI and differ from working-tree candidate26;
none was installed or device-accepted. Preserve every original runtime candidate;
no transfer of physical acceptance. [Checkpoint evidence](reports/isolated-mobile-checkpoint-20261104.md).
Whole plan remains Partial; selected200%/landscape TalkBack was Deferred by owner4November
and explicitly resumed6November; selected EN27/VI29 observations now accepted separately. Human Google
cancellation/different-Menu and broader physical/lifecycle/history gates remain open.

## Earlier selected runtime —6November2026 before manual29

[Actual results and recovery limits](reports/nov6-resume-runtime-20261106.md): human27
run01 NotStarted/run02 NotExercised33.994s without Google UI; grid01 Failed1.657s.
Font04 portrait/EN timeout remains Failed; font05 never dispatched native and restored
settings. Font06 actual200%/landscape: EN owner confirmation accepted, VI timed out;
late owner clarification says literal “Master password”, so VI hearing remains open.
All owner/source/APK and exact original display/WM restoration audits Passed.
Physical27 warm01 Failed49.096s at Favorites; physical28 warm01 Failed15.035s at
detail username/Recipe visibility. Both precede task baseline/death; owner/source
preservation and native inactivity verified. Build28 app16/lint0errors Passed.
Current repair/OS-label diagnosis remains separate from runtime acceptance.
Candidate27 added one test to194; build27 is compilation only, no fresh JVM claim.
Human/different-Menu/control-layout and physical lifecycle gates stay open.

## Latest bounded outcomes —6November2026

- [x] Close selected200%landscape font observations: EN font06/source195/candidate27
  and independent VI manual29/source199/candidate29. VI owner actually heard
  “Mật khẩu chính” and could move focus; native prep alone did not establish speech.
  Current retained guards/emptyUUID cleanup/owner/display restoration all Passed.
  Preserve different-candidate limits, original VI timeout/old-English report,
  Nov4 deferral and Nov6 resumption; no full bilingual same-candidate acceptance.
- [x] Record actual grid29 runtime Passed10.027s host,9samples/language and18FINGER
  pairs/toggles at110%portrait; independent strict74-frame runtime review Passed.
- [x] Complete selected physicalL1 on unchanged199/APK29 attempt02: warm17.945s,
  cold6.423s and cleanup0.030s native Passed; exact same task6685/ActivityRecord,
  PID29885→30235, real saved Bundle/locked restoration, genuine password/cache Cook,
  encrypted payload/owner/source/APKs/UUID cleanup verified; final overall Passed.
  Attempt01 overallFailed/late owner input remains historical; no SDK-cause claim.
  Natural LMK/long-background/L2 and broader biometric combinations stay separate.

[Actual terminal evidence](reports/nov6-resume-runtime-20261106.md) distinguishes
native preparation, actual manual owner verdict and cleanup. Human Google/different-
Menu, broader control/layout/device and physical lifecycle gates remain Pending.

## Selected long-background functional return —6November2026

- [x] Accept independently reviewed manual variant after663.477s passive background:
  same original process/task/ActivityRecord, native inactive during owner checks;
  actual owner confirmed `Mail-cold`, Dish `aB!*abcd`, username `00123` after Reveal.
  Menu/encrypted Spice payload/owner/source199/APKs29/policies exact; separate
  cleanup Passed0.066s, UUID absent. Original native wait timeout remains Failed;
  no password-method, timeout-cause or OS-LMK acceptance is inferred.

[Current evidence and boundaries](reports/nov6-resume-runtime-20261106.md).

## Actual early-action restoration —6November2026

- [x] Record new owner action performed before retained native readiness, without
  retrospective401/marker/provider-A credit. Selected human01 Failed at initial
  authenticated setup; cause unknown, owner data/UUID/source/APKs preserved.
- [x] Complete mandatory separate normal SDK restore01 Passed46.722s native/
  51.055s host: real metadata/Test/mapping/cache, exact owner/source202/APKs30/
  UUID cleanup/native inactivity. Derived restorationRequired=false; actual owner
  confirmed Google grant and return. Human cancellation remains Pending.

[Current scope and original Failed evidence](reports/nov6-resume-runtime-20261106.md).
