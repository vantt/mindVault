# System Overview

PassChef stores recipes in a Google Menu and mixes them with the Chef's private
spices to cook each dish when needed. The Menu holds recipes rather than cooked
passwords. The browser extension protects stored spices with its encrypted
profile and keeps existing recipe/tag behavior covered by approved tests.

## Browser and QR sender

The extension provides recipe cooking, tagged recipe checks and encrypted
full-access bundle export. Its QR sender displays a bundle as one QR or a
sequence of chunks. Actual canvas decoding verified the sender; a phone QR
reader, reassembly and import screen are still separate work.
See the [QR specification](qr-handoff.md).

The browser now uses one setup/unlock authority, durable failed-attempt cooldown,
session nonce/deadline checks and a bundled offscreen Worker for Argon2. Copied
Dishes are compared before clearing after30 seconds, even when their popup closes.
Builder and profile editing reject stale asynchronous results. See the
[security/session contract](extension-security.md) for implementation and limits.

## Mobile foundation

The Android/JVM shared kitchen ports recipe cooking/tag checks and decrypts the
extension's full-access bundles. Its parity proof is pinned to the tested browser
behavior; iOS/Kotlin-Native remains deferred. See [Core](mobile-core.md).

A separate Kotlin Menu client reads and appends/updates recipes through Google
Sheets API. It has a manual OAuth setup guide and a JVM connection check;
synthetic responses/loopback tests verify client behavior. Real Google client
creation and a live Cookbook read remain the account owner's follow-up.
No full phone UI consumes this standalone connection yet.
See [Menu connection](mobile-sheets-client.md).

The minimal Android demo keeps an encrypted Cookbook, authorizes device-key
wrap/open operations with strong biometrics and recovers with the original
master password. It locks on background, after five idle minutes or ten total
minutes. The owner accepted tested Xiaomi/API36 coverage; unexecuted device
matrix and typed lockout callback paths remain tracked.
See [storage/unlock](mobile-keystore.md) and
[accepted limits](../../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md).

## Remaining product areas

Production mobile screens/edit-existing, phone QR import, Drive transport and
autofill are separate work. The marketing landing page is another subproject.
Use the [reading map](reading-map.md), [product backlog](../backlog.md) and
[preserved decisions](../decisions/mobile-product-decisions.md) for current scope.
[History](../history/README.md) records earlier plans and verification.
