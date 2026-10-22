# Track02 — Mobile acceptance và các lát sản phẩm tiếp theo

Ngày:2026-10-22. Status: **Partial product / Pending remaining tasks**.
Parent:[master plan](plan.md). Dispatch:[A/B/C catalog](task-catalog.md).

## Context và scope

Tiếp tục [Cookbook Phase1](../261012-mobile-cookbook-phase1/plan.md), không tạo lại
phase/code đã có. [Verification](../261012-mobile-cookbook-phase1/reports/verification.md)
và [independent review](../261012-mobile-cookbook-phase1/reports/independent-review.md)
giữ lịch sử. Google read/cache/network-off/cold/selected403 đã có scoped acceptance;
401/human cancel/broader lifecycle/TalkBack vẫn Pending. Keystore current-Xiaomi
đã accepted; [matrix residual](../../archive/plans/261011-keystore-recovery/reports/current-device-acceptance.md)
không mở lại completion đã nhận.

Đích gần: từng ca acceptance khả thi chạy độc lập. Đích sau: QR receiver/import,
Autofill spike, authoring và Drive/multi-profile theo
[mobile roadmap](../../docs/mobile-app-brainstorm-qa.md) và
[decisions](../../docs/decisions/mobile-product-decisions.md).
Không triển khai toàn bộ Later chỉ vì task có trong master.

## Registry và deliverables

| Task | Status / depends | Việc thực hiện và tiêu chí hoàn tất |
|---|---|---|
| <a id="task-a11"></a>A11 — prepare401 | Blocked C06 | Tái dùng provider baseline/helpers; genuine setup/Cook, freeze sau IO, retained-token tab read, exact preservation và old Dish absence; selected runner/JUnit/UUID/source/APK/history/owner/cleanup fail-closed. Compile/tests/review/freeze trước runtime. Chi tiết:[401 checklist](../261012-mobile-cookbook-phase1/reports/google-http401-preparation.md). |
| <a id="task-a12"></a>A12 — acceptance preparation | Ready | Tách plan/fixture cho human cancellation/resultCode, lifecycle/clipboard và accessibility/layout. Mỗi ca có prerequisite, bounded checkpoints, expected state, no-stop/stale policy và cleanup. Không viết fixture khi chưa có cách exercise hợp lệ; không replay Passed không liên quan. |
| <a id="task-c06"></a>C06 — actual401 | Pending owner transition; execution sau A11 | Xác định exact Google connection/project, tác động và restoration scope trước grant removal nếu chọn. Sau build/review lấy fresh readiness, biometric/provider baseline, remote action được duyệt, tab read thật. Chỉ Passed với provenance401 + token RAM cleared + reconnect + preserved cache/jars/RAM + old Dish unusable. Không dùng SDK/no-grant status thay HTTP401; không đảm bảo propagation. |
| <a id="task-c07"></a>C07 — human cancellation | Blocked A12 + external resolution khả thi | Genuine provider-loaded baseline, owner cancel ở marker, actual resultCode nếu fixture đo được; natural-lock ownership/preservation. Host OS-Back Passed là scope riêng, không thay human action. Evidence:[cancellation history](../261012-mobile-cookbook-phase1/reports/google-cancellation-preparation.md). |
| <a id="task-c08"></a>C08 — lifecycle/clipboard | Blocked A12 + reviewed per-case candidate | Tách recreation, background, process death và saved task; owner authenticate khi cần. Exact deadline/ownership phù hợp Android; record native verdict và cleanup. Controlled SIGKILL không thay natural LMK. [Return handoff](../261012-mobile-cookbook-phase1/reports/clipboard-return-handoff.md); [screen-off scope](../261012-mobile-cookbook-phase1/reports/owner-screen-off-clipboard-runtime.md). |
| <a id="task-c09"></a>C09 — accessibility/layout | Blocked A12 + phone slot | Automated nodes/rotation/font/layout riêng với owner nghe TalkBack/đánh giá UX. Per-case report gồm setting/candidate/observations. [VI association](../261012-mobile-cookbook-phase1/reports/vi-accessibility-runtime.md) không chứng nhận spoken output. |
| <a id="task-a15"></a>A15 — QR receiver | Later; p-542801dc,p-c46b217c | Chia parser/reassembly limits→camera scanner→preview/import; test malformed/duplicate/missing chunks/expiry và error preservation. [QR sender contract](../../docs/specs/qr-handoff.md), Core decrypt đã có; receiver Done local không thay physical handoff. |
| <a id="task-a16"></a>A16 — onboarding/import | Later A15 | Nối browser-first/mobile-first với encrypted storage; explicit preview/confirm; không silent overwrite Cookbook. Deliver plan + code/local regression + reviewer verdict trước C10. |
| <a id="task-a17"></a>A17 — Autofill spike | Later Phase1 usable | Native prototype lock→biometric→Cook; OS domain/package boundary; protocol RAM/latency. Build local trước physical measurement; không lấy Core Argon2 parity thay spike acceptance. |
| <a id="task-a18"></a>A18 — authoring | Later read/mapping UI; p-c1480680,p-fa39ea00 | Hai child plans trước implementation: thêm mới append và edit-existing đúng-row update. Preview/rotate nudge/write failure preservation; không delete, không ghi Menu thật mặc định. |
| <a id="task-a19"></a>A19 — Drive handoff | Later extension Drive-sync + mobile foundation; p-e117d9aa | Child plans extension sync→consent→mobile import/conflict. Không coi Chrome Sync là Drive-sync. Scope/grant/real fixture changes cần quyền cụ thể, local protocol tests tách provider evidence. |
| <a id="task-a20"></a>A20 — profiles/Autofill production | Later A16/A17 | Tách multi-profile ownership/storage khỏi native service/domain mapping. Spike findings quyết định production plan; iOS/Native hoãn khi thiếu Mac. |
| <a id="task-b10"></a>B10 — cross-platform handoff | Later feature A15/A17 phù hợp + C10 readiness, không cần C10 Done | Desktop slot nếu QR sender/native clipboard cần; actual receiver hoặc service trên apps/browsers đã chọn. Synthetic fixture, context/identity/ownership và cleanup đúng; kết quả gắn source/APK. |
| <a id="task-c10"></a>C10 — feature phone acceptance | Later per-feature A15–A20 | Mốc1 readiness/quyền camera, OS Autofill, biometric, fixture trước B10 nếu ca cần; mốc2 final acceptance sau evidence B10 hoặc selected phone run. Một feature/candidate mỗi lượt; test authorization/confirmation trước write, không đòi B10 trước readiness. Không batch tất cả thành một phiên owner chờ dài. |
| <a id="task-c13"></a>C13 — residual device matrix | Later environment available | API26/no hardware/no enrollment/typed callbacks/exact kill windows chỉ chạy ở môi trường thực phù hợp và có quyền. Retain unexecuted cells; không thay enrollment phone cá nhân để tiện test. |

