package com.abyss.system;

import com.abyss.model.Relic;

import java.util.*;

public class RelicPool {

    private static final Map<String, Map<String, Object>> RELIC_POOL = new LinkedHashMap<>();

    static {
        // === 遗物 ===
        RELIC_POOL.put("burning_blood", mapOf("type", "heal_on_combat_start", "value", 6));
        RELIC_POOL.put("ring_of_snake", mapOf("type", "first_turn_draw", "value", 3));
        RELIC_POOL.put("anchor", mapOf("type", "damage_reduce", "value", 1));
        RELIC_POOL.put("lantern", mapOf("type", "extra_energy", "value", 1));
        RELIC_POOL.put("bag_of_marbles", mapOf("type", "enemy_weak", "value", 1));
        RELIC_POOL.put("paper_krane", mapOf("type", "extra_debuff_stack", "value", 1));
        RELIC_POOL.put("pen_nib", mapOf("type", "attack_damage_up", "value", 1));
        RELIC_POOL.put("red_skull", mapOf("type", "strength_on_hit", "value", 1));
        RELIC_POOL.put("vajra", mapOf("type", "permanent_strength", "value", 1));
        RELIC_POOL.put("blood_vial", mapOf("type", "heal_on_combat_start", "value", 2));
        RELIC_POOL.put("bronze_scales", mapOf("type", "poison_resist", "value", 0.5));
        RELIC_POOL.put("cultist_mask", mapOf("type", "start_strength", "value", 1));
        RELIC_POOL.put("golden_fleece", mapOf("type", "gold_bonus_percent", "value", 25));
        RELIC_POOL.put("iron_underwear", mapOf("type", "start_guard", "value", 2));
        RELIC_POOL.put("armor_break_blade", mapOf("type", "attack_aoe", "value", 2));
        RELIC_POOL.put("hermes_boots", mapOf("type", "start_dexterity", "value", 1));
        RELIC_POOL.put("eternal_fire", mapOf("type", "burn_on_hit", "value", 1));
        RELIC_POOL.put("honey_lips", mapOf("type", "poison_on_hit", "value", 1));
        RELIC_POOL.put("justice_scale", mapOf("type", "reflect_damage", "value", 1));
        RELIC_POOL.put("ox_talisman", mapOf("type", "strength_every_2_turns", "value", 1));
        RELIC_POOL.put("life_branch", mapOf("type", "heal_per_turn", "value", 1));
        RELIC_POOL.put("twin_fish_pendant", mapOf("type", "first_card_double", "value", 1));
        RELIC_POOL.put("scepter", mapOf("type", "turn_start_aoe_damage", "value", 0));
        RELIC_POOL.put("osmanthus_cake", mapOf("type", "instant_heal_pct", "value", 50));
        RELIC_POOL.put("millennium_ice", mapOf("type", "frostbite_per_turn", "value", 1));
        RELIC_POOL.put("hundred_bill", mapOf("type", "instant_gold", "value", 100));
        RELIC_POOL.put("fury_scythe", mapOf("type", "turn_start_random_damage", "value", 5));
        RELIC_POOL.put("guardian_charm", mapOf("type", "instant_guard", "value", 1));
        RELIC_POOL.put("nanfu_battery", mapOf("type", "carry_energy", "value", 1));
        RELIC_POOL.put("crown_of_overlord", mapOf("type", "combat_start_add_card", "value", "overlord_deterrence"));
    }

    private static Map<String, Object> mapOf(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            m.put((String) kv[i], kv[i + 1]);
        }
        return m;
    }

    public static Relic getRelic(String nameKey) {
        Map<String, Object> effect = RELIC_POOL.get(nameKey);
        if (effect == null) return null;
        return new Relic(nameKey, nameKey, new LinkedHashMap<>(effect));
    }

    public static Relic getRandomRelic() {
        List<String> keys = new ArrayList<>(RELIC_POOL.keySet());
        String key = keys.get(new Random().nextInt(keys.size()));
        return getRelic(key);
    }

    public static List<String> getAllRelicKeys() {
        return new ArrayList<>(RELIC_POOL.keySet());
    }
}