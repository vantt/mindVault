// screen-options-secrets.jsx — Options page · Secrets tab
//
// Per spec 07-secrets-tab.md:
//   • 5 secret inputs (password type, eye toggle)
//   • Profile context badge above
//   • Inline "Show pepper hint" checkbox
//   • Actions: Save Changes (primary) · Change Password · Lock
//
// States (one render per state, all share this file):
//   default   — Banking profile, secrets pre-filled, all hidden
//   reveal    — secret #2 unmasked
//   saving    — submit in progress
//   saved     — success toast visible
//   migration — one-time toast about v1 → v2 migration

function OSecretsTab({ state = "default" }) {
  const isSaving    = state === "saving";
  const isSaved     = state === "saved";
  const showMigrate = state === "migration";
  const reveal2     = state === "reveal";

  const profileName = "Banking";

  const secrets = [
    { i: 1, val: "midnight harbour fern" },
    { i: 2, val: "salt copper afternoon" },
    { i: 3, val: "owls quiet glassworks" },
    { i: 4, val: "" },
    { i: 5, val: "" },
  ];

  return (
    <OPage>
      <OAppBar profile />
      <OTabBar active="secrets" />

      <OContainer width={760} style={{ padding: `${M_SP(6)}px ${M_SP(5)}px ${M_SP(7)}px` }}>
        <OPageTitle
          eyebrow="Secrets · per profile"
          title="Manage Secrets"
          sub="Five private phrases that combine with each recipe to derive its password. Nothing here ever leaves your device unencrypted."
          right={
            <div style={{
              display: "inline-flex", alignItems: "center", gap: 8,
              padding: "5px 10px 5px 8px",
              border: `1px solid ${M_TOK.ink300}`,
              borderRadius: 999,
              fontSize: 11.5, color: M_TOK.ink700,
              background: M_TOK.ink100,
            }}>
              <span style={{
                width: 8, height: 8, borderRadius: "50%",
                background: "#58a6ff",
                boxShadow: "0 0 6px #58a6ff66",
              }} />
              <span style={{ fontFamily: M_FONT.mono, fontSize: 10.5, letterSpacing: "0.14em", textTransform: "uppercase", color: M_TOK.ink600 }}>
                Editing
              </span>
              <span style={{ color: M_TOK.ink900, fontWeight: 500 }}>{profileName}</span>
            </div>
          }
        />

        <OCard padding={5} accent={M_TOK.amber}>
          <OSectionHeader>The five phrases</OSectionHeader>

          <div style={{ display: "flex", flexDirection: "column", gap: M_SP(3) }}>
            {secrets.map(s => (
              <SecretRow key={s.i}
                index={s.i}
                value={s.val}
                visible={reveal2 && s.i === 2}
              />
            ))}
          </div>

          <div style={{ height: 1, background: M_TOK.ink200, margin: `${M_SP(5)}px 0 ${M_SP(4)}px` }} />

          <OSectionHeader>Display</OSectionHeader>
          <OCheckbox
            checked={true}
            label="Show pepper hint in popup"
            sub="A short hint shown next to the generated password to help you recall which phrase was used. Stored per-profile, inside the encrypted blob."
          />

          <div style={{ height: 1, background: M_TOK.ink200, margin: `${M_SP(5)}px 0 ${M_SP(4)}px` }} />

          <div style={{ display: "flex", gap: M_SP(3), alignItems: "center", flexWrap: "wrap" }}>
            <PrimarySaveButton saving={isSaving} />
            <OBtn kind="secondary">Change Password</OBtn>
            <div style={{ flex: 1 }} />
            <OBtn kind="secondary" leading={<MLockIcon size={11}/>}>Lock</OBtn>
          </div>
        </OCard>

        <FooterMicroLegend />
      </OContainer>

      {isSaved && (
        <OToast tone="moss">
          <MCheckTiny size={12}/> Secrets saved
        </OToast>
      )}
      {showMigrate && (
        <OToast tone="moss" style={{ bottom: M_SP(5) }}>
          <span style={{ fontFamily: M_FONT.mono, fontSize: 10, letterSpacing: "0.16em", textTransform: "uppercase", color: M_TOK.moss }}>
            v1 → v2
          </span>
          <span style={{ color: M_TOK.ink800 }}>
            Migrated. Your secrets are now in profile <em style={{ fontFamily: M_FONT.display, fontStyle: "italic" }}>“Default”</em>.
          </span>
        </OToast>
      )}
    </OPage>
  );
}

