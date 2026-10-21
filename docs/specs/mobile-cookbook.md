# Android Cookbook

Status2026-10-15: **Code complete; locally verified**. Product acceptance remains Partial.
The [execution plan](../../plans/261012-mobile-cookbook-phase1/plan.md) records scope;
[owner acceptance](../runbooks/android-cookbook-acceptance.md) has selected Passed cases;
broader provider/device coverage remains Pending.

## Session and cooking

Reuse the existing encrypted Cookbook and shared Recipe kitchen. Manual untagged
Recipes work without Google. Tagged Recipes require an explicitly selected Menu;
absent/wrong context refuses cooking. Verification tags are metadata, not proof
that an account has been checked before cooking. No extra Argon2 derivation occurs
for each Dish. Only the required authenticated jar is copied for owned worker use.

Editing, navigation, newer requests, explicit/background lock and session expiry
invalidate the previous result. Publication/reveal/copy check current ownership.
The existing monotonic5minute idle/10minute total deadlines are retained. A new
Cook/reveal/copy does not create a new absolute session window.

## Private offline Menu

Public Recipes and private account metadata remain separate from encrypted jars.
Versioned atomic snapshots live under app-private `noBackupFilesDir`; metadata is
**not encrypted**. They contain Recipe, original Menu/tab/row, label and optional
username/URL/notes, plus favorites/recent entry IDs. No Dish, Spice, key or token
belongs in snapshots. Unknown/corrupt versions and malformed UTF-8 are refused.
Good snapshots survive failed writes and other corrupt cache files.

Limits are20 sources,1000rows,32columns,4096characters per cell and bounded payloads.
Actual parse-valid cells drive suggestions;≥3 examples and≥0.6 confidence can
propose a column, never confirm it. The Chef confirms unique roles and first data
row after preview. Headers are secondary EN/VI hints; ragged/no-header data works.
Confirmed header changes, ambiguous empty Recipe columns and invalid drift request
reconfirmation before cache replacement. Search uses labels/usernames; original
row identity and Menu context survive favorites/recent filtering.

## UI and clipboard

Compose exposes setup/unlock, Cookbook lookup, manual Cook, source/tab selection,
mapping preview and masked Dish actions. EN/VI strings and bounded list pagination
are included. Reveal hides after10seconds. Real Android clipboard writes mark a
Dish sensitive and report success only after the write succeeds.

Process-owned cleanup compares a unique clipboard label and content digest after
30seconds. It survives detail closure and Activity recreation; username/newer or
external copies must survive old cleanup. Background read restrictions defer
reconciliation until foreground. Process death and OS clipboard history are outside
the app's timing/removal guarantee. No plaintext Dish is retained by cleanup policy.
FLAG_SECURE and cold/background locking stay in place.

## Android Google connection

The root `:sheets-android` bridge compiles the canonical standalone client's
common sources/tests with Android engine support; it does not copy the client or
bundle desktop loopback OAuth. Explicit Connect uses Google's AuthorizationClient
and the settled Sheets read/write scope. This slice consumes metadata/read paths;
append/update authoring remains later work, deletion absent.

Tokens stay in RAM. Background/lock/source changes invalidate pending permission,
network and cache work. An external consent screen can lock the Cookbook; unlock
and Connect again after granting access. Reads preserve literal values, quote tab
names, bound HTTP/body/table sizes and close owned clients.401 requests reconnect;
403 explains denied access; failed refresh preserves last good offline data.

## Proof and deferred work

[Independent JVM/build/lint evidence](../../plans/261012-mobile-cookbook-phase1/reports/verification.md)
and [review](../../plans/261012-mobile-cookbook-phase1/reports/independent-review.md)
cover the frozen source; no unresolved P1/P2 remains in the implemented scope.
Compiled instrumentation is not executed UI acceptance. Selected actual Google grant/
reads/mapping/cache, offline error preservation, controlled process-death reopening,
Cook/reveal/copy, real foreground5/10minute timers, secure capture/Recents and owner
revealed-Dish screenshot refusal passed. Selected real host Android Back from Google
consent and stale callback after natural lock Passed17.949s with synthetic offline
cache/jars preserved; human cancellation/resultCode, different-Menu callbacks/401/403,
natural LMK/task restoration, broader clipboard/background and TalkBack/layout remain
Pending. Historical failures remain recorded. Prior Keystore proof retains its accepted scope.
QR receiving, Autofill, Drive, multiple-profile authoring and account-write screens
are separate follow-ups. Whole mobile Phase1 remains Partial.
