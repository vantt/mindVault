# Phase 01 — Update UI Specs

**Status:** completed
**Effort:** ~1h
**Dependency:** None — must complete before all code phases

---

## Context Links

- `docs/ui-specs/03-popup-home.md` — Home screen spec
- `docs/ui-specs/04-popup-generated.md` — Generated screen spec
- `docs/ui-specs/05-recipe-builder.md` — Builder spec
- `docs/ui-specs/15-i18n-keys.md` — i18n keys catalogue

---

## Overview

Specs are reverse-engineered docs of the current UI. They must be updated to describe the NEW design before any code changes. This ensures the spec is the single source of truth and prevents drift between docs and code.

---

## Files to Modify

| File | Changes |
|------|---------|
| `docs/ui-specs/03-popup-home.md` | Add all new contextual states (B1), Quick Start panel (A), sheet name display |
| `docs/ui-specs/04-popup-generated.md` | Restrict to success-only; remove error states (moved to Home) |
| `docs/ui-specs/05-recipe-builder.md` | Add micro-copy, More Options collapsible, smart defaults, "? How it works" link |
| `docs/ui-specs/15-i18n-keys.md` | Add all new keys (pre-populate before Phase 7 implements them) |

## File to Create

| File | Contents |
|------|----------|
| `docs/ui-specs/16-demo-page.md` | New spec for the standalone demo HTML page |

---

## Implementation Steps

### 1. Update `03-popup-home.md`

Replace the single layout block with multiple contextual state layouts:

**A. First-use state** (no `hasSeenQuickStart` in local storage):
```
┌──────────────────────────────────────────┐
│ 🔐 PassChef          🟢 Unlocked    ⚙️ │
│ ──────────────────────────────────────── │
│ ┌── How it works ───────────────────── ×┐│
│ │ 1. Build a recipe                     ││
│ │ 2. Paste it into a Google Sheets cell ││
│ │ 3. Click the cell → password appears  ││
│ │ [Got it ✓]          [Learn more →]    ││
│ └──────────────────────────────────────┘│
│                                          │
│  [     Create First Recipe     ]         │  ← CTA text changes
│  [           Lock              ]         │
└──────────────────────────────────────────┘
```

**B. Standard state** (not on Sheets tab, `hasSeenQuickStart` set):
```
┌──────────────────────────────────────────┐
│ 🔐 PassChef          🟢 Unlocked    ⚙️ │
│ ──────────────────────────────────────── │
│  Click any cell in Google Sheets         │
│  containing a recipe.                    │
│  Profiles: 3 own · 1 shared              │
│  [        Build Recipe        ]          │
│  [           Lock             ]          │
└──────────────────────────────────────────┘
```

**C. On Sheets tab — empty/invalid cell:**
```
│ 📄 Budget 2026                           │  ← sheet name from tab title
│ ⚠ Selected cell is empty or has no      │  ← amber inline notice
│   recipe.                                │
│  [        Build Recipe        ]          │
│  [           Lock             ]          │
```

**D. On Sheets tab — connection error:**
```
│ 📄 Budget 2026                           │
│ ⚠ Extension needs tab reload.           │  ← amber inline warning
│  [        Build Recipe        ]          │
│  [           Lock             ]          │
```

Document for each state:
- DOM elements added/modified
- `chrome.storage.local` key: `hasSeenQuickStart` (boolean)
- New i18n keys needed
- How state is determined (routing logic)

Also document updated auto-routing:
```
popup open (unlocked)
  └─ Sheets tab?
       ├─ YES → sendMessage(GET_CURRENT_CELL_PASSWORD)
       │         ├─ success → status-generated (SUCCESS ONLY)
       │         ├─ empty cell → stay Home, amber notice
       │         ├─ other error → stay Home, amber warning (connection type)
       │         └─ catch → stay Home, amber warning
       └─ NO → stay Home, standard hint
```

### 2. Update `04-popup-generated.md`

