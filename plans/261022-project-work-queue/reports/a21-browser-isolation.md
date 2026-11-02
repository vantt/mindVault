# A21 — browser isolation feasibility

Date:2026-10-26, Asia/Saigon. Status:**Done2026-10-26 — read-only feasibility audit**;
[independent review Passed](a21-independent-review.md).
No browser/display/VM/input/clipboard/network-provider action. All isolation canaries
and B executions NotRun; audit completion cannot release a desktop gate.

## TODO

- [x] Map actual fixture focus/input/clipboard/shortcut/network boundaries.
- [x] Inspect read-only local tool/config/runtime availability without owner data.
- [x] Separate potentially isolated cases from owner-slot requirements.
- [x] Define future canary prerequisites, acceptance and fallback.
- [x] Check links/source consistency/preservation; independent review Passed and registries updated.

## Decision from this audit

**No proven isolated browser runtime is available from this audit.** Existing
headed tests can surface windows and some tests alter the actual system clipboard.
Keep B01/B02/B04/B07 behind an owner desktop slot or separately authorized,
demonstrated isolation. A temporary browser profile, WSL boundary, headless flag,
display number or installed sandbox tool alone does not discharge that gate.
No config/fixture/runtime change is made by A21.

## Actual source boundaries

| Source | Operation / consequence |
|---|---|
| [Playwright fixture](../../../chrome-extension/test/e2e/fixtures.ts)L12–39 |Creates temporary persistent profile, loads working `src`, `headless:false`; no explicit private display/VM, OS clipboard partition, outbound policy or owner-focus guard. Profile isolation concerns browser data, not desktop devices. |
| [Playwright config](../../../chrome-extension/playwright.config.ts)L3–24 |`fullyParallel:true`, local workers unspecified, CI1;30s default test timeout, action timeout0 under test scope. Sharing an unisolated display/clipboard across parallel cases is unsuitable for native acceptance. |
| [Toolbar helper](../../../chrome-extension/test/e2e/actual-toolbar-popover.ts)L4–24 |Actual `chrome.action.openPopup` and CDP target attach. Real popover target has focus/lifetime needs; opening popup HTML in a tab is a different claim. |
| [Popup acceptance](../../../chrome-extension/test/e2e/popup-acceptance.spec.ts)L16–98 |Uses `bringToFront`/actual toolbar; can affect visible desktop even with fresh profile. Routed synthetic Menu/receiver checks are not a live provider test. |
| [Menu contract](../../../chrome-extension/test/e2e/google_sheet_integration.spec.ts)L7–42, [DOM contract](../../../chrome-extension/test/e2e/menu-contract.spec.ts) |Mocks Google URL/DOM and dispatches `TRIGGER_HOTKEY` through tab messages. Playwright Escape covers page behavior; neither is OS-native shortcut proof. |
| [Clipboard case](../../../chrome-extension/test/e2e/extension-security.spec.ts)L63–87 |Actual `COPY_DISH`, DOM paste/copy, page close, owned30s cleanup, later replacement and explicit final clear;95s timeout. Reads/writes OS clipboard and takes focus. Never run on owner clipboard without the required slot/isolation. |
| [Offscreen clipboard](../../../chrome-extension/src/offscreen/clipboard-dom.js), [ownership](../../../chrome-extension/src/security/clipboard-owner.js) |DOM clipboard plus compare-before-clear and timer; system clipboard behavior cannot be established by a mocked clipboard or profile-only test. OS history/external copies are outside erase guarantees. |
| [Demo offline](../../../chrome-extension/test/e2e/demo-offline.spec.ts), [tokens CSS](../../../chrome-extension/src/styles/tokens.css) |Demo blocks routed HTTP(S); ordinary extension CSS imports Google Fonts. Page routes do not establish an OS-wide network boundary or prove every browser/worker/background request is blocked. |

[Historical native/provider evidence](../../../archive/plans/261012-extension-completion/reports/live-google-native-proof.json)
records Chromium143.0.7499.4, four native command events and8selected outcomes.
The [historical verification](../../../archive/plans/261012-extension-completion/reports/verification.md)
identifies X11 XTest Ctrl+Shift+L with no message fallback. Preserve that acceptance;
it is not a fresh current-source/candidate or isolation result. No reusable native
injector implementation was identified among the current tracked extension test
fixtures; a retained external helper must be located, reviewed and bound by hash,
or a valid reviewed helper prepared, before new B04 native execution. Do not infer
current helper availability from old JSON or replace its command with a message.

## Read-only environment findings

