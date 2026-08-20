package com.abyss.system;

import com.abyss.model.Enemy;

import java.util.*;

public class EnemyData {

    private static final Map<String, Map<String, Object>> ENEMY_DATA = new LinkedHashMap<>();

    static {
        // === 普通怪物 ===
        ENEMY_DATA.put("slime", dataOf(24, 4, 8, "普通的史莱姆，行动缓慢", "普通"));
        ENEMY_DATA.put("goblin", dataOf(20, 5, 6, "狡猾的哥布林，喜欢偷袭", "普通"));
        ENEMY_DATA.put("skeleton", dataOf(28, 6, 10, "不死骷髅兵，不知疲倦", "普通"));
        ENEMY_DATA.put("orc", dataOf(32, 8, 12, "强壮的兽人，攻击力较高", "普通"));
        ENEMY_DATA.put("vampire", dataOf(28, 7, 11, "嗜血的吸血鬼，会吸取生命", "普通"));
        ENEMY_DATA.put("demon", dataOf(36, 9, 15, "来自地狱的恶魔，十分危险", "普通"));
        ENEMY_DATA.put("mushroom", dataOf(22, 4, 7, "蘑菇精，每回合获得3点格挡", "普通"));
        ENEMY_DATA.put("bat", dataOf(18, 5, 6, "嗜血蝙蝠，攻击时回复2点生命", "普通"));
        ENEMY_DATA.put("gargoyle", dataOf(40, 3, 9, "石像鬼，战斗开始时获得8点格挡", "普通"));
        ENEMY_DATA.put("viper", dataOf(20, 4, 8, "毒蛇，攻击时施加1层中毒", "普通"));
        ENEMY_DATA.put("wraith", dataOf(25, 6, 10, "幽灵，死亡时对玩家造成5点伤害", "普通"));
        ENEMY_DATA.put("goblin_shaman", dataOf(22, 4, 9, "哥布林萨满，每2回合增加1点力量", "普通"));
        ENEMY_DATA.put("werewolf", dataOf(34, 7, 11, "凶猛的狼人，月圆之夜力量倍增", "普通"));
        ENEMY_DATA.put("elf_archer", dataOf(16, 9, 9, "敏捷的精灵射手，箭无虚发，高攻低血", "普通"));
        ENEMY_DATA.put("goblin_bomber", dataOf(14, 3, 7, "疯狂的哥布林，身上绑满了炸弹，死亡时造成6点伤害", "普通"));
        ENEMY_DATA.put("stone_golem", dataOf(50, 4, 13, "坚硬的石人，每回合获得4点格挡", "普通"));
        ENEMY_DATA.put("frost_spider", dataOf(20, 4, 8, "来自极寒之地的冰霜蜘蛛，攻击时施加1层冻伤", "普通"));
        ENEMY_DATA.put("goblin_warlock", dataOf(24, 5, 10, "精通诅咒的哥布林术士，攻击时施加1层虚弱", "普通"));
        ENEMY_DATA.put("imp", dataOf(10, 10, 6, "调皮的小恶魔，攻击力惊人但非常脆弱", "普通"));
        ENEMY_DATA.put("shadow_wraith", dataOf(60, 10, 28, "暗影幽灵，每回合获得1层虚化，高攻低血", "精英"));
        ENEMY_DATA.put("bandit", dataOf(30, 7, 14, "强盗，攻击时额外获得1金币", "普通"));
        ENEMY_DATA.put("cursed_statue", dataOf(36, 5, 11, "诅咒雕像，战斗开始时给玩家施加1层虚弱", "普通"));
        ENEMY_DATA.put("thunder_bird", dataOf(20, 6, 9, "雷鸟，攻击时施加1层麻痹", "普通"));
        ENEMY_DATA.put("healing_nymph", dataOf(24, 3, 10, "治愈精灵，每回合回复所有队友3点生命", "普通"));
        ENEMY_DATA.put("fire_imp", dataOf(14, 8, 7, "火焰小鬼，攻击时施加1层灼烧", "普通"));

        // === 精英怪 ===
        ENEMY_DATA.put("goblin_captain", dataOf(75, 16, 30, "哥布林队长，统领小喽啰", "精英"));
        ENEMY_DATA.put("skeleton_knight", dataOf(85, 17, 35, "骷髅骑士，装备精良", "精英"));
        ENEMY_DATA.put("orc_warrior", dataOf(100, 19, 38, "兽人战士，凶猛残暴", "精英"));
        ENEMY_DATA.put("slime_ang", dataOf(110, 12, 32, "史莱姆ang死亡时分裂成两个史莱姆", "精英"));
        ENEMY_DATA.put("terror_eye", dataOf(240, 8, 38, "恐怖眼球，血量很高的眼球", "精英"));
        ENEMY_DATA.put("chicken_hotpot_killer", dataOf(92, 15, 38, "鸡煲杀手，第二回合起，每打出一张非攻击牌，增加3点力量", "精英"));
        ENEMY_DATA.put("blood_monster", dataOf(105, 14, 35, "血液怪，回复等同造成的伤害的血量", "精英"));
        ENEMY_DATA.put("shadow_assassin", dataOf(55, 18, 32, "暗影刺客，每回合获得1层虚化，高攻低血", "精英"));
        ENEMY_DATA.put("lava_beast", dataOf(130, 10, 35, "熔岩巨兽，每回合获得5点格挡，攻击时附加2层灼烧", "精英"));
        ENEMY_DATA.put("frost_mage", dataOf(70, 8, 33, "冰霜法师，每回合给玩家施加2层冻伤", "精英"));
        ENEMY_DATA.put("curse_priest", dataOf(80, 9, 34, "诅咒祭司，单数回合施加1层虚弱，双数回合施加1层易伤", "精英"));
        ENEMY_DATA.put("poison_witch", dataOf(85, 12, 35, "毒术士，每次造成未被格挡的伤害，给予3层中毒", "精英"));
        ENEMY_DATA.put("snow_fairy", dataOf(75, 10, 33, "雪妖精，每回合给予玩家2层冻伤，免疫冻伤", "精英"));
        ENEMY_DATA.put("thorn_ghost", dataOf(95, 14, 36, "荆棘鬼，每次受到攻击，对玩家造成2点伤害", "精英"));

        // === Boss ===
        ENEMY_DATA.put("killer_machine", dataOf(300, 21, 90, "杀手机器，拥有机甲之力。", "Boss"));
        ENEMY_DATA.put("dragon", dataOf(278, 20, 70, "远古巨龙，拥有龙息之力。", "Boss"));
        ENEMY_DATA.put("lich", dataOf(240, 19, 60, "巫妖王，掌控死亡之力。", "Boss"));
        ENEMY_DATA.put("dark_lord", dataOf(345, 25, 90, "暗黑领主，拥有诅咒之力。", "Boss"));

        // === 双生BOSS ===
        ENEMY_DATA.put("disaster_left_hand", dataOf(200, 14, 45, "灾祸左手，与诅咒右手成对出现。", "Boss"));
        ENEMY_DATA.put("curse_right_hand", dataOf(200, 14, 45, "诅咒右手，与灾祸左手成对出现。", "Boss"));

        // === 僵王小老弟召唤的小怪 ===
        ENEMY_DATA.put("normal_zombie", dataOf(25, 10, 8, "普通僵尸，最多存在3回合", "召唤"));
        ENEMY_DATA.put("bucket_zombie", dataOf(50, 10, 15, "铁桶僵尸，血量更厚，最多存在4回合", "召唤"));
        ENEMY_DATA.put("raging_newspaper", dataOf(25, 20, 12, "暴走读报，攻击力高，最多存在4回合", "召唤"));
        ENEMY_DATA.put("iron_door_zombie", dataOf(25, 10, 20, "铁门僵尸，拥有50点常驻护盾且不会消失，最多存在4回合", "召唤"));

        // === BOSS: 僵王小老弟 ===
        ENEMY_DATA.put("jiangwang_xiao_laodi", dataOf(320, 22, 100, "僵王小老弟", "Boss"));
    }

