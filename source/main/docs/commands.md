# Weave Commands

Commands are provided to Weave via stdin (stream) or from a file; the format is identical in both cases.

## Command Line
The following command line parameters are supported:

    --stream            Read commands from stdin
    --file <file>       Read commands from the specified file

## Format
Each line is a single command:

    <command> <parameter>...

- Separate parameters with spaces.
- Wrap parameters containing spaces in quotes.
- Lines are executed sequentially; failures trigger rollback where supported.

## Supported Commands

### header
Set the UI header (not the progress message). Useful for grouping related steps.

Syntax:

    header "<text>"

Example:

    header "Preparing update"

---

### log
Write a message to the log.

Syntax:

    log "<message>"

Example:

    log "Backing up files"

---

### elevated-log
Like `log`, but will request elevation if the current process is not elevated.

Syntax:

    elevated-log "<message>"

Note: Elevation is only requested when required by the platform and current process privileges.

---

### pause
Pause execution for a specified time (milliseconds). Optionally display a message while waiting.

Syntax:

    pause <milliseconds> ["<message>"]

Notes:
- Minimum effective delay is 100 ms.

Example:

    pause 2000 "Allow services to settle"

---

### elevated-pause
Same as `pause`, but will request elevation if the current process is not elevated.

Syntax:

    elevated-pause <milliseconds> ["<message>"]

---

### execute
Synchronously start a process and wait for it to exit.

Syntax:

    execute <workingDir> <executable> [args...]

Examples:

    execute /usr/share/myapp /usr/bin/java -jar mytool.jar --verify
    execute . ./scripts/migrate.sh

---

### launch
Asynchronously start a process without waiting for completion. The launcher retries starting for a short timeout window for robustness.

Syntax:

    launch <workingDir> <executable> [args...]

Examples:

    launch /usr/share/myapp /usr/bin/java -jar program.jar
    launch . myapp.exe --start-minimized

Notes:
- Retries are attempted up to an internal timeout (currently ~2 seconds) with short delays between attempts.

---

### delete
Delete a file or folder (recursively for folders). Will request elevation if the target exists but is not writable.

Syntax:

    delete <path>

Examples:

    delete /usr/share/myapp/old-cache
    delete C:\\ProgramData\\MyApp\\temp\\stale.tmp

---

### move
Move a file or folder to a new path. Creates missing target directories when needed. Requests elevation when required by permissions.

Syntax:

    move <source> <target>

Examples:

    move /opt/myapp/data.new /opt/myapp/data
    move C:\\MyApp\\logs C:\\MyApp\\archive\\logs-2025-12-31

Notes:
- Fails if the target already exists.
- On rollback, attempts to move the target back to the original source.

Alias:

    rename <source> <target>

`rename` maps to the same behavior as `move`.

---

### permissions
Set file permissions on one or more files using a numeric mask (e.g., 755). The same mask is applied to all specified files.

Syntax:

    permissions <mask> <path1> [path2 ...]

Examples:

    permissions 755 /usr/share/myapp/program.sh
    permissions 644 /usr/share/myapp/conf.d/*.conf

Notes:
- A leading digit (e.g., 0755) is accepted; it will be normalized internally.
- Elevation is requested if any target exists but is not writable with current privileges.

---

### unpack
Overlay the contents of a zip file onto a target directory (atomic per-file staging/commit with hash validation). Creates the target directory if it does not exist.

Syntax:

    unpack <zipFile> <targetDir>

Example:

    unpack update.zip /usr/share/myapp

Notes:
- If `<targetDir>` doesn’t exist, its parent must be writable (or elevation will be requested).
- Existing files are staged with a temporary `.del`/`.add` mechanism and then committed with hash checks to ensure integrity.
- On failure during staging, changes are reverted.

---

### fail
Intentionally fail with the provided message (useful for testing/guard rails).

Syntax:

    fail "<message>"

Example:

    fail "Detected unsupported environment"

## Examples
Update then launch:

    header "Updating core modules"
    unpack update.zip /usr/share/myapp
    permissions 755 /usr/share/myapp/bin/run.sh
    launch /usr/share/myapp /usr/bin/java -jar /usr/share/myapp/app.jar

Clean up, wait, then restart:

    header "Cleanup and restart"
    delete /usr/share/myapp/cache
    pause 1500 "Draining requests"
    launch /usr/share/myapp /usr/bin/java -jar /usr/share/myapp/app.jar --restart

## Notes on Elevation
- The following may request elevation depending on file system permissions: `delete`, `move`/`rename`, `permissions`, `unpack`.
- `elevated-log` and `elevated-pause` explicitly request elevation if the current process isn’t already elevated.
- Process-launching commands (`execute`, `launch`) themselves do not request elevation; if the launched process needs elevation, wrap this tool appropriately in your environment.
