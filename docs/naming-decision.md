# Product Naming Decision

> **Status:** Decision Ready — Council deliberation + focused debate complete
> **Last updated:** 2026-05-25
> **Current name:** mindVault (to be replaced before Chrome Web Store publish)
> **Council Winner:** VoidKey (9/18 initial + 5/6 debate) — see Section 10.9

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

### NEW: Tier 1 Candidates (Post-Council Brainstorming)

> **Source:** Council of High Intelligence brainstorming session (18 members, 2026-05-25)

#### 🥇 VoidKey — COUNCIL WINNER (9/18 votes)
- **Strategy:** Zero-storage security signal
- **Story:** "Nothing stored. Nothing to steal. Your passwords exist only when you need them."
- **Strengths:** 
  - Directly communicates core differentiator (zero-storage)
  - Antifragile positioning — signals "no attack surface"
  - Professional, memorable, unique
  - Differentiates strongly from vault-based competitors
- **Weaknesses:**
  - "Void" may sound cold, nihilistic, or empty
  - Requires careful onboarding to frame absence as strength
  - Doesn't reference cooking metaphor
- **Tagline:** "Nothing stored. Nothing to steal."
- **Council Quote:** "VoidKey signals to attackers: 'no central honeypot, nothing to steal, move along.' Antifragility through absence." — Taleb

#### 🥈 PassChef — COUNCIL RUNNER-UP (7/18 votes)
- **Strategy:** Password + Cooking (unchanged from original)
- **Story:** "You're the chef. Extension is your kitchen. Sheet has recipes. You cook passwords."
- **Strengths:** Most memorable, best storytelling, cooking metaphor = product moat, "Pass" aids search
- **Weaknesses:** Slightly playful for security tool, blends into XxxPass crowd
- **Tagline:** "Cook your password from a recipe. Nothing stored."
- **Council Quote:** "PassChef explains the system best. I can say: 'Your sheet holds recipes, your head holds secret spices, and this tool helps you cook the password when you need it.'" — Feynman

#### 🥉 Derivio — COUNCIL THIRD (2/18 votes)
- **Strategy:** Technical accuracy + Scalability
- **Story:** "Passwords are derived from your recipe. Never stored, always computed."
- **Strengths:**
  - Technically accurate (derivation is the core mechanism)
  - Sounds like a product (Figma, Notion, Derivio)
  - Scales to enterprise ("Derivio for Teams")
  - No metaphor to outgrow
- **Weaknesses:**
  - Too clinical for consumer audience
  - Less memorable than cooking metaphors
  - Doesn't immediately signal "password tool"
- **Tagline:** "Passwords derived, never stored."
- **Council Quote:** "Low search term overlap with existing products, ensuring clearer brand differentiation." — Karpathy

### NEW: Other Strong Candidates (from Brainstorming)

| Name | Proposers | Strengths | Why Not Tier 1 |
|------|-----------|-----------|----------------|
| MintKey | Aurelius, Meadows | Fresh + security, professional | Less differentiated than VoidKey |
| SpiceForge | Musashi, Sun Tzu | Upgrade of SpiceKey, has weight | Still in cooking metaphor bucket |
| CipherMint | Munger | Clean, modern, encryption feel | Less memorable |
| KeyForge | Socrates, Kahneman | Creation + strength | "Key" less clear than "Password" |
| SecretSauce | Aurelius, Aristotle | Idiomatic, everyone understands | May be too casual |

---

## 7. Decision Matrix — Tier 1 Candidates

### Original Matrix (Pre-Council)

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

### Updated Matrix (Post-Council) — NEW TIER 1

| Criterion (weight) | VoidKey | PassChef | Derivio |
|---------------------|:---:|:---:|:---:|
| Password Signal (H) | 3 | 5 | 3 |
| Differentiator Signal (H) | 5 | 4 | 4 |
| Memorability (H) | 5 | 5 | 3 |
| Storytelling (M) | 4 | 5 | 2 |
| Professionalism (M) | 5 | 3 | 5 |
| Simplicity (M) | 5 | 4 | 4 |
| Chrome Store Stand-out (M) | 5 | 3 | 4 |
| Future-proof (L) | 5 | 5 | 5 |
| Enterprise Scale (L) | 4 | 2 | 5 |
| **Weighted Total** | **42** | **40** | **36** |
| **Council Votes** | **9** | **7** | **2** |

