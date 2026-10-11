# Phase01 — Security, sessions and clipboard ownership

Current acceptance2026-10-12: Done in the scope below.
[Combined evidence](reports/verification.md) records147 unit/25 distinct headed
E2E and8 actual Google/native cases, proof boundaries and independent review.
Original pre-repair observations and conditional verification paths remain below
as history; a checked criterion means its current applicable outcome was resolved,
not that every historical method/conditional branch was separately executed.


Status: Done. Priority:1. Existing requirements: `docs/prd/v1-mvp.md` §5.4/5.5
and current relevance audit; this slice excludes release and the rest of MVP.

## Current source and defects

- `chrome-extension/src/popup/popup.js`: direct Argon2/AES unlock, no failed-attempt
  coordinator, concurrent click/Enter possible; toolbar Copy has no clear timer.
- `src/options/options.js`: separate direct unlock/setup/change-password paths.
- `src/service_worker.js`: idle300s and absolute10m alarms, but no durable lockout;
  alarm existence alone must not reset or substitute for session expiration.
- `src/content/content.js`:30s timer clears blindly if clipboard read rejects;
  page/popup closure destroys ordinary document timers.
- Manifest has clipboardWrite but no clipboardRead/offscreen. Argon2 adapter and
  custom wrapper use dynamic import, unsupported in an extension service worker.

## Implementation TODOs

- [x] Preserve Argon2id32-byte/t3/m65536KiB/p4 and current encrypted vault shape.
  Quality choice is Argon2-only fail-closed; vault600k fallback is Superseded.
  No new KDF metadata/migration. Runtime failure never overwrites existing data.
- [x] Extract authoritative authentication/session coordination into focused
  background helpers; both popup and Options must call the same unlock entry.
  Reject content-tab/untrusted senders for authentication and internal helper calls.
- [x] Persist nonsecret attempts/lockedUntil/in-flight identity in storage.local;
  no password/key/spice there. Five failed attempts lock for five minutes.
  Serialize admission, derivation/verification and record updates across all UI
  clients. Reserve an attempt before expensive work; crash/abandoned attempts
  must not reset counters or allow unlimited restart bypass. Use bounded recovery
  for stale leases; distinguish known runtime/config failure from bad password.
- [x] Central authority verifies encrypted profile authentication before publishing
  a session. Success clears failed count; wrong password changes no sync ciphertext.
  Explicit lock/expiry invalidates operation epoch; late success cannot reopen.
  Countdown/retry error codes are sanitized and localized; no raw error logging.
- [x] Keep derivation in a compatible extension execution context. Preferred
  bounded helper: bundled offscreen document spawning an actual module Worker,
  with reason WORKERS; existing Argon2 wrapper runs there, not dynamic import in SW.
  Prove real WASM before switching callers; never pretend a worker was used.
  Pending computation/replies need IDs, teardown and cleanup on cancellation/restart.
- [x] Store session start/expiration beside key in storage.session; reconstruct
  absolute10m alarm from deadline on startup, never extend by popup reopening.
  Check expiration before recipe/tag reads as well as on alarm; idle/locked state
  clears session and pending dish UI. Runtime strings cannot promise perfect erasure.
- [x] Add one bundled offscreen helper, reason CLIPBOARD, using actual supported
  clipboard DOM operations in headed Chrome. Add only offscreen+clipboardRead;
  these allow read-before-clear after popup closure, not collection/history.
  Retain at most owned copied dish/generation/deadline in RAM, never on disk.
- [x] Shared Copy endpoint supports popup and retained content UI. Confirm actual
  write before feedback; replace older ownership on new Copy. At30s compare current
  text to owned text immediately before clearing; changed clipboard/read denial
  means no write. Never blind-clear. Clipboard compare/write is not atomic: report
  that API limit; avoid claiming protection from every concurrent external write.
- [x] Keep offscreen context while pending copy/crypto work exists; close when
  idle without losing unrelated work. SW restart must reconnect to its live owner;
  do not use Chrome150-only hasDocument on current Chromium143. Alarms may be late,
  so use live helper deadline timer plus reconciliation; do not promise exact timing
  while the machine sleeps. Recipe-only Copy is not dish auto-clear scope.

## Meaningful verification

- [x] New `test/unit/auth-session.spec.mjs`: fifth failure, boundary cooldown,
  serialized competing clients, persistent reload, abandoned in-flight recovery,
  runtime failure, authenticated success reset, stale result after lock/expiry.
- [x] New `test/unit/clipboard-owner.spec.mjs`: close-independent deadline,
  later user text preserved, denied read no write, old timer/new copy, teardown.
- [x] New `test/e2e/extension-security.spec.ts`: actual WASM setup/unlock and
  ciphertext unchanged on wrong password; popup close/reopen + genuine SW stop/
  wake lockout persists; concurrent popup/Options actions obey one authority.
  Real copied dish clears after30s with popup closed; externally replaced text
  survives. Do not accelerate the browser clipboard assertion into fake proof.
- [x] Deterministic unit/API regressions test10m/5m boundaries, alarm reconstruction
  and stale-session gates. Real browser verifies actual Worker/WASM, restart-persistent
  lockout and30-second clipboard ownership; it does not assert alarm expiration.
  Independent review checks permissions/no plaintext disk.

## Primary platform references

- [Offscreen](https://developer.chrome.com/docs/extensions/reference/api/offscreen):
  bundled document/runtime-only API, CLIPBOARD and WORKERS reasons, Chrome109+.
- [SW imports](https://developer.chrome.com/docs/extensions/develop/concepts/service-workers/basics):
  dynamic import unsupported in extension SW.
- [Alarms](https://developer.chrome.com/docs/extensions/reference/api/alarms):
  delivery can be delayed; persisted deadline remains authoritative.

Browser feasibility and actual30-second clipboard behavior proved. Clock/API
regressions cover session boundaries; they do not certify a broad real-time matrix.
No unresolved question in this bounded slice.
