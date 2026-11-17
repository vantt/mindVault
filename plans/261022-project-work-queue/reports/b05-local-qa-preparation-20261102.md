# B05 — bounded local website QA preparation

Date:2026-11-02, Asia/Saigon. **Preparation only; runtime NotRun; B05 Partial.**
Parent:[Track03 B05](../phase-03-website.md#task-b05).
Reviewable next B05 desktop proposal; no new A task or website/deployed acceptance.
[Independent preparation review Passed](sequential-readiness-20261102-independent-review.md); runtime remains NotRun.

## Candidate, existing verification and limits
Candidate HEAD:`352ffa9300dbd3698f3d32e48b833cf41d841c83`.
Root's actual ignored-local [verification.json](../../../chrome-extension/build/sequential-readiness-20261102/verification.json)
records the seven website file hashes,46unique task IDs,74held runtime hashes,
46canonical test hashes and unchanged ZIP278b59…f1e9e; no website rendering/performance proof.

Root's [toolchain.json](../../../chrome-extension/build/sequential-readiness-20261102/toolchain.json)
records existing Node24.18.0 at `/home/vantt/.nvm/versions/node/v24.18.0/bin/node`,
`@playwright/test`1.57.0 and `/home/vantt/.cache/ms-playwright/chromium-1200/chrome-linux64/chrome`.
Bare Node/pnpm absent from PATH; `require('playwright')` exited1 MODULE_NOT_FOUND,
recovered using direct dependency `@playwright/test`; not a website/build failure.
No browser launch, download or installation.

Read inputs:repository README/rules, `landing-page/README.md`, index/CSS/script and
legal pages, [landing audit](../../../landing-page-improve.md),
[A03 DEMO01](a03-product-claims.md#demo01--known-standalone-illustration-limit),
[A08 inventory](a08-assets-brief.md), [design references](../../../docs/designs/README.md)
and [reading map](../../../docs/specs/reading-map.md).
`docs/code-standards.md`/`docs/design-guidelines.md` absent; follow repo rules/current
landing CSS. Extension prototypes do not authorize landing restyling. Old `npx serve`
example superseded by repo package rules; website has no package/build/test script.

## Proposed execution boundary and preflight
- Root alone operates provisional local QA; C05 publication decision unnecessary.
  Public URLs/fonts/provider/deployment/Store CTA remain C05/C11/A22/B09 gates.
- Fresh15minute desktop handoff before launch; record start/deadline. A21 isolation
  Unverified. This preparation authorizes no GUI/native/clipboard action.
- Recheck HEAD/website hashes/runtime/helper/profile paths; changed bytes require
  packet/affected checks review. Select unused loopback port48761 fresh; occupied
  means another recorded high port, never terminate its owner. Record owned PIDs/paths.
- Server command, **proposed only**, from repository root:

  ```bash
  /home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python -m http.server 48761 --bind 127.0.0.1 --directory /home/vantt/projects/passMan/landing-page
  ```

  Existing Python only; no downloads/public binding. Track process handle/stdout/stderr;
  serve only `landing-page/`. Root confirmed offline uv run works; harmless no-project warning.
- Helper uses direct `@playwright/test` from `chrome-extension/`, absolute Node and
  verified Chromium; no install or extension suite. Fresh UUID-owned private profile,
  no account/extension/credentials/storage import/grant. Root reviews/freezes bounded
  helper before runtime; helper implementation not delivered by this packet.
- Before navigation, deny all HTTP(S)/WebSocket destinations except exact
  `127.0.0.1:48761` (or the recorded replacement port); block service workers,
  downloads, unexpected popups and external navigation. Suppress background browser
  networking and speculative external DNS/preconnect; fail closed if the proposed
  guard cannot establish that boundary. Record blocked Google font requests as
  expected test restrictions, separate from local404/script failures.
- Google Fonts blocked:system fallback-font layout/performance only. Inspect external
  hrefs as text; no external navigation/reachability proof. No `#demo-copy`, Copy
  shortcut, OS clipboard, real account/Menu writes or shared input outside handoff.

## Selected first-slot cells — all NotRun

15minutes **including cleanup**; full matrix/repairs not promised. Record each cell
Passed/Failed/NotExercised/NotRun with raw evidence; no source edit during measurements.

| Cell | Bounded exercise and evidence | Budget |
|---|---|---:|
| W00 | Fresh identity/port/profile/network guard, loopback server and browser launch; capture versions, viewport/DPR and denied external resources. Any unexpected off-loopback attempt that escapes the guard stops the run. |2min|
| W01 | Index at390×844 and1440×900,DPR1:top/Recipe/FAQ/footer screenshots, section visibility after scrolling, document/element overflow and clipped controls. Use layout bounds and screenshots, since body overflow-x:hidden can conceal overflow. |3min|
| W02 | At390px, Tab/Shift+Tab through nav toggle and nearby links; Enter/Space open, Escape close and selected local anchor close. Record focus target/order/visible outline, overlay visibility, scroll lock and focus restoration; FAQ Enter/Space and actual aria-expanded/control association. Do not activate Copy or off-origin links. |2min|
| W03 | New reduced-motion context at390×844:record matchMedia/computed animation/transition/scroll style, terminal/demo settling and anchor scroll/parallax behavior. Compare ordinary context; CSS declarations alone cannot Pass JavaScript motion. |2min|
| W04 | Visit privacy/terms locally at390×844; local CSS/favicon/navigation/anchors render without local network errors. Inspect every local href/src destination/fragment from the three pages; request OG image explicitly because metadata may not fetch it. Record placeholders/404 independently. |1min|
| W05 | Index synthetic Recipe demos and builder:shown three presets, all four shown cooking styles, one alternate Spice jar; after animation settles compare literal Recipe/Dish with production cooking. Enter invalid ingredient `bad!` and observe known fallback; record DEMO01 limitations without modifying parser. |1min|
| W06 | At1440×900, three fresh-context index navigations with cache disabled/empty profile cache and identical guards. Record navigation milestones/paint/DOM/resource/long-task data as supported; see protocol below. |1min|
| W99 | Close contexts/browser, verify owned browser exit, stop and verify exact owned server, remove UUID-owned profile, preserve logs/screenshots/results and return desktop before deadline. |3min|

At12minutes enter W99. Finite operation timeouts bounded by remaining slot;
prioritize W00/W01/W02/W99 if setup overruns; others NotRun. Preserve failures;
no silent retries. Host watchdog/timeout never proves browser/server termination.

Remaining Pending:320/360/768/1024px/landscape, full legal/keyboard/zoom/200%text,
manual understanding, online fonts, browser/device matrix and representative performance.
No Lighthouse/WCAG/screen-reader/broad responsiveness/deployed certification.

## Known source findings to preserve
Missing `assets/og-image.png`:W04 records actual local status; no OG/public URL acceptance.
Store/GitHub/social/license placeholders retain exact target/ownership and Pending status.

DEMO01 accepts obsolete suffixes/invalid-input fallback; illustration caveat present.
W05 records shown examples versus invalid input separately; no full Recipe/Tag/modifier
parity, implicit repair or production Cooking/Golden changes.

W03 observes CSS versus JS smooth scroll/terminal timers/parallax. W02 observes
actual expanded semantics/focus, since toggle/FAQ source changes classes. Source
targets are not fresh runtime findings or accessibility acceptance.

## Descriptive measurement and evidence protocol
W06 records source/browser executable/version/OS/viewport/DPR/guard/font restrictions,
load if observable, sample count and UTC (user times Asia/Saigon). Collect response/
DOMContentLoaded/load relative to navigation start and supported FP/FCP/long-task/
CLS/LCP raw entries at stated finite horizon,e.g.3s after load. Missing entry/API
NotObserved, never zero/Pass. Report3samples/min/median/max. Fresh context is not
proven disk-cold/OS/filesystem/network cache clearance.

Loopback/fonts/load/animation/small samples limit interpretation. No CPU/network
throttling/SLO/true RAM peak/Lighthouse/Core Web Vitals field/performance certification.
Insufficient time means W06 NotRun.

Retain cell JSON/console/pageerrors/local failures/expected blocks/screenshots/helper
SHA/version/times/cleanup under ignored UUID evidence. No broad profile deletion or
kill-by-name. Stop surviving exact owned handles, verify exit and preserve failure;
desktop returned only after actual cleanup.

After runtime:independent result/findings/cleanup review → report → Track03/catalog/
master/index. Scoped diagnosed repair gets static/build checks/review and affected
runtime rechecks in a subsequent bounded slot if needed. Preparation never sets Done/publication.

## Unresolved

Root-reviewed/frozen helper, fresh15minute handoff, runtime/fixes/full matrix/OG/public
URLs/deployment/publisher decisions Pending. All selected runtime cells NotRun.
