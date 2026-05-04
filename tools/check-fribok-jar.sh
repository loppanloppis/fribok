#!/bin/sh
set -eu

JAR="${1:-target/fribok-2.3-SNAPSHOT-jar-with-dependencies.jar}"

echo "Checking: $JAR"

jar tf "$JAR" | grep -q '^org/fribok/fonts/fonts.xml$'
jar tf "$JAR" | grep -q '^org/fribok/fonts/OCRA.ttf$'

tmp="$(mktemp)"
unzip -p "$JAR" org/fribok/fonts/OCRA.ttf > "$tmp"

file "$tmp"

if ! file "$tmp" | grep -qi 'font'; then
    echo "ERROR: OCRA.ttf does not look like a valid font"
    rm -f "$tmp"
    exit 1
fi

rm -f "$tmp"

echo "OK: font resources present"