Observed kernel `5.15.133.1-microsoft-standard-WSL2`; DISPLAY and WAYLAND_DISPLAY
are present, XAUTHORITY absent, WSLg directory present and one X11 socket entry.
Only presence/count recorded; no environment values/secrets, window titles,
screenshots, active-window inspection or clipboard data read. `/proc/*/comm`
category counts found0visible Linux browser/display-server/VM processes at the
sample. This does not enumerate Windows/system-distro processes or prove desktop
inactivity/exclusivity. No display connection/query was made.

| Availability check | Actual result / limit |
|---|---|
| `Xvfb`, `xvfb-run`, `Xephyr`, `Xorg`, `weston`, `Xwayland` on PATH |Absent on PATH. Reviewer-directed hidden-file audit found cached Xvfb/xvfb-run outside PATH (below); no binary started. WSLg presence is not isolated server evidence. |
| `xauth`, `xdpyinfo`, `xprop` |Present; not executed against a display. Their presence proves no authentication/isolation policy. |
| `xdotool`, `xclip`, `xsel`, `wl-copy`, `wl-paste` |Absent. No clipboard/input utility invoked or installed. |
| `qemu-system-x86_64`, `virsh`, `VBoxManage`, `/dev/kvm` |Absent here. Host virtualization availability/settings uninspected; no enablement/VM/service changes. |
| `bwrap`, `unshare` |Present; privileges/capabilities, device/network/display isolation untested. No namespace creation/probe. |
| `chromium`, `google-chrome` commands |Absent on PATH. Playwright1.57.0 package resolves its cached Chromium executable and that file exists; no launch/version execution. |
| `powershell.exe` |Present via interop; not executed. Its availability grants no Windows UI/input/clipboard access. |
| Node runtime |Read-only package lookup with Node24.18.0; pnpm10.24.0 environment already established. No package install or browser download. |

Existing ignored cache `.toolchain/xvfb/` contains15files:executable
`runtime/usr/bin/Xvfb`/`xvfb-run`, three Debian packages, two libraries and
manual/notices. Binary file SHA256:
`6b27845931670c8c9b3ce7423696a452ce3cab1c7f316ce5f7ea7f1297fa2f84`;
wrapper SHA256:
`48ee444c30fdaaede4cc311644b30e554162e956f949b206858e37eb8ba1ae05`.
These are file-presence/hash observations, not binary dependency/provenance/runtime
or isolation verification. No proof config/report was identified in that15-file
cache. Reuse may be possible after validation/authorization; a new installation is
not established as necessary. Nothing in this audit starts the cached executable.

