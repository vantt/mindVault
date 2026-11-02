# A10 — Independent Sync/upgrade protocol review

Date:2026-10-26, Asia/Saigon. Reviewer:independent agent, not protocol author.
Source:HEAD039c76d + preserved post-FA01 WIP. Scope:[A10 protocol](a10-sync-upgrade-protocol.md).
Verdict:**Passed for protocol preparation**. All13real S00–S08/U01–U04 cases NotRun.

## Review

Protocol requires two physical devices,same extension identity and authorized Sync
account/installation mode;local profiles/copied JSON cannot substitute. A nonsecret
transport prerequisite precedes Cookbook setup. Public-key preparation,Dashboard/
installation routes,accounts and grants retain explicit owner gates. No assumed
cross-machine unpacked identity or permission to upload/publish.

Reviewed source routing,encrypted records/salt/default/maps,local/session boundaries,
rotation and FA01 import behavior. Cases cover online/offline/disjoint/same-record
conflicts,rotation/quota/recovery/import and complete preservation evidence. No
distributed transaction,field merge,instant remote lock or full backup guarantee.
Bounded settle policy distinguishes its test budget from a platform SLA;failures
retain both snapshots and stop dependents rather than silently retrying to Pass.

Same-version2.3.0 source replacement,greater-version installed upgrade and actual
Store delivery are separate lanes. Reload/update session clearing differs from
worker wake preserving the absolute deadline. Old/new IDs,versions,source/hash,
formats and observed entry events are required. No automatic rollback,Reset,
salt rewrite or real-data cleanup. Failed migration/preservation remains Failed.
FA01's old malformed records are not claimed repaired by an upgrade.

## Independent local checks

- Re-read current worker/background/reader/rotation/import sources and protocol
  expectations;current Dish display is visible text,not an invented Reveal control.
- Parsed the synthetic fixture JSON and verified its Recipe;earlier independent
  production Cook checks establish the stated public practice Dish,not Sync.
- Current manifest2.3.0 has no `key`;both reference ZIP hashes match files:
  frozen73-file`6c6fc80055c61d49cba404694fa8691a1c41316bccc2a14b491e930995fc0fe9`
  and post-FA01 74-file`b4986a20b48da3b7a519c09b02ed19ebd409ace8aeb50781cf61b7fbd1fac4de`.
- All13case IDs and12local file targets present/resolve;`git diff --check` Passed.

Only report/protocol work;no unchanged unit-suite replay,browser/Sync/update/provider,
account,clipboard,ADB/Gradle or publication. Official Chrome sources are dated and
linked by the author;final case execution/installation feasibility remain unverified.

## Handoff and unresolved

A10 can be Done;next A12. B08's A10 prerequisite is satisfied,but A13/C02,C03 where
real Sync is selected,and required desktop/device resources remain Pending.
Overall A23/UI01/C01 and actual browser/upgrade/restore acceptance remain open.
