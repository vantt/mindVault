# Mobile App — Brainstorm (Q&A)

> Tài liệu ghi lại quá trình brainstorm ý tưởng app mobile cho PassChef, dạng Hỏi–Đáp.
> Cập nhật liên tục theo mỗi lượt trao đổi. Không phải quyết định cuối cùng — là suy nghĩ/lựa chọn kèm lý do.

**Bắt đầu:** 2026-07-19

---

## Bối cảnh nền (grounding — xác nhận từ codebase)

Ba sự thật quyết định toàn bộ thiết kế mobile:

1. **App KHÔNG lưu password.** Nó lưu "recipe" (công thức) trong Google Sheets, rồi "nấu" tại chỗ từ secret (local) + master password. Password sinh on-demand, không bao giờ lưu.
2. **Extension đọc recipe bằng scrape DOM** trang `docs.google.com/spreadsheets` (formula bar / active cell). Mô hình này **không thể bê nguyên sang mobile** — không có browser extension, app Sheets mobile không cho inject DOM.
3. **Core "nấu" là portable.** DDD/Clean Architecture: `core/usecases/generate_password.js` chỉ phụ thuộc ports (IParser, IStorageRepository); Argon2 là WASM. Logic tái dùng được.

**Grammar recipe (quan trọng cho detection):**
```
/^([a-zA-Z0-9]+)([#@$%^])(\d)([_!?~]*)(?:\.([a-z2-9]{4}))?$/
```
- position ∈ `# @ $ % ^` — cooking style
- spice index `1–5`
- modifiers `_ ! ? ~`
- tag tùy chọn: 4 ký tự base32 (verify tag, HMAC theo secret + sheetId)

---

## Q1 — Có cách nào làm app mobile cho sản phẩm này không? Suy nghĩ tất cả vấn đề UI/UX trên phone và cách giải.

**Insight cốt lõi:** App mobile KHÔNG phải là "port của extension". Trên desktop user đã nhìn vào sheet → click cell (tương tác miễn phí vì extension ký sinh trên trang). Trên mobile chuỗi đó đứt: không extension, và nơi thật sự cần password là **màn login của app/website khác**, không phải sheet. → Đây là sản phẩm khác về tương tác, chỉ chung lõi crypto.

**Các vấn đề UX và hướng giải:**

- **#0 — Lấy recipe từ đâu?** (quyết định mọi thứ)
  - A. **App-as-Reader** (Sheets API + OAuth): app tự đọc sheet → danh sách vault tìm-kiếm-được. → *xương sống*.
  - B. Paste/Share thủ công → *fallback*.
  - C. Clipboard/QR → chỉ dùng lẻ.
  - **Chốt: A là xương sống, B là fallback.** Recipe KHÔNG bí mật → được cache plaintext local → vault offline, mở tức thì.

- **#1 — Dùng password thực tế = Autofill (KHÓ NHẤT).** iOS Credential Provider Extension / Android Autofill Service + Credential Manager. Nút thắt: recipe đặt tên theo *service tự do* (`fb`, `gmail`), không phải domain → cần map domain→recipe (thêm cột domain, hoặc mapping local). Autofill phải nấu on-the-fly → cần unlock biometric ngay trong luồng. Phần native, tốn công & rủi ro nhất.

- **#2 — Nhập master password = ma sát.** Giải: biometric (FaceID/vân tay) bọc master-key; nhập master 1 lần lúc setup → key trong Secure Enclave/Keystore. Giữ auto-lock như desktop.

- **#3 — Secret lưu ở đâu (không có Chrome Sync)?** Ưu tiên: blob mã hóa trong **Google Drive appDataFolder** (cùng account đã dùng cho Sheets, giữ zero-knowledge) + cache local mở bằng biometric. Thay thế: chỉ local (nhập lại mỗi máy) / iCloud Keychain.

- **#4 — Onboarding/handoff.** Gõ lại 5 secret trên phone dễ sai (sai secret → password sai âm thầm). Giải: **QR handoff** từ extension (export secret mã hóa → quét). Tận dụng **verify tag** để phát hiện setup sai và từ chối tạo thay vì ra password sai.