> Note: VoidKey scores highest on differentiation + professionalism + stand-out. PassChef scores highest on storytelling + password signal.

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

- [x] ~~Resolve open questions (section 8)~~ — Resolved via Council deliberation
- [ ] Verify domain availability for NEW Tier 1 candidates (VoidKey, PassChef, Derivio)
- [ ] Check Chrome Web Store for existing extensions with similar names
- [ ] Final decision on name
- [ ] Plan rebrand execution (manifest, locales, README, landing page, docs)

---

## 10. Council of High Intelligence Deliberation

> **Date:** 2026-05-25
> **Mode:** Full Council (18 members)
> **Providers:** Anthropic (6), OpenAI (7), Google (5)

### 10.1 Initial Evaluation (Existing Candidates)

The first council session evaluated the original shortlist (PassChef, SpiceKey, SheetVault, RecipeKey, BrewPass, ChefKey).

**Result:** PassChef won 10/18 votes in the initial evaluation.

| Name | Votes | Key Supporters |
|------|-------|----------------|
| PassChef | 10 | Socrates, Aurelius, Lao Tzu, Feynman, Aristotle, Musashi, Meadows, Torvalds, Watts, Munger |
| SpiceKey | 5 | Sutskever, Kahneman, Taleb, Sun Tzu, Rams |
| RecipeKey | 2 | Ada, Machiavelli |

**Key Insights from Initial Evaluation:**
- **Feynman:** "PassChef explains the system best. I can say: 'Your sheet holds recipes, your head holds secret spices, and this tool helps you cook the password when you need it.'"
- **Torvalds:** "Stop talking and ship the code."
- **Taleb:** "PassChef has tail risk — a security researcher tweets 'Your password manager is called Chef? I rest my case.'"
- **Sutskever:** "SpiceKey has better scaling properties. A 'SpiceKey SDK' sounds natural; 'PassChef Enterprise' sounds like a restaurant POS."

---

### 10.2 Brainstorming Session (New Candidates)

A second session asked all 18 members to propose NEW names beyond the existing shortlist.

**New Names Generated:**

| Category | Names | Proposers |
|----------|-------|-----------|
| **Mint/Forge** | MintKey, KeyForge, SpiceForge, SeedForge | Aurelius, Meadows, Socrates, Kahneman, Musashi, Sutskever |
| **Derive** | Deriva, Derivio, DerivPass, DeriveDish | Lao Tzu, Sutskever, Meadows, Ada, Feynman |
| **Zero/Void** | VoidKey, Vaultless, ZeroPass, HollowVault | Taleb, Sutskever, Karpathy |
| **Mix/Blend** | VaultMix, Keyblend, AlchemKey, CipherMint | Aurelius, Kahneman, Lao Tzu, Meadows, Munger |
| **Cooking-adjacent** | SecretSauce, VaultChef, DishKey, RecipeCipher | Aurelius, Aristotle, Torvalds, Musashi, Feynman |
| **Abstract** | KineticKey, CipherKey, Mnemonic | Karpathy, Rams, Watts |

**Most-Endorsed New Names:**
1. **MintKey** (3 endorsements) — "Mint fresh keys on demand"
2. **SpiceForge** (2 endorsements) — Stronger than SpiceKey
3. **Derivio** (4 variants mentioned) — Technically accurate, scalable
4. **VoidKey** (1 strong endorsement from Taleb) — "Nothing to steal"

---

### 10.3 Final Vote (6 Candidates)

Final voting session with expanded shortlist:

| # | Candidate | Tagline |
|---|-----------|---------|
| 1 | PassChef | "Cook your password from a recipe." |
| 2 | MintKey | "Mint fresh keys on demand." |
| 3 | SpiceForge | "Forge passwords from secret spice." |
| 4 | Derivio | "Passwords derived, never stored." |
| 5 | CipherMint | "Cipher + Mint = encrypted freshness." |
| 6 | VoidKey | "Nothing stored. Nothing to steal." |

**FINAL RESULTS:**

| Name | Votes | Percentage | Voters |
|------|-------|------------|--------|
| **🥇 VoidKey** | **9** | 50% | Socrates, Aurelius, Lao Tzu, Sutskever, Kahneman, Taleb (Anthropic) + Machiavelli, Rams, Watts (Google) |
| **🥈 PassChef** | **7** | 39% | Feynman, Sun Tzu, Aristotle, Ada, Torvalds, Musashi, Meadows (OpenAI) |
| **🥉 Derivio** | **2** | 11% | Karpathy, Munger (Google) |

