package com.abyss.state;

import com.abyss.model.*;
import com.abyss.system.CardFactory;
import com.abyss.system.EnemyData;
import com.abyss.system.ItemFactory;
import com.abyss.system.RelicPool;
import java.util.*;

/* ================================================================
 * GameState — 游戏全局状态容器
 * ================================================================ */
public class GameState {
    // ── 基础状态 ──
    public GamePhase phase = GamePhase.TITLE;
    public int turn = 1;
    public int currentFloor = 1;
    public List<String> elitesUsed = new ArrayList<>();
    public int maxFloors = 15;
    public String gameMode = "normal";

    // ── BOSS连战模式 ──
    public boolean bossRushSetupDone = false;
    public List<Object> bossRushCardPool = new ArrayList<>();
    public List<Object> bossRushRelicPool = new ArrayList<>();
    public List<Object> bossRushSelectedCards = new ArrayList<>();
    public Object bossRushSelectedRelic = null;
    public List<String> bossRushBossesUsed = new ArrayList<>();
    public List<String> bossRushBossOrder = new ArrayList<>();

    // ── 自由模式 ──
    public List<Object> freeCardPool = new ArrayList<>();
    public List<Object> freeRelicPool = new ArrayList<>();
    public Set<String> freeSelectedCards = new HashSet<>();
    public Set<String> freeSelectedRelics = new HashSet<>();
    public int freeSetupPage = 0, freeSetupRelicPage = 0;
    public String freeSelectedBoss = null;
    public boolean freeBossSelectOpen = false;

    // ── 设置 ──
    public boolean settingsOpen = false;
    public boolean isFullscreen = false;
    public boolean fullscreenRequested = false;

    // ── 玩家与战斗对象 ──
    public Player player = null;
    public List<Object> enemies = new ArrayList<>();
    public List<MapNode> mapNodes = new ArrayList<>();
    public MapNode selectedNode = null;
    public Object selectedEnemy = null, selectedCard = null;
    public Object selectedExhaustCard = null, selectedShopDeleteCard = null;
    public boolean selectingDiscard = false, selectingExhaust = false;
    public int discardCount = 0;

    // ── 战斗日志 ──
    public List<String> combatLog = new ArrayList<>();

    // ── 奖励 / 商店 ──
    public List<Object> rewardCards = new ArrayList<>();
    public int rewardGold = 0;
    public Object rewardRelic = null;
    public int rewardSelectedCard = -1;
    public boolean rewardRelicTaken = false;
    public List<Map<String, Object>> shopItems = new ArrayList<>();

    // ── 角色加点 ──
    public int buildPoints = 2;
    public Map<String, Integer> buildAlloc = new LinkedHashMap<>();

    // ── 角色选择 / 图鉴 ──
    public String selectedCharacter = null;
    public String encyclopediaModule = "cards";
    public int encyclopediaPage = 0, deckViewPage = 0, relicViewPage = 0;
    public String encyclopediaFilter = null, encyclopediaClassFilter = null;

    // ── 地图滚动 / 商店删牌 ──
    public int mapScrollOffset = 0;
    public Object mapDragStart = null;
    public boolean shopDeleteMode = false;
    public int cardDeleteCount = 0;

    // ── 新手指引 / 杂项 ──
    public boolean showTutorial = true, tutorialActive = false;
    public int tutorialStep = 0;
    public String result = null;
    public boolean expAwarded = false, combatMenuOpen = false, powersMenuOpen = false, combatEnding = false;
    public GamePhase prevPhase = null;

    // ── 道具 ──
    public boolean itemSelectMode = false, opportunityRewardActive = false;
    public Object selectedItem = null;
    public String itemTargetMode = null;

    // ── 机遇房事件 ──
    public boolean opportunityEventActive = false;
    public String opportunityEventType = "", opportunityEventStage = "";
    public Map<String, Object> opportunityEventData = new HashMap<>();
    public String opportunityPopupMessage = "";

    // ── 战斗动画 ──
    public Object animCardPlaying = null;
    public int animCardTimer = 0, animCardSx = 0, animCardSy = 0, animCardEx = 0, animCardEy = 0;
    public int animDamageTimer = 0, animAttackTimer = 0, animAttackEnemyIdx = -1;
    public int animCardLastCx = 0, animCardLastCy = 0, animEnemyDamageTimer = 0, animEnemyDamageIdx = -1;

    // ── 敌人行动队列 ──
    public List<Map<String, Object>> enemyActionQueue = new ArrayList<>();
    public String enemyActionState = "idle";
    public int enemyActionDelay = 0, animEnemyActionIdx = -1, animEnemyActionTimer = 0;
    public String animEnemyActionType = null;

