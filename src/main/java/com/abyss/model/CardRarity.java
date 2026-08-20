package com.abyss.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CardRarity {
    COMMON("common"),
    UNCOMMON("uncommon"),
    RARE("rare"),
    LEGENDARY("legendary");

    private final String value;

    CardRarity(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}