# Chef guide — browser kitchen draft

> Local A09 draft2026-10-26;C02 target2.3.1 controlledbeta approved2026-10-27.
> Supported cells,contacts,live URLs and final release artifact approval remain
> Pending. This guide does not install or publish anything.

## Prepare your Cookbook

Open PassChef's Options from the extension. On first use, create and confirm a
strong master password and confirm you have a secure recovery record. The UI
requires at least8characters; choose a long, unique password you can protect.
This password protects the encrypted Cookbook; it is different from each account
Dish and from a sharing password. The extension cannot recover it for you.

After setup/unlock, open Profiles. The initial own profile is Default. Choose its
Edit Secrets action to enter your private Spice jars1–5, then Save changes. New
Profile creates another own profile and opens the same editor. Keep a secure record
of the Spices and Recipes needed to recover your accounts; do not put private
Spices or a master password in a shared Menu or support report.

You can unlock in Options or the popup with the same master password. Lock when
finished. The current kitchen also locks on browser idle/device lock and at the
original ten-minute session deadline. Session unlock does not decrypt your Menu;
Google controls access to that separately. The three global Settings Display
defaults are temporarily hidden by the owner-approved UI01 deferral2026-10-26.
Stored defaults are retained, but global inheritance/reveal/Menu-detection settings
are unavailable. Edit Spices and the existing per-profile hint in the profile editor.

## Cook from a selected Menu cell

Use your own authorized Google Menu and any Google sign-in it requires. The
extension does not create a Google account or grant access to a private Menu.
Add Recipes only where you have permission to edit. Keep sensitive account labels
and metadata private, even when the Menu contains no saved cooked Dishes.

1. In Options Profiles, add/confirm the Menu assignment and intended own profile.
   Unassigned Menus fall back to the selected default; check routing before cooking.
2. Put a valid Recipe in the intended cell. Select that cell, then open the extension
   or press its configured shortcut:Ctrl+Shift+L, Mac Control+Shift+L. Browser conflicts
   or unavailable receivers need attention; don't assume a shortcut fired.
3. Check the displayed profile and Dish. Generated displays the Dish visibly;
   there is no eye/masked-default control on this view. Empty/invalid/mismatched
   context should show guidance instead of a new Dish.
4. Click Copy only when needed. Cooking itself does not copy. After Dish Copy,
   cleanup is attempted at30seconds only if current clipboard text still matches.
   Later different text or denied reading prevents a clear; sleep/closed Chrome,
   OS history and concurrent apps limit cleanup. Recipe/bundle Copy are separate.

If the popup says reload the Menu, reload that authorized tab and retry deliberately.
A previous Dish is not evidence that a changed Recipe/profile/Tag worked.

## Build a Recipe in the popup

Choose Build Recipe from Home or Generated. Enter ASCII ingredient text; choose
cooking style and Spice jar. The real defaults pick a random style/jar and keep
More options collapsed. Expand More options to inspect the Menu/profile or optional
modifiers. Confirm the intended Menu before copying a tagged Recipe back to it.
The preview is a real Dish; keep it out of public screenshots except synthetic demos.

Optional four-character Tags check Recipe/Spice/Menu context without changing the
Dish. A mismatched present Tag refuses cooking. Legacy untagged Recipes remain
usable with guidance; removing the Tag removes its check. Public Tags can help test
weak Spice guesses. They are not a promise against all tampering.

## Cook by hand — public practice example

For an **own profile** only, the synthetic Recipe `r4nd0m#1` means place Spice1
before the ingredient. With public practice Spice1=`Basic*`:

```text
Basic* + r4nd0m = Basic*r4nd0m
```

These public strings must never protect a real account. For a tagged own-profile
Recipe, the Tag does not change that Dish; manually ignore its metadata only when
you deliberately know the correct context. Legacy derived/shared profiles use
additional derivation and are not covered by this hand-cooking example.

This is a human method. The browser does not have a separate standalone “Manual
Cook” screen accepting a whole Recipe; its Builder preview is a different flow.
Android manual cooking belongs to the separate mobile app and acceptance plan.

## Protect recovery material and understand sharing

A secure recovery record should preserve your required Recipes, original Spices,
profile/Menu assignment notes and any protected export's sharing password. Keep
private records separate from public Menus. Do not Reset, uninstall or overwrite
working profiles until you have verified how you will regain account access.

Export on an own profile creates a **Full Access** bundle for that profile's
original Spices. The optional Menu ID is routing metadata, not a restriction on
what the recipient can cook with recovered Spices. Use a strong separate sharing
password, confirm it, then protect the downloaded JSON/QR and communicate the
password safely. A recipient with both can recover the original Spices and cook
the same Dishes. QR display proves the sender only; phone receiving/import remains
later work. Don't publish a real bundle, scannable QR or sharing password.

This export does **not** back up the whole Cookbook:it omits other profiles, the
master password, global preferences, default profile, all Menu assignments and
per-profile settings. The optional single Menu ID is not a complete assignment map.

**FA01 update2026-10-26:the own-profile shape is repaired locally.** The earlier
wizard could report success yet leave later cooking unable to find the imported
Spices; that failure remains recorded. New actual-handler/real-crypto/core regressions
pass, and [selected B01 Chrome tests](../../../plans/261022-project-work-queue/reports/b01-run-01.md)
verify new imports with/without Menu metadata cooking the same Dish. Final-candidate/
full-suite/upgrade recovery acceptance is still Pending. Keep the original
working profile/recovery record; never test recovery by deleting the only copy.
This fixes new imports; records created by the earlier broken import are not
automatically repaired.

When the actual candidate is approved for a restore rehearsal, use a disposable Cookbook first:
Import Shared accepts pasted JSON or a JSON file, validates the bundle, requests its
sharing password, then asks for a local name. Choose a **new, unused name** before
confirming. Current import directly writes the selected name; it can overwrite an
existing same-name own/legacy shared profile without merging or an overwrite prompt.
A Full Access bundle with a Menu ID can replace that Menu's existing assignment;
other assignments are retained. It does not automatically set the default profile
or restore global settings. Verify the exact expected Dish and routing before
trusting any recovery result. Legacy imports are separate derived, Menu-bound profiles.

Deleting a source profile or changing its master password does not revoke exported
copies or recipients' account access. Plan actual account-password/Spice rotation
when access should end.

## Three different changes

| Change | What changes / what you must do |
|---|---|
| Cookbook master password | Settings→Change Password verifies the old password, derives new Cookbook protection and re-encrypts profiles. Existing Spices and account Dishes should remain the same; protect your new recovery record. It does not revoke exported copies. |
| Spice in a profile | Edit Secrets changes Dishes for every Recipe using that jar. Plan affected accounts first and update each account password deliberately. |
| Recipe ingredient | Change only that Recipe's ingredient to cook a different Dish; update the actual account password as well. Rebuild any Tag for the new Recipe/context. |

Reset clears extension Sync/session records after confirmation, but not local
attempt/onboarding records, Menu content, exported/recipient copies or OS history.
There is no developer recovery for forgotten master passwords or lost Spices.
If you retain the original Spices/Recipes, own-profile hand cooking can help; a new
empty Cookbook alone cannot recreate old Dishes.

For help, final monitored support/private security channels are still Pending C04.
Do not invent a support address or send real credentials. See the current
[browser contract](../../specs/extension-security.md), [Privacy draft](../../../landing-page/privacy.html)
and [Terms draft](../../../landing-page/terms.html). Actual release/support/Sync
coverage must come from the approved candidate's records, not this draft.