    // ── 手牌悬浮 ──
    public Object hoveredCard = null;
    public int hoveredCardX = 0, hoveredCardY = 0;

    // ── 浮动提示 ──
    public String floatingMessage = "";
    public int floatingMessageTimer = 0;

    // ── 双生BOSS ──
    public boolean twinBossActive = false;
    public int twinBossRevivalTurns = 0;
    public String twinBossDefeatedKey = null;

    // ================================================================
    //  静态常量（卡牌池、遗物池、价格）
    // ================================================================
    public static final Map<String, Set<String>> CLASS_EXCLUSIVE_POOL = new LinkedHashMap<>();
    public static final Map<CardRarity, List<String>> FULL_CARD_POOL = new LinkedHashMap<>();
    public static final List<String> CURSE_CARD_POOL = new ArrayList<>();
    public static final Set<String> NO_DROP_RELICS = new HashSet<>();
    public static final Map<String, Integer> CARD_PRICE = new LinkedHashMap<>();

    static {
        CLASS_EXCLUSIVE_POOL.put("warrior", Set.of(
            "attack_and_defend","guard_to_attack","pumped_up","bloodletting","offering","bloodbath",
            "gradual_madness","endless_power","high_kill_intent","uppercut","bash","blood_drink",
            "unyielding","blood_burn","blood_blade","become_king","blood_calamity","shield_dance",
            "absolute_defense","shield_hero","death_breath","blood_demon","hell_chaos","blood_cloak","body_armor"));
        CLASS_EXCLUSIVE_POOL.put("mage", Set.of(
            "soul_flame","chaos_strike","fireball","shadow_strike","elemental_rush","hot_cold",
            "stinky_tofu","yuanhua","poison_quench","ice_spike","thunder_call","lightning_bolt",
            "lightning_shield","heaven_thunder","snake_notbite","concentrated_toxin","high_heat_boil",
            "ice_sorrow","freeze_ray","element_recycle","palm_thunder","poison_fire_heart",
            "elemental_mastery","echo_burst","deep_hell","thundercloud_roll","increase_power",
            "urgent_frost","poison_spread","fire_connect","frost_shatter","freeze_rigid","fuel_booster",
            "violent_combustion","extreme_cold","alternating_voltage","heaven_earth_law","cold_fever",
            "frost_crack","flame_burst","conduct_circuit","electromagnetic_field","power_surge",
            "current_symbiosis","thunder_god_descend","overload","elemental_coat","certification_master",
            "primordial_energy","return_to_chaos","lightning_flash","element_reuse","pure_white_state",
            "invade_body","grand_fireworks","armored_ice","rotten_miasma","poison_material","sizzling",
            "accumulated_electric_charge","snow_on_frost"));
        CLASS_EXCLUSIVE_POOL.put("rogue", Set.of(
            "brilliant_strike","gold_strike","coin_throw","oppression","get_rich_dumbbell",
            "bankruptcy_palm","buy","conceal","sneak_attack","retire","advancing_to_retreat",
            "impregnable","pay_the_bill","economic_law","sell_soul","steal","pickpocket","finisher",
            "sudden_ambush","nightfall","instant_kill","break_stealth","constant_harass","battle_master",
            "night_like_day","come_and_go","kill","perfect_skill","future_strike","recycle",
            "not_willing_to_lose","after_a_hundred_years"));
        CLASS_EXCLUSIVE_POOL.put("priest", Set.of(
            "focused_ray","judgment","bath","holy_shield","eternal_chant","zealot","weathering",
            "faith_accumulation","divine_judgment","all_living_wish","wish_power","chant","gold_curse",
            "prayer","light_energy_wave","promised_thing","gift_of_blessing","bodyguard","demonize",
            "dark_gem","faith_annihilation","energy_backlash","nourish_soul","i_have_ascended",
            "light_burst","sacrifice_everything"));

        FULL_CARD_POOL.put(CardRarity.COMMON, List.of(
            "heavy_strike","knife_attack","twin_strike","pommel_strike","shrug_it_off","ice_spike",
            "lightning_bolt","quick_slash","dagger_throw","dynasty_horse","guard_to_attack",
            "attack_and_defend","soul_flame","pumped_up","little_being","disarm","chaos_strike","mock",
            "stinky_tofu","bash","brilliant_strike","get_rich_dumbbell","thunder_call","lightning_shield",
            "weathering","divine_judgment","venom_strike","snake_notbite","conceal","sneak_attack","retire",
            "palm_thunder","steal","blood_blade","sudden_ambush","chant","advancing_to_retreat","finisher",
            "prayer","light_energy_wave","echo_burst",
            "extreme_cold","alternating_voltage","flame_burst","lightning_flash",
            "emergency_evasion","night_like_day","come_and_go","kill","not_willing_to_lose",
            "instant_kill","break_stealth","bodyguard","dark_gem","frost_crack"));
        FULL_CARD_POOL.put(CardRarity.UNCOMMON, List.of(
            "bloodletting","bath","true_grit","uppercut","fury","sword_boomerang","fireball","arcane_blast",
            "shadow_strike","backstab","bankruptcy_palm","iron_strike","gradual_madness","elemental_rush",
            "heavy_armor","eternal_storm","focused_ray","fengshui_lunliu","gold_strike","oppression","buy",
            "holy_shield","yuanhua","hot_cold","high_kill_intent","coin_throw","unyielding","eternal_chant",
            "zealot","faith_accumulation","carnage","battle_trance","judgment","gather_energy",
            "meteor_strike","concentrated_toxin","high_heat_boil","ice_sorrow","freeze_ray",
            "impregnable","pay_the_bill","sell_soul","blood_burn","gold_curse","become_king","blood_calamity",
            "wish_power","nightfall","pickpocket","shield_dance","absolute_defense","promised_thing",
            "elemental_mastery","deep_hell","thundercloud_roll","increase_power","poison_spread","fire_connect",
            "frost_shatter","freeze_rigid","fuel_booster","cold_fever","conduct_circuit","power_surge","overload",
            "elemental_coat","certification_master","return_to_chaos","element_reuse","shield_hero",
            "constant_harass","invade_body","faith_annihilation","armored_ice","rotten_miasma","poison_material",
            "accumulated_electric_charge","snow_on_frost","blood_demon","hell_chaos","blood_cloak","body_armor",
            "energy_backlash","nourish_soul","light_burst","battle_master","future_strike","recycle",
            "after_a_hundred_years","endless_power","liqun"));
        FULL_CARD_POOL.put(CardRarity.RARE, List.of(
            "offering","assassinate","adrenaline","kidney_pill","divine_power","evasion","fate_manipulate",
            "bloodbath","blood_drink","heaven_thunder","poison_quench","element_recycle","economic_law",
            "all_living_wish","unmatched","poison_fire_heart","gift_of_blessing","demonize","urgent_frost",
            "violent_combustion","heaven_earth_law","electromagnetic_field","current_symbiosis",
            "thunder_god_descend","primordial_energy","grand_fireworks","sizzling","death_breath",
            "i_have_ascended","sacrifice_everything","perfect_skill"));
        FULL_CARD_POOL.put(CardRarity.LEGENDARY, List.of("xianye_fury","xianye_blessing","xianye_guard"));

        CURSE_CARD_POOL.addAll(List.of("dog_skin_plaster","bad_illness","hopeless","persistent_curse"));
        NO_DROP_RELICS.add("crown_of_overlord");

        // 商店价格
        String[] priceKeys = {"heavy_strike","bash","knife_attack","twin_strike","pommel_strike","shrug_it_off",
            "ice_spike","lightning_bolt","quick_slash","dagger_throw","dynasty_horse","guard_to_attack",
            "attack_and_defend","soul_flame","pumped_up","little_being","disarm","chaos_strike","mock",
            "stinky_tofu","brilliant_strike","get_rich_dumbbell","thunder_call","lightning_shield","weathering",
            "divine_judgment","venom_strike","snake_notbite","conceal","sneak_attack","retire","bloodletting",
            "bath","true_grit","uppercut","fury","sword_boomerang","fireball","arcane_blast","shadow_strike",
            "backstab","bankruptcy_palm","iron_strike","gradual_madness","elemental_rush","heavy_armor",
            "eternal_storm","focused_ray","fengshui_lunliu","oppression","buy","holy_shield","yuanhua","hot_cold",
            "gold_strike","high_kill_intent","coin_throw","unyielding","eternal_chant","zealot",
            "faith_accumulation","carnage","battle_trance","judgment","endless_power","gather_energy",
            "meteor_strike","liqun","palm_thunder","offering","assassinate","adrenaline","kidney_pill",
            "divine_power","evasion","fate_manipulate","bloodbath","blood_drink","heaven_thunder","poison_quench",
            "gold_curse","concentrated_toxin","high_heat_boil","ice_sorrow","freeze_ray","element_recycle",
            "advancing_to_retreat","steal","impregnable","pay_the_bill","sell_soul","economic_law","pickpocket",
            "finisher","blood_burn","blood_blade","sudden_ambush","become_king","blood_calamity","wish_power",
            "chant","nightfall","elemental_mastery","echo_burst","deep_hell","thundercloud_roll","increase_power",
            "urgent_frost","poison_spread","fire_connect","frost_shatter","freeze_rigid","fuel_booster",
            "violent_combustion","extreme_cold","alternating_voltage","heaven_earth_law","cold_fever","frost_crack",
            "flame_burst","conduct_circuit","all_living_wish","shield_dance","absolute_defense","unmatched",
            "xianye_fury","xianye_blessing","xianye_guard","electromagnetic_field","power_surge",
            "current_symbiosis","thunder_god_descend","overload","elemental_coat","certification_master",
            "primordial_energy","return_to_chaos","lightning_flash","element_reuse","shield_hero","constant_harass",
            "invade_body","faith_annihilation","emergency_evasion","grand_fireworks","armored_ice","rotten_miasma",
            "poison_material","sizzling","accumulated_electric_charge","snow_on_frost","death_breath","blood_demon",
            "hell_chaos","blood_cloak","body_armor"};
        int[] priceVals = {30,25,15,25,25,20,25,25,20,25,20,25,20,30,20,10,25,25,20,20,20,20,20,20,30,25,25,20,20,20,20,
            50,50,50,60,50,50,40,45,40,40,35,55,50,50,50,55,45,45,50,45,55,50,55,45,50,45,55,45,45,150,45,60,110,50,50,100,
            30,100,140,100,120,100,160,120,120,130,150,120,55,50,50,50,55,120,45,20,50,45,50,120,50,15,50,25,25,50,50,50,25,
            50,50,25,25,50,50,50,50,50,50,120,50,50,50,50,120,50,50,50,50,50,50,50,120,50,25,25,50,50,120,55,50,110,350,300,350,
            150,50,120,150,50,75,75,100,75,50,75,75,75,75,30,100,75,75,75,100,75,75,120,75,75,75,75};
        for (int i = 0; i < priceKeys.length && i < priceVals.length; i++) {
            CARD_PRICE.put(priceKeys[i], priceVals[i]);
        }
    }

