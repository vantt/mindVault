# Development Roadmap

## In progress

- [Commercialization hub](commercialization/README.md) and [Phase 0 plan](commercialization/plan.md): preparation proceeds
  alongside remaining bug/UI work: listing/privacy drafts, publisher/support
  decisions, non-UI brand assets, release documentation and packaging tooling.
  App is not ready for release per owner report. Final screenshots, release version/
  ZIP acceptance and Store publication wait for release-blocking bugs to be resolved
  and UI to stabilize. Historical selected acceptance below retains its original scope.
- [Prioritized active plans](../plans/README.md): the five browser slices passed
  independent acceptance. Next: remaining MVP Sync/platform/performance verification
  and release readiness, followed by mobile/website/commercialization dependencies.
  Full MVP remains Partial; closed Builder/Tag/Core/Popup UX plans are archived.
- [Android Cookbook Phase1](../plans/261012-mobile-cookbook-phase1/plan.md): real
  manual cooking, private cache/confirmed columns, Compose lookup/reveal/copy and
  Android authorization/Sheets engine are implemented and locally verified. Owner
  Selected Android Google grant/read/mapping/cache accepted; broader phone/Google
  acceptance remains Pending. Actual
  UI Run01 failed after reveal; [unattended regressions/diagnostics](../plans/261012-mobile-cookbook-phase1/reports/unattended-regression.md)
  passed local checks without resolving the original phone cause. [Later runtime](../plans/261012-mobile-cookbook-phase1/reports/ui-runtime-continuation.md)
  passed locked diagnostics and selected UI Run04; normal-policy comparison
  subsequently passed Run05 with KEEP_SCREEN_ON=false. Broader acceptance
  stays Pending; original Run01 cause is still unknown/not reproduced.
  [Oct13 clipboard adapter](../plans/261012-mobile-cookbook-phase1/reports/clipboard-runtime.md)
  Passed3/3 on API36; [same-process locked clipboard recreation](../plans/261012-mobile-cookbook-phase1/reports/clipboard-recreation-runtime.md)
  subsequently Passed1/1,31.721s with original deadline preserved. API28+/API36 adapter
  scope accepted; background/process death/history and broader
  platform/UI acceptance remain Pending. Owner reports Android OAuth client configured;
  [Google acceptance preparation](../plans/261012-mobile-cookbook-phase1/reports/google-acceptance-preparation.md)
  is the earlier compilation snapshot; later selected runtime is recorded separately.
  Local protocol fixtures do not certify an account connection.
  [Selected Google runtime](../plans/261012-mobile-cookbook-phase1/reports/google-runtime.md)
  GoogleRun03 Passed1/1,23.309s after fixture completion: all11 match flags, explicit
  mapping and stored cache accepted; earlier Run01/02 Failed history retained. Fresh
  consent observed in Run01; Run03 can reuse grant.
  [Google cache UI continuation](../plans/261012-mobile-cookbook-phase1/reports/google-cache-ui-runtime.md)
  Run02 Passed1/1,42.393s after retained Failed Run01,294.651s and helper-only normal
  Compose synchronization: exact cached-row Cook/favorites/recent/search, lock/reopen
  and real-biometric authenticated same-process recreation accepted. Default two-dir
  proof regression Passed1/1,31.769s; owner digest unchanged. App-disconnected cache
  only in that run. Later [network-off Run04](../plans/261012-mobile-cookbook-phase1/reports/network-off-runtime.md)
  Passed1/1,31.477s under strict actual default/bound/internet-network absence:
  retained provider NETWORK_FAILED exact cache/jars preservation and unchanged cached
  UI/Cook/favorites/recent/search/real-biometric same-process recreation accepted.
  Three prior failures retained; unchanged candidate, owner digest preserved, no
  production repair. Cancellation/stale callbacks/401/403/cold-start/process death/
  broader timer UX/capture/ENVI/accessibility remain Pending; original UI Run01/network Run03
  key-wrap causes unresolved. Whole mobile remains Partial.
  [Real foreground timer continuation](../plans/261012-mobile-cookbook-phase1/reports/session-timer-runtime.md)
  has compiled/reviewed genuine-biometric/actual-window-input fixtures; idle Run01/02
  Failed before pointer baseline; Run03/04 Failed at setup. Run05 actual5-minute
  idle Passed1/1,341.722s under proof foreground KEEP_SCREEN_ON; total Run01 setup
  Failed182.309s/Run02 Failed182.279s with correlated code15 wrap cause unknown;
  Run03 setup Failed182.138s.14October Total04 Passed1/1,609.969s,runner[1,0]/OK1:
  original ten-minute deadline locked despite real minute1–9 touches/recooking;
  session/Dish/Copy cleared, jars/owner digest preserved. Same frozen03/APKs, no
  rebuild/install/source change. Selected5/10-minute API36 foreground proof KEEP_SCREEN_ON
  accepted; broader timer UX remains Pending, no production repair/cause resolution.
  [Security/UI continuation](../plans/261012-mobile-cookbook-phase1/reports/security-ui-runtime.md)
  locked pixels01 Passed1/1,2.321s and authenticated02 Passed1/1,25.890s: selected
  EN/VI setup/manual Cook and actual secure shell display exclusion on API36 proof
  foreground. Locked01/Capture01/Auth01 remain Failed; no production repair claimed.
  Recents01 Failed206.052s without post-checkpoint stop/resume; Recents02
  Failed182.457s before setup readiness/checkpoint, no navigation commands sent.
  Recents03 Passed1/1,137.789s with real pause/stop/locked read/genuine reopening;
  owner confirmed own card blank/covered. Same frozen04/APKs, no rebuild/install
  between attempts; earlier causes unconfirmed.
  Six scoped test-APK/lint builds Passed; no fresh JVM/browser execution.
  Owner digest/protected sources preserved. OS screenshot shortcut, Vietnamese
  OS accessibility/TalkBack and broader layout/font/orientation remain Pending;
  Mobile Partial, original UI/key-wrap causes and cold-start/provider gaps retained.
  [Offline cold-process proof](../plans/261012-mobile-cookbook-phase1/reports/cold-process-runtime.md)
  warm02 InterruptedByVerifiedSIGKILL/cold02 Passed1/1,5.993s: new PID starts locked,
  genuine biometric reload/exact cache/Cook/jars preserved. Five new bounded files/
  unexported dedicated-task manifest entry, build03/lint/release exclusion Passed;
  old49 means48 unchanged+intentional entry, protected208/23 intact. Warm01 pre-signal
  focus-query orchestration Failed retained; source/APKs unchanged for retry. Provider
  LMK/task restore/broader matrix Pending. Four new provider-cold files preserve54/
  original17 predicates. [Provider cold02](../plans/261012-mobile-cookbook-phase1/reports/google-cold-runtime.md)
  Passed1/1,8.886s after actualSIGKILL13092→14408, real fingerprint/exact cache/Cook/
  jars/owned cleanup. Cold01 Failed181.588s/cause unknown retained; only diagnostics
  changed in two new files, build03 Passed58s10/101,lint0/11/1; no production repair.
  [VI OS-field01](../plans/261012-mobile-cookbook-phase1/reports/vi-accessibility-runtime.md)
  Passed1/1,1.5s: exact one own password node with descendant label, not spokenTalkBack.
  Source59/old58 preserved/build55s9/102/lint0/11/1; original checker remains Failed.
  [Native language touch](../plans/261012-mobile-cookbook-phase1/reports/language-touch-runtime.md)
  center02 Passed1.852s, edge01/02 Failed1.408s/1.211s with English retained after
  actualedge pair; no physical whole48dp target credit/cause resolution. Separate
  matched edge60 candidate04/source60/testce61 built/reviewed43s9/102/lint0/11/1,
  USB empty before install, not run. Lastinstalled84dd/old59 preserved; owner digest
  last verified after edge02. Spoken TalkBack/full layout/font/rotation still Pending.
  Earlier owner "can't take screenshot"
  report has unconfirmed shortcut/reveal timing, separate from accepted Recents card.
