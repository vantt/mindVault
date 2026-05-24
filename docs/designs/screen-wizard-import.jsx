// screen-wizard-import.jsx — Per spec 10-import-wizard.md
//
// 3-step modal wizard for importing a shared profile bundle.
//   Step 1: Paste / upload JSON · live validate
//   Step 2: Enter sharing password · decrypt
//   Step 3: Name profile · confirm

function OWizardImport({ state = "step1-valid" }) {
  // states:
  //   step1-empty   — paste field empty, Continue disabled
  //   step1-valid   — pasted, validated green
  //   step1-error   — invalid JSON / bad version
  //   step2         — sharing password prompt
  //   step2-error   — wrong password decrypt error
  //   step3         — name profile / locked sheet preview

  const stepMap = {
    "step1-empty": 1, "step1-valid": 1, "step1-error": 1,
    "step2": 2, "step2-error": 2,
    "step3": 3,
  };
  const step = stepMap[state] || 1;

  return (
    <OPage height="auto">
      <OAppBar profile/>
      <OTabBar active="profiles"/>
      <DimmedBg/>

      <OModalShell
        width={580}
        title={
          <>
            <span style={{ color: M_TOK.ink600 }}>Import </span>
            <span style={{ fontStyle: "italic" }}>shared profile</span>
          </>
        }
        sub={<div style={{ marginTop: 4 }}><OStepDots step={step} total={3}/></div>}
        footer={<ImportFooter state={state}/>}
      >
        {step === 1 && <ImpStep1 state={state}/>}
        {step === 2 && <ImpStep2 state={state}/>}
        {step === 3 && <ImpStep3/>}
      </OModalShell>
    </OPage>
  );
}

// ── Step 1 ───────────────────────────────────────────────────────────────

function ImpStep1({ state }) {
  const isEmpty = state === "step1-empty";
  const isErr   = state === "step1-error";
  const isOK    = state === "step1-valid";

  const sample = isEmpty ? "" :
`{
  "type": "mindvault-profile-share",
  "version": "${isErr ? "1.9" : "2.1"}",
  "bundleId": "f9a1c8b2-e3d4-4a6b-9c8d-1e2f3a4b5c6d",
  "profileName": "TeamFromB",
  "exportedAt": "2026-01-10T09:14:55.012Z",
  "sheetId": "1BxCdefGhIjklmnopqrSt...",
  "encryptedData": [42, 19, 188, …],
  "iv":            [98, 22, 31, …],
  "exportSalt":    [11, 209, 76, …]
}`;

  return (
    <WizImpBody
      eyebrow="Step 1 — bundle"
      title="Paste or upload the bundle"
      lead="A bundle is the JSON file the sender exported from their copy of mindVault. Paste the text below or attach the .json file."
    >
      <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
        <OLabel>Bundle JSON</OLabel>
        <div style={{
          background: M_TOK.ink050,
          border: `1px solid ${isErr ? M_TOK.coral + "88" : isOK ? M_TOK.moss + "55" : M_TOK.ink300}`,
          borderRadius: 4,
          padding: M_SP(3),
          height: 180,
          fontFamily: M_FONT.mono, fontSize: 11, color: isEmpty ? M_TOK.ink500 : M_TOK.ink800,
          lineHeight: 1.55,
          whiteSpace: "pre", overflow: "auto",
        }}>{sample || "Paste bundle JSON here…"}</div>

        <div style={{
          display: "flex", alignItems: "center", justifyContent: "space-between",
          marginTop: 4,
        }}>
          <OBtn kind="secondary" leading={<Upload/>}>Upload .json</OBtn>
          {isOK && (
            <span style={{ display: "inline-flex", alignItems: "center", gap: 6, color: M_TOK.moss, fontSize: 12 }}>
              <MCheckTiny color={M_TOK.moss} size={11}/>
              <span style={{ color: M_TOK.ink800 }}>Valid v2.1 bundle</span>
              <span style={{ color: M_TOK.ink500 }}>·</span>
              <span style={{ fontFamily: M_FONT.mono, fontSize: 11, color: M_TOK.ink700 }}>TeamFromB</span>
              <span style={{ color: M_TOK.ink500 }}>·</span>
              <span style={{ fontFamily: M_FONT.mono, fontSize: 11, color: M_TOK.ink700 }}>1BxCdefGhIjklm…</span>
            </span>
          )}
          {isErr && (
            <span style={{ display: "inline-flex", alignItems: "center", gap: 6, color: M_TOK.coral, fontSize: 12 }}>
              <Warning/>
              <span>Unsupported bundle version: <code style={{ fontFamily: M_FONT.mono }}>1.9</code></span>
            </span>
          )}
        </div>
      </div>
    </WizImpBody>
  );
}

// ── Step 2 ───────────────────────────────────────────────────────────────

