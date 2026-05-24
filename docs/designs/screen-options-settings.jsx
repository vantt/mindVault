// screen-options-settings.jsx — Options page · Settings tab
//
// The Secrets-tab → "Change Password" button switches to this tab (see spec 07).
// The tab itself is unspec'd; I designed a reasonable v2 Settings page covering
// the cross-cutting controls referenced in other specs:
//   • Change master password (Argon2id re-encrypt all profiles)
//   • Language (EN / VI — i18n spec 15)
//   • Display preferences (pepper hint default, password reveal default)
//   • Storage usage (chrome.storage.sync quota)
//   • Danger zone — reset extension
//
// States:
//   default        — view all panels
//   change-pwd     — change-password panel expanded
//   change-pwd-err — mismatch / wrong current password
//   storage-warn   — quota over 75%

function OSettingsTab({ state = "default" }) {
  const changePwd    = state === "change-pwd" || state === "change-pwd-err";
  const pwdError     = state === "change-pwd-err";
  const storageWarn  = state === "storage-warn";
  const quotaUsed    = storageWarn ? 0.82 : 0.18;

  return (
    <OPage height="auto">
      <OAppBar profile/>
      <OTabBar active="settings"/>

      <OContainer width={760} style={{ padding: `${M_SP(6)}px ${M_SP(5)}px ${M_SP(7)}px` }}>
        <OPageTitle
          eyebrow="Settings"
          title="Preferences & vault"
          sub="Master password, language, defaults, and the controls that affect every profile."
        />

        {/* MASTER PASSWORD */}
        <PanelGroup>
          <Panel
            label="Vault"
            title="Master password"
            sub="Derives the AES key for every profile via Argon2id (m=64 MB, t=3, p=1). Changing it re-encrypts all profiles in place."
          >
            {!changePwd ? (
              <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", gap: M_SP(3) }}>
                <div style={{ fontSize: 12.5, color: M_TOK.ink600 }}>
                  Last changed <span style={{ color: M_TOK.ink800 }}>3 weeks ago</span>
                  <span style={{ margin: "0 8px", color: M_TOK.ink400 }}>·</span>
                  Argon2id <span style={{ fontFamily: M_FONT.mono, color: M_TOK.ink800 }}>0.8s</span> derive
                </div>
                <OBtn kind="primary">Change Password</OBtn>
              </div>
            ) : (
              <ChangePwdForm error={pwdError}/>
            )}
          </Panel>

          {/* LANGUAGE */}
          <Panel
            label="Locale"
            title="Language"
            sub="Applies to popup, Options page, and Demo. Vietnamese is fully translated (~99 keys)."
          >
            <SegmentedRadio
              value={"en"}
              options={[
                { v: "en", label: "English",   sub: "EN" },
                { v: "vi", label: "Tiếng Việt", sub: "VI" },
              ]}
            />
          </Panel>

          {/* DISPLAY */}
          <Panel
            label="Display"
            title="Defaults"
            sub="Per-profile setting; new profiles inherit these values."
          >
            <div style={{ display: "flex", flexDirection: "column", gap: M_SP(3) }}>
              <OCheckbox
                checked={true}
                label="Show pepper hint with generated passwords"
                sub="A short hint that helps you remember which secret was used."
              />
              <OCheckbox
                checked={false}
                label="Reveal password by default after Generate"
                sub="Off: passwords stay masked (•••) until you click 👁."
              />
              <OCheckbox
                checked={true}
                label="Auto-detect Sheet ID from the active tab"
                sub="Pre-fills the Sheet field in the Recipe Builder."
              />
            </div>
          </Panel>

          {/* STORAGE */}
          <Panel
            label="Storage"
            title="chrome.storage.sync usage"
            sub="Encrypted blob synced via your Google account. Hard cap is 100 KB across all keys."
          >
            <QuotaBar used={quotaUsed} warn={storageWarn}/>
            <div style={{
              display: "flex", flexWrap: "wrap", gap: M_SP(5),
              marginTop: M_SP(3),
              fontFamily: M_FONT.mono, fontSize: 11,
              letterSpacing: "0.06em", color: M_TOK.ink600,
            }}>
              <KV label="profile:*"  v={storageWarn ? "61.4 KB / 4 keys" : "12.1 KB / 3 keys"}/>
              <KV label="shared:*"   v={storageWarn ? "18.2 KB / 5 keys" : " 3.4 KB / 1 key" }/>
              <KV label="sheetMap"   v={storageWarn ? " 1.9 KB" : " 0.4 KB"}/>
              <KV label="metadata"   v={storageWarn ? " 0.4 KB" : " 0.2 KB"}/>
            </div>
            {storageWarn && (
              <div style={{
                marginTop: M_SP(3),
                padding: `${M_SP(2) + 2}px ${M_SP(3)}px`,
                background: M_TOK.amberBg + "55",
                border: `1px solid ${M_TOK.amber}55`,
                borderRadius: 4,
                color: M_TOK.amber, fontSize: 12, lineHeight: 1.5,
              }}>
                82% used. Delete unused profiles or imported bundles to free space — Chrome rejects writes above 100 KB.
              </div>
            )}
          </Panel>

          {/* DANGER ZONE */}
          <Panel
            label="Danger zone"
            title="Reset extension"
            sub="Wipes every key from chrome.storage.sync — profiles, shared imports, assignments, and the master-password salt. This cannot be undone."
            accent={M_TOK.coral}
          >
            <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", gap: M_SP(3) }}>
              <div style={{ fontSize: 12.5, color: M_TOK.ink600, lineHeight: 1.5 }}>
                You will lose access to every recipe stored in any spreadsheet. Export your profiles first if you want to keep them.
              </div>
              <OBtn kind="secondary" danger>Reset everything…</OBtn>
            </div>
          </Panel>
        </PanelGroup>

        <Footnote/>
      </OContainer>
    </OPage>
  );
}

