# 05 - Plain-language terminology + first-run explainer

Author: UX writer. Scope: copy only. Supersedes glossary in `ux-flow-redesign-spec.md` L10-12 and OB1-OB3 copy (L206-227) once owner approves.

## 0. Position (strategy)

Owner is right. "Thực đơn" as the first word a new user sees asks them to decode a metaphor before they understand the product. The cooking metaphor is a good *explanation tool* but a bad *interface vocabulary*.

**Recommendation: plain words in every functional UI string. Cooking only as brand flavor.**

- Functional UI (tabs, buttons, labels, errors, snackbars, settings): plain words only. Zero cooking terms.
- Brand flavor (allowed): chef-keyhole logo, tagline, illustrations, store listing, landing page, README. Example tagline: "Don't keep the password. Keep the recipe." / "Đừng giữ mật khẩu. Giữ công thức."
- Explanation metaphor (per report 00): **"two-half password" / "mật khẩu hai nửa"**, pictured as a bank box with two keys (one on paper, one in your head). No cooking words in the explainer either.
- Core line (from report 00): EN "Write your passwords down. A stranger who reads them still can't log in." / VI "Ghi mật khẩu ra giấy mà người lạ đọc cũng vô dụng." Use on store listing, E1, empty state.
- Biggest differentiator to say loudly: it works WITHOUT the app (sheet + memory, combine by hand). App = convenience, not the only safe.
- Why not keep the metaphor in UI: (1) users 50+ and non-English-native read "Thực đơn/Gia vị/Món" literally (restaurant menu? food?) and hunt for food meaning; (2) each metaphor word adds a mapping step (Menu = sheet, Spice = secret) on top of a concept that is already new; (3) support and family hand-over ("tìm trong Sổ bếp") get harder, not easier; (4) crypto-adjacent apps need trust, and a wrapped-in-jokes UI reads less serious on the unlock screen.
- Counter-argument (honest): metaphor gives memorability and brand. That is kept via logo/tagline/illustration, where a miss costs nothing.
- Action for AGENTS.md "Cooking Protocol": narrow its scope to docs, marketing, internal naming. Add "End-user UI uses plain terms from report 05." (owner decision, not done here.)

## 1. Terminology table

Plain-word anchor used everywhere: **"written code"** = the half that is written in the sheet/on paper; **"secret part"** = the half only you know.

