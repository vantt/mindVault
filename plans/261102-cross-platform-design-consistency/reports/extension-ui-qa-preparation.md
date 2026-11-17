# E04 — source-bound UI QA packet preparation

Date:2026-11-02. **Revision04 preparation Done;[independent verification32/32](ui-qa-preparation-verification.md) and [review Passed](extension-ui-qa-preparation-independent-review.md). Runtime UNEXECUTED.** No product/test/font/packager changes or Phase05 runtime acceptance claim. Root owns fresh resource handoff after packet review.

## Frozen inputs and runnable entry points

Ignored stage:`chrome-extension/build/cross-platform-design-consistency-20261102/ui-qa-preparation/`.
Current `qa-manifest04.json` SHA256 `e75402fb39ab992a46277a141d7f4d092a4539f031d5ad44f6d2df67197e5e71`;`packet-freeze04.json` binds397paths+17helpers. Website19current files copied byte-exact into `frozen-web04/`;extension83 files from **extracted ZIP**,not working src. APK/source/Golden/helper/browser identities are pinned;entry verifies bindings before any approval-dependent output/native action and again after runtime.

|Input|Binding / limit|
|---|---|
|Extension current|E03artifact02 ZIP `c11e3a6a1812800f45b4770af7e82b648709a28989ec937d45c1e90c27691ae9`;83extracted files byte-identical/current source,zero external stylesheet records.|
|Website|Current19files;all three pages/four TTFs/OFLs/JS/brand assets. Exact original geometry rules preserved by E03/W03 evidence;new renderer QA remains required.|
|Android|M04source113;retained appAPK `f2dd45440d0fbe0011cef0951bd4642db4e2cb2e797e51cfb833a2aa1df495f8`,testAPK `a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`. No build/install/device action.|
|History|Held74ZIP278b…,E02fontZIP3ed8…,E03candidate01ZIP9519… and currentc11e verified unchanged in `historical-preservation.json`;older raw evidence not rewritten.|
|Browser proposal|Existing cached Linux ELF bytes/hash only. Presence does not prove launch/sandbox/library/headless capability,OS isolation or permission. No executable/plugin/browser installation.|

Offline-only entry (executed Passed):

```sh
node chrome-extension/build/cross-platform-design-consistency-20261102/ui-qa-preparation/run-qa.mjs --preflight
```

Future runtime command **only after root records fresh specific approval/resource readiness**,using a new copy of the unapproved template;no timestamp supplied here:

```sh
node chrome-extension/build/cross-platform-design-consistency-20261102/ui-qa-preparation/run-qa.mjs --approval <ROOT_RECORDED_FRESH_APPROVAL_JSON>
```

`approval.template.json` remains approved:false/no timestamp/runUUID. CLI actually rejected it exit1 (“Fresh specific approval/binding required”) before a runtime directory existed. The packet does not infer consent from old15min replies. Root runs one batch per approved finite lease;no default headed `pnpm test`.

## Prepared finite cases

`case-matrix.json`,actual adapters `web-cases.mjs`/`extension-cases.mjs`,and `phone-cases.json` specify scope.

|Batch|Actual prepared checks|
|---|---|
|web-geometry|12page/width cells:all three pages at320/390/768/1440;four actual local font-load roles,NFC/NFD probes and required matching loaded faces/static display400. Initial/root/selected/affected rectangles checked with original+2px tolerance. Copy/Dish settle to effective opacity≥.99 within4500ms,no hiding/style suppression. Twelve actual tooltip hovers retain previous9at390/768/1440 plus3at320;original text/pseudo bounds/root/evidence saved before assertions.|
|web-interaction|Keyboard Enter menu/FAQ,ARIA-expanded/controls and unique answer target;initial/live reduced-motion glow reset with actual matchMedia/transform observations. Broader motion/zoom/focus visual judgments remain owner observations,not a blanket animation pass.|
|extension|Current extracted83artifact only;existing `current-cookbook-fixture.ts` performs real synthetic setup/encryption in fresh private profile. NFD profile/name/edit/export fragments require first family **exact Geist**. Fields/compact26px Settings/30px toggles and400×600 popup renderer;Demo/Options/Popup EN/VI require observed visible UI strings matching frozen localeJSON,not storage language alone. Four font roles must load/check and all four matching faces be loaded;fallback-only CSS-family observations cannot pass. No provider/real account/Copy/export-download action.|
|phone|Canonical unchanged fixtures plus owner observations:real MainActivity/theme path,locked/password/setup/Spices/busy/errors,Menu/list/search/filter/detail/manual Recipe/Dish/mask/reveal,offline source/mapping/clear Cancel/confirm,locale/IME/scaling/rotation selected states. Owner performs biometric/native credentials. Canonical Copy-containing fixture requires its own fresh specific phone clipboard scope;it is not authorized by renderer-only approval.|

