# Implementation Plan - Phase 1: Core Infrastructure (Clean Architecture)

**Goal:** Establish the foundational security and storage layer using Hexagonal Architecture (Ports & Adapters) to ensure testability and separation of concerns.

## 1. User Review Required

> [!IMPORTANT] > **Architecture Decision:** We are adopting **Clean Architecture** for a Chrome Extension.
>
> - **Core (Domain & Use Cases):** Pure JS, no Chrome APIs. Testable in Node/Jest.
> - **Adapters:** Handle Chrome Storage, Argon2 WASM, DOM interaction.
> - **Composition Root:** Service Worker & UI Entry points wiring everything together.

## 2. Proposed Changes

### Directory Structure

#### [NEW] [Folder Layout]

- `src/core/domain/` - Entities (Formula, Secret, Vault)
- `src/core/ports/` - Interfaces (ICrypto, IStorageRepository, IParser)
- `src/core/usecases/` - Application Login (GeneratePassword, UnlockVault)
- `src/adapters/infrastructure/` - Implementations (ChromeStorage, Argon2Wasm)
- `src/adapters/ui/` - View logic (Popup, messaging)
- `src/manifest.json`

### Component: Core (Pure Logic)

#### [NEW] [src/core/domain/formula.js](file:///d:/_1.FWG_PARA/1.Projects/dev/toys/passMan/src/core/domain/formula.js)

- Entity describing `<hash><id><mod>`. Validation logic independent of Parsing.

#### [NEW] [src/core/usecases/generate_password.js](file:///d:/_1.FWG_PARA/1.Projects/dev/toys/passMan/src/core/usecases/generate_password.js)

- Implements the "Business Logic" of combining Secret + Hash.
- Accepts `ICrypto` and `IStorageRepository` injection.

### Component: Adapters (Infrastructure)

#### [NEW] [src/adapters/infrastructure/argon2_adapter.js](file:///d:/_1.FWG_PARA/1.Projects/dev/toys/passMan/src/adapters/infrastructure/argon2_adapter.js)

- Implements `ICrypto`.
- Wraps `argon2-browser` dependencies.

#### [NEW] [src/adapters/infrastructure/chrome_storage_adapter.js](file:///d:/_1.FWG_PARA/1.Projects/dev/toys/passMan/src/adapters/infrastructure/chrome_storage_adapter.js)

- Implements `IStorageRepository`.
- Wraps `chrome.storage.sync` and `chrome.storage.session`.

#### [NEW] [src/adapters/infrastructure/regex_parser_adapter.js](file:///d:/_1.FWG_PARA/1.Projects/dev/toys/passMan/src/adapters/infrastructure/regex_parser_adapter.js)

- Implements `IParser`.
- Contains the Regex Logic from PRD v1.2.

### Component: Manifest V3

#### [NEW] [manifest.json](file:///d:/_1.FWG_PARA/1.Projects/dev/toys/passMan/src/manifest.json)

- Configured to expose `adapters/primary/content` and `background`.

## 3. Verification Plan

### Automated Tests (Crucial benefit of Clean Arch)

- **Domain Tests (No Browser Required):**
  - Test `GeneratePassword` use case by mocking `ICrypto` and `IStorage`.
  - Verify business rules (rotation, formula composition) run in pure Node.js environment.
- **Adapter Tests:**
  - Mock `chrome.*` APIs to test `ChromeStorageAdapter`.

### Manual Verification

1.  **Dependency Injection Check:** Inspect `service-worker.js` (Composition Root) to ensure Adapters are correctly injected into Use Cases.
2.  **End-to-End:**
    - Trigger Hotkey -> Adapter reads DOM -> Calls Use Case -> Adapter shows Popup.