// ── Subcomponents ────────────────────────────────────────────────────────

function SecretRow({ index, value, visible }) {
  const dots = "•".repeat(Math.max(value.length, 0));
  return (
    <div style={{
      display: "grid", gridTemplateColumns: "84px 1fr",
      alignItems: "center", gap: M_SP(3),
    }}>
      <div style={{
        fontFamily: M_FONT.mono, fontSize: 11,
        letterSpacing: "0.14em", textTransform: "uppercase",
        color: M_TOK.ink600,
      }}>
        Secret <span style={{ color: M_TOK.amber, marginLeft: 2 }}>#{index}</span>
      </div>
      <div style={{
        display: "flex", alignItems: "center", gap: M_SP(2),
        background: M_TOK.ink050,
        border: `1px solid ${M_TOK.ink300}`,
        borderRadius: 4,
        padding: `0 ${M_SP(2) + 2}px 0 ${M_SP(3)}px`,
      }}>
        <div style={{
          flex: 1,
          padding: `${M_SP(2) + 2}px 0`,
          fontFamily: visible ? M_FONT.body : M_FONT.mono,
          fontSize: 13,
          fontStyle: visible ? "italic" : "normal",
          color: value ? M_TOK.ink900 : M_TOK.ink500,
          letterSpacing: visible ? "-0.005em" : "0.18em",
          whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis",
        }}>
          {value ? (visible ? value : dots) : "Enter secret phrase"}
        </div>
        <button style={{
          border: 0, background: "transparent", color: M_TOK.ink600,
          cursor: "pointer", padding: 4, display: "flex",
        }}>
          <MEyeIcon off={visible} size={14}/>
        </button>
      </div>
    </div>
  );
}

function PrimarySaveButton({ saving }) {
  return (
    <button disabled={saving} style={{
      background: saving ? M_TOK.amberLo : M_TOK.amber,
      color: "#1a0f02", border: 0,
      padding: `${M_SP(2) + 2}px ${M_SP(4) + 2}px`,
      borderRadius: 4,
      fontFamily: M_FONT.body, fontSize: 13, fontWeight: 500,
      letterSpacing: "-0.005em",
      cursor: saving ? "default" : "pointer",
      display: "inline-flex", alignItems: "center", gap: 8,
    }}>
      {saving && (
        <span style={{
          width: 10, height: 10, borderRadius: "50%",
          border: `1.5px solid #1a0f02`, borderRightColor: "transparent",
          animation: "spin .8s linear infinite",
        }} />
      )}
      {saving ? "Saving…" : "Save Changes"}
    </button>
  );
}

function FooterMicroLegend() {
  return (
    <div style={{
      marginTop: M_SP(5),
      display: "flex", alignItems: "center", gap: M_SP(5),
      color: M_TOK.ink500, fontSize: 11.5,
    }}>
      <span style={{
        fontFamily: M_FONT.mono, fontSize: 10,
        letterSpacing: "0.18em", textTransform: "uppercase",
      }}>
        Stored locally · AES-GCM
      </span>
      <span style={{ color: M_TOK.ink400 }}>·</span>
      <span style={{ fontStyle: "italic", fontFamily: M_FONT.display, fontSize: 12.5, color: M_TOK.ink600 }}>
        Never synced unencrypted, never sent to a server.
      </span>
    </div>
  );
}

Object.assign(window, { OSecretsTab });
