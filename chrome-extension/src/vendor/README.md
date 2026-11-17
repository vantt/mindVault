# Vendored QR encoder

Project Nayuki QR-Code-generator, MIT license retained in qrcodegen.js.
Upstream commit: `3c6d0b3cefb4e049dc337e82237c9644399716a8`.
Source: https://github.com/nayuki/QR-Code-generator/blob/3c6d0b3cefb4e049dc337e82237c9644399716a8/typescript-javascript/qrcodegen.ts

Compiled once with TypeScript 5.9.3, output unchanged:

```sh
pnpm --package=typescript@5.9.3 dlx tsc qrcodegen.ts --target es2020 --outDir compiled
```

SHA256 compiled qrcodegen.js: `79f419f267ce5a80d97f8099e0a789a4ecc4697b5348d86371a1f3b2a75d6e03`.

The extension loads this as a classic script before its Options module.
It requires no runtime package, bundler, eval, or remote download.

The compiler emits five trailing-space lines. A file-specific Git whitespace
attribute preserves the verified upstream bytes; authored files keep normal checks.

## Bundled Precision fonts

Fraunces (upright/italic),Geist and Geist Mono are unmodified variable TTFs from
Google Fonts commit `6c1dbe27dcf7d817d4b62cec033ba77fa8e2a5ec`. Each is SIL Open Font
License1.1;original copyright/license notices remain in `../assets/fonts/`:
`fraunces-ofl.txt`,`geist-ofl.txt`,`geistmono-ofl.txt`. Source URLs,upstream paths,
byte sizes and SHA256 values are recorded in `../assets/fonts/provenance.json`
and pinned by the packaging contract. Popup and Options share the font-only
`../styles/fonts.css`;standalone Demo embeds matching local face declarations.
All three reuse the same four pinned font binaries without a Google Fonts
stylesheet dependency.
No font conversion or subsetting. The family contract reserves Fraunces for static
NFC display,Geist for dynamic names/body,and Geist Mono for Recipe/Dish;remaining
consumer-role corrections are tracked separately. Bundled font bytes
do not alone prove offline font loading,glyph shaping or final browser acceptance.
