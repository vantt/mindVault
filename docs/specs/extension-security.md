# Browser Cookbook security and session lifetime

Current implementation accepted2026-10-12. This contract describes the browser
extension; Android storage and encrypted sharing bundles have separate contracts.

## Authentication and encrypted storage

Cookbook encryption stays Argon2id (32-byte key,t3,m65536KiB,p4) + AES-256-GCM.
Unavailable derivation fails closed. There is no vault PBKDF2 fallback or format
migration; sharing-bundle PBKDF2 is unchanged. Weak master passwords remain weak
against offline attacks; the UI lockout does not protect stolen ciphertext.

`security/background-security.js` owns setup, unlock and publication. Authentication
requests are accepted from the bundled popup/Options pages. A bundled offscreen
document runs genuine Argon2 in a module Worker; Options password rotation retains
compatible page-side derivation and commits through the background authority.

Five failed/abandoned reserved unlock attempts impose a five-minute cooldown.
Nonsecret counters, deadline and reservation identity persist in storage.local;
passwords, keys and Spices do not. Runtime/configuration failure is distinguished
from wrong-password failure. First setup is a single-writer operation; stale or
duplicate setup cannot replace an existing Cookbook. Rotation verifies a snapshot
of encrypted profiles and the current session before replacing it.

## Session and asynchronous ownership

The key, nonce, start and absolute ten-minute deadline stay in storage.session.
Checks reconstruct missing alarms from the original deadline and reject expiration
before cooking/tag access. Browser idle or locked state clears the session after
the configured five-minute idle threshold. Restarting a worker or opening another
popup never grants a new ten-minute window by itself.

Pending authentication, Home detection, Builder preview and profile editing have
ownership/version checks. Lock, expiry, close, invalid form or newer input prevents
late completion from redisplaying a stale Dish or saving with an old session.
Profile encrypted writes also check the current nonce at the background authority.
These controls do not promise perfect memory erasure or protection from malware.

## Clipboard ownership

Popup/content Copy sends the Dish to the bundled offscreen owner. Feedback follows
the actual write. The owner keeps only the current copied text/deadline in RAM and
can survive closing the copying page. After30 seconds it reads the current clipboard
and clears only an exact match. Later user text or denied reading causes no write.
Recipe-only Copy remains a separate action.

The extra permissions are `offscreen` and `clipboardRead`; there is no clipboard
history, wider host access, `scripting` or new web-accessible resource. Minimum
Chrome116 reflects `runtime.getContexts`; acceptance ran on Chromium143.0.7499.4.
Comparison and writing are not atomic against other applications. Sleeping/closed
Chrome can delay cleanup, and OS clipboard history is outside this contract.

## Evidence and remaining scope

[Current verification](../../archive/plans/261012-extension-completion/reports/verification.md)
separates deterministic regressions, headed extension UI, actual system clipboard
and native Google Cookbook proof. Cross-device Sync/conflicts, the broad browser/OS
matrix, release benchmarks and Store publication remain in the active MVP roadmap.
