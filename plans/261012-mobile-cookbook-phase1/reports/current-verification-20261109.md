# Current Cookbook verification —9November2026 (Asia/Saigon)

Status: **Local checks/review Passed; plan remains Partial**.
No production/test source changed in this continuation. Existing extension/site WIP
and historical runtime results are preserved. Per-step tracking is in
[phase05](../phase-05-verification-delivery.md#current-execution-checklist-9november2026-asiasaigon).

## Actual local results

Independent tester used the documented JDK17/SDK36 root toolchain and standalone
JDK21. Test cleanup forced fresh JVM assertions; build/compile/lint reuse is explicit.

| Check | Observed result |
|---|---|
| Root shared kitchen |78 tests,0 failures/errors/skips |
| Root Android Menu bridge |22 tests,0 failures/errors/skips |
| App JVM |16 tests,0 failures/errors/skips |
| Root build |Passed56s;185tasks,11executed/174up-to-date |
| Standalone Menu client |Fresh35 tests,0 failures/errors/skips; Passed14s,9tasks/4executed |
| Compilation/assembly |Current debug/test/release outputs accepted as UP-TO-DATE; no fresh compilation claim |
| Lint |Task succeeded; analysis/report reused,0errors/7warnings/1hint |
| Source freeze |207 mobile paths unchanged before/after verification |
| Original baseline |106 paths checked;7 historical production differences; no protected-test differences |

The first standalone build reused its JVM results; the separate `cleanJvmTest build test`
execution supplies the35 fresh assertions. Total fresh assertions: **151**.
No protected/Golden test was edited. The seven differences from the original baseline
are existing manifests, MainActivity, VaultController, VaultScreen and two Menu-client
production files; they are not changes made during this continuation.

All three resulting APKs exactly match the archived candidate32:

| Artifact | SHA256 |
|---|---|
| Debug |`921f057eb1380b7a537e7cf4ac3f28563776c184a9583a677707cafcc6e94444` |
| Android test |`d55b1f2f81d2994e6238a3beba20a977281ca952ec177e00134ecacd4227689f` |
| Unsigned release |`f2a86210fa254f5050bebd1086092dcd53dd87b7bb63b346c49c9ac11f57f264` |

Raw logs, copied XML/APKs, source/baseline hashes and summary:
`/home/vantt/.local/share/passchef-mobile-evidence/2026-11-09-current-tree/`.
Independent final evidence review Passed:28 copied XML files, fresh task logs,
207 current source hashes, baseline audit and all three copied/current APKs were
checked against the reported counts and candidate32. Audit: `independent-review.json`
in that evidence directory. No reviewer device/Google calls occurred.
Release manifest/DEX were not freshly audited; no release acceptance follows.

## Independent source review

No concrete P1/P2 found within reviewed production ownership/auth/clipboard scope:

- Menu edits invalidate authorization/source ownership; stale callbacks require the
  original current ticket and an unlocked Cookbook before publication.
- Rejected local tokens must finish SDK eviction before an explicit Connect;
  failed eviction does not permit authorization.
- Cache commit serializes with invalidation. Last-good offline Menu remains protected.
- Clipboard cleanup requires legal foreground access, original deadline and owned
  identity; a foreign or null-label replacement is preserved.

Existing SDK32 retry02 preparation remains source-compatible: all204 frozen paths,
complete app-source inventory and three archived APK hashes match. This is preparation
only. No `core01/native-start.json` exists in that retry02 namespace. Any execution
still requires current owner participation and actual installed-byte/foreground checks.

## Initial runtime discovery and remaining gates

Root read-only ADB checks observed one connected Xiaomi/API36, no debug app PID and
actual `mWakefulness=Dozing`. No install, launch, authentication, setting/grant change,
SDK clearing, remote revocation or native test occurred during that local-verification scope.
Owner readiness was requested at that point. Subsequent actual owner replies enabled
the separate [local SDK/normal reconnect continuation](google-local-sdk-runtime-20261109.md)
and [selected200%landscape EN offline UI](large-landscape-runtime-20261109.md).
Those reports supply actual native outcomes; the original local-only evidence stays scoped.

- Genuine human Google cancellation/resultCode and provider-loaded different-Menu
  stale callback remain Pending. Silent grant reuse supplies no cancellation proof.
- Broader physical Cookbook/source/mapping/detail layout, focus/touch/orientation
  coverage and natural Android LMK restoration remain Pending.
- Prior setup code15/UI/wrap causes remain unknown; immediate authentication at
  every real prompt is required before the prepared final unchanged pre-SDK retry.
  Do not infer a repair, successful SDK action or reconnect obligation from setup failure.

Selected earlier401/restoration,403, controlled saved-task, long-background and
EN/VI font/grid acceptance keep their original scopes. Current status summaries
are reconciled against those dated results; historical failures remain retained.
No final plan integration/archive or release completion is claimed.
