# UI Spec v2: Multi-Sheet Profiles & Shared Access

**Product:** mindVault Chrome Extension  
**Version:** 2.1 (Revised)  
**Date:** 2026-05-17  
**Status:** Draft — Ready for Implementation  
**PRD Reference:** [docs/prd/v2-multi-sheet-profiles.md](./prd/v2-multi-sheet-profiles.md)

### Changelog v2.1
- Export Wizard: 6 bước → 3 bước (bỏ Tier 2/3/4)
- Thêm Sheet ID field vào Export Wizard (sheet-bound generation)
- Bỏ expiry countdown khỏi Shared Profile card
- Bỏ error state "expired bundle"
- Consumer không cần manual assign sheet (sheetId locked trong bundle)

---

## 0. Design Tokens (unchanged from v1)

```css
--bg-color:       #0d1117
--card-bg:        #161b22
--text-primary:   #f0f6fc
--text-secondary: #8b949e
--accent:         #58a6ff
--accent-hover:   #1f6feb
--border:         #30363d
--danger:         #da3633
--success:        #238636
--warning:        #d29922
--readonly:       #21262d   /* new — shared/readonly bg */
Font: Outfit (Google Fonts), 300/400/500/600
Container width: 600px
Border-radius cards: 12px
Border-radius inputs/buttons: 6px
```

---

## 1. Options Page — Tab Navigation (New)

Tab bar hiển thị giữa header và main content, chỉ khi unlocked.

```
┌──────────────────────────────────────────────────────────┐
│  🔐 mindVault                                            │
│  Secure Password Generator                               │
│                                                          │
│  ┌───────────┬────────────┬────────────┐                 │
│  │  Secrets  │  Profiles  │  Settings  │  ← tab bar      │
│  └───────────┴────────────┴────────────┘                 │
│                                                          │
│  [active tab content]                                    │
└──────────────────────────────────────────────────────────┘
```

**Tab rules:**
- Tab bar only visible khi unlocked (dashboard-section)
- Default active tab: `Secrets` (preserves v1 behavior)
- Active tab: accent underline + white text; inactive: text-secondary

**Tab bar HTML:**
```html
<div id="tab-bar" class="tab-bar hidden">
  <button class="tab active" data-tab="secrets">Secrets</button>
  <button class="tab" data-tab="profiles">Profiles</button>
  <button class="tab" data-tab="settings">Settings</button>
</div>
```

**CSS:**
```css
.tab-bar {
  display: flex;
  border-bottom: 1px solid var(--border);
  margin-bottom: 1.5rem;
}
.tab {
  padding: 10px 20px;
  background: transparent;
  border: none;
  border-bottom: 2px solid transparent;
  color: var(--text-secondary);
  font-family: inherit;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.2s;
}
.tab.active {
  color: var(--text-primary);
  border-bottom-color: var(--accent);
}
```

---

## 2. Secrets Tab (v1 — unchanged)

Giữ nguyên dashboard-section. Wrap nội dung vào `<div id="tab-secrets" class="tab-content">`.

---

## 3. Profiles Tab (New)

### 3.1 Layout tổng quan

```
┌─ My Profiles ──────────────────────────────────────────┐
│  [+ New Profile]                                       │
│                                                        │
│  ╔══════════════════════════════════════════════════╗  │
│  ║ 🔵 Default                          ⭐ default   ║  │
│  ║     Used by: 2 sheets                           ║  │
│  ║     [Edit Secrets]  [Export]                    ║  │
│  ╚══════════════════════════════════════════════════╝  │
│                                                        │
│  ╔══════════════════════════════════════════════════╗  │
│  ║ 🟠 Banking                                       ║  │
│  ║     Used by: 1 sheet                            ║  │
│  ║     [Edit Secrets]  [Export]  [Set Default]     ║  │
│  ║                               [Delete]          ║  │
│  ╚══════════════════════════════════════════════════╝  │
│                                                        │
├─ Shared Profiles (Received) ───────────────────────────┤
│  [+ Import Profile]                                    │
│                                                        │
│  ╔══════════════════════════════════════════════════╗  │
│  ║ 📥 TeamFromB               🔒 read-only          ║  │
│  ║     From: User B · 2026-01-10                   ║  │
│  ║     Sheet: 1BxCdef...  (locked)                 ║  │
│  ║                                    [Remove]     ║  │
│  ╚══════════════════════════════════════════════════╝  │
│                                                        │
├─ Sheet Assignments ────────────────────────────────────┤
│  [+ Add Assignment]                                    │
│                                                        │
│  ┌──────────────────────────────────────────────────┐  │
│  │ ABC123...  "My Personal Sheet"                   │  │
│  │                        Profile: [Default     ▼] │  │
│  └──────────────────────────────────────────────────┘  │
│                                                        │
│  Unknown sheets → [Default                        ▼]  │
└────────────────────────────────────────────────────────┘
```

