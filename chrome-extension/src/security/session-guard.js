// An access check that discovers expiry also invalidates any outstanding unlock.
export function createSessionGuard({ check, invalidate }) {
    return async () => {
        const state = await check();
        if (state.expired) await invalidate();
        return state;
    };
}
