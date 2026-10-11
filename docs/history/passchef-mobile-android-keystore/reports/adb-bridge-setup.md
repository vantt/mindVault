# ADB Bridge Setup — Windows Host to WSL2 (cell android-keystore-e2)

**Status:** BLOCKED — bridge works, no physical device currently attached to the Windows host.

## Working setup (confirmed)

Per D6 (real physical device only, no emulator, no faked state) and the ADB_SERVER_SOCKET
path chosen over usbipd-win (no driver re-bind dance, survives replug, Windows-side tools
keep the device too).

1. **Host IP reachable from WSL2:** `cat /etc/resolv.conf` → `nameserver 192.168.32.1`
   (standard WSL2 host-IP trick; matches `ip route show | grep default` gateway).
2. **Start adb.exe as a TCP-reachable server on the Windows host**, bound to all interfaces:
   ```
   /mnt/c/Users/van.tran_fgorg/AppData/Local/Android/Sdk/platform-tools/adb.exe -a server start
   ```
   (`-a` binds all interfaces instead of localhost-only; the command self-daemonizes into a
   background Windows process — confirmed it survives independently across separate shell
   invocations, no `nodaemon` foreground/backgrounding needed.)
3. **Point the WSL2 adb client at it via `ADB_SERVER_SOCKET`:**
   ```
   export ADB_SERVER_SOCKET=tcp:192.168.32.1:5037
   export PATH=$HOME/dev-tools/android-sdk/platform-tools:$PATH
   ```
   Persisted in `~/.bashrc` (appended, effective in new **interactive** shells).
   **Caveat found:** this repo's `~/.bashrc` has the standard Debian early-return guard for
   non-interactive shells (`case $- in *i*) ;; *) return;; esac`), so non-interactive tool
   invocations (e.g. bee worker Bash calls) do not pick up the persisted var by sourcing
   `~/.bashrc` — they must set `ADB_SERVER_SOCKET` explicitly per-command, exactly as the
   cell's own `verify` string does via inline `KEY=value` prefixes. A real interactive human
   terminal is unaffected by this guard.

## Evidence the bridge is real (not a silent local-daemon fallback)

- With the Windows adb.exe server running, WSL2 client `adb devices -l` (via
  `ADB_SERVER_SOCKET=tcp:192.168.32.1:5037`) returns cleanly: `List of devices attached`
  (empty list — see below).
- Killed the Windows-side server (`adb.exe kill-server`) and re-ran the WSL2 client with the
  same `ADB_SERVER_SOCKET` — it hung and timed out (`timeout 8 adb devices` → exit 124),
  proving the WSL2 client was genuinely talking to the Windows-hosted daemon over TCP, not
  silently falling back to a local WSL2 daemon.
- Restarted the Windows adb.exe server (`adb.exe -a server start`) — WSL2 client bridge
  connectivity resumed immediately (`List of devices attached`, exit 0).
- Re-verified in a fresh, independent shell invocation (no continuity with the shell that
  started the server) — Windows server and bridge both still up and responsive, confirming
  the Windows-side adb.exe process persists as its own OS process, not tied to the WSL2
  shell/session that launched it.

## Verify command run (cell's exact `verify` string)

```
JAVA_HOME=$HOME/dev-tools/jdk-17.0.20+8 PATH=$HOME/dev-tools/android-sdk/platform-tools:$PATH adb devices | grep -qw device
```

Output:
```
* daemon not running; starting now at tcp:5037
* daemon started successfully
```
Exit code: `1` (fail — correctly does not pass with zero devices attached, satisfying the
cell's prohibition that verify must fail-closed when no device is present).

Note: this exact stored command does not itself set `ADB_SERVER_SOCKET`, so run standalone
(without the persisted-but-guarded `~/.bashrc` export active) it spins up a **local** WSL2
adb daemon rather than using the Windows-host bridge. In the current all-devices-absent
state the observable result is identical either way (no device found, fail-closed exit 1),
so this was not a blocker for today's run — but it means: once a device is actually
attached to the Windows host, this verify string must be run with `ADB_SERVER_SOCKET`
explicitly exported in the same command (or in a shell where it was actually sourced
interactively) or it will keep reporting zero devices even with a device physically present.
Flagging this for whoever resumes this cell.

## What's blocking

`adb devices` (via the confirmed-working bridge) shows zero devices — no physical Android
device is currently connected to the Windows host. This is the expected real-world
precondition gap called out by the orchestrator, not a bridge defect: the bridge itself is
proven working end-to-end (start/kill/restart test above).

The remaining must-haves for this cell — `adb devices` listing the device in `device` state,
installing `app/build/outputs/apk/debug/app-debug.apk` via `adb install -r`, launching it
(`adb shell am start -n dev.passchef.mobile/.MainActivity`), and a clean logcat capture with
no `FATAL EXCEPTION` — all require a physically attached, USB-debugging-enabled device and
could not be exercised.

## Ask

Please connect your Android device via USB to the Windows host and enable USB debugging,
then this cell can proceed (re-run `adb devices` via the bridge above — should show the
device in `device` state, possibly requiring a one-time on-device "Allow USB debugging?"
authorization prompt — then install/launch/logcat can complete).

## Full trace

See `.bee/cells/android-keystore-e2.json` for the cell record. This cell was **not capped** —
no verify pass was recorded, per the must-haves gap above.