> **Note:** Shared profiles không xuất hiện trong Sheet Assignments dropdown — chúng tự route qua sheetId locked trong bundle.

### 3.2 Profile Card (Own)

```
╔═══════════════════════════════════════════════╗
║  ● Profile Name                  [badge]      ║
║  Used by: N sheet(s)                          ║
║  [Edit Secrets]  [Export]  [Set Default]      ║
║                             [Delete]          ║
╚═══════════════════════════════════════════════╝
```

**Color dots (auto-assigned):** `#58a6ff` · `#e3b341` · `#3fb950` · `#a371f7` → cycle

**Actions:**

| Profile type | Actions |
|---|---|
| Own (default) | Edit Secrets · Export |
| Own (non-default) | Edit Secrets · Export · Set Default · Delete |

**Delete guard:** Profile đang được assign → show inline warning:  
*"Assigned to N sheet(s). Reassign before deleting."*

### 3.3 Shared Profile Card

```
╔═══════════════════════════════════════════════╗
║  📥 TeamFromB               🔒 read-only      ║
║  From: User B · Imported: 2026-01-10          ║
║  Sheet: 1BxCdefGhIj... (locked)               ║
║                                  [Remove]    ║
╚═══════════════════════════════════════════════╝
```

- Không có expiry info (bỏ Tier 2)
- SheetId hiển thị dạng truncated (12 chars + "...")
- `[Remove]` xóa profile khỏi extension (không ảnh hưởng Owner)

### 3.4 Sheet Assignments

Chỉ dành cho **own profiles**. Shared profiles tự route.

```
┌─ Sheet Assignments ──────────────────────────────────┐
│ [+ Add Assignment]                                   │
│                                                      │
│ ┌──────────────────────────────────────────────────┐ │
│ │ Sheet ID / URL: [_______________________________] │ │
│ │ Label:          [My Personal Sheet  ] (optional) │ │
│ │ Profile:        [Default            ▼]           │ │
│ │                                      [Remove]   │ │
│ └──────────────────────────────────────────────────┘ │
│                                                      │
│ Default fallback:                                    │
│ Unknown sheets → [Default                        ▼] │
└──────────────────────────────────────────────────────┘
```

Sheet ID input: auto-extract ID từ pasted Google Sheets URL.  
"Use current sheet" button: fills từ active tab nếu có GSheets tab open.

---

## 4. Export Wizard (Modal, 3 Bước)

Triggered bởi `[Export]` trên any own profile.

### Modal Shell

```
┌──────────────────────────────────────── × ┐
│  Export Profile: "TeamShare"               │
│  ──────────────────────────────────────    │
│  ● ● ○   Step 2 of 3                       │
│                                            │
│  [step content]                            │
│                                            │
│  [← Back]                   [Continue →]  │
└────────────────────────────────────────────┘
```

Progress dots: filled = done, accent = current, empty = upcoming.  
`[×]` → confirm dialog nếu đã bắt đầu ("Discard export?")

### Step 1 — Relationship Label

```
📝 Set Relationship Label

Identifies who you're sharing with.
Used to derive isolated secrets — changing it
later will invalidate existing bundles.

Label: [________________________________]
       e.g. "team-alice-2026", "family-vault"

⚠️ Keep this private. Do not share with recipient.
   Lowercase letters, numbers, hyphens only.
```

