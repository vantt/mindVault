# A08 — Independent capture/asset brief review

Date:2026-10-26, Asia/Saigon. Reviewer:independent agent, not brief author.
Source:HEAD039c76d + preserved WIP; manifest2.3.0, stable A13 candidate Pending.
Scope:[A08 delivery](a08-assets-brief.md) and corrected screenshot/asset brief.
Verdict:**Passed for brief/inventory preparation**; no unresolved review finding.

## Review

Five proposed real shots map to current Generated, popup Builder, Options profiles,
Full Access wizard and Tag-mismatch Home. Current Generated Dish is visible readonly
text; no invented masking/eye/countdown/sidebar. Brief requires actual candidate
pixels, synthetic data/Tag context, source/locale/raw/edit records, rights and
cleanup. Existing images are not substituted for final captures. Full Access and
Tag/legacy limits remain explicit; artwork illustrations remain separate.

Independently reopened official [Chrome images](https://developer.chrome.com/docs/webstore/images)
and [listing graphics](https://developer.chrome.com/docs/webstore/cws-dashboard-listing)
guidance2026-10-26:128px icon, square artwork/padding guidance,1–5screenshots at
1280×800 or640×400,optional440×280small and1400×560marquee. Brief correctly separates
these from project PNG/OG choices and still-unverified Dashboard byte constraints.

## Independent checks executed

Node24.18.0, existing jsdom/parser/Cooking; no scripts/network resources executed.

| Check | Result |
|---|---|
| Brief selectors |27ID selectors + actual Profiles tab selector resolve in HTML;6export IDs resolve in source templates. |
| Generated field |Readonly `type=text` confirmed. |
| Five PNG signatures/IHDR/type/size/digest |All match report;16/32/48/128px icons plus `icon-1024.png` actually680×680. |
| Favicon SVG |Parses;viewBox0 0 32 32. |
| OG path |Index meta points at `./assets/og-image.png`;file missing. |
| Three synthetic Recipes |All parse;`r4nd0m#1` + `Basic*` cooks `Basic*r4nd0m`. |
| Brief/report local links |7/7 resolve;proposed capture names are not nonexistent evidence links. |
| `git diff --exit-code -- chrome-extension/test chrome-extension/src/manifest.json chrome-extension/src/assets landing-page/assets` |exit0. |
| `git diff --check` |exit0. |

Initial independent OG literal probe omitted the source's `./` prefix;DOM check
corrected the assumption and confirmed the missing-file finding without edits.
No image decoding/visual/padding/rights approval, unit-suite replay, browser capture,
artwork creation, deployment or Store acceptance claimed.

## Handoff and unresolved

A08 can be Done; next A09. B06's A08 prerequisite is satisfied, but stable A13,
C01/C02,desktop slot/verified isolation and actual captures remain Pending.
Artwork provenance/rights, C04 locales, C05 OG/live URLs and final graphic approval
remain open; file presence/dimensions do not establish those outcomes.