Extension popup-as-tab at400×600 is renderer evidence,**not native toolbar popover**,Windows Stable116 or Chrome-minimum-version acceptance. Current executable is cached Linux Chromium;no Windows/native capability claim. Synthetic profile data is owned disposable data,no real Google account/provider/Sync connection. Existing accepted security/clipboard/timer policies are unchanged;style verification need not rerun native Copy/credential operations without their separate scope.

`fonts()` now asserts load counts/check flags and all four exact matching loaded families/styles. `observation-checks.mjs` rejects first-family Fraunces-with-Geist-fallback and Geist Mono for dynamic text,untranslated EN fallback in VI cases,and selected rectangles outside bounds even when root fits. Glyph loading/metrics do not certify mark placement;actual rendered NFC/NFD and platform font acceptance still require observation.

## Lease, process and cleanup controls

`qa-session.mjs` is pure until called;`owned-process.mjs` imports builtins only and exposes injected identity/stop IO for offline tests. `run-qa.mjs` imports the native driver only after source/approval/time/resource gates. Helpers all<200lines.

- Strict canonicalv4UUID and exact UTC millisecond timestamp;15min lease,cleanup at12min,last case cutoff45s earlier,useful initial budget≥60s. Missing/malformed/future/expired/replayed/insufficient leases fail before runtime output/timers/native launch. Required cells missing/NotRun/empty produce Partial/nonzero;cleanup success alone cannot certify selected cases.
- Fresh **exclusive owner desktop slot OR separately authorized demonstrated isolation**. Exclusive mode records isolation UNVERIFIED and permits only the approved private headless project harness;no extra isolation/clipboard canary prerequisite for renderer-only exclusive-slot cases. Verified-isolation mode separately checks same run/route and fresh observed evidence within the approved slot and≤now;future/replayed evidence rejected.
- Child executable must be pinned LinuxELF;allowlisted env drops DISPLAY/WAYLAND/DBUS/WSL/session bridges,uses task-private XDG/TMP directories. Headless flag is not treated as proven OS isolation. No automatic no-sandbox/driver/browser/display fallback if launch fails.
- Proposed loopback ports48763/48764 checked free after approval;occupied ports fail,no foreign process killed. Serve only19frozen files. New exclusive run directory/profile;no owner browser/profile import. Native child registered immediately before reading/proving `/proc` birth/pgroup;unknown/reused PID refuses signals. Bounded TERM/KILL for verified owned group only;unknown surviving group fails cleanup.
- Current c11e worker origin constructed explicitly as `chrome-extension://<id>/` (WHATWG custom-scheme origin is null),then actual worker manifest/runtime origin verified. Local-only page requests,WebSocket interception,navigation/download/unknown-worker boundaries and expected page-route canary are finite;W00 is **launch/selected page-route check only**. It does not certify OS/background/service-worker network isolation.
- Each cell timeout aborts/halts dependencies. Finalization attempts closes/each owned stop independently,clears timers,preserves result/fatal/partial cells despite close/stop/save failure,returns nonzero if absence unknown/late. Only group absence is machine-checked;escaped descendants/ports and profile deletion require root postflight. No watchdog/close timeout alone proves termination. Driver retains profile;root deletes only after owned descendant/port absence and refreeze proof,then records desktop return time.

## Phone source path and handoff limits

`CookbookProofActivity`/`ColdStartProofActivity` extend actual MainActivity and call super;MainActivity supplies FLAG_SECURE and MainCookbookScreen→PassChefTheme/PassChefHeader for locked/unlocked paths. This source path proves inheritance only. `BiometricProofActivity` is a separate secure prompt harness,not app-theme visual coverage. Use retained M04APKs and fresh verified serial/UUID;root-only ADB. No pm clear/uninstall/reset of owner Cookbook;canonical cleanup deletes only UUID-owned storage/referenced test aliases.

Command templates/fixture names and fresh `passchefMappingFixtureId` are in `phone-cases.json`. `CookbookUiPhysicalTest` creates random owned proof storage itself and includes real reveal/Copy/owner biometric waits;do not run it when only style/readiness or no-clipboard scope is given. Clear-confirm/IME/font-scale/rotation owner observations remain explicit manual cells where existing test does not prove them. Preserve original settings and restore selected changes exactly. FLAG_SECURE never disabled;no fake screenshot proof or CUA/native auth automation.

## Policy and feasibility boundary