// ── Panel ────────────────────────────────────────────────────────────────

function PanelGroup({ children }) {
  return <div style={{ display: "flex", flexDirection: "column", gap: M_SP(3) }}>{children}</div>;
}

function Panel({ label, title, sub, accent = null, children }) {
  return (
    <div style={{
      background: M_TOK.ink100,
      border: `1px solid ${M_TOK.ink200}`,
      borderRadius: 6,
      padding: M_SP(5),
      ...(accent ? { borderLeft: `3px solid ${accent}` } : {}),
    }}>
      <div style={{
        display: "grid", gridTemplateColumns: "180px 1fr", gap: M_SP(5),
        alignItems: "flex-start",
      }}>
        <div>
          <MCaption style={{ marginBottom: 6, color: accent || M_TOK.ink500 }}>{label}</MCaption>
          <div style={{
            fontFamily: M_FONT.display, fontSize: 17, fontWeight: 400,
            letterSpacing: "-0.015em", color: M_TOK.ink900, lineHeight: 1.2,
          }}>
            <span style={{ fontStyle: "italic" }}>{title}</span>
          </div>
          {sub && (
            <div style={{ fontSize: 11.5, color: M_TOK.ink600, marginTop: 6, lineHeight: 1.5 }}>{sub}</div>
          )}
        </div>
        <div>{children}</div>
      </div>
    </div>
  );
}

// ── Change-password form ────────────────────────────────────────────────

function ChangePwdForm({ error }) {
  return (
    <div style={{ display: "flex", flexDirection: "column", gap: M_SP(3) }}>
      <Field label="Current password">
        <OPwdInput value="********"/>
      </Field>
      <Field label="New password">
        <OPwdInput value="*********"/>
        <OFieldNote tone="muted">12+ characters recommended. Argon2id makes this slow; you only need to enter it once per browser session.</OFieldNote>
      </Field>
      <Field label="Confirm new password">
        <OPwdInput value={error ? "*******" : "*********"}/>
        {error && (
          <OFieldNote tone="coral">Passwords do not match.</OFieldNote>
        )}
      </Field>

      <div style={{ display: "flex", gap: M_SP(2), marginTop: M_SP(2) }}>
        <button style={{
          background: M_TOK.amber, color: "#1a0f02", border: 0,
          padding: `${M_SP(2) + 2}px ${M_SP(4)}px`,
          fontFamily: M_FONT.body, fontSize: 13, fontWeight: 500,
          borderRadius: 4, cursor: "pointer",
        }}>
          Re-encrypt &amp; save
        </button>
        <OBtn kind="secondary">Cancel</OBtn>
        <div style={{ flex: 1 }}/>
        <div style={{ fontSize: 11.5, color: M_TOK.ink500, alignSelf: "center" }}>
          ~<span style={{ fontFamily: M_FONT.mono, color: M_TOK.ink700 }}>0.8s</span> per profile
        </div>
      </div>
    </div>
  );
}

