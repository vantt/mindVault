# Phase01 — Extension: blocker, verification và candidate

Ngày:2026-10-22; updated2026-10-26, Asia/Saigon. Status: **Partial; A02/A05–A07/A10/A21 prep Done; A23 Partial**.
Điều phối qua [master plan](plan.md) và [46 task IDs](task-catalog.md).
Ready chỉ có nghĩa đủ điều kiện bắt đầu; Done vẫn theo đúng scope/evidence, không thay release acceptance.

## Phạm vi và giới hạn

Sở hữu17 ID: A02,A05,A06,A07,A10,A13,A21,A23,B01,B02,B03,B04,B07,B08,C01,C02,C03.
Mục tiêu: làm rõ blocker còn thật, chuẩn bị tooling/protocol độc lập, rồi xác minh
đúng candidate. Không mở lại5 browser slices đã Done, thay Golden, hạ Argon2 hoặc
đổi encrypted Cookbook format. Release copy/account/publication thuộc [Phase04](phase-04-release.md).
Mobile, landing implementation và Drive-sync tương lai ngoài phạm vi file này.

## Đầu vào có thật

- [MVP current status](../../docs/prd/v1-mvp.md#current-implementation-status--relevance--2026-10-12): phases1–4 Done trong phạm vi; Sync/platform/performance/release Partial.
- [Completion plan](../../archive/plans/261012-extension-completion/plan.md),
  [verification](../../archive/plans/261012-extension-completion/reports/verification.md),
  [delivery](../../archive/plans/261012-extension-completion/reports/delivery.md):147 unit,
  25 distinct headed E2E và8 actual Google/native là historical evidence, không phải run mới.
- [Security contract](../../docs/specs/extension-security.md),
  [permissions working record](../../docs/commercialization/legal/permissions-review.md),
  [manifest](../../chrome-extension/src/manifest.json),
  [storage adapter](../../chrome-extension/src/adapters/infrastructure/chrome_storage_adapter.js).
- [Unit/E2E commands](../../chrome-extension/package.json),
  [headed fixture](../../chrome-extension/test/e2e/fixtures.ts),
  [actual toolbar helper](../../chrome-extension/test/e2e/actual-toolbar-popover.ts).
- [Copy-WASM script](../../chrome-extension/scripts/copy_wasm.js),
  [CI workflow](../../.github/workflows/build_and_deploy.yml),
  [packaging/release gates](../../docs/commercialization/plan.md).
- [Owner-reported product gate](../../docs/commercialization/plan.md): bugs/unfinished UI reported; not a diagnosed issue list.

## Thứ tự thực hiện

1. A02 gom evidence/repro đã có; C01 chỉ bổ sung điều chưa biết. A05/A06/A10/A21
   có thể tiến hành độc lập; không chờ owner cho phần audit/local preparation.
2. C02 chốt release/support/Sync boundary. A23 chỉ sửa issue đã định danh; A07 theo A06.
   B01/B02/B04 lấy đúng patch/candidate khi cần, không chạy lại suite để lấp thời gian.
3. A10 chuẩn bị cả Sync và upgrade; C03 chuẩn bị hai Chrome environments nếu real
   Sync nằm trong boundary. B03 cần support matrix C02 và measurement protocol trước.
4. A13 tạo stable local candidate sau A06/A07/A23 và C01/C02. **Không phụ thuộc
   B07/B08 trước khi candidate tồn tại**. Nếu A23 không có blocker, ghi N/A có căn cứ.
5. B07/B08 xác minh đúng candidate/ZIP; chuyển evidence sang A14 ở Phase04. B01
   full-suite evidence có thể dùng lại cho B07 nếu cùng bytes/runtime/scope; ZIP smoke vẫn riêng.

## Task registry

Report Done có link evidence/review; các report khác là **planned, chưa tồn tại**; đường dẫn code chỉ là destination,
không phải evidence hoặc hyperlink tới file chưa có. Mỗi report ghi task ID,
source/candidate/commit, command/runtime khi có, verdict, giới hạn và next task.

| ID / nhóm | Status / dependencies hiện tại | Artifacts cụ thể và tiêu chí Done | Report destination (planned) |
|---|---|---|---|
| <a id="task-a02"></a>A02 / A | Done2026-10-26; independent review Passed; broader C01 Partial | Original ledger8ID: UI01 source gap, later owner-approved deferral locally reviewed; OWN01 MissingRepro; tooling/copy/legal/decision/acceptance tách đúng owner. Original fresh147unit,55JS,4JSON; không GUI, không mở release gate. | [Report](reports/a02-extension-blockers.md), [review](reports/a02-independent-review.md) |
| <a id="task-a05"></a>A05 / A | Done2026-10-26; audit/protocol only; browser conclusion B02 |11calls/5direct modules plus runtime consumer; P0/P1/P2/P3 retain/remove proposals và B02 protocol reviewed. Manifest unchanged; no minimum-permission/runtime conclusion. | [Audit](reports/a05-permission-audit.md), [review](reports/a05-independent-review.md) |
| <a id="task-a06"></a>A06 / A | Done2026-10-26; development tooling only | Fail-closed mandatory assets/provenance/notices,73-file resource/ZIP inventory;49filesystem/archive tests,147unit and independent artifact/review checks Passed. Runtime bytes unchanged; development ZIP is not A13/B07/B08 acceptance. | [Delivery](reports/a06-packaging-validation.md), [review](reports/a06-independent-review.md) |
| <a id="task-a07"></a>A07 / A | Done2026-10-26; reviewed local CI preparation; remote NotRun | Node24/pnpm10.24/frozen install;147unit/49package/source checks before ZIP;manifest-versioned ZIP/inventory/SHA upload14days. YAML/shell/semantic/local command chain and independent review Passed;landing deployment unchanged. | [Delivery](reports/a07-ci-packaging.md), [review](reports/a07-independent-review.md) |
| <a id="task-a10"></a>A10 / A | Done2026-10-26; protocol only; real cases NotRun | Same-ID/account2physical-device Sync S00–S08;upgrade U01–U04,full salt/profile/map preservation,conflict/recovery/quota/rotation,timeout/evidence/cleanup reviewed. Same-version reload versus installed/Store upgrade distinct. | [Protocol](reports/a10-sync-upgrade-protocol.md),[review](reports/a10-independent-review.md) |
| <a id="task-a13"></a>A13 / A | Held preparation reviewed;NOT Done;A06/A07/C02 Done;stable gate C01/A23 | Approved2.3.1 held artifact74files/SHA278b59…f1e9e/root manifest/source+license provenance reviewed;not clean release commit/stable acceptance. Remaining owner blocker disposition/freeze and B07/B08/WinStable116/realSync required. | [Held provenance](reports/a13-release-candidate.md),[independent review](reports/a13-held-independent-review.md),[metadata verification](reports/c02-version-231-local-verification.md) |
| <a id="task-a21"></a>A21 / A | Done2026-10-26; audit only; actual isolation Unverified | Source/environment/tool/cache inventory and future per-boundary canary/fallback reviewed. Cached Xvfb exists but not launched/proven. No GUI/input/clipboard/VM/helper/canary/B run;all B resource gates retained. | [Audit](reports/a21-browser-isolation.md),[review](reports/a21-independent-review.md) |
| <a id="task-a23"></a>A23 / A | Partial;known FA01/UI01 heldZIP full suite/fault Passed/reviewed | Exact2.3.1 ZIP151unit+28E2E+same-ID WASM fault and nativeStable156P0 verified;prior2.3.0 proofs retained. Global defaults deferred;no old-record migration. C01 other observations unconfirmed,not a repeated provisional-test prerequisite;stable/upgrade/116/Sync scope remains. | [Rollup](reports/a23-blocker-repairs.md),[ZIP review](reports/b07-run-01-independent-review.md),[native review](reports/b03-native-stable-independent-review.md) |
| <a id="task-b01"></a>B01 / B | Partial;held2.3.1 extracted-ZIP full28/28 Passed/reviewed | Full pnpm test151unit+28E2E on LinuxCfT143,320.237s browser/no retries/skips/errors;28profiles+clipboard cleanup verified. Stable-candidate/full-platform/upgrade acceptance separate;desktop released. | [ZIP evidence](reports/b07-zip-suite.md),[independent runtime review](reports/b07-run-01-independent-review.md),[historical selected3](reports/b01-run-01.md) |
| <a id="task-b02"></a>B02 / B | Partial2026-11-02;P0–P3 selected24/24cases Passed/reviewed | Native Stable156.76/Node26.13:actual permission sets8/7/7/6;all6synthetic cases per variant Passed. Two earlier P0harness failures retained;nonhost URL/title absent without tabs while scoped Menu metadata works. Two separately authorized15min slots completed;6profiles/2workspaces cleaned,desktop returned. No permission adoption;native gesture/hotkey/clipboard/provider/edge/fullsupport coverage remains. | [Actual comparison](reports/b02-permission-runtime-20261102.md),[independent review](reports/b02-runtime-20261102-independent-review.md),[preparation](reports/b02-permission-runtime.md) |
| <a id="task-b03"></a>B03 / B | Done2026-10-30;selected Windows measurements;independent runtime review Passed | Actual Stable156.0.8078.76 and explicit CfT116.0.5845.96:P0/P4functional Passed,cold10/warm20 each,original sparseRAM plus separate5fastRAM intervals each. Cadence jitter/worker labels/noSLO/truepeak limits retained;source74/tests46 unchanged,11profiles/workspace cleaned. Not fullplatform/Store/Sync acceptance. | [Actual measurements](reports/b03-measurements-20261030.md),[independent review](reports/b03-measurements-20261030-independent-review.md),[protocol/history](reports/b03-browser-measurements.md) |
| <a id="task-b04"></a>B04 / B | Pending; relevant candidate + desktop slot | Selected real native shortcut/OS clipboard/session timer trên patch liên quan; synthetic data, focus/exclusive clipboard gates, actual deadline/cleanup/limits. Không message injection thay native command; không replay Passed ngoài affected scope. | `reports/b04-native-regression.md` |
| <a id="task-b07"></a>B07 / B | Partial;heldZIP full suite+Linux WASM fault Passed/reviewed | Exact278bZIP151unit+28E2E;separate248.666s baseline/missing/corrupt/restored same-ID fault preserves Cookbook/session refusal and restores literal Cook. Tests46 unchanged;owned processes/profiles/workspaces cleaned. Stable candidate/required platform/upgrade gates remain. | [Evidence](reports/b07-zip-suite.md),[independent review](reports/b07-run-01-independent-review.md) |
| <a id="task-b08"></a>B08 / B | Partial;selected local numeric upgrade Passed/reviewed2026-10-30 | WindowsStable156.76 patched2.3.0 ZIP8e92→2.3.1 ZIP278b,samepath/ID/profile,allSync/local preserved beforeunlock;10jars/hints/2Menus/Otherdefault/VI/4Dishes+Tags retained. NoStore/fullshared-format/U04Sync acceptance;realSync mandatory,owner schedules later. | [Selected upgrade](reports/b08-artifact-acceptance.md),[review](reports/b08-upgrade-20261030-independent-review.md) |
| <a id="task-c01"></a>C01 / C | Partial;UI01 disposition approved;other owner observations unconfirmed | Owner requested proceeding;exactheldZIP151unit+28E2E/fault and nativeStableP0 reviewed. No repeat no-other-bugs answer before provisional testing;broader bug-list/final-release acceptance unconfirmed. | [Decision/followup](reports/c01-owner-blocker-decisions.md),[ZIP review](reports/b07-run-01-independent-review.md) |
| <a id="task-c02"></a>C02 / C | Done2026-10-27 as owner decision;required runtime evidence Pending | Owner approved2.3.1/package alignment/controlledbeta/Win11x64Stable+116/real2deviceSyncbeforepublication. Metadata and held artifact locally reviewed;old2.3.0B01 remains scoped historical. No key/account/publication or blanketC01 approval. | [Decision](reports/c02-release-boundary.md),[implementation/review](reports/c02-version-231-independent-review.md) |
| <a id="task-c03"></a>C03 / C | Partial;owner has second computer,Sync slot deferred later2026-10-27 | Owner “Có, nhưng để lượt sau”;actual Sync NotRun. C02 mandatory before-publication acceptance unchanged;device/account/same-ID route and owner login readiness remain. No account/grant/publication action. | [Owner readiness](reports/c03-sync-owner-readiness.md),[protocol](reports/a10-sync-upgrade-protocol.md) |

## Bàn giao, kiểm chứng và cập nhật

- A dùng pnpm; meaningful unit/static/package checks không chiếm desktop. Root giữ
  ADB/Gradle; file này không cấp runtime permission. New code kebab-case,<200lines.
- Hiện E2E `headless:false`; full `pnpm test` gồm GUI. B phải có slot riêng hoặc
  isolation đã được chứng minh, đặc biệt native focus/clipboard/timer.
- C có thể chỉ cần trả lời message; account credentials không đưa vào report/chat.
  Hai Chrome profiles local/mock storage không thay actual cross-device Sync.
- Sửa website/legal HTML liên quan phải theo C05: push landing-page có thể auto-deploy.
- Evidence sau task ghi report thật rồi cập nhật row/catalog/master với commit/link/verdict;
  giữ historical Failed/Unknown/Pending và archived Done. Candidate đổi bytes thì
  đánh giá lại affected evidence; không blanket rerun.
- A13/B07/B08 bàn giao A14 [Phase04](phase-04-release.md#task-a14); publication xong
  mới A22 website rồi B09. Không đảo dependencies để yêu cầu listing URL trước packet.

Unresolved: remaining OWN01/C01 blocker details; C02-required support/Sync evidence; C03 permitted
devices/accounts; desktop slot và candidate/runtime cho từng B case. A02/A05/A06/A07 prep Done; product gate đóng.
