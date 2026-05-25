# Product Naming Decision

> **Status:** In Progress — shortlisted, not finalized
> **Last updated:** 2026-05-25
> **Current name:** mindVault (to be replaced before Chrome Web Store publish)

---

## 1. Why Rename

mindVault sounds like every other password manager (1Password, LastPass, Bitwarden — all "vaults"). It fails to communicate the product's actual differentiator: passwords are **generated on demand from recipes**, never stored.

The name was fine for development but insufficient for commercialization where first impression = install decision.

---

## 2. Product Identity

### What the product IS
- Chrome Extension that generates passwords from "recipes" in Google Sheets + encrypted "secret spices"
- Zero-storage: passwords don't exist until created on demand
- Uses Argon2id + AES-256-GCM encryption
- Currently Google Sheets only

### Core Metaphor (permeates entire UX)
| Term | Meaning |
|------|---------|
| Recipe | Formula stored in Google Sheet (e.g., `r4nd0m#1`) |
| Secret Spice | Encrypted secret phrase stored in Chrome |
| Cooking | Combining recipe + spice to produce password |
| Chef | The user (or the extension, depending on framing) |
| Ingredient | The random text portion of a recipe |

### Value Proposition (1 sentence)
> Your Google Sheet stores recipes publicly. Your passwords stay private — cooked fresh on demand.

### Confirmed Copy (liked by stakeholder)
- **Tagline:** "Cook your password from a recipe. Nothing stored."
- **Chrome Web Store subtitle:** "Recipe-based password generator for Google Sheets"

---

## 3. Differentiators — Ranked by Importance

