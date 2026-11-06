# Human Google cancellation preparation review —4November2026

Status: **Corrected source review Passed; final post-correction candidate/host/runtime Pending**.
Independent read-only review of three debug/test paths, existing auth/observation
code and pinned upstream dispatch. No Gradle, install, device or implementation edits.

## Source and callback authority

The independently downloaded
[official Activity1.13.0 source archive](https://dl.google.com/dl/android/maven2/androidx/activity/activity/1.13.0/activity-1.13.0-sources.jar)
SHA256`c7cf1b2e315e08867b10ef4505945d1993d90281445c65b929e81b9420216ce9`
confirms IntentSender routing and normal result-registry forwarding. Its registry
may defer pending result delivery until ON_START. The witness accordingly checks
eventual pending consumption rather than requiring immediate synchronous delivery.

- Only opt-in ColdStart proof instances create the witness. Default existing
  fixtures keep null witness and unchanged super dispatch. Canonical UUID/fresh
  directory guards and existing saved-instance refusal remain intact.
- The real pending object and registry request code are captured immediately before
  forwarding actual IntentSender launch. Matching result count/code/original identity
  are observed before normal super dispatch; a normal single forwarding return is
  counted afterward. Observation errors become failure metadata and never suppress
  or replace the real super call. No reply Intent/token/account content is read.
- Witness reflection/snapshots execute on main and are read-only. Duplicate matching
  launch/result/forward, replaced pending object, abnormal forwarding and observation
  fault cannot pass. A pending request consumed at later ON_START remains permitted.

## Native test and corrected classification

- Actual password/strong-biometric setup, confirmed synthetic offline cache and
  literal Cook precede Connect. The existing observer is registered before dispatch
  and requires actual external Google package, original pending request, natural
  stop/lock and unchanged jar/cache/RAM inventory before the owner checkpoint.
- Entering Sources already clears the old Dish before Connect. Return must be
  secure own foreground, still locked with no session/token/Dish/Copy and exact
  offline cache preserved. One matched real RESULT_CANCELED, exactly one normal
  forwarding and eventual original pending consumption are mandatory.
- Native canceled result alone does not establish the owner's hand action. A later
  external host must bind an actual current owner response to live real Google
  cancellation; host Back, synthetic callbacks and grant/account/ACL changes cannot
  substitute. Wrong result, timeout, ownership/lifecycle or preservation error fail.
- Initial no-external classification was tightened during review: only an idle
  observer with no stop/pending and a fault-free witness with zero launches/results/
  forwards, null request/result codes and no original/pending/consumed identity can
  be skipped. Attempted/failed launches or witness faults no longer become silent
  reuse NotExercised. No actual external prompt means no human-cancellation credit.
- Cleanup retains primary failure and independently closes observer/Activity, reads
  only this UUID's record, deletes only its captured/record-owned aliases and removes
  then verifies exact directory absence. Setup captures owned aliases before later
  interaction. User's default Cookbook and preexisting proof directories are outside
  this test directory; old observer assertions remain unchanged.

## Candidate14 snapshot and required continuation

`2026-11-04-google-human/reviewer-google-human-source14.json` confirms all184 source
archive entries match frozen14 and all candidate14 APK bytes match. Relative to182,
only existing debug ColdStart Activity changes; witness/test are additive. Original
61 Android test files remain byte-exact. Debug`71ccff6a…d38c2`,test`131fabaf…e2d29`,
release`039e2420…641d` belong to that pre-tightening snapshot; no install/native ran.

The tighter skip assertion changes only the new test after candidate14. Compile/
freeze the final corrected source before runtime; candidate14 is retained history.
No remaining source-review blocker found. Local source/build evidence cannot make
a naturally available Google prompt, prove human cancellation or certify401.

## Candidate16 and external host review

The corrected Google source is included in source186/candidate16, independently
verified with the API26 audit. Existing61 Android tests and54 protected/Golden
entries remain byte-exact. Debug`71ccff6a…d38c2`,test`a716cd39…f1221`,release
`039e2420…641d`. API26 runtime does not exercise this Google case. Candidate14
remains historical; no physical Google install/native acceptance is claimed here.

Prepared six-helper hashes match `host-preparation01.json`; independent rerun of
all35 pure protocol tests passes. Fixed physical serial, source/APK binding, fresh
owner readiness, actual Awake/unlocked/no-process preflight and before/after file,
directory-name, owner digest and plugged-policy checks are appropriate. The runner
does not install, wake, unlock, send Back, change accounts/grants/ACL or settings.
Timeout/unconfirmed inactivity stays Unknown; killing the host ADB client does not
claim native termination or permit automatic UUID cleanup/retry.

Native pass alone remains OwnerPending. A separately recorded actual reply binds
the pending UUID/request code/ready-record digest; conflicting actions aggregate
Unknown. Native wrong-result/failure and preservation failure cannot become Passed
through an owner sidecar. Narrow source-bound zero-witness skip remains
NotExercised. Recording the actual reply may follow native completion; initial
readiness cannot substitute for that action evidence.

Two host corrections required before native launch:

- **P2 — growing transcript parsing:** `human-host.py:100` calls the final strict
  parser; `human-protocol.py:60` rejects trailing incomplete human metadata. A
  normal read between field writes and status2 aborts the observer and leaves
  native liveness Unknown. Pure replay reproduced this exact assertion. Live
  parsing should retain completed bundles and ignore only the unfinished tail;
  completed/final transcript validation must remain strict.
- **P2 — stale pending export:** `human-host.py:102–120` finds a historical pending
  record and tests ADB-child liveness, but permits owner-ready export after the
  transcript already contains returned/completed phases or native terminal. Pure
  replay of a complete Passed transcript with a live client exported ready=true;
  the prepared test currently expects that behavior. Require current pending
  phase and absence of terminal/end/failure before and after device observations.
  Re-read current raw output before exporting. Root must inspect current live
  pending evidence before asking the owner to act.

Evidence: external `reviewer-google-human-host01.json`. No reviewer device calls.

## Corrected host02 — focused review Passed

Both findings resolved in `host-preparation02.json`. Live parsing now ignores only
an unfinished trailing line/bundle; completed malformed bundles still fail. Final
transcript parsing remains strict. Current pending requires the latest completed
human phase, exact selected JUnit start without end, no terminal/result/summary/
failure and no unfinished tail. A complete newline-terminated status2 is required.

After actual Google focus observations, the host rereads native.log and requires
the same currently pending witness and live client again before exporting ready.
This is bounded observation; root must still inspect fresh pending evidence before
requesting the owner's hand action. Result0 alone remains insufficient actor proof.

Independent rerun:35 terminal protocol +17 live-stream tests,52 passed/0 failures/
0 errors. Separate replays confirm incomplete live tail waits, complete pending
is eligible, historical pending/full terminal and returned phases are ineligible,
and incomplete final input still fails. No reviewer device calls.

Archived prior six helper hashes match initial preparation. Corrected current
helpers exactly match `host-freeze02.zip`, SHA256
`3e755eecc38b5df3d7b1f0437e758afcb45d6ec6a1ccf0a86d5caf7744370492`.
Prior archive SHA256
`7fd65f4a30c02d94426ff39bba18a3cec8c4b4af9d7d180167bb43080ba38aa2`.
Candidate16/source186 manifest hashes and archived APK bytes remain exact.
Evidence: external `reviewer-google-human-host02.json`.

This accepts preparation, without Google physical install/native acceptance. Any
new production/source change requires a fresh reviewed candidate/source binding;
the current host deliberately requires source186 to match before launching.

## Host03 candidate20/source189 rebind — Passed

Independent focused diff versus archived host02 finds only `Host.local()` changed
(plus ZIP import). All6 other helpers remain byte-exact02. Local provenance now
requires189 source paths, matched candidate/source version filenames, exact189 ZIP
entry inventory without extras/duplicates and exact archive content; all3 candidate
APK digests and actual current frozen source paths remain checked. Real pure
`Host.local()` invocation passes candidate20/source189 and original no-stop guard.

Prepared03 helper hashes match current bytes. Prior02 archive SHA256
`c45cefe6097bdbd8718a61d6382c67f6e02e604c9db9f03261b113b8076e4e8d`
preserves all7 previous helper hashes. Independently reran35 terminal +17 live
protocol tests,52 passed/0 errors/failures. Current-pending/latest-raw/liveness,
separate ready-SHA-bound actual-owner action, preservation and timeout Unknown
logic remain unchanged. Evidence: external `reviewer-google-human-host03.json`.

This accepts updated preparation for candidate20 (debug`c4f24726…ce454`,test
`35a08698…aca14`,release`87fc6d3e…45471`), without phone install/native acceptance.
No reviewer ADB/Gradle/device calls. Concurrent API27 runtime was not inspected.

## Host04 candidate23/source192 rebind — Passed

Independent comparison to retained host03 confirms exactly4 literal189→192
replacements in human-host.py; all6 other helpers remain byte-exact03. Prior03
archive/current prepared hashes match. Host04 ZIP SHA256
`f6d42e6382372c9190c0ce4a22abff4d0d7dea73160b118860ab6b4b1ca83868`.
Pure local candidate23/source192/ZIP/3APK audit passes; independently reran52
unchanged protocol/live tests,0 failures/errors. Current pending/action/liveness
and preservation rules remain unchanged. Evidence: external
`reviewer-google-human-host04.json`. No reviewer device calls/native acceptance.

Root's separate saved installation23 evidence binds debug`c4f24726…ce454` and
test`8a8d0857…0f4a7`, original owner inventory unchanged, without granting account
action permission or proving human cancellation. Fresh readiness and actual
resolution/action/native/preservation evidence are still required for this case.

## Separate human26 sibling preparation — Passed

New `2026-11-04-google-human26` sibling preserves all7 original host04 helpers in
their old directory. Independent comparison finds only4 literal192→194 substitutions
in new human-host.py; other6 helpers remain byte-exact04. All new freeze entries
match. Actual pure local candidate26/source194/ZIP/3APK audit passes. Independent
35 terminal +17 live protocol tests rerun,52 Passed/0 failures/errors. Evidence:
external `reviewer-human26-preparation.json`. No reviewer device/Gradle calls.

Prepared scope uses existing restored grant state only. Genuine human cancellation
may run only if an external Google resolution naturally appears after fresh owner
readiness; no new revocation/account/ACL/settings action to force it. Silent reuse
remains source-bound NotExercised. Actual live prompt, separate actual-owner hand
reply and native/preservation evidence remain necessary; this rebind is not runtime
acceptance. Candidate26 production local-clear repair is separately reviewed in401
report; it does not itself prove human cancellation.

## 6November human27 sibling preparation — Passed

Separate `2026-11-06-google-human27` changes only4 literal194→195 counts/messages
in human-host.py; other6 helpers remain byte-exact26. All7 prepared hashes match.
Actual pure candidate27/source195/ZIP/3APK verification passes, with production
debug/release unchanged26 and new grid-only test APK. Independently reran35 terminal
+17 live protocol tests,52 Passed/0 failures. Evidence: external
`reviewer-human27-preparation.json`. No reviewer device/Gradle/Git calls.

Existing grant state/naturally available Google prompt only. Fresh actual readiness,
live current prompt and separate owner hand-action evidence remain required.
No new revocation/account/ACL/settings action to force resolution; zero-witness
silent reuse stays NotExercised. This source/provenance rebind is not native credit.

## 6November human27 actual run02 — NotExercised

Independent raw replay confirms source-bound clean assumption`[1,-4]/[-1]/OK1`,
33.994s native/35.697s host. UUID`91491aba-de75-43e9-afb1-d3865c55fe9f`;
raw SHA256`78d2231e450fe82b6457d11c8428eab0f6b3d7eb3212b98326ce8617fb62a6b0`.
Completed zero-witness snapshot has launches/results/forwarded0, no request/result
code, pending/identity/consumption false and no observer fault. Native original
no-stop/no-pending guard holds. No real external Google UI or owner checkpoint
appeared; no human Cancel instruction/resultCode acceptance, no new revocation.

Actual owner files/digest and six historical names are exact, current UUID absent,
source195/ZIP/APKs unchanged, native inactive. Source-backed normal Connect silently
reused existing authorization; this is NotExercised, not Passed/failed cancellation.
Evidence: external `reviewer-human-runtime02.json`. No reviewer device calls.

Earlier run01 was NotStarted because ordinary MainActivity PID existed. Root
inspected own ordinary process/no native and preserved owner inventory before
normal Home/own-package force-stop preparation. This is not saved-task/death proof.
Refreshed device-readiness record retains the prior cooperation reply's original
timestamp chain; it does not invent a new chat reply or actual cancellation action.

Unresolved: naturally available Google resolution and actual owner hand cancellation/
result/native evidence. No forced account/grant change is authorized to manufacture
availability; different-Menu and broader plan gates stay separate.
