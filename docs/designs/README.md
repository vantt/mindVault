# Handoff — PassChef Chrome Extension (v2)

> **Audience:** developer using Claude Code (or working alone) to implement
> the PassChef UI in a real Chrome extension codebase.
>
> **What is PassChef?** A Chrome Manifest-V3 extension that generates
> deterministic per-site passwords inside Google Sheets, with multi-profile
> + sharing support. Product context lives in `docs/prd/v2-multi-sheet-profiles.md`.

---

## 0 · About these files

The files in `designs/` are **design references** authored in HTML + React +
inline Babel. They are *prototypes showing intended look and behavior* —
not production code.

Your job is to **recreate these screens in the target codebase** (Manifest V3
Chrome extension: `popup.html`, `options.html`, vanilla JS/CSS or whatever
the existing project uses). Don't ship the in-browser Babel from the mockups
into the extension — it violates MV3's default CSP. Translate JSX into static
HTML + plain JS/CSS.

**Source of truth split:**
- **Look (visuals, spacing, copy)** → the HTML mockups in `designs/`.
- **Behavior (states, flows, i18n keys, edge cases)** → the markdown specs
  in `docs/ui-specs/`. There is one numbered file per screen.

When the two disagree, the markdown wins for *what the screen does*, the
mockup wins for *how it looks*. Most disagreements are because v2 was
redesigned after the markdown was written — the mockup is newer.

---

## 1 · Fidelity

**High-fidelity (hi-fi).** Reproduce visual details pixel-close:
- Spacing ≤ 2px tolerance.
- Exact hex codes for colors.
- Exact font families, weights, letter-spacing.
- Iconography stroke width 1.3px, sizes as documented.

Interactions in the mockups are *stubs* — buttons don't actually copy, states
are toggled per artboard rather than wired to real logic. Wire interactions
according to the behavior markdown.

---

## 2 · How the design files are organised

```
design_handoff_passchef/
├── README.md                          ← you are here
├── designs/                           ← open the *.html files to preview
│   ├── PassChef Popup.html           ← canvas: every popup screen × state
│   ├── PassChef Options.html         ← canvas: every options / modal / wizard / demo screen
│   ├── DesignSystem.html              ← design system doc (type, color, components)
│   ├── mvault-system.jsx              ← shared tokens + primitives for popup screens
│   ├── options-system.jsx             ← shared tokens + primitives for options screens
│   ├── precision-refined.jsx          ← deeper reference for the "Precision" direction
│   ├── design-system-doc.jsx          ← renderer for DesignSystem.html
│   ├── design-canvas.jsx              ← infra only (pan/zoom canvas), not part of the product
│   │
│   ├── screen-01-setup.jsx            ← Popup · First Setup
│   ├── screen-02-unlock.jsx           ← Popup · Unlock
│   ├── screen-03-home.jsx             ← Popup · Home / Hub
│   ├── screen-04-generated.jsx       ← Popup · Generated password
│   ├── screen-05-builder.jsx          ← Popup · Recipe builder
│   ├── screen-options-secrets.jsx     ← Options · Secrets tab
│   ├── screen-options-profiles.jsx    ← Options · Profiles tab
│   ├── screen-options-settings.jsx    ← Options · Settings tab
│   ├── screen-modal-new-profile.jsx   ← Modal · New profile
│   ├── screen-modal-edit-secrets.jsx  ← Modal · Edit secret
│   ├── screen-wizard-export.jsx       ← Wizard · Export shared profile (3 steps)
│   ├── screen-wizard-import.jsx       ← Wizard · Import shared profile (3 steps)
│   └── screen-demo.jsx                ← Standalone demo / explainer page
└── docs/
    ├── prd.md
    ├── prd/v1-mvp.md
    ├── prd/v2-multi-sheet-profiles.md ← the spec you are implementing
    ├── ui-specs/                      ← per-screen behavior (16 files + README)
    ├── password-system-design.md
    ├── security-assessment.md
    ├── recipe-tag-design-rationale.md
    ├── technical_insights.md
    ├── implementation_plan.md
    ├── development-roadmap.md
    ├── project-changelog.md
    └── ui-spec-v2-multi-sheet-profiles.md  ← older single-doc UI spec; superseded by ui-specs/*.md
```

