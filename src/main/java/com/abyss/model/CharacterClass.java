package com.abyss.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CharacterClass {
    WARRIOR("warrior"),
    MAGE("mage"),
    ROGUE("rogue"),
    PRIEST("priest");

    private final String value;

    CharacterClass(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static CharacterClass fromValue(String v) {
        for (CharacterClass c : values()) {
            if (c.value.equals(v)) return c;
        }
        return WARRIOR;
    }
}