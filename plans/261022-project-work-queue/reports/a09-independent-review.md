# A09 — Independent Chef/reviewer guide review

Date:2026-10-26, Asia/Saigon. Reviewer:independent agent, not guide author.
Source:HEAD039c76d + preserved WIP; manifest2.3.0, stable candidate Pending.
Scope:[A09 delivery](a09-chef-reviewer-guides.md),three operations guides/index.
Verdict:**Passed for truthful documentation preparation**. Product finding FA01
remains open for separate A23 repair; no working-import/restore acceptance granted.

## FA01 — independent actual-handler reproduction

P1 source defect in `options/options-import-wizard.js:173`:Full Access writes an
encrypted raw jar map to an own-profile record; cooking expects a `{secrets,...}`
envelope. Legacy import constructs its separate wrapped/shared record correctly.

Executed Linux Node24.18.0 with JSDOM Options markup, the real `openImportWizard`
handlers and actual ExportImportAdapter/AES helpers/ChromeStorageAdapter/parser/
GeneratePassword. Chrome storage was an isolated in-memory adapter; no browser,
provider, account, clipboard, real storage or source mutation occurred.

1. Created an actual AES-GCM key and properly enveloped synthetic Original profile.
   Control Recipe`r4nd0m#1` with public practice Spice`Basic*` cooked`Basic*r4nd0m`.
2. Actual `createFullAccessBundle` encrypted that jar map with a synthetic sharing
   password;opened real import wizard,pasted JSON,validated,continued and decrypted.
3. Entered a new local name and awaited actual confirm handler. Its completion
   callback fired and import modal closed:the user-facing import path succeeded.
4. Actual decrypted stored own profile had top-level key`1`,without `.secrets`.
   Subsequent real GeneratePassword rejected with`Secret #1 not found`.

This proves the handler/data-shape failure beyond text inspection; it does not
certify Chrome UI/runtime or recovery. Required repair:own-profile envelope with
unchanged original jars,meaningful handler→storage→subsequent Cook regression,
independent review and affected B01 later. No encrypted format/KDF redesign needed.

## Documentation review and independent checks

Author incorporated FA01 explicitly in all three guides,blocks working recovery
claims and routes repair separately. Guides distinguish own-profile hand Cooking
from Builder/mobile UI; original-Spice Full Access from Recipe-only sharing; one-
profile jar export from full Cookbook/settings/mapping backup; and master-password
protection changes from account/Spice/ingredient rotation. Same-name import and
single-Menu reassignment overwrite risks require disposable/unused-name practice.

Setup/unlock/profile/rotation steps checked against current source;public synthetic
fixtures and actual expected Dish are explicit. Final artifact/URLs/contacts/access,
rights,supported environments and all release acceptance remain Pending. Existing
UI01/DEMO01,clipboard/Tag/Sync limits remain;no fake screenshot or recovery result.

Independent local Markdown file checks:36/36 links resolve. All42 baseline test
SHA256 checks pass;`git diff --exit-code -- chrome-extension/src/options
chrome-extension/src/adapters chrome-extension/src/core chrome-extension/test`
and `git diff --check` exit0. No unchanged unit-suite replay or GUI proof.

## Handoff and unresolved

A09 can be Done as preparation;next **A23-FA01 local repair**,then remaining Ready
work. Overall A23 remains incomplete:FA01 still needs repair/verification and
affected headed B01;UI01 still needs C01 behavior choice. C01/C02/C04/C05/A13/
B07/B08/final restore and release gates stay open.
