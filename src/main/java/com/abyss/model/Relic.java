package com.abyss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class Relic {
    private final String id;
    private final String nameKey;
    private final Map<String, Object> effect;
    private final String descKey;

    @JsonCreator
    public Relic(
            @JsonProperty("id") String id,
            @JsonProperty("name_key") String nameKey,
            @JsonProperty("effect") Map<String, Object> effect,
            @JsonProperty("desc_key") String descKey) {
        this.id = id;
        this.nameKey = nameKey;
        this.effect = effect;
        this.descKey = descKey != null ? descKey : nameKey;
    }

    public Relic(String id, String nameKey, Map<String, Object> effect) {
        this(id, nameKey, effect, null);
    }

    @JsonProperty("id")
    public String getId() { return id; }

    @JsonProperty("name_key")
    public String getNameKey() { return nameKey; }

    @JsonProperty("effect")
    public Map<String, Object> getEffect() { return effect; }

    @JsonProperty("desc_key")
    public String getDescKey() { return descKey; }

    public Map<String, Object> toMap() {
        return Map.of(
                "id", id,
                "name_key", nameKey,
                "effect", effect,
                "desc_key", descKey
        );
    }

    public static Relic fromMap(Map<String, Object> data) {
        String id = (String) data.get("id");
        String nameKey = (String) data.get("name_key");
        @SuppressWarnings("unchecked")
        Map<String, Object> effect = (Map<String, Object>) data.get("effect");
        String descKey = data.containsKey("desc_key") ? (String) data.get("desc_key") : null;
        if (descKey == null) {
            descKey = nameKey;
        }
        return new Relic(id, nameKey, effect, descKey);
    }
}