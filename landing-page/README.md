# PassChef landing page

Buildless HTML/CSS/JavaScript for introducing the PassChef Recipe kitchen.
The [cross-platform design contract](../docs/specs/cross-platform-design.md) uses
the extension's Precision family: warm ink surfaces, amber actions, restrained
hairlines and readable semantic status colors.

## Current foundation

- Static headings: local Fraunces at explicit weight400; short display emphasis
  uses its real italic face. Body/navigation use Geist; Recipes, Dishes and the
  terminal use Geist Mono.
- Four unmodified variable TTFs and three original OFL notices live in
  `assets/fonts/`. [Font provenance](assets/fonts/provenance.json) records the
  pinned upstream commit, source URLs, sizes and SHA256 values.
- Fraunces covers the checked Vietnamese NFC text but has documented NFD gaps.
  Freeform/decomposed user labels use Geist; never normalize Recipes or Spices
  to fit a font. Cmap checks do not establish rendered mark placement.
- Shared controls use4px radii and visible amber focus. Main surfaces use square
  hairlines. Pure decorative grid/glow layers are disabled as a visual choice.
- Existing section structure, Recipe interactions, legal text and CTA targets
  are retained. Section composition and favicon/OG brand artwork remain later
  slices; this foundation is not completed visual or browser acceptance.

## Files

`index.html`, `privacy.html`, `terms.html` share `styles.css` and local font faces.
`script.js` owns existing interactions; `assets/` contains artwork and font files.
No framework, package installation or application build step is required.

From the repository root, a local preview can be served with existing uv/Python:

```bash
uv run --offline --no-project --python /usr/bin/python3 python -m http.server 8000 --bind 127.0.0.1 --directory landing-page
```

Use pnpm for any JavaScript tooling and uv for Python tooling. The website has no
Node package manifest or runtime dependency on the extension project.

## Verification boundaries

Source/font/license/HTML/CSS checks accompany the foundation delivery. New font
metrics and colors still need affected viewport, keyboard, focus, motion, tooltip,
zoom and actual font-loading checks. Older scoped browser evidence remains dated;
it does not automatically accept this changed source or a broad browser matrix.

Deployment is owner-deferred until website, Android and extension styling aligns.
The existing main-branch workflow can publish landing changes to GitHub Pages;
local editing does not authorize pushing, deploying or selecting a hosting route.
