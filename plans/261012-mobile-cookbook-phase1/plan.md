# Android Cookbook — first production slice

Date:2026-10-12; updated2026-10-14 (Asia/Saigon). Status: **Partial — code/local checks Done; owner acceptance Pending**.
Baseline: main `2579e29044924e1582b78414b0314baffc72abe5`.
Owner authorized two agents: recheck the five browser slices and implement mobile.
Production code is implemented and independently verified locally.
Selected UI/clipboard/Google cache recreation Passed; broader device acceptance Pending.
Actual UI Run01 failed after reveal; [device evidence](reports/device-acceptance.md)
and [unattended regressions](reports/unattended-regression.md) track investigation.

## Outcome and settled scope

Turn the existing storage/unlock demo into an Android Cookbook: manual Recipe
paste/cook, private offline Menu cache, confirmed column mapping, list/search,
masked reveal/copy and safe session ownership. Reuse the shared kitchen and the
owner-accepted Keystore foundation; never duplicate their cooking/key algorithms.

KMP + Compose and Android-first are settled. Google permission covers Sheets
read/write; product writes remain append/update only, never delete. This slice
consumes read operations; account authoring/write UI belongs to a later phase.
Q6's historical read-only wording is superseded by Q9 and the retained decisions.

## Phases and order

| # | Detailed work | Status | Dependency / proof boundary |
|---|---|---|---|
| 1 | [Session-owned manual cooking](phase-01-session-cooking.md) | Done (local scope) | First useful offline path; shared JVM regressions + Android compile |
| 2 | [Private cache and column mapping](phase-02-cache-mapping.md) | Done (local scope) | Pure model/storage tests; no Google credential required |
| 3 | [Cookbook Compose screens and clipboard](phase-03-cookbook-ui.md) | Partial | Selected UI/cache/real5+10min Passed; broader phone/clipboard matrix Pending |
| 4 | [Android Sheets/authorization integration](phase-04-android-sheets-connection.md) | Partial | Google/cache/network-off Passed;401/403/cancel Pending |
| 5 | [Independent verification and delivery](phase-05-verification-delivery.md) | Partial | Evidence separates code/build acceptance from physical/live acceptance |

Phases1–3 can produce useful real offline code without a phone/account. Phase4
can be built and tested against explicit protocol fixtures; such fixtures never
certify an Android Google connection. [Verification](reports/verification.md) and
[independent review](reports/independent-review.md) record exact local evidence.

## Verified integration choices

- `MainActivity` already secures the window and locks on background/recreation;
  `VaultController` keeps its owned session private; Cookbook screens now call the
  guarded kitchen after unlock.
- Root uses Kotlin2.4.10/AGP9, JDK17 and SDK36. The standalone Sheets client uses
  Kotlin1.9.24/JVM21 and keeps its independent build.
  The root `:sheets-android` bridge now reuses canonical common sources/tests;
  its desktop loopback OAuth tool is not bundled in Android.
- App manifest adds INTERNET for the real Android Sheets engine. AuthorizationClient
  and Compose test dependencies are pinned and compile in the actual app.
- Available SDK has no verified emulator executable. Compiling instrumentation
  is feasible; running it cannot be claimed without an authorized device/runtime.

## Owner-dependent acceptance and deferred work

Owner confirmed real Google consent; [Google runtime](reports/google-runtime.md)
records Google Run03 Passed metadata/rows/mapping/cache-file load after Failed Run01/02.
[Cache UI Run02](reports/google-cache-ui-runtime.md) Passed lock/reopen/cached Cook
and authenticated same-process recreation; app-disconnected cache only. Later
[Network-off Run04](reports/network-off-runtime.md) Passed real offline Cook/error
preservation;401/403/cancel/cold-start/process-death remain Pending in phase04.
Run04/05 cover selected synthetic-jars biometric/Cook/reveal/copy/background flow.
Foreground clipboard adapter3/3 and [actual5-minute idle Run05](reports/session-timer-runtime.md)
and total04 Passed; API36 foreground proof only, cold-start/process-death/broader UX Pending.
QR receiver/camera, Autofill spike/service, Drive, multi-profile authoring and
append/update screens remain separate later work. No reopened stack/write decision.

## Context and verification

[Roadmap/Q6/Q8/Q9](../../docs/mobile-app-brainstorm-qa.md),
[settled decisions](../../docs/decisions/mobile-product-decisions.md),
[Core](../../docs/specs/mobile-core.md), [Keystore](../../docs/specs/mobile-keystore.md),
[Sheets client](../../docs/specs/mobile-sheets-client.md).
Exact local commands and the Pending owner checklist are in phase5.

## Open dependencies

Google401/403/cancellation and later physical acceptance; no product
question blocks the offline implementation. Whole roadmap Phase1 remains Partial
until real connection and required user/device flows have evidence.
