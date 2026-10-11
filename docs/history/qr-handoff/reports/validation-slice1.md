# Validation report — qr-handoff, slice 1 (cells qr-handoff-1, qr-handoff-2)

## Reality gate

| Check | Result | Evidence |
|---|---|---|
| MODE FIT | PASS | 2 coupled capabilities, ~6-7 files across 2 sequenced cells, 1 cross-app-contract flag — standard is the least workflow that honestly covers this |
| REPO FIT | PASS (after repair) | export-import-adapter.js:95 `createFullAccessBundle`, options-export-wizard.js's existing result step (lines 89-144), no bundler in chrome-extension (only postinstall copy_wasm.js precedent) |
| ASSUMPTIONS | PASS (after repair) | Original assumption "vendor a ready-made qrcodegen.js" was FALSE — Nayuki's lib ships as `qrcodegen.ts`, compiles to a global IIFE (no ESM export), not importable via `import`. Corrected: classic `<script>` tag + global reference, verified by actually compiling the file (plan-checker did this) and by checking options.html's real script tag (`type="module"`) and manifest.json CSP |
| SMALLER PATH | PASS | Reusing the existing export wizard result step (no new modal) already is the smaller path; considered and rejected a dedicated "Connect phone" modal as YAGNI |
| PROOF SURFACE | PASS (after repair) | qrcodegen's real capacity-fit signal (`QrCode.encodeBinary` → `RangeError('Data too long')`) verified against the actual upstream source, not assumed; chunking logic redesigned with dependency-injected fit-check so it is unit-testable without a browser/jsdom global |

## Feasibility matrix

| Assumption | Risk | Proof required | Evidence | Result |
|---|---|---|---|---|
| qrcodegen vendoring mechanism | MEDIUM | Confirm actual upstream file shape and module system | Fetched & compiled `typescript-javascript/qrcodegen.ts` from github.com/nayuki/QR-Code-generator; output is a global IIFE, no exports (confirmed by `grep -c export` on the compiled output = 0) | PASS |
| npm package name collision | LOW | Check registry.npmjs.org/qrcodegen | Registry entry is Pitchview/qrcodejs (unrelated), not Nayuki's — do not `pnpm add qrcodegen` | PASS |
| QR byte-mode capacity vs. encrypted bundle size | MEDIUM | Real capacity-fit API, not a guessed constant | `QrCode.encodeBinary` throws `RangeError('Data too long')` at qrcodegen.ts:107 when data exceeds max version 40 at the requested ECC — used directly as the fit-check, no hardcoded byte budget | PASS |
| Existing bundle encoding wastes QR capacity | MEDIUM | Inspect actual bundle field encoding | export-import-adapter.js:104-114 stores encryptedData/iv/exportSalt as JSON decimal-number arrays (~4x less space-efficient than base64) and the wizard pretty-prints with indentation — both cut into scarce capacity; addressed with a QR-only compact re-serialization (base64 fields, no whitespace), leaving the existing Copy/Download textarea untouched | PASS |
| `cells schedule` — no cycles, valid wave shape | LOW | `bee cells schedule --feature qr-handoff` | 2 waves: [qr-handoff-1], [qr-handoff-2]; `diagnostics.cycles: []` | PASS |
| First-ever export-wizard test coverage is a real risk | MEDIUM | Existing e2e fixtures cover the setup this needs | `test/e2e/setup_flow.spec.ts` + `test-utils.ts`'s `setupExtension()` + `fixtures.ts` already drive unlock → profile → secrets; cell 1 reuses this pattern instead of inventing it | PASS |

## Plan-checker (adversarial, review tier / opus)

- **Iteration 1** (full 5-dimension check): 2 BLOCKERs (qrcodegen ESM/classic-script mismatch; cell 2 verify couldn't prove UI truths) + 5 WARNINGs (pnpm vs npx, missing package.json in cell 1 files for a devDependency that turned out unnecessary, undocumented payload-encoding choice, chunking-is-the-common-path capacity concern, decision-drift note on the "màn" wording — accepted as non-blocking). Requirement/decision coverage, dependency correctness, scope sanity: PASS.
- **Iteration 2** (cold-pickup cell review + blocker re-check): found the classic/module-script fix itself was correct, but surfaced 3 new CRITICALs from the repair: unit test couldn't reach the vendored global in jsdom, cell 1's e2e was still unproven/risky, cell 1's roundtrip truth had no verifier. All three repaired: dependency-injected `chunkForCapacity(str, fitsFn)` (unit-testable without any browser global) + `chunkBundleForQr` production wrapper; cell 1 e2e now reuses existing setup-flow fixtures; added `test/unit/qr-serialize.spec.mjs` for the roundtrip.
- **Iteration 3** (final, confirms repair): all 4 items (3 CRITICAL + 1 MINOR `Array.from` note) RESOLVED, no new blocker introduced. Two residual MINORs (default file location for the serialize helpers; don't read the `qrcodegen` global at module top level) folded directly into both cells' action text.

Full transcripts available via the review-tier subagent runs in this session; not persisted as separate files per decision 0009 (evidence lives in cell trace / this report, not a parallel evidence artifact).

## Decision

**READY** — cells `qr-handoff-1` and `qr-handoff-2` (current slice, both PBI p-23394604 and p-048c26bc) are cold-pickup ready, deps/wave-schedule clean, all must_haves have a real verifier.
