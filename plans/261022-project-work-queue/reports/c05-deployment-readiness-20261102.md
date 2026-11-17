# C05 — read-only deployment readiness

Date:2026-11-02, Asia/Saigon. **Preparation source/evidence review Passed;final bookkeeping Passed.
C05 NOTDone;owner holds deployment for cross-platform design alignment.**
Parent:[Track03 C05](../phase-03-website.md#task-c05).
Owner already chose passchef.com + Cloudflare;these choices are preserved.
[Operations ledger](../../../docs/commercialization/operations/publisher-and-support.md)
was unchanged at the original preparation checkpoint;the subsequent owner update below is now recorded. No account/dashboard/credential inspection,registration,grant,GUI,
repository push,DNS write,workflow application,upload/deploy or outbound message.

## Public observations — actual2026-11-02

[Raw selected metadata](../../../chrome-extension/build/c05-deployment-readiness-20261102/public-probes.json)
and seven individual receipts retain exact start/end/error/vantage. Probes started
10:44:46UTC (17:44:46Asia/Saigon),LinuxWSL2/Python urllib,verified TLS when reached;
DNS uses Google Public DNS HTTPS resolver. No cookie jar/auth,Set-Cookie or raw
response body retained. GET10s socket timeout,1MiB response cap,HTTPS-only redirects
capped3;seven independent probes launched concurrently. Outcomes are this vantage
at this time,not ownership/registration/global availability or local-source deployment proof.

| Probe | Actual result | Meaning/limit |
|---|---|---|
| NS |DoHHTTP200/DNSstatus0;`brianna.ns.cloudflare.com.` and `damiete.ns.cloudflare.com.`|Cloudflare delegation observed;owner/account/active-zone control unproved.|
| A / AAAA / CNAME |EachDoHHTTP200/DNSstatus0,noAnswer;SOAauthority retained|NOERROR/NODATA from this resolver,not NXDOMAIN or proof of domain availability.|
| `https://passchef.com/` |ProbeError beforeHTTP:URLError `[Errno -5] No address associated with hostname`|No HTTP status,TLS/content/body-match observed;do not call this server500/websitePass.|
| `https://passchef.com/privacy.html` |Same resolver error beforeHTTP|Legal HTTPS reachability/content NotVerified.|
| `https://passchef.com/terms.html` |Same resolver error beforeHTTP|Legal HTTPS reachability/content NotVerified.|

Actual command,exit0 means metadata collection completed,not all probesPassed:

```bash
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/c05-deployment-readiness-20261102/probe-public.py
```

Initial uv warning `--no-project ... no project was found` retained in tool output;
script completed. Public DNS records do not identify the user's Cloudflare account.
Available catalog/tools lack docs-seeker/context7;used web tool with primary official
docs directly,checked2026-11-02;no skill/tool installation required.

## Two concrete route proposals — neither selected or executed

| Route | Reviewable configuration and controlled sequence | Ownership/deployment prerequisite |
|---|---|---|
| A:Cloudflare Pages static HTML |Serve existing `landing-page/` directory,entry `index.html`,no framework/build dependencies. Git-integrated setup:repo root `/`,output `landing-page`,build `exit 0`;production branch and automatic deployment controls explicit before linking repo. Initial provider URL `<OWNER_PROJECT>.pages.dev` is a placeholder,not an observed site. Stage reviewed candidate→preview URL/body checks→approved apex association. |Owner identifies existing/new Pages project/account and selects Git integration versus Direct Upload before creation. Direct Upload cannot later convert to Git integration in the same project. Apex requires Cloudflare zone on the project's account.|
| B:Workers Static Assets |Assets-only Worker,no server script/framework needed;Wrangler configuration at repo root would set owner-chosen `name`,reviewed `compatibility_date`,and `assets.directory:"./landing-page"`. Default static routing;initial `<OWNER_WORKER>.<OWNER_SUBDOMAIN>.workers.dev` placeholder→provider preview/body checks→approved `passchef.com`Custom Domain. No Worker/application code required for present pages. |Owner identifies existing/new Worker/account and active owned Cloudflare zone;review existing DNS conflict before association. No account IDs/tokens/project names invented;no Wrangler install/login/deploy attempted.|

Cloudflare primary support:
[Pages static HTML](https://developers.cloudflare.com/pages/framework-guides/deploy-anything/),
[Pages Direct Upload/project-mode constraint](https://developers.cloudflare.com/pages/get-started/direct-upload/),
[Pages apex association](https://developers.cloudflare.com/pages/configuration/custom-domains/),
[Workers asset routing](https://developers.cloudflare.com/workers/static-assets/),
[Workers directory configuration](https://developers.cloudflare.com/workers/static-assets/binding/),
[Workers Custom Domain prerequisites](https://developers.cloudflare.com/workers/configuration/routing/custom-domains/).
These are proposals for the existing vanilla landing site,not service selection.

URL acceptance plan for either route:provider preview first,then approved apex
`https://passchef.com/`,Privacy and Terms plus favicon/OG/CSS/JS. Both platforms'
default HTML handling may redirect `.html` to `/privacy` or `/terms`;retain every
redirect/finalURL and verify correct page content,not status alone. Current relative
links remain valid targets for a measured redirect chain. Canonical URLs,`www`
redirect/support destination and approved legal/contact details remain decisions;
no unsupported support endpoint,listing URL or final policy acceptance invented.
[Pages serving/redirect defaults](https://developers.cloudflare.com/pages/configuration/serving-pages/),
[Workers HTML handling](https://developers.cloudflare.com/workers/static-assets/routing/advanced/html-handling/).

Rollback proposal:retain candidate hashes,prior deployment/version and account-side
DNS/routes before any approved change. Pages:restore prior successful production
deployment;preview is not a production rollback target. Workers:restore known prior
version;resource bindings/DNS configuration need separate restoration checks.
First deployment with no prior version:remove only explicitly introduced domain
association/records and restore recorded originals;never delete a zone or assume
rolling back code reverses DNS. Recheck root/legal/assets after rollback.
[Pages rollback](https://developers.cloudflare.com/pages/configuration/rollbacks/),
[Workers rollback/limits](https://developers.cloudflare.com/workers/versions-and-deployments/rollbacks/).

## Existing GitHub Pages publication conflict — draft only

Actual `.github/workflows/build_and_deploy.yml` pushes to main with landing paths
run `deploy-landing` (Pages write/id-token permissions). Extension-only changes
have a separate job/filter. No current GitHub/environment execution/account state
was inspected;source establishes an automatic publication path,not a past deployment.

Ignored [disable-only patch](../../../chrome-extension/build/c05-deployment-readiness-20261102/github-pages-disable-only.patch)
changes only that job's condition to `${{ false }}` with an explanatory comment;
all triggers,changes outputs,extension job and artifact steps remain byte-identical.
This temporarily gates GitHub Pages while Cloudflare route is decided;it does not
publish to Cloudflare or remove already-hosted content. No canonical config changed.
`git apply --check .../github-pages-disable-only.patch` exit0;patch NOT applied.
Workflow baselineSHA256`60ae3aafb91bec1bf7aac56e58127a2c9f588dbfc40e0a4164b9fde33a56be5b`;
patchSHA256`ba01910d0cf6433db169c6e1a8c6b8ee0eef0197bd6d88e10186a9010d09cdfc`;
draftworkflowSHA256`d9ade15ff5f460fbd18273775aa14daa7e484a9825e8c6a2bbecf150712319c7`.
Apply/merge and any future re-enabling of this automatic publication route require
explicit owner scope;reversing the gate must not silently resume unwanted deploys.

## Verification, TODOs and owner inputs

[Before](../../../chrome-extension/build/c05-deployment-readiness-20261102/preservation-before.json)/
[after](../../../chrome-extension/build/c05-deployment-readiness-20261102/preservation-after.json):
HEAD352ffa9300dbd3698f3d32e48b833cf41d841c83,website9/runtime74/tests46/heldZIP
allmatch;workflow byte-identical toHEAD,extension job byte-identical in draft.
No product/Golden/ops/config mutation;no browser/test replay needed for this packet.

- [x] Actual bounded DNS/HTTPS metadata and dated official route research retained.
- [x] Reviewable two-route/URL/rollback proposal and non-applied publication gate.
- [x] Patch dryrun/source preservation verification;`git diff --check` exit0.
- [x] Independent packet/probe/patch source/evidence review Passed;root updates C05 dispatch with owner hold.
- [x] Owner reports management of passchef.com in Cloudflare;no independent account/active-zone verification.
- [ ] Service/project/account readiness stays deferred until UI alignment;existing references through normal owner UI,never credentials in chat.
- [ ] Owner chooses exact project-mode/canonical/support route and approves publication-gate/config scope;then prepare frozen provider-specific candidate before any deploy approval.
- [ ] Explicit deploy/DNS association approval→actual provider/apex/legal/assets checks→rollback evidence;C04 legal/contact and final CTA/C11 gates retained.

Unresolved:independent account/zone verification,service/project/permissions,actual public addresses/HTTPS,
provider candidate/rollback target,canonical/legal/support choices and deploy action.
C05/website/program remain Pending/Partial;no live deployment acceptance claimed.

## Subsequent owner steering —2026-11-02

Owner confirms management of passchef.com in Cloudflare;this is owner-reported,not account/zone/project verification. Owner explicitly defers deployment until web,Android and extension styling is consistent. Pages/Workers choice remains deferred and is not required for the new local UI work. Draft workflow patch stays unapplied;C05 remains NOTDone/owner-held.

[Independent preparation review](c05-deployment-readiness-20261102-independent-review.md) Passed source/evidence scope;final manager bookkeeping Passed after this owner update.
