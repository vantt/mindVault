# 03 — Popup: Home / Hub Screen

Current contract — 2026-10-12. Sources: [popup shell](../../chrome-extension/src/popup/popup.js),
[Home controller](../../chrome-extension/src/popup/popup-home.js),
[security controller](../../chrome-extension/src/popup/popup-security.js),
[markup](../../chrome-extension/src/popup/popup.html). Precision styling remains;
this update reconciles behavior, not a new visual design.

## Entry and authoritative state

- No saved setup salt: `#status-setup`, with Start Setup opening Options.
- Setup present but `AUTH_STATUS` reports locked: `#status-locked`.
- `AUTH_STATUS` reports unlocked: `#status-unlocked`, then active-cell detection.
- Successful unlock reloads the popup through the authoritative auth path.
- Session key removal clears the visible Dish and closes Builder work before
  displaying Locked. A stored local selection never authorizes an unlocked view.

## Contextual Home states

| Context | Visible result |
|---|---|
| First use (`hasSeenQuickStart` false/absent) | Quick Start plus contextual hint; primary CTA Create First Recipe. |
| No active Google Cookbook | Generic cell-selection hint; no Cookbook badge/share control. |
| Cookbook, empty cell | Home with localized empty-cell notice. |
| Invalid Recipe | Home with localized invalid-format guidance. |
| Missing Spice | Home with localized configuration guidance. |
| Verification mismatch | Home with distinct localized mismatch guidance; no Dish. |
| Content bridge unavailable | Home with localized reload guidance. |
| Other generation/connection failure | Home with localized generic failure; no raw exception echo. |
| Valid Recipe and authoritative session still unlocked | Generated view; errors never route there as successful cooking. |

`#home-sheet-context` displays the detected Cookbook title, using text-safe
substitution. `#home-notice` contains `#home-notice-text`; it is a div, not the
obsolete single hint paragraph. Error notices take priority over the generic hint.
Detection rechecks auth before presenting a successful Dish. Actual Google/provider
and native-shortcut evidence is recorded separately from fixture UI verification.

## Controls

| Selector | Behavior |
|---|---|
| `#btn-qs-close`, `#btn-qs-got-it` | Hide Quick Start and attempt to save `hasSeenQuickStart` locally; restore Build Recipe CTA. |
| `#btn-qs-learn-more` | Same dismissal, then programmatically open bundled static demo tab. |
| `#btn-build-recipe` | Reset random Builder defaults, load profiles/context, enter Builder. More options stays collapsed. |
| `#btn-manage-profiles`, global Settings | Open Options; profile count can appear in the manage label. |
| `#btn-share-sheet` | Cookbook context only; save pending share action and open Options. |
| `#btn-lock` | Request authoritative lock. Successful lock clears session state; failure must remain visible rather than claim success. See integration verification for actual failure-path acceptance. |

Quick Start persistence is device-local. Its existence does not alter Recipe
history: Builder does not restore/save old picks. The demo is an extension-owned
tab; internal opening needs no additional web-accessible-resource declaration.

## Status and locale

The header owns the single global pill (`#global-status`, `#global-status-dot`,
`#global-status-text`): Home uses `statusUnlocked`, green/active; setup and lock use
red/inactive. Runtime `loadLocale()` follows the stored EN/VI preference.
Home notices, Cookbook title placeholders, profile labels and pepper reminder
use runtime locale keys. See [catalogue](15-i18n-keys.md).

## Acceptance boundary

Current source/behavior correction does not claim every popup scenario or live
Google test passed. The combined completion report records the actual setup,
locked/unlocked, no-Cookbook, Builder, successful/mismatched Dish, copy/lock-failure
and Back scenarios. Demo-only verification is narrower.

Related: [Generated](04-popup-generated.md), [Builder](05-recipe-builder.md),
[demo](16-demo-page.md), [tokens](00-design-tokens.md).
