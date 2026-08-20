package com.abyss.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CardType {
    ATTACK("attack"),
    SKILL("skill"),
    POWER("power"),
    CURSE("curse");

    private final String value;

    CardType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}