Input: required, pattern `[a-z0-9_-]+`, max 50 chars.

### Step 2 — Sheet to Lock

```
🔒 Lock to Google Sheet

Passwords generated from this bundle will ONLY
work on this specific sheet. Copying the sheet
won't work — the sheet ID is embedded in the
password formula.

Sheet URL or ID:
[___________________________________________]
[📋 Use current sheet]   ← fills từ active tab

Sheet ID: 1BxCdefGhIjKlMnOpQrStUvWxYz  ✓

ℹ️ After sharing, you must set up account
   passwords using the derived secrets
   (the export wizard will remind you).
```

- Auto-extract sheetId từ pasted URL
- "Use current sheet" → message content_script lấy sheet ID
- Hiển thị extracted ID để confirm

### Step 3 — Sharing Password & Bundle

```
🔑 Set Sharing Password

This password protects the bundle.
The recipient needs it to import.

Sharing Password:  [____________________] 👁
Confirm Password:  [____________________]

Strength: ████████░░  Good

[← Back]                  [Generate Bundle]
```

Sau khi Generate:

```
✅ Bundle Ready

┌──────────────────────────────────────────┐
│ {"type":"mindvault-profile-share",        │
│  "version":"2.1",                         │
│  "sheetId":"1BxCdef...",                  │
│  ...                                      │
│ }                                         │
└──────────────────────────────────────────┘

[📋 Copy Bundle]   [⬇️ Download .json]

⚠️ Next step: Set up account passwords in your
   shared sheet using the DERIVED secrets.
   Use this extension with the shared sheet
   (while still Owner) to generate and update
   each account password.

⚠️ Transmit via encrypted channel.

[Done]
```

> "Next step" reminder quan trọng — Owner phải dùng DS-generated passwords khi set up accounts, không phải S-generated.

---

## 5. Import Wizard (Modal, 3 Bước)

Triggered bởi `[+ Import Profile]`.

### Step 1 — Paste hoặc Upload Bundle

```
📥 Import Shared Profile

Provide the bundle from the sender.

[📋 Paste JSON]   [⬆️ Upload .json]

┌────────────────────────────────────────┐
│ Paste bundle JSON here...              │
└────────────────────────────────────────┘

Bundle status: ✓ Valid v2.1 bundle
               Profile: TeamShare
               Sheet: 1BxCdef... (locked)
               Exported: 2026-05-17
```

Validate: JSON format + version. Hiển thị sheetId từ bundle.

### Step 2 — Sharing Password

```
🔑 Enter Sharing Password

Password given by the sender.

Sharing Password: [____________________] 👁

[← Back]                       [Decrypt →]
```

On decrypt fail: "Incorrect sharing password."  
On success: proceed to Step 3.

### Step 3 — Name & Confirm

```
✏️ Name This Profile

Local name (only visible to you):

Profile Name: [TeamFromBoss____________]

Locked to sheet: 1BxCdefGhIjKlMnOpQrS...
This profile will only work on that sheet.
No manual sheet assignment needed.

[← Back]                         [Import]
```

On Import: save → toast "Profile imported" → close modal.  
Extension tự route khi Consumer mở sheet đó.

---

## 6. Edit Secrets Modal (Profile-scoped)

Triggered bởi `[Edit Secrets]`.

```
┌─── Edit Secrets: Banking ──────────────── × ┐
│                                              │
│  Secret #1  [_______________] 👁             │
│  Secret #2  [_______________] 👁             │
│  Secret #3  [_______________] 👁             │
│  Secret #4  [_______________] 👁             │
│  Secret #5  [_______________] 👁             │
│                                              │
│  [Cancel]                    [Save Changes] │
└──────────────────────────────────────────────┘
```

---

## 7. New Profile Modal

Triggered bởi `[+ New Profile]`.

```
┌─── Create New Profile ─────────────────── × ┐
│                                              │
│  Profile Name: [__________________________]  │
│                e.g. "Banking", "TeamShare"   │
│                                              │
│  [Cancel]                          [Create] │
└──────────────────────────────────────────────┘
```

On Create → mở Edit Secrets modal ngay.

---

## 8. Popup Changes (v2)

