# Complete the five remaining extension slices

Date: 2026-10-12 (Asia/Saigon). Status: Done — integrated and pushed to main.
Scope: current extension security, Builder, Tag UI, Menu QA and popup/demo polish.
This executes remaining outcomes of existing plans; it does not recreate completed
Core/QR/mobile work or include Chrome Web Store publication.

## Decisions and boundaries

- Settled quality choice: retain Argon2id-only fail-closed behavior; historical vault
  PBKDF2-SHA256600k fallback is Superseded. Existing Argon2 parameters and encrypted
  vault format stay unchanged; sharing-bundle PBKDF2 100k remains its separate contract.
- Owner supplied a public synthetic Cookbook; anonymous read/edit UI works.
  Routed local HTML is contract proof only, never a real Google result.
- Existing golden/approved tests immutable. Preserve tag/password compatibility,
  current Precision styling and random Builder defaults from later decisions.
- Work limited to `chrome-extension/` and its current plans/specs/reports. No Bee,
  mobile/landing changes, real credentials, existing browser profiles or release.
- Minimal `offscreen` + `clipboardRead` permissions are justified by compare-and-
  clear after popup closure; no broad hosts or `scripting` for test injection.

## Execution phases

| # | Slice | Status | Dependency/detail |
|---|---|---|---|
| 1 | Security/session and copied-dish lifetime | Done | [phase01](phase-01-security-session-clipboard.md); Argon2-only policy settled |
| 2 | Builder asynchronous ownership | Done | [phase02](phase-02-builder-lifecycle.md); independent code work may proceed after plan |
| 3 | Tag errors/warnings and localized UI acceptance | Done | [phase03](phase-03-tag-ui-contract.md); integrates phase2 |
| 4 | Current Core/Menu/hotkey QA | Done | [phase04](phase-04-menu-core-acceptance.md); combines1–3; real Google proof depends on URL |
| 5 | Demo/i18n/spec polish and combined acceptance | Done | [phase05](phase-05-popup-demo-polish.md); localization can parallel2–3, final QA after1–4 |

## Delivery TODOs

1. [x] Record baseline HEAD/dirty paths and preserve unrelated Caveman WIP.
2. [x] Implement security and independent Builder repairs in small helpers;
   source files touched by extraction must remain understandable and under200 lines.
3. [x] Complete Tag presentation and demo localization/offline behavior; avoid
   changing cooking/tag algorithms, shared-profile binding or bundle encryption.
4. [x] Run meaningful unit regressions and actual headed extension UI scenarios,
   using temporary profiles and synthetic ingredients/spices only.
5. [x] Independently review frozen source/permissions/lifecycle, repair findings,
   then tester validates final impacted scope. No author self-approval.
6. [x] Run existing unit/E2E suites as applicable; report old failures separately
   without weakening tests, replacing keyboard failure with message injection,
   or silently skipping accepted outcomes.
7. [x] Update existing five plan/phase statuses and current specs only from actual
   evidence. Archive a whole plan only when no relevant outcome remains.
8. [x] Root commits/pushes scoped reviewed work; verify remote SHA. Actual Google/native hotkey acceptance is complete.

## Verification entry points

From `chrome-extension`, Linux Node24.18/pnpm10.24 and real WSLg `DISPLAY=:0`:

```sh
export PATH="/home/vantt/.nvm/versions/node/v24.18.0/bin:$PATH"
pnpm test:unit
DISPLAY=:0 pnpm test:e2e -- --workers=1 --reporter=line
```

Phase files name focused regressions and additional headed scenarios. Use actual
browser Clipboard/Argon2/storage paths for integration proof; clock/API doubles
establish deterministic control behavior only. No overnight completion guarantee.

## Unresolved questions

Implementation and independent acceptance complete; main commit0cba85b pushed
and remote SHA verified. See [delivery](reports/delivery.md).
See [verification](reports/verification.md) and [review](reports/independent-review.md).
Full MVP release/Sync/platform measurements remain outside this bounded closure.
