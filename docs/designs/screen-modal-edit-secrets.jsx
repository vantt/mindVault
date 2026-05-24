// screen-modal-edit-secrets.jsx — Per spec 11-edit-secrets-modal.md
//
// 5 secret slots · password input + eye toggle · Save / Cancel.
//
// States:
//   first-time — all 5 empty (after profile creation)
//   editing    — some pre-filled, one revealed
//   saved      — toast visible

function OModalEditSecrets({ state = "editing" }) {
  const isFirstTime = state === "first-time";
  const isSaved     = state === "saved";

  const secrets = isFirstTime
    ? [{i:1, v:""}, {i:2, v:""}, {i:3, v:""}, {i:4, v:""}, {i:5, v:""}]
    : [
        { i: 1, v: "midnight harbour fern" },
        { i: 2, v: "salt copper afternoon", reveal: true },
        { i: 3, v: "owls quiet glassworks" },
        { i: 4, v: "" },
        { i: 5, v: "" },
      ];

  const profileName = isFirstTime ? "TeamShare" : "Banking";

  return (
    <OPage height="auto">
      <OAppBar profile/>
      <OTabBar active="profiles"/>
      <DimmedBg/>

      <OModalShell
        width={520}
        title={
          <>
            <span style={{ color: M_TOK.ink600 }}>Edit secrets · </span>
            <span style={{ fontStyle: "italic" }}>{profileName}</span>
          </>
        }
        sub={
          isFirstTime
            ? "First time — these are the five phrases that build every password in this profile. Choose phrases you'll remember without writing them down."
            : "Changes apply instantly. Existing recipes will derive different passwords if you change a secret they reference."
        }
        footer={
          <>
            <OBtn kind="ghost">Cancel</OBtn>
            <div style={{ display: "inline-flex", alignItems: "center", gap: M_SP(3) }}>
              <span style={{
                fontFamily: M_FONT.mono, fontSize: 10,
                letterSpacing: "0.16em", color: M_TOK.ink500,
                textTransform: "uppercase",
              }}>AES-GCM · local</span>
              <button style={{
                background: M_TOK.amber, color: "#1a0f02",
                border: 0, borderRadius: 4,
                padding: `${M_SP(2) + 2}px ${M_SP(4)}px`,
                fontFamily: M_FONT.body, fontSize: 13, fontWeight: 500,
                cursor: "pointer",
                display: "inline-flex", alignItems: "center", gap: 6,
              }}>
                Save changes <span>→</span>
              </button>
            </div>
          </>
        }
      >
        <div style={{ display: "flex", flexDirection: "column", gap: M_SP(3) }}>
          {secrets.map(s => (
            <EditSecretRow key={s.i} index={s.i} value={s.v} reveal={s.reveal}/>
          ))}
        </div>

        <div style={{
          marginTop: M_SP(4),
          padding: `${M_SP(2) + 2}px ${M_SP(3)}px`,
          background: M_TOK.ink050,
          borderLeft: `2px solid ${M_TOK.amber}88`,
          fontSize: 11.5, color: M_TOK.ink700, lineHeight: 1.5,
          fontStyle: "italic", fontFamily: M_FONT.display, fontSize: 13,
        }}>
          Empty slots are fine — recipes only reference the indexes they use. But once a recipe relies on Secret&nbsp;#N, never reorder.
        </div>
      </OModalShell>

      {isSaved && (
        <OToast tone="moss">
          <MCheckTiny size={12}/> Secrets saved
        </OToast>
      )}
    </OPage>
  );
}

function EditSecretRow({ index, value, reveal }) {
  const dots = "•".repeat(value.length);
  return (
    <div style={{
      display: "grid", gridTemplateColumns: "92px 1fr",
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
          fontFamily: reveal ? M_FONT.body : M_FONT.mono,
          fontStyle: reveal ? "italic" : "normal",
          fontSize: 13,
          color: value ? M_TOK.ink900 : M_TOK.ink500,
          letterSpacing: reveal ? "-0.005em" : "0.18em",
          whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis",
        }}>
          {value ? (reveal ? value : dots) : "Enter secret phrase"}
        </div>
        <button style={{
          border: 0, background: "transparent", color: M_TOK.ink600,
          cursor: "pointer", padding: 4, display: "flex",
        }}>
          <MEyeIcon off={reveal} size={14}/>
        </button>
      </div>
    </div>
  );
}

Object.assign(window, { OModalEditSecrets });
