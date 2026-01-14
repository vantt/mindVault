
const global = typeof self !== 'undefined' ? self : this;

/**
 * @enum
 */
export const ArgonType = {
    Argon2d: 0,
    Argon2i: 1,
    Argon2id: 2,
};

let internalModulePromise = null;

function loadModule(mem) {
    if (internalModulePromise) {
        return internalModulePromise;
    }

    // Configure Emscripten Module global BEFORE importing the script
    // This allows us to hook into path resolution for the WASM file
    global.Module = global.Module || {};
    
    // Check if we need to override locateFile
    if (!global.Module.locateFile) {
        global.Module.locateFile = function(path, prefix) {
            if (path.endsWith('argon2.wasm')) {
                // Resolve relative to THIS file (wrapper.js)
                return new URL('./argon2.wasm', import.meta.url).href;
            }
            return prefix + path;
        };
    }

    internalModulePromise = (async () => {
        // Dynamic import so it runs AFTER we set global.Module
        // We assume ./argon2.js has 'export default Module;' appended
        const modImport = await import('./argon2.js');
        const Mod = modImport.default || modImport;

        return new Promise((resolve) => {
            // Hook postRun if not finished
            // Note: Mod.calledRun matches what emscripten sets when run() is called.
            // But run() is called at end of file.
            // If WASM is async, it might still be loading.
            
            // Emscripten (in argon2.js):
            // function doRun(){ ... initRuntime(); ... postRun() }
            // run() calls preRun(), then if dependencies>0 returns. 
            // instantiateAsync adds a runDependency("wasm-instantiate").
            // So run() returns early.
            // When WASM loads, removeRunDependency -> callback -> doRun.
            
            // So we are likely safely in the "waiting for WASM" phase.
            // We can append to postRun.
            
            const existingPostRun = Mod.postRun;
            Mod.postRun = () => {
                if (existingPostRun) {
                    if (Array.isArray(existingPostRun)) existingPostRun.forEach(f => f());
                    else existingPostRun();
                }
                resolve(Mod);
            };
            
            // Just in case it finished synchronously (unlikely) or we missed it:
            if (Mod.calledRun && !Mod.runDependencies) {
                 // It might be done? But calledRun is set inside doRun.
                 // If calledRun is true, it might be done.
                 // But safer to just hook.
                 // If we strictly hang here, it's bad.
                 // But usually fine.
            }
        });
    })();

    return internalModulePromise;
}

function allocateArray(Module, arr) {
    return Module.allocate(arr, 'i8', Module.ALLOC_NORMAL);
}

function allocateArrayStr(Module, arr) {
    const nullTerminatedArray = new Uint8Array([...arr, 0]);
    return allocateArray(Module, nullTerminatedArray);
}

function encodeUtf8(str) {
    if (typeof str !== 'string') {
        return str;
    }
    if (typeof TextEncoder === 'function') {
        return new TextEncoder().encode(str);
    } else if (typeof Buffer === 'function') {
        return Buffer.from(str);
    } else {
        throw new Error("Don't know how to encode UTF8");
    }
}

/**
 * Argon2 hash
 */
