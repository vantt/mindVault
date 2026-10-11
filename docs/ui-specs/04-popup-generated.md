# 04 — Generated Screen (Popup)

Current contract — 2026-10-12. Sources: [popup shell](../../chrome-extension/src/popup/popup.js),
[Home controller](../../chrome-extension/src/popup/popup-home.js),
[security controller](../../chrome-extension/src/popup/popup-security.js) and
[copy/auth binding](../../chrome-extension/src/security/page-security.js).
Precision styling and the single header status pill remain unchanged.

## Entry

Only a successful current-cell cooking response, with an authoritative session
still unlocked, enters `#status-generated`. Empty cell, invalid Recipe, missing
Spice, mismatch and unavailable content bridge remain on [Home](03-popup-home.md)
with localized notices. Do not treat a previous Dish as proof a later request worked.

## Visible elements

| Selector | Content |
|---|---|
| `#gen-password` | Readonly Dish; clear when the authoritative session locks. |
| `#gen-profile-label` | Optional localized profile prefix plus own/shared profile name, assigned as text. |
| `#gen-hint` | Optional localized pepper reminder; copy failure can also show honest localized feedback here. |
| `#btn-copy` | Copy Dish, with successful/failed feedback. |
| `#btn-build-recipe-gen` | Enter the same Builder available from Home. |
| `#btn-back` | Return to Home, requery the active tab, rerun detection. |
| `#btn-lock-gen` | Authoritative lock request, same security contract as Home. |

## Copy and session safety

`bindDishCopy()` sends `COPY_DISH` to the trusted security path instead of assuming
an unchecked clipboard write succeeded. Busy work disables duplicate submission.
Only a successful response flashes localized Copied for1500ms; failed copy shows
localized guidance and does not replace it with a false success indicator.

The clipboard is a system resource. Closing the popup alone does not clear it.
The shared security path attempts expiry after30 seconds and respects ownership:
it must not erase unrelated clipboard text that replaced the copied Dish.
Clipboard permissions or browser conditions can prevent a write/clear; report those
truthfully. Demo text explains this limitation rather than promising automatic
clear on close.

An unlocked session locks after5 idle minutes or10 total minutes. On session-key
removal the popup clears the visible Dish, invalidates Builder work and displays
Locked. This does not delete encrypted Cookbook/profile storage in Chrome Sync.

## Navigation and lock

- Builder empties the ingredient and resets random style/Spice choices, then loads the current
  own profiles and Cookbook context. More options remains collapsed; no restored
  last-used picks/history.
- Back re-detects the active Cookbook. A valid unchanged cell may immediately
  return to Generated; a new invalid/empty context remains on Home with guidance.
- Successful explicit lock closes the popup after the authoritative lock request;
  failed delivery must show localized guidance, not pretend the Cookbook locked.
  Actual failure-path acceptance belongs to the combined UI verification report.
- Ready pill: `statusReady`, green/active; no separate per-view status indicator.

## Verification boundary

The static demo's offline/rendering checks do not establish this whole state
matrix. Report real native Google/shortcut results separately from mocked provider
fixtures; preserve existing approved cooking tests and QR regressions.

Related: [Home](03-popup-home.md), [Builder](05-recipe-builder.md),
[locale catalogue](15-i18n-keys.md), [demo](16-demo-page.md).
