# Development Roadmap

## In progress

- [Prioritized active plans](../plans/README.md): the five browser slices passed
  independent acceptance. Next: remaining MVP Sync/platform/performance verification
  and release readiness, followed by mobile/website/commercialization dependencies.
  Full MVP remains Partial; closed Builder/Tag/Core/Popup UX plans are archived.
- Real Google OAuth setup and one live Cookbook read remain pending; local
  client verification does not certify an account connection.
- [Keystore physical coverage follow-up](../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md):
  API26/no hardware/no enrollment, typed7/9 callbacks, exact process-kill/
  retry-exhaustion windows and precise Recents observation remain unexecuted.
- Phone QR reading/reassembly and broader mobile screens are separate follow-ups
  from main; all original feature checkouts are closed.


## Completed

- 2026-10-12 — Extension security, Builder/Tag lifetime repairs, current Google
  integration QA and offline EN/VI Demo/spec polish complete. Independent147 unit
  and25 distinct headed E2E passed; actual public synthetic Google/native8/8 passed
  without keyboard fallback. Golden unchanged. All current Builder/Tag/Core/Popup
  UX phases Done and archived. See [verification](../plans/261012-extension-completion/reports/verification.md);
  delivery to main is tracked in the completion dossier.

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
