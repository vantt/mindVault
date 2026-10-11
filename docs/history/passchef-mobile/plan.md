---
artifact_contract: bee-plan/v1
mode: high-risk
approved_gate2: 2026-07-28
---

# Plan: Track B — Mobile Core (KMP, no OAuth)

Mode: `high-risk` — 2 risk flags: **audit/security** (bundle decrypt handles the user's password-vault secrets directly; Argon2 is the unlock-key primitive), **cross-platform** (first-ever mobile/KMP code in this repo). Story-sized (3 backlog PBIs, a from-scratch module).
Why this is the least workflow that honestly protects the work: crypto correctness on a secrets-handling path is the textbook hard-gate case (per bee-hive's mode gate: "any hard-gate flag" forces high-risk regardless of flag count) — a `standard` lane's lighter validation would let a subtly wrong PBKDF2/AES-GCM or Argon2 port ship unnoticed until a real user's vault failed to decrypt.

## Requirements (from CONTEXT.md)

- D1: KMP core + Compose UI, Android-first; iOS deferred. This slice touches only the KMP-core/Android-first part — no Compose UI.
- D5: No KMP scaffold exists yet — bootstrapping it (android + jvm targets only) is this slice's first unit of work.
- D6: p-542801dc's decrypt must be proven against a real extension-produced fixture, generated and sequenced *before* the decrypt-implementation cell.
- D7: p-c2ad072e's Argon2id spike must match argon2-browser WASM byte-for-byte for a fixed test vector — BouncyCastle `Argon2BytesGenerator`, version 0x13, hashLen 32/time 3/mem 65536 KiB/parallelism 4, UTF-8 password encoding, empty secret/AD.
- D8: p-6dee7ae8's recipe-engine port must reproduce all 12 `@approved` golden tests in `generate_password.spec.mjs` byte-for-byte.
- D2/D3/D4: carried-forward context only (Sheets write scope, QR transport, add-account UX) — out of scope for this slice's own cells; no cell here implements them.

## Discovery

L1 (quick verify, cited — no separate discovery.md needed): confirmed via direct source reads (bee-gather agent, see approach.md's Files section for exact anchors) that `_combine()`/`_interleave()` are pure string ops with no crypto dependency, that `createFullAccessBundle`/`decryptBundle` use PBKDF2-SHA256 (100000 iter) + AES-GCM (12-byte IV, 16-byte salt), and that `argon2_adapter.js` uses Argon2id/32/3/65536/4. Confirmed (`grep -c "@approved"`) exactly 12 golden tests, not 13 as first assumed (caught and fixed during CONTEXT.md's fresh-eyes review). Library/packaging discovery (BouncyCastle direct-class-usage avoiding the Android JCA-provider conflict; argon2kt/ionspin rejected) is real comparative (L2-shaped) work — written up in `approach.md` per the fan-out table's high-risk rule, not restated here.

## Approach

See `docs/history/passchef-mobile/approach.md` (recommended path, rejected alternatives, risk map, file order, validating questions) — graduated to its own file per the high-risk fan-out rule (decision 0009); this section is a pointer, not a restatement.

## Shape — Epic map

**Feature outcome:** a KMP shared module that (a) generates passwords byte-identical to the extension for any recipe/secret, (b) decrypts a real `createFullAccessBundle` JSON on-device, and (c) has a proven Argon2id implementation ready for the later Keystore/unlock-key slice.

**Repo-reality basis:** greenfield — no `mobile/`, no gradle/kotlin anywhere in the repo today (verified). All three epics build on one shared scaffold (Epic 0).

| Epic | Capability/Risk Area | Why It Exists | Slices | Proof Needed |
|---|---|---|---|---|
| 0 | KMP scaffold bootstrap | Every other epic needs a compiling module to land code in | 1 slice: Gradle multiplatform project (android+jvm), empty smoke test | `./gradlew :mobile:shared:jvmTest` succeeds (even with zero real tests) |
| 1 (p-6dee7ae8) | Recipe engine port | Core password-generation logic must exist on-device before any UI can use it | 1 slice: tag-codec → parser → generate_password → golden tests | All 12 ported golden tests pass on `jvmTest` |
| 2 (p-542801dc) | Bundle decrypt | Mobile must be able to read what the extension exports via QR (D3) | 2 slices: (a) real fixture generation, (b) decrypt implementation + verification against (a)'s fixture — (b) depends on (a) per D6 | Fixture decrypts to the exact known plaintext/password pair |
| 3 (p-c2ad072e) | Argon2id parity spike | De-risks the Argon2 backend choice before the later Keystore/unlock-key slice depends on it | 1 slice: fixed test vector, KMP output vs argon2-browser WASM output | Byte-for-byte hash equality on the fixed vector |

**Slice queue (current slice = all four rows above; no future-slice cells written):**

1. Epic 0 → Epic 1 and Epic 3 can start in parallel once Epic 0 lands (no dependency between 1 and 3).
2. Epic 2a (fixture) can start as soon as Epic 0 lands (independent of 1/3); Epic 2b depends on 2a.
3. Feasibility status: Epic 0/1 LOW risk (validating's reality check suffices); Epic 2/3 MEDIUM-HIGH — validating must produce concrete proof (a real Gradle dependency resolution for BouncyCastle on both targets, and ideally a hand-run smoke check of PBKDF2/AES-GCM round-trip against one real extension-produced ciphertext) before cells for 2/3 are trusted as feasible.

## Test matrix

Full pass over the 12 edge dimensions at high-risk depth (probes written out per dimension that applies; "n/a" where the dimension doesn't touch this slice):

| Dimension | Applies? | Probe |
|---|---|---|
| Empty/null input | Yes | Recipe parser: empty string, `null` → `parse()` returns `null` (matches JS null-return contract) |
| Boundary sizes | Yes | Recipe with 0 modifiers vs all 4 (`_!?~`); bundle with empty `secrets` object |
| Malformed/invalid input | Yes | Invalid recipe (bad regex match) → same rejection as extension; bundle with wrong `sharingPassword` → decrypt throws (D6: mirrors "Incorrect sharing password") |
| Concurrency | n/a | No concurrent-write path in this slice (pure functions + one-shot decrypt) |
| Idempotency | Yes | `generate_password.execute()` called twice with same recipe/secret → identical output (pure function) |
| Ordering | n/a | No ordered-collection logic ported in this slice |
| Partial failure / rollback | n/a | No multi-step transactional write in this slice |
| Auth/permission boundaries | n/a | No auth in this slice (per user's explicit "no OAuth" scope) |
| Encoding/locale/unicode | Yes | Recipe/secret strings with non-ASCII characters — UTF-8 byte handling must match JS's UTF-16→UTF-8 behavior exactly (D7's own encoding note applies here too) |
| Time/timezone | n/a | No timestamp-dependent logic in the ported functions themselves (`exportedAt` is passthrough, not computed) |
| Version/compatibility | Yes | Bundle `version: "1.0"` field — decrypt path should reject/flag an unrecognized future version rather than silently mis-parsing (mirrors extension's `validateBundle`) |
| Resource exhaustion | Yes (Epic 3 only) | Argon2 at mem=64MB/time=3 must not OOM or timeout on a real Android device profile — noted as a validating question, not solved by this slice's spike itself (spike proves output correctness, not device performance) |

## Out of scope

- Sheets API client, OAuth, Google Cloud project setup (separate backlog items, D-locked as excluded from this slice's boundary).
- Android Keystore secret storage and biometric unlock (p-8b12c072) — the Argon2 spike (Epic 3) only proves the primitive works; wiring it into real on-device key storage is a later slice.
- Any of the 6 real mobile UI screens (p-c46b217c) or the prototype screens already built in `docs/designs/`.
- iOS/Kotlin-Native target and its own Argon2 backend (deferred per D1; tracked separately as PBI p-23837c9e).
- Drive appData transport (p-e117d9aa) — QR/`createFullAccessBundle` only, per D3.
