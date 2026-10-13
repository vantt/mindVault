# Commercialization Plan

> **Status:** Partial — preparation can proceed; release waits for bug fixes and completed UI
> **Last updated:** 2026-10-13 (Phase 0 reordered around pending bugs/UI; historical estimates retained)
> **Product:** PassChef Password Generator (name decided — see [naming-decision.md](marketing/naming-decision.md))
> **Current version:** v2.3 (Full Access Sharing)
> **Documentation index:** [Commercialization hub](README.md)

---

## 1. Current Product State

### What Exists
| Area | Status | Detail |
|------|--------|--------|
| Core extension | Historically accepted bounded scope; current release blocked | Recipe kitchen, Argon2id + AES-256-GCM, authentication/session/clipboard and real Google Menu acceptance passed in the recorded scope. Owner reports remaining bugs/unfinished UI; release gate plus Sync/platform/performance checks remain. |
| Multi-profile | Implemented; accepted bounded UI scope | Named profiles, per-Menu routing and guarded profile lifecycle exist; final release artifact acceptance remains. |
| Sharing | Implemented | Full Access bundle (v2.3) and QR sender exist; document recipient access to Spices and verify sharing in the release artifact. |
| Verification tag | Done — accepted scope | Recipe/profile/Menu mismatch refusal, legacy warnings and selected EN/VI UI passed; Tag does not change the Dish or guarantee protection against all tampering. |
| i18n | Accepted selected EN/VI scope | Tag/popup UI and offline Demo have acceptance evidence; final listing locales and release-wide wording review remain. |
| Tests | Source verification exists; release artifact unverified | Independent recheck: 147/147 unit and 25/25 headed E2E passed 2026-10-12; separate real Google/native proof 8/8 retained. No fresh product tests in this checklist update. |
| Landing page | Partial launch readiness | Recipe demos/FAQ/testimonial UI/responsive implementation exist; assets, content alignment and QA remain |
| Chrome Web Store | **Publication unverified / pending** | Listing copy + privacy/terms files exist; screenshot assets and actual listing URL not established in repo |

### What's Missing for Commercial Launch
| Area | Priority | Effort |
|------|----------|--------|
| Privacy Policy + Terms of Service | Draft files exist; permissions/storage/clipboard/licensing corrections required | Review required |
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
| **Licensing wording alignment** | Apply decided Closed source model consistently in public materials | Blocker |
| **"How it works" explainer** | Demystify the recipe concept for non-technical users | High |
| **No-telemetry badge** | "We don't track you" — powerful for privacy audience | Medium |
| **Third-party audit** | Expensive but highest trust signal. Consider after traction | Later |

### Open Source — Historical Options; Closed Source Decided

| Option | Trust | Moat | Revenue Impact |
|--------|:---:|:---:|:---:|
| Fully open-source (MIT/Apache) | Highest | Lowest | Harder to charge, but community contribution |
| Source-available (BSL/SSPL) | High | Medium | Can still monetize, code is inspectable |
| Closed source | Lowest | Highest | Traditional SaaS model |

**Current decision:** Closed source (2026-06-13; Section 11). The options above are
historical context, not an open decision. Align listing, landing, Privacy and Terms;
preserve required notices for shipped third-party libraries.

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

**Status: Partial.** Name/rebrand and initial legal/listing drafts exist. The
2026-10-13 checklist incorporates the audit and owner-reported bugs/unfinished UI; adding a task
does not complete it. Historic timing/domain/pricing/fee estimates are not current
availability or price checks. Scope: browser extension and launch-facing website.

Source baseline: [independent extension recheck](../../plans/reports/261012-extension-recheck.md)
(147 unit + 25 headed E2E passed 2026-10-12), [completion verification](../../archive/plans/261012-extension-completion/reports/verification.md)
(separate 8/8 actual Google/native proof), [browser security contract](../specs/extension-security.md).
These establish historical acceptance for the tested slices. The owner reports
remaining bugs and unfinished UI; final screenshots/ZIP/acceptance/publication
wait for the product readiness gate below.

Existing preparation:

- [x] Finalize product name → **PassChef** (2026-05-25, unanimous council decision)
- [x] Product rebrand completed 2026-05-25; legacy CI artifact name remains a packaging task below.
- [x] Initial Privacy Policy + Terms drafts written 2026-06-13: [Privacy](../../landing-page/privacy.html), [Terms](../../landing-page/terms.html). Pre-publication corrections remain.
- [x] Initial [Chrome Web Store listing draft](marketing/cws-listing-copy.md) written 2026-06-13. Corrections remain; not approved submission copy.
- [x] Manifest updated to 2.3.0 with revised description 2026-06-13. Release candidate version remains to be confirmed.

