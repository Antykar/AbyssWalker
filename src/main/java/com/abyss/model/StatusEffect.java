package com.abyss.model;

import java.util.Objects;

/**
 * 状态效果模型类。
 * <p>
 * 表示战斗中角色（玩家或敌人）身上的一个状态效果，
 * 例如中毒、灼烧、虚弱、易伤等。
 */
public class StatusEffect {

    /** 状态名称。 */
    private String name;

    /** 当前层数/数值。 */
    private int stacks;

    /**
     * 构造一个状态效果实例。
     *
     * @param name   状态名称
     * @param stacks 初始层数/数值
     */
    public StatusEffect(String name, int stacks) {
        this.name = name;
        this.stacks = stacks;
    }

    /**
     * 获取状态名称。
     *
     * @return 状态名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置状态名称。
     *
     * @param name 状态名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取当前层数/数值。
     *
     * @return 当前层数
     */
    public int getStacks() {
        return stacks;
    }

    /**
     * 设置当前层数/数值。
     *
     * @param stacks 当前层数
     */
    public void setStacks(int stacks) {
        this.stacks = stacks;
    }

    /**
     * 增加层数。
     *
     * @param amount 增加的层数
     */
    public void addStacks(int amount) {
        this.stacks += amount;
    }

    /**
     * 减少层数（不会低于 0）。
     *
     * @param amount 减少的层数
     */
    public void reduceStacks(int amount) {
        this.stacks = Math.max(0, this.stacks - amount);
    }

    /**
     * 判断是否为增益效果（buff）。
     * <p>
     * 增益效果：力量、敏捷、格挡增强等。
     *
     * @return 如果是增益效果返回 true
     */
    public boolean isBuff() {
        return switch (name) {
            case "strength", "dexterity", "guard", "phasing", "dodge" -> true;
            default -> false;
        };
    }

    /**
     * 判断是否为减益效果（debuff）。
     * <p>
     * 减益效果：虚弱、易伤、中毒、灼烧、冻伤、麻痹、流血、眩晕、减速、脆弱等。
     *
     * @return 如果是减益效果返回 true
     */
    public boolean isDebuff() {
        return switch (name) {
            case "weak", "vulnerable", "poison", "burn", "frostbite",
                 "paralysis", "bleed", "stun", "slow", "fragile" -> true;
            default -> false;
        };
    }

    /**
     * 状态效果每回合结算。
     * <p>
     * 根据不同的状态类型执行对应的 tick 逻辑：
     * <ul>
     *   <li>灼烧 (burn)：扣血后层数减 1</li>
     *   <li>虚弱/易伤/减速/麻痹/眩晕 (weak/vulnerable/slow/paralysis/stun)：层数减 1</li>
     *   <li>中毒 (poison)：扣血后层数减 1</li>
     *   <li>流血 (bleed)：层数减半（向下取整）</li>
     *   <li>冻伤 (frostbite)：不掉层，持续存在</li>
     *   <li>其他：层数减 1</li>
     * </ul>
     *
     * @return 本回合该状态造成的伤害（0 表示无伤害）
     */
    public int tick() {
        if (stacks <= 0) {
            return 0;
        }

        int damage = 0;

        switch (name) {
            case "burn" -> {
                // 灼烧：扣血后层数减 1
                damage = stacks;
                stacks = Math.max(0, stacks - 1);
            }
            case "poison" -> {
                // 中毒：扣血后层数减 1
                damage = stacks;
                stacks = Math.max(0, stacks - 1);
            }
            case "weak", "vulnerable", "slow", "paralysis", "stun" -> {
                // 层数减 1
                stacks = Math.max(0, stacks - 1);
            }
            case "bleed" -> {
                // 流血：层数减半（向下取整）
                stacks = stacks / 2;
            }
            case "frostbite" -> {
                // 冻伤：不掉层
                // 但冻伤有致死检查机制（由外部逻辑处理）
            }
            default -> {
                // 默认：层数减 1
                stacks = Math.max(0, stacks - 1);
            }
        }

        return damage;
    }

    /**
     * 判断状态是否已过期（层数 <= 0）。
     *
     * @return 如果已过期返回 true
     */
    public boolean isExpired() {
        return stacks <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusEffect that = (StatusEffect) o;
        return stacks == that.stacks && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, stacks);
    }

    @Override
    public String toString() {
        return name + "(" + stacks + ")";
    }
}