# A07 — Reviewed CI development packaging

Date:2026-10-26, Asia/Saigon. Source:HEAD039c76d + preserved A06/WIP;
manifest2.3.0 unchanged. Status:**Done2026-10-26 — CI preparation**;
[independent review Passed](a07-independent-review.md).
Remote GitHub Actions run:NotRun; no push/dispatch/deploy/account action.

## TODO

- [x] Read current workflow/A06 commands and official versioned action inputs.
- [x] Patch extension CI validation-before-package and PassChef development artifacts.
- [x] Verify YAML/order/triggers/landing preservation and local command chain.
- [x] Actual results/limits recorded; independent review Passed and registries updated.

## Exact edited scope and behavior

- [Workflow](../../../.github/workflows/build_and_deploy.yml):only top push path,
  extension changes filter and build-extension steps changed. Existing push/main
  scope remains; no PR, scheduled, release, manual-dispatch or publication automation added.
- [Changelog](../../../docs/project-changelog.md) and
  [roadmap](../../../docs/development-roadmap.md):tooling note distinguishes local
  CI patch from an actual GitHub run; existing WIP retained.
- This report. No production source/test/manifest, A06 scripts/lock or legal files
  changed by A07. The4files above are this task's scope.

The extension job selects Node24 and pnpm10.24.0 explicitly. pnpm setup points at
`chrome-extension/package.json`, because no root package.json exists; auto-install
is disabled so frozen installation happens in the extension working directory.
Node cache is keyed from the existing extension pnpm lockfile.

Order:checkout→tool setup→frozen install/postinstall→unit+package tests+source
validation→validated manifest-version output→development package→ZIP validation→upload.
There is no continue-on-error or always-upload branch. A failed verification
prevents package/upload under ordinary GitHub success gating; no headed E2E is run.

Development artifact name is `passchef-extension-2.3.0-development`, derived from
the actual manifest rather than the separate package1.0.0 metadata. Upload includes
the exact ZIP, per-file inventory and SHA256 sidecar. Missing all selected files
is an action error; package creation already fails if its writes/validation fail.
Retention is explicitly14days, subject to repository/organization limits;
compression-level0 avoids recompressing the existing ZIP. Remote retention/upload
success is unverified. These are development artifacts, not Store/release candidates.

## Trigger and landing boundaries

The workflow file itself now appears both in push paths and the extension-only
changes filter. A workflow-only main push therefore selects extension CI. Landing's
filter remains `landing-page/**`; its entire deploy job, permissions, environment,
artifact route and action versions are byte-identical to HEAD.

Local filter simulation covered4paths:workflow→extension only; extension source→
extension only; landing source→landing only; docs→neither. This is a local pattern
check, not execution of dorny/paths-filter on GitHub. A future push including actual
landing changes can still deploy through the existing route; C05 authorization
remains necessary. No push or deploy occurred here.

## Official action/input checks —2026-10-26

Repo docs-seeker detect/fetch found no Context7 document; fallback read the
versioned official action READMEs and GitHub syntax reference:

- [pnpm/action-setup v4](https://github.com/pnpm/action-setup/blob/v4/README.md):
  exact version, package_json_file and false run_install inputs verified.
- [actions/setup-node v4](https://github.com/actions/setup-node/blob/v4/README.md):
  node-version, pnpm cache and subproject cache-dependency-path inputs verified.
- [actions/upload-artifact v4](https://github.com/actions/upload-artifact/blob/v4/README.md):
  multiple paths, if-no-files-found, retention-days and compression-level inputs verified.
- [GitHub workflow syntax](https://docs.github.com/en/actions/reference/workflows-and-actions/workflow-syntax):
  existing push branch/path conditions and normal step failure gating retained.

No action-major upgrade or new account/service connection. Local actionlint and
Go executables were unavailable; no actionlint/full-schema pass is claimed.

## Actual local checks

YAML parsing used `uv run --no-project --with pyyaml==6.0.3 python`; no repository
Python dependency or lockfile added. BaseLoader preserves the `on` key rather
than treating it as a YAML1.1 boolean. Recursive mapping checks reject duplicate
keys. Semantic assertions checked9extension steps, runtime inputs, frozen install,
verification/package/upload order,3artifact paths, failure-gating absence of
continue-on-error, retention and preserved landing job. Every run script also
passed `bash -n` with the version expression resolved to the actual manifest.

The same parser then executed the workflow's shell commands locally in order,
from install through ZIP verification, with a temporary real GITHUB_OUTPUT file.
Action checkout/setup/cache/upload themselves were not executed. Local runtime
was Linux Node24.18.0/pnpm10.24.0; GitHub uses the selected Node24 major, not a
claim of identical runner/patch/environment bytes.

| Check / command | Actual result / limit |
|---|---|
| YAML syntax/unique keys/semantics |Passed; workflow111lines. All declared run scripts pass shell syntax. This is not GitHub schema/runner validation. |
| Landing preservation |Whole deploy-landing job structurally and textually identical to HEAD; same landing filter. |
| `pnpm install --frozen-lockfile` |exit0; lock current and mandatory-copy postinstall Passed. Existing ignored esbuild build-script warning retained; no setting changed. |
| `pnpm test:unit` |exit0;147/147,20files,3.23s, no skip/failure. Expected invalid-Tag diagnostics only. |
| `pnpm test:package` |exit0;49/49,27.24s, no skip/failure. Actual filesystem/ZIP mutation suite from A06 unchanged. |
| `pnpm validate` |exit0;73files, required resources/notices/JSON/JS/WASM checked; known external CSS data recorded. |
| Workflow manifest output command |Actual temp output contained `version=2.3.0`; downstream expression resolved correctly. Temp output directory cleaned. |
| `pnpm package` then workflow `pnpm validate:zip` |Both exit0;73files,1,102,759-byte development ZIP; all3declared upload paths exist locally. |
| Artifact SHA256 |`6c6fc80055c61d49cba404694fa8691a1c41316bccc2a14b491e930995fc0fe9`, matches A06 current runtime bytes. |
| `git diff --check`; scoped links |Passed. Existing runtime/tests/Golden remain unchanged; no lint/full-headed test or GitHub run claimed. |

Existing [A06 inventory/provenance/artifact record](a06-packaging-validation.md)
retains the source/ZIP/notice limits. No copied Recipe, private Spice or production
credential enters CI configuration. Product readiness, supported platforms, Sync,
extracted-ZIP Chrome/WASM runtime and final release acceptance remain Pending.

Closed as CI preparation only after independent review; next A08 Ready.

## Unresolved

Remote CI run; C05 deployment route/authorization; C01/C02/A13/B07/B08 product/
candidate/browser/Sync acceptance; C11 Store publication. Tooling is not release acceptance.
