# B02 independent review — 2026-11-02

Date: 2026-11-02, Asia/Saigon.
Current verdict: **Selected P0/P1/P2/P3 comparisons and both-slot cleanup Passed,24/24 case assertions.** Earlier P0 Runs01/02 Failed and retained. Full B02 remains Partial;no minimum-permission adoption accepted.

The owner asked to continue after the prepared 15-minute desktop proposal. This review reuses the [October 30 preparation review](b02-preparation-independent-review.md) and checks the retained packet before root operates the browser. No additional permission question is introduced by this review.

## Readiness checks performed

Read the current [B02 packet](b02-permission-runtime.md), frozen inputs, helper files and native copies under [the evidence folder](../../../chrome-extension/build/b02-permissions-20261030/). Independent read-only file checks passed:

- All five runtime helpers match their reviewed local and native-copy hashes.
- All 296 native files across P0–P3 match the prepared inventories. P0 preserves the held artifact; P1 removes only `activeTab`, P2 only `tabs`, and P3 both. Permission counts remain 8/7/7/6. Other manifest fields and 73 runtime files per variant are unchanged.
- All four configs match their frozen local/native hashes. Source, profile, output, inventory and base-manifest paths remain inside the recorded owned Windows root.
- All four profile and output paths are absent. The parent profiles/results directories are empty, so no earlier runtime evidence is overwritten or profile grants reused.
- Canonical 74 source files and 46 test files match the retained snapshot. Held ZIP SHA256 remains `278b59df8ddef0085c9c064da11485695074419894c3f43b5077a10fc44f1e9e`.
- Direct file hashing of the installed Chrome executable still gives `66885d764fa17c7a7c53e1f94148e46959c5ec21fa5bc3ac22143eb2a1f7731a`, matching the frozen 156.0.8078.76 configs. Fresh Windows metadata, live version and process readiness remain root's preflight; reviewer ran no Windows command.

The retained owned root is `passchef-b02-oct30-8yT0uC`. It is intentionally retained for this run and has not been reported as cleaned.

## Reviewed execution boundary

The unchanged helpers exercise genuine setup, literal Builder control, Chrome's actual programmatic action popover, routed synthetic HTTPS Menu tabs, two-window picker/manual input, content receivers and real Options Lock. A programmatic action popover is not physical user activation; a `TRIGGER_HOTKEY` message is not a native shortcut. Synthetic Menu DOM is not live Google/provider evidence.

Root owns the browser, window focus, native preflight, raw evidence and cleanup. The reviewed packet allows up to four separate 180-second blocks, with 30-second host watchdog allowances and a 15-minute overall resource budget. A failed P0 control stops dependent comparisons. Actual closure and owned-process/profile cleanup must be proved separately from watchdog expiry.

No production manifest, Golden test, held artifact, account, clipboard, phone, Sync, policy or publication change is authorized by these disposable permission experiments. The owner-deferred real Sync requirement remains mandatory before publication.

## Findings and next checkpoint

No prelaunch blocker or actionable discrepancy found. At that initial checkpoint all B02 cases were **NotRun**. Subsequent numbered results below preserve failures and distinguish control evidence from a minimum-permission decision.

Reviewer performed no browser launch, Windows command, test replay, download or source/manager edit. Only this new review report was written; earlier preparation and runtime evidence remain unchanged.

## Native launcher diagnostic and repaired preflight

The old `C:\Program Files\nodejs\node.exe` path was absent. Retained P0 launch log shows shell exit127 before Node or Chrome started;this is a launcher-path failure,not a B02 control assertion or repo build result. Root found native Node at `C:\nvm4w\nodejs\node.exe`;actual runtime is now **Node26.13.0**,not the earlier22.14.0 environment.

Root's first native canary printed win32/x64/Node26.13.0/Playwright1.57.0 with `browserLaunched:false`,but launch02 recorded a null exit code. That output alone was not accepted as proven process success. Later launch03/04 capture actual exit0. Launch04 also records TimedOut false,StillAlive false,Termination NotNeeded.

Reviewed the new launcher and canary without executing them. Two findings were resolved before browser launch:all JSON/stdout/stderr outputs are now checked for existing evidence,and unconditional final wait was replaced with a bounded15-second cleanup wait. Owned-tree termination has a10-second wait and PID/runner identity guard;timeout,nullable exit and residual-process status are retained. No blanket Chrome kill. The main launcher wait is210seconds;termination/final-wait tail can add25seconds,so the original15-minute overall desktop limit still governs dispatch and cleanup.

