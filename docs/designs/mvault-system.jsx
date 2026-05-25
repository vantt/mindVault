// mvault-system.jsx — Shared tokens, primitives, frame chrome for PassChef popup screens.
// Sizing target: 400 × 600 popup. Derived from Precision direction.

// ──────────────────────────────────────────────────────────────────────────
// TOKENS — warm ink ramp + amber/moss/coral accents (Precision DNA)
// ──────────────────────────────────────────────────────────────────────────

const M_TOK = {
  // ink ramp — warm desaturated grays
  ink000: "#0a0a0c",
  ink050: "#0e0e10",   // page base
  ink100: "#15151a",   // surface
  ink150: "#1c1c22",   // raised
  ink200: "#232329",   // hairline strong
  ink300: "#2f2f36",   // border strong
  ink400: "#44413c",   // disabled
  ink500: "#6a655d",   // tertiary text
  ink600: "#8a857a",   // secondary text
  ink700: "#b4ad9f",
  ink800: "#d8d2c3",
  ink900: "#f0ece2",   // display, warm off-white

  // semantic accents
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

const M_FONT = {
  display: "'Fraunces', serif",
  body:    "'Geist', system-ui, sans-serif",
  mono:    "'Geist Mono', ui-monospace, monospace",
};

// 4-base spacing scale
const M_SP = (n) => [0, 4, 8, 12, 16, 20, 24, 32, 40, 56][n];

// shared sample data
const M_PWD = "f7nQp2!vXm9kR$8B";
const M_SHEET_ID = "1BxCdefGh_aWxYz3";
const M_SHEET_NAME = "Banking — 2026";

// ──────────────────────────────────────────────────────────────────────────
// FRAME — full-bleed popup container with subtle vignette
// ──────────────────────────────────────────────────────────────────────────

function MFrame({ children, bg = M_TOK.ink050, glow = "amber" }) {
  const glowColor = glow === "coral" ? "rgba(224,116,108,0.05)"
                  : glow === "moss"  ? "rgba(132,181,119,0.04)"
                  : "rgba(232,163,65,0.05)";
  return (
    <div style={{
      width: "100%", height: "100%",
      background: bg, color: M_TOK.ink900,
      fontFamily: M_FONT.body,
      display: "flex", flexDirection: "column",
      position: "relative", overflow: "hidden",
    }}>
      <div style={{
        position: "absolute", inset: 0, pointerEvents: "none",
        background: `radial-gradient(ellipse at 75% -10%, ${glowColor}, transparent 55%)`,
      }} />
      {children}
    </div>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// HEADER — compact for 400px width
// ──────────────────────────────────────────────────────────────────────────

function MHeader({ status = "ready", customLabel = null, hideStatusText = false }) {
  const map = {
    ready:   { dot: M_TOK.moss,   label: "Ready",  text: M_TOK.ink800 },
    locked:  { dot: M_TOK.ink400, label: "Locked", text: M_TOK.ink600 },
    setup:   { dot: M_TOK.coral,  label: "Setup",  text: M_TOK.coral },
    warn:    { dot: M_TOK.amber,  label: "Needs attention", text: M_TOK.amber },
    blocked: { dot: M_TOK.coral,  label: "Cannot generate", text: M_TOK.coral },
    busy:    { dot: M_TOK.amber,  label: "Unlocking…", text: M_TOK.amber },
  };
  const s = map[status];
  return (
    <div style={{
      display: "flex", alignItems: "center", justifyContent: "space-between",
      padding: `${M_SP(5)}px ${M_SP(5)}px 0`,
      position: "relative", zIndex: 1,
    }}>
      <MWordmark />
      <div style={{ display: "flex", alignItems: "center", gap: M_SP(3) }}>
        <div style={{ display: "flex", alignItems: "center", gap: M_SP(2) }}>
          <span style={{
            width: 7, height: 7, borderRadius: "50%",
            background: s.dot,
            boxShadow: `0 0 7px ${s.dot}66`,
          }} />
          {!hideStatusText && (
            <span style={{
              color: s.text, fontSize: 11.5,
              letterSpacing: "0.01em",
            }}>{customLabel || s.label}</span>
          )}
        </div>
        <MIconButton title="Settings">
          <svg width="14" height="14" viewBox="0 0 16 16" fill="none">
            <circle cx="8" cy="8" r="2" stroke="currentColor" strokeWidth="1.3"/>
            <circle cx="8" cy="8" r="6" stroke="currentColor" strokeWidth="1.3"/>
            <path d="M8 1v2M8 13v2M1 8h2M13 8h2" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>
          </svg>
        </MIconButton>
      </div>
    </div>
  );
}

function MWordmark({ size = 22 }) {
  return (
    <div style={{
      fontFamily: M_FONT.display, fontSize: size, fontWeight: 400,
      letterSpacing: "-0.02em",
      lineHeight: 1, display: "flex", alignItems: "baseline", gap: 5,
    }}>
      <span style={{ color: M_TOK.ink600 }}>mind</span>
      <span style={{
        color: M_TOK.ink900,
        fontStyle: "italic", letterSpacing: "-0.025em",
      }}>Vault</span>
      <span style={{
        marginLeft: 6, width: 4, height: 4, borderRadius: "50%",
        background: M_TOK.amber, alignSelf: "center",
        boxShadow: `0 0 6px ${M_TOK.amber}aa`,
      }} />
    </div>
  );
}

function MIconButton({ children, title }) {
  return (
    <button title={title} style={{
      width: 26, height: 26, border: 0, background: "transparent",
      color: M_TOK.ink600, cursor: "pointer",
      display: "flex", alignItems: "center", justifyContent: "center",
      borderRadius: 4,
    }}>{children}</button>
  );
}

// hairline under header
function MHeaderRule() {
  return <div style={{
    height: 1, background: M_TOK.ink200,
    margin: `${M_SP(4)}px ${M_SP(5)}px 0`,
  }} />;
}

// ──────────────────────────────────────────────────────────────────────────
// TYPOGRAPHIC HELPERS
// ──────────────────────────────────────────────────────────────────────────

function MCaption({ children, accent = false, style = {} }) {
  return (
    <div style={{
      fontFamily: M_FONT.mono, fontSize: 9.5, fontWeight: 500,
      letterSpacing: "0.22em", textTransform: "uppercase",
      color: accent ? M_TOK.amber : M_TOK.ink500,
      ...style,
    }}>{children}</div>
  );
}

function MDisplay({ children, size = 22, italic = false, style = {} }) {
  return (
    <div style={{
      fontFamily: M_FONT.display, fontSize: size, fontWeight: 400,
      fontStyle: italic ? "italic" : "normal",
      letterSpacing: "-0.02em", lineHeight: 1.15,
      color: M_TOK.ink900,
      ...style,
    }}>{children}</div>
  );
}

function MBody({ children, muted = false, style = {} }) {
  return (
    <div style={{
      fontSize: 13, lineHeight: 1.5,
      color: muted ? M_TOK.ink600 : M_TOK.ink800,
      ...style,
    }}>{children}</div>
  );
}

function MDivider({ kind = "solid", style = {} }) {
  return <div style={{
    height: 1,
    background: kind === "dashed" ? "none" : M_TOK.ink200,
    borderTop: kind === "dashed" ? `1px dashed ${M_TOK.ink300}` : "none",
    ...style,
  }} />;
}

function MChip({ children, tone = "ink", style = {} }) {
  const palette = {
    ink:    { fg: M_TOK.ink700, bg: "transparent",            bd: M_TOK.ink300 },
    amber:  { fg: M_TOK.amber,  bg: M_TOK.amberBg + "80",     bd: M_TOK.amber + "55" },
    moss:   { fg: M_TOK.moss,   bg: M_TOK.mossBg + "80",      bd: M_TOK.moss + "55" },
    coral:  { fg: M_TOK.coral,  bg: M_TOK.coralBg + "80",     bd: M_TOK.coral + "55" },
  }[tone];
  return (
    <span style={{
      display: "inline-flex", alignItems: "center", gap: 5,
      padding: "2px 7px",
      fontFamily: M_FONT.mono, fontSize: 9.5, fontWeight: 500,
      letterSpacing: "0.14em", textTransform: "uppercase",
      color: palette.fg, background: palette.bg,
      border: `1px solid ${palette.bd}`, borderRadius: 3,
      ...style,
    }}>{children}</span>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// BUTTONS
// ──────────────────────────────────────────────────────────────────────────

function MPrimaryButton({ children, hover = false, leadingArrow = false, trailingArrow = true, disabled = false, style = {} }) {
  return (
    <button disabled={disabled} style={{
      display: "flex", alignItems: "center", justifyContent: "space-between",
      width: "100%",
      padding: `${M_SP(3)}px ${M_SP(4)}px`,
      background: disabled ? M_TOK.ink200 : (hover ? M_TOK.amberHi : M_TOK.amber),
      color: disabled ? M_TOK.ink500 : "#1a0f02",
      border: 0,
      fontFamily: M_FONT.body, fontSize: 14, fontWeight: 500,
      cursor: disabled ? "default" : "pointer",
      borderRadius: 4,
      letterSpacing: "-0.005em",
      boxShadow: hover && !disabled
        ? `0 8px 22px ${M_TOK.amber}33`
        : "none",
      transition: "all 120ms ease",
      ...style,
    }}>
      <span style={{ display: "flex", alignItems: "center", gap: 6 }}>
        {leadingArrow && <span style={{ fontSize: 16 }}>→</span>}
        <span>{children}</span>
      </span>
      {trailingArrow && <span style={{ fontSize: 16, fontWeight: 400 }}>→</span>}
    </button>
  );
}

function MGhostButton({ children, style = {} }) {
  return (
    <button style={{
      border: 0, background: "transparent",
      color: M_TOK.ink600, cursor: "pointer",
      fontFamily: M_FONT.body, fontSize: 12.5, fontWeight: 400,
      padding: "4px 0", letterSpacing: "-0.005em",
      ...style,
    }}>{children}</button>
  );
}

function MSecondaryButton({ children, tone = "ink", style = {} }) {
  const color = tone === "amber" ? M_TOK.amber : M_TOK.ink800;
  const border = tone === "amber" ? M_TOK.amber + "55" : M_TOK.ink300;
  return (
    <button style={{
      background: "transparent", color,
      border: `1px solid ${border}`,
      padding: `${M_SP(2) + 2}px ${M_SP(4)}px`,
      fontFamily: M_FONT.body, fontSize: 13, fontWeight: 500,
      borderRadius: 4, cursor: "pointer",
      ...style,
    }}>{children}</button>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// SVG ICONS
// ──────────────────────────────────────────────────────────────────────────

function MCheckTiny({ color = M_TOK.moss, size = 11 }) {
  return (
    <svg width={size} height={size} viewBox="0 0 12 12" fill="none">
      <path d="M2 6.5L4.8 9.2L10 3.5" stroke={color} strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
  );
}

function MCopyIcon({ color = "currentColor", size = 13 }) {
  return (
    <svg width={size} height={size} viewBox="0 0 14 14" fill="none">
      <rect x="3.5" y="3.5" width="7" height="7" rx="1" stroke={color} strokeWidth="1.3"/>
      <path d="M2 9V2.5C2 2.22 2.22 2 2.5 2H9" stroke={color} strokeWidth="1.3" strokeLinecap="round"/>
    </svg>
  );
}

function MEyeIcon({ off = false, size = 14 }) {
  return (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none">
      <path d="M1.5 8C1.5 8 4 3.5 8 3.5C12 3.5 14.5 8 14.5 8C14.5 8 12 12.5 8 12.5C4 12.5 1.5 8 1.5 8Z"
            stroke="currentColor" strokeWidth="1.3"/>
      <circle cx="8" cy="8" r="2" stroke="currentColor" strokeWidth="1.3"/>
      {off && <path d="M2 14L14 2" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>}
    </svg>
  );
}

function MRefreshIcon({ size = 13 }) {
  return (
    <svg width={size} height={size} viewBox="0 0 14 14" fill="none">
      <path d="M12 7C12 9.76 9.76 12 7 12C5.34 12 3.88 11.19 3 9.94M2 7C2 4.24 4.24 2 7 2C8.66 2 10.12 2.81 11 4.06"
            stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>
      <path d="M2.5 9.5V12H5M11.5 4.5V2H9" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
  );
}

function MLockIcon({ size = 12 }) {
  return (
    <svg width={size} height={size} viewBox="0 0 12 12" fill="none">
      <rect x="2.5" y="5.5" width="7" height="5" rx="1" stroke="currentColor" strokeWidth="1.2"/>
      <path d="M4 5.5V3.5C4 2.4 4.9 1.5 6 1.5C7.1 1.5 8 2.4 8 3.5V5.5" stroke="currentColor" strokeWidth="1.2"/>
    </svg>
  );
}

// expose to other JSX files
Object.assign(window, {
  M_TOK, M_FONT, M_SP, M_PWD, M_SHEET_ID, M_SHEET_NAME,
  MFrame, MHeader, MHeaderRule, MWordmark, MIconButton,
  MCaption, MDisplay, MBody, MDivider, MChip,
  MPrimaryButton, MSecondaryButton, MGhostButton,
  MCheckTiny, MCopyIcon, MEyeIcon, MRefreshIcon, MLockIcon,
});
