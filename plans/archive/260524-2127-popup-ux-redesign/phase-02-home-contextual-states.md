# Phase 02 — Home Screen Contextual States + Error Routing (B1)

**Status:** pending
**Effort:** ~1.5h
**Dependency:** Phase 01 complete

---

## Context Links

- `docs/ui-specs/03-popup-home.md` — updated spec (after Phase 01)
- `docs/ui-specs/04-popup-generated.md` — updated spec (success-only)
- `chrome-extension/src/popup/popup.html` — `#status-unlocked`, `#status-generated`
- `chrome-extension/src/popup/popup.js` — auto-detection block (lines 126–166), routing logic

---

## Overview

**Problem:** Currently ALL paths when on a Sheets tab route to `status-generated` — both success AND errors. This means users see an empty password field with a red error message on the "Generated" screen, which is confusing (that screen implies success).

**Fix:** Only `response.success === true` routes to `status-generated`. All error and empty-cell states stay on `status-unlocked` (Home) with contextual inline notices.

Additionally, when on a Sheets tab the Home screen shows the sheet name and a relevant contextual hint instead of the generic "Click any cell" text.

---

## Requirements

### Functional
- Success → `status-generated` (unchanged behavior)
- `response.error === "Empty cell"` → Home, amber notice: `hintEmptyCell`
- Any other `response.error` → Home, amber notice: `hintNeedsReload` (or generic)
- `catch(e)` where message is a connection error → Home, amber warning: `hintNeedsReload`
- `catch(e)` other → Home, amber warning with `e.message`
- On Sheets tab: show sheet name badge above hint (`📄 Budget 2026`)
- Not on Sheets tab: hide sheet badge, show generic `hintClickCell`

### Non-functional
- Sheet name extracted from `tab.title` using same `tabLabel()` logic as `sheet-detector.js` (strip " - Google Sheets" suffix)
- No window.location.reload() calls — in-place state updates only
- Error hint text must use i18n keys (no hardcoded English strings)

---

## Architecture

### New DOM elements in `#status-unlocked`

```html
<!-- Sheet context badge — hidden unless on Sheets tab -->
<p id="home-sheet-context" class="home-sheet-context hidden"></p>

<!-- Contextual hint (replaces static hintClickCell for error states) -->
<!-- #home-hint replaces the static <p data-i18n="hintClickCell"> -->
<p id="home-hint" class="hint"></p>

<!-- Inline notice for cell/connection errors (amber) -->
<div id="home-notice" class="home-notice hidden">
  <span id="home-notice-text"></span>
</div>
```

The existing `<p data-i18n="hintClickCell">` is replaced by `#home-hint` (same class, dynamic content). The static `data-i18n` attribute is removed — content set by JS.

### New CSS classes

```css
/* Sheet name badge */
.home-sheet-context {
  font-size: 0.78rem;
  color: var(--text-muted);
  margin: 0 0 6px;
}

/* Amber inline notice */
.home-notice {
  font-size: 0.78rem;
  color: var(--amber);
  background: rgba(210,153,34,.08);
  border: 1px solid rgba(210,153,34,.25);
  border-radius: 6px;
  padding: 6px 10px;
  margin-bottom: 10px;
  line-height: 1.4;
}
```

### JS refactor in `popup.js`

Extract the auto-detection block into a named function `tryAutoDetect(tab)`:

```js
async function tryAutoDetect(tab) {
  const homeHint    = document.getElementById('home-hint');
  const homeNotice  = document.getElementById('home-notice');
  const homeNoticeText = document.getElementById('home-notice-text');
  const homeContext = document.getElementById('home-sheet-context');

  // Reset notice state
  homeNotice.classList.add('hidden');
  homeContext.classList.add('hidden');

  if (!tab?.url?.includes('docs.google.com/spreadsheets')) {
    // Not on Sheets tab — generic hint
    homeHint.textContent = chrome.i18n.getMessage('hintClickCell');
    return;
  }

  // Show sheet name badge
  const sheetName = (tab.title || '').replace(/ [-–] Google Sheets$/i, '').trim();
  if (sheetName) {
    homeContext.textContent = chrome.i18n.getMessage('hintSheetContext', [sheetName]) || `📄 ${sheetName}`;
    homeContext.classList.remove('hidden');
  }

  try {
    const response = await chrome.tabs.sendMessage(tab.id, { action: 'GET_CURRENT_CELL_PASSWORD' });

    if (response?.success) {
      // SUCCESS ONLY → navigate to generated
      genPasswordInput.value = response.password;
      if (response.profileName) {
        const prefix = response.isShared ? '📥 ' : '';
        genProfileLabel.textContent = `${chrome.i18n.getMessage('lblProfile') || 'Profile'}: ${prefix}${response.profileName}`;
        genProfileLabel.classList.remove('hidden');
      }
      if (response.settings?.pepperingHint) {
        genHint.textContent = chrome.i18n.getMessage('hintPepperReminder') || "🔑 Don't forget your pepper!";
      }
      showSection('status-generated');
      return;
    }

    // Error → stay Home
    const msg = response?.error === 'Empty cell'
      ? chrome.i18n.getMessage('hintEmptyCell')
      : (chrome.i18n.getMessage('hintCellError') || `Error: ${response?.error}`);
    homeNoticeText.textContent = msg;
    homeNotice.classList.remove('hidden');

  } catch (e) {
    const isConnErr = e.message?.includes('Extension context invalidated')
      || e.message?.includes('Could not establish connection')
      || e.message?.includes('Receiving end does not exist');
    homeNoticeText.textContent = isConnErr
      ? chrome.i18n.getMessage('hintNeedsReload')
      : `⚠️ ${e.message}`;
    homeNotice.classList.remove('hidden');
  }
}
```

