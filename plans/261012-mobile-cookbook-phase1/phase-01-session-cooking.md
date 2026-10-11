# Phase01 — Session-owned manual Recipe cooking

Status: **Pending**. Priority:1. No source change or test run recorded.

## Context and key insight

[Core](../../docs/specs/mobile-core.md), [Keystore](../../docs/specs/mobile-keystore.md),
[Q6 fallback](../../docs/mobile-app-brainstorm-qa.md#q6--đi-sâu-ui-vault-phase-1).
`VaultController` owns a private `VaultSession`, serial worker, operation epoch and
`SessionClock`; the screen cannot yet cook. The kitchen already parses/transforms
and verifies tags. Keep the existing encrypted record and device-key recovery.

## Requirements and architecture

UI request → session/epoch/deadline guard → short-lived seasoning provider →
existing `GeneratePassword`/`TagCodec` → owned result → current foreground UI.
Never expose the master key or retain a public long-lived map of Spices.
Paste an untagged Recipe without Google; a tagged Recipe needs explicitly selected
Menu context and refuses absent/wrong context. Legacy warning remains visible.
Cooking must not derive Argon2 again or reset the absolute session deadline.

## Related files

- Modify `app/src/main/kotlin/dev/passchef/mobile/{VaultController,MainActivity,VaultScreen}.kt`.
- Add bounded controller/helper files for cooking/result lifetime under the same package.
- Add pure session-cooking ownership policy under `shared/src/commonMain/.../cookbook/`
  and its JVM regressions; keep Android session access in the app controller.
- Reuse `shared/.../recipe/{GeneratePassword,CookingPorts,TagCodec}.kt` unchanged.
- Delete no vault data, aliases, Golden tests or parity fixtures.

## Implementation steps / TODO

- [ ] Define safe `Locked/Ready/Busy/Refused` result state, typed errors and request ownership.
- [ ] Add a bounded session accessor/action that checks foreground, epoch and monotonic
  expiry before reading jars, and checks again before publishing a Dish.
- [ ] Decode only required jar bytes for the kitchen call; discard temporary buffers
  and result references on invalidation. Do not claim immutable-string zeroization.
- [ ] Connect manual paste + optional saved Menu context to the existing kitchen.
- [ ] Clear old Dish/Copy immediately on every edit, invalid Recipe, lock or newer request.
- [ ] Route background/recreation/explicit lock/timeout through one cleanup callback;
  no result or Spice value goes into rememberSaveable, saved state, logs or disk.
- [ ] Keep existing setup/recovery and code15/lockout behavior; preserve old record bytes.

## Success criteria and positive proof

- [ ] Real existing kitchen returns literal expected Dishes for known synthetic inputs;
  exact tagged match succeeds, wrong/missing Menu refuses, missing jar/invalid Recipe refuses.
- [ ] Deferred old cook completion after edit/lock/background/reopen cannot publish/copy.
- [ ] Expiry is checked before access and publication; cooking cannot extend total lifetime.
- [ ] Original shared cooking/Unicode/cache/security tests remain unchanged and pass.
Use phase5 `:shared:jvmTest :shared:assemble` and app compile, with real kitchen
and pure clock/ownership regressions. These do not prove physical authentication.

## Risks, security and next steps

Never move key ownership into a Compose screen or pass raw jars to a saved model.
The worker/session cleanup must serialize access to avoid wiping a jar during use.
Physical background/timer acceptance stays Pending. Continue with cached Menu
entries and Compose presentation; one owned cooking path serves both paste and list.
