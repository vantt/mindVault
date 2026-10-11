# Finish and integrate the OAuth/Menu client worktree

Date: 2026-10-11
Worktree: `/home/vantt/projects/passMan--wt--passchef-mobile-oauth-sheets`
Branch: `wt/passchef-mobile-oauth-sheets`
Base: local `main`, currently `ef720b1`.

## Outcome and boundary

Finish the accepted Track D code deliverable: an independent Kotlin module
that reads and updates recipes in the Google Menu, an OAuth setup runbook,
and a compiled JVM connection-check tool. Preserve the accepted boundary:
creating a real Google OAuth client and running the live check remain the
account owner's follow-up. Keep deferred workflow-tooling edits recoverable.

## Product repairs

1. Add a fresh OAuth state value and PKCE S256 challenge to each authorization
   attempt; verify callback state before accepting a code and send the matching
   verifier during token exchange. Reject missing/mismatched state and malformed
   callbacks. Add deterministic regression tests using synthetic values.
2. Encode Menu ranges as UTF-8 bytes before percent encoding. Test Vietnamese,
   supplementary characters/emoji, spaces, punctuation, and exact request paths.
   Default writes to RAW so usernames and recipes remain literal text; test
   formula-like values. Load properties as UTF-8 and test Vietnamese/emoji values.
3. Make HTTP-client ownership explicit and close resources on success/failure.
   Test cleanup where practical without contacting Google.
4. Complete the runbook's properties/environment configuration and the actual
   module command for the connection check. Document state/PKCE and expected
   authentication failures without printing tokens or private seasoning.
   Make connection-check failures exit nonzero with sanitized messages, including
   token-endpoint failures; test exit/error handling using synthetic responses.

## Verification and ordinary review

5. Run the module's affected regression tests, then `./gradlew test build` from
   `mobile/sheets-client` with the existing portable JDK. Confirm the connection
   tool compiles; do not start its live Google flow.
6. Independently review the product diff and runbook, repair actionable findings,
   and repeat only checks affected by those repairs. Record results and any
   remaining live-check limitation in the recovery report.
7. Commit only the reviewed product repairs, their tests, docs, and this plan.
   Do not stage deferred `.bee/bin`, onboarding, or skill edits.

## Preserve, integrate, and close

8. Before integration, record exact main/worktree HEADs, dirty path lists, and
   recovery refs. Save tracked and untracked WIP with descriptive Git stashes;
   retain their immutable IDs and do not drop them. No `stash --all`: ignored
   toolchains/builds and private configuration require separate handling.
9. Recheck overlaps/collisions. Current main overlaps are `.gitignore` and
   `.bee/decisions.jsonl`; no untracked path currently collides with branch
   additions. Main and OAuth deferred tooling files presently have matching
   hashes, but preserve both snapshots before relying on that observation.
10. Merge the completed branch into clean local main with `git merge --ff-only`
    when ancestry permits; otherwise inspect divergence before proceeding.
    Apply the main WIP stash while keeping its recovery ref. Preserve both
    ignore-rule blocks and all distinct decision events if restoration conflicts.
    Keep restored unrelated WIP out of integration commits.
11. Verify merged product tests, committed ancestry, and preservation of main's
    WIP. Save a recovery manifest outside the checkout with commit/stash IDs,
    path inventories, and the deferred tooling snapshot. Archive the complete
    selected checkout, including ignored toolchain/runtime files, before closure;
    verify its inventory without displaying private configuration or deleting data.
12. Close the OAuth worktree only after recoverable files are accounted for and
    main is verified. Use ordinary Git worktree cleanup; keep the branch and
    stash recovery refs until the deferred tooling work is explicitly resumed.

## Known constraints

- Existing accepted commits include historical workflow records; integration
  retains that history but introduces no new workflow-tooling changes.
- Main currently has 90 tracked changes and 30 untracked status entries.
- OAuth worktree currently has 26 tracked tooling edits and 32 untracked files.
- No change to extension/landing-page dependencies or golden tests is planned.
