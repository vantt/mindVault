# Agent Rules

> **Target Audience:** Antigravity (AI), Claude Code, Amp Code, Windsurf, Cursor, or any other AI Agent working on this repo.
> **Status:** MANDATORY - READ BEFORE ACTING.

## Critical Protocols

### 1. Test Case Protocol (The "Golden" Rule)

**Principle:** Test cases are the **Source of Truth**. Your primary job is to make code pass the tests, NOT to make tests pass the code.

#### A. Identification (Marking)

To explicitly identify "Golden" tests that must not be touched, use these markers in `.js` or `.ts` files:

- **Comments:** `// @golden` or `// @approved`
- **Docblocks:**
  ```javascript
  /**
   * @status GOLDEN
   */
  ```

#### B. Immutability & Modification Policy

- **Absolute Ban:** NEVER automatically fix, modify, or delete a marked test case to resolve a failure.
- **Protocol when a "Golden" test fails:**
  1.  **Assume Code Error:** The bug is in your implementation, not the test.
  2.  **Stop & Report:** If you strongly believe the test itself is wrong/outdated, **STOP**. discuss with the user.
  3.  **Approval Required:** specific explicit approval is required to touch any marked test.
- **Rule of thumb:** If in doubt, skip the test temporarily. NEVER auto-fix.
