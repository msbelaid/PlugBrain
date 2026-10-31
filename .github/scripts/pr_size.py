"""Counts the lines a pull request changes, leaving out tests and generated or bulk files.

Reads the JSON of `GET /repos/{repo}/pulls/{number}/files` (one array, or several
concatenated by `gh api --paginate --slurp`) from the path given as the first argument
and prints a Markdown summary. Exits 1 when the count is over MAX_LINES.
"""

import json
import os
import sys
from fnmatch import fnmatch

MAX_LINES = int(os.environ.get("MAX_LINES", "300"))

# fnmatch's * also matches "/", so "*/src/test*/*" covers any module and any test source set.
EXCLUDED = [
    "*/src/test*/*",  # unit and Paparazzi tests, testDebug, testFixtures
    "*/src/androidTest*/*",  # instrumented tests
    "*/snapshots/*",  # Paparazzi snapshots
    "fastlane/metadata/*",  # store listings and screenshots
    "*/res/values-*/strings.xml",  # translations
    "*.lockfile",
    "gradle/verification-metadata.xml",
]


def excluded(path):
    return any(fnmatch(path, pattern) for pattern in EXCLUDED)


def main():
    with open(sys.argv[1]) as f:
        data = json.load(f)
    # --slurp wraps each page in an outer array.
    files = [item for page in data for item in page] if data and isinstance(data[0], list) else data

    counted = [f for f in files if not excluded(f["filename"])]
    skipped = [f for f in files if excluded(f["filename"])]
    total = sum(f["additions"] + f["deletions"] for f in counted)
    skipped_total = sum(f["additions"] + f["deletions"] for f in skipped)

    status = "within" if total <= MAX_LINES else "over"
    print(f"### PR size: {total} lines ({status} the {MAX_LINES} line limit)\n")
    print(f"Counted {len(counted)} files. Left out {len(skipped)} test, generated or translation files ({skipped_total} lines).\n")
    if counted:
        print("| File | + | − |")
        print("|---|---|---|")
        for f in sorted(counted, key=lambda f: f["additions"] + f["deletions"], reverse=True)[:15]:
            print(f"| `{f['filename']}` | {f['additions']} | {f['deletions']} |")
    if total > MAX_LINES:
        print(f"\nSplit the PR, or add the `pr-size-approved` label if it can't be split (release, bulk rename).")
        sys.exit(1)


if __name__ == "__main__":
    main()
