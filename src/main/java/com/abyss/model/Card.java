package com.abyss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Card {
    // Card ID constants for all cards
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

    private final String id;
    private final String nameKey;
    private CardType type;
    private int cost;
    private CardRarity rarity;
    private int damage;
    private int penetratingDamage;
    private int block;
    private int heal;
    private Object effect;
    private String target;
    private boolean retain;
    private String requireStatus;
    private boolean turnEndExhaust;

    @JsonCreator
    public Card(
            @JsonProperty("id") String id,
            @JsonProperty("name_key") String nameKey,
            @JsonProperty("type") CardType type,
            @JsonProperty("cost") int cost,
            @JsonProperty("rarity") CardRarity rarity) {
        this.id = id;
        this.nameKey = nameKey;
        this.type = type;
        this.cost = cost;
        this.rarity = rarity;
        this.damage = 0;
        this.penetratingDamage = 0;
        this.block = 0;
        this.heal = 0;
        this.effect = null;
        this.target = "enemy";
        this.retain = false;
        this.requireStatus = null;
        this.turnEndExhaust = false;
        initStats();
    }

    private void initStats() {
        switch (nameKey) {
            case STRIKE -> {
                damage = 6;
                target = "enemy";
            }
            case DEFEND -> {
                block = 5;
                target = "self";
            }
            case HEAVY_STRIKE -> {
                damage = 15;
                cost = 2;
                target = "enemy";
            }
            case BASH -> {
                damage = 8;
                effect = Map.of("type", "vulnerable", "value", 2);
                target = "enemy";
            }
            case BLOOD_BLADE -> {
                damage = 3;
                cost = 0;
                target = "all_enemies";
                effect = List.of(
                        Map.of("type", "hit_multiple", "value", 2),
                        Map.of("type", "lose_hp", "value", 2)
                );
            }
            case TWIN_STRIKE -> {
                damage = 5;
                effect = Map.of("type", "hit_twice", "value", 2);
                target = "enemy";
            }
            case BLOODLETTING -> {
                effect = List.of(
                        Map.of("type", "draw_cards", "value", 2),
                        Map.of("type", "lose_hp", "value", 2)
                );
                cost = 0;
                target = "self";
            }
            case POMMEL_STRIKE -> {
                damage = 9;
                effect = Map.of("type", "draw_cards", "value", 1);
                target = "enemy";
            }
            case SHRUG_IT_OFF -> {
                block = 8;
                effect = Map.of("type", "draw_cards", "value", 1);
                target = "self";
            }
            case BATH -> {
                heal = 5;
                cost = 2;
                effect = Map.of("type", "guard_up", "value", 1);
                target = "self";
                this.type = CardType.SKILL;
            }
            case TRUE_GRIT -> {
                block = 7;
                effect = Map.of("type", "exhaust_card", "value", 1);
                target = "self";
            }
            case CARNAGE -> {
                damage = 50;
                cost = 3;
                effect = Map.of("type", "exhaust_self", "value", 1);
            }
            case UPPERCUT -> {
                damage = 13;
                cost = 2;
                effect = Map.of("type", "weak", "value", 2);
                target = "enemy";
            }
            case FURY -> {
                effect = Map.of("type", "strength", "value", 2);
                cost = 1;
                target = "self";
            }
            case SWORD_BOOMERANG -> {
                damage = 6;
                effect = Map.of("type", "hit_random", "value", 3);
                target = "enemy";
            }
            case BATTLE_TRANCE -> {
                effect = List.of(
                        Map.of("type", "draw_to_max"),
                        Map.of("type", "skip_next_turn")
                );
                cost = 0;
                target = "self";
            }
            case OFFERING -> {
                effect = List.of(
                        Map.of("type", "draw_cards", "value", 5),
                        Map.of("type", "lose_hp", "value", 3)
                );
                cost = 0;
                target = "self";
            }
            case METEOR_STRIKE -> {
                damage = 30;
                cost = 3;
                target = "enemy";
            }
            case FIREBALL -> {
                damage = 10;
                effect = Map.of("type", "burn", "value", 5);
                cost = 2;
                target = "enemy";
            }
            case ICE_SPIKE -> {
                damage = 5;
                effect = Map.of("type", "weak", "value", 1);
                target = "enemy";
            }
            case ARCANE_BLAST -> {
                damage = 10;
                effect = Map.of("type", "strength", "value", 1);
                cost = 2;
                target = "enemy";
            }
            case LIGHTNING_BOLT -> {
                damage = 12;
                effect = Map.of("type", "paralysis", "value", 1);
                cost = 2;
                target = "enemy";
            }
            case SHADOW_STRIKE -> {
                damage = 6;
                effect = Map.of("type", "poison", "value", 3);
                target = "enemy";
            }
            case BACKSTAB -> {
                damage = 12;
                cost = 0;
                target = "enemy";
            }
            case OPPRESSION -> {
                damage = 8;
                cost = 0;
                target = "enemy";
                effect = Map.of("type", "kill_gold", "value", 20);
                this.type = CardType.ATTACK;
            }
            case BLOOD_CALAMITY -> {
                damage = 20;
                cost = 2;
                target = "enemy";
                effect = Map.of("type", "kill_heal", "value", 3);
            }
            case QUICK_SLASH -> {
                damage = 4;
                cost = 0;
                effect = Map.of("type", "draw_cards", "value", 1);
                target = "enemy";
            }
            case DAGGER_THROW -> {
                damage = 5;
                effect = Map.of("type", "hit_random", "value", 2);
                target = "enemy";
            }
            case VENOM_STRIKE -> {
                damage = 5;
                effect = Map.of("type", "poison", "value", 7);
                cost = 2;
                target = "enemy";
            }
            case ASSASSINATE -> {
                damage = 50;
                cost = 3;
                target = "enemy";
            }
            case EVASION -> {
                effect = List.of(
                        Map.of("type", "guard_up", "value", 2),
                        Map.of("type", "phasing", "value", 1)
                );
                cost = 2;
                target = "self";
            }
            case ADRENALINE -> {
                effect = List.of(
                        Map.of("type", "strength", "value", 1),
                        Map.of("type", "draw_cards", "value", 1)
                );
                cost = 1;
                target = "self";
            }
            case XIANYE_FURY -> {
                damage = 50;
                block = 10;
                cost = 1;
                target = "enemy";
            }
            case BANKRUPTCY_PALM -> {
                damage = 15;
                effect = Map.of("type", "lose_gold", "value", 10);
                cost = 0;
                target = "enemy";
            }
            case GET_RICH_DUMBBELL -> {
                effect = Map.of("type", "gain_gold", "value", 30);
                cost = 1;
                target = "self";
            }
            case KNIFE_ATTACK -> {
                damage = 5;
                cost = 0;
                target = "enemy";
            }
            case IRON_STRIKE -> {
                damage = 12;
                block = 9;
                cost = 2;
                target = "enemy";
            }
            case GRADUAL_MADNESS -> {
                effect = Map.of("type", "strength_per_turn", "value", 2);
                cost = 2;
                target = "self";
            }
            case KIDNEY_PILL -> {
                effect = Map.of("type", "draw_extra", "value", 1);
                cost = 1;
                target = "self";
            }
            case DIVINE_POWER -> {
                effect = Map.of("type", "phasing", "value", 1);
                cost = 1;
                target = "self";
            }
            case ENDLESS_POWER -> {
                effect = Map.of("type", "double_strength", "value", 0);
                cost = 1;
                target = "self";
            }
            case BECOME_KING -> {
                effect = Map.of("type", "sacrifice_strength_heal", "value", 2);
                cost = 0;
                target = "self";
            }
            case DISARM -> {
                effect = Map.of("type", "strength", "value", -2);
                cost = 1;
                target = "enemy";
            }
            case XIANYE_BLESSING -> {
                heal = 15;
                effect = Map.of("type", "strength", "value", 3);
                cost = 1;
                target = "self";
            }
            case XIANYE_GUARD -> {
                block = 25;
                effect = Map.of("type", "guard_up", "value", 2);
                cost = 1;
                target = "self";
            }
            case DYNASTY_HORSE -> {
                effect = Map.of("type", "fragile", "value", 1);
                cost = 1;
                target = "enemy";
            }
            case GUARD_TO_ATTACK -> {
                effect = Map.of("type", "guard_to_attack", "value", 0);
                cost = 1;
                target = "enemy";
            }
            case ELEMENTAL_RUSH -> {
                damage = 6;
                effect = Map.of("type", "hit_multiple", "value", 5);
                cost = 3;
                target = "enemy";
            }
            case ATTACK_AND_DEFEND -> {
                damage = 5;
                block = 5;
                cost = 1;
                target = "enemy";
            }
            case HEAVY_ARMOR -> {
                block = 40;
                cost = 3;
                target = "self";
            }
            case BLOODBATH -> {
                effect = List.of(
                        Map.of("type", "lose_hp", "value", 10),
                        Map.of("type", "restore_energy"),
                        Map.of("type", "strength", "value", 5)
                );
                cost = 0;
                target = "self";
            }
            case ETERNAL_STORM -> {
                cost = 3;
                damage = 15;
                penetratingDamage = 15;
                target = "enemy";
            }
            case SOUL_FLAME -> {
                cost = 2;
                penetratingDamage = 6;
                target = "enemy";
                effect = List.of(Map.of("type", "burn", "value", 5));
            }
            case GATHER_ENERGY -> {
                cost = 2;
                effect = List.of(Map.of("type", "restore_energy"));
                target = "self";
            }
            case FATE_MANIPULATE -> {
                cost = 1;
                effect = List.of(Map.of("type", "double_enemy_debuff"));
                target = "enemy";
            }
            case PUMPED_UP -> {
                cost = 0;
                effect = List.of(Map.of("type", "temp_strength", "value", 3));
                target = "self";
            }
            case LITTLE_BEING -> {
                cost = 0;
                block = 4;
                target = "self";
            }
            case FOCUSED_RAY -> {
                damage = 2;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "damage_increase", "value", 1);
            }
            case LIQUN -> {
                cost = 0;
                heal = 1;
                target = "self";
                effect = List.of(Map.of("type", "gain_energy", "value", 1));
            }
            case JUDGMENT -> {
                damage = 0;
                cost = 0;
                target = "enemy";
                effect = Map.of("type", "judgment_damage", "value", 0);
            }
            case HOLY_SHIELD -> {
                block = 9;
                cost = 2;
                target = "self";
                effect = Map.of("type", "guard_up", "value", 1);
            }
            case FENGSHUI_LUNLIU -> {
                cost = 1;
                target = "self";
                effect = Map.of("type", "draw_and_discard", "value", 3);
            }
            case GOLD_STRIKE -> {
                damage = 10;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "gold_damage", "value", 100);
            }
            case BUY -> {
                cost = 0;
                block = 6;
                target = "self";
                effect = List.of(
                        Map.of("type", "lose_gold", "value", 6),
                        Map.of("type", "draw_cards", "value", 1)
                );
            }
            case YUANHUA -> {
                effect = Map.of("type", "frostbite_per_turn", "value", 4);
                cost = 1;
                target = "self";
            }
            case CHAOS_STRIKE -> {
                damage = 8;
                effect = Map.of("type", "random_debuff", "value", 1);
                cost = 1;
                target = "enemy";
            }
            case HOT_COLD -> {
                damage = 10;
                effect = List.of(
                        Map.of("type", "burn", "value", 3),
                        Map.of("type", "frostbite", "value", 5)
                );
                cost = 2;
                target = "enemy";
            }
            case MOCK -> {
                effect = Map.of("type", "vulnerable", "value", 1);
                cost = 0;
                target = "enemy";
            }
            case STINKY_TOFU -> {
                cost = 1;
                effect = Map.of("type", "poison", "value", 6);
                target = "enemy";
            }
            case HIGH_KILL_INTENT -> {
                cost = 1;
                effect = Map.of("type", "strength_on_hp_loss", "value", 1);
                target = "self";
            }
            case POISON_QUENCH -> {
                cost = 1;
                effect = Map.of("type", "poison_on_unblocked_damage", "value", 1);
                target = "self";
            }
            case BRILLIANT_STRIKE -> {
                damage = 6;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "gold_damage", "value", 50);
            }
            case COIN_THROW -> {
                damage = 4;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "gold_hit_multiple", "value", 100);
            }
            case THUNDER_CALL -> {
                cost = 1;
                effect = Map.of("type", "paralysis", "value", 2);
                target = "enemy";
            }
            case BLOOD_DRINK -> {
                damage = 6;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "lifesteal", "value", 2, "extra", "exhaust_self");
            }
            case UNYIELDING -> {
                cost = 2;
                effect = Map.of("type", "hp_change_pierce", "value", 3);
                target = "self";
            }
            case LIGHTNING_SHIELD -> {
                cost = 1;
                block = 5;
                effect = Map.of("type", "paralysis", "value", 1);
                target = "enemy";
            }
            case HEAVEN_THUNDER -> {
                damage = 3;
                cost = 3;
                target = "enemy";
                effect = Map.of("type", "hit_multiple", "value", 7);
            }
            case ETERNAL_CHANT -> {
                cost = 3;
                effect = Map.of("type", "block_per_turn", "value", 5);
                target = "self";
            }
            case ZEALOT -> {
                damage = 4;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "draw_damage_up", "value", 3);
            }
            case WEATHERING -> {
                damage = 20;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "draw_damage_down", "value", 2);
            }
            case FAITH_ACCUMULATION -> {
                damage = 6;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "play_attack_damage_up", "value", 1);
            }
            case WISH_POWER -> {
                cost = 1;
                target = "self";
                effect = List.of(
                        Map.of("type", "strength", "value", 1),
                        Map.of("type", "dexterity", "value", 1),
                        Map.of("type", "guard_up", "value", 1)
                );
            }
            case DIVINE_JUDGMENT -> {
                cost = 2;
                damage = 8;
                heal = 4;
                target = "enemy";
            }
            case CHANT -> {
                damage = 7;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "strength", "value", -1);
            }
            case SNAKE_NOTBITE -> {
                cost = 2;
                effect = Map.of("type", "poison", "value", 8);
                target = "enemy";
                retain = true;
            }
            case CONCEAL -> {
                cost = 1;
                block = 5;
                effect = Map.of("type", "lurk");
                target = null;
            }
            case NIGHTFALL -> {
                cost = 2;
                block = 15;
                effect = Map.of("type", "lurk");
                target = null;
            }
            case SNEAK_ATTACK -> {
                cost = 1;
                damage = 6;
                effect = Map.of("type", "assassinate");
                target = "enemy";
            }
            case SUDDEN_AMBUSH -> {
                cost = 0;
                damage = 3;
                effect = Map.of("type", "assassinate");
                target = "enemy";
            }
            case RETIRE -> {
                cost = 1;
                block = 8;
                effect = Map.of("type", "exit_stealth");
                target = null;
            }
            case CONCENTRATED_TOXIN -> {
                cost = 1;
                effect = List.of(
                        Map.of("type", "double_poison"),
                        Map.of("type", "exhaust_self")
                );
                target = "enemy";
            }
            case HIGH_HEAT_BOIL -> {
                cost = 1;
                effect = List.of(
                        Map.of("type", "double_burn"),
                        Map.of("type", "exhaust_self")
                );
                target = "enemy";
            }
            case ICE_SORROW -> {
                cost = 1;
                effect = List.of(
                        Map.of("type", "double_frostbite"),
                        Map.of("type", "exhaust_self")
                );
                target = "enemy";
            }
            case FREEZE_RAY -> {
                cost = 2;
                effect = List.of(
                        Map.of("type", "frostbite", "value", 15),
                        Map.of("type", "draw_cards", "value", 2)
                );
                target = "enemy";
            }
            case ELEMENT_RECYCLE -> {
                cost = 0;
                effect = Map.of("type", "element_recycle");
                target = "enemy";
            }
            case IMPREGNABLE -> {
                cost = 1;
                block = 6;
                effect = Map.of("type", "gold_to_shield");
                target = "self";
            }
            case PAY_THE_BILL -> {
                cost = 0;
                effect = Map.of("type", "spend_gold_for_energy", "value", 5);
                target = null;
            }
            case ECONOMIC_LAW -> {
                cost = 0;
                effect = Map.of("type", "spend_gold_for_draw", "value", 10);
                target = null;
            }
            case SELL_SOUL -> {
                cost = 0;
                effect = List.of(
                        Map.of("type", "lose_hp", "value", 3),
                        Map.of("type", "gain_gold", "value", 30)
                );
                target = null;
            }
            case STEAL -> {
                damage = 6;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "gain_gold", "value", 2);
            }
            case PICKPOCKET -> {
                cost = 0;
                effect = Map.of("type", "steal_draw", "value", 1);
                target = "self";
            }
            case FINISHER -> {
                cost = 0;
                damage = 4;
                effect = Map.of("type", "assassinate_bonus", "value", 4);
                target = "enemy";
            }
            case ALL_LIVING_WISH -> {
                cost = 0;
                effect = Map.of("type", "draw_per_enemy");
                target = null;
            }
            case BLOOD_BURN -> {
                cost = 1;
                effect = Map.of("type", "blood_burn_round", "value", 1);
                target = null;
            }
            case PALM_THUNDER -> {
                cost = 2;
                damage = 7;
                effect = Map.of("type", "paralysis", "value", 2);
                target = "enemy";
            }
            case GOLD_CURSE -> {
                cost = 3;
                effect = List.of(
                        Map.of("type", "vulnerable", "value", 2),
                        Map.of("type", "weak", "value", 2),
                        Map.of("type", "fragile", "value", 2)
                );
                target = "enemy";
            }
            case PRAYER -> {
                cost = 0;
                effect = Map.of("type", "holy_energy", "value", 3);
                target = "self";
            }
            case LIGHT_ENERGY_WAVE -> {
                cost = 1;
                damage = 8;
                effect = Map.of("type", "consume_holy_energy", "damage_per_layer", 4);
                target = "enemy";
            }
            case DOG_SKIN_PLASTER -> {
                cost = 0;
                target = null;
                effect = null;
                retain = false;
            }
            case BAD_ILLNESS -> {
                cost = 0;
                target = null;
                effect = null;
                retain = false;
            }
            case HOPELESS -> {
                cost = 0;
                target = null;
                effect = null;
                retain = false;
            }
            case EMERGENCY_EVASION -> {
                cost = 0;
                block = 20;
                target = "self";
                effect = null;
            }
            case PERSISTENT_CURSE -> {
                cost = 0;
                target = null;
                effect = null;
                retain = true;
            }
            case OVERLORD_DETERRENCE -> {
                cost = 0;
                effect = Map.of("type", "stun", "value", 1);
                target = "enemy";
                retain = true;
            }
            case ADVANCING_TO_RETREAT -> {
                damage = 6;
                cost = 1;
                effect = Map.of("type", "lurk");
                target = null;
            }
            case SHIELD_DANCE -> {
                damage = 0;
                cost = 2;
                effect = Map.of("type", "shield_dance", "value", 3);
                target = "enemy";
            }
            case ABSOLUTE_DEFENSE -> {
                cost = 1;
                effect = Map.of("type", "double_block");
                target = "self";
            }
            case UNMATCHED -> {
                cost = 1;
                effect = Map.of("type", "next_attack_double");
                target = "self";
            }
            case POISON_FIRE_HEART -> {
                cost = 2;
                effect = Map.of("type", "poison_burn_sync");
                target = "self";
            }
            case PROMISED_THING -> {
                cost = 0;
                effect = List.of(
                        Map.of("type", "draw_cards", "value", 1),
                        Map.of("type", "consume_holy_energy", "value", 1)
                );
                target = "self";
            }
            case INSTANT_KILL -> {
                damage = 15;
                cost = 1;
                effect = Map.of("type", "exit_stealth");
                target = "enemy";
                requireStatus = "assassinate";
            }
            case BREAK_STEALTH -> {
                damage = 8;
                block = 6;
                cost = 1;
                effect = Map.of("type", "exit_stealth");
                target = "enemy";
                requireStatus = "lurk";
            }
            case GIFT_OF_BLESSING -> {
                cost = 1;
                effect = Map.of("type", "holy_energy_per_turn", "value", 2);
                target = "self";
            }
            case BODYGUARD -> {
                damage = 8;
                cost = 1;
                effect = Map.of("type", "consume_holy_energy", "value", 2, "extra_hit", true);
                target = "enemy";
            }
            case DEMONIZE -> {
                cost = 0;
                effect = List.of(
                        Map.of("type", "lose_hp", "value", 6),
                        Map.of("type", "holy_energy", "value", 10)
                );
                target = "self";
            }
            case DARK_GEM -> {
                cost = 0;
                effect = Map.of("type", "holy_energy_shield", "base", 5, "multiplier", 3);
                target = "self";
            }
            case ENERGY_BACKLASH -> {
                damage = 4;
                cost = 1;
                effect = Map.of("type", "holy_energy_hits");
                target = "enemy";
            }
            case NOURISH_SOUL -> {
                cost = 0;
                effect = Map.of("type", "holy_energy_heal");
                target = "self";
            }
            case I_HAVE_ASCENDED -> {
                cost = 0;
                effect = Map.of("type", "holy_energy_double_damage", "value", 10);
                target = "self";
            }
            case LIGHT_BURST -> {
                cost = 0;
                effect = List.of(
                        Map.of("type", "holy_energy", "value", 5),
                        Map.of("type", "block_holy_energy")
                );
                target = "self";
            }
            case SACRIFICE_EVERYTHING -> {
                cost = 0;
                effect = Map.of("type", "triple_damage_die", "value", 3);
                target = "self";
                retain = true;
            }
            case ELEMENTAL_MASTERY -> {
                cost = 1;
                effect = Map.of("type", "elemental_mastery", "value", 50);
                target = "self";
                turnEndExhaust = true;
            }
            case ECHO_BURST -> {
                damage = 1;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "skill_count_damage", "value", 2);
            }
            case DEEP_HELL -> {
                cost = 1;
                effect = Map.of("type", "burn_per_turn", "value", 2);
                target = "self";
            }
            case THUNDERCLOUD_ROLL -> {
                cost = 1;
                effect = List.of(
                        Map.of("type", "attack_paralysis_trigger", "value", 1),
                        Map.of("type", "exhaust_self")
                );
                target = "self";
                turnEndExhaust = true;
            }
            case INCREASE_POWER -> {
                cost = 1;
                effect = Map.of("type", "paralysis_if_paralyzed", "value", 2);
                target = "enemy";
                retain = true;
            }
            case URGENT_FROST -> {
                cost = 0;
                effect = List.of(
                        Map.of("type", "gain_energy", "value", 3),
                        Map.of("type", "draw_cards", "value", 2),
                        Map.of("type", "frostbite_self_per_turn", "value", 3)
                );
                target = "self";
            }
            case POISON_SPREAD -> {
                cost = 1;
                effect = Map.of("type", "poison_transfer_on_death");
                target = "self";
            }
            case FIRE_CONNECT -> {
                cost = 1;
                effect = Map.of("type", "burn_transfer_on_death");
                target = "self";
            }
            case FROST_SHATTER -> {
                cost = 0;
                effect = List.of(
                        Map.of("type", "transfer_frostbite", "value", 1),
                        Map.of("type", "exhaust_self")
                );
                target = "enemy";
            }
            case FREEZE_RIGID -> {
                cost = 0;
                effect = List.of(
                        Map.of("type", "frostbite_self", "value", 4),
                        Map.of("type", "gain_energy", "value", 2)
                );
                target = "self";
            }
            case FUEL_BOOSTER -> {
                cost = 1;
                effect = List.of(
                        Map.of("type", "burn", "value", 6),
                        Map.of("type", "exhaust_card", "value", 1)
                );
                target = "enemy";
            }
            case VIOLENT_COMBUSTION -> {
                cost = 1;
                effect = Map.of("type", "trigger_burn");
                target = "enemy";
            }
            case EXTREME_COLD -> {
                cost = 0;
                effect = Map.of("type", "frostbite_if_none", "value", 5);
                target = "enemy";
            }
            case ALTERNATING_VOLTAGE -> {
                cost = 1;
                effect = List.of(
                        Map.of("type", "paralysis", "value", 1),
                        Map.of("type", "draw_cards", "value", 2)
                );
                target = "enemy";
            }
            case HEAVEN_EARTH_LAW -> {
                cost = 2;
                effect = List.of(
                        Map.of("type", "paralysis", "value", 3),
                        Map.of("type", "paralysis_no_decay")
                );
                target = "enemy";
            }
            case COLD_FEVER -> {
                cost = 1;
                effect = List.of(
                        Map.of("type", "convert_frostbite_to_burn"),
                        Map.of("type", "exhaust_self")
                );
                target = "enemy";
            }
            case FROST_CRACK -> {
                damage = 0;
                cost = 1;
                effect = Map.of("type", "damage_by_frostbite");
                target = "enemy";
            }
            case FLAME_BURST -> {
                damage = 9;
                cost = 2;
                target = "all_enemies";
                effect = Map.of("type", "burn_all_enemies", "value", 4);
            }
            case CONDUCT_CIRCUIT -> {
                cost = 1;
                effect = List.of(
                        Map.of("type", "spread_paralysis"),
                        Map.of("type", "exhaust_self")
                );
                target = "enemy";
            }
            case ELECTROMAGNETIC_FIELD -> {
                cost = 2;
                effect = Map.of("type", "paralysis_per_turn", "value", 1);
                target = "self";
            }
            case POWER_SURGE -> {
                cost = 1;
                effect = Map.of("type", "draw_by_paralysis", "value", 5);
                target = "self";
            }
            case CURRENT_SYMBIOSIS -> {
                cost = 1;
                effect = List.of(
                        Map.of("type", "double_paralysis"),
                        Map.of("type", "exhaust_self")
                );
                target = "enemy";
            }
            case THUNDER_GOD_DESCEND -> {
                cost = 1;
                effect = Map.of("type", "draw_on_paralysis");
                target = "self";
            }
            case OVERLOAD -> {
                cost = 0;
                effect = List.of(
                        Map.of("type", "gain_energy", "value", 1),
                        Map.of("type", "extra_energy_if_paralysis", "value", 1),
                        Map.of("type", "block_energy_gain")
                );
                target = "self";
            }
            case ELEMENTAL_COAT -> {
                cost = 1;
                effect = Map.of("type", "elemental_coat");
                target = "self";
            }
            case CERTIFICATION_MASTER -> {
                cost = 1;
                effect = Map.of("type", "skill_block_this_turn", "value", 4);
                target = "self";
            }
            case PRIMORDIAL_ENERGY -> {
                cost = 0;
                effect = List.of(
                        Map.of("type", "gain_energy", "value", 1),
                        Map.of("type", "next_turn_energy", "value", 2)
                );
                target = "self";
            }
            case RETURN_TO_CHAOS -> {
                damage = 20;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "clear_all_elemental_debuffs");
            }
            case LIGHTNING_FLASH -> {
                cost = 0;
                block = 8;
                effect = Map.of("type", "dexterity_if_paralysis", "value", 1);
                target = "self";
            }
            case ELEMENT_REUSE -> {
                cost = 1;
                block = 8;
                effect = Map.of("type", "next_turn_block_if_elemental", "value", 8);
                target = "self";
            }
            case PURE_WHITE_STATE -> {
                cost = 0;
                effect = Map.of("type", "clear_self_negative_status");
                target = "self";
            }
            case SHIELD_HERO -> {
                cost = 2;
                effect = Map.of("type", "shield_hero");
                target = "self";
            }
            case CONSTANT_HARASS -> {
                cost = 2;
                effect = Map.of("type", "constant_harass");
                target = "self";
            }
            case INVADE_BODY -> {
                cost = 2;
                effect = Map.of("type", "invade_body");
                target = "self";
            }
            case GRAND_FIREWORKS -> {
                cost = 1;
                effect = Map.of("type", "grand_fireworks", "extra", "exhaust_self");
                target = "enemy";
            }
            case ARMORED_ICE -> {
                cost = 1;
                effect = Map.of("type", "frostbite_shield");
                target = "self";
            }
            case ROTTEN_MIASMA -> {
                cost = 1;
                effect = Map.of("type", "rotten_miasma");
                target = "self";
            }
            case POISON_MATERIAL -> {
                cost = 1;
                effect = List.of(
                        Map.of("type", "draw_cards", "value", 1),
                        Map.of("type", "exhaust_card"),
                        Map.of("type", "poison_random", "value", 3)
                );
                target = "self";
            }
            case SIZZLING -> {
                cost = 1;
                effect = Map.of("type", "sizzling");
                target = "self";
            }
            case ACCUMULATED_ELECTRIC_CHARGE -> {
                damage = 6;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "accumulated_electric_charge");
            }
            case SNOW_ON_FROST -> {
                cost = 1;
                effect = Map.of("type", "snow_on_frost");
                target = "self";
            }
            case FAITH_ANNIHILATION -> {
                cost = 2;
                effect = Map.of("type", "faith_annihilation");
                target = "self";
            }
            case DEATH_BREATH -> {
                cost = 0;
                effect = List.of(
                        Map.of("type", "lose_max_hp", "value", 1),
                        Map.of("type", "gain_energy", "value", 2),
                        Map.of("type", "strength", "value", 2),
                        Map.of("type", "draw_cards", "value", 2)
                );
                target = "self";
            }
            case BLOOD_DEMON -> {
                cost = 1;
                effect = Map.of("type", "blood_demon");
                target = "self";
            }
            case HELL_CHAOS -> {
                damage = 6;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "double_strength_damage");
            }
            case BLOOD_CLOAK -> {
                cost = 1;
                effect = Map.of("type", "blood_cloak");
                target = "self";
            }
            case BODY_ARMOR -> {
                cost = 2;
                effect = Map.of("type", "shield_no_decay");
                target = "self";
            }
            case BATTLE_MASTER -> {
                cost = 1;
                effect = Map.of("type", "stealth_trigger_shield_damage");
                target = "self";
            }
            case NIGHT_LIKE_DAY -> {
                damage = 6;
                cost = 1;
                target = "enemy";
                effect = Map.of("type", "lurk_extra_hit", "value", 1);
            }
            case COME_AND_GO -> {
                cost = 1;
                effect = Map.of("type", "lurk_shield_on_enter", "value", 3);
                target = "self";
            }
            case KILL -> {
                cost = 1;
                effect = Map.of("type", "assassinate_damage_on_enter", "value", 3);
                target = "self";
            }
            case PERFECT_SKILL -> {
                cost = 2;
                effect = Map.of("type", "stealth_negate_penalty");
                target = "self";
            }
            case FUTURE_STRIKE -> {
                damage = 2;
                cost = 0;
                target = "enemy";
                effect = Map.of("type", "future_strike_gold", "value", 10);
            }
            case RECYCLE -> {
                cost = 1;
                effect = Map.of("type", "recycle", "value", 2);
                target = "self";
            }
            case NOT_WILLING_TO_LOSE -> {
                cost = 0;
                effect = Map.of("type", "not_willing_to_lose");
                target = "self";
            }
            case AFTER_A_HUNDRED_YEARS -> {
                cost = 1;
                block = 6;
                effect = Map.of("type", "after_a_hundred_years", "value", 1);
                target = "self";
            }
        }
    }

    // Getters
    @JsonProperty("id")
    public String getId() { return id; }

    @JsonProperty("name_key")
    public String getNameKey() { return nameKey; }

    @JsonProperty("type")
    public CardType getType() { return type; }

    @JsonProperty("cost")
    public int getCost() { return cost; }

    @JsonProperty("rarity")
    public CardRarity getRarity() { return rarity; }

    @JsonProperty("damage")
    public int getDamage() { return damage; }

    @JsonProperty("penetrating_damage")
    public int getPenetratingDamage() { return penetratingDamage; }

    @JsonProperty("block")
    public int getBlock() { return block; }

    @JsonProperty("heal")
    public int getHeal() { return heal; }

    @JsonProperty("effect")
    public Object getEffect() { return effect; }

    @JsonProperty("target")
    public String getTarget() { return target; }

    @JsonProperty("retain")
    public boolean isRetain() { return retain; }

    @JsonProperty("require_status")
    public String getRequireStatus() { return requireStatus; }

    public boolean isTurnEndExhaust() { return turnEndExhaust; }

    /**
     * 检查卡牌效果是否包含指定类型。
     */
    public boolean hasEffectType(String type) {
        if (effect == null) return false;
        if (effect instanceof Map) {
            return type.equals(((Map<?, ?>) effect).get("type"));
        } else if (effect instanceof List) {
            for (Object e : (List<?>) effect) {
                if (e instanceof Map && type.equals(((Map<?, ?>) e).get("type"))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取指定效果类型的数值。
     */
    public int getEffectValue(String type, int defaultValue) {
        if (effect == null) return defaultValue;
        if (effect instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) effect;
            if (type.equals(m.get("type"))) {
                Object val = m.get("value");
                return val instanceof Number ? ((Number) val).intValue() : defaultValue;
            }
        } else if (effect instanceof List) {
            for (Object e : (List<?>) effect) {
                if (e instanceof Map) {
                    Map<?, ?> m = (Map<?, ?>) e;
                    if (type.equals(m.get("type"))) {
                        Object val = m.get("value");
                        return val instanceof Number ? ((Number) val).intValue() : defaultValue;
                    }
                }
            }
        }
        return defaultValue;
    }

    /**
     * 将 effect 字段转换为 List&lt;Map&lt;String, Object&gt;&gt; 形式。
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getEffectsAsList() {
        if (effect == null) return List.of();
        if (effect instanceof List) {
            List<Map<String, Object>> result = new ArrayList<>();
            for (Object e : (List<?>) effect) {
                if (e instanceof Map) {
                    result.add((Map<String, Object>) e);
                }
            }
            return result;
        } else if (effect instanceof Map) {
            return List.of((Map<String, Object>) effect);
        }
        return List.of();
    }

    /**
     * 检查卡牌是否消耗自身（打出后进入消耗堆）。
     */
    public boolean isExhaustSelf() {
        return hasEffectType("exhaust_self");
    }

    // Setters for mutable fields
    public void setType(CardType type) { this.type = type; }
    public void setCost(int cost) { this.cost = cost; }
    public void setRarity(CardRarity rarity) { this.rarity = rarity; }
    public void setDamage(int damage) { this.damage = damage; }
    public void setPenetratingDamage(int penetratingDamage) { this.penetratingDamage = penetratingDamage; }
    public void setBlock(int block) { this.block = block; }
    public void setHeal(int heal) { this.heal = heal; }
    public void setEffect(Object effect) { this.effect = effect; }
    public void setTarget(String target) { this.target = target; }
    public void setRetain(boolean retain) { this.retain = retain; }
    public void setRequireStatus(String requireStatus) { this.requireStatus = requireStatus; }
    public void setTurnEndExhaust(boolean turnEndExhaust) { this.turnEndExhaust = turnEndExhaust; }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new java.util.LinkedHashMap<>();
        m.put("id", id);
        m.put("name_key", nameKey);
        m.put("type", type.getValue());
        m.put("cost", cost);
        m.put("rarity", rarity.getValue());
        m.put("damage", damage);
        m.put("penetrating_damage", penetratingDamage);
        m.put("block", block);
        m.put("heal", heal);
        m.put("effect", effect);
        m.put("target", target);
        m.put("retain", retain);
        m.put("require_status", requireStatus);
        return m;
    }

    public static Card fromMap(Map<String, Object> data) {
        String id = (String) data.get("id");
        String nameKey = (String) data.get("name_key");
        String typeStr = (String) data.get("type");
        if ("defense".equals(typeStr)) {
            typeStr = "skill";
        }
        CardType type = CardType.valueOf(typeStr.toUpperCase());
        int cost = ((Number) data.get("cost")).intValue();
        String rarityStr = (String) data.get("rarity");
        CardRarity rarity = CardRarity.valueOf(rarityStr.toUpperCase());

        Card card = new Card(id, nameKey, type, cost, rarity);
        card.damage = data.containsKey("damage") ? ((Number) data.get("damage")).intValue() : 0;
        card.penetratingDamage = data.containsKey("penetrating_damage") ? ((Number) data.get("penetrating_damage")).intValue() : 0;
        card.block = data.containsKey("block") ? ((Number) data.get("block")).intValue() : 0;
        card.heal = data.containsKey("heal") ? ((Number) data.get("heal")).intValue() : 0;
        card.effect = data.get("effect");
        card.target = data.containsKey("target") ? (String) data.get("target") : "enemy";
        card.retain = data.containsKey("retain") ? (Boolean) data.get("retain") : false;
        card.requireStatus = data.containsKey("require_status") ? (String) data.get("require_status") : null;
        return card;
    }
}
