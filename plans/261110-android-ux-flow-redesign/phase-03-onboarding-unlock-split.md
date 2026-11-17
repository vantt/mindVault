# Phase 03 — Onboarding / unlock split

## Context links
- Spec §3.1, §3.2, OB/SU/UL screens, D3/D6 · audit #2, #9, #13, #15, #20 · 03-P1–P7

## Overview
- Priority: P1. Status: Done (2026-11-10); fingerprint unlock verified on device (V12) — [report 08](reports/08-implementation-verification.md). Depends 02.
- Split `VaultScreen` into onboarding slides, setup wizard (master + confirm + ack, Spice jars, fingerprint), and biometric-first unlock.

## Key insights
- Returning users see 6 fields today; first-timers lack confirm + jar guidance.
- UI must not infer setup from strings/repo in composition → controller exposes `vaultPhase`.

## Requirements
- Functional: `vaultPhase` NEW / LOCKED_BIOMETRIC / LOCKED_PASSWORD; E1–E4 explainer skippable (E1 interactive tear: toggle + auto-tear once + replay, reduced-motion instant swap, verdict liveRegion; E2 stranger view; E3 by hand; E4 secret parts CTA), example rand0m$1 → rand0mBasic*; SU1 confirm + ack; SU2 1–5 jars add/remove; SU3 enable fingerprint or info; UL1 auto BiometricPrompt once per lock event; UL2 password; degraded messages shown before leaving gate; localized BiometricPrompt.
- Non-functional: `remember` only for secrets, cleared on background; Argon2id busy state; 200% font + VI fit.

## Architecture
`VaultController` publishes `vaultPhase` (computed off-composition). Gate composable switches `OnboardingPager` / `SetupWizard` / `UnlockScreen`. Setup calls existing setup API with collected jars.

## Related code files
- Modify: `VaultController.kt` (phase flag, one-time notice), `VaultScreen.kt` (→ thin router or delete), `MainActivity.kt` (localized prompt, auto-launch hook)
- Create: `vault/onboarding-concept-slides.kt`, `vault/setup-master-password-step.kt`, `vault/setup-spice-jars-step.kt`, `vault/setup-fingerprint-step.kt`, `vault/unlock-biometric-first-screen.kt`

## Implementation steps
1. Add `vaultPhase` + tests. 2. Slides w/ sample + step dots. 3. SU1–SU3. 4. UL1/UL2 with auto-prompt guard (no relaunch after cancel). 5. Strings EN/VI (spec §4). 6. testTags new + keep existing.

## Todo
- [x] vaultPhase  - [x] slides  - [x] SU1  - [x] SU2  - [x] SU3  - [x] UL1/UL2  - [x] strings EN/VI  - [x] tests

## Success criteria
Returning user unlock = 1 touch; password path ≤2 taps; setup has confirm + ack; no Spice fields on unlock; all degraded messages visible.

## Risk assessment
Auto-prompt loop on cancel → once-per-lock guard. Proofs typing into old fields → keep tags on equivalent fields.

## Security considerations
No change to crypto, Keystore, 3-fail rule, no PIN fallback. Jar values never re-displayed.

## Next steps
Phase 05 KT3 Spices (D8) reuses SU2 components.