- Remove all error states from this screen (they no longer route here)
- Update the triggered-by description: "success ONLY — `response.success === true`"
- Update the layout to remove error hint content (hint now only shows pepper reminder)
- Remove the error hint table rows (empty cell, parse error, connection error)
- Status pill stays green "Ready"
- Note Back → Home with `tryAutoDetect()` re-detection (from Phase 4)

### 3. Update `05-recipe-builder.md`

Add sections for:

**"? How it works" link** in builder header area:
- A small `<a>` or `<button>` link right of the section title / back button row
- Opens demo page via `chrome.tabs.create()`
- i18n key: `linkHowItWorks` → "? How it works"

**Micro-copy subtitles:**
Each field label gets a subtitle `<p>` with class `builder-subtitle`:
| Field | Subtitle (EN) |
|-------|--------------|
| Hash | "The text part of your recipe" |
| Position | "Where your secret goes" |
| Secret | "Which secret phrase to use" |
| Modifiers | "Optional transformations" |

**"More options" collapsible section:**
- Contains: Sheet ID field, Modifiers row, Profile dropdown
- `<details>`/`<summary>` element with text "More options"
- Auto-expand rule (open question — document as TBD, decided during impl)
- Default: collapsed

**Smart defaults (returning users):**
- `lastUsedPosition` + `lastUsedSecret` keys in `chrome.storage.local`
- On form open: read these keys, pre-select if present
- On Copy Recipe: write current selection back to storage
- First-time users: form starts empty (no random defaults)

### 4. Update `15-i18n-keys.md`

Add new keys table for this redesign. Increment audit version to v2.5. List all new keys with EN values (VI to be filled in Phase 7):

New keys (EN):
- `quickStartTitle` → "How it works"
- `quickStartStep1` → "Build a recipe"
- `quickStartStep2` → "Paste it into a Google Sheets cell"
- `quickStartStep3` → "Click the cell → password appears"
- `btnGotIt` → "Got it ✓"
- `linkLearnMore` → "Learn more →"
- `btnCreateFirstRecipe` → "Create First Recipe"
- `hintEmptyCell` → "Selected cell is empty or has no recipe."
- `hintNeedsReload` → "Extension needs tab reload."
- `hintSheetContext` → "📄 $1" (sheet name, with placeholder)
- `linkHowItWorks` → "? How it works"
- `builderSubtitleHash` → "The text part of your recipe"
- `builderSubtitlePosition` → "Where your secret goes"
- `builderSubtitleSecret` → "Which secret phrase to use"
- `builderSubtitleModifiers` → "Optional transformations"
- `moreOptions` → "More options"
- All demo page strings (see Phase 6)

### 5. Create `16-demo-page.md`

Document:
- Trigger: `chrome.tabs.create({ url: chrome.runtime.getURL('demo/demo.html') })`
- Entry points: "Learn more →" on Quick Start panel, "? How it works" in Builder header
- 4 content sections: The Idea, Anatomy of a Recipe, Modifiers, Daily Use
- i18n: EN + VI
- Styling: dark theme, same CSS vars as popup
- Back link to extension popup (not functional — just a closing note)
- manifest.json declaration needed

---

## Todo List

- [x] Update `docs/ui-specs/03-popup-home.md` with all 4 contextual states + new routing diagram
- [x] Update `docs/ui-specs/04-popup-generated.md` — success-only, remove error states
- [x] Update `docs/ui-specs/05-recipe-builder.md` — micro-copy, More options, smart defaults, How it works link
- [x] Update `docs/ui-specs/15-i18n-keys.md` — add new keys table, bump to v2.5
- [x] Create `docs/ui-specs/16-demo-page.md`

---

## Success Criteria

- Each spec file accurately describes the NEW design (not current code)
- No contradictions between spec files
- All new UI elements have i18n keys listed in `15-i18n-keys.md`
- Spec is detailed enough that a developer could implement from spec alone

---

## Next Steps

All code phases (02–06) are unblocked after this phase.
