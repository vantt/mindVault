# E04 QA preparation verification — 2026-11-02

Final revision04 **offline verification Passed: 32/32 independent groups**, plus read-only preflight of 397 bound inputs and 17 helpers. Actual browser/phone/UI/network/isolation checks remain **UNEXECUTED**. No product or canonical test edits by tester.

Evidence directory: `chrome-extension/build/cross-platform-design-consistency-20261102/ui-qa-preparation/`. Final `qa-manifest04.json` SHA256 `e75402fb39ab992a46277a141d7f4d092a4539f031d5ad44f6d2df67197e5e71`. Node24.18.0; no installs/downloads.

## Actual independent checks

```sh
/home/vantt/.nvm/versions/node/v24.18.0/bin/node chrome-extension/build/cross-platform-design-consistency-20261102/ui-qa-preparation/verify-tester-guards04b.mjs
```

Exit 0, 32 Passed / 0 Failed. Tests exercise actual pure exports. The native launch declaration is extracted into a VM with fake filesystem/spawn/stat; its single virtual spawn is not a native child. Process-stop/listener IO is injected; timeout uses a bounded 2ms harness timer. No runtime driver imported or invoked.

- Real frozen binding checks pass; injected source/helper drift rejects. Missing/unapproved/mismatched leases, traversal/hyphen-only/noncanonical UUIDs, malformed/future/expired timestamps, exact useful-time cutoff and forbidden display/clipboard/provider/account/install scopes reject. Lease arithmetic is exactly 15min deadline / 12min cleanup / 45s last-cell reserve.
- Fresh exclusive-owner readiness is an **OR** authorization path and explicitly returns isolation `UNVERIFIED`. Demonstrated-isolation path accepts a same-run canary observed after slot start and before now/deadline; rejects stale/future/replayed/worker-unverified evidence. Tests validate metadata guards, not the truth of an isolation assertion.
- Worker origin handles `chrome-extension:` URLs whose standard `.origin` is `null`; wrong host/scheme/path/credentials/query rejects. Private environment strips ambient owner/display/session bridges and scopes XDG/TMP paths. Actual launch declaration rejects wrong ELF/hash/environment/missing marker before virtual spawn and registers the virtual child before injected stat failure.
- Actual `/proc/self/stat` and synthetic parenthesized command parsing pass. Mocked stop refuses reused/unknown identity, reports surviving groups, and performs bounded TERM/KILL attempts without real signals.
- Missing/empty required coverage, cutoff refusal, thrown cells, pending work, timeout, cleanup/stop rejection, false absence receipt and save failure cannot produce successful exit. Both cleanup branches run after the first failure, registered timers clear, active-cell finalization aborts/retains one failure, late completion cannot turn it into a pass, and finalization saves once.
- New listener tests cover TCP state/port parsing, owned PGID member socket FD, missing/foreign/nonloopback/IPv6/ambiguous listener rejection, birth/group/run-marker mismatch, scan bounds, IO refusal and postscan identity change. No real socket, process, browser or server exercised. Driver source requires proof before GET/CDP attach, after attach and before context access.
- Font outcomes reject missing roles, zero/failed loads, missing/unloaded faces and absent/wrong static brand family/weight. First-family guard rejects fallback declarations. Observed localization must equal frozen EN/VI strings. Root/selected/affected rectangles reject overflow and zero area at the original ±2px threshold. These are guard outcome tests, not rendered typography assertions.

Raw `verification-tester-guards04b.json` SHA256 `7ef50235ce702d0860391363ed8e26a14d2b18af2172329db4e3398dd41aec89`. Reusable script and immutable input snapshots retained.

Read-only `main(['--preflight'])` branch ran 15:33:59.037–15:33:59.872 UTC, exit 0; 397 bindings / 17 helpers matched again afterward. It returned before dynamic driver import. Runtime directory absent before and after. Raw `verification-tester-preflight04.json` SHA256 `da5d4a4cf9bd3e9934a82c26f86f779a57cd633ab058df9f407ca6d1906ee2bf`.

## Coverage / preservation

Required IDs match actual finite case declarations: web geometry 13, web interaction 3, extension 7, each including its launch canary. Underlying proposed UI cases are 12 geometry / 2 interaction / 6 extension; none executed here. Frozen website has 19 files; extension is the extracted 83-file c11e candidate; M04 retains 113 source files and its reviewed app/test APKs.

Against E03's older 292-path test receipt, 291 remain byte-identical. One authorized root bookkeeping change in `docs/specs/cross-platform-design.md` clarifies the 6px desktop language-toggle enclosure; current hash `6d8a667013d001c575ec270dfc9501843248e807db998fdf6850e0b589b1ea52` matches the E04 binding. No product/test relaxation. All 113 M04 sources independently match. Current ZIP remains `c11e3a6a1812800f45b4770af7e82b648709a28989ec937d45c1e90c27691ae9`; reviewed app APK `f2dd45440d0fbe0011cef0951bd4642db4e2cb2e797e51cfb833a2aa1df495f8`, test APK `a5bdc551b621df13a24b3f377962efd99a52b89a54d45ba737f1fdf5ea8c8951`.

Actual source chain confirmed: canonical Cookbook UI/diagnostics target `CookbookProofActivity`; mapping/cold fixtures target `ColdStartProofActivity`. Both inherit `MainActivity`, call `super.onCreate` and do not replace content. MainActivity sets FLAG_SECURE and MainCookbookScreen; that screen wraps both locked/unlocked branches in PassChefTheme and renders the shared header. Separate BiometricProofActivity is a secure native prompt harness, not theme-visual proof. No fixture replay performed.

## Findings and historical attempts

Tester found extension font/locale observations could previously pass without asserting load success or observed translated text. Revision03 added mandatory shared resource/font/first-family/localization guards; revision04 additionally checks actual static brand subjects and CDP ownership. Final negative tests pass. No claim those fixes have run in a browser.

Earlier tester receipts remain immutable: run01 21/22 (expected helper-drift refusal during author revision); run02 26/27 (older contract-document hash, later confirmed authorized); initial run04 31/32 (tester positive font fixture omitted newly required heading data). Corrected fixture in separate run04b gives 32/32. No product assertions weakened to resolve these harness/baseline issues. Author's separate 36/36 offline result is distinct from tester's count.

## Limits / unresolved

A21/OS/browser/background/service-worker network isolation remains UNVERIFIED. Headless/profile/environment flags, mocked ownership tests and a future page-route canary do not prove it. Every runtime/canary launch still requires fresh scoped resources: exclusive owner readiness or separately authorized demonstrated isolation. Windows CUA password-manager/auth automation is excluded; native biometric/provider actions remain owner-controlled. Phone Copy-containing canonical fixture needs explicit fresh synthetic clipboard permission; no real account/provider/clipboard actions or installs authorized by this packet. No unresolved bounded offline finding; independent review and plan update belong to root.
