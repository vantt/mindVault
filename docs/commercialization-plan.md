# Commercialization Plan

> **Status:** Partial — pre-launch material exists; publication and later launch phases pending
> **Last updated:** 2026-10-11 (status/relevance audit; historical estimates retained)
> **Product:** PassChef Password Generator (name decided — see [naming-decision.md](marketing/naming-decision.md))
> **Current version:** v2.3 (Full Access Sharing)

---

## 1. Current Product State

### What Exists
| Area | Status | Detail |
|------|--------|--------|
| Core extension | Implemented; security/acceptance gaps remain | Recipe kitchen and Argon2id + AES-256-GCM exist; MVP roadmap tracks remaining unlock/clipboard/real Menu checks |
| Multi-profile | Implemented | Named profiles, per-sheet routing, export/import; current UI acceptance remains tracked |
| Sharing | Implemented | Full Access bundle (v2.3); source implementation does not close all sharing UX checks |
| Verification tag | Core Done; UI verification Partial | Recipe integrity validation (v2.2); active tag plan retains warnings/localization/UI tests |
| i18n | Partial | EN/VI core messages exist; active tag/popup plans retain missing warnings and full demo Vietnamese |
| Tests | Existing verification | Latest tooling-removal check:77 unit tests passed; historical E2E evidence retained, no new full E2E run in this audit |
| Landing page | Partial launch readiness | Recipe demos/FAQ/testimonial UI/responsive implementation exist; assets, content alignment and QA remain |
| Chrome Web Store | **Publication unverified / pending** | Listing copy + privacy/terms files exist; screenshot assets and actual listing URL not established in repo |

### What's Missing for Commercial Launch
| Area | Priority | Effort |
|------|----------|--------|
| Privacy Policy + Terms of Service | Files complete; pre-publication content review | Low |
| Chrome Web Store listing (screenshots, description, promo images) | **Blocker** | Medium |
| Brand name finalization | Done | PassChef decided/rebranded |
| Landing page upgrade (conversion-focused) | High | Medium |
| Analytics (privacy-respecting) | High | Low |
| Payment integration (if freemium) | Medium | Medium |
| Demo page / onboarding video | Demo implemented; video optional/format undecided | Medium |
| Support channel | Medium | Low |

---

## 2. First Principles: Why Would Someone Pay?

### The Real Value
Users don't pay for "a Chrome extension." They pay for:
1. **Peace of mind** — passwords can't be stolen because they don't exist
2. **Convenience** — Google Sheets is already their system, this makes it secure
3. **Simplicity** — no new app to learn, no migration, no vendor lock-in
4. **Self-sufficiency** — users can rebuild any password by hand from recipe + secret. No tool dependency. Recipe on paper + secret in memory = full access even without the extension. The extension is a convenience layer, not a lock-in

### Who Is the Customer?

#### Primary (highest intent)
- **People already storing passwords in Google Sheets** — they know the risk, want a solution that doesn't force migration
- Estimated size: millions globally (Google Sheets has 900M+ users; password-in-spreadsheet is a common anti-pattern)

#### Secondary
- **Privacy-conscious users** who distrust cloud password managers (1Password, LastPass breaches)
- **Small teams** sharing account access via spreadsheets (common in startups, families)

#### Not the Customer (yet)
- Enterprise with compliance requirements
- Users happy with existing password managers
- Non-Chrome browser users

---

## 3. Trust — The #1 Barrier

Password managers require extraordinary trust. Without it, no marketing matters.

### Trust Building Checklist

| Action | Purpose | Priority |
|--------|---------|----------|
| **Privacy Policy** | Legal requirement + user confidence | Blocker |
| **Terms of Service** | Legal protection | Blocker |
| **Security whitepaper** (public) | Technical credibility — already have `security-assessment.md` as base | High |
| **Open-source decision** | Transparency = trust. Trade-off: open-source increases trust but reduces moat | High — decide |
| **"How it works" explainer** | Demystify the recipe concept for non-technical users | High |
| **No-telemetry badge** | "We don't track you" — powerful for privacy audience | Medium |
| **Third-party audit** | Expensive but highest trust signal. Consider after traction | Later |

### Open Source — Decision Needed

| Option | Trust | Moat | Revenue Impact |
|--------|:---:|:---:|:---:|
| Fully open-source (MIT/Apache) | Highest | Lowest | Harder to charge, but community contribution |
| Source-available (BSL/SSPL) | High | Medium | Can still monetize, code is inspectable |
| Closed source | Lowest | Highest | Traditional SaaS model |

**Recommendation:** Source-available. Users can inspect code (trust), but can't commercially redistribute (moat). Many security tools do this (e.g., Bitwarden uses AGPL).

---

## 4. Distribution — How People Find You

### Channel Strategy