Latest launcher SHA`c3e793d9b8f103ec7dc3595514ac01b59f086ede01c2834b174fd9649750a641` and canary SHA`a0da64f397b23a3dde45b78c41e15c5469e40ad610f856c1d919067931440867` match retained native copies. Canary launch04 raw confirms exit0/no timeout/no residual Node and browserLaunched false. **Repaired native preflight Passed**;prior diagnostic evidence retained. Root subsequently started real P0;no B02 runtime verdict is accepted here until its raw result arrives.

## P0 Run01 — Failed at fixture navigation

Read actual result and host launch record. Native Chrome156.0.8078.76/Node26.13.0 completed R00 setup/control,then timed out15seconds waiting for synthetic home URL;page instead reached `chrome-error://chromewebdata/`. Host exit1/no timeout/no residual Node;browser root53412 ESRCH/context closed/no page errors. Reduced variants halted without execution.

This failed before R01's permission assertions. Initial Chrome-API navigation escaping the attached Playwright route is a hypothesis,not a proven network or product root cause. Prelaunch static review did not establish runtime fixture navigation. Failed evidence remains unchanged.

Reviewed separately numbered `revised-runner-02`:only `synthetic-menus.cjs` changed,other four helper hashes identical. Chrome API now creates about:blank first,awaits actual page attachment,then uses page.goto for the same HTTPS fixture and requires a local fulfillment-count increase. Request/fulfillment/navigation observations are logged;real origin/content-script/permission checks remain unchanged. Source review Passed,synthetic helper SHA`d51e97f5bc059e6ba661bedad760cdd4815df60f717031d22efa91ef0a32522d`. Native helper/config freeze checks passed;new profiles/outputs are separately numbered. Run02 launcher is a separate retained file,changing only runner directory/output prefix from the bounded launcher.

## P0 Run02 — Failed reopening action popover

Actual R00 and R01 Passed. Home/Menu A local fulfillment,real receiver and active Menu metadata were observed;then second `chrome.action.openPopup()` rejected with **Failed to open popup** before R02 assertions. Browser root6320 ESRCH/context closed/no page errors. P1–P3 remain NotRun.

Raw target lifecycle shows the first action target detached after close but persisted until final context closure. The helper ignored Target.closeTarget's result and did not verify target disappearance. This supports unproven popup closure as a harness concern;it does not establish native-widget/focus root cause or a product permission regression. The established Linux/CfT helper also used Target.closeTarget without a disappearance assertion;historical success there does not certify this Stable156 lifecycle.

Reviewed separately numbered `revised-runner-03`:only toolbar close changed from revision02;other four helpers unchanged. One scheduled actual popup window.close is followed by an exact-target absence poll bounded15seconds,an assertion and closure log. No forced Target.closeTarget fallback or popup-opening retry;existing case assertions remain. Prelaunch source review Passed,toolbar SHA`8f6d1cac055d2413a2e78a234a87d738f26331aa77b3b163f092b839daaa0e7f`. Root may execute a new fresh-profile/control block only within the original desktop deadline and cleanup reserve. Both previous Failed controls remain recorded.

## P0 Run03 — selected control Passed

Independently read the actual result,host launch06 record,stdout route/closure observations,frozen helper/native copies and config. **All six R00–R05 cases Passed;native exit0,no timeout or residual Node,context closed.** Actual Node26.13.0/Playwright1.57.0 and CDP Chrome156.0.8078.76 match recorded identities;Chrome binary hash unchanged. Native browser root54544 exited with explicit ESRCH proof. Page errors empty.

Eight actual granted permissions equal P0's original declaration. Home/Menu A/Menu B each have an observed local fulfillment and asserted navigation;automatic real content receivers remained in use. Three separate actual action targets have positive absence/closure logs. The active Menu correctly cooked the literal Dish;two-window picker labels/selection/manual input and bundled Demo passed. Real Options Lock closed both content Dishes;repeated locked cooking refused,AUTH_STATUS locked/session absent.

Verified all74P0 source hashes against the held inventory and all five latest helper/native-copy hashes plus revision metadata/config binding before workspace removal. P1–P3 profile/result paths remain absent. Current raw result SHA`eaa882ade1cae239007636fb8e1a6bff7328e7f76e0f71887ea11bea8f170ac0`.

This stronger navigation/closure harness completed the selected control. It does not identify a general browser/native-widget root cause for earlier failures. Raw attempts remain separate;no silent retry or softened assertion. Programmatic action/receiver evidence does not prove native keys,physical activeTab grants,clipboard/provider acceptance or permission reduction.

