# Phase 05 — Quick Start Panel (A)

**Status:** complete
**Effort:** ~1h
**Dependency:** Phase 02 complete (Home contextual states in place), Phase 04 complete (navigation wired)

---

## Context Links

- `docs/ui-specs/03-popup-home.md` — updated spec with Quick Start layout
- `chrome-extension/src/popup/popup.html` — `#status-unlocked`
- `chrome-extension/src/popup/popup.js` — DOMContentLoaded init, `#btn-build-recipe` handler
- `chrome-extension/src/popup/popup.css`

---

## Overview

A dismissible "How it works" panel shown on the Home screen for first-time users. Once dismissed it never shows again. While the panel is visible, the primary CTA changes from "Build Recipe" to "Create First Recipe".

---

## Requirements

### Functional
- Show panel when `hasSeenQuickStart` is absent (undefined/null) in `chrome.storage.local`
- Panel content: title "How it works" + 3 numbered steps
- "Got it ✓" button: set `hasSeenQuickStart = true` in local storage, hide panel, reset CTA text
- "Learn more →" button: open demo page via `chrome.tabs.create()`, also dismiss (set flag + hide)
- While panel visible: `#btn-build-recipe` text = i18n `btnCreateFirstRecipe` ("Create First Recipe")
- After dismiss: `#btn-build-recipe` text = i18n `btnBuildRecipe` ("Build Recipe")
- Panel has a close `×` icon button (top-right) — same behavior as "Got it ✓"

### Non-functional
- `hasSeenQuickStart` stored in `chrome.storage.local` (not sync — device-local UX state)
- Panel visibility check is async (storage read) — happens after `showSection('status-unlocked')`
- All panel strings are i18n'd
- Panel is in static HTML (not injected by JS) — hidden by default, shown by JS after storage check

---

## Architecture

### New DOM in `#status-unlocked`

Insert before `#home-sheet-context` (top of the unlocked section):

```html
<div id="quick-start-panel" class="quick-start-panel hidden">
  <div class="qs-header">
    <span class="qs-title" data-i18n="quickStartTitle">How it works</span>
    <button id="btn-qs-close" class="btn-qs-close" aria-label="Dismiss">×</button>
  </div>
  <ol class="qs-steps">
    <li data-i18n="quickStartStep1">Build a recipe</li>
    <li data-i18n="quickStartStep2">Paste it into a Google Sheets cell</li>
    <li data-i18n="quickStartStep3">Click the cell → password appears</li>
  </ol>
  <div class="qs-actions">
    <button id="btn-qs-got-it" class="btn secondary btn-sm" data-i18n="btnGotIt">Got it ✓</button>
    <button id="btn-qs-learn-more" class="btn primary btn-sm" data-i18n="linkLearnMore">Learn more →</button>
  </div>
</div>
```

### New CSS

```css
/* Quick Start panel */
.quick-start-panel {
  background: var(--surface2);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 10px 12px;
  margin-bottom: 10px;
}
.qs-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}
.qs-title {
  font-size: 0.78rem;
  font-weight: 600;
  color: var(--text);
}
.btn-qs-close {
  background: none;
  border: none;
  color: var(--text-muted);
  cursor: pointer;
  font-size: 1rem;
  line-height: 1;
  padding: 0 2px;
}
.btn-qs-close:hover { color: var(--text); }
.qs-steps {
  font-size: 0.75rem;
  color: var(--text-muted);
  padding-left: 18px;
  margin-bottom: 8px;
  line-height: 1.6;
}
.qs-steps li { margin-bottom: 1px; }
.qs-actions {
  display: flex;
  gap: 6px;
}
/* Small button variant for panel actions */
.btn-sm {
  padding: 5px 10px;
  font-size: 0.75rem;
}
```

### JS logic in `popup.js`

Add `initQuickStart()` async function, called after `showSection('status-unlocked')` in the init block:

