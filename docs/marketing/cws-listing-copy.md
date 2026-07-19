# Chrome Web Store Listing Copy — PassChef Password Generator

> Source of truth for all CWS submission fields.
> Last updated: 2026-06-13 | Product version: v2.3

---

## 1. Extension Name (max 45 chars)

```
PassChef — Recipe Password Generator
```

*(37 chars — fits CWS limit with room)*

---

## 2. Short Description (max 132 chars)

```
Cook passwords from recipes in Google Sheets. Zero-knowledge. Nothing stored. Argon2id encrypted.
```

*(99 chars)*

---

## 3. Full Description

### Headline

**Cook your password from a recipe. Nothing stored.**

---

### The Problem With "Storing" Passwords

Most people who use Google Sheets to track logins already know it's risky. But every alternative — 1Password, Bitwarden, LastPass — asks you to *trust a vault*. You move your passwords into their cloud. You hope their servers never get breached. You've seen the headlines.

LastPass. RockYou. Adobe. The pattern is always the same: a database of stored passwords gets stolen.

**What if there was nothing to steal?**

---

### Introducing the Recipe Model

PassChef takes a fundamentally different approach. Instead of storing passwords, it *generates* them on demand from two ingredients:

- **Your recipe** — a short formula you write in a Google Sheet (e.g., `site+length32+symbols`)
- **Your secret spice** — a private phrase you remember, encrypted locally and never uploaded anywhere

Recipe + secret spice = password. Generated fresh each time. Never saved anywhere.

**Your Google Sheet holds recipes — publicly shareable, harmless without the secret. Your secret spice stays on your device. Neither half is useful alone.**

This is zero-storage security: if there's no vault, there's nothing to breach.

---

### Core Features

- **Zero-storage architecture** — passwords are computed, not saved. Close the extension and they vanish completely.
- **Google Sheets as your recipe book** — use the tool 900M+ people already have. No new apps to learn.
- **Argon2id + AES-256-GCM encryption** — industry-standard algorithms protect your secret spice at rest.
- **Multiple profiles** — organize recipes by team, project, or context. Route different Sheets to different profiles automatically.
- **Recipe Verification Tags** — detect if a recipe was accidentally changed in your Sheet before generating.
- **Keyboard shortcut** — press Ctrl+Shift+L (Mac: ⌃⇧L) on any Sheet cell to generate instantly.
- **Auto-lock + clipboard clear** — extension locks after idle and clears clipboard after 30 seconds. Nothing lingers.
- **Export / Import profiles** — share your recipe set with a teammate without exposing any passwords.
- **English + Vietnamese** — full bilingual support.

---

### How It Works (3 Steps)

**1. Write a recipe in Google Sheets**
Add a row to any Sheet with your recipe formula — site name, length, character rules. This is your public ingredient list. Safe to share, useless without the secret.

**2. Set your secret spice (once)**
In the extension options, enter your private phrase — your "secret spice." It's encrypted with AES-256-GCM and stored only on your device. PassChef never sees it. No server ever receives it.

**3. Press Ctrl+Shift+L on the recipe cell**
PassChef reads the recipe from the active cell, combines it with your secret spice using Argon2id key derivation, and generates your password. Click copy. Done.

---

### Security Architecture

PassChef was designed around one principle: **nothing to steal means nothing to breach.**

| What | How |
|------|-----|
| Password generation | Argon2id key derivation (memory-hard, GPU-resistant) |
| Secret spice storage | AES-256-GCM encrypted, stored in Chrome's local storage only |
| Recipe data | Plaintext in your Google Sheet — contains no secrets |
| Network requests | Only to `docs.google.com` to read your Sheet |
| External servers | None — zero backend infrastructure |
| Telemetry / analytics | None — zero tracking in the extension |

The only data that ever leaves your device is a read request to your own Google Sheet. That's it.

**The math:** even if someone steals your Sheet, they get recipes with no passwords. Even if someone steals your device, they get an AES-256-GCM blob that requires your secret phrase to decrypt. The only attack that works is knowing both — which means they're already inside your head.

---

### Privacy Commitment