- [Keystore physical coverage follow-up](../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md):
  API26/no hardware/no enrollment, typed7/9 callbacks, exact process-kill/
  retry-exhaustion windows and precise Recents observation remain unexecuted.
- Phone QR reading/reassembly, Autofill and account-authoring screens are separate follow-ups
  from main; all original feature checkouts are closed.


## Completed

- 2026-10-12 — Extension security, Builder/Tag lifetime repairs, current Google
  integration QA and offline EN/VI Demo/spec polish complete. Independent147 unit
  and25 distinct headed E2E passed; actual public synthetic Google/native8/8 passed
  without keyboard fallback. Golden unchanged. All current Builder/Tag/Core/Popup
  UX phases Done and archived. See [verification](../archive/plans/261012-extension-completion/reports/verification.md);
  integrated/pushed to main at0cba85b; remote SHA verified.

- 2026-10-11 — Plan-status/relevance audit: per-phase evidence and explicit
  Superseded/Deferred outcomes reconciled; closed recovery/foundation plans moved
  to repository-root `archive/plans/`. Three extension plans with relevant gaps
  returned to active `plans/`; no product source or test criteria changed.
- 2026-10-11 — Android Keystore integrated/pushed at `b959aca`, archived and
  closed within owner-accepted Xiaomi/API36 scope. Fresh main56-JVM/build/lint
  passed; device hardware/recovery and real idle5/total10 evidence retained.
  All four original feature checkouts are closed; future plans start from main.
- 2026-10-11 — Mobile Core integrated into local main at `7d8808c`: 26 JVM
  tests, Android packaging, 22 unchanged extension tests, and 56 actual browser
  Recipe/HMAC vectors passed independent verification. See
  [Core recovery report](../archive/plans/passchef-mobile/reports/mobile-core-recovery.md).
  Fresh main checks passed; checkout archived and closed.
- 2026-10-11 — Extension QR sender implemented and independently reviewed:
  15 QR plus 22 unchanged recipe golden tests and two headed E2E scenarios
  passed with independent canvas decoding; no P1/P2 found. Integrated at `3d3239d`; fresh main checks passed and the checkout is
  archived and closed. See
  [verification report](../archive/plans/261011-qr-handoff-worktree-recovery/reports/verification.md).

- 2026-10-11 — OAuth/Menu client code recovered and integrated into local main:
  state/PKCE, UTF-8 ranges/configuration, literal recipe writes, resource cleanup,
  and sanitized failures; 22 tests passing in the original worktree and main.
  Worktree archived and closed; remaining plan dossiers consolidated into main.
  See [worktree recovery](worktree-recovery.md).
- 2026-05-22 — v2.2 Recipe Verification Tag (Option B): explicit mismatch detection; Cookbook insight preserved.
