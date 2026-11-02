# A07 — Independent CI preparation review

Date:2026-10-26, Asia/Saigon. Reviewer:independent agent, not workflow author.
Source:HEAD039c76d + preserved A06/WIP; exact4-file scope in
[A07 delivery](a07-ci-packaging.md). Manifest2.3.0 unchanged.
Verdict:**Passed for reviewed CI patch/local preparation**; no unresolved finding.
Remote GitHub Actions run remains **NotRun**.

## Review

Node24/pnpm10.24.0 and the extension package location are explicit. Frozen install,
unit/package tests and resource validation precede manifest-version extraction,
development ZIP assembly/verification and upload. No failure bypass or unconditional
upload introduced. The three artifact paths derive from the validated manifest;
name/retention14days/missing-file behavior are explicit. A06 runtime/notice/ZIP
limits remain applicable; neither local commands nor this patch certify a runner.

Workflow-only main pushes select extension verification through both top-level
paths and the extension filter. Existing push/main event semantics remain; no
new PR/manual/release/scheduled path. Landing's filter and entire deploy job,
permissions/environment/routes/action versions remain unchanged. An authorized
future landing push can still deploy; this task performs no push or deployment.

## Independent verification executed

Cached PyYAML6.0.3 through `uv run --no-project --offline --with pyyaml==6.0.3
python3`;BaseLoader preserves `on`, with duplicate-key rejection. No repo dependency.

| Check | Result |
|---|---|
| YAML parse/unique mappings |Passed. |
| Runtime/install/verification/package/upload semantics |9extension steps;expected inputs/order/three artifact paths/retention/failure gating. |
| Changed-path simulation |Workflow and extension source→extension only;landing source→landing only. This does not execute dorny on GitHub. |
| Event/landing preservation against HEAD |Only push/main remains;landing filter unchanged;whole deploy job byte-identical. |
| Real manifest output step in temporary `GITHUB_OUTPUT` |Exactly `version=2.3.0`;temporary directory removed. |
| Every declared extension run script |`bash -n` Passed. |
| Resolve upload expressions to current version |All3declared ZIP/inventory/SHA paths exist. |
| Original tests + formatting |All42 baseline test SHA256 checks and `git diff --check` Passed. |

Author executed the exact local install→147unit→49package→validate→version→package→
ZIP-verify command chain;results and matching A06 ZIP SHA are in delivery. No reason
to repeat unchanged broad suites after these semantic checks. GitHub action setup,
cache,filter,upload/retention and runner services remain unexecuted. actionlint/Go
were unavailable;no full-schema or remote CI pass inferred from YAML parsing.

## Handoff and unresolved

A07 can be Done as preparation; next A08. A13's A06/A07 prerequisites are satisfied,
but C01/C02/A23 remain. Remote CI run, C05 deployment authorization, stable artifact/
browser/Sync acceptance and C11 publication remain Pending.