**Previewing the mockups:** open `designs/PassChef Popup.html` and
`designs/PassChef Options.html` in any modern browser. They use pan/zoom —
scroll to pan, ⌘/Ctrl + scroll to zoom, click a card to focus.

---

## 3 · Design System ("Precision")

A warm-ink ramp + amber primary + moss/coral/honey semantics, paired with
Fraunces (display, italic), Geist (body), Geist Mono (caption + password
slab). Tokens below are the *authoritative* values used in every screen.

> **Note:** these supersede the v1 tokens in
> `docs/ui-specs/00-design-tokens.md` (which describe the older GitHub-dark
> look). v2 was redesigned.

### 3.1 · Colors

**Ink ramp** — 12 steps of warm desaturated gray. Tone is neutral-warm, not
pure gray.

| Token   | Hex       | Use |
|---------|-----------|-----|
| ink/000 | `#0a0a0c` | deepest black (rare) |
| ink/050 | `#0e0e10` | **page base** |
| ink/100 | `#15151a` | surface (cards) |
| ink/150 | `#1c1c22` | raised surface |
| ink/200 | `#232329` | hairline strong |
| ink/300 | `#2f2f36` | border strong |
| ink/400 | `#44413c` | disabled fg |
| ink/500 | `#6a655d` | tertiary text |
| ink/600 | `#8a857a` | secondary text |
| ink/700 | `#b4ad9f` | secondary strong |
| ink/800 | `#d8d2c3` | body text |
| ink/900 | `#f0ece2` | **display / primary text** (warm off-white) |

**Semantic accents** — functional only; never decorative.

| Token       | Hex       | Use |
|-------------|-----------|-----|
| amber/500   | `#e8a341` | **primary action** · verification · pepper hint |
| amber/Hi    | `#f5b35a` | hover |
| amber/Lo    | `#b97d23` | pressed |
| amber/Bg    | `#3a2a14` | tinted background |
| moss/500    | `#84b577` | success · ready · verified |
| moss/Bg     | `#1d2818` | success panel bg |
| coral/500   | `#e0746c` | blocking error · danger zone |
| coral/Bg    | `#2b1815` | error panel bg |
| honey/500   | `#d4a548` | warning · pepper dot |

**Rule:** amber is the only color the user *clicks*. Moss / coral / honey are
passive — status, verification, warning. No accent outside this set.

### 3.2 · Typography

```html
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Geist:wght@300;400;500;600;700&family=Geist+Mono:wght@300;400;500;600;700&family=Fraunces:ital,opsz,wght@0,9..144,300;0,9..144,400;0,9..144,500;1,9..144,400;1,9..144,500&display=swap" rel="stylesheet">
```

In MV3, **self-host** these fonts in an `assets/fonts/` folder and reference
them via `@font-face` — some users' CSPs block remote font loads.

| Role | Family | Size | Weight | Letter-spacing | Notes |
|------|--------|------|--------|----------------|-------|
| Wordmark | Fraunces | 22 popup / 26 options | 400 | -0.02em | "mind" ink/600 + "Vault" ink/900 italic + 4px amber dot |
| Display | Fraunces | 28–44 | 400 | -0.02em | screen titles |
| Voice italic | Fraunces | 15–17 | 400 italic | -0.005em | "Don't forget your pepper." |
| Name / stat | Geist | 20–32 | 400 | -0.015em | profile names, big numbers |
| Body | Geist | 13–14 | 400 | normal | running prose |
| Body small | Geist | 12–12.5 | 400 | normal | inline helpers |
| Mono display | Geist Mono | 22–32 | 400 | 0.005em | the password slab |
| Mono caption | Geist Mono | 9.5–11 | 500 | **0.18–0.22em UPPERCASE** | "PASSWORD", "PROFILE", "VAULT INVENTORY" |
| Button label | Geist | 13–15 | 500 | -0.005em | primary CTAs |

