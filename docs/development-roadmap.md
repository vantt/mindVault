# Development Roadmap

## In progress

- Real Google OAuth setup and one live Cookbook read remain pending; local
  client verification does not certify an account connection.
- Android Keystore and biometric unlock are being completed in their original
  worktree using the verified Mobile Core. Physical-device acceptance is required.
- Phone QR reading/reassembly remains a separate follow-up after existing
  worktrees are completed.


## Completed

- 2026-10-11 — Mobile Core integrated into local main at `7d8808c`: 26 JVM
  tests, Android packaging, 22 unchanged extension tests, and 56 actual browser
  Recipe/HMAC vectors passed independent verification. See
  [Core recovery report](history/passchef-mobile/reports/mobile-core-recovery.md).
- 2026-10-11 — Extension QR sender implemented and independently reviewed:
  15 QR plus 22 unchanged recipe golden tests and two headed E2E scenarios
  passed with independent canvas decoding; no P1/P2 found. Integrated by this
  merge; fresh main verification and checkout closure follow. See
  [verification report](../plans/261011-qr-handoff-worktree-recovery/reports/verification.md).

- 2026-10-11 — OAuth/Menu client code recovered and integrated into local main:
  state/PKCE, UTF-8 ranges/configuration, literal recipe writes, resource cleanup,
  and sanitized failures; 22 tests passing in the original worktree and main.
  Worktree archived and closed; remaining plan dossiers consolidated into main.
  See [worktree recovery](worktree-recovery.md).
- 2026-05-22 — v2.2 Recipe Verification Tag (Option B): explicit mismatch detection; Cookbook insight preserved.
