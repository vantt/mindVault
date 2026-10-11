import { requireSession } from './security/background-security.js';
import { GeneratePassword } from "./core/usecases/generate_password.js";
import { ChromeStorageAdapter } from "./adapters/infrastructure/chrome_storage_adapter.js";
import { RegexParserAdapter } from "./adapters/infrastructure/regex_parser_adapter.js";
import { RecipeProfileMismatchError } from "./core/domain/recipe-errors.js";
import { computeRecipeTag } from "./core/domain/tag-codec.js";

let storageAdapter, parserAdapter, generatePasswordUseCase;

try {
    storageAdapter = new ChromeStorageAdapter();
    parserAdapter = new RegexParserAdapter();
    generatePasswordUseCase = new GeneratePassword(parserAdapter, storageAdapter);
    console.log("SW: Init complete");
} catch (e) {
    console.warn("Cookbook initialization unavailable");
}

// Run v1 → v2 migration on startup
async function runMigrationIfNeeded() {
    try {
        const migrated = await storageAdapter.migrateV1ToV2();
        if (migrated) {
            await chrome.storage.sync.set({ migrationNotified: false });
        }
    } catch (e) {
        console.warn("Cookbook migration unavailable");
    }
}

runMigrationIfNeeded();

chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {

    if (request.target === "offscreen") return;

    if (request.action === "GET_CONTENT_LOCALE") {
        if (sender.id !== chrome.runtime.id || !sender.tab?.url?.startsWith("https://docs.google.com/spreadsheets/")) return;
        (async () => {
            const { language } = await chrome.storage.sync.get("language");
            const locale = language === "vi" ? "vi" : "en";
            const response = await fetch(chrome.runtime.getURL(`_locales/${locale}/messages.json`));
            if (!response.ok) throw new Error("Locale unavailable");
            sendResponse({ success: true, messages: await response.json() });
        })().catch(() => sendResponse({ success: false }));
        return true;
    }

    if (request.action === "GENERATE_PASSWORD") {
        handleGeneratePassword(request.text, request.sheetId || null, request.profileName || null, sendResponse);
        return true;
    }

    if (request.action === "COMPUTE_RECIPE_TAG") {
        handleComputeRecipeTag(request, sendResponse);
        return true;
    }

    if (request.action === "GET_SHEET_ID_FROM_ACTIVE_TAB") {
        getSheetIdFromActiveTab().then(sendResponse);
        return true;
    }
});

async function handleGeneratePassword(text, sheetId, profileName, sendResponse) {
    try {
        const session = await requireSession();
        const result = await generatePasswordUseCase.execute(text, sheetId, profileName);
        if ((await requireSession()).nonce !== session.nonce) throw new Error("Cookbook is locked");
        sendResponse({
            success: true,
            password: result.password,
            profileName: result.profileName,
            isShared: result.isShared,
            settings: result.settings,
            warning: result.warning,
        });
    } catch (e) {

        const code = e instanceof RecipeProfileMismatchError ? "RECIPE_MISMATCH" : (e.code ||
            (e.message?.includes("not found") ? "SECRET_NOT_FOUND" : e.message === "Invalid recipe format" ? "INVALID_RECIPE" : "GENERIC"));
        sendResponse({ success: false, error: code === "INVALID_RECIPE" ? "Invalid recipe format" : code === "SECRET_NOT_FOUND" ? "Secret not found" : "Cookbook could not cook this recipe", code });
    }
}

/**
 * Compute a recipe verification tag without ever exposing raw secret to the popup.
 * Popup builder sends recipe fields + sheetId + profileName; SW resolves the raw
 * secret internally and returns only the 4-char tag.
 */
async function handleComputeRecipeTag(request, sendResponse) {
    try {
        const session = await requireSession();
        const { recipeFields, sheetId, profileName } = request;
        if (!recipeFields || !sheetId || !profileName) {
            sendResponse({ success: false, error: "Missing recipeFields/sheetId/profileName" });
            return;
        }
        // Pass sheetId=null to storage to force own-profile path with rawSecret unbound.
        // Tag computation then mixes in the build-time sheetId via HMAC message.
        const result = await storageAdapter.getSecretForGeneration(recipeFields.secretIndex, null, profileName);
        if (!result) {
            sendResponse({ success: false, error: "Secret not found", code: "SECRET_NOT_FOUND" });
            return;
        }
        const tag = await computeRecipeTag({
            hash: recipeFields.hash,
            position: recipeFields.position,
            secretIndex: recipeFields.secretIndex,
            modifiers: recipeFields.modifiers || [],
            secret: result.rawSecret,
            sheetId,
        });
        if ((await requireSession()).nonce !== session.nonce) throw new Error("Cookbook is locked");
        sendResponse({ success: true, tag });
    } catch (e) {

        sendResponse({ success: false, error: "Cookbook could not verify this recipe", code: e.code || "GENERIC" });
    }
}

/**
 * Detect Google Sheets tabs across all open windows.
 * Returns: { active: {sheetId, tabUrl, title}|null, otherTabs: [{sheetId, tabUrl, title}] }
 * Priority: active tab first; if not a Sheet, query all tabs for Sheets URLs.
 */
async function getSheetIdFromActiveTab() {
    const SHEET_RE = /\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/;
    try {
        const [activeTab] = await chrome.tabs.query({ active: true, currentWindow: true });
        const activeMatch = activeTab?.url ? activeTab.url.match(SHEET_RE) : null;
        const active = activeMatch
            ? { sheetId: activeMatch[1], tabUrl: activeTab.url, title: activeTab.title || '' }
            : null;

        // Always query other Sheets tabs so popup can offer a picker
        const allSheetTabs = await chrome.tabs.query({ url: '*://docs.google.com/spreadsheets/*' });
        const otherTabs = allSheetTabs
            .filter(t => !active || t.url !== activeTab.url)
            .map(t => {
                const m = t.url.match(SHEET_RE);
                return m ? { sheetId: m[1], tabUrl: t.url, title: t.title || '' } : null;
            })
            .filter(Boolean);

        return { active, otherTabs };
    } catch { return { active: null, otherTabs: [] }; }
}

chrome.commands.onCommand.addListener((command) => {
    if (command === "generate-password") {
        chrome.tabs.query({ active: true, currentWindow: true }, (tabs) => {
            if (tabs[0]) {
                chrome.tabs.sendMessage(tabs[0].id, { action: "TRIGGER_HOTKEY" });
            }
        });
    }
});

console.log("Service Worker v2 initialized");
