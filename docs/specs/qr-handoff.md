---
area: qr-handoff
updated: 2026-10-11
coverage: sender-only
---

# Phone cookbook handoff

An unlocked Chef can select **Connect phone app** on an own-profile card in
Options. The existing export wizard asks for an optional Menu and a sharing
password, cooks the existing encrypted full-access bundle, then displays QR.
The ordinary export action offers **Show QR** beside unchanged Copy/Download.
The phone reader is a separate, unfinished feature; displaying QR does not claim
that a phone app can already import it. Drive transport remains outside this work.

## Behavior

A small bundle appears as one QR. A large bundle cycles through fitted frames
every 1.5 seconds with `QR i/total` progress. The encoder runs locally; no bundle,
sharing password, or spices are sent to a network service. QR contains encrypted
spices and the same visible metadata as JSON export, never the sharing password.
The recipient still needs the sharing password to open the cookbook.

Closing, completing, or reopening the wizard clears its QR canvas and timers.
Closing during cooking prevents a stale result from replacing a newer session.
Encoder errors show an actionable download fallback without exposing payloads.
Existing bundle encryption, JSON export format, and Copy/Download stay intact.

## Transport contract for a future reader

Compact the original JSON with no whitespace; preserve all metadata. Convert
only `encryptedData`, `iv`, and `exportSalt` integer-byte arrays to standard padded
base64 strings. Reverse those three fields to recover the exact original bundle.

A single QR contains this compact JSON directly. A multi-frame QR contains:

```json
{"bundleId":"original-bundle-id","idx":0,"total":2,"payload":"plain substring of compact JSON"}
```

`idx` is zero-based; `total` is the final frame count, and every frame shares
the original bundle's `bundleId`. `payload` is a plain substring, with Unicode
code-point boundaries preserved. JSON escaping is undone before concatenation.
Reconstruct every index in order, reject conflicting IDs/counts/duplicate values,
then reverse the three base64 byte fields. Do not treat unauthenticated headers
or visible metadata as proof of identity or correctness. Decryption and bundle
validation belong to the existing full-access import contract.

The sender fits the complete frame JSON, including headers and escaping, using
the real encoder's binary-mode capacity signal. No fixed byte budget is assumed.
The four-module white quiet zone surrounds every QR; rendering uses integer pixels.

## Evidence and implementation

- Existing accepted plan: `docs/history/qr-handoff/plan.md`.
- Recovery execution plan: `plans/261011-qr-handoff-worktree-recovery/plan.md`.
- `chrome-extension/src/adapters/infrastructure/qr-serialization.js` and
  `qr-chunking.js`: reversible transport and fitted framing.
- `chrome-extension/src/options/options-qr-display.js`: canvas and timer owner.
- `chrome-extension/src/vendor/README.md`: pinned MIT encoder provenance.
- Unit regressions and headed extension E2E use synthetic spices only. An
  independent test-only decoder reads the actual canvas for single and multiple
  frames, reconstructing compact JSON and original byte arrays exactly.