#### 0.1 Listing and product claims — prepare now

Draft and correct wording now. Feature/support promises and approved submission copy
remain subject to product readiness and final review in 0.7.

Files: [listing copy](marketing/cws-listing-copy.md), [value statements](marketing/product-value-statements.md),
[README](../../README.md), [Tag rationale](../recipe-tag-design-rationale.md), landing and public security explainer.

- [ ] Replace invalid `site+length32+symbols` example and site/length/rules instructions with real Recipe syntax such as `r4nd0m#1`.
- [ ] Explain Recipe + Spices cooking separately from Argon2id key derivation/AES-GCM Cookbook protection. Do not claim Argon2id cooks the Dish.
- [ ] Correct local-only/never-uploaded/never-synced/no-external-server claims to match encrypted Chrome Sync and Google Menu usage.
- [ ] Describe Tag mismatch detection and legacy behavior accurately; Tag does not change the Dish or guarantee protection against all Recipe tampering/tag removal.
- [ ] Describe Full Access sharing accurately; do not imply the recipient receives only Recipes or cannot cook account Dishes.
- [ ] Remove/qualify absolute claims such as `Hacker Proof`, `Unbreakable`, `Nothing to breach`, `Nothing lingers` and universally harmless public Menus. Explain encrypted Cookbook, metadata, weak-password, malware and clipboard boundaries.
- [ ] Advertise only current implemented features and accepted EN/VI coverage; configurable timeout/Free-Pro restrictions are future commercial work, not assumed current behavior.
- [ ] Replace stale 55-unit/6-E2E counts with release-linked evidence or omit counts. Never present historical source tests as final artifact verification.
- [ ] Reconcile title/manifest name, short/full descriptions, category, shortcut including Mac, feature list and support URLs; verify current field limits.
- [ ] Confirm listing languages and prepare approved EN/VI copy if bilingual Store distribution is selected.

Deliverable now: corrected listing draft; final submission copy waits for the release gate.

#### 0.2 Permissions, Privacy and Terms — prepare now

Audit current code and prepare accurate drafts now. Permission removal needs its own
browser verification; freeze declarations only against the final manifest in 0.7.

Files: [Privacy](../../landing-page/privacy.html), [Terms](../../landing-page/terms.html),
[security contract](../specs/extension-security.md), [permissions review](legal/permissions-review.md), manifest and Store Privacy fields.

The current permission/host matrix, code evidence and pending Store declarations
are maintained in [permissions review](legal/permissions-review.md). Revalidate
that record after implementation/manifest changes; this plan tracks the work below.

- [ ] Audit minimum permissions, particularly `activeTab`/`tabs`; verify reduced-permission Menu detection, picker, shortcut and lock flows before removal.
- [ ] Write separate justifications for every retained permission and host grant; align manifest, public permission explanations and Store declarations.
- [ ] Describe Sync/local/session storage and data types. Replace claims that all data is exclusively in Sync or that no plaintext metadata is stored.
- [ ] Explain encrypted Spices/configuration can sync via Google infrastructure; distinguish this from plaintext Spices transmission or developer-operated backend/telemetry.
- [ ] Describe selected Recipe processing through the Menu DOM; do not invent Google Sheets API requests in extension listing copy.
- [ ] Document export/import and Full Access/QR transfer, including recipient access to Spices and account Dishes.
- [ ] Verify reset/uninstall/Chrome Sync retention and deletion behavior; current reset does not clear every local record. Fix claims of complete deletion or guaranteed survival after uninstall.
- [ ] Align clipboard text with current contract: cleanup attempts after 30s, exact-match-only clearing, no write after read denial; Chrome sleep/closure and OS history can outlive cleanup. Compare/write is not atomic across apps.
- [ ] Align landing/listing/Privacy/Terms with decided Closed source licensing; remove contradictory MIT/open-source promises without removing third-party notices.
- [ ] Review GDPR/CCPA wording, maintainer/contact details, website hosting/third-party requests, update notices and canonical legal URLs against actual data flow.
- [ ] Prepare Store single-purpose description, permission justification, data-use/limited-use declarations and remote-code answer. Verify bundled JS/WASM before declaring no remote code; zero telemetry alone does not establish no user-data handling.

Deliverable now: permission matrix, corrected legal drafts and draft Store Privacy fields; revalidate after code/permission changes.

#### 0.3 Publisher, public URLs and support — prepare now

Decision/evidence record: [publisher and support](operations/publisher-and-support.md).

Owner decisions and account/hosting preparation can proceed while product bugs/UI
are being fixed; this checklist does not execute purchases or external publication.

