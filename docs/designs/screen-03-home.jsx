// screen-03-home.jsx — Popup Home / Hub screen.
// Main hub after unlock. Routes between contextual states based on active tab
// and Quick Start dismissal flag.
//
// Contextual states (per spec 03-popup-home.md):
//   A · "quick-start"  — first-use; #home-quick-start panel + "Create First Recipe"
//   B · "standard"     — non-Sheets tab; #home-hint + profiles summary
//   C · "sheet-empty"  — Sheets tab, no recipe in cell; #home-sheet-context + amber notice
//   D · "sheet-reload" — Sheets tab, content script lost; same badge + "needs reload" notice

// A · Faithful — all 4 contextual states in one component, props-driven
function MHomeFaithful({ context = "standard", withInventory = true }) {
  // context: "quick-start" | "standard" | "sheet-empty" | "sheet-reload"
  const isQuickStart = context === "quick-start";
  const isSheetEmpty = context === "sheet-empty";
  const isSheetReload = context === "sheet-reload";
  const onSheet = isSheetEmpty || isSheetReload;

  return (
    <MFrame glow={onSheet ? "amber" : "moss"}>
      <MHeader status="ready" />
      <MHeaderRule />
      <div style={{
        flex: 1, padding: `${M_SP(5)}px ${M_SP(5)}px ${M_SP(5)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
      }}>

        {/* QUICK START PANEL — first-use only */}
        {isQuickStart && <QuickStartPanel />}

        {/* SHEET CONTEXT BADGE — on Sheets tab states */}
        {onSheet && <SheetContextBadge name="Budget 2026" />}

        {/* AMBER NOTICE — sheet error states */}
        {onSheet && (
          <SheetNotice
            kind={isSheetReload ? "reload" : "empty"}
          />
        )}

        {/* DEFAULT BODY — standard state only (not first-use, not sheet) */}
        {!isQuickStart && !onSheet && (
          <>
            <MCaption>Vault unlocked</MCaption>
            <div style={{ height: M_SP(3) }} />
            <MDisplay size={22}>
              <span style={{ fontStyle: "italic" }}>Click a cell</span><br/>
              containing a recipe.
            </MDisplay>
            <div style={{ height: M_SP(2) }} />
            <MBody muted>
              In Google Sheets, select any cell with a recipe — the password decodes here.
            </MBody>

            {withInventory && (
              <>
                <div style={{ height: M_SP(5) }} />
                <MDivider />
                <div style={{ height: M_SP(4) }} />
                <MCaption>Inventory</MCaption>
                <div style={{
                  marginTop: M_SP(3),
                  display: "flex", gap: M_SP(5),
                }}>
                  <MStat label="Own" value="3" />
                  <MStat label="Shared" value="1" />
                  <MStat label="Sheets bound" value="4" />
                </div>
              </>
            )}
          </>
        )}

        <div style={{ flex: 1 }} />

        <MPrimaryButton>
          {isQuickStart ? "Create first recipe" : "Build recipe"}
        </MPrimaryButton>
        <div style={{
          display: "flex", justifyContent: "space-between", alignItems: "center",
          marginTop: M_SP(3),
        }}>
          <MGhostButton>Manage profiles</MGhostButton>
          <MGhostButton>
            Lock <span style={{ marginLeft: 6, opacity: 0.55 }}>⌘L</span>
          </MGhostButton>
        </div>
      </div>
    </MFrame>
  );
}

// Quick Start panel (#home-quick-start)
function QuickStartPanel() {
  return (
    <div style={{
      border: `1px solid ${M_TOK.ink300}`,
      borderRadius: 4,
      padding: `${M_SP(4)}px ${M_SP(4)}px ${M_SP(3)}px`,
      background: M_TOK.ink100 + "90",
      position: "relative",
    }}>
      {/* dismiss × */}
      <button title="Dismiss" style={{
        position: "absolute", top: 6, right: 8,
        border: 0, background: "transparent",
        color: M_TOK.ink500, cursor: "pointer",
        fontSize: 14, lineHeight: 1, padding: 4,
      }}>×</button>

      <div style={{
        fontFamily: M_FONT.display, fontSize: 16, fontStyle: "italic",
        color: M_TOK.ink900, letterSpacing: "-0.01em",
      }}>How it works</div>

      <div style={{ height: M_SP(3) }} />

      <QuickStartStep n="1" text="Build a recipe" />
      <QuickStartStep n="2" text="Paste it into a Google Sheets cell" />
      <QuickStartStep n="3" text="Click the cell → password appears" last />

      <div style={{
        marginTop: M_SP(3),
        display: "flex", alignItems: "center", justifyContent: "space-between",
      }}>
        <button style={{
          border: `1px solid ${M_TOK.ink300}`,
          background: "transparent",
          color: M_TOK.ink800,
          padding: "4px 10px", borderRadius: 3,
          fontSize: 11.5, cursor: "pointer",
          display: "flex", alignItems: "center", gap: 5,
        }}>
          <MCheckTiny size={9} color={M_TOK.ink700}/>
          <span>Got it</span>
        </button>
        <MGhostButton style={{ color: M_TOK.amber, fontSize: 11.5 }}>
          Learn more →
        </MGhostButton>
      </div>
    </div>
  );
}

function QuickStartStep({ n, text, last }) {
  return (
    <div style={{
      display: "flex", alignItems: "baseline", gap: M_SP(2),
      paddingBottom: last ? 0 : 4,
    }}>
      <span style={{
        fontFamily: M_FONT.mono, fontSize: 10,
        color: M_TOK.amber, letterSpacing: "0.05em",
        minWidth: 14,
      }}>{n}</span>
      <span style={{
        fontSize: 12.5, color: M_TOK.ink800, lineHeight: 1.5,
      }}>{text}</span>
    </div>
  );
}

// Sheet name badge (#home-sheet-context) — formatted "📄 $1"
function SheetContextBadge({ name }) {
  return (
    <div style={{
      display: "flex", alignItems: "center", gap: M_SP(2),
      padding: `${M_SP(2)}px ${M_SP(3)}px`,
      border: `1px solid ${M_TOK.ink200}`,
      borderRadius: 3,
      background: M_TOK.ink100 + "70",
      width: "fit-content",
      maxWidth: "100%",
    }}>
      <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
        <rect x="2" y="1.5" width="8" height="9" rx="1" stroke={M_TOK.ink600} strokeWidth="1"/>
        <path d="M4 4h4M4 6h4M4 8h2.5" stroke={M_TOK.ink500} strokeWidth="1" strokeLinecap="round"/>
      </svg>
      <span style={{
        fontSize: 12, color: M_TOK.ink800,
        fontFamily: M_FONT.body,
        whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis",
      }}>{name}</span>
    </div>
  );
}

// Amber inline notice (#home-notice)
function SheetNotice({ kind }) {
  const text = kind === "reload"
    ? "Extension needs tab reload."
    : "Selected cell is empty or has no recipe.";
  const sub = kind === "reload"
    ? "Reload the Sheets tab so the content script re-injects."
    : "Click a different cell, or build a new recipe and paste it.";

  return (
    <>
      <div style={{ height: M_SP(4) }} />
      <div style={{
        padding: `${M_SP(3)}px ${M_SP(3)}px`,
        borderTop: `1px solid ${M_TOK.amber}66`,
        borderBottom: `1px solid ${M_TOK.ink300}`,
        background: `linear-gradient(180deg, ${M_TOK.amberBg}66 0%, transparent 100%)`,
        display: "flex", flexDirection: "column", gap: 4,
      }}>
        <div style={{
          display: "flex", alignItems: "center", gap: M_SP(2),
        }}>
          <span style={{ fontSize: 13, color: M_TOK.amber }}>⚠</span>
          <span style={{
            fontFamily: M_FONT.display, fontStyle: "italic",
            fontSize: 15, color: M_TOK.ink900, letterSpacing: "-0.01em",
          }}>{text}</span>
        </div>
        <div style={{
          fontSize: 11.5, color: M_TOK.ink600, lineHeight: 1.4,
          paddingLeft: 22,
        }}>{sub}</div>
      </div>

      <div style={{ height: M_SP(4) }} />
      <MCaption>Or</MCaption>
      <div style={{ height: M_SP(2) }} />
      <MBody muted style={{ fontSize: 12.5 }}>
        Build a fresh recipe below and paste it back into a cell.
      </MBody>
    </>
  );
}

function MStat({ label, value }) {
  return (
    <div>
      <div style={{
        fontFamily: M_FONT.display, fontSize: 26, fontWeight: 400,
        color: M_TOK.ink900, lineHeight: 1, letterSpacing: "-0.02em",
      }}>{value}</div>
      <div style={{
        marginTop: 4,
        fontFamily: M_FONT.mono, fontSize: 9.5, fontWeight: 500,
        letterSpacing: "0.18em", textTransform: "uppercase",
        color: M_TOK.ink500,
      }}>{label}</div>
    </div>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// EXPLORES
// ──────────────────────────────────────────────────────────────────────────

// B · Explore — recipe-shape hero, builder-first
function MHomeBuilderFirst() {
  return (
    <MFrame glow="amber">
      <MHeader status="ready" />
      <MHeaderRule />
      <div style={{
        flex: 1, padding: `${M_SP(5)}px ${M_SP(5)}px ${M_SP(5)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
      }}>
        <MCaption accent>Ready to compose</MCaption>
        <div style={{ height: M_SP(3) }} />
        <MDisplay size={28} italic>
          Build something<br/>
          <span style={{ fontStyle: "normal" }}>memorable.</span>
        </MDisplay>

        <div style={{ height: M_SP(5) }} />

        <div style={{
          padding: `${M_SP(3)}px ${M_SP(4)}px`,
          border: `1px dashed ${M_TOK.ink300}`,
          borderRadius: 4,
          fontFamily: M_FONT.mono, fontSize: 16,
          color: M_TOK.ink600, letterSpacing: "0.04em",
          background: M_TOK.ink100 + "60",
        }}>
          <span style={{ color: M_TOK.ink500 }}>hash</span>
          <span style={{ color: M_TOK.amber, margin: "0 4px" }}>#</span>
          <span style={{ color: M_TOK.ink500 }}>n</span>
          <span style={{ color: M_TOK.ink500, marginLeft: 4 }}>[mods]</span>
          <span style={{ color: M_TOK.ink500, marginLeft: 4 }}>.tag</span>
        </div>
        <div style={{ height: M_SP(2) }} />
        <MBody muted style={{ fontSize: 12 }}>
          A recipe is a public string that regenerates your private password.
        </MBody>

        <div style={{ flex: 1 }} />

        <MPrimaryButton>Build recipe</MPrimaryButton>
        <div style={{
          marginTop: M_SP(3),
          display: "flex", justifyContent: "space-between", alignItems: "center",
          fontFamily: M_FONT.mono, fontSize: 10,
          letterSpacing: "0.18em", textTransform: "uppercase",
          color: M_TOK.ink500,
        }}>
          <span>or click a cell on a sheet</span>
          <MGhostButton style={{
            fontFamily: M_FONT.mono, fontSize: 10,
            letterSpacing: "0.18em", textTransform: "uppercase",
          }}>Lock</MGhostButton>
        </div>
      </div>
    </MFrame>
  );
}

// C · Explore — terminal status
function MHomeTerminal() {
  return (
    <MFrame glow="moss">
      <MHeader status="ready" />
      <MHeaderRule />
      <div style={{
        flex: 1, padding: `${M_SP(5)}px ${M_SP(5)}px ${M_SP(5)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
      }}>
        <div style={{
          fontFamily: M_FONT.mono, fontSize: 12,
          color: M_TOK.ink700, lineHeight: 1.7,
        }}>
          <TermLine k="vault" v="unlocked · 14:32" />
          <TermLine k="profile" v="Default" highlight />
          <TermLine k="own" v="3 profiles" />
          <TermLine k="shared" v="1 profile · TeamFromB" />
          <TermLine k="sheets" v="4 bound · 2 unmapped" />
          <TermLine k="active tab" v="not a sheet" muted />
        </div>

        <div style={{ height: M_SP(5) }} />
        <MDivider kind="dashed" />
        <div style={{ height: M_SP(4) }} />

        <MDisplay size={20} italic>
          Open a sheet to decode,<br/>
          <span style={{ fontStyle: "normal", color: M_TOK.ink700 }}>or build a recipe here.</span>
        </MDisplay>

        <div style={{ flex: 1 }} />

        <MPrimaryButton>Build recipe</MPrimaryButton>
        <div style={{
          display: "flex", justifyContent: "space-between", alignItems: "center",
          marginTop: M_SP(3),
        }}>
          <MGhostButton>Profiles ↗</MGhostButton>
          <MGhostButton>Lock <span style={{ marginLeft: 6, opacity: 0.55 }}>⌘L</span></MGhostButton>
        </div>
      </div>
    </MFrame>
  );
}

function TermLine({ k, v, highlight, muted }) {
  return (
    <div style={{
      display: "flex", gap: M_SP(3),
      color: muted ? M_TOK.ink500 : M_TOK.ink700,
    }}>
      <span style={{
        color: M_TOK.ink500, minWidth: 90,
        letterSpacing: "0.04em",
      }}>{k}</span>
      <span style={{ color: M_TOK.ink400 }}>·</span>
      <span style={{
        color: highlight ? M_TOK.amber : (muted ? M_TOK.ink500 : M_TOK.ink800),
      }}>{v}</span>
    </div>
  );
}

Object.assign(window, {
  MHomeFaithful, MHomeBuilderFirst, MHomeTerminal,
  MStat, QuickStartPanel, SheetContextBadge, SheetNotice,
});
