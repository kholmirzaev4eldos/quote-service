#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
rm -rf out && mkdir -p out
javac -d out src/App.java test/Tests.java
java -cp out Tests
