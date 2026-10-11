# QR sender verification

Date: 2026-10-11. Worktree: `/home/vantt/projects/passMan--wt--qr-handoff`.
Base synced by root: `b097b9c`. Status: product implementation and independent
review complete; no staging, commit, merge, or remote push by this worker.

## Delivered scope

- Existing export wizard and own-profile **Connect phone app** entry.
- Reversible compact JSON; only encrypted byte arrays become base64.
- Actual binary encoder capacity checks; full-header fitted, zero-based frames
  preserve original bundle ID and Unicode code-point boundaries.
- Local canvas QR, four-module quiet zone, 1.5-second animation/progress.
- Session/render guards reject stale cooking completions after Back or reopen;
  QR close clears timers, payload and canvas.
- Existing crypto adapter, JSON Copy/Download format, and golden tests untouched.
  Phone reader and Drive excluded. No real spices or credentials used for proof.

## Runnable proof

From `chrome-extension` with the provisioned Linux runtime:

```sh
export PATH=/home/vantt/projects/passMan/.toolchain/node/bin:$PATH
pnpm exec vitest run test/unit/qr-serialize.spec.mjs test/unit/qr-chunking.spec.mjs test/unit/qr-display.spec.mjs test/unit/qr-wizard-lifetime.spec.mjs
DISPLAY=:0 pnpm exec playwright test test/e2e/export_qr.spec.ts --workers=1 --reporter=line
```

Final run: **15 unit tests across four files passed**, then **two headed Chromium
extension scenarios passed in 28.2 seconds**; both commands exited zero.
Independent `jsqr@1.4.0` decodes the rendered canvas. Small QR equals the exact
compact serializer output; all large animated frames reconstruct that same
output and deserialize to exact original byte arrays. Close/reopen cleanup and
deferred Back-during-cooking regressions pass. Syntax and `git diff --check` pass.
This scoped run does not claim the full pre-existing extension suite was run.

Independent reviewer subsequently observed **37/37 unit tests** (15 QR plus 22
unchanged golden recipe tests), **2/2 headed decoder E2E** in 29.0 seconds, and
real-vendor boundary/Unicode checks; no P1/P2 findings. Detailed commands and
limits: [independent review](../../../docs/history/qr-handoff/reports/recovery-independent-review.md).

## Provenance

Nayuki official upstream commit `3c6d0b3cefb4e049dc337e82237c9644399716a8`,
TypeScript compiled with pinned `typescript@5.9.3` to ES2020 classic JavaScript.
MIT license retained. Vendor SHA-256:
`79f419f267ce5a80d97f8099e0a789a4ecc4697b5348d86371a1f3b2a75d6e03`.
Compilation command/source link recorded in the vendor README. `jsqr@1.4.0`
is pinned only as a test dev dependency; extension QR has no runtime network.

## Remaining

Root owns impacted integration checks, product-only commit and local main merge.
Existing unrelated untracked Bee skill directories
were not changed and must stay outside this commit. Phone reader acceptance
requires its own later work; no phone transport end-to-end claim is made here.
No unresolved product questions within this accepted sender scope.

## Integration follow-up (2026-10-11)

QR commit `4a4395c` merged into local main at `3d3239d`. Independent fresh-main
checks passed 37 scoped unit tests and two headed canvas-decoding E2E scenarios.
Complete archive verified; checkout closed.
Branch and recovery refs retained; no remote push.
