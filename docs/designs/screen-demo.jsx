// screen-demo.jsx — Per spec 16-demo-page.md
//
// Standalone explainer page (opened via chrome.tabs.create on Learn-more / How-it-works).
// Max-width 700px, dark theme, 4 sections + Close button.
//
// Single state — content is mostly static.

function ODemoPage({ state = "default" }) {
  return (
    <OPage height="auto" glow="amber">
      <div style={{ position: "relative", zIndex: 1, paddingBottom: M_SP(7) }}>

        {/* Top nav strip */}
        <div style={{
          borderBottom: `1px solid ${M_TOK.ink200}`,
          padding: `${M_SP(4)}px 0`,
        }}>
          <OContainer width={760}>
            <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
              <MWordmark size={22}/>
              <div style={{
                fontFamily: M_FONT.mono, fontSize: 10.5,
                letterSpacing: "0.18em", textTransform: "uppercase",
                color: M_TOK.ink500,
              }}>
                Demo · how it works
              </div>
              <button style={{
                border: `1px solid ${M_TOK.ink300}`,
                background: "transparent", color: M_TOK.ink700,
                padding: "5px 10px", borderRadius: 4,
                fontSize: 11.5, cursor: "pointer",
              }}>Close this tab</button>
            </div>
          </OContainer>
        </div>

        {/* Hero */}
        <OContainer width={720} style={{ padding: `${M_SP(8)}px ${M_SP(5)}px ${M_SP(5)}px` }}>
          <MCaption style={{ marginBottom: 12 }}>Read time · ~3 min</MCaption>
          <div style={{
            fontFamily: M_FONT.display, fontSize: 48, fontWeight: 400,
            letterSpacing: "-0.03em", lineHeight: 1.05,
            color: M_TOK.ink900,
          }}>
            How <span style={{ fontStyle: "italic" }}>mind</span>
            <span style={{ fontStyle: "italic" }}>Vault</span> works.
          </div>
          <div style={{
            color: M_TOK.ink600, fontSize: 16, lineHeight: 1.55,
            marginTop: 14, maxWidth: 580,
            fontFamily: M_FONT.display, fontStyle: "italic",
          }}>
            A short recipe in a spreadsheet, five private phrases in your head — every password derived on demand, nothing at rest.
          </div>

          {/* TOC */}
          <div style={{ display: "flex", flexWrap: "wrap", gap: 8, marginTop: M_SP(5) }}>
            {["The idea", "Anatomy of a recipe", "Modifiers", "Daily use"].map((t, i) => (
              <span key={t} style={{
                display: "inline-flex", alignItems: "center", gap: 6,
                padding: "5px 10px",
                border: `1px solid ${M_TOK.ink300}`,
                borderRadius: 999,
                fontSize: 11.5, color: M_TOK.ink700,
              }}>
                <span style={{
                  fontFamily: M_FONT.mono, fontSize: 9.5,
                  letterSpacing: "0.16em", color: M_TOK.amber,
                }}>{String(i+1).padStart(2, "0")}</span>
                <span>{t}</span>
              </span>
            ))}
          </div>
        </OContainer>

        {/* Sections */}
        <OContainer width={720}>
          <DemoSection
            n="01"
            title="The idea"
            body="Your passwords live in your head — not in a database. A recipe is a short code you store in Google Sheets. PassChef reads the cell, combines it with one of your five private phrases, and computes the password on demand. Close the popup and the password is gone."
            visual={<IdeaVisual/>}
          />

          <DemoSection
            n="02"
            title="Anatomy of a recipe"
            body="A recipe looks like fb#1 — a hash you choose, a position symbol, and a secret index from 1 to 5. The hash is the only public part; the index points at one of your five phrases, never spelling it out."
            visual={<RecipeAnatomy/>}
          />

          <DemoSection
            n="03"
            title="Modifiers"
            body="Trailing symbols change how the secret is woven into the hash. Each modifier is deterministic and reversible — same recipe, same output, every time."
            visual={<ModifierTable/>}
          />

          <DemoSection
            n="04"
            title="Daily use"
            body="Open a spreadsheet, click the cell with your recipe, then open PassChef. The popup auto-detects the cell and generates the password. Paste it, then close. Nothing was written to disk, nothing was sent over the network."
            visual={<DailyFlow/>}
          />

          {/* Closing */}
          <div style={{
            marginTop: M_SP(7),
            paddingTop: M_SP(5),
            borderTop: `1px solid ${M_TOK.ink200}`,
            display: "flex", justifyContent: "space-between", alignItems: "center",
            gap: M_SP(4), flexWrap: "wrap",
          }}>
            <div>
              <div style={{
                fontFamily: M_FONT.display, fontSize: 22, fontStyle: "italic",
                letterSpacing: "-0.02em", color: M_TOK.ink900,
              }}>
                That's it.
              </div>
              <div style={{ color: M_TOK.ink600, fontSize: 13, marginTop: 6, maxWidth: 460, lineHeight: 1.55 }}>
                The rest is detail — profile routing, sheet locking, shared bundles. Worth knowing later. Not worth knowing today.
              </div>
            </div>
            <div style={{ display: "flex", gap: 8 }}>
              <OBtn kind="secondary">Open Recipe Builder</OBtn>
              <button style={{
                background: M_TOK.amber, color: "#1a0f02", border: 0,
                padding: `${M_SP(2) + 2}px ${M_SP(4)}px`,
                fontFamily: M_FONT.body, fontSize: 13, fontWeight: 500,
                borderRadius: 4, cursor: "pointer",
              }}>
                Close this tab
              </button>
            </div>
          </div>
        </OContainer>
      </div>
    </OPage>
  );
}

