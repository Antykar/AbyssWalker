package com.abyss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Enemy {
    private final int id;
    private final String nameKey;
    private int hp;
    private final int maxHp;
    private int attack;
    private int baseAttack;
    private int block;
    private EnemyIntent intent;
    private int intentValue;
    private List<Map<String, Object>> statusEffects;
    private boolean isBoss;
    private boolean isElite;
    private String bossAbility;
    private int goldDrop;
    private int strength;
    private int guard;
    private int dexterity;
    private int increaseDamage;
    private String specialAbility;
    private transient Object playerRef;
    private boolean frostbiteImmune;
    private Integer maxLifetime;
    private int currentLifetime;
    private int currentTurn;
    private transient List<Enemy> enemiesRef;
    private int jiangwangSummonCount;
    private boolean persistentBlock;

    // Internal counters for special abilities
    private int shamanTurnCounter;
    private int curseTurnCounter;
    private int lichTurnCounter;

    private static final Random RANDOM = new Random();

    private static final java.util.Set<String> LAYER_STATUS = java.util.Set.of(
            "burn", "vulnerable", "weak", "slow", "poison", "frostbite", "paralysis", "bleed", "stun"
    );

    @JsonCreator
    public Enemy(
            @JsonProperty("id") int id,
            @JsonProperty("name_key") String nameKey,
            @JsonProperty("hp") int hp,
            @JsonProperty("max_hp") int maxHp,
            @JsonProperty("attack") int attack) {
        this.id = id;
        this.nameKey = nameKey;
        this.hp = hp;
        this.maxHp = maxHp;
        this.attack = attack;
        this.baseAttack = attack;
        this.block = 0;
        this.intent = EnemyIntent.ATTACK;
        this.intentValue = 0;
        this.statusEffects = new ArrayList<>();
        this.isBoss = false;
        this.isElite = false;
        this.bossAbility = null;
        this.goldDrop = 0;
        this.strength = 0;
        this.guard = 0;
        this.dexterity = 0;
        this.increaseDamage = 0;
        this.specialAbility = null;
        this.playerRef = null;
        this.frostbiteImmune = false;
        this.maxLifetime = null;
        this.currentLifetime = 0;
        this.currentTurn = 1;
        this.enemiesRef = null;
        this.jiangwangSummonCount = 0;
        this.persistentBlock = false;
        this.shamanTurnCounter = 0;
        this.curseTurnCounter = 0;
        this.lichTurnCounter = 0;
    }

    // ============== Status Methods ==============

    public StatusEffect getStatus(String stype) {
        for (Map<String, Object> s : statusEffects) {
            if (stype.equals(s.get("type"))) {
                int val = ((Number) s.get("value")).intValue();
                return new StatusEffect(stype, val);
            }
        }
        return null;
    }

    public boolean hasStatus(String stype) {
        for (Map<String, Object> s : statusEffects) {
            if (stype.equals(s.get("type")) && ((Number) s.get("value")).intValue() > 0) {
                return true;
            }
        }
        return false;
    }

    public void removeStatus(String stype) {
        statusEffects.removeIf(s -> stype.equals(s.get("type")));
    }

    public void addStatus(String stype, int value) {
        // 僵王小老弟：站立状态不新增负面效果
        if ("jiangwang_standing".equals(bossAbility) && currentTurn % 2 == 1
                && (stype.equals("weak") || stype.equals("vulnerable") || stype.equals("poison")
                || stype.equals("burn") || stype.equals("frostbite") || stype.equals("paralysis")
                || stype.equals("fragile"))) {
            return;
        }
        if (stype.equals("frostbite") && frostbiteImmune) {
            return;
        }
        for (Map<String, Object> s : statusEffects) {
            if (stype.equals(s.get("type"))) {
                int oldVal = ((Number) s.get("value")).intValue();
                s.put("value", oldVal + value);
                if (stype.equals("frostbite")) {
                    checkFrostbiteDeath();
                }
                return;
            }
        }
        Map<String, Object> newStatus = new HashMap<>();
        newStatus.put("type", stype);
        newStatus.put("value", value);
        newStatus.put("duration", 2);
        statusEffects.add(newStatus);
        if (stype.equals("frostbite")) {
            checkFrostbiteDeath();
        }
    }

    private boolean checkFrostbiteDeath() {
        for (Map<String, Object> s : statusEffects) {
            if ("frostbite".equals(s.get("type")) && ((Number) s.get("value")).intValue() > 0
                    && ((Number) s.get("value")).intValue() >= hp && hp > 0) {
                hp = 0;
                return true;
            }
        }
        return false;
    }

    // ============== Damage Methods ==============

    public int takeDamage(int damage) {
        // 僵王小老弟：站立状态免疫伤害
        if ("jiangwang_standing".equals(bossAbility) && currentTurn % 2 == 1) {
            return 0;
        }
        if (hasStatus("phasing")) {
            removeStatus("phasing");
            return 0;
        }
        // 麻痹：每有一层，受到的伤害+1
        int paralysisLayers = 0;
        for (Map<String, Object> s : statusEffects) {
            if ("paralysis".equals(s.get("type"))) {
                paralysisLayers += ((Number) s.get("value")).intValue();
                break;
            }
        }
        damage += paralysisLayers;
        // 易伤：受到伤害 +50%
        int vuln = 0;
        for (Map<String, Object> s : statusEffects) {
            if ("vulnerable".equals(s.get("type"))) {
                vuln = ((Number) s.get("value")).intValue();
                break;
            }
        }
        if (vuln > 0) {
            damage = (int) (damage * 1.5);
        }
        // 脆弱：格挡量减半
        int blockVal = block;
        if (hasStatus("fragile")) {
            blockVal = blockVal / 2;
        }
        int actualDamage = Math.max(0, damage - blockVal);
        if (guard > 0 && actualDamage > 0) {
            actualDamage = Math.max(0, actualDamage - guard);
        }
        this.block -= Math.min(this.block, damage);
        hp -= actualDamage;
        // 荆棘鬼特性
        if ("thorns".equals(specialAbility) && actualDamage > 0 && playerRef != null) {
            // playerRef.takeDamage(2) - would need player reference
        }
        checkFrostbiteDeath();
        return actualDamage;
    }

    public int takePenetratingDamage(int damage) {
        if (hasStatus("phasing")) {
            removeStatus("phasing");
            return 0;
        }
        // 麻痹：每有一层，受到的伤害+1
        int paralysisLayers = 0;
        for (Map<String, Object> s : statusEffects) {
            if ("paralysis".equals(s.get("type"))) {
                paralysisLayers += ((Number) s.get("value")).intValue();
                break;
            }
        }
        damage += paralysisLayers;
        // 易伤：受到伤害 +50%
        int vuln = 0;
        for (Map<String, Object> s : statusEffects) {
            if ("vulnerable".equals(s.get("type"))) {
                vuln = ((Number) s.get("value")).intValue();
                break;
            }
        }
        if (vuln > 0) {
            damage = (int) (damage * 1.5);
        }
        hp -= damage;
        if ("thorns".equals(specialAbility) && damage > 0 && playerRef != null) {
            // playerRef.takeDamage(2)
        }
        checkFrostbiteDeath();
        return damage;
    }

    // ============== Tick Methods ==============

    public void tickStatus(Object player) {
        List<Map<String, Object>> expired = new ArrayList<>();
        for (Map<String, Object> s : statusEffects) {
            String stype = (String) s.get("type");
            int value = ((Number) s.get("value")).intValue();
            if ("burn".equals(stype) && value > 0) {
                hp -= value;
                s.put("value", Math.max(0, value - 1));
            } else if (java.util.Set.of("vulnerable", "weak", "slow", "paralysis", "stun").contains(stype)) {
                if ("paralysis".equals(stype) && player != null) {
                    // Check paralysis_no_decay - would need player access
                }
                s.put("value", Math.max(0, value - 1));
            }
            // 层数类状态：duration 不递减，仅靠 value 归零移除
            if (!LAYER_STATUS.contains(stype)) {
                int dur = ((Number) s.get("duration")).intValue();
                s.put("duration", dur - 1);
            }
            int currentVal = ((Number) s.get("value")).intValue();
            int currentDur = ((Number) s.get("duration")).intValue();
            if (currentVal <= 0 || (!LAYER_STATUS.contains(stype) && currentDur <= 0)) {
                expired.add(s);
            }
        }
        statusEffects.removeAll(expired);
        checkFrostbiteDeath();
    }

    public void tickPoison() {
        List<Map<String, Object>> expired = new ArrayList<>();
        for (Map<String, Object> s : statusEffects) {
            if ("poison".equals(s.get("type")) && ((Number) s.get("value")).intValue() > 0) {
                int val = ((Number) s.get("value")).intValue();
                hp -= val;
                s.put("value", Math.max(0, val - 1));
                if (((Number) s.get("value")).intValue() <= 0) {
                    expired.add(s);
                }
            }
        }
        statusEffects.removeAll(expired);
        checkFrostbiteDeath();
    }

    public void tickBleed() {
        List<Map<String, Object>> expired = new ArrayList<>();
        for (Map<String, Object> s : statusEffects) {
            if ("bleed".equals(s.get("type")) && ((Number) s.get("value")).intValue() > 0) {
                int val = ((Number) s.get("value")).intValue();
                s.put("value", val / 2);
                if (((Number) s.get("value")).intValue() <= 0) {
                    expired.add(s);
                }
            }
        }
        statusEffects.removeAll(expired);
    }

    // ============== Intent & Action ==============

    public int getDisplayIntentValue(Object player) {
        int value = intentValue;
        if (intent != EnemyIntent.ATTACK) {
            return value;
        }
        if (hasStatus("weak")) {
            value = value / 2;
        }
        if (player != null) {
            // player.hasStatus("vulnerable") - would need player reference
        }
        return value;
    }

    public void setIntent() {
        // 僵王小老弟
        if ("jiangwang_standing".equals(bossAbility)) {
            intent = EnemyIntent.ATTACK;
            int base = attack * 2 + strength;
            intentValue = (int) (base * (0.8 + RANDOM.nextDouble() * 0.4));
            return;
        }
        // 召唤僵尸
        if (nameKey.equals("normal_zombie") || nameKey.equals("bucket_zombie")
                || nameKey.equals("raging_newspaper") || nameKey.equals("iron_door_zombie")) {
            intent = EnemyIntent.ATTACK;
            int base = attack + strength;
            intentValue = (int) (base * (0.8 + RANDOM.nextDouble() * 0.4));
            return;
        }
        double rand = RANDOM.nextDouble();
        if (isBoss) {
            if (rand < 0.6) {
                intent = EnemyIntent.ATTACK;
                int base = attack * 2 + strength;
                intentValue = (int) (base * (0.8 + RANDOM.nextDouble() * 0.4));
            } else if (rand < 0.8) {
                intent = EnemyIntent.DEFEND;
                intentValue = 10;
            } else {
                intent = EnemyIntent.BUFF;
                intentValue = 3;
            }
        } else if (isElite) {
            if (rand < 0.6) {
                intent = EnemyIntent.ATTACK;
                int base = attack + 2 + strength;
                intentValue = (int) (base * (0.8 + RANDOM.nextDouble() * 0.4));
            } else if (rand < 0.8) {
                intent = EnemyIntent.DEFEND;
                intentValue = 8;
            } else {
                intent = EnemyIntent.BUFF;
                intentValue = 2;
            }
        } else {
            if (rand < 0.7) {
                intent = EnemyIntent.ATTACK;
                int base = attack + strength;
                intentValue = (int) (base * (0.8 + RANDOM.nextDouble() * 0.4));
            } else if (rand < 0.9) {
                intent = EnemyIntent.DEFEND;
                intentValue = 5;
            } else {
                intent = EnemyIntent.BUFF;
                intentValue = 2;
            }
        }
    }

    public void act(Object target) {
        // 减速：50% 概率跳过
        if (hasStatus("slow") && RANDOM.nextDouble() < 0.5) {
            setIntent();
            return;
        }
        // 眩晕
        if (hasStatus("stun")) {
            setIntent();
            return;
        }
        // 僵王小老弟：站立状态
        if ("jiangwang_standing".equals(bossAbility) && currentTurn % 2 == 1) {
            block = 0;
            spawnZombies();
            tickStatus(target);
            setIntent();
            return;
        }
        // 清零格挡
        if (!persistentBlock) {
            block = 0;
        }
        if (intent == EnemyIntent.ATTACK) {
            int dmg = intentValue;
            if (hasStatus("weak")) {
                dmg = dmg / 2;
            }
            dmg = Math.max(0, dmg);
            // The actual damage application would be handled by the game engine
            // This mirrors the Python logic structure
        } else if (intent == EnemyIntent.DEFEND) {
            block += intentValue;
        } else if (intent == EnemyIntent.BUFF) {
            strength += intentValue;
        }
        // 僵王小老弟：双数回合
        if ("jiangwang_standing".equals(bossAbility) && currentTurn % 2 == 0) {
            // target.draw_count = Math.max(0, target.draw_count - 1)
        }
        tickStatus(target);
        // 蘑菇精
        if ("regen_block".equals(specialAbility)) {
            block += 3;
        }
        // 石人
        if ("stone_armor".equals(specialAbility)) {
            block += 4;
        }
        // 哥布林萨满
        if ("periodic_buff".equals(specialAbility)) {
            shamanTurnCounter++;
            strength += 1;
        }
        // 暗影幽灵
        if ("shadow_phase_common".equals(specialAbility)) {
            addStatus("phasing", 1);
        }
        // 治愈精灵
        if ("heal_ally".equals(specialAbility)) {
            // Would need enemiesRef
        }
        // 暗影刺客
        if ("shadow_phase".equals(specialAbility)) {
            addStatus("phasing", 1);
        }
        // 熔岩巨兽
        if ("lava_armor".equals(specialAbility)) {
            block += 5;
        }
        // 冰霜法师
        if ("frost_curse".equals(specialAbility)) {
            // target.addStatus("frostbite", 2)
        }
        // 诅咒祭司
        if ("curse_weak".equals(specialAbility)) {
            curseTurnCounter++;
            if (curseTurnCounter % 2 == 1) {
                // target.addStatus("weak", 1)
            } else {
                // target.addStatus("vulnerable", 1)
            }
        }
        // Boss 特殊能力：巫妖
        if ("lich_drain".equals(bossAbility)) {
            hp = Math.min(maxHp, hp + 5);
            lichTurnCounter++;
            if (lichTurnCounter % 2 == 0) {
                strength += 1;
            }
        }
        setIntent();
    }

    private void spawnMinion(String nameKey, int hp, int att, Integer maxLife, int blockVal) {
        if (enemiesRef == null) return;
        int maxId = 0;
        for (Enemy e : enemiesRef) {
            if (e.id > maxId) maxId = e.id;
        }
        int newId = maxId + 1;
        Enemy minion = new Enemy(newId, nameKey, hp, hp, att);
        minion.playerRef = this.playerRef;
        minion.maxLifetime = maxLife;
        minion.currentLifetime = 0;
        if (blockVal > 0) {
            minion.block = blockVal;
            minion.persistentBlock = true;
        }
        enemiesRef.add(minion);
    }

    private void spawnZombies() {
        jiangwangSummonCount++;
        if (jiangwangSummonCount <= 2) {
            spawnMinion("normal_zombie", 25, 10, 3, 0);
            return;
        }
        String[] zombiePool = {"normal_zombie", "bucket_zombie", "raging_newspaper", "iron_door_zombie"};
        int count = RANDOM.nextDouble() < 0.3 ? 2 : 1;
        for (int i = 0; i < count; i++) {
            String name = zombiePool[RANDOM.nextInt(zombiePool.length)];
            int hp, att, maxLife;
            int blockVal = 0;
            switch (name) {
                case "normal_zombie" -> { hp = 25; att = 10; maxLife = 3; }
                case "bucket_zombie" -> { hp = 50; att = 10; maxLife = 4; }
                case "raging_newspaper" -> { hp = 25; att = 20; maxLife = 4; }
                case "iron_door_zombie" -> { hp = 25; att = 10; maxLife = 4; blockVal = 50; }
                default -> { hp = 25; att = 10; maxLife = 3; }
            }
            spawnMinion(name, hp, att, maxLife, blockVal);
        }
    }

    // ============== Serialization ==============

    @JsonProperty("id")
    public int getId() { return id; }

    @JsonProperty("name_key")
    public String getNameKey() { return nameKey; }

    @JsonProperty("hp")
    public int getHp() { return hp; }

    @JsonProperty("max_hp")
    public int getMaxHp() { return maxHp; }

    @JsonProperty("attack")
    public int getAttack() { return attack; }

    @JsonProperty("base_attack")
    public int getBaseAttack() { return baseAttack; }

    @JsonProperty("block")
    public int getBlock() { return block; }

    @JsonProperty("intent")
    public EnemyIntent getIntent() { return intent; }

    @JsonProperty("intent_value")
    public int getIntentValue() { return intentValue; }

    @JsonProperty("status_effects")
    public List<Map<String, Object>> getStatusEffects() { return statusEffects; }

    @JsonProperty("is_boss")
    public boolean isBoss() { return isBoss; }

    @JsonProperty("is_elite")
    public boolean isElite() { return isElite; }

    @JsonProperty("gold_drop")
    public int getGoldDrop() { return goldDrop; }

    @JsonProperty("special_ability")
    public String getSpecialAbility() { return specialAbility; }

    public String getBossAbility() { return bossAbility; }
    public int getStrength() { return strength; }
    public int getGuard() { return guard; }
    public int getDexterity() { return dexterity; }
    public int getIncreaseDamage() { return increaseDamage; }
    public boolean isFrostbiteImmune() { return frostbiteImmune; }
    public Integer getMaxLifetime() { return maxLifetime; }
    public int getCurrentLifetime() { return currentLifetime; }
    public int getCurrentTurn() { return currentTurn; }
    public boolean isPersistentBlock() { return persistentBlock; }
    public Object getPlayerRef() { return playerRef; }

    public void setHp(int hp) { this.hp = hp; }
    public void setBlock(int block) { this.block = block; }
    public void setIntent(EnemyIntent intent) { this.intent = intent; }
    public void setIntentValue(int intentValue) { this.intentValue = intentValue; }
    public void setBoss(boolean boss) { isBoss = boss; }
    public void setElite(boolean elite) { isElite = elite; }
    public void setBossAbility(String bossAbility) { this.bossAbility = bossAbility; }
    public void setGoldDrop(int goldDrop) { this.goldDrop = goldDrop; }
    public void setStrength(int strength) { this.strength = strength; }
    public void setGuard(int guard) { this.guard = guard; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }
    public void setIncreaseDamage(int increaseDamage) { this.increaseDamage = increaseDamage; }
    public void setSpecialAbility(String specialAbility) { this.specialAbility = specialAbility; }
    public void setPlayerRef(Object playerRef) { this.playerRef = playerRef; }
    public void setFrostbiteImmune(boolean frostbiteImmune) { this.frostbiteImmune = frostbiteImmune; }
    public void setMaxLifetime(Integer maxLifetime) { this.maxLifetime = maxLifetime; }
    public void setCurrentLifetime(int currentLifetime) { this.currentLifetime = currentLifetime; }
    public void setCurrentTurn(int currentTurn) { this.currentTurn = currentTurn; }
    public void setEnemiesRef(List<Enemy> enemiesRef) { this.enemiesRef = enemiesRef; }
    public void setPersistentBlock(boolean persistentBlock) { this.persistentBlock = persistentBlock; }
    public void setBaseAttack(int baseAttack) { this.baseAttack = baseAttack; }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name_key", nameKey);
        result.put("hp", hp);
        result.put("max_hp", maxHp);
        result.put("attack", attack);
        result.put("base_attack", baseAttack);
        result.put("block", block);
        result.put("intent", intent.getValue());
        result.put("intent_value", intentValue);
        result.put("status_effects", statusEffects);
        result.put("is_boss", isBoss);
        result.put("is_elite", isElite);
        result.put("gold_drop", goldDrop);
        result.put("special_ability", specialAbility);
        return result;
    }

    public static Enemy fromMap(Map<String, Object> data) {
        int id = ((Number) data.get("id")).intValue();
        String nameKey = (String) data.get("name_key");
        int hp = ((Number) data.get("hp")).intValue();
        int maxHp = ((Number) data.get("max_hp")).intValue();
        int attack = data.containsKey("attack") ? ((Number) data.get("attack")).intValue() : 5;
        Enemy enemy = new Enemy(id, nameKey, hp, maxHp, attack);
        if (data.containsKey("block")) enemy.block = ((Number) data.get("block")).intValue();
        if (data.containsKey("intent")) {
            enemy.intent = EnemyIntent.valueOf(((String) data.get("intent")).toUpperCase());
        }
        if (data.containsKey("intent_value")) enemy.intentValue = ((Number) data.get("intent_value")).intValue();
        if (data.containsKey("status_effects")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> effects = (List<Map<String, Object>>) data.get("status_effects");
            enemy.statusEffects = new ArrayList<>(effects);
        }
        if (data.containsKey("is_boss")) enemy.isBoss = (Boolean) data.get("is_boss");
        if (data.containsKey("is_elite")) enemy.isElite = (Boolean) data.get("is_elite");
        if (data.containsKey("gold_drop")) enemy.goldDrop = ((Number) data.get("gold_drop")).intValue();
        if (data.containsKey("special_ability")) enemy.specialAbility = (String) data.get("special_ability");
        if (data.containsKey("base_attack")) enemy.baseAttack = ((Number) data.get("base_attack")).intValue();
        return enemy;
    }
}