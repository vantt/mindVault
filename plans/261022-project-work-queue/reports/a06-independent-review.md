# A06 — Independent packaging review

Date:2026-10-26, Asia/Saigon. Reviewer:independent agent, not tooling author.
Source:HEAD039c76d + preserved WIP; exact16-file scope and author verification in
[A06 delivery](a06-packaging-validation.md). Manifest2.3.0/package1.0.0 unchanged.
Verdict:**Passed for local development packaging**; no unresolved review finding.

## Review and resolved findings

Reviewed copy/provenance/contract, resource and source validation, ZIP assembly/
verification, filesystem fixtures, package/lock, MIT notice and tooling docs.
Mandatory asset failure cannot fall back to stale destination files; accepted
Argon2 loader/WASM and QR bytes stay pinned. Dependencies/notices are traceable.
The explicit resource graph handles current ESM/Worker/runtime/HTML/CSS/locale
paths and distinguishes reviewed external font data from executable resources.

Two P2 findings were reproduced independently before repair:copy assembly followed
a destination symlink into an outside-project sentinel; ZIP's predictable temporary
output and sidecars could follow an outside-output symlink. Author added shared
preflight over destination ancestors/directories/files before writes, with6assembly
and5output filesystem regression cases. Independent repros below now reject safely.
This is bounded preflight, not protection against a hostile filesystem race.

Development ZIP has a root manifest, exact reviewed entries/bytes and required
notices; private/debug extras, wrapper roots, corrupt/truncated/missing/duplicate
entries are tested. Four reviewed unused source assets stay excluded, not deleted.
Explicit source-directory override is intentional; child symlinks are rejected.
Normal source review still must catch sensitive text in approved authored paths.

## Independent verification executed

Node24.18.0 and Python standard-library ZIP reader through `uv run --no-project
--offline python3`; isolated temporary trees removed in finally/temporary contexts.

| Check | Result |
|---|---|
| Original assembly destination-symlink reproduction after fix |Rejects before writes;outside sentinel and stale WASM unchanged. |
| ZIP temporary-file and inventory-sidecar symlink reproductions after fix |Both reject before any output write;outside sentinel unchanged. |
| Independent missing Worker URL mutation |Rejected as missing resource;real source untouched. |
| CLI validation with missing mandatory WASM in disposable source |exit1;stale-file bypass not accepted. |
| ZIP read with independent Python `zipfile` |CRC passes;73root entries,including manifest/notices;all bytes match reviewed source. |
| Generated inventory + SHA sidecar |Every size/hash matches independently extracted bytes;sidecar and archive SHA agree. |
| Final archive SHA256 / size |`6c6fc80055c61d49cba404694fa8691a1c41316bccc2a14b491e930995fc0fe9`;1,102,759bytes. |
| Changed/added code syntax and size |8/8 Node syntax checks;each code file below200lines. |
| Delivery report local links |20/20 resolve,including actual ignored development artifacts. |
| Original test preservation |All42 baseline test SHA256 checks pass;tracked runtime source/test diff empty. |
| `git diff --check` |exit0. |

Initial independent ZIP probe could not find `unzip`;Python ZIP reader was used
successfully instead. No failed probe counted as Passed. Author's final49 packaging
cases and147 unchanged unit tests are documented in delivery; unchanged unit suite
was not replayed here. Final independent checks target the actual artifact and
review-discovered failures rather than restating implementation assertions.

Font discovery also corrected A04's initially incomplete external-resource disclosure;
its separately reviewed amendment remains recorded in the A04 dossier.

## Handoff and unresolved

A06 can be Done; A07 CI adoption is Ready. This ZIP is a reproducible development
artifact, not A13's stable candidate. C01/C02, extracted-ZIP browser/KDF/fail-closed,
upgrade/Sync and Store acceptance remain Pending. No GUI, phone, account action,
publication, crypto parameter/format or protected-test change.
