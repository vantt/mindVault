# Android Cookbook — first production slice

Date:2026-10-12; updated2026-10-21 (Asia/Saigon). Status: **Partial — code/local checks Done; owner acceptance Pending**.
Baseline: main `2579e29044924e1582b78414b0314baffc72abe5`.
Owner authorized two agents: recheck the five browser slices and implement mobile.
Selected UI/cache/cold/clipboard/timers/capture/Recents/owner shortcut/tag-mapping Passed; broader acceptance Pending.
Latest: [provider-loaded403 + fresh cached Cook](reports/google-provider403-preparation.md#actual-provider-loaded-http403-run01--passed21902s) Run01 Passed21.902s; exact pre-Cook preservation; Mobile Partial.
Earlier: [actual Google HTTP403](reports/google-http403-preparation.md#actual-google-http403-run04--passed27530s) Passed27.530s; exact synthetic cache/jars preserved, stale Dish cleared; Mobile Partial. Earlier [real host Google OS-Back/stale reply](reports/google-cancellation-preparation.md#actual-host-android-system-back-run01--passed) Passed17.949s; synthetic cache/jars preserved; human cancellation/401/different-Menu Pending. Earlier [clipboard repair](reports/clipboard-null-label-repair-runtime.md) remains accepted.
Actual UI Run01 failed after reveal; [device evidence](reports/device-acceptance.md)
and [unattended regressions](reports/unattended-regression.md) track investigation.

## Outcome and settled scope

Extend storage/unlock into an Android Cookbook: manual Recipe paste/cook, private
offline Menu cache, confirmed columns, list/search, masked reveal/copy and session ownership.
Reuse shared kitchen/owner-accepted Keystore; never duplicate cooking/key algorithms.

KMP/Compose/Android-first settled. Sheets permission covers read/write; product writes
remain append/update only, never delete. This slice reads; account authoring/write UI later.
Q6 historical read-only wording superseded by Q9 and retained decisions.

## Phases and order

| # | Detailed work | Status | Dependency / proof boundary |
|---|---|---|---|
| 1 | [Session-owned manual cooking](phase-01-session-cooking.md) | Done (local scope) | First useful offline path; shared JVM regressions + Android compile |
| 2 | [Private cache and column mapping](phase-02-cache-mapping.md) | Done (local scope) | Pure model/storage tests; no Google credential required |
| 3 | [Cookbook Compose screens and clipboard](phase-03-cookbook-ui.md) | Partial | Selected UI/cache/real5+10min/secure capture/Recents Passed; accessibility/broader phone matrix Pending |
| 4 | [Android Sheets/authorization integration](phase-04-android-sheets-connection.md) | Partial | Google/cache/network-off/host OS-Back/selected403 Passed;401/human cancel/different-Menu Pending |
| 5 | [Independent verification and delivery](phase-05-verification-delivery.md) | Partial | Evidence separates code/build acceptance from physical/live acceptance |

Phases1–3 support real offline code without phone/account. Phase4 protocol fixtures
permit build/tests but never certify Android Google connection. Exact local evidence:
[Verification](reports/verification.md), [independent review](reports/independent-review.md).

## Verified integration choices

- `MainActivity` secures the window/locks on background/recreation; `VaultController`
  keeps session private; Cookbook screens call the guarded kitchen after unlock.
- Root uses Kotlin2.4.10/AGP9, JDK17 and SDK36. The standalone Sheets client uses
  Kotlin1.9.24/JVM21 and keeps its independent build. Root `:sheets-android` reuses
  canonical common sources/tests; desktop loopback OAuth tool stays outside Android.
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
preservation;401/403/cancel/LMK/task restoration remain Pending in phase04.
Run04/05 cover selected synthetic-jars biometric/Cook/reveal/copy/background flow.
Foreground clipboard adapter3/3 and [actual5-minute idle Run05](reports/session-timer-runtime.md)
and total04 Passed; API36 foreground proof only, broader UX Pending.
[Security/UI pixels01/authenticated02](reports/security-ui-runtime.md) Passed1/1 each,
2.321s/25.890s: selected EN/VI setup/manual Cook and secure shell captures with
genuine synthetic-jars authentication. Locked01/Capture01/Auth01 remain Failed;
Recents01 Failed206.052s without observed checkpoint stop/resume; Recents02
Failed182.457s at setup, before checkpoint/navigation; causes unconfirmed.
Run03 Passed1/1,137.789s with pause/stop/locked read/genuine reopening; owner confirmed
own card blank/covered. Same frozen04/APKs, no rebuild/install between attempts.
Owner revealed-Dish screenshot shortcut07 Passed; spoken TalkBack/full layout and orientation
remain Pending. [Offline cold02](reports/cold-process-runtime.md) Passed5.993s after
verified warm02 SIGKILL; provider cold02 Passed8.886s/VI OS-field01 Passed1.5s. No repair claim.
QR receiver/camera, Autofill spike/service, Drive, multi-profile authoring and
append/update screens remain separate later work. No reopened stack/write decision.

## Context and verification

[Roadmap/Q6/Q8/Q9](../../docs/mobile-app-brainstorm-qa.md), [settled decisions](../../docs/decisions/mobile-product-decisions.md),
[Core](../../docs/specs/mobile-core.md), [Keystore](../../docs/specs/mobile-keystore.md), [Sheets client](../../docs/specs/mobile-sheets-client.md).
Exact local commands and the Pending owner checklist are in phase5.

## Open dependencies
Google401/cancellation/later physical acceptance; no product question blocks offline code.
[Actual401 preparation](reports/google-http401-preparation.md): checkpoint audit Passed;
retained-grant tab-read checklist ready, permitted credential transition still Pending.
Whole Phase1 remains Partial until required connection/user/device flows have evidence.