Computer Use [SKILL.md](/mnt/c/Users/van.tran_fgorg/.codex/plugins/cache/openai-bundled/computer-use/26.1027.30824/skills/computer-use/SKILL.md) requires `@oai/sky` through NodeRepl and forbids custom helper protocols/executable spawning for CUA. Its [guidance.md:238–239](/mnt/c/Users/van.tran_fgorg/.codex/plugins/cache/openai-bundled/computer-use/26.1027.30824/docs/guidance.md:238) says “Do not automate user authentication dialogs” and “Do not automate password manager apps or password manager websites.” This packet prepares **no CUA route for PassChef**;project-owned coded synthetic tests are a separate route. Windows-specific/native observations remain owner/manual or separately approved project-test scope. No BrowserUse plugin is available here;none installed or promised.

Official [Playwright extensions guide](https://playwright.dev/docs/chrome-extensions) documents bundled Chromium persistent-context/headless extension testing. That supports the proposal,not capability acceptance for this cache/host. Official [network guidance](https://playwright.dev/docs/network) and [service-worker guidance](https://playwright.dev/docs/service-workers) describe interception limitations;page-route proof is not whole-browser isolation. A21 remains UNVERIFIED;no unattended reclassification based on headless/ELF presence.

## Offline verification and preserved preparation history

Draft01/initial17checks and draft02/22checks/manifest8aeded… retained byte-for-byte. Initial freeze refused a canonical contract hash advanced by root's post-E03 bookkeeping;current manifest explicitly pins the known new contract6d8a… instead of ignoring drift. No product mismatch or runtime failure inferred. Root review found origin/lease/env/completeness issues and font/locale/bounds acceptance gaps;revision03 fixes actual guards/assertions,not canonical product tests.

`offline-checks03.mjs` actual30/30Passed:missing/forged/expired/future/reserve lease,source tamper,exclusive-vs-isolation routes,post-approval evidence/future/replay,actual own `/proc` parsing,unknown PID refusal,bounded escalation,no signals to orphan group,independent cleanup attempts/save rejection/false absence/cell timeout/active-cell interruption/idempotent timer cleanup,required incomplete cannot green;negative font/first-family/locale/selected-bound guards. All process/stop work mocked except read-only own `/proc`;no native spawn/browser/server/display/clipboard/device. Eight actual Node--check commands exit0;preflight397bindings/17helpers Passed;git diff --check0. Product83/web19/native113/46tests/four ZIPs unchanged;new files only under ignored stage plus this report.

Unresolved:independent offline verification/review;fresh owner desktop/phone readiness and exact selected resource/clipboard scope;cached Linux launch/sandbox/dependencies/canary capability;actual font/renderer/native/owner comparison,root postflight and Phase05 acceptance. Root asks one bundled fresh handoff only after packet review;no live resource request is made by this preparation agent.

## Revision04 — final target ownership and actual brand coverage

Root review found that probing a free CDP port and reading `/json/version` did not bind its eventual listener to the owned child. `proveListener(port,child,io)` now checks the exact child birth/pgroup/private-profile run marker,unique127.0.0.1 LISTEN socket inode from Linux `/proc/net/tcp*`,and that inode among FDs of the verified owned process group. Bounded process/group/FD scans fail closed for missing/foreign/ambiguous/wildcard/unreadable evidence. Proof runs before metadata GET/connect and again before any context/permissions/UI action;the observed exact browser WebSocket endpoint is used instead of reconnecting by generic port. Remote close also refuses an unproved target. This is source/mocked guard evidence,no actual listener/browser was launched.

Font collection now targets actual `.brand-wordmark`,`.demo-wordmark` and named static display subjects,asserting first-family Fraunces/weight400. It does not impose a universal H1 rule. Dynamic names still require first-family exact Geist;four actual local loaded-face roles/translated visible locale UI/selected bounds remain mandatory.

Revision03 modules,manifest1935d…,30check receipt and freeze metadata are preserved in `draft03/` plus `draft03-receipt.json`;earlier17/22 histories remain. Final `offline-checks04.mjs`→`offline-verification04.json`:**36/36 Passed**,including owned/foreign/missing/wildcard/duplicate/reused-birth/wrong-marker/unreadable-FD listener cases and actual static-brand primary400 guards. Eight Node syntax checks exit0;current offlinepreflight397paths/17helpers Passed;git diff --check0. Final manifest hash is the current04 identity above;no helper edits after final READY. Report-only changes do not alter that manifest.

Independent tester/reviewer now own final frozen-packet checks. Productsource/runtime remain untouched and UNEXECUTED;no owner resource question or V01 work started by this agent.

Root closure:independent verification/review Passed;child/phase04/parent/index/roadmap/changelog updated and diffcheck0. E04 preparation Done;V01 local consolidation next. All actual resource/runtime/owner cells remain Pending.
