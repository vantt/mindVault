// screen-02-unlock.jsx — Unlock popup state (frequent — seen every browser restart).
// All states for the single-input unlock form: empty / typing / busy / errors / explore.

// A · Spec-faithful — covers all error & busy states
function MUnlockFaithful({ state = "empty" }) {
  // state: empty | typing | busy | error-invalid | error-missing | error-nodata
  const isError = state === "error-invalid" || state === "error-missing" || state === "error-nodata";
  const isBusy = state === "busy";
  const isTyping = state === "typing";

  const headerStatus = isBusy ? "busy" : "locked";

  const errorMessage =
    state === "error-invalid" ? "Invalid password. Try again." :
    state === "error-missing" ? "Missing data. Reset in Settings." :
    state === "error-nodata"  ? "No data found. Reset in Settings." :
    null;

  return (
    <MFrame glow={isError ? "coral" : "amber"}>
      <MHeader status={headerStatus} />
      <MHeaderRule />
      <div style={{
        flex: 1, padding: `${M_SP(6)}px ${M_SP(5)}px ${M_SP(5)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
      }}>
        <MCaption>Unlock</MCaption>
        <div style={{ height: M_SP(2) }} />
        <MDisplay size={22}>
          <span style={{ fontStyle: "italic" }}>Enter</span> your master password.
        </MDisplay>

        <div style={{ height: M_SP(5) }} />

        <MPasswordField
          value={isTyping || isBusy ? "••••••••••" : ""}
          focused={isTyping}
          invalid={isError}
        />

        {errorMessage && (
          <div style={{
            marginTop: M_SP(3),
            display: "flex", alignItems: "center", gap: M_SP(2),
            fontSize: 12.5, color: M_TOK.coral,
          }}>
            <span style={{ fontSize: 14 }}>⚠</span>
            <span>{errorMessage}</span>
          </div>
        )}

        <div style={{ flex: 1 }} />

        <MPrimaryButton hover={isTyping} disabled={isBusy}>
          {isBusy ? "Unlocking…" : "Unlock"}
        </MPrimaryButton>

        <div style={{
          marginTop: M_SP(3),
          display: "flex", justifyContent: "space-between", alignItems: "center",
          fontSize: 12, color: M_TOK.ink500,
        }}>
          <span style={{
            fontFamily: M_FONT.mono, fontSize: 10,
            letterSpacing: "0.15em", textTransform: "uppercase",
          }}>press ⏎ to submit</span>
          <MGhostButton>Forgot? Reset</MGhostButton>
        </div>
      </div>
    </MFrame>
  );
}

function MPasswordField({ value = "", focused = false, invalid = false }) {
  const borderColor = invalid ? M_TOK.coral
                    : focused ? M_TOK.amber
                    : M_TOK.ink300;
  return (
    <div style={{
      display: "flex", alignItems: "center", gap: M_SP(3),
      borderTop: `1px solid ${borderColor}`,
      borderBottom: `1px solid ${borderColor}`,
      padding: `${M_SP(3) + 2}px 0`,
      transition: "border-color 150ms ease",
      position: "relative",
    }}>
      <div style={{
        flex: 1,
        fontFamily: M_FONT.mono, fontSize: 20, fontWeight: 400,
        color: M_TOK.ink900, letterSpacing: "0.15em",
        minHeight: 22, display: "flex", alignItems: "center",
      }}>
        {value || (
          <span style={{
            color: M_TOK.ink500, fontStyle: "italic",
            fontFamily: M_FONT.body, fontSize: 14,
            letterSpacing: 0,
          }}>Master password…</span>
        )}
        {focused && !value && (
          <span style={{
            width: 1.5, height: 18, background: M_TOK.amber,
            marginLeft: 2,
            animation: "mvault-blink 1s steps(2) infinite",
          }} />
        )}
      </div>
      <button style={{
        border: 0, background: "transparent",
        color: M_TOK.ink500, cursor: "pointer",
        padding: 4,
      }} title="Show password">
        <MEyeIcon off />
      </button>
    </div>
  );
}

// B · Explore — quieter; sealed-vault metaphor
function MUnlockExplore() {
  return (
    <MFrame glow="amber">
      <MHeader status="locked" hideStatusText />
      <div style={{
        flex: 1, padding: `${M_SP(7)}px ${M_SP(5)}px ${M_SP(5)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
      }}>
        <div style={{
          display: "flex", alignItems: "center", gap: M_SP(2),
          color: M_TOK.ink500,
        }}>
          <MLockIcon size={14} />
          <span style={{
            fontFamily: M_FONT.mono, fontSize: 10,
            letterSpacing: "0.22em", textTransform: "uppercase",
          }}>Locked since 14:02 yesterday</span>
        </div>

        <div style={{ height: M_SP(4) }} />

        <MDisplay size={34} italic style={{ lineHeight: 1.1 }}>
          Welcome<br/>back.
        </MDisplay>

        <div style={{ height: M_SP(2) }} />
        <MBody muted style={{ fontSize: 14 }}>
          Your vault is sealed until you remember it.
        </MBody>

        <div style={{ flex: 1 }} />

        <div style={{
          display: "flex", alignItems: "stretch",
          borderTop: `1px solid ${M_TOK.ink300}`,
          borderBottom: `1px solid ${M_TOK.ink300}`,
        }}>
          <div style={{
            flex: 1,
            padding: `${M_SP(3)}px 0`,
            fontFamily: M_FONT.mono, fontSize: 18,
            color: M_TOK.ink900, letterSpacing: "0.2em",
          }}>
            ••••••••••
          </div>
          <button style={{
            border: 0, background: "transparent",
            color: M_TOK.amber, cursor: "pointer",
            fontFamily: M_FONT.body, fontSize: 13, fontWeight: 500,
            padding: `0 ${M_SP(3)}px`,
            display: "flex", alignItems: "center", gap: 6,
          }}>
            Unlock <span style={{ fontSize: 16 }}>→</span>
          </button>
        </div>

        <div style={{
          marginTop: M_SP(2),
          fontFamily: M_FONT.mono, fontSize: 10,
          letterSpacing: "0.2em", textTransform: "uppercase",
          color: M_TOK.ink500,
        }}>
          Argon2id · 64 MB · ~0.8s
        </div>
      </div>
    </MFrame>
  );
}

// inject keyframes once (cursor blink)
if (typeof document !== 'undefined' && !document.getElementById('mvault-anim')) {
  const s = document.createElement('style');
  s.id = 'mvault-anim';
  s.textContent = `@keyframes mvault-blink { 50% { opacity: 0; } }`;
  document.head.appendChild(s);
}

Object.assign(window, {
  MUnlockFaithful, MUnlockExplore, MPasswordField,
});