    // ================================================================
    //  构造方法
    // ================================================================
    public GameState() {
        buildAlloc.put("strength", 0);
        buildAlloc.put("dexterity", 0);
        buildAlloc.put("guard", 0);
        buildAlloc.put("draw", 0);
    }

    /** 重置游戏状态到初始状态。 */
    public void reset() {
        phase = GamePhase.TITLE;
        turn = 1;
        currentFloor = 1;
        maxFloors = 15;
        gameMode = "normal";
        player = null;
        enemies.clear();
        mapNodes.clear();
        selectedNode = null;
        selectedEnemy = null;
        selectedCard = null;
        selectedExhaustCard = null;
        selectedShopDeleteCard = null;
        selectingDiscard = false;
        selectingExhaust = false;
        discardCount = 0;
        combatLog.clear();
        rewardCards.clear();
        rewardGold = 0;
        rewardRelic = null;
        rewardSelectedCard = -1;
        rewardRelicTaken = false;
        shopItems.clear();
        selectedCharacter = null;
        mapScrollOffset = 0;
        shopDeleteMode = false;
        cardDeleteCount = 0;
        showTutorial = true;
        tutorialActive = false;
        tutorialStep = 0;
        result = null;
        expAwarded = false;
        combatMenuOpen = false;
        powersMenuOpen = false;
        combatEnding = false;
        prevPhase = null;
        itemSelectMode = false;
        opportunityRewardActive = false;
        selectedItem = null;
        itemTargetMode = null;
        opportunityEventActive = false;
        opportunityEventType = "";
        opportunityEventStage = "";
        opportunityEventData.clear();
        opportunityPopupMessage = "";
        elitesUsed.clear();
        animCardPlaying = null;
        animCardTimer = 0;
        floatingMessage = "";
        floatingMessageTimer = 0;
        hoveredCard = null;
        enemyActionQueue.clear();
        enemyActionState = "idle";
        buildAlloc.put("strength", 0);
        buildAlloc.put("dexterity", 0);
        buildAlloc.put("guard", 0);
        buildAlloc.put("draw", 0);
        buildPoints = 2;
        twinBossActive = false;
        twinBossRevivalTurns = 0;
        twinBossDefeatedKey = null;
    }

