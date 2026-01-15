
self.SW_LOADED = true;
import { GeneratePassword } from "./core/usecases/generate_password.js";
import { ChromeStorageAdapter } from "./adapters/infrastructure/chrome_storage_adapter.js";
import { RegexParserAdapter } from "./adapters/infrastructure/regex_parser_adapter.js";

// Composition Root
self.DEBUG_LOGS = [];
function logDebug(msg) {
    self.DEBUG_LOGS.push(msg);
    console.log(msg);
}

logDebug("SW: Imports done");

let storageAdapter, parserAdapter, generatePasswordUseCase;

try {
    logDebug("SW: Init StorageAdapter");
    storageAdapter = new ChromeStorageAdapter();
    logDebug("SW: Init ParserAdapter");
    parserAdapter = new RegexParserAdapter();
    logDebug("SW: Init UseCase");
    generatePasswordUseCase = new GeneratePassword(parserAdapter, storageAdapter);
    self.storageAdapter = storageAdapter;
    self.generatePasswordUseCase = generatePasswordUseCase;
    logDebug("SW: Init Complete");
} catch (e) {
    logDebug(`SW: Init Error ${e.name} ${e.message} ${e.stack}`);
}

// Installation/Update Handler
// chrome.runtime.onInstalled.addListener(async () => {
//     console.log("mindVault installed/updated. Injecting content scripts...");
//     // ... logic removed to avoid duplication ...
// });

// Message Listener
logDebug("SW: Registering listener");
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    logDebug(`SW: Received ${JSON.stringify(request)} from ${sender.tab ? sender.tab.id : 'unknown'}`);
    if (request.action === "GENERATE_PASSWORD") {
        handleGeneratePassword(request.text, sendResponse);
        return true; // Keep channel open for async response
    }
});

async function handleGeneratePassword(text, sendResponse) {
    try {
        const password = await generatePasswordUseCase.execute(text);
        const settings = await storageAdapter.getSettings();
        
        sendResponse({ 
            success: true, 
            password,
            settings 
        });
    } catch (e) {
        console.error("Password generation failed:", e);
        sendResponse({ success: false, error: e.message });
    }
}

// Auto-Lock Constants
const SESSION_TIMEOUT_MIN = 10;
const IDLE_TIMEOUT_SEC = 300; // 5 minutes

// Initialize Idle Detection
chrome.idle.setDetectionInterval(IDLE_TIMEOUT_SEC);

chrome.idle.onStateChanged.addListener((state) => {
    if (state === 'idle' || state === 'locked') {
        console.log(`State changed to ${state}, locking vault.`);
        lockVault();
    }
});

// Alarm for Absolute Timeout
chrome.alarms.onAlarm.addListener((alarm) => {
    if (alarm.name === "sessionTimeout") {
        console.log("Absolute session timeout, locking vault.");
        lockVault();
    }
});

// Watch for Session Changes to start/stop timers
chrome.storage.onChanged.addListener((changes, area) => {
    if (area === 'session' && changes.sessionKey) {
        if (changes.sessionKey.newValue) {
            // Session Started
            console.log("Session started. Setting timers.");
            chrome.alarms.create("sessionTimeout", { delayInMinutes: SESSION_TIMEOUT_MIN });
        } else {
            // Session Ended
            console.log("Session ended. Clearing timers.");
            chrome.alarms.clear("sessionTimeout");
        }
    }
});

async function lockVault() {
    await chrome.storage.session.remove("sessionKey");
    // Optionally notify active tabs to close popup or update UI?
    // Messages sent to content scripts might fail if connection closed, but consistent state in storage is key.
}

console.log("Service Worker initialized with Auto-Lock");

// Handle Hotkeys
chrome.commands.onCommand.addListener((command) => {
    if (command === "generate-password") {
        chrome.tabs.query({active: true, currentWindow: true}, (tabs) => {
            if (tabs[0]) {
                chrome.tabs.sendMessage(tabs[0].id, { action: "TRIGGER_HOTKEY" });
            }
        });
    }
});
