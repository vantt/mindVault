# Store reviewer guide — synthetic browser kitchen draft

> A09 preparation2026-10-26. Not yet an approved submission instruction packet.
> Final ZIP/version/SHA, public policy/support URLs, selected Menu access and known
> limits must be bound by A13/A14/C04/C05 before C11 submits anything.
> C02 target2.3.1 controlledbeta is approved;WindowsStable/116 and mandatory
> real2deviceSync acceptance are still Pending,not replaced by old2.3.0 source tests.

## Access and test material

No developer-operated password-backend account is required by the current extension.
For actual Google Menu flows, use an account/authorized Menu the reviewer may access;
Google sign-in can be required. No Google account creation, new grant or credentials
are supplied by this draft. The final packet must provide an authorized synthetic
fixture URL and any necessary access instructions; missing access remains Pending.
Never use production accounts, private Spices or personal login data.

For a development ZIP, local developer-mode loading is preparation evidence only;
Store-installed acceptance is later B09. Inspect the final supplied artifact identity
and load it using the authorized reviewer environment. Do not treat the A06/A07
ignored development ZIP as an approved release candidate. No new browser is launched
by this document.

## Synthetic fixture and expected Dish

Create a disposable Cookbook through Options:choose and protect your own temporary
master password, confirm backup, then Edit Secrets on Default. Set only the practice
Spice1=`Basic*` and save. These values are public test data, never real credentials.
Prepare own profiles/assignments only in the disposable test scope.

| Recipe | Context | Expected result |
|---|---|---|
| `r4nd0m#1` | Own profile with practice Spice1 | Exact Dish`Basic*r4nd0m` |
| Same Recipe with actual Builder-produced Tag | Same Menu/profile/Spice | Same exact Dish; Tag does not change it |
| A present Tag confirmed not to match current synthetic context | Wrong Tag/context | Localized mismatch guidance, no new Dish |
| Empty cell or invalid Recipe | Authorized Menu | Guidance, no new Dish |

Generate Tags through the actual candidate for the actual fixture Menu; do not use
an invented decorative suffix. If fixture access requires a provider write, obtain
that permission and prepare only designated synthetic cells.

## Review sequence

1. **Setup/unlock:**Options uses setup/confirm/backup; unlock uses the same authority
   as popup. Initial own profile Default appears. Configure practice jar in its
   Edit Secrets modal and Save. Unlock after Lock with the same temporary master.
2. **Non-Menu Home/Builder:**Open popup on a non-Menu tab. It should show Home.
   Open Build Recipe, explicitly set ingredient`r4nd0m`, style`#`, jar1 and own Default.
   The synthetic preview should match the literal Dish. This is Builder preview,
   not Google/native acceptance or an Android-style standalone Manual Cook screen.
3. **Actual selected Menu:**With authorized fixture Recipe selected and correct
   profile assignment, open the actual action popup. Confirm profile and literal
   Dish, then click Copy if the reviewer owns the clipboard resource. The native
   shortcut can be tested separately; a tab-message fixture is not native proof.
4. **Tag refusal/navigation:**Check matched/failing Tag and empty cell cases. Back
   re-reads the current selection. No stale success may stand in for the new result.
5. **Profiles and session:**Inspect actual horizontal Profiles/Settings navigation,
   mapping and default-profile selection using synthetic records. Lock must prevent
   new cooking and clear visible Dishes. Full5/10-minute elapsed/platform claims
   need their own evidence; mocks/unit clocks are not physical elapsed proof.
6. **Export/QR:**From a synthetic own profile, Export uses a separate sharing
   password and optional Menu ID. Show only synthetic output. Recipient gets the
   original Spices with bundle+password and can cook the same Dishes. Sender QR
   does not establish phone receiver/import support.
7. **Recovery/import gate:**FA01's own-profile shape is locally repaired2026-10-26;
   actual handler/crypto/core regression Passed, while the earlier failure is retained.
   Selected B01 Chrome import→Builder Cook now Passed with/without Menu metadata;
   final-candidate/full-suite/upgrade restore acceptance remains Pending. Do not mark
   full recovery Passed. For an approved candidate rehearsal, test only in a
   disposable Cookbook with a unique unused profile name; current import may
   overwrite same-name records or the bundle Menu's assignment without prompting.
   Verify actual exact Dish/routing; export is not a full Cookbook/settings backup.
8. **Master-password rotation:**Change Password with old/new/confirmation and backup
   confirmation re-encrypts Cookbook profiles. Spices/Recipes should cook the same
   Dish after the new unlock; this is different from changing a Spice/ingredient
   and updating the password on an actual account. Do not rotate real accounts.
9. **Locale/guidance:**Use the extension's actual EN/VI language setting and bundled
   Demo entry. Record untranslated wizard text honestly. The separate website
   interactive illustration has DEMO01 parser limitations; it is not core parity proof.

## Clipboard and cleanup

Dish Copy is explicit. At30seconds the owner attempts a current-text comparison
and clears only an exact match; a later different value or denied read prevents a
clear. Sleep/closed Chrome, OS history and concurrent writes limit cleanup. There
is no countdown strip on Generated. Recipe-only/bundle Copy is separate and does
not inherit Dish ownership cleanup.

Keep the raw observation/command/result tied to source/ZIP/hash, runtime and fixture.
Use Passed/Failed/NotExercised/Pending per case; don't turn a displayed success toast
into persistence/recovery evidence. Do not post real keys/Spices/Dishes/QRs in logs.
Close the test browser and remove its disposable profile and synthetic downloads;
clear only still-owned synthetic clipboard data under the approved resource slot.
Do not use Reset/uninstall on a real Cookbook as a cleanup shortcut. They cannot
remotely erase exported copies, recipients or Menu content.

## Required final packet fields and open limits

Final artifact identity, approved boundary/version/platforms, policy/support HTTPS
URLs, authorized fixture access, actual screenshots/rights and known-limit list
are Pending. C04 must supply any reviewer access choices without production secrets.
Current known limits include FA01's pending final-candidate/upgrade restore acceptance, UI01's
owner-approved temporary removal of three global Display defaults2026-10-26
(saved values retained; no new inheritance/reveal/detection behavior), DEMO01 website illustration,
cross-device Sync/conflicts, broader platform/provider/performance and accepted
clipboard/Tag boundaries. A14 must reconcile all fixes/evidence before submission.

[Chef guide](chef-guide.md) · [Release-note template](release-notes-template.md) ·
[Browser contract](../../specs/extension-security.md)