## Slot01 dispatch stop and remaining scope

Root stopped GUI after the successful control because remaining time was below a worst-case next block plus cleanup reserve. No extension of the original15-minute resource slot;P1–P3 remain **NotRun**,not Failed or Passed. Desktop returned;no further browser run. Root's retained native postflight records no owned Chrome/Node process.

Selected P0 runtime evidence review **Passed**;B02 comparison/full retain-remove acceptance remains incomplete. Real two-device Sync stays owner-deferred and mandatory before publication. Reviewer performed no GUI/Windows query/test replay/source/manager change.

## Slot01 cleanup review

Read the retained native postflight,recorded-PID query and cleanup record. Root completed cleanup at03:19:15.322UTC,before the03:20:43deadline. Independently verified exact owned workspace absent and all24expected native result/start/stdout/stderr files retained locally. All three numbered P0 result records retain context closure and their actual browser-root ESRCH exit proofs. Source74,test46 and held ZIP278b hashes rechecked unchanged;all native helper/copy reads preceded removal.

The24recorded PID numbers are **not all absent**. Native postflight found PID54776 now belongs to `msedge.exe`,created03:17:25UTC after the final owned browser closed03:16:14.636UTC. This is a different process identity/PID reuse;it was never terminated. Separate exact-owned-workspace Chrome/Node query returned empty. These observations support owned-process cleanup without claiming a fresh unrelated process is residual Chrome. Reviewer read evidence only and ran no Windows query.

Cleanup removed the three synthetic P0 profiles and recorded owned workspace,not an owner profile/account. Desktop returned,no further GUI. **Selected runtime/cleanup independent review Passed**;P1–P3,physical/native/clipboard/provider/edge matrix and final permission decision remain open. No product permission was changed or minimum set adopted.

## Slot01 final report and manager reconciliation

At the **historical slot01 handoff**,bounded audit Passed for the author report/this review and six manager files:Track01,catalog,master,plans index,roadmap and changelog. Eight scoped Markdown files had410resolved local targets;all46catalog IDs retained exact owner anchors,master79lines. Seven published raw-record hashes matched their files. At that checkpoint status stated B02 Partial,P0 Run03's six selected cases Passed,two Failed controls retained,P1–P3 NotRun,workspace/profile cleanup complete and desktop returned. Slot02 outcomes below supersede only that earlier NotRun state. No permission reduction or minimum-set conclusion.

One stale author-report sentence still described managers as prepared. Root corrected it to the applied current rollup and labelled the prior proposal historical;re-read correction. October30 preparation now links to today's actual/review with an explicit historical-snapshot note. Earlier outcomes were not relabelled. No remaining actionable discrepancy found.

Source74/test46/held ZIP identity and absence were independently checked at cleanup;root's final-checks record confirms those postconditions again. B03 scoped Done,B08 Partial and owner-deferred mandatory Sync remain unchanged. `git diff --check` Passed. Reviewer edited only this review report;no GUI,Windows query,broad test replay,source or manager change.

## Slot02 readiness — separately authorized continuation

Owner explicitly answered “Bàn giao thêm 15 phút ngay”. New slot03:24:33→03:39:33UTC covers the same synthetic P1–P3 cases,with no account/clipboard/Sync/native-key expansion. Root staged a new owned workspace `passchef-b02-nov02-vb51hQ`;the cleaned slot01 workspace is not reused. Earlier slot01 NotRun values and failures retain their historical meaning.

Independent prelaunch checks **Passed**:five native helpers byte-match the successful P0 Run03 revision;three configs match frozen local/native hashes and all paths lie under the new owned root. All222native variant files match their expected7/7/6permission overlays and otherwise held bytes;profile/output paths absent and parent directories empty at preflight. Root's fresh host record confirms unchanged Stable156.0.8078.76/signature/Chrome SHA and Node26.13 identity,no owned Chrome. Launcher preserves reviewed bounded wait/exclusivity logic;only owned root,runner/output paths change,SHA`e89437f1a2d59cb9b89356ddfa279f58a32319cd1d7cf642a47dd9e7525ae0c7` matches freeze.

P0 result hash matches the reused actual control;no replay is required solely for new disposable profiles/paths. P1–P3 outcomes remain unaccepted until their raw results arrive. Root operates browser/focus,retains each result and observes the new deadline/cleanup reserve. No reviewer GUI,Windows command or product/test change.

## Slot02 actual reduced-variant comparison

