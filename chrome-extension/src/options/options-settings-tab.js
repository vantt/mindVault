/* options-settings-tab.js — Settings tab logic
   Handles: language toggle, display checkboxes, storage quota, danger zone,
   and the collapsed/expanded state of the master-password form. */

import { t } from "../i18n-loader.js";

const STORAGE_SYNC_QUOTA_BYTES = 102400; // 100 KB

// ── Helpers ──────────────────────────────────────────────────────────────────

function fmt(bytes) {
    if (bytes < 1024) return `${bytes} B`;
    return `${(bytes / 1024).toFixed(1)} KB`;
}

function pct(bytes) {
    return Math.min(100, (bytes / STORAGE_SYNC_QUOTA_BYTES) * 100);
}

// ── Language segmented radio ──────────────────────────────────────────────────

async function initLangRadio() {
    const { language = "en" } = await chrome.storage.sync.get("language");
    const options = document.querySelectorAll("#lang-radio .segmented-option");
    options.forEach(btn => {
        const isActive = btn.dataset.lang === language;
        btn.classList.toggle("active", isActive);
        btn.setAttribute("aria-checked", String(isActive));
    });
    document.getElementById("lang-radio").addEventListener("click", async (e) => {
        const btn = e.target.closest(".segmented-option");
        if (!btn) return;
        const lang = btn.dataset.lang;
        options.forEach(o => {
            const active = o.dataset.lang === lang;
            o.classList.toggle("active", active);
            o.setAttribute("aria-checked", String(active));
        });
        await chrome.storage.sync.set({ language: lang });
        location.reload();
    });
}

// ── Display settings checkboxes ───────────────────────────────────────────────

const DISPLAY_DEFAULTS = {
    showPepperHint: true,
    revealPassword: false,
    autoDetectSheet: true,
};

async function initDisplaySettings() {
    const { displaySettings = DISPLAY_DEFAULTS } = await chrome.storage.sync.get("displaySettings");
    const merged = { ...DISPLAY_DEFAULTS, ...displaySettings };

    document.getElementById("disp-pepper-hint").checked   = merged.showPepperHint;
    document.getElementById("disp-reveal-password").checked = merged.revealPassword;
    document.getElementById("disp-auto-detect-sheet").checked = merged.autoDetectSheet;

    async function saveDisplaySettings() {
        await chrome.storage.sync.set({
            displaySettings: {
                showPepperHint:   document.getElementById("disp-pepper-hint").checked,
                revealPassword:   document.getElementById("disp-reveal-password").checked,
                autoDetectSheet:  document.getElementById("disp-auto-detect-sheet").checked,
            }
        });
    }

    ["disp-pepper-hint", "disp-reveal-password", "disp-auto-detect-sheet"].forEach(id => {
        document.getElementById(id).addEventListener("change", saveDisplaySettings);
    });
}

// ── Storage quota ─────────────────────────────────────────────────────────────

async function loadStorageUsage() {
    try {
        const all = await chrome.storage.sync.get(null);
        const totalBytes = await chrome.storage.sync.getBytesInUse(null);
        const usedPct = pct(totalBytes);

        // Update quota bar
        document.getElementById("quota-pct-label").textContent = `${usedPct.toFixed(0)}% used`;
        document.getElementById("quota-abs-label").textContent = `${(totalBytes / 1024).toFixed(1)} / 100 KB`;
        const fill = document.getElementById("quota-bar-fill");
        fill.style.width = `${usedPct}%`;
        fill.classList.toggle("warn", usedPct > 75);

        // KV breakdown — count bytes per category
        const profileKeys  = Object.keys(all).filter(k => k.startsWith("profile:"));
        const sharedKeys   = Object.keys(all).filter(k => k.startsWith("shared:"));
        const sheetMapKeys = Object.keys(all).filter(k => k === "sheetMapping" || k === "sheetMap");
        const metaKeys     = Object.keys(all).filter(k =>
            !k.startsWith("profile:") && !k.startsWith("shared:") &&
            k !== "sheetMapping" && k !== "sheetMap"
        );

        async function bytesFor(keys) {
            if (!keys.length) return 0;
            return chrome.storage.sync.getBytesInUse(keys);
        }

        const [profileBytes, sharedBytes, sheetBytes, metaBytes] = await Promise.all([
            bytesFor(profileKeys),
            bytesFor(sharedKeys),
            bytesFor(sheetMapKeys),
            bytesFor(metaKeys),
        ]);

        document.getElementById("kv-profiles").textContent =
            `${fmt(profileBytes)} / ${profileKeys.length} key${profileKeys.length !== 1 ? "s" : ""}`;
        document.getElementById("kv-shared").textContent =
            `${fmt(sharedBytes)} / ${sharedKeys.length} key${sharedKeys.length !== 1 ? "s" : ""}`;
        document.getElementById("kv-sheetmap").textContent = fmt(sheetBytes);
        document.getElementById("kv-metadata").textContent = fmt(metaBytes);

        // Warning
        const warn = document.getElementById("quota-warning");
        if (usedPct > 75) {
            const msg = t("settingsStorageWarn");
            warn.textContent = `${usedPct.toFixed(0)}% ${msg || "used. Delete unused profiles or imported bundles to free space."}`;
            warn.classList.remove("hidden");
        } else {
            warn.classList.add("hidden");
        }
    } catch (err) {
        console.error("[settings] storage usage error:", err);
    }
}

// ── Password panel toggle ─────────────────────────────────────────────────────

function initPwdPanelToggle() {
    const summary    = document.getElementById("pwd-summary");
    const form       = document.getElementById("change-password-form");
    const btnShow    = document.getElementById("btn-show-change-pwd");
    const btnCancel  = document.getElementById("btn-cancel-change-pwd");

    btnShow.addEventListener("click", () => {
        summary.classList.add("hidden");
        form.classList.remove("hidden");
        document.getElementById("old-password").focus();
    });

    btnCancel.addEventListener("click", () => {
        form.classList.add("hidden");
        form.reset();
        summary.classList.remove("hidden");
    });
}

// ── Danger zone ───────────────────────────────────────────────────────────────

function initDangerZone(showToast) {
    document.getElementById("btn-reset-everything").addEventListener("click", async () => {
        const confirmed = window.confirm(
            "This will permanently wipe ALL data in chrome.storage.sync — every profile, secret, and assignment. This cannot be undone.\n\nAre you absolutely sure?"
        );
        if (!confirmed) return;
        try {
            await chrome.storage.sync.clear();
            await chrome.storage.session.clear();
            showToast("Extension reset. Reloading…");
            setTimeout(() => location.reload(), 1200);
        } catch (err) {
            showToast("Reset failed: " + err.message, true);
        }
    });
}

// ── Public init ───────────────────────────────────────────────────────────────

export async function initSettingsTab({ showToast }) {
    initPwdPanelToggle();
    await Promise.all([
        initLangRadio(),
        initDisplaySettings(),
        loadStorageUsage(),
    ]);
    initDangerZone(showToast);
}
