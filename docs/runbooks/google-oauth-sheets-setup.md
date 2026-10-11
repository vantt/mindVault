# Google OAuth Console Setup Runbook — PassChef Mobile Sheets Client

**Audience:** you, the project owner, doing this by hand in the Google Cloud Console web UI.
**Purpose:** create a Google Cloud project and an OAuth client so the PassChef mobile Sheets
client (a JVM/desktop smoke-test today, an Android app later) can read and write your
password-recipe Google Sheet on your behalf.

**What this is not:** this runbook does not touch any code, and no `gcloud` CLI commands are
used anywhere in it — every step below is a click-path in the Console web UI, done manually by
you. No real Client ID, Client Secret, or token value is written into this document at any
point; when the steps below say "copy it somewhere safe," they mean your own local, gitignored
config file or a password manager — never this file, never a commit.

**Time estimate:** 15–20 minutes for a first-time setup.

---

## Before you start

The Console may group these screens under **Google Auth Platform**: Branding for app
information, Audience for user type/test users/publishing status, Data Access for
scopes, and Clients for OAuth client creation. Use those equivalent sections if the
older APIs & Services labels below redirect there.

- You need a Google account. A plain personal Gmail account works for everything in this
  runbook (see the Internal-app note in Step 4 if you additionally have a Google Workspace
  domain).
- You do **not** need to install anything locally — everything below happens at
  https://console.cloud.google.com in your browser.
- Keep this runbook open in one tab and the Cloud Console in another so you can follow along
  step by step.

---

## Step 1 — Create a Google Cloud project

1. Go to https://console.cloud.google.com in your browser and sign in with the Google account
   you want the Sheet to live under.
2. Click the project picker at the top of the page (it shows the current project name, or "Select
   a project" if none exists yet) → **New Project**.
3. Give it a name you'll recognize later, e.g. `passchef-mobile`. Leave "Location" as the
   default (`No organization`) unless you know you want otherwise.
4. Click **Create**. Wait for the notification bell (top right) to show "Project created" —
   this can take a few seconds.
5. Click the project picker again and select the new project so it's the active one (its name
   shows in the top bar next to the Google Cloud logo).

**Done when:** the top bar of the Console shows your new project's name as the active project.

---

## Step 2 — Enable the Google Sheets API

