# Design baseline audit and contract draft

Date:2026-11-02, Asia/Saigon. **Read-only source audit and [independent review](design-baseline-independent-review.md) Passed.**
No GUI/browser/phone/build/Gradle/ADB executed. Source proves styling/structure,
not actual rendering,contrast,usability or visual consistency acceptance.
Owner reports website style differs and mobile UI basic;deployment deferred until
alignment. Root adopts current extension Precision as default baseline.

## Evidence hierarchy

Current runtime `chrome-extension/src/styles/tokens.css` and popup/options CSS/HTML
lead. `docs/designs/README.md`§3 explicitly supersedes old `ui-specs/00`tokens;
HTML/JSX design artboards are references,not shipped runtime or behavior proof.
Reading map:`docs/specs/reading-map.md`;Android contract:`docs/specs/mobile-cookbook.md`.
Preserve settled `reports/c01-owner-blocker-decisions.md` and
`a23-display-defaults-deferral.md` in the parent queue:no resurrected disconnected
Display defaults,global reveal/inheritance changes or new release acceptance.

## Actual mismatch inventory

| Dimension | Extension baseline — actual source | Website — actual source | Android — actual source |
|---|---|---|---|
| Palette/semantics |`tokens.css:10–42` warm ink,amber actions,moss success,coral error,honey warning;profile identity colors remain explicit exceptions. |`landing-page/styles.css:7–38` neon green/cyan/purple,cool navy,green CTAs;semantics/decorations mixed. |`MainCookbookScreen.kt:14`, `VaultScreen.kt:23` bare MaterialTheme;no application ColorScheme/custom token layer found. Do not infer device's actual colors.|
| Typography |`tokens.css:51–54` Fraunces display,Geist body,Geist Mono Dish/captions. |`styles.css:41–43`,threeHTML heads use Space Grotesk/DM Sans/Fira Code. |No custom FontFamily/Typography/font resource found;Material3 defaults.|
| Density/shape |`popup.css:9–18`,options shell760px,4px spacing;buttons4px/chips3px;quiet hairlines. Full-tab popup12px outer frame is an intentional runtime exception. |6–24px rounded/glass cards,glow shadows,large sections and decorative animated backgrounds (`styles.css:72–96`). |`CookbookScreen.kt:22`20dp page/10dp gaps,12dp generic card interior;VaultScreen24dp/12dp gaps;default Material cards/buttons and no shared density/shape grammar.|
| Brand/assets |Popup/options HTML split Pass/Chef wordmark+amber dot. |Neon favicon/OG/terminal imagery and gradient wordmark contrast with Precision. |Plain Text title and standalone EN/VI button;no shared wordmark/status/app shell.|
| Navigation/hierarchy |Popup state journeys/options tabs already accepted in bounded evidence. |Responsive section navigation and retained links;keep marketing progression instead of cloning popup400px layout. |TextButton Menu/Cook/Sources/Lock row;list/manual/sources state branches,detail temporarily replaces branch,Back returns. No native visual selected-tab hierarchy.|
| Components/states |Structured primary/secondary/ghost controls,status dot,mono Dish slab,forms and inline warnings. |Large rounded CTA/demo cards;W02 menu/FAQ semantics and W03 parallax repair already verified scoped. |Plain inline cooking/error/warning/clipboard text;DishPanel's reveal/copy buttons lack grouped slab/hierarchy;source/mapping controls densely stacked.|
| Motion/accessibility |Current functional baseline;do not assume all colors/tiny captions meet new family targets without measurement. |Preserve mobile tooltip shared anchor,AOS vertical motion,stable grid/wrapping,ARIA and live reduced-motion fallback. |Material native semantics exist but no new visual/TalkBack/large-font acceptance;single scroll Column,nonwrapping nav Row and long EN/VI labels are layout risks requiring runtime checks.|

## Proposed shared contract — freeze D02 before UI code

| Role | Exact baseline / platform adaptation |
|---|---|
| Canvas/surfaces |`#0e0e10`/`#15151a`/`#1c1c22`;borders`#232329`/`#2f2f36`;primary text`#f0ece2`,body`#d8d2c3`,secondary`#b4ad9f`/`#8a857a`. Measure actual contrast before assigning small text.|
| Action/status |Primary amber`#e8a341`,hover`#f5b35a`,pressed`#b97d23`;moss`#84b577`ready,coral`#e0746c`error,honey`#d4a548`warning. Status also has readable words/icons;do not use green for every primary action.|
| Typography |Fraunces title/wordmark,Geist body,Geist Mono Recipe/Dish;web body~16px/native14–16sp by reading task. Small extension captions are reference roles,not a demand for10sp phone text. Vietnamese glyphs/weight and fallback metrics require checks.|
| Spacing/shape |4unit rhythm;web16/24/32px gutters/native20/24dp;buttons/fields4px/dp,captions/chips3px/dp,hairline/square surface structure. Native touch targets≥48dp;never shrink for visual fidelity.|
| Components |Consistent wordmark/header/status,primary/secondary/ghost actions,labeled inputs,grouped Menu rows,masked Dish slab,helper/warning/error blocks;platform-native keyboard/back/IME behavior. No new hidden states or decorative disabled controls.|
| Navigation |Website sections vs mobile Menu/Cook/Sources vs extension accepted state/tab journeys retain their tasks. One type/color/component family,not identical information architecture or tiny popup dimensions.|
| Motion |Subtle opacity/vertical transitions;honor reduce motion in existing web/native mechanisms. Keep focus/busy feedback and explicit visible status. No CSS hiding as a width repair.|

