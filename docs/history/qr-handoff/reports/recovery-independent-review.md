# QR sender recovery — independent review

Date: 2026-10-11. Checkout: `/home/vantt/projects/passMan--wt--qr-handoff`.
Comparison: local main `b097b9c`; product source frozen after the render-version
guard and three wizard lifetime regressions. No source edits by this reviewer.

**Result: no P1/P2 findings in the accepted sender scope.** Integration remains
pending the owner's product commit and merge. This is ordinary independent
review; historical workflow state was not changed.

## Scope and evidence

- Read accepted `docs/history/qr-handoff/plan.md`, validation report, original
  `qr-handoff-1`/`qr-handoff-2` acceptance criteria, and recovery execution plan.
  Phone reader/reassembly and Drive remain outside this delivery.
- Nayuki classic script precedes the Options module. Existing MV3 CSP is
  unchanged. Vendor code has no runtime fetch, eval, or Function construction.
  MIT notice retained; README pins upstream commit
  `3c6d0b3cefb4e049dc337e82237c9644399716a8` and TypeScript 5.9.3 compilation.
  Local SHA256 matches documented compiled output:
  `79f419f267ce5a80d97f8099e0a789a4ecc4697b5348d86371a1f3b2a75d6e03`.
  Checked the pinned [upstream source](https://github.com/nayuki/QR-Code-generator/blob/3c6d0b3cefb4e049dc337e82237c9644399716a8/typescript-javascript/qrcodegen.ts).
  An independent TypeScript recompilation was not repeated.
- Only encryptedData/iv/exportSalt become base64; metadata and original arrays
  roundtrip without mutation. Original pretty Copy/Download JSON is preserved.
- Production fitting uses actual binary encoder capacity. Headers and JSON
  escaping count toward capacity; conservative total budgeting handles digit
  growth. Unicode code points remain intact. Frame indices are zero-based and
  every frame preserves the original bundle ID/final count.
- Canvas has an integer-pixel grid and four-module quiet zone. Repeated Show QR
  restarts one animation owner. Close/Done/reopen clear payload, canvas and timer;
  safe error fallback exposes no encoder exception or private payload.
- Wizard captures both session identity and render version after storage,
  decryption and bundle awaits, including catch. Back during cooking/decryption
  and close/reopen another profile cannot install an obsolete result.
- Existing crypto adapter, AES helper, WASM files, manifest, golden recipe test
  and production recipe engine are unchanged against the comparison revision.
  Clipboard/download handlers retain their original JSON path; download now
  revokes its temporary object URL. No cryptographic format change introduced.

## Commands and observed results

Portable runtime used for every Node command:

```bash
export PATH=/home/vantt/projects/passMan/.toolchain/node/bin:$PATH
pnpm --dir chrome-extension install --frozen-lockfile
pnpm --dir chrome-extension exec vitest run test/unit/qr-serialize.spec.mjs test/unit/qr-chunking.spec.mjs test/unit/qr-display.spec.mjs test/unit/qr-wizard-lifetime.spec.mjs test/unit/generate_password.spec.mjs
DISPLAY=:0 pnpm --dir chrome-extension exec playwright test test/e2e/export_qr.spec.ts --headed --workers=1 --reporter=line
git diff --check
```

- Frozen install: exit 0; lockfile already current. Existing WASM-copy lifecycle
  ran; resulting WASM files have no tracked diff. pnpm reported its existing
  ignored esbuild build script warning; focused Vitest ran successfully.
- Vitest: exit 0; five files, **37/37 tests** (15 QR + 22 original recipe golden),
  no skipped tests; 1.02 seconds.
- Headed Chromium: exit 0; **2/2 tests**, 29.0 seconds. Test-only pinned jsQR 1.4.0
  independently decoded actual displayed canvas pixels for single and animated
  frames, reconstructed compact JSON, and recovered original byte arrays exactly.
  Both also exercised wizard completion/close cleanup; single case reopened.
- Additional read-only `node --input-type=module` assertions loaded the actual
  vendored encoder: exactly **2953 UTF-8 bytes** fit one version-40 QR; **2954**
  throws overflow and splits into two fitted frames. A **9672-byte** Unicode and
  JSON-escaping fixture split into six actual fitted frames, reconstructed
  exactly, and preserved surrogate-pair boundaries. Exit 0.
- Whitespace validation: exit 0. Newly authored sender modules are under 200
  lines; updated wizard is 199 lines. No dedicated lint script exists.
  The later staged check found five compiler-output trailing-space lines in the
  previously untracked vendor file. A vendor-only Git attribute preserves its
  verified bytes; the staged check then passed with normal checks on authored code.

## Limits

This proves the extension sender, not a physical phone camera or reader. E2E
decodes intrinsic canvas pixels; camera distance, screen scaling and glare were
not measured. Clipboard contents and downloaded-file IO were inspected in code,
not exercised by the two scoped E2E cases. Broad legacy E2E was not run. Other
unrelated tooling WIP was excluded. No unresolved P1/P2 or scope question found.