    private static Map<String, Object> dataOf(int hp, int attack, int gold, String desc, String type) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("hp", hp);
        data.put("attack", attack);
        data.put("gold", gold);
        data.put("desc", desc);
        data.put("type", type);
        return data;
    }

    public static Enemy createEnemy(String id, int enemyId) {
        Map<String, Object> data = ENEMY_DATA.get(id);
        if (data == null) return null;

        int hp = ((Number) data.get("hp")).intValue();
        int attack = ((Number) data.get("attack")).intValue();
        int gold = ((Number) data.get("gold")).intValue();
        String type = (String) data.get("type");

        Enemy enemy = new Enemy(enemyId, id, hp, hp, attack);
        enemy.setGoldDrop(gold);

        if ("精英".equals(type)) {
            enemy.setElite(true);
        } else if ("Boss".equals(type)) {
            enemy.setBoss(true);
        }

        // 特殊能力设置
        setSpecialAbility(enemy);
        return enemy;
    }

    public static Enemy createEnemy(String id) {
        return createEnemy(id, 0);
    }

    private static void setSpecialAbility(Enemy enemy) {
        switch (enemy.getNameKey()) {
            case "mushroom": enemy.setSpecialAbility("regen_block"); break;
            case "bat": enemy.setSpecialAbility("minor_lifesteal"); break;
            case "viper": enemy.setSpecialAbility("poison_attack"); break;
            case "goblin_shaman": enemy.setSpecialAbility("periodic_buff"); break;
            case "frost_spider": enemy.setSpecialAbility("frost_attack"); break;
            case "goblin_warlock": enemy.setSpecialAbility("weak_attack"); break;
            case "shadow_wraith": enemy.setSpecialAbility("shadow_phase_common"); break;
            case "bandit": enemy.setSpecialAbility("bandit_steal"); break;
            case "healing_nymph": enemy.setSpecialAbility("heal_ally"); break;
            case "thunder_bird": enemy.setSpecialAbility("paralysis_attack"); break;
            case "fire_imp": enemy.setSpecialAbility("burn_attack_1"); break;
            case "stone_golem": enemy.setSpecialAbility("stone_armor"); break;
            case "blood_monster": enemy.setSpecialAbility("lifesteal"); break;
            case "shadow_assassin": enemy.setSpecialAbility("shadow_phase"); break;
            case "lava_beast": enemy.setSpecialAbility("lava_armor"); break;
            case "frost_mage": enemy.setSpecialAbility("frost_curse"); break;
            case "curse_priest": enemy.setSpecialAbility("curse_weak"); break;
            case "poison_witch": enemy.setSpecialAbility("poison_attack_3"); break;
            case "snow_fairy": enemy.setSpecialAbility("frost_curse"); enemy.setFrostbiteImmune(true); break;
            case "thorn_ghost": enemy.setSpecialAbility("thorns"); break;
            case "chicken_hotpot_killer": enemy.setSpecialAbility("strength_on_non_attack"); break;
            case "lich": enemy.setBossAbility("lich_drain"); break;
            default: break;
        }
    }

    public static Map<String, Integer> getEnemyData(String id) {
        Map<String, Object> data = ENEMY_DATA.get(id);
        if (data == null) return null;
        Map<String, Integer> result = new LinkedHashMap<>();
        result.put("hp", ((Number) data.get("hp")).intValue());
        result.put("attack", ((Number) data.get("attack")).intValue());
        result.put("gold", ((Number) data.get("gold")).intValue());
        return result;
    }

    public static Set<String> getAllEnemyKeys() {
        return ENEMY_DATA.keySet();
    }
}