No new universal token package/code generator. A small documented mapping with
platform-owned CSS/Kotlin values is sufficient;no import of extension dependencies
into website/Android. Existing runtime external Google Fonts import conflicts with
design README's self-host recommendation;no bundled font assets currently found.
Font source/license/files must be settled for offline phone use;role fallback can
be explicit but cannot be presented as exact Fraunces/Geist matching.
Proposed D02 sourcing:official Google Fonts Fraunces and Vercel/Google Fonts Geist
repositories;stage actual binaries and OFL in ignored evidence,record source/commit/
SHA and validate Vietnamese cmap plus offline loading before web/native delivery.
No exact font files have been downloaded or certified here. Extension self-hosting
is a separately reviewed E01 packaging option;held74-source/ZIP remains preserved.

## Android implementation constraints and screen map

- `MainCookbookScreen.kt`24lines:language CompositionLocal,unlocked branch,reload
  effect/registerClear. Add theme/shell here;do not move callbacks into visual state.
- `VaultScreen.kt`51lines:masked password/five Spices,remember(notSaveable),clears
  background/submission,busy disabling,password/biometric routes. Controller
  `VaultController.kt:82` decides first setup privately on worker;UI lacks explicit
  setup-state model. Do not guess saved-state from status strings or query storage
  in composition. Separate setup/recovery interaction is a future diagnosed scope,
  not silently introduced by a visual refresh.
- `CookbookScreen.kt`99lines:list/search/favorites/recent/pagination,manual Menu
  context,detail,masked Dish/reveal/copy. Extract presentational functions under200
  lines if needed;keep model/edit/back/invalidation/testTags and real capability set.
- `SourceScreen.kt`40lines:Google Connect/tab selection,offline import,cache clear
  confirmation;`MappingScreen.kt`31lines:role confirmation/row preview/save. Style
  these existing controls;no consent shortcut/new scopes/real Menu write.
- `MainActivity.kt:27–39,58–69`:FLAG_SECURE,biometric prompt,background lock/input
  clearing,clipboard foreground and destroy ownership stay intact. No screenshot
  workaround or auth-policy change to make design review convenient.
- JDK17/SDK36,ComposeMaterial3/BOM2026.06.01,min26/target34/compile36 are current
  `app/build.gradle.kts`;no dependency/BOM/SDK upgrade needed for initial theming.
- Preserve5minute idle/10minute total lock,10second reveal,30second owned clipboard,
  sensitive masking,EN/VI,provider/cache failure preservation. Tests select both
  labels and tags;test failures require real UI/code diagnosis,not assertion removal.

## Bounded next slice and verification

After contract review,W01 changes website `styles.css` shared semantic aliases,
control/card treatments and threeHTML font references only;DOM sections/IDs/demo
handlers/legal wording/CTA destinations remain. Preserve B05 repaired geometry.
No whole-site markup rewrite. Android M01 then introduces a small native theme and
root shell;M02–M04 handle presentational grouping in existing journeys separately.
Extension E01 is an evidence-led delta audit,default NoChange for accepted flows.

Each task compiles/parses,uses affected existing regression checks,gets independent
review,records its own changed-source limits,then updates plan before next task.
Fresh browser/phone runs require source freeze,reviewed cases and owner resource
readiness. Android FLAG_SECURE prevents ordinary capture:use synthetic safe design
boards as proposals and owner observation/native semantics for actual acceptance;
never label a static board as device proof or disable security for screenshots.

## Actual planning verification

[34-file source snapshot](../../../chrome-extension/build/cross-platform-design-consistency-20261102/baseline.json)
binds current extension/web/Android sources/Gradle/manifest/resources toHEAD352ffa9.
No new runtime/contrast/font/TalkBack/visual acceptance. Sites local-only and
presentation guidance read:preserve branding/content constraints,narrative progression
and existing checkout;no registration/sync/publication. Broader C05/C04/C11/Sync and
phone acceptance remain separate. Parent owns linkage/rollup after independent review.

## Unresolved choices

Font provenance/offline bundling/Vietnamese fallback;contract review and exact
first-slice grouping;fresh desktop/phone acceptance slots. Domain control confirmed;
Cloudflare service/project unchosen and deployment deferred,not a current question.
