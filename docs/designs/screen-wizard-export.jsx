// screen-wizard-export.jsx — Per spec 09-export-wizard.md
//
// 3-step modal wizard + post-generate bundle screen.
//   Step 1: Relationship label
//   Step 2: Sheet to lock
//   Step 3: Sharing password
//   Done : Bundle ready (copy / download)

function OWizardExport({ state = "step1" }) {
  const stepMap = { step1: 1, step2: 2, step3: 3, done: 3 };
  const step = stepMap[state] || 1;
  const done = state === "done";

  const profileName = "TeamShare";

  return (
    <OPage height="auto">
      <OAppBar profile/>
      <OTabBar active="profiles"/>
      <DimmedBg/>

      <OModalShell
        width={580}
        title={
          <>
            <span style={{ color: M_TOK.ink600 }}>Export profile · </span>
            <span style={{ fontStyle: "italic" }}>{profileName}</span>
          </>
        }
        sub={
          <div style={{ display: "flex", alignItems: "center", gap: M_SP(3), marginTop: 4 }}>
            <OStepDots step={step} total={3} done={done}/>
          </div>
        }
        footer={<ExportFooter state={state}/>}
      >
        {state === "step1" && <Step1/>}
        {state === "step2" && <Step2/>}
        {state === "step3" && <Step3/>}
        {state === "done"  && <StepDone profile={profileName}/>}
      </OModalShell>
    </OPage>
  );
}

// ── Step 1: Label ────────────────────────────────────────────────────────

function Step1() {
  return (
    <WizardBody
      eyebrow="Step 1 — relationship"
      title="Set a relationship label"
      lead="Used to derive isolated secrets for this share. Changing the label later invalidates every bundle made under it."
    >
      <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
        <OLabel>Label</OLabel>
        <OInput value="team-alice-2026" mono/>
        <OFieldNote tone="muted">
          Lowercase letters, digits, hyphens and underscores. e.g. <code style={{ fontFamily: M_FONT.mono, color: M_TOK.ink700 }}>team-alice-2026</code>
        </OFieldNote>
      </div>

      <WarningRow tone="amber">
        Keep this label private. It's part of the key derivation — sharing the bundle alone is not enough to compromise it, but combining a leaked label with a leaked bundle and sharing password is.
      </WarningRow>
    </WizardBody>
  );
}

// ── Step 2: Sheet to lock ────────────────────────────────────────────────

function Step2() {
  return (
    <WizardBody
      eyebrow="Step 2 — bound sheet"
      title="Lock to a Google Sheet"
      lead="Derived passwords only work on this specific Sheet ID. Copying the sheet changes its ID — derived passwords won't work on the copy."
    >
      <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
        <OLabel>Sheet URL or ID</OLabel>
        <OInput value="1BxCdefGh_aWxYz3...mnoP" mono/>
        <div style={{ display: "flex", justifyContent: "space-between", marginTop: 4 }}>
          <OBtn kind="secondary" leading={<Clip/>}>Use current sheet</OBtn>
          <span style={{
            display: "inline-flex", alignItems: "center", gap: 6,
            color: M_TOK.moss, fontSize: 12,
          }}>
            <MCheckTiny color={M_TOK.moss} size={11}/>
            <span style={{ fontFamily: M_FONT.mono, fontSize: 11.5, color: M_TOK.ink800 }}>
              1BxCdef…aWxYz3
            </span>
          </span>
        </div>
      </div>

      <WarningRow tone="moss">
        <span style={{ color: M_TOK.ink800 }}>Owner step (after share):</span>
        <span style={{ color: M_TOK.ink600 }}> Open this sheet using mindVault and update every account password to its derived value. Recipients can then decode passwords without ever seeing your originals.</span>
      </WarningRow>
    </WizardBody>
  );
}

// ── Step 3: Sharing password ─────────────────────────────────────────────

function Step3() {
  return (
    <WizardBody
      eyebrow="Step 3 — sharing password"
      title="Set a sharing password"
      lead="Encrypts the bundle. The recipient needs this password to import — share it through a different channel than the bundle file."
    >
      <div style={{ display: "flex", flexDirection: "column", gap: M_SP(3) }}>
        <Field label="Sharing password">
          <OPwdInput value="*************" visible/>
        </Field>
        <Field label="Confirm sharing password">
          <OPwdInput value="*************"/>
        </Field>
      </div>

      <WarningRow tone="amber">
        <span style={{ color: M_TOK.ink800 }}>Transmit via encrypted channel only.</span>
        <span style={{ color: M_TOK.ink600 }}> The bundle + sharing password together unlock everything in this profile for the bound sheet.</span>
      </WarningRow>
    </WizardBody>
  );
}

// ── Done: Bundle ready ──────────────────────────────────────────────────

