// screen-options-profiles.jsx — Options page · Profiles tab
//
// Per spec 08-profiles-tab.md:
//   Sections: My Profiles · Shared Profiles · Sheet Assignments
//   Own profile card actions: ✏️ Secrets · ↑ Export · ★ Default · Delete
//   Shared profile: read-only, locked to sheetId, can Remove
//   Sheet Assignments: pasted ID → profile dropdown · default fallback row
//
// States (in same file):
//   empty         — single Default profile · no shared · no assignments
//   populated     — 3 own profiles · 1 shared · 2 assignments · default fallback
//   delete-guard  — profile assigned to sheets shows inline warn-text
//   shared-only   — focus shared profile card

const PROF_COLORS = ["#58a6ff", "#e3b341", "#3fb950", "#a371f7", "#f78166"];

function OProfilesTab({ state = "populated" }) {
  const isEmpty       = state === "empty";
  const isDeleteGuard = state === "delete-guard";

  const ownProfiles = isEmpty
    ? [{ name: "Default", sheets: 0, isDefault: true }]
    : [
        { name: "Default",     sheets: 0, isDefault: true },
        { name: "Banking",     sheets: 2, isDefault: false },
        { name: "Work / SaaS", sheets: isDeleteGuard ? 3 : 1, isDefault: false, deleteGuardOpen: isDeleteGuard },
      ];

  const sharedProfiles = isEmpty ? [] : [
    { name: "TeamFromB", from: "User B", at: "2026-01-10", sheetId: "1BxCdefGhIjklmn..." },
  ];

  const assignments = isEmpty ? [] : [
    { sheetId: "1BxCdefGhIjklmnopqrSt", profile: "Banking" },
    { sheetId: "1QrStUvWxYz0987abcDe1", profile: "Work / SaaS" },
  ];
  const defaultFallback = "Default";

  return (
    <OPage height="auto">
      <OAppBar profile />
      <OTabBar active="profiles" />

      <OContainer width={760} style={{ padding: `${M_SP(6)}px ${M_SP(5)}px ${M_SP(7)}px` }}>
        <OPageTitle
          eyebrow="Profiles · v2"
          title="Profiles & Sheet routing"
          sub="Each profile holds an independent set of 5 secrets. Map sheets to profiles so the same recipe yields different passwords in different contexts."
        />

        {/* My Profiles */}
        <SectionBlock title="My Profiles" right={
          <OBtn kind="secondary" leading={<Plus/>}>New Profile</OBtn>
        }>
          {ownProfiles.length === 0 ? (
            <EmptyHint>No profiles yet. Create one above.</EmptyHint>
          ) : (
            <div style={{ display: "flex", flexDirection: "column", gap: M_SP(3) }}>
              {ownProfiles.map((p, i) => (
                <OwnProfileCard key={p.name} index={i} profile={p} />
              ))}
            </div>
          )}
        </SectionBlock>

        {/* Shared */}
        <SectionBlock title="Shared Profiles" right={
          <OBtn kind="secondary" leading={<Arrow down/>}>Import Shared</OBtn>
        }>
          {sharedProfiles.length === 0 ? (
            <EmptyHint>No imported profiles.</EmptyHint>
          ) : (
            <div style={{ display: "flex", flexDirection: "column", gap: M_SP(3) }}>
              {sharedProfiles.map(p => (
                <SharedProfileCard key={p.name} profile={p}/>
              ))}
            </div>
          )}
        </SectionBlock>

        {/* Sheet Assignments */}
        <SectionBlock title="Sheet Assignments" right={
          <OBtn kind="secondary" leading={<Plus/>}>Add Assignment</OBtn>
        }>
          {assignments.length === 0 ? (
            <EmptyHint>No assignments yet. Add one above, or set a default fallback below.</EmptyHint>
          ) : (
            <div style={{ display: "flex", flexDirection: "column", gap: M_SP(2) }}>
              {assignments.map(a => <AssignmentRow key={a.sheetId} row={a}/>)}
            </div>
          )}

          <div style={{
            marginTop: M_SP(4),
            paddingTop: M_SP(3),
            borderTop: `1px dashed ${M_TOK.ink300}`,
            display: "flex", alignItems: "center", justifyContent: "space-between",
            gap: M_SP(3),
          }}>
            <div>
              <div style={{ fontSize: 12.5, color: M_TOK.ink800 }}>Unknown sheets →</div>
              <div style={{ fontSize: 11.5, color: M_TOK.ink600, marginTop: 2 }}>
                Fallback used when a sheet isn’t in the table above.
              </div>
            </div>
            <OSelect value={defaultFallback} style={{ width: 220 }}/>
          </div>
        </SectionBlock>

      </OContainer>
    </OPage>
  );
}

