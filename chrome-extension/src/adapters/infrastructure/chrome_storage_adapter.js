import { IStorageRepository } from "../../core/ports/interfaces.js";
import { getSessionKey, decryptWithKey } from "./aes-storage-crypto-helper.js";

const SHEET_BIND_INFO = "mindvault-sheet-bind-v1";

export class ChromeStorageAdapter extends IStorageRepository {
    constructor() {
        super();
        this.sync = chrome.storage.sync;
        this.session = chrome.storage.session;
    }

    async isAuthenticated() {
        try {
            const { sessionKey } = await this.session.get("sessionKey");
            return !!sessionKey;
        } catch { return false; }
    }

    // ---------- Profile Routing ----------

    /**
     * Resolves which profile to use for a given sheetId.
     * @returns {{ profileKey: string, isShared: boolean, displayName: string }}
     */
    async resolveProfileForSheet(sheetId) {
        const stored = await this.sync.get(["sheetMapping", "defaultProfile"]);
        const sheetMapping = stored.sheetMapping || {};
        const defaultName = stored.defaultProfile || "Default";

        // 1. Check own profile assignments
        if (sheetId && sheetMapping[sheetId]) {
            const name = sheetMapping[sheetId];
            return { profileKey: `profile:${name}`, isShared: false, displayName: name };
        }

        // 2. Check shared profiles locked to this sheetId
        if (sheetId) {
            const all = await this.sync.get(null);
            for (const [key, val] of Object.entries(all)) {
                if (key.startsWith("shared:") && val && val.sheetId === sheetId) {
                    return { profileKey: key, isShared: true, displayName: key.slice(7) };
                }
            }
        }

        // 3. Default fallback
        return { profileKey: `profile:${defaultName}`, isShared: false, displayName: defaultName };
    }

    /** Decrypt a profile by its storage key (e.g. "profile:Default" or "shared:TeamB"). */
    async getDecryptedProfile(profileKey) {
        const keyJWK = await getSessionKey();
        const { [profileKey]: profileData } = await this.sync.get(profileKey);
        if (!profileData) throw new Error(`Profile "${profileKey}" not found`);
        return decryptWithKey(profileData.encryptedData, profileData.iv, keyJWK);
    }

    // ---------- Generation Helpers ----------

    /**
     * Get the effective secret for password generation, with profile routing.
     *
     * Returns both `rawSecret` (always = secretObj.base) and `effectiveSecret`
     * (HKDF-bound to sheetId for shared profiles only — PRD §2.4).
     *
     * **Own profiles are never bound** (design Option B — see
     * `docs/recipe-tag-design-rationale.md`). This preserves the README "Cookbook"
     * insight: user can manually compute `S_i + hash` without the extension.
     * Mismatch detection for own profiles relies on HMAC verification tag
     * (`tag-codec.js`), not silent secret divergence.
     *
     * `rawSecret` is exposed because Phase 05's `COMPUTE_RECIPE_TAG` SW endpoint
     * needs it to compute HMAC; the popup builder never sees the raw secret directly.
     *
     * @param {number} index Secret index (1-5)
     * @param {string|null} sheetId Google Sheet ID for profile routing + binding
     * @param {string|null} profileNameOverride Bypass sheet routing and load this own profile directly. Used by popup recipe builder.
     * @returns {{ rawSecret: string, effectiveSecret: string, profileName: string, isShared: boolean, settings: object, sheetId: string|null }|null}
     */
    async getSecretForGeneration(index, sheetId, profileNameOverride = null) {
        let profileKey, isShared, displayName;
        if (profileNameOverride) {
            // Explicit own-profile selection (e.g. popup recipe builder, no sheet context)
            profileKey = `profile:${profileNameOverride}`;
            isShared = false;
            displayName = profileNameOverride;
        } else {
            ({ profileKey, isShared, displayName } = await this.resolveProfileForSheet(sheetId));
        }
        const profile = await this.getDecryptedProfile(profileKey);
        const secretObj = profile.secrets?.[index.toString()];
        if (!secretObj) return null;

        const rawSecret = secretObj.base;
        // Own profiles never bind (Option B). Only shared profiles with sheetId bind via HKDF.
        const shouldBind = isShared && !!sheetId && !!rawSecret;
        const effectiveSecret = shouldBind
            ? await this._bindSecretToSheet(rawSecret, sheetId)
            : rawSecret;
        return {
            rawSecret,
            effectiveSecret,
            profileName: displayName,
            isShared,
            settings: profile.settings || {},
            sheetId: sheetId || null,
        };
    }

    async _bindSecretToSheet(secret, sheetId) {
        const keyMaterial = await crypto.subtle.importKey(
            "raw", new TextEncoder().encode(secret), "HKDF", false, ["deriveBits"]
        );
        const bits = await crypto.subtle.deriveBits(
            { name: "HKDF", hash: "SHA-256", salt: new TextEncoder().encode(sheetId), info: new TextEncoder().encode(SHEET_BIND_INFO) },
            keyMaterial, 256
        );
        return btoa(String.fromCharCode(...new Uint8Array(bits)));
    }

    // ---------- Backward Compat (v1 API) ----------

    async getSecret(index) {
        const result = await this.getSecretForGeneration(index, null);
        return result ? result.effectiveSecret : null;
    }

    // ---------- Migration ----------

    /** Migrate flat v1 storage to profile:Default. Returns true if migration occurred. */
    async migrateV1ToV2() {
        const all = await this.sync.get(null);
        const hasProfiles = Object.keys(all).some(k => k.startsWith("profile:") || k.startsWith("shared:"));
        if (hasProfiles) return false;

        if (all.encryptedData && all.iv) {
            await this.sync.set({
                "profile:Default": { encryptedData: all.encryptedData, iv: all.iv },
                defaultProfile: "Default",
                sheetMapping: {}
            });
            return true;
        }
        return false;
    }
}
