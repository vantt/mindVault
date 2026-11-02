# Product value statements — reusable draft

> **Status:** Corrected local wording2026-10-26, A03; final release claims Pending.
> Use with the [browser contract](../../specs/extension-security.md) and
> [listing draft](cws-listing-copy.md). Historical naming discussion is in
> [naming decision](naming-decision.md); its simulated council opinions are not
> real endorsements, user research or quotes authorized for public use.

## Core copy

- “Cook your password from a Recipe.”
- “Your Menu holds Recipes. Your encrypted Cookbook protects your private Spices.”
- “The extension cooks a Dish when you need it, without saving a list of cooked passwords.”
- “Argon2id derives the Cookbook key from your master password; AES-256-GCM protects stored Spices.”
- “Choose the right profile and Menu before cooking.”
- “An optional Tag checks the Recipe's Spice and Menu context without changing the Dish.”
- “You're the Chef. The extension is your kitchen.”

## Required qualifiers

| Topic | Accurate boundary |
|---|---|
| Cooking | Own-profile Recipes combine ingredients and Spices; Argon2id protects the Cookbook and does not cook the Dish. Legacy shared profiles use additional derivation. |
| Storage | Encrypted profiles and preferences use Chrome Sync; names and Menu mappings may be readable. Session keys use session storage; nonsecret counters/onboarding use local storage. |
| Clipboard | Owned Dish cleanup is attempted at 30s only if the current text still matches. Read denial, sleep/closed Chrome, OS history and concurrent app writes limit cleanup. |
| Tag | Present mismatches refuse cooking; legacy untagged Recipes work with guidance. A short Tag does not prove safety against tampering/removal and can assist guessing weak Spices offline. |
| Full Access | Bundle plus sharing password gives the recipient original profile Spices and the ability to cook the same Dishes. It is access delegation. |
| Public Menu | Recipes are not saved cooked Dishes, but account names/ingredients/Tags disclose information. Sensitive Menus should stay private. |
| Threat model | Weak master passwords or Spices, malware, phishing and clipboard capture remain risks. No universal safety or “zero attack surface” claim. |
| Verification | Historical bounded browser acceptance is dated2026-10-12. Cross-device Sync/conflicts, broad platform/performance and final release artifact acceptance remain Pending. |
| Licensing | Closed source is the recorded product decision. Preserve separate third-party notices. No MIT/open-source/auditable-source marketing promise. |

## Reuse rules

Do not reuse historical “Nothing stored”, “Nothing to steal”, competitor-exclusivity,
unverified adoption numbers or simulated council quotes as product evidence.
No real testimonials with permission/provenance are on file; C12 owns any future
approved quotes. Keep security boundaries close to the corresponding feature claim.
No Store release, phone receiver, unlimited profiles, payment implementation or
support matrix is established by this wording draft.

Unresolved:C02 release boundary; C04 publisher/contact/locales; C12 quote provenance.
