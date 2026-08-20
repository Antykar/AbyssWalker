package com.abyss.state;

/**
 * 游戏阶段枚举，控制游戏当前处于哪个界面。
 */
public enum GamePhase {
    TITLE("title"),
    MODE_SELECT("mode_select"),
    CHARACTER_SELECT("character_select"),
    CHARACTER_BUILD("character_build"),
    BUILD("build"),
    BOSS_RUSH_SETUP("boss_rush_setup"),
    FREE_SETUP("free_setup"),
    MAP("map"),
    COMBAT("combat"),
    REWARD("reward"),
    SHOP("shop"),
    ENCYCLOPEDIA("encyclopedia"),
    DECK_VIEW("deck_view"),
    RELIC_VIEW("relic_view"),
    SETTINGS("settings"),
    GAME_OVER("game_over");

    private final String value;

    GamePhase(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static GamePhase fromValue(String v) {
        for (GamePhase p : values()) {
            if (p.value.equals(v)) return p;
        }
        return null;
    }
}