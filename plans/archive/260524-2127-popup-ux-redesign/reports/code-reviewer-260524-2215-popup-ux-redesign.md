# Code Review: Popup UX Redesign (Phases 2–7)

**Date:** 2026-05-24  
**Reviewer:** code-reviewer agent  
**Plan:** `plans/260524-2127-popup-ux-redesign/`

---

## Scope

- Files reviewed: 9 files (popup.html, popup.js, popup-recipe-builder.js, popup.css, demo.html, demo.css, demo.js, en/messages.json, vi/messages.json)
- Lines of code analyzed: ~1,850 LOC across modified/new files
- Review focus: correctness, security, i18n completeness, edge cases, consistency
- Updated plans: `plan.md` — all phases marked **completed**

---

## Overall Assessment

Implementation is solid. All checklist items from the task description are correctly implemented. No critical or high-severity issues found. Three warnings (one behavioral, two minor) and several low-priority notes.

---

## Critical Issues

None.

---

## High Priority Findings

None.

---

## Warnings (Should Fix)

### W1 — `hintCellError` fallback exposes raw service worker error string

**File:** `chrome-extension/src/popup/popup.js` line ~179

```js
: (chrome.i18n.getMessage('hintCellError') || `Error: ${response?.error}`);
homeNoticeText.textContent = msg;
```

The `||` fallback embeds `response?.error` into the UI text. `response.error` originates from `content.js` which can send `e.message` from the service worker call chain. Not XSS (uses `textContent`), but leaks internal error strings to the user if `hintCellError` key is somehow absent.

**Fix:** Replace fallback with a static string, e.g. `|| 'An error occurred'`. The i18n key is always present in the bundle so this is defensive code, but the fallback is needlessly leaky.

---

### W2 — `saveLastUsed()` only fires on Copy, not on Back navigation

**File:** `chrome-extension/src/popup/popup-recipe-builder.js` lines ~279–286

If a user selects position + secret, builds a recipe, then presses **Back** without copying, their choices are silently discarded. The code comment says "Persist current position + secret so `loadLastUsed()` can restore them", which implies save-on-use intent, but the behavior is save-on-copy-only.

**Fix (if intentional):** Add a comment clarifying this is deliberate (only persist when recipe is actually used/copied). **Fix (if unintentional):** Also call `saveLastUsed()` in the Back button handler before `reset()`.

---

### W3 — Stray `chrome-extension/plans/` directory in extension source tree

**From git status:** `?? chrome-extension/plans/`

This directory contains a code-reviewer report and is untracked. It sits inside the extension source dir and would be bundled into the extension package on a naive `cp -r` or zip-all build.

**Fix:** Move to `plans/` (project root level) or add `chrome-extension/plans/` to `.gitignore`.

---

## Low Priority Notes

### N1 — Quick Start panel visible flash before auto-detect routes to Generated

**File:** `chrome-extension/src/popup/popup.js` lines ~228–234

Sequence: `initQuickStart()` (shows panel on Home) → `chrome.tabs.query()` → `tryAutoDetect()` (may route to Generated). On Sheets tabs with an active recipe, first-time users will briefly see the Quick Start panel on Home before the popup jumps to the Generated screen. The comment acknowledges this but notes it "is never seen anyway" — which is incorrect on slower devices. The flash window is the async gap of `chrome.tabs.query + sendMessage`.

Not a functional bug. Acceptable UX trade-off.

---

### N2 — `data-i18n-html` handler in `demo.js` is dead code

**File:** `chrome-extension/src/demo/demo.js` lines ~13–16

```js
document.querySelectorAll('[data-i18n-html]').forEach(el => {
    const msg = chrome.i18n.getMessage(el.dataset.i18nHtml);
    if (msg) el.innerHTML = msg;
});
```

No elements in `demo.html` use `data-i18n-html`. The handler is unused. If future translations do use this attribute, the `innerHTML` path would allow HTML injection from i18n values — acceptable since i18n source is the extension bundle, but worth noting.

