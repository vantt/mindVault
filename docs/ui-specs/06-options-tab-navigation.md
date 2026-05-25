# 06 — Options Page · Tab Navigation

Tab bar hiển thị giữa header và main content, **chỉ khi unlocked** (dashboard-section visible).

## Layout

```
┌──────────────────────────────────────────────────────────┐
│  🔐 PassChef                                            │
│  Secure Password Generator                               │
│                                                          │
│  ┌───────────┬────────────┬────────────┐                 │
│  │  Secrets  │  Profiles  │  Settings  │  ← tab bar      │
│  └───────────┴────────────┴────────────┘                 │
│                                                          │
│  [active tab content]                                    │
└──────────────────────────────────────────────────────────┘
```

## Rules

- Tab bar (`#tab-bar`) carries class `hidden` by default; removed only when `dashboardSection` is active via `showSection()`
- Default active tab on unlock: `Secrets` (button has `class="tab active"` in HTML)
- Active tab: accent bottom-border (`--accent`) + full text color (`--text`)
- Inactive tab: subdued color (`--text-sub`); hover adds faint background tint
- Only one `.tab-content` is visible at a time; all others carry `.hidden`

## HTML

```html
<div id="tab-bar" class="tab-bar hidden">
  <button class="tab active" data-tab="secrets" data-i18n="tabSecrets">Secrets</button>
  <button class="tab" data-tab="profiles" data-i18n="tabProfiles">Profiles</button>
  <button class="tab" data-tab="settings" data-i18n="tabSettings">Settings</button>
</div>
```

Tab content panels live inside `#dashboard-section`:

```html
<section id="dashboard-section" class="hidden">
  <div id="tab-secrets"  class="tab-content">…</div>
  <div id="tab-profiles" class="tab-content hidden">…</div>
  <div id="tab-settings" class="tab-content hidden">…</div>
</section>
```

## JS — Tab Switching Logic (`options.js`)

```js
// Wire click listeners on DOMContentLoaded
function initTabBar() {
    document.querySelectorAll('.tab').forEach(tab => {
        tab.addEventListener('click', () => switchTab(tab.dataset.tab));
    });
}

// name: "secrets" | "profiles" | "settings"
async function switchTab(name) {
    // Toggle .active on all .tab buttons
    document.querySelectorAll('.tab')
        .forEach(t => t.classList.toggle('active', t.dataset.tab === name));
    // Toggle .hidden on all .tab-content divs
    document.querySelectorAll('.tab-content')
        .forEach(c => c.classList.toggle('hidden', c.id !== `tab-${name}`));
    // Lazy-init Profiles tab on first open
    if (name === 'profiles') {
        await initProfilesTab({ sessionKey, onExport: openExportWizard, onImport: openImportWizard, showToast });
    }
}
```

`showSection()` also toggles `#tab-bar` visibility:

```js
function showSection(section) {
    [setupSection, unlockSection, dashboardSection].forEach(s => s.classList.add("hidden"));
    const tabBar = document.getElementById("tab-bar");
    tabBar.classList.toggle("hidden", section !== dashboardSection);
    section.classList.remove("hidden");
}
```

Additional shortcut: `#btn-change-pwd` in the Secrets tab calls `switchTab("settings")`.

## CSS

```css
.tab-bar {
  display: flex;
  border-bottom: 1px solid var(--border);
  margin-bottom: 1.75rem;   /* NOT 1.5rem */
  gap: 4px;
}

.tab {
  padding: 9px 18px;          /* NOT 10px 20px */
  background: transparent;
  border: none;
  border-bottom: 2px solid transparent;
  margin-bottom: -1px;        /* overlaps tab-bar bottom border */
  color: var(--text-sub);     /* NOT --text-secondary */
  font-family: inherit;
  font-size: .9rem;           /* NOT 0.95rem */
  font-weight: 500;
  cursor: pointer;
  transition: color .15s, border-color .15s;   /* NOT all 0.2s */
  border-radius: 6px 6px 0 0;
}

.tab:hover:not(.active) {
  color: var(--text);
  background: rgba(255,255,255,.04);
}

.tab.active {
  color: var(--text);         /* NOT --text-primary */
  border-bottom-color: var(--accent);
}
```

## i18n Keys

| Key | EN | VI |
|-----|----|----|
| `tabSecrets` | "Secrets" | "Bí mật" |
| `tabProfiles` | "Profiles" | "Hồ sơ" |
| `tabSettings` | "Settings" | "Cài đặt" |

All three keys exist in both locale files.

## Related Screens

- [07-secrets-tab.md](./07-secrets-tab.md) — Default active tab content
