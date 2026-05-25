// options-system.jsx — Page chrome + form primitives for PassChef Options page.
// Uses tokens from mvault-system.jsx (M_TOK / M_FONT / M_SP).
//
// Sizing: options page is fullpage (browser tab). Spec calls for 600px content
// container. We render artboards at 760×820 (some taller) with the content
// column centered.

// ── Page shell ───────────────────────────────────────────────────────────

function OPage({ children, glow = "amber", height }) {
  const glowColor = glow === "coral" ? "rgba(224,116,108,0.05)"
                  : glow === "moss"  ? "rgba(132,181,119,0.04)"
                  : "rgba(232,163,65,0.04)";
  return (
    <div style={{
      width: "100%", minHeight: "100%", height: height || "100%",
      background: M_TOK.ink050, color: M_TOK.ink900,
      fontFamily: M_FONT.body,
      position: "relative", overflow: "hidden",
      display: "flex", flexDirection: "column",
    }}>
      <div style={{
        position: "absolute", inset: 0, pointerEvents: "none",
        background: `radial-gradient(ellipse at 85% -20%, ${glowColor}, transparent 50%)`,
      }} />
      {children}
    </div>
  );
}

function OContainer({ children, width = 600, style = {} }) {
  return (
    <div style={{
      width: "100%", maxWidth: width, margin: "0 auto",
      padding: `0 ${M_SP(5)}px`, position: "relative", zIndex: 1,
      ...style,
    }}>{children}</div>
  );
}

// ── Top app bar ──────────────────────────────────────────────────────────

function OAppBar({ profile = null }) {
  return (
    <div style={{
      borderBottom: `1px solid ${M_TOK.ink200}`,
      position: "relative", zIndex: 1,
      background: M_TOK.ink050,
    }}>
      <OContainer width={760} style={{ padding: `${M_SP(4)}px ${M_SP(5)}px` }}>
        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
          <div style={{ display: "flex", alignItems: "baseline", gap: M_SP(3) }}>
            <MWordmark size={24} />
            <span style={{
              fontFamily: M_FONT.display, fontStyle: "italic",
              fontSize: 14, color: M_TOK.ink600, letterSpacing: "-0.005em",
            }}>· Options</span>
          </div>
          <div style={{ display: "flex", alignItems: "center", gap: M_SP(3) }}>
            {profile && (
              <span style={{
                fontFamily: M_FONT.mono, fontSize: 10.5,
                letterSpacing: "0.16em", textTransform: "uppercase",
                color: M_TOK.ink600,
              }}>
                <span style={{
                  display: "inline-block", width: 6, height: 6, borderRadius: "50%",
                  background: M_TOK.moss, marginRight: 7,
                  boxShadow: `0 0 6px ${M_TOK.moss}66`,
                }} />
                Unlocked
              </span>
            )}
            <button style={{
              border: `1px solid ${M_TOK.ink300}`,
              background: "transparent", color: M_TOK.ink700,
              padding: "5px 10px", borderRadius: 4,
              fontFamily: M_FONT.body, fontSize: 11.5,
              letterSpacing: "0.04em",
              display: "inline-flex", alignItems: "center", gap: 5, cursor: "pointer",
            }}>
              <MLockIcon size={11}/> Lock
            </button>
          </div>
        </div>
      </OContainer>
    </div>
  );
}

// ── Tab bar ──────────────────────────────────────────────────────────────

function OTabBar({ active = "secrets" }) {
  const tabs = [
    { id: "secrets",  label: "Secrets" },
    { id: "profiles", label: "Profiles" },
    { id: "settings", label: "Settings" },
  ];
  return (
    <div style={{
      borderBottom: `1px solid ${M_TOK.ink200}`,
      position: "relative", zIndex: 1,
    }}>
      <OContainer width={760} style={{ padding: `0 ${M_SP(5)}px` }}>
        <div style={{ display: "flex", gap: 4, marginBottom: -1 }}>
          {tabs.map(t => {
            const isActive = t.id === active;
            return (
              <div key={t.id} style={{
                padding: "11px 18px",
                color: isActive ? M_TOK.ink900 : M_TOK.ink500,
                borderBottom: `2px solid ${isActive ? M_TOK.amber : "transparent"}`,
                fontFamily: M_FONT.body, fontSize: 13, fontWeight: 500,
                cursor: "pointer", letterSpacing: "-0.005em",
              }}>{t.label}</div>
            );
          })}
        </div>
      </OContainer>
    </div>
  );
}

