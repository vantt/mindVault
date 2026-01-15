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