// ── Section wrapper ──────────────────────────────────────────────────────

function DemoSection({ n, title, body, visual }) {
  return (
    <div style={{
      marginTop: M_SP(6),
      paddingTop: M_SP(5),
      borderTop: `1px solid ${M_TOK.ink200}`,
      display: "grid", gridTemplateColumns: "1fr 1fr", gap: M_SP(5),
      alignItems: "flex-start",
    }}>
      <div>
        <div style={{
          fontFamily: M_FONT.mono, fontSize: 10.5,
          letterSpacing: "0.22em", textTransform: "uppercase",
          color: M_TOK.amber,
          marginBottom: 10,
        }}>
          Section {n}
        </div>
        <div style={{
          fontFamily: M_FONT.display, fontSize: 28, fontWeight: 400,
          letterSpacing: "-0.025em", color: M_TOK.ink900, lineHeight: 1.1,
        }}>
          <span style={{ fontStyle: "italic" }}>{title}</span>
        </div>
        <div style={{
          color: M_TOK.ink700, fontSize: 14, lineHeight: 1.6,
          marginTop: M_SP(3),
        }}>{body}</div>
      </div>
      <div>{visual}</div>
    </div>
  );
}

// ── Section 1 visual: idea (in head + in sheet → password) ──────────────

function IdeaVisual() {
  return (
    <div style={{
      background: M_TOK.ink100,
      border: `1px solid ${M_TOK.ink200}`,
      borderRadius: 6,
      padding: M_SP(5),
    }}>
      <div style={{ display: "flex", alignItems: "center", gap: M_SP(3) }}>
        <PieceLabel small>in your head</PieceLabel>
        <div style={{
          flex: 1,
          padding: `${M_SP(3)}px`,
          background: M_TOK.ink050,
          border: `1px dashed ${M_TOK.ink300}`,
          borderRadius: 4,
          fontFamily: M_FONT.display, fontStyle: "italic", fontSize: 14,
          color: M_TOK.ink800, textAlign: "center",
        }}>
          five private phrases
        </div>
      </div>

      <div style={{ display: "flex", justifyContent: "center", color: M_TOK.ink500, fontSize: 16, padding: `${M_SP(2)}px 0` }}>+</div>

      <div style={{ display: "flex", alignItems: "center", gap: M_SP(3) }}>
        <PieceLabel small>in your sheet</PieceLabel>
        <div style={{
          flex: 1, display: "flex", alignItems: "center", justifyContent: "center",
          padding: `${M_SP(3)}px`,
          background: M_TOK.ink050,
          border: `1px solid ${M_TOK.ink300}`,
          borderRadius: 4,
          fontFamily: M_FONT.mono, fontSize: 15, color: M_TOK.amber,
          letterSpacing: "0.04em",
        }}>
          fb#1
        </div>
      </div>

      <div style={{ display: "flex", justifyContent: "center", color: M_TOK.ink500, fontSize: 16, padding: `${M_SP(2)}px 0` }}>↓</div>

      <div style={{ display: "flex", alignItems: "center", gap: M_SP(3) }}>
        <PieceLabel small>derived</PieceLabel>
        <div style={{
          flex: 1, display: "flex", alignItems: "center", justifyContent: "center",
          padding: `${M_SP(3)}px`,
          background: M_TOK.amberBg, border: `1px solid ${M_TOK.amber}55`,
          borderRadius: 4,
          fontFamily: M_FONT.mono, fontSize: 15, color: M_TOK.amber,
          letterSpacing: "0.05em",
        }}>
          {M_PWD}
        </div>
      </div>
    </div>
  );
}

