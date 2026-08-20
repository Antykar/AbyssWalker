package com.abyss.system;

import com.abyss.model.Item;
import java.util.*;

public class ItemFactory {

    public static final String BOMB = "bomb";
    public static final String SIGNAL_ARROW = "signal_arrow";
    public static final String GRENADE = "grenade";
    public static final String PAPER_SHIELD = "paper_shield";
    public static final String ENERGY_BALL = "energy_ball";
    public static final String VULN_POTION = "vuln_potion";
    public static final String WEAK_POTION = "weak_potion";
    public static final String FRAGILE_POTION = "fragile_potion";
    public static final String CYCLE_CARD = "cycle_card";
    public static final String POWER_GLOVE = "power_glove";
    public static final String TREATMENT_BOTTLE = "treatment_bottle";

    private static final List<String> ITEM_POOL = List.of(
        BOMB, SIGNAL_ARROW, GRENADE, PAPER_SHIELD, ENERGY_BALL,
        VULN_POTION, WEAK_POTION, FRAGILE_POTION, CYCLE_CARD,
        POWER_GLOVE, TREATMENT_BOTTLE
    );

    public static Item createItem(String nameKey) {
        return createItemWithId(nameKey, nameKey + "_" + UUID.randomUUID().toString().substring(0, 8));
    }

    public static Item createItemWithId(String nameKey, String id) {
        Item item = new Item(id, nameKey);
        initStats(item);
        return item;
    }

    public static Item getRandomItem() {
        List<String> pool = new ArrayList<>(ITEM_POOL);
        Collections.shuffle(pool);
        return createItem(pool.get(0));
    }

    public static List<String> getAllItemKeys() {
        return new ArrayList<>(ITEM_POOL);
    }

    public static String getChineseName(String nameKey) {
        LangManager lang = LangManager.getInstance();
        String name = lang.getText("items." + nameKey);
        return name != null ? name : nameKey;
    }

    private static void initStats(Item item) {
        String nameKey = item.getNameKey();
        LangManager lang = LangManager.getInstance();

        if (BOMB.equals(nameKey)) {
            item.setDamage(20);
            item.setTarget("enemy");
            item.setDescription(lang.getText("items_desc.bomb"));
        } else if (SIGNAL_ARROW.equals(nameKey)) {
            item.setPenetratingDamage(15);
            item.setTarget("enemy");
            item.setDescription(lang.getText("items_desc.signal_arrow"));
        } else if (GRENADE.equals(nameKey)) {
            item.setDamage(10);
            item.setTarget("all_enemies");
            item.setDescription(lang.getText("items_desc.grenade"));
        } else if (PAPER_SHIELD.equals(nameKey)) {
            item.setBlock(15);
            item.setTarget("self");
            item.setDescription(lang.getText("items_desc.paper_shield"));
        } else if (ENERGY_BALL.equals(nameKey)) {
            item.setEnergy(2);
            item.setTarget("self");
            item.setDescription(lang.getText("items_desc.energy_ball"));
        } else if (VULN_POTION.equals(nameKey)) {
            item.setStatusType("vulnerable");
            item.setStatusValue(2);
            item.setTarget("enemy");
            item.setDescription(lang.getText("items_desc.vuln_potion"));
        } else if (WEAK_POTION.equals(nameKey)) {
            item.setStatusType("weak");
            item.setStatusValue(2);
            item.setTarget("enemy");
            item.setDescription(lang.getText("items_desc.weak_potion"));
        } else if (FRAGILE_POTION.equals(nameKey)) {
            item.setStatusType("fragile");
            item.setStatusValue(2);
            item.setTarget("enemy");
            item.setDescription(lang.getText("items_desc.fragile_potion"));
        } else if (CYCLE_CARD.equals(nameKey)) {
            item.setDrawCards(3);
            item.setTarget("self");
            item.setDescription(lang.getText("items_desc.cycle_card"));
        } else if (POWER_GLOVE.equals(nameKey)) {
            item.setStrength(3);
            item.setTarget("self");
            item.setDescription(lang.getText("items_desc.power_glove"));
        } else if (TREATMENT_BOTTLE.equals(nameKey)) {
            item.setHeal(24);
            item.setTarget("self");
            item.setDescription(lang.getText("items_desc.treatment_bottle"));
        }
    }
}