# Extension completion verification

Accepted2026-10-12 (Asia/Saigon); baseline main `e195a9d`.
Implementation/QA complete; main delivery is recorded separately after pushing.

## Results

| Proof | Result | What it establishes |
|---|---|---|
| Independent Vitest |147/147,20 files,3.72s; no skip/failure| Cooking/QR compatibility plus auth/session/setup/rotation/clipboard/Builder/profile regressions |
| Independent full headed Playwright |22/22,5.2m; no skip/failure| Actual extension UI, real Worker/WASM, restart/ciphertext/clipboard and regression flows |
| Additional focused action-popover suite |3/3,13.2s; no skip/failure| Actual Chrome toolbar Quick Start/Home/Back and localized connection-error UI |
| Actual public synthetic Google Cookbook |8/8, Chromium143.0.7499.4| Genuine native command, real provider DOM, toolbar/Copy, Builder parity, tag match/refusal, empty cell and persisted Recipe |
| Golden preservation |22 original assertions unchanged| SHA256 `5f9faa1e2bb5e3d38151250ffaa961a94efba459f41786f5ae90402ab07aae26` |
| Independent production review |No remaining P1/P2| Frozen authority/lifetime/permissions/profile/Builder/Demo scope; findings repaired before tests |

The25 E2E total means22 full-suite cases plus3 newly added focused cases, not a
single25-case invocation. There was no production change between these runs.
Detailed counts/log digests/hash preservation are retained in
[independent evidence](independent-verification.json). Unit/E2E fixtures use
temporary profiles and synthetic Spices; no real account/credential was used.

Commands from `chrome-extension`, Linux Node24.18.0/pnpm10.24:

```sh
export PATH="/home/vantt/.nvm/versions/node/v24.18.0/bin:$PATH"
pnpm test:unit
DISPLAY=:0 pnpm exec playwright test --workers=1 --reporter=line
DISPLAY=:0 pnpm exec playwright test test/e2e/popup-acceptance.spec.ts --workers=1 --reporter=line
```

Final staged whitespace validation removed only blank EOF lines from
profile-rendering.js and popup-localization.js; executable text is unchanged.
The repository has no lint script. Changed JavaScript receives `node --check`,
JSON parses, and `git diff --check`; TypeScript E2E files execute through Playwright.
QR, original recipe mismatch and extension fixture files remain byte-identical.
Independent checks preserve112 non-extension product/config files and23 unrelated
Cave/skills-lock paths. No source, dependencies or build inputs from mobile/landing
changed. Root staging excludes those unrelated untracked tools.

## Relevant plan closure

- **Security:** same Argon2id/AES disk format; real bundled offscreen Worker;
  Argon2-unavailable fail closed; durable5-attempt/5-minute cooldown; serialized
  setup/unlock/session nonce; original absolute deadline; guarded encrypted commits.
  Actual two-client setup, stopped/woken SW lockout and password rotation passed.
- **Clipboard:** actual OS clipboard clears an owned Dish after30 seconds with
  copying page closed. Replaced user text survives. Denied reads/old generations
  are deterministic regressions. New permission scope is only offscreen/clipboardRead.
- **Builder:** immediate invalidation before validation/debounce; coherent snapshot;
  late tag/Dish/profile/detection/copy results ignored. All16 modifier subsets and
  all5 positions have literal independent Dish assertions; profile/rapid/invalid/
  reset behavior passed. Random picks and collapsed options are retained.
- **Tag:** actual Builder produces the tag; same-Menu/other-profile and other-Menu
  content UI refuses without a Dish; matching tags preserve exact Dish; legacy and
  missing-Menu warnings remain allowed/visible. EN/VI selection reaches content.
- **Demo/specs:** all74 marked text/alt fields localized;248/248 locale keys;
  static bundled Demo works offline at400/1200px. Both entry paths and close
  buttons passed. Current specs describe actual IDs, defaults, callbacks and limits.

## Current Popup UX A–J acceptance

