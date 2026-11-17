# Current mobile verification —10November2026 (Asia/Saigon)

Status: **Local verification Passed; G1/G2/L3 and final delivery remain separate**.
No production/test source changed in this verification. No device, Google account,
grant, Menu or owner Cookbook operation occurred.

## Actual checks

Root build used JDK17 and SDK36. Fresh test tasks were preceded by their specific
`cleanJvmTest` / `cleanTestDebugUnitTest` tasks; compilation and APK assembly stayed
UP-TO-DATE. Build Passed127s,185tasks:12executed/173up-to-date. Current Android-test
lint analysis executed; main/unit lint analysis and report were UP-TO-DATE. The
report remains0errors/7warnings/1hint; no fresh main-lint analysis is claimed.

| Kitchen | Fresh tests | Failures/errors/skips |
|---|---:|---|
| Shared |78|0/0/0|
| Android Menu bridge |22|0/0/0|
| App |16|0/0/0|
| Independent Menu client, JDK21 |35|0/0/0|
| Total |151|0/0/0|

Standalone `cleanJvmTest build test` Passed20s,9tasks:4executed/5up-to-date.
All28actual XML reports were copied into separate evidence. Compilation of device
assertions does not execute them.

## Preservation and candidate identities

All214frozen source/config paths stayed exact across verification. This inventory
includes every207path from the9November local verification, all matching. The
additional paths include the later VI offline fixture and additional build config;
the inventory difference is not a new implementation claim.

Original106baseline audit retains the same seven known production differences;
all64baseline test/fixture paths, including Golden/vector bytes, remain exact.
All three APKs match the latest9November VI-fixture-repair candidate:

| APK | SHA256 |
|---|---|
| Debug |`921f057eb1380b7a537e7cf4ac3f28563776c184a9583a677707cafcc6e94444`|
| Device tests |`e59af129d0a99d454e025eb7559c37bebffff699eaa3191f5e2c135923fa412f`|
| Unsigned release |`f2a86210fa254f5050bebd1086092dcd53dd87b7bb63b346c49c9ac11f57f264`|

Evidence:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-10-current-tree/`.
`commands.json` records exact commands/exit codes; `summary.json`, copied XML/APKs,
source-before/after inventories, baseline audit and both build logs retain results.
No fresh packaged manifest/DEX, installed-byte or signed-release acceptance claimed.

Unresolved: real human Google cancellation/different-Menu callbacks, actual OS-LMK
recovery and final accepted-candidate delivery. Local checks do not close those gates.
