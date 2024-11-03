Fixing Firefox's Session Restore After Restart
==============================================

Firefox doesn't seem to respect the "open previous windows" setting when
the browser is closed for a restart. As someone who likes to start from
a clean slate, this was frustrating me quite a bit. I decided to fix it
the hard way.

----------

Tabs Restoring is a Known Bug in Firefox
----------------------------------------

There is a [bug report] in Mozilla's tracker describing this exact issue.
Inexplicably, however, the bug is closed as "invalid" with a comment that
describes the issue, but provides no workaround or justification for the
behavior, instead simply stating why the bug happens.

[bug report]: https://bugzilla.mozilla.org/show_bug.cgi?id=1604709

Automatically Deleting Previous Sessions on Startup
---------------------------------------------------

I decided to fix this myself, admittedly in a hacky way. What I did is
create a service that runs on startup and deletes the previous session
store files. This is potentially dangerous, so proceed at your own risk,
but it works for me at the time of writing.

The session store files are located in Firefox's profile directory, which
can be found by going to `about:support`. Using the directory in that, the
`find` command can be used to delete files within any profiles'
`sessionstore-backups` folder. I am running Fedora/Linux, so mine is located
in `$HOME/.mozilla/firefox`. The following code should be modified to match
your system directory:

```bash
find "$HOME/.mozilla/firefox" -path "*/sessionstore-backups/*" -type f -print -exec rm -f "{}" \;
```

All you need to do is put this in a bash script to run at Startup.
In Fedora/linux, this can be done with a systemd service:

```ini
[Unit]
Description=Delete Firefox Sessions

[Service]
Type=oneshot
ExecStart=/bin/bash -l -c 'exec "$@"' _ find "$HOME/.mozilla/firefox" -path "*/sessionstore-backups/*" -type f -print -exec rm -f "{}" \;

[Install]
WantedBy=default.target
```

Other systems may follow the same pattern in whatever startup mechanism they
choose, so long as the script is run before Firefox launches.