Primary guidance checked2026-10-26:
[Microsoft WSLg README](https://github.com/microsoft/wslg/blob/main/README.md)
describes integrated Windows/Linux GUI and cross-app cut/paste. Therefore the
observed default WSLg endpoints are a shared-desktop risk, not isolation evidence
(source-based inference, no actual bridge canary run).
[Playwright extension docs](https://playwright.dev/docs/chrome-extensions) support
persistent Chromium contexts and a `chromium` channel for headless extension tests.
That is a possible future harness mode, not proof current actual toolbar/native/
clipboard assertions work headlessly. Current fixture remains headed. The attempted
official X.org Xvfb manual URL was inaccessible; the cached gzip manual was read as
text and describes a virtual framebuffer. That description is not a focus/input/
clipboard isolation canary or acceptance claim.

## Feasible proposals and exact case routing

| Proposal / cases | Prerequisites and claim limit |
|---|---|
| Private persistent browser profile alone |Already present for setup/unlock/profile/rotation/Builder/QR state. Does not isolate display focus, native input, system clipboard or network. Remains B-slot-gated in current harness. |
| Dedicated private Xvfb-style X server for local extension DOM/worker/WASM/state tests |Cached server needs dependency/provenance validation, reviewed runtime/auth configuration and later authorized launch; install only if actually needed/authorized. Dedicated socket/auth, no WSLg mount/bridge, no owner display fallback, single worker/fresh profile required. Potentially isolates B01 local setup/unlock/rotation/profile/Builder/Tag/QR/Demo after canaries. Cached bytes exist; runnable/proven isolation remains unverified. |
| Dedicated VM/test desktop |Potentially contains display/input/clipboard and process resources; requires approved provisioned runtime, disabled shared clipboard/drag-drop/folders as appropriate, network policy and canaries. Host display integration/console focus still needs checking. No available VM verified here. |
| Headless Chromium local state tests |Future reviewed fixture proposal; may reduce visible windows but does not inherently partition OS clipboard/network. Cannot substitute for real toolbar-popover/native key/session/OS clipboard claims without equivalent assertions and measured boundary. No headless config change today. |
| B02 picker/receiver/permission variants on synthetic routed Menus |Could run on proven isolated display/candidate; retain exact P0/P1/P2/P3 manifest/hash evidence and actual picker/focus behavior. Local routing doesn't establish live Google/network compatibility. |
| B04 native shortcut + clipboard/timers |Needs isolated display/native input implementation and clipboard server proven independent, real `chrome.commands` observation and original deadlines. Otherwise exclusive owner desktop slot. Unit messages/clipboard mocks do not satisfy it. |
| B01/B07 extracted-ZIP/WASM/offscreen/fail-closed browser smoke |May be isolated once canaries pass; bind exact reviewed candidate and fresh extracted ZIP, not working src fixture silently. Full existing `pnpm test` still launches headed E2E; no permission to run it follows from this report. |
| Actual Google Menu/account/Sync or production Store/browser/OS support |Still owner-authorized remote/account slot and matching actual environment. Isolation does not authorize Google login/writes/grants or substitute two physical devices (A10). VM/headless results cannot certify all supported owner OS integrations. |

## Future isolation acceptance protocol — not executed

1. Root records selected exact source/ZIP SHA, Playwright/browser/runtime versions,
   proposed display/VM identity and helper hashes. Obtain authorization for any
   install/launch/display/VM/input/clipboard/network action first; configure
   workers1/retries0 for the selected canary/case so concurrent attempts cannot
   obscure focus/clipboard ownership. No production profile/account copied in.
2. Prove launch fails closed when isolated endpoint is missing; reject fallback to
   inherited owner DISPLAY/Wayland/WSLg. Inspect target process/display socket/auth
   and namespace/VM boundaries without logging owner titles or credentials. Single
   private profile must contain only synthetic Cookbook data; network deny/allowlist
   comes from the actual isolation layer, not a presumed page-route guarantee.
3. Under separately approved **synthetic-only** host/guest canary ownership, prove
   guest window activation/native keys stay in guest and do not steal host focus or
   trigger host shortcut. Owner observes host independently; absence of a known
   host observation is Inconclusive, never isolated Passed. Never sample arbitrary
   host input/window contents. Capture only aliases/counts/event IDs.
4. Clipboard canary requires explicit approved disposable host and guest markers;
   verify bidirectional nonpropagation plus guest owned-Dish cleanup/replacement
   survival at original deadline. Do not read/save a pre-existing owner clipboard
   to create a backup. If owner cannot provide a disposable marker/exclusive slot,
   this canary remains Blocked. Do not promise erasure of OS history/external copies.
5. Network canary records actual synthetic permitted/denied requests and background/
   worker paths, no account tokens. Verify blocked external requests and allowed
   intended fixture only; separate later live provider authorization. Prove process/
   profile/download/report paths and timer clock remain in chosen boundary.
6. Use finite reviewed budgets:proposed60s launch/focus,60s key/window,120s clipboard
   and60s network canary, host watchdog10minutes maximum. These are preparation
   budgets, not executable helpers. On any leak/missing event/timeout stop further
   input/writes, preserve actual results, confirm native child termination, then
   clean only own processes/profile/auth/socket/files. Don't kill shared WSLg or
   owner browser. Host watchdog alone is not cleanup/native termination evidence.
7. Independent review records Passed/Failed/Blocked/Inconclusive **per boundary**.
   Only exact covered case IDs move to the corresponding B execution queue; actual
   B assertions/candidate/runtime verdicts still required. Audit Done is not blanket
   authorization, canary Passed is not a product test Pass, and isolated Linux is
   not owner Windows/macOS support acceptance.

Fallback:if no authorized proven isolation is available, schedule a short exclusive
owner desktop slot for exact selected B cases with synthetic data and explicit
clipboard/native input ownership. Do not run in default WSLg because the profile is
temporary. Keep current B gates Pending until the slot or reviewed isolation exists.

## Verification and exact scope

Only this report edited. Read-only checks:fixture/config/resource/native-history
source reads; `shutil.which` tool booleans; environment-variable presence only;
kernel/WSLg/socket/KVM existence; `/proc/*/comm` category counts; Node module/version/
`chromium.executablePath()` file-existence lookup without launching browser;
official primary documentation reads. Local checks Passed13Markdown targets,
6source ranges, retained8case/4native-command/no-fallback JSON and NotRun/gate
status assertions. `git diff --check` and unchanged Playwright config/E2E tracked
file checks exit0. Runtime/tests/Golden/config
unchanged by A21. No full test/headed suite, native helper, GUI/display/VM/server,
clipboard/input/shortcut/provider/account/phone/ADB/Gradle/install/service/reboot.

## Unresolved

Actual isolated runtime/display and clipboard canary; reviewed native helper path;
owner desktop authorization or proven equivalent isolation; C02/A13 candidate and
selected B01/B02/B04/B07 scopes. No implementation or B run authorized by this audit.
