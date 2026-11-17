# API27 clipboard fallback —4November2026

Status: **All four selected API27 native cases, independent review and owned VM cleanup Passed**.
Separate actual runtime for the API26–27 legacy adapter branch. This is emulator
clipboard evidence, not physical/Keystore authentication or Google acceptance.

Evidence: `/home/vantt/.local/share/passchef-mobile-evidence/2026-11-04-api27-clipboard/`.
Fresh `PassChefLegacy27`, serial5584, actual SDK27/x86/boot1. Google APIs image
revision11; Windows emulator36.3.10 runs the32-bit guest through its observed
`qemu-system-x86_64-headless.exe` processPID4268, launcherPID6800. Parent command
detaches, so exit0 alone never establishes VM termination. Own temporary AVD/image
and disabled host clipboard sharing; original user AVD untouched.

Candidate20 source189/ZIP and all three APKs are exact copies of the reviewed
capacity candidate. Installed guest debug/test bytes match; app bootstrap is alive,
then pre-test force-stop establishes a fresh inactive process/private namespace.
Four legacy test methods are byte-unchanged from the API26 candidate and explicitly
support SDK26–27. Retained `api26` diagnostic names are not SDK attribution: every
actual metadata frame must state API27, with the exact owned UUID/PID.

| Selected case | Native | Host |
|---|---:|---:|
| Untouched Dish expires to empty clip | Passed36.766s |38.189s |
| Foreign and null-label replacements survive | Passed66.740s |69.205s |
| Newer Copy retains its own later deadline | Passed41.532s |43.867s |
| Username survives retired Dish cleanup | Passed36.866s |39.451s |

Each method runs alone with exact JUnit[1,0], terminal[-1] and anchored `OK (1 test)`;
ordered completed phases bind one canonical UUID/PID/API27. Current source189,
exact archived source ZIP and all three candidate APKs are checked before/after.
Native inactivity and restored empty private namespace are mandatory after each
method. Overall host Passed; no timeout, automatic retry or cleanup fallback.

The real production Handler/monotonic30s deadlines run unchanged. API27 uses the
actual empty-label/empty-text ClipData fallback; absence/`hasPrimaryClip=false` is
not required. Null-label content is inspected only behind this run's marker.
Only policy metadata is read; no authentication, cooked Dish, session/clock/policy
injection or manual reconcile occurs. Sensitive extras prove metadata retention,
not an old-system privacy overlay.

Independent review verifies all four raw results, candidate/source/archive189,
61 original Android tests and54 protected/Golden files. `resource-cleanup20.json`
records native inactive/private empty before shutdown; exact-AVD launcher/QEMU/
headless processes and serial absent afterward, exact temporary Windows AVD/image
removed, definitions/APKs/results and original user AVD retained.

Unresolved: broader physical devices/authentication, background/history timing and
Google remain separate acceptance scope; no resource-release work remains for this VM.