// ── Section block ────────────────────────────────────────────────────────

function SectionBlock({ title, right, children }) {
  return (
    <div style={{ marginTop: M_SP(5) }}>
      <div style={{
        display: "flex", alignItems: "center", justifyContent: "space-between",
        marginBottom: M_SP(3),
      }}>
        <div style={{
          fontFamily: M_FONT.display, fontSize: 17, fontWeight: 400,
          fontStyle: "italic", letterSpacing: "-0.015em", color: M_TOK.ink900,
        }}>{title}</div>
        {right}
      </div>
      {children}
    </div>
  );
}

function EmptyHint({ children }) {
  return (
    <div style={{
      border: `1px dashed ${M_TOK.ink300}`,
      borderRadius: 4,
      padding: `${M_SP(4)}px ${M_SP(5)}px`,
      color: M_TOK.ink600, fontSize: 13,
      fontStyle: "italic", fontFamily: M_FONT.display, fontSize: 14,
      textAlign: "center",
    }}>{children}</div>
  );
}

// ── Own profile card ─────────────────────────────────────────────────────

function OwnProfileCard({ index, profile }) {
  const color = PROF_COLORS[index % PROF_COLORS.length];
  const sheetText = profile.sheets === 0
    ? "No sheet assignments"
    : `${profile.sheets} sheet assignment${profile.sheets === 1 ? "" : "s"}`;
  return (
    <div style={{
      background: M_TOK.ink100,
      border: `1px solid ${M_TOK.ink200}`,
      borderLeft: `3px solid ${color}`,
      borderRadius: 6,
      padding: M_SP(4),
    }}>
      <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", gap: M_SP(3) }}>
        <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
          <span style={{
            width: 9, height: 9, borderRadius: "50%", background: color,
            boxShadow: `0 0 6px ${color}66`,
          }} />
          <span style={{
            fontFamily: M_FONT.display, fontSize: 18, fontWeight: 400,
            letterSpacing: "-0.015em", color: M_TOK.ink900,
          }}>{profile.name}</span>
        </div>
        {profile.isDefault && (
          <span style={{
            display: "inline-flex", alignItems: "center", gap: 5,
            padding: "2px 8px", borderRadius: 999,
            background: M_TOK.amberBg + "aa", color: M_TOK.amber,
            border: `1px solid ${M_TOK.amber}55`,
            fontFamily: M_FONT.mono, fontSize: 9.5,
            letterSpacing: "0.16em", textTransform: "uppercase",
          }}>
            <Star size={9}/> Default
          </span>
        )}
      </div>

      <div style={{ marginTop: 6, color: M_TOK.ink600, fontSize: 12 }}>
        {sheetText}
      </div>

      <div style={{ height: M_SP(3) }}/>
      <div style={{ display: "flex", flexWrap: "wrap", gap: 8 }}>
        <OBtn kind="secondary" leading={<Pencil/>}>Edit Secrets</OBtn>
        <OBtn kind="secondary" leading={<Arrow up/>}>Export</OBtn>
        {!profile.isDefault && (
          <>
            <OBtn kind="secondary" leading={<Star size={11}/>}>Set Default</OBtn>
            <OBtn kind="secondary" danger>Delete</OBtn>
          </>
        )}
      </div>

      {profile.deleteGuardOpen && (
        <div style={{
          marginTop: M_SP(3),
          padding: `${M_SP(2) + 2}px ${M_SP(3)}px`,
          background: M_TOK.amberBg + "55",
          border: `1px solid ${M_TOK.amber}55`,
          borderRadius: 4,
          color: M_TOK.amber, fontSize: 12, lineHeight: 1.5,
        }}>
          Assigned to {profile.sheets} sheet{profile.sheets === 1 ? "" : "s"}. Reassign before deleting.
        </div>
      )}
    </div>
  );
}

// ── Shared profile card ──────────────────────────────────────────────────

