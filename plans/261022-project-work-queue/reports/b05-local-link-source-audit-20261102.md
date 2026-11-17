# B05 — local link and asset source audit

Date:2026-11-02, Asia/Saigon. **Local source audit and independent source/evidence review Passed;final bookkeeping Passed.**
Review:[independent source audit](b05-local-link-source-audit-20261102-independent-review.md).
Parent:[Track03 B05](../phase-03-website.md#task-b05).
Selected [slot04 width/tooltip evidence](b05-pseudo-overflow-slot04-20261102.md)
retains its separate runtime scope. B05/website/program remain Partial.

## Actual command and bounded scope

From repository root,existing uv/systemPython3;no installation:

```bash
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/b05-local-link-source-audit-20261102/audit-local-links.py
```

Exit0. Reproducible153line [verifier](../../../chrome-extension/build/b05-local-link-source-audit-20261102/audit-local-links.py)
uses stdlibHTMLParser/URL/path/hash functions only. It reads index/privacy/termsHTML,
staticIDs,href/src/xlinkhref,OG/Twitter-image/OGURLmeta;styles.css and5inline CSS fragments (2<style> blocks+3style attributes)
for`url()`. Local files/fragments resolve against the real landing-page root;
URL-decoded traversal/symlink escapes reject. Six in-memory verifier selfchecksPassed:
duplicateID detection,href parsing,query removal,fragment decoding,site-root path,
encoded traversal rejection. No product test/fixture files written.
No nested AGENTS instructions found in landing-page/chrome-extension source search.

## Actual results

| Check | Result |
|---|---|
|HTML/staticIDs|3pages,26IDoccurrences;0duplicateIDs|
|All references|90occurrences;69localPassed,21NotExercised|
|Local targets|69/69files/refs resolve;49include matching staticHTMLfragments;20file-only|
|CSS|styles.css+5inlineblocks inspected;0url()occurrences;noCSSimports found|
|Errors|0missinglocalfiles,0missingstaticfragments,0pathescapes|
|External/mailto|14externalNotExercised;0mailtooccurrences|
|Empty/top placeholders|7`#`linksNotExercised;inventory below|

Immutable [actual results](../../../chrome-extension/build/b05-local-link-source-audit-20261102/audit-results.json)
retain every sourcefile/line/attribute/target/category/verdict. No actionable broken
local file/fragment/duplicateID defect demonstrated;no source repair applied.

Remaining destination inventory is explicit:

- `index.html:39,1305`:nav/footerlogo`#`top shortcuts;source classification only.
- `index.html:1333`:License`#`;license destination remains unresolved.
- `index.html:1342,1347`, `privacy.html:496`, `terms.html:479`:GitHub/Twitter social`#`placeholders;no provider target/ownership proved.
- `index.html:1261,1269`:genericChromeStore/GitHub destinations;not a published listing or project/support acceptance.
- Nine font/preconnect references and three legal-provider references remain external NotExercised.

DynamicFAQcontrol IDs created by JavaScript are outside staticHTMLID acceptance;
this audit checks only actual static href fragments. It does not simulate JavaScript,
CSSlayout,click handlers,HTTPstatuses,live redirects/provider availability,rendered
assets,publicURLs,domainownership,publisher/supportsetup or StoreCTA completion.
The CSSurl scanner covers literal present source;it is not a general CSS escape/
JavaScript-generated URL evaluator. Existing runtime08/14 scopes are not broadened.

## Identity and preservation

Verifier SHA256`84c6dd245b657ad7eb2584cba967fc63762707f2fa627d1dd9eba6e38a4c9e1c`;
results SHA256`04a1786a1b706a913e1b57723d6bb5e53b264e8a6b71305812228f790153ae8e`.
Audit checks before/after all9website file hashes against the frozen slot04 candidate;
allmatch/unchanged. Fresh [preservation receipt](../../../chrome-extension/build/b05-local-link-source-audit-20261102/preservation.json)
confirms runtime74/tests46unchanged and heldZIP matches SHA256
`278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`.
No Golden/product/source modification,GUI/browser/server/network/provider/native/
clipboard/account action or fullheaded test run. Raw artifacts remain ignored/local.

## Unresolved

Independent review;License/social/project/listing destinations and C04/C05/C11/A22
public gates;remaining broad B05/runtime/deployed matrices. No broken-local-source
finding blocks the next independent preparation task.