Analysis identified 4 layers of differentiation. Each is a different way to express the same underlying truth (passwords aren't stored), but they differ in **what they emphasize**:

### Layer A: Recipe/Cooking Metaphor — UNIQUE, highest moat
- **What:** Passwords are "cooked" from recipes + secret spices
- **Why strongest:** No other password manager uses this concept. Competitors can copy features but copying a metaphor looks like imitation. Creates a complete storytelling framework (recipe, spice, ingredient, cooking style, topping)
- **Implication for name:** A cooking-related word signals uniqueness immediately
- **Risk:** Ties brand to a UX metaphor that could theoretically change

### Layer B: Zero-Storage / Ephemeral — strongest security argument
- **What:** Passwords never exist until needed, nothing to steal
- **Why important:** Directly attacks the vulnerability of vault-based managers
- **Implication for name:** Words like "fresh", "mint", "ghost", "never" — but hard to fit in a name without sounding negative or vague
- **Decision:** Better suited for **tagline** than name. Confirmed tagline: "Nothing stored."

### Layer C: Generation Mechanism — technically accurate
- **What:** Passwords are derived/generated, not retrieved
- **Why important:** Positive framing of Layer B
- **Implication for name:** Words like "derive", "forge", "brew", "mint"
- **Decision:** Can be expressed through cooking words (brew, cook) which also serve Layer A

### Layer D: Google Sheets Platform — most immediately understandable
- **What:** Works with Google Sheets, the tool 900M+ people already use
- **Why important:** Instant clarity, strong SEO, unclaimed territory
- **Implication for name:** "Sheet" in the name
- **Risk:** Limits perceived scope if product expands to Excel/Notion
- **Counter-argument:** Product currently ONLY works with Sheets. Being specific beats being generic for a small unknown product
- **Decision:** Better suited for **subtitle** than name. Confirmed subtitle mentions "Google Sheets."

### Summary: Name should carry Layer A (cooking). Layers B+D go to tagline/subtitle.

---

## 4. Naming Criteria — Evaluation Framework

Each candidate is scored 1-5 on these criteria:

| # | Criterion | Weight | Description |
|---|-----------|--------|-------------|
| 1 | **Password Signal** | High | Does a user immediately know this is a password/security tool? |
| 2 | **Differentiator Signal** | High | Does the name communicate something unique vs. competitors? |
| 3 | **Memorability** | High | Can someone recall and spell the name after hearing it once? |
| 4 | **Storytelling** | Medium | Does the name invite a story? ("Why is it called that?") |
| 5 | **Professionalism** | Medium | Does it feel trustworthy for a security product? |
| 6 | **Simplicity** | Medium | Short, easy to pronounce in English and Vietnamese |
| 7 | **Chrome Web Store Stand-out** | Medium | Does it visually/textually stand out in a list of XxxPass extensions? |
| 8 | **Future-proof** | Low | Still makes sense if product expands beyond Google Sheets |
| 9 | **Domain Availability** | Low | Likely available as .com/.app/.io (not verified) |

### Discovered Principles During Discussion
- **Compound names work best** (2 concepts joined) — concrete, visual, story-carrying
- **"Pass" in name is nice-to-have, not required** — Chrome Web Store searches name + subtitle + description. Bitwarden ranks #1 for "password manager" without "pass" in name
- **"Pass" in name has a COST** — blends into the LastPass/NordPass/KeePass crowd
- **"Vault/Lock/Safe/Guard" = trap** — sounds like every competitor, implies storage (contradicts zero-storage philosophy)
- **"Key" is underused** — fewer competitors use it, technically accurate (product produces keys)
- **Clarity vs. Creativity trade-off** — small unknown products benefit from clarity (SheetVault), established brands can afford creativity (PassChef)

---

## 5. Naming Strategies Explored

### Strategy 1: [Password-word] + [Cooking-word]
Directly combines the two most important signals.
- Examples: PassChef, BrewPass, SpicePass, CookKey

### Strategy 2: [Cooking-word] + [Security-word]
Leads with cooking metaphor, security second.
- Examples: ChefKey, SpiceKey, SaltKey, RecipeKey

### Strategy 3: [Platform-word] + [Security-word]
Leads with Google Sheets connection.
- Examples: SheetVault, SheetKey, SheetGuard

### Strategy 4: [Ephemeral-word] + [Password-word]
Leads with "nothing stored" concept.
- Examples: FreshPass, GhostPass, NeverStored

### Strategy 5: Abstract / Single-word
Professional, minimal, not tied to any specific concept.
- Examples: Derive, Distill, Forge, Simmer

---

## 6. Candidate Shortlist

### Tier 1 — Strongest Candidates

#### PassChef
- **Strategy:** Password + Cooking
- **Story:** "You're the chef. Extension is your kitchen. Sheet has recipes. You cook passwords."
- **Strengths:** Most memorable, best storytelling, cooking metaphor = product moat, "Pass" aids search
- **Weaknesses:** Slightly playful for security tool, "Chef" implies expertise/complexity (product sells simplicity), Chef.io SEO overlap, blends into XxxPass crowd
- **Tagline:** "Cook your password from a recipe. Nothing stored."

#### SheetVault
- **Strategy:** Platform + Security
- **Story:** "Your Google Sheet becomes a secure vault."
- **Strengths:** Instantly understood, strongest SEO for "google sheets password", professional, claims unclaimed territory
- **Weaknesses:** "Vault" implies storage (contradicts zero-storage), sounds like a generic password manager with Sheets, doesn't hint at recipe/cooking mechanism, limits future expansion
- **Tagline:** "Your spreadsheet. Your recipes. Your passwords — generated, never stored."

#### SpiceKey
- **Strategy:** Cooking + Security
- **Story:** "Add your secret spice, get your key."
- **Strengths:** Most elegant, dual meaning (cooking spice + crypto salt), "Key" avoids XxxPass crowd, professional yet unique
- **Weaknesses:** Doesn't immediately signal "password tool", could be mistaken for keyboard product, "Spice" alone doesn't fully convey cooking metaphor
- **Tagline:** "Your secret ingredient for every password."

### Tier 2 — Strong Alternatives

#### RecipeKey
- Most descriptive of the mechanism. "Recipe produces a key." Self-explanatory.
- Risk: feels explanatory rather than brand-like.

#### BrewPass
- Clean, professional, implies creation. Middle ground between PassChef and Derive.
- Risk: "brew" = beer/coffee association, less unique.

#### ChefKey
- Balanced: cooking + security, avoids "Pass" crowd.
- Risk: same Chef.io concern, "chef" implies complexity.

### Tier 3 — Considered and Set Aside

| Name | Why set aside |
|------|--------------|
| SaltBox | "Box" implies storage — contradicts value prop |
| SaltPass / SaltKey | Dual meaning is clever but "salt" doesn't tell a story |
| FreshPass | Too generic, could be any product |
| GhostPass | "Ghost" undermines trust |
| NeverStored | Marketing-bold but negative framing, hard to pronounce in Vietnamese |
| Derive | Elegant but too abstract, doesn't signal password tool |
| Distill | Pretentious, whiskey association |
| PassMint | "Mint" doesn't clearly connect to cooking |
| CookKey | Sounds like "cookie" — web security confusion |
| mindVault | Generic, indistinguishable from competitors |

---

## 7. Decision Matrix — Tier 1 Candidates

| Criterion (weight) | PassChef | SheetVault | SpiceKey |
|---------------------|:---:|:---:|:---:|
| Password Signal (H) | 5 | 4 | 3 |
| Differentiator Signal (H) | 5 | 3 | 4 |
| Memorability (H) | 5 | 4 | 4 |
| Storytelling (M) | 5 | 2 | 3 |
| Professionalism (M) | 3 | 5 | 5 |
| Simplicity (M) | 4 | 4 | 5 |
| Chrome Store Stand-out (M) | 3 | 5 | 5 |
| Future-proof (L) | 5 | 2 | 5 |
| Domain Availability (L) | 4 | 3 | 4 |
| **Weighted Total** | **40** | **34** | **38** |

> Weighted: H=x1.5, M=x1.0, L=x0.5

---

## 8. Open Questions — Resolved

1. **Brand tone: playful or professional?**
   - **RESOLVED: Consumer/approachable.** Product targets end-users, not enterprise. Playful tone is a strength, not a weakness.
   - Implication: PassChef's approachability is a fit, not a risk. SheetVault's corporate tone is a mismatch.

2. **Who is the chef — user or extension?**
   - **RESOLVED: User is the chef.** The extension is the kitchen tool. User chooses recipes, remembers spices, cooks passwords.
   - Implication: "PassChef" names the user's role — empowering framing. The name says "YOU are the chef of your passwords."

3. **How important is Google Sheets in the name vs. subtitle?**
   - **RESOLVED: Subtitle/description only.** "Google Sheets" must be searchable and readable somewhere (subtitle, description, landing page) but NOT in the title. Keeps name flexible for future platform expansion.
   - Implication: SheetVault is eliminated from Tier 1. PassChef and SpiceKey remain.

4. **Does the cooking metaphor survive product evolution?**
   - **RESOLVED: Yes.** The RECIPE mechanism is the core — it defines the product's identity. Even if features grow (auto-fill, teams, new platforms), the fundamental operation remains: recipe + spice = password. The metaphor is durable.
   - Implication: Cooking-based names are future-proof because the mechanism won't change.

5. **Domain and trademark check needed**
   - **UNRESOLVED.** Still need to verify domain (.com/.app/.io) and Chrome Web Store availability for remaining candidates.

### Impact of Resolutions on Tier 1

| Candidate | Status After Resolutions |
|-----------|-------------------------|
| **PassChef** | **Strengthened** — consumer tone fits, "user as chef" confirmed, recipe mechanism is durable. "PassChef explains the system best. I can say: 'Your sheet holds recipes, your head holds secret spices, and this tool helps you cook the password when you need it.'" |
| **SpiceKey** | Remains viable — professional but still consumer-accessible |
| **SheetVault** | **Eliminated** — corporate tone mismatches consumer target; "Sheet" in name conflicts with subtitle-only decision |

---

## 9. Next Steps

- [ ] Resolve open questions (section 8)
- [ ] Verify domain availability for Tier 1 candidates
- [ ] Check Chrome Web Store for existing extensions with similar names
- [ ] Final decision on name
- [ ] Plan rebrand execution (manifest, locales, README, landing page, docs)
