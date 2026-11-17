# Password Manager UX Patterns for PassChef Android

Date: 2026-11-10. Scope: research only. Confidence tags: [H] official/fetched, [M] search snippet or single secondary, [L] inference.

## Source gaps (read first)
- Bitwarden/Proton Pass 2025 mobile design: little found. Bitwarden redesign beta = desktop/extension only.
- 1Password 2025 mobile redesign: not confirmed (coverage undated).
- Material 3 empty-state guidance: not found.
- Google Password Manager UX: not researched in depth.
- Autofill/Credential Manager: 2023-era pages; API details unverified for 2025-26.

---

## 1. Onboarding / first run

**P1. Teach by worked example, not by text.** [M] 1Password onboarding reportedly uses fake input fields and deliberate delays to make security tangible (third-party/blog snippet, not fetched).
- Apply to PassChef as: 3 screens with one fake sample: `fb#1` (Menu) + `Basic*` (Spice) → `Basic*fb` (Dish). Show "Menu visible to others / Spice stays on this phone / Dish made only when you cook". Use a sample, never the user's real data.

**P2. Defer the master secret until it is needed.** [L] Progressive disclosure: create vault shell first, ask for master password + first Spice at first real Recipe/cook, not at app open.
- Apply to PassChef as: Onboarding = 3 explainer screens → "Connect Menu (optional)" → "Set master password" only when Spice is created. Skip-able Google connect.

**P3. Honest irreversibility notice.** [H-ish, 1Password recovery-code pattern, fetched blog Nov 2025] 1Password prompts a recovery code when loosening friction.
- Apply to PassChef as: if no recovery path exists, say so plainly on master-password screen ("Lose this = lose every Dish"). If a recovery/export of Spices is added later, mirror the recovery-code step. Open question for product.

**P4. Three-screen concept model (suggested copy, cooking metaphor per AGENTS.md):**
1. "Your Menu lists Recipes" (sheet = public-ish labels).
2. "Spices are your secret, kept on this phone" (encrypted cookbook).
3. "We cook the Dish when you ask, then clear it" (on-demand, clipboard auto-clear).
- Apply to PassChef as: exactly these 3 slides, each with the sample from P1, "Skip" always visible, and a glossary tooltip for Menu/Cookbook/Spice/Dish reused on Home.

## 2. Unlock and re-lock

**P5. Presets, not a settings maze.** [H] 1Password device-unlock (Nov 2025) offers Convenient (auto with device) / Balanced (one unlock per 8 h, then device) / Strict (unlock every return). Per-device, not synced.
- Apply to PassChef as: 3 presets on first unlock screen. Default = Balanced or Strict. Biometric unlocks the encrypted cookbook key only; Argon2id master stays the fallback and is required after app restart/reboot.

**P6. Short re-lock window.** [H] 1Password mobile auto-unlocks only if app was used within 10 min of last unlock.
- Apply to PassChef as: lock on background > 60-120 s by default (tune); lock immediately on screen-off option; clear in-memory Dish on lock. Align with README note that Dishes can linger in memory.

**P7. Biometric is convenience, master is authority.** [H] 1Password: device verification relies on secure hardware + E2E encryption; when biometric fails, fallback is device password/SSO.
- Apply to PassChef as: BiometricPrompt via Android Keystore-bound key; on 3 failures or key invalidation, force master password. Never let biometric alone reveal Spices without a key that is hardware-bound (standard Android practice, [L] unverified here).

## 3. Home: list, search, grouping, empty state

**P8. Search-first single bar with dismissible filter chips.** [H] Bitwarden Redesign Beta (desktop/extension, 2026): one bar for search + filter, filter chips, selections persist across navigation, Esc clears.
- Apply to PassChef as: sticky search at top of Menu list. Filter chips: "Menu source", "Tag", "Recently cooked". Persist last filter. Mobile equivalent of Cmd+F is N/A; keep "clear" X.

**P9. Group by source/tag, not by type.** [M] Bitwarden user feedback (Reddit, single user, weak): Favorites near top, then types/folders. Vault-style grouping is common; Proton uses vaults with top-left switcher [M].
- Apply to PassChef as: group by Menu (sheet/tab) → then tag. Show "Recent" row (last 3 cooked) above list. Favorites = pinned Recipes. Do not add vault switcher unless multiple Menus exist (YAGNI).

**P10. Empty states as next-step CTAs.** [L] M3 empty-state guidance not found; use general pattern.
- Apply to PassChef as: no Menu → "Connect your Google Sheet" primary + "Add a Recipe manually" secondary. No Spice → "Create your first Spice" (links to P2). Never show a blank list.

## 4. Item detail and copy flow

**P11. Masked by default, reveal on hold.** [L, common pattern across managers] Dish hidden until long-press or eye toggle; reveal auto-hides after N s.
- Apply to PassChef as: Dish card shows `••••••••`; tap-hold reveal; auto-hide 10 s; biometric re-auth optional for reveal.