function Field({ label, children }) {
  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
      <OLabel>{label}</OLabel>
      {children}
    </div>
  );
}

// ── Segmented radio (language) ──────────────────────────────────────────

function SegmentedRadio({ value, options }) {
  return (
    <div style={{ display: "inline-flex", padding: 3, background: M_TOK.ink050, border: `1px solid ${M_TOK.ink300}`, borderRadius: 6 }}>
      {options.map(o => {
        const active = o.v === value;
        return (
          <div key={o.v} style={{
            padding: "6px 14px",
            borderRadius: 4,
            background: active ? M_TOK.ink150 : "transparent",
            color: active ? M_TOK.ink900 : M_TOK.ink600,
            display: "flex", alignItems: "center", gap: 8,
            cursor: "pointer",
            fontSize: 12.5,
          }}>
            {active && <Dot/>}
            <span>{o.label}</span>
            <span style={{
              fontFamily: M_FONT.mono, fontSize: 10,
              letterSpacing: "0.16em", color: active ? M_TOK.amber : M_TOK.ink500,
            }}>{o.sub}</span>
          </div>
        );
      })}
    </div>
  );
}

function Dot() {
  return <span style={{
    width: 6, height: 6, borderRadius: "50%",
    background: M_TOK.amber, boxShadow: `0 0 5px ${M_TOK.amber}88`,
  }}/>;
}

// ── Quota bar ────────────────────────────────────────────────────────────

function QuotaBar({ used, warn }) {
  return (
    <div>
      <div style={{
        display: "flex", justifyContent: "space-between",
        fontFamily: M_FONT.mono, fontSize: 10.5,
        letterSpacing: "0.16em", textTransform: "uppercase",
        color: M_TOK.ink600, marginBottom: 6,
      }}>
        <span>{(used * 100).toFixed(0)}% used</span>
        <span>{(used * 100).toFixed(1)} / 100 KB</span>
      </div>
      <div style={{
        height: 6, background: M_TOK.ink050,
        border: `1px solid ${M_TOK.ink300}`,
        borderRadius: 3, overflow: "hidden",
      }}>
        <div style={{
          width: `${used * 100}%`, height: "100%",
          background: warn ? M_TOK.amber : M_TOK.moss,
          boxShadow: `0 0 8px ${(warn ? M_TOK.amber : M_TOK.moss)}55`,
        }}/>
      </div>
    </div>
  );
}

function KV({ label, v }) {
  return (
    <div>
      <div style={{ color: M_TOK.ink500, textTransform: "uppercase", letterSpacing: "0.18em", fontSize: 9.5, marginBottom: 2 }}>
        {label}
      </div>
      <div style={{ color: M_TOK.ink800, fontSize: 12, whiteSpace: "pre" }}>{v}</div>
    </div>
  );
}

// ── footnote ─────────────────────────────────────────────────────────────

function Footnote() {
  return (
    <div style={{
      marginTop: M_SP(5),
      borderTop: `1px solid ${M_TOK.ink200}`,
      paddingTop: M_SP(4),
      display: "flex", justifyContent: "space-between", alignItems: "center",
      color: M_TOK.ink500, fontSize: 11.5,
    }}>
      <div style={{ display: "flex", alignItems: "center", gap: M_SP(4) }}>
        <span style={{ fontFamily: M_FONT.mono, letterSpacing: "0.14em", textTransform: "uppercase" }}>
          mindVault · v2.4
        </span>
        <span style={{ color: M_TOK.ink400 }}>·</span>
        <span style={{ fontFamily: M_FONT.display, fontStyle: "italic", fontSize: 12.5 }}>
          Local-first. Sync via your Google account.
        </span>
      </div>
      <div style={{ display: "flex", alignItems: "center", gap: M_SP(3) }}>
        <a style={{ color: M_TOK.ink700, textDecoration: "none", borderBottom: `1px solid ${M_TOK.ink300}` }}>How it works</a>
        <a style={{ color: M_TOK.ink700, textDecoration: "none", borderBottom: `1px solid ${M_TOK.ink300}` }}>Source</a>
      </div>
    </div>
  );
}

Object.assign(window, { OSettingsTab });
