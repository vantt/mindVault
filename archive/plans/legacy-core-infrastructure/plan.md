# Implementation Plan - Phase 1: Core Infrastructure (Clean Architecture)

**Goal:** Establish the foundational security and storage layer using Hexagonal Architecture (Ports & Adapters) to ensure testability and separation of concerns.

## Current completion — 2026-10-12

**Status: Done — current Core scope.** Ports/adapters and composition root live
under `chrome-extension/src/`; Recipe cooking uses parser/storage injection and
Argon2id/AES-GCM at the storage boundary. The original proposal and verification
criteria below remain historical; old names, root paths and Jest are superseded
by Recipe naming, current monorepo paths and Vitest. Ingredient changes replace
the old proposed recipe-version rotation mechanism.

- **Infrastructure verified:** independent147-unit and22-headed-E2E suites passed.
  Golden tests remain unchanged. Genuine bundled offscreen Worker authentication,
  encrypted storage, current UI and regression controls have runtime evidence.
- **Former remaining Core acceptance is Done:** the actual public synthetic Google
  Cookbook on Chromium143.0.7499.4 passed8/8 native/provider cases at
  `2026-10-11T18:00:35.046Z`: native selected-cell command, real toolbar popover/Copy,
  Builder/live parity, matching Tag, wrong-profile refusal, empty-cell refusal and
  persisted synthetic fixture. `nativeFallback:false`; this proof does not depend
  on the old route-served fixture/direct-worker fallback test.
- **Historical mechanisms reconciled:** focused editor whitespace is normalized
  before formula-bar fallback; on-demand extraction satisfies the accepted Menu
  behavior. No continuous observer or duplicate generation/ICrypto signature is
  required merely to reproduce the old proposal.
- **Limits:** broader provider/browser/OS measurements, cross-device Sync/conflicts
  and Chrome Web Store/release acceptance belong to the Partial MVP roadmap.
  They do not keep this bounded infrastructure/Core plan open or imply full MVP
  completion. No merge/publication action is claimed by this completion entry.

Canonical source/test/provider evidence and current verification counts:
[current extension verification](../../../plans/261012-extension-completion/reports/verification.md).
The earlier 2026-10-11 audit correctly recorded native Google acceptance as
pending at that time; the new genuine native proof closes that specific gap.

## Historical criteria

[Original architecture and verification proposal](historical-proposal.md) is
retained without treating obsolete mechanisms as new work.
