// precision-refined.jsx — Refined Precision design system for PassMan.
// Self-contained: all components, tokens, and helpers under the P_ namespace
// to avoid collision with variations.jsx if both are loaded together.

// ──────────────────────────────────────────────────────────────────────────
// TOKENS
// ──────────────────────────────────────────────────────────────────────────

const P_TOK = {
  // ink ramp — warm desaturated grays, paper-leaning highlights
  ink000: "#0a0a0c",
  ink050: "#0e0e10",   // page base
  ink100: "#15151a",   // surface
  ink150: "#1c1c22",   // raised
  ink200: "#232329",   // hairline strong
  ink300: "#2f2f36",   // border strong
  ink400: "#44413c",   // disabled
  ink500: "#6a655d",   // tertiary
  ink600: "#8a857a",   // secondary
  ink700: "#b4ad9f",
  ink800: "#d8d2c3",
  ink900: "#f0ece2",   // display, warm off-white

  amber:    "#e8a341",
  amberHi:  "#f5b35a",
  amberLo:  "#b97d23",
  amberBg:  "#3a2a14",

  moss:     "#84b577",
  mossBg:   "#1d2818",
  coral:    "#e0746c",
  coralBg:  "#2b1815",
  honey:    "#d4a548",
};

const P_FONT = {
  display: "'Fraunces', serif",
  body:    "'Geist', system-ui, sans-serif",
  mono:    "'Geist Mono', ui-monospace, monospace",
};

const P_SP = (n) => [0, 4, 8, 12, 16, 24, 36, 56, 80][n];

// shared sample data
const P_PWD = "f7nQp2!vXm9kR$8B";
const P_SHEET = "1BxCdefGh...aWxYz";

// ──────────────────────────────────────────────────────────────────────────
// PRIMITIVES
// ──────────────────────────────────────────────────────────────────────────

function PFrame({ children, bg = P_TOK.ink050 }) {
  return (
    <div style={{
      width: "100%", height: "100%",
      background: bg, color: P_TOK.ink900,
      fontFamily: P_FONT.body,
      display: "flex", flexDirection: "column",
      position: "relative", overflow: "hidden",
    }}>
      {/* atmospheric vignette */}
      <div style={{
        position: "absolute", inset: 0, pointerEvents: "none",
        background: "radial-gradient(ellipse at 70% -10%, rgba(232,163,65,0.04), transparent 60%)",
      }} />
      {children}
    </div>
  );
}

function PHeader({ status = "ready", note = null }) {
  const map = {
    ready:    { dot: P_TOK.moss,   label: "Ready",            text: P_TOK.ink900 },
    blocked:  { dot: P_TOK.coral,  label: "Cannot generate",  text: P_TOK.coral },
    warn:     { dot: P_TOK.amber,  label: "Needs attention",  text: P_TOK.amber },
    locked:   { dot: P_TOK.ink400, label: "Locked",           text: P_TOK.ink700 },
  };
  const s = map[status];
  return (
    <div style={{
      display: "flex", alignItems: "center", justifyContent: "space-between",
      padding: `${P_SP(5)}px ${P_SP(6)}px 0`,
      position: "relative", zIndex: 1,
    }}>
      <PWordmark />
      <div style={{ display: "flex", alignItems: "center", gap: P_SP(5) }}>
        {note && <span style={{
          fontFamily: P_FONT.mono, fontSize: 10.5,
          letterSpacing: "0.18em", textTransform: "uppercase",
          color: P_TOK.ink500,
        }}>{note}</span>}
        <div style={{ display: "flex", alignItems: "center", gap: P_SP(2) }}>
          <span style={{
            width: 7, height: 7, borderRadius: "50%",
            background: s.dot,
            boxShadow: `0 0 8px ${s.dot}66`,
          }} />
          <span style={{
            color: s.text, fontSize: 12.5,
            letterSpacing: "0.01em",
          }}>{s.label}</span>
        </div>
        <PIconButton title="Settings">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
            <circle cx="8" cy="8" r="2" stroke="currentColor" strokeWidth="1.3"/>
            <circle cx="8" cy="8" r="6" stroke="currentColor" strokeWidth="1.3"/>
            <path d="M8 1v2M8 13v2M1 8h2M13 8h2" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>
          </svg>
        </PIconButton>
      </div>
    </div>
  );
}