- [ ] **[USER ACTION]** Confirm/create Chrome Web Store developer account; verify registration fee at checkout rather than relying on historical $5 estimate.
- [ ] **[USER ACTION]** Set publisher identity, verify contact email and complete applicable account requirements; retain operational account-readiness confirmation.
- [ ] **[USER DECISION]** Choose domain/hosting and real HTTPS URLs for landing, Privacy, Terms and support. A custom domain is optional; reachable policy/support URLs are required for readiness.
- [ ] **[USER ACTION, IF CUSTOM DOMAIN]** Verify/register selected domain and configure hosting. Historical `passchef.com` recommendation does not prove availability; replace unverified `passchef.io/privacy` reference.
- [ ] **[USER DECISION]** Confirm support channel (email/GitHub Issues/Discord) and private security-disclosure route. Existing legal-page email is not proof of operational support setup.
- [ ] Verify testimonial provenance/consent and adoption claims such as `Join thousands`; remove unsupported social proof.
- [ ] Fix placeholder/general links and copyright/license text. Prepare install CTA replacement; connect to actual PassChef listing in Phase 1 when its URL is available.
- [ ] Prepare Store distribution choices (free soft launch, visibility, countries/languages) and reviewer material. Actual submission/publication remains Phase 1.

Deliverable now: publisher/hosting/support decisions and public-URL preparation. Final submission packet follows release acceptance.

#### 0.4 Brand assets and documentation — prepare now

Existing [screenshot prompts](marketing/cws-screenshot-prompts.md) are briefs, not
captures. Their invented countdown/sidebar/in-Menu Builder UI must not substitute
for actual release-candidate experience. Prepare briefs, captions and assets without
product UI now; capture real UI only after the release gate in 0.6.

| Asset | Requirement / acceptance |
|---|---|
| Store icon 128×128 PNG | Existing 16/32/48/128 files; check padding, clarity and light/dark backgrounds. |
| Screenshots 1280×800 | At least 1 required; target 5 actual captures. 640×400 also supported. |
| Small promo 440×280 | Required; communicate PassChef brand and real value. |
| Marquee 1400×560 | Optional; prepare only if desired for featured promotion. |
| Large promo 920×680 | Historical brief; verify actual dashboard support before making this required. |
| Website OG image | Fix missing `landing-page/assets/og-image.png`; use actual absolute public URL when hosting is set. |

- [ ] Confirm five-shot story: Google Menu + shortcut cooking; masked popup Reveal/Copy; profiles/Menu routing; actual popup Recipe Builder with Tag; Full Access sharing or accurate security explainer.
- [ ] Add concise truthful captions/callouts without depicting unsupported UI; align existing cyberpunk prompts with current design.
- [ ] Prepare icon checks, small promo and OG artwork without unstable UI; optional marquee can also be designed now. Keep final public URLs/claims pending confirmation.
- [ ] Draft release-note template and known-limitations structure now; fill version/changes/evidence only after the final candidate is verified.
- [ ] Update user guidance: setup, valid Recipes, manual cooking, safe backup/restore, Full Access sharing/recipient responsibilities, password rotation and loss of master password/Spices.
- [ ] Prepare Store reviewer instructions with synthetic Menu/Recipe fixture, expected flows and any required test access; include no personal production credentials.
- [ ] Align public security explainer with current browser contract rather than historical recommendations; include threat boundaries and disclosure route.

Deliverable now: non-UI brand artwork, screenshot brief/captions and documentation drafts. Actual captures/final assets wait for 0.6.

#### 0.5 Packaging tooling — prepare now

Prepare scripts, CI gates and dependency/license checks now. Any interim ZIP is a
development artifact; final version/commit and release ZIP wait for 0.6.

Files: [manifest](../../chrome-extension/src/manifest.json), [package](../../chrome-extension/package.json),
[CI workflow](../../.github/workflows/build_and_deploy.yml), [WASM copy script](../../chrome-extension/scripts/copy_wasm.js).

- [ ] Rename CI ZIP/upload artifact from `mindvault-extension` to PassChef with release version; define release trigger and artifact retention.
- [ ] Provide repeatable package/validate commands using `pnpm install --frozen-lockfile`; record the Node/pnpm runtime used.
- [ ] Make assembly fail when mandatory `argon2.js`/`argon2.wasm` are missing; current copy script only warns. Verify intended bundled dependency bytes.
- [ ] Validate JS/JSON, manifest, imports and resource paths: WASM, module Worker/offscreen pages, locales, icons and Demo resources.
- [ ] Inventory shipped files; remove obsolete/unreferenced content only after dependency review. Exclude tests, development/debug payloads and private data.
- [ ] Inventory shipped third-party JS/WASM and include required copyright/license notices; confirm rights to branded assets.
- [ ] Add verification before release ZIP generation in CI; existing workflow installs/zips/uploads without tests or validation.

