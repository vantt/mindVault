# Phase 08 — Testing + Code Review

**Status:** pending
**Effort:** ~1h
**Dependency:** Phases 02–07 all complete

---

## Context Links

- `chrome-extension/` — all modified files
- Vitest available at `chrome-extension/node_modules/.bin/vitest`
- Playwright available at `chrome-extension/node_modules/.bin/playwright`

---

## Overview

Verify all redesign changes work correctly via manual smoke tests (no test framework currently in place for the popup — the `tests/` directory does not exist yet). Also perform a code review pass against the development rules.

Since there are no existing Vitest/Playwright test files, this phase focuses on:
1. Manual verification checklist (all user flows)
2. Code review against YAGNI/KISS/DRY + file size rules
3. i18n smoke test (switch locale, verify key strings appear)

---

## Manual Test Checklist

### Setup preconditions

- Extension loaded in Chrome developer mode (`chrome-extension/src/`)
- At least one profile + secrets configured (setup complete, extension unlocked)
- A Google Sheet open in a tab with at least one cell containing a valid recipe
- A second Chrome tab open on a non-Sheets page

---

### Flow A — Home screen, not on Sheets tab

| Step | Expected |
|------|----------|
| Open popup (active tab = non-Sheets) | Home screen shown, generic hint: "Click any cell in Google Sheets containing a recipe." |
| No sheet badge visible | `#home-sheet-context` has class `hidden` |
| No amber notice | `#home-notice` has class `hidden` |
| First open ever (no `hasSeenQuickStart`) | Quick Start panel visible, CTA = "Create First Recipe" |
| Click "Got it ✓" | Panel hides, CTA = "Build Recipe" |
| Reopen popup | Panel NOT shown, CTA = "Build Recipe" |

---

### Flow B — Home screen on Sheets tab, empty cell

| Step | Expected |
|------|----------|
| Navigate to Sheets tab, click an empty cell | Active tab is Sheets |
| Open popup | Home screen shown (NOT Generated) |
| Sheet name badge visible | "📄 [sheet name]" shown |
| Amber notice visible | "Selected cell is empty or has no recipe." |
| No password displayed | `#gen-password` not shown (not on Generated) |

---

### Flow C — Generated screen (success)

| Step | Expected |
|------|----------|
| Navigate to Sheets tab, click cell with valid recipe | — |
| Open popup | Auto-routes to Generated screen |
| Green status pill | "🟢 Ready" |
| Password displayed | Non-empty value in `#gen-password` |
| No amber notice anywhere | Home screen not visible |
| Click Copy → "Copied!" flash → "Copy" restored | Clipboard contains password |

---

### Flow D — Back from Generated

| Step | Expected |
|------|----------|
| From Generated screen, click Back | Home screen shown |
| Still on Sheets tab | Re-detection runs; if cell still valid → routes back to Generated |
| Switch to non-Sheets tab, then click Back on Generated | Home shows generic hint, no sheet badge |

---

### Flow E — Builder, first-time user

| Step | Expected |
|------|----------|
| Clear `lastUsedPosition` + `lastUsedSecret` from chrome.storage.local | — |
| Click "Build Recipe" from Home | Builder opens |
| All position toggles unselected | No `.active` button in `#bld-position-group` |
| All secret toggles unselected | No `.active` button in `#bld-secret-group` |
| "More options" section collapsed | `<details>` has no `open` attribute |
| Subtitles visible under Hash, Position, Secret | `.builder-subtitle` elements visible |
| "? How it works" link visible in header | `#btn-how-it-works` visible |
| Select position + secret + type hash | Recipe preview updates |
| Click "Copy Recipe" | Recipe copied; `lastUsedPosition` + `lastUsedSecret` written to storage |

---

### Flow F — Builder, returning user

| Step | Expected |
|------|----------|
| `lastUsedPosition` = "#", `lastUsedSecret` = "2" stored | — |
| Open Builder | Position "#" pre-selected (`.active`), Secret "2" pre-selected (`.active`) |

---

### Flow G — Builder on Sheets tab (More options auto-expand)

| Step | Expected |
|------|----------|
| Active tab is Sheets | — |
| Open Builder from Home | After detection, `<details id="bld-more-options">` is `open` |
| Sheet ID input pre-filled with detected sheet ID | `#bld-sheet-url` contains sheet ID |

---

### Flow H — "? How it works" and "Learn more →" links

| Step | Expected |
|------|----------|
| Click "? How it works" in Builder header | New tab opens to `demo/demo.html` |
| Click "Learn more →" on Quick Start panel | New tab opens to `demo/demo.html`, panel dismissed |
| Demo page renders all 4 sections | The Idea, Anatomy, Modifiers, Daily Use all visible |
| Click "← Back to extension" on demo page | Tab closes |

---

### Flow I — Connection error (needs tab reload)

| Step | Expected |
|------|----------|
| Open popup immediately after installing extension (content script not yet loaded) | Home shown with amber: "Extension needs tab reload." |
| OR: manually `chrome.tabs.sendMessage` will throw "Receiving end does not exist" | Same result |

