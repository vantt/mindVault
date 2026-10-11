# 16 — Demo Page (Standalone)

Current implementation — 2026-10-12. A static, synthetic Recipe explainer bundled
with the extension. It reads only the locale preference, not a Cookbook, a private
Spice, or an unlocked-session key. It does not persist examples or cook real dishes.

## Entry and close controls

| Entry | Selector | Outcome |
|---|---|---|
| Home Quick Start | `#btn-qs-learn-more` | Dismisses Quick Start and opens bundled `demo/demo.html` via `chrome.tabs.create`. |
| Builder How it works | `#btn-how-it-works` | Opens the same bundled guide. |
| Top/footer Close this tab | `#btn-close-top`, `#btn-close-demo` | `window.close()` closes a programmatically opened guide tab. |

Internal `chrome.runtime.getURL()` tabs require no `web_accessible_resources` entry
or expanded match/host permissions. No manifest change accompanies this page.
The dead Open Recipe Builder control was removed: the guide directs users back
to the real popup Builder rather than offering an unwired new feature.

## Layout and content

- Precision ink/amber/moss/coral palette and spacing are retained locally in
  `demo.css`. System body/monospace and Georgia/serif display fonts work offline;
  the demo does not load the shared token stylesheet with its remote font import.
- Navigation, four section chips, heading/body copy, tables, annotations, image alt
  and both close controls localize in English and Vietnamese. Semantic `<code>`
  examples remain deterministic: `fb#1`, `S3cretfb`, `abc` + `XYZ`, `r4nd0m#1!?`.
- Content explains Recipe/ingredient/style/jar/modifiers/tag, private encrypted
  Spices and Chrome Sync, manual rotation, shortcut versus icon, clipboard and
  session boundaries. It never claims all data vanishes when the popup closes.
- Strip keeps ASCII letters/digits, reverse uses the existing Recipe semantics,
  uppercase follows; `_` switches only prefix/suffix. Strip is not reversible.
- Max-width720px; two-column sections collapse at640px. Tables wrap text and
  code without horizontal overflow; verified viewports are400px and1200px.
- Images are bundled and synthetic. Section chips are real anchors, with visible
  keyboard focus; no remote script/font/CDN is needed.

## Locale handling

`loadLocale()` follows `chrome.storage.sync.language`; the demo sets document
`lang` to `en`/`vi`. `localizeDemo()` uses `textContent` for marked text and
`setAttribute` for image alt, never HTML parsing. Mark only prose leaves so code
examples and semantic structure survive substitution. Fallback English is present
in the HTML; close handlers register before asynchronous locale loading.

## Rendered keys

