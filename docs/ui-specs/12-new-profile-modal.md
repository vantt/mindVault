# 12 — New Profile Modal

Triggered bởi `[New Profile]` trong Profiles tab (`openNewProfileModal` trong `options-profiles-tab.js`).

## DOM IDs

| Element | ID / selector |
|---|---|
| Modal overlay | `#modal-new-profile` |
| Name input | `#new-profile-name` |
| Create button | `#btn-create-profile` |
| Cancel / × | `.modal-close[data-modal="modal-new-profile"]` |

## Layout

```
┌─── Create New Profile ─────────────────── × ┐
│                                              │
│  Profile Name                                │
│  [______________________________]            │
│  e.g. "Banking", "TeamShare"                 │
│                                              │
│  [Cancel]                          [Create] │
└──────────────────────────────────────────────┘
```

## Rules

### Input
- `maxlength="50"` (enforced bởi HTML attribute)
- Placeholder: `e.g. "Banking", "TeamShare"` (hardcoded EN — i18n gap)
- Label: `"Profile Name"` (hardcoded EN — i18n gap)
- **Validation**: `name.trim()` non-empty check; nếu rỗng → `return` silently (không có error toast)
- **Không có uniqueness check** — tạo profile trùng tên sẽ ghi đè key `profile:{name}` trong storage

### Create flow (`[Create]`)
1. Trim value; abort nếu rỗng
2. `createProfile(name, deps)`:
   - Tạo `profile:{name}` trong `chrome.storage.sync` với 5 secret slots rỗng + `settings: {}`
   - Toast: `"Profile \"${name}\" created"` (hardcoded EN — i18n gap)
   - Re-render toàn bộ profiles list
3. Close `#modal-new-profile`
4. **Ngay lập tức mở** `openEditSecretsModal(profileKey, name, deps)` — wired, không optional

### Color Dot
- Gán theo **render index** của profile trong danh sách: `PROFILE_COLORS[idx % PROFILE_COLORS.length]`
- Palette (5 màu theo thứ tự):
  `#58a6ff` · `#e3b341` · `#3fb950` · `#a371f7` · `#f78166`
- Màu KHÔNG được lưu vào storage; tái tính mỗi lần render → màu có thể thay đổi nếu profiles bị thêm/xóa

### Default Profile
- Profile mới KHÔNG được set làm default
- `defaultProfile` key trong storage không thay đổi khi tạo profile mới
- User phải explicitly click `[★ Default]` trên profile card

### Cancel / ×
- `closeModal('modal-new-profile')` — không tạo profile

## i18n Keys

| Key | EN | VI | Dùng ở |
|---|---|---|---|
| `btnNewProfile` | "New Profile" | "Tạo Hồ sơ" | Profiles tab trigger button |

### Hardcoded strings (i18n gaps)
- Modal title: `"Create New Profile"` (HTML)
- Label: `"Profile Name"` (HTML)
- Placeholder: `e.g. "Banking", "TeamShare"` (HTML)
- Button: `"Create"` (HTML)
- Button: `"Cancel"` (HTML)
- Toast: `"Profile \"${name}\" created"` (JS string)