### 3.3 · Spacing — 4px scale

| Step | px | Use |
|------|----|----|
| 1 | 4 | hairline gaps |
| 2 | 8 | tight |
| 3 | 12 | default inline |
| 4 | 16 | in-card section gap |
| 5 | 20 | row gap · **popup lateral padding** |
| 6 | 24 | large gap / between cards |
| 7 | 32 | **options lateral padding** |
| 8 | 40 | reserved |
| 9 | 56 | reserved |

### 3.4 · Other tokens

- **Border radius:** `4px` for buttons / inputs / chips; `3px` for tag chips.
  Cards have **no radius** — they use hairlines or full bleed. (v1 used 12px;
  v2 dropped it.)
- **Hairline border:** `1px solid ink/200` in-card dividers, `1px solid
  ink/300` stronger borders. Dashed `1px dashed ink/300` for callouts.
- **Atmospheric vignette:** every popup screen has a soft radial-gradient in
  the top-right (`radial-gradient(ellipse at 75% -10%,
  rgba(232,163,65,0.05), transparent 55%)`). Hue varies — moss for
  "ready" home, coral for setup errors. Keep this; it's character.
- **Status dot:** `7×7px circle, box-shadow: 0 0 7px <color>66`, paired with
  an 11.5–12.5px label.
- **Shadows:** mostly none. The amber primary button gets `0 8px 22px
  rgba(232,163,65,0.2)` on hover.

### 3.5 · Iconography

Custom inline SVG, `13×13` (table icons) or `14×14` (header icons), all 1.3px
stroke. Canonical set in `designs/mvault-system.jsx`: `MCopyIcon`, `MEyeIcon`,
`MCheckTiny`, `MRefreshIcon`, `MLockIcon`. Redraw inline; **don't** pull in
an icon library.

---

## 4 · Layout containers

### 4.1 · Popup
- **400 × 600 px.** The PRD says 600 wide; that's unrealistic for popups
  (industry norm 320–400). The Builder is the widest screen and fits at 400.
- Full-bleed `ink/050` background + amber vignette. No window chrome
  (Chrome draws its own).
- Standard structure:
  1. Header (24–32px vertical inset). Wordmark left · status dot + cog right.
  2. Hairline rule (`ink/200`) inset 20px each side.
  3. Body (20px lateral padding).
  4. Optional primary CTA ~16px above the footer.
  5. Footer row: ghost links like `Back` / `Lock session ⌘L`, 12.5px ink/600.

### 4.2 · Options page
- Content width **760 px**, centered. (v1 doc says 600; v2 uses 760 to fit
  profile cards.)
- Same `ink/050` base + vignette.
- Tab bar (Secrets / Profiles / Settings) across the top.
- Modals: centered card on `rgba(0,0,0,0.7)` scrim. Card ink/100, 480–520px
  wide, 24px padding, no radius. Top-right ✕.

---

## 5 · Component inventory

Primitives live in `designs/mvault-system.jsx` (popup) and
`designs/options-system.jsx` (options). Reproduce as plain HTML+CSS. JSX
names are for reference only.

### 5.1 · Cross-screen

| Component         | Defined in       | What it is |
|-------------------|------------------|------------|
| `MFrame`          | mvault-system    | Popup container + vignette. Props: `bg`, `glow` ∈ {amber, moss, coral} |
| `MHeader`         | mvault-system    | Wordmark + status dot + settings cog. Status ∈ {ready, locked, setup, warn, blocked, busy} |
| `MWordmark`       | mvault-system    | "mind" ink/600 + "Vault" ink/900 italic + 4px amber dot |
| `MHeaderRule`     | mvault-system    | 1px hairline ink/200 inset 20px |
| `MCaption`        | mvault-system    | 9.5–10.5px Geist Mono UPPERCASE, 0.22em tracking |
| `MDisplay`        | mvault-system    | Fraunces display |
| `MBody`           | mvault-system    | 13px Geist |
| `MDivider`        | mvault-system    | 1px solid or dashed |
| `MChip`           | mvault-system    | Pill. Tones: ink / amber / moss / coral. `2px 7px`. Mono 9.5px UPPERCASE 0.14em |
| `MPrimaryButton`  | mvault-system    | Full-width amber CTA, label-left + arrow-right. Hover lifts with amber shadow |
| `MSecondaryButton`| mvault-system    | Outlined ink/300 1px |
| `MGhostButton`    | mvault-system    | Plain text link, ink/600 |
| `MIconButton`     | mvault-system    | 26×26 transparent, ink/600 |