**Provider Distribution:**
| Provider | VoidKey | PassChef | Derivio |
|----------|---------|----------|---------|
| Anthropic (6) | 6 | 0 | 0 |
| OpenAI (7) | 0 | 7 | 0 |
| Google (5) | 3 | 0 | 2 |

---

### 10.4 Key Arguments

#### For VoidKey (Winner)
- **Taleb:** "VoidKey signals to attackers: 'no central honeypot, nothing to steal, move along.' Antifragility through absence."
- **Kahneman:** "'Nothing stored. Nothing to steal.' directly addresses loss aversion — the dominant psychological driver."
- **Aurelius:** "VoidKey communicates the core value with moral clarity — nothing stored means nothing to steal."
- **Rams:** "It communicates the essential user benefit—nothing is stored—with the utmost clarity and honesty."

**Concern (raised by ALL 9 VoidKey voters):** "Void" may sound cold, nihilistic, or empty. Requires careful framing in onboarding.

#### For PassChef (Runner-up)
- **Feynman:** "It wins on first principles because consumers understand it instantly."
- **Aristotle:** "Clearest taxonomy: it immediately signals both category (Pass) and core metaphor (Chef)."
- **Torvalds:** "Clearest, most ownable consumer name. Directly matches the product metaphor."
- **Meadows:** "Creates the strongest feedback loop because the cooking metaphor is instantly legible."

**Concern (raised by ALL 7 PassChef voters):** Slightly playful for security tool. Execution must signal serious security.

#### For Derivio (Third)
- **Karpathy:** "Low search term overlap with existing products, ensuring clearer brand differentiation."
- **Munger:** "Most directly describes the 'derived' benefit. Clever metaphors only invite confusion."

**Concern:** Too clinical for consumer audience. Feels more like enterprise brand.

---

### 10.5 Updated Shortlist (Post-Council)

| Rank | Name | Tagline | Best For | Concerns |
|------|------|---------|----------|----------|
| 🥇 | **VoidKey** | "Nothing stored. Nothing to steal." | Security-first positioning, differentiation from vaults | "Void" sounds cold; needs warm onboarding |
| 🥈 | **PassChef** | "Cook your password from a recipe." | Consumer friendliness, memorability, storytelling | Playful tone; needs serious execution |
| 🥉 | **Derivio** | "Passwords derived, never stored." | Enterprise scale, technical accuracy | Too clinical for consumers |

---

### 10.6 Decision Framework

**Choose VoidKey if:**
- Target is security-conscious users who value differentiation
- Want to signal "we are NOT a vault"
- Comfortable with bold, unconventional branding
- Planning to compete on security philosophy, not just features

**Choose PassChef if:**
- Target is mainstream consumers who want simplicity
- Want instant understanding via cooking metaphor
- Comfortable with playful-but-trustworthy tone
- Planning to compete on user experience and memorability

**Choose Derivio if:**
- Planning enterprise/B2B expansion
- Want maximum scalability ("Derivio for Teams")
- Prefer technical accuracy over consumer appeal
- Targeting developer/security professional audience

---

### 10.7 Unresolved Questions

1. **Domain/trademark availability** — Not yet verified for VoidKey, PassChef, Derivio
2. **"Void" perception test** — Should test with real users if "Void" triggers negative associations
3. **Tagline for VoidKey** — "Nothing stored. Nothing to steal." is strong but consider alternatives:
   - "Your passwords exist only when you need them."
   - "The key to nothing. And everything."
   - "Zero storage. Zero risk."

---

### 10.8 Council Verdict Summary

**Winner: VoidKey (9/18 votes, 50%)**

VoidKey emerged as an unexpected winner from the brainstorming session. It was not on the original shortlist but captures the product's core differentiator — zero-storage architecture — in the name itself.

The council split by provider family:
- Anthropic models favored VoidKey (security-through-absence philosophy)
- OpenAI models favored PassChef (consumer-friendly metaphor)
- Google models split between VoidKey and Derivio

---

### 10.9 Focused Debate: VoidKey vs PassChef

