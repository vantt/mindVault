# Phase 09 — E2E (Playwright) Mismatch Flow

## Completion audit — 2026-10-12

- Current status: **Done**; evidence: [completion verification](../../../plans/261012-extension-completion/reports/verification.md) (independent 147 units  + 22 full headed E2E; real Google/native 8/8 separately).
- Independent22-test real headed extension suite passes: popup-built tags, wrong Menu, same-Menu wrong profile, absent Menu and legacy warnings in EN/VI; exact Dish/absence assertions. Routed HTML is a Menu contract fixture with real manifest/content/worker/UI, not live Google. Separate8/8 anonymous real Google/native cases pass without message-injection fallback.
- Original criteria and prior audit are retained below. Headed automation exercises the actual extension UI; neither routed fixtures nor SW-only checks are presented as live-provider or human-manual signoff.
- Superseded test mode: the current accepted extension harness is headed Chromium 143.0.7499.4; no headless run or human-manual signoff is claimed. Auth setup is genuine Argon2/AES, replacing historical pre-set session-key suggestions.

## Historical relevance audit — 2026-10-11

- Audit status at baseline: **Partial**; original recorded status: `completed`.
- Baseline: main `b5be3b0`; historical checklist/criteria below are retained. This was the baseline audit; the 2026-10-12 audit above governs current status. Old ticks alone did not certify acceptance.
- Evidence / remaining: chrome-extension/test/e2e/recipe-mismatch.spec.ts:11–69 uses a genuine loaded extension and checks match/mismatch/legacy through SW. It bypasses popup building, two-profile mapping and content toast; header explicitly leaves toast to manual QA. Fixture runs headless:false. Historical six-E2E report is reported evidence, not dual headed/headless or live Google signoff.
- Evidence paths above are repository-root-relative; source paths beginning src/ are inside chrome-extension/. No source/test changes or new runtime verification in this audit.

## Context Links
- Existing E2E suite (search `chrome-extension/test/e2e/`)
- Phases 05–07 (SW, content, i18n)
- Parent plan: `plan.md`

## Overview
- **Priority:** P2
- **Status:** Done
- **Description:** End-to-end test: build tagged recipe in popup for sheet A; switch to sheet B mapped to a different profile; trigger generation → verify mismatch toast appears.

## Key Insights
- Reuse existing E2E harness pattern (Playwright + extension loaded).
- Two profiles needed in storage; two sheets in `sheetMapping` mapping each sheet to its profile.
- Sheets do not need real Google Docs URLs — `chrome.tabs` API is mocked by URL, content script reads `window.location.pathname`. For E2E with real browser, use local HTML fixtures with URL `/spreadsheets/d/<id>/` rewritten via mock server OR test through SW message-passing directly.

## Requirements
**Functional Test Cases**
1. **Happy path (tag verifies):**
   - Setup: Profile_A + Profile_B with distinct secrets; sheetMapping {sheetA: Profile_A, sheetB: Profile_B}.
   - Build recipe in popup, sheet URL = sheetA → recipe has tag.
   - On sheet A page, send `GENERATE_PASSWORD {text: tagged, sheetId: sheetA}` → success.
2. **Mismatch path:**
   - Build recipe with sheet URL sheetA → tag.
   - Send `GENERATE_PASSWORD {text: tagged, sheetId: sheetB}` → response `code === "RECIPE_MISMATCH"`.
   - Content script toast contains localized mismatch message.
3. **Legacy (untagged) still works:**
   - Build recipe with sheet URL blank → no tag.
   - Send on any sheet → success, response `warning === "legacy_no_tag"` (if sheetId present).

**Non-functional**
- Each test isolated (fresh extension storage per run).
- Run on CI headless.

## Architecture
```
e2e/recipe-mismatch.spec.mjs
  beforeEach: seed chrome.storage.sync with two profiles + sheetMapping
  test 1: SW message → expect success
  test 2: SW message with wrong sheet → expect code mismatch
  test 3: untagged + sheet → expect warning
```

## Related Code Files
**Create:**
- `chrome-extension/test/e2e/recipe-mismatch.spec.mjs`

**Possibly modify (helpers):**
- `chrome-extension/test/e2e/helpers/seed-storage.mjs` (if exists; create if not)

## Implementation Steps
1. Inspect existing E2E setup to confirm Playwright pattern + extension loading.
2. Create helper to seed two profiles via `chrome.storage.sync.set` (call from a privileged context — `background.html` or an extension page).
3. Open popup, navigate builder, fill form including sheet URL → assert recipe output contains `.` separator + 4 chars.
4. Use `chrome.runtime.sendMessage` from a background fixture to invoke generation against wrong sheet → expect mismatch response.
5. Validate toast DOM in a sheet fixture page that triggers content script hotkey path.

## Todo List
- [x] Locate existing E2E setup pattern
- [x] Storage seeding helper
- [x] Test 1: happy path
- [x] Test 2: mismatch path
- [x] Test 3: legacy untagged
- [x] Included in the ordinary Playwright suite; separate-workflow condition does not apply.

## Success Criteria
- All 3 scenarios pass headless and headed.
- Tests reliable (no flaky timing) — use Playwright `expect.poll` for SW responses.

## Risk Assessment
- **R1:** Google Sheets real URLs not viable in CI. Mitigation: use local fixture HTML at `file://` or local server with `/spreadsheets/d/...` path; the regex in content script only inspects pathname.
- **R2:** Vault locked at test start. Mitigation: pre-set `sessionKey` in `chrome.storage.session` via fixture.

## Security Considerations
- Test secrets are dummy; never commit real keys.

## Next Steps
- Phase 10 updates docs.

## Historical unresolved questions
- Q7: Does existing E2E suite already mock Google Sheets URLs? If not, scope adds ~1h. Confirm during implementation.

## Current unresolved questions

None. Resolved: routed Menu HTML supplies deterministic contract proof; separate actual public Google/native 8/8 supplies provider acceptance.
