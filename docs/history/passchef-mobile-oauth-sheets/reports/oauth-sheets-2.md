# Cell oauth-sheets-2 — Report

**Status:** [DONE]

**Outcome:** Wrote the Google OAuth Console setup runbook at
`docs/runbooks/google-oauth-sheets-setup.md` — 8 numbered steps + a summary checklist,
each step with an explicit done-condition. Covers: GCP project creation, Sheets API
enablement, OAuth consent screen in Testing mode + test user, Desktop-app-type OAuth
client (loopback flow, no SHA-1/package name), the `spreadsheets` read/write scope (D3),
the 7-day refresh-token expiry caveat for unverified Testing-mode apps, the Internal-app
shortcut for Workspace-domain accounts, and a forward pointer noting a separate
Android-type OAuth client will be added later for the real app (p-c46b217c). Performed
the required dry-run read-through of the full sequence and fixed one ambiguous
cross-reference found during that pass (Step 4's reference to "Step 3.2" was unclear,
reworded to point at the named sub-action instead).

**Files touched:** `docs/runbooks/google-oauth-sheets-setup.md` (new file)

**Verify:** passed — see `.bee/cells/oauth-sheets-2.json` for the full trace and recorded
verify output.

**Commit:** `ac2f0a4` — "docs: add Google OAuth Console setup runbook (oauth-sheets-2)"
