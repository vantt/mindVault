# 07 — Secrets Tab

Default active tab khi unlock. Nội dung nằm trong `#tab-secrets` bên trong `#dashboard-section`.

## HTML Structure

```html
<div id="tab-secrets" class="tab-content">
  <div class="card">
    <h2 data-i18n="manageSecretsTitle">Manage Secrets</h2>

    <!-- Profile context label (populated by JS) -->
    <p id="secrets-profile-label" class="text-muted"></p>

    <p data-i18n="manageSecretsDesc">Configure your 5 secret phrases…</p>

    <form id="secrets-form">
      <!-- 5 secret inputs (data-index="1"…"5") -->
      <div class="secret-item">
        <label>Secret #N</label>
        <div class="input-wrapper">
          <input type="password" class="secret-input" data-index="N"
                 placeholder="Enter secret phrase" />
          <button type="button" class="btn-toggle-visibility">👁</button>
        </div>
      </div>
      <!-- …repeated for indices 1–5… -->

      <!-- Inline settings row -->
      <div class="secret-item">
        <label data-i18n="lblSettings">Settings</label>
        <div class="form-group checkbox-group">
          <input type="checkbox" id="setting-peppering-hint" />
          <label for="setting-peppering-hint" data-i18n="lblPepperingHint">
            Show pepper hint
          </label>
        </div>
      </div>

      <!-- Actions -->
      <div class="actions">
        <button type="submit" class="btn primary" data-i18n="btnSafeChanges">Save Changes</button>
        <button type="button" id="btn-change-pwd" class="btn secondary" data-i18n="btnChangePassword">Change Password</button>
        <button type="button" id="btn-lock" class="btn secondary" data-i18n="btnLock">Lock</button>
      </div>
    </form>
  </div>
</div>
```

## Key DOM IDs / Selectors

| Selector | Purpose |
|----------|---------|
| `#tab-secrets` | Tab content wrapper (`.tab-content`) |
| `#secrets-form` | Submit handler saves all 5 secrets + settings |
| `.secret-input[data-index="1"…"5"]` | Password fields for secrets 1–5 |
| `#setting-peppering-hint` | Checkbox: show/hide pepper hint in popup |
| `#secrets-profile-label` | Displays active profile name (pill badge via CSS) |
| `#btn-change-pwd` | Shortcut → calls `switchTab("settings")` |
| `#btn-lock` | Clears `sessionKey` from session storage → `location.reload()` |
| `.btn-toggle-visibility` | Toggles `input.type` password ↔ text; icon cycles 👁 / 🙈 |

## JS Behavior (`options.js`)

**`loadSecrets()`** — called on unlock and after setup:
1. Reads `chrome.storage.sync` for `activeProfileKey` (e.g. `"profile:Default"`)
2. Falls back to legacy `encryptedData`/`iv` keys if profile slot is empty (pre-migration)
3. Decrypts with `sessionKey` via `decryptWithKey()`
4. Populates `.secret-input[data-index]` inputs from `decrypted.secrets[idx].base`
5. Sets `#setting-peppering-hint` checked from `decrypted.settings.pepperingHint`
6. Sets `#secrets-profile-label` text to `"Editing: {profileName}"` (strips `"profile:"` prefix)

**`#secrets-form` submit**:
- Collects all `.secret-input` values into `{ secrets: { "1": { base }, … }, settings: { pepperingHint } }`
- Encrypts with `encryptWithKey()` → saves to `chrome.storage.sync[activeProfileKey]`
- Shows toast via `toastSaveSuccess` i18n key on success

## CSS (`#secrets-profile-label`)

```css
#secrets-profile-label {
  font-size: .78rem;
  color: var(--text-muted);
  background: var(--surface2);
  border: 1px solid var(--border);
  border-radius: 20px;
  padding: 2px 10px;
  display: inline-block;
  margin: -.25rem 0 1rem;
}
```

## i18n Coverage

| Key | Used for |
|-----|---------|
| `manageSecretsTitle` | `<h2>` heading |
| `manageSecretsDesc` | Description paragraph |
| `lblSettings` | Inline settings label |
| `lblPepperingHint` | Peppering checkbox label |
| `btnSafeChanges` | Save button |
| `btnChangePassword` | Change-pwd redirect button |
| `btnLock` | Lock button |
| `toastSaveSuccess` | Toast on save |

**i18n gaps (hardcoded strings):**
- `placeholder="Enter secret phrase"` — key `placeholderSecret` exists but is NOT wired to the inputs in HTML
- `"Editing: {profileName}"` — profile label text is hardcoded in JS (`loadSecrets`)
- `"Save failed: …"` / `"Failed to load secrets"` — error toasts are hardcoded in JS

## Rules

- Tab is active by default (button has `.active` in HTML; panel has no `.hidden`)
- `activeProfileKey` is a module-level var in `options.js`; switching profiles (from Profiles tab) must update it and re-call `loadSecrets()`
- Peppering hint setting is stored per-profile inside the encrypted blob, not in plain storage

## Related Screens

- [06-options-tab-navigation.md](./06-options-tab-navigation.md) — Tab bar controlling visibility
