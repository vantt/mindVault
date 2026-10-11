import { t } from '../i18n-loader.js';
import { TAG_REGEX } from '../core/domain/tag-codec.js';

const ERRORS = { RECIPE_MISMATCH: 'errRecipeMismatch', VAULT_LOCKED: 'securityCancelled', SECRET_NOT_FOUND: 'hintSecretNotFound' };
export function createBuilderPreview(el, isActive) {
    let epoch = 0, timer, feedbackTimer, ready = null;
    const copyLabel = el.copyBtn.textContent;
    const current = token => isActive() && token === epoch;
    function fail(key) {
        ready = null; el.copyBtn.disabled = true; el.recipeOut.textContent = '—'; el.passwordOut.textContent = '—';
        if (el.previewError) {
            el.previewError.textContent = t(key) || t('errBuilderPreview') || 'Could not cook this recipe.';
            el.previewError.classList.remove('hidden');
        }
    }
    function invalidate() {
        epoch++; clearTimeout(timer); clearTimeout(feedbackTimer); ready = null;
        el.copyBtn.disabled = true; el.copyBtn.textContent = copyLabel;
        for (const output of [el.recipeOut, el.passwordOut]) {
            output.textContent = '—'; output.classList.add('muted'); output.classList.remove('error');
        }
        el.previewError?.classList.add('hidden');
    }
    function schedule(snapshot) {
        const token = epoch; el.passwordOut.textContent = '…';
        timer = setTimeout(() => cook(snapshot, token), 300);
    }
    async function cook(snapshot, token) {
        if (!current(token)) return;
        if (!snapshot.profileName) { fail('errNoProfile'); return; }
        let recipe = snapshot.base;
        try {
            if (snapshot.sheetId) {
                const result = await chrome.runtime.sendMessage({ action: 'COMPUTE_RECIPE_TAG',
                    recipeFields: snapshot.recipeFields, sheetId: snapshot.sheetId, profileName: snapshot.profileName });
                if (!current(token)) return;
                if (!result?.success || !TAG_REGEX.test(result.tag || '')) {
                    fail(ERRORS[result?.code] || 'errRecipeTag'); return;
                }
                recipe += `.${result.tag}`;
            }
            const result = await chrome.runtime.sendMessage({ action: 'GENERATE_PASSWORD', text: recipe,
                sheetId: snapshot.sheetId, profileName: snapshot.profileName });
            if (!current(token)) return;
            if (!result?.success || typeof result.password !== 'string') {
                fail(ERRORS[result?.code || result?.error] || 'errBuilderPreview'); return;
            }
            ready = { recipe, token }; el.recipeOut.textContent = recipe; el.passwordOut.textContent = result.password;
            [el.recipeOut, el.passwordOut].forEach(output => output.classList.remove('muted', 'error'));
            el.copyBtn.disabled = false;
        } catch { if (current(token)) fail(snapshot.sheetId ? 'errRecipeTag' : 'errBuilderPreview'); }
    }
    el.copyBtn.addEventListener('click', async () => {
        const selected = ready;
        if (!selected || !current(selected.token) || el.copyBtn.disabled) return;
        el.copyBtn.disabled = true;
        try {
            await navigator.clipboard.writeText(selected.recipe);
            if (!current(selected.token)) return;
            el.copyBtn.textContent = t('lblCopied');
            feedbackTimer = setTimeout(() => { if (current(selected.token)) el.copyBtn.textContent = copyLabel; }, 1500);
        } catch {
            if (current(selected.token) && el.previewError) {
                el.previewError.textContent = t('errRecipeCopy'); el.previewError.classList.remove('hidden');
            }
        } finally { if (current(selected.token)) el.copyBtn.disabled = false; }
    });
    return { invalidate, schedule, fail };
}
