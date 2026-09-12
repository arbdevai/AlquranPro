#!/usr/bin/env python3
"""
Fetch and validate complete EQuran.id v2 snapshot (list, 114 surahs, 114 tafsirs).
Zero external Python dependencies (standard library urllib.request + json + hashlib).
"""
import hashlib
import json
import os
import sys
import time
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

BASE_URL = "https://equran.id/api/v2"
ASSET_ROOT = os.path.join(os.path.dirname(__file__), "..", "app", "src", "main", "assets", "equran", "v2")
SURAT_DIR = os.path.join(ASSET_ROOT, "surat")
TAFSIR_DIR = os.path.join(ASSET_ROOT, "tafsir")

os.makedirs(SURAT_DIR, exist_ok=True)
os.makedirs(TAFSIR_DIR, exist_ok=True)

USER_AGENT = "AlquranPro-AssetBuilder/1.0 (Android; Offline-First)"


def fetch_json(url: str, retries: int = 5, backoff: float = 1.0):
    req = Request(url, headers={"User-Agent": USER_AGENT, "Accept": "application/json"})
    for attempt in range(1, retries + 1):
        try:
            with urlopen(req, timeout=30) as resp:
                data = resp.read().decode("utf-8")
                parsed = json.loads(data)
                if parsed.get("code") != 200:
                    raise ValueError(f"API error: code={parsed.get('code')} msg={parsed.get('message')}")
                return parsed
        except (HTTPError, URLError, TimeoutError, ValueError, json.JSONDecodeError) as e:
            if attempt == retries:
                raise RuntimeError(f"Failed to fetch {url} after {retries} attempts: {e}")
            time.sleep(backoff * attempt)


def sha256_file(path: str) -> str:
    h = hashlib.sha256()
    with open(path, "rb") as f:
        while chunk := f.read(65536):
            h.update(chunk)
    return h.hexdigest()


def main():
    print("1/4. Fetching /surat index...")
    surat_list_resp = fetch_json(f"{BASE_URL}/surat")
    surat_list = surat_list_resp.get("data", [])
    if len(surat_list) != 114:
        raise ValueError(f"Expected 114 surahs in list, got {len(surat_list)}")

    surat_index_file = os.path.join(ASSET_ROOT, "surat.json")
    with open(surat_index_file, "w", encoding="utf-8") as f:
        json.dump(surat_list_resp, f, ensure_ascii=False, separators=(",", ":"))

    print(f"   Saved {surat_index_file}")

    total_verses = 0
    surah_meta = {}

    print("2/4. Fetching and verifying 114 surah details and 114 tafsirs...")
    for item in surat_list:
        n = item["nomor"]
        name_lat = item["namaLatin"]
        count = item["jumlahAyat"]
        surah_meta[n] = count

        # 1. Surah detail
        surat_file = os.path.join(SURAT_DIR, f"{n}.json")
        if not (os.path.exists(surat_file) and os.path.getsize(surat_file) > 100):
            print(f"   [{n}/114] Fetching Surat {name_lat}...")
            surat_resp = fetch_json(f"{BASE_URL}/surat/{n}")
            with open(surat_file, "w", encoding="utf-8") as f:
                json.dump(surat_resp, f, ensure_ascii=False, separators=(",", ":"))
            time.sleep(0.05)

        # 2. Tafsir detail
        tafsir_file = os.path.join(TAFSIR_DIR, f"{n}.json")
        if not (os.path.exists(tafsir_file) and os.path.getsize(tafsir_file) > 100):
            print(f"   [{n}/114] Fetching Tafsir {name_lat}...")
            tafsir_resp = fetch_json(f"{BASE_URL}/tafsir/{n}")
            with open(tafsir_file, "w", encoding="utf-8") as f:
                json.dump(tafsir_resp, f, ensure_ascii=False, separators=(",", ":"))
            time.sleep(0.05)

    print("3/4. Validating downloaded corpus integrity...")
    total_verses_found = 0
    manifest_files = {}

    manifest_files["surat.json"] = {
        "size": os.path.getsize(surat_index_file),
        "sha256": sha256_file(surat_index_file),
    }

    for n in range(1, 115):
        surat_file = os.path.join(SURAT_DIR, f"{n}.json")
        tafsir_file = os.path.join(TAFSIR_DIR, f"{n}.json")

        if not os.path.exists(surat_file) or not os.path.exists(tafsir_file):
            raise FileNotFoundError(f"Missing file for surah {n}")

        with open(surat_file, "r", encoding="utf-8") as f:
            s_data = json.load(f)["data"]

        with open(tafsir_file, "r", encoding="utf-8") as f:
            t_data = json.load(f)["data"]

        expected_count = surah_meta[n]
        actual_verses = s_data.get("ayat", [])
        actual_tafsir = t_data.get("tafsir", [])

        if len(actual_verses) != expected_count:
            raise ValueError(f"Surah {n} verse count mismatch: expected {expected_count}, got {len(actual_verses)}")

        if len(actual_tafsir) != expected_count:
            raise ValueError(f"Surah {n} tafsir count mismatch: expected {expected_count}, got {len(actual_tafsir)}")

        # Verify verse numbering and non-empty text
        for idx, verse in enumerate(actual_verses, 1):
            if verse.get("nomorAyat") != idx:
                raise ValueError(f"Surah {n} verse index {idx} has invalid nomorAyat: {verse.get('nomorAyat')}")
            if not verse.get("teksArab") or not verse.get("teksIndonesia"):
                raise ValueError(f"Surah {n} verse {idx} missing Arabic or Indonesian text")

        for idx, tafsir in enumerate(actual_tafsir, 1):
            if tafsir.get("ayat") != idx:
                raise ValueError(f"Surah {n} tafsir index {idx} has invalid ayat: {tafsir.get('ayat')}")
            if not tafsir.get("teks"):
                raise ValueError(f"Surah {n} tafsir {idx} missing text")

        total_verses_found += len(actual_verses)

        rel_surat = f"surat/{n}.json"
        manifest_files[rel_surat] = {
            "size": os.path.getsize(surat_file),
            "sha256": sha256_file(surat_file),
            "verses": len(actual_verses),
        }

        rel_tafsir = f"tafsir/{n}.json"
        manifest_files[rel_tafsir] = {
            "size": os.path.getsize(tafsir_file),
            "sha256": sha256_file(tafsir_file),
            "tafsirs": len(actual_tafsir),
        }

    if total_verses_found != 6236:
        raise ValueError(f"Total Quran verses count must be 6236, found {total_verses_found}")

    print(f"   Corpus verified: exactly 114 surahs, 6,236 verses, and 6,236 tafsir entries.")

    print("4/4. Writing manifest.json...")
    manifest = {
        "schemaVersion": "2.0",
        "source": "https://equran.id/api/v2",
        "retrievedAtUtc": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime()),
        "totalSurahs": 114,
        "totalVerses": 6236,
        "files": manifest_files,
    }

    manifest_file = os.path.join(ASSET_ROOT, "manifest.json")
    with open(manifest_file, "w", encoding="utf-8") as f:
        json.dump(manifest, f, ensure_ascii=False, indent=2)

    print(f"   Manifest created at {manifest_file}")
    print("All dataset assets ready!")


if __name__ == "__main__":
    main()
