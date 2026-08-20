package com.abyss.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MapNodeType {
    COMBAT("combat"),
    ELITE("elite"),
    SHOP("shop"),
    REST("rest"),
    EVENT("event"),
    BOSS("boss"),
    TREASURE("treasure"),
    OPPORTUNITY("opportunity");

    private final String value;

    MapNodeType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}