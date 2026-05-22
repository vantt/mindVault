# 08 — Profiles Tab

Tab chính cho v2. Chia thành 3 sections: My Profiles, Shared Profiles, Sheet Assignments.

## Layout

```
┌─ My Profiles ──────────────────────────────────────────┐
│  [New Profile]                                         │
│                                                        │
│  ╔══════════════════════════════════════════════════╗  │
│  ║ ● Default                          ⭐ default    ║  │
│  ║   No sheet assignments                          ║  │
│  ║   [✏️ Secrets]  [↑ Export]                       ║  │
│  ╚══════════════════════════════════════════════════╝  │
│                                                        │
│  ╔══════════════════════════════════════════════════╗  │
│  ║ ● Banking                                        ║  │
│  ║   2 sheet assignments                           ║  │
│  ║   [✏️ Secrets]  [↑ Export]  [★ Default]          ║  │
│  ║                               [Delete]          ║  │
│  ╚══════════════════════════════════════════════════╝  │
│                                                        │
├─ Shared Profiles ──────────────────────────────────────┤
│  [Import Shared]                                       │
│                                                        │
│  ╔══════════════════════════════════════════════════╗  │
│  ║ 📥 TeamFromB                    🔒 read-only     ║  │
│  ║   From: User B · Imported: 2026-01-10           ║  │
│  ║   Sheet: 1BxCdefGhIjklmn... (locked)            ║  │
│  ║                                   [Remove]      ║  │
│  ╚══════════════════════════════════════════════════╝  │
│                                                        │
├─ Sheet Assignments ────────────────────────────────────┤
│  [+ Add Assignment]                                    │
│                                                        │
│  ┌──────────────────────────────────────────────────┐  │
│  │ ABC123DEFghiJklmnopqr...  [Default     ▼]  [✕]  │  │
│  └──────────────────────────────────────────────────┘  │
│                                                        │
│  Unknown sheets → [Default                        ▼]  │
└────────────────────────────────────────────────────────┘
```

> **Note:** Shared profiles KHÔNG xuất hiện trong Sheet Assignments dropdown — chúng tự route qua `sheetId` locked trong bundle.

---

## 3.1 Profile Card (Own)

```
╔═══════════════════════════════════════════════╗
║  ● Profile Name                  [badge]      ║
║  N sheet assignment(s) / No sheet assignments ║
║  [✏️ Secrets]  [↑ Export]  [★ Default] [Delete]║
║  (delete guard warn-text spans full width)    ║
╚═══════════════════════════════════════════════╝
```

Rendered via JS (no `data-i18n`). Button labels are **hardcoded emoji strings**, not i18n keys:
- `✏️ Secrets` (not "Edit Secrets")
- `↑ Export`
- `★ Default`
- `Delete`

**Color strip:** 3px left border using `--profile-color` CSS var. Color dot (9×9px circle) inside header.

**Color cycle (5 values, assigned by index % 5):**

`#58a6ff` · `#e3b341` · `#3fb950` · `#a371f7` · `#f78166`

**Empty state:** `"No profiles yet. Create one above."` (hardcoded, no i18n).

### Actions

| Profile type | Actions available |
|---|---|
| Own (default) | ✏️ Secrets · ↑ Export |
| Own (non-default) | ✏️ Secrets · ↑ Export · ★ Default · Delete |

### Delete guard

Khi `sheetCount > 0` → inline `warn-text` span hiển thị (amber color, full width):

> *"Assigned to N sheet(s). Reassign before deleting."*

(Hardcoded string — `errProfileAssigned` i18n key tồn tại nhưng **không được dùng** trong code.)

Khi `sheetCount === 0` → `confirm()` native dialog trước khi xóa:

> *"Delete profile \"Name\"? This cannot be undone."*

### After Create

Sau khi create profile, Edit Secrets modal mở ngay lập tức (`openEditSecretsModal` được gọi từ `createProfile`).

---

## 3.2 Shared Profile Card

```
╔═══════════════════════════════════════════════╗
║  📥 TeamFromB               🔒 read-only      ║
║  From: User B · Imported: 2026-01-10          ║
║  Sheet: 1BxCdefGhIjklmn... (locked)           ║
║                                  [Remove]    ║
╚═══════════════════════════════════════════════╝
```

- Section title i18n key `profilesSharedTitle` → "Shared Profiles" (không phải "Shared Profiles (Received)")
- SheetId truncated: `data.sheetId.slice(0, 14) + '...'` (14 chars, not 12)
- Meta lines: `From: {meta.importedFrom} · Imported: {meta.importedAt.slice(0,10)}` (cả hai optional)
- `profile-meta monospace` class cho Sheet line
- `[Remove]` → `confirm()` dialog → `chrome.storage.sync.remove(key)` → re-render
- Shared card gets `.profile-card.shared` → `var(--readonly)` background + muted left strip
- Empty state: `"No imported profiles."` (hardcoded)

---

## 3.3 Sheet Assignments

```
┌─ Sheet Assignments ──────────────────────────────────┐
│ [+ Add Assignment]   ← hardcoded, no i18n            │
│                                                      │
│ ┌──────────────────────────────────────────────────┐ │
│ │ ABC123DEFghijklmnopqr...  [Profile ▼]  [✕]       │ │
│ └──────────────────────────────────────────────────┘ │
│                                                      │
│ Unknown sheets →  [Default                       ▼] │
└──────────────────────────────────────────────────────┘
```