// ── Page title block ─────────────────────────────────────────────────────

function OPageTitle({ eyebrow, title, sub, italic = true, right = null }) {
  return (
    <div style={{
      display: "flex", justifyContent: "space-between",
      alignItems: "flex-end", gap: M_SP(4),
      paddingBottom: M_SP(4),
    }}>
      <div>
        {eyebrow && <MCaption style={{ marginBottom: 8 }}>{eyebrow}</MCaption>}
        <div style={{
          fontFamily: M_FONT.display, fontSize: 28, fontWeight: 400,
          letterSpacing: "-0.025em", lineHeight: 1.1,
          color: M_TOK.ink900,
        }}>
          {italic ? <span style={{ fontStyle: "italic" }}>{title}</span> : title}
        </div>
        {sub && (
          <div style={{
            color: M_TOK.ink600, fontSize: 13, marginTop: 8,
            maxWidth: 460, lineHeight: 1.5,
          }}>{sub}</div>
        )}
      </div>
      {right}
    </div>
  );
}

// ── Card ─────────────────────────────────────────────────────────────────

function OCard({ children, accent = null, style = {}, padding = 5 }) {
  return (
    <div style={{
      background: M_TOK.ink100,
      border: `1px solid ${M_TOK.ink200}`,
      borderRadius: 6,
      padding: M_SP(padding),
      position: "relative",
      ...(accent ? { borderLeft: `3px solid ${accent}` } : {}),
      ...style,
    }}>
      {children}
    </div>
  );
}

function OSectionHeader({ children, action = null, style = {} }) {
  return (
    <div style={{
      display: "flex", justifyContent: "space-between", alignItems: "center",
      marginBottom: M_SP(3),
      ...style,
    }}>
      <MCaption style={{ color: M_TOK.ink600 }}>{children}</MCaption>
      {action}
    </div>
  );
}

// ── Form primitives ──────────────────────────────────────────────────────

function OLabel({ children, optional = false }) {
  return (
    <div style={{
      fontSize: 12, color: M_TOK.ink700, fontWeight: 500,
      letterSpacing: "-0.005em",
      display: "flex", alignItems: "baseline", gap: 8,
    }}>
      <span>{children}</span>
      {optional && (
        <span style={{
          fontFamily: M_FONT.mono, fontSize: 9, letterSpacing: "0.18em",
          textTransform: "uppercase", color: M_TOK.ink500,
        }}>optional</span>
      )}
    </div>
  );
}

function OInput({ value = "", placeholder = "", type = "text", invalid = false,
                 trailing = null, mono = false, readOnly = false, style = {} }) {
  return (
    <div style={{
      display: "flex", alignItems: "center",
      background: readOnly ? M_TOK.ink150 : M_TOK.ink100,
      border: `1px solid ${invalid ? M_TOK.coral + "88" : M_TOK.ink300}`,
      borderRadius: 4,
      padding: `0 ${M_SP(3)}px`,
      ...style,
    }}>
      <div style={{
        flex: 1,
        padding: `${M_SP(2) + 2}px 0`,
        fontFamily: mono ? M_FONT.mono : M_FONT.body,
        fontSize: mono ? 12.5 : 13,
        color: value ? M_TOK.ink900 : M_TOK.ink500,
        letterSpacing: type === "password" ? "0.2em" : "-0.005em",
        whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis",
      }}>
        {value || placeholder}
      </div>
      {trailing}
    </div>
  );
}

function OPwdInput({ value = "", placeholder = "", visible = false, style = {} }) {
  const dots = "•".repeat(Math.min(value.length, 16));
  return (
    <OInput
      value={visible ? value : (value ? dots : "")}
      placeholder={placeholder}
      mono={visible}
      trailing={
        <button style={{
          border: 0, background: "transparent", color: M_TOK.ink600,
          cursor: "pointer", padding: 4, display: "flex",
        }}>
          <MEyeIcon off={visible} size={14}/>
        </button>
      }
      style={style}
    />
  );
}

