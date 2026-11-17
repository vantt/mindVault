# V01 — local verification consolidation

Date:2026-11-02. **V01 local consolidation Done;[verification Passed](verification-preparation-checks.md),[independent review Passed](verification-preparation-independent-review.md).** Per-slice source/build/package/preparation evidence Passed as bounded below. Current design runtime/phone/owner acceptance UNEXECUTED/Pending;A21/OS isolation UNVERIFIED. Deployment remains owner-held. No suite replay,product/helper/manifest edits or new resource request in V01.

## Evidence matrix

|Slice|Latest actual local evidence|Review / practical limit|
|---|---|---|
|D01|[Source baseline audit](design-baseline-audit.md):actual web/native/extension mismatch inventory;Precision adopted baseline.|[Independent review](design-baseline-independent-review.md) Passed;no visual approval.|
|D02|[Contract/fonts](design-contract-fonts.md):pinned original fonts/OFLs,metadata/cmap/axes and34opaque normal-text pairs.|[Review](design-contract-independent-review.md) Passed;boards/cmap are not shaping/render proof.|
|W01|[Foundation](website-foundation.md):PostCSS/local fonts/HTML body+JS/geometry preservation;final349local documentation links checked.|[Review](website-foundation-independent-review.md) Passed;later W02/W03/E02source supersedes its hashes.|
|W02|[Composition verification](website-composition-verification.md):final03tester7/7;594rules/zero warnings,30geometry nodes/31memberships across20selectors;44px button/social fixes.|[Review](website-composition-independent-review.md) Passed. [Privacy alignment](website-privacy-alignment.md)/[review](website-privacy-alignment-independent-review.md) Passed;E02 later updates current extension-font disclosure.|
|W03|[Asset/source verification](website-brand-assets-verification.md):six original-mark wordmarks,597CSSrules/zero warnings,1200×630PNG/original bytes/font outlines/local references.|[Review](website-brand-assets-independent-review.md) Passed;root actual PNG inspection found no clipping,asset-only—not browser acceptance.|
|M01|[Theme verification](android-theme-verification.md):finalrootrun03 four Gradle tasks Passed,10JVMtests/0failure/error/skip;lint0errors,3existing warnings+1hint.|[Review](android-theme-independent-review.md) Passed;API27resource/lint failure and intermediate warning history retained. No instrumentation.|
|M02|[Locked-form verification](android-locked-form-verification.md):rootrun01 four tasks Passed;10JVMtests/0failure/error/skip;110source identities stable.|[Review](android-locked-form-independent-review.md) Passed;actual auth/biometric/theme visuals Pending.|
|M03|[Cookbook verification](android-cookbook-journeys-verification.md):finalrootrun02 four tasks Passed;10JVMtests/0failure/error/skip;113sources stable.|[Review](android-cookbook-journeys-independent-review.md) Passed;state/callback/scroll/test-tag preservation,not device acceptance.|
|M04|[Sources/mapping verification](android-sources-mapping-verification.md):rootrun01 four tasks Passed;10JVMtests/0failure/error/skip;113sources/two retained APKs exact.|[Review](android-sources-mapping-independent-review.md) Passed;latest app candidate for all M01–M04.|
|E01|[Coherence verification](extension-coherence-verification.md):real font/role/resource/shape gaps,actual CSS subjects and opacity-free source contrast.|[Review](extension-coherence-independent-review.md) Passed;compact26/30px baseline/other documented exceptions retained.|
|E02|[Font/package verification](extension-font-package-verification.md):final151unit/67package tests Passed;83inventory/zero remote styling/new exact reproducible ZIP.|[Review](extension-font-package-independent-review.md) Passed;canonical standalone Demo and JSON-tamper fixture corrections retained;old46tests unchanged.|
|E03|[Final02 verification](extension-role-alignment-verification.md):`pnpm validate`,151/151unit and67/67package Passed;518CSSrules/zero warnings;finalc11e83ZIP/extract/reproducibility verified.|[Review](extension-role-alignment-independent-review.md) Passed;all source roles/shapes/geometry/Golden preserved;no rendered WCAG/flow certification.|
|E04|[Preparation verification](ui-qa-preparation-verification.md):author36/36 and independent tester32/32pure/mocked groups;397binding/17helper read-only preflight Passed.|[Revision04 review](extension-ui-qa-preparation-independent-review.md) Passed;[packet](extension-ui-qa-preparation.md) is inspectable preparation,all runtime unexecuted.|