function PieceLabel({ children, small }) {
  return (
    <div style={{
      width: 96, flexShrink: 0,
      fontFamily: M_FONT.mono, fontSize: small ? 9.5 : 11,
      letterSpacing: "0.18em", textTransform: "uppercase",
      color: M_TOK.ink500,
    }}>{children}</div>
  );
}

// ── Section 2 visual: recipe anatomy ─────────────────────────────────────

function RecipeAnatomy() {
  const annot = [
    { part: "fb", label: "hash", sub: "any ASCII text you choose", color: M_TOK.ink700 },
    { part: "#",  label: "position", sub: "where the secret is woven in", color: M_TOK.amber },
    { part: "1",  label: "secret index", sub: "1 – 5, picks one of your phrases", color: M_TOK.moss },
    { part: "_",  label: "modifier", sub: "optional — transforms the combination", color: M_TOK.coral },
  ];
  return (
    <div style={{
      background: M_TOK.ink100,
      border: `1px solid ${M_TOK.ink200}`,
      borderRadius: 6,
      padding: M_SP(5),
    }}>
      {/* the recipe rendered large */}
      <div style={{
        background: M_TOK.ink050,
        border: `1px solid ${M_TOK.ink300}`,
        borderRadius: 4,
        padding: `${M_SP(4)}px ${M_SP(3)}px`,
        display: "flex", justifyContent: "center", gap: 0,
        fontFamily: M_FONT.mono, fontSize: 32,
        letterSpacing: "0.08em",
      }}>
        {annot.map((a, i) => (
          <span key={i} style={{ color: a.color, padding: "0 2px" }}>{a.part}</span>
        ))}
      </div>

      <div style={{
        display: "grid", gridTemplateColumns: "auto 1fr",
        rowGap: 10, columnGap: M_SP(3),
        marginTop: M_SP(4),
      }}>
        {annot.map((a, i) => (
          <React.Fragment key={i}>
            <span style={{
              fontFamily: M_FONT.mono, fontSize: 16, color: a.color,
              alignSelf: "center", textAlign: "center", minWidth: 22,
            }}>{a.part}</span>
            <div>
              <div style={{
                fontFamily: M_FONT.mono, fontSize: 9.5,
                letterSpacing: "0.16em", textTransform: "uppercase",
                color: a.color,
              }}>{a.label}</div>
              <div style={{ fontSize: 12, color: M_TOK.ink700, marginTop: 2 }}>{a.sub}</div>
            </div>
          </React.Fragment>
        ))}
      </div>
    </div>
  );
}

// ── Section 3 visual: modifier table ────────────────────────────────────

