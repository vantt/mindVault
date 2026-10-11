---
area: mobile-core
updated: 2026-10-11
sources: [passchef-mobile]
coverage: partial
---

# Mobile recipe kitchen

The shared kitchen lets an Android or JVM caller cook the same dish as the
browser extension from a recipe, seasoning, and optional Menu context. It
contains no screens, persistent seasoning storage, Google connection, or camera.
The caller supplies seasoning lookup and a tag verifier; own profiles use raw
seasoning, while an existing shared-profile caller may supply effective seasoning.

## Cooking rules

- Recipes use an ASCII ingredient, one cooking position, a spice index1–5,
  optional modifiers, and an optional four-character verification tag.
- Invalid recipes and missing seasoning fail explicitly. Duplicate modifiers
  apply once; seasoning transforms run strip-special, reverse, then uppercase.
- Positions support topping, suffix, middle, character mixing, and pair mixing.
  Position reversal exchanges topping/suffix. UTF-16 behavior matches the browser.
- A tag checks the raw seasoning and Menu context. It does not change the dish.
  Missing or wrong context on a tagged recipe refuses to cook. Untagged recipes
  with a Menu context return the existing legacy warning.

## Imported seasoning bundles

The decryptor accepts the extension's original full-access bundle JSON v1.0.
It checks type/version and byte fields, derives the sharing key, authenticates
and opens the package, and returns its seasoning map in memory. Wrong sharing
passwords, tampered packages, or malformed inputs fail; plaintext is not logged.
Derived key bytes and plaintext byte buffers are cleared after use. This does
not promise complete removal of immutable strings from managed runtime memory.

Compact QR payload decoding and frame reassembly are separate future work;
this decryptor accepts the original bundle only. The caller owns any subsequent
secure persistence and session locking.

## Evidence and boundaries

The twelve approved browser assertions are ported literally. Additional vectors
come from the real JavaScript kitchen and include Vietnamese, emoji, isolated
surrogates, tags, and modifier behavior. A synthetic full-access fixture is
produced by the real extension adapter before decrypt implementation.

Argon2id correctness is a test-only proof against the real browser WASM package:
v0x13,32-byte result,time3,memory65536KiB,parallelism4,UTF-8 input. BouncyCastle
is confined to the test dependency; no production Argon2 cooking API ships here.
The later Keystore plan consumes this backend proof and adds production storage.

See [current recovery report](../history/passchef-mobile/reports/mobile-core-recovery.md)
for observed build/tests and limitations. Android compilation establishes package
compatibility; hardware-backed storage and biometric behavior need their own
physical-device acceptance in the Keystore worktree.
