# 05 — Recipe Builder (Popup)

Current behavior — 2026-10-12. Source:
[controller](../../chrome-extension/src/popup/popup-recipe-builder.js),
[form](../../chrome-extension/src/popup/builder-form.js),
[preview](../../chrome-extension/src/popup/builder-preview.js),
[Menu context](../../chrome-extension/src/popup/builder-sheet-context.js),
[detector](../../chrome-extension/src/popup/sheet-detector.js) and
[markup](../../chrome-extension/src/popup/popup.html).

The Home and Generated Build Recipe controls open `#status-builder`.
The Builder cooks from own profiles. Shared profiles retain their Menu-bound
routing outside this form. Current accepted random defaults supersede the old
persisted-pick and automatic-disclosure proposal (commit18b13eb).

## Layout and initial state

Back and How it works appear above Ingredients, cooking style and Spice jar.
A native `#bld-more-options` details element contains target Menu, modifiers and
profile. It opens collapsed every time; detection never opens it automatically.
The Recipe and real Dish previews, warnings and Copy Recipe follow the form.

Each opening clears Ingredients, modifiers, target Menu, previews and copy state.
Current own-profile metadata loads from Sync. Untouched cooking-style/Spice-jar
choices initialize randomly; early explicit choices and typed Ingredients survive
an asynchronous metadata response. Old metadata cannot cross a close/reopen.
No last-used choices or Recipe history are saved.

| Element | Contract |
|---|---|
| `#bld-hash` | Required ASCII letters/digits; invalid input shows `errHashAscii`, clears both previews and disables Copy. Its existing length caption is `Len` followed by the numeric count. |
| `#btn-hash-generate` | Fills24 random Ingredients from the selected uppercase/lowercase/digit groups. At least one character group remains enabled. |
| `#bld-position-group` | One of `#`, `$`, `@`, `%`, `^`; clicking the current choice clears it and makes the form incomplete. |
| `#bld-secret-group` | One of Spice jars1–5; clicking the current choice clears it. |
| `#bld-modifier-row` | Optional modifiers read in markup order. `_` flips prefix/suffix; `!` uppercases; `?` reverses UTF-16 units; `~` strips non-ASCII-alphanumeric characters. |
| `#bld-sheet-url` | Optional full Menu URL or bare ID, normalized to ID. Invalid nonempty input blocks preview; empty input intentionally permits a legacy untagged Recipe. |
| `#bld-profile` | Current own profiles only. Existing valid Menu mapping selects/locks its profile; unmapped Menu shows a warning and allows a choice. |
| `#bld-more-options` | User-controlled disclosure, reset collapsed on reopening. |

Cooking style uses the extension's existing Recipe engine: prefix/suffix, middle
at `Math.floor(length/2)`, one-by-one or two-by-two interleaving. This UI does not
implement a separate cooking oracle.

## Menu detection and warnings

`initSheetDetector()` wraps the target input and adds `#bld-detect-refresh` plus
`#bld-tabs-picker`. It asks the worker for current/other Menu tabs. One result can
fill context; multiple results produce a text-only picker. Refresh with no result
recooks existing manual context rather than leaving Copy disabled indefinitely.
Late detection cannot overwrite a later edit or publish after close/lock.

| Visible feedback | Meaning |
|---|---|
| `#bld-sheet-url-error` / `errInvalidSheetUrl` | Typed Menu context is invalid. |
| `#bld-sheet-auto-hint` / `hintSheetUrlAuto` | Target Menu was detected; hint remains inside More options. |
| `#bld-sheet-warning` / `warnNoSheetUrl` | No target Menu selected. |
| `#bld-legacy-warning` / `warnLegacyNoTag` | Intentional untagged Recipe; warning is above the previews, outside collapsed details. |
| `#bld-profile-hint` | `hintProfileFromMapping` for a locked mapping, or `hintProfileUnmapped` for an unmapped Menu. |
| `#bld-preview-error` | Localized cooking/tag/copy failure, with no raw worker/provider exception text. |

## Preview and asynchronous ownership

Every edit synchronously invalidates the previous preview and copy feedback.
Both outputs become `—`; Copy stays disabled. A complete valid snapshot schedules
cooking after300ms and displays `…` in the Dish preview while waiting.

For a selected Menu, the worker first computes the verification tag using the
snapshot's Ingredients/style/Spice/modifiers/Menu/profile. A failed or malformed
tag fails visibly; the UI never silently falls back to an untagged Recipe.
Then the worker cooks the real Dish from that exact final Recipe/context.
Only a current successful result publishes both Recipe and Dish and enables Copy.
Neither the base Recipe nor a missing-profile placeholder is published as success.

| Result | Output and Copy |
|---|---|
| Incomplete, invalid or newly edited form | Both `—`; Copy disabled. |
| Tag/Dish unresolved | Recipe remains `—`, Dish `…`; Copy disabled. |
| Both current operations succeed | Final Recipe and real Dish; Copy enabled. |
| Missing own profile | `errNoProfile`, both `—`; Copy disabled. |
| Tag or cooking failure | Safe localized error, both `—`; Copy disabled. |
| Back, close, pagehide or session removal | Pending loads/detection/tag/Dish/copy feedback invalidated; form cleared. |

Open lifetime and load sequence govern metadata. Form/preview epochs govern edits
and async cooking. Early user edits remain usable when metadata arrives, while a
closed opening never receives an old load. Frozen snapshots prevent a tag from
one Menu/profile being combined with a Dish from another.

## Copy and navigation

Copy Recipe copies the current final Recipe, not the Dish. It is disabled while
cooking and during its own clipboard write. `Copied!` appears only after that
promise resolves; rejection shows `errRecipeCopy` and allows retry. Editing,
closing or locking suppresses stale success/error feedback.
Recipe-only copy is outside the Dish's30-second cleanup scope.

Back closes the Builder lifetime and returns Home. Reopening resets the form.
How it works opens `chrome.runtime.getURL('demo/demo.html')` in a bundled tab;
no web-accessible-resource expansion is required. See [demo](16-demo-page.md).

## Localization and validation boundary

HTML labels/subtitles/tooltips use locale markers. Dynamic warnings/errors use
`t()` plus text-only assignment. Symbols, Recipe syntax and length numerals remain
language-neutral; the existing `Len` caption is a fixed English abbreviation. Source locale files are authoritative; see [catalogue](15-i18n-keys.md).

New Builder tests exercise delayed metadata/tag/Dish responses, invalid edits,
coherent snapshots, selected-Menu tag failure, denied clipboard, stale copy,
actual registered session/pagehide callbacks and close/reopen cleanup.
The demo's real entry test proves Builder How it works opens/closes the guide;
it does not certify all Builder cooking/security/clipboard scenarios. Combined
headed Builder acceptance follows the frozen security integration.

Related: [Home](03-popup-home.md), [Generated](04-popup-generated.md),
[error states](13-error-states.md), [demo](16-demo-page.md).