**Fix:** Remove the `data-i18n-html` block from `demo.js` (YAGNI). Add a comment if the feature is intentionally reserved.

---

### N3 — `demo/` not declared in `web_accessible_resources`

`chrome.runtime.getURL('demo/demo.html')` works from within the extension without a `web_accessible_resources` entry (extension pages can always access their own bundle). However, if any future content script or external page needs to link to the demo, the resource would need to be declared.

**Status:** Not a bug for current usage. No action needed.

---

### N4 — `moreOptions` key used in JS via `getMessage` but `data-i18n` attr in HTML

The `<summary data-i18n="moreOptions">` is correctly localized by `localizeHtml()`. The `moreOptions` key in JS is used in a different context — this is a false match in the key audit; both usages are correct. No issue.

---

## Positive Observations

- **tryAutoDetect routing:** Correctly routes success-only to Generated; all error/empty/connection cases stay on Home with amber notice. The `Empty cell` specialization avoids confusing "Could not read cell" wording for a common state.
- **Null tab guard:** `tab?.url?.includes(...)` correctly handles undefined tab from `chrome.tabs.query`.
- **Incognito resilience:** All `chrome.storage.local` calls wrapped in `try/catch` with sensible fallbacks (show panel, leave form blank, ignore save).
- **i18n completeness:** 120 keys, EN/VI in perfect sync, zero orphans, all 26 new keys used.
- **DOM ID consistency:** All IDs referenced in JS (`home-hint`, `home-notice`, `home-notice-text`, `home-sheet-context`, `quick-start-panel`, `btn-qs-close`, `btn-qs-got-it`, `btn-qs-learn-more`, `bld-more-options`) confirmed present in HTML.
- **XSS discipline:** All user-facing dynamic text uses `textContent`. Tooltip `innerHTML` is populated exclusively from the extension's own i18n bundle (developer-controlled, not user-input).
- **`<details>` structure:** Sheet ID + Modifiers + Profile correctly nested inside `#bld-more-options`, with custom `▸/▾` marker replacing native arrow.
- **`reset()` fix:** Now calls `onFormChange()` at the end — stale recipe output after reset was a pre-existing bug now corrected.
- **Back button re-detection:** Correctly calls `showSection('status-unlocked')` first, then `tryAutoDetect(activeTab)`, so Home is displayed before detection runs.
- **loadLastUsed selector safety:** Uses `querySelector('[data-val="${value}"]')` from `chrome.storage.local` — device-local, extension-only writes, no injection surface.
- **profilesSummary placeholder:** Correctly uses Chrome i18n named-placeholder format (`$OWN$`/`$SHARED$`) with positional content `$1`/`$2`, and called with `[String(ownCount), String(sharedCount)]`.

---

## Recommended Actions

1. **W1 (low urgency):** Replace `|| \`Error: ${response?.error}\`` fallback with `|| 'An error occurred'` in `tryAutoDetect`.
2. **W2 (clarify intent):** Either add comment to `saveLastUsed` usage ("save only when recipe is used") or also call it in Back handler — intent is ambiguous without a comment.
3. **W3 (housekeeping):** Move `chrome-extension/plans/` out of the extension source tree before packaging.
4. **N2 (YAGNI):** Remove dead `data-i18n-html` block from `demo.js`.

---

## Metrics

- Type Coverage: N/A (vanilla JS)
- Test Coverage: Existing unit tests unchanged; no new tests added for Phases 2–7
- Linting Issues: 0 syntax errors found
- i18n: 120 keys, EN=VI, 0 orphans

---

## Unresolved Questions

1. Is the `saveLastUsed`-on-copy-only behavior intentional? If yes, the comment should say so explicitly.
2. Should `phase-08-testing.md` produce unit tests for `initQuickStart()` / `loadLastUsed()` / `tryAutoDetect()` as part of the plan scope? Currently no new tests cover these functions.
