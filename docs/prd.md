# PRD — PassChef Password Generator

## What is PassChef?

PassChef is a **Chrome extension** that generates passwords on-demand by combining two ingredients:

1. A **Recipe** — a short formula stored openly in Google Sheets (e.g., `r4nd0m#1`)
2. A **Secret Spice** — one of your 5 private phrases, encrypted locally in the extension

The password is never stored anywhere. It is cooked fresh each time. Without your secret phrases, the recipe in the spreadsheet is meaningless to an attacker.

> **The "Sous-Chef" model:** You manage recipes in a spreadsheet like an IT admin manages a menu. PassChef reads the recipe, fetches the secret from its encrypted vault, and serves you the finished password — in under 2 seconds, with one click.

---

## Unique Selling Points

| # | USP                                      | Detail                                                                                  |
| - | ---------------------------------------- | --------------------------------------------------------------------------------------- |
| 1 | **Zero stored passwords**          | Passwords are derived on-demand, never persisted anywhere                               |
| 2 | **Spreadsheet as ledger**          | Recipes live in plain sight in Google Sheets; without secret spices they reveal nothing |
| 3 | **Military-grade encryption**      | Argon2id (OWASP 2024) + AES-256-GCM protects all secrets at rest                        |
| 4 | **Cross-device sync**              | Encrypted blob syncs via your Chrome account — no server, no cloud vendor              |
| 5 | **One-click workflow**             | Select recipe cell → click icon → copy password (< 3 clicks, < 2 seconds)             |
| 6 | **Multi-profile sharing** *(v2)* | Share access with teammates without exposing your master password                       |

---

## The Chef Metaphor

| Culinary Term           | PassChef Concept                    | Example           |
| ----------------------- | ----------------------------------- | ----------------- |
| **Recipe**        | Formula stored in Google Sheet      | `r4nd0m#1`      |
| **Secret Spice**  | One of 5 private phrases            | `Basic*`        |
| **Cooking Style** | How recipe + secret are combined    | `#` = prefix    |
| **Topping**       | Modifier that transforms the output | `!` = uppercase |
| **Dish**          | The generated password              | `Basic*r4nd0m`  |

---

## Recipe Grammar (v1.x — Current)

```
<hash><cooking_style><secret_index>[modifiers][.<verification_tag>]

Cooking styles:
  r4nd0m#1   → # Top Garnish   secret + hash   →  "Basic*r4nd0m"
  r4nd0m$3   → $ Base Sauce    hash + secret   →  "r4nd0mUltra$"
  r4nd0m@2   → @ Filling       hash[mid]+secret →  "r4nSecure#d0m"
  r4nd0m%4   → % Mixed Salad   interleave chars →  "rT4rnadd0em&"
  r4nd0m^4   → ^ Layer Cake    interleave pairs →  "r4Trnd..."

Modifiers (stackable):
  _  Flip      secret moves to opposite end
  !  Sear      UPPERCASE the secret
  ?  Stir      reverse the secret
  ~  Mild      strip special chars from secret

  r4nd0m#1_!  → flip then uppercase → "r4nd0mBASIC*"

Verification tag (v2.2+):
  r4nd0m#1.ab3z  → 4-char HMAC suffix; mismatch = RecipeProfileMismatchError
```

---

## Core Features by Version

### v1.x MVP — Active

- Detects recipe when user selects a Google Sheets cell (hotkey `Ctrl+Shift+L`)
- 5 secret phrases encrypted with AES-256-GCM, synced via `chrome.storage.sync`
- Argon2id key derivation (64 MB memory-hard, WebAssembly) with PBKDF2-600k fallback
- In-popup unlock flow — no separate settings page required
- Session security: 10-min absolute timeout, 5-min idle auto-lock
- Clipboard auto-clears 30 seconds after copy

### v2.x Multi-Profile — Design Finalized, Pending Implementation

- **Named Profiles** — multiple independent secret sets (Personal, Banking, TeamShare, …)
- **Sheet routing** — each Google Sheet ID maps to a specific profile automatically
- **Full Access sharing** — export an encrypted bundle; recipient imports it and generates identical passwords without knowing your master password
- **Verification tag** — 4-char HMAC suffix on recipe detects profile/sheet mismatch at parse time, before any password is produced
- **Simple revocation** — revoking Google Sheets access is the only tier needed; no server required

---

## Security Model

| Layer           | Mechanism                                                                |
| --------------- | ------------------------------------------------------------------------ |
| Key derivation  | Argon2id primary (64 MB, 3 iterations) / PBKDF2-SHA256-600k fallback     |
| Encryption      | AES-256-GCM, 256-bit key, 12-byte random IV per write                    |
| Secret storage  | `chrome.storage.sync` — encrypted blob only, salt/IV in plaintext     |
| Session key     | `chrome.storage.session` — RAM only, never written to disk            |
| Clipboard       | Auto-clear after 30s                                                     |
| Profile sharing | Separate sharing password with independent PBKDF2 salt; raw secrets only |

**Known limitations (transparent):** JS memory cannot be securely zeroed; DevTools can inspect decrypted data while unlocked; malicious extensions can read the clipboard. PassChef is suitable for everyday convenience, not high-security environments.

---

## Version Index

All detailed PRDs live in [`docs/prd/`](./prd/).

| Version            | File                                                                | Status                                  |
| ------------------ | ------------------------------------------------------------------- | --------------------------------------- |
| v1.x MVP           | [`prd/v1-mvp.md`](./prd/v1-mvp.md)                                   | Active — grammar in use                |
| v2.x Multi-Profile | [`prd/v2-multi-sheet-profiles.md`](./prd/v2-multi-sheet-profiles.md) | Draft — design finalized, pending impl |

---

## Quick Reference

- **Recipe grammar (current):** `prd/v1-mvp.md` § 4
- **Cooking styles:** `#` prefix · `$` suffix · `@` middle · `%` interleave-char · `^` interleave-pair
- **Modifiers:** `_` flip · `!` uppercase · `?` reverse · `~` strip special chars
- **Sharing & multi-profile spec:** `prd/v2-multi-sheet-profiles.md`
- **Verification tag design rationale:** `recipe-tag-design-rationale.md`