    // ================================================================
    //  generate_map — 生成地图节点
    // ================================================================
    public void generateMap() {
        mapNodes.clear();
        int nodeId = 0;

        if ("boss_rush".equals(gameMode)) {
            int numBosses = bossRushBossOrder.size();
            maxFloors = 3 * numBosses - 2;
            List<MapNodeType> seq = new ArrayList<>();
            for (int i = 0; i < numBosses; i++) {
                seq.add(MapNodeType.BOSS);
                if (i < numBosses - 1) { seq.add(MapNodeType.SHOP); seq.add(MapNodeType.REST); }
            }
            int floor = 1;
            for (MapNodeType nt : seq) {
                int y = 100 + floor * 120;
                int x = 500; // BASE_WIDTH / 2 近似
                mapNodes.add(new MapNode(nodeId++, nt, x, y, floor++));
            }
            return;
        }

        // 普通模式
        for (int floor = 1; floor <= maxFloors; floor++) {
            int y = 100 + floor * 120;
            if (floor == maxFloors) {
                mapNodes.add(new MapNode(nodeId++, MapNodeType.BOSS, 500, y, floor));
            } else {
                for (int i = 0; i < 3; i++) {
                    int x = 200 + i * 300;
                    MapNodeType nt;
                    if (floor == 1) {
                        nt = MapNodeType.COMBAT;
                    } else if (floor == maxFloors - 1) {
                        nt = MapNodeType.REST;
                    } else {
                        double r = Math.random();
                        if (r < 0.55) nt = MapNodeType.COMBAT;
                        else if (r < 0.65) nt = MapNodeType.ELITE;
                        else if (r < 0.75) nt = MapNodeType.OPPORTUNITY;
                        else if (r < 0.88) nt = MapNodeType.SHOP;
                        else nt = MapNodeType.REST;
                    }
                    mapNodes.add(new MapNode(nodeId++, nt, x, y, floor));
                }
            }
        }
    }