- **#5 — Vault UX.** Search tức thì, favorites, recent; **offline-first** (cache recipe list local vì không bí mật); chọn sheet/tab/profile.

- **#6 — Tạo/sửa recipe trên mobile.** Recipe Builder dạng form (giống popup builder desktop) sinh recipe + verify tag; ghi lại sheet qua Sheets API (write scope) hoặc chỉ copy để dán tay.

- **#7 — Clipboard & privacy.** Ưu tiên autofill hơn clipboard; auto-clear 30s; blur app-switcher preview; FLAG_SECURE chặn screenshot; lock khi background.

- **#8 — Tech stack.** Autofill bắt buộc native dù chọn gì. **Khuyến nghị React Native** vì tái dùng được lõi JS Clean Architecture đã có; chỉ viết native module cho autofill/biometric/secure-storage/Argon2/QR. (Flutter phải viết lại lõi bằng Dart.)

**Lộ trình đề xuất:**
- Phase 1 — Vault đọc (MVP): OAuth + Sheets API + cache offline + unlock biometric + nấu local + tap-to-copy. (B nhét kèm gần như free.)
- Phase 2 — Autofill + domain-mapping. (Lý do thật để cài app.)
- Phase 3 — Handoff QR + secret blob trên Drive + multi-profile.
- Phase 4 — Authoring recipe + ghi sheet + quản lý mapping.

**Câu hỏi mở (chưa chốt):** ưu tiên "tra cứu/copy" hay "autofill"? cross-platform hay 1 nền trước? có sẵn lòng thêm cột `domain`? secret sync qua Drive có OK không? app có quyền ghi sheet không?

---

## Q2 — Nói rõ hơn "A là xương sống, B là fallback".

Không phải chọn một trong hai — mà là **phân vai**: A lo ~95% việc hằng ngày, B vá khe A không với tới.

**A — App-as-Reader (xương sống):** OAuth → đọc sheet qua Sheets API → danh sách vault search-được → cache local. Là xương sống vì:
- Dùng lặp lại hằng ngày cần "duyệt & chọn", không ai paste tay mỗi lần.
- Recipe không bí mật → cache plaintext → vault offline, mở tức thì.
- **Là nền bắt buộc cho autofill** (cần tập recipe truy vấn được để match domain→recipe).

**B — Paste/Share (fallback):** nấu 1 chuỗi recipe rời từ bất kỳ đâu (chat, sheet chưa kết nối). Vẫn cần vì: recipe ngoài sheet đã add; user không muốn cấp OAuth (dùng thử ngay); độ bền khi API lỗi/mất mạng chưa cache; onboarding nấu thử 5 giây.

**Điểm mấu chốt:** cả A và B đổ vào **cùng `GeneratePassword` usecase** — chỉ khác lớp "lấy input", không nhân đôi logic bảo mật.

**Vì sao không đảo vai:** lấy B làm chính (như extension đọc từng ô) sẽ tệ trên mobile — mỗi lần phải mở Sheets, cuộn tìm ô, copy, đổi app, dán. B chỉ hợp việc lẻ, không scale cho 100+ account.

---

## Q3 — Thử thách: detect đúng cột trên sheet mà KHÔNG ép user theo tiêu chuẩn. Giải UX thật tốt.

**Nguyên lý:** cái "chuẩn" cần detect nằm trong **chính chuỗi recipe** (grammar tự nhận diện), không nằm ở header/vị trí cột. → Detect theo **nội dung**, không theo header. *Ta phát hiện cái chuẩn, không áp đặt nó lên sheet của user.*

Chạy chính `RegexParserAdapter` lên từng ô: cột có **tỉ lệ ô parse-hợp-lệ cao nhất** = cột recipe. Rất đáng tin vì đuôi `<style><digit>` (`#1`, `@2`, `$3`) gần như không xuất hiện trong text thường; có tag `.a4b2` thì nhầm lẫn ~0. Chỉ 5–10 dòng mẫu là đủ.

