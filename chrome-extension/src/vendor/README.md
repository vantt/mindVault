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
