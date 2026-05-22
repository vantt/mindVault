# 14 — Migration Notice (v1 → v2)

Toast one-time hiển thị sau khi migration từ v1 hoàn tất và user unlock lần đầu.

---

## Implementation status: FULLY IMPLEMENTED

---

## Trigger flow

1. **SW startup** (`service_worker.js` → `runMigrationIfNeeded()`): gọi `storageAdapter.migrateV1ToV2()`.
   - `migrateV1ToV2()` kiểm tra: nếu storage có `encryptedData`/`iv` nhưng chưa có key `profile:*` → copy vào `"profile:Default"`, set `defaultProfile`, `sheetMapping: {}`.
   - Nếu migration xảy ra → SW set `migrationNotified: false` vào `chrome.storage.sync`.
2. **Options page** (`options.js` → `checkStatus()`): đọc `migrationNotified` cùng lúc với `salt`.
   - Điều kiện hiển thị: `stored.migrationNotified === false` **và** `session.sessionKey` tồn tại (user đã unlock).
   - Gọi `showToast(...)` → set `migrationNotified: true` vào storage (không hiện lại).

---

## Toast content

```
✅ Migrated to v2. Secrets are now in profile "Default".
```

**Note:** String này **hardcoded** trong `options.js`. i18n key `migrationNotice` tồn tại trong `messages.json` nhưng **không được dùng** — toast gọi `showToast()` trực tiếp với literal string.

---

## Toast behavior (actual)

| Property | Spec trước | Thực tế code |
|---|---|---|
| Auto-dismiss | 6–8s | **3000ms (3s)** — `setTimeout(..., 3000)` trong `showToast()` |
| Dismissable by click | Có | **Không** — `showToast()` không wire click handler |
| Position | top-center hoặc bottom-center | Vị trí `#toast` element trong `options.html` (không xác định ở đây) |
| Page | Options page | Options page ✓ |
| Flag sau khi hiển thị | `migrationNotified: true` | `migrationNotified: true` ✓ |
| Storage area | sync | `chrome.storage.sync` ✓ |

---

## i18n Gap

- `migrationNotice` key (`"Migrated to v2. Secrets are now in profile \"Default\"."`) **tồn tại** nhưng chưa được sử dụng.
- Toast string hiện tại: `"✅ Migrated to v2. Secrets are now in profile \"Default\"."` (hardcoded, có ✅ prefix).

---

## i18n Keys

| Key | Actual message |
|---|---|
| `migrationNotice` | `"Migrated to v2. Secrets are now in profile \"Default\"."` *(unused — toast is hardcoded)* |
