// screen-modal-new-profile.jsx — Per spec 12-new-profile-modal.md
//
// States:
//   default — empty input
//   typing  — name typed, ready to create
//   error   — name empty (silent in code but I show inline note for clarity)

function OModalNewProfile({ state = "typing" }) {
  const value = state === "typing" ? "TeamShare" : "";
  return (
    <OPage height="auto" glow="amber">
      <OAppBar profile/>
      <OTabBar active="profiles"/>
      <DimmedBg/>

      <OModalShell
        width={480}
        title={<><span style={{ color: M_TOK.ink600 }}>Create </span><span style={{ fontStyle: "italic" }}>new profile</span></>}
        sub="A profile is an independent set of 5 secrets. After creation, the Edit Secrets dialog opens automatically."
        footer={
          <>
            <OBtn kind="ghost">Cancel</OBtn>
            <button style={{
              background: value ? M_TOK.amber : M_TOK.ink300,
              color: value ? "#1a0f02" : M_TOK.ink500,
              border: 0, borderRadius: 4,
              padding: `${M_SP(2) + 2}px ${M_SP(4)}px`,
              fontFamily: M_FONT.body, fontSize: 13, fontWeight: 500,
              cursor: value ? "pointer" : "default",
              display: "inline-flex", alignItems: "center", gap: 6,
            }}>
              Create profile <span>→</span>
            </button>
          </>
        }
      >
        <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
          <OLabel>Profile name</OLabel>
          <OInput value={value} placeholder='e.g. "Banking", "TeamShare"'/>
          <OFieldNote tone="muted">
            Max 50 chars. Used as the storage key <code style={{ fontFamily: M_FONT.mono, fontSize: 11, color: M_TOK.ink700 }}>profile:{value || "Name"}</code>.
            Creating with an existing name overwrites it.
          </OFieldNote>
        </div>

        <div style={{
          marginTop: M_SP(5),
          background: M_TOK.ink050,
          border: `1px dashed ${M_TOK.ink300}`,
          borderRadius: 4,
          padding: M_SP(3),
          fontSize: 11.5, color: M_TOK.ink600, lineHeight: 1.5,
        }}>
          <div style={{
            fontFamily: M_FONT.mono, fontSize: 9.5, letterSpacing: "0.18em",
            textTransform: "uppercase", color: M_TOK.amber, marginBottom: 6,
          }}>Next step</div>
          The Edit Secrets dialog will open so you can enter five private phrases. You can change them any time from the profile card.
        </div>
      </OModalShell>
    </OPage>
  );
}

// ── shared backdrop hint (Profiles tab shows faintly under modal) ────────

function DimmedBg() {
  return (
    <OContainer width={760} style={{ padding: `${M_SP(6)}px ${M_SP(5)}px ${M_SP(7)}px`, opacity: .35, filter: "blur(.5px)" }}>
      <div style={{
        fontFamily: M_FONT.display, fontSize: 28, fontStyle: "italic",
        letterSpacing: "-0.025em", color: M_TOK.ink900,
      }}>Profiles & Sheet routing</div>
      <div style={{ height: M_SP(5) }}/>
      <div style={{
        height: 96, background: M_TOK.ink100,
        border: `1px solid ${M_TOK.ink200}`,
        borderLeft: `3px solid #58a6ff`,
        borderRadius: 6,
      }}/>
      <div style={{ height: M_SP(3) }}/>
      <div style={{
        height: 96, background: M_TOK.ink100,
        border: `1px solid ${M_TOK.ink200}`,
        borderLeft: `3px solid #e3b341`,
        borderRadius: 6,
      }}/>
    </OContainer>
  );
}

Object.assign(window, { OModalNewProfile, DimmedBg });