| Channel | Action | Cost | Expected Impact |
|---------|--------|------|-----------------|
| **Chrome Web Store** | Publish listing with optimized description, screenshots, promo tiles | Free | Primary discovery — most users find extensions here |
| **Landing page** | Upgrade to conversion-focused: hero + demo + trust signals + install CTA | Free (time) | SEO + direct traffic + credibility |
| **SEO / Content** | Target: "google sheets password manager", "spreadsheet password generator", "password recipe" | Free (time) | Long-tail organic traffic |
| **Product Hunt** | Launch post with demo video | Free | Initial spike, tech-savvy audience |
| **Reddit** | r/privacy, r/selfhosted, r/googsheets, r/chrome — genuine participation, not spam | Free | Community trust, early adopters |
| **Vietnamese tech communities** | Spiderum, Viblo, Vietnamese dev groups | Free | Home market advantage (bilingual product) |
| **YouTube** | "How I manage 100+ passwords with Google Sheets" tutorial | Free (time) | Evergreen content, high intent viewers |

### Chrome Web Store Optimization

**Title:** `{ProductName} — Recipe-based Password Generator for Google Sheets`

**Short description (132 chars max):**
> Cook passwords from recipes in Google Sheets. Zero-knowledge. Nothing stored. Argon2id encrypted.

**Category:** Productivity (not "Security" — less competition, broader audience)

**Screenshots needed (5):**
1. Recipe in Google Sheet → password generated (core flow)
2. Popup showing generated password with copy button
3. Options page — profile management
4. Recipe Builder — creating a new recipe
5. Export/Import — sharing with team

---

## 5. Revenue Model

### Freemium — **DECIDED: Option A2** (2026-06-13)

Free = perfect for personal + work (2 Sheets). Pro = power user / team / multi-context.

| Feature | Free | Pro |
|---------|:---:|:---:|
| Password generation — all cooking styles | ✓ | ✓ |
| All modifiers (`_` `!` `?` `~`) | ✓ | ✓ |
| Recipe verification tags | ✓ | ✓ |
| Recipe Builder UI | ✓ | ✓ |
| Hotkey (Ctrl+Shift+L) | ✓ | ✓ |
| Auto-lock, clipboard clear (30s) | ✓ | ✓ |
| **2 profiles** (personal + work covered) | ✓ | ✓ |
| **Unlimited profiles (3+)** | ✗ | ✓ |
| **Per-sheet auto-routing** | ✗ | ✓ |
| **Profile Export/Import (sharing)** | ✗ | ✓ |
| **Session timeout — configurable** | fixed 5/10 min | ✓ up to 60 min |
| **Priority support** | ✗ | ✓ |

**Rationale:** 2 profiles covers the most common free use case (personal + work). Wall appears naturally at the 3rd profile (team / family / banking separation), not at initial setup. Closed-source model supports this gate.

> **Note:** Verification tags and Recipe Builder stay free — gating safety/core UX features would undermine trust with a security-conscious audience.

### Pricing Options

| Model | Price | Pros | Cons |
|-------|-------|------|------|
| **Monthly subscription** | $3/month | Recurring revenue, lower barrier | Subscription fatigue |
| **Annual subscription** | $29/year | Better retention, perceived value | Higher upfront commitment |
| **Lifetime license** | $49 one-time | Users love it, simple | No recurring revenue |
| **Pay-what-you-want** | $5+ suggested | Low barrier, good will | Unpredictable revenue |

**DECIDED (2026-06-13):** Lifetime license with tiered launch pricing:
- **Phase 1 (soft launch):** Free — collect reviews + installs
- **Phase 2 (early bird):** $9 lifetime — "Founding price, cheaper than 1 month of 1Password"
- **Phase 3 (after 500+ installs, 20+ reviews):** Raise to $14-19 lifetime

### Payment Infrastructure
- **Stripe** or **LemonSqueezy** (handles taxes, invoicing, Chrome extension licensing)
- License key validation via Chrome extension — check on install, periodic re-validation
- Grace period on validation failure (offline users)

---

## 6. Launch Sequence — Phased Approach

### Phase 0: Pre-launch (1-2 weeks)

**Status: Partial.** Name/rebrand/legal files/listing copy exist; owner actions and
release assets below have no completion proof here. Historic domain/pricing/fee
estimates are not a current availability or price check.
- [x] Finalize product name → **PassChef** (2026-05-25, unanimous council decision)
- [x] Execute rebrand across codebase (manifest, locales, README, landing page, docs) → done 2026-05-25
- [x] Write Privacy Policy + Terms of Service → `landing-page/privacy.html`, `landing-page/terms.html` (2026-06-13)
- [x] Write Chrome Web Store listing copy → `docs/marketing/cws-listing-copy.md` (2026-06-13)
- [x] Manifest version updated to 2.3.0, CWS-optimized description (2026-06-13)
- [ ] **[USER ACTION]** Create Chrome Web Store developer account ($5 one-time fee)
- [ ] **[USER ACTION]** Register domain — passchef.com recommended (all 4 variants appear unclaimed; verify via registrar)
- [ ] **[USER DECISION]** Set up support channel (GitHub Issues / email / Discord)
- [ ] Prepare screenshots and promo images (440x280, 920x680, 1400x560) — see `docs/marketing/cws-listing-copy.md` for specs

