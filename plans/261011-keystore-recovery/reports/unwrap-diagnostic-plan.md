# Actual unwrap failure — minimal diagnostic plan

Date: 2026-10-11. Root explicitly authorized this focused planner stage now;
root routes builder → independent tester → reviewer after this artifact. No new
approval question. Planning writes only this artifact/owned review chronology.
Base: product0a99d63, documentation HEAD1350474, original E4/E5 failure handling.

## Actual blocker and scope

Postreboot `timer-total-20261011T071305213998Z.json` failed before timer start:
actual owned prompt requested at uptime1407.93; owner authentication around1602
then UI `Device key unavailable`. Numeric cause was not captured. Exact record
SHA256 remains894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6.
Idle5 physical pass remains valid; no total10/actual7/9 acceptance is inferred.
All parent device observers ended; temporary keepawake restored0.

Only distinguish actual Keystore unwrap doFinal failure from repository/cache
opening failure using existing sanitized debug diagnostics. No retry, new prompt,
fallback/bypass, hardware-policy, record-format, storage or session semantics change.
Earlier unclassified failures are not retroactively assigned a numeric cause.

## Source/test TODOs — exact bounded scope

- [ ] `app/src/main/kotlin/dev/passchef/mobile/VaultController.kt`: inside the
  existing authorized biometric worker/catch at115–121, initialize constant phase
  `unwrap-doFinal`. Split the existing expression without changing call order:
  `unwrapped = authorized.doFinal(record.wrappedKey)`, then phase `unwrap-cache`,
  immediately `opened = repository.unwrap(unwrapped)`. Keep existing epoch guards.
- [ ] Before unchanged retirement/session delivery use their existing constant
  phase labels if needed, so a later failure is not falsely called cache failure.
  Do not alter swallowed retirement failures or current safe UI guidance.
- [ ] Catch the existing Exception as `error` and call
  `WrapFailureProbe.record(phase, error) { ... }` before unchanged `fail(...)`.
  Inspector reads `crypto.info(record.alias).securityLevel` only on Build>=31;
  otherwise returns-1. Debug helper already catches inspector/logging failures.
  Do not add a second inspection or exception string/message output.
- [ ] `app/src/debug/kotlin/dev/passchef/mobile/WrapFailureProbe.kt`: add exactly
  two whitelist values `unwrap-doFinal` and `unwrap-cache`. The latter covers
  repository record read/validation plus cache authentication, not only AES-GCM.
  Preserve bounded class chains/API33 typed numeric fields and existing flags.
- [ ] `app/src/release/kotlin/dev/passchef/mobile/WrapFailureProbe.kt`: unchanged
  no-op API; release must neither emit diagnostics nor invoke key inspector.
- [ ] `app/src/androidTest/kotlin/dev/passchef/mobile/proof/WrapFailureProbeTest.kt`:
  add meaningful sentinel tests for accepted unwrap phases, class-only output for
  IllegalBlockSizeException/AEADBadTagException, no exception message/phase injection.
  Keep existing unknown-phase/failed-inspection assertions; no fake biometric proof.
- [ ] Shared repository/crypto and golden tests remain unchanged. Existing
  `VaultRepository.unwrap` owns supplied key and wipes on record/cache failure.
  New local references must not copy, log, persist or retain raw key/spice buffers;
  successful session ownership/delivery cleanup remains the existing path.
- [ ] Keep files under200 lines; no new dependency or generic diagnostic framework.

## Verification TODOs and truthful acceptance

- [ ] Builder runs impacted debug/test APK, release and lint with existing JDK17/
  SDK36. Independent tester reruns frozen impacted build; source/hash snapshots
  prove the scope stayed diagnostic-only. Prior56 shared JVM tests remain applicable
  if shared source is unchanged; no need to repeat unrelated tests for a probe.
- [ ] Independently inspect release artifact for absent PassChefProof/proof Activity
  and verify release no-op does not invoke inspector. Preserve debug `.demo` UID
  isolation, nonexported proof Activity and backup exclusions.
- [ ] Root pins APK SHA256/install-r while preserving existing Cookbook bytes.
  Run new formatter cases on actual device and report formatter-only scope.
- [ ] Root reproduces genuine owned biometric unwrap and captures only fixed phase,
  bounded classes/public numeric flags/security level from current PID. No raw
  logcat dump, errString, Throwable/message, alias, IV/key/cache/spice or credentials.
  A missing numeric field remains unclassified; code15 alone does not authorize retry.
- [ ] Record actual cause for the captured attempt; root decides any later repair
  separately. A successful unwrap must precede a new total10 timer anchor/probe.
  Diagnostic compilation/formatter pass never substitutes for timer/7/9 acceptance.

## Unresolved questions

Does the current actual unwrap fail in doFinal, repository/cache opening or later
delivery, and which public numeric cause is emitted? Implementation/proof pending;
this artifact makes no hardware fix, timer pass or merge-ready claim.