Deliverable now: repeatable packaging/validation tooling and CI preparation; no release-readiness claim.

#### Product readiness gate — before 0.6–0.7 and Phase 1

Owner reports 2026-10-13: product still has bugs and unfinished UI; release is not
ready. Earlier passing tests remain historical evidence for their bounded scope,
not proof that the current app/UI is ready to ship. This update records sequencing,
not a new bug diagnosis or a completed repair.

- [ ] Record release-blocking bugs and unfinished UI flows with clear acceptance criteria; retain current product issue tracking as the implementation source.
- [ ] Resolve those blockers and complete the intended UI; verify relevant regressions and affected flows before release captures/acceptance.
- [ ] Confirm the capture/release candidate has stable UI and no unresolved release-blocking bugs. Reopen this gate if later changes invalidate that baseline.

#### 0.6 Final candidate, ZIP and real screenshots — after release gate

- [ ] Confirm release version/commit and reconcile manifest 2.3.0 versus package 1.0.0. Root `release-manifest.json` is an old toolkit inventory, not the extension release record.
- [ ] Produce ZIP with `manifest.json` at its root; retain artifact name/version/commit/SHA-256/content inventory.
- [ ] Capture release candidate with synthetic accounts/Recipes/Spices; hide personal URLs, account identifiers and private data.
- [ ] Export assets at required sizes; check legibility when downscaled, file format, rights and final listing order. Prepare locale-specific screenshots if selected.
- [ ] Retain editable sources and finalized asset inventory; link actual files from listing brief once produced.

Deliverable: final candidate/version/commit, release ZIP/provenance and real UI
assets matching that candidate. New code/UI changes require updating affected
captures and artifact evidence.

#### 0.7 Artifact acceptance and submission finalization — after release gate

Run final acceptance against the candidate from 0.6; draft documents from 0.1–0.4
are inputs and must be reconciled with the verified artifact.

- [ ] Run `pnpm test` from `chrome-extension/` on the final candidate in a suitable browser environment; keep Golden tests unchanged and retain commands/results/commit evidence.
- [ ] Extract final ZIP into a clean environment and load it; verify all runtime resources and fail-closed behavior when derivation is unavailable.
- [ ] Verify setup/unlock, real Menu/native shortcut, Builder, matching Tag success and mismatched Tag refusal, Reveal/Copy/cleanup, session expiry/worker restart, profiles/routing, export/import/QR and selected EN/VI flows in the release artifact.
- [ ] Verify upgrade with existing encrypted Cookbook data: compatible unlock and preserved Spices/profiles/Menu mappings; document any actual migration requirements.
- [ ] Confirm supported Chrome/OS matrix. Manifest minimum 116 is not acceptance proof; existing browser evidence ran on Chromium 143.
- [ ] Close required cross-device Chrome Sync/conflict, KDF timing/memory/performance and platform/provider checks, or explicitly record deferral, known limitations and accurate release claims. Full MVP stays Partial while broader outcomes remain open.
- [ ] Write release notes with version, features/fixes, changed permissions, compatibility, known limitations and links to artifact evidence.
- [ ] Update this plan/roadmap/changelog on actual delivery with evidence. Do not reopen accepted Builder/Tag/Popup work or mark release Done merely because a draft or checklist exists.
- [ ] Run launch-facing landing/legal QA: mobile/responsive, keyboard/reduced-motion, links, assets, Recipe explanation and performance.
- [ ] Reconcile final manifest/permissions, feature claims, EN/VI copy, legal pages, reviewer instructions, asset inventory and public URLs with the verified candidate before submission.

Deliverable: artifact acceptance report, release notes, user/reviewer guidance and known limitations.

#### Phase 0 order, exit criteria and boundaries

Current order: listing corrections/drafts (0.1); permissions/legal drafts (0.2);
publisher/support/URL preparation (0.3); non-UI assets and documentation (0.4);
packaging tooling (0.5). These can proceed alongside product bug/UI work without
waiting for a release candidate. Recheck drafts whenever implementation changes.

Final candidate/version/ZIP and real screenshots (0.6), artifact acceptance and final
submission packet (0.7), then Phase 1 submission/publication must wait for the product
readiness gate. Do not spend effort capturing unstable UI or declare the app ready
based only on historical suite results. Final landing QA waits for the chosen UI,
assets and URLs; content/link cleanup can happen earlier.

