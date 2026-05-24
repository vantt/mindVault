// screen-01-setup.jsx — First Setup popup state.
// Triggered when `salt` is missing from chrome.storage.sync.
// Popup is a CTA only — actual master-password form lives on Options page.

// A · Spec-faithful: minimal, single CTA → opens Options page
function MSetupFaithful() {
  return (
    <MFrame glow="amber">
      <MHeader status="setup" customLabel="Setup required" />
      <MHeaderRule />
      <div style={{
        flex: 1, padding: `${M_SP(6)}px ${M_SP(5)}px ${M_SP(5)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
      }}>
        <MCaption>Welcome</MCaption>
        <div style={{ height: M_SP(3) }} />
        <MDisplay size={24} italic>
          Set a master password<br/>
          <span style={{ fontStyle: "normal" }}>to unlock everything else.</span>
        </MDisplay>
        <div style={{ height: M_SP(4) }} />
        <MBody muted>
          mindVault encrypts your secrets with this single password.
          It never leaves your machine. There is no recovery — back it up.
        </MBody>

        <div style={{ flex: 1 }} />

        <MPrimaryButton>Start setup</MPrimaryButton>
        <div style={{
          marginTop: M_SP(3),
          display: "flex", alignItems: "center", justifyContent: "center", gap: 6,
          fontFamily: M_FONT.mono, fontSize: 10,
          letterSpacing: "0.15em", textTransform: "uppercase",
          color: M_TOK.ink500,
        }}>
          <span>opens settings tab</span>
          <span>↗</span>
        </div>
      </div>
    </MFrame>
  );
}

// B · Explore — progressive disclosure of what's inside the vault
function MSetupExplore() {
  return (
    <MFrame glow="amber">
      <MHeader status="setup" customLabel="Setup required" />
      <MHeaderRule />
      <div style={{
        flex: 1, padding: `${M_SP(5)}px ${M_SP(5)}px ${M_SP(5)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
      }}>
        <MDisplay size={26} italic>
          An empty vault,<br/>
          <span style={{ fontStyle: "normal", color: M_TOK.ink700 }}>waiting for one key.</span>
        </MDisplay>

        <div style={{ height: M_SP(5) }} />

        <div style={{
          border: `1px solid ${M_TOK.ink200}`,
          borderRadius: 4,
          padding: M_SP(4),
          background: M_TOK.ink100 + "80",
        }}>
          <MCaption>You'll create</MCaption>
          <div style={{ height: M_SP(3) }} />
          <SetupStep n="01" label="A master password" detail="Argon2id, 64 MB" />
          <SetupStep n="02" label="A default profile" detail='Named "Default"' />
          <SetupStep n="03" label="Manual backup confirmation" detail="No recovery flow" last />
        </div>

        <div style={{ flex: 1 }} />

        <MPrimaryButton>Open setup</MPrimaryButton>
        <MGhostButton style={{ marginTop: M_SP(3), alignSelf: "center" }}>
          Why no recovery?
        </MGhostButton>
      </div>
    </MFrame>
  );
}

function SetupStep({ n, label, detail, last }) {
  return (
    <div style={{
      display: "flex", alignItems: "baseline", gap: M_SP(3),
      padding: `${M_SP(2)}px 0`,
      borderBottom: last ? "none" : `1px solid ${M_TOK.ink200}`,
    }}>
      <span style={{
        fontFamily: M_FONT.mono, fontSize: 10,
        color: M_TOK.ink500, letterSpacing: "0.1em",
      }}>{n}</span>
      <span style={{
        fontSize: 13, color: M_TOK.ink900, flex: 1,
      }}>{label}</span>
      <span style={{
        fontFamily: M_FONT.mono, fontSize: 10,
        color: M_TOK.ink500,
      }}>{detail}</span>
    </div>
  );
}

Object.assign(window, {
  MSetupFaithful, MSetupExplore,
});