function PWordmark({ size = 26 }) {
  return (
    <div style={{
      fontFamily: P_FONT.display, fontSize: size, fontWeight: 400,
      letterSpacing: "-0.02em",
      lineHeight: 1, display: "flex", alignItems: "baseline", gap: 6,
    }}>
      <span style={{ color: P_TOK.ink600 }}>mind</span>
      <span style={{
        color: P_TOK.ink900,
        fontStyle: "italic", letterSpacing: "-0.025em",
      }}>Vault</span>
      <span style={{
        marginLeft: 8, width: 4, height: 4, borderRadius: "50%",
        background: P_TOK.amber, alignSelf: "center",
        boxShadow: `0 0 6px ${P_TOK.amber}aa`,
      }} />
    </div>
  );
}

function PIconButton({ children, title }) {
  return (
    <button title={title} style={{
      width: 28, height: 28, border: 0, background: "transparent",
      color: P_TOK.ink500, cursor: "pointer",
      display: "flex", alignItems: "center", justifyContent: "center",
      borderRadius: 4,
    }}>{children}</button>
  );
}

function PCaption({ children, accent = false, style = {} }) {
  return (
    <div style={{
      fontFamily: P_FONT.mono, fontSize: 10.5, fontWeight: 500,
      letterSpacing: "0.22em", textTransform: "uppercase",
      color: accent ? P_TOK.amber : P_TOK.ink500,
      ...style,
    }}>{children}</div>
  );
}

function PDivider({ kind = "solid", style = {} }) {
  return <div style={{
    height: 1,
    background: kind === "dashed" ? "none" : P_TOK.ink200,
    borderTop: kind === "dashed" ? `1px dashed ${P_TOK.ink300}` : "none",
    ...style,
  }} />;
}

