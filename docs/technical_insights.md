# Technical Insights & Debugging Log

Tài liệu này tổng hợp các bài học kinh nghiệm, các vấn đề kỹ thuật hóc búa (Edge Cases) và giải pháp đã thực hiện trong quá trình phát triển SecretHash Chrome Extension.

## 1. Chrome Manifest V3 & WASM (Argon2)

### Vấn đề

Khi sử dụng thư viện `argon2-browser` trong môi trường Chrome Extension Manifest V3, Service Worker sẽ báo lỗi vi phạm Content Security Policy (CSP) khi cố gắng load module WebAssembly (WASM).
Lỗi thường gặp: `Wasm code generation disallowed by embedder`.

### Nguyên nhân

Manifest V3 mặc định rất nghiêm ngặt về việc thực thi code động (eval, new Function, WASM) để đảm bảo bảo mật.

### Giải pháp

Cần khai báo rõ ràng quyền thực thi WASM trong `manifest.json` thông qua `content_security_policy`.

```json
"content_security_policy": {
  "extension_pages": "script-src 'self' 'wasm-unsafe-eval'; object-src 'self'"
}
```

_Lưu ý: `script-src 'self'` là bắt buộc trước khi thêm các directive khác._

---

## 2. UI Isolation (Shadow DOM)

### Vấn đề

Khi Inject UI (Popup hiển thị mật khẩu) trực tiếp vào trang web (Content Script):

1.  **CSS Bleeding (In)**: CSS của trang web (Google Sheets) làm hỏng giao diện của Popup (ví dụ: thay đổi font, box-sizing, màu sắc).
2.  **CSS Bleeding (Out)**: Class CSS của Extension (`.popup`, `.btn`) có thể vô tình trùng và làm hỏng giao diện trang web gốc.
3.  **Z-Index War**: Popup bị đè bởi các thành phần điều hướng của trang web.

### Giải pháp

Sử dụng **Shadow DOM** với chế độ `open`.

```javascript
const host = document.createElement("div");
const shadow = host.attachShadow({ mode: "open" });
const style = document.createElement("style");
style.textContent = `...css rules...`; // Style bên trong Shadow DOM là local
shadow.appendChild(style);
shadow.appendChild(content);
document.body.appendChild(host);
```

**Lợi ích**:

- CSS bên trong Shadow DOM không ảnh hưởng ra ngoài.
- CSS bên ngoài không (dễ dàng) ảnh hưởng vào trong.
- `zIndex` cao nhất (`2147483647`) đặt ở Host element đảm bảo luôn nằm trên cùng.

---

## 3. Robust Formula Parsing (Xử lý Regex)

### Vấn đề

Regex ban đầu được thiết kế quá chặt chẽ: `^([a-z]+)...$`.
Khi người dùng click vào một ô trong Google Sheet, `innerText` trả về có thể chứa khoảng trắng thừa, hoặc các ký tự định dạng ẩn, hoặc người dùng gõ thêm text mô tả (ví dụ: `Link: fb#1`).
-> Regex strict (`^...$`) sẽ trả về `null` -> Extension thất bại im lặng.

### Giải pháp

1.  **Loose Match, Strict Validate**: Content Script sử dụng regex lỏng hơn (`match`) để **trích xuất** cụm công thức tiềm năng từ chuỗi văn bản hỗn độn.
2.  **Trim**: Luôn `trim()` input trước khi xử lý.
3.  **Feedback**: Nếu `RegexParserAdapter` ở backend vẫn từ chối (do sai cấu trúc), Content Script phải hiển thị thông báo lỗi (Error Toast) thay vì im lặng.

```javascript
// Content Script Extraction
const formulaMatch = text.trim().match(/([a-zA-Z0-9]+)([#@$%^])(\d)(...)?/);
if (formulaMatch) {
  // Send ONLY the extracted formula (e.g., "fb#1") to backend
  sendMessage(formulaMatch[0]);
}
```

---

## 4. Zero-Friction Installation (Auto-Injection)

### Vấn đề

Sau khi cài đặt Extension (hoặc reload trong quá trình dev), Extension **không hoạt động** trên các tab Google Sheets đã mở từ trước. Người dùng phải ấn F5 thủ công.

### Giải pháp

Sử dụng sự kiện `chrome.runtime.onInstalled` trong Service Worker để tự động tiêm (inject) Content Script vào các tab phù hợp ngay khi cài đặt xong.

```javascript
// service_worker.js
chrome.runtime.onInstalled.addListenerAttribute(async () => {
  const tabs = await chrome.tabs.query({
    url: "*://docs.google.com/spreadsheets/*",
  });
  for (const tab of tabs) {
    chrome.scripting.executeScript({
      target: { tabId: tab.id },
      files: ["content/content.js"],
    });
  }
});
```

