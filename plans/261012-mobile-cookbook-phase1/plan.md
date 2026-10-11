# Android Cookbook — first production slice

Date: 2026-10-12 (Asia/Saigon). Status: **Pending — planning only**.
Baseline: main `ae9a702ba04ff37b316cba89634ba474d6de27a0`.
No implementation, approval, build, device run or completion is implied by this dossier.

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
| 1 | [Session-owned manual cooking](phase-01-session-cooking.md) | Pending | First useful offline path; shared JVM regressions + Android compile |
| 2 | [Private cache and column mapping](phase-02-cache-mapping.md) | Pending | Pure model/storage tests; no Google credential required |
| 3 | [Cookbook Compose screens and clipboard](phase-03-cookbook-ui.md) | Pending | Uses1–2; APK/test-APK/lint autonomous, actual UI/device proof Pending |
| 4 | [Android Sheets/authorization integration](phase-04-android-sheets-connection.md) | Pending | Root-platform bridge and real connection code; owner grant still required |
| 5 | [Independent verification and delivery](phase-05-verification-delivery.md) | Pending | Evidence separates code/build acceptance from physical/live acceptance |

Phases1–3 can produce useful real offline code without a phone/account. Phase4
can be built and tested against explicit protocol fixtures; such fixtures never
certify an Android Google connection. Overnight work has no guaranteed finish time.

## Current implementation constraints

- `MainActivity` already secures the window and locks on background/recreation;
  `VaultController` keeps its owned session private; `VaultScreen` only sets up/unlocks.
- Root uses Kotlin2.4.10/AGP9, JDK17 and SDK36. The standalone Sheets client uses
  Kotlin1.9.24/JVM21, has no Android target and is absent from root settings.
  Its desktop loopback OAuth tool is not an Android authorization adapter.
- App manifest currently has biometrics only; networking needs explicit INTERNET.
  Dependency versions/Android engine compatibility must be verified during phase4.
- Available SDK has no verified emulator executable. Compiling instrumentation
  is feasible; running it cannot be claimed without an authorized device/runtime.

## Owner-dependent acceptance and deferred work

Android OAuth client/package/signing configuration, consent and a real authorized
Sheets read remain Pending. The public browser Cookbook grants no Android token.
Biometrics, actual clipboard/reveal/lifecycle/5-minute idle/10-minute total and
phone UX need authorized physical acceptance; prior demo proof is not new UI proof.
QR receiver/camera, Autofill spike/service, Drive, multi-profile authoring and
append/update screens remain separate later work. No reopened stack/write decision.

## Context and verification

[Roadmap/Q6/Q8/Q9](../../docs/mobile-app-brainstorm-qa.md),
[settled decisions](../../docs/decisions/mobile-product-decisions.md),
[Core](../../docs/specs/mobile-core.md), [Keystore](../../docs/specs/mobile-keystore.md),
[Sheets client](../../docs/specs/mobile-sheets-client.md).
Exact local commands and the Pending owner checklist are in phase5.

## Open dependencies

Owner Android Google setup/consent and later physical acceptance; no product
question blocks the offline implementation. Whole roadmap Phase1 remains Partial
until real connection and required user/device flows have evidence.