### Generated state — profile indicator

```
┌──────────────────────────────────────────┐
│ 🔐 mindVault                         ⚙️  │
│ ─────────────────────────────────────── │
│  🟢 Ready                               │
│                                          │
│  Recipe:  fb#1                          │
│  Profile: Default               ← new  │
│                                          │
│  ┌───────────────────────┐  [Copy]      │
│  │ ••••••••••••          │              │
│  └───────────────────────┘              │
│  ✓ Copied! (Clears in 30s)              │
│                                          │
│  [Back]                       [Lock]    │
└──────────────────────────────────────────┘
```

Profile line: text-secondary, 0.85rem. Shared profile → prefix 📥.

### Unlocked state — profile summary

```
│  Profiles: 3 own · 1 shared        ← new │
```

Small summary, text-secondary. Click → opens options page Profiles tab.

---

## 9. Error States

### Generating from unassigned sheet

Silent fallback to default profile. Popup shows:  
`Profile: Default (fallback)` — text-secondary, no blocking.

### Generating from shared profile on wrong sheet

Không thể xảy ra bình thường vì extension chỉ trigger trên đúng sheetId. Nếu xảy ra (edge case):

```
⛔ Sheet not authorized

This profile is locked to a different sheet.
```

### Shared profile sheetId mismatch (DevTools tampering)

```
⛔ Sheet mismatch

Generated password may be incorrect.
Profile is locked to sheet: 1BxCdef...
```

---

## 10. Migration Notice (v1 → v2)

Toast one-time sau migration:

```
✅ Migrated to v2
   Your secrets are now in profile "Default".
   Create more profiles in Settings → Profiles.
```

Lưu `migrationNotified: true` để không hiện lại.

---

## 11. i18n Keys (New for v2)

| Key | English |
|---|---|
| `tabSecrets` | Secrets |
| `tabProfiles` | Profiles |
| `tabSettings` | Settings |
| `profilesOwnTitle` | My Profiles |
| `profilesSharedTitle` | Shared Profiles (Received) |
| `profilesAssignTitle` | Sheet Assignments |
| `btnNewProfile` | + New Profile |
| `btnImportProfile` | + Import Profile |
| `btnEditSecrets` | Edit Secrets |
| `btnExportProfile` | Export |
| `btnSetDefault` | Set as Default |
| `btnRemoveProfile` | Remove |
| `lblSheetLocked` | Sheet: {id} (locked) |
| `lblImportedFrom` | From |
| `lblUsedBySheets` | Used by: {n} sheet(s) |
| `exportWizardTitle` | Export Profile: "{name}" |
| `importWizardTitle` | Import Shared Profile |
| `errSheetMismatch` | Sheet not authorized |
| `errProfileAssigned` | Assigned to {n} sheet(s). Reassign before deleting. |
| `profileFallback` | Profile: {name} (fallback) |
| `migrationNotice` | Migrated to v2. Secrets now in profile "Default". |
| `exportNextStep` | Next: update account passwords using derived secrets. |

---

## 12. File Changes Summary

| File | Change type | Description |
|---|---|---|
| `options.html` | Modify | Tab bar, Profiles tab HTML, modal shells |
| `options.css` | Modify | Tab styles, profile card styles, modal overlay |
| `options.js` | Modify | Tab switching, profile CRUD, 3-step export/import wizard |
| `popup.html` | Modify | Profile name line in generated state |
| `popup.js` | Modify | Show profile name from service worker response |
| `_locales/vi/messages.json` | Modify | New i18n keys |

---

## Unresolved Questions

1. **Owner setup UX**: Wizard phải nhắc Owner rõ ràng rằng họ cần set up account passwords dùng DS-generated (không phải S-generated). Step 3 đã có warning — đủ chưa?
2. **"Use current sheet" button**: Yêu cầu `activeTab` permission + content_script messaging. Nếu không có GSheets tab active → disable với tooltip. Có worth it không hay bỏ?
3. **Import channel**: Paste + upload file (cả hai) hay chỉ paste?
4. **Multiple sheets per bundle**: Enforce 1 bundle per sheet hay chỉ document?