```js
async function initQuickStart() {
  const panel     = document.getElementById('quick-start-panel');
  const buildBtn  = document.getElementById('btn-build-recipe');
  if (!panel || !buildBtn) return;

  const { hasSeenQuickStart } = await chrome.storage.local.get('hasSeenQuickStart');
  if (hasSeenQuickStart) return; // Already dismissed — nothing to do

  // Show panel + update CTA
  panel.classList.remove('hidden');
  buildBtn.textContent = chrome.i18n.getMessage('btnCreateFirstRecipe') || 'Create First Recipe';

  async function dismiss() {
    await chrome.storage.local.set({ hasSeenQuickStart: true });
    panel.classList.add('hidden');
    buildBtn.textContent = chrome.i18n.getMessage('btnBuildRecipe') || 'Build Recipe';
  }

  document.getElementById('btn-qs-close')?.addEventListener('click', dismiss);
  document.getElementById('btn-qs-got-it')?.addEventListener('click', dismiss);
  document.getElementById('btn-qs-learn-more')?.addEventListener('click', async () => {
    await dismiss();
    chrome.tabs.create({ url: chrome.runtime.getURL('demo/demo.html') });
  });
}
```

Call site in DOMContentLoaded (after `showSection('status-unlocked')`, before `tryAutoDetect()`):
```js
// Only show quick start on Home screen (not if auto-routing to Generated)
// initQuickStart runs first; tryAutoDetect may route away — that's fine,
// panel is only visible if we stay on Home.
await initQuickStart();
await tryAutoDetect(tab);
```

Order matters: `initQuickStart()` runs before `tryAutoDetect()`. If `tryAutoDetect()` routes to Generated, the panel is irrelevant (hidden under `status-unlocked` which is not visible). On next popup open from a non-Sheets tab, the panel will be visible if not yet dismissed.

---

## Related Code Files

### Modify
- `chrome-extension/src/popup/popup.html` — add `#quick-start-panel` inside `#status-unlocked`
- `chrome-extension/src/popup/popup.js` — add `initQuickStart()`, call in init block
- `chrome-extension/src/popup/popup.css` — add `.quick-start-panel` and related styles

### Do Not Modify
- `popup-recipe-builder.js`, `sheet-detector.js`

---

## Implementation Steps

1. **popup.html** — add `#quick-start-panel` div as first child of `#status-unlocked` (before `#home-sheet-context`)

2. **popup.css** — append quick-start styles block (~30 lines); also add `.btn-sm` modifier

3. **popup.js** — add `initQuickStart()` function; call it in the unlocked branch of the init flow, before `tryAutoDetect()`

4. **Verify panel + CTA interaction:**
   - Clear `hasSeenQuickStart` from local storage (DevTools → Application → Local Storage)
   - Open popup → panel visible, CTA = "Create First Recipe"
   - Click "Got it ✓" → panel hides, CTA = "Build Recipe"
   - Close + reopen popup → panel NOT shown

5. **Verify "Learn more →":**
   - Click → new tab opens `demo/demo.html` (or 404 until Phase 06 creates the file — that's OK)
   - Panel dismissed after click

---

## Todo List

- [x] Add `#quick-start-panel` HTML to `#status-unlocked`
- [x] Add quick-start + `.btn-sm` CSS
- [x] Add `initQuickStart()` to `popup.js`
- [x] Call `initQuickStart()` before `tryAutoDetect()` in init block
- [ ] Test: first open → panel visible, CTA changed
- [ ] Test: dismiss → panel gone, CTA restored
- [ ] Test: reopen → panel still gone
- [ ] Test: "Learn more →" → demo tab opens + panel dismissed

---

## Success Criteria

- Panel shows exactly once per device (controlled by `hasSeenQuickStart` in local storage)
- CTA button text changes to "Create First Recipe" while panel is visible
- All three dismissal paths (×, Got it ✓, Learn more →) work correctly
- Panel is not shown when popup opens on a Sheets tab with a valid cell (user is immediately routed to Generated — panel never visible in that flow)
- All strings i18n'd

---

## Risk Assessment

| Risk | Mitigation |
|------|-----------|
| Storage read fails (incognito/restricted) | Wrap in try/catch; default to "not seen" → show panel |
| `demo/demo.html` doesn't exist yet (Phase 06) | `chrome.tabs.create` silently creates a tab with error page — acceptable during development |
| Panel visible during auto-route to Generated | Not a problem — `#status-unlocked` is hidden after `showSection('status-generated')` |