| Flow | Current accepted behavior and evidence |
|---|---|
|A|Actual toolbar non-Menu Home hides badge/amber; Quick Start dismisses/persists across reopening in EN/VI.|
|B|Actual toolbar on an injected synthetic empty Menu stays Home with localized amber/context and no visible Dish; live Google empty native case also passed.|
|C|Actual Google toolbar shows exact Dish; its actual Copy button writes verified system clipboard; header Ready covered in focused popup test.|
|D|Actual toolbar Back re-reads changed focused Recipe and cooks its exact new Dish. Switching tabs closes Chrome's popover; reopening on non-Menu shows generic Home (A), rather than pressing Back in a closed popup.|
|E|Actual Builder opens with one random position/Spice and collapsed options; literal previews, invalid clearing and reset passed. Historical blank-default/stored-write steps Superseded.|
|F|Current reopen resets ingredient/output, uses accepted random defaults, and keeps advanced collapsed. Last-used restoration Superseded by18b13eb.|
|G|Current detection aligns Menu/profile context without forced expansion. Automatic expansion Superseded; stale/edit-preservation checks cover late detection. Live Menu parity passed.|
|H|Actual Quick Start Learn More and Builder How It Works open all4 Demo sections; both close controls close their tab.|
|I|An explicitly unmatched synthetic URL has a genuine missing receiver verified through Chrome before opening actual toolbar; localized reload/no-Dish UI passed EN/VI. This is a connection-error contract, not actual installation/reload reproduction.|
|J|User-selected EN/VI runtime locale reaches popup/Builder/content/Demo; locale coverage and actual responsive offline Demo passed. Changing Chrome's global language is an obsolete mechanism.|

## Actual Google and native proof

The owner supplied a publicly editable **synthetic** Cookbook. Initial blank
CSV was observed before writing only the dedicated fixture cells. The retained
[native/provider evidence](live-google-native-proof.json) contains booleans and
configuration, with no derived key/private Spice/cooked Dish values.

An ordinary selected Recipe, tagged Recipe, wrong profile and blank cell delivered
four genuine `chrome.commands` events through X11 XTest Ctrl+Shift+L. There was
**no keyboard-to-message fallback**. Google's focused rich-text editor contained
only newlines; normalizing that text before formula-bar fallback fixed the actual
baseline detection failure. A real action popover was read through its Chrome CDP
target; Copy and Builder/Google parity were independently compared to exact
synthetic expected Dishes. CSV reread confirmed fixture persistence. Temporary
browser context/profile and copied synthetic clipboard data were cleaned up.

Routed HTML tests dispatch explicit tab messages and establish extension contracts;
they are never represented as Google provider/native-shortcut proof.

## Repairs and limitations retained

The initial additional popup fixture failed1 case: Chrome actually injected a
receiver on bare `/spreadsheets`. Only the synthetic URL changed to an unmatched
path, with an added real receiving-end rejection assertion; the required localized
reload/no-Dish assertions were retained. Focused rerun3/3 passed.

Historical unmarked E2E selectors/setup helpers already disagreed with accepted
Precision/profile-modal UI at baseline. Five files were modernized to the current
real UI; exact Dish/persistence/lock assertions remain meaningful. Golden tests
were never edited, and the native test uses no message substitution.

Browser evidence is Chromium143 on WSLg, not a full OS/Chrome matrix. Minimum116
is the declared API boundary, not a tested matrix. Session timeout edges use
deterministic clocks; this report does not claim new real elapsed5/10-minute
browser measurements. One actual setup was928ms; no broad latency/detection/
memory percentage follows from that observation. Clipboard comparison/write
cannot be atomic against another application's concurrent write; OS history and
sleep-delayed cleanup are outside its guarantee.

Cross-device Chrome Sync/conflicts, broad performance/security/platform metrics,
release assets and Chrome Web Store publication remain active MVP/release work.
Android account connection/UI/QR receiver and website work remain separate plans.
No unresolved product choice blocks this completed browser slice.