function OCheckbox({ checked = false, label, sub = null }) {
  return (
    <label style={{
      display: "flex", alignItems: "flex-start", gap: 10,
      cursor: "pointer",
    }}>
      <span style={{
        width: 14, height: 14, marginTop: 2, flexShrink: 0,
        border: `1px solid ${checked ? M_TOK.amber : M_TOK.ink300}`,
        background: checked ? M_TOK.amber : "transparent",
        borderRadius: 3,
        display: "flex", alignItems: "center", justifyContent: "center",
      }}>
        {checked && (
          <svg width="10" height="10" viewBox="0 0 10 10" fill="none">
            <path d="M2 5.5L4 7.5L8 3" stroke="#1a0f02" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        )}
      </span>
      <div style={{ flex: 1 }}>
        <div style={{ fontSize: 13, color: M_TOK.ink800, letterSpacing: "-0.005em" }}>{label}</div>
        {sub && <div style={{ fontSize: 11.5, color: M_TOK.ink600, marginTop: 2, lineHeight: 1.45 }}>{sub}</div>}
      </div>
    </label>
  );
}

function OSelect({ value, placeholder = "Select…", style = {} }) {
  return (
    <div style={{
      display: "flex", alignItems: "center", justifyContent: "space-between",
      background: M_TOK.ink100,
      border: `1px solid ${M_TOK.ink300}`,
      borderRadius: 4,
      padding: `${M_SP(2) + 2}px ${M_SP(3)}px`,
      fontSize: 13, color: value ? M_TOK.ink900 : M_TOK.ink500,
      cursor: "pointer",
      ...style,
    }}>
      <span>{value || placeholder}</span>
      <span style={{ color: M_TOK.ink600, fontSize: 10 }}>▾</span>
    </div>
  );
}

// ── Button variants for options ──────────────────────────────────────────

function OBtn({ kind = "ghost", children, leading = null, danger = false, tone, style = {} }) {
  const palettes = {
    primary: { bg: M_TOK.amber, fg: "#1a0f02", bd: "transparent" },
    secondary: { bg: "transparent", fg: M_TOK.ink800, bd: M_TOK.ink300 },
    ghost: { bg: "transparent", fg: M_TOK.ink700, bd: "transparent" },
    danger: { bg: "transparent", fg: M_TOK.coral, bd: M_TOK.coral + "55" },
  };
  const p = danger ? palettes.danger : palettes[kind];
  return (
    <button style={{
      background: p.bg, color: p.fg,
      border: kind === "ghost" ? 0 : `1px solid ${p.bd}`,
      padding: kind === "ghost" ? "5px 8px" : `${M_SP(2) + 1}px ${M_SP(3) + 2}px`,
      borderRadius: 4,
      fontFamily: M_FONT.body, fontSize: 12.5,
      fontWeight: kind === "primary" ? 500 : 400,
      letterSpacing: "-0.005em",
      cursor: "pointer",
      display: "inline-flex", alignItems: "center", gap: 6,
      ...style,
    }}>
      {leading && <span style={{ display: "inline-flex" }}>{leading}</span>}
      {children}
    </button>
  );
}

// ── Toast / status row ───────────────────────────────────────────────────

function OToast({ tone = "moss", children, style = {} }) {
  const bg = tone === "moss"  ? M_TOK.mossBg
           : tone === "coral" ? M_TOK.coralBg
           : tone === "amber" ? M_TOK.amberBg
                              : M_TOK.ink150;
  const fg = tone === "moss"  ? M_TOK.moss
           : tone === "coral" ? M_TOK.coral
           : tone === "amber" ? M_TOK.amber
                              : M_TOK.ink700;
  return (
    <div style={{
      position: "absolute", bottom: M_SP(5), left: "50%",
      transform: "translateX(-50%)",
      background: bg + "ee", color: fg,
      border: `1px solid ${fg}55`,
      padding: `${M_SP(2) + 2}px ${M_SP(4)}px`,
      borderRadius: 4,
      fontSize: 12.5, letterSpacing: "-0.005em",
      display: "inline-flex", alignItems: "center", gap: 8,
      boxShadow: "0 8px 24px rgba(0,0,0,0.4)",
      zIndex: 50,
      ...style,
    }}>{children}</div>
  );
}

