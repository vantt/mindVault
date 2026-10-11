import { createRequire } from 'node:module';
import { writeFile, mkdir } from 'node:fs/promises';

const require = createRequire(import.meta.url);
const savedFetch = globalThis.fetch;
// Old Emscripten glue mistakes modern Node fetch for browser execution.
delete globalThis.fetch;
try {
  const argon2 = require('argon2-browser');
  const vector = {
    _warning: 'SYNTHETIC TEST DATA ONLY',
    password: 'SYNTHETIC_ARGON2_VECTOR_🍳', salt: 'SYNTHETIC_SALT16',
    version: 19, hashLength: 32, time: 3, memoryKiB: 65536, parallelism: 4,
  };
  const result = await argon2.hash({ pass: vector.password, salt: vector.salt,
    type: argon2.ArgonType.Argon2id, hashLen: vector.hashLength,
    time: vector.time, mem: vector.memoryKiB, parallelism: vector.parallelism });
  vector.expectedHex = result.hashHex;
  const path = new URL('../../shared/src/jvmTest/resources/argon2-reference-vector.json', import.meta.url);
  await mkdir(new URL('.', path), { recursive: true });
  await writeFile(path, JSON.stringify(vector, null, 2) + '\n');
  console.log(`Synthetic Argon2id WASM reference: ${vector.expectedHex}`);
} finally { globalThis.fetch = savedFetch; }
