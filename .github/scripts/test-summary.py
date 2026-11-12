"""Writes JUnit XML results to the job summary and adds an error annotation per failed test.

Usage: test-summary.py <title> <glob> [<glob> ...]
Globs are relative to the working directory, e.g. '**/build/test-results/**/*.xml'.
Uses only workflow commands, so it needs no extra token permissions and works on fork PRs.
"""
import glob
import os
import sys
import xml.etree.ElementTree as ET

MAX_MESSAGE = 1500
MAX_ANNOTATIONS = 10  # GitHub shows at most 10 error annotations per step.


def escape(text):
    # Workflow command values can't hold raw newlines or percent signs.
    return text.replace("%", "%25").replace("\r", "").replace("\n", "%0A")


def escape_property(text):
    return escape(text).replace(":", "%3A").replace(",", "%2C")


def main():
    title, patterns = sys.argv[1], sys.argv[2:]
    files = sorted({f for p in patterns for f in glob.glob(p, recursive=True)})
    total = skipped = 0
    failures = []
    for path in files:
        try:
            root = ET.parse(path).getroot()
        except ET.ParseError as error:
            print(f"Skipping {path}: {error}")
            continue
        for case in root.iter("testcase"):
            total += 1
            if case.find("skipped") is not None:
                skipped += 1
                continue
            problem = case.find("failure")
            if problem is None:
                problem = case.find("error")
            if problem is None:
                continue
            name = f"{case.get('classname', '?').rsplit('.', 1)[-1]}.{case.get('name', '?')}"
            message = (problem.get("message") or problem.text or "").strip()
            failures.append((name, message[:MAX_MESSAGE]))

    passed = total - skipped - len(failures)
    lines = [f"### {title}", ""]
    if not files:
        lines.append("No test results found.")
    else:
        lines.append(f"{passed} passed, {len(failures)} failed, {skipped} skipped.")
    if failures:
        lines += ["", "| Failed test | Message |", "|---|---|"]
        for name, message in failures:
            first_line = message.splitlines()[0] if message else ""
            cell = first_line.replace("|", "\\|")[:200]
            lines.append(f"| `{name}` | {cell} |")
        for name, message in failures[:MAX_ANNOTATIONS]:
            print(f"::error title={escape_property(title + ' - ' + name)}::{escape(message or 'Failed')}")
    summary = os.environ.get("GITHUB_STEP_SUMMARY")
    if summary:
        with open(summary, "a", encoding="utf-8") as out:
            out.write("\n".join(lines) + "\n\n")
    print("\n".join(lines))


if __name__ == "__main__":
    main()