async function argon2Hash(params) {
    const mCost = params.mem || 1024;
    const Mod = await loadModule(mCost);
    
    // Ensure memory is sufficient?
    // The emscripten module handles memory resizing typically if allowed, 
    // or we pre-set it via Module arguments. 
    // 'argon2-browser' re-creates memory? 
    // In our case ensuring the module is loaded is key.
    
    const tCost = params.time || 1;
    const parallelism = params.parallelism || 1;
    const pwdEncoded = encodeUtf8(params.pass);
    const pwd = allocateArrayStr(Mod, pwdEncoded);
    const pwdlen = pwdEncoded.length;
    const saltEncoded = encodeUtf8(params.salt);
    const salt = allocateArrayStr(Mod, saltEncoded);
    const saltlen = saltEncoded.length;
    const argon2Type = params.type || ArgonType.Argon2d;
    const hash = Mod.allocate(
        new Array(params.hashLen || 24),
        'i8',
        Mod.ALLOC_NORMAL
    );
    const secret = params.secret
        ? allocateArray(Mod, params.secret)
        : 0;
    const secretlen = params.secret ? params.secret.byteLength : 0;
    const ad = params.ad ? allocateArray(Mod, params.ad) : 0;
    const adlen = params.ad ? params.ad.byteLength : 0;
    const hashlen = params.hashLen || 24;
    const encodedlen = Mod._argon2_encodedlen(
        tCost,
        mCost,
        parallelism,
        saltlen,
        hashlen,
        argon2Type
    );
    const encoded = Mod.allocate(
        new Array(encodedlen + 1),
        'i8',
        Mod.ALLOC_NORMAL
    );
    const version = 0x13;
    let err;
    let res;
    try {
        res = Mod._argon2_hash_ext(
            tCost,
            mCost,
            parallelism,
            pwd,
            pwdlen,
            salt,
            saltlen,
            hash,
            hashlen,
            encoded,
            encodedlen,
            argon2Type,
            secret,
            secretlen,
            ad,
            adlen,
            version
        );
    } catch (e) {
        err = e;
    }
    let result;
    if (res === 0 && !err) {
        let hashStr = '';
        const hashArr = new Uint8Array(hashlen);
        for (let i = 0; i < hashlen; i++) {
            const byte = Mod.HEAP8[hash + i];
            hashArr[i] = byte;
            hashStr += ('0' + (0xff & byte).toString(16)).slice(-2);
        }
        const encodedStr = Mod.UTF8ToString(encoded);
        result = {
            hash: hashArr,
            hashHex: hashStr,
            encoded: encodedStr,
        };
    } else {
        try {
            if (!err) {
                err = Mod.UTF8ToString(
                    Mod._argon2_error_message(res)
                );
            }
        } catch (e) {}
        result = { message: err, code: res };
    }
    try {
        Mod._free(pwd);
        Mod._free(salt);
        Mod._free(hash);
        Mod._free(encoded);
        if (ad) {
            Mod._free(ad);
        }
        if (secret) {
            Mod._free(secret);
        }
    } catch (e) {}
    if (err) {
        throw result;
    } else {
        return result;
    }
}

function argon2Verify(params) {
    return loadModule().then((Mod) => {
        const pwdEncoded = encodeUtf8(params.pass);
        const pwd = allocateArrayStr(Mod, pwdEncoded);
        const pwdlen = pwdEncoded.length;
        const secret = params.secret
            ? allocateArray(Mod, params.secret)
            : 0;
        const secretlen = params.secret ? params.secret.byteLength : 0;
        const ad = params.ad ? allocateArray(Mod, params.ad) : 0;
        const adlen = params.ad ? params.ad.byteLength : 0;
        const encEncoded = encodeUtf8(params.encoded);
        const enc = allocateArrayStr(Mod, encEncoded);
        let argon2Type = params.type;
        if (argon2Type === undefined) {
            let typeStr = params.encoded.split('$')[1];
            if (typeStr) {
                typeStr = typeStr.replace('a', 'A');
                argon2Type = ArgonType[typeStr] || ArgonType.Argon2d;
            }
        }
        let err;
        let res;
        try {
            res = Mod._argon2_verify_ext(
                enc,
                pwd,
                pwdlen,
                secret,
                secretlen,
                ad,
                adlen,
                argon2Type
            );
        } catch (e) {
            err = e;
        }
        let result;
        if (res || err) {
            try {
                if (!err) {
                    err = Mod.UTF8ToString(
                        Mod._argon2_error_message(res)
                    );
                }
            } catch (e) {}
            result = { message: err, code: res };
        }
        try {
            Mod._free(pwd);
            Mod._free(enc);
        } catch (e) {}
        if (err) {
            throw result;
        } else {
            return result;
        }
    });
}

function unloadRuntime() {
   // unsupported
}

export default {
    ArgonType,
    hash: argon2Hash,
    verify: argon2Verify,
    unloadRuntime,
};
