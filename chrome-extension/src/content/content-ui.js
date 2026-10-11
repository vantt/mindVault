// Classic content entry: selected locale without adding a web-accessible module.
(() => {
    let messages;
    const ready = (async () => {
        try {
            const response = await chrome.runtime.sendMessage({ action: 'GET_CONTENT_LOCALE' });
            if (response?.success) messages = response.messages;
        } catch { /* Browser locale remains the safe fallback. */ }
    })();
    function text(key, fallback) {
        return messages?.[key]?.message || chrome.i18n.getMessage(key) || fallback;
    }
    function error(response) {
        const codes = {
            RECIPE_MISMATCH: ['errRecipeMismatch', 'Recipe verification failed. Check the Menu and profile.'],
            SECRET_NOT_FOUND: ['hintSecretNotFound', 'Seasoning is not configured. Check Options.'],
            INVALID_RECIPE: ['hintInvalidRecipe', 'Invalid recipe format.'],
            VAULT_LOCKED: ['securityCancelled', 'Cookbook locked. Unlock again.'],
        };
        const [key, fallback] = codes[response?.code] || ['hintCellError', 'Could not cook this recipe.'];
        return text(key, fallback);
    }
    globalThis.PassChefContentUI = { ready, text, error };
})();