Call `tryAutoDetect(tab)` in the initial load (replacing the existing inline block) and expose it for Phase 4 (Back from Generated re-detection).

---

## Related Code Files

### Modify
- `chrome-extension/src/popup/popup.html` — add `#home-sheet-context`, `#home-hint`, `#home-notice` to `#status-unlocked`; remove static `data-i18n="hintClickCell"` from old hint `<p>`
- `chrome-extension/src/popup/popup.js` — extract `tryAutoDetect()`, call on load; remove inline auto-detect block; wire Back from Generated to call `tryAutoDetect()` (Phase 4)
- `chrome-extension/src/popup/popup.css` — add `.home-sheet-context` + `.home-notice` styles

### Do Not Modify
- `sheet-detector.js` — no changes needed
- `popup-recipe-builder.js` — no changes

---

## Implementation Steps

1. **popup.html** — inside `#status-unlocked`, after `<header>` equivalent:
   - Replace `<p class="hint" data-i18n="hintClickCell">` with:
     ```html
     <p id="home-sheet-context" class="home-sheet-context hidden"></p>
     <p id="home-hint" class="hint"></p>
     <div id="home-notice" class="home-notice hidden">
       <span id="home-notice-text"></span>
     </div>
     ```
   - Keep `#profiles-summary` element unchanged

2. **popup.css** — append `.home-sheet-context` and `.home-notice` blocks (see Architecture section above)

3. **popup.js** — extract `tryAutoDetect(tab)` function:
   - Accepts active `tab` object (already queried in DOMContentLoaded)
   - Contains all the routing logic from current lines 126–166
   - Returns void; calls `showSection('status-generated')` on success only
   - On error: populates `#home-notice`, stays on Home
   - Initialize `homeHint` to `hintClickCell` text at bottom of function (sets default regardless of path)

4. **popup.js** — in DOMContentLoaded init block:
   - Query active tab first: `const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });`
   - Call `await tryAutoDetect(tab);` (replaces lines 126–166 inline block)
   - Store `tab` reference in outer scope so Phase 4 can re-call `tryAutoDetect(tab)`

5. **popup.js** — update `status-generated` error hint references:
   - Remove `genHint.style.color = "#da3633"` calls from the old error paths (those paths no longer reach Generated)
   - `genHint` is now only ever set on success (pepper hint only)

6. **Verify** popup.css file length stays under 200 lines — if not, extract theme variables + section-specific styles into separate files

---

## Todo List

- [ ] Modify `popup.html` — replace static hint with 3 new elements in `#status-unlocked`
- [ ] Add `.home-sheet-context` + `.home-notice` CSS to `popup.css`
- [ ] Extract `tryAutoDetect(tab)` in `popup.js`
- [ ] Call `tryAutoDetect(tab)` in init block
- [ ] Verify `status-generated` no longer receives error states
- [ ] Manual test: open popup on Sheets tab with empty cell → see amber notice on Home
- [ ] Manual test: open popup on Sheets tab with valid cell → see Generated screen with password
- [ ] Manual test: open popup not on Sheets → see generic hint, no badge

---

## Success Criteria

- Generated screen = green pill + non-empty password ONLY
- Empty cell → Home amber notice "Selected cell is empty or has no recipe."
- Connection error → Home amber warning "Extension needs tab reload."
- Sheet name badge appears only when on Sheets tab
- No hardcoded error strings in popup.js (all i18n keyed)
- `popup.css` remains under 200 lines (or is split)

---

## Risk Assessment

| Risk | Mitigation |
|------|-----------|
| `tab` reference stale when Back re-runs detection | Phase 4 handles by re-querying active tab |
| `chrome.i18n.getMessage` returns empty string | Always provide fallback string in function call |
| `popup.css` grows past 200 lines | Split into `popup-home.css` if needed |

---

## Security Considerations

- No user input is rendered as HTML (textContent only) — no XSS risk
- `e.message` from caught errors rendered as textContent (safe)
