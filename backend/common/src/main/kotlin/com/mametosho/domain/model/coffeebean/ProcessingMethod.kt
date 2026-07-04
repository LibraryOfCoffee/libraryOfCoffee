package com.mametosho.domain.model.coffeebean

/**
 * コーヒー豆の精製方法。
 *
 * @property label 日本語表示名（正本: docs/ubiquitousLanguage.md）
 */
enum class ProcessingMethod(val label: String) {
    FULLY_WASHED("フリーウォッシュド"),
    WASHED("ウォッシュド"),
    ANAEROBIC_WASHED("アナエロビックウォッシュド"),
    THERMAL_SHOCK_NATURAL("サーマルショックナチュラル"),
    NATURAL("ナチュラル"),
    ANAEROBIC_NATURAL("アナエロビックナチュラル"),
    DRY_ON_TREE_NATURAL("ドライオンツリーナチュラル"),
    LACTIC_NATURAL("ラクティックナチュラル"),
    WET_HULLING("スマトラ式"),
    HONEY("ハニー"),
    MOUNTAIN_WATER("マウンテンウォーター"),
    LADO_A_LADO_PROCESS("ラドラドプロセス"),

    /** ブレンドコーヒーの精製方法を表すための一時的な複合種別。 */
    LADO_A_LADO_PROCESS_FULLY_WASHED("ラドラドプロセス/フリーウォッシュド"),
}