> **Date:** 2026-05-25
> **Mode:** Focused 6-member debate
> **Purpose:** Deep adversarial analysis of top 2 candidates

#### Debate Panel

| Role | Expert | Assignment |
|------|--------|------------|
| VoidKey Advocate | Taleb | Steel-man VoidKey, attack PassChef |
| PassChef Advocate | Feynman | Steel-man PassChef, attack VoidKey |
| Neutral Psychology | Kahneman | Consumer decision science analysis |
| Strategic Analysis | Sun Tzu | Market positioning & competitive terrain |
| User-Centered | Rams | Design honesty & user clarity |
| Final Verdict | Torvalds | Pragmatic shipping decision |

#### Debate Results: **VoidKey 5 — PassChef 1**

| Expert | Vote | Key Argument |
|--------|------|--------------|
| **Taleb** | VoidKey | Antifragile positioning — attacks on "vault" competitors strengthen VoidKey's narrative. "Cold is a feature, not a bug." PassChef optimizes for wrong metric (warmth vs. security signal). |
| **Feynman** | PassChef | "VoidKey tells users what's ABSENT; PassChef tells users what they DO." Discoverability beats purity. "Void" is marketing disaster for consumer discovery. |
| **Kahneman** | VoidKey (75%) | Loss aversion: "nothing to steal" triggers protective instinct. Security category anchoring outweighs warmth penalty. For stated target (consumers): VoidKey wins. |
| **Sun Tzu** | VoidKey | Creates defensible terrain competitors cannot occupy without self-destruction (abandoning vault architecture). PassChef = feature war they win in 90 days. "VoidKey claims terrain competitors cannot occupy without self-destruction. In warfare, that is called 'death ground' — for them, not you." |
| **Rams** | VoidKey | Serious tone matches security domain. "Void" + security context reads as "we hold nothing." Cooking metaphor imports foreign context — more confusing, not less. |
| **Torvalds** | VoidKey | Self-documenting. PassChef requires explanation. "Security products SHOULD feel cold. Users don't want a cuddly password manager." |

#### Key Arguments Synthesized

**VoidKey Strengths (5 experts agreed):**
1. **Strategic moat** — Creates new category "zero-storage security" that vault-based competitors cannot copy (Sun Tzu)
2. **Self-documenting** — "Void" + "Key" immediately signals value prop (Torvalds, Rams)
3. **Psychological anchoring** — Loss aversion ("nothing to steal") stronger than warmth (Kahneman)
4. **Antifragile** — Every competitor breach strengthens VoidKey's narrative (Taleb)
5. **Domain honesty** — Security products should feel serious (Rams, Torvalds)

**PassChef's Best Defense (Feynman):**
- "VoidKey optimizes for security experts, not discovery"
- Cooking metaphor teaches the product — recipe + spice = password
- "Void" tells what's absent; "Chef" tells what user does
- First 3 seconds = everything. PassChef wins those 3 seconds.

**VoidKey's Acknowledged Weakness:**
- "Void" may read as "empty/broken" to some consumers (all 5 VoidKey voters acknowledged)
- Cold tone requires marketing discipline to not feel hostile

#### Torvalds' Pragmatic Directive

> **What I'd do TODAY:**
> 1. Verify voidkey.com/voidkey.app availability
> 2. If available, register it, ship with VoidKey
> 3. If not, ship PassChef — don't let domain hunting block launch another day
>
> Stop debating. Ship the damn thing.

#### Debate Verdict

**Winner: VoidKey**

The focused debate confirmed the initial council vote. VoidKey wins on strategic defensibility and psychological anchoring. PassChef remains strong fallback if "Void" tests poorly with users or domains unavailable.

---

### 10.10 Final Recommendation

**Primary Choice: VoidKey**
- Tagline: "Nothing stored. Nothing to steal."
- Positioning: Zero-storage security (new category)
- Risk: "Void" perception — mitigate with warm onboarding

**Fallback: PassChef**
- Tagline: "Cook your password from a recipe."
- Positioning: Consumer-friendly password creation
- Use if: VoidKey domains unavailable OR user testing shows negative "void" reaction

**Recommended Next Steps:**
1. Verify domain availability (voidkey.com, voidkey.app, voidkey.io)
2. If available, register immediately
3. Test "VoidKey" with 5-10 target users for immediate reaction
4. If "Void" tests poorly, switch to PassChef
5. Make final decision and execute rebrand
