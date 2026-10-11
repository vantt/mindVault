# Development Roadmap

## In progress

- Real Google OAuth setup and one live Cookbook read remain pending; local
  client verification does not certify an account connection.
- Next: Mobile Core recipe/decrypt/Argon2 parity; complete Argon2 proof before
  dependent Keystore unlock work. Extension QR handoff can proceed independently.

## Completed

- 2026-10-11 — OAuth/Menu client code recovered and integrated into local main:
  state/PKCE, UTF-8 ranges/configuration, literal recipe writes, resource cleanup,
  and sanitized failures; 22 tests passing in the original worktree and main.
  Worktree archived and closed; remaining plan dossiers consolidated into main.
  See [worktree recovery](worktree-recovery.md).
- 2026-05-22 — v2.2 Recipe Verification Tag (Option B): explicit mismatch detection; Cookbook insight preserved.
