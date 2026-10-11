# Finish the existing phone QR handoff plan

Date: 2026-10-11. Worktree: `/home/vantt/projects/passMan--wt--qr-handoff`.
Existing accepted plan: `docs/history/qr-handoff/plan.md`; validated original
cells: `qr-handoff-1` and `qr-handoff-2` in the main checkout. Bee work deferred
by the user. Finish this work before integration; phone reader and Drive remain excluded.

1. Pin and vendor Nayuki's MIT encoder as compiled classic JavaScript; retain
   license and compilation provenance. No extension bundler or runtime network.
2. Add the named phone entry to own profile cards, opening the existing export
   wizard. Add QR display to its result alongside unchanged Copy/Download.
3. Compact only the encrypted byte fields as base64. Prove exact reversible
   serialization without changing the original bundle or crypto adapter.
4. Use real encoder capacity checks, then fit oversized compact JSON into
   `{bundleId, idx, total, payload}` frames, zero-based indexes, original bundleId,
   plain substring payloads. Count full headers and preserve Unicode boundaries.
   Single-frame output remains compact JSON. Animate every 1.5 seconds.
5. Clear timers/canvas/payload on close, Done, reopen and stale async completion;
   show actionable QR errors without logging payloads or seasoning.
6. Add unit serialization/chunk/controller regressions and headed Chromium
   extension E2E for one small and one oversized synthetic cookbook. Preserve
   all existing golden tests. Use frozen dependencies and existing fixtures.
7. Verify scoped tests, independent product review, and document the transport
   contract/evidence. Root handles product-only commit and integration after green.

Verification uses test-only `jsqr@1.4.0`, pinned as a dev dependency, to independently
decode the rendered canvas. This is evidence for the sender contract, not phone
reader product code. The extension runtime remains dependency-free for QR.

## Integration follow-up (2026-10-11)

QR commit `4a4395c` merged into local main at `3d3239d`. Independent fresh-main
checks passed 37 scoped unit tests and two headed canvas-decoding E2E scenarios.
Complete archive verified; checkout closed.
Branch and recovery refs retained; no remote push.