// ── Modal scaffold ───────────────────────────────────────────────────────

function OModalShell({ children, title, onClose = true, sub = null, footer = null,
                      width = 480, height = null }) {
  return (
    <div style={{
      position: "absolute", inset: 0, zIndex: 30,
      background: "rgba(8, 8, 10, 0.55)",
      backdropFilter: "blur(2px)",
      display: "flex", alignItems: "center", justifyContent: "center",
      padding: M_SP(5),
    }}>
      <div style={{
        width: "100%", maxWidth: width, maxHeight: "100%",
        ...(height ? { height } : {}),
        background: M_TOK.ink100,
        border: `1px solid ${M_TOK.ink300}`,
        borderRadius: 8,
        display: "flex", flexDirection: "column",
        boxShadow: "0 24px 64px rgba(0,0,0,0.5)",
        overflow: "hidden",
      }}>
        <div style={{
          padding: `${M_SP(4)}px ${M_SP(5)}px`,
          borderBottom: `1px solid ${M_TOK.ink200}`,
          display: "flex", alignItems: "flex-start", justifyContent: "space-between",
          gap: M_SP(3),
        }}>
          <div>
            <div style={{
              fontFamily: M_FONT.display, fontSize: 19, fontWeight: 400,
              letterSpacing: "-0.015em",
            }}>{title}</div>
            {sub && (
              <div style={{
                color: M_TOK.ink600, fontSize: 12, marginTop: 4, lineHeight: 1.45,
              }}>{sub}</div>
            )}
          </div>
          {onClose && (
            <button style={{
              width: 26, height: 26, border: 0, background: "transparent",
              color: M_TOK.ink600, cursor: "pointer", fontSize: 18,
              display: "flex", alignItems: "center", justifyContent: "center",
              borderRadius: 4,
            }}>×</button>
          )}
        </div>
        <div style={{
          flex: 1,
          padding: `${M_SP(4)}px ${M_SP(5)}px`,
          overflow: "auto",
        }}>{children}</div>
        {footer && (
          <div style={{
            padding: `${M_SP(3)}px ${M_SP(5)}px`,
            borderTop: `1px solid ${M_TOK.ink200}`,
            display: "flex", justifyContent: "space-between", alignItems: "center",
            gap: M_SP(3), background: M_TOK.ink050,
          }}>{footer}</div>
        )}
      </div>
    </div>
  );
}

// ── Step dots (wizards) ──────────────────────────────────────────────────

function OStepDots({ step = 1, total = 3, done = false }) {
  return (
    <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
      <div style={{ display: "flex", gap: 6 }}>
        {Array.from({ length: total }, (_, i) => {
          const isDone = done || i + 1 < step;
          const isCurr = !done && i + 1 === step;
          return (
            <span key={i} style={{
              width: isCurr ? 14 : 6, height: 6,
              borderRadius: 3,
              background: isDone ? M_TOK.moss
                       : isCurr ? M_TOK.amber
                                : M_TOK.ink300,
              transition: "all .2s",
            }} />
          );
        })}
      </div>
      <div style={{
        fontFamily: M_FONT.mono, fontSize: 10,
        letterSpacing: "0.16em", textTransform: "uppercase",
        color: M_TOK.ink500,
      }}>
        {done ? "Complete" : `Step ${step} of ${total}`}
      </div>
    </div>
  );
}

// ── Inline status (e.g. validation) ──────────────────────────────────────

function OFieldNote({ tone = "muted", children }) {
  const colors = {
    muted: M_TOK.ink600,
    moss:  M_TOK.moss,
    coral: M_TOK.coral,
    amber: M_TOK.amber,
  };
  return (
    <div style={{
      fontSize: 11.5, color: colors[tone], marginTop: 5,
      letterSpacing: "-0.005em", lineHeight: 1.4,
    }}>{children}</div>
  );
}

// expose
Object.assign(window, {
  OPage, OContainer, OAppBar, OTabBar, OPageTitle,
  OCard, OSectionHeader,
  OLabel, OInput, OPwdInput, OCheckbox, OSelect,
  OBtn, OToast, OModalShell, OStepDots, OFieldNote,
});
