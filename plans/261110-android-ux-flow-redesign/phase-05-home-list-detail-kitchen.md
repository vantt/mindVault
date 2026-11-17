# Phase 05 — Home list / detail / Cook / Kitchen

## Context links
- Spec §3.5–3.7, MN1, RD1, CK1, KT1–KT3, D8/D10/D12 · audit #10–#17, #22–#24 · 03-P8–P13, P24–P25

## Overview
- Priority: P2. Status: Done (2026-11-10) incl. D17 vault v2 — [report 08](reports/08-implementation-verification.md). Depends 02.
- Fast list (search + chips + row copy), reordered detail with sticky actions, clearer manual Cook, new Kitchen settings tab.

## Key insights
- Daily job = find → copy username → copy Dish. Make it 2 taps from list.
- Kitchen gives homes to Menus, Spices, lock info, language.

## Requirements
- Functional: MN1 LazyColumn 64dp rows, search label/username/website/notes, header scope selector + bottom sheet (All / per sheet w/ status + refresh), chips = filters only (All/Fav/Recent), All scope grouped by sheet w/ sticky section headers + in-section stale/error banner, search across sheets w/ sheet badge, (D17, decided yes) per-sheet secret-part set in gloss, empty/no-hit/stale states, row cook+copy (D12) w/ snackbar countdown; RD1 order username→Dish→website→notes→Recipe, sticky [Copy username][Copy Dish], favorite, website confirm; CK1 Paste + Menu dropdown "Sheet › Tab"; KT1 Menus/Spices/Security/Language/How it cooks/Lock now; KT2 refresh/check columns/remove (coral); KT3 add jar (+ change master if approved, D8); KT4 (D17) secret-part sets: create/rename/delete set, each set 1–5 parts, user picks count (D15), Settings › Sheets › sheet › "Secret parts set" picker, sets hidden while only one exists; language persisted (D10).
- Data (D17): vault format v2 = list of sets {id, name, encrypted parts}; sheet→set binding stored in snapshot metadata (non-secret). One-way migration v1 → v2 wraps existing parts as set "Default", every sheet bound to it; migration atomic (write new, verify decrypt, then swap). Cook resolves set from row's sheet before RECIPE_MISMATCH check.
- Non-functional: TalkBack semantics (masked Dish, live region copy msg w/o Dish); 200% font EN/VI; keep `menu-search`, `manual-recipe`, `manual-cook`, `dish-*`, `cook-error`.

## Architecture
Lists read `CookbookSources` snapshots via controller-provided UI models. Row copy calls `CookbookController.cook` + `OwnedClipboard` (existing). Kitchen = sections over existing controllers; Spice add via VaultController re-auth then re-encrypt jar store.

## Related code files
- Modify: `cookbook/cookbook-list.kt`, `cookbook/cookbook-manual-detail.kt`, `cookbook/cookbook-dish-panel.kt`, `cookbook/CookbookController.kt`, shared `MenuModels.kt` (search fields), `VaultController.kt` (add jar, D8; sets D17), vault store/serializer (format v2 + migration)
- Create: `home/menu-recipe-row.kt`, `home/menu-filter-chips.kt`, `home/menu-empty-states.kt`, `detail/recipe-detail-screen.kt`, `detail/sticky-copy-action-bar.kt`, `kitchen/kitchen-settings-screen.kt`, `kitchen/kitchen-menu-detail-screen.kt`, `kitchen/kitchen-spices-screen.kt`, `kitchen/language-preference-store.kt`, `kitchen/kitchen-secret-part-sets-screen.kt`, `vault/vault-format-v2-migration.kt`

## Implementation steps
1. Row + list + chips + search. 2. Empty/loading/stale states. 3. Row copy + snackbar. 4. RD1 layout + sticky bar. 5. CK1 dropdown. 6. KT1–KT2. 7. KT3 (D8). 7b. D17 vault v2 + migration + unit tests (v1 fixture decrypts after migration). 7c. KT4 sets UI + sheet binding. 8. Language persistence. 9. Semantics + strings.

## Todo
- [x] list  - [x] states  - [x] row copy  - [x] detail  - [x] Cook tab  - [x] Kitchen  - [x] Spices  - [x] D17 vault v2 + migration  - [x] D17 sets UI  - [x] language  - [x] a11y

## Success criteria
Copy Dish from list in 1 tap after unlock; detail actions in thumb zone; no blank screens; Kitchen reachable everything previously "setup only".

## Risk assessment
Vault migration bug = user locked out of secret parts → keep v1 blob until v2 verified, test with real v1 fixture. Proofs using Prev/Next pager → check before removing. Row copy without visual Dish → users unsure → snackbar names account.

## Security considerations
Dish never in snackbar/semantics; clipboard rules unchanged; Spice values never shown (count only); Spice/set change requires re-auth; set names + sheet binding non-secret but stay in no-backup storage; v2 keeps same Keystore/biometric key, no plaintext parts on disk during migration; language pref non-sensitive, no-backup.

## Next steps
Phase 06 skins these components.
