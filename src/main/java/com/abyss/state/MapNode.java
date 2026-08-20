package com.abyss.state;

import java.util.*;

/**
 * 地图节点，包含类型、位置、完成状态等信息。
 */
public class MapNode {
    private int id;
    private MapNodeType type;
    private int x, y, floor;
    private boolean completed;

    public MapNode() {} // Jackson 反序列化用

    public MapNode(int id, MapNodeType type, int x, int y, int floor, boolean completed) {
        this.id = id; this.type = type; this.x = x; this.y = y; this.floor = floor; this.completed = completed;
    }

    public MapNode(int id, MapNodeType type, int x, int y, int floor) {
        this(id, type, x, y, floor, false);
    }

    public int getId() { return id; }
    public MapNodeType getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getFloor() { return floor; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id); m.put("type", type.getValue()); m.put("x", x);
        m.put("y", y); m.put("floor", floor); m.put("completed", completed);
        return m;
    }

    public static MapNode fromMap(Map<String, Object> data) {
        return new MapNode(
            ((Number) data.get("id")).intValue(),
            MapNodeType.fromValue((String) data.get("type")),
            ((Number) data.get("x")).intValue(),
            ((Number) data.get("y")).intValue(),
            ((Number) data.get("floor")).intValue(),
            data.containsKey("completed") && (Boolean) data.get("completed")
        );
    }
}