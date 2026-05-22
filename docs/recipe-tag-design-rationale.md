# Recipe Verification Tag — Design Decision

**Date:** 2026-05-22
**Status:** Accepted — Option B
**Related plan:** [`plans/260522-1314-recipe-verification-tag-sheet-binding/`](../plans/260522-1314-recipe-verification-tag-sheet-binding/plan.md)
**Relates to:** [PRD v2.1 §2.4](./prd/v2-multi-sheet-profiles.md) Sheet-Bound Generation

---

## 1. Problem

Recipe text `fb#1` không mang thông tin profile / sheet identity. Khi user build recipe trong popup builder với Profile A, paste vào Google Sheet bound (qua `sheetMapping`) tới Profile B, hệ thống **silent generate password sai** thay vì fail.

**Yêu cầu:** Mismatch phải fail tường minh, không silent.

---

## 2. Hard Constraint — "Cookbook" Insight

README mô tả selling point cốt lõi:

> When you need to log in, you become the Chef. You look at the menu (`r4nd0m#1`), take the ingredients (`r4nd0m`), and add your secret spice (`Basic*`) according to the instructions (`#` = put spice on top).
>
> **Result**: A perfect password `Basic*r4nd0m` that never existed until you cooked it.

→ **User phải compute password được bằng tay (mental math)** kể cả khi mất extension. Đây là **non-negotiable** — bất kỳ thay đổi nào làm mất khả năng này đều vi phạm value proposition gốc.

---

## 3. Options Considered

### Option A — Layer 1 + Layer 2 (HKDF sheet binding + verification tag)

Mở rộng PRD §2.4 Sheet-Bound Generation sang own profiles. Cả secret derivation lẫn verification đều dùng sheetId.

```
Tagged recipe: fb#1.a4b2
Secret effective: HKDF(S_i, sheetId)
Password: HKDF(S_i, sheetId) + hash
Verify: HMAC(S_i, "fb#1|sheetId").slice(0,4) === "a4b2"
```

**Pros:**
- Defense-in-depth: kể cả attacker strip tag, password vẫn khác trên sheet sai
- Nhất quán với PRD pattern cho shared profiles

**Cons:**
- ❌ **Vi phạm Cookbook insight** — password = HKDF, không tính nhẩm được
- Tagged recipes mất khả năng rebuild bằng tay

### Option B — Chỉ Layer 2 (verification tag, no HKDF cho own profiles) ✅ CHOSEN

Tag là pure verification metadata. Password value KHÔNG đổi so với v1 generation.

```
Tagged recipe: fb#1.a4b2
Secret effective: S_i  (unchanged)
Password: S_i + hash  (unchanged — manual rebuildable)
Verify: HMAC(S_i, "fb#1|sheetId").slice(0,4) === "a4b2"
  → mismatch throws RecipeProfileMismatchError
```

**Pros:**
- ✅ **Cookbook insight giữ nguyên** — user vẫn cook `Basic*r4nd0m` bằng tay, ignore `.a4b2` suffix
- Explicit fail trên mismatch (đáp ứng requirement)
- Backward compat trivial: untagged recipes generate y hệt cũ
- Tag không leak secret (HMAC preimage-resistant; 20 bits truncated → không brute-force được)

**Cons:**
- Không có defense-in-depth crypto layer — nếu attacker strip tag thì password sinh ra như bình thường (nhưng vẫn cần đúng secret + đúng sheet routing)
- Layer 1 cho shared profiles (PRD §2.4) vẫn giữ — own vs shared có path generation khác nhau

### Option C — Per-recipe mode (user chọn binding cho từng recipe)

Hai loại tag: pure-verify vs HKDF-bound. UI cho user chọn.

**Pros:** Flexibility tối đa
**Cons:** YAGNI — phức tạp UX, 2 paths song song, user khó hiểu

---

## 4. Decision: Option B

**Rationale:**

1. **Cookbook insight là core value prop** — README mở đầu bằng nó, mọi quyết định khác phải subordinate. Option A đánh đổi cái này lấy defense-in-depth không cần thiết cho own profiles.

2. **Threat model khác giữa own vs shared:**
   - **Shared (PRD §2.4 keep HKDF):** Consumer copy sheet → cần silent revocation. Threat: bên thứ 3 cố ý bypass.
   - **Own (Option B no HKDF):** User sở hữu cả sheets. Mismatch là do nhầm lẫn (paste sai sheet, đổi sheetMapping), không phải tấn công. Explicit error message đủ — không cần crypto layer bảo vệ user khỏi chính họ.

3. **Layer 2 đủ cho requirement:** User yêu cầu "fail tường minh khi mismatch." HMAC verify + explicit error throw đáp ứng. Không cần layer thứ 2 để fail.

4. **KISS:** Một path generation cho own profiles (S_i + hash) thay vì hai (tagged vs untagged khác nhau crypto). Test surface nhỏ hơn, ít edge case.

---

## 5. Trade-offs Accepted

| Trade-off | Lý do chấp nhận |
|-----------|-----------------|
| Không có crypto defense-in-depth cho own profiles | Threat model không yêu cầu — user là principal duy nhất |
| Tagged và untagged recipes ra cùng password value | Đây thực ra là feature: user có thể rotate untagged → tagged mà không phải đổi password trên các sites |
| Tag không bảo vệ chống "tag stripping" attack | Attacker phải đã có access vào sheet + biết tag là gì — threat model giả định attacker không có quyền sheet |

---

## 6. What Changes vs PRD v2.1

| Aspect | PRD v2.1 | Option B (v2.2) |
|--------|----------|-----------------|
| Own profile generation | `S_i + hash` | `S_i + hash` (unchanged) |
| Shared profile generation | `HKDF(DS_i, sheetId) + hash` | Same (PRD §2.4 unchanged) |
| Recipe grammar | `<hash><pos><idx>[mods]` | `<hash><pos><idx>[mods][.<tag4>]` (additive) |
| Mismatch behavior | Silent wrong password | **Tagged: throw error.** Untagged: silent (legacy) |
| Manual rebuildability (own) | ✅ Yes | ✅ Yes (preserved) |
| Manual rebuildability (shared) | ❌ No (PRD design) | ❌ No (unchanged) |

---

## 7. Recipe Format Cheat Sheet

```
Untagged (legacy, backward compat):
  fb#1           ← manual rebuild: Basic* + fb  →  Basic*fb

Tagged (new, with verification):
  fb#1.a4b2      ← manual rebuild: Basic* + fb  →  Basic*fb
                   (suffix `.a4b2` ignored by human cook;
                    extension verifies and fails on mismatch)
```

→ **Human-cooked password identical with or without tag.** Tag chỉ là safety net cho extension users.

---

## 8. Implementation

Xem chi tiết tại [`plans/260522-1314-recipe-verification-tag-sheet-binding/`](../plans/260522-1314-recipe-verification-tag-sheet-binding/plan.md).

**Phải update plan:** Phase 03 (generation usecase) và Phase 04 (storage adapter) trong plan hiện tại đang dựa trên Option A — cần điều chỉnh xoá phần HKDF sheet binding cho own profiles, giữ secret raw cho generation, chỉ áp dụng tag verify.

---

## 9. Unresolved Questions

1. Có cần warn UI khi user paste recipe untagged vào sheet không? (Hiện plan: subtle badge, không block.)
2. Future: nên có command "upgrade legacy recipes" để bulk-add tag không? (YAGNI cho v2.2.)