### 5.2 · Form controls (options)

In `options-system.jsx`. Specs:
- **Text input** — `ink/100` bg, `1px solid ink/300`, padding `10px 12px`,
  13px Geist, focus border `amber/500`, radius 4px.
- **Password field** — same + eye toggle on the right. Revealed: ink/900 mono.
  Hidden: `••••••••` (preserve width).
- **Toggle switch** — `32×18` pill. Track ink/300 off / amber on, dot ink/900.
- **Tab bar** — flex row. Each tab `padding: 12px 20px`. Active tab has a
  2px amber bottom border, inactive tabs are ink/600 text.
- **Profile card** — `ink/100`, no radius, 3px coloured left strip, 20px
  padding. Shared cards: muted ink/400 strip + a read-only `MChip tone="ink"`
  badge in the title row.
- **Modal shell** — centered card on `rgba(0,0,0,0.7)` scrim. ink/100,
  480–520px, 24px padding, top-right ✕.

### 5.3 · The password slab (hero element)

Distinctive component, used on Generated (popup §04).

- 100% wide; `borderTop` + `borderBottom: 1px solid ink/300` only — **no
  side borders**.
- Left: password in Geist Mono **32px** ink/900, `letter-spacing: 0.005em`.
  Vertical padding 16px.
- Right: Copy button — transparent bg, ink/800 text, 13px Geist 500, copy
  SVG. Hover/active turns text to amber.
- Just-copied state: 1.5s animated `linear-gradient` sweep horizontally
  across the slab (transparent → amber alpha 10 → transparent), and both
  border lines turn amber for the same 1.5s. Show a check icon + "1.5s"
  timer in amber/Lo on the right during that window.

### 5.4 · Profile color palette

Cards rotate a 4-color left-strip palette by render index:

```js
strip[i] = ["#58a6ff", "#e3b341", "#3fb950", "#a371f7"][i % 4]
```

Default is always index 0. Shared profile cards use `ink/400` (no rotation).

---

## 6 · Screen catalogue

13 production screens + the standalone Demo. Each screen has multiple visual
*states* shown side-by-side in the canvas HTML. Cross-reference
`docs/ui-specs/<NN>-*.md` for full behavior, i18n keys, edge cases.

### POPUP — 5 screens

#### 01 · First Setup — `screen-01-setup.jsx` · spec `01-popup-first-setup.md`
- **When:** `chrome.storage.local.salt` is missing.
- **What:** CTA to open the Options page where the master password actually
  gets set (the popup is too small for the form).
- **States:**
  - A · Faithful — minimal CTA ("Open settings →").
  - B · Explore — preview of the 5 secret types you're about to create.

#### 02 · Unlock — `screen-02-unlock.jsx` · spec `02-popup-unlock.md`
- **When:** Salt exists but no in-memory session key.
- **What:** Master password prompt. Argon2id takes ~0.8s — the "Unlocking…"
  state is real time.
- **States:** A Empty · B Typing · C Unlocking · D Invalid password ·
  E Missing salt · F No encrypted data · G "Welcome back" explore variant.

#### 03 · Home / Hub — `screen-03-home.jsx` · spec `03-popup-home.md`
- **When:** Unlocked but no recipe is running (non-Sheets tab, or Sheets
  with no auto-detected recipe).
- **v2.4 change:** this screen now owns all sheet-related errors that used
  to live on Generated.
