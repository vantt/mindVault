# Commercialization Plan

> **Status:** Draft
> **Last updated:** 2026-05-25
> **Product:** mindVault Password Generator (name pending — see [naming-decision.md](./naming-decision.md))
> **Current version:** v2.3 (Full Access Sharing)

---

## 1. Current Product State

### What Exists
| Area | Status | Detail |
|------|--------|--------|
| Core extension | Done | Recipe parsing, password generation, Argon2id + AES-256-GCM |
| Multi-profile | Done | Named profiles, per-sheet routing, export/import |
| Sharing | Done | Full Access bundle (v2.3) |
| Verification tag | Done | Recipe integrity validation (v2.2) |
| i18n | Done | English + Vietnamese |
| Tests | Done | 55 unit + 6 E2E (Vitest + Playwright) |
| Landing page | Exists | Basic, needs conversion optimization |
| Chrome Web Store | **Not published** | No listing, no screenshots, no privacy policy |

### What's Missing for Commercial Launch
| Area | Priority | Effort |
|------|----------|--------|
| Privacy Policy + Terms of Service | **Blocker** | Low |
| Chrome Web Store listing (screenshots, description, promo images) | **Blocker** | Medium |
| Brand name finalization | **Blocker** | In progress (see naming doc) |
| Landing page upgrade (conversion-focused) | High | Medium |
| Analytics (privacy-respecting) | High | Low |
| Payment integration (if freemium) | Medium | Medium |
| Demo page / onboarding video | Medium | Medium |
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

### Recommended: Freemium

Free tier covers core use case. Paid tier unlocks power features.

| Feature | Free | Pro |
|---------|:---:|:---:|
| Password generation from recipes | Yes | Yes |
| Up to 1 profile, 5 secrets | Yes | Yes |
| Recipe verification tags | Yes | Yes |
| Hotkey support (Ctrl+Shift+L) | Yes | Yes |
| Auto-lock, clipboard clear | Yes | Yes |
| **Multiple profiles** | 1 | **Unlimited** |
| **Profile export/import (sharing)** | - | **Yes** |
| **Per-sheet profile routing** | - | **Yes** |
| **Priority support** | - | **Yes** |

### Pricing Options

| Model | Price | Pros | Cons |
|-------|-------|------|------|
| **Monthly subscription** | $3/month | Recurring revenue, lower barrier | Subscription fatigue |
| **Annual subscription** | $29/year | Better retention, perceived value | Higher upfront commitment |
| **Lifetime license** | $49 one-time | Users love it, simple | No recurring revenue |
| **Pay-what-you-want** | $5+ suggested | Low barrier, good will | Unpredictable revenue |

**Recommendation for launch:** Start with **lifetime license ($29-49)** to maximize early adoption. Switch to subscription later when user base grows and features justify ongoing payment.

### Payment Infrastructure
- **Stripe** or **LemonSqueezy** (handles taxes, invoicing, Chrome extension licensing)
- License key validation via Chrome extension — check on install, periodic re-validation
- Grace period on validation failure (offline users)

---

## 6. Launch Sequence — Phased Approach

### Phase 0: Pre-launch (1-2 weeks)
- [ ] Finalize product name (see [naming-decision.md](./naming-decision.md))
- [ ] Execute rebrand across codebase (manifest, locales, README, landing page, docs)
- [ ] Write Privacy Policy + Terms of Service
- [ ] Create Chrome Web Store developer account ($5 one-time fee)
- [ ] Prepare screenshots and promo images (440x280, 920x680, 1400x560)
- [ ] Write Chrome Web Store listing copy
- [ ] Set up support channel (GitHub Issues or simple email)

### Phase 1: Soft Launch — Free (week 3-4)
- [ ] Publish to Chrome Web Store (free, all features unlocked)
- [ ] Update landing page with install CTA
- [ ] Share in Vietnamese tech communities (personal network)
- [ ] Collect feedback, fix bugs, iterate
- [ ] **Goal:** 50-100 installs, 5+ reviews

### Phase 2: Public Launch (week 5-6)
- [ ] Product Hunt launch
- [ ] Reddit posts (genuine, valuable content — not ads)
- [ ] YouTube tutorial video
- [ ] SEO-optimized blog post on landing page
- [ ] **Goal:** 500+ installs, 4.5+ star rating

### Phase 3: Monetization (week 8+)
- [ ] Implement free/pro tier split
- [ ] Integrate payment (LemonSqueezy or Stripe)
- [ ] License key system in extension
- [ ] Upgrade landing page with pricing section
- [ ] **Goal:** First paying customers

### Phase 4: Growth (ongoing)
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
| Product name | PassChef / SpiceKey — SheetVault eliminated (see naming doc) | In progress |
| Brand tone | Consumer/approachable (not enterprise/corporate) | **Decided** |
| Google Sheets in name? | No — subtitle/description only, not in title | **Decided** |
| Recipe mechanism as core identity? | Yes — cooking metaphor is durable, defines the product | **Decided** |
| Open-source model | MIT / Source-available / Closed | Not started |
| Revenue model | Lifetime / Subscription / Pay-what-you-want | Leaning lifetime |
| Free tier feature split | Which features to gate? | Not started |
| Analytics tool | None / Plausible / Umami (landing page only) | Not started |
| Support channel | GitHub Issues / Email / Discord | Not started |
| Demo video | Screen recording / Animated explainer | Not started |

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
