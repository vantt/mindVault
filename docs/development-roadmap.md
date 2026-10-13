# Development Roadmap

## In progress

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
  timers/capture/ENVI/accessibility remain Pending; original UI Run01/network Run03
  key-wrap causes unresolved. Whole mobile remains Partial.
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