Independently read all three actual results,native completion records and stdout fixture/closure observations. **P1/P2/P3 each Passed all six R00–R05 cases**,exit0/no timeout/no residual Node/no page errors,context closure and actual native root ESRCH proved. Actual Chrome156.0.8078.76/Node26.13.0/Playwright1.57.0 and unchanged Chrome binary hashes match the accepted P0 control. Each result binds its frozen config and exact declared/granted permission set.

| Variant | Actual granted permissions | Native browser root | Native UTC span | Result SHA256 |
|---|---|---:|---:|---|
| P1 |7;activeTab absent,tabs retained |52152|11.504s|`15fdc34a60146c0806c4371a19bf90ce23bc846c7d5b3d053020859ae0893069`|
| P2 |7;tabs absent,activeTab retained |53092|9.966s|`8ccb8b993d0a1e9f3acb7396785ee9c3c1b31134224d043d91b6eb0f1559289b`|
| P3 |6;tabs and activeTab absent |38692|10.191s|`a7e321e05d10eaccc9a9a68c6140b9375f6f88d6be44fb8386ad2979f40acb25`|

These spans are native wall-clock observations,not benchmark/derivation comparisons. Each variant has three fulfilled local fixtures,three observed actual action-target closures,two distinct Menu windows,correct picker labels/Dish,real Options Lock/no session and locked cooking refusal. No assertions or helpers were weakened between control and variants.

Observed A05-relevant metadata difference:with P0/P1,nonhost home and about:blank tabs expose URL/title. With P2/P3 those fields are absent;own Options and supported HTTPS docs Menu metadata remain available,including exact active Menu and two-window picker URLs/titles. Host grants remain unchanged and origin-wide;this is not a claim that all Google metadata access ended. No physical activation/cross-origin grant/native command or provider result is inferred.

Verified all five latest native helpers,three native config hashes and222variant source files again after runs,before root cleanup. Native copy reads complete. Four accepted selected runs now account for24Passed case assertions;the two earlier Failed P0 controls remain separately preserved. Root stopped GUI and returned desktop without expanding scope. Slot02 native process/profile/workspace postflight remains the final review checkpoint;full B02 and permission adoption remain incomplete for the documented unselected cases.

## Slot02 cleanup — Passed

Read retained operator cleanup/native postflight and independently verified exact owned root `passchef-b02-nov02-vb51hQ` absent,three profile names accounted for and all15expected native result/start/completion/stdout/stderr files retained locally. Three actual results still retain successful context closure/root ESRCH. Cleanup03:30:45.769UTC precedes slot deadline03:39:33;desktop returned. No further browser or new scope.

The native query records no exact-owned Chrome/Node. Of24recorded PID numbers,15060 now belongs to `msedge.exe`,created03:29:55.307UTC after final owned Chrome closed03:28:04.737UTC. This different identity is PID reuse and was never terminated;no claim all numeric PID values are absent. Reviewer read native evidence without executing a Windows query.

Canonical74source,46tests and held ZIP278b independently rehashed unchanged after cleanup. No production permission or artifact adoption occurred. **Selected P0–P3 runtime and cleanup independent review Passed**;full B02 remains Partial for native/physical-grant/clipboard/provider/edge/support cases and final permission decision. Mandatory real Sync remains owner-deferred.

## Final slot02 report/manager reconciliation

Bounded final audit **Passed** across11scoped Markdown documents:today's report/review,six manager files,A05 audit,permissions working record and the old preparation pointer.475local targets resolved;46catalog IDs retain their exact owner anchors;master79lines. Ten published raw-result/check hashes independently match files. Prior slot01 NotRun statements and October preparation are explicitly historical;earlier Failed controls remain retained.

Current statements agree on selected P0–P3/R00–R05 **24/24 Passed**,actual8/7/7/6grants,full B02 Partial and no permission adoption. Both separately authorized15-minute slots ended with all six profiles/two exact workspaces removed and desktop returned. A05/legal working-record additions accurately bound the observed nonhost metadata difference and distinguish held2.3.1 beta from older2.3.0 snapshots;they do not certify privacy,necessity or broader acceptance.

Source74/test46/held ZIP identity and both-root absence were independently checked at cleanup;root's fresh final-slot02 record confirms them again,including unchanged production8permissions. B03 scoped Done,B08 Partial and deferred mandatory Sync/release gates remain. No open actionable discrepancy.`git diff --check` Passed. Reviewer changed only this review report;no further browser,Windows query,test replay,source or manager edit.