function ImpStep2({ state }) {
  const isErr = state === "step2-error";
  return (
    <WizImpBody
      eyebrow="Step 2 — sharing password"
      title="Decrypt the bundle"
      lead="Enter the password the sender gave you. It’s checked locally — no network call."
    >
      <Field label="Sharing password">
        <OPwdInput value={isErr ? "********" : "*************"}/>
        {isErr && <OFieldNote tone="coral">Wrong password — could not decrypt.</OFieldNote>}
      </Field>

      <div style={{
        marginTop: M_SP(3),
        padding: `${M_SP(3)}px`,
        background: M_TOK.ink050,
        border: `1px solid ${M_TOK.ink200}`,
        borderRadius: 4,
        display: "grid", gridTemplateColumns: "auto 1fr",
        rowGap: 6, columnGap: M_SP(3),
        fontSize: 12, color: M_TOK.ink700,
      }}>
        <SummaryK>Profile</SummaryK>
        <span style={{ color: M_TOK.ink900, fontFamily: M_FONT.display, fontStyle: "italic" }}>TeamFromB</span>
        <SummaryK>Sheet</SummaryK>
        <span style={{ fontFamily: M_FONT.mono, fontSize: 11.5, color: M_TOK.ink800 }}>1BxCdefGhIjklmnopqrSt…</span>
        <SummaryK>Exported</SummaryK>
        <span style={{ fontFamily: M_FONT.mono, fontSize: 11.5, color: M_TOK.ink800 }}>2026-01-10</span>
      </div>
    </WizImpBody>
  );
}

function SummaryK({ children }) {
  return (
    <span style={{
      fontFamily: M_FONT.mono, fontSize: 9.5,
      letterSpacing: "0.18em", textTransform: "uppercase",
      color: M_TOK.ink500, alignSelf: "center",
    }}>{children}</span>
  );
}

// ── Step 3 ───────────────────────────────────────────────────────────────

function ImpStep3() {
  return (
    <WizImpBody
      eyebrow="Step 3 — name"
      title="Name this profile"
      lead="This name is local — only visible to you. The profile is locked to the sender's Sheet ID and is read-only."
    >
      <Field label="Local name">
        <OInput value="TeamFromB"/>
        <OFieldNote tone="muted">Max 50 chars. Stored under <code style={{ fontFamily: M_FONT.mono, color: M_TOK.ink700 }}>shared:TeamFromB</code>.</OFieldNote>
      </Field>

      <div style={{
        marginTop: M_SP(3),
        padding: `${M_SP(3)}px`,
        background: M_TOK.ink050,
        borderLeft: `2px solid ${M_TOK.ink400}`,
        fontSize: 12, color: M_TOK.ink700, lineHeight: 1.55,
      }}>
        <div style={{
          display: "inline-flex", alignItems: "center", gap: 6,
          fontFamily: M_FONT.mono, fontSize: 9.5,
          letterSpacing: "0.18em", textTransform: "uppercase",
          color: M_TOK.ink600, marginBottom: 6,
        }}>
          <MLockIcon size={10}/> Locked to sheet
        </div>
        <div style={{ fontFamily: M_FONT.mono, fontSize: 11.5, color: M_TOK.ink800, wordBreak: "break-all" }}>
          1BxCdefGhIjklmnopqrStuVwXyZab…
        </div>
        <div style={{ marginTop: 6, color: M_TOK.ink600 }}>
          This profile only generates passwords on that exact sheet. No manual assignment needed.
        </div>
      </div>
    </WizImpBody>
  );
}

// ── footer ───────────────────────────────────────────────────────────────

function ImportFooter({ state }) {
  if (state === "step1-empty") return (
    <><div/><OBtn kind="primary">Continue <span style={{ marginLeft: 4 }}>→</span></OBtn></>
  );
  if (state.startsWith("step1")) return (
    <><div/><OBtn kind="primary">Continue <span style={{ marginLeft: 4 }}>→</span></OBtn></>
  );
  if (state.startsWith("step2")) return (
    <>
      <OBtn kind="ghost">← Back</OBtn>
      <OBtn kind="primary">Decrypt <span style={{ marginLeft: 4 }}>→</span></OBtn>
    </>
  );
  return (
    <>
      <OBtn kind="ghost">← Back</OBtn>
      <OBtn kind="primary">Import</OBtn>
    </>
  );
}

function WizImpBody({ eyebrow, title, lead, children }) {
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
          <div style={{ color: M_TOK.ink600, fontSize: 13, marginTop: 6, lineHeight: 1.55, maxWidth: 480 }}>{lead}</div>
        )}
      </div>
      {children}
    </div>
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

function Upload() {
  return (
    <svg width="11" height="11" viewBox="0 0 12 12" fill="none">
      <path d="M6 9V2M3 4.5L6 1.5L9 4.5M2 10H10" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
  );
}
function Warning() {
  return (
    <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
      <path d="M6 1.5L11 10H1L6 1.5Z" stroke="currentColor" strokeWidth="1.2" strokeLinejoin="round"/>
      <path d="M6 5V7" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>
      <circle cx="6" cy="8.5" r=".7" fill="currentColor"/>
    </svg>
  );
}

Object.assign(window, { OWizardImport });
