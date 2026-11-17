# Current release package audit —9November2026 (Asia/Saigon)

Status: **Fresh static audit Passed; runtime/signing acceptance remains separate**.

Independent tester inspected the existing unsigned release APK without rebuilding,
editing repository files or using the phone. SHA256:
`f2a86210fa254f5050bebd1086092dcd53dd87b7bb63b346c49c9ac11f57f264`.

- All3DEX/25,535class definitions checked: direct class-definition parsing matched
  official dexdump after each DEX checksum was verified. No own proof, AndroidX test,
  JUnit, desktop smoke/loopback or instrumentation subclasses; five selected proof
  string markers absent. This is a bounded marker/class audit, not a secret scan.
- Packaged manifest has expected production permissions, no instrumentation,
  debug flag or proof Activities; backup/full-backup disabled. Actual packaged
  `res/4j.xml` excludes all five storage domains from cloud backup/device transfer.
  Existing exported Google revocation service/profileinstaller receiver retain
  permission guards; the audit does not claim no exported dependency components.
- Debug APK positive comparator contains18own proof classes across13DEX and all
  three proof Activities are unexported. Both APK hashes remained unchanged.

Original host attempt failed strict UTF-8 presentation decoding of dexdump's MUTF-8;
that failed attempt is retained. The separate corrected presentation decode passed
exact class-list comparison; it does not alter packaged DEX bytes or checksums.

Evidence: `/home/vantt/.local/share/passchef-mobile-evidence/2026-11-09-release-package-audit/`;
reproducible `audit02.py`, `attempt02/summary.json` and `attempt02/provenance.json`.
This closes the fresh packaged manifest/DEX inspection gap in
[earlier local verification](current-verification-20261109.md), without upgrading
Google, phone, signing, publication or overall plan acceptance.