    // ================================================================
    //  generate_enemies — 生成敌人
    // ================================================================
    public void generateEnemies(MapNodeType nodeType) {
        enemies.clear();
        Random rnd = new Random();
        int floor = currentFloor;

        double hpMult = 1.0;
        double atkMult = 1.0;
        if (player != null) {
            hpMult = 1 + (player.getLevel() - 1) * 0.02;
            atkMult = 1 + (player.getLevel() - 1) * 0.01;
        }

        if (nodeType == MapNodeType.BOSS) {
            String[] bossKeys = {"killer_machine", "dragon", "lich", "dark_lord"};
            if (currentFloor == maxFloors && "boss_rush".equals(gameMode)) {
                // BOSS rush order
            }
            String bossKey = bossKeys[rnd.nextInt(bossKeys.length)];
            Enemy boss = EnemyData.createEnemy(bossKey, 0);
            if (boss != null) {
                boss.setPlayerRef(player);
                boss.setHp((int)(boss.getHp() * hpMult));
                enemies.add(boss);
            }
        } else if (nodeType == MapNodeType.ELITE) {
            String[] eliteKeys = {"goblin_captain", "skeleton_knight", "orc_warrior",
                "slime_ang", "terror_eye", "blood_monster", "shadow_assassin",
                "lava_beast", "frost_mage", "curse_priest", "poison_witch",
                "snow_fairy", "thorn_ghost", "chicken_hotpot_killer"};
            String eliteKey = eliteKeys[rnd.nextInt(eliteKeys.length)];
            Enemy elite = EnemyData.createEnemy(eliteKey, 0);
            if (elite != null) {
                elite.setPlayerRef(player);
                elite.setHp((int)(elite.getHp() * hpMult));
                enemies.add(elite);
            }
        } else {
            String[][] pools = {
                {"slime", "goblin", "bat", "mushroom", "imp"},
                {"skeleton", "orc", "vampire", "viper", "goblin_shaman",
                 "werewolf", "elf_archer", "bandit", "fire_imp", "thunder_bird"},
                {"demon", "gargoyle", "wraith", "goblin_bomber", "stone_golem",
                 "frost_spider", "goblin_warlock", "cursed_statue", "healing_nymph"}
            };
            int poolIdx = Math.min(floor / 5, pools.length - 1);
            int count = 1 + rnd.nextInt(Math.min(2, floor));
            for (int i = 0; i < count; i++) {
                String enemyKey = pools[poolIdx][rnd.nextInt(pools[poolIdx].length)];
                Enemy enemy = EnemyData.createEnemy(enemyKey, i);
                if (enemy != null) {
                    enemy.setPlayerRef(player);
                    enemy.setHp((int)(enemy.getHp() * hpMult));
                    enemies.add(enemy);
                }
            }
        }
        for (Object e : enemies) {
            if (e instanceof Enemy) {
                ((Enemy) e).setEnemiesRef((List<Enemy>)(List<?>) enemies);
            }
        }
        combatLog.add("生成敌人（节点类型: " + nodeType.getValue() + " 楼层: " + currentFloor + "）");
    }