- No data sent to any external server — ever
- No analytics, no crash reporting, no usage tracking inside the extension
- Secret spice is stored encrypted on your device only, never synced or uploaded
- Clipboard is automatically cleared after 30 seconds
- Extension locks itself after an idle period you configure
- You can verify all of this — the extension's network requests are auditable in DevTools

We built this for people who don't trust password managers with their passwords. It would be hypocritical to track you.

---

### Who Is PassChef For?

**Primary audience:** People who already manage passwords in Google Sheets and know it's a problem. You have the Sheet, the workflow, the habit. PassChef secures it without forcing migration.

**Also great for:**
- Privacy-conscious users who've lost faith in cloud vault providers after repeated breach headlines
- Small teams and families sharing account access — share the recipe Sheet, keep the secret spice private
- Power users who want full transparency into how their passwords are derived (the recipe is fully human-readable)
- Anyone who values self-sufficiency: recipe on paper + secret in memory = full access even without the extension

**Not for:** Users happy with existing password managers (stay there — it works). PassChef is a fundamentally different model for people who want zero-storage security.

---

### FAQ

**Q: What if I forget my secret spice?**
There is no recovery. If you lose your secret spice, you cannot regenerate your passwords. Write it down and store it securely (a physical safe, an encrypted note). This is intentional — no recovery mechanism means no recovery attack surface.

**Q: Is my Sheet public? Isn't that risky?**
Your Sheet can be private, shared with specific people, or even public — it doesn't matter. A recipe without the secret spice generates nothing useful. You could post your recipe Sheet publicly and it would tell an attacker nothing about your passwords.

**Q: What happens if Google changes Sheets?**
The extension uses the Sheets DOM to read recipe cells. If Google changes the interface significantly, an update may be needed. We maintain a test suite (55 unit + 6 E2E tests) and monitor for regressions. If a breaking change occurs, we ship an update.

**Q: Does PassChef work offline?**
Partially. The extension can generate passwords from recipes that are already loaded in the active Sheet tab. It cannot fetch new recipe data without network access to Google Sheets.

---

## 4. Screenshots Needed (5 shots)

> Recommended: 1280×800 or 640×400. PNG. Annotate with clean callout labels.

| # | Screen | Key Elements to Show | Caption Text |
|---|--------|---------------------|--------------|
| 1 | **Core flow** — Sheet cell selected, popup open, password generated | Google Sheet visible behind popup; recipe cell highlighted; password field showing masked output; Copy button | "Select a recipe cell. Press Ctrl+Shift+L. Your password is ready." |
| 2 | **Popup close-up** — password generated and ready to copy | Password field (masked), Copy button, profile name, auto-lock timer visible | "Generated on demand. Nothing stored. Clipboard clears in 30 seconds." |
| 3 | **Options page — profile management** | Profile list with names, linked Sheet URLs, Add/Edit/Delete controls | "Organize recipes by project, team, or context. Unlimited profiles." |
| 4 | **Recipe Builder in Sheet** | Google Sheet with recipe columns visible (site, length, rules, verification tag), extension panel open alongside | "Write recipes in plain English. Your Sheet stays useful even without PassChef." |
| 5 | **Security explainer / How it works** | Clean diagram: Sheet (recipe) + Device (secret spice) → PassChef → Password. OR show the empty server side ("no server") | "Recipe + secret spice = password. No server. No storage. Nothing to breach." |

---

## 5. Promo Tile Copy

### Small Tile (440×280)

```
Headline:  Cook your password.
Subhead:   Nothing stored.
Logo:      PassChef icon (chef hat)
```

### Large Tile (920×680)

```
Headline:  Recipe-based passwords.
           Nothing to steal.
Subhead:   Argon2id · AES-256-GCM · Zero storage
CTA area:  "Add to Chrome — Free"
Logo:      PassChef icon + wordmark
```

---

## 6. Category

**Productivity**

*(Not "Security" — less competition in Productivity; the recipe/Sheets angle makes Productivity the honest fit. Security users will find it via keywords regardless.)*

---

## 7. Tags / Keywords (5 primary search terms)

1. `google sheets password manager`
2. `password generator chrome extension`
3. `zero knowledge password manager`
4. `spreadsheet password generator`
5. `argon2 password generator`

> Additional long-tail terms for organic discovery: "password recipe", "no storage password manager", "offline password generator", "google sheets security", "stateless password generator"
