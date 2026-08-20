package com.abyss.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnemyIntent {
    ATTACK("attack"),
    DEFEND("defend"),
    BUFF("buff"),
    DEBUFF("debuff");

    private final String value;

    EnemyIntent(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}