    // ================================================================
    //  generate_reward — 生成奖励
    // ================================================================
    public void generateReward() {
        rewardCards.clear();
        rewardGold = 15 + currentFloor * 4;
        rewardSelectedCard = -1;
        rewardRelicTaken = false;
        rewardRelic = null;

        // 判断是否精英/Boss战
        boolean isBoss = false;
        boolean isElite = false;
        for (Object e : enemies) {
            if (e instanceof Enemy) {
                Enemy en = (Enemy) e;
                if (en.isBoss() && en.getHp() <= 0) isBoss = true;
                if (en.isElite() && en.getHp() <= 0) isElite = true;
            }
        }

        // 生成3张奖励卡牌
        List<String> allCardKeys = new ArrayList<>();
        for (List<String> pool : FULL_CARD_POOL.values()) {
            allCardKeys.addAll(pool);
        }
        // 根据玩家职业过滤专属卡
        if (player != null) {
            String charClass = player.getCharClass().getValue();
            Set<String> exclusive = CLASS_EXCLUSIVE_POOL.getOrDefault(charClass, Set.of());
            allCardKeys.addAll(exclusive);
        }
        Collections.shuffle(allCardKeys);
        for (int i = 0; i < 3 && i < allCardKeys.size(); i++) {
            rewardCards.add(CardFactory.createCard(allCardKeys.get(i)));
        }

        // 精英/Boss额外掉落遗物
        if (isBoss || isElite) {
            String relicKey;
            int attempts = 0;
            do {
                relicKey = RelicPool.getAllRelicKeys().get(new Random().nextInt(RelicPool.getAllRelicKeys().size()));
                attempts++;
            } while (NO_DROP_RELICS.contains(relicKey) && attempts < 20);
            if (!NO_DROP_RELICS.contains(relicKey)) {
                rewardRelic = RelicPool.getRelic(relicKey);
            }
        }

        // 道具掉落：BOSS 100%、精英50%、普通小怪10%
        generateItemDrops(isBoss, isElite);

        combatLog.add("奖励已生成，金币: " + rewardGold);
    }

    private void generateItemDrops(boolean isBoss, boolean isElite) {
        if (player == null) return;
        // BOSS战：100%掉落1个道具
        if (isBoss) {
            Item itemDrop = ItemFactory.getRandomItem();
            player.getItems().add(itemDrop);
            combatLog.add("掉落道具：" + ItemFactory.getChineseName(itemDrop.getNameKey()));
            return;
        }
        // 精英战：每个精英50%概率掉落
        if (isElite) {
            for (Object e : enemies) {
                if (e instanceof Enemy) {
                    Enemy en = (Enemy) e;
                    if (en.isElite() && en.getHp() <= 0) {
                        if (new Random().nextDouble() < 0.5) {
                            Item itemDrop = ItemFactory.getRandomItem();
                            player.getItems().add(itemDrop);
                            combatLog.add("「" + en.getNameKey() + "」掉落道具：" + ItemFactory.getChineseName(itemDrop.getNameKey()));
                        }
                    }
                }
            }
            return;
        }
        // 普通战斗：每个小怪10%概率掉落
        for (Object e : enemies) {
            if (e instanceof Enemy) {
                Enemy en = (Enemy) e;
                if (en.getHp() <= 0) {
                    if (new Random().nextDouble() < 0.1) {
                        Item itemDrop = ItemFactory.getRandomItem();
                        player.getItems().add(itemDrop);
                        combatLog.add("「" + en.getNameKey() + "」掉落道具：" + ItemFactory.getChineseName(itemDrop.getNameKey()));
                    }
                }
            }
        }
    }

    // ================================================================
    //  generate_shop — 生成商店
    // ================================================================
    public void generateShop() {
        shopItems.clear();
        Random rnd = new Random();

        // 生成5张卡牌商品
        List<String> allCardKeys = new ArrayList<>();
        for (List<String> pool : FULL_CARD_POOL.values()) {
            allCardKeys.addAll(pool);
        }
        Collections.shuffle(allCardKeys);
        for (int i = 0; i < 5 && i < allCardKeys.size(); i++) {
            String cardKey = allCardKeys.get(i);
            Card card = CardFactory.createCard(cardKey);
            int price = CARD_PRICE.getOrDefault(cardKey, 50);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("type", "card");
            item.put("card", card);
            item.put("price", price);
            item.put("sold", false);
            shopItems.add(item);
        }

        // 生成2件遗物商品（排除NO_DROP_RELICS和玩家已拥有的）
        List<String> relicKeys = RelicPool.getAllRelicKeys().stream()
            .filter(k -> !NO_DROP_RELICS.contains(k))
            .filter(k -> player == null || player.getRelics().stream().noneMatch(r -> r.getNameKey().equals(k)))
            .collect(java.util.stream.Collectors.toList());
        Collections.shuffle(relicKeys);
        for (int i = 0; i < 2 && i < relicKeys.size(); i++) {
            Relic relic = RelicPool.getRelic(relicKeys.get(i));
            if (relic != null) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("type", "relic");
                item.put("relic", relic);
                item.put("price", 150 + rnd.nextInt(100));
                item.put("sold", false);
                shopItems.add(item);
            }
        }

