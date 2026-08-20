package com.abyss.system;

import com.abyss.model.*;

import java.util.*;

public class CardFactory {

    // ============== 卡牌 ID 常量 ==============
    public static final String STRIKE = "strike";
    public static final String DEFEND = "defend";
    public static final String HEAVY_STRIKE = "heavy_strike";
    public static final String BASH = "bash";
    public static final String BLOOD_BLADE = "blood_blade";
    public static final String TWIN_STRIKE = "twin_strike";
    public static final String BLOODLETTING = "bloodletting";
    public static final String POMMEL_STRIKE = "pommel_strike";
    public static final String SHRUG_IT_OFF = "shrug_it_off";
    public static final String BATH = "bath";
    public static final String TRUE_GRIT = "true_grit";
    public static final String CARNAGE = "carnage";
    public static final String UPPERCUT = "uppercut";
    public static final String FURY = "fury";
    public static final String SWORD_BOOMERANG = "sword_boomerang";
    public static final String BATTLE_TRANCE = "battle_trance";
    public static final String OFFERING = "offering";
    public static final String METEOR_STRIKE = "meteor_strike";
    public static final String FIREBALL = "fireball";
    public static final String ICE_SPIKE = "ice_spike";
    public static final String ARCANE_BLAST = "arcane_blast";
    public static final String LIGHTNING_BOLT = "lightning_bolt";
    public static final String SHADOW_STRIKE = "shadow_strike";
    public static final String BACKSTAB = "backstab";
    public static final String OPPRESSION = "oppression";
    public static final String BLOOD_CALAMITY = "blood_calamity";
    public static final String QUICK_SLASH = "quick_slash";
    public static final String DAGGER_THROW = "dagger_throw";
    public static final String VENOM_STRIKE = "venom_strike";
    public static final String ASSASSINATE = "assassinate";
    public static final String EVASION = "evasion";
    public static final String ADRENALINE = "adrenaline";
    public static final String XIANYE_FURY = "xianye_fury";
    public static final String BANKRUPTCY_PALM = "bankruptcy_palm";
    public static final String GET_RICH_DUMBBELL = "get_rich_dumbbell";
    public static final String KNIFE_ATTACK = "knife_attack";
    public static final String IRON_STRIKE = "iron_strike";
    public static final String GRADUAL_MADNESS = "gradual_madness";
    public static final String KIDNEY_PILL = "kidney_pill";
    public static final String DIVINE_POWER = "divine_power";
    public static final String ENDLESS_POWER = "endless_power";
    public static final String BECOME_KING = "become_king";
    public static final String DISARM = "disarm";
    public static final String XIANYE_BLESSING = "xianye_blessing";
    public static final String XIANYE_GUARD = "xianye_guard";
    public static final String DYNASTY_HORSE = "dynasty_horse";
    public static final String GUARD_TO_ATTACK = "guard_to_attack";
    public static final String ELEMENTAL_RUSH = "elemental_rush";
    public static final String ATTACK_AND_DEFEND = "attack_and_defend";
    public static final String HEAVY_ARMOR = "heavy_armor";
    public static final String BLOODBATH = "bloodbath";
    public static final String ETERNAL_STORM = "eternal_storm";
    public static final String SOUL_FLAME = "soul_flame";
    public static final String GATHER_ENERGY = "gather_energy";
    public static final String FATE_MANIPULATE = "fate_manipulate";
    public static final String PUMPED_UP = "pumped_up";
    public static final String LITTLE_BEING = "little_being";
    public static final String FOCUSED_RAY = "focused_ray";
    public static final String LIQUN = "liqun";
    public static final String JUDGMENT = "judgment";
    public static final String HOLY_SHIELD = "holy_shield";
    public static final String FENGSHUI_LUNLIU = "fengshui_lunliu";
    public static final String GOLD_STRIKE = "gold_strike";
    public static final String BUY = "buy";
    public static final String YUANHUA = "yuanhua";
    public static final String CHAOS_STRIKE = "chaos_strike";
    public static final String HOT_COLD = "hot_cold";
    public static final String MOCK = "mock";
    public static final String STINKY_TOFU = "stinky_tofu";
    public static final String HIGH_KILL_INTENT = "high_kill_intent";
    public static final String POISON_QUENCH = "poison_quench";
    public static final String BRILLIANT_STRIKE = "brilliant_strike";
    public static final String COIN_THROW = "coin_throw";
    public static final String THUNDER_CALL = "thunder_call";
    public static final String BLOOD_DRINK = "blood_drink";
    public static final String UNYIELDING = "unyielding";
    public static final String LIGHTNING_SHIELD = "lightning_shield";
    public static final String HEAVEN_THUNDER = "heaven_thunder";
    public static final String ETERNAL_CHANT = "eternal_chant";
    public static final String ZEALOT = "zealot";
    public static final String WEATHERING = "weathering";
    public static final String FAITH_ACCUMULATION = "faith_accumulation";
    public static final String WISH_POWER = "wish_power";
    public static final String DIVINE_JUDGMENT = "divine_judgment";
    public static final String CHANT = "chant";
    public static final String SNAKE_NOTBITE = "snake_notbite";
    public static final String CONCEAL = "conceal";
    public static final String NIGHTFALL = "nightfall";
    public static final String SNEAK_ATTACK = "sneak_attack";
    public static final String SUDDEN_AMBUSH = "sudden_ambush";
    public static final String RETIRE = "retire";
    public static final String CONCENTRATED_TOXIN = "concentrated_toxin";
    public static final String HIGH_HEAT_BOIL = "high_heat_boil";
    public static final String ICE_SORROW = "ice_sorrow";
    public static final String FREEZE_RAY = "freeze_ray";
    public static final String ELEMENT_RECYCLE = "element_recycle";
    public static final String IMPREGNABLE = "impregnable";
    public static final String PAY_THE_BILL = "pay_the_bill";
    public static final String ECONOMIC_LAW = "economic_law";
    public static final String SELL_SOUL = "sell_soul";
    public static final String STEAL = "steal";
    public static final String PICKPOCKET = "pickpocket";
    public static final String FINISHER = "finisher";
    public static final String ALL_LIVING_WISH = "all_living_wish";
    public static final String BLOOD_BURN = "blood_burn";
    public static final String PALM_THUNDER = "palm_thunder";
    public static final String GOLD_CURSE = "gold_curse";
    public static final String PRAYER = "prayer";
    public static final String LIGHT_ENERGY_WAVE = "light_energy_wave";
    public static final String DOG_SKIN_PLASTER = "dog_skin_plaster";
    public static final String BAD_ILLNESS = "bad_illness";
    public static final String HOPELESS = "hopeless";
    public static final String EMERGENCY_EVASION = "emergency_evasion";
    public static final String PERSISTENT_CURSE = "persistent_curse";
    public static final String OVERLORD_DETERRENCE = "overlord_deterrence";
    public static final String ADVANCING_TO_RETREAT = "advancing_to_retreat";
    public static final String SHIELD_DANCE = "shield_dance";
    public static final String ABSOLUTE_DEFENSE = "absolute_defense";
    public static final String UNMATCHED = "unmatched";
    public static final String POISON_FIRE_HEART = "poison_fire_heart";
    public static final String PROMISED_THING = "promised_thing";
    public static final String INSTANT_KILL = "instant_kill";
    public static final String BREAK_STEALTH = "break_stealth";
    public static final String GIFT_OF_BLESSING = "gift_of_blessing";
    public static final String BODYGUARD = "bodyguard";
    public static final String DEMONIZE = "demonize";
    public static final String DARK_GEM = "dark_gem";
    public static final String ENERGY_BACKLASH = "energy_backlash";
    public static final String NOURISH_SOUL = "nourish_soul";
    public static final String I_HAVE_ASCENDED = "i_have_ascended";
    public static final String LIGHT_BURST = "light_burst";
    public static final String SACRIFICE_EVERYTHING = "sacrifice_everything";
    public static final String ELEMENTAL_MASTERY = "elemental_mastery";
    public static final String ECHO_BURST = "echo_burst";
    public static final String DEEP_HELL = "deep_hell";
    public static final String THUNDERCLOUD_ROLL = "thundercloud_roll";
    public static final String INCREASE_POWER = "increase_power";
    public static final String URGENT_FROST = "urgent_frost";
    public static final String POISON_SPREAD = "poison_spread";
    public static final String FIRE_CONNECT = "fire_connect";
    public static final String FROST_SHATTER = "frost_shatter";
    public static final String FREEZE_RIGID = "freeze_rigid";
    public static final String FUEL_BOOSTER = "fuel_booster";
    public static final String VIOLENT_COMBUSTION = "violent_combustion";
    public static final String EXTREME_COLD = "extreme_cold";
    public static final String ALTERNATING_VOLTAGE = "alternating_voltage";
    public static final String HEAVEN_EARTH_LAW = "heaven_earth_law";
    public static final String COLD_FEVER = "cold_fever";
    public static final String FROST_CRACK = "frost_crack";
    public static final String FLAME_BURST = "flame_burst";
    public static final String CONDUCT_CIRCUIT = "conduct_circuit";
    public static final String ELECTROMAGNETIC_FIELD = "electromagnetic_field";
    public static final String POWER_SURGE = "power_surge";
    public static final String CURRENT_SYMBIOSIS = "current_symbiosis";
    public static final String THUNDER_GOD_DESCEND = "thunder_god_descend";
    public static final String OVERLOAD = "overload";
    public static final String ELEMENTAL_COAT = "elemental_coat";
    public static final String CERTIFICATION_MASTER = "certification_master";
    public static final String PRIMORDIAL_ENERGY = "primordial_energy";
    public static final String RETURN_TO_CHAOS = "return_to_chaos";
    public static final String LIGHTNING_FLASH = "lightning_flash";
    public static final String ELEMENT_REUSE = "element_reuse";
    public static final String PURE_WHITE_STATE = "pure_white_state";
    public static final String SHIELD_HERO = "shield_hero";
    public static final String CONSTANT_HARASS = "constant_harass";
    public static final String INVADE_BODY = "invade_body";
    public static final String GRAND_FIREWORKS = "grand_fireworks";
    public static final String ARMORED_ICE = "armored_ice";
    public static final String ROTTEN_MIASMA = "rotten_miasma";
    public static final String POISON_MATERIAL = "poison_material";
    public static final String SIZZLING = "sizzling";
    public static final String ACCUMULATED_ELECTRIC_CHARGE = "accumulated_electric_charge";
    public static final String SNOW_ON_FROST = "snow_on_frost";
    public static final String FAITH_ANNIHILATION = "faith_annihilation";
    public static final String DEATH_BREATH = "death_breath";
    public static final String BLOOD_DEMON = "blood_demon";
    public static final String HELL_CHAOS = "hell_chaos";
    public static final String BLOOD_CLOAK = "blood_cloak";
    public static final String BODY_ARMOR = "body_armor";
    public static final String BATTLE_MASTER = "battle_master";
    public static final String NIGHT_LIKE_DAY = "night_like_day";
    public static final String COME_AND_GO = "come_and_go";
    public static final String KILL = "kill";
    public static final String PERFECT_SKILL = "perfect_skill";
    public static final String FUTURE_STRIKE = "future_strike";
    public static final String RECYCLE = "recycle";
    public static final String NOT_WILLING_TO_LOSE = "not_willing_to_lose";
    public static final String AFTER_A_HUNDRED_YEARS = "after_a_hundred_years";