_Yêu cầu quyền `scripting` trong manifest._

---

## 5. Clipboard Handling in MV3

### Vấn đề

Tính năng "Auto-clear clipboard" cần kiểm tra xem trong clipboard có phải là mật khẩu của mình không trước khi xóa (để tránh xóa nhầm nội dung mới copy của user).
Tuy nhiên, quyền `clipboardRead` không được cấp cho Content Script trong ngữ cảnh này (yêu cầu User Gesture tại thời điểm đọc).

### Giải pháp & Đánh đổi

- Sử dụng quyền `clipboardWrite` (dễ xin hơn).
- Chấp nhận đánh đổi: Thực hiện xóa mù (Write empty string) sau 30 giây.
- Logic giảm thiểu rủi ro: Chỉ xóa nếu user không thực hiện thao tác copy khác (Rất khó kiểm soát hoàn hảo trong Content Script mà không có quyền Read, nhưng tạm chấp nhận được cho MVP).

---

## 6. Smart Positioning (UX)

### Vấn đề

Popup hiển thị ở `rect.bottom`. Nếu ô tính nằm ở dòng cuối cùng của màn hình, popup bị che khuất.

### Giải pháp

Tính toán không gian khả dụng (`window.innerHeight - rect.bottom`).

- Nếu không gian < chiều cao popup (100px) -> Hiển thị popup bên trên (`rect.top - height`).
- Ngược lại -> Hiển thị bên dưới.

---

## 7. Localization (i18n) Pattern

### Pattern hiệu quả

Thay vì dùng JS để set text thủ công cho từng ID, sử dụng thuộc tính `data-i18n` trong HTML và một hàm helper chung:

```html
<h1 data-i18n="appName">Header</h1>
```

```javascript
function localizeHtml() {
  document.querySelectorAll("[data-i18n]").forEach((el) => {
    const key = el.getAttribute("data-i18n");
    el.textContent = chrome.i18n.getMessage(key);
  });
}
```

Giúp code JS sạch hơn và dễ bảo trì file ngôn ngữ.

---

## 8. Web Crypto API: Exportable Keys

### Vấn đề

Khi thực hiện Setup hoặc Change Password, cần lưu trữ `sessionKey` dưới dạng JWK vào `chrome.storage.session`.
Tuy nhiên, `crypto.subtle.exportKey("jwk", key)` ném lỗi:
`DOMException: key is not extractable`.

### Nguyên nhân

Khi import key (sau khi derive từ Argon2) bằng `crypto.subtle.importKey`, cờ `extractable` được set là `false` (mặc định cho mục đích bảo mật, để key không bị lộ ra ngoài JS context).
Tuy nhiên, để lưu trữ lại key này vào session (để dùng lại mà không cần derive lại tốn thời gian), chúng ta buộc phải export nó.

### Giải pháp

Khi import key lần đầu (để tạo object Key dùng cho session), phải set `extractable: true`.

```javascript
const key = await crypto.subtle.importKey(
  "raw",
  keyMaterial,
  { name: "AES-GCM" },
  true, // <--- MUST BE TRUE
  ["encrypt", "decrypt"]
);
```

---

## 9. Argon2 WASM in Chrome Extension (Custom Wrapper)

### Vấn đề

Thư viện `argon2-browser` mặc định sử dụng cơ chế loading WASM của Emscripten phụ thuộc vào việc fetch file `.wasm`.
Trong môi trường Extension, đường dẫn file bị thay đổi (tương đối với background script hoặc content script), và logic mặc định của Emscripten thường tìm file `.wasm` không đúng chỗ.
Ngoài ra, việc dùng trực tiếp `import argon2 from 'argon2-browser'` có thể trả về một module chưa sẵn sàng (Promise) hoặc Object cấu hình thấp cấp, gây lỗi `ArgonType is undefined` hoặc `Module is not a function`.

### Giải pháp

Tạo một **Wrapper Module** (`src/wasm/wrapper.js`) để kiểm soát việc khởi tạo module Emscripten:

1.  **Locate File Hook**: Can thiệp vào `locateFile` của Emscripten để chỉ định chính xác đường dẫn file `.wasm` sử dụng `import.meta.url` hoặc `chrome.runtime.getURL`.
2.  **Promise Wrappers**: Đảm bảo Module chỉ được trả về khi WASM đã thực sự load xong (`postRun` hook).
3.  **High-Level API**: Export một API đơn giản (`hash`, `verify`) che giấu sự phức tạp của việc cấp phát bộ nhớ (malloc/free) trong WASM.

````javascript
// wrapper.js snippet
global.Module = {
  locateFile: (path) => new URL("./argon2.wasm", import.meta.url).href,
};

// ... load script ...

---

## 10. Extension Context Invalidation Handling