**Máy dò 4 lớp:**
1. **Cột Recipe (dựa parser):** `score = ô_hợp_lệ / ô_không_rỗng`; chọn max, ≥ ~0.6 và ≥ 3 recipe. Có tag → tin cao nhất.
2. **Cột Nhãn (title):** ingredient là hash ngẫu nhiên nên vô dụng làm tên → phải tìm cột "tên account": text ngắn, unique cao, không phải recipe, thường sát trái cột recipe.
3. **Cột làm giàu:** email/username, URL/domain (nuôi autofill Phase 2), notes.
4. **Từ khóa header (chỉ cộng điểm, không bắt buộc):** từ điển EN/VI (`recipe/công thức`, `service/tài khoản`, `user/email`, `url`, `note/ghi chú`) — chỉ tie-break khi lớp 1–3 lưỡng lự.

**UX thích ứng theo confidence (giảm ma sát khi chắc):**
- **Tin cao:** nhảy thẳng màn **Preview** 3–5 dòng vault → **1 chạm "Đúng rồi"**.
- **Tin thấp/mơ hồ:** mở **bộ chọn cột trực quan** (chạm đầu cột → gán vai *Recipe/Nhãn/Username/URL/Notes/Bỏ qua*), preview live — không dùng form dropdown.

**Xử lý ca thực tế:**
- Nhiều tab: dò từng tab, bật/tắt, có thể map tab → profile.
- Không header / data không từ dòng 1: dò theo nội dung tự bỏ qua rác; "dòng là account" ⇔ ô cột-recipe parse hợp lệ.
- Nhớ mapping theo `spreadsheetId + tab` (cache local).
- Drift (đổi cột): refresh thấy cột recipe hết parse tốt → nhắc kiểm tra lại, không phá dữ liệu.
- Dò chạy **local** sau fetch, **dùng lại đúng parser của extension** → thêm lý do chọn React Native.

**Giới hạn thành thật:** recipe trộn chung ô với text khác → ngoài phạm vi (YAGNI, có thể thêm regex-extract nâng cao sau); sheet chỉ có cột recipe không có nhãn → phải hỏi user 1 câu.

---

## Q4 — Đi sâu nhánh Autofill + domain-mapping.

**Bài toán lõi = 3 lệch pha phải khớp cùng lúc:**
1. **Lệch khóa:** entry khóa theo *nhãn tự do* (`fb`), autofill khóa theo **domain** (iOS/browser) + **package name** (Android app) → cần lớp map `entry ↔ {domains[], packages[]}`.
2. **Lệch unlock:** lúc autofill vault thường khóa; nhưng "có account nào" là dữ liệu **không bí mật** → tách: hiện danh sách khi khóa, chỉ *nấu* mới cần unlock.
3. **Lệch ngữ cảnh sheet:** verify tag cần `sheetId` mà lúc autofill không ở trong sheet.

**Cơ chế 2 nền (bất đối xứng quan trọng):**
- **iOS — store-based:** populate `ASCredentialIdentityStore` identity `(domain, username)` KHÔNG kèm password (chỉ dữ liệu không bí mật). OS lo map app-native→domain qua Associated Domains mà app đích khai (`webcredentials:`). Khóa → `provideCredentialWithoutUserInteraction` ném `.userInteractionRequired` → `prepareInterfaceToProvideCredential` (biometric) → nấu.
- **Android — request-based:** `onFillRequest` đọc `AssistStructure`, lấy package name / `getWebDomain()`. `Dataset.setAuthentication()` → activity biometric → nấu. App native chỉ có **package name** (không domain trừ khi có `assetlinks.json`).
- → **iOS domain-centric (OS lo mapping), Android package-centric (ta tự lo)**; Android app-native là mắt xích yếu nhất.

