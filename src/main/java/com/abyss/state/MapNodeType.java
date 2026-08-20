package com.abyss.state;

/**
 * 地图节点类型枚举，决定玩家点击节点后进入的场景。
 * <p>
 * 每种节点类型对应不同的游戏玩法和奖励。
 */
public enum MapNodeType {
    /** 普通战斗，1~3 个普通敌人。 */
    COMBAT("combat"),
    /** 精英战斗，1 个精英敌人（可能附带普通敌人）。 */
    ELITE("elite"),
    /** Boss 战，每局最后一个节点。 */
    BOSS("boss"),
    /** 商店，可购买卡牌、删除卡组中的卡牌。 */
    SHOP("shop"),
    /** 休息点，回复玩家生命值。 */
    REST("rest"),
    /** 宝箱。 */
    TREASURE("treasure"),
    /** 神秘事件。 */
    EVENT("event"),
    /** 机遇房。 */
    OPPORTUNITY("opportunity");

    private final String value;

    MapNodeType(String value) {
        this.value = value;
    }

    /**
     * 获取枚举的字符串值。
     *
     * @return 字符串表示
     */
    public String getValue() {
        return value;
    }

    /**
     * 根据字符串值解析为 MapNodeType。
     *
     * @param value 字符串值
     * @return 对应的 MapNodeType，未匹配时返回 null
     */
    public static MapNodeType fromValue(String value) {
        for (MapNodeType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}