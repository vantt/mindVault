# mindVault Password Generator

**Turn Google Sheets into a Secure Password Manager**

> **Note:** This repository contains two projects:
>
> 1. **[Chrome Extension](./chrome-extension)**: The main password manager tool.
> 2. **[Landing Page](./landing-page)**: Marketing website.

mindVault is a Chrome Extension that allows you to generate strong, consistent passwords directly from your Google Sheets cells using a formula-based approach (e.g., `r4nd0m#1`). It uses **Argon2id** and **AES-256-GCM** to ensure your master secrets are secure.

## Why is this Safe? (The "Recipe" Insight)

> **Your Google Sheet can stay public. Your passwords stay private.**

### The Problem

> **Think like a Chef, not an Admin.**

Storing passwords in Google Sheets is like leaving cooked food out in the open. Anyone who finds it can eat it.

### The "Cookbook" Solution

Instead of storing the food (Password), we only store the **Recipe**.

- **Part 1 (The Menu)**: Publicly visible in your sheet. e.g., "Facebook: **r4nd0m`#1`**".
- **Part 2 (The Secret Spice)**: Kept only in your head. e.g., "Spice #1 is `Basic*`".

When you need to log in, you become the Chef. You look at the menu (`r4nd0m#1`), take the ingredients (`r4nd0m`), and add your secret spice (`Basic*`) according to the instructions (`#` = put spice on top).

**Result**: A perfect password `Basic*r4nd0m` that never existed until you cooked it.

### Why It Works

**Hacker Proof**: If a hacker steals your sheet, they just get a list of recipes. They can't "cook" the password without your secret spices.
**Convenience**: You manage a whole menu of 100+ accounts but only need to remember few jars of spices.
**Taste Control**: Need to change a password? Just update the recipe version (`_v2`).
**Freshness**: Passwords are cooked fresh on demand, never stored stale.

### The Recipe (Math)

```
What's on your sheet: r4nd0m`#1`
What you remember: `Basic*`
Your actual password: `Basic*r4nd0m`
```

_Simple formula. Unbreakable security._

![Security Diagram](./docs/security_diagram.png)

## Features

- 🔐 **Zero-Knowledge**: Your master password and secrets never leave your device (stored encrypted in Chrome Sync).
- ⚡ **Instant Generation**: Click any cell with a formula (e.g., `fb#1`) to generate a password.
- ⌨️ **Hotkey Support**: Press `Ctrl+Shift+L` (or `Cmd+Shift+L` on Mac) to generate from the focused cell.
- 📋 **Auto-Clear Clipboard**: Passwords are automatically cleared from your clipboard after 30 seconds.
- 🔒 **Auto-Lock**: Automatically locks after 5 minutes of inactivity or 10 minutes total.
- 🌍 **Localization**: Supports English and **Vietnamese**.

                 |

## Core Documentations

- [Password System Design](./docs/password-system-design.md)
- [Product Requirements Document (PRD)](./docs/prd.md)
- [Security Assessment](./docs/security-assessment.md)

## Installation (Developer Mode)

1. **Clone or Download** this repository.
2. Open Chrome and navigate to `chrome://extensions/`.
3. Enable **Developer mode** (top right toggle).
4. Click **Load unpacked**.
5. Select the `src` folder inside `chrome-extension` (e.g., `./chrome-extension/src`).

## Usage

### 1. Setup

- Click the extension icon.
- Create a strong **Master Password**. This is used to encrypt your secret phrases.
- **Important**: If you lose this password, you lose access to your secrets. There is no reset.

### 2. Configure Secrets

- Enter up to 5 secret phrases corresponding to different modifiers:
  - `1` (Prefix `#`): e.g., "MySecretPrefix"
  - `2` (Middle `@`): e.g., "MySecretMiddle"
  - ...
- Save your changes.

### 3. Generate Passwords

- Open any Google Sheet.
- Type a formula into a cell, e.g., `fb#1` (Service: fb, Modifier: #, Secret Index: 1).
- **Click the cell** OR press `Ctrl+Shift+L`.
- A popup will appear with your generated password. Click **Copy**.

## Recipe Syntax

`<Ingredient><CookingStyle><SpiceIndex>[_Version]`

- **Ingredient** (Hash): Any random text (e.g., `f3c4b00k`, `r4nd0m`).
- **Cooking Style** (Modifier):
  - `#`: **Topping** (Prefix Secret)
  - `@`: **Filling** (Middle Secret)
  - `$`: **Base** (Suffix Secret)
  - `%`: **Mix** (Interleaved)
  - `^`: **Sandwich** (Paired)
- **Spice Index**: 1-5 (Which secret jar to use).
- **Version** (Optional): `_v2`, `_v3` to rotate passwords.

Example: `gmail@2_v2`

## Development

- **Architecture**: Domain-Driven Design (DDD) with Clean Architecture.
- **Tech Stack**: Vanilla JS, Chrome Extensions Manifest V3.
- **Security**: Argon2 (WASM), Web Crypto API.

## Testing

This project uses **Vitest** for Unit/Integration tests and **Playwright** for E2E tests.

### Running Tests

We support `npm`, `pnpm`, or `yarn`. Commands below use `pnpm` as recommended.

- **Run All Tests**:

  ```bash
  cd chrome-extension
  pnpm test
  ```

  _Runs Unit tests followed by E2E tests._

- **Unit Tests Only**:

  ```bash
  pnpm test:unit
  ```

- **E2E Tests Only**:
  ```bash
  pnpm test:e2e
  ```

### Test Protocols

- **Golden Tests**: Tests marked with `// @approved` are considered "Source of Truth". **Do not modify** them without explicit user approval.
