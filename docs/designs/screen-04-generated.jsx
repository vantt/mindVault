// screen-04-generated.jsx — Generated screen (SUCCESS ONLY per v2.4 spec).
//
// IMPORTANT: This screen is auto-routed to ONLY when response.success === true.
// All error states (empty cell, connection error, parse error, sheet mismatch)
// now remain on the Home screen with an amber inline notice — see screen-03-home.jsx.
//
// Remaining states here are success-only:
//   default · copied · shared · pepper

function MGenerated({ state = "default" }) {
  // states: default | copied | shared | pepper
  const isShared = state === "shared";
  const showPepper = state === "pepper" || state === "shared";
  const isCopied = state === "copied";

  const profileName = isShared ? "TeamFromB" : "Default";

  return (
    <MFrame glow="moss">
      <MHeader status="ready" />
      <MHeaderRule />

      <div style={{
        flex: 1, padding: `${M_SP(4)}px ${M_SP(5)}px ${M_SP(5)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
      }}>

        {/* PROFILE STRIP — shown when response.profileName present */}
        <div style={{
          display: "flex", alignItems: "flex-start", justifyContent: "space-between",
          gap: M_SP(3),
        }}>
          <div style={{ minWidth: 0, flex: 1 }}>
            <MCaption>
              Profile
              {isShared && <span style={{ marginLeft: 6, color: M_TOK.amber }}>· received 📥</span>}
            </MCaption>
            <div style={{
              marginTop: M_SP(2),
              display: "flex", alignItems: "baseline", gap: M_SP(2),
              flexWrap: "wrap",
            }}>
              <span style={{
                fontSize: 17, fontWeight: 400,
                letterSpacing: "-0.015em",
                color: M_TOK.ink900,
              }}>{profileName}</span>
              {isShared && (
                <span style={{
                  fontFamily: M_FONT.mono, fontSize: 10.5,
                  color: M_TOK.ink600,
                }}>from User B</span>
              )}
            </div>
          </div>

          {isShared && (
            <div style={{ textAlign: "right", flexShrink: 0 }}>
              <MCaption style={{ fontSize: 9 }}>Sheet</MCaption>
              <div style={{
                marginTop: M_SP(2),
                fontFamily: M_FONT.mono, fontSize: 10.5,
                color: M_TOK.ink700, letterSpacing: "0.03em",
              }}>{M_SHEET_ID.slice(0, 10)}…</div>
            </div>
          )}
        </div>

        <div style={{ height: M_SP(4) }} />
        <MDivider />
        <div style={{ height: M_SP(4) }} />

        {/* PASSWORD BLOCK */}
        <div>
          <div style={{
            display: "flex", alignItems: "baseline", justifyContent: "space-between",
            marginBottom: M_SP(3),
          }}>
            <MCaption>Password</MCaption>
            <div style={{
              display: "flex", alignItems: "center", gap: M_SP(2),
              fontFamily: M_FONT.mono, fontSize: 9.5,
              letterSpacing: "0.12em", textTransform: "uppercase",
              color: M_TOK.ink500,
            }}>
              <span>16 ch</span>
              <span style={{ color: M_TOK.ink300 }}>·</span>
              <span>~96b</span>
              <span style={{ color: M_TOK.ink300 }}>·</span>
              <span style={{ color: M_TOK.moss, display: "flex", alignItems: "center", gap: 3 }}>
                <MCheckTiny size={9} color={M_TOK.moss}/> ok
              </span>
            </div>
          </div>

          <MPasswordSlab copied={isCopied} />

          {/* pepper hint — only shown when settings.pepperingHint is truthy */}
          {showPepper && (
            <div style={{
              marginTop: M_SP(3),
              display: "flex", alignItems: "center", gap: M_SP(2),
            }}>
              <span style={{
                width: 5, height: 5, borderRadius: "50%",
                background: M_TOK.honey,
              }} />
              <span style={{
                fontFamily: M_FONT.display, fontStyle: "italic",
                fontSize: 14, fontWeight: 400,
                color: M_TOK.ink800, letterSpacing: "-0.005em",
              }}>
                Don't forget your pepper.
              </span>
            </div>
          )}
        </div>

        <div style={{ flex: 1, minHeight: M_SP(4) }} />

        {/* PRIMARY: Build recipe */}
        <MPrimaryButton>Build recipe</MPrimaryButton>

        {/* Back / Lock row */}
        <div style={{
          display: "flex", justifyContent: "space-between", alignItems: "center",
          marginTop: M_SP(3),
        }}>
          <MGhostButton><span style={{ marginRight: 6 }}>←</span>Back</MGhostButton>
          <MGhostButton>
            Lock<span style={{ marginLeft: 6, opacity: 0.55 }}>⌘L</span>
          </MGhostButton>
        </div>
      </div>
    </MFrame>
  );
}

function MPasswordSlab({ copied }) {
  const borderColor = copied ? M_TOK.amber : M_TOK.ink300;
  return (
    <div style={{
      display: "flex", alignItems: "stretch",
      borderTop: `1px solid ${borderColor}`,
      borderBottom: `1px solid ${borderColor}`,
      transition: "border-color 200ms ease",
      position: "relative",
    }}>
      <div style={{
        padding: `${M_SP(3)}px 0`,
        flex: 1,
        fontFamily: M_FONT.mono,
        fontSize: 22, fontWeight: 400,
        letterSpacing: "0.005em",
        color: M_TOK.ink900,
        lineHeight: 1.1,
        display: "flex", alignItems: "center",
        overflow: "hidden",
      }}>
        {M_PWD}
      </div>

      <button style={{
        border: 0, background: "transparent",
        color: copied ? M_TOK.amber : M_TOK.ink800,
        padding: `0 ${M_SP(3)}px`,
        fontFamily: M_FONT.body, fontSize: 12.5, fontWeight: 500,
        letterSpacing: "0.02em",
        cursor: "pointer",
        display: "flex", alignItems: "center", gap: 6,
        flexShrink: 0,
      }}>
        {copied ? <MCheckTiny color={M_TOK.amber}/> : <MCopyIcon/>}
        <span>{copied ? "Copied" : "Copy"}</span>
      </button>

      {copied && (
        <span style={{
          position: "absolute", inset: 0, pointerEvents: "none",
          background: `linear-gradient(90deg, transparent 0%, ${M_TOK.amber}10 50%, transparent 100%)`,
        }} />
      )}
    </div>
  );
}

// ══════════════════════════════════════════════════════════════════════════
// EXPLORES
// ══════════════════════════════════════════════════════════════════════════

// A · Hero password — drop most chrome
function MGeneratedHero() {
  return (
    <MFrame glow="moss">
      <MHeader status="ready" hideStatusText />

      <div style={{
        flex: 1, padding: `${M_SP(6)}px ${M_SP(5)}px ${M_SP(5)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
      }}>
        <div style={{
          display: "flex", alignItems: "center", gap: M_SP(2),
          fontFamily: M_FONT.mono, fontSize: 10,
          letterSpacing: "0.18em", textTransform: "uppercase",
          color: M_TOK.ink500,
        }}>
          <MCheckTiny size={10} color={M_TOK.moss}/>
          <span>generated · default profile · {M_SHEET_NAME}</span>
        </div>

        <div style={{ flex: 1 }} />

        <div style={{
          fontFamily: M_FONT.mono,
          fontSize: 26, fontWeight: 400,
          color: M_TOK.ink900,
          lineHeight: 1.2,
          wordBreak: "break-all",
        }}>
          {M_PWD}
        </div>

        <div style={{ height: M_SP(3) }} />

        <button style={{
          display: "flex", alignItems: "center", justifyContent: "center", gap: M_SP(2),
          width: "100%",
          padding: `${M_SP(3)}px ${M_SP(4)}px`,
          background: "transparent",
          color: M_TOK.amber,
          border: `1px solid ${M_TOK.amber}`,
          fontFamily: M_FONT.body, fontSize: 14, fontWeight: 500,
          cursor: "pointer", borderRadius: 4,
        }}>
          <MCopyIcon size={14}/>
          Copy to clipboard
        </button>

        <div style={{ flex: 1 }} />

        <div style={{
          display: "flex", gap: M_SP(2),
          padding: `${M_SP(3)}px 0 0`,
          borderTop: `1px solid ${M_TOK.ink200}`,
        }}>
          <MGhostButton style={{ flex: 1, textAlign: "left" }}>← Back</MGhostButton>
          <MGhostButton style={{ flex: 1, textAlign: "center" }}>Build recipe</MGhostButton>
          <MGhostButton style={{ flex: 1, textAlign: "right" }}>Lock</MGhostButton>
        </div>
      </div>
    </MFrame>
  );
}

// B · Receipt / ledger — full disclosure
function MGeneratedReceipt() {
  return (
    <MFrame glow="amber">
      <MHeader status="ready" />
      <MHeaderRule />

      <div style={{
        flex: 1, padding: `${M_SP(4)}px ${M_SP(5)}px ${M_SP(5)}px`,
        display: "flex", flexDirection: "column", position: "relative", zIndex: 1,
        fontFamily: M_FONT.mono,
      }}>
        <MCaption>Decoded from sheet</MCaption>
        <div style={{ height: M_SP(3) }} />

        <div style={{ fontSize: 11.5, color: M_TOK.ink700, lineHeight: 1.85 }}>
          <ReceiptRow k="recipe" v="fb#1_.a3f2" />
          <ReceiptRow k="profile" v="Default" />
          <ReceiptRow k="sheet" v={M_SHEET_NAME} />
          <ReceiptRow k="length" v="16 chars · ~96 bits" />
          <ReceiptRow k="tag" v="a3f2 · verified" highlight />
        </div>

        <div style={{ height: M_SP(3) }} />
        <div style={{
          borderTop: `1px dashed ${M_TOK.ink300}`,
          paddingTop: M_SP(3),
        }}>
          <div style={{
            fontFamily: M_FONT.mono, fontSize: 18,
            color: M_TOK.ink900, letterSpacing: "0.02em",
            wordBreak: "break-all",
            padding: `${M_SP(2)}px 0`,
          }}>
            {M_PWD}
          </div>
        </div>

        <div style={{ flex: 1 }} />

        <div style={{ display: "flex", gap: M_SP(2) }}>
          <button style={{
            flex: 1,
            padding: `${M_SP(3)}px`,
            background: M_TOK.amber, color: "#1a0f02",
            border: 0, borderRadius: 4,
            fontFamily: M_FONT.body, fontSize: 13, fontWeight: 500,
            cursor: "pointer",
            display: "flex", alignItems: "center", justifyContent: "center", gap: 6,
          }}>
            <MCopyIcon size={13} color="#1a0f02"/> Copy password
          </button>
          <MSecondaryButton>Build new</MSecondaryButton>
        </div>

        <div style={{
          marginTop: M_SP(3),
          display: "flex", justifyContent: "space-between",
          fontSize: 10, color: M_TOK.ink500,
          letterSpacing: "0.15em", textTransform: "uppercase",
        }}>
          <span>← back</span>
          <span>lock ⌘L</span>
        </div>
      </div>
    </MFrame>
  );
}

function ReceiptRow({ k, v, highlight }) {
  return (
    <div style={{ display: "flex", alignItems: "baseline", gap: M_SP(2) }}>
      <span style={{
        color: M_TOK.ink500, minWidth: 60,
      }}>{k}</span>
      <span style={{
        flex: 1, borderBottom: `1px dotted ${M_TOK.ink300}`,
        margin: "0 4px", transform: "translateY(-3px)",
      }} />
      <span style={{
        color: highlight ? M_TOK.moss : M_TOK.ink800,
      }}>{v}</span>
    </div>
  );
}

Object.assign(window, {
  MGenerated, MGeneratedHero, MGeneratedReceipt,
  MPasswordSlab,
});