function StepDone({ profile }) {
  const bundleJson =
`{
  "type": "mindvault-profile-share",
  "version": "2.1",
  "bundleId": "f9a1c8b2-e3d4-4a6b-9c8d-1e2f3a4b5c6d",
  "profileName": "${profile}",
  "exportedAt": "2026-05-24T18:42:11.094Z",
  "sheetId": "1BxCdefGh_aWxYz3_mnoPqrStuVwXyZ",
  "encryptedData": [42, 19, 188, 7, 240, 113, …],
  "iv":            [98, 22, 31, 4, 199, 17, 220, 56, 90, 162, 1, 245],
  "exportSalt":    [11, 209, 76, 33, 158, 244, …]
}`;
  return (
    <WizardBody
      eyebrow="Bundle ready"
      title="Encrypted bundle generated"
      lead="Save this JSON to a file or copy it. The recipient pastes it into Import Shared Profile and enters the sharing password you set."
    >
      <div style={{
        background: M_TOK.ink050,
        border: `1px solid ${M_TOK.ink300}`,
        borderRadius: 4,
        padding: M_SP(3),
        fontFamily: M_FONT.mono, fontSize: 11,
        color: M_TOK.ink700, lineHeight: 1.55,
        whiteSpace: "pre", overflow: "auto",
        maxHeight: 220,
      }}>{bundleJson}</div>

      <div style={{ display: "flex", gap: M_SP(2), marginTop: M_SP(3) }}>
        <OBtn kind="secondary" leading={<MCopyIcon size={11}/>}>Copy bundle</OBtn>
        <OBtn kind="secondary" leading={<Download/>}>Download .json</OBtn>
      </div>

      <WarningRow tone="amber">
        <span style={{ color: M_TOK.ink800 }}>Next:</span>
        <span style={{ color: M_TOK.ink600 }}> open this extension on the bound sheet (as Owner) to generate and set each account password from the derived secrets.</span>
      </WarningRow>
    </WizardBody>
  );
}

// ── Footer per state ─────────────────────────────────────────────────────

function ExportFooter({ state }) {
  if (state === "step1") return (
    <>
      <div/>
      <OBtn kind="primary">Continue <span style={{ marginLeft: 4 }}>→</span></OBtn>
    </>
  );
  if (state === "step2" || state === "step3") return (
    <>
      <OBtn kind="ghost">← Back</OBtn>
      <OBtn kind="primary">
        {state === "step3" ? "Generate bundle" : "Continue"}
        {state !== "step3" && <span style={{ marginLeft: 4 }}>→</span>}
      </OBtn>
    </>
  );
  return (
    <>
      <span style={{ fontFamily: M_FONT.mono, fontSize: 10.5, letterSpacing: "0.16em", color: M_TOK.moss, textTransform: "uppercase" }}>
        ● Complete
      </span>
      <OBtn kind="primary">Done</OBtn>
    </>
  );
}

// ── Shared body wrapper ──────────────────────────────────────────────────

function WizardBody({ eyebrow, title, lead, children }) {
  return (
    <div style={{ display: "flex", flexDirection: "column", gap: M_SP(4) }}>
      <div>
        <MCaption style={{ marginBottom: 6 }}>{eyebrow}</MCaption>
        <div style={{
          fontFamily: M_FONT.display, fontSize: 22, fontWeight: 400,
          letterSpacing: "-0.02em", color: M_TOK.ink900,
        }}>
          <span style={{ fontStyle: "italic" }}>{title}</span>
        </div>
        {lead && (
          <div style={{
            color: M_TOK.ink600, fontSize: 13, marginTop: 6, lineHeight: 1.55, maxWidth: 480,
          }}>{lead}</div>
        )}
      </div>
      {children}
    </div>
  );
}

function WarningRow({ tone = "amber", children }) {
  const bg = tone === "moss" ? M_TOK.mossBg + "70"
           : tone === "coral" ? M_TOK.coralBg + "70"
                              : M_TOK.amberBg + "70";
  const bd = tone === "moss" ? M_TOK.moss + "55"
           : tone === "coral" ? M_TOK.coral + "55"
                              : M_TOK.amber + "55";
  return (
    <div style={{
      background: bg, border: `1px solid ${bd}`,
      borderRadius: 4, padding: `${M_SP(2) + 2}px ${M_SP(3)}px`,
      fontSize: 12, lineHeight: 1.55,
      color: M_TOK.ink800,
    }}>{children}</div>
  );
}

function Field({ label, children }) {
  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
      <OLabel>{label}</OLabel>
      {children}
    </div>
  );
}

function Clip() {
  return (
    <svg width="11" height="11" viewBox="0 0 12 12" fill="none">
      <rect x="3" y="2" width="6" height="8" rx="1" stroke="currentColor" strokeWidth="1.2"/>
      <path d="M5 1H7V3H5V1Z" stroke="currentColor" strokeWidth="1.2"/>
    </svg>
  );
}
function Download() {
  return (
    <svg width="11" height="11" viewBox="0 0 12 12" fill="none">
      <path d="M6 2V8M3 5.5L6 8.5L9 5.5M2 10H10" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
  );
}

Object.assign(window, { OWizardExport });
