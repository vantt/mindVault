# Android Cookbook

Status2026-11-06: **Code/local delivery verified; product acceptance Partial**.
The [existing plan](../../plans/261012-mobile-cookbook-phase1/plan.md) and
[owner checklist](../runbooks/android-cookbook-acceptance.md) keep each candidate's
accepted/failed/unexercised scope. [Isolated delivery verification](../../plans/261012-mobile-cookbook-phase1/reports/delivery-verification-20261106.md)
records fresh116 JVM cases, APK builds, lint0errors/8warnings/1hint and release
proof exclusion. These different delivery APKs have not been device-accepted.

Selected [Nov4](../../plans/261012-mobile-cookbook-phase1/reports/google-grant-restoration-runtime-20261104.md)
physical401 and usable-grant restoration passed; proposal/limits/capacity and
API26/27 clipboard passes remain emulator-scoped. [Nov6](../../plans/261012-mobile-cookbook-phase1/reports/nov6-resume-runtime-20261106.md)
accepts EN27/VI29 speech/focus separately at200%landscape, the110%portrait nine-point
language grid, physical controlled saved-task/password L1 and selected manual663s
background return. Early30 human cancellation Failed before Google; mandatory
normal restoration independently Passed46.722s. Human cancellation/different-Menu
and actual OS-LMK remain Pending. Old failures/unknown causes are retained.

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
Good snapshots survive failed writes and other corrupt cache files. A valid21st
Menu reports CACHE_FAILED when the20-source store refuses the save; it does not
ask the Chef to change an otherwise valid column mapping.

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
30seconds. Below API28, successful cleanup leaves an empty ClipData; it need not
make hasPrimaryClip false. It survives detail closure and Activity recreation; username/newer or
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
Accepted-current401 also evicts the rejected token from Google's local cache. A
later explicit Connect waits for successful eviction and rechecks ownership and
readiness; failure stays disconnected and can retry local clearing on a later
Connect. No automatic authorization/read loop or remote grant revocation is added.

## Proof and deferred work

[Earlier verification/review](../../plans/261012-mobile-cookbook-phase1/reports/verification.md)
and [current delivery checks](../../plans/261012-mobile-cookbook-phase1/reports/delivery-verification-20261106.md)
are local source/artifact evidence. Compiled instrumentation is not device runtime.
Selected real Google reads/mapping/cache, network-off and403 preservation, retained401
and restoration, Cook/reveal/clipboard,5/10-minute timers, secure capture/Recents and
owner screenshot refusal retain their accepted candidates. Nov6 physical L1 proves
controlled existing-task/password/cache recovery; selected manual663s proves functional
long-background return, without upgrading its earlier native timeout.

Human cancellation/resultCode and different-Menu/provider-loaded stale callbacks
remain Pending. Early30 failed in setup before Connect; its independently successful
mandatory restoration closes the early30 performed-action obligation only. Approved
Provider31 reached retained-A readiness, then its action wait Failed; owner confirmed
no additional revocation; account/cache diagnostics remain pending, without401/cancellation credit. OS-LMK remains Pending: owned
emulator trial1 NotExercised and trial2 Unknown are preserved; VM disposal does not
turn either into restoration acceptance. No physical memory pressure was performed.

EN27 and VI29 large-font observations are separate candidates; the grid samples
nine points per language at110%portrait. No every-pixel/all-control/font/device matrix
is claimed. Clipboard process death/history stay outside the timing/removal guarantee.
QR receiving, Autofill, Drive, multiple-profile authoring and account-write screens
remain separate follow-ups. Whole Mobile Phase1 stays Partial.