| Key | English | Vietnamese |
|---|---|---|
| `demoPageTitle` | PassChef — How it works | PassChef — Cách hoạt động |
| `demoNavLabel` | Demo · how it works | Minh họa · cách hoạt động |
| `demoCloseTab` | Close this tab | Đóng tab này |
| `demoReadTime` | Read time · ~3 min | Thời gian đọc · khoảng 3 phút |
| `demoHeroTitle` | How PassChef works. | PassChef hoạt động thế nào. |
| `demoHeroBody` | Keep Recipes in your Cookbook and private Spices encrypted on your device. Cook each Dish when you need it. | Giữ Recipe trong Menu và mã hóa gia vị riêng trên thiết bị. Nấu món ăn mỗi khi cần. |
| `demoToc01` | The idea | Ý tưởng |
| `demoToc02` | Anatomy of a recipe | Cấu trúc Recipe |
| `demoToc03` | Modifiers | Biến thể |
| `demoToc04` | Daily use | Sử dụng hằng ngày |
| `demoIdeaBody` | A Recipe combines an ingredient with one of your private Spices to cook a Dish. The Cookbook holds Recipes, not the finished passwords. | Recipe kết hợp nguyên liệu với một gia vị riêng để nấu món ăn. Menu chứa Recipe, không chứa mật khẩu hoàn chỉnh. |
| `demoIdeaStorage` | Your Spices are encrypted on the device and in Chrome Sync. You unlock the kitchen with your master password; Spices do not appear in the Cookbook. | Gia vị được mã hóa trên thiết bị và trong Chrome Sync. Bạn mở bếp bằng mật khẩu chính; gia vị không xuất hiện trong Menu. |
| `demoCookbookAlt` | Synthetic Cookbook containing fb#1 and gm$2 Recipes | Menu minh họa chứa Recipe fb#1 và gm$2 |
| `demoSheetCaption` | Only Recipes are visible in the Cookbook. | Menu chỉ hiển thị Recipe. |
| `demoConstruction` | How a Dish is cooked | Cách nấu món ăn |
| `demoIngredient` | ingredient | nguyên liệu |
| `demoPrefix` | Spice first | gia vị trước |
| `demoFirstSpice` | Spice jar 1 | hũ gia vị 1 |
| `demoYourSpice` | + Example Spice: | + Gia vị minh họa: |
| `demoSynthetic` | synthetic example only | chỉ là ví dụ minh họa |
| `demoDish` | Dish: | món ăn: |
| `demoInsight` | Anyone with only the Recipe still lacks the private Spice needed to cook your Dish. Use long, unpredictable ingredients and strong Spices. | Chỉ có Recipe thì vẫn thiếu gia vị riêng để nấu món ăn của bạn. Hãy dùng nguyên liệu dài, khó đoán và gia vị mạnh. |
| `demoSection01` | Section 01 | Phần 01 |
| `demoIdeaTitle` | The Idea | Ý tưởng |
| `demoAnatomyBody` | Read a Recipe as ingredient, cooking style, Spice jar, then optional modifiers and verification tag. | Đọc Recipe theo thứ tự: nguyên liệu, kiểu nấu, hũ gia vị, rồi các biến thể và thẻ kiểm chứng tùy chọn. |
| `demoIngredientLabel` | Ingredient | Nguyên liệu |
| `demoIngredientDetail` | Unpredictable account text; change it when rotating a Dish. | Chuỗi khó đoán cho tài khoản; đổi chuỗi này khi thay món ăn. |
| `demoPlacementLabel` | Cooking style | Kiểu nấu |
| `demoPlacementDetail` | Where to mix the Spice into the ingredient. | Vị trí trộn gia vị vào nguyên liệu. |
| `demoJarLabel` | Spice jar | Hũ gia vị |
| `demoJarDetail` | Choose one of up to five private Spices. | Chọn một trong tối đa năm gia vị riêng. |
| `demoModifierLabel` | Optional modifier | Biến thể tùy chọn |
| `demoModifierDetail` | Transform the Spice or switch prefix/suffix placement. | Biến đổi gia vị hoặc đổi vị trí trước/sau. |
| `demoPlacementExample` | Cooking styles: ingredient abc, Spice XYZ. | Kiểu nấu: nguyên liệu abc, gia vị XYZ. |
| `demoSymbol` | Symbol | Ký hiệu |
| `demoMeaning` | Meaning | Ý nghĩa |
| `demoResult` | Dish | món ăn |
| `demoStyleTopping` | Topping — Spice first | Phủ — gia vị trước |
| `demoStyleBase` | Base — Spice last | Lót — gia vị sau |
| `demoStyleFilling` | Filling — Spice in the middle | Nhồi — gia vị ở giữa |
| `demoStyleMix` | Mix — alternate one unit | Trộn — xen kẽ từng ký tự |
| `demoStyleSandwich` | Sandwich — alternate pairs | Kẹp — xen kẽ từng cặp |
| `demoTagNote` | A Builder verification tag checks the Cookbook/profile match. It does not change the Dish; you can cook by hand without the tag. | Thẻ kiểm chứng từ Builder kiểm tra Menu/hồ sơ có khớp không. Thẻ không đổi món ăn; bạn có thể tự nấu mà bỏ qua thẻ. |
| `demoSection02` | Section 02 | Phần 02 |
| `demoAnatomyTitle` | Anatomy of a Recipe | Cấu trúc Recipe |
| `demoModifiersBody` | Place modifiers after the Spice jar number. They are deterministic, but stripping characters is not reversible. Combined Spice transformations run strip, reverse, then uppercase. | Đặt biến thể sau số hũ gia vị. Kết quả xác định, nhưng lọc ký tự không thể đảo ngược. Khi kết hợp, gia vị được lọc, đảo rồi viết hoa. |
| `demoModifierFlip` | flip | đổi vị trí |
| `demoEffectFlip` | Switches topping # and base $; other styles stay the same. | Đổi kiểu phủ # và lót $; các kiểu khác giữ nguyên. |
| `demoModifierUpper` | upper | viết hoa |
| `demoEffectUpper` | Uppercases the Spice before cooking. | Viết hoa gia vị trước khi nấu. |
| `demoModifierReverse` | reverse | đảo |
| `demoEffectReverse` | Reverses the Spice before cooking. | Đảo gia vị trước khi nấu. |
| `demoModifierStrip` | strip | lọc |
| `demoEffectStrip` | Keeps only A–Z, a–z and 0–9 in the Spice. | Chỉ giữ A–Z, a–z và 0–9 trong gia vị. |
| `demoRecipeExample` | Recipe with modifiers: | Recipe có biến thể: |
| `demoModifierExample` | Spice jar 1 is reversed, then uppercased, before being placed first. | Gia vị hũ 1 được đảo rồi viết hoa, sau đó đặt trước nguyên liệu. |
| `demoSection03` | Section 03 | Phần 03 |
| `demoModifiersTitle` | Modifiers | Biến thể |
| `demoDailyBody` | Unlock PassChef, select a Recipe cell in your Google Cookbook, then open the extension. A valid Recipe cooks a Dish; missing data or a mismatch stays on Home with guidance. | Mở khóa PassChef, chọn ô Recipe trong Google Menu, rồi mở extension. Recipe hợp lệ nấu món ăn; thiếu dữ liệu hoặc không khớp sẽ ở Home với hướng dẫn. |
| `demoStepOpen` | Open your Cookbook | Mở Menu |
| `demoStepOpenBody` | Keep Recipes in cells; private Spices stay encrypted separately. | Giữ Recipe trong ô; gia vị riêng được mã hóa ở nơi khác. |
| `demoStepSelect` | Select a Recipe cell | Chọn ô Recipe |
| `demoStepSelectBody` | Click a cell such as fb#1 or gh$2!, then open PassChef with its toolbar icon. | Chọn ô như fb#1 hoặc gh$2!, rồi mở PassChef bằng biểu tượng trên thanh công cụ. |
| `demoStepUnlock` | Unlock and cook | Mở khóa và nấu |
| `demoStepUnlockBody` | The icon opens the popup. Ctrl+Shift+L cooks the selected cell through the page flow; it is not the popup opener. | Biểu tượng mở popup. Ctrl+Shift+L nấu ô đã chọn qua luồng trên trang; phím này không mở popup. |
| `demoStepCopy` | Copy and paste | Sao chép và dán |
| `demoStepCopyBody` | Use Copy to put the Dish on the clipboard. PassChef attempts to clear it after 30 seconds; closing the popup alone does not clear the system clipboard. | Dùng Sao chép để đưa món ăn vào clipboard. PassChef thử xóa sau 30 giây; chỉ đóng popup không xóa clipboard của hệ thống. |
| `demoSessionNote` | The unlocked session locks after 5 idle minutes or 10 total minutes. Encrypted Spices can remain in Chrome Sync; this static demo reads no Cookbook or private Spice. | Phiên mở khóa khóa sau 5 phút không hoạt động hoặc 10 phút tổng cộng. Gia vị mã hóa có thể nằm trong Chrome Sync; trang minh họa tĩnh này không đọc Menu hay gia vị riêng. |
| `demoRotateTitle` | Rotating a Dish? | Đổi món ăn? |
| `demoRotateBody` | Change the ingredient, for example fb#1 to fb2#1, then update the password on the account website yourself. Your Spice can stay the same. | Đổi nguyên liệu, ví dụ fb#1 thành fb2#1, rồi tự đổi mật khẩu trên website tài khoản. Gia vị có thể giữ nguyên. |
| `demoSection04` | Section 04 | Phần 04 |
| `demoClosingTitle` | Ready to cook. | Sẵn sàng nấu. |
| `demoClosingBody` | Close this guide and open Recipe Builder from the popup. The Builder starts with random picks and keeps More options collapsed. | Đóng hướng dẫn rồi mở Recipe Builder trong popup. Builder bắt đầu với lựa chọn ngẫu nhiên và thu gọn Tùy chọn khác. |
| `demoDailyUseTitle` | Daily Use | Sử dụng hằng ngày |

## Verification boundary

`test/unit/demo-locale.spec.mjs` checks all rendered EN/VI keys, unmarked prose,
literal substitution, preserved examples and bundled assets/font independence.
`test/e2e/demo-offline.spec.ts` checks the real loaded extension with HTTP(S)
blocked, two locales/two viewports, section navigation, image rendering, both
popup entry controls and both programmatic close controls. Evidence is recorded
in the current completion report after the run finishes. These demo checks do not
certify live Google detection/native shortcut or the complete security/UI matrix.

Related: [Home](03-popup-home.md), [Generated](04-popup-generated.md),
[Builder](05-recipe-builder.md), [locale catalogue](15-i18n-keys.md).
