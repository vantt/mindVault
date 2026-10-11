---
artifact_contract: bee-plan/v1
mode: standard
approved_gate2: 2026-07-27T10:43:40Z
---

# Plan: qr-handoff

**Status audit — 2026-10-11: Done (extension sender only).**
Phase1 QR button and Phase2 oversized-bundle chunking are Done. Commit `4a4395c`
merged at `3d3239d`;37 scoped unit tests and2 headed E2E independently decoded
actual canvases, then the checkout was archived/closed. Main/origin integration
is recorded in [worktree recovery](../../../docs/worktree-recovery.md).
See [verification](../261011-qr-handoff-worktree-recovery/reports/verification.md)
and [current transport contract](../../../docs/specs/qr-handoff.md).
The early `{id,...}` sketch below is Superseded by validated `{bundleId,idx,total,payload}`
framing and reversible compact JSON. Mobile reader/reassembly/import and Drive
remain Pending separate outcomes; no complete phone handoff is claimed.
Eligible for whole-plan archive. Phase3 Drive belongs to the broader mobile roadmap,
not this plan's two accepted sender phases.

Mode: `standard` — 1 risk flag: public contract (new QR frame-chunking format a future mobile app must parse).
Why this is the least workflow that protects the work: two coupled capabilities (single-QR export, then chunking for oversized bundles) touch ~5-6 product files across 2 sequenced slices and introduce a vendored crypto-adjacent library plus a cross-app data format — enough surface that a `small`-lane single gate would under-review it, but not enough distinct domains to be `high-risk`.

## Requirements (from CONTEXT.md)
No dedicated CONTEXT.md exists for this feature yet (scope-earlier shortcut, bee-hive). Requirements come from locked decisions already logged in `.bee/decisions.jsonl` and backlog PBI CoS:
- decision `deb89c4a`: add a "Kết nối app điện thoại" entry point on the Options page, reusing `ExportImportAdapter.createFullAccessBundle` (export-import-adapter.js:95) already used by the export wizard; render it as a QR.
- decision `97118ee8`: QR transport ships before Drive appData transport (Drive is Phase 3, explicitly out of scope here).
- PBI p-23394604 CoS: Options page has a button that turns the existing adapter bundle into a QR.
- PBI p-048c26bc CoS: an oversized bundle still transmits via QR safely, no data loss (chunking/animated multi-frame QR).

## Discovery
L1 (quick verify, no separate discovery.md). Inspected `options-export-wizard.js` (172 lines): `generateBundle()` already calls `adapter.createFullAccessBundle(...)` and renders the resulting `bundleJson` in a readonly textarea with Copy/Download buttons (lines 89-144) — this is the natural integration point, no new modal needed. Checked build tooling: no bundler (only `postinstall: copy_wasm.js` copies `argon2-browser`'s wasm/js dist into `src/wasm`); MV3 CSP forbids remote/eval'd code, so any QR encoder must ship as plain vendored JS, not a bundler-built npm import. Checked test layout: `chrome-extension/test/unit` (vitest) and `test/e2e` (playwright); no existing coverage for the export wizard or adapter at all.

## Approach

Recommended path: vendor Project Nayuki's `qrcodegen` (single-file, dependency-free, MIT/ISC, pure JS, widely used offline QR encoder with explicit ECC-level/version control needed for capacity planning) into `chrome-extension/src/vendor/qrcodegen.js`, per D-`deb89c4a`/`97118ee8`. Extend the existing export-wizard result step (reuse, no new modal) with a "📱 Hiện QR" action next to Copy/Download. For oversized bundles, split `bundleJson` into self-describing frames (`{id, idx, total, payload}`) cycled as an animated QR sequence — mobile-side reassembly is a separate, not-yet-built PBI (p-542801dc), so this slice only documents the frame header format, it does not implement a reader.

Rejected alternatives:
- npm `qrcode`/`qrcode-generator` package requiring a build step — rejected, no bundler exists in this extension and adding one is out of scope for a QR button.
- A brand-new dedicated "Connect phone" modal separate from the export wizard — rejected as YAGNI; the export wizard already produces the exact bundle needed, a new modal would duplicate the sheet-lock/password steps for no benefit.

Risk map:
| Component | Risk | Proof needed |
|---|---|---|
| QR capacity vs. encrypted bundle size | MEDIUM | confirm byte-mode capacity at ECC LOW across QR versions; unit-test the chunking threshold |
| Vendored lib CSP/MV3 compatibility | LOW | manifest.json CSP already forbids eval/remote; qrcodegen has neither |
| Cross-app frame format (extension writes, future mobile reads) | MEDIUM | frame header documented in code comments + test fixtures so p-542801dc has a concrete contract to implement against |

## Shape

**Phase plan** (chunking demoably builds on the QR button):

| Phase | What Changes | Why Now | Demo | Unlocks |
|---|---|---|---|---|
| 1 — Done: QR button (p-23394604) | Vendor `qrcodegen.js`; add "📱 Hiện QR" to the export wizard result step, rendering `bundleJson` as a QR canvas | Decisions already locked; smallest slice that satisfies p-23394604's CoS | Generate a bundle in Options → see a scannable QR on screen | Phase 2 (needs the render pipeline) |
| 2 — Done: QR chunking (p-048c26bc) | Add `qr-chunking.js` (pure split/frame module); wire it into the Phase-1 render path so oversized bundles cycle multiple QR frames with a progress indicator | Only meaningful once single-QR rendering exists | Export a profile with many secrets (bundle > single-QR capacity) → see an animated multi-frame QR with "N/M" indicator | Future mobile-side reassembly (p-542801dc, separate PBI) |

Current slice to prepare: **both phases**, as 2 sequenced cells (cell 2 depends on cell 1) — the design for both is already locked and neither is blocked on an unknown outcome from the other.

## Test matrix
One pass over the 12 edge dimensions, lane depth:
- **Boundary:** bundle exactly at single-QR byte capacity (no chunking) vs. one byte over (chunking kicks in) — covered by qr-chunking unit tests.
- **Empty/degenerate:** a profile with zero secrets still produces a valid (tiny) bundle/QR.
- **Data integrity:** concatenated chunk payloads reconstruct byte-identical `bundleJson` — covered by unit test.
- **Concurrency/UI state:** re-opening the wizard mid QR-display resets cleanly (existing `confirmClose` pattern).
- **Error surface:** encoder failure (should not happen with vendored pure-JS lib, but capacity overflow is user-visible, not a silent truncation).
- Other 7 dimensions (auth, external systems, perf, i18n, a11y, migration, rollback) are not material to this offline, client-side, additive UI feature — noted, not probed.

## Out of scope
- Mobile-side QR/frame reader (separate PBI p-542801dc).
- Drive appData transport (Phase 3 per decision `97118ee8`).
- Any change to `createFullAccessBundle`'s crypto or bundle JSON schema.
