#!/usr/bin/env python3
"""
Offline validation of the bundled EQuran snapshot.
Used by GitHub Actions; does not touch the network.
"""
import hashlib
import json
import os
import sys

ASSET_ROOT = os.path.join(
    os.path.dirname(__file__), "..", "app", "src", "main", "assets", "equran", "v2"
)


def sha256_file(path: str) -> str:
    digest = hashlib.sha256()
    with open(path, "rb") as handle:
        while chunk := handle.read(65536):
            digest.update(chunk)
    return digest.hexdigest()


def fail(message: str) -> "NoReturn":
    print(f"BUNDLE VALIDATION FAILED: {message}", file=sys.stderr)
    raise SystemExit(1)


def main() -> None:
    manifest_path = os.path.join(ASSET_ROOT, "manifest.json")
    index_path = os.path.join(ASSET_ROOT, "surat.json")
    if not os.path.exists(manifest_path) or not os.path.exists(index_path):
        fail("manifest.json or surat.json is missing from assets")

    with open(index_path, "r", encoding="utf-8") as handle:
        index_response = json.load(handle)

    entries = index_response.get("data")
    if not isinstance(entries, list) or len(entries) != 114:
        fail(f"expected 114 surah index entries, got {len(entries) if isinstance(entries, list) else type(entries)}")

    total_verses = 0
    total_tafsirs = 0
    for entry in entries:
        number = entry.get("nomor")
        expected_verses = entry.get("jumlahAyat")
        if not isinstance(number, int) or not isinstance(expected_verses, int):
            fail(f"invalid index entry: {entry}")

        for kind in ("surat", "tafsir"):
            path = os.path.join(ASSET_ROOT, kind, f"{number}.json")
            if not os.path.exists(path):
                fail(f"missing bundled file: {kind}/{number}.json")
            with open(path, "r", encoding="utf-8") as handle:
                response = json.load(handle)
            if response.get("code") != 200 or not isinstance(response.get("data"), dict):
                fail(f"invalid envelope in {kind}/{number}.json")
            data = response["data"]
            if data.get("nomor") != number:
                fail(f"surah number mismatch in {kind}/{number}.json")

            items_key = "ayat" if kind == "surat" else "tafsir"
            items = data.get(items_key)
            if not isinstance(items, list) or len(items) != expected_verses:
                fail(f"{kind}/{number}.json has {len(items) if isinstance(items, list) else 'invalid'} entries, expected {expected_verses}")
            for position, item in enumerate(items, 1):
                number_key = "nomorAyat" if kind == "surat" else "ayat"
                if item.get(number_key) != position:
                    fail(f"{kind}/{number}.json entry {position} has invalid number")
                required = ("teksArab", "teksLatin", "teksIndonesia") if kind == "surat" else ("teks",)
                for field in required:
                    if not item.get(field):
                        fail(f"{kind}/{number}.json entry {position} is missing {field}")

        total_verses += expected_verses
        total_tafsirs += expected_verses

    if total_verses != 6236 or total_tafsirs != 6236:
        fail(f"expected 6236 verses and tafsirs, got {total_verses} and {total_tafsirs}")

    with open(manifest_path, "r", encoding="utf-8") as handle:
        manifest = json.load(handle)
    if manifest.get("totalSurahs") != 114 or manifest.get("totalVerses") != 6236:
        fail("manifest totals are incorrect")

    print("EQuran bundle validation passed: 114 surahs, 6236 verses, 6236 tafsirs.")


if __name__ == "__main__":
    main()