1. With the new project active, go to **APIs & Services → Library**
   (https://console.cloud.google.com/apis/library).
2. In the search box, type `Google Sheets API` and click the result.
3. Click **Enable**. Wait for the page to redirect to the API's overview/dashboard page.

**Done when:** the Sheets API page shows a **Manage** button (not an **Enable** button) —
that button flip is Google's own signal that the API is now on for this project.

---

## Step 3 — Configure the OAuth consent screen (Testing mode)

1. Open **Google Auth Platform → Branding** in the active Cloud project. For a
   new configuration, choose **Get Started**.
2. Enter the app name (e.g. `PassChef Mobile`) and support email. Continue through
   **Audience**, selecting **External** for a personal account. Add your developer
   contact email, accept the required Google API data-policy agreement if you agree,
   and finish creating the configuration.
3. Open **Audience → Test users → Add users**. Add the Google account that owns or
   can read your Menu, and save. Leave publishing status at **Testing**; do not
   publish the app for this development check.
4. Open **Data Access → Add or Remove Scopes**. Select or enter
   `https://www.googleapis.com/auth/spreadsheets`, apply the selection, and save.
   The client also requests this exact scope during sign-in.

**Done when:** Audience shows **Testing** and the intended test account; Data Access
lists the `spreadsheets` scope. These labels follow Google's current
[consent configuration guide](https://developers.google.com/workspace/guides/configure-oauth-consent).

---

## Step 4 — (Optional) Internal-app shortcut, if you have a Workspace domain

Skip this step entirely if you're using a plain personal `@gmail.com`-style account — it does
not apply to you, and Step 3 already covers your case.

If you *do* have a Google Workspace domain (e.g. a company or organization Google account) and
created the project under that organization, you can instead set **User Type** to
**Internal** when selecting Audience in Step 3 above, instead of
**External**. An Internal app has no 100-test-user cap and — more importantly —
its refresh tokens never expire on the 7-day Testing-mode timer described in Step 6, because
Internal apps are not subject to the "unverified app" restrictions at all. This is purely an
option, not a requirement: everything in this runbook works correctly on External/Testing too,
just with the 7-day refresh caveat in Step 6.

**Done when:** N/A (informational step; done as soon as you've decided which path applies to
your account).

---

## Step 5 — Create the OAuth client (Desktop app type)

1. Go to **Google Auth Platform → Clients** in the active project.
2. Click **Create client** (older Console navigation may call this
   **APIs & Services → Credentials → Create Credentials → OAuth client ID**).
3. For **Application type**, select **Desktop app**. (This is the correct type for this
   track — it uses the loopback-redirect flow, does not need a SHA-1 fingerprint or a package
   name, and is not affected by Google's loopback-flow deprecation, which only applies to other
   client types. See the forward-pointer note in Step 8 for why this is *not* the client type
   used by the eventual Android app.)
4. Give it a name, e.g. `passchef-mobile-desktop-smoketest`.
5. Click **Create**. Save the displayed **Client ID** and **Client Secret** before closing the dialog.
6. Click **Download JSON** (or copy the Client ID and Client Secret values directly) and save
   them into your own local, gitignored config file or a password manager — **never** paste
   them into this runbook, into any file tracked by git, or into a chat/issue. If you use the
   downloaded JSON file, keep it outside the repo or make sure its path is covered by
   `.gitignore` before it ever touches disk inside the project folder.

**Done when:** the **Clients** page lists your new OAuth client with type **Desktop**, and you have the Client ID + Client Secret
saved somewhere private (not in this repo).

---

## Step 6 — Understand the scope and the Testing-mode 7-day refresh-token limit

This step is informational — there is nothing to click here, just two things to know before
you use the credentials from Step 5.

**The scope this app requests:** the client code (built in a separate track of this same
feature) requests `https://www.googleapis.com/auth/spreadsheets` — full read **and** write
access to Sheets, not the read-only variant. This is intentional (locked decision D3): the
mobile app is allowed to append and update rows in your sheet, never delete them, and asking
for the broader scope now avoids having to re-consent with a different scope later.

**The 7-day refresh-token expiry:** because your OAuth consent screen is in **Testing** status
(Step 3) and not Google-verified, every refresh token Google issues to this app **expires after
7 days**. This is expected dev-time friction, not a bug: once a token expires, the fix is
simply to re-run the OAuth consent flow again (open the smoke-test tool, go through the
login/consent screen once more) to get a fresh token — no Console changes needed. If you set up
the Internal-app path in Step 4 instead, this 7-day limit does not apply to you.

**Done when:** you've read and understood both points above — no action to take, just
awareness before running the connection check. This one-time tool requests only an
access token for its current read; it does not request, persist, or refresh offline tokens.
Future offline mobile access must account for the Testing-mode refresh-token limit.

---

## Step 7 — Hand off the credentials to the client code

1. At the repo root, create `mobile/sheets-client/smoketest.local.properties` in
   **UTF-8** with these keys. Replace the placeholder values privately:

   ```properties
   clientId=YOUR_DESKTOP_CLIENT_ID
   clientSecret=YOUR_DESKTOP_CLIENT_SECRET
   spreadsheetId=YOUR_MENU_ID
   range='Tên Menu 🍳'!A1:Z
   ```

   The Menu ID is the segment between `/d/` and `/edit` in its Google Sheets URL.
   Use the exact tab name in the A1 range; quote names containing spaces or punctuation.
   Java properties treat backslashes as escapes: double a literal backslash.
   The range above is an example; choose a range covering your cookbook's accounts.
   This tool reads a properties file only, not environment variables.
2. Confirm the file is ignored before inserting private values:

   ```sh
   git check-ignore mobile/sheets-client/smoketest.local.properties
   git status --short
   ```

   The first command must print the config path; the second must not list it.
   Keep downloaded OAuth JSON outside the repository. Never put credentials in
   this runbook, tracked files, build arguments, or chat.
3. With JDK 21 available, run from the repo root:

   ```sh
   cd mobile/sheets-client
   ./gradlew runSheetsSmokeTest
   ```

   If using the portable JDK installed by this track, run from the repo root:

   ```sh
   export JAVA_HOME="$PWD/.toolchain/jdk"
   export PATH="$JAVA_HOME/bin:$PATH"
   cd mobile/sheets-client
   ./gradlew runSheetsSmokeTest
   ```

   See `docs/mobile-kmp-toolchain-setup.md` if the JDK needs installation.
4. Open the printed Google authorization URL in your browser and complete consent.
   Each attempt creates a fresh state and PKCE S256 challenge, waits on a local
   loopback port, verifies the returned state, and exchanges the code with the
   matching verifier. Unrelated, malformed, or forged callbacks do not end the
   pending sign-in. The wait expires after five minutes.
5. Confirm `Read N row(s) from the Menu.` and a successful Gradle run. Only a row
   count is printed; recipes, access tokens, and client seasoning are never printed.
   Missing/invalid config, denied or timed-out consent, token errors, network errors,
   and rejected reads exit nonzero. A 401 asks for fresh sign-in; a 403 asks you to
   check Menu sharing permissions, enabled API, requested scope, and test users.
   Error messages omit Google response bodies and private values.

**Done when:** your local config is ignored, the real sign-in/token exchange completes,
and one real Menu read succeeds. Creating this client and running this live check
remain the account owner's follow-up, outside this track's completed code deliverable.

---

## Step 8 — Forward pointer: a second OAuth client for the real Android app (not built now)

This step is a note, not an action — nothing to do today.

When the real Android app (a separate PBI, not part of this track) is built later, it will
need its **own**, second OAuth client of type **Android** (requiring a SHA-1 signing
fingerprint and the app's package name) created in the **same** Google Cloud project used
above. That Android-type client is a different credential from the Desktop-app client created
in Step 5 — the Desktop client stays in place for the JVM smoke-test tool and does not get
replaced or deleted when the Android client is added later. Do not attempt to create the
Android-type client now; there is no Android app yet to receive its redirect.

**Done when:** N/A (informational; nothing to check off today — revisit this step only when
the Android app track begins).

---

## Summary checklist

Use this to confirm the whole runbook is complete:

- [ ] Step 1 — Google Cloud project created and selected as active.
- [ ] Step 2 — Google Sheets API shows **Manage** (enabled) for the project.
- [ ] Step 3 — OAuth consent screen shows **Publishing status: Testing** with your email in
      **Test users** (or Step 4's Internal path chosen instead, if applicable).
- [ ] Step 5 — A **Desktop app**-type OAuth 2.0 Client ID exists under Credentials, with the
      Client ID + Secret saved privately outside this repo.
- [ ] Step 6 — You understand the `spreadsheets` scope and the 7-day Testing-mode
      refresh-token expiry (or that it doesn't apply, if you're on the Internal path).
- [ ] Step 7 — Client ID/Secret are wired into the client tool's local config, confirmed not
      committed to git.
- [ ] Step 8 — Noted for later: a separate Android-type OAuth client is still needed when the
      real Android app is built; not needed yet.