### Phase 1: Soft Launch — Free (week 3-4)

**Status: Pending release; CTA implementation Partial.** Existing CTA links to
the generic Chrome store (`landing-page/index.html`:1355), not a verified PassChef
listing. No actual publication/install/review result is established by this audit.
- [ ] Publish to Chrome Web Store (free, all features unlocked)
- [ ] Connect existing landing-page install CTA to the real PassChef listing after publication
- [ ] Share in Vietnamese tech communities (personal network)
- [ ] Collect feedback, fix bugs, iterate
- [ ] **Goal:** 50-100 installs, 5+ reviews

### Phase 2: Public Launch (week 5-6)

**Status: Pending.** No campaign publication or adoption evidence in repo. These
are launch goals, not authority to publish posts or contact communities now.
- [ ] Product Hunt launch
- [ ] Reddit posts (genuine, valuable content — not ads)
- [ ] YouTube tutorial video
- [ ] SEO-optimized blog post on landing page
- [ ] **Goal:** 500+ installs, 4.5+ star rating

### Phase 3: Monetization (week 8+)

**Status: Pending implementation.** A2/lifetime decisions below remain accepted;
no payment/license integration or production tier enforcement is established here.
Selecting a provider and implementing billing requires its own scoped work.
- [ ] Implement free/pro tier split
- [ ] Integrate payment (LemonSqueezy or Stripe)
- [ ] License key system in extension
- [ ] Upgrade landing page with pricing section
- [ ] **Goal:** First paying customers

### Phase 4: Growth (ongoing)

**Status: Deferred / exploratory.** Depends on real launch/feedback. Consider/
explore/localization ideas are not approved commitments or blockers for Phase0/1.
- [ ] Content marketing (blog, YouTube, community)
- [ ] Feature development based on user feedback
- [ ] Consider third-party security audit
- [ ] Explore team/business tier
- [ ] Localization expansion (Japanese? Korean? Spanish?)

---

## 7. Metrics to Track

### Product Metrics
| Metric | Tool | Target (6 months) |
|--------|------|-------------------|
| Chrome Web Store installs | CWS dashboard | 1,000+ |
| Weekly active users | Privacy-respecting analytics | 300+ |
| Rating | CWS dashboard | 4.5+ stars |
| Reviews | CWS dashboard | 20+ |
| Uninstall rate | CWS dashboard | < 30% (7-day) |

### Revenue Metrics (Phase 3+)
| Metric | Target |
|--------|--------|
| Conversion rate (free → paid) | 3-5% |
| Monthly recurring revenue | Track |
| Lifetime value per customer | Track |
| Churn rate | < 5% monthly |

### Analytics Approach
- **No third-party tracking scripts** — this is a security product, users expect privacy
- Use **Chrome Web Store built-in analytics** (installs, uninstalls, demographics)
- Optional: self-hosted privacy-respecting analytics (Plausible, Umami) on landing page only
- Extension itself: zero telemetry. Period.

---

## 8. Legal Requirements

### Must-Have Before Publish

| Document | Purpose | Notes |
|----------|---------|-------|
| **Privacy Policy** | CWS requirement + GDPR/CCPA | Must explain: what data is collected (none sent to servers), what permissions are used and why, how encrypted data is stored |
| **Terms of Service** | Legal protection | Liability limitation, no warranty on password security, user responsibility for master password |

### Key Privacy Claims (must be truthful)
- No data sent to external servers
- No analytics in the extension
- Encrypted data stored only in Chrome's sync storage (Google's infrastructure)
- Extension cannot recover master password
- Clipboard cleared after 30 seconds

### Intellectual Property
- Trademark search for chosen name — verify no conflicts
- Domain registration for chosen name
- Logo/icon — original design, no stock assets with restrictive licenses

---

## 9. Competitive Landscape

| Competitor | Model | Differentiator vs. Us |
|------------|-------|----------------------|
| **1Password** | Subscription $3-5/mo | Cloud vault — stores passwords. We don't store anything |
| **Bitwarden** | Freemium, $10/year | Open-source vault. Still storage-based |
| **LastPass** | Freemium | History of breaches. Storage-based |
| **KeePass** | Free, local | Desktop-only, no Sheets integration, steep learning curve |
| **Google Password Manager** | Free, built-in | Auto-fill only, no recipe system, no Sheets integration |
| **No competitor** | - | Nobody does recipe-based password generation from Google Sheets |

