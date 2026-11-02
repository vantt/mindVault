# A05 — Independent permission audit review

Date:2026-10-26, Asia/Saigon. Reviewer:independent agent, not audit author.
Source:HEAD `039c76d17a2765bd753e09a7009e004dadfd6638` + preserved WIP;
manifest2.3.0/package1.0.0. Scope:[A05 report](a05-permission-audit.md) and
its pointer/host-path qualification in the permission working record.
Verdict:**Passed for audit/protocol preparation**; no unresolved review finding.

## Review

All11 direct tabs calls map to five production modules; the sixth involved module
is a runtime-message consumer. Sensitive URL/title access is distinguished from
tab creation/messaging. The report does not infer a permission's necessity merely
from use of its API namespace or from deterministic tests mirroring detector logic.

Retain/remove proposals preserve the fixed host scope, installed content-script
matches, CSP and other manifest fields. Host permission paths versus injection
matches, HTTP wildcard, temporary gesture grants, missing metadata and the worker's
path-only active detector are explicitly bounded. A false nonhost candidate is not
a supported feature to preserve or evidence that broader access is necessary.

Prepared B02 cases cover exact grants/candidate bytes, picker and manual context,
Recipe/Tag errors, native shortcut, lock/idle/session, clipboard, bundled guidance,
unsupported tabs and cleanup. No browser permission enforcement, minimum set or
manifest reduction is accepted. Isolation/desktop/account boundaries remain intact.

## Independent checks executed

Linux Node24.18.0; static source scan/JSON parsing, no browser launch.

| Check | Result |
|---|---|
| Direct production `chrome.tabs` call scan |11calls in5modules:6query,3sendMessage,2create. |
| Parse current manifest and three JSON proposals |Current8permissions;proposals7/7/6,exact intended removals only. |
| Merge proposals onto manifest |All non-permission fields equal unchanged current manifest. |
| Report/working-record local file links |37/37 resolve. |
| Manifest SHA256 |`0a8d20053a13dfd6bda522caab2f4950f7026a2543a01eeb2b8bf4e1e3a78bf3`. |
| `sha256sum --quiet -c /tmp/passchef-a-ready-20261026/initial-tests.sha256` |All42 existing extension test files unchanged. |
| `git diff --exit-code -- chrome-extension .github/workflows/build_and_deploy.yml` |exit0;no product/config/test changes. |
| `git diff --check` |exit0. |

Author's initial six-direct-module assumption was corrected to five by recounting
source, before final handoff. No Golden assertion modified. No unchanged suite
replay, browser profile/grant mutation, actual candidate or runtime acceptance.

## Handoff and unresolved

A05 can be Done as preparation; next A06. B02's A05 prerequisite is satisfied,
but an exact reviewed manifest candidate, desktop slot/verified isolation and
permission-runtime evidence remain Pending. C02 support scope and final artifact
acceptance remain open. Product/UI gate C01 stays closed.
