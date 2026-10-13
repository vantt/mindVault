# Android Google acceptance preparation

Date2026-10-13 (Asia/Saigon). **Prepared; actual Google tests0/Pending**.
Owner reports Android OAuth client configured. Sheets API/test-user configuration,
actual Android consent/usable authorization and provider read remain unconfirmed.
Dedicated synthetic Menu URL and ready owner are required; mobile stays **Partial**.
Historical preparation snapshot. [Later runtime](google-runtime.md) installed this
exact candidate and executed Google Run01/02 Failed; usable grant/provider reads
observed, fixed fixture/schema/mapping/cache still unaccepted.

## Local proof and exact artifacts

Independent review: no P1/P2. Frozen fixture157lines. Root-only incremental test-APK/
lint build **Passed59s**,10 executed/84 UP-TO-DATE; lint0 errors/11 warnings/1 hint.
Compilation is not a Google runtime pass; existing production/Golden remain unchanged.

| Artifact | SHA256 |
|---|---|
| Frozen Google fixture | `8b19ef0eca500afd935fd8cf1ae668341863e774f6b6d9e71150b16be033be13` |
| Local prepared test APK, not installed | `5eb335d423d6712809ddbe513b7171958dda517317e361d6f7d4f1fac33908d1` |
| Unchanged local/installed debug APK | `d4eb8401d0b2cc71f9ca800614dc7f7d8cf391d3e3432cd6a7537ce91382f9a4` |
| Still-installed recreation test APK | `a2b1622ed2f857bb2c70a5a1698206ab134599c0816cb7e829f4a65792907680` |

No new APK install/runtime attempted. Current owner Cookbook digest remains
`894f73e51e316b6a369368ad4cebbc901609ccfe7eabe855aa73c2cbf4e7a4d6`.
Evidence:`/tmp/passchef-cookbook-device-acceptance/google-acceptance-preparation-build.log`
and `google-acceptance-preparation.json`; JSON records exact local/installed identities.

## Prepared selected flow

Class:`dev.passchef.mobile.proof.GoogleCookbookPhysicalTest`.
Runtime-only argument `passchefGoogleMenuURL` is required; `passchefGoogleTab`
defaults to `Test`. Dedicated header:Label/Recipe/Username; row:Mail-test/abcd#1/00123.
Format Username as plain text to preserve the literal00123. No Menu URL logged.

Actual isolated setup/owner biometric → Sources/Connect → Android authorization →
real metadata/matching Menu ID → selected tab origin/known row → explicit columns
1/2/3 and first row2 → confirmed cache/file load. Encrypted synthetic jars must stay
byte-identical. Natural external-consent background lock is retained: only one normal
owner-biometric unlock/fresh Connect retry is allowed if the actual returned UI locks.
Pause-only flows can accept matching metadata directly; no forced lock/retry.

No token inspection/injection, simulated grant, offline fallback or production change.
Waits are bounded; failure remains Failed with sanitized phase/booleans/class only,
never an original exception/Compose tree/Intent/private value. Cleanup closes the proof
Activity and removes only its exact record aliases and unique private directory.

## Remaining acceptance

Install/verify the prepared test APK with digest-preserving preflight, confirm actual
Awake/keyguard=false and ready owner, then run only this class with the supplied
synthetic Menu. Record Passed/Failed/Pending from actual runner evidence, not native
exit0 or compilation. No biometric/Google request belongs to this preparation.

A future success may use an existing Google grant; record any freshly observed
consent screen separately. Cache-file load is not UI reopening/disconnect acceptance.
Actual grant/read/cache runtime, complete Google configuration and broader phone
matrix remain Pending. Run01 cause remains unknown/not reproduced; no production
repair or whole-mobile Done/archive claimed. [Phase04](../phase-04-android-sheets-connection.md).
