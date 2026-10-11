# Phase02 — Private offline Menu cache and confirmed columns

Status: **Done — implemented and independently verified locally**. Priority:2.

## Context and key insight

[Q3/Q6](../../docs/mobile-app-brainstorm-qa.md), [Core](../../docs/specs/mobile-core.md).
Recipes are not Spices, but labels/usernames/URLs/notes remain private account
metadata. Store in app-private backup-excluded files; never external/shared storage.
Column proposals come from the existing parser and actual cell contents, not a
required header schema. Confidence suggestions never bypass user confirmation.

## Requirements and architecture

Bounded table rows → local detector → preview/confirmed `spreadsheetId + tab`
mapping → immutable entries with original row identity → atomic private snapshot.
An entry keeps Recipe/Menu/tab/row, label and optional username/URL/notes, never Dish,
master key, token or Spice. Preserve leading zeroes and Unicode strings exactly.
Keep label/username search, local favorites/recent entry IDs and source freshness.
Do not infer account identity from URL text or implement Autofill/domain trust here.

## Related files

- Add `shared/src/commonMain/kotlin/dev/passchef/mobile/cookbook/` focused models,
  detector, mapping validator, row projection and search rules; add JVM tests.
- Add `shared/src/jvmAndroidMain/.../cookbook/` bounded snapshot codec/store + tests.
- Add app `cookbook/` repository/source coordinator using `noBackupFilesDir`.
- Reuse actual `RecipeParser`; do not broaden grammar or rewrite cooking fixtures.
- Keep the existing encrypted `cookbook-v1.json` separate and byte-compatible.

## Implementation steps / TODO

- [x] Model source/tab/confirmed column roles/header offset and stable original row ID.
- [x] Score candidate Recipe columns using parse-valid/nonempty ratio and sufficient
  examples (Q3 suggests≥0.6 and≥3); ties/low evidence require explicit column choice.
- [x] Use EN/VI header hints only as secondary evidence; support no-header/ragged rows,
  non-first data row, duplicate labels and single Recipe-only columns.
- [x] Offer live3–5-row preview and assign Recipe/label/username/URL/notes/ignored roles;
  one column cannot silently serve incompatible roles. Manual labels stay possible.
- [x] Persist versioned, size-bounded mappings/snapshots atomically; reject corrupt,
  unsupported or traversal-shaped identifiers without overwriting a good cache.
- [x] Refresh into a staged snapshot, detect mapping drift before replacement and
  keep last good offline data on network/token/server failure or interrupted write.
- [x] Preserve source/tab origin across search/favorites; never cook an entry against
  another Menu's selected context. Empty successful ranges have explicit empty state.

## Success criteria and positive proof

- [x] Real parser tests cover headers/no headers, ambiguous/tied columns, sparse/ragged
  rows, Unicode usernames, leading zeroes, duplicates, invalid cells and tagged context.
- [x] Real temporary-file tests prove reload, bounded decoding, failed atomic replacement,
  corrupt snapshot refusal and independent mapping per Menu/tab; no Dish/jar/token persisted.
- [x] Search uses labels/usernames, not ingredient strings; clear-source removes only
  selected local cache/mapping, never Google rows or encrypted jars.
Use phase5 shared JVM tests; supplied synthetic table fixtures are explicit protocol
inputs, never a claim that the real owner Google data was fetched.

## Risks, security and next steps

Fail closed on mapping drift; a plausible label is not authorization to cook.
Private cache is protected by sandbox/backup exclusion; do not promise plaintext
metadata is cryptographically encrypted. UI stays gated by Cookbook unlock.
Continue to phase03 screens; phase04 supplies real network snapshots later.

Implementation exists in the production app/shared/client paths above. Final local
results and unchanged Golden evidence are recorded in [verification](reports/verification.md);
compiled physical tests remain unexecuted. No phone/account grant was used tonight.