function SharedProfileCard({ profile }) {
  return (
    <div style={{
      background: "#1c1c22",
      border: `1px solid ${M_TOK.ink200}`,
      borderLeft: `3px solid ${M_TOK.ink400}`,
      borderRadius: 6,
      padding: M_SP(4),
    }}>
      <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", gap: M_SP(3) }}>
        <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
          <Inbox/>
          <span style={{
            fontFamily: M_FONT.display, fontSize: 18, fontWeight: 400,
            letterSpacing: "-0.015em", color: M_TOK.ink900,
          }}>{profile.name}</span>
        </div>
        <span style={{
          display: "inline-flex", alignItems: "center", gap: 5,
          padding: "2px 8px", borderRadius: 999,
          background: M_TOK.ink050, color: M_TOK.ink600,
          border: `1px solid ${M_TOK.ink300}`,
          fontFamily: M_FONT.mono, fontSize: 9.5,
          letterSpacing: "0.16em", textTransform: "uppercase",
        }}>
          <MLockIcon size={9}/> Read-only
        </span>
      </div>

      <div style={{ marginTop: 6, fontSize: 12, color: M_TOK.ink600 }}>
        From: <span style={{ color: M_TOK.ink800 }}>{profile.from}</span>
        <span style={{ margin: "0 8px", color: M_TOK.ink400 }}>·</span>
        Imported: <span style={{ color: M_TOK.ink800 }}>{profile.at}</span>
      </div>
      <div style={{
        marginTop: 4,
        fontFamily: M_FONT.mono, fontSize: 11.5,
        color: M_TOK.ink600,
      }}>
        Sheet: <span style={{ color: M_TOK.ink800 }}>{profile.sheetId}</span>
        <span style={{ color: M_TOK.ink500 }}> (locked)</span>
      </div>

      <div style={{ height: M_SP(3) }}/>
      <div style={{ display: "flex", justifyContent: "flex-end" }}>
        <OBtn kind="secondary" danger>Remove</OBtn>
      </div>
    </div>
  );
}

// ── Assignment row ───────────────────────────────────────────────────────

function AssignmentRow({ row }) {
  return (
    <div style={{
      display: "grid",
      gridTemplateColumns: "1fr 200px 30px",
      gap: M_SP(2) + 2,
      alignItems: "center",
      padding: `${M_SP(2) + 2}px ${M_SP(3)}px`,
      background: M_TOK.ink100,
      border: `1px solid ${M_TOK.ink200}`,
      borderRadius: 4,
    }}>
      <div style={{
        fontFamily: M_FONT.mono, fontSize: 12, color: M_TOK.ink800,
        whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis",
      }}>
        {row.sheetId.slice(0, 20)}<span style={{ color: M_TOK.ink500 }}>...</span>
      </div>
      <OSelect value={row.profile}/>
      <button style={{
        width: 26, height: 26, border: `1px solid ${M_TOK.ink300}`,
        background: "transparent", color: M_TOK.ink600,
        borderRadius: 4, cursor: "pointer",
        display: "flex", alignItems: "center", justifyContent: "center",
        fontSize: 14,
      }}>×</button>
    </div>
  );
}

// ── icons ────────────────────────────────────────────────────────────────

function Plus() {
  return (
    <svg width="11" height="11" viewBox="0 0 12 12" fill="none">
      <path d="M6 1.5V10.5M1.5 6H10.5" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round"/>
    </svg>
  );
}
function Arrow({ up = false, down = false }) {
  return (
    <svg width="11" height="11" viewBox="0 0 12 12" fill="none">
      {up && <path d="M6 10V2M2.5 5.5L6 2L9.5 5.5" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round" strokeLinejoin="round"/>}
      {down && <path d="M6 2V10M2.5 6.5L6 10L9.5 6.5" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round" strokeLinejoin="round"/>}
    </svg>
  );
}
function Pencil() {
  return (
    <svg width="11" height="11" viewBox="0 0 12 12" fill="none">
      <path d="M8 2L10 4L4 10H2V8L8 2Z" stroke="currentColor" strokeWidth="1.3" strokeLinejoin="round"/>
    </svg>
  );
}
function Star({ size = 10 }) {
  return (
    <svg width={size} height={size} viewBox="0 0 12 12" fill="none">
      <path d="M6 1.5L7.4 4.4L10.5 4.85L8.25 7.05L8.8 10.1L6 8.65L3.2 10.1L3.75 7.05L1.5 4.85L4.6 4.4L6 1.5Z" stroke="currentColor" strokeWidth="1.2" strokeLinejoin="round"/>
    </svg>
  );
}
function Inbox() {
  return (
    <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
      <path d="M2 8V11C2 11.5 2.5 12 3 12H11C11.5 12 12 11.5 12 11V8" stroke={M_TOK.ink600} strokeWidth="1.3" strokeLinecap="round"/>
      <path d="M2 8H4.5L5.5 9.5H8.5L9.5 8H12" stroke={M_TOK.ink600} strokeWidth="1.3" strokeLinejoin="round"/>
      <path d="M7 2V6M4.5 4.5L7 7L9.5 4.5" stroke={M_TOK.ink800} strokeWidth="1.3" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
  );
}

Object.assign(window, { OProfilesTab });
