# V01 consolidation checks — 2026-11-02

**READY for independent review.** Current local evidence rollup is consistent; no content finding. Integrated browser/native/owner acceptance remains Pending. No suite, build, package, browser, server, device or network replay.

Checked frozen author versions: `verification.md` SHA256 `105f4335de15ba0e91e321fe62eaf826e785e11c1f4019852b1bb2bde4fc85cf` (79 lines); `phase-05-verification.md` SHA256 `81cb384d13d4e9d9fbc5b192179f8c6c39617719e3b81da6ba5decb6d2d81ad4` (54 lines).

Bounded read-only command:

```sh
/home/vantt/.local/bin/uv run --offline --no-project --python /usr/bin/python3 python chrome-extension/build/cross-platform-design-consistency-20261102/v01-tester-consolidation/verify-consolidation02.py
```

Exit 0. **37 local Markdown links** resolve; **11 printed SHA256 identities** match current ZIP, APKs, manifest and seven font/license resources. E04 manifest04 remains `e75402fb…e5e71`, with 397 inputs / 17 helpers exact and no runtime directory. Current development ZIP is c11e, 83 files; M04 APKs and 113 current native sources match reviewed identities.

Existing receipts support the summarized results: M01–M04 each retain four JUnit XML files totaling 10 tests / zero failures, errors or skips; current E03 unit log 151 Passed and package receipt 67 Passed; new artifact reproducibility/extraction checks Passed; E04 author 36 and independent tester 32 offline groups remain distinct. These records were inspected, not rerun. An initial checker assumed the older M01 receipt used the later `buildPassed` schema; separate corrected checker reads its actual `exit:0` record. Draft/failure preserved; no report/product change needed.

Prepared case totals match manifest: 13 geometry / 3 interaction / 7 extension required IDs, including each batch's launch canary. V01 clearly keeps all three desktop batches inside **one absolute 15-minute lease**, shared start/deadline, 12-minute cleanup and honest NotRun/Partial when budget ends. A separate fresh phone 15-minute slot begins **after desktop return**, with specific owner biometric/visual readiness, synthetic Copy permission and separately authorized APK update if needed. No new permission is inferred from consolidation.

Source/local preparation passes are separated from unexecuted integrated rendering, actual font/NFD/focus/journey checks, OS isolation and final owner verdict. Historical candidate failures and earlier B05 evidence retain their limits. Catalog's original 46 IDs, 46 canonical test files and 79-line master remain intact. `git diff --check` exit 0. No product/helper/manifest edits.

Unique ignored receipt: `chrome-extension/build/cross-platform-design-consistency-20261102/v01-tester-consolidation/verification.json`, SHA256 `2732e396eae1506069b3870a879277967c902500bc630ab0c9e716ebb4869c26`.

Unresolved: independent V01 review/root rollup, then fresh scoped desktop/phone resources and actual integrated acceptance. Deployment/release/provider/isolation gates remain held.