| Concept | Current EN / VI | Plain alternatives EN / VI | **Recommended EN / VI** | Why |
|---|---|---|---|---|
| Google Sheet source (Menu) | Menu / Thực đơn | Your sheet, Password list, Google Sheet / Bảng tính, Danh sách mật khẩu, Google Sheet | **Your sheet / Bảng tính** (full: "Google Sheet" / "Bảng tính Google") | Users already own and name this thing. "Bảng tính" is everyday Vietnamese for spreadsheet; "Trang tính" (Google's official VI) sounds odd to most adults. |
| Recipe string (`r4nd0m#1`) | Recipe / Công thức | Written code, Saved code, Code / Mã đã ghi, Mã lưu, Mã ghi chú | **Written code / Mã đã ghi** | Says where it lives (written down, sheet or paper) and that it is not the password. "Mã" is familiar (mã PIN, mã OTP). |
| Random part (Ingredients) | Ingredients / Nguyên liệu | Random part, Base code, Scrambled part / Phần ngẫu nhiên, Phần mã gốc, Phần xáo trộn | **Random part / Phần ngẫu nhiên** | Literal and short. "Base/gốc" implies it is the password; it is not. |
| Secret part (Spice, spice jar) | Spice, jar / Gia vị, hũ | Secret part, Secret phrase, Key phrase / Phần bí mật, Cụm bí mật, Cụm khóa | **Secret part / Phần bí mật** (numbered: "secret part #1 / phần bí mật số 1") | "Bí mật" needs no explanation. Drop "jar": number them instead. Collective: "your secret parts / các phần bí mật của bạn". |
| Generating (Cook) | Cook / Nấu | Get password, Combine, Build, Unlock / Lấy mật khẩu, Ghép, Ghép lại, Mở mật khẩu | **Button: Get password / Lấy mật khẩu**. Explaining verb: "combine / ghép". | Button states the outcome. "Ghép" describes the mechanism (two halves joined) in the explainer. |
| Final password (Dish) | Dish / Món | Password, Full password / Mật khẩu, Mật khẩu đầy đủ | **Password / Mật khẩu** | It IS a password. Use "full password / mật khẩu đầy đủ" only when contrasting with the written code. |
| Encrypted local vault (Cookbook) | Cookbook / Sổ bếp | Vault, Private vault, Safe, Locked storage / Két, Két riêng, Két an toàn, Kho khóa | **Private vault / Két riêng** (short: "Vault / Két") | "Két" (safe-box) is universally understood by Vietnamese adults. Matches the lock mental model. Avoid "Sổ" (notebook): implies readable. |
| Settings tab (Kitchen) | Kitchen / Bếp | Settings, More / Cài đặt, Thêm | **Settings / Cài đặt** | Standard Android. Do not be clever here. |
| Bottom tab 1 (home list) | Menu / Thực đơn | Passwords, Accounts, My list / Mật khẩu, Tài khoản, Danh sách của tôi | **Passwords / Mật khẩu** | Users come to find a password. "Tài khoản" collides with Google-account sign-in in Android. |
| Bottom tab 2 (manual) | Cook / Nấu | Enter code, Manual, Quick / Nhập mã, Nhập tay, Làm nhanh | **Enter code / Nhập mã** | Describes the act: paste/type a written code, get the password. Useful for paper users. |
| Bottom tab 3 | Kitchen / Bếp | see Settings | **Settings / Cài đặt** | as above |
| Master password | Master password / Mật khẩu chính | App password, Vault password, Unlock password / Mật khẩu ứng dụng, Mật khẩu két, Mật khẩu mở khóa | **Master password / Mật khẩu chính**; helper line "opens your vault / mở két của bạn" | Keep: matches other password managers, so users with any prior experience transfer it. Add helper text instead of renaming. |

Supporting terms: Connect sheet / Kết nối bảng tính; Lock / Khóa; Copy / Sao chép; Account name / Tên tài khoản; "How it works" / "Cách hoạt động" (replaces "How PassChef cooks / Cách PassChef nấu").

## 2. First-run explainer (5 screens)

Shown once on cold start before master-password setup. "Skip / Bỏ qua" top-right on every screen. Skipping skips the explainer only, not setup. Re-open from Settings > How it works / Cách hoạt động. Limits: headline <= 8 words, body <= 25 words (VI counted as words, not syllables; E1 VI headline is the approved core line, slightly trimmed). All data fake and labelled.

Backbone metaphor (replaces cooking in the explainer): **two-half password = bank box with two keys.** One key is written on the paper/sheet, one key is in your head. The word "recipe" is not used on any screen.

Story order: E1 problem and fix (normal vs PassChef) > E2 worked example > E3 works without the app > E4 your few secret parts + family > E5 setup.

### E1 - Normal way vs PassChef way (stacked top/bottom, v5)
Rule (owner 2026-11-10): reader must never take the written half for the full password. Two named things, never mixed: **Full password / Mật khẩu đầy đủ** (what you type to log in) vs **Written code / Mã đã ghi** (what goes on paper/sheet). Avoid "half of the password" wording on paper side — say "code", "not the password".
- EN headline: **Write it down. Strangers still can't log in.**
- EN body: Paper or sheet holds only the **code**, never the full password. You keep the secret part apart.
- VI headline: **Ghi ra giấy, người lạ đọc cũng vô dụng**
- VI body: Giấy hay bảng tính chỉ ghi **mã**, không ghi mật khẩu đầy đủ. Phần bí mật bạn giữ riêng.
- Illustration: two cards stacked.
  - Top "Normal way · on paper / Cách thường · giấy ghi": paper `Facebook / Basic*r4nd0m`, red label "= full password / = mật khẩu đầy đủ"; "Stranger reads it → logs in / Người lạ đọc được → đăng nhập được".
  - Bottom "Two-half way / Cách hai nửa": "Full password / Mật khẩu đầy đủ" `[Basic*|r4nd0m]` (two joined boxes), arrows split into: "Basic* · Secret part · kept apart, never written / Phần bí mật · giữ riêng · không ghi" and paper "Facebook r4nd0m#1 · Written code · on paper, not the password / Mã đã ghi · lên giấy · không phải mật khẩu"; "Stranger reads paper → sees only a code / Người lạ đọc giấy → chỉ thấy mã".
  - Family line moved to E3. Bank-box caption dropped (no room; optional in Settings › How it works).

### E2 - Worked example (fake data)
- EN headline: **See it with a made-up password**
- EN body: On the page: r4nd0m#1. The # means "put secret part #1 (Basic*) in front". Password: Basic*r4nd0m.
- VI headline: **Xem ví dụ với mật khẩu giả**
- VI body: Trên tờ giấy: r4nd0m#1. Dấu # nghĩa là thêm phần bí mật số 1 (Basic*) vào đầu. Mật khẩu: Basic*r4nd0m.
- Illustration: three-step flow. Card 1 "Written code r4nd0m#1" (random part and "#1" in two colors, key icon 1). Card 2 "+ secret part #1 = Basic*" (key icon 2, head badge). Card 3 "Password Basic*r4nd0m" with check. Tag "Example, not real data / Ví dụ, không phải dữ liệu thật".

### E3 - No app? You can still combine by hand
- EN headline: **No app? Combine it by hand**
- EN body: Your sheet plus your secret parts is enough to combine by hand. Forgot the app password? Your accounts are still reachable.
- VI headline: **Không có app? Vẫn tự ghép được**
- VI body: Chỉ cần bảng tính và các phần bí mật là tự ghép được. Quên mật khẩu app cũng không mất tài khoản.
- Illustration: phone with a crossed-out badge on the left, paper + pencil + head on the right, arrow to "Basic*r4nd0m". Caption: "The app is a shortcut, not the only way in. / Ứng dụng chỉ giúp nhanh hơn, không phải cửa duy nhất."
- Condition made explicit on E4 (secret parts must be remembered or stored elsewhere).

### E4 - Few secret parts, stored apart
- EN headline: **Remember just 1 to 3 secret parts**
- EN body: Many accounts, only a few secret parts. Keep them in your head and a copy apart from the paper, like a sealed envelope for family.
- VI headline: **Chỉ nhớ 1 đến 3 phần bí mật**
- VI body: Nhiều tài khoản, chỉ vài phần bí mật. Hãy nhớ chúng, và cất một bản riêng, như phong bì dán kín cho người nhà.
- Illustration: long faded list of 100+ accounts narrowing into three short pills ("Basic*", "Sun#7", "..."), a sealed envelope and a head icon beside them. Paper and envelope drawn in two different places.

### E5 - Setup CTA
- EN headline: **Ready? Let's set up your vault**
- EN body: Choose a master password, add your secret parts, then connect your sheet. Or skip for now and finish later.
- VI headline: **Sẵn sàng? Cùng lập két của bạn**
- VI body: Chọn mật khẩu chính, thêm phần bí mật, rồi kết nối bảng tính. Bạn cũng có thể bỏ qua và làm sau.
- CTA primary: **Set up now / Bắt đầu thiết lập**. Secondary: **Not now / Để sau** (empty Passwords tab with connect CTA).
- Illustration: closed vault door with chef-keyhole logo, amber sweep as it opens slightly.

Notes: dots 1-5, Back gesture goes to previous. Target <= 45s total. No "Cookbook/Menu/Spice/Dish" anywhere. Family "inheritance" kept to one gentle mention (E1 icon, E4 envelope); a full "Prepare for family" flow is a later phase.

## 3. Updated microcopy

### Unlock
| Element | EN | VI |
|---|---|---|
| Title | Vault locked | Két đang khóa |
| Primary | Open with fingerprint | Mở bằng vân tay |
| Secondary | Use master password | Dùng mật khẩu chính |
| Field label | Master password | Mật khẩu chính |
| Wrong password | That password didn't open the vault. Try again. | Mật khẩu chưa đúng. Bạn thử lại nhé. |
| Forgot link | Forgot password? | Quên mật khẩu? |
| Forgot sheet | We can't reset it. Your secret parts exist only on this phone, so nobody can recover them for you. | Chúng tôi không đặt lại được. Các phần bí mật chỉ nằm trên điện thoại này, nên không ai khôi phục giúp bạn được. |
| After biometric | Touch the sensor to continue where you left off. | Chạm cảm biến để tiếp tục nơi bạn dừng lại. |

### Passwords tab (home)
| Element | EN | VI |
|---|---|---|
| Empty title | No passwords yet | Chưa có mật khẩu nào |
| Empty body | Connect your Google Sheet and your list shows up here. | Kết nối bảng tính Google, danh sách của bạn sẽ hiện ở đây. |
| Empty CTA | Connect my sheet | Kết nối bảng tính |
| Empty secondary | Get one password by hand | Tự lấy một mật khẩu |
| Search placeholder | Search name, username or website | Tìm theo tên, tài khoản hoặc trang web |
| Chips | All · Favorites · Recent | Tất cả · Yêu thích · Gần đây |
| List footer | 109 accounts · updated 2 hours ago | 109 tài khoản · cập nhật 2 giờ trước |

### Connect sheet (chooser)
| Element | EN | VI |
|---|---|---|
| Title | Connect your sheet | Kết nối bảng tính |
| Option 1 (recommended) | Choose from Google Drive. Recommended: PassChef only sees the sheet you pick. | Chọn từ Google Drive. Khuyên dùng: PassChef chỉ thấy bảng tính bạn chọn. |
| Option 2 | Recent sheets | Bảng tính gần đây |
| Option 3 | Share from the Google Sheets app | Chia sẻ từ ứng dụng Google Trang tính |
| Option 4 | Paste a link | Dán đường liên kết |
| Option 5 | Offline file (no Google needed) | Tệp ngoại tuyến (không cần Google) |
| Link error | That isn't a Google Sheets link. Example: docs.google.com/spreadsheets/d/… | Đây không phải liên kết Google Trang tính. Ví dụ: docs.google.com/spreadsheets/d/… |
| Wrong account | This Google account can't open that sheet. Pick again. | Tài khoản Google này không mở được bảng tính đó. Bạn chọn lại nhé. |

(The "Pick" chip "Pick the sheet / Chọn bảng tính" for wizard step title: "Step 1 of 4: Choose sheet / Bước 1/4: Chọn bảng tính".)

### Column mapping
| Role | EN | VI |
|---|---|---|
| Screen title | Which column is what? | Cột nào là gì? |
| Helper | We guessed from the headings. Tap a column to change it. | Chúng tôi đoán theo tiêu đề. Chạm vào cột để đổi. |
| Role: name | Account name | Tên tài khoản |
| Role: username | Username | Tên đăng nhập |
| Role: website | Website | Trang web |
| Role: notes | Notes | Ghi chú |
| Role: written code | Written code (required) | Mã đã ghi (bắt buộc) |
| Header toggle | First row is headings | Hàng đầu là tiêu đề |
| Confirm | Looks right. Show 97 accounts | Đúng rồi. Xem 97 tài khoản |
| Privacy note | Your sheet is read, never changed. | Bảng tính chỉ được đọc, không bị sửa. |

### Copy snackbar and actions
| Element | EN | VI |
|---|---|---|
| Success | Password for GitHub copied. Clears in 30s. | Đã chép mật khẩu GitHub. Tự xóa sau 30 giây. |
| Copy button | Copy password | Sao chép mật khẩu |
| Missing secret part | This one needs secret part #3. Add it in Settings. | Mục này cần phần bí mật số 3. Thêm trong Cài đặt. |
| Wrong sheet | This code belongs to a different sheet. | Mã này thuộc bảng tính khác. |

### Detail screen labels
| Label | EN | VI |
|---|---|---|
| Account | Account name | Tên tài khoản |
| User | Username | Tên đăng nhập |
| Site | Website | Trang web |
| Code | Written code | Mã đã ghi |
| Secret | Secret part used: #1 | Phần bí mật dùng: số 1 |
| Pw note | Password isn't shown on screen. Copy it instead. | Mật khẩu không hiện trên màn hình. Hãy sao chép. |
| Source | From sheet: Family | Từ bảng tính: Gia đình |

### Settings (renamed rows)
Sheets / Bảng tính · Secret parts / Phần bí mật · Lock and fingerprint / Khóa và vân tay · Language / Ngôn ngữ · How it works / Cách hoạt động.

## 4. Risks and honest caveats (calm phrasing)

Place in "How it works" screen, and one short line on the connect-sheet step. Do NOT replace "Người khác thấy cũng không sao" with another absolute; wireframe line "Google Sheet chỉ chứa nhãn và công thức. Người khác thấy cũng không sao" overclaims and should go.

1. **Sheet still shows account names.**
   EN: "Your sheet doesn't contain full passwords, but it still shows which accounts you have. Keep it private, or share it only with people you trust."
   VI: "Bảng tính không chứa mật khẩu đầy đủ, nhưng vẫn cho thấy bạn có những tài khoản nào. Hãy giữ riêng tư, hoặc chỉ chia sẻ với người bạn tin."
2. **Weak secret part can be guessed.**
   EN: "A short or common secret part is easier to guess. Pick something personal and hard to predict, ideally 8+ characters."
   VI: "Phần bí mật ngắn hoặc quá quen thuộc thì dễ bị đoán. Hãy chọn thứ riêng của bạn, khó đoán, nên từ 8 ký tự."
3. **No recovery for the app, but not the end of the world.**
   EN: "We can't reset your master password. Your accounts stay reachable as long as you remember your secret parts or keep a copy somewhere separate."
   VI: "Chúng tôi không đặt lại được mật khẩu chính. Tài khoản vẫn lấy lại được nếu bạn nhớ các phần bí mật hoặc cất một bản ở nơi riêng."
4. **Family hand-over needs the secret parts stored separately.**
   EN: "For family to log in, they need the paper and the secret parts. Keep the secret parts in a different place, like a sealed envelope."
   VI: "Để người nhà đăng nhập được, họ cần tờ giấy và các phần bí mật. Hãy cất phần bí mật ở nơi khác, như một phong bì dán kín."
   Wording says "safer", never "safe". Paper alone is useless to a stranger and also to family never told the secret parts.
5. **One site leak plus sheet leak together.** If a website leaks your password in plain text AND your sheet is also exposed, someone could work out a secret part.
   EN: "If a website leaks your password, change that account's random part, and keep your sheet private."
   VI: "Nếu một trang web bị lộ mật khẩu, hãy đổi phần ngẫu nhiên của tài khoản đó và giữ bảng tính riêng tư."
   Show it where the user creates secret parts, not on the intro screens.
6. **Pattern leakage.** Many written codes in one sheet could show habits. Low risk if random parts are truly random; do not mention beyond help text.
7. **Copied password sits on the clipboard** until it clears in 30s. Already covered by snackbar.

Tone rule: state the limit, then the one thing the user can do. No fear language, no "hackers".

## Recommended term set (compact)

- Sheet / Bảng tính
- Written code / Mã đã ghi
- Random part / Phần ngẫu nhiên
- Secret part / Phần bí mật (numbered #1-#3)
- Get password / Lấy mật khẩu (action); combine / ghép (explainer verb)
- Password / Mật khẩu
- Private vault / Két riêng
- Master password / Mật khẩu chính
- Tabs: Passwords / Mật khẩu · Enter code / Nhập mã · Settings / Cài đặt
- Explainer metaphor: two-half password / mật khẩu hai nửa (bank box, two keys)
- Brand flavor only: chef-keyhole logo, tagline "Don't keep the password. Keep the recipe."

## Unresolved questions

1. Owner: accept "plain words in UI, cooking only as brand flavor", and narrow AGENTS.md Cooking Protocol to docs/marketing?
2. Are there other position operators besides `#` (front)? E3 teaches only `#`. If suffix/other symbols exist, add them to "How it works", not the explainer.
3. Does the Android app allow 1-3 secret parts, or still up to 5 (spec says 1-5 jars)? Copy says 1-3 per owner; confirm cap and recommend wording if 5.
4. Is the "8+ characters" guidance for secret parts enforced by the app? If not, drop the number or add a strength hint.
5. Does the phone-side "locked copy" of secret parts hold for the offline path too, per spec? E4 claims it.
6. Test comprehension with 3-5 real Vietnamese adults 50+: "Két riêng" vs "Két an toàn", and "Mã đã ghi" vs "Mã lưu". Not tested here.
7. Landing page and Chrome extension still use cooking terms; align or accept divergence?
9. Syntax: extension recipes use `#`, `$`, `@`, digits 1-5. Android UI should hide symbols and let users pick "secret part at the start/end" in words; E2 shows `#` only as one example. Confirm.
8. Family hand-over: add a printable "instruction card" feature? Out of scope here but the E2 promise ("family can log in") depends on it.
