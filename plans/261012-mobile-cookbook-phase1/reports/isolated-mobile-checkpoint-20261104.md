# Isolated mobile code checkpoint —4November2026

Status: **Local verification/review Passed; scoped source commits recorded locally**.
Whole Cookbook plan remains Partial. This checkpoint does not close owner/device gates.

Base: `352ffa9300dbd3698f3d32e48b833cf41d841c83`.
Branch: `codex/mobile-cookbook-checkpoint-20261104`.
Scope:29 code paths,6 modified/23 added; source hashes frozen in external
`2026-11-04-delivery-scope/scope29.json`. HEAD preimages of all five modified Kotlin
files equal the session's original source baseline; the saved Gradle preimage also
equals HEAD. The checkpoint excludes existing theme/fonts/resources, extension,
landing-page, skills and overlapping documentation work.

## Resulting behavior

- A valid21st Menu mapping reports CACHE_FAILED and preserves the last20 cached
  sources when storage refuses the save. Invalid mappings still report INVALID_MAPPING.
- Accepted-current metadata/rows401 clears the rejected token from Google's local
  cache before a later explicit Connect authorizes. Clearing is serialized; failure
  keeps invalidation pending and fails closed. Ownership/readiness guards prevent
  completion after lock or source change from publishing a grant. No remote revoke,
  account/ACL mutation, automatic authorization loop or token persistence/logging.
- Debug-only opt-in witnesses and separate native fixtures cover authenticated
  recreation, saved system task restoration, legacy API26/27 clipboard behavior,
  mapping/limits/capacity, TalkBack and genuine conditional Google cancellation/401/
  restoration. Compiled tests do not establish device acceptance.
- Android-test Espresso3.7/runner1.7/rules1.7/extJUnit1.3 replaces the incompatible
  older instrumentation combination observed on API36. Production dependencies unchanged.

## Isolated verification

With JDK17 and SDK36, from this separate checkout:

```sh
./gradlew :shared:jvmTest :shared:assemble :sheets-android:jvmTest \
  :sheets-android:assemble :app:testDebugUnitTest :app:assembleDebug \
  :app:assembleDebugAndroidTest :app:assembleRelease :app:lintDebug
```

Passed48s;182 tasks, all182 executed. Fresh XML: shared78, Android Menu bridge22,
app16 tests; zero failures/errors/skips. App includes five new pure token-clear
ordering cases and the cache-capacity regression. Lint:0 fatal/0 errors/8 warnings/1 hint.
Original61 Android test files and54 protected/Golden files unchanged. Root workspace
index and all194 frozen candidate26 source paths remain exact.

| Isolated artifact | SHA256 |
|---|---|
| debug | `4c417ccefc01526761b1c9a7377aa65df465011e39f131ea2deafbd1dcd243f6` |
| Android test | `71be3748d47ceb5d5e62636136f11420edc6fbd8132b428c8843ccbeea2e791f` |
| unsigned release | `cbcedf2e73fb5f4672260b204453dd6854231053d684b504301ff5ca2857e756` |

External evidence: `2026-11-04-delivery-scope/verification-isolated02.json`,
`build-isolated01.log`, copied XML/lint and separately archived three APKs.
No isolated artifact was installed, signed for distribution, published or device-accepted.

Independent audits: `tester-isolated-audit01.json` and
`reviewer-isolated-checkpoint01.json`. Release manifest has backup disabled, only
the MainActivity component and no debug proof namespace/witness markers in any DEX.

## Local source commits

| Commit | Scope |
|---|---|
| `27a12a90ed106ecd1f0acdc8af4ca25357aadd4a` | Cache-capacity classification and regression |
| `a2a7a5cca08858442111a3534ed51e27ad2f3832` | Rejected Google token eviction/ordering and tests |
| `dc40229215b18ae72af95bc4aa506317ac7c427d` | Native fixtures, debug witnesses and test dependencies |

All29 final code files still match their reviewed source hashes. Staging/commits
occur only in this separate worktree; the original branch/index/source remain exact.
This report is a separate documentation checkpoint after those three source commits.

## Runtime and remaining acceptance boundary

Earlier working-tree candidate26 includes separate preexisting UI changes. Its
selected physical401 passed105.047s, SDK local eviction was observed after a retained
Failed17.988s attempt, then a separate normal Connect restored usable grant/read/
mapping/cache in33.115s. Earlier failed restoration11.014s and diagnostic8.419s
remain Failed. Owner regular files/six prior directories stayed exact; native UUIDs
were removed. These are source-history observations, not runtime proof for the
different isolated APKs above. Exact token-byte reuse/fresh consent not claimed.

The previously accepted physical recreation/TalkBack and emulator saved-task/API26/27
clipboard/proposal cases also keep their original candidates and scope. Human Google
cancellation/resultCode, different-Menu callbacks, large-font/orientation/full physical
touch coverage, natural LMK/broader task matrix and historical causes remain open.
No full-plan Done, release, merge, push or archive follows from this local checkpoint.
