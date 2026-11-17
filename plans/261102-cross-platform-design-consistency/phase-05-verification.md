# Phase05 — cross-platform verification and independent acceptance

Context:[overview](plan.md),[shared contract](phase-01-contract.md),
[website](phase-02-website.md),[Android](phase-03-android.md),[extension](phase-04-extension.md).
Priority:5. Status:V01 local consolidation Done and independently reviewed. Integrated runtime/owner acceptance and V02 Pending.

## Requirements and architecture

One source-bound evidence matrix for actual palette/type/components/navigation and
existing journey states. Separate compile/static/source check,runtime observations,
owner visual verdict and broad untested matrices. No mockup/device-proof substitution.

## Related artifacts and sequence

V01 writes `reports/verification.md`:source/candidate/font/license/APK/ZIP identities,
per-slice commands/results/actual cases/cleanup/limits. V02 writes
`reports/independent-review.md` after independent source/evidence review,then final
child phase status and parent rollup. [V01 local matrix](reports/verification.md) and [bounded preparation review](reports/verification-preparation-independent-review.md) Passed;V02 remains planned,not acceptance proof.
No product source changes here except findings routed to owning slice.

- [x] V01 local source/build/package/preparation [matrix](reports/verification.md),[identity/link verification](reports/verification-preparation-checks.md) and [independent review](reports/verification-preparation-independent-review.md) Passed;root rollup updated,no suite replay or runtime acceptance.
- [x] Freeze current web19/extension83/native113/APKs/46canonical tests and E04manifest04/cases;earlier B05/style/font observations require affected retakes.
- [ ] Fresh website/extension desktop and owner/phone slots scoped independently.
- [ ] Compare actual existing journeys across platforms;record owner visual feedback.
- [ ] V02 independent review;resolve findings,reverify affected work,update all plans.
- [ ] Report alignment verdict separately from remaining feature/release scope.

## Acceptance matrix

| Area | Required evidence / limits |
|---|---|
| Shared family |Exact semantic colors/type roles/shape/spacing mappings,licensed fonts/fallbacks and actual visual comparison. Platform sizes/navigation intentionally differ.|
| Website |320/390/768/1440,fallback and available approved-font rendering,keyboard/focus/FAQ/menu,reduced motion,root bounds/tooltips/visible demos;Privacy/Terms/OG coherent. Public/deployed checks remain C05 gated.|
| Android |EN/VI,locked/busy/error,Menu list/search/filter/detail/manual Dish and source/mapping;phone font scaling/IME/rotation/readability/touch. Native biometric/provider interactions only with owner readiness.|
| Security/behavior |Controllers/policies unchanged;affected selected lock/mask/reveal/copy/error preservation verified. No claim all timers/providers/hardware reaccepted from a theme build.|
| Extension |Existing accepted journeys retained;NoChange or precisely scoped new artifact evidence if its source changes.|
| Accessibility |Readable measured contrast,text/control semantics and selected keyboard/TalkBack observations as actually executed;no blanket WCAG/TalkBack certification from source.|

## Resources, risks and exit criteria

Root exclusively ADB/Gradle;at most2subagents. No GUI/input/clipboard/phone actions
without current scoped slot/readiness. Each run has finite cases/deadline/owned
cleanup and evidence;watchdog is not termination proof. Golden immutable;no test
weakening,empty synthetic visual tests or new tests that mirror CSS assignments.
FLAG_SECURE cannot be disabled for comparison screenshots;safe synthetic design
boards remain proposals and owner/native observations establish phone acceptance.

Complete design track only when required existing UI family/flow checks and
independent review pass or explicit owner boundary records remaining scope.
Whole Mobile/MVP/release and pending Sync/provider/platform cells remain Partial.
Deployment stays deferred until alignment;even a completed design track needs
later explicit C05/C04/C11 hosting/legal/publication prerequisites and approval.

Unresolved:actual implementation/runtime/owner visual verdict and fresh resources.
