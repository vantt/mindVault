# Product Value Statements

> Tổng hợp từ `docs/naming-decision.md` — các câu đánh giá thể hiện giá trị và bản chất của hệ thống.
> Dùng làm nguồn ngữ liệu cho demo page, landing page, onboarding copy.

---

## Bản chất hệ thống

- "Passwords are **generated on demand from recipes**, never stored."
- "Zero-storage: passwords don't exist until created on demand"
- "Chrome Extension that generates passwords from 'recipes' in Google Sheets + encrypted 'secret spices'"
- "Uses Argon2id + AES-256-GCM encryption"
- "Recipe mechanism is the core — it defines the product's identity"
- "The fundamental operation remains: recipe + spice = password"

---

## Value Proposition chính thức

- **Value Prop (1 sentence):** "Your Google Sheet stores recipes publicly. Your passwords stay private — cooked fresh on demand."
- **Tagline xác nhận:** "Cook your password from a recipe. Nothing stored."
- **Chrome Web Store subtitle:** "Recipe-based password generator for Google Sheets"

---

## Lớp giá trị (Differentiators)

- "No other password manager uses this concept." *(Layer A — Recipe/Cooking Metaphor)*
- "Directly attacks the vulnerability of vault-based managers" *(Layer B — Zero-Storage)*
- "Nothing to steal" *(Zero-storage as security argument)*
- "Works with Google Sheets, the tool 900M+ people already use" *(Layer D)*
- "Creates new category 'zero-storage security' that vault-based competitors cannot copy"

---

## Câu chuyện sản phẩm (Story)

- "You're the chef. Extension is your kitchen. Sheet has recipes. You cook passwords."
- "Your sheet holds recipes, your head holds secret spices, and this tool helps you cook the password when you need it." — Feynman
- "Add your secret spice, get your key." *(SpiceKey story)*
- "The name says 'YOU are the chef of your passwords.'" *(User empowerment framing)*
- "Mint fresh keys on demand." *(MintKey)*
- "Passwords derived, never stored." *(Derivio)*
- "Nothing stored. Nothing to steal." *(VoidKey — eliminated)*

---

## Câu trích dẫn đánh giá từ Council

- **Feynman:** "It wins on first principles because consumers understand it instantly."
- **Feynman:** "VoidKey tells users what's ABSENT; PassChef tells users what they DO."
- **Feynman:** "Discoverability beats purity."
- **Aristotle:** "Clearest taxonomy: it immediately signals both category (Pass) and core metaphor (Chef)."
- **Torvalds:** "Clearest, most ownable consumer name. Directly matches the product metaphor."
- **Torvalds (Final):** "VoidKey is a clever word. PassChef is a product. Users don't buy clever — they buy things they can explain to their grandmother in a sentence."
- **Meadows:** "Creates the strongest feedback loop because the cooking metaphor is instantly legible."
- **Machiavelli (91% confidence):** "PassChef wins on power, memory, and conversion. 'VoidKey' signals emptiness, coldness, and risk; it sounds niche, even adversarial. 'PassChef' is vivid, ownable, and immediately teaches the product's metaphor."
- **Rams:** "Good design makes a product understandable. VoidKey requires explanation, which is poor design."
- **Rams:** "PassChef serves the user; VoidKey serves the engineer's ego."
- **Taleb (concession):** "Antifragility requires surviving volatility first. A name that fails at installation is fragile, not antifragile."
- **Sun Tzu:** "VoidKey claims terrain competitors cannot occupy without self-destruction."
- **Watts:** "PassChef is semantically coherent — you are the chef of your own passwords."
- **Kahneman:** "'Nothing stored. Nothing to steal.' directly addresses loss aversion — the dominant psychological driver."

---

## Lý do VoidKey thua — insight về người dùng

- "System 1 does not parse taglines first." — tiếp cận tự nhiên quan trọng hơn lý luận kỹ thuật
- "Cooking metaphor teaches the product in 3 seconds"
- "Concrete nouns beat abstract ones"
- "Cooking metaphor is instantly legible"

---

## Tóm tắt giá trị cốt lõi

| Chiều | Câu đánh giá |
|-------|-------------|
| Cơ chế | "Recipe + spice = password, generated on demand, never stored" |
| Bảo mật | "Nothing to steal — zero attack surface" |
| UX | "Cooking metaphor teaches the product in 3 seconds" |
| Positioning | "New category: zero-storage security competitors cannot copy" |
| Người dùng | "You are the chef of your own passwords" |
