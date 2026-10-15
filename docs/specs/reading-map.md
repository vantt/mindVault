# Reading Map

Current specifications and implementation entry points. Maintain this map when
an area changes; it does not require a workflow tool or generated state.

| Area | Specification | Code entry points |
|---|---|---|
| Browser recipe kitchen | [Product requirements](../prd.md), [password system](../password-system-design.md) | `chrome-extension/src/` |
| Browser Cookbook authentication/session/clipboard | [Extension security](extension-security.md) | `chrome-extension/src/security/`, `chrome-extension/src/offscreen/` |
| Mobile recipe kitchen | [Core](mobile-core.md) | `shared/` |
| Mobile Menu connection | [Menu client](mobile-sheets-client.md) | `mobile/sheets-client/` |
| Android Cookbook storage/unlock | [Keystore](mobile-keystore.md) | `app/`, `shared/` |
| Android Cookbook lookup/cooking/connection (code complete; selected device flows Passed, broader acceptance Pending) | [Cookbook](mobile-cookbook.md) | `app/.../cookbook/`, `shared/.../cookbook/`, `mobile/sheets-android/` |
| Phone QR Cookbook handoff: extension sender | [QR sender](qr-handoff.md) | `chrome-extension/src/options/options-qr-display.js`, `chrome-extension/src/adapters/infrastructure/qr-serialization.js`, `chrome-extension/src/adapters/infrastructure/qr-chunking.js` |
| Marketing landing page | Area specification not yet consolidated | `landing-page/` |

Start with [system overview](system-overview.md). Product choices and remaining
work are maintained in [decisions](../decisions/mobile-product-decisions.md) and
[backlog](../backlog.md). [Historical plans/reports](../history/README.md)
preserve earlier implementation evidence; their former automation is retired.
