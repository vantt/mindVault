# Extension priorities1–5 — independent recheck

Date: 2026-10-12 (Asia/Saigon). Reviewer/tester: Agent A.
Baseline main: `2579e29044924e1582b78414b0314baffc72abe5`.
Accepted implementation: `0cba85be7358cfe83f658b133641b33010021c68`;
archive closure: `ae9a702ba04ff37b316cba89634ba474d6de27a0`.

**Verdict:** no additional relevant code gap or test regression found in the
accepted five extension slices. No extension source/test modification was needed.
The full MVP remains Partial; this recheck does not declare release/Sync work Done.

## Original priority and current scope

| Original priority | Plan | Rechecked status |
|---|---|---|
|1|[Extension MVP](../../docs/prd/v1-mvp.md)|Current Core/UI/Menu/security phases1–4 accepted; overall MVP Partial for broader phase5/6 outcomes.|
|2|[Recipe Builder](../../archive/plans/260522-0832-recipe-builder-popup/plan.md)|Done current scope; real worker previews, independent literal modifier/position Dishes, profile routing and stale/edit/reset controls passed.|
|3|[Verification Tag](../../archive/plans/260522-1314-recipe-verification-tag-sheet-binding/plan.md)|Done; matched/mismatched Menu/profile, no-Dish refusal, legacy warnings and selected EN/VI UI passed.|
|4|[Core infrastructure](../../archive/plans/legacy-core-infrastructure/plan.md)|Done bounded infrastructure/provider scope; existing native proof retained separately.|
|5|[Popup UX](../../archive/plans/260524-2127-popup-ux-redesign/plan.md)|Done current scope; actual toolbar/Home/Back/Quick Start, guarded profile modal, offline EN/VI Demo and entry/close paths passed.|

Current source/dossier preserves Argon2-only fail-closed, unchanged cooking/tag
grammar, Precision styling and random defaults/collapsed Builder options. Historical
stored-pick/auto-expansion mechanisms are superseded, not new missing deliverables.

## Fresh commands and results

Working directory: `chrome-extension/`. Existing dependencies were used; no install.

```sh
export PATH="/home/vantt/.nvm/versions/node/v24.18.0/bin:$PATH"
pnpm test:unit
DISPLAY=:0 pnpm exec playwright test --workers=1 --reporter=line
```

- Unit: **exit0,147/147 tests,20 files,3.78s**, no failure or skip.
- Headed Chromium: **exit0,25/25 E2E,4.7m**, one full sequential invocation,
  no failure or skip. This is a new full25-case run, distinct from the earlier22+3 proof.
- Actual browser tests cover real bundled Argon2/WASM, worker stop/wake lockout,
  competing setup, password rotation, profile-modal lifetime, two actual QR canvas
  decoder cases, actual action popover and real clipboard behavior. Owned Dish
  clears after30s with copying page closed; later user clipboard text survives.
- Production syntax: explicit `node --check` on all55 `src/**/*.js` files passed.
  Parsed manifest, both locale bundles and package JSON: **4/4 valid**.
- There is no extension build or lint script. No nonexistent build/lint pass is
  claimed; runtime loading, JS/JSON checks and scoped `git diff --check` passed.
- Expected invalid-Recipe test diagnostics and Node color warnings occurred;
  no assertion/runtime failure or retry workaround was needed.

## Preservation and evidence

All128 tracked extension files kept exact bytes/modes through the recheck;
extension diff against accepted implementation commit is empty. Original Golden
test SHA256 remains `5f9faa1e2bb5e3d38151250ffaa961a94efba459f41786f5ae90402ab07aae26`.
All23 unrelated Cave/skills-lock paths remained byte-identical. No Git mutation,
provider write, test rewrite or additional agent occurred during this recheck.

Local evidence: `/tmp/passchef-extension-recheck-evidence.json`,
`/tmp/passchef-extension-recheck-unit.log`, `/tmp/passchef-extension-recheck-e2e.log`.
[Prior scoped verification](../../archive/plans/261012-extension-completion/reports/verification.md)
retains repaired findings and the separate root-observed8/8 actual Google/native
proof. That provider proof was not rerun here; routed HTML/message contracts in
the fresh suite are explicitly fixtures, never newly claimed live/native results.

## Remaining broader scope

Cross-device Chrome Sync/conflicts, broad Chrome/OS/provider/security/performance
measurements and Chrome Web Store/release remain open MVP work. Deterministic
unit/API tests establish5/10-minute session boundaries; this run adds no real
elapsed5/10-minute browser matrix. Clipboard compare/write is not atomic against
unrelated applications, and OS history/sleep can outlive cleanup guarantees.
Mobile OAuth/phone acceptance and website/commercialization belong to other plans.

Unresolved questions: none blocking the accepted bounded extension slices.