**P12. One-tap copy from list, with flag and timed clear.** [H] Android Secure Clipboard guidance:
- Set `ClipDescription.EXTRA_IS_SENSITIVE` (API 33+; key `android.content.extra.IS_SENSITIVE` for ≤32) via `PersistableBundle` on `ClipData.description.extras` before `setPrimaryClip()`. It only obfuscates the keyboard preview; it is not a security control.
- Android 12+ (API 31) shows a toast when an app pastes clipboard. Android 10+ (API 29) blocks background clipboard reads.
- Google says clipboard auto-clear is platform-automatic from Android 13 (API 33); for older devices, schedule `clearPrimaryClip()` (API 28+) or replace with empty clip.
- Apply to PassChef as: every Dish copy sets the sensitive flag; min SDK 29 (Android 10) per guide; own timer (default 30 s) that clears only if clipboard still holds our Dish (compare content, don't blind-clear); snackbar "Copied. Clears in 30 s". Do not claim the clipboard is secure in UI copy.

**P13. Feedback that confirms without leaking.** [L] Snackbar shows "Copied Facebook" not the Dish. Haptic tick on copy.
- Apply to PassChef as: snackbar text uses account name only; ensure Dish never appears in snackbar, notification, or Android recents thumbnail (set `FLAG_SECURE` on cook/reveal screens — [L] standard Android practice).

## 5. Autofill and Credential Manager (future)

**P14. Autofill service is the realistic cross-app path.** [H] Android Autofill framework: API 26+; app declares an AutofillService in manifest; user enables it in system settings; Google note: autofill services must use data only to provide suggestions. [M] KeePassDX uses Autofill (not Accessibility service) and its wiki says it will not use Accessibility on pre-8.0 devices; user reports of browser-specific failures (Vivaldi) and battery-optimization breakage.
- Apply to PassChef as: future phase, not in redesign v1. Flow: Autofill request → PassChef prompt unlock → user picks Recipe → cook Dish → fill. Requires a "pending unlock" activity and per-app/domain matching by Menu account name. Effort estimate [L]: MVP 3-5 engineer-weeks plus device test matrix (browsers, banks, Chrome). Do not use Accessibility Service.

**P15. Credential Manager is a poor fit for v1.** [M] Android 14+ supports multiple providers; passkeys are stored in the user-selected provider after screen-lock verification; needs Digital Asset Links. PassChef stores Spices and cooks Dishes; it does not store passwords or passkeys, so Credential Manager provider would need a different model (or stored credentials), which conflicts with the deterministic design.
- Apply to PassChef as: park. Revisit only if PassChef chooses to store generated secrets. Effort [L]: large (provider service, sync, passkey crypto), out of scope.

## 6. Navigation and data-source setup

**P16. Bottom bar for 2-5 top-level destinations; no drawer.** [M] Material 3 Expressive (May 2025, 9to5Google): flexible navigation bar replaces original bar; navigation drawer deprecated in favor of rail on large screens. SAP Fiori M3 guidance: nav bar for 2-5 core destinations, not for one.
- Apply to PassChef as: 3 tabs: **Menu** (Recipes + search), **Cookbook** (Spices, key/backup), **Settings** (Sheets connection, language VI/EN, lock presets). Cook is an action on Menu items, not a tab.

**P17. Keep data-source setup in a linear wizard reachable from empty state.** [L] Sheets connection is one-time; wizard is correct, but also reachable from Settings.
- Apply to PassChef as: "Connect Menu" wizard: sign-in → pick sheet → map columns (Recipe, Tag, Account) → preview 3 rows → done. Show a read-only preview before saving. Keep existing mapping screen as step 3, not a separate screen.

## 7. Visual personality without losing trust

**P18. Calm chrome, expressive moments.** [M] 1Password 8 mobile coverage: new icons, typography, detailed item/vault views (TechRadar/Engadget, date unclear). Trust cues come from restraint: neutral surfaces, clear hierarchy, security screens plain.
- Apply to PassChef as: M3 color roles seeded from brand (warm cream surface, terracotta/paprika primary, sage secondary for "Dish" accents). Keep security screens (unlock, Spice, Dish reveal) neutral; put warmth in onboarding, empty states, and copy success.

**P19. Illustrate concepts, one per screen.** [L] Use the chef-keyhole logo kit (commit d058311) as the anchor. Onboarding illustrations: a recipe card, a jar (Spice), a plate (Dish). Flat, two-color, no faces on security screens.
- Apply to PassChef as: 3 onboarding icons; reuse same jar/card in Menu/Cookbook empty states. Avoid skeuomorphic kitchens.

**P20. Motion: short, functional.** [L] M3 motion: shared-axis for tab/wizard steps, fade-through for list changes; keep under ~300 ms. "Cooking" = a single steam/simmer loader during Dish generation (<300 ms in practice, so likely skip; use it only if cook is slow).
- Apply to PassChef as: shared-axis between wizard steps; no looping animation on secure screens. Respect reduced-motion.

**P21. Typography: one display face for brand, system/Roboto for data.** [L] Dishes and Recipes are monospace-friendly: show Dish in monospace with grouping for readability, Recipe in mono with ingredient/spice split colored (ingredient vs spice tint). Tint must pass contrast.
- Apply to PassChef as: Recipe `r4nd0m#1` rendered as `r4nd0m` + `#1` with two tones; Dish in monospace, large, selectable only on reveal.

**P22. Cooking vocabulary in UI copy, sparing.** [H] AGENTS.md mapping: Recipe, Menu, Ingredients, Spices, Cooking, Dish. Use in headings/CTAs ("Cook Dish", "Add Spice"), keep technical words (Argon2id, AES) out of primary UI; put them in an "How it works" sheet.

## 8. Accessibility and one-handed reach

**P23. Target size: 48dp Android, 24 CSS px WCAG floor.** [H] WCAG 2.2 SC 2.5.8 AA = 24×24 px (or spacing); Android Material guidance = 48dp. Design to 48dp.
- Apply to PassChef as: min 48dp touch target for copy, reveal, filter chips, tab items; 8dp min spacing between chips.

**P24. Primary actions in the thumb zone.** [L] Put Cook/Copy as bottom-anchored FAB or sticky bottom action on Menu item detail; search can sit at top (tap-once-and-type is acceptable). Avoid top-right-only actions.
- Apply to PassChef as: Menu item detail: bottom sheet with "Cook Dish" (primary) and "Copy" after cook. Top-bar contains only back and overflow.

**P25. Labels and states for TalkBack.** [L] Every masked Dish needs description "Dish hidden, double tap to reveal"; copy button announces "Dish copied, clears in 30 seconds"; avoid reading Dish aloud (set `contentDescription` on reveal to generic).
- Apply to PassChef as: custom semantics for masked field; live region for copy confirmation with account name only.

**P26. Font scale and bilingual layout.** [L] Vietnamese diacritics need taller line height; test at 200% font scale (P0 in existing mobile cookbook acceptance already tracks 200% landscape).
- Apply to PassChef as: no fixed-height rows; use `sp` everywhere; test EN + VI at 200% font scale; allow text wrap in chips.

---

## Adoption priority (suggested)
1. P12 clipboard flag + timed clear (security, low effort).
2. P5-P7 unlock presets + re-lock window + biometric-over-master.
3. P16 3-tab nav + P8 search bar + P10 empty states.
4. P1-P4 onboarding with sample.
5. P23-P25 accessibility baseline.
6. P18-P22 visual system.
7. P14 Autofill (future phase, effort [L] 3-5 weeks).
8. P15 Credential Manager (park).

## Unresolved questions
- Does PassChef have any Spice recovery/export path? Drives P3 copy and onboarding.
- Min SDK: P12 assumes API 29+; confirm from `app/build.gradle.kts`.
- Should Recent/Favorites exist given Menu is sheet-backed (no local state beyond cache)?
- Is Autofill in scope for any roadmap phase, and is a browser-coverage target defined?
- Brand palette: confirm terracotta/cream vs existing chrome-extension tokens (`chrome-extension/src/styles/tokens.css`) to keep parity.
- Bitwarden/Proton/Google 2025 mobile specifics still unverified; treat P9 and P18 as [M].

## Sources
- 1Password unlock post, Nov 2025: https://1password.com/blog/a-simpler-way-to-unlock-1password (fetched)
- 1Password community Nov 2025 announcement: https://www.1password.community/discussions/announcements/november-2025-at-1password-access-trust-gap-planning-your-estate-and-an-updated-/165089
- 1Password onboarding blog (snippet only): https://1password.com/blog/product-update-improvements-and-features
- Bitwarden Redesign Beta help: https://bitwarden.com/help/redesign-beta/ (fetched; 404 on some locales)
- Bitwarden Android GA: https://community.bitwarden.com/t/bitwarden-android-app-now-in-general-availability-ga/79270
- Proton Pass Android help: https://proton.me/support/use-pass-android
- Proton Pass Android changelog: https://www.github.com/protonpass/android-pass/blob/main/CHANGELOG.md
- KeePassDX Autofill wiki: https://github.com/Kunzisoft/KeePassDX/wiki/AutoFill
- Android Autofill services guide: https://developer.android.com/identity/autofill/autofill-services
- Android Secure Clipboard Handling (MASVS-CODE): https://developer.android.com/privacy-and-security/risks/secure-clipboard-handling (fetched)
- Android Credential Manager, create passkeys: https://developer.android.com/identity/passkeys/create-passkeys
- Android identity hub (credential provider): https://developer.android.com/identity
- Material 3 Expressive navigation (9to5Google, May 2025): https://9to5google.com/2025/05/14/material-3-expressive-navigation/
- SAP Fiori M3 navigation bar usage: https://www.sap.com/design-system/fiori-design-android/v26-1/components/m3-standard-components/navigation-bar/usage
- WCAG 2.2 SC 2.5.8 (DAISY KB): https://kb.daisy.org/publishing/docs/conformance/wcag/target-size-minimum.html
- Android touch target / 48dp (UK gov a11y guidance): https://govtnz.github.io/web-a11y-guidance/ka/accessible-ux-best-practices/mobile-apps/mobile-app-a11y-by-feature-type/touch-and-pointer-target-sizing.html
- Material 3 layout (m3.material.io) returned 404 on fetch; not used.
