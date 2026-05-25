# 09 — Export Wizard (Modal, 3 Bước)

Triggered bởi `[Export]` trên any own profile (`options.js` → `openExportWizard`).

## Modal Shell

```
┌──────────────────────────────────────── × ┐
│  Export Profile: "TeamShare"               │  ← hardcoded JS string, NOT data-i18n
│  ──────────────────────────────────────    │
│  ● ● ○   Step 2 of 3                       │  ← label = "Complete" after generate
│                                            │
│  [step content]                            │
│                                            │
│  [← Back]                   [Continue →]  │
└────────────────────────────────────────────┘
```

**HTML IDs:**
- Modal overlay: `#modal-export`
- Title: `#export-modal-title` (set via `element.textContent`, not i18n attribute)
- Progress dots: `#exp-dot-1`, `#exp-dot-2`, `#exp-dot-3` + label `#exp-step-label`
- Step content container: `#export-step-content` (innerHTML replaced per step)
- Footer: `#export-modal-footer` (innerHTML replaced per step)
- Close: `#btn-close-export`

**Dot classes:** `wizard-dot` + `done` (past) | `current` | `` (upcoming)

**`[×]` close behaviour:** `confirmClose('modal-export')` — shows native `confirm("Discard export?")` ONLY when `state.step > 1 && state.step <= 3`. No dialog if on step 1, or after bundle is generated (done state).

---

## Step 1 — Relationship Label

```
📝 Set Relationship Label

Identifies who you're sharing with. Used to derive isolated secrets —
changing it later invalidates existing bundles.

Label: [________________________________]
       e.g. "team-alice-2026"

⚠️ Keep private. Lowercase, numbers, hyphens only.
```

**Footer:** `[Continue →]` only (no Back button).

**Input rules:**
- Element id: `#exp-label`
- `pattern="[a-z0-9_-]+"` / `maxlength="50"`
- Regex validation on Continue: `/^[a-z0-9_-]+$/` (underscore also allowed — placeholder text "hyphens only" understates this)
- Error via `alert("Label must be lowercase letters, numbers, and hyphens only.")` — underscore omitted from message text (i18n gap)
- Value persisted in `state.label`; pre-filled if user navigates back

---

## Step 2 — Sheet to Lock

```
🔒 Lock to Google Sheet

Passwords from this bundle will ONLY work on this specific sheet.
Copying the sheet changes its ID — derived passwords won't work on copies.

Sheet URL or ID:
[___________________________________________]

[📋 Use current sheet]   ← message: GET_SHEET_ID_FROM_ACTIVE_TAB
                           no disabled state; shows "No Google Sheet tab found"
                           inline if no Sheets tab active

Sheet ID preview (inline, same row): ✓ 1BxCdef...

ℹ️ After sharing, set up account passwords using derived secrets:
   use this extension on the shared sheet (as Owner) to generate
   and update each account password.
```

**Footer:** `[← Back]`  `[Continue →]`

**Sheet ID extraction (`extractSheetId`):**
1. URL pattern: `/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/` → captures ID
2. Bare ID: `/^[a-zA-Z0-9_-]{20,}$/` → accepted as-is
3. Else: `alert("Invalid Sheet URL or ID.")`

**"Use current sheet" button** (`#btn-use-current-sheet`):
- Sends `{ action: "GET_SHEET_ID_FROM_ACTIVE_TAB" }` to service worker
- Service worker calls `getSheetIdFromActiveTab()` and returns `{ sheetId }`
- On success: fills `#exp-sheet` input + sets `#exp-sheet-preview` to `✓ {sheetId}`
- On failure/no tab: `#exp-sheet-preview` = `"No Google Sheet tab found"` (no alert, no disable)

**On Continue:** extracts, validates, updates `#exp-sheet-preview`, saves `state.sheetId`, advances to step 3.

---

## Step 3 — Sharing Password & Bundle Generation

```
🔑 Set Sharing Password

This password protects the bundle. The recipient needs it to import.

Sharing Password:  [____________________] 👁   ← toggle: 👁 / 🙈
Confirm Password:  [____________________]        ← no toggle on confirm field

[status area #exp-step3-status]
```

**Footer:** `[← Back]`  `[Generate Bundle]`

**No strength meter** — spec previously showed one; it does not exist in code.

**Validation (inline, in `#exp-step3-status`):**
- Empty password → `<p class="text-danger">Sharing password is required.</p>`
- Mismatch → `<p class="text-danger">Passwords do not match.</p>`
- Min length: none enforced beyond non-empty check

**Generate button** (`#exp-generate`): sets `textContent = "Generating…"`, `disabled = true` during async call; re-enables on error.

---

## Bundle Ready (Post-Generate, Still Step 3 Container)

After successful `adapter.createBundle()`, `#export-step-content` and `#export-modal-footer` are fully replaced:

```
✅ Bundle Ready

┌──────────────────────────────────────────┐
│ <textarea readonly class="bundle-textarea readonly">  │
│   { bundle JSON, 2-space pretty-printed } │
└──────────────────────────────────────────┘

[📋 Copy Bundle]   [⬇️ Download .json]

⚠️ Next step: Use this extension on the shared sheet (as Owner)
   to generate and update each account password using derived secrets.

⚠️ Transmit via encrypted channel only.
```

**Footer:** `[Done]` only (no Back).

**Progress dots:** `updateDots('exp', 4, 3)` → all dots = `done`, label = `"Complete"`.

**Copy** (`#btn-copy-bundle`): `navigator.clipboard.writeText(bundleJson)`, button text → `"✓ Copied"`.

**Download** (`#btn-download-bundle`): blob URL, filename = `passchef-bundle-{profileName}.json`.

**Done** (`#exp-done`): hides `#modal-export`, calls `state.onComplete?.()`.

---

## Bundle JSON Shape

```json
{
  "type": "passchef-profile-share",
  "version": "2.1",
  "bundleId": "<uuid>",
  "profileName": "<string>",
  "exportedAt": "<ISO8601>",
  "sheetId": "<string>",
  "encryptedData": [<byte array>],
  "iv": [<12 bytes>],
  "exportSalt": [<16 bytes>]
}
```

- Encryption: AES-GCM, key from PBKDF2-SHA256 (100 000 iterations)
- Payload: HKDF-SHA256 derived secrets (`DS_i`) — originals never exported
- `encryptedData` / `iv` / `exportSalt`: plain JS number arrays (not base64)

---

## i18n Keys

| Key | en value | Used in wizard JS? |
|-----|----------|--------------------|
| `exportWizardTitle` | `"Export Profile"` | No — title set via JS string template `Export Profile: "${profileName}"` |
| `exportNextStep` | `"Use this extension on the shared sheet to generate and set account passwords using derived secrets."` | No — warning text hardcoded in innerHTML |

**i18n gaps (all hardcoded JS strings):**
- Modal title with profile name
- All step section titles and descriptions
- Validation alerts (`alert(...)`)
- Button labels (Continue, Back, Generate Bundle, Done, Copy Bundle, Download .json)
- Status messages ("Generating…", "✅ Bundle Ready", "No Google Sheet tab found", "✓ Copied")
- Warning boxes

---

## State Object

```js
state = {
  profileName, profileKey, sessionKey,   // passed in
  onComplete,                            // callback
  step,                                  // 1 | 2 | 3
  label,                                 // set after step 1
  sheetId                                // set after step 2
}
```