- **Add Assignment flow:** `prompt()` native dialog (browser built-in) — NOT an inline form. User pastes Sheet ID or URL.
- **Auto-extract:** regex `/\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/` extracts ID from pasted URL; raw IDs validated by `/^[a-zA-Z0-9_-]{20,}$/`.
- **No "Label" field** — assignments store only `{ sheetId: profileName }` mapping. No human-readable label.
- **No "Use current sheet" button** — not implemented.
- **Sheet ID display:** truncated at 20 chars: `sheetId.slice(0, 20) + '...'` (with `title` attr for full ID on hover).
- **Profile select:** dropdown with only own profiles (no shared profiles in list).
- **Remove:** `✕` button → removes entry from `sheetMapping` in `chrome.storage.sync` → re-render.
- **Inline save:** `select.onchange` immediately writes to `chrome.storage.sync` (no Save button).
- **Default fallback row:** `select#select-default-profile` → `onchange` writes `defaultProfile` key to sync storage → `showToast("Default profile updated")`.

---

## Storage & SW Interactions

**No Service Worker message actions.** All CRUD goes directly via `chrome.storage.sync`:

| Operation | Storage call |
|---|---|
| Create profile | `chrome.storage.sync.set({ "profile:{name}": encryptedData })` |
| Delete profile | `chrome.storage.sync.remove("profile:{name}")` |
| Set default | `chrome.storage.sync.set({ defaultProfile: name })` |
| Remove shared | `chrome.storage.sync.remove("shared:{name}")` |
| Add assignment | `chrome.storage.sync.set({ sheetMapping: {...} })` |
| Remove assignment | `chrome.storage.sync.set({ sheetMapping: {...} })` (entry deleted) |
| Inline profile change | `chrome.storage.sync.set({ sheetMapping: {...} })` on select change |
| Set default fallback | `chrome.storage.sync.set({ defaultProfile: value })` on select change |

Storage keys: `profile:{name}` (own), `shared:{name}` (shared), `sheetMapping` (object), `defaultProfile` (string).

Secrets encrypted with `encryptWithKey` / decrypted with `decryptWithKey` (AES, sessionKey from `chrome.storage.session`).

---

## Edit Secrets Modal (inline, not a separate screen)

Opened by `[✏️ Secrets]` button OR automatically after profile creation.

- 5 secret slots, each: `<input type="password">` + `👁` toggle button
- Toggle switches `type` between `password` / `text`, icon between `👁` / `🙈`
- On open: decrypts existing secrets and pre-fills inputs
- On save: re-encrypts all 5 values + preserves `settings` object → `chrome.storage.sync.set`
- Modal ID: `modal-edit-secrets`

---

## i18n Gaps (hardcoded strings not using i18n keys)

The following strings are hardcoded in JS and NOT using i18n keys:

- `"No profiles yet. Create one above."` — own profiles empty state
- `"No imported profiles."` — shared profiles empty state
- `"N sheet assignment(s)"` / `"No sheet assignments"` — profile meta (key `lblUsedBySheets` exists but unused)
- `"✏️ Secrets"` / `"↑ Export"` / `"★ Default"` / `"Delete"` / `"Remove"` — card action buttons (keys exist but unused)
- `"Assigned to N sheet(s). Reassign before deleting."` — delete guard (key `errProfileAssigned` exists but unused)
- `"Delete profile \"Name\"? This cannot be undone."` — delete confirm dialog
- `"Remove imported profile \"Name\"?"` — remove shared confirm dialog
- `"Default profile updated"` — default fallback save toast
- `"+ Add Assignment"` — add assignment button (no i18n key)
- `"Unknown sheets →"` — default fallback label (no i18n key)
- `"Sheet: ... (locked)"` — shared card sheet line (key `lblSheetLocked` exists but unused)
- `"From: ... · Imported: ..."` — shared card meta (key `lblImportedFrom` exists but unused)

---

## Related Screens

- [09-export-wizard.md](./09-export-wizard.md) — triggered by `[↑ Export]`
- [10-import-wizard.md](./10-import-wizard.md) — triggered by `[Import Shared]`
- [11-edit-secrets-modal.md](./11-edit-secrets-modal.md) — triggered by `[✏️ Secrets]` or auto-opened after profile creation
- [12-new-profile-modal.md](./12-new-profile-modal.md) — triggered by `[New Profile]`

## i18n Keys (defined in `_locales/en` and `_locales/vi`)

| Key | EN value | VI value |
|---|---|---|
| `profilesOwnTitle` | My Profiles | Hồ sơ của tôi |
| `profilesSharedTitle` | Shared Profiles | Hồ sơ được chia sẻ |
| `profilesAssignTitle` | Sheet Assignments | Gán Sheet |
| `btnNewProfile` | New Profile | Tạo Hồ sơ |
| `btnImportProfile` | Import Shared | Nhập Hồ sơ |
| `btnEditSecrets` | Edit Secrets | Sửa Bí mật |
| `btnExportProfile` | Export | Xuất |
| `btnSetDefault` | Set Default | Đặt mặc định |
| `btnRemoveProfile` | Remove | Xóa |
| `lblSheetLocked` | Locked to sheet | Khóa với sheet |
| `lblImportedFrom` | Imported from | Nhập từ |
| `lblUsedBySheets` | Used by sheets | Dùng bởi sheets |
| `errProfileAssigned` | Profile is assigned to sheets. Remove assignments first. | Hồ sơ đang được gán cho sheets. Hãy xóa gán trước. |

> Keys `btnEditSecrets`, `btnExportProfile`, `btnSetDefault`, `btnRemoveProfile`, `lblSheetLocked`, `lblImportedFrom`, `lblUsedBySheets`, `errProfileAssigned` are **defined but not wired** to any rendered element — all corresponding UI text is hardcoded in JS.