Actual Android commands for M01–M04:`:app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug` under root JDK17/SDK36,maxworkers2. Last M04rootrun01 retains the current113sources;assembly is not device instrumentation. Extension actual commands:`pnpm validate`,`pnpm test:unit --maxWorkers=2`,`pnpm test:package`;final E03run02 receipts are current. Node/PostCSS/asset checks are static;no headed/default `pnpm test` rerun. Existing deeper test logs/results/commands remain linked above instead of replaying suites.

## Current source/artifact freeze

|Artifact / scope|Full SHA256 / identity|
|---|---|
|Current E03final02 development ZIP,83files|`c11e3a6a1812800f45b4770af7e82b648709a28989ec937d45c1e90c27691ae9`|
|M04retained app-debug.apk|`f2dd45440d0fbe0011cef0951bd4642db4e2cb2e797e51cfb833a2aa1df495f8`|
|M04retained app-debug-androidTest.apk|`a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`|
|E04qa-manifest04.json,397paths/17helpers|`e75402fb39ab992a46277a141d7f4d092a4539f031d5ad44f6d2df67197e5e71`|
|Current scope|web19/copiedfrozen-web04;extension83/extractedc11e;Android113+retainedAPKs;46canonical tests unchanged.|

Raw paths: `chrome-extension/build/cross-platform-design-consistency-20261102/extension-role-alignment/artifact-02/`, `android-sources-mapping/run01-result.json`, `ui-qa-preparation/qa-manifest04.json`/`packet-freeze04.json`. Runtime uses frozen web and extracted ZIP,not mutable working src. MainActivity→MainCookbookScreen→PassChefTheme/PassChefHeader and FLAG_SECURE apply to both locked/unlocked paths;canonical CookbookProofActivity/ColdStartProofActivity inherit that root. Source inheritance is not native rendering;BiometricProofActivity alone is not theme visual evidence.

## Font and contrast boundary

Four unmodified TTFs/three original OFL1.1 notices from Google Fonts commit `6c1dbe27dcf7d817d4b62cec033ba77fa8e2a5ec`;[source/hash/license metadata](../../../landing-page/assets/fonts/provenance.json) and packaged extension contract pin the same bytes. No dependency/provider download needed for current typography. Popup/Options share a local face sheet;Demo embeds equivalent faces to preserve its canonical standalone boundary;native bundles exact original files.

|Resource|SHA256|
|---|---|
|Fraunces upright|`177ff6c0f14e5550a3c624247cd1189611d4eb65d000b14944c63d967958abbb`|
|Fraunces italic|`b24448c43702fac4ee856781d461a0dfba8d8e594b6e8e190234b75fed2c0e01`|
|Geist|`73894e0448cae90a92b6c2f8732b7bb9acb7b94c418bff559dad4a18e1de9659`|
|Geist Mono|`d00e590b8eb3a59acc329b2d044fd143ae935090b7da33199ebee27cc7de8196`|
|Fraunces OFL|`bdf4c22802eaf804f998195871c6b8938aac2ac14b2d78a8bd66a6f1eced833b`|
|Geist OFL|`1781d2806a07d91c4edf4740b88449fab7d0eadad53f7c351b94cd4d4eb8c00f`|
|Geist Mono OFL|`1781d2806a07d91c4edf4740b88449fab7d0eadad53f7c351b94cd4d4eb8c00f`|

All four coverASCII95/Vietnamese NFC146/bullet+ellipsis. Geist/Mono cover complete tested NFD22;Fraunces upright lacksU+0309/U+0323,italic lacksU+0309. Fraunces raw weight default900 requires explicit400;static NFC display only. Dynamic/user text uses Geist;Recipe/Dish/terminal Mono also coversU+2588 absent from Geist. No Unicode filtering/normalization of user Recipes/Spices. Cmap and future loaded-face guards do not prove shaping/mark placement.

Opaque source math:Ink600 text5.2496/4.9535/4.6155 on canvas/surface/raised;Ink500 field outline3.3356/3.1474 on canvas/surface but2.9327 on raised—not accepted there. Ink500 normal text fails4.5 and stays disabled/decorative;Amber focus/text and status tints have their bounded source receipts. Actual translucency,anti-aliasing,focus/targets/font scaling/visual comparison remain Pending;no general WCAG claim.

