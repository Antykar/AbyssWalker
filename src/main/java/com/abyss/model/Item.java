package com.abyss.model;

import java.util.*;

public class Item {
    private String id;
    private String nameKey;
    private int damage;
    private int penetratingDamage;
    private int block;
    private int heal;
    private int energy;
    private int drawCards;
    private int strength;
    private int dexterity;
    private int guard;
    private String statusType;
    private int statusValue;
    private String target;
    private String description;

    public Item() {}

    public Item(String id, String nameKey) {
        this.id = id;
        this.nameKey = nameKey;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNameKey() { return nameKey; }
    public void setNameKey(String nameKey) { this.nameKey = nameKey; }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }

    public int getPenetratingDamage() { return penetratingDamage; }
    public void setPenetratingDamage(int penetratingDamage) { this.penetratingDamage = penetratingDamage; }

    public int getBlock() { return block; }
    public void setBlock(int block) { this.block = block; }

    public int getHeal() { return heal; }
    public void setHeal(int heal) { this.heal = heal; }

    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }

    public int getDrawCards() { return drawCards; }
    public void setDrawCards(int drawCards) { this.drawCards = drawCards; }

    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }

    public int getDexterity() { return dexterity; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }

    public int getGuard() { return guard; }
    public void setGuard(int guard) { this.guard = guard; }

    public String getStatusType() { return statusType; }
    public void setStatusType(String statusType) { this.statusType = statusType; }

    public int getStatusValue() { return statusValue; }
    public void setStatusValue(int statusValue) { this.statusValue = statusValue; }

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<String, Object> toMap() {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("id", id);
        d.put("name_key", nameKey);
        d.put("damage", damage);
        d.put("penetrating_damage", penetratingDamage);
        d.put("block", block);
        d.put("heal", heal);
        d.put("energy", energy);
        d.put("draw_cards", drawCards);
        d.put("strength", strength);
        d.put("dexterity", dexterity);
        d.put("guard", guard);
        d.put("status_type", statusType);
        d.put("status_value", statusValue);
        d.put("target", target);
        d.put("description", description);
        return d;
    }

    @SuppressWarnings("unchecked")
    public static Item fromMap(Map<String, Object> data) {
        Item item = new Item((String) data.get("id"), (String) data.get("name_key"));
        item.damage = ((Number) data.getOrDefault("damage", 0)).intValue();
        item.penetratingDamage = ((Number) data.getOrDefault("penetrating_damage", 0)).intValue();
        item.block = ((Number) data.getOrDefault("block", 0)).intValue();
        item.heal = ((Number) data.getOrDefault("heal", 0)).intValue();
        item.energy = ((Number) data.getOrDefault("energy", 0)).intValue();
        item.drawCards = ((Number) data.getOrDefault("draw_cards", 0)).intValue();
        item.strength = ((Number) data.getOrDefault("strength", 0)).intValue();
        item.dexterity = ((Number) data.getOrDefault("dexterity", 0)).intValue();
        item.guard = ((Number) data.getOrDefault("guard", 0)).intValue();
        item.statusType = (String) data.getOrDefault("status_type", null);
        item.statusValue = ((Number) data.getOrDefault("status_value", 0)).intValue();
        item.target = (String) data.getOrDefault("target", "self");
        item.description = (String) data.getOrDefault("description", "");
        return item;
    }
}