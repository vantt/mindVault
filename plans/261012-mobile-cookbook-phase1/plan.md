# Android Cookbook — first production slice

Parent: [project master](../261022-project-work-queue/plan.md) → [Mobile track/task ownership](../261022-project-work-queue/phase-02-mobile.md). This plan retains its existing five phases and runtime reports.

Date:2026-10-12; updated2026-11-06 (Asia/Saigon). Status: **Partial — code/local checks Done; owner acceptance Pending**. Baseline: main `2579e29044924e1582b78414b0314baffc72abe5`.
Latest accepted4November: [physical401](reports/google-http401-review-20261104.md#actual-physical-retained-grant401-run01--passed) independently Passed105.047s; [grant restoration](reports/google-grant-restoration-runtime-20261104.md) Passed33.115s after local rejected-token repair/JVM16; earlier failures retained. [Proposal/limits/capacity](reports/cache-capacity-status-20261104.md) UI4, [API26](reports/api26-clipboard-runtime-20261104.md)/[API27](reports/api27-clipboard-runtime-20261104.md) clipboard4+4/VM cleanup, selected [TalkBack](reports/talkback-runtime-20261104.md), [authenticated recreation](reports/authenticated-recreation-runtime-20261104.md) and emulator [saved-task](reports/saved-task-runtime-20261104.md) accepted; broader phone/Google gates Pending.
6November [actual continuation](reports/nov6-resume-runtime-20261106.md): selected200%/landscape EN27/VI29 accepted separately; grid29 independently Passed9points/language/18toggles. Physical saved-task29 attempt02 independently Passed warm/password-cache-Cook/cleanup; attempt01 Failed retained. Selected663s background/manual cached Cook independently accepted; original native timeout retained. Google candidate30 compiled/installed/reviewed; early owner revocation restored Passed; human setup Failed retained. Human/different-Menu and OS-LMK remain Pending. [Isolated delivery](reports/delivery-verification-20261106.md) passed116 fresh JVM cases/build/lint/release exclusion; different delivery APKs have no inherited phone acceptance.
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
| 3 | [Cookbook Compose screens and clipboard](phase-03-cookbook-ui.md) | Partial | Selected200%landscape EN27/VI29 separately accepted; rectangle-grid29 independently Passed; selected physical task/background accepted |
| 4 | [Android Sheets/authorization integration](phase-04-android-sheets-connection.md) | Partial | Google/cache/network-off/host OS-Back/selected403/401/grant restoration Passed; human cancel/different-Menu Pending |
| 5 | [Independent verification and delivery](phase-05-verification-delivery.md) | Partial | Scoped main push `2748d0b` Done; required physical/live acceptance Pending |

Phases1–3 support real offline code without phone/account. Phase4 protocol fixtures
permit build/tests but never certify Android Google connection. Exact local evidence:
[Verification](reports/verification.md), [independent review](reports/independent-review.md), [isolated code checkpoint](reports/isolated-mobile-checkpoint-20261104.md).

## Verified integration choices

- `MainActivity` secures the window/locks on background/recreation; `VaultController`
  keeps session private; Cookbook screens call the guarded kitchen after unlock.
- Root uses Kotlin2.4.10/AGP9, JDK17 and SDK36. The standalone Sheets client uses
  Kotlin1.9.24/JVM21 and keeps its independent build. Root `:sheets-android` reuses
  canonical common sources/tests; desktop loopback OAuth tool stays outside Android.
- App manifest adds INTERNET for the real Android Sheets engine. AuthorizationClient
  and Compose test dependencies are pinned and compile in the actual app.
- Separate Windows API36.1 emulator now verified: selected offline UI/password,
  VI OS association and EN/VI secure pixels at font2/landscape Passed. This does
  not replace physical/provider acceptance; original no-emulator reports stay dated.

## Owner-dependent acceptance and deferred work

Owner confirmed real Google consent; [Google runtime](reports/google-runtime.md)
records Google Run03 Passed metadata/rows/mapping/cache-file load after Failed Run01/02.
[Cache UI Run02](reports/google-cache-ui-runtime.md) Passed lock/reopen/cached Cook
and authenticated same-process recreation; app-disconnected cache only. Later
[Network-off Run04](reports/network-off-runtime.md) Passed real offline Cook/error
preservation; human cancel/OS-LMK remain Pending; selected physical task/background and later401/restoration accepted above.
Run04/05 cover selected synthetic-jars biometric/Cook/reveal/copy/background flow.
Foreground clipboard adapter3/3 and [actual5-minute idle Run05](reports/session-timer-runtime.md)
and total04 Passed; API36 foreground proof only, broader UX Pending.
[Security/UI pixels01/authenticated02](reports/security-ui-runtime.md) Passed1/1 each,
2.321s/25.890s: selected EN/VI setup/manual Cook and secure shell captures with
genuine synthetic-jars authentication. Historical failures remain in linked reports;
Recents03 Passed137.789s with genuine reopening and owner-confirmed hidden card.
Owner revealed-Dish screenshot shortcut07 and selected spoken TalkBack Passed; full physical
layout/orientation remain Pending. [Offline cold02](reports/cold-process-runtime.md) Passed5.993s after
verified warm02 SIGKILL; provider cold02 Passed8.886s/VI OS-field01 Passed1.5s. No repair claim.
QR receiver/camera, Autofill spike/service, Drive, multi-profile authoring and
append/update screens remain separate later work. No reopened stack/write decision.

## Context and verification

[Roadmap/Q6/Q8/Q9](../../docs/mobile-app-brainstorm-qa.md), [settled decisions](../../docs/decisions/mobile-product-decisions.md),
[Core](../../docs/specs/mobile-core.md), [Keystore](../../docs/specs/mobile-keystore.md), [Sheets client](../../docs/specs/mobile-sheets-client.md).

## Open dependencies
Google cancellation/physical acceptance remain; offline implementation unblocked.
[Actual401 preparation](reports/google-http401-preparation.md): checkpoint audit Passed;
retained-grant401 Run01 and post-revocation normal SDK restoration accepted.
Whole Phase1 remains Partial until required connection/user/device flows have evidence.
