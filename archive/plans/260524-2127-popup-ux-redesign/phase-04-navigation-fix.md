# Phase 04 — Navigation Fix (D)

## Current acceptance — 2026-10-12

- Current status: **Done**.
- Generated Back re-queries current tab; stale detection canceled on navigation. Chrome closes a physical popover on tab switching: reopening on a non-Menu tab is the meaningful current behavior; a Back button in a closed popover is not a user flow.
- Evidence: [combined verification](../261012-extension-completion/reports/verification.md).
- Original dated requirements/failures remain below as history. Random picks,
  collapsed options, runtime locale selection and Precision styling supersede
  conflicting historical mechanisms; none is reintroduced to tick an old box.

## Current relevance audit — 2026-10-11

- Current status: **Done**; original recorded status: `complete`.
- Baseline: main `b5be3b0`; historical checklist/criteria below are retained. Historical audit; superseded by the2026-10-12 acceptance above.
- Evidence / remaining: chrome-extension/src/popup/popup.js:342–346 re-queries the active tab and re-detects in place; builder onBack at230 remains separate. No reload in Generated Back. Manual navigation acceptance remains in phase 08.
- Evidence paths above are repository-root-relative; source paths beginning src/ are inside chrome-extension/. No source/test changes or new runtime verification in this audit.

**Status:** Done
**Effort:** ~0.5h
**Dependency:** Phase 02 complete (needs `tryAutoDetect()` extracted)

---

## Context Links

- `chrome-extension/src/popup/popup.js` — `#btn-back` handler (line 243), `tryAutoDetect()` (extracted in Phase 02)

---

## Overview

**Problem:** Back from Generated → Home currently calls `showSection('status-unlocked')` with no re-detection. User returns to a stale Home screen showing the generic hint even when still on a Sheets tab.

**Also:** The current unlock handler uses `window.location.reload()` after successful unlock, which is a full page reload. This is acceptable for the unlock flow, but Back from Generated should NOT reload.

**Fix:** Back button calls `showSection('status-unlocked')` then immediately calls `tryAutoDetect(tab)` in-place. The `tab` reference is already in scope from the outer DOMContentLoaded block.

---

## Requirements

- Back from Generated → Home with in-place re-detection (no `window.location.reload()`)
- Re-detection uses the same `tryAutoDetect(tab)` function extracted in Phase 02
- Active tab must be re-queried (not cached) — user may have switched tabs
- Back from Builder → Home with NO re-detection (builder back is already correct behavior; sheet state is preserved in builder form anyway)

---

## Architecture

### Current Back handler (line 243)
```js
document.getElementById('btn-back').addEventListener('click', () => showSection('status-unlocked'));
```

### New Back handler
```js
document.getElementById('btn-back').addEventListener('click', async () => {
  showSection('status-unlocked');
  // Re-query active tab — user may have switched tabs while popup was open
  const [activeTab] = await chrome.tabs.query({ active: true, currentWindow: true });
  await tryAutoDetect(activeTab);
});
```

No changes to builder Back handler (`#btn-builder-back` → `onBack()` → `showSection('status-unlocked')`) — builder back intentionally does NOT re-detect.

---

## Related Code Files

### Modify
- `chrome-extension/src/popup/popup.js` — update `#btn-back` click handler only (~3 lines changed)

### Do Not Modify
- Any other file

---

## Implementation Steps

1. In `popup.js`, find the `#btn-back` event listener (currently line 243, may shift after Phase 02 edits)
2. Replace the one-liner with the async handler above
3. Ensure `tryAutoDetect` is defined before this handler executes (it will be, as a hoisted `async function` declaration or defined earlier in DOMContentLoaded)

---

## Todo List

- [x] Update `#btn-back` click handler to async + call `tryAutoDetect`
- [ ] Test: Generated → Back → on Sheets tab → amber notice or re-routes to Generated if cell valid
- [ ] Test: Generated → Back → switch to non-Sheets tab in between → generic hint shown

---

## Success Criteria

- Back from Generated re-runs detection against the current active tab
- If user is still on Sheets tab with valid cell: routes to Generated again
- If user is on Sheets tab with empty cell: Home with amber notice
- If user switched to non-Sheets tab: Home with generic hint
- No `window.location.reload()` called in this flow
- Builder Back still does NOT re-detect (unchanged)