## Prepared runtime sequence — no lease active

All three desktop batches share **one absolute15min** beginning at a newly approvedT;unique canonical UUID/profile/output per batch but the **same slotStartUtc=T**. Do not restart/extend the clock between batches. Cleanup checkpointT+12min,stop starting cellsT+11min15s;minimum useful entry budget enforced. If remaining batches do not fit,retain NotRun/Partial and stop;no automatic fresh lease.

|Sequential desktop batch|Exact prepared case count / scope|
|---|---|
|1 web-geometry|12UIcases(all3pages×320/390/768/1440)+its1W00canary=13required IDs;12tooltip hovers include prior9plus320,original±2px bounds and≥.99settled Copy/Dish.|
|2 web-interaction|2UIcases+its1W00canary=3required IDs;keyboard menu/FAQ and initial/live reduced-motion observations.|
|3 extension|6UIcases+its1W00canary=7required IDs;owned synthetic setup,NFDdynamic types,400×600compact renderer,Demo EN/VI,Popup/Options locale/font evidence.|

Fresh exclusive owner desktop readiness **OR** separately authorized demonstrated isolation;headless/private env/ELF/cache and mocked ownership tests do not prove OS isolation. Loopback listener inode/ownedPGID/birth/run marker checks precede CDP attach/contexts;unknown target stops. No providers,real accounts,desktop Clipboard or downloads. Required missing/NotRun cases cannot green. Root must verify owned descendants/ports/profile cleanup/refreeze and record desktop return;watchdog/group absence alone is insufficient.

A separate fresh **phone15min starts only AFTER desktop is returned**. Owner biometric/native credentials and visual observations must be ready;canonical Copy-containing synthetic fixture needs explicit fresh phone clipboard scope. Root checks current serial/installed APK identities and requests an APK update only if needed/authorized;no install is implied. Use UUID-owned fixtures,no owner Cookbook wipe,FLAG_SECURE never disabled;observe selected EN/VI/IME/font-scale/rotation/mask/reveal/source/mapping/clear states and restore selected owner settings. Uncovered cases remain Pending rather than inferred Passed. Canonical CookbookUiPhysicalTest includes about41s real sleeps plus owner-biometric waits and actual Cook/reveal/Copy/background lock;MenuMappingPhysicalTest (`menu-mapping-physical-test.kt`) covers selected cache/mapping. Clear Cancel/confirm,font-scale,IME and rotation are manual/Pending cells;neither APK compilation nor either fixture Pass establishes all phone visuals.

## Historical preservation and V01 exit

Held74ZIP278b…,E02fontZIP3ed8…,E03candidate01ZIP9519… and currentc11e remain separately retained with source/sidecars/raws. M01API27lint/intermediate warning,E02initial JSON fixture and standalone Demo unit failure,E03remaining-surface review findings,E04guard/locale/font/ownership revisions and tester drift/fixture failures remain dated history. Prior B05actual passes/failures are historical earlier-source evidence;redesign metrics invalidate their use as current visual proof.

Excluded acceptance:Windows CUA/password-manager/auth automation,native toolbar popover,WindowsStable/minChrome116,OS isolation/general WCAG/TalkBack,whole Mobile/MVP/release/Sync/provider/deployed site. Native/renderer/owner runtime remains UNEXECUTED/Pending;public deployment C05/C04/C11 and service/account/legal prerequisites remain held after owner UI-alignment priority.

V01 actual read-only consistency check:existing pure `verifyBindings` matched397paths/17helpers;two retained APK hashes,4TTF/3OFL identities and exact native/web/extension font bytes matched;37local Markdown links across this report/phase05 resolve. Runtime directory absent;git diff --check exit0. No suite replay or new test/source/helper file.

**V01 local exit Passed:**tester verified37links/11printed identities/current397inputs/17helpers;independent review found no blocker. Root updated phase/child/parent/index/roadmap/changelog. This completes local consolidation;V02/runtime and final owner verdict still required. No runtime action here.

Unresolved:fresh desktop then phone readiness/scopes;actual source-bound renderer/native/font/owner comparison,root cleanup and V02;OS isolation and release/deployment gates.
