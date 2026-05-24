// design-system-doc.jsx — Standalone design system documentation page.
// Reads tokens + primitives from precision-refined.jsx (exposed on window).

const dsTok = window.P_TOK;
const dsFont = window.P_FONT;
const dsSp = window.P_SP;

// ──────────────────────────────────────────────────────────────────────────
// PAGE
// ──────────────────────────────────────────────────────────────────────────

function DesignSystemDoc() {
  return (
    <div style={{
      background: dsTok.ink050,
      color: dsTok.ink900,
      fontFamily: dsFont.body,
      minHeight: "100vh",
      padding: "0 0 120px",
    }}>
      <DSHeader/>
      <DSHero/>

      <DSSection num="01" title="Foundations" lede="The atoms. Color, type, spacing, radii, motion — everything else is a composition of these.">
        <DSColor/>
        <DSTypography/>
        <DSSpacing/>
        <DSRadii/>
        <DSMotion/>
      </DSSection>

      <DSSection num="02" title="Components" lede="The smallest reusable pieces. Each one has a single responsibility and a strict variant set.">
        <DSStatus/>
        <DSChips/>
        <DSButtons/>
        <DSPasswordSlab/>
        <DSField/>
      </DSSection>

      <DSSection num="03" title="Patterns" lede="Component compositions used in real screens. How the system reads when assembled.">
        <DSGenAnatomy/>
        <DSErrorGrid/>
      </DSSection>

      <DSFooter/>
    </div>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// CHROME
// ──────────────────────────────────────────────────────────────────────────

function DSHeader() {
  return (
    <div style={{
      position: "sticky", top: 0, zIndex: 10,
      background: dsTok.ink050 + "f0",
      backdropFilter: "blur(12px)",
      borderBottom: `1px solid ${dsTok.ink200}`,
      padding: `${dsSp(3)}px ${dsSp(6)}px`,
      display: "flex", alignItems: "center", justifyContent: "space-between",
    }}>
      <div style={{ display: "flex", alignItems: "center", gap: dsSp(5) }}>
        <window.PWordmark size={20}/>
        <span style={{
          fontFamily: dsFont.mono, fontSize: 10.5, fontWeight: 500,
          letterSpacing: "0.18em", textTransform: "uppercase",
          color: dsTok.ink500,
        }}>Design system · Precision</span>
      </div>
      <div style={{ display: "flex", gap: dsSp(5), fontSize: 12.5 }}>
        <a href="#foundations" style={dsNavLink}>Foundations</a>
        <a href="#components" style={dsNavLink}>Components</a>
        <a href="#patterns"   style={dsNavLink}>Patterns</a>
        <span style={{
          fontFamily: dsFont.mono, color: dsTok.ink500,
          letterSpacing: "0.04em", paddingLeft: dsSp(3),
          borderLeft: `1px solid ${dsTok.ink300}`,
        }}>v0.1 · 2026-05-22</span>
      </div>
    </div>
  );
}

const dsNavLink = {
  color: dsTok.ink700, textDecoration: "none",
  letterSpacing: "-0.005em",
};

function DSHero() {
  return (
    <div style={{
      maxWidth: 1040, margin: "0 auto",
      padding: `${dsSp(8)}px ${dsSp(6)}px ${dsSp(7)}px`,
      borderBottom: `1px solid ${dsTok.ink200}`,
      position: "relative",
    }}>
      <div style={{
        position: "absolute", top: 60, right: 60,
        width: 180, height: 180, borderRadius: "50%",
        background: `radial-gradient(circle, ${dsTok.amber}26 0%, transparent 70%)`,
        filter: "blur(8px)", pointerEvents: "none",
      }}/>

      <div style={{
        fontFamily: dsFont.mono, fontSize: 11, fontWeight: 500,
        letterSpacing: "0.22em", textTransform: "uppercase",
        color: dsTok.amber,
      }}>PassMan · v2 chrome extension</div>

      <h1 style={{
        margin: `${dsSp(4)}px 0 0`,
        fontFamily: dsFont.display, fontWeight: 400,
        fontSize: 72, lineHeight: 1.02, letterSpacing: "-0.03em",
        color: dsTok.ink900, maxWidth: 880,
      }}>
        A design system for <span style={{ fontStyle: "italic", color: dsTok.ink800 }}>a careful tool.</span>
      </h1>

      <p style={{
        margin: `${dsSp(5)}px 0 0`, maxWidth: 620,
        fontSize: 18, lineHeight: 1.55, color: dsTok.ink700,
        letterSpacing: "-0.005em",
      }}>
        Precision is the visual language of <i>PassMan</i> — a password
        generator that lives in your toolbar. Editorial in tone, sparse in
        chrome, deliberate in motion. The user is here for one thing:
        a credential. Everything else stands back.
      </p>

      <div style={{
        marginTop: dsSp(6),
        display: "grid", gridTemplateColumns: "repeat(4, minmax(0, 1fr))",
        gap: dsSp(5),
        paddingTop: dsSp(5),
        borderTop: `1px solid ${dsTok.ink200}`,
      }}>
        <HeroMeta label="Type pair" value="Fraunces · Geist · Geist Mono"/>
        <HeroMeta label="Surfaces" value="Near-black ink"/>
        <HeroMeta label="Accent" value="Amber 500 (one)"/>
        <HeroMeta label="Aim" value="Trustworthy · precise · quiet"/>
      </div>
    </div>
  );
}

function HeroMeta({ label, value }) {
  return (
    <div>
      <div style={{
        fontFamily: dsFont.mono, fontSize: 10, fontWeight: 500,
        letterSpacing: "0.22em", textTransform: "uppercase",
        color: dsTok.ink500,
      }}>{label}</div>
      <div style={{
        marginTop: 8, fontSize: 14.5, color: dsTok.ink800,
        letterSpacing: "-0.005em",
      }}>{value}</div>
    </div>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// SECTION SHELL
// ──────────────────────────────────────────────────────────────────────────

function DSSection({ num, title, lede, children }) {
  return (
    <section
      id={title.toLowerCase()}
      style={{
        maxWidth: 1040, margin: "0 auto",
        padding: `${dsSp(8)}px ${dsSp(6)}px ${dsSp(7)}px`,
        borderBottom: `1px solid ${dsTok.ink200}`,
      }}>
      <div style={{
        display: "grid", gridTemplateColumns: "120px 1fr",
        gap: dsSp(6), alignItems: "baseline",
        paddingBottom: dsSp(6),
      }}>
        <div style={{
          fontFamily: dsFont.mono, fontSize: 12, fontWeight: 500,
          letterSpacing: "0.22em", textTransform: "uppercase",
          color: dsTok.amber,
        }}>§ {num}</div>
        <div>
          <h2 style={{
            margin: 0,
            fontFamily: dsFont.display, fontSize: 48, fontWeight: 400,
            letterSpacing: "-0.025em", lineHeight: 1.05,
            color: dsTok.ink900,
          }}>{title}</h2>
          <p style={{
            margin: `${dsSp(3)}px 0 0`, maxWidth: 580,
            fontSize: 15, lineHeight: 1.55, color: dsTok.ink600,
            letterSpacing: "-0.005em",
          }}>{lede}</p>
        </div>
      </div>
      <div>{children}</div>
    </section>
  );
}

function DSSubsection({ eyebrow, title, lede, children, gridAlign = "baseline" }) {
  return (
    <div style={{
      padding: `${dsSp(6)}px 0`,
      borderTop: `1px solid ${dsTok.ink200}`,
      display: "grid", gridTemplateColumns: "240px 1fr", gap: dsSp(6),
      alignItems: gridAlign,
    }}>
      <div>
        <div style={{
          fontFamily: dsFont.mono, fontSize: 10, fontWeight: 500,
          letterSpacing: "0.22em", textTransform: "uppercase",
          color: dsTok.ink500,
        }}>{eyebrow}</div>
        <h3 style={{
          margin: `${dsSp(2)}px 0 ${dsSp(3)}px`,
          fontFamily: dsFont.display, fontSize: 26, fontWeight: 400,
          letterSpacing: "-0.02em", color: dsTok.ink900, lineHeight: 1.1,
        }}>{title}</h3>
        {lede && <p style={{
          margin: 0, fontSize: 13.5, lineHeight: 1.55,
          color: dsTok.ink600, letterSpacing: "-0.005em",
        }}>{lede}</p>}
      </div>
      <div>{children}</div>
    </div>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// FOUNDATIONS · COLOR
// ──────────────────────────────────────────────────────────────────────────

const inkRamp = [
  ["000", dsTok.ink000], ["050", dsTok.ink050],
  ["100", dsTok.ink100], ["150", dsTok.ink150],
  ["200", dsTok.ink200], ["300", dsTok.ink300],
  ["400", dsTok.ink400], ["500", dsTok.ink500],
  ["600", dsTok.ink600], ["700", dsTok.ink700],
  ["800", dsTok.ink800], ["900", dsTok.ink900],
];

const accents = [
  ["amber/500", dsTok.amber,   "Primary action · verification glyph · pepper dot"],
  ["amber/Hi",  dsTok.amberHi, "Hover state"],
  ["amber/Lo",  dsTok.amberLo, "Pressed / muted"],
  ["moss/500",  dsTok.moss,    "Success · Ready · Verified"],
  ["coral/500", dsTok.coral,   "Blocking error"],
  ["honey/500", dsTok.honey,   "Warning · Pepper hint dot"],
];

function DSColor() {
  return (
    <DSSubsection eyebrow="Color · 12-step + 6 accent" title="Warm ink, one accent."
      lede="Surfaces are a 12-step warm-gray ramp. Saturation comes only from semantic accents — and only when something needs to be true (verified), wrong (error), or actionable (primary).">

      <div style={{ marginBottom: dsSp(5) }}>
        <window.PCaption>Ink ramp</window.PCaption>
        <div style={{
          marginTop: dsSp(3),
          display: "grid", gridTemplateColumns: "repeat(12, 1fr)",
          gap: 0,
          border: `1px solid ${dsTok.ink200}`, height: 72,
        }}>
          {inkRamp.map(([name, hex], i) => (
            <div key={name} style={{
              background: hex,
              borderRight: i < 11 ? `1px solid ${dsTok.ink200}` : "none",
            }}/>
          ))}
        </div>
        <div style={{
          display: "grid", gridTemplateColumns: "repeat(12, 1fr)",
          marginTop: 8,
        }}>
          {inkRamp.map(([name, hex]) => (
            <div key={name} style={{
              fontFamily: dsFont.mono, fontSize: 9.5,
              color: dsTok.ink500, letterSpacing: "0.04em",
              paddingLeft: 2,
            }}>
              <div>ink/{name}</div>
              <div style={{ opacity: 0.55 }}>{hex.slice(1).toLowerCase()}</div>
            </div>
          ))}
        </div>
      </div>

      <window.PCaption style={{ marginTop: dsSp(5) }}>Semantic accents</window.PCaption>
      <div style={{
        marginTop: dsSp(3),
        border: `1px solid ${dsTok.ink200}`,
      }}>
        {accents.map(([name, hex, use], i) => (
          <div key={name} style={{
            display: "grid", gridTemplateColumns: "56px 160px 1fr",
            gap: dsSp(4), alignItems: "center",
            padding: `${dsSp(3)}px ${dsSp(4)}px`,
            borderBottom: i < accents.length - 1 ? `1px solid ${dsTok.ink200}` : "none",
            background: dsTok.ink100,
          }}>
            <div style={{ width: 40, height: 40, background: hex, borderRadius: 4 }}/>
            <div>
              <div style={{
                fontFamily: dsFont.mono, fontSize: 13, color: dsTok.ink800,
              }}>{name}</div>
              <div style={{
                fontFamily: dsFont.mono, fontSize: 10.5, color: dsTok.ink500,
              }}>{hex}</div>
            </div>
            <div style={{ fontSize: 13.5, color: dsTok.ink700, lineHeight: 1.5 }}>{use}</div>
          </div>
        ))}
      </div>

      <DSRule>
        <b style={{ color: dsTok.amber, fontWeight: 500 }}>Rule</b>
        <span style={{ color: dsTok.ink500 }}> · </span>
        Amber is the only accent the user clicks. Moss and honey are passive:
        verification, status, hint dots. Coral blocks. Anything outside this
        set is a system bug.
      </DSRule>
    </DSSubsection>
  );
}

function DSRule({ children }) {
  return (
    <div style={{
      marginTop: dsSp(5),
      padding: `${dsSp(3)}px ${dsSp(4)}px`,
      borderLeft: `2px solid ${dsTok.amber}`,
      background: dsTok.ink100,
      fontSize: 13.5, color: dsTok.ink700, lineHeight: 1.6,
    }}>{children}</div>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// FOUNDATIONS · TYPOGRAPHY
// ──────────────────────────────────────────────────────────────────────────

const typeRows = [
  { font: dsFont.display, size: 64, weight: 400, italic: false,
    label: "Display", spec: "Fraunces · 64/400 · -0.03em",
    sample: "A careful tool." },
  { font: dsFont.display, size: 28, weight: 400, italic: true,
    label: "Italic display", spec: "Fraunces italic · 28/400",
    sample: "Don't forget your pepper." },
  { font: dsFont.mono, size: 32, weight: 400, italic: false,
    label: "Display mono", spec: "Geist Mono · 32/400 · 0.005em",
    sample: "f7nQp2!vXm9kR$8B" },
  { font: dsFont.body, size: 20, weight: 400, italic: false,
    label: "Name", spec: "Geist · 20/400 · -0.015em",
    sample: "TeamFromB" },
  { font: dsFont.body, size: 15, weight: 400, italic: false,
    label: "Body", spec: "Geist · 15/400 · 1.55 leading",
    sample: "Precision is the visual language of PassMan." },
  { font: dsFont.body, size: 13, weight: 400, italic: false,
    label: "Body small", spec: "Geist · 13/400",
    sample: "Reload the tab to reconnect the content script." },
  { font: dsFont.mono, size: 10.5, weight: 500, italic: false, caps: true,
    label: "Caption", spec: "Geist Mono · 10.5/500 · 0.22em · uppercase",
    sample: "GENERATED · 16 CHARS · 96 BITS · VERIFIED" },
];

function DSTypography() {
  return (
    <DSSubsection eyebrow="Type · 3 families, 7 sizes" title="Editorial type pair."
      lede="Fraunces does the talking — display, italic accents, human voice. Geist holds structure. Geist Mono carries data and labels. Three families, no exceptions.">

      <div style={{ border: `1px solid ${dsTok.ink200}`, background: dsTok.ink100 }}>
        {typeRows.map((r, i) => (
          <div key={r.label} style={{
            display: "grid", gridTemplateColumns: "200px 1fr",
            gap: dsSp(4), alignItems: "baseline",
            padding: `${dsSp(4)}px ${dsSp(4)}px`,
            borderBottom: i < typeRows.length - 1 ? `1px solid ${dsTok.ink200}` : "none",
          }}>
            <div>
              <div style={{
                fontFamily: dsFont.mono, fontSize: 11, fontWeight: 500,
                letterSpacing: "0.18em", textTransform: "uppercase",
                color: dsTok.amber,
              }}>{r.label}</div>
              <div style={{
                marginTop: 6,
                fontFamily: dsFont.mono, fontSize: 10.5,
                color: dsTok.ink500, letterSpacing: "0.02em",
              }}>{r.spec}</div>
            </div>
            <div style={{
              fontFamily: r.font, fontSize: r.size, fontWeight: r.weight,
              fontStyle: r.italic ? "italic" : "normal",
              color: dsTok.ink900, lineHeight: 1.1,
              letterSpacing: r.caps ? "0.22em" : (r.size > 24 ? "-0.025em" : "-0.005em"),
              textTransform: r.caps ? "uppercase" : "none",
            }}>{r.sample}</div>
          </div>
        ))}
      </div>

      <DSRule>
        <b style={{ color: dsTok.amber, fontWeight: 500 }}>Pairing</b>
        <span style={{ color: dsTok.ink500 }}> · </span>
        Fraunces italic is reserved for human-toned moments: the pepper hint,
        error headlines, any line where the system speaks like a person. Never
        italicize body, never use Fraunces below 15px.
      </DSRule>
    </DSSubsection>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// FOUNDATIONS · SPACING
// ──────────────────────────────────────────────────────────────────────────

function DSSpacing() {
  const steps = [1,2,3,4,5,6,7,8];
  return (
    <DSSubsection eyebrow="Spacing · 8 steps" title="A 4-base scale."
      lede="Every gap, padding, and offset snaps to this. Steps below 4 hold text together; steps above 5 separate ideas.">

      <div style={{
        padding: `${dsSp(5)}px ${dsSp(4)}px`,
        background: dsTok.ink100, border: `1px solid ${dsTok.ink200}`,
        display: "flex", alignItems: "flex-end", gap: dsSp(4),
      }}>
        {steps.map(n => (
          <div key={n} style={{ textAlign: "center" }}>
            <div style={{
              width: 32, height: dsSp(n),
              background: dsTok.amber, opacity: 0.35 + n * 0.07,
            }}/>
            <div style={{
              marginTop: 8,
              fontFamily: dsFont.mono, fontSize: 11, color: dsTok.ink700,
            }}>{dsSp(n)}<span style={{ color: dsTok.ink500 }}>px</span></div>
            <div style={{
              fontFamily: dsFont.mono, fontSize: 9.5, color: dsTok.ink500,
              letterSpacing: "0.08em", textTransform: "uppercase",
            }}>sp/{n}</div>
          </div>
        ))}
      </div>

      <div style={{ marginTop: dsSp(4), display: "grid", gridTemplateColumns: "1fr 1fr", gap: dsSp(4) }}>
        <UsageList title="Within a unit" items={[
          "sp/1 (4px) — icon to label",
          "sp/2 (8px) — control internals",
          "sp/3 (12px) — caption to value",
        ]}/>
        <UsageList title="Between units" items={[
          "sp/5 (24px) — field to field",
          "sp/6 (36px) — section gap",
          "sp/7 (56px) — major break",
        ]}/>
      </div>
    </DSSubsection>
  );
}

function UsageList({ title, items }) {
  return (
    <div>
      <window.PCaption>{title}</window.PCaption>
      <ul style={{
        margin: `${dsSp(2)}px 0 0`, padding: 0, listStyle: "none",
        fontFamily: dsFont.mono, fontSize: 12, lineHeight: 1.9,
        color: dsTok.ink700,
      }}>
        {items.map(i => <li key={i}>{i}</li>)}
      </ul>
    </div>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// FOUNDATIONS · RADII
// ──────────────────────────────────────────────────────────────────────────

const radii = [
  { name: "hairline", value: 2, use: "Chips · tag pills" },
  { name: "control",  value: 4, use: "Buttons · inputs · cards" },
  { name: "soft",     value: 8, use: "Raised surfaces · modal" },
  { name: "pill",     value: 999, use: "Status indicators only" },
];

function DSRadii() {
  return (
    <DSSubsection eyebrow="Radii · 4 tokens" title="Quiet corners."
      lede="Most surfaces use 4px. Big radii feel friendly — Precision is sharp on purpose.">

      <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: dsSp(4) }}>
        {radii.map(r => (
          <div key={r.name} style={{
            border: `1px solid ${dsTok.ink200}`,
            background: dsTok.ink100,
            padding: dsSp(4),
          }}>
            <div style={{
              width: "100%", height: 72,
              background: dsTok.ink200,
              borderRadius: r.value,
              border: `1px solid ${dsTok.ink300}`,
            }}/>
            <div style={{
              marginTop: dsSp(3),
              fontFamily: dsFont.mono, fontSize: 12, color: dsTok.ink800,
            }}>radii/{r.name}</div>
            <div style={{
              fontFamily: dsFont.mono, fontSize: 10.5, color: dsTok.ink500,
            }}>{r.value === 999 ? "fully rounded" : r.value + "px"}</div>
            <div style={{
              marginTop: 6, fontSize: 12, color: dsTok.ink600,
            }}>{r.use}</div>
          </div>
        ))}
      </div>
    </DSSubsection>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// FOUNDATIONS · MOTION
// ──────────────────────────────────────────────────────────────────────────

const motions = [
  { name: "instant", dur: "0ms",   ease: "linear",
    use: "Toggling, focus rings — no fade allowed." },
  { name: "fast",    dur: "120ms", ease: "cubic-bezier(.2,0,.1,1)",
    use: "Hover, press, button color shifts." },
  { name: "settle",  dur: "200ms", ease: "cubic-bezier(.2,.6,.1,1)",
    use: "Copied flash, status pill transitions." },
  { name: "open",    dur: "260ms", ease: "cubic-bezier(.2,.8,.2,1)",
    use: "Popup enter, screen change, hint reveal." },
];

function DSMotion() {
  return (
    <DSSubsection eyebrow="Motion · 4 tokens" title="Quick, never showy."
      lede="If a transition lasts longer than 260ms, the user is waiting on the system instead of using it.">

      <div style={{ border: `1px solid ${dsTok.ink200}`, background: dsTok.ink100 }}>
        {motions.map((m, i) => (
          <div key={m.name} style={{
            display: "grid", gridTemplateColumns: "140px 100px 1fr",
            gap: dsSp(4), alignItems: "center",
            padding: `${dsSp(3)}px ${dsSp(4)}px`,
            borderBottom: i < motions.length - 1 ? `1px solid ${dsTok.ink200}` : "none",
          }}>
            <div>
              <div style={{
                fontFamily: dsFont.mono, fontSize: 13, color: dsTok.ink800,
              }}>motion/{m.name}</div>
              <div style={{
                fontFamily: dsFont.mono, fontSize: 10.5, color: dsTok.ink500,
              }}>{m.dur}</div>
            </div>
            <div style={{
              fontFamily: dsFont.mono, fontSize: 10.5, color: dsTok.ink500,
              letterSpacing: "0.02em",
            }}>{m.ease}</div>
            <div style={{ fontSize: 13, color: dsTok.ink700, lineHeight: 1.5 }}>{m.use}</div>
          </div>
        ))}
      </div>
    </DSSubsection>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// COMPONENTS · STATUS
// ──────────────────────────────────────────────────────────────────────────

function DSStatus() {
  const items = [
    ["ready",   dsTok.moss,    "Ready",            "Session live and recipe valid."],
    ["warn",    dsTok.amber,   "Needs attention",  "Recoverable mismatch or input issue."],
    ["blocked", dsTok.coral,   "Cannot generate",  "Hard failure — recipe parse, no cell, no signal."],
    ["locked",  dsTok.ink400,  "Locked",           "No session key. Master password required."],
  ];
  return (
    <DSSubsection eyebrow="Component · status pill" title="Four states, one shape."
      lede="The status pill sits in every popup header. Its color and label are the user's first piece of context.">
      <div style={{ border: `1px solid ${dsTok.ink200}`, background: dsTok.ink100 }}>
        {items.map(([k, c, l, d], i) => (
          <div key={k} style={{
            display: "grid", gridTemplateColumns: "200px 1fr",
            gap: dsSp(4), alignItems: "center",
            padding: `${dsSp(3)}px ${dsSp(4)}px`,
            borderBottom: i < items.length - 1 ? `1px solid ${dsTok.ink200}` : "none",
          }}>
            <div style={{ display: "flex", alignItems: "center", gap: dsSp(2) }}>
              <span style={{
                width: 7, height: 7, borderRadius: "50%", background: c,
                boxShadow: `0 0 8px ${c}66`,
              }}/>
              <span style={{ color: dsTok.ink900, fontSize: 13.5 }}>{l}</span>
            </div>
            <div style={{ fontSize: 13, color: dsTok.ink600, lineHeight: 1.5 }}>{d}</div>
          </div>
        ))}
      </div>
    </DSSubsection>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// COMPONENTS · CHIPS
// ──────────────────────────────────────────────────────────────────────────

function DSChips() {
  return (
    <DSSubsection eyebrow="Component · chip" title="Tiny labels.">
      <div style={{
        padding: dsSp(5),
        border: `1px solid ${dsTok.ink200}`, background: dsTok.ink100,
        display: "flex", gap: dsSp(3), flexWrap: "wrap", alignItems: "center",
      }}>
        <window.PChip tone="moss">verified</window.PChip>
        <window.PChip tone="amber">received</window.PChip>
        <window.PChip tone="coral">blocked</window.PChip>
        <window.PChip tone="ink">read-only</window.PChip>
        <window.PChip tone="amber">shared</window.PChip>
        <window.PChip tone="ink">v2.2 tag</window.PChip>
      </div>
    </DSSubsection>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// COMPONENTS · BUTTONS
// ──────────────────────────────────────────────────────────────────────────

function DSButtons() {
  return (
    <DSSubsection eyebrow="Component · button" title="Three weights."
      lede="Primary is amber. Secondary is amber outline. Ghost is text. One primary per screen — non-negotiable.">

      <div style={{
        padding: dsSp(5),
        border: `1px solid ${dsTok.ink200}`, background: dsTok.ink100,
        display: "flex", gap: dsSp(3), alignItems: "center", flexWrap: "wrap",
      }}>
        <button style={{
          background: dsTok.amber, color: "#1a0f02", border: 0,
          padding: "12px 22px",
          fontFamily: dsFont.body, fontSize: 14, fontWeight: 500,
          borderRadius: 4, cursor: "pointer", letterSpacing: "-0.005em",
        }}>Primary action →</button>

        <button style={{
          background: "transparent", color: dsTok.amber,
          border: `1px solid ${dsTok.amber}`,
          padding: "11px 22px",
          fontFamily: dsFont.body, fontSize: 14, fontWeight: 500,
          borderRadius: 4, cursor: "pointer", letterSpacing: "-0.005em",
        }}>Secondary</button>

        <button style={window.pGhostBtn()}>Ghost link →</button>

        <button disabled style={{
          background: dsTok.ink200, color: dsTok.ink400, border: 0,
          padding: "12px 22px",
          fontFamily: dsFont.body, fontSize: 14, fontWeight: 500,
          borderRadius: 4, cursor: "not-allowed",
        }}>Disabled</button>
      </div>
    </DSSubsection>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// COMPONENTS · PASSWORD SLAB
// ──────────────────────────────────────────────────────────────────────────

function DSPasswordSlab() {
  return (
    <DSSubsection eyebrow="Component · password slab" title="The hero of every screen."
      lede="A pair of hairlines, mono type at 32px, a Copy on the right. No background, no rounded box. Editorial like a pull-quote.">

      <div style={{
        padding: `${dsSp(5)}px ${dsSp(5)}px ${dsSp(4)}px`,
        border: `1px solid ${dsTok.ink200}`, background: dsTok.ink100,
      }}>
        <div style={{
          display: "flex", alignItems: "stretch",
          borderTop: `1px solid ${dsTok.ink300}`,
          borderBottom: `1px solid ${dsTok.ink300}`,
        }}>
          <div style={{
            padding: `${dsSp(4)}px 0`, flex: 1,
            fontFamily: dsFont.mono, fontSize: 32, color: dsTok.ink900,
            lineHeight: 1, display: "flex", alignItems: "center",
          }}>f7nQp2!vXm9kR$8B</div>
          <button style={{
            border: 0, background: "transparent",
            color: dsTok.ink800, padding: `0 ${dsSp(4)}px`,
            fontFamily: dsFont.body, fontSize: 13, fontWeight: 500,
            cursor: "pointer", display: "flex", alignItems: "center", gap: 8,
          }}>
            <window.PCopyIcon/> Copy
          </button>
        </div>

        <div style={{
          marginTop: dsSp(3),
          display: "flex", gap: dsSp(3),
          fontFamily: dsFont.mono, fontSize: 10.5,
          letterSpacing: "0.14em", textTransform: "uppercase",
          color: dsTok.ink500,
        }}>
          <span>16 chars</span>
          <span style={{ color: dsTok.ink300 }}>·</span>
          <span>~96 bits</span>
          <span style={{ color: dsTok.ink300 }}>·</span>
          <span style={{ color: dsTok.moss, display: "flex", alignItems: "center", gap: 4 }}>
            <window.PCheckTiny color={dsTok.moss}/> verified
          </span>
        </div>
      </div>

      <DSAnatomy items={[
        ["1", "Top hairline", "1px ink/300, becomes amber on Copied state."],
        ["2", "Password line", "Geist Mono 32/400, full-bleed left."],
        ["3", "Copy control", "Ghost button, ink/800. Amber + check on Copied."],
        ["4", "Bottom hairline", "1px ink/300."],
        ["5", "Meta strip", "Caption mono. 'Verified' moss when v2.2 tag matches sheet."],
      ]}/>
    </DSSubsection>
  );
}

function DSAnatomy({ items }) {
  return (
    <div style={{
      marginTop: dsSp(4),
      border: `1px solid ${dsTok.ink200}`,
    }}>
      {items.map(([n, t, d], i) => (
        <div key={n} style={{
          display: "grid", gridTemplateColumns: "40px 160px 1fr",
          gap: dsSp(3), alignItems: "baseline",
          padding: `${dsSp(2)}px ${dsSp(4)}px`,
          borderBottom: i < items.length - 1 ? `1px solid ${dsTok.ink200}` : "none",
          background: dsTok.ink050,
        }}>
          <div style={{
            fontFamily: dsFont.mono, fontSize: 11, color: dsTok.amber,
            letterSpacing: "0.04em",
          }}>{n}</div>
          <div style={{ fontSize: 13, color: dsTok.ink800 }}>{t}</div>
          <div style={{ fontSize: 12.5, color: dsTok.ink600, lineHeight: 1.5 }}>{d}</div>
        </div>
      ))}
    </div>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// COMPONENTS · FIELD
// ──────────────────────────────────────────────────────────────────────────

function DSField() {
  return (
    <DSSubsection eyebrow="Component · field" title="Caption + value."
      lede="The only structural pattern below the password. A caption above, a value below — single line or stacked. Right-side metadata when relevant.">

      <div style={{
        padding: `${dsSp(5)}px`,
        border: `1px solid ${dsTok.ink200}`, background: dsTok.ink100,
        display: "grid", gridTemplateColumns: "1fr 1fr", gap: dsSp(5),
      }}>
        <div>
          <window.PCaption>Profile</window.PCaption>
          <div style={{
            marginTop: dsSp(2),
            fontSize: 20, color: dsTok.ink900, letterSpacing: "-0.015em",
          }}>Default</div>
        </div>
        <div style={{ textAlign: "right" }}>
          <window.PCaption>Bound to sheet</window.PCaption>
          <div style={{
            marginTop: dsSp(2),
            fontFamily: dsFont.mono, fontSize: 11.5, color: dsTok.ink700,
            letterSpacing: "0.04em",
          }}>1BxCdefGh...aWxYz</div>
        </div>
      </div>
    </DSSubsection>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// PATTERNS · GENERATED ANATOMY
// ──────────────────────────────────────────────────────────────────────────

function DSGenAnatomy() {
  return (
    <DSSubsection eyebrow="Pattern · screen layout" title="Generated screen anatomy."
      lede="How the components stack to make one of the most-touched screens in the product." gridAlign="start">

      <div style={{ display: "grid", gridTemplateColumns: "300px 1fr", gap: dsSp(4) }}>
        <div style={{
          border: `1px solid ${dsTok.ink200}`,
          background: dsTok.ink100, padding: dsSp(3),
        }}>
          <AnatomyBlock label="A" name="Header" detail="Wordmark + status pill" h={40}/>
          <AnatomyBlock label="B" name="Hairline" detail="1px ink/200" h={6}/>
          <AnatomyBlock label="C" name="Profile field" detail="Name + binding" h={56}/>
          <AnatomyBlock label="D" name="Divider" detail="" h={6}/>
          <AnatomyBlock label="E" name="Password slab" detail="The hero" h={120} hero/>
          <AnatomyBlock label="F" name="Hint (optional)" detail="Italic Fraunces" h={28}/>
          <AnatomyBlock label="G" name="Primary CTA" detail="Build recipe" h={52} accent/>
          <AnatomyBlock label="H" name="Footer" detail="Back · Lock" h={32}/>
        </div>

        <div>
          <AnatomyRow letter="A" title="Header" body="Status reflects the system's commitment: Ready (moss), Warn (amber), Blocked (coral). The wordmark anchors brand without competing."/>
          <AnatomyRow letter="C" title="Profile field" body="Always shows. For shared profiles, the bound-sheet ID lives on the right — it's the v2.2 cryptographic anchor and deserves the real estate."/>
          <AnatomyRow letter="E" title="Password slab" body="Sized to be the single largest element. Mono 32px. If the user only looks for one second, they should see exactly this."/>
          <AnatomyRow letter="F" title="Hint" body="Optional — appears for pepper hint or as part of error treatment. Italic Fraunces gives it a human voice distinct from system labels."/>
          <AnatomyRow letter="G" title="Primary CTA" body="One per screen. Build Recipe forward-routes to the composer. Even in error states it remains primary — most fixes lead through it."/>
        </div>
      </div>
    </DSSubsection>
  );
}

function AnatomyBlock({ label, name, detail, h, hero, accent }) {
  return (
    <div style={{
      display: "flex", alignItems: "center", gap: dsSp(2),
      marginBottom: 4,
    }}>
      <div style={{
        width: 22,
        fontFamily: dsFont.mono, fontSize: 10, color: dsTok.amber,
        letterSpacing: "0.08em",
      }}>{label}</div>
      <div style={{
        flex: 1, height: h,
        background: accent ? dsTok.amber + "55" : (hero ? dsTok.ink200 : dsTok.ink150),
        border: `1px solid ${accent ? dsTok.amber : dsTok.ink300}`,
        borderRadius: 2,
        padding: "6px 10px",
        display: "flex", flexDirection: "column", justifyContent: "center",
      }}>
        <div style={{ fontSize: 11.5, color: dsTok.ink800 }}>{name}</div>
        {detail && <div style={{
          fontFamily: dsFont.mono, fontSize: 9.5, color: dsTok.ink500,
          letterSpacing: "0.04em", marginTop: 2,
        }}>{detail}</div>}
      </div>
    </div>
  );
}

function AnatomyRow({ letter, title, body }) {
  return (
    <div style={{
      display: "grid", gridTemplateColumns: "32px 1fr",
      gap: dsSp(3), padding: `${dsSp(3)}px 0`,
      borderBottom: `1px solid ${dsTok.ink200}`,
    }}>
      <div style={{
        fontFamily: dsFont.mono, fontSize: 11, color: dsTok.amber,
        letterSpacing: "0.08em", paddingTop: 2,
      }}>{letter}</div>
      <div>
        <div style={{ fontSize: 14, color: dsTok.ink900, letterSpacing: "-0.005em", marginBottom: 4 }}>{title}</div>
        <div style={{ fontSize: 13, color: dsTok.ink600, lineHeight: 1.55 }}>{body}</div>
      </div>
    </div>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// PATTERNS · ERROR GRID
// ──────────────────────────────────────────────────────────────────────────

const errorPatterns = [
  { name: "Empty cell",
    tone: "coral",
    voice: "Select a cell with a recipe.",
    body: "Hard block. User picked nothing — no parse, no signal. Coral border, refresh action.",
    when: "content.js returns no value for the active cell." },
  { name: "Reload required",
    tone: "coral",
    voice: "Reload the tab to reconnect.",
    body: "Hard block. Service worker / content script dropped. The user needs a one-action fix.",
    when: "Extension context invalidated · Could not establish connection." },
  { name: "Profile mismatch",
    tone: "amber",
    voice: "This recipe is bound to another sheet.",
    body: "Recoverable warn. The v2.2 verification tag failed for this sheet — switch sheets, don't break.",
    when: "RecipeProfileMismatchError from generate_password.js." },
];

function DSErrorGrid() {
  return (
    <DSSubsection eyebrow="Pattern · error treatment" title="Three error tones, in order of severity."
      lede="An error is also content. It has a voice, a tone color, and an action — never just a red line of text.">

      <div style={{
        display: "grid", gridTemplateColumns: "repeat(3, 1fr)",
        gap: dsSp(3),
      }}>
        {errorPatterns.map(p => {
          const c = p.tone === "amber" ? dsTok.amber : dsTok.coral;
          const bg = p.tone === "amber" ? dsTok.amberBg : dsTok.coralBg;
          return (
            <div key={p.name} style={{
              border: `1px solid ${dsTok.ink200}`,
              borderTop: `2px solid ${c}`,
              background: `linear-gradient(180deg, ${bg}44 0%, ${dsTok.ink100} 100%)`,
              padding: dsSp(4),
              display: "flex", flexDirection: "column", gap: dsSp(3),
              minHeight: 220,
            }}>
              <window.PChip tone={p.tone}>{p.name}</window.PChip>
              <div style={{
                fontFamily: dsFont.display, fontStyle: "italic",
                fontSize: 22, fontWeight: 400, lineHeight: 1.2,
                color: dsTok.ink900, letterSpacing: "-0.015em",
              }}>{p.voice}</div>
              <div style={{
                fontSize: 13, color: dsTok.ink700, lineHeight: 1.55,
              }}>{p.body}</div>
              <div style={{ flex: 1 }}/>
              <div style={{
                paddingTop: dsSp(3),
                borderTop: `1px dashed ${dsTok.ink300}`,
                fontFamily: dsFont.mono, fontSize: 10.5,
                color: dsTok.ink500, letterSpacing: "0.04em",
                lineHeight: 1.5,
              }}>
                <span style={{ color: dsTok.ink600 }}>fires when</span><br/>
                {p.when}
              </div>
            </div>
          );
        })}
      </div>
    </DSSubsection>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// FOOTER
// ──────────────────────────────────────────────────────────────────────────

function DSFooter() {
  return (
    <div style={{
      maxWidth: 1040, margin: "0 auto",
      padding: `${dsSp(6)}px ${dsSp(6)}px ${dsSp(7)}px`,
      display: "flex", alignItems: "center", justifyContent: "space-between",
      borderTop: `1px solid ${dsTok.ink200}`,
    }}>
      <div style={{ display: "flex", alignItems: "center", gap: dsSp(4) }}>
        <window.PWordmark size={18}/>
        <span style={{
          fontFamily: dsFont.mono, fontSize: 10.5, color: dsTok.ink500,
          letterSpacing: "0.18em", textTransform: "uppercase",
        }}>Precision · v0.1</span>
      </div>
      <div style={{
        fontFamily: dsFont.mono, fontSize: 11, color: dsTok.ink500,
        letterSpacing: "0.04em",
      }}>2026-05-22 · for PassMan v2.2</div>
    </div>
  );
}

Object.assign(window, { DesignSystemDoc });
