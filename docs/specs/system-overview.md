# System Overview

One-page, technology-agnostic description of what this system does and how its
areas fit together. bee-scribing owns this file; it is the first read for any
human or agent new to the repository.

*(Full system map not yet written — most areas, including the browser
extension and the landing page, have not gone through a bee-scribing
bootstrap/harvest pass. The section below covers only the one area scribed so
far.)*

## Mobile Sheets Connection

A foundational connection layer for a not-yet-built mobile app: it lets code
read and update the account owner's Google Sheet (the same sheet the browser
extension already works with) using a permission the owner grants once, by
hand, outside this product. See `docs/specs/mobile-sheets-client.md` for the
full spec. No mobile app screens consume it yet — today it is the connection
itself, a one-time setup guide, and a one-time check that proves the
connection works.
