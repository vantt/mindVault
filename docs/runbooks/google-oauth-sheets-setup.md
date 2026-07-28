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

1. Go to **APIs & Services → OAuth consent screen**
   (https://console.cloud.google.com/apis/credentials/consent).
2. Choose **User Type**:
   - Pick **External** if you're using a plain personal Google account (this is the common
     case — see the Internal-app note below if you have a Workspace domain instead).
   - Click **Create**.
3. Fill in the required **App information** fields: an app name (e.g. "PassChef Mobile"), your
   own email as the **User support email**, and your own email again under **Developer contact
   information**. Everything else on this page can stay blank/default.
4. Click **Save and Continue** through the **Scopes** page without adding anything here — the
   scope itself is requested by the client code at consent time, not pre-registered on this
   screen (click **Save and Continue**).
5. On the **Test users** page, click **+ Add users**, enter your own Google account email
   address, and click **Add**, then **Save and Continue**.
6. On the **Summary** page, click **Back to Dashboard**. Do **not** click any "Publish App"
   button — leaving the app unpublished is exactly what keeps it in **Testing** status, which
   is what this track needs (see the caveat in Step 6 below).

**Done when:** the OAuth consent screen dashboard shows **Publishing status: Testing**, and
your own email address appears in the **Test users** list.

---

## Step 4 — (Optional) Internal-app shortcut, if you have a Workspace domain

Skip this step entirely if you're using a plain personal `@gmail.com`-style account — it does
not apply to you, and Step 3 already covers your case.

If you *do* have a Google Workspace domain (e.g. a company or organization Google account) and
created the project under that organization, you can instead set **User Type** to
**Internal** when you reach the "Choose User Type" action in Step 3 above (item 2), instead of
**External**. An Internal app has no 100-test-user cap and — more importantly —
its refresh tokens never expire on the 7-day Testing-mode timer described in Step 6, because
Internal apps are not subject to the "unverified app" restrictions at all. This is purely an
option, not a requirement: everything in this runbook works correctly on External/Testing too,
just with the 7-day refresh caveat in Step 6.

**Done when:** N/A (informational step; done as soon as you've decided which path applies to
your account).

---

## Step 5 — Create the OAuth client (Desktop app type)

1. Go to **APIs & Services → Credentials**
   (https://console.cloud.google.com/apis/credentials).
2. Click **+ Create Credentials → OAuth client ID**.
3. For **Application type**, select **Desktop app**. (This is the correct type for this
   track — it uses the loopback-redirect flow, does not need a SHA-1 fingerprint or a package
   name, and is not affected by Google's loopback-flow deprecation, which only applies to other
   client types. See the forward-pointer note in Step 8 for why this is *not* the client type
   used by the eventual Android app.)
4. Give it a name, e.g. `passchef-mobile-desktop-smoketest`.
5. Click **Create**. A dialog appears showing a **Client ID** and **Client Secret**.
6. Click **Download JSON** (or copy the Client ID and Client Secret values directly) and save
   them into your own local, gitignored config file or a password manager — **never** paste
   them into this runbook, into any file tracked by git, or into a chat/issue. If you use the
   downloaded JSON file, keep it outside the repo or make sure its path is covered by
   `.gitignore` before it ever touches disk inside the project folder.

**Done when:** the **Credentials** page lists your new OAuth 2.0 Client ID under
**OAuth 2.0 Client IDs** with type **Desktop**, and you have the Client ID + Client Secret
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
awareness before running the smoke-test tool for the first time (and again whenever it reports
an expired/invalid token after ~7 days of inactivity).

---

## Step 7 — Hand off the credentials to the client code

1. The Sheets API client module (built in a separate track of this feature) reads the Client
   ID and Client Secret from a local config file or environment variables — it never has them
   hardcoded in source. Follow that module's own README/setup instructions for exactly where
   to place the values you saved in Step 5.
2. Do not commit that config file. Confirm it is listed in `.gitignore` before running
   anything, and confirm with `git status` that it does not show up as a new/untracked file
   ready to be added.

**Done when:** `git status` in the repo shows no new file containing your Client ID/Secret,
and the client tool's own setup doc confirms where it expects the values.

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
