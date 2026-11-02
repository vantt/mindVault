# Chrome Web Store listing draft — PassChef

> **Status:** Corrected local draft2026-10-26 (A03); not approved submission copy.
> Source:manifest2.3.0 plus current browser contracts; final version/support C02,
> publisher/contact/locales C04, candidate acceptance and publication still Pending.

## 1. Extension name

```text
PassChef Password Generator
```

Matches the current manifest; 27 characters. Official maximum 75 characters,
checked2026-10-26: [Chrome manifest name](https://developer.chrome.com/docs/extensions/reference/manifest/name).

## 2. Short description

```text
Cook passwords from Recipes in Google Sheets. Keep private Spices encrypted with Argon2id and AES-GCM.
```

102 characters; matches current manifest. Official maximum 132 characters,
checked2026-10-26: [Chrome description](https://developer.chrome.com/docs/extensions/reference/manifest/description).

## 3. Full description — draft body

<!-- listing-body-start -->
PassChef cooks passwords, called Dishes, from Recipes in your Google Menu and private Spices in an encrypted Cookbook.

Keep Recipes such as r4nd0m#1 in Google Sheets. The # style places Spice jar1 before the ingredient text. With the synthetic Spice Basic*, this Recipe cooks Basic*r4nd0m. This example explains the kitchen; choose your own strong, unpredictable ingredients and Spices.

HOW TO COOK
1. Open extension Options, create a strong master password and configure your Spice jars. Keep a safe recovery record: the extension cannot reset a forgotten master password or recover lost Spices.
2. Store a valid Recipe in a Google Menu and assign the Menu to the intended profile. Select its cell, then open the extension or use Ctrl+Shift+L (Mac: Control+Shift+L, subject to browser shortcut conflicts).
3. Check the profile and Dish, then click Copy when needed. Cooking does not automatically copy. The popup Recipe Builder can prepare a Recipe and its optional verification Tag.

WHAT THE KITCHEN PROVIDES
- Multiple own profiles and Menu assignments to organize your Spices. Chrome storage quotas apply.
- Five cooking styles and optional Spice modifiers. Own-profile Recipes can also be cooked manually with the same Spices; legacy sheet-bound shared profiles use additional key derivation.
- Optional four-character verification Tags. A present Tag checks the Recipe, Spice and Menu context and refuses a mismatch. It does not change the Dish. Legacy untagged Recipes remain usable with guidance; removing a Tag removes that check. Tags are brief error checks, not a guarantee against tampering or weak-Spice guessing.
- An encrypted Cookbook: Argon2id derives its key from the master password; AES-256-GCM protects stored profiles. Recipe cooking is a separate string-combination process, not Argon2 password derivation.
- Automatic session lock after five minutes of browser idle, on device lock, or at the ten-minute absolute session deadline.
- Clipboard ownership: after Copy, PassChef attempts cleanup at 30 seconds and clears only if the current text still matches its Dish. Later different text or a denied read prevents the clear. Sleeping/closed Chrome can delay cleanup; OS history and another application's concurrent writes are outside this guarantee.
- English and Vietnamese extension interface text.

STORAGE AND SHARING
Encrypted profiles, salt, Menu assignments, names and preferences use Chrome storage.sync. Google may synchronize this data according to Chrome settings; some metadata remains readable. Session keys and lifetime records use Chrome storage.session; nonsecret onboarding and authentication counters use local storage. Actual cross-device Sync/conflict recovery and the broader browser/OS matrix are still being verified.

The browser extension reads Recipe cells through the Google Sheets page. Google hosts your Menu and Chrome Sync infrastructure. The extension contains no developer-operated password backend or telemetry client; this is not a claim that no data leaves your device.

Full Access export/QR encrypts the selected profile's original Spices with a separate sharing password (PBKDF2-SHA256 and AES-GCM). A recipient with the bundle and sharing password can recover those Spices and cook the same account Dishes. Give Full Access only to someone you authorize. Sharing a Recipe or Tag alone is a different action. The extension QR sender does not establish an accepted phone QR receiver/import workflow.

SECURITY BOUNDARIES
The extension stores encrypted Spices, not a saved list of cooked Dishes. Dishes still exist briefly in memory and, after Copy, on the clipboard. Strong master passwords and Spices matter. A leaked Menu can reveal account names, ingredient text and other metadata; a visible Tag can help test guessed weak Spices offline. Keep sensitive Menus private. A compromised device, malware, phishing, stolen clipboard history or weak credentials can defeat this model.

AVAILABILITY
This is a local preparation draft. Final supported environments, release version, support links, legal disclosures and artifact acceptance must be confirmed before submission. No published Store installation, broad performance promise or universal cross-device support is claimed here.
<!-- listing-body-end -->

Editorial note:remove the AVAILABILITY preparation paragraph only after C02/A14
bind the final support/disclosure text to accepted artifact evidence. Do not drop
actual known limits to make the submission sound complete.

## 4. Graphic copy handoff

A08 owns current image specs/brief; B06 owns actual candidate captures.
Safe draft captions: “Select a Recipe. Cook a Dish.” / “Choose the right profile.” /
“Build a Recipe in the popup.” / “Spices protected in an encrypted Cookbook.”
Do not invent countdowns, an in-Menu Builder, unlimited storage or accepted phone Sync.
Optional promo headline:“Cook your password.” Subhead:“Recipes + private Spices.”
No screenshots or promotional artwork are accepted by this text correction.

## 5. Category and search wording

Category candidate:Productivity, subject to C04 final listing choices.
Use natural phrases such as “Google Sheets Recipes” and “Recipe password cooking”
in descriptive prose. This is not a separate keyword field or a license to repeat
search terms. [Chrome listing guidance](https://developer.chrome.com/docs/webstore/best-listing)
requires accurate, concise text and discourages keyword stuffing; checked2026-10-26.
The opened official listing/text pages do not publish a long-description numeric
maximum; confirm the actual Dashboard field on A14/C11 rather than asserting an
unverified16,000-character limit. Current body length is recorded in the A03 report.