        // 生成3件道具商品
        List<String> itemKeys = ItemFactory.getAllItemKeys();
        Collections.shuffle(itemKeys);
        for (int i = 0; i < 3 && i < itemKeys.size(); i++) {
            Item itemObj = ItemFactory.createItem(itemKeys.get(i));
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("type", "item");
            item.put("item", itemObj);
            item.put("price", 50 + rnd.nextInt(50));
            item.put("sold", false);
            shopItems.add(item);
        }

        combatLog.add("商店已生成");
    }

    // ================================================================
    //  start_combat — 进入战斗初始化
    // ================================================================
    public void startCombat() {
        combatLog.clear();
        turn = 1;
        if (player != null) {
            // 重置玩家战斗状态
            player.setBlock(0);
            player.setEnergy(player.getMaxEnergy() + player.getBaseMaxEnergy());
            player.setStrength(player.getBaseStrength());
            player.setTempStrength(0);
            player.setGuard(player.getBaseGuard());
            player.setDexterity(player.getBaseDexterity());
            player.setFirstCardPlayed(false);
            player.setNextAttackMultiplier(1);
            player.setAllDamageMultiplierThisTurn(1);
            player.setNextTurnBlock(0);
            player.setNextTurnEnergy(0);
            player.setPendingDiscard(0);
            player.setPendingExhaust(0);
            player.setStatusEffects(new ArrayList<>());

            // 重置牌堆
            player.getHand().clear();
            player.getDiscardPile().clear();
            player.getExhaustPile().clear();

            // 将抽牌堆与弃牌堆合并后洗牌
            player.getDrawPile().addAll(player.getDiscardPile());
            player.getDiscardPile().clear();
            player.shuffleDrawPile();

            // 设置敌人引用
            for (Object e : enemies) {
                if (e instanceof Enemy) {
                    Enemy en = (Enemy) e;
                    en.setPlayerRef(player);
                    en.setCurrentTurn(1);
                    en.setIntent();
                }
            }

            // 处理遗物效果
            for (Relic relic : player.getRelics()) {
                player.applyRelicInstantEffects(relic);
            }

            // 抽初始手牌
            int drawCount = player.getDrawCount() + player.getBaseDrawCount();
            player.drawCards(drawCount);
        }
        // 首次进入战斗时激活新手指引
        if (showTutorial && !tutorialActive) {
            tutorialActive = true;
            tutorialStep = 0;
        }
        combatLog.add("战斗开始！");
    }

    // ================================================================
    //  process_enemy_deaths — 处理敌人死亡效果
    // ================================================================
    public void processEnemyDeaths() {
        List<Enemy> deadEnemies = new ArrayList<>();
        for (Object e : enemies) {
            if (e instanceof Enemy) {
                Enemy enemy = (Enemy) e;
                if (enemy.getHp() <= 0) {
                    // 史莱姆分裂
                    if ("slime_ang".equals(enemy.getNameKey())) {
                        int maxId = 0;
                        for (Object e2 : enemies) {
                            if (e2 instanceof Enemy) maxId = Math.max(maxId, ((Enemy) e2).getId());
                        }
                        for (int i = 0; i < 2; i++) {
                            Enemy slime = EnemyData.createEnemy("slime", maxId + 1 + i);
                            if (slime != null) {
                                slime.setPlayerRef(player);
                                slime.setHp(Math.max(1, enemy.getMaxHp() / 2));
                                enemies.add(slime);
                            }
                        }
                    }

                    // 幽灵死亡伤害
                    if ("wraith".equals(enemy.getNameKey()) && player != null) {
                        player.takeDamage(5);
                        combatLog.add("幽灵死亡对玩家造成5点伤害");
                    }

                    // 哥布林炸弹兵爆炸
                    if ("goblin_bomber".equals(enemy.getNameKey()) && player != null) {
                        player.takeDamage(6);
                        combatLog.add("哥布林炸弹兵爆炸对玩家造成6点伤害");
                    }

                    // 毒素蔓延/火烧连云
                    if (player != null) {
                        if (player.isPoisonTransferOnDeath()) {
                            for (Object e2 : enemies) {
                                if (e2 instanceof Enemy && ((Enemy) e2).getHp() > 0) {
                                    ((Enemy) e2).addStatus("poison", 3);
                                }
                            }
                        }
                        if (player.isBurnTransferOnDeath()) {
                            for (Object e2 : enemies) {
                                if (e2 instanceof Enemy && ((Enemy) e2).getHp() > 0) {
                                    ((Enemy) e2).addStatus("burn", 3);
                                }
                            }
                        }
                    }

                    // 双生BOSS复活
                    if (twinBossActive && enemy.isBoss()) {
                        twinBossDefeatedKey = enemy.getNameKey();
                        twinBossRevivalTurns = 3;
                    }

                    deadEnemies.add(enemy);
                }
            }
        }
        enemies.removeAll(deadEnemies);

        // 处理双生BOSS复活
        if (twinBossActive && twinBossRevivalTurns > 0) {
            twinBossRevivalTurns--;
            if (twinBossRevivalTurns <= 0 && twinBossDefeatedKey != null) {
                Enemy revived = EnemyData.createEnemy(twinBossDefeatedKey, 100);
                if (revived != null) {
                    revived.setBoss(true);
                    revived.setPlayerRef(player);
                    revived.setHp(revived.getMaxHp() / 2);
                    enemies.add(revived);
                    combatLog.add("双生BOSS复活！");
                }
                twinBossDefeatedKey = null;
            }
        }
    }

    // ================================================================
    //  generate_opportunity_reward_cards — 机遇房选卡
    // ================================================================
    public void generateOpportunityRewardCards(int count) {
        rewardCards.clear();
        // 从可用卡牌池中随机抽取 count 张
        opportunityRewardActive = true;
    }

    // ================================================================
    //  generate_boss_rush_setup — BOSS连战模式准备
    // ================================================================
    public void generateBossRushSetup() {
        bossRushCardPool.clear();
        bossRushRelicPool.clear();
        // 构建候选卡牌池（18张）
        // 构建候选遗物池（5个）
        // 随机BOSS出场顺序
        List<String> allBossKeys = new ArrayList<>(List.of(
            "killer_machine","dragon","lich","dark_lord","disaster_left_hand","jiangwang_xiao_laodi"));
        Collections.shuffle(allBossKeys);
        bossRushBossOrder = allBossKeys;
    }

    // ================================================================
    //  generate_free_setup — 自由模式准备
    // ================================================================
    public void generateFreeSetup() {
        freeCardPool.clear();
        freeRelicPool.clear();
        // 收集所有可用卡牌（按稀有度排序）
        // 收集所有可用遗物
    }

    // ================================================================
    //  toMap / fromMap — 序列化/反序列化
    // ================================================================
    public Map<String, Object> toMap() {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("phase", phase.getValue());
        d.put("turn", turn);
        d.put("currentFloor", currentFloor);
        d.put("maxFloors", maxFloors);
        d.put("gameMode", gameMode);
        d.put("combatLog", combatLog);
        d.put("mapNodes", mapNodes.stream().map(MapNode::toMap).toList());
        d.put("rewardGold", rewardGold);
        d.put("rewardSelectedCard", rewardSelectedCard);
        d.put("rewardRelicTaken", rewardRelicTaken);
        d.put("shopDeleteMode", shopDeleteMode);
        d.put("cardDeleteCount", cardDeleteCount);
        d.put("player", player != null ? player.toMap() : null);
        d.put("elitesUsed", elitesUsed);
        return d;
    }

    public static GameState fromMap(Map<String, Object> data) {
        GameState gs = new GameState();
        if (data.containsKey("phase")) gs.phase = GamePhase.fromValue((String) data.get("phase"));
        if (data.containsKey("turn")) gs.turn = ((Number) data.get("turn")).intValue();
        if (data.containsKey("currentFloor")) gs.currentFloor = ((Number) data.get("currentFloor")).intValue();
        if (data.containsKey("maxFloors")) gs.maxFloors = ((Number) data.get("maxFloors")).intValue();
        if (data.containsKey("gameMode")) gs.gameMode = (String) data.get("gameMode");
        if (data.containsKey("combatLog")) gs.combatLog = new ArrayList<>((List<String>) data.get("combatLog"));
        if (data.containsKey("mapNodes")) {
            List<Map<String, Object>> nodeList = (List<Map<String, Object>>) data.get("mapNodes");
            gs.mapNodes = nodeList.stream().map(MapNode::fromMap).toList();
        }
        if (data.containsKey("rewardGold")) gs.rewardGold = ((Number) data.get("rewardGold")).intValue();
        if (data.containsKey("rewardSelectedCard")) gs.rewardSelectedCard = ((Number) data.get("rewardSelectedCard")).intValue();
        if (data.containsKey("rewardRelicTaken")) gs.rewardRelicTaken = (Boolean) data.get("rewardRelicTaken");
        if (data.containsKey("shopDeleteMode")) gs.shopDeleteMode = (Boolean) data.get("shopDeleteMode");
        if (data.containsKey("cardDeleteCount")) gs.cardDeleteCount = ((Number) data.get("cardDeleteCount")).intValue();
        if (data.containsKey("elitesUsed")) gs.elitesUsed = new ArrayList<>((List<String>) data.get("elitesUsed"));
        return gs;
    }
}