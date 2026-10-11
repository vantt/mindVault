# Current-Xiaomi completion accepted — integrated and closed

Date: 2026-10-11. Original Keystore worktree; current Xiaomi14TPro/2407FPN8EG
API36, source `0d38e62e6f9c67b80ac8f585dd0c8ab8807e1716`, pinned isolated demo/test APKs.
Feature complete within owner-agreed current-Xiaomi scope; original physical matrix incomplete.
No source/auth/storage policy change or simulated biometric proof is proposed.

## Verified implementation and current-device evidence

- Independent56 JVM tests:0 failures/errors/skips; original Core goldens untouched.
  Independent debug/test/release/lint builds passed,0 errors/8 known warnings;
  diagnostic impacted140 rerun tasks passed69s. Release excludes debug proof/logging.
- E1/E2: genuine USB device authorization, pinned install/launch from WSL using
  native Windows adb; demo isolation and backup exclusions confirmed.
- E3: physical wrap→lock→real biometric→unwrap equality, nonexportability,
  absent-auth refusal and authenticated cache. Later genuine recovery runner
  inspected actual StrongBox level2. Initial physical runner1PASS12.431s;
  focused recovery1PASS17.193s, all four owner fingerprint prompts.
- E4: owner-added enrollment invalidated old key; original password recovered
  unchanged encrypted cache, committed fresh alias/IV, then genuine new-key unlock.
  Prior wrap failures remain recorded; actual code15 wrap repair succeeded on `d2a76ca`.
  Owner saw one recovery prompt; fresh callback after expiry is reviewed control-flow
  inference, not independent visual confirmation of two dialogs.
- Wrong password on current `0d38e62`: visible `password_open_failed`, whole
  record/alias/salt/cache/retirement queue unchanged. Cancel, pending-wrap background
  return and force-stop/new-process cold lock have actual device evidence.
- Real idle5: prior functional pin `0a99d636`, lastopen292.74s/locked305.95s.
  Real total10: current `0d38e62`, lastopen597.21s/locked610.30s; owner explicitly
  confirmed manual title touches through lock. Individual touch uptimes unmeasured.
- OS-lockout availability fallback: owner five failed scans/"too many attempts";
  Main correct password submitted10.10s after probe, then opened strong-biometric unavailable,
  entire ciphertext record unchanged. Intentional seed-runner refusal is NOT a
  passing test; its unique alias/file cleanup completed. No same-jar UI-count claim.
- Screen protection owner exact reports: "screenshot không cho, quay bị đen",
  then "riêng paschef đen". Screenshot blocked/only PassChef black are manual
  reports; no root visual/video capture or definitive Recents-category claim.
  Root confirmed own foreground exit→ordinary return locked+FLAG_SECURE.
- Final pinned APKs/full ciphertext unchanged; bounded ownUID10422 log snapshot
  7 lines/six known synthetic literals0hits, not comprehensive log absence.
  Keep-awake original0 restored/verified; all ADB workers ended, phone free.

## Unexecuted original physical matrix

Original [plan](../../../docs/history/passchef-mobile-android-keystore/plan.md):55/57/60/61
and [recovery acceptance](../plan.md):70–71 remain the reference requirements:

- Zero enrollment, no biometric hardware, API26/no StrongBox: current enrolled
  API36/StrongBox phone does not establish these physical environments.
- Typed initial BiometricPrompt7/9 callbacks: Main failed scans logged5,5 only;
  observed OS availability fallback does not establish the separate callback route.
  Both typed policy paths have source/JVM ownership/record tests, not physical proof.
- Exact process kill between authenticated callback and doFinal, and hardware
  retry exhaustion: ordinary lifecycle tests do not establish those exact windows.
  Earlier held unwrap numeric cause remains unknown; fresh unwrap succeeded with
  no captured numeric error. No new defect or unwrap retry is inferred.

## Owner decision — accepted 2026-10-11

Direct owner reply: **"Merge/push hôm nay, ghi rõ bài kiểm tra còn thiếu"**.
Owner accepted completion of the validated current-Xiaomi scope with the above
unexecuted physical matrix retained as tracked coverage limits, not passing tests.

## Integration and closure

Source `0d38e62` unchanged/frozen. Main merge `b959aca` pushed/remote SHA verified;
independent fresh main56JVM/150tasks/debug/test/release/lint passed,0errors8warnings.
Demo/test APKs match the physical candidate byte-for-byte. All149 original main
WIP statuses/hashes restored exactly. Full final checkout archive verified
3,464 files/234,260,698 bytes plus all directory modes before checkout removal.
Branch/archive/recovery refs remain. All original feature checkouts are closed.

Additional-device/matrix coverage remains tracked above; owner scope is resolved.