## Trình tự thực thi

1. A12 lập ca khả thi, reuse reports hiện tại; A11 chỉ sau chọn transition C06.
   C06 có hai mốc: quyết định trước A11, actual phone runtime sau A11.
2. Root compile/tests/lint, independent review và freeze; không toàn bộ instrumentation.
3. Hẹn đúng owner action, fresh serial/awake/keyguard/installed/owner digest; cắm USB
   không thay readiness. Tới biometric/Google marker thì yield; mỗi reply poll native trước.
4. Kết thúc kiểm tra raw selected JUnit/terminal/native, audits và own-UUID cleanup;
   watchdog chỉ dừng host, không tự suy native đã kết thúc.
5. Review rồi update child report→child plan→track/master/catalog; vẫn Partial khi
   outcomes bắt buộc còn thiếu. Không sửa Golden/old assertions để pass.

## Evidence và report destinations

Runtime continuation cập nhật report thuộc
`plans/261012-mobile-cookbook-phase1/reports/`; raw/freeze/candidate dùng persistent
evidence root riêng mỗi ca. Link report cụ thể từ child plan và row task sau khi có.
Các feature Later phải tạo child plan dưới `plans/<date>-<feature>/plan.md` và
`reports/verification.md`, `reports/independent-review.md` trước khi claim delivery;
đây là đường dẫn dự kiến, chưa phải report đã tồn tại.

Track exit: accepted required Phase1 cells hoặc scope decision rõ ràng, rồi từng
feature mới có local/runtime evidence riêng. Không archive Mobile vì host checks Passed.
Unresolved:401 transition, cancellation availability, owner slots và extra hardware.