function ModifierTable() {
  const rows = [
    { sym: "_", name: "flip",    eff: "Reverses how the secret combines with the hash" },
    { sym: "!", name: "upper",   eff: "Uppercases the secret before combining" },
    { sym: "?", name: "reverse", eff: "Reverses the secret before combining" },
    { sym: "~", name: "strip",   eff: "Strips spaces and punctuation from the secret" },
  ];
  return (
    <div style={{
      background: M_TOK.ink100,
      border: `1px solid ${M_TOK.ink200}`,
      borderRadius: 6,
      overflow: "hidden",
    }}>
      {rows.map((r, i) => (
        <div key={r.sym} style={{
          display: "grid", gridTemplateColumns: "44px 88px 1fr",
          alignItems: "center", gap: M_SP(3),
          padding: `${M_SP(3) + 2}px ${M_SP(4)}px`,
          borderTop: i === 0 ? "none" : `1px solid ${M_TOK.ink200}`,
        }}>
          <span style={{
            fontFamily: M_FONT.mono, fontSize: 22,
            color: M_TOK.coral, textAlign: "center",
          }}>{r.sym}</span>
          <span style={{
            fontFamily: M_FONT.display, fontStyle: "italic", fontSize: 15,
            color: M_TOK.ink900,
          }}>{r.name}</span>
          <span style={{ fontSize: 12.5, color: M_TOK.ink700, lineHeight: 1.5 }}>{r.eff}</span>
        </div>
      ))}
    </div>
  );
}

// ── Section 4 visual: daily flow ────────────────────────────────────────

function DailyFlow() {
  const steps = [
    { n: "01", t: "Click a recipe cell", s: "Anywhere on the spreadsheet — PassChef reads the active cell." },
    { n: "02", t: "Open the extension",  s: "The popup auto-routes to the Generated screen if the cell holds a recipe." },
    { n: "03", t: "Copy & paste",        s: "The password is on the clipboard. Close the popup and it disappears." },
  ];
  return (
    <div style={{
      background: M_TOK.ink100,
      border: `1px solid ${M_TOK.ink200}`,
      borderRadius: 6,
      padding: M_SP(4),
      display: "flex", flexDirection: "column", gap: M_SP(3),
    }}>
      {steps.map((s, i) => (
        <div key={s.n} style={{ display: "flex", gap: M_SP(3), alignItems: "flex-start" }}>
          <div style={{
            width: 32, height: 32, flexShrink: 0,
            background: M_TOK.ink050,
            border: `1px solid ${M_TOK.ink300}`,
            borderRadius: 4,
            display: "flex", alignItems: "center", justifyContent: "center",
            fontFamily: M_FONT.mono, fontSize: 11, color: M_TOK.amber,
            letterSpacing: "0.12em",
          }}>{s.n}</div>
          <div style={{ flex: 1 }}>
            <div style={{
              fontFamily: M_FONT.display, fontStyle: "italic", fontSize: 15,
              color: M_TOK.ink900,
            }}>{s.t}</div>
            <div style={{ fontSize: 12, color: M_TOK.ink700, marginTop: 3, lineHeight: 1.5 }}>{s.s}</div>
          </div>
        </div>
      ))}

      <div style={{
        marginTop: M_SP(2),
        padding: `${M_SP(2) + 2}px ${M_SP(3)}px`,
        background: M_TOK.mossBg + "70",
        border: `1px solid ${M_TOK.moss}55`,
        borderRadius: 4,
        fontSize: 11.5, color: M_TOK.ink800, lineHeight: 1.55,
        display: "flex", alignItems: "center", gap: 8,
      }}>
        <span style={{
          width: 6, height: 6, borderRadius: "50%",
          background: M_TOK.moss, boxShadow: `0 0 6px ${M_TOK.moss}aa`,
        }}/>
        Nothing is stored, transmitted, or logged outside your device.
      </div>
    </div>
  );
}

Object.assign(window, { ODemoPage });
