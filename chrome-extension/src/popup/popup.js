import { createPopupHome } from "./popup-home.js";
import { localizeHtml } from "./popup-localization.js";
import { initPopupSecurity } from "./popup-security.js";
import { securityRequest } from "../security/page-security.js";
import { initRecipeBuilder } from "./popup-recipe-builder.js";
import { loadLocale, t } from "../i18n-loader.js";

document.addEventListener('DOMContentLoaded', async () => {
    await loadLocale();
    localizeHtml();


    const statusSetup = document.getElementById('status-setup');
    const statusUnlocked = document.getElementById('status-unlocked');
    const statusLocked = document.getElementById('status-locked');
    const statusGenerated = document.getElementById('status-generated');
    const statusBuilder = document.getElementById('status-builder');
    const genPasswordInput = document.getElementById('gen-password');
    const genHint = document.getElementById('gen-hint');
    const genProfileLabel = document.getElementById('gen-profile-label');

    // ── Global status pill (header) ─────────────────────────────────────────
    // Per-section status indicators are gone; the header pill is the single source of truth.
    const SECTION_STATUS = {
        'status-setup':     { i18nKey: 'statusSetup',    dot: 'red',   active: false },
        'status-locked':    { i18nKey: 'statusLocked',   dot: 'red',   active: false },
        'status-unlocked':  { i18nKey: 'statusUnlocked', dot: 'green', active: true  },
        'status-generated': { i18nKey: 'statusReady',    dot: 'green', active: true  },
        'status-builder':   { i18nKey: 'statusBuilder',  dot: 'green', active: true  },
    };
    const globalStatusEl   = document.getElementById('global-status');
    const globalStatusDot  = document.getElementById('global-status-dot');
    const globalStatusText = document.getElementById('global-status-text');

    const hideAll = () => [statusSetup, statusUnlocked, statusLocked, statusGenerated, statusBuilder].forEach(el => el.classList.add('hidden'));

    let currentSection;
    let cancelDetection = () => {};
    /** Show one section by id and sync the header status pill. */
    function showSection(id) {
        currentSection = id;
        cancelDetection();
        hideAll();
        document.getElementById(id).classList.remove('hidden');
        const cfg = SECTION_STATUS[id];
        if (!cfg) return;
        globalStatusText.textContent = t(cfg.i18nKey) || cfg.i18nKey;
        globalStatusDot.className = 'dot ' + cfg.dot;
        globalStatusEl.classList.toggle('active', cfg.active);
    }

    const home = createPopupHome({ showSection, genPasswordInput, genProfileLabel, genHint,
        isHome: () => currentSection === "status-unlocked" });
    const { initQuickStart, tryAutoDetect } = home;
    cancelDetection = home.cancelDetection;

    // Recipe builder controller — wired regardless of auth state (handlers safely ignored if elements absent)
    const builder = initRecipeBuilder({
        onBack: () => showSection('status-unlocked'),
    });

    const { salt } = await chrome.storage.sync.get("salt");

    if (!salt) {
        showSection('status-setup');
    } else {
        const { unlocked } = await securityRequest("AUTH_STATUS");

        if (!unlocked) {
            showSection('status-locked');
        } else {
            showSection('status-unlocked');

            // Embed profile count into "Manage profiles" button label
            try {
                const all = await chrome.storage.sync.get(null);
                const ownCount = Object.keys(all).filter(k => k.startsWith("profile:")).length;
                const sharedCount = Object.keys(all).filter(k => k.startsWith("shared:")).length;
                const total = ownCount + sharedCount;
                if (total > 0) {
                    const manageBtn = document.getElementById('btn-manage-profiles');
                    if (manageBtn) {
                        const label = t('btnManageProfiles') || 'Manage profiles';
                        manageBtn.textContent = `${label} (${total})`;
                    }
                }
            } catch {}

            // Show Quick Start panel for first-time users (before auto-detect so panel is
            // visible on Home; if tryAutoDetect routes to Generated, panel is never seen anyway)
            await initQuickStart();

            // Try auto-generate from active cell (success → status-generated; errors stay on Home)
            const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
            void tryAutoDetect(tab);
        }
    }

    // Handlers
    const openOptions = () => chrome.runtime.openOptionsPage();

    document.getElementById('btn-start-setup')?.addEventListener('click', openOptions);
    document.getElementById('btn-settings').addEventListener('click', openOptions);
    document.getElementById('btn-manage-profiles')?.addEventListener('click', openOptions);

    // Share sheet — store pending action then open Options (which reads it after unlock)
    document.getElementById('btn-share-sheet')?.addEventListener('click', async () => {
        const sheetId = document.getElementById('btn-share-sheet').dataset.sheetId;
        if (!sheetId) return;
        await chrome.storage.session.set({ pendingAction: { type: 'share', sheetId } });
        chrome.runtime.openOptionsPage();
    });

    // Open recipe builder — reachable from both unlocked state and the auto-generated
    // state (which is what popup lands on when opened from a Sheets tab). Wiring both
    // buttons to the same handler keeps the button accessible regardless of flow.
    //
    // Order matters: reset() FIRST to clear form fields, then loadProfiles() which
    // runs sheet auto-detection and populates the URL field. Inverted order would
    // wipe the just-detected URL.
    const openBuilder = async () => {
        builder.reset();
        showSection('status-builder');
        await builder.loadProfiles();
    };
    document.getElementById('btn-build-recipe')?.addEventListener('click', openBuilder);
    document.getElementById('btn-build-recipe-gen')?.addEventListener('click', openBuilder);

    initPopupSecurity({ showSection, builder, passwordInput: genPasswordInput });

    // Back (from generated → unlocked) — re-run detection so Home reflects current tab state
    document.getElementById('btn-back').addEventListener('click', async () => {
        showSection('status-unlocked');
        // Re-query active tab — user may have switched tabs while popup was open
        const [activeTab] = await chrome.tabs.query({ active: true, currentWindow: true });
        await tryAutoDetect(activeTab);
    });
});