### Vấn đề

Sau khi Extension được cập nhật hoặc reload (do developer hoặc browser auto-update), các Content Script đang chạy trên các tab cũ bị "mồ côi" (orphaned). Kết nối đến Background Script bị ngắt hoàn toàn.
Mọi nỗ lực gọi `chrome.runtime.sendMessage` từ Content Script sẽ ném lỗi: `Error: Extension context invalidated`.
User sẽ thấy chức năng không phản hồi hoặc lỗi console đỏ lòm, gây trải nghiệm người dùng rất tệ.

### Giải pháp

1.  **Try/Catch Wrapper**: Luôn bọc mọi lệnh gọi `sendMessage` trong khối `try/catch`.
2.  **Specific Detection**: Bắt chính xác chuỗi lỗi `Extension context invalidated`.
3.  **User Actionable Feedback**: Hiển thị thông báo (Toast) ngay trên giao diện yêu cầu User reload lại trang (F5). Đây là cách duy nhất để nạp lại Content Script mới kết nối được với Extension mới.
4.  **Graceful Degradation**: Nếu tính năng không quá quan trọng, hãy disable nó đi thay vì liên tục thử lại gây spam lỗi.

---

## 11. UI Visibility Management (Single Source of Truth)

### Vấn đề

Khi xây dựng giao diện Single Page Application (SPA) đơn giản bằng Vanilla JS (như Options Page hoặc Popup), thường có nhiều "màn hình" ảo (các thẻ `div`).
Nếu quản lý ẩn/hiện thủ công (ví dụ: khi bật A thì nhớ tắt B), rất dễ xảy ra lỗi logic khi thêm màn hình C. Kết quả là **"Double Screens"** - hiển thị chồng chéo hai màn hình cùng lúc (ví dụ: vừa hiện Unlock vừa hiện Dashboard).

### Giải pháp

Luôn sử dụng một hàm **Centralized Helper** để chuyển đổi màn hình. Nguyên tắc: **Reset tất cả về ẩn trước, sau đó mới hiện cái cần thiết.**

```javascript
// options.js
const allSections = [setupSection, unlockSection, dashboardSection, changePasswordSection];

function showSection(targetSection) {
    // 1. Force hide ALL sections first (Reset state)
    allSections.forEach(s => s.classList.add("hidden"));

    // 2. Show ONLY the target
    targetSection.classList.remove("hidden");
}
````

---

## 12. Cross-Context State Synchronization

### Vấn đề

Chrome Extension hoạt động trên nhiều context khác nhau:

1.  **Popup**: Khi click vào icon.
2.  **Options Page**: Khi mở trang cài đặt full tab.
3.  **Background**: Service Worker.

Trạng thái Unlock (Session Key) được lưu trong `chrome.storage.session`. Nếu User mở Options Page (đang bị khóa), sau đó click vào Popup và Unlock thành công, thì Options Page vẫn hiển thị màn hình khóa -> Trạng thái bị lệch (Stale State).

### Giải pháp

Sử dụng `chrome.storage.onChanged` trong các trang UI (Options/Popup) để lắng nghe sự thay đổi trạng thái từ bất kỳ đâu.

```javascript
// options.js
chrome.storage.onChanged.addListener((changes, area) => {
  // Nếu sessionKey thay đổi (vừa unlock hoặc vừa lock từ popup)
  if (area === "session" && changes.sessionKey) {
    checkStatus(); // Gọi hàm kiểm tra và vẽ lại UI ngay lập tức
  }
});
```

---

## 13. In-Popup Unlock vs In-Page Popup (UX Decision)

### Vấn đề

Cách tiếp cận ban đầu: Bắt click vào ô input trên trang web -> Hiển thị Popup nhập mật khẩu ngay tại chỗ (In-Page Popup).
Nhược điểm:

- **Intrusive**: Che mất nội dung trang web.
- **Technical Debt**: Phải xử lý vị trí (positioning), z-index, và focus stealing rất phức tạp.
- **Security Perception**: User có thể e ngại khi nhập Master Password vào một khung nhìn giống như thuộc về trang web đó.

### Giải pháp (Refactored UX)

Chuyển hoàn toàn việc nhập Master Password về **Extension Popup** (Context riêng của Extension).

1.  **Workflow**: Click Icon -> Nhập Pass -> Enter.
2.  **Auto-resume**: Ngay khi Unlock thành công, Extension tự động quét lại tab hiện tại. Nếu đang ở trên Google Sheet có công thức, nó tự động tính toán và hiển thị kết quả luôn mà không cần user thao tác thêm.
3.  **Disable In-Page Click**: Tắt bỏ tính năng click-to-show popup trên trang web để tránh conflict, giúp trải nghiệm mượt mà và "sạch" hơn.
