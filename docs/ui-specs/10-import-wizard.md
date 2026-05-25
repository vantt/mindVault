# 10 — Import Wizard (Modal, 3 Bước)

Triggered bởi `[+ Import Profile]` trong Profiles tab → gọi `openImportWizard(sessionKey, onComplete)`.
Modal ID: `#modal-import`. Progress bar: 3 dots (`#imp-dot-1..3`) + label `#imp-step-label` ("Step N of 3").

---

## Step 1 — Paste / Upload Bundle

```
📥 Import Shared Profile
Provide the bundle JSON from the sender. You can paste the text or upload a .json file.

Bundle JSON
┌─────────────────────────────────────────┐
│ Paste bundle JSON here...               │
└─────────────────────────────────────────┘

[⬆️ Upload .json]

[bundle-status]                 [Continue →]
```

**Inputs:**
- Textarea `#imp-bundle-input` — paste JSON; `oninput` triggers live validation
- Hidden `<input type="file" accept=".json" #imp-file-input>` wrapped in label button — reads file text, populates textarea, then calls same validation

**Validation (`adapter.validateBundle`):**
- `bundle.type === "passchef-profile-share"`
- `bundle.version === "2.1"` (exact match; other versions → "Unsupported bundle version: X")
- Required fields: `sheetId`, `encryptedData`, `iv`, `exportSalt`
- On fail: red status `#imp-bundle-status` with error text
- On pass: green status — `✓ Valid v2.1 bundle · Profile: <name> · Sheet: <id[0..14]>...`
  (sheetId truncated to 14 chars with `...` suffix)

**Footer:** `[Continue →]` (`#imp-next-1`) — calls `validateBundle` again before advancing.
No "Back" button on Step 1.

---

## Step 2 — Sharing Password

```
🔑 Enter Sharing Password
Password given by the sender.

Sharing Password  [________________] 👁/🙈

[error area]

✓ Bundle: <profileName> · Sheet: <sheetId[0..20]>... · Exported: YYYY-MM-DD

[← Back]                            [Decrypt →]
```

**Inputs:**
- `#imp-sharing-pwd` (type=password, toggle via 👁 button: `password` ↔ `text`, icon switches to 🙈)

**Bundle summary** displayed inline on this step (not Step 1):
- profileName from bundle; sheetId truncated to 20 chars; exportedAt sliced to `[0..10]`

**Decrypt flow (`#imp-decrypt`):**
1. Empty password → `#imp-decrypt-error`: "Password is required." (no network call)
2. Button shows "Decrypting…" + disabled while awaiting `adapter.decryptBundle(bundle, pwd)`
3. Post-decrypt structure check: keys must match `/^[1-5]$/`, values `{base: string}`, `base.length ≤ 500`
4. Fail (wrong password or structure): error shown as `<p class="text-danger">` with `e.message`
   - Button restores to "Decrypt →" + re-enabled
5. Success → `state.derivedSecrets = secrets` → `renderStep(3)`

**Footer:** `[← Back]` → Step 1 | `[Decrypt →]` (`#imp-decrypt`)

---

## Step 3 — Name & Confirm

```
✏️ Name This Profile
Local name — only visible to you.

Profile Name  [<bundle.profileName>_____________]  (max 50)

Locked to sheet: <full sheetId, truncated at 30>...
This profile will only work on that sheet. No manual assignment needed.

[← Back]                               [Import]
```

**Inputs:**
- `#imp-profile-name` pre-filled with `bundle.profileName` (fallback: `"ImportedProfile"`), `maxlength=50`

**On Import (`#imp-confirm` → `confirmImport`):**
1. Trim name; empty → `alert("Profile name is required.")` (native browser alert, not inline)
2. Build storage key: `shared:<localName>`
3. Encrypt `{ secrets: derivedSecrets, isShared: true }` with `sessionKey` via `encryptWithKey`
4. Write to `chrome.storage.sync`:
   ```
   "shared:<name>": {
     ...encrypted,
     readOnly: true,
     sheetId: bundle.sheetId,
     meta: {
       importedFrom: bundle.profileName,
       importedAt: YYYY-MM-DD,
       bundleId: bundle.bundleId || ""
     }
   }
   ```
5. Hide modal (`#modal-import` ← add class `hidden`)
6. Call `state.onComplete?.()` — caller (`options.js`) is responsible for showing toast

**No service worker involved** — entire flow is client-side (storage write via `chrome.storage.sync` directly).

**Footer:** `[← Back]` → Step 2 | `[Import]` (`#imp-confirm`)

---

## Close Behaviour

`#btn-close-import`: if `state.step > 1` → `confirm("Discard import?")` before closing.
Step 1 closes immediately.

---

## Post-Import Routing

Profile stored with `readOnly: true` + `sheetId` from bundle.
Extension auto-routes Consumer when that sheet is opened (sheetId locked in stored profile).
No manual sheet assignment needed.

---

## i18n Keys

| Key | en | vi | Used in HTML |
|---|---|---|---|
| `importWizardTitle` | "Import Shared Profile" | "Nhập Hồ sơ Chia sẻ" | `data-i18n` on `<h3>` |
| `lblSheetLocked` | "Locked to sheet" | "Khóa với sheet" | defined but not used in wizard JS |

**i18n gaps (hardcoded strings in JS, not i18n-keyed):**
- Step titles: "📥 Import Shared Profile", "🔑 Enter Sharing Password", "✏️ Name This Profile"
- Step description paragraphs
- Button labels: "Continue →", "← Back", "Decrypt →", "Import", "Decrypting…"
- Validation messages: "Not valid JSON", "Not a PassChef bundle", "Unsupported bundle version: X",
  "Incomplete bundle — missing required fields", "Password is required.", "Bundle contains invalid secret structure"
- Decrypt error fallback: `e.message` (English from crypto layer)
- Step counter: "Step N of 3"
- Confirm dialog: "Discard import?"
- Alert: "Profile name is required."
