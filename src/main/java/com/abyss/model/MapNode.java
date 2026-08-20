package com.abyss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class MapNode {
    private final int id;
    private final MapNodeType type;
    private final int x;
    private final int y;
    private final int floor;
    private boolean completed;

    @JsonCreator
    public MapNode(
            @JsonProperty("id") int id,
            @JsonProperty("type") MapNodeType type,
            @JsonProperty("x") int x,
            @JsonProperty("y") int y,
            @JsonProperty("floor") int floor,
            @JsonProperty("completed") boolean completed) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.completed = completed;
    }

    public MapNode(int id, MapNodeType type, int x, int y, int floor) {
        this(id, type, x, y, floor, false);
    }

    @JsonProperty("id")
    public int getId() { return id; }

    @JsonProperty("type")
    public MapNodeType getType() { return type; }

    @JsonProperty("x")
    public int getX() { return x; }

    @JsonProperty("y")
    public int getY() { return y; }

    @JsonProperty("floor")
    public int getFloor() { return floor; }

    @JsonProperty("completed")
    public boolean isCompleted() { return completed; }

    public void setCompleted(boolean completed) { this.completed = completed; }

    public Map<String, Object> toMap() {
        return Map.of(
                "id", id,
                "type", type.getValue(),
                "x", x,
                "y", y,
                "floor", floor,
                "completed", completed
        );
    }

    public static MapNode fromMap(Map<String, Object> data) {
        int id = ((Number) data.get("id")).intValue();
        MapNodeType type = MapNodeType.valueOf(((String) data.get("type")).toUpperCase());
        int x = ((Number) data.get("x")).intValue();
        int y = ((Number) data.get("y")).intValue();
        int floor = ((Number) data.get("floor")).intValue();
        boolean completed = data.containsKey("completed") && (Boolean) data.get("completed");
        return new MapNode(id, type, x, y, floor, completed);
    }
}