### Our Unfair Advantages
1. **Zero-storage architecture** — fundamentally different security model
2. **Google Sheets as platform** — users keep their existing workflow
3. **Recipe metaphor** — memorable, explainable, marketable
4. **No server costs** — all computation client-side, no infrastructure to maintain
5. **Bilingual (EN/VI)** — underserved Vietnamese market

---

## 10. Risks and Mitigations

| Risk | Likelihood | Impact | Mitigation |
|------|:---:|:---:|------------|
| Google Sheets DOM changes break extension | High | High | E2E tests catch regressions; multiple detection strategies |
| Chrome Web Store rejection | Medium | High | Follow all CWS policies strictly; privacy policy ready |
| Low adoption — users don't understand recipe concept | Medium | High | Demo video, onboarding tutorial, simple landing page explainer |
| Security incident (vulnerability discovered) | Low | Critical | Responsible disclosure policy; quick patch process; transparent communication |
| Competitor copies the concept | Low | Medium | Brand + community + first-mover advantage |
| Google changes storage API / Manifest V3 changes | Low | Medium | Monitor Chrome platform blog; maintain test coverage |
| Payment integration complexity | Medium | Low | Use managed service (LemonSqueezy); start with lifetime license (simplest) |

---

## 11. Open Decisions

| Decision | Options | Status |
|----------|---------|--------|
| Product name | PassChef / SpiceKey — SheetVault eliminated (see naming doc) | **DECIDED: PassChef** |
| Brand tone | Consumer/approachable (not enterprise/corporate) | **Decided** |
| Google Sheets in name? | No — subtitle/description only, not in title | **Decided** |
| Recipe mechanism as core identity? | Yes — cooking metaphor is durable, defines the product | **Decided** |
| Privacy Policy + ToS | — | **Done** (2026-06-13) |
| CWS listing copy | — | **Done** (2026-06-13) |
| Open-source model | MIT / Source-available / Closed | **DECIDED: Closed source** |
| Revenue model | Lifetime license — launch $9 → raise to $14-19 after 500+ installs / 20+ reviews | **DECIDED** (2026-06-13) |
| Free tier feature split | Option A2 — 2 profiles free, gate at 3+ + routing + sharing + configurable timeout | **DECIDED: A2** (2026-06-13) |
| Analytics tool | None / Plausible / Umami (landing page only) | **USER DECISION NEEDED** |
| Support channel | GitHub Issues / Email / Discord | **USER DECISION NEEDED** |
| Demo video | Screen recording / Animated explainer | **USER DECISION NEEDED** |

---

## 12. Budget Estimate (Minimal Launch)

| Item | Cost | Notes |
|------|------|-------|
| Chrome Web Store developer account | $5 (one-time) | Required to publish |
| Domain name | $10-15/year | Depends on chosen name |
| LemonSqueezy/Stripe | 5% + $0.50 per transaction | Only when revenue starts |
| Landing page hosting | Free | GitHub Pages / Cloudflare Pages |
| Analytics (Plausible) | $9/month or self-host free | Optional, landing page only |
| **Total to launch** | **~$20** | Extension + domain only |

## 13. Relevance and next work — audited 2026-10-11

| Priority | Remaining work | Evidence / boundary |
|---|---|---|
| 1 | Complete Phase0 release readiness, then actual Phase1 publication | Listing copy [exists](marketing/cws-listing-copy.md); legal files exist. Account/domain/support completion is not established; asset prompts are not screenshots. Keep original user actions open. |
| 2 | Finish launch-facing landing content/assets and QA | [Landing task](../landing-page-improve.md): real CWS/OG assets, actual install URL, responsive/performance evidence and testimonial provenance pending. |
| 3 | Execute Phase2 promotion after release; implement Phase3 monetization as separate work | Preserve decided A2/lifetime model; no external publication, spending or provider choice occurs in this audit. |
| 4 | Phase4 optional growth work after feedback | No requirement to implement every speculative idea before finishing an earlier phase. |

Pre-launch content mismatch: this plan records **Closed source** as decided, while
`landing-page/index.html`:1335/1434 currently claims MIT/open source. Section3's
source-available recommendation predates the settled Section11 decision; it is
superseded as a recommendation. Resolve public wording before release without
inventing a new licensing decision. Existing testimonial cards have no verified
provenance in this audit; UI presence is not customer-validation evidence.

This remains an active Partial plan. Phase0–4 are not wholly Done; do not archive
the entire launch plan because development foundation or copy writing is done.
No publishing/account check, browser QA, performance measurement or product test
was performed for this documentation-only status audit.