- **States:** A First-use Quick-Start · B Standard (non-Sheets) ·
  C Sheets · empty cell · D Sheets · needs reload · E Builder-first explore ·
  F Terminal status line explore.

#### 04 · Generated — `screen-04-generated.jsx` · spec `04-popup-generated.md`
- **When:** On a Sheets tab AND service worker returned `success: true`.
  **v2.4: success-only.** All errors moved to Home.
- **States:** A Default (own profile) · B Just-copied flash · C Shared
  profile + bound-sheet header · D Pepper hint visible · E Hero password
  explore · F Receipt / ledger explore.

#### 05 · Recipe Builder — `screen-05-builder.jsx` · spec `05-recipe-builder.md`
- **What:** Form to compose a new recipe. v2.4 adds: "? How it works" link
  top-right, subtitle paragraphs under each field, and a More-options
  collapsible (Sheet ID + Modifiers + Profile) that auto-expands when a
  sheet is detected.
- **States:** A Empty (first-time) · B Smart defaults · C Sheet
  auto-detected · D Tabs picker popover open · E Profile locked (mapped to
  sheet) · F Hash error (non-ASCII).

### OPTIONS — 3 tabs + 2 modals + 2 wizards + demo

#### 06 · Secrets tab — `screen-options-secrets.jsx` · spec `07-secrets-tab.md`
- The default active tab.
- Five password fields (`length-9`, `length-12`, `length-16`,
  `passphrase-3`, `passphrase-4`) each with eye toggle, plus the pepper-hint
  string toggle, plus a Save / Change Password / Lock action row.
- **States:** A Idle · B Eye revealed · C Dirty (Save enabled) ·
  D Save success · E Master password locked-out hint.

#### 07 · Profiles tab — `screen-options-profiles.jsx` · spec `08-profiles-tab.md`
- Card list of profiles. Default first, then owned, then shared (visually
  separated by a muted divider with `MCaption` label "SHARED WITH ME").
- Card actions: rename, set as default, delete (with confirm), Export… (own),
  Import… (top CTA).
- **States:** A Empty (just Default) · B Multiple owned · C Mixed
  owned+shared · D Rename inline · E Delete confirm modal · F Mapped-to-sheet
  badge showing.

#### 08 · Settings tab — `screen-options-settings.jsx` · spec none
  (closest: `06-options-tab-navigation.md`)
- Auto-lock timer, theme placeholder (currently dark only), data &
  privacy section ("Clear all data" danger button), "About" with version +
  build hash.
- **States:** A Idle · B Auto-lock dropdown open · C Clear-all confirm modal.

#### 09 · Modal · New Profile — `screen-modal-new-profile.jsx` · spec `12-new-profile-modal.md`
- Opened from Profiles tab "New profile" button.
- Fields: name (required), optional bound-sheet ID, color (4 swatches),
  optional "use as default for this sheet" toggle.
- **States:** A Default · B Filled · C Color picker hover · D Validation
  error.

#### 10 · Modal · Edit Secret — `screen-modal-edit-secrets.jsx` · spec `11-edit-secrets-modal.md`
- Opened from Secrets tab when user taps a row.
- Single password field + visibility + Save + Cancel.
- **States:** A View (masked) · B Reveal · C Edit · D Saving · E Saved.

#### 11 · Wizard · Export Shared Profile — `screen-wizard-export.jsx` · spec `09-export-wizard.md`
- 3-step modal: (1) pick profile, (2) confirm sharing scope + bound sheet,
  (3) show shareable bundle (encrypted blob + QR + copy button).
- **States:** Step 1, 2, 3 + Success.

#### 12 · Wizard · Import Shared Profile — `screen-wizard-import.jsx` · spec `10-import-wizard.md`
- 3-step modal: (1) paste bundle, (2) enter shared password, (3) confirm
  & install.
- **States:** Step 1, 2, 3 + Invalid bundle error + Success.

#### 13 · Demo page — `screen-demo.jsx` · spec `16-demo-page.md`
- Standalone explainer page (linked from setup screen + chrome web store).
  Not part of the extension proper — could be a marketing page.

