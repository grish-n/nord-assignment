#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

RESULTS_DIR="$PROJECT_DIR/target/allure-results"
REPORT_DIR="$PROJECT_DIR/allure-report"
HISTORY_DIR="$PROJECT_DIR/allure-history"
CONFIG_DIR="$PROJECT_DIR/allure-config"

if ! command -v allure >/dev/null 2>&1; then
    echo "Ошибка: Allure CLI не установлен"
    exit 1
fi

if [[ ! -d "$RESULTS_DIR" ]] ||
   [[ -z "$(find "$RESULTS_DIR" -maxdepth 1 -name '*-result.json' -print -quit)" ]]; then
    echo "Нет результатов тестирования. Сначала выполни mvn clean test"
    exit 1
fi

if [[ -f "$CONFIG_DIR/environment.properties" ]]; then
    cp "$CONFIG_DIR/environment.properties" "$RESULTS_DIR/environment.properties"
fi

rm -rf "$RESULTS_DIR/history"

if [[ -d "$HISTORY_DIR" ]]; then
    mkdir -p "$RESULTS_DIR/history"
    cp -R "$HISTORY_DIR/." "$RESULTS_DIR/history/"
fi

allure generate "$RESULTS_DIR" --clean -o "$REPORT_DIR"

if [[ -d "$REPORT_DIR/history" ]]; then
    mkdir -p "$HISTORY_DIR"
    cp -R "$REPORT_DIR/history/." "$HISTORY_DIR/"
fi

allure open "$REPORT_DIR"