---

### Flow J — i18n smoke test (Vietnamese)

| Step | Expected |
|------|----------|
| Change Chrome language to Vietnamese (Settings → Languages) | — |
| Reopen popup | All i18n-keyed strings appear in Vietnamese |
| Quick Start panel (if not dismissed): steps in Vietnamese | — |
| Builder subtitles in Vietnamese | — |
| Demo page headings in Vietnamese | — |

---

## Code Review Checklist

Review all modified files against development rules:

### popup.js
- [ ] No hardcoded English error strings (all i18n keyed)
- [ ] `tryAutoDetect()` extracted as named function (not inline block)
- [ ] `initQuickStart()` defined as named function
- [ ] No `window.location.reload()` in Back handler
- [ ] `#btn-back` handler is async, re-queries active tab
- [ ] Try/catch on all `chrome.storage` calls
- [ ] File length check (currently ~245, will grow ~+40 lines → ~285 — acceptable, under 200-line ideal but single-responsibility module; note concern)

### popup-recipe-builder.js
- [ ] `pickRandomDefaults`, `pickRandomToggle`, `pickRandomCheckbox` removed
- [ ] `loadLastUsed` + `saveLastUsed` added and called correctly
- [ ] `#btn-how-it-works` event listener present
- [ ] No random number usage (`Math.random` removed)
- [ ] File length: ~402 lines — DONE_WITH_CONCERNS (over 200 ideal, but coherent single module; flag for future extraction)

### popup.html
- [ ] `#quick-start-panel` present in `#status-unlocked`
- [ ] `#home-sheet-context`, `#home-hint`, `#home-notice` present
- [ ] Builder `<details id="bld-more-options">` wraps Sheet ID + Modifiers + Profile
- [ ] `.builder-subtitle` `<p>` elements after Hash, Position, Secret labels
- [ ] `#btn-how-it-works` in `.builder-header-row`
- [ ] All new elements have `data-i18n` attributes where applicable

### popup.css
- [ ] `.home-notice` (amber), `.home-sheet-context`, `.quick-start-panel`, `.builder-subtitle`, `.btn-link`, `.builder-more-summary`, `.btn-sm` all defined
- [ ] No duplicate selectors
- [ ] File length check: currently ~485 lines → will grow ~+70 → ~555 lines — **over 200 ideal by a lot**
  - CSS is a single concern file, standard to keep together
  - If it crosses 600 lines, split into `popup-builder.css` (builder-specific) + `popup-home.css` (home + quick-start)
  - For this phase: note as DONE_WITH_CONCERNS, defer split

### messages.json (both locales)
- [ ] EN and VI key counts match
- [ ] `hintSheetContext` has `placeholders` object
- [ ] No orphan keys (all new keys are wired to HTML/JS)

### demo files
- [ ] `demo.html` has all 4 sections
- [ ] `demo.js` uses `localizeHtml()` pattern (not duplicating from popup.js — acceptable; file is standalone)
- [ ] `demo.css` redeclares CSS vars (no import from popup.css — correct for standalone page)
- [ ] `demo.js` is ~30 lines (under 200 ✓)

---

## Automated Tests (Future)

No Vitest/Playwright tests exist yet for the popup. The following test cases should be written when a test harness is established:

- Unit: `tryAutoDetect()` with mocked `chrome.tabs.sendMessage` — verify all routing paths
- Unit: `initQuickStart()` — storage read, panel show/hide, dismiss, CTA text change
- Unit: `loadLastUsed()` / `saveLastUsed()` — storage round-trip
- E2E: Full unlock → Home → Builder → Copy Recipe flow
- E2E: Sheets tab auto-route to Generated

Document these as deferred TODOs in code comments, not blocking this phase.

---

## Todo List

- [ ] Run through flows A–J manually
- [ ] Code review popup.js against checklist
- [ ] Code review popup-recipe-builder.js against checklist
- [ ] Code review popup.html against checklist
- [ ] Code review popup.css against checklist — note if split needed
- [ ] Code review both messages.json files
- [ ] Code review demo files
- [ ] Fix any issues found during review
- [ ] Update plan.md phase statuses to "completed"
- [ ] Update `docs/ui-specs/15-i18n-keys.md` final count

---

## Success Criteria

- All flows A–J pass manually
- No hardcoded English strings in new code paths
- All new i18n keys present in both locale files
- `popup.js` file size noted; over-200-line files flagged with DONE_WITH_CONCERNS
- No regressions in existing flows (unlock, lock, setup screen, options page link)

---

## Risk Assessment

| Risk | Mitigation |
|------|-----------|
| Existing tests (if any added between planning and implementation) broken by HTML restructure | Check `chrome-extension/tests/` before starting; fix failing tests before merging |
| `<details>` inside `.builder-form` breaks `gap: 4px` flex layout | `.builder-form` uses `flex-direction: column` — `<details>` is a block element, renders fine in flex column |
| `sheet-detector.js` dynamic wrapper fails after `<details>` restructure | `getElementById` is document-wide, not parent-scoped — unaffected by nesting change |
