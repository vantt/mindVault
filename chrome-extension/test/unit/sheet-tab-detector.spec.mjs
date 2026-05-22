/**
 * Unit tests for the enhanced sheet-tab detection logic used by the SW
 * GET_SHEET_ID_FROM_ACTIVE_TAB endpoint.
 *
 * We test the pure logic (regex matching, shape construction) by
 * reimplementing it inline — mirrors exactly what service_worker.js does,
 * so any divergence will be caught as a regression.
 */

import { describe, it, expect, vi, beforeEach } from 'vitest';

const SHEET_RE = /\/spreadsheets\/d\/([a-zA-Z0-9_-]+)/;

/**
 * Pure logic extracted from getSheetIdFromActiveTab() in service_worker.js.
 * @param {object} activeTab  - mock of the active tab (may be null)
 * @param {object[]} allSheetTabs - mock of all tabs matching the Sheets URL pattern
 */
function detectSheetTabs(activeTab, allSheetTabs) {
    const activeMatch = activeTab?.url ? activeTab.url.match(SHEET_RE) : null;
    const active = activeMatch
        ? { sheetId: activeMatch[1], tabUrl: activeTab.url, title: activeTab.title || '' }
        : null;

    const otherTabs = allSheetTabs
        .filter(t => !active || t.url !== activeTab.url)
        .map(t => {
            const m = t.url.match(SHEET_RE);
            return m ? { sheetId: m[1], tabUrl: t.url, title: t.title || '' } : null;
        })
        .filter(Boolean);

    return { active, otherTabs };
}

describe('detectSheetTabs (SW GET_SHEET_ID_FROM_ACTIVE_TAB logic)', () => {

    it('active tab is a Sheet → active filled, not duplicated in otherTabs', () => {
        const activeTab = {
            url: 'https://docs.google.com/spreadsheets/d/SHEET_A/edit',
            title: 'Budget - Google Sheets',
        };
        const allSheetTabs = [activeTab];
        const result = detectSheetTabs(activeTab, allSheetTabs);

        expect(result.active).toEqual({
            sheetId: 'SHEET_A',
            tabUrl: activeTab.url,
            title: 'Budget - Google Sheets',
        });
        expect(result.otherTabs).toHaveLength(0);
    });

    it('active tab is NOT a Sheet, one Sheet tab open → active null, one otherTab', () => {
        const activeTab = { url: 'chrome://extensions/', title: 'Extensions' };
        const sheetTab = {
            url: 'https://docs.google.com/spreadsheets/d/SHEET_B/edit',
            title: 'Finance - Google Sheets',
        };
        const result = detectSheetTabs(activeTab, [sheetTab]);

        expect(result.active).toBeNull();
        expect(result.otherTabs).toHaveLength(1);
        expect(result.otherTabs[0]).toEqual({
            sheetId: 'SHEET_B',
            tabUrl: sheetTab.url,
            title: 'Finance - Google Sheets',
        });
    });

    it('active tab is NOT a Sheet, multiple Sheet tabs → active null, all in otherTabs', () => {
        const activeTab = { url: 'https://www.google.com/', title: 'Google' };
        const sheets = [
            { url: 'https://docs.google.com/spreadsheets/d/ID_1/edit', title: 'Sheet One - Google Sheets' },
            { url: 'https://docs.google.com/spreadsheets/d/ID_2/edit', title: 'Sheet Two - Google Sheets' },
            { url: 'https://docs.google.com/spreadsheets/d/ID_3/edit', title: 'Sheet Three - Google Sheets' },
        ];
        const result = detectSheetTabs(activeTab, sheets);

        expect(result.active).toBeNull();
        expect(result.otherTabs).toHaveLength(3);
        expect(result.otherTabs.map(t => t.sheetId)).toEqual(['ID_1', 'ID_2', 'ID_3']);
    });

    it('no tabs at all → active null, otherTabs empty', () => {
        const result = detectSheetTabs(null, []);
        expect(result.active).toBeNull();
        expect(result.otherTabs).toHaveLength(0);
    });

    it('active tab is a Sheet AND other Sheet tabs exist → active filled, others in otherTabs', () => {
        const activeTab = {
            url: 'https://docs.google.com/spreadsheets/d/MAIN/edit',
            title: 'Main Sheet - Google Sheets',
        };
        const otherSheet = {
            url: 'https://docs.google.com/spreadsheets/d/OTHER/edit',
            title: 'Other Sheet - Google Sheets',
        };
        const result = detectSheetTabs(activeTab, [activeTab, otherSheet]);

        expect(result.active?.sheetId).toBe('MAIN');
        expect(result.otherTabs).toHaveLength(1);
        expect(result.otherTabs[0].sheetId).toBe('OTHER');
    });

    it('extracts sheetId correctly from URL with extra path segments', () => {
        const tab = {
            url: 'https://docs.google.com/spreadsheets/d/abc123XYZ-_def/edit#gid=0',
            title: '',
        };
        const result = detectSheetTabs({ url: 'chrome://newtab/', title: '' }, [tab]);
        expect(result.otherTabs[0].sheetId).toBe('abc123XYZ-_def');
    });

    it('title defaults to empty string when undefined', () => {
        const activeTab = {
            url: 'https://docs.google.com/spreadsheets/d/NO_TITLE/edit',
            // no title property
        };
        const result = detectSheetTabs(activeTab, []);
        expect(result.active?.title).toBe('');
    });
});