Phase 0 Done requires the product readiness gate passed, verified ZIP/provenance,
accurate copy/declarations, required assets, working legal/support URLs, reviewer
instructions and documented release limits. Owner dependencies remain open until
evidenced. Phase 1 owns submission/review/publication, Store-install acceptance and
real CTA connection. Payment, Free/Pro gating, campaigns, video, analytics, broad
website redesign and third-party audit remain later/optional work.

Reference requirements checked 2026-10-13: [package preparation](https://developer.chrome.com/docs/webstore/prepare),
[Privacy fields](https://developer.chrome.com/docs/webstore/cws-dashboard-privacy),
[images](https://developer.chrome.com/docs/webstore/images), [listing policies](https://developer.chrome.com/docs/webstore/program-policies/policies),
[publisher setup](https://developer.chrome.com/docs/webstore/set-up-account), [submission](https://developer.chrome.com/docs/webstore/publish).
Recheck requirements against the actual dashboard before submission.

### Phase 1: Soft Launch — Free (week 3-4)

**Status: Pending release; CTA implementation Partial.** Existing CTA links to
the generic Chrome store (`landing-page/index.html`:1355), not a verified PassChef
listing. No actual publication/install/review result is established by this audit.
- [ ] Submit completed Phase 0 packet, track review outcome and resolve any findings; verify installation from the published Store artifact before declaring release accepted.
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
explore/localization ideas are not approved commitments or blockers for Phase 0/1.
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
| **Privacy Policy** | CWS requirement + applicable privacy obligations | Explain handled data, permissions, Sync/local/session storage, Google infrastructure, sharing, retention/deletion and no developer-operated telemetry. Draft corrections remain in Phase 0. |
| **Terms of Service** | Legal protection | Liability limitation, no warranty on password security, user responsibility for master password |

### Key Privacy Claims (must be truthful)
- No developer-operated telemetry/backend in the current extension; encrypted Chrome Sync uses Google infrastructure and user-initiated sharing can transfer Spices.
- Cookbook ciphertext lives in Sync; plaintext metadata/local state and session key/deadline have separate storage/lifetime contracts.
- Extension cannot recover a lost master password; document backup/restore and lost-Spices consequences.
- Clipboard cleanup attempts after 30s and clears only an exact owned-Dish match; OS history/sleep/read denial limits remain.
- Final public statements and Store declarations must match the retained permissions and verified release artifact (Phase 0.2/0.7).

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
| Privacy Policy + ToS | — | **Drafts written** 2026-06-13; pre-publication corrections Pending (Phase 0.2) |
| CWS listing copy | — | **Draft written** 2026-06-13; accuracy/assets review Pending (Phase 0.1/0.4/0.6–0.7) |
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

## 13. Relevance and next work — reconciled 2026-10-13

| Priority | Remaining work | Evidence / boundary |
|---|---|---|
| 1 | Execute preparation 0.1–0.5 alongside bug/UI fixes; then 0.6–0.7 and Phase 1 | Historical source tests retained. Current product readiness gate is closed; final screenshots, ZIP acceptance and publication wait for fixed blockers/stable UI. |
| 2 | Finish launch-facing landing content/assets and QA | [Landing task](../../landing-page-improve.md): real CWS/OG assets, actual install URL, responsive/performance evidence and testimonial provenance pending. |
| 3 | Execute Phase 2 promotion after release; implement Phase 3 monetization as separate work | Preserve decided A2/lifetime model; no external publication, spending or provider choice occurs in this audit. |
| 4 | Phase 4 optional growth work after feedback | No requirement to implement every speculative idea before finishing an earlier phase. |

Pre-launch content mismatch: **Closed source** is decided, while landing, Privacy
and Terms still claim MIT/open source. Apply that decision consistently before
release. Existing testimonial cards have no verified provenance here; UI presence
is not customer-validation evidence. Historical value/competitor/security phrasing
elsewhere in this plan is subject to Phase 0.1 review, not approved public copy.

This remains an active Partial plan. Phase 0–4 are not wholly Done; do not archive
the entire launch plan because development foundation or copy writing is done.
No publishing/account check, browser QA, performance measurement or product test
was performed for this documentation-only checklist update. Existing source
verification is linked separately; broader MVP acceptance remains Partial.

Unresolved decisions/dependencies: release-blocking bug/UI acceptance and stable candidate;
release version/channel and Chrome/OS support;
publisher-account readiness; domain/hosting and support/disclosure channel;
permission minimization outcome (`activeTab`/`tabs`); whether cross-device Sync
acceptance blocks the first release or is an explicitly disclosed deferred limit.
