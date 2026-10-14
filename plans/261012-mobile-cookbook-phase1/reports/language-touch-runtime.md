# Language control native touch —14October2026

Status: **Partial — center02 Passed; edge01/02 Failed; matched dwell Pending USB**.
Whole mobile Partial. Original raw48dp layout checker remains Failed/unchanged;
neither public expanded bounds nor a center pass certifies the whole touch rectangle.

One new bounded locked-UI fixture uses its own CookbookProofActivity/UUID directory,
no authentication/secret input/Google/cache/Cook/capture replay. Actual secure locked,
awake/unlocked/focused own window required. Fresh placed semantics/main-thread decor
offsets map root→window→screen; no coordinates or raw trees/text are logged.
Only booleans, DP×1000 dimensions and native event counts recorded. Actual
Instrumentation.sendPointerSync DOWN/UP required; no performClick fallback.

## Actual evidence

Edge01 **Failed1/1,1.408s**, raw JUnit[1,-2]/terminal[-1]/FAILURES, no watchdog/error.
One DOWN/UP returned; ten guards all true: finite/placed, declared touch≥48dp,
point strictly inside expanded bounds/outside raw layout, clear vertical-edge midpoint,
own window/visible frame/insets/IME-hidden. Measured raw58.153×40dp, public touch
58.153×48dp. No locale-switch marker after the first pair; generic catch cannot
identify the later failed assertion. Does not establish undersized-target/root cause.
Frozen01/source60/testa084 preserved exactly.

Center02 **Passed1/1,1.852s**, raw JUnit[1,0]/terminal[-1]/anchoredOK1,0 failures/
errors/skips, no watchdog/error. Two guarded native pairs at fresh centers with
ordinary60ms configured physical dwell; EN→VI→EN headline/Master/opposite-toggle
assertions and post-input foreground/read booleans passed. Source60/frozen02/test84dd.
This changes both point and dwell relative to edge01; no edge/cause credit.

Edge02 **Failed1/1,1.211s** on that same frozen02/test84dd, raw[1,-2]/[-1]/FAILURES,
no watchdog/error. One native pair, same raw/public touch sizes/all point guards true.
Added read-only post-input diagnostics: actualEnglish=true/Vietnamese=false,
headlineReadSucceeded/ownForeground=true. Original geometry/input/assertions unchanged;
only observer reads added. Failure persists; physical edge acceptance remains Pending.
Original source59/production/protected208/unrelated23 and owner894f73…a4d6 unchanged
before/after both runtimes. No encrypted Cookbook created; finally only owned-dir cleanup.

## Matched dwell candidate — not installed/run

New separate edge-dwell wrapper configures ordinary60ms hold, using the same edge
geometry/assertions. Original edge method defaults0/no added dwell; all assertions
retained. Existing center method still uses its own60ms. Numeric
`OptionalEdgeDwellMillis` records only the optional edge setting; never center duration.
Native selector/completion/log-stem gates distinguish center/edge/edge-dwell explicitly.

Build01 Passed51s10/101; diagnostic/center build02 Passed48s9/102. Build03 Passed52s
9/102 and exact frozen03/testa9da archived before an edge-only metadata-name repair;
no runtime under03. Corrected build04 Passed43s9/102,lint0 errors/11 warnings/1 hint,
frozen04/source60/testce61. Class stays<200lines; only this new language file differs
from earlier60 snapshots, all prior59 remain byte-identical. No new manifest entry/
production change. Releaseefd3 unchanged with prior proof exclusion; JVM/browser
counts retained, not fresh executions. All builds select debug/test/lint only.

| Identity | SHA256 |
|---|---|
| Debug retained | `1f42a2c4b369492b0ed804fab3ec282882e831336cf4ebfec48dbdba05bfd772` |
| Edge01 test | `a084e591d7d5bd2dd6e6924d4ac6d9249acd4d57164a163b3dee8076aae0fc9f` |
| Center02/edge02 last installed test | `84dd72281a1eb92f213145c360d82c77e35521a5547328ecd31807aa8f87a5e6` |
| Candidate03 test; not run | `a9dafa341d015032e1a39fc0b74035128d862050255f3ca2a51c7d79cf862021` |
| Candidate04 test; install aborted | `ce61b888629b3bb1a6343ae2b05de6dcfa642002d9b9ca4590c2a90247a69cf2` |

Install04 stopped before package checks because physical USB unavailable; repeat
read-only `adb devices -l` empty. No install/edge-dwell04 instrumentation invoked.
No driver/USB/security policy or network ADB change. Needs owner cable/readiness,
fresh serial/Awake/keyguard/digest and exact installed artifact guards before runtime.

Outside repo `/tmp/passchef-cookbook-device-acceptance/`: language-edge-build01..04,
frozen01..04/candidate01..04 (source60/exact APKs/eight helper hashes), install01/02,
edge-run01/02 and center-run02 log/result/preflight/immediate, runner and
edge-dwell-04-device-unavailable.json. Failed original binaries/reports never overwritten.

Unresolved: matched-dwell edge result and edge failure cause; old raw48dp checker,
spoken TalkBack, actual shortcut/larger-font/orientation/full target matrix. No
production repair or historical-cause resolution inferred from center acceptance.
