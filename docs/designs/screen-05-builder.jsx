// screen-05-builder.jsx — Recipe Builder popup screen.
//
// Per spec 05-recipe-builder.md (v2.4):
//   • Top row: ← Back · "? How it works" link
//   • Subtitle paragraph under each primary field label (.builder-subtitle)
//   • Sheet ID + Modifiers + Profile wrapped in <details id="bld-more-options"> collapsible
//   • Auto-expand More Options when sheet is auto-detected
//   • Smart defaults: Position + Secret pre-selected from chrome.storage.local.lastUsed*

function MBuilder({ state = "first-time" }) {
  // states:
  //   first-time     — empty, no smart defaults, more-options collapsed
  //   smart-default  — returning user: pos+secret pre-selected, hash empty, more-options collapsed
  //   filled         — all filled, sheet auto-detected → more-options EXPANDED
  //   tabs-picker    — more-options expanded, tabs picker visible
  //   profile-locked — more-options expanded, profile locked by sheet mapping
  //   hash-error     — hash has non-ASCII, more-options collapsed
  const isFirstTime    = state === "first-time";
  const isSmartDefault = state === "smart-default";
  const isFilled       = state === "filled";
  const isTabsPicker   = state === "tabs-picker";
  const isProfileLock  = state === "profile-locked";
  const isHashError    = state === "hash-error";

  // values per state
  const sheetValue = isFilled ? M_SHEET_ID
                   : isProfileLock ? M_SHEET_ID
                   : "";
  const hashValue = isFilled || isProfileLock ? "fb"
                  : isHashError ? "café"
                  : "";
  const position = (isFilled || isProfileLock || isSmartDefault || isHashError) ? "#" : null;
  const secret = (isFilled || isProfileLock || isSmartDefault || isHashError) ? "1" : null;
  const modifiers = (isFilled || isProfileLock) ? ["_"] : [];
  const profile = isProfileLock ? "Banking" : "Default";

  // More options collapsed/expanded behaviour
  // Auto-expand when sheet detected; otherwise default collapsed.
  const moreOpen = isFilled || isTabsPicker || isProfileLock;

  // recipe preview
  const baseRecipe = hashValue + (position || "") + (secret || "") + modifiers.join("");
  const canBuild = hashValue && position && secret && !isHashError;
  const recipeStr =
    !canBuild ? "—" :
    sheetValue ? `${baseRecipe}.a3f2` : baseRecipe;
  const passwordStr = canBuild ? M_PWD : "—";

  return (
    <MFrame glow="amber">
      <MHeader status="ready" hideStatusText />
      <MHeaderRule />

      <div style={{
        flex: 1, padding: `${M_SP(3)}px ${M_SP(5)}px ${M_SP(4)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
        gap: M_SP(3),
        overflow: "hidden",
      }}>

        {/* TOP ROW: Back · How it works */}
        <div style={{
          display: "flex", alignItems: "center", justifyContent: "space-between",
        }}>
          <MGhostButton style={{ fontSize: 12 }}>
            <span style={{ marginRight: 4 }}>←</span>Back
          </MGhostButton>
          <button style={{
            border: 0, background: "transparent",
            color: M_TOK.amber, cursor: "pointer",
            fontSize: 12, padding: 0,
            display: "flex", alignItems: "center", gap: 4,
          }}>
            <span style={{
              display: "inline-flex", alignItems: "center", justifyContent: "center",
              width: 13, height: 13, borderRadius: "50%",
              border: `1px solid ${M_TOK.amber}88`,
              fontSize: 9, fontWeight: 500,
            }}>?</span>
            <span>How it works</span>
          </button>
        </div>

        {/* HASH */}
        <BuilderField
          label="Hash"
          subtitle="The text part of your recipe"
        >
          <TextInput value={hashValue} placeholder="any ASCII string" invalid={isHashError} />
          {isHashError && (
            <FieldNote tone="error">
              Only ASCII letters and digits. Unicode is unreliable across devices.
            </FieldNote>
          )}
        </BuilderField>

        {/* POSITION + SECRET — paired row */}
        <div style={{ display: "flex", gap: M_SP(3) }}>
          <BuilderField
            label="Position"
            subtitle="Where your secret goes"
            style={{ flex: 1 }}
          >
            <ToggleGroup items={["#", "$", "@", "%", "^"]} selected={position} />
          </BuilderField>
          <BuilderField
            label="Secret"
            subtitle="Which secret phrase"
            style={{ flex: 1 }}
          >
            <ToggleGroup items={["1", "2", "3", "4", "5"]} selected={secret} mono />
          </BuilderField>
        </div>

        {/* MORE OPTIONS — collapsible (Sheet + Modifiers + Profile) */}
        <MoreOptions open={moreOpen}>
          {/* Target Sheet */}
          <BuilderField label="Target sheet ID">
            <SheetInput value={sheetValue} hasError={false} />
            {!sheetValue && (
              <FieldNote tone="warn">
                Without a target sheet, this recipe won't verify on decode.
              </FieldNote>
            )}
            {isTabsPicker && <TabsPicker />}
          </BuilderField>

          {/* Modifiers */}
          <BuilderField
            label="Modifiers"
            subtitle="Optional transformations"
          >
            <CheckboxGroup items={["_", "!", "?", "~"]} selected={modifiers} />
          </BuilderField>

          {/* Profile */}
          <BuilderField
            label="Profile"
            inlineHint={isProfileLock ? "auto-selected from sheet mapping" : null}
          >
            <ProfileSelect value={profile} locked={isProfileLock} />
            {!isProfileLock && sheetValue && (
              <FieldNote tone="warn">
                Sheet not mapped — using default. Map in Options for stable behavior.
              </FieldNote>
            )}
          </BuilderField>
        </MoreOptions>

        <div style={{ flex: 1, minHeight: 4 }} />

        {/* PREVIEW */}
        <PreviewBlock recipe={recipeStr} password={passwordStr} hasError={isHashError} />

        {/* ACTIONS */}
        <div style={{
          display: "flex", gap: M_SP(2),
        }}>
          <button style={{
            flex: 1,
            padding: `${M_SP(2) + 4}px ${M_SP(3)}px`,
            background: canBuild ? M_TOK.amber : M_TOK.ink200,
            color: canBuild ? "#1a0f02" : M_TOK.ink500,
            border: 0, borderRadius: 4,
            fontFamily: M_FONT.body, fontSize: 13, fontWeight: 500,
            cursor: canBuild ? "pointer" : "default",
            display: "flex", alignItems: "center", justifyContent: "center", gap: 8,
          }}>
            <MCopyIcon size={12} color={canBuild ? "#1a0f02" : M_TOK.ink500}/>
            Copy recipe
          </button>
        </div>
      </div>
    </MFrame>
  );
}

// ──────────────────────────────────────────────────────────────────────────
// PRIMITIVES
// ──────────────────────────────────────────────────────────────────────────

function BuilderField({ label, subtitle, inlineHint, children, style = {} }) {
  return (
    <div style={{ ...style }}>
      <div style={{
        display: "flex", alignItems: "baseline", justifyContent: "space-between",
        marginBottom: subtitle ? 2 : M_SP(2),
        gap: M_SP(2),
      }}>
        <MCaption>{label}</MCaption>
        {inlineHint && (
          <span style={{
            fontFamily: M_FONT.display, fontStyle: "italic",
            fontSize: 10.5, color: M_TOK.ink600,
            textAlign: "right",
          }}>{inlineHint}</span>
        )}
      </div>
      {subtitle && (
        <div style={{
          fontSize: 10.5, color: M_TOK.ink600,
          lineHeight: 1.3, marginBottom: M_SP(2),
          fontStyle: "italic",
          fontFamily: M_FONT.display,
        }}>
          {subtitle}
        </div>
      )}
      {children}
    </div>
  );
}

function FieldNote({ children, tone = "info" }) {
  const color = tone === "error" ? M_TOK.coral
              : tone === "warn"  ? M_TOK.amber
              : M_TOK.ink500;
  return (
    <div style={{
      marginTop: 6,
      fontSize: 10.5, color, lineHeight: 1.4,
      display: "flex", alignItems: "flex-start", gap: 5,
    }}>
      <span style={{ fontSize: 10, marginTop: 1 }}>
        {tone === "error" || tone === "warn" ? "⚠" : "·"}
      </span>
      <span>{children}</span>
    </div>
  );
}

// "More options" disclosure block (mimics <details>/<summary>)
function MoreOptions({ open, children }) {
  return (
    <div style={{
      border: `1px solid ${M_TOK.ink200}`,
      borderRadius: 4,
      background: open ? M_TOK.ink100 + "60" : "transparent",
      overflow: "hidden",
    }}>
      {/* summary row */}
      <div style={{
        display: "flex", alignItems: "center", gap: M_SP(2),
        padding: `${M_SP(2)}px ${M_SP(3)}px`,
        cursor: "pointer",
        borderBottom: open ? `1px solid ${M_TOK.ink200}` : "none",
      }}>
        <span style={{
          fontFamily: M_FONT.mono, fontSize: 9,
          color: M_TOK.ink500,
          transform: open ? "rotate(90deg)" : "rotate(0deg)",
          transition: "transform 120ms ease",
          display: "inline-block", width: 8,
        }}>▶</span>
        <span style={{
          fontFamily: M_FONT.mono, fontSize: 9.5, fontWeight: 500,
          letterSpacing: "0.22em", textTransform: "uppercase",
          color: open ? M_TOK.ink800 : M_TOK.ink600,
        }}>
          More options
        </span>
        {open && (
          <span style={{
            fontFamily: M_FONT.display, fontStyle: "italic",
            fontSize: 10.5, color: M_TOK.amber,
            marginLeft: "auto",
          }}>
            sheet detected · auto-expanded
          </span>
        )}
      </div>

      {open && (
        <div style={{
          padding: `${M_SP(3)}px ${M_SP(3)}px ${M_SP(3) + 2}px`,
          display: "flex", flexDirection: "column",
          gap: M_SP(3),
        }}>
          {children}
        </div>
      )}
    </div>
  );
}

function SheetInput({ value, hasError }) {
  const empty = !value;
  return (
    <div style={{
      display: "flex", alignItems: "stretch",
      borderTop: `1px solid ${hasError ? M_TOK.coral : M_TOK.ink300}`,
      borderBottom: `1px solid ${hasError ? M_TOK.coral : M_TOK.ink300}`,
    }}>
      <div style={{
        flex: 1, minWidth: 0,
        padding: `${M_SP(2) + 2}px 0`,
        fontFamily: M_FONT.mono, fontSize: 12,
        color: empty ? M_TOK.ink500 : M_TOK.ink900,
        letterSpacing: "0.02em",
        fontStyle: empty ? "italic" : "normal",
        whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis",
      }}>
        {value || "Paste URL or Sheet ID"}
      </div>
      <button style={{
        border: 0, background: "transparent",
        color: M_TOK.ink500, cursor: "pointer",
        padding: `0 ${M_SP(2)}px`,
        display: "flex", alignItems: "center",
      }} title="Re-detect from active tab">
        <MRefreshIcon />
      </button>
    </div>
  );
}

function TextInput({ value, placeholder, invalid }) {
  const empty = !value;
  const borderColor = invalid ? M_TOK.coral : M_TOK.ink300;
  return (
    <div style={{
      borderTop: `1px solid ${borderColor}`,
      borderBottom: `1px solid ${borderColor}`,
      padding: `${M_SP(2) + 2}px 0`,
      fontFamily: M_FONT.mono, fontSize: 14,
      color: empty ? M_TOK.ink500 : (invalid ? M_TOK.coral : M_TOK.ink900),
      letterSpacing: "0.02em",
      fontStyle: empty ? "italic" : "normal",
      minHeight: 18,
    }}>
      {value || placeholder}
    </div>
  );
}

function TabsPicker() {
  return (
    <div style={{
      marginTop: M_SP(2),
      padding: M_SP(3),
      background: M_TOK.ink150,
      border: `1px solid ${M_TOK.ink300}`,
      borderRadius: 4,
    }}>
      <div style={{
        fontFamily: M_FONT.mono, fontSize: 10,
        letterSpacing: "0.15em", textTransform: "uppercase",
        color: M_TOK.amber, marginBottom: M_SP(2),
      }}>
        3 sheets tabs open — pick one:
      </div>
      <div style={{ display: "flex", flexDirection: "column", gap: 4 }}>
        <TabOption name="Banking — 2026" sub="…/Bxy" />
        <TabOption name="Q1 Planning" sub="…/Lmn" />
        <TabOption name="Personal Logins" sub="…/Rst" />
      </div>
    </div>
  );
}

function TabOption({ name, sub }) {
  return (
    <button style={{
      display: "flex", alignItems: "baseline", gap: M_SP(2),
      padding: `6px 8px`,
      background: "transparent",
      border: `1px solid ${M_TOK.ink200}`,
      borderRadius: 3,
      color: M_TOK.ink800,
      cursor: "pointer",
      textAlign: "left",
    }}>
      <span style={{ fontSize: 12, color: M_TOK.ink900 }}>{name}</span>
      <span style={{
        fontFamily: M_FONT.mono, fontSize: 10,
        color: M_TOK.ink500, marginLeft: "auto",
      }}>{sub}</span>
    </button>
  );
}

function ToggleGroup({ items, selected, mono = false }) {
  return (
    <div style={{ display: "flex", gap: 4 }}>
      {items.map(it => {
        const active = it === selected;
        return (
          <button key={it} style={{
            flex: 1, height: 30,
            background: active ? M_TOK.amber : "transparent",
            color: active ? "#1a0f02" : M_TOK.ink700,
            border: `1px solid ${active ? M_TOK.amber : M_TOK.ink300}`,
            borderRadius: 3,
            fontFamily: mono ? M_FONT.mono : M_FONT.body,
            fontSize: mono ? 13 : 15, fontWeight: active ? 600 : 400,
            cursor: "pointer",
            display: "flex", alignItems: "center", justifyContent: "center",
          }}>{it}</button>
        );
      })}
    </div>
  );
}

function CheckboxGroup({ items, selected }) {
  return (
    <div style={{ display: "flex", gap: 4 }}>
      {items.map(it => {
        const active = selected.includes(it);
        return (
          <button key={it} style={{
            flex: 1, height: 30,
            background: active ? M_TOK.amberBg : "transparent",
            color: active ? M_TOK.amber : M_TOK.ink700,
            border: `1px solid ${active ? M_TOK.amber + "88" : M_TOK.ink300}`,
            borderRadius: 3,
            fontFamily: M_FONT.mono,
            fontSize: 15, fontWeight: 400,
            cursor: "pointer",
            display: "flex", alignItems: "center", justifyContent: "center",
            gap: 4,
          }}>
            <span style={{
              width: 8, height: 8, borderRadius: 1,
              border: `1px solid ${active ? M_TOK.amber : M_TOK.ink400}`,
              background: active ? M_TOK.amber : "transparent",
              display: "inline-block",
            }}/>
            <span>{it}</span>
          </button>
        );
      })}
    </div>
  );
}

function ProfileSelect({ value, locked }) {
  return (
    <div style={{
      display: "flex", alignItems: "center", justifyContent: "space-between",
      padding: `${M_SP(2) + 2}px ${M_SP(2)}px ${M_SP(2) + 2}px 0`,
      borderTop: `1px solid ${M_TOK.ink300}`,
      borderBottom: `1px solid ${M_TOK.ink300}`,
      background: locked ? M_TOK.ink100 + "70" : "transparent",
    }}>
      <div style={{ display: "flex", alignItems: "center", gap: M_SP(2) }}>
        <span style={{
          width: 7, height: 7, borderRadius: "50%",
          background: M_TOK.amber,
        }}/>
        <span style={{
          fontSize: 14, color: M_TOK.ink900,
        }}>{value}</span>
        {locked && (
          <span style={{
            color: M_TOK.ink500,
            display: "flex", alignItems: "center", gap: 4,
          }}>
            <MLockIcon size={11}/>
          </span>
        )}
      </div>
      <span style={{
        color: locked ? M_TOK.ink400 : M_TOK.ink600,
        fontSize: 11,
      }}>{locked ? "" : "▾"}</span>
    </div>
  );
}

function PreviewBlock({ recipe, password, hasError }) {
  return (
    <div style={{
      padding: M_SP(3),
      background: M_TOK.ink150,
      border: `1px solid ${M_TOK.ink300}`,
      borderRadius: 4,
    }}>
      <div style={{
        display: "flex", alignItems: "baseline", gap: M_SP(2),
        marginBottom: M_SP(2),
      }}>
        <MCaption style={{ fontSize: 9, minWidth: 55 }}>Recipe</MCaption>
        <span style={{
          fontFamily: M_FONT.mono, fontSize: 13,
          color: recipe === "—" ? M_TOK.ink500 : M_TOK.ink900,
          letterSpacing: "0.02em",
        }}>{recipe}</span>
      </div>

      <div style={{
        borderTop: `1px dashed ${M_TOK.ink300}`,
        margin: `${M_SP(2)}px 0`,
      }}/>

      <div style={{
        display: "flex", alignItems: "baseline", gap: M_SP(2),
      }}>
        <MCaption style={{ fontSize: 9, minWidth: 55, color: M_TOK.amber }}>
          Real password
        </MCaption>
        <span style={{
          fontFamily: M_FONT.mono, fontSize: 13,
          color: hasError ? M_TOK.coral
               : password === "—" ? M_TOK.ink500
               : M_TOK.ink900,
          letterSpacing: "0.02em",
          wordBreak: "break-all", flex: 1,
        }}>{password}</span>
      </div>
    </div>
  );
}

Object.assign(window, {
  MBuilder, MoreOptions, BuilderField,
});