---

## 7 · Implementation guidance

### 7.1 · Repo layout (suggested)

```
extension/
├── manifest.json              ← MV3, version 2.x
├── popup/
│   ├── popup.html
│   ├── popup.js
│   └── popup.css
├── options/
│   ├── options.html
│   ├── options.js
│   └── options.css
├── background/
│   └── service-worker.js
├── content-scripts/
│   └── sheets-bridge.js
├── styles/
│   ├── tokens.css             ← :root custom properties for all colors/spacing
│   └── components.css         ← shared button, chip, input, slab, modal
├── assets/
│   └── fonts/                 ← self-hosted Fraunces, Geist, Geist Mono
└── _locales/
    └── en/
        └── messages.json      ← keys per docs/ui-specs/15-i18n-keys.md
```

### 7.2 · Implementation order

1. **Tokens + fonts.** Get `tokens.css` and `@font-face` working. Verify
   wordmark renders correctly first — it's the easiest visual smoke test.
2. **Popup · Unlock.** Smallest screen with real logic; validates the whole
   stack (storage + Argon2 + status dot + error handling).
3. **Popup · Home.** Adds inventory chips, sheet-tab detection, error states.
4. **Popup · Generated.** Adds the password slab + copy-flash animation.
5. **Popup · Builder.** Form-heavy; gates profile mapping logic.
6. **Popup · First Setup.** Trivial after the above.
7. **Options · Secrets.** Form-only, no wizard logic — easiest options tab.
8. **Options · Profiles + modals.** Core multi-profile work.
9. **Options · Settings.**
10. **Export / Import wizards.** Last because they depend on profile
    serialization being stable.
11. **Demo page.** Optional / can ship separately.

### 7.3 · Gotchas

- **CSP.** Don't ship in-browser Babel. Don't load remote fonts in MV3.
  No inline event handlers — use `addEventListener`.
- **Popup focus.** First focusable element on Unlock = password input.
  On Builder = the first text field. Use `autofocus` carefully — Chrome
  popups sometimes steal focus.
- **Lock on tab change.** The "Lock session ⌘L" footer link needs a
  global shortcut declared in `manifest.json` under `commands`.
- **Status dot color must match the screen's vignette.** They are paired:
  - ready / generated → moss
  - locked / unlock → amber
  - setup / error → coral
  - busy → amber (animated pulse)
- **Mono font must be Geist Mono, not the OS monospace.** Test on Windows
  where the fallback (Consolas) looks dramatically different.
- **Password slab letter-spacing is 0.005em**, not the Geist Mono default.
  Without it, the password looks cramped.
- **i18n.** Every visible string must be a `chrome.i18n.getMessage` lookup.
  Keys are catalogued in `docs/ui-specs/15-i18n-keys.md`.

### 7.4 · Verification checklist

For each screen, before marking done:
- [ ] Open the corresponding `screen-*.jsx` artboard in the canvas HTML
      next to your live build at the same zoom level and spot-diff.
- [ ] All visible strings are i18n keys, not hardcoded English.
- [ ] All colors are CSS custom properties (`var(--ink-900)` etc.), not
      hex literals.
- [ ] All spacing values come from the `--space-N` scale.
- [ ] Status dot color matches vignette tint.
- [ ] Tab order is logical (header buttons last, body controls first).
- [ ] No `console.log`, no commented-out code, no TODO without a ticket.

---

## 8 · Where to start reading

If you are just starting:
1. Open `designs/PassChef Popup.html` and `designs/PassChef Options.html`
   side by side.
2. Read `docs/prd/v2-multi-sheet-profiles.md` end-to-end.
3. Skim `docs/ui-specs/README.md` for the spec index, then read
   `01-popup-first-setup.md` through `05-recipe-builder.md` in order.
4. Open `designs/mvault-system.jsx` and `designs/options-system.jsx` to see
   the component implementations — they are short and readable.
5. Start with the Unlock screen (§7.2).

Good luck. Ping the designer when something is ambiguous; don't guess on
copy, colors, or spacing.