    // ============== 创建单张卡牌 ==============

    @SuppressWarnings("unchecked")
    public static Card createCard(String nameKey) {
        return createCardWithId(nameKey, nameKey + "_0");
    }

    @SuppressWarnings("unchecked")
    public static Card createCardWithId(String nameKey, String id) {
        // 先创建基础卡牌对象，再在 initStats 中设置具体数值
        Card card = new Card(id, nameKey, CardType.ATTACK, 1, CardRarity.COMMON);
        initStats(card);
        return card;
    }

    @SuppressWarnings("unchecked")
    private static void initStats(Card card) {
        String nameKey = card.getNameKey();

        // === 基础卡牌 ===
        if (STRIKE.equals(nameKey)) {
            card.setDamage(6);
            card.setTarget("enemy");
        } else if (DEFEND.equals(nameKey)) {
            card.setBlock(5);
            card.setType(CardType.SKILL);
            card.setTarget("self");
        } else if (HEAVY_STRIKE.equals(nameKey)) {
            card.setDamage(15);
            card.setCost(2);
            card.setTarget("enemy");
        } else if (BASH.equals(nameKey)) {
            card.setDamage(8);
            card.setEffect(mapOf("type", "vulnerable", "value", 2));
            card.setTarget("enemy");
        } else if (BLOOD_BLADE.equals(nameKey)) {
            card.setDamage(3);
            card.setCost(0);
            card.setTarget("all_enemies");
            card.setEffect(listOf(
                mapOf("type", "hit_multiple", "value", 2),
                mapOf("type", "lose_hp", "value", 2)
            ));
        } else if (TWIN_STRIKE.equals(nameKey)) {
            card.setDamage(5);
            card.setEffect(mapOf("type", "hit_twice", "value", 2));
            card.setTarget("enemy");
        } else if (BLOODLETTING.equals(nameKey)) {
            card.setEffect(listOf(
                mapOf("type", "draw_cards", "value", 2),
                mapOf("type", "lose_hp", "value", 2)
            ));
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (POMMEL_STRIKE.equals(nameKey)) {
            card.setDamage(9);
            card.setEffect(mapOf("type", "draw_cards", "value", 1));
            card.setTarget("enemy");
        } else if (SHRUG_IT_OFF.equals(nameKey)) {
            card.setBlock(8);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "draw_cards", "value", 1));
            card.setTarget("self");
        } else if (BATH.equals(nameKey)) {
            card.setHeal(5);
            card.setCost(2);
            card.setEffect(mapOf("type", "guard_up", "value", 1));
            card.setTarget("self");
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.UNCOMMON);
        } else if (TRUE_GRIT.equals(nameKey)) {
            card.setBlock(7);
            card.setEffect(mapOf("type", "exhaust_card", "value", 1));
            card.setTarget("self");
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.UNCOMMON);
        } else if (CARNAGE.equals(nameKey)) {
            card.setDamage(50);
            card.setCost(3);
            card.setEffect(mapOf("type", "exhaust_self", "value", 1));
            card.setRarity(CardRarity.UNCOMMON);
        } else if (UPPERCUT.equals(nameKey)) {
            card.setDamage(13);
            card.setCost(2);
            card.setEffect(mapOf("type", "weak", "value", 2));
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (FURY.equals(nameKey)) {
            card.setEffect(mapOf("type", "strength", "value", 2));
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (SWORD_BOOMERANG.equals(nameKey)) {
            card.setDamage(6);
            card.setEffect(mapOf("type", "hit_random", "value", 3));
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (BATTLE_TRANCE.equals(nameKey)) {
            card.setEffect(listOf(
                mapOf("type", "draw_to_max"),
                mapOf("type", "skip_next_turn")
            ));
            card.setCost(0);
            card.setTarget("self");
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.UNCOMMON);
        } else if (OFFERING.equals(nameKey)) {
            card.setEffect(listOf(
                mapOf("type", "draw_cards", "value", 5),
                mapOf("type", "lose_hp", "value", 3)
            ));
            card.setCost(0);
            card.setTarget("self");
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.RARE);
        } else if (METEOR_STRIKE.equals(nameKey)) {
            card.setDamage(30);
            card.setCost(3);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (FIREBALL.equals(nameKey)) {
            card.setDamage(10);
            card.setEffect(mapOf("type", "burn", "value", 5));
            card.setCost(2);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (ICE_SPIKE.equals(nameKey)) {
            card.setDamage(5);
            card.setEffect(mapOf("type", "weak", "value", 1));
            card.setTarget("enemy");
        } else if (ARCANE_BLAST.equals(nameKey)) {
            card.setDamage(10);
            card.setEffect(mapOf("type", "strength", "value", 1));
            card.setCost(2);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (LIGHTNING_BOLT.equals(nameKey)) {
            card.setDamage(12);
            card.setEffect(mapOf("type", "paralysis", "value", 1));
            card.setCost(2);
            card.setTarget("enemy");
        } else if (SHADOW_STRIKE.equals(nameKey)) {
            card.setDamage(6);
            card.setEffect(mapOf("type", "poison", "value", 3));
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (BACKSTAB.equals(nameKey)) {
            card.setDamage(12);
            card.setCost(0);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (OPPRESSION.equals(nameKey)) {
            card.setDamage(8);
            card.setCost(0);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "kill_gold", "value", 20));
        } else if (BLOOD_CALAMITY.equals(nameKey)) {
            card.setDamage(20);
            card.setCost(2);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "kill_heal", "value", 3));
        } else if (QUICK_SLASH.equals(nameKey)) {
            card.setDamage(4);
            card.setCost(0);
            card.setEffect(mapOf("type", "draw_cards", "value", 1));
            card.setTarget("enemy");
        } else if (DAGGER_THROW.equals(nameKey)) {
            card.setDamage(5);
            card.setEffect(mapOf("type", "hit_random", "value", 2));
            card.setTarget("enemy");
        } else if (VENOM_STRIKE.equals(nameKey)) {
            card.setDamage(5);
            card.setEffect(mapOf("type", "poison", "value", 7));
            card.setCost(2);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
        } else if (ASSASSINATE.equals(nameKey)) {
            card.setDamage(50);
            card.setCost(3);
            card.setTarget("enemy");
            card.setRarity(CardRarity.RARE);
        } else if (EVASION.equals(nameKey)) {
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "guard_up", "value", 2),
                mapOf("type", "phasing", "value", 1)
            ));
            card.setCost(2);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
        } else if (ADRENALINE.equals(nameKey)) {
            card.setEffect(listOf(
                mapOf("type", "strength", "value", 1),
                mapOf("type", "draw_cards", "value", 1)
            ));
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
        } else if (XIANYE_FURY.equals(nameKey)) {
            card.setDamage(50);
            card.setBlock(10);
            card.setCost(1);
            card.setTarget("enemy");
            card.setRarity(CardRarity.LEGENDARY);
        } else if (BANKRUPTCY_PALM.equals(nameKey)) {
            card.setDamage(15);
            card.setEffect(mapOf("type", "lose_gold", "value", 10));
            card.setCost(0);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (GET_RICH_DUMBBELL.equals(nameKey)) {
            card.setEffect(mapOf("type", "gain_gold", "value", 30));
            card.setCost(1);
            card.setTarget("self");
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.COMMON);
        } else if (KNIFE_ATTACK.equals(nameKey)) {
            card.setDamage(5);
            card.setCost(0);
            card.setTarget("enemy");
        } else if (IRON_STRIKE.equals(nameKey)) {
            card.setDamage(12);
            card.setBlock(9);
            card.setCost(2);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (GRADUAL_MADNESS.equals(nameKey)) {
            card.setEffect(mapOf("type", "strength_per_turn", "value", 2));
            card.setCost(2);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (KIDNEY_PILL.equals(nameKey)) {
            card.setEffect(mapOf("type", "draw_extra", "value", 1));
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
        } else if (DIVINE_POWER.equals(nameKey)) {
            card.setEffect(mapOf("type", "phasing", "value", 1));
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
        } else if (ENDLESS_POWER.equals(nameKey)) {
            card.setEffect(mapOf("type", "double_strength", "value", 0));
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (BECOME_KING.equals(nameKey)) {
            card.setEffect(mapOf("type", "sacrifice_strength_heal", "value", 2));
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (DISARM.equals(nameKey)) {
            card.setEffect(mapOf("type", "strength", "value", -2));
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
        } else if (XIANYE_BLESSING.equals(nameKey)) {
            card.setHeal(15);
            card.setEffect(mapOf("type", "strength", "value", 3));
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.LEGENDARY);
        } else if (XIANYE_GUARD.equals(nameKey)) {
            card.setBlock(25);
            card.setEffect(mapOf("type", "guard_up", "value", 2));
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.LEGENDARY);
        } else if (DYNASTY_HORSE.equals(nameKey)) {
            card.setEffect(mapOf("type", "fragile", "value", 1));
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
        } else if (GUARD_TO_ATTACK.equals(nameKey)) {
            card.setEffect(mapOf("type", "guard_to_attack", "value", 0));
            card.setCost(1);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
        } else if (ELEMENTAL_RUSH.equals(nameKey)) {
            card.setDamage(6);
            card.setEffect(mapOf("type", "hit_multiple", "value", 5));
            card.setCost(3);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (ATTACK_AND_DEFEND.equals(nameKey)) {
            card.setDamage(5);
            card.setBlock(5);
            card.setCost(1);
            card.setTarget("enemy");
        } else if (HEAVY_ARMOR.equals(nameKey)) {
            card.setBlock(40);
            card.setCost(3);
            card.setTarget("self");
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.UNCOMMON);
        } else if (BLOODBATH.equals(nameKey)) {
            card.setEffect(listOf(
                mapOf("type", "lose_hp", "value", 10),
                mapOf("type", "restore_energy"),
                mapOf("type", "strength", "value", 5)
            ));
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.SKILL);
        } else if (ETERNAL_STORM.equals(nameKey)) {
            card.setCost(3);
            card.setDamage(15);
            card.setPenetratingDamage(15);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.UNCOMMON);
        } else if (SOUL_FLAME.equals(nameKey)) {
            card.setCost(2);
            card.setPenetratingDamage(6);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.ATTACK);
            card.setEffect(listOf(mapOf("type", "burn", "value", 5)));
        } else if (GATHER_ENERGY.equals(nameKey)) {
            card.setCost(2);
            card.setEffect(listOf(mapOf("type", "restore_energy")));
            card.setTarget("self");
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.UNCOMMON);
        } else if (FATE_MANIPULATE.equals(nameKey)) {
            card.setCost(1);
            card.setEffect(listOf(mapOf("type", "double_enemy_debuff")));
            card.setTarget("enemy");
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.RARE);
        } else if (PUMPED_UP.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.COMMON);
            card.setEffect(listOf(mapOf("type", "temp_strength", "value", 3)));
        } else if (LITTLE_BEING.equals(nameKey)) {
            card.setCost(0);
            card.setBlock(4);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.COMMON);
        } else if (FOCUSED_RAY.equals(nameKey)) {
            card.setDamage(2);
            card.setCost(1);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setEffect(mapOf("type", "damage_increase", "value", 1));
            card.setRarity(CardRarity.UNCOMMON);
        } else if (LIQUN.equals(nameKey)) {
            card.setCost(0);
            card.setHeal(1);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(listOf(mapOf("type", "gain_energy", "value", 1)));
        } else if (JUDGMENT.equals(nameKey)) {
            card.setDamage(0);
            card.setCost(0);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "judgment_damage", "value", 0));
        } else if (HOLY_SHIELD.equals(nameKey)) {
            card.setBlock(9);
            card.setCost(2);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "guard_up", "value", 1));
        } else if (FENGSHUI_LUNLIU.equals(nameKey)) {
            card.setDamage(0);
            card.setCost(1);
            card.setTarget("self");
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "draw_and_discard", "value", 3));
        } else if (GOLD_STRIKE.equals(nameKey)) {
            card.setDamage(10);
            card.setCost(1);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "gold_damage", "value", 100));
        } else if (BUY.equals(nameKey)) {
            card.setCost(0);
            card.setBlock(6);
            card.setEffect(listOf(
                mapOf("type", "lose_gold", "value", 6),
                mapOf("type", "draw_cards", "value", 1)
            ));
            card.setTarget("self");
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.UNCOMMON);
        } else if (YUANHUA.equals(nameKey)) {
            card.setEffect(mapOf("type", "frostbite_per_turn", "value", 4));
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (CHAOS_STRIKE.equals(nameKey)) {
            card.setDamage(8);
            card.setEffect(mapOf("type", "random_debuff", "value", 1));
            card.setCost(1);
            card.setType(CardType.ATTACK);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
        } else if (HOT_COLD.equals(nameKey)) {
            card.setDamage(10);
            card.setEffect(listOf(
                mapOf("type", "burn", "value", 3),
                mapOf("type", "frostbite", "value", 5)
            ));
            card.setCost(2);
            card.setType(CardType.ATTACK);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (MOCK.equals(nameKey)) {
            card.setEffect(mapOf("type", "vulnerable", "value", 1));
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
        } else if (STINKY_TOFU.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "poison", "value", 6));
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
        } else if (HIGH_KILL_INTENT.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "strength_on_hp_loss", "value", 1));
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
        } else if (POISON_QUENCH.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "poison_on_unblocked_damage", "value", 1));
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
        } else if (BRILLIANT_STRIKE.equals(nameKey)) {
            card.setDamage(6);
            card.setCost(1);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.COMMON);
            card.setEffect(mapOf("type", "gold_damage", "value", 50));
        } else if (COIN_THROW.equals(nameKey)) {
            card.setDamage(4);
            card.setCost(1);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "gold_hit_multiple", "value", 100));
        } else if (THUNDER_CALL.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "paralysis", "value", 2));
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
        } else if (BLOOD_DRINK.equals(nameKey)) {
            card.setDamage(6);
            card.setCost(1);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.RARE);
            card.setEffect(mapOf("type", "lifesteal", "value", 2, "extra", "exhaust_self"));
        } else if (UNYIELDING.equals(nameKey)) {
            card.setCost(2);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "hp_change_pierce", "value", 3));
        } else if (LIGHTNING_SHIELD.equals(nameKey)) {
            card.setCost(1);
            card.setBlock(5);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setEffect(mapOf("type", "paralysis", "value", 1));
        } else if (HEAVEN_THUNDER.equals(nameKey)) {
            card.setDamage(3);
            card.setCost(3);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.RARE);
            card.setEffect(mapOf("type", "hit_multiple", "value", 7));
        } else if (ETERNAL_CHANT.equals(nameKey)) {
            card.setCost(3);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "block_per_turn", "value", 5));
        } else if (ZEALOT.equals(nameKey)) {
            card.setDamage(4);
            card.setCost(1);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "draw_damage_up", "value", 3));
        } else if (WEATHERING.equals(nameKey)) {
            card.setDamage(20);
            card.setCost(1);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.COMMON);
            card.setEffect(mapOf("type", "draw_damage_down", "value", 2));
        } else if (FAITH_ACCUMULATION.equals(nameKey)) {
            card.setDamage(6);
            card.setCost(1);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "play_attack_damage_up", "value", 1));
        } else if (WISH_POWER.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setType(CardType.POWER);
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(listOf(
                mapOf("type", "strength", "value", 1),
                mapOf("type", "dexterity", "value", 1),
                mapOf("type", "guard_up", "value", 1)
            ));
        } else if (DIVINE_JUDGMENT.equals(nameKey)) {
            card.setCost(2);
            card.setDamage(8);
            card.setHeal(4);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.ATTACK);
        } else if (CHANT.equals(nameKey)) {
            card.setDamage(7);
            card.setCost(1);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.ATTACK);
            card.setEffect(mapOf("type", "strength", "value", -1));
        } else if (SNAKE_NOTBITE.equals(nameKey)) {
            card.setCost(2);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "poison", "value", 8));
            card.setRetain(true);
        } else if (CONCEAL.equals(nameKey)) {
            card.setCost(1);
            card.setTarget(null);
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.SKILL);
            card.setBlock(5);
            card.setEffect(mapOf("type", "lurk"));
        } else if (NIGHTFALL.equals(nameKey)) {
            card.setCost(2);
            card.setTarget(null);
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setBlock(15);
            card.setEffect(mapOf("type", "lurk"));
        } else if (SNEAK_ATTACK.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.ATTACK);
            card.setDamage(6);
            card.setEffect(mapOf("type", "assassinate"));
        } else if (SUDDEN_AMBUSH.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.ATTACK);
            card.setDamage(3);
            card.setEffect(mapOf("type", "assassinate"));
        } else if (RETIRE.equals(nameKey)) {
            card.setCost(1);
            card.setTarget(null);
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.SKILL);
            card.setBlock(8);
            card.setEffect(mapOf("type", "exit_stealth"));
        } else if (CONCENTRATED_TOXIN.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "double_poison"),
                mapOf("type", "exhaust_self")
            ));
        } else if (HIGH_HEAT_BOIL.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "double_burn"),
                mapOf("type", "exhaust_self")
            ));
        } else if (ICE_SORROW.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "double_frostbite"),
                mapOf("type", "exhaust_self")
            ));
        } else if (FREEZE_RAY.equals(nameKey)) {
            card.setCost(2);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "frostbite", "value", 15),
                mapOf("type", "draw_cards", "value", 2)
            ));
        } else if (ELEMENT_RECYCLE.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("enemy");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "element_recycle"));
        } else if (IMPREGNABLE.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setBlock(6);
            card.setEffect(mapOf("type", "gold_to_shield"));
        } else if (PAY_THE_BILL.equals(nameKey)) {
            card.setCost(0);
            card.setTarget(null);
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "spend_gold_for_energy", "value", 5));
        } else if (ECONOMIC_LAW.equals(nameKey)) {
            card.setCost(0);
            card.setTarget(null);
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "spend_gold_for_draw", "value", 10));
        } else if (SELL_SOUL.equals(nameKey)) {
            card.setCost(0);
            card.setTarget(null);
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "lose_hp", "value", 3),
                mapOf("type", "gain_gold", "value", 30)
            ));
        } else if (STEAL.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.ATTACK);
            card.setDamage(6);
            card.setEffect(mapOf("type", "gain_gold", "value", 2));
        } else if (PICKPOCKET.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "steal_draw", "value", 1));
        } else if (FINISHER.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.ATTACK);
            card.setDamage(4);
            card.setEffect(mapOf("type", "assassinate_bonus", "value", 4));
        } else if (ALL_LIVING_WISH.equals(nameKey)) {
            card.setCost(0);
            card.setTarget(null);
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "draw_per_enemy", "extra", "exhaust_self"));
        } else if (BLOOD_BURN.equals(nameKey)) {
            card.setCost(1);
            card.setTarget(null);
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "blood_burn_round", "value", 1));
        } else if (PALM_THUNDER.equals(nameKey)) {
            card.setCost(2);
            card.setDamage(7);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.ATTACK);
            card.setEffect(mapOf("type", "paralysis", "value", 2));
        } else if (GOLD_CURSE.equals(nameKey)) {
            card.setCost(3);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "vulnerable", "value", 2),
                mapOf("type", "weak", "value", 2),
                mapOf("type", "fragile", "value", 2)
            ));
        } else if (PRAYER.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "holy_energy", "value", 3));
        } else if (LIGHT_ENERGY_WAVE.equals(nameKey)) {
            card.setCost(1);
            card.setDamage(8);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "consume_holy_energy", "damage_per_layer", 4));
        } else if (DOG_SKIN_PLASTER.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.CURSE);
            card.setRarity(CardRarity.COMMON);
            card.setEffect(null);
            card.setTarget(null);
            card.setRetain(false);
        } else if (BAD_ILLNESS.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.CURSE);
            card.setRarity(CardRarity.COMMON);
            card.setEffect(null);
            card.setTarget(null);
            card.setRetain(false);
        } else if (HOPELESS.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.CURSE);
            card.setRarity(CardRarity.COMMON);
            card.setEffect(null);
            card.setTarget(null);
            card.setRetain(false);
        } else if (EMERGENCY_EVASION.equals(nameKey)) {
            card.setCost(0);
            card.setBlock(20);
            card.setTarget("self");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.SKILL);
            card.setEffect(null);
        } else if (PERSISTENT_CURSE.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.CURSE);
            card.setRarity(CardRarity.COMMON);
            card.setEffect(null);
            card.setRetain(true);
        } else if (OVERLORD_DETERRENCE.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setRarity(CardRarity.RARE);
            card.setTarget("enemy");
            card.setEffect(mapOf("type", "stun", "value", 1));
            card.setRetain(true);
        } else if (ADVANCING_TO_RETREAT.equals(nameKey)) {
            card.setCost(1);
            card.setDamage(6);
            card.setTarget(null);
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.ATTACK);
            card.setEffect(mapOf("type", "lurk"));
        } else if (SHIELD_DANCE.equals(nameKey)) {
            card.setCost(2);
            card.setDamage(0);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.ATTACK);
            card.setEffect(mapOf("type", "shield_dance", "value", 3));
        } else if (ABSOLUTE_DEFENSE.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "double_block"));
        } else if (UNMATCHED.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "next_attack_double"));
        } else if (POISON_FIRE_HEART.equals(nameKey)) {
            card.setCost(2);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "poison_burn_sync"));
        } else if (PROMISED_THING.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "draw_cards", "value", 1),
                mapOf("type", "consume_holy_energy", "value", 1)
            ));
        } else if (INSTANT_KILL.equals(nameKey)) {
            card.setCost(1);
            card.setDamage(15);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRequireStatus("assassinate");
            card.setEffect(mapOf("type", "exit_stealth"));
        } else if (BREAK_STEALTH.equals(nameKey)) {
            card.setCost(1);
            card.setDamage(8);
            card.setBlock(6);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRequireStatus("lurk");
            card.setEffect(mapOf("type", "exit_stealth"));
        } else if (GIFT_OF_BLESSING.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "holy_energy_per_turn", "value", 2));
        } else if (BODYGUARD.equals(nameKey)) {
            card.setCost(1);
            card.setDamage(8);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setEffect(mapOf("type", "consume_holy_energy", "value", 2, "extra_hit", true));
        } else if (DEMONIZE.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "lose_hp", "value", 6),
                mapOf("type", "holy_energy", "value", 10)
            ));
        } else if (DARK_GEM.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("self");
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "holy_energy_shield", "base", 5, "multiplier", 3));
        } else if (ENERGY_BACKLASH.equals(nameKey)) {
            card.setCost(1);
            card.setDamage(4);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.ATTACK);
            card.setEffect(mapOf("type", "holy_energy_hits"));
        } else if (NOURISH_SOUL.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "holy_energy_heal"));
        } else if (I_HAVE_ASCENDED.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "holy_energy_double_damage", "value", 10));
        } else if (LIGHT_BURST.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "holy_energy", "value", 5),
                mapOf("type", "block_holy_energy")
            ));
        } else if (SACRIFICE_EVERYTHING.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.POWER);
            card.setRetain(true);
            card.setEffect(mapOf("type", "triple_damage_die", "value", 3));
        } else if (ELEMENTAL_MASTERY.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "elemental_mastery", "value", 50));
            card.setTurnEndExhaust(true);
        } else if (ECHO_BURST.equals(nameKey)) {
            card.setDamage(1);
            card.setCost(1);
            card.setTarget("enemy");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.COMMON);
            card.setEffect(mapOf("type", "skill_count_damage", "value", 2));
        } else if (DEEP_HELL.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "burn_per_turn", "value", 2));
        } else if (THUNDERCLOUD_ROLL.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(listOf(
                mapOf("type", "attack_paralysis_trigger", "value", 1),
                mapOf("type", "exhaust_self")
            ));
            card.setTurnEndExhaust(true);
        } else if (INCREASE_POWER.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "paralysis_if_paralyzed", "value", 2));
            card.setRetain(true);
        } else if (URGENT_FROST.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setEffect(listOf(
                mapOf("type", "gain_energy", "value", 3),
                mapOf("type", "draw_cards", "value", 2),
                mapOf("type", "frostbite_self_per_turn", "value", 3)
            ));
        } else if (POISON_SPREAD.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "poison_transfer_on_death"));
        } else if (FIRE_CONNECT.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "burn_transfer_on_death"));
        } else if (FROST_SHATTER.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(listOf(
                mapOf("type", "transfer_frostbite", "value", 1),
                mapOf("type", "exhaust_self")
            ));
        } else if (FREEZE_RIGID.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(listOf(
                mapOf("type", "frostbite_self", "value", 4),
                mapOf("type", "gain_energy", "value", 2)
            ));
        } else if (FUEL_BOOSTER.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(listOf(
                mapOf("type", "burn", "value", 6),
                mapOf("type", "exhaust_card", "value", 1)
            ));
        } else if (VIOLENT_COMBUSTION.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.RARE);
            card.setEffect(mapOf("type", "trigger_burn"));
        } else if (EXTREME_COLD.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setEffect(mapOf("type", "frostbite_if_none", "value", 5));
        } else if (ALTERNATING_VOLTAGE.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setEffect(listOf(
                mapOf("type", "paralysis", "value", 1),
                mapOf("type", "draw_cards", "value", 2)
            ));
        } else if (HEAVEN_EARTH_LAW.equals(nameKey)) {
            card.setCost(2);
            card.setType(CardType.POWER);
            card.setTarget("enemy");
            card.setRarity(CardRarity.RARE);
            card.setEffect(listOf(
                mapOf("type", "paralysis", "value", 3),
                mapOf("type", "paralysis_no_decay")
            ));
        } else if (COLD_FEVER.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(listOf(
                mapOf("type", "convert_frostbite_to_burn"),
                mapOf("type", "exhaust_self")
            ));
        } else if (FROST_CRACK.equals(nameKey)) {
            card.setDamage(0);
            card.setCost(1);
            card.setTarget("enemy");
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "damage_by_frostbite"));
        } else if (FLAME_BURST.equals(nameKey)) {
            card.setDamage(9);
            card.setCost(2);
            card.setTarget("all_enemies");
            card.setType(CardType.ATTACK);
            card.setRarity(CardRarity.COMMON);
            card.setEffect(mapOf("type", "burn_all_enemies", "value", 4));
        } else if (CONDUCT_CIRCUIT.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(listOf(
                mapOf("type", "spread_paralysis"),
                mapOf("type", "exhaust_self")
            ));
        } else if (ELECTROMAGNETIC_FIELD.equals(nameKey)) {
            card.setCost(2);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setEffect(mapOf("type", "paralysis_per_turn", "value", 1));
        } else if (POWER_SURGE.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "draw_by_paralysis", "value", 5));
        } else if (CURRENT_SYMBIOSIS.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("enemy");
            card.setRarity(CardRarity.RARE);
            card.setEffect(listOf(
                mapOf("type", "double_paralysis"),
                mapOf("type", "exhaust_self")
            ));
        } else if (THUNDER_GOD_DESCEND.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setEffect(mapOf("type", "draw_on_paralysis"));
        } else if (OVERLOAD.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(listOf(
                mapOf("type", "gain_energy", "value", 1),
                mapOf("type", "extra_energy_if_paralysis", "value", 1),
                mapOf("type", "block_energy_gain")
            ));
        } else if (ELEMENTAL_COAT.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "elemental_coat"));
        } else if (CERTIFICATION_MASTER.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "skill_block_this_turn", "value", 4));
        } else if (PRIMORDIAL_ENERGY.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setEffect(listOf(
                mapOf("type", "gain_energy", "value", 1),
                mapOf("type", "next_turn_energy", "value", 2)
            ));
        } else if (RETURN_TO_CHAOS.equals(nameKey)) {
            card.setDamage(20);
            card.setCost(1);
            card.setType(CardType.ATTACK);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "clear_all_elemental_debuffs"));
        } else if (LIGHTNING_FLASH.equals(nameKey)) {
            card.setCost(0);
            card.setBlock(8);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.COMMON);
            card.setEffect(mapOf("type", "dexterity_if_paralysis", "value", 1));
        } else if (ELEMENT_REUSE.equals(nameKey)) {
            card.setCost(1);
            card.setBlock(8);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "next_turn_block_if_elemental", "value", 8));
        } else if (PURE_WHITE_STATE.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setEffect(mapOf("type", "clear_self_negative_status"));
        } else if (SHIELD_HERO.equals(nameKey)) {
            card.setCost(2);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "shield_hero"));
        } else if (CONSTANT_HARASS.equals(nameKey)) {
            card.setCost(2);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "constant_harass"));
        } else if (INVADE_BODY.equals(nameKey)) {
            card.setCost(2);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "invade_body"));
        } else if (GRAND_FIREWORKS.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("enemy");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "grand_fireworks", "extra", "exhaust_self"));
        } else if (ARMORED_ICE.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "frostbite_shield"));
        } else if (ROTTEN_MIASMA.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "rotten_miasma"));
        } else if (POISON_MATERIAL.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(listOf(
                mapOf("type", "draw_cards", "value", 1),
                mapOf("type", "exhaust_card"),
                mapOf("type", "poison_random", "value", 3)
            ));
        } else if (SIZZLING.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "sizzling"));
        } else if (ACCUMULATED_ELECTRIC_CHARGE.equals(nameKey)) {
            card.setCost(1);
            card.setDamage(6);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.ATTACK);
            card.setEffect(mapOf("type", "accumulated_electric_charge"));
        } else if (SNOW_ON_FROST.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "snow_on_frost"));
        } else if (FAITH_ANNIHILATION.equals(nameKey)) {
            card.setCost(2);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "faith_annihilation"));
        } else if (DEATH_BREATH.equals(nameKey)) {
            card.setCost(0);
            card.setType(CardType.SKILL);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setEffect(listOf(
                mapOf("type", "lose_max_hp", "value", 1),
                mapOf("type", "gain_energy", "value", 2),
                mapOf("type", "strength", "value", 2),
                mapOf("type", "draw_cards", "value", 2)
            ));
        } else if (BLOOD_DEMON.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "blood_demon"));
        } else if (HELL_CHAOS.equals(nameKey)) {
            card.setCost(1);
            card.setDamage(6);
            card.setType(CardType.ATTACK);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "double_strength_damage"));
        } else if (BLOOD_CLOAK.equals(nameKey)) {
            card.setCost(1);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "blood_cloak"));
        } else if (BODY_ARMOR.equals(nameKey)) {
            card.setCost(2);
            card.setType(CardType.POWER);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setEffect(mapOf("type", "shield_no_decay"));
        } else if (BATTLE_MASTER.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "stealth_trigger_shield_damage"));
        } else if (NIGHT_LIKE_DAY.equals(nameKey)) {
            card.setCost(1);
            card.setDamage(6);
            card.setTarget("enemy");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.ATTACK);
            card.setEffect(mapOf("type", "lurk_extra_hit", "value", 1));
        } else if (COME_AND_GO.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "lurk_shield_on_enter", "value", 3));
        } else if (KILL.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "assassinate_damage_on_enter", "value", 3));
        } else if (PERFECT_SKILL.equals(nameKey)) {
            card.setCost(2);
            card.setTarget("self");
            card.setRarity(CardRarity.RARE);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "stealth_negate_penalty"));
        } else if (FUTURE_STRIKE.equals(nameKey)) {
            card.setCost(0);
            card.setDamage(2);
            card.setTarget("enemy");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.ATTACK);
            card.setEffect(mapOf("type", "future_strike_gold", "value", 10));
        } else if (RECYCLE.equals(nameKey)) {
            card.setCost(1);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.POWER);
            card.setEffect(mapOf("type", "recycle", "value", 2));
        } else if (NOT_WILLING_TO_LOSE.equals(nameKey)) {
            card.setCost(0);
            card.setTarget("self");
            card.setRarity(CardRarity.COMMON);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "not_willing_to_lose"));
        } else if (AFTER_A_HUNDRED_YEARS.equals(nameKey)) {
            card.setCost(1);
            card.setBlock(6);
            card.setTarget("self");
            card.setRarity(CardRarity.UNCOMMON);
            card.setType(CardType.SKILL);
            card.setEffect(mapOf("type", "after_a_hundred_years", "value", 1));
        }
    }

    // ============== 创建默认卡组 ==============

    public static List<Card> createDefaultDeck(CharacterClass charClass) {
        List<Card> deck = new ArrayList<>();
        // 所有职业共用：4张打击 + 4张防御
        for (int i = 0; i < 4; i++) {
            deck.add(createCardWithId(STRIKE, "strike_" + i));
        }
        for (int i = 0; i < 4; i++) {
            deck.add(createCardWithId(DEFEND, "defend_" + i));
        }

        // 职业专属卡牌（各2张）
        if (charClass == CharacterClass.WARRIOR) {
            deck.add(createCardWithId(UPPERCUT, "uppercut_0"));
            deck.add(createCardWithId(BASH, "bash_0"));
        } else if (charClass == CharacterClass.MAGE) {
            deck.add(createCardWithId(FIREBALL, "fireball_0"));
            deck.add(createCardWithId(ICE_SPIKE, "ice_spike_0"));
        } else if (charClass == CharacterClass.ROGUE) {
            deck.add(createCardWithId(OPPRESSION, "oppression_0"));
            deck.add(createCardWithId(BUY, "buy_0"));
        } else if (charClass == CharacterClass.PRIEST) {
            deck.add(createCardWithId(JUDGMENT, "judgment_0"));
            deck.add(createCardWithId(HOLY_SHIELD, "holy_shield_0"));
        }

        return deck;
    }

    // ============== 工具方法 ==============

    @SuppressWarnings("unchecked")
    private static Map<String, Object> mapOf(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            m.put((String) kv[i], kv[i + 1]);
        }
        return m;
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> listOf(Map<String, Object>... items) {
        List<Map<String, Object>> list = new ArrayList<>();
        Collections.addAll(list, items);
        return list;
    }
}