**Nguồn mapping — ZERO-CONFIG, xếp lớp, tự học (không ép user điền domain):**
1. **Learned mapping** (học theo lần dùng) — cao nhất, killer UX.
2. **Cột domain trong sheet** (nếu có, auto-detect) — portable, sync desktop.
3. **Catalog alias dựng sẵn** (~200 service: nhãn ↔ domain ↔ package).
4. **Fuzzy từ nhãn** (`github`→`github.com`).
5. **Learn-by-use fallback:** không match → "Tìm trong PassChef" → user chọn → ghi nhớ (về #1).

**Pipeline match:** learned → cột domain → catalog → fuzzy → (không tự tin) item "Tìm" → LEARN. Nhiều kết quả: top-N; không tự điền sai âm thầm.

**Tách index không-bí-mật khỏi nấu-cần-unlock:** index `{entryId,label,username,domains[],packages[],recipeText,spreadsheetId,profile}` sẵn cả khi khóa để match+hiển thị; nấu chỉ sau biometric (thêm 1 chạm).

**Hai "sát thủ ngầm" phải thiết kế trước:**
- **(a) Argon2id vs RAM extension (iOS):** extension bị giới hạn RAM rất ngặt (~120MB, *cần verify*), Argon2id memory-hard → dễ OOM-kill + chậm. **Giải:** dẫn xuất master-key 1 lần lúc unlock → cache **derived key** trong Secure Enclave/Keychain (access group). Autofill chỉ chạy phần rẻ (hash/HMAC/AES) → nhanh, không đụng trần RAM. Quyết định kiến trúc bắt buộc.
- **(b) Verify tag cần sheetId:** cache mỗi recipe kèm `spreadsheetId` (không bí mật) → nấu lúc autofill dùng sheetId đã lưu → tag verify OK.

**Username:** có cột user/email → điền cả cặp; không có → chỉ điền password (vẫn giá trị lớn).

**Bảo mật autofill:** chỉ khớp danh tính do OS xác thực (packageName/domain), không theo text user gõ; learned-mapping khóa theo packageName (OS-verified) an toàn; không lộ secret/derived-key cho app đích, chỉ trả chuỗi password cuối; auto-lock áp mỗi phiên.

**Hệ quả kiến trúc:** lõi nấu phải gọi được từ native extension (cân nhắc viết lại phần "nấu rẻ" bằng native cho extension, Argon2 nặng để app chính — làm giảm chút lợi thế chung-lõi-JS của RN); iOS App Group + Keychain access group; giữ `ASCredentialIdentityStore` tươi sau mỗi refresh/learn; optional Phase 4 ghi learned-mapping ngược vào cột domain sheet.

**Rủi ro nhất:** (1) Android app-native matching phụ thuộc catalog+learn; (2) Argon2 trong extension iOS nếu không tách derived-key sẽ vỡ; (3) vòng đời derived-key giữa app↔extension vs auto-lock.
→ **Khuyến nghị: spike Phase 2a** = prototype autofill tối giản 1 nền, đo RAM/latency khi nấu, verify luồng lock→biometric→cook, trước khi cam kết full.

---

## Q5 — Chốt lại tech stack dưới ràng buộc autofill.

**Câu hỏi quyết định (duy nhất):** lõi "nấu" + crypto phải chạy ở đâu? → Ở **app chính** *và* **trong autofill extension native, RAM ngặt (~120MB), không chỗ cho VM JS/Dart**. Điều này **lật ngược** khuyến nghị RN ban đầu.

**3 sự thật làm tan lợi thế RN:**
1. **Cook rất nhỏ** — parse regex + lấy secret + HMAC tag + string transform + AES. KHÔNG có Argon2 trong đường nấu (Argon2 chỉ lúc unlock → cache KEK). Viết lại native không tốn, nhưng "nhỏ" ≠ "được phép 2 bản".
2. **Crypto đằng nào cũng native** — RN không có WebCrypto/Argon2 tử tế (WASM tệ), buộc dùng native crypto module. Phần nhạy cảm nhất không ở lại JS được.
3. **Extension không host VM JS/Dart rẻ** — boot Hermes/Flutter engine trong extension 120MB = chậm + tốn RAM sai chỗ. → Cook trong extension buộc native → RN/Flutter kết cục cook ở **2 nơi** = **lệch source-of-truth** ngay tại code bảo mật nhất (README: golden tests là source of truth).

**Chấm điểm (★ = quan trọng cho autofill):**
| Tiêu chí | Native | KMP | RN | Flutter |
|---|---|---|---|---|
| Cook single-source chạy trong extension ★★★ | dup (nhỏ) | **1 bản native ✓** | dup JS+native | dup Dart+native |
| Crypto kiểm toán ★★★ | ✓✓ | ✓✓ | native mod | native mod |
| Autofill UX/RAM/latency ★★★ | ✓✓ | ✓✓ | TB | yếu |
| Tái dùng lõi JS | ✗ | ref+port test | ✓ app/✗ ext | ✗ |
| Tốc độ Vault MVP | chậm | TB | **nhanh** | nhanh |
| Hợp team (vanilla JS) | thấp | thấp (cần Kotlin) | **cao** | TB |

**CHỐT:**
- **Nếu autofill là mục tiêu cốt lõi đã cam kết → KMP** (shared core native + UI SwiftUI/Compose, hoặc Compose Multiplatform). Lõi Kotlin compile thành framework native, gọi được từ cả app + 2 extension **không cần VM** → cook/crypto một nguồn native chạy được trong extension. Á quân: **fully-native** (an toàn nhất, nhưng cook viết 2 lần + x2 UI).
- **Nếu autofill hoãn / cần MVP nhanh với kỹ năng JS → React Native Phase 1**, chấp nhận **nợ Phase 2**: sẽ phải thêm native cook module (bridge cho RN + dùng lại trong extension) — tức viết lại cook native ở Phase 2 dù muốn hay không.
- **Không dùng Flutter** ở dự án này (gộp nhược điểm: rewrite Dart + vẫn native ở extension).

**Kiến trúc KMP:** `shared/` (domain parser/tag-codec, usecase cook/mapping/column-detection, ports, crypto expect/actual: Argon2/AES/HMAC) ← link bởi `iosApp` (SwiftUI + Credential Provider Extension) và `androidApp` (Compose + AutofillService). Secure store: Keychain access group / Keystore, cache derived-key.

**Tái dùng lõi JS:** làm **spec tham chiếu** + **port toàn bộ golden tests `@approved`** sang shared → đảm bảo password khớp 100% extension (desktop↔mobile khác password = thảm họa).

**Rủi ro/phải xác thực:** kỹ năng Kotlin của team (yếu tố quyết định chính KMP vs RN); Compose MP trên iOS còn non (nhưng app surface nhỏ, rủi ro thấp; có thể UI native riêng); dù gì cũng **spike Phase 2a** trước khi khóa.

**Một dòng:** ràng buộc autofill biến "chung lõi JS" thành ảo ảnh → app muốn lõi native single-source → **KMP nếu cam kết autofill; RN nếu chấp nhận nợ Phase 2 để ra MVP nhanh**.

---

## Q6 — Đi sâu UI Vault Phase 1.

**Phạm vi Phase 1 — IN:** OAuth read-only → đọc sheet → cache offline → unlock biometric → nấu local → reveal/copy (auto-clear) + **Cook fallback** (paste) + column-mapping confirm (Q3). **OUT:** autofill, ghi sheet, QR handoff, Drive-sync, recipe builder (Phase 2–4). Phase 1 secret **nhập tay** lúc onboarding.

**Bản đồ màn hình:** Onboarding (Welcome → Connect Google → Pick sheet/tab → Confirm mapping → Setup secrets+master → Enable biometric) → Vault (Search + list, tap→bottom-sheet, Cook FAB, Settings); Unlock chắn mỗi lần mở/auto-lock.

**1. Unlock:** Face ID/vân tay tự bật khi mở; fail → master password. Unlock 1 lần → cache derived-key cả phiên → nấu sau đó tức thì (đến auto-lock).

**2. Vault list (ngôi sao):**
- Offline-first: list hiện tức thì từ cache (recipe không bí mật); chip "đang đồng bộ/ngoại tuyến" nhưng vẫn nấu local.
- Search = tương tác chính, lọc theo label+username (không search hash). Trống → Recent + Favorites.
- Icon favicon theo domain (dùng mapping-engine Q4); badge `✓verify` nếu recipe có tag; phụ đề = username nếu detect được.
- Cook FAB ở thumb-zone; pull-to-refresh = sync sheet.

**3. Bottom-sheet khi tap entry:** nấu on-demand (đã unlock → tức thì, dùng `spreadsheetId` cache verify tag). Reveal mặc định ẩn (dots), tap hiện, tự ẩn ~10s, nhóm ký tự tô màu dễ đọc. Copy → toast "tự xóa sau 30s" + auto-clear. Copy username riêng.

**4. Cook fallback:** chung `GeneratePassword` usecase; nhận Share Sheet từ app khác; recipe có tag → chọn sheet ngữ cảnh; không tag → nấu thẳng.

**5. Onboarding 5 bước:** Welcome → Connect Google (OAuth read-only) + pick sheet/tab → Confirm mapping (tin cao 1 chạm / mơ hồ bộ chọn cột) → Setup ≤5 secrets + master (cảnh báo mất master = mất secret; verify-tag bắt lỗi nhập sai) → Enable biometric.

**6. States/lỗi:** loading=skeleton; offline=chip + vẫn nấu; empty=hướng dẫn kiểm tra mapping; no-result=gợi ý Cook; **verify-tag mismatch = thông điệp thân thiện "công thức thuộc profile/sheet khác, không nấu an toàn ở đây" (KHÔNG lỗi thô)**; token hết hạn=banner "Kết nối lại Google" không chặn cache.

**7. Bảo mật UX:** auto-lock (5' idle / 10' total, background→lock); app-switcher blur + FLAG_SECURE + che screen-record; reveal ẩn+tự ẩn chống shoulder-surf; clipboard auto-clear 30s + toast; nấu trong phiên unlock không hỏi lại (tùy chọn "hỏi mỗi reveal" trong Settings).

**8. Micro-interactions:** pull-to-refresh sync; swipe row (trái=copy pass, phải=favorite); long-press=menu; search+FAB thumb-zone.

**Next đề xuất:** dựng prototype HTML clickable (unlock→list→bottom-sheet→cook) để bấm thử.

**→ ĐÃ DỰNG prototype clickable** (2026-07-20): file `docs/designs/passchef-vault-prototype.html`, artifact https://claude.ai/code/artifact/bfea75ef-e9dd-4950-bf25-6f13c69ed4d8. Demo đủ: unlock sinh trắc, search+chip profile, bottom-sheet reveal (tô màu ký tự + tự-ẩn 10s) + copy (toast auto-clear 30s), verify-tag mismatch thân thiện (entry "Ví Crypto"), Cook fallback (validate bằng đúng regex extension), light/dark. *Chỉ mockup UX — password giả, không chạy crypto thật.*

---

## Câu hỏi mở tổng hợp (cần user quyết)

1. Ưu tiên mobile: "tra cứu/copy" (Phase 1 đủ) hay "autofill tự động" (Phase 2, tốn gấp nhiều lần)?
2. Cross-platform (React Native) hay làm 1 nền trước (iOS/Android)?
3. Có sẵn lòng thêm cột `domain` vào sheet để autofill match chuẩn không?
4. Secret sync qua Google Drive (mã hóa) có chấp nhận được về niềm tin/marketing không, hay bắt buộc "local-only"?
5. App mobile có quyền **ghi** sheet không, hay chỉ read-only?
6. (Autofill) Chấp nhận kiến trúc **cache derived-key** trong Secure Enclave/Keystore để né giới hạn RAM extension iOS không? (ảnh hưởng vòng đời khóa & auto-lock)
7. (Autofill) Có muốn **catalog alias** dựng sẵn (~200 service) không, hay chỉ dựa learn-by-use + fuzzy? (catalog cần bảo trì)
8. (Autofill) Ưu tiên nền nào cho **spike Phase 2a** trước — iOS (khó vì RAM) hay Android (khó vì package-only)?
9. (Stack) **Team có sẵn sàng học/dùng Kotlin không?** → yếu tố quyết định chính giữa **KMP** (nếu cam kết autofill) và **React Native** (nếu ưu tiên MVP nhanh, chấp nhận nợ Phase 2).
