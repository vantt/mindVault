# Development Roadmap

## In progress

- Real Google OAuth setup and one live Cookbook read remain pending; local
  client verification does not certify an account connection.
- 2026-10-11 — Mobile Core implemented in its original worktree: recipe/tag
  parity, synthetic bundle decrypt and Argon2 proof; 26 JVM tests and Android
  packaging pass. Browser-derived Unicode parity passed independent review;
  local merge remains pending. See
  [Core recovery report](history/passchef-mobile/reports/mobile-core-recovery.md).
- After Core completion: dependent Keystore unlock work; physical-device acceptance
  required. Extension QR handoff remains independent.

## Completed

- 2026-10-11 — OAuth/Menu client code recovered and integrated into local main:
  state/PKCE, UTF-8 ranges/configuration, literal recipe writes, resource cleanup,
  and sanitized failures; 22 tests passing in the original worktree and main.
  Worktree archived and closed; remaining plan dossiers consolidated into main.
  See [worktree recovery](worktree-recovery.md).
- 2026-05-22 — v2.2 Recipe Verification Tag (Option B): explicit mismatch detection; Cookbook insight preserved.