function PChip({ children, tone = "ink", style = {} }) {
  const palette = {
    ink:    { fg: P_TOK.ink700, bg: "transparent",    bd: P_TOK.ink300 },
    amber:  { fg: P_TOK.amber,  bg: P_TOK.amberBg + "80", bd: P_TOK.amber + "55" },
    moss:   { fg: P_TOK.moss,   bg: P_TOK.mossBg + "80",  bd: P_TOK.moss + "55" },
    coral:  { fg: P_TOK.coral,  bg: P_TOK.coralBg + "80", bd: P_TOK.coral + "55" },
  }[tone];
  return (
    <span style={{
      display: "inline-flex", alignItems: "center", gap: 6,
      padding: "3px 8px",
      fontFamily: P_FONT.mono, fontSize: 10, fontWeight: 500,
      letterSpacing: "0.14em", textTransform: "uppercase",
      color: palette.fg, background: palette.bg,
      border: `1px solid ${palette.bd}`, borderRadius: 3,
      ...style,
    }}>{children}</span>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// GENERATED SCREEN — full refined design with 8 states
// ──────────────────────────────────────────────────────────────────────────

function PrecisionGen({ state = "default" }) {
  // states: default | pepper | shared | copied | empty | reload | mismatch | hover
  const isError = state === "empty" || state === "reload" || state === "mismatch";
  const isShared = state === "shared";
  const showPepper = state === "pepper" || state === "shared";
  const isCopied = state === "copied";
  const isHover  = state === "hover";

  const headerStatus = state === "mismatch" ? "warn"
                     : isError ? "blocked"
                     : "ready";

  const profileName = isShared ? "TeamFromB" : "Default";

  return (
    <PFrame>
      <PHeader status={headerStatus} />

      {/* hairline below header */}
      <div style={{
        height: 1, background: P_TOK.ink200,
        margin: `${P_SP(5)}px ${P_SP(6)}px 0`,
      }} />

      {/* body */}
      <div style={{
        padding: `${P_SP(5)}px ${P_SP(6)}px 0`,
        flex: 1, display: "flex", flexDirection: "column",
        position: "relative", zIndex: 1,
      }}>

        {/* PROFILE BLOCK */}
        <div style={{ display: "flex", alignItems: "flex-start", justifyContent: "space-between", gap: P_SP(4) }}>
          <div>
            <PCaption>
              Profile
              {isShared && <span style={{ marginLeft: 8, color: P_TOK.amber }}>· received</span>}
            </PCaption>
            <div style={{
              marginTop: P_SP(2),
              display: "flex", alignItems: "baseline", gap: P_SP(3),
            }}>
              <span style={{
                fontSize: 20, fontWeight: 400,
                letterSpacing: "-0.015em",
                color: P_TOK.ink900,
              }}>{profileName}</span>
              {isShared && (
                <span style={{
                  fontFamily: P_FONT.mono, fontSize: 11,
                  color: P_TOK.ink600,
                }}>from User B</span>
              )}
            </div>
          </div>

          {isShared && (
            <div style={{ textAlign: "right" }}>
              <PCaption style={{ fontSize: 9.5 }}>Bound to sheet</PCaption>
              <div style={{
                marginTop: P_SP(2),
                fontFamily: P_FONT.mono, fontSize: 11.5,
                color: P_TOK.ink700, letterSpacing: "0.04em",
              }}>{P_SHEET}</div>
            </div>
          )}
        </div>

        <div style={{ height: P_SP(5) }} />
        <PDivider />
        <div style={{ height: P_SP(5) }} />

        {/* PASSWORD BLOCK */}
        <div>
          <div style={{ display: "flex", alignItems: "baseline", justifyContent: "space-between", marginBottom: P_SP(3) }}>
            <PCaption>
              {state === "empty"    ? "No recipe"
               : state === "reload" ? "Connection lost"
               : state === "mismatch" ? "Recipe rejected"
               : "Password"}
            </PCaption>
            {!isError && (
              <div style={{
                display: "flex", alignItems: "center", gap: P_SP(3),
                fontFamily: P_FONT.mono, fontSize: 10.5,
                letterSpacing: "0.14em", textTransform: "uppercase",
                color: P_TOK.ink500,
              }}>
                <span>16 chars</span>
                <span style={{ color: P_TOK.ink300 }}>·</span>
                <span>~96 bits</span>
                <span style={{ color: P_TOK.ink300 }}>·</span>
                <span style={{ color: P_TOK.moss, display: "flex", alignItems: "center", gap: 4 }}>
                  <PCheckTiny color={P_TOK.moss}/> verified
                </span>
              </div>
            )}
          </div>

          {/* the password slab */}
          {isError
            ? <PErrorSlab state={state} />
            : <PPasswordSlab copied={isCopied} />
          }

          {/* hint row */}
          {showPepper && !isError && (
            <div style={{
              marginTop: P_SP(4),
              display: "flex", alignItems: "center", gap: P_SP(3),
            }}>
              <span style={{
                width: 6, height: 6, borderRadius: "50%",
                background: P_TOK.honey,
              }} />
              <span style={{
                fontFamily: P_FONT.display, fontStyle: "italic",
                fontSize: 15, fontWeight: 400,
                color: P_TOK.ink800, letterSpacing: "-0.005em",
              }}>
                Don't forget your pepper.
              </span>
              <span style={{
                marginLeft: "auto",
                fontFamily: P_FONT.mono, fontSize: 10,
                letterSpacing: "0.2em", textTransform: "uppercase",
                color: P_TOK.ink500,
              }}>optional suffix</span>
            </div>
          )}
        </div>

        <div style={{ flex: 1, minHeight: P_SP(5) }} />

        {/* PRIMARY ACTION */}
        <button style={{
          display: "flex", alignItems: "center", justifyContent: "space-between",
          width: "100%", padding: `${P_SP(4)}px ${P_SP(5)}px`,
          background: isHover ? P_TOK.amberHi : P_TOK.amber,
          color: "#1a0f02", border: 0,
          fontFamily: P_FONT.body, fontSize: 15, fontWeight: 500,
          cursor: "pointer", borderRadius: 4,
          letterSpacing: "-0.005em",
          boxShadow: isHover
            ? `0 8px 24px ${P_TOK.amber}33, 0 0 0 1px ${P_TOK.amberHi}`
            : "none",
          transition: "all 120ms ease",
        }}>
          <span style={{ display: "flex", alignItems: "center", gap: P_SP(2) }}>
            <span style={{ fontWeight: 500 }}>Build recipe</span>
            <span style={{
              fontFamily: P_FONT.mono, fontSize: 11,
              opacity: 0.65, letterSpacing: "0.04em",
            }}>compose with secrets</span>
          </span>
          <span style={{ fontSize: 18, fontWeight: 400 }}>→</span>
        </button>

        {/* footer */}
        <div style={{
          display: "flex", justifyContent: "space-between", alignItems: "center",
          padding: `${P_SP(4)}px 0 ${P_SP(5)}px`,
          fontSize: 13, color: P_TOK.ink600,
        }}>
          <button style={pGhostBtn()}>
            <span style={{ marginRight: 8 }}>←</span>Back
          </button>
          <button style={pGhostBtn()}>
            Lock session<span style={{ marginLeft: 10, opacity: 0.6 }}>⌘L</span>
          </button>
        </div>
      </div>
    </PFrame>
  );
}

function PPasswordSlab({ copied }) {
  return (
    <div style={{
      display: "flex", alignItems: "stretch", gap: 0,
      borderTop: `1px solid ${copied ? P_TOK.amber : P_TOK.ink300}`,
      borderBottom: `1px solid ${copied ? P_TOK.amber : P_TOK.ink300}`,
      transition: "border-color 200ms ease",
      position: "relative",
    }}>
      <div style={{
        padding: `${P_SP(4)}px 0`,
        flex: 1,
        fontFamily: P_FONT.mono,
        fontSize: 32, fontWeight: 400,
        letterSpacing: "0.005em",
        color: P_TOK.ink900,
        lineHeight: 1, display: "flex", alignItems: "center",
      }}>
        {P_PWD}
      </div>

      <button style={{
        border: 0, background: "transparent",
        color: copied ? P_TOK.amber : P_TOK.ink800,
        padding: `0 ${P_SP(4)}px`,
        fontFamily: P_FONT.body, fontSize: 13, fontWeight: 500,
        letterSpacing: "0.02em",
        cursor: "pointer",
        display: "flex", alignItems: "center", gap: 8,
      }}>
        {copied ? <PCheckTiny color={P_TOK.amber}/> : <PCopyIcon/>}
        <span>{copied ? "Copied" : "Copy"}</span>
        {copied && (
          <span style={{
            fontFamily: P_FONT.mono, fontSize: 10,
            color: P_TOK.amberLo, marginLeft: 4,
          }}>1.5s</span>
        )}
      </button>

      {copied && (
        <span style={{
          position: "absolute", inset: 0, pointerEvents: "none",
          background: `linear-gradient(90deg, transparent 0%, ${P_TOK.amber}10 50%, transparent 100%)`,
        }} />
      )}
    </div>
  );
}

function PErrorSlab({ state }) {
  const cfg = {
    empty: {
      headline: "Select a cell with a recipe.",
      body: "No recipe was found in the cell you had selected on this sheet.",
      tone: "coral",
      action: { label: "Refresh popup", kbd: "⏎" },
    },
    reload: {
      headline: "Reload the tab to reconnect.",
      body: "The content script lost its handle. Reloading restores the extension's access to this sheet.",
      tone: "coral",
      action: { label: "Reload tab", kbd: "⌘R" },
    },
    mismatch: {
      headline: "This recipe is bound to another sheet.",
      body: "The verification tag in the recipe matches a different sheet or profile than the one you're on.",
      tone: "amber",
      action: { label: "Switch sheet", kbd: null },
    },
  }[state];

  const accent = cfg.tone === "amber" ? P_TOK.amber : P_TOK.coral;
  const accentBg = cfg.tone === "amber" ? P_TOK.amberBg : P_TOK.coralBg;

  return (
    <div style={{
      display: "flex", flexDirection: "column", gap: P_SP(3),
      padding: `${P_SP(4)}px ${P_SP(4)}px`,
      borderTop: `1px solid ${accent}`,
      borderBottom: `1px solid ${P_TOK.ink300}`,
      background: `linear-gradient(180deg, ${accentBg}66 0%, transparent 100%)`,
    }}>
      <div style={{
        fontFamily: P_FONT.display, fontStyle: "italic",
        fontSize: 22, fontWeight: 400,
        color: P_TOK.ink900, letterSpacing: "-0.015em",
        lineHeight: 1.15,
      }}>{cfg.headline}</div>
      <div style={{
        fontSize: 13.5, color: P_TOK.ink700,
        lineHeight: 1.5, maxWidth: 480,
      }}>{cfg.body}</div>
      <div style={{
        display: "flex", alignItems: "center", gap: P_SP(3), marginTop: P_SP(1),
      }}>
        <PChip tone={cfg.tone}>
          {cfg.action.label}
        </PChip>
        {cfg.action.kbd && (
          <span style={{
            fontFamily: P_FONT.mono, fontSize: 10.5,
            color: P_TOK.ink500, letterSpacing: "0.08em",
          }}>press {cfg.action.kbd}</span>
        )}
      </div>
    </div>
  );
}

function pGhostBtn() {
  return {
    border: 0, background: "transparent",
    color: P_TOK.ink600, cursor: "pointer",
    fontFamily: P_FONT.body, fontSize: 13, fontWeight: 400,
    padding: "4px 0", letterSpacing: "-0.005em",
  };
}

function PCheckTiny({ color }) {
  return (
    <svg width="11" height="11" viewBox="0 0 12 12" fill="none">
      <path d="M2 6.5L4.8 9.2L10 3.5" stroke={color} strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
  );
}

function PCopyIcon() {
  return (
    <svg width="13" height="13" viewBox="0 0 14 14" fill="none">
      <rect x="3.5" y="3.5" width="7" height="7" rx="1" stroke="currentColor" strokeWidth="1.3"/>
      <path d="M2 9V2.5C2 2.22 2.22 2 2.5 2H9" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>
    </svg>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// HOME SCREEN — sister screen using same system
// ──────────────────────────────────────────────────────────────────────────

function PrecisionHome() {
  return (
    <PFrame>
      <PHeader status="ready" note="not on sheets" />
      <div style={{
        height: 1, background: P_TOK.ink200,
        margin: `${P_SP(5)}px ${P_SP(6)}px 0`,
      }} />

      <div style={{
        padding: `${P_SP(6)}px ${P_SP(6)}px 0`,
        flex: 1, display: "flex", flexDirection: "column",
        position: "relative", zIndex: 1,
      }}>
        <div style={{
          fontFamily: P_FONT.display, fontSize: 30, fontWeight: 400,
          letterSpacing: "-0.02em", lineHeight: 1.15,
          color: P_TOK.ink900,
        }}>
          Open a sheet, then <span style={{ fontStyle: "italic" }}>click any cell</span><br/>
          containing a recipe.
        </div>
        <div style={{
          marginTop: P_SP(3),
          fontSize: 14, color: P_TOK.ink600, lineHeight: 1.5,
          maxWidth: 460,
        }}>
          Or stay here and compose a new recipe with the secrets in your vault.
        </div>

        <div style={{ height: P_SP(6) }} />
        <PDivider />
        <div style={{ height: P_SP(5) }} />

        <PCaption>Vault inventory</PCaption>
        <div style={{
          marginTop: P_SP(3),
          display: "flex", gap: P_SP(5),
        }}>
          <PStat label="Own profiles" value="3" />
          <PStat label="Shared profiles" value="1" />
          <PStat label="Sheets bound" value="4" />
        </div>

        <div style={{ flex: 1 }} />

        <button style={{
          display: "flex", alignItems: "center", justifyContent: "space-between",
          width: "100%", padding: `${P_SP(4)}px ${P_SP(5)}px`,
          background: P_TOK.amber, color: "#1a0f02", border: 0,
          fontFamily: P_FONT.body, fontSize: 15, fontWeight: 500,
          cursor: "pointer", borderRadius: 4,
        }}>
          <span>Build recipe</span>
          <span style={{ fontSize: 18 }}>→</span>
        </button>

        <div style={{
          display: "flex", justifyContent: "space-between", alignItems: "center",
          padding: `${P_SP(4)}px 0 ${P_SP(5)}px`,
          fontSize: 13, color: P_TOK.ink600,
        }}>
          <button style={pGhostBtn()}>Open vault settings</button>
          <button style={pGhostBtn()}>Lock session<span style={{ marginLeft: 10, opacity: 0.6 }}>⌘L</span></button>
        </div>
      </div>
    </PFrame>
  );
}

function PStat({ label, value }) {
  return (
    <div>
      <div style={{
        fontFamily: P_FONT.display, fontSize: 32, fontWeight: 400,
        color: P_TOK.ink900, lineHeight: 1, letterSpacing: "-0.02em",
      }}>{value}</div>
      <div style={{
        marginTop: 6,
        fontFamily: P_FONT.mono, fontSize: 10.5, fontWeight: 500,
        letterSpacing: "0.18em", textTransform: "uppercase",
        color: P_TOK.ink500,
      }}>{label}</div>
    </div>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// DESIGN SYSTEM DOC CARDS
// ──────────────────────────────────────────────────────────────────────────

function PrecisionTypeDoc() {
  return (
    <DocFrame title="Typography">
      <TypeRow font={P_FONT.display} size={44} weight={400} italic
               label="Display · Fraunces 44/400 italic"
               sample="PassChef" />
      <TypeRow font={P_FONT.display} size={28} weight={400}
               label="Title · Fraunces 28/400"
               sample="Open a sheet, then click any cell." />
      <TypeRow font={P_FONT.mono} size={32} weight={400}
               label="Display Mono · Geist Mono 32/400"
               sample="f7nQp2!vXm9kR$8B" />
      <TypeRow font={P_FONT.body} size={20} weight={400}
               label="Name · Geist 20/400"
               sample="TeamFromB" />
      <TypeRow font={P_FONT.body} size={14} weight={400}
               label="Body · Geist 14/400"
               sample="Don't forget your pepper. Sheet binding is verified." />
      <TypeRow font={P_FONT.mono} size={10.5} weight={500}
               label="Caption · Geist Mono 10.5/500 · letter-spacing 0.22em · uppercase"
               sample="GENERATED · 16 CHARS · ~96 BITS · VERIFIED"
               caps />
      <TypeRow font={P_FONT.display} size={15} weight={400} italic
               label="Voice · Fraunces 15/400 italic — for human-toned hints"
               sample="Don't forget your pepper." />
    </DocFrame>
  );
}

function TypeRow({ font, size, weight, label, sample, italic, caps }) {
  return (
    <div style={{
      display: "flex", flexDirection: "column", gap: 8,
      padding: `${P_SP(3)}px 0`,
      borderBottom: `1px solid ${P_TOK.ink200}`,
    }}>
      <div style={{
        fontFamily: P_FONT.mono, fontSize: 10.5, fontWeight: 500,
        letterSpacing: "0.18em", textTransform: "uppercase",
        color: P_TOK.ink500,
      }}>{label}</div>
      <div style={{
        fontFamily: font, fontSize: size, fontWeight: weight,
        fontStyle: italic ? "italic" : "normal",
        color: P_TOK.ink900,
        letterSpacing: caps ? "0.22em" : (size > 24 ? "-0.02em" : "-0.005em"),
        textTransform: caps ? "uppercase" : "none",
        lineHeight: 1.15,
      }}>{sample}</div>
    </div>
  );
}

function PrecisionColorDoc() {
  const inkRamp = [
    ["ink/000", P_TOK.ink000], ["ink/050", P_TOK.ink050],
    ["ink/100", P_TOK.ink100], ["ink/150", P_TOK.ink150],
    ["ink/200", P_TOK.ink200], ["ink/300", P_TOK.ink300],
    ["ink/400", P_TOK.ink400], ["ink/500", P_TOK.ink500],
    ["ink/600", P_TOK.ink600], ["ink/700", P_TOK.ink700],
    ["ink/800", P_TOK.ink800], ["ink/900", P_TOK.ink900],
  ];
  const accents = [
    ["amber/500", P_TOK.amber, "Primary action · verification ·\u00a0\u00a0pepper hint"],
    ["amber/Hi",  P_TOK.amberHi, "Hover"],
    ["amber/Lo",  P_TOK.amberLo, "Pressed"],
    ["moss/500",  P_TOK.moss,  "Success · Ready · Verified"],
    ["coral/500", P_TOK.coral, "Blocking error"],
    ["honey/500", P_TOK.honey, "Warning · Pepper dot"],
  ];

  return (
    <DocFrame title="Color">
      <div style={{ marginTop: P_SP(2) }}>
        <PCaption>Ink ramp · 12 steps</PCaption>
        <div style={{
          marginTop: P_SP(3),
          display: "grid", gridTemplateColumns: "repeat(12, 1fr)",
          gap: 1, height: 64,
          border: `1px solid ${P_TOK.ink200}`,
        }}>
          {inkRamp.map(([name, hex]) => (
            <div key={name} style={{
              background: hex,
              borderRight: `1px solid ${P_TOK.ink200}`,
              position: "relative",
            }}/>
          ))}
        </div>
        <div style={{
          display: "grid", gridTemplateColumns: "repeat(12, 1fr)",
          gap: 1, marginTop: 6,
          fontFamily: P_FONT.mono, fontSize: 9, color: P_TOK.ink500,
          letterSpacing: "0.04em",
        }}>
          {inkRamp.map(([name, hex]) => (
            <div key={name} style={{ paddingLeft: 2 }}>
              <div>{name.split("/")[1]}</div>
              <div style={{ fontSize: 8, opacity: 0.7 }}>{hex.slice(1)}</div>
            </div>
          ))}
        </div>
      </div>

      <div style={{ marginTop: P_SP(6) }}>
        <PCaption>Semantic accents</PCaption>
        <div style={{
          marginTop: P_SP(3),
          display: "flex", flexDirection: "column", gap: P_SP(2),
        }}>
          {accents.map(([name, hex, use]) => (
            <div key={name} style={{
              display: "flex", alignItems: "center", gap: P_SP(3),
              padding: `${P_SP(2)}px 0`,
              borderBottom: `1px solid ${P_TOK.ink200}`,
            }}>
              <div style={{
                width: 36, height: 36,
                background: hex, borderRadius: 4,
                border: `1px solid ${P_TOK.ink200}`,
              }}/>
              <div style={{ minWidth: 90 }}>
                <div style={{
                  fontFamily: P_FONT.mono, fontSize: 12,
                  color: P_TOK.ink800,
                }}>{name}</div>
                <div style={{
                  fontFamily: P_FONT.mono, fontSize: 10,
                  color: P_TOK.ink500, opacity: 0.7,
                }}>{hex}</div>
              </div>
              <div style={{
                fontSize: 12.5, color: P_TOK.ink700, flex: 1,
              }}>{use}</div>
            </div>
          ))}
        </div>
      </div>

      <div style={{
        marginTop: P_SP(5),
        padding: `${P_SP(3)}px ${P_SP(4)}px`,
        border: `1px dashed ${P_TOK.ink300}`,
        background: P_TOK.ink100,
        fontSize: 12.5, color: P_TOK.ink700, lineHeight: 1.55,
      }}>
        <span style={{ color: P_TOK.amber, fontWeight: 500 }}>Rule</span>
        <span style={{ color: P_TOK.ink500 }}> · </span>
        Amber is the only accent the user clicks. Moss and honey are
        passive — verification, status, hint dots. Coral blocks. No accent
        outside this set.
      </div>
    </DocFrame>
  );
}

function PrecisionComponentsDoc() {
  return (
    <DocFrame title="Components">
      <CompSection title="Status indicators">
        <div style={{ display: "flex", gap: P_SP(5), flexWrap: "wrap" }}>
          <StatusPreview status="ready"   label="Ready" />
          <StatusPreview status="warn"    label="Needs attention" />
          <StatusPreview status="blocked" label="Cannot generate" />
          <StatusPreview status="locked"  label="Locked" />
        </div>
      </CompSection>

      <CompSection title="Chips">
        <div style={{ display: "flex", gap: P_SP(3), flexWrap: "wrap" }}>
          <PChip tone="moss">verified</PChip>
          <PChip tone="amber">received</PChip>
          <PChip tone="coral">blocked</PChip>
          <PChip tone="ink">read-only</PChip>
        </div>
      </CompSection>

      <CompSection title="Buttons">
        <div style={{ display: "flex", gap: P_SP(3), alignItems: "center", flexWrap: "wrap" }}>
          <button style={{
            background: P_TOK.amber, color: "#1a0f02", border: 0,
            padding: "10px 18px", fontFamily: P_FONT.body, fontSize: 13,
            fontWeight: 500, borderRadius: 4, cursor: "pointer",
          }}>Primary</button>
          <button style={{
            background: "transparent", color: P_TOK.amber,
            border: `1px solid ${P_TOK.amber}`,
            padding: "9px 18px", fontFamily: P_FONT.body, fontSize: 13,
            fontWeight: 500, borderRadius: 4, cursor: "pointer",
          }}>Secondary</button>
          <button style={pGhostBtn()}>Ghost link</button>
        </div>
      </CompSection>

      <CompSection title="Password slab">
        <div style={{
          display: "flex", alignItems: "stretch",
          borderTop: `1px solid ${P_TOK.ink300}`,
          borderBottom: `1px solid ${P_TOK.ink300}`,
        }}>
          <div style={{
            padding: `${P_SP(3)}px 0`, flex: 1,
            fontFamily: P_FONT.mono, fontSize: 22,
            color: P_TOK.ink900,
          }}>{P_PWD}</div>
          <button style={{
            border: 0, background: "transparent", color: P_TOK.ink800,
            padding: `0 ${P_SP(3)}px`, fontFamily: P_FONT.body, fontSize: 13,
            cursor: "pointer", display: "flex", alignItems: "center", gap: 6,
          }}>
            <PCopyIcon/> Copy
          </button>
        </div>
      </CompSection>

      <CompSection title="Spacing scale · 4-base">
        <div style={{ display: "flex", alignItems: "flex-end", gap: P_SP(3) }}>
          {[1,2,3,4,5,6,7].map(n => (
            <div key={n} style={{ textAlign: "center" }}>
              <div style={{
                width: 28, height: P_SP(n),
                background: P_TOK.amber, opacity: 0.4 + n * 0.08,
              }} />
              <div style={{
                marginTop: 6,
                fontFamily: P_FONT.mono, fontSize: 10,
                color: P_TOK.ink500, letterSpacing: "0.04em",
              }}>{P_SP(n)}px</div>
            </div>
          ))}
        </div>
      </CompSection>
    </DocFrame>
  );
}

function CompSection({ title, children }) {
  return (
    <div style={{ padding: `${P_SP(4)}px 0`, borderBottom: `1px solid ${P_TOK.ink200}` }}>
      <PCaption style={{ marginBottom: P_SP(3) }}>{title}</PCaption>
      {children}
    </div>
  );
}

function StatusPreview({ status, label }) {
  const map = {
    ready: P_TOK.moss, warn: P_TOK.amber,
    blocked: P_TOK.coral, locked: P_TOK.ink400,
  };
  return (
    <div style={{ display: "flex", alignItems: "center", gap: P_SP(2) }}>
      <span style={{
        width: 7, height: 7, borderRadius: "50%",
        background: map[status],
        boxShadow: `0 0 8px ${map[status]}66`,
      }} />
      <span style={{ fontSize: 13, color: P_TOK.ink800 }}>{label}</span>
    </div>
  );
}

function DocFrame({ title, children }) {
  return (
    <div style={{
      width: "100%", height: "100%",
      background: P_TOK.ink050, color: P_TOK.ink900,
      fontFamily: P_FONT.body, padding: `${P_SP(6)}px ${P_SP(6)}px ${P_SP(5)}px`,
      overflow: "hidden", display: "flex", flexDirection: "column",
    }}>
      <div style={{
        display: "flex", alignItems: "baseline", gap: P_SP(3),
        marginBottom: P_SP(5),
        paddingBottom: P_SP(3),
        borderBottom: `1px solid ${P_TOK.ink200}`,
      }}>
        <div style={{
          fontFamily: P_FONT.display, fontSize: 26, fontWeight: 400,
          letterSpacing: "-0.02em", color: P_TOK.ink900,
        }}>{title}</div>
        <div style={{
          fontFamily: P_FONT.mono, fontSize: 10.5, color: P_TOK.ink500,
          letterSpacing: "0.18em", textTransform: "uppercase",
        }}>precision · v0.1</div>
      </div>
      <div style={{ flex: 1, overflow: "hidden" }}>{children}</div>
    </div>
  );
}

// expose
Object.assign(window, {
  PrecisionGen, PrecisionHome,
  PrecisionTypeDoc, PrecisionColorDoc, PrecisionComponentsDoc,
  // tokens + primitives — re-used by DesignSystem.html doc page
  P_TOK, P_FONT, P_SP,
  PCaption, PChip, PDivider, PWordmark, pGhostBtn,
  PCopyIcon, PCheckTiny,
});
