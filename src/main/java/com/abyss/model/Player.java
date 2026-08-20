package com.abyss.model;

import com.abyss.system.LangManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private static final Set<String> LAYER_STATUS = new HashSet<>(Arrays.asList(
        "burn", "vulnerable", "weak", "slow", "poison", "paralysis", "stun"
    ));
    private static final Set<String> PERSISTENT_STATUS = new HashSet<>(Arrays.asList(
        "weak_persistent", "lurk", "assassinate", "holy_energy"
    ));
    private static final Set<String> NEGATIVE_STATUS = new HashSet<>(Arrays.asList(
        "burn", "vulnerable", "weak", "fragile", "slow", "poison",
        "paralysis", "frostbite", "skip_turn", "stun"
    ));

    // === 基础属性 ===
    private CharacterClass charClass;
    private int hp;
    private int maxHp;
    private int gold;
    private int level;

    // === 战斗属性 ===
    private int block;
    private int strength;
    private int tempStrength;
    private int guard;
    private int dexterity;

    // === 基础属性（加点永久） ===
    private int baseStrength;
    private int baseDexterity;
    private int baseGuard;
    private int baseMaxEnergy;
    private int baseDrawCount;

    // === 牌堆 ===
    private List<Card> hand;
    private List<Card> drawPile;
    private List<Card> discardPile;
    private List<Card> exhaustPile;

    // === 进度 ===
    private List<Relic> relics;
    private List<Item> items;
    private int maxHandSize;
    private int drawCount;

    // === 能量 ===
    private int energy;
    private int maxEnergy;

    // === 状态与触发器 ===
    private List<Map<String, Object>> statusEffects;
    private boolean startDebuff;
    private int strengthPerTurn;
    private int frostbitePerTurn;
    private boolean firstCardPlayed;
    private int nextTurnBlock;
    private int pendingDiscard;
    private int pendingExhaust;

    // === 能力牌效果 ===
    private int strengthOnHpLoss;
    private int poisonOnUnblockedDamage;
    private int hpChangePierce;
    private int blockPerTurn;
    private int bloodBurnRound;
    private List<Enemy> enemies;
    private int nextAttackMultiplier;
    private boolean poisonBurnSync;
    private int holyEnergyPerTurn;
    private int allDamageMultiplierThisTurn;
    private boolean holyEnergyBlocked;
    private boolean dieIfEnemiesAlive;
    private boolean elementalMastery;
    private boolean elementalMasteryUsed;
    private int burnPerTurn;
    private boolean attackParalysisActive;
    private int frostbiteSelfPerTurn;
    private boolean poisonTransferOnDeath;
    private boolean burnTransferOnDeath;
    private boolean paralysisNoDecay;
    private int paralysisPerTurn;
    private boolean drawOnParalysis;
    private boolean blockEnergyGain;
    private boolean elementalCoat;
    private int skillBlockThisTurn;
    private int nextTurnEnergy;
    private boolean elementalAppliedThisTurn;
    private int nextTurnBlockElemental;
    private List<Card> playedPowerCards;
    private int frostbiteAppliedThisTurn;
    private boolean rottenMiasma;
    private boolean sizzling;
    private boolean shieldHero;
    private boolean constantHarass;
    private boolean invadeBody;
    private boolean faithAnnihilation;
    private boolean snowOnFrost;
    private boolean bloodDemon;
    private boolean bloodCloak;
    private boolean shieldNoDecay;
    private boolean stealthTriggerShieldDamage;
    private int lurkShieldOnEnter;
    private int assassinateDamageOnEnter;
    private boolean stealthNegatePenalty;
    private int permanentDamageBonus;
    private int recycleGoldPerExhaust;
    private boolean drawOnDiscardThisTurn;
    private int ahbcGoldDecreaseCount;

    // === 内部计数器 ===
    private int oxTalismanTurn;

    public Player() {
        this(CharacterClass.WARRIOR, 80, 80, 99, 1);
    }

    public Player(CharacterClass charClass, int hp, int maxHp, int gold, int level) {
        this.charClass = charClass;
        this.hp = hp;
        this.maxHp = maxHp;
        this.gold = gold;
        this.level = level;
        this.block = 0;
        this.strength = 0;
        this.tempStrength = 0;
        this.guard = 0;
        this.dexterity = 0;
        this.baseStrength = 0;
        this.baseDexterity = 0;
        this.baseGuard = 0;
        this.baseMaxEnergy = 0;
        this.baseDrawCount = 0;
        this.hand = new ArrayList<>();
        this.drawPile = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.exhaustPile = new ArrayList<>();
        this.relics = new ArrayList<>();
        this.items = new ArrayList<>();
        this.maxHandSize = 10;
        this.drawCount = 5;
        this.energy = 3;
        this.maxEnergy = 3;
        this.statusEffects = new ArrayList<>();
        this.startDebuff = false;
        this.strengthPerTurn = 0;
        this.frostbitePerTurn = 0;
        this.firstCardPlayed = false;
        this.nextTurnBlock = 0;
        this.pendingDiscard = 0;
        this.pendingExhaust = 0;
        this.strengthOnHpLoss = 0;
        this.poisonOnUnblockedDamage = 0;
        this.hpChangePierce = 0;
        this.blockPerTurn = 0;
        this.bloodBurnRound = 0;
        this.enemies = new ArrayList<>();
        this.nextAttackMultiplier = 1;
        this.poisonBurnSync = false;
        this.holyEnergyPerTurn = 0;
        this.allDamageMultiplierThisTurn = 1;
        this.holyEnergyBlocked = false;
        this.dieIfEnemiesAlive = false;
        this.elementalMastery = false;
        this.elementalMasteryUsed = false;
        this.burnPerTurn = 0;
        this.attackParalysisActive = false;
        this.frostbiteSelfPerTurn = 0;
        this.poisonTransferOnDeath = false;
        this.burnTransferOnDeath = false;
        this.paralysisNoDecay = false;
        this.paralysisPerTurn = 0;
        this.drawOnParalysis = false;
        this.blockEnergyGain = false;
        this.elementalCoat = false;
        this.skillBlockThisTurn = 0;
        this.nextTurnEnergy = 0;
        this.elementalAppliedThisTurn = false;
        this.nextTurnBlockElemental = 0;
        this.playedPowerCards = new ArrayList<>();
        this.frostbiteAppliedThisTurn = 0;
        this.rottenMiasma = false;
        this.sizzling = false;
        this.shieldHero = false;
        this.constantHarass = false;
        this.invadeBody = false;
        this.faithAnnihilation = false;
        this.snowOnFrost = false;
        this.bloodDemon = false;
        this.bloodCloak = false;
        this.shieldNoDecay = false;
        this.stealthTriggerShieldDamage = false;
        this.lurkShieldOnEnter = 0;
        this.assassinateDamageOnEnter = 0;
        this.stealthNegatePenalty = false;
        this.permanentDamageBonus = 0;
        this.recycleGoldPerExhaust = 0;
        this.drawOnDiscardThisTurn = false;
        this.ahbcGoldDecreaseCount = 0;
        this.oxTalismanTurn = 0;
    }

    // ============== Getters & Setters ==============

    public CharacterClass getCharClass() { return charClass; }
    public void setCharClass(CharacterClass charClass) { this.charClass = charClass; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getMaxHp() { return maxHp; }
    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getBlock() { return block; }
    public void setBlock(int block) { this.block = block; }
    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }
    public int getTempStrength() { return tempStrength; }
    public void setTempStrength(int tempStrength) { this.tempStrength = tempStrength; }
    public int getGuard() { return guard; }
    public void setGuard(int guard) { this.guard = guard; }
    public int getDexterity() { return dexterity; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }
    public int getBaseStrength() { return baseStrength; }
    public void setBaseStrength(int baseStrength) { this.baseStrength = baseStrength; }
    public int getBaseDexterity() { return baseDexterity; }
    public void setBaseDexterity(int baseDexterity) { this.baseDexterity = baseDexterity; }
    public int getBaseGuard() { return baseGuard; }
    public void setBaseGuard(int baseGuard) { this.baseGuard = baseGuard; }
    public int getBaseMaxEnergy() { return baseMaxEnergy; }
    public void setBaseMaxEnergy(int baseMaxEnergy) { this.baseMaxEnergy = baseMaxEnergy; }
    public int getBaseDrawCount() { return baseDrawCount; }
    public void setBaseDrawCount(int baseDrawCount) { this.baseDrawCount = baseDrawCount; }
    public List<Card> getHand() { return hand; }
    public void setHand(List<Card> hand) { this.hand = hand; }
    public List<Card> getDrawPile() { return drawPile; }
    public void setDrawPile(List<Card> drawPile) { this.drawPile = drawPile; }
    public List<Card> getDiscardPile() { return discardPile; }
    public void setDiscardPile(List<Card> discardPile) { this.discardPile = discardPile; }
    public List<Card> getExhaustPile() { return exhaustPile; }
    public void setExhaustPile(List<Card> exhaustPile) { this.exhaustPile = exhaustPile; }
    public List<Relic> getRelics() { return relics; }
    public void setRelics(List<Relic> relics) { this.relics = relics; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    public int getMaxHandSize() { return maxHandSize; }
    public void setMaxHandSize(int maxHandSize) { this.maxHandSize = maxHandSize; }
    public int getDrawCount() { return drawCount; }
    public void setDrawCount(int drawCount) { this.drawCount = drawCount; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }
    public int getMaxEnergy() { return maxEnergy; }
    public void setMaxEnergy(int maxEnergy) { this.maxEnergy = maxEnergy; }
    public List<Map<String, Object>> getStatusEffects() { return statusEffects; }
    public void setStatusEffects(List<Map<String, Object>> statusEffects) { this.statusEffects = statusEffects; }
    public boolean isStartDebuff() { return startDebuff; }
    public void setStartDebuff(boolean startDebuff) { this.startDebuff = startDebuff; }
    public int getStrengthPerTurn() { return strengthPerTurn; }
    public void setStrengthPerTurn(int strengthPerTurn) { this.strengthPerTurn = strengthPerTurn; }
    public int getFrostbitePerTurn() { return frostbitePerTurn; }
    public void setFrostbitePerTurn(int frostbitePerTurn) { this.frostbitePerTurn = frostbitePerTurn; }
    public boolean isFirstCardPlayed() { return firstCardPlayed; }
    public void setFirstCardPlayed(boolean firstCardPlayed) { this.firstCardPlayed = firstCardPlayed; }
    public int getNextTurnBlock() { return nextTurnBlock; }
    public void setNextTurnBlock(int nextTurnBlock) { this.nextTurnBlock = nextTurnBlock; }
    public int getPendingDiscard() { return pendingDiscard; }
    public void setPendingDiscard(int pendingDiscard) { this.pendingDiscard = pendingDiscard; }
    public int getPendingExhaust() { return pendingExhaust; }
    public void setPendingExhaust(int pendingExhaust) { this.pendingExhaust = pendingExhaust; }
    public int getStrengthOnHpLoss() { return strengthOnHpLoss; }
    public void setStrengthOnHpLoss(int strengthOnHpLoss) { this.strengthOnHpLoss = strengthOnHpLoss; }
    public int getPoisonOnUnblockedDamage() { return poisonOnUnblockedDamage; }
    public void setPoisonOnUnblockedDamage(int poisonOnUnblockedDamage) { this.poisonOnUnblockedDamage = poisonOnUnblockedDamage; }
    public int getHpChangePierce() { return hpChangePierce; }
    public void setHpChangePierce(int hpChangePierce) { this.hpChangePierce = hpChangePierce; }
    public int getBlockPerTurn() { return blockPerTurn; }
    public void setBlockPerTurn(int blockPerTurn) { this.blockPerTurn = blockPerTurn; }
    public int getBloodBurnRound() { return bloodBurnRound; }
    public void setBloodBurnRound(int bloodBurnRound) { this.bloodBurnRound = bloodBurnRound; }
    public List<Enemy> getEnemies() { return enemies; }
    public void setEnemies(List<Enemy> enemies) { this.enemies = enemies; }
    public int getNextAttackMultiplier() { return nextAttackMultiplier; }
    public void setNextAttackMultiplier(int nextAttackMultiplier) { this.nextAttackMultiplier = nextAttackMultiplier; }
    public boolean isPoisonBurnSync() { return poisonBurnSync; }
    public void setPoisonBurnSync(boolean poisonBurnSync) { this.poisonBurnSync = poisonBurnSync; }
    public int getHolyEnergyPerTurn() { return holyEnergyPerTurn; }
    public void setHolyEnergyPerTurn(int holyEnergyPerTurn) { this.holyEnergyPerTurn = holyEnergyPerTurn; }
    public int getAllDamageMultiplierThisTurn() { return allDamageMultiplierThisTurn; }
    public void setAllDamageMultiplierThisTurn(int allDamageMultiplierThisTurn) { this.allDamageMultiplierThisTurn = allDamageMultiplierThisTurn; }
    public boolean isHolyEnergyBlocked() { return holyEnergyBlocked; }
    public void setHolyEnergyBlocked(boolean holyEnergyBlocked) { this.holyEnergyBlocked = holyEnergyBlocked; }
    public boolean isDieIfEnemiesAlive() { return dieIfEnemiesAlive; }
    public void setDieIfEnemiesAlive(boolean dieIfEnemiesAlive) { this.dieIfEnemiesAlive = dieIfEnemiesAlive; }
    public boolean isElementalMastery() { return elementalMastery; }
    public void setElementalMastery(boolean elementalMastery) { this.elementalMastery = elementalMastery; }
    public boolean isElementalMasteryUsed() { return elementalMasteryUsed; }
    public void setElementalMasteryUsed(boolean elementalMasteryUsed) { this.elementalMasteryUsed = elementalMasteryUsed; }
    public int getBurnPerTurn() { return burnPerTurn; }
    public void setBurnPerTurn(int burnPerTurn) { this.burnPerTurn = burnPerTurn; }
    public boolean isAttackParalysisActive() { return attackParalysisActive; }
    public void setAttackParalysisActive(boolean attackParalysisActive) { this.attackParalysisActive = attackParalysisActive; }
    public int getFrostbiteSelfPerTurn() { return frostbiteSelfPerTurn; }
    public void setFrostbiteSelfPerTurn(int frostbiteSelfPerTurn) { this.frostbiteSelfPerTurn = frostbiteSelfPerTurn; }
    public boolean isPoisonTransferOnDeath() { return poisonTransferOnDeath; }
    public void setPoisonTransferOnDeath(boolean poisonTransferOnDeath) { this.poisonTransferOnDeath = poisonTransferOnDeath; }
    public boolean isBurnTransferOnDeath() { return burnTransferOnDeath; }
    public void setBurnTransferOnDeath(boolean burnTransferOnDeath) { this.burnTransferOnDeath = burnTransferOnDeath; }
    public boolean isParalysisNoDecay() { return paralysisNoDecay; }
    public void setParalysisNoDecay(boolean paralysisNoDecay) { this.paralysisNoDecay = paralysisNoDecay; }
    public int getParalysisPerTurn() { return paralysisPerTurn; }
    public void setParalysisPerTurn(int paralysisPerTurn) { this.paralysisPerTurn = paralysisPerTurn; }
    public boolean isDrawOnParalysis() { return drawOnParalysis; }
    public void setDrawOnParalysis(boolean drawOnParalysis) { this.drawOnParalysis = drawOnParalysis; }
    public boolean isBlockEnergyGain() { return blockEnergyGain; }
    public void setBlockEnergyGain(boolean blockEnergyGain) { this.blockEnergyGain = blockEnergyGain; }
    public boolean isElementalCoat() { return elementalCoat; }
    public void setElementalCoat(boolean elementalCoat) { this.elementalCoat = elementalCoat; }
    public int getSkillBlockThisTurn() { return skillBlockThisTurn; }
    public void setSkillBlockThisTurn(int skillBlockThisTurn) { this.skillBlockThisTurn = skillBlockThisTurn; }
    public int getNextTurnEnergy() { return nextTurnEnergy; }
    public void setNextTurnEnergy(int nextTurnEnergy) { this.nextTurnEnergy = nextTurnEnergy; }
    public boolean isElementalAppliedThisTurn() { return elementalAppliedThisTurn; }
    public void setElementalAppliedThisTurn(boolean elementalAppliedThisTurn) { this.elementalAppliedThisTurn = elementalAppliedThisTurn; }
    public int getNextTurnBlockElemental() { return nextTurnBlockElemental; }
    public void setNextTurnBlockElemental(int nextTurnBlockElemental) { this.nextTurnBlockElemental = nextTurnBlockElemental; }
    public List<Card> getPlayedPowerCards() { return playedPowerCards; }
    public void setPlayedPowerCards(List<Card> playedPowerCards) { this.playedPowerCards = playedPowerCards; }
    public int getFrostbiteAppliedThisTurn() { return frostbiteAppliedThisTurn; }
    public void setFrostbiteAppliedThisTurn(int frostbiteAppliedThisTurn) { this.frostbiteAppliedThisTurn = frostbiteAppliedThisTurn; }
    public boolean isRottenMiasma() { return rottenMiasma; }
    public void setRottenMiasma(boolean rottenMiasma) { this.rottenMiasma = rottenMiasma; }
    public boolean isSizzling() { return sizzling; }
    public void setSizzling(boolean sizzling) { this.sizzling = sizzling; }
    public boolean isShieldHero() { return shieldHero; }
    public void setShieldHero(boolean shieldHero) { this.shieldHero = shieldHero; }
    public boolean isConstantHarass() { return constantHarass; }
    public void setConstantHarass(boolean constantHarass) { this.constantHarass = constantHarass; }
    public boolean isInvadeBody() { return invadeBody; }
    public void setInvadeBody(boolean invadeBody) { this.invadeBody = invadeBody; }
    public boolean isFaithAnnihilation() { return faithAnnihilation; }
    public void setFaithAnnihilation(boolean faithAnnihilation) { this.faithAnnihilation = faithAnnihilation; }
    public boolean isSnowOnFrost() { return snowOnFrost; }
    public void setSnowOnFrost(boolean snowOnFrost) { this.snowOnFrost = snowOnFrost; }
    public boolean isBloodDemon() { return bloodDemon; }
    public void setBloodDemon(boolean bloodDemon) { this.bloodDemon = bloodDemon; }
    public boolean isBloodCloak() { return bloodCloak; }
    public void setBloodCloak(boolean bloodCloak) { this.bloodCloak = bloodCloak; }
    public boolean isShieldNoDecay() { return shieldNoDecay; }
    public void setShieldNoDecay(boolean shieldNoDecay) { this.shieldNoDecay = shieldNoDecay; }
    public boolean isStealthTriggerShieldDamage() { return stealthTriggerShieldDamage; }
    public void setStealthTriggerShieldDamage(boolean stealthTriggerShieldDamage) { this.stealthTriggerShieldDamage = stealthTriggerShieldDamage; }
    public int getLurkShieldOnEnter() { return lurkShieldOnEnter; }
    public void setLurkShieldOnEnter(int lurkShieldOnEnter) { this.lurkShieldOnEnter = lurkShieldOnEnter; }
    public int getAssassinateDamageOnEnter() { return assassinateDamageOnEnter; }
    public void setAssassinateDamageOnEnter(int assassinateDamageOnEnter) { this.assassinateDamageOnEnter = assassinateDamageOnEnter; }
    public boolean isStealthNegatePenalty() { return stealthNegatePenalty; }
    public void setStealthNegatePenalty(boolean stealthNegatePenalty) { this.stealthNegatePenalty = stealthNegatePenalty; }
    public int getPermanentDamageBonus() { return permanentDamageBonus; }
    public void setPermanentDamageBonus(int permanentDamageBonus) { this.permanentDamageBonus = permanentDamageBonus; }
    public int getRecycleGoldPerExhaust() { return recycleGoldPerExhaust; }
    public void setRecycleGoldPerExhaust(int recycleGoldPerExhaust) { this.recycleGoldPerExhaust = recycleGoldPerExhaust; }
    public boolean isDrawOnDiscardThisTurn() { return drawOnDiscardThisTurn; }
    public void setDrawOnDiscardThisTurn(boolean drawOnDiscardThisTurn) { this.drawOnDiscardThisTurn = drawOnDiscardThisTurn; }
    public int getAhbcGoldDecreaseCount() { return ahbcGoldDecreaseCount; }
    public void setAhbcGoldDecreaseCount(int ahbcGoldDecreaseCount) { this.ahbcGoldDecreaseCount = ahbcGoldDecreaseCount; }

    // ============== 核心方法 ==============

    public void resetTurn() {
        // 步骤1：清零格挡
        if (!shieldNoDecay) block = 0;
        tempStrength = 0;
        allDamageMultiplierThisTurn = 1;
        holyEnergyBlocked = false;
        dieIfEnemiesAlive = false;

        // 南孚电池
        int carryEnergyBonus = 0;
        if (hasRelic("nanfu_battery") && energy > 0) carryEnergyBonus = 1;
        energy = maxEnergy + carryEnergyBonus;

        // 下回合护盾
        if (nextTurnBlock > 0) {
            block += nextTurnBlock;
            nextTurnBlock = 0;
            triggerShieldHero();
        }
        // 永恒吟唱
        if (blockPerTurn > 0) {
            block += blockPerTurn + dexterity;
            triggerShieldHero();
        }
        // 热血燃烧
        if (bloodBurnRound > 0) {
            hp -= bloodBurnRound;
            for (Enemy enemy : enemies) {
                if (enemy.getHp() > 0) {
                    enemy.takePenetratingDamage(bloodBurnRound * 4);
                }
            }
        }

        // 步骤2：跳过回合
        boolean skip = hasStatus("stun") || hasStatus("skip_turn");

        // 步骤3：手牌入弃牌堆
        int discardedCount = 0;
        List<Card> toDiscard = new ArrayList<>();
        for (Card card : hand) {
            if (!card.isRetain()) {
                toDiscard.add(card);
                discardedCount++;
            }
        }
        hand.removeAll(toDiscard);
        discardPile.addAll(toDiscard);
        if (drawOnDiscardThisTurn && discardedCount > 0) {
            drawCards(discardedCount);
        }

        // 步骤4：结算状态
        tickStatus();

        // 步骤5：能力效果
        if (strengthPerTurn > 0) strength += strengthPerTurn;
        if (holyEnergyPerTurn > 0) {
            addStatus("holy_energy", holyEnergyPerTurn);
            if (faithAnnihilation) dealRandomDamage(5);
        }
        elementalMasteryUsed = false;
        attackParalysisActive = false;
        blockEnergyGain = false;
        skillBlockThisTurn = 0;
        drawOnDiscardThisTurn = false;
        if (nextTurnEnergy > 0) {
            energy += nextTurnEnergy;
            nextTurnEnergy = 0;
        }
        if (nextTurnBlockElemental > 0 && elementalAppliedThisTurn) {
            block += nextTurnBlockElemental;
            triggerShieldHero();
        }
        nextTurnBlockElemental = 0;
        elementalAppliedThisTurn = false;
        frostbiteAppliedThisTurn = 0;
        if (frostbiteSelfPerTurn > 0) addStatus("frostbite", frostbiteSelfPerTurn);

        // 步骤6：牛符咒
        if (hasRelic("ox_talisman")) {
            oxTalismanTurn++;
            if (oxTalismanTurn % 2 == 0) strength += 1;
        }
        // 步骤7：生命枝杈
        if (hasRelic("life_branch")) heal(1);
        // 步骤8：抽牌
        if (!skip) drawHand();
    }

    public void drawHand() {
        drawCards(drawCount);
    }

    public void drawCards(int count) {
        int drawn = 0;
        while (drawn < count) {
            if (drawPile.isEmpty() && !discardPile.isEmpty()) shuffleDiscard();
            if (drawPile.isEmpty()) break;
            Random rnd = new Random();
            Card card = drawPile.remove(rnd.nextInt(drawPile.size()));
            // 狂热信徒
            if ("zealot".equals(card.getNameKey())) {
                Object effect = card.getEffect();
                if (effect instanceof Map) {
                    @SuppressWarnings("unchecked")
                    int val = ((Number) ((Map<String, Object>) effect).getOrDefault("value", 3)).intValue();
                    card.setDamage(card.getDamage() + val);
                }
            }
            // 风化
            if ("weathering".equals(card.getNameKey())) {
                Object effect = card.getEffect();
                if (effect instanceof Map) {
                    @SuppressWarnings("unchecked")
                    int val = ((Number) ((Map<String, Object>) effect).getOrDefault("value", 2)).intValue();
                    card.setDamage(Math.max(0, card.getDamage() - val));
                }
            }
            if (hand.size() < maxHandSize) {
                hand.add(card);
            } else {
                discardPile.add(card);
            }
            drawn++;
        }
    }

    public void shuffleDiscard() {
        drawPile = new ArrayList<>(discardPile);
        Collections.shuffle(drawPile);
        discardPile.clear();
    }

    /**
     * 使用道具。
     * @param item 要使用的道具
     * @param target 目标敌人（对self/all_enemies时为null）
     * @param allEnemies 所有敌人列表（用于AOE道具）
     * @param combatLog 战斗日志（用于记录信息）
     */
    public void useItem(Item item, Enemy target, List<Enemy> allEnemies, List<String> combatLog) {
        String nameKey = item.getNameKey();
        LangManager lang = LangManager.getInstance();

        if ("bomb".equals(nameKey)) {
            // 炸弹：对一名敌人造成20点伤害
            if (target != null) {
                target.setHp(target.getHp() - item.getDamage());
                String name = lang.getText("items.bomb", "炸弹");
                String tName = lang.getText("enemies." + target.getNameKey(), target.getNameKey());
                combatLog.add("使用「" + name + "」对 " + tName + " 造成 " + item.getDamage() + " 点伤害");
            }
        } else if ("signal_arrow".equals(nameKey)) {
            // 穿云箭：对一名敌人造成15点穿透伤害
            if (target != null) {
                int dmg = item.getPenetratingDamage();
                target.setHp(target.getHp() - dmg);
                String name = lang.getText("items.signal_arrow", "穿云箭");
                String tName = lang.getText("enemies." + target.getNameKey(), target.getNameKey());
                combatLog.add("使用「" + name + "」对 " + tName + " 造成 " + dmg + " 点穿透伤害");
            }
        } else if ("grenade".equals(nameKey)) {
            // 高爆手雷：对所有敌人造成10点伤害
            String name = lang.getText("items.grenade", "高爆手雷");
            for (Enemy e : allEnemies) {
                if (e.getHp() > 0) {
                    e.setHp(e.getHp() - item.getDamage());
                    String tName = lang.getText("enemies." + e.getNameKey(), e.getNameKey());
                    combatLog.add("使用「" + name + "」对 " + tName + " 造成 " + item.getDamage() + " 点伤害");
                }
            }
        } else if ("paper_shield".equals(nameKey)) {
            // 纸壳盾：给自己15点格挡
            block += item.getBlock();
            String name = lang.getText("items.paper_shield", "纸壳盾");
            combatLog.add("使用「" + name + "」获得 " + item.getBlock() + " 点格挡");
        } else if ("energy_ball".equals(nameKey)) {
            // 能量球：获得2点能量
            String name = lang.getText("items.energy_ball", "能量球");
            int gain = Math.min(item.getEnergy(), maxEnergy - energy);
            energy += gain;
            combatLog.add("使用「" + name + "」获得 " + gain + " 点能量");
        } else if ("cycle_card".equals(nameKey)) {
            // 轮转卡：抽取3张卡牌
            String name = lang.getText("items.cycle_card", "轮转卡");
            drawCards(item.getDrawCards());
            combatLog.add("使用「" + name + "」抽取了 " + item.getDrawCards() + " 张卡牌");
        } else if ("power_glove".equals(nameKey)) {
            // 力量手套：获得3点力量
            String name = lang.getText("items.power_glove", "力量手套");
            strength += item.getStrength();
            combatLog.add("使用「" + name + "」获得 " + item.getStrength() + " 点力量");
        } else if ("vuln_potion".equals(nameKey) || "weak_potion".equals(nameKey) || "fragile_potion".equals(nameKey)) {
            // 药水类：对目标敌人施加状态
            if (target != null) {
                target.addStatus(item.getStatusType(), item.getStatusValue());
                String name = lang.getText("items." + nameKey, nameKey);
                String tName = lang.getText("enemies." + target.getNameKey(), target.getNameKey());
                combatLog.add("使用「" + name + "」对 " + tName + " 施加 " + item.getStatusValue() + " 层" + item.getStatusType());
            }
        } else if ("treatment_bottle".equals(nameKey)) {
            // 治疗瓶：回复24点生命
            String name = lang.getText("items.treatment_bottle", "治疗瓶");
            int actualHeal = heal(item.getHeal());
            combatLog.add("使用「" + name + "」回复了 " + actualHeal + " 点生命");
        }

        // 从玩家道具列表中移除已使用的道具
        items.remove(item);
    }

    public boolean playCard(Card card, Enemy target, List<Enemy> allEnemies) {
        if (energy < card.getCost() && card.getCost() != 0) return false;

        // 审判
        boolean isJudgment = card.hasEffectType("judgment_damage");
        int judgmentN = 0;
        if (isJudgment) {
            judgmentN = energy;
            energy = 0;
        } else {
            energy -= card.getCost();
        }

        // 双鱼玉佩
        boolean talismanDouble = !firstCardPlayed && hasRelic("twin_fish_pendant");
        firstCardPlayed = true;
        int repeatCount = talismanDouble ? 2 : 1;

        hand.remove(card);
        boolean cardInDiscard = false;
        if (card.getType() != CardType.POWER) {
            discardPile.add(card);
            cardInDiscard = true;
        }

        for (int rep = 0; rep < repeatCount; rep++) {
            if (card.getType() == CardType.ATTACK) {
                playAttackCard(card, target, allEnemies, isJudgment, judgmentN);
            } else if (card.getType() == CardType.SKILL) {
                playSkillCard(card, target, allEnemies);
            }

            // 紧急避险
            if ("emergency_evasion".equals(card.getNameKey())) {
                Card hopeless = new Card("temp_hopeless_" + hand.size(), "hopeless", CardType.CURSE, 0, CardRarity.COMMON);
                if (hand.size() < maxHandSize) hand.add(hopeless);
                else discardPile.add(hopeless);
            }
            // 盛大烟火
            if ("grand_fireworks".equals(card.getNameKey()) && target != null) {
                int exhaustedCount = hand.size();
                for (Card c : new ArrayList<>(hand)) {
                    hand.remove(c);
                    exhaustPile.add(c);
                }
                target.addStatus("burn", exhaustedCount * 5);
                for (int i = 0; i < exhaustedCount; i++) triggerRottenMiasma();
            }

            if (card.getType() == CardType.POWER) {
                if (card.getEffect() != null) applyCardEffect(card, target, allEnemies);
                if (!playedPowerCards.contains(card)) playedPowerCards.add(card);
            }
        }

        // 不断骚扰
        if (constantHarass && card.getCost() == 0) dealRandomDamage(5);

        // 鸡煲杀手
        if (card.getType() != CardType.ATTACK && allEnemies != null) {
            for (Enemy enemy : allEnemies) {
                if (enemy.getHp() > 0 && "strength_on_non_attack".equals(enemy.getSpecialAbility())) {
                    enemy.setStrength(enemy.getStrength() + 3);
                    if (enemy.getIntent() == EnemyIntent.ATTACK) {
                        enemy.setIntentValue(enemy.getIntentValue() + 3);
                    }
                }
            }
        }

        // 聚能射线
        if (card.hasEffectType("damage_increase")) {
            card.setDamage(card.getDamage() + card.getEffectValue("damage_increase", 1));
        }

        // 信念积累
        if (card.getType() == CardType.ATTACK) {
            for (List<Card> pile : Arrays.asList(hand, drawPile, discardPile)) {
                for (Card c : pile) {
                    if ("faith_accumulation".equals(c.getNameKey())) {
                        c.setDamage(c.getDamage() + c.getEffectValue("play_attack_damage_up", 1));
                    }
                }
            }
        }

        // 雷云翻滚
        if (card.getType() == CardType.ATTACK && attackParalysisActive && allEnemies != null) {
            for (Enemy enemy : allEnemies) {
                if (enemy.getHp() > 0) enemy.addStatus("paralysis", 1);
            }
            elementalAppliedThisTurn = true;
        }

        // 卡牌离场处理
        if (card.getType() == CardType.ATTACK || card.getType() == CardType.SKILL) {
            if (card.isExhaustSelf() && cardInDiscard) {
                discardPile.remove(card);
                exhaustPile.add(card);
                triggerRottenMiasma();
                if (recycleGoldPerExhaust > 0) gold += recycleGoldPerExhaust;
            }
        } else if (card.getType() == CardType.POWER) {
            exhaustPile.add(card);
            if (recycleGoldPerExhaust > 0) gold += recycleGoldPerExhaust;
        }

        return true;
    }

    private void playAttackCard(Card card, Enemy target, List<Enemy> allEnemies, boolean isJudgment, int judgmentN) {
        int baseDamage = card.getDamage();
        if (isJudgment) baseDamage = 3 + (5 + judgmentN) * judgmentN;
        if (card.hasEffectType("gold_damage")) {
            int goldPerDmg = card.getEffectValue("gold_damage", 100);
            if (goldPerDmg > 0) baseDamage += gold / goldPerDmg;
        }
        if (card.hasEffectType("skill_count_damage")) {
            int skillCount = 0;
            for (Card c : combinePiles()) {
                if (c.getType() == CardType.SKILL) skillCount++;
            }
            baseDamage += skillCount * card.getEffectValue("skill_count_damage", 2);
        }

        int damage = baseDamage + strength + tempStrength;
        if (card.hasEffectType("double_strength_damage")) {
            damage = baseDamage + strength * 2 + tempStrength;
        }
        boolean hasWeak = hasStatus("weak") || hasStatus("weak_persistent");
        if (hasWeak && !hasStatus("weak_immunity")) damage = damage / 2;
        if (hasStatus("lurk") && !stealthNegatePenalty) damage = (int) (damage * 0.75);
        if (hasStatus("increase_damage")) {
            for (Map<String, Object> s : statusEffects) {
                if ("increase_damage".equals(s.get("type"))) {
                    damage += ((Number) s.get("value")).intValue();
                    break;
                }
            }
        }
        if (card.hasEffectType("assassinate_bonus") && hasStatus("assassinate")) {
            damage += card.getEffectValue("assassinate_bonus", 4);
        }
        if (nextAttackMultiplier > 1) {
            damage = (int) (damage * nextAttackMultiplier);
            nextAttackMultiplier = 1;
        }
        if (allDamageMultiplierThisTurn > 1) {
            damage = (int) (damage * allDamageMultiplierThisTurn);
        }

        // 圣能消耗
        int holyExtra = 0;
        if (card.hasEffectType("consume_holy_energy") && hasStatus("holy_energy")) {
            for (Map<String, Object> s : statusEffects) {
                if ("holy_energy".equals(s.get("type"))) {
                    int val = ((Number) s.getOrDefault("value", 0)).intValue();
                    s.put("value", Math.max(0, val - 2));
                    if (val >= 2) holyExtra = 1;
                    break;
                }
            }
        }

        // 全体攻击
        if ("all_enemies".equals(card.getTarget()) && allEnemies != null) {
            int aoeHits = card.hasEffectType("hit_multiple") ? card.getEffectValue("hit_multiple", 1) : 1;
            aoeHits += holyExtra;
            for (int h = 0; h < aoeHits; h++) {
                for (Enemy enemy : allEnemies) {
                    if (enemy.getHp() > 0) dealDamageToEnemy(enemy, damage);
                }
            }
        }
        // 随机攻击
        else if (card.hasEffectType("hit_random") && allEnemies != null) {
            int hitCount = card.getEffectValue("hit_random", 1);
            List<Enemy> alive = allEnemies.stream().filter(e -> e.getHp() > 0).collect(Collectors.toList());
            Random rnd = new Random();
            for (int h = 0; h < hitCount; h++) {
                if (!alive.isEmpty()) dealDamageToEnemy(alive.get(rnd.nextInt(alive.size())), damage);
            }
        }
        // 单体攻击
        else if ("enemy".equals(card.getTarget()) && target != null) {
            int dealtDamageOnTarget = 0;
            if (card.hasEffectType("holy_energy_hits")) {
                int holyLayers = 0;
                for (Map<String, Object> s : statusEffects) {
                    if ("holy_energy".equals(s.get("type"))) {
                        holyLayers = ((Number) s.getOrDefault("value", 0)).intValue();
                        s.put("value", 0);
                        break;
                    }
                }
                int hits = Math.max(holyLayers, 1);
                for (int h = 0; h < hits; h++) {
                    if (target.getHp() > 0) {
                        int dealt = dealDamageToEnemy(target, damage);
                        if (dealt > 0) dealtDamageOnTarget += dealt;
                    }
                }
            } else if (card.hasEffectType("gold_hit_multiple")) {
                int goldPerHit = card.getEffectValue("gold_hit_multiple", 100);
                int hits = 2 + (goldPerHit > 0 ? gold / goldPerHit : 0);
                for (int h = 0; h < hits; h++) {
                    if (target.getHp() > 0) {
                        int dealt = dealDamageToEnemy(target, damage);
                        if (dealt > 0) dealtDamageOnTarget += dealt;
                    }
                }
            } else if (card.hasEffectType("hit_multiple")) {
                int hits = card.getEffectValue("hit_multiple", 1) + holyExtra;
                for (int h = 0; h < hits; h++) {
                    if (target.getHp() > 0) {
                        int dealt = dealDamageToEnemy(target, damage);
                        if (dealt > 0) dealtDamageOnTarget += dealt;
                    }
                }
            } else if (card.hasEffectType("shield_dance")) {
                int shieldDmg = block + strength + tempStrength;
                int hits = card.getEffectValue("shield_dance", 3);
                for (int h = 0; h < hits; h++) {
                    if (target.getHp() > 0) {
                        int dealt = dealDamageToEnemy(target, shieldDmg);
                        if (dealt > 0) dealtDamageOnTarget += dealt;
                    }
                }
            } else {
                int hits = 1;
                if (card.hasEffectType("hit_twice")) hits = card.getEffectValue("hit_twice", 2);
                for (int h = 0; h < hits; h++) {
                    int dealt = dealDamageToEnemy(target, damage);
                    if (dealt > 0) dealtDamageOnTarget += dealt;
                }
            }
            // 黑夜如昼
            if (card.hasEffectType("lurk_extra_hit") && hasStatus("lurk")) {
                int extraHits = card.getEffectValue("lurk_extra_hit", 1);
                for (int h = 0; h < extraHits; h++) {
                    if (target.getHp() > 0) dealDamageToEnemy(target, damage);
                }
            }
            // 化守为攻
            if (card.hasEffectType("guard_to_attack")) {
                int dmg = block + strength + tempStrength;
                if (target != null) dealDamageToEnemy(target, dmg);
            }
            // 压榨
            if (card.hasEffectType("kill_gold") && target.getHp() <= 0) {
                gold += card.getEffectValue("kill_gold", 20);
            }
            // 血灾
            if (card.hasEffectType("kill_heal") && target.getHp() <= 0) {
                int healV = card.getEffectValue("kill_heal", 3);
                if (healV > 0) heal(healV);
            }
            // 畅饮鲜血
            if (card.hasEffectType("lifesteal") && target != null && dealtDamageOnTarget > 0) {
                int divisor = card.getEffectValue("lifesteal", 2);
                heal(dealtDamageOnTarget / divisor);
            }
        }

        // 遗物attack_aoe
        for (Relic relic : relics) {
            if ("attack_aoe".equals(relic.getEffect().get("type")) && allEnemies != null) {
                int aoeDmg = ((Number) relic.getEffect().getOrDefault("value", 2)).intValue();
                for (Enemy enemy : allEnemies) {
                    if (enemy.getHp() > 0) dealDamageToEnemy(enemy, aoeDmg);
                }
                break;
            }
        }

        // 攻击牌附加效果
        if (card.getEffect() != null) applyCardEffect(card, target, allEnemies);

        // 穿透伤害
        if (card.getPenetratingDamage() > 0) {
            int penDmg = card.getPenetratingDamage() + strength + tempStrength;
            hasWeak = hasStatus("weak") || hasStatus("weak_persistent");
            if (hasWeak && !hasStatus("weak_immunity")) penDmg = penDmg / 2;
            if (hasStatus("increase_damage")) {
                for (Map<String, Object> s : statusEffects) {
                    if ("increase_damage".equals(s.get("type"))) {
                        penDmg += ((Number) s.get("value")).intValue();
                        break;
                    }
                }
            }
            if ("all_enemies".equals(card.getTarget()) && allEnemies != null) {
                for (Enemy enemy : allEnemies) {
                    if (enemy.getHp() > 0) dealDamageToEnemy(enemy, penDmg);
                }
            } else if (target != null) {
                dealDamageToEnemy(target, penDmg);
            }
        }

        // 攻击牌附带格挡/治疗
        if (card.getBlock() > 0) {
            int blockGain = card.getBlock() + dexterity;
            if (hasStatus("lurk")) blockGain = (int) (blockGain * 1.5);
            else if (hasStatus("assassinate") && !stealthNegatePenalty) blockGain = (int) (blockGain * 0.75);
            block += blockGain;
            triggerShieldHero();
        }
        if (card.getHeal() > 0) heal(card.getHeal());
    }

    private void playSkillCard(Card card, Enemy target, List<Enemy> allEnemies) {
        int blockGain = card.getBlock();
        if (card.getBlock() > 0) {
            int extraBlock = 0;
            if (card.hasEffectType("after_a_hundred_years")) extraBlock = ahbcGoldDecreaseCount;
            blockGain = card.getBlock() + dexterity + extraBlock;
            if (hasStatus("lurk")) blockGain = (int) (blockGain * 1.5);
            else if (hasStatus("assassinate") && !stealthNegatePenalty) blockGain = (int) (blockGain * 0.75);
        }

        // 圣能护盾
        if (card.hasEffectType("holy_energy_shield")) {
            int base = 5, multiplier = 3;
            for (Map<String, Object> e : card.getEffectsAsList()) {
                if ("holy_energy_shield".equals(e.get("type"))) {
                    base = ((Number) e.getOrDefault("base", 5)).intValue();
                    multiplier = ((Number) e.getOrDefault("multiplier", 3)).intValue();
                    break;
                }
            }
            int holyLayers = 0;
            for (Map<String, Object> s : statusEffects) {
                if ("holy_energy".equals(s.get("type"))) {
                    holyLayers = ((Number) s.getOrDefault("value", 0)).intValue();
                    s.put("value", 0);
                    break;
                }
            }
            blockGain += base + multiplier * holyLayers;
        }

        // 圣能消耗
        if (card.hasEffectType("consume_holy_energy")) {
            for (Map<String, Object> e : card.getEffectsAsList()) {
                if ("consume_holy_energy".equals(e.get("type")) && !e.containsKey("damage_per_layer")) {
                    int consumeValue = ((Number) e.getOrDefault("value", 1)).intValue();
                    for (Map<String, Object> s : statusEffects) {
                        if ("holy_energy".equals(s.get("type"))) {
                            int val = ((Number) s.getOrDefault("value", 0)).intValue();
                            int consumed = Math.min(consumeValue, val);
                            s.put("value", val - consumed);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        // 光能波动
        int holyDamage = 0;
        if (card.hasEffectType("consume_holy_energy")) {
            for (Map<String, Object> e : card.getEffectsAsList()) {
                if ("consume_holy_energy".equals(e.get("type")) && e.containsKey("damage_per_layer")) {
                    int dmgPerLayer = ((Number) e.getOrDefault("damage_per_layer", 4)).intValue();
                    int holyLayers = 0;
                    for (Map<String, Object> s : statusEffects) {
                        if ("holy_energy".equals(s.get("type"))) {
                            holyLayers = ((Number) s.getOrDefault("value", 0)).intValue();
                            s.put("value", 0);
                            break;
                        }
                    }
                    holyDamage = card.getDamage() + dmgPerLayer * holyLayers;
                    break;
                }
            }
        }

        // 认证大师
        if (skillBlockThisTurn > 0) {
            block += skillBlockThisTurn;
            triggerShieldHero();
        }

        block += blockGain;
        triggerShieldHero();
        if (card.getEffect() != null) applyCardEffect(card, target, allEnemies);

        if (holyDamage > 0) {
            if ("enemy".equals(card.getTarget()) && target != null) dealDamageToEnemy(target, holyDamage);
            else if ("all_enemies".equals(card.getTarget()) && allEnemies != null) {
                for (Enemy e : allEnemies) {
                    if (e.getHp() > 0) dealDamageToEnemy(e, holyDamage);
                }
            }
        }
        if (card.getHeal() > 0) heal(card.getHeal());
    }

    public int dealDamageToEnemy(Enemy enemy, int damage) {
        return dealDamageToEnemy(enemy, damage, false, "card");
    }

    public int dealDamageToEnemy(Enemy enemy, int damage, boolean penetrating, String source) {
        // 流血
        for (Map<String, Object> s : enemy.getStatusEffects()) {
            if ("bleed".equals(s.get("type"))) {
                damage += ((Number) s.getOrDefault("value", 0)).intValue();
                break;
            }
        }
        int actual;
        if (penetrating) actual = enemy.takePenetratingDamage(damage);
        else actual = enemy.takeDamage(damage);

        if ("card".equals(source)) {
            if (actual > 0 && poisonOnUnblockedDamage > 0) {
                enemy.addStatus("poison", poisonOnUnblockedDamage);
                elementalAppliedThisTurn = true;
                triggerInvadeBody();
            }
            for (Relic relic : relics) {
                if ("poison_on_hit".equals(relic.getEffect().get("type"))) {
                    int poison = ((Number) relic.getEffect().getOrDefault("value", 1)).intValue();
                    if (actual > 0) {
                        enemy.addStatus("poison", poison);
                        elementalAppliedThisTurn = true;
                        triggerInvadeBody();
                    }
                    break;
                }
            }
            if (actual > 0 && hasStatus("assassinate")) {
                enemy.addStatus("bleed", 1);
            }
        }
        return actual;
    }

    public void applyCardEffect(Card card, Enemy target, List<Enemy> allEnemies) {
        List<Map<String, Object>> effects = card.getEffectsAsList();
        if (effects.isEmpty()) return;

        int paperBonus = hasRelic("paper_krane") ? 1 : 0;
        int extraDebuff = paperBonus;

        for (Map<String, Object> effect : effects) {
            String etype = (String) effect.get("type");
            if (etype == null) continue;

            // === 敌人状态效果 ===
            if ("vulnerable".equals(etype) && target != null) {
                target.addStatus("vulnerable", ((Number) effect.getOrDefault("value", 1)).intValue() + paperBonus);
            } else if ("poison".equals(etype) && target != null) {
                int poisonVal = ((Number) effect.getOrDefault("value", 3)).intValue();
                if (elementalMastery && !elementalMasteryUsed) {
                    elementalMasteryUsed = true;
                    poisonVal = (int) Math.ceil(poisonVal * 1.5);
                }
                target.addStatus("poison", poisonVal);
                if (elementalCoat) block += 2;
                elementalAppliedThisTurn = true;
                triggerInvadeBody();
                if (poisonBurnSync) target.addStatus("burn", poisonVal);
            } else if ("weak".equals(etype) && target != null) {
                target.addStatus("weak", ((Number) effect.getOrDefault("value", 1)).intValue() + paperBonus);
            } else if ("slow".equals(etype) && target != null) {
                target.addStatus("slow", ((Number) effect.getOrDefault("value", 1)).intValue());
            } else if ("burn".equals(etype) && target != null) {
                int burnVal = ((Number) effect.getOrDefault("value", 1)).intValue();
                if (elementalMastery && !elementalMasteryUsed) {
                    elementalMasteryUsed = true;
                    burnVal = (int) Math.ceil(burnVal * 1.5);
                }
                target.addStatus("burn", burnVal);
                if (elementalCoat) block += 2;
                elementalAppliedThisTurn = true;
                triggerInvadeBody();
                if (poisonBurnSync) target.addStatus("poison", burnVal);
            } else if ("fragile".equals(etype) && target != null) {
                target.addStatus("fragile", ((Number) effect.getOrDefault("value", 1)).intValue() + paperBonus);
            } else if ("frostbite".equals(etype) && target != null) {
                int frostVal = ((Number) effect.getOrDefault("value", 1)).intValue();
                if (elementalMastery && !elementalMasteryUsed) {
                    elementalMasteryUsed = true;
                    frostVal = (int) Math.ceil(frostVal * 1.5);
                }
                if (snowOnFrost) frostVal += 2;
                target.addStatus("frostbite", frostVal);
                frostbiteAppliedThisTurn += frostVal;
            } else if ("stun".equals(etype) && target != null) {
                target.addStatus("stun", ((Number) effect.getOrDefault("value", 1)).intValue());
                if (elementalCoat) block += 2;
                elementalAppliedThisTurn = true;
                triggerInvadeBody();
            } else if ("paralysis".equals(etype) && target != null) {
                int paraVal = ((Number) effect.getOrDefault("value", 1)).intValue();
                if (elementalMastery && !elementalMasteryUsed) {
                    elementalMasteryUsed = true;
                    paraVal = (int) Math.floor(paraVal * 1.5);
                }
                target.addStatus("paralysis", paraVal);
                // 滋滋作响
                if (sizzling && enemies != null) {
                    for (Enemy e : enemies) {
                        if (e.getHp() > 0 && e != target) e.addStatus("paralysis", paraVal);
                    }
                }
                // 积蓄电能
                for (List<Card> pile : Arrays.asList(hand, drawPile, discardPile, exhaustPile)) {
                    for (Card c : pile) {
                        if ("accumulated_electric_charge".equals(c.getNameKey())) {
                            c.setDamage(c.getDamage() + 2);
                        }
                    }
                }
                if (elementalCoat) block += 2;
                elementalAppliedThisTurn = true;
                triggerInvadeBody();
                if (drawOnParalysis) drawCards(1);
            } else if ("bleed".equals(etype) && target != null) {
                target.addStatus("bleed", ((Number) effect.getOrDefault("value", 1)).intValue());
            } else if ("lurk".equals(etype)) {
                removeStatus("assassinate");
                addStatus("lurk", 1);
                if (lurkShieldOnEnter > 0) block += lurkShieldOnEnter;
                if (stealthTriggerShieldDamage) {
                    block += 1;
                    dealRandomDamage(1);
                }
            } else if ("assassinate".equals(etype)) {
                removeStatus("lurk");
                addStatus("assassinate", 1);
                if (stealthTriggerShieldDamage) {
                    block += 1;
                    dealRandomDamage(1);
                }
                if (assassinateDamageOnEnter > 0) dealRandomDamage(assassinateDamageOnEnter);
            } else if ("exit_stealth".equals(etype)) {
                removeStatus("lurk");
                removeStatus("assassinate");
            }
            // === 抽牌类 ===
            else if ("draw_cards".equals(etype)) {
                drawCards(((Number) effect.getOrDefault("value", 1)).intValue());
            } else if ("draw_per_enemy".equals(etype)) {
                int alive = (int) enemies.stream().filter(e -> e.getHp() > 0).count();
                if (alive > 0) drawCards(alive);
            } else if ("draw_and_discard".equals(etype)) {
                drawCards(((Number) effect.getOrDefault("value", 3)).intValue());
                pendingDiscard++;
            } else if ("draw_to_max".equals(etype)) {
                drawCards(maxHandSize - hand.size());
            } else if ("next_turn_block".equals(etype)) {
                nextTurnBlock += ((Number) effect.getOrDefault("value", 0)).intValue();
            } else if ("skip_next_turn".equals(etype)) {
                addStatus("skip_turn", 1);
            } else if ("steal_draw".equals(etype)) {
                int count = ((Number) effect.getOrDefault("value", 1)).intValue();
                drawCards(count);
                if (hasStatus("lurk")) drawCards(1);
            }
            // === 力量类 ===
            else if ("strength".equals(etype)) {
                int val = ((Number) effect.getOrDefault("value", 1)).intValue();
                if (target != null) target.setStrength(target.getStrength() + val);
                else strength += val;
            } else if ("double_strength".equals(etype)) {
                strength = strength * 2;
            } else if ("sacrifice_strength_heal".equals(etype)) {
                int multiplier = ((Number) effect.getOrDefault("value", 2)).intValue();
                int lost = Math.max(0, strength);
                if (lost > 0) {
                    strength -= lost;
                    heal(lost * multiplier);
                }
            }
            // === 牌堆操作 ===
            else if ("upgrade_card".equals(etype)) {
                if (!hand.isEmpty()) {
                    Card c = hand.get(0);
                    int val = ((Number) effect.getOrDefault("value", 1)).intValue();
                    if (c.getDamage() > 0) c.setDamage(c.getDamage() + val);
                    if (c.getBlock() > 0) c.setBlock(c.getBlock() + val);
                }
            } else if ("exhaust_card".equals(etype)) {
                pendingExhaust = ((Number) effect.getOrDefault("value", 1)).intValue();
            } else if ("exhaust_random".equals(etype)) {
                if (!hand.isEmpty()) {
                    Random rnd = new Random();
                    Card c = hand.remove(rnd.nextInt(hand.size()));
                    exhaustPile.add(c);
                    triggerRottenMiasma();
                }
            }
            // === 自身状态 ===
            else if ("dodge".equals(etype)) addStatus("dodge", ((Number) effect.getOrDefault("value", 1)).intValue());
            else if ("increase_damage".equals(etype)) addStatus("increase_damage", ((Number) effect.getOrDefault("value", 2)).intValue());
            else if ("dexterity".equals(etype)) dexterity += ((Number) effect.getOrDefault("value", 1)).intValue();
            else if ("guard_up".equals(etype)) guard += ((Number) effect.getOrDefault("value", 1)).intValue();
            else if ("temp_strength".equals(etype)) tempStrength += ((Number) effect.getOrDefault("value", 1)).intValue();
            else if ("phasing".equals(etype)) addStatus("phasing", ((Number) effect.getOrDefault("value", 1)).intValue());
            else if ("weak_immunity".equals(etype)) addStatus("weak_immunity", ((Number) effect.getOrDefault("value", 1)).intValue());
            // === 圣能 ===
            else if ("holy_energy".equals(etype)) {
                if (!holyEnergyBlocked) addStatus("holy_energy", ((Number) effect.getOrDefault("value", 1)).intValue());
            } else if ("consume_holy_energy".equals(etype)) { /* handled in play_card */ }
            else if ("holy_energy_hits".equals(etype)) { /* handled in play_card */ }
            else if ("holy_energy_heal".equals(etype)) {
                int holyLayers = 0;
                for (Map<String, Object> s : statusEffects) {
                    if ("holy_energy".equals(s.get("type"))) {
                        holyLayers = ((Number) s.getOrDefault("value", 0)).intValue();
                        s.put("value", 0);
                        break;
                    }
                }
                if (holyLayers > 0) heal(holyLayers);
            } else if ("holy_energy_double_damage".equals(etype)) {
                int consumeValue = ((Number) effect.getOrDefault("value", 10)).intValue();
                for (Map<String, Object> s : statusEffects) {
                    if ("holy_energy".equals(s.get("type"))) {
                        int val = ((Number) s.getOrDefault("value", 0)).intValue();
                        if (val >= consumeValue) {
                            s.put("value", val - consumeValue);
                            allDamageMultiplierThisTurn = 2;
                        }
                        break;
                    }
                }
            } else if ("block_holy_energy".equals(etype)) holyEnergyBlocked = true;
            else if ("triple_damage_die".equals(etype)) {
                allDamageMultiplierThisTurn = ((Number) effect.getOrDefault("value", 3)).intValue();
                dieIfEnemiesAlive = true;
            }
            // === 扣血/回能 ===
            else if ("lose_hp".equals(etype)) {
                int hpLoss = ((Number) effect.getOrDefault("value", 1)).intValue();
                hp -= hpLoss;
                if (bloodCloak) {
                    block += 4;
                    triggerShieldHero();
                }
                if (strengthOnHpLoss > 0) strength += strengthOnHpLoss;
            } else if ("lose_max_hp".equals(etype)) {
                int hpLoss = ((Number) effect.getOrDefault("value", 1)).intValue();
                maxHp = Math.max(1, maxHp - hpLoss);
                hp = Math.min(hp, maxHp);
            } else if ("blood_demon".equals(etype)) bloodDemon = true;
            else if ("blood_cloak".equals(etype)) bloodCloak = true;
            else if ("shield_no_decay".equals(etype)) shieldNoDecay = true;
            else if ("stealth_trigger_shield_damage".equals(etype)) stealthTriggerShieldDamage = true;
            else if ("lurk_shield_on_enter".equals(etype)) lurkShieldOnEnter = ((Number) effect.getOrDefault("value", 3)).intValue();
            else if ("assassinate_damage_on_enter".equals(etype)) assassinateDamageOnEnter = ((Number) effect.getOrDefault("value", 3)).intValue();
            else if ("stealth_negate_penalty".equals(etype)) stealthNegatePenalty = true;
            else if ("future_strike_gold".equals(etype)) {
                int cost = ((Number) effect.getOrDefault("value", 10)).intValue();
                if (gold >= cost) {
                    gold -= cost;
                    baseStrength += 1;
                    ahbcGoldDecreaseCount++;
                }
            } else if ("recycle".equals(etype)) recycleGoldPerExhaust = ((Number) effect.getOrDefault("value", 2)).intValue();
            else if ("not_willing_to_lose".equals(etype)) drawOnDiscardThisTurn = true;
            else if ("after_a_hundred_years".equals(etype)) ahbcGoldDecreaseCount = 0;
            else if ("restore_energy".equals(etype)) energy = maxEnergy;
            else if ("gain_energy".equals(etype)) {
                if (!blockEnergyGain) energy += ((Number) effect.getOrDefault("value", 1)).intValue();
            }
            // === 其他 ===
            else if ("lose_gold".equals(etype)) {
                int oldGold = gold;
                gold = Math.max(0, gold - ((Number) effect.getOrDefault("value", 10)).intValue());
                if (gold < oldGold) ahbcGoldDecreaseCount++;
            } else if ("gain_gold".equals(etype)) gold += ((Number) effect.getOrDefault("value", 3)).intValue();
            else if ("guard_to_attack".equals(etype)) { /* handled in play_card */ }
            else if ("hit_multiple".equals(etype)) { /* handled in play_card */ }
            else if ("shield_dance".equals(etype)) { /* handled in play_card */ }
            else if ("double_block".equals(etype)) block = block * 2;
            else if ("next_attack_double".equals(etype)) nextAttackMultiplier = 2;
            else if ("strength_per_turn".equals(etype)) strengthPerTurn += ((Number) effect.getOrDefault("value", 1)).intValue();
            else if ("frostbite_per_turn".equals(etype)) frostbitePerTurn += ((Number) effect.getOrDefault("value", 3)).intValue();
            else if ("draw_extra".equals(etype)) drawCount += ((Number) effect.getOrDefault("value", 1)).intValue();
            else if ("random_debuff".equals(etype) && target != null) {
                Random rnd = new Random();
                String[] choices = {"poison", "burn", "frostbite"};
                String choice = choices[rnd.nextInt(choices.length)];
                if ("poison".equals(choice)) target.addStatus("poison", 1 + extraDebuff);
                else if ("burn".equals(choice)) target.addStatus("burn", 1 + extraDebuff);
                else target.addStatus("frostbite", 2 + extraDebuff);
                elementalAppliedThisTurn = true;
                triggerInvadeBody();
            } else if ("double_enemy_debuff".equals(etype) && target != null) {
                for (Map<String, Object> s : target.getStatusEffects()) {
                    String t = (String) s.get("type");
                    if (("vulnerable".equals(t) || "weak".equals(t) || "fragile".equals(t))
                        && ((Number) s.getOrDefault("value", 0)).intValue() > 0) {
                        s.put("value", ((Number) s.get("value")).intValue() * 2);
                    }
                }
            } else if ("double_poison".equals(etype) && target != null) {
                for (Map<String, Object> s : target.getStatusEffects()) {
                    if ("poison".equals(s.get("type"))) {
                        s.put("value", ((Number) s.get("value")).intValue() * 2);
                        break;
                    }
                }
            } else if ("double_burn".equals(etype) && target != null) {
                for (Map<String, Object> s : target.getStatusEffects()) {
                    if ("burn".equals(s.get("type"))) {
                        s.put("value", ((Number) s.get("value")).intValue() * 2);
                        break;
                    }
                }
            } else if ("double_frostbite".equals(etype) && target != null) {
                for (Map<String, Object> s : target.getStatusEffects()) {
                    if ("frostbite".equals(s.get("type"))) {
                        s.put("value", ((Number) s.get("value")).intValue() * 2);
                        break;
                    }
                }
            } else if ("element_recycle".equals(etype) && target != null) {
                int removed = 0;
                for (String st : Arrays.asList("poison", "burn", "frostbite")) {
                    if (target.hasStatus(st)) {
                        target.removeStatus(st);
                        removed++;
                    }
                }
                if (removed > 0) energy += removed;
            } else if ("gold_to_shield".equals(etype)) {
                int extra = gold / 50;
                if (extra > 0) {
                    if (hasStatus("lurk")) extra = (int) (extra * 1.5);
                    else if (hasStatus("assassinate")) extra = (int) (extra * 0.75);
                    block += extra;
                }
            } else if ("spend_gold_for_energy".equals(etype)) {
                int cost = ((Number) effect.getOrDefault("value", 5)).intValue();
                if (gold >= cost) {
                    gold -= cost;
                    energy += 1;
                    ahbcGoldDecreaseCount++;
                }
            } else if ("spend_gold_for_draw".equals(etype)) {
                int cost = ((Number) effect.getOrDefault("value", 10)).intValue();
                if (gold >= cost) {
                    gold -= cost;
                    drawCount += 1;
                    ahbcGoldDecreaseCount++;
                }
            }
            // === 能力永久效果 ===
            else if ("strength_on_hp_loss".equals(etype)) strengthOnHpLoss += ((Number) effect.getOrDefault("value", 1)).intValue();
            else if ("poison_on_unblocked_damage".equals(etype)) poisonOnUnblockedDamage += ((Number) effect.getOrDefault("value", 1)).intValue();
            else if ("hp_change_pierce".equals(etype)) hpChangePierce += ((Number) effect.getOrDefault("value", 3)).intValue();
            else if ("block_per_turn".equals(etype)) blockPerTurn += ((Number) effect.getOrDefault("value", 5)).intValue();
            else if ("lifesteal".equals(etype)) { /* handled in play_card */ }
            else if ("blood_burn_round".equals(etype)) bloodBurnRound += ((Number) effect.getOrDefault("value", 1)).intValue();
            else if ("holy_energy_per_turn".equals(etype)) holyEnergyPerTurn += ((Number) effect.getOrDefault("value", 1)).intValue();
            else if ("holy_energy_shield".equals(etype)) { /* handled in play_card */ }
            else if ("elemental_mastery".equals(etype)) elementalMastery = true;
            else if ("poison_burn_sync".equals(etype)) poisonBurnSync = true;
            else if ("attack_paralysis_trigger".equals(etype)) attackParalysisActive = true;
            else if ("burn_per_turn".equals(etype)) burnPerTurn += ((Number) effect.getOrDefault("value", 3)).intValue();
            else if ("paralysis_if_paralyzed".equals(etype) && target != null) {
                if (target.hasStatus("paralysis")) {
                    target.addStatus("paralysis", ((Number) effect.getOrDefault("value", 2)).intValue() + extraDebuff);
                    elementalAppliedThisTurn = true;
                    triggerInvadeBody();
                }
            } else if ("frostbite_self_per_turn".equals(etype)) frostbiteSelfPerTurn += ((Number) effect.getOrDefault("value", 3)).intValue();
            else if ("poison_transfer_on_death".equals(etype)) poisonTransferOnDeath = true;
            else if ("burn_transfer_on_death".equals(etype)) burnTransferOnDeath = true;
            else if ("transfer_frostbite".equals(etype) && target != null) {
                int frostVal = 0;
                for (Map<String, Object> s : statusEffects) {
                    if ("frostbite".equals(s.get("type"))) {
                        frostVal = ((Number) s.getOrDefault("value", 0)).intValue();
                        break;
                    }
                }
                if (frostVal > 0) {
                    statusEffects.removeIf(s -> "frostbite".equals(s.get("type")));
                    target.addStatus("frostbite", frostVal);
                    elementalAppliedThisTurn = true;
                    triggerInvadeBody();
                }
            } else if ("frostbite_self".equals(etype)) addStatus("frostbite", ((Number) effect.getOrDefault("value", 4)).intValue());
            else if ("trigger_burn".equals(etype) && target != null) {
                int burnDmg = 0;
                for (Map<String, Object> s : target.getStatusEffects()) {
                    if ("burn".equals(s.get("type"))) {
                        burnDmg = ((Number) s.getOrDefault("value", 0)).intValue();
                        s.put("value", Math.max(0, burnDmg - 1));
                        if (((Number) s.get("value")).intValue() <= 0) {
                            target.getStatusEffects().remove(s);
                        }
                        break;
                    }
                }
                if (burnDmg > 0) target.setHp(target.getHp() - burnDmg);
            } else if ("frostbite_if_none".equals(etype) && target != null) {
                int frostVal = ((Number) effect.getOrDefault("value", 5)).intValue() + extraDebuff;
                boolean hadFrost = target.hasStatus("frostbite");
                target.addStatus("frostbite", frostVal);
                if (!hadFrost) target.addStatus("frostbite", frostVal);
                elementalAppliedThisTurn = true;
                triggerInvadeBody();
            } else if ("paralysis_no_decay".equals(etype)) paralysisNoDecay = true;
            else if ("convert_frostbite_to_burn".equals(etype) && target != null) {
                int frostVal = 0;
                for (Map<String, Object> s : target.getStatusEffects()) {
                    if ("frostbite".equals(s.get("type"))) {
                        frostVal = ((Number) s.getOrDefault("value", 0)).intValue();
                        break;
                    }
                }
                if (frostVal > 0) {
                    target.removeStatus("frostbite");
                    target.addStatus("burn", frostVal);
                    elementalAppliedThisTurn = true;
                    triggerInvadeBody();
                }
            } else if ("damage_by_frostbite".equals(etype) && target != null) {
                int frostVal = 0;
                for (Map<String, Object> s : target.getStatusEffects()) {
                    if ("frostbite".equals(s.get("type"))) {
                        frostVal = ((Number) s.getOrDefault("value", 0)).intValue();
                        s.put("value", Math.max(0, frostVal / 2));
                        break;
                    }
                }
                if (frostVal > 0) dealDamageToEnemy(target, frostVal);
            } else if ("burn_all_enemies".equals(etype) && allEnemies != null) {
                int burnVal = ((Number) effect.getOrDefault("value", 4)).intValue() + extraDebuff;
                for (Enemy e : allEnemies) {
                    if (e.getHp() > 0) e.addStatus("burn", burnVal);
                }
                elementalAppliedThisTurn = true;
                triggerInvadeBody();
            } else if ("spread_paralysis".equals(etype) && target != null) {
                int paraVal = 0;
                for (Map<String, Object> s : target.getStatusEffects()) {
                    if ("paralysis".equals(s.get("type"))) {
                        paraVal = ((Number) s.getOrDefault("value", 0)).intValue();
                        break;
                    }
                }
                if (paraVal > 0 && enemies != null) {
                    if (elementalMastery && !elementalMasteryUsed) {
                        elementalMasteryUsed = true;
                        paraVal = (int) Math.floor(paraVal * 1.5);
                    }
                    for (Enemy e : enemies) {
                        if (e.getHp() > 0 && e != target) e.addStatus("paralysis", paraVal);
                    }
                    elementalAppliedThisTurn = true;
                    triggerInvadeBody();
                }
            } else if ("paralysis_per_turn".equals(etype)) paralysisPerTurn += ((Number) effect.getOrDefault("value", 1)).intValue();
            else if ("draw_by_paralysis".equals(etype)) {
                int maxPara = 0;
                if (enemies != null) {
                    for (Enemy e : enemies) {
                        if (e.getHp() > 0) {
                            for (Map<String, Object> s : e.getStatusEffects()) {
                                if ("paralysis".equals(s.get("type"))) {
                                    maxPara = Math.max(maxPara, ((Number) s.getOrDefault("value", 0)).intValue());
                                }
                            }
                        }
                    }
                }
                int draw = Math.min(maxPara, ((Number) effect.getOrDefault("value", 5)).intValue());
                if (draw > 0) drawCards(draw);
            } else if ("double_paralysis".equals(etype) && target != null) {
                for (Map<String, Object> s : target.getStatusEffects()) {
                    if ("paralysis".equals(s.get("type"))) {
                        int addVal = ((Number) s.getOrDefault("value", 0)).intValue();
                        if (elementalMastery && !elementalMasteryUsed) {
                            elementalMasteryUsed = true;
                            addVal = (int) Math.floor(addVal * 1.5);
                        }
                        s.put("value", ((Number) s.get("value")).intValue() + addVal);
                        elementalAppliedThisTurn = true;
                        triggerInvadeBody();
                        break;
                    }
                }
            } else if ("draw_on_paralysis".equals(etype)) drawOnParalysis = true;
            else if ("extra_energy_if_paralysis".equals(etype)) {
                boolean hasPara = false;
                if (enemies != null) {
                    for (Enemy e : enemies) {
                        if (e.getHp() > 0) {
                            for (Map<String, Object> s : e.getStatusEffects()) {
                                if ("paralysis".equals(s.get("type"))) {
                                    hasPara = true;
                                    break;
                                }
                            }
                        }
                        if (hasPara) break;
                    }
                }
                if (hasPara && !blockEnergyGain) energy += ((Number) effect.getOrDefault("value", 1)).intValue();
            } else if ("block_energy_gain".equals(etype)) blockEnergyGain = true;
            else if ("elemental_coat".equals(etype)) elementalCoat = true;
            else if ("skill_block_this_turn".equals(etype)) skillBlockThisTurn = ((Number) effect.getOrDefault("value", 4)).intValue();
            else if ("next_turn_energy".equals(etype)) nextTurnEnergy += ((Number) effect.getOrDefault("value", 0)).intValue();
            else if ("clear_all_elemental_debuffs".equals(etype) && target != null) {
                for (String dt : Arrays.asList("poison", "burn", "frostbite", "paralysis")) {
                    target.removeStatus(dt);
                }
            } else if ("dexterity_if_paralysis".equals(etype)) {
                boolean hasPara = false;
                if (enemies != null) {
                    for (Enemy e : enemies) {
                        if (e.getHp() > 0) {
                            for (Map<String, Object> s : e.getStatusEffects()) {
                                if ("paralysis".equals(s.get("type"))) {
                                    hasPara = true;
                                    break;
                                }
                            }
                        }
                        if (hasPara) break;
                    }
                }
                if (hasPara) dexterity += ((Number) effect.getOrDefault("value", 1)).intValue();
            } else if ("next_turn_block_if_elemental".equals(etype)) nextTurnBlockElemental = ((Number) effect.getOrDefault("value", 8)).intValue();
            else if ("clear_self_negative_status".equals(etype)) {
                statusEffects.removeIf(s -> NEGATIVE_STATUS.contains(s.get("type")));
            } else if ("shield_hero".equals(etype)) shieldHero = true;
            else if ("constant_harass".equals(etype)) constantHarass = true;
            else if ("invade_body".equals(etype)) invadeBody = true;
            else if ("faith_annihilation".equals(etype)) faithAnnihilation = true;
            else if ("frostbite_shield".equals(etype)) {
                block += frostbiteAppliedThisTurn;
                triggerShieldHero();
            } else if ("rotten_miasma".equals(etype)) rottenMiasma = true;
            else if ("poison_random".equals(etype)) {
                int poisonVal = ((Number) effect.getOrDefault("value", 3)).intValue();
                List<Enemy> alive = enemies.stream().filter(e -> e.getHp() > 0).collect(Collectors.toList());
                if (!alive.isEmpty()) {
                    Enemy targetEnemy = alive.get(new Random().nextInt(alive.size()));
                    targetEnemy.addStatus("poison", poisonVal);
                    elementalAppliedThisTurn = true;
                    triggerInvadeBody();
                }
            } else if ("sizzling".equals(etype)) sizzling = true;
            else if ("snow_on_frost".equals(etype)) snowOnFrost = true;
        }
    }

    public void addStatus(String stype, int value) {
        for (Map<String, Object> s : statusEffects) {
            if (stype.equals(s.get("type"))) {
                s.put("value", ((Number) s.get("value")).intValue() + value);
                return;
            }
        }
        Map<String, Object> newStatus = new LinkedHashMap<>();
        newStatus.put("type", stype);
        newStatus.put("value", value);
        newStatus.put("duration", 2);
        statusEffects.add(newStatus);
    }

    public boolean hasStatus(String stype) {
        for (Map<String, Object> s : statusEffects) {
            if (stype.equals(s.get("type")) && ((Number) s.getOrDefault("value", 0)).intValue() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRelic(String nameKey) {
        for (Relic relic : relics) {
            if (nameKey.equals(relic.getNameKey())) return true;
        }
        return false;
    }

    public void removeStatus(String stype) {
        statusEffects.removeIf(s -> stype.equals(s.get("type")));
    }

    public void tickStatus() {
        List<Map<String, Object>> expired = new ArrayList<>();
        for (Map<String, Object> s : statusEffects) {
            String stype = (String) s.get("type");
            if (PERSISTENT_STATUS.contains(stype)) continue;
            int val = ((Number) s.getOrDefault("value", 0)).intValue();
            if ("burn".equals(stype) && val > 0) {
                hp -= val;
                s.put("value", Math.max(0, val - 1));
            } else if (stype.equals("vulnerable") || stype.equals("weak") || stype.equals("slow")
                || stype.equals("paralysis") || stype.equals("stun")) {
                s.put("value", Math.max(0, val - 1));
            }
            if (!LAYER_STATUS.contains(stype)) {
                int dur = ((Number) s.getOrDefault("duration", 2)).intValue();
                s.put("duration", dur - 1);
            }
            int newVal = ((Number) s.getOrDefault("value", 0)).intValue();
            int dur = ((Number) s.getOrDefault("duration", 2)).intValue();
            if (newVal <= 0 || (!LAYER_STATUS.contains(stype) && dur <= 0)) {
                expired.add(s);
            }
        }
        statusEffects.removeAll(expired);
    }

    public void tickPoison() {
        List<Map<String, Object>> expired = new ArrayList<>();
        for (Map<String, Object> s : statusEffects) {
            if ("poison".equals(s.get("type"))) {
                int val = ((Number) s.getOrDefault("value", 0)).intValue();
                if (val > 0) {
                    hp -= val;
                    s.put("value", Math.max(0, val - 1));
                    if (((Number) s.get("value")).intValue() <= 0) expired.add(s);
                }
            }
        }
        statusEffects.removeAll(expired);
    }

    public int takeDamage(int damage) {
        if (hasStatus("phasing")) {
            removeStatus("phasing");
            return 0;
        }
        if (hasStatus("dodge")) {
            removeStatus("dodge");
            return 0;
        }
        int paralysisLayers = 0;
        for (Map<String, Object> s : statusEffects) {
            if ("paralysis".equals(s.get("type"))) {
                paralysisLayers += ((Number) s.getOrDefault("value", 0)).intValue();
                break;
            }
        }
        damage += paralysisLayers;

        int blockVal = block;
        if (hasStatus("fragile")) blockVal = blockVal / 2;

        for (Relic relic : relics) {
            if ("damage_reduce".equals(relic.getEffect().get("type"))) {
                damage = Math.max(0, damage - ((Number) relic.getEffect().getOrDefault("value", 1)).intValue());
                break;
            }
        }

        if (guard > 0 && damage > 0) damage = Math.max(0, damage - guard);

        int actualDamage = Math.max(0, damage - blockVal);
        block -= Math.min(block, damage);
        hp -= actualDamage;

        if (actualDamage > 0 && strengthOnHpLoss > 0) strength += strengthOnHpLoss;
        if (actualDamage > 0 && hpChangePierce > 0) triggerHpChangePierce();
        if (actualDamage > 0) {
            for (Relic relic : relics) {
                if ("strength_on_hit".equals(relic.getEffect().get("type"))) {
                    strength += ((Number) relic.getEffect().getOrDefault("value", 1)).intValue();
                    break;
                }
            }
        }
        return actualDamage;
    }

    public int heal(int amount) {
        if (amount <= 0) return 0;
        int oldHp = hp;
        hp = Math.min(hp + amount, maxHp);
        int actualHeal = hp - oldHp;
        if (actualHeal > 0 && hpChangePierce > 0) triggerHpChangePierce();
        if (actualHeal > 0 && bloodDemon && enemies != null) {
            for (Enemy enemy : enemies) {
                if (enemy.getHp() > 0) enemy.takePenetratingDamage(actualHeal);
            }
        }
        return actualHeal;
    }

    public void applyRelicInstantEffects(Relic relic) {
        String etype = (String) relic.getEffect().get("type");
        if ("instant_gold".equals(etype)) {
            gold += ((Number) relic.getEffect().getOrDefault("value", 0)).intValue();
        } else if ("instant_heal_pct".equals(etype)) {
            int pct = ((Number) relic.getEffect().getOrDefault("value", 50)).intValue();
            heal((int) (maxHp * pct / 100.0));
        } else if ("instant_guard".equals(etype)) {
            int val = ((Number) relic.getEffect().getOrDefault("value", 1)).intValue();
            guard += val;
            baseGuard += val;
        }
    }

    private void triggerHpChangePierce() {
        for (Enemy enemy : enemies) {
            if (enemy.getHp() > 0) enemy.takePenetratingDamage(hpChangePierce);
        }
    }

    private void dealRandomDamage(int damage) {
        List<Enemy> alive = enemies.stream().filter(e -> e.getHp() > 0).collect(Collectors.toList());
        if (!alive.isEmpty()) {
            alive.get(new Random().nextInt(alive.size())).takeDamage(damage);
        }
    }

    private void triggerShieldHero() {
        if (shieldHero) dealRandomDamage(5);
    }

    private void triggerInvadeBody() {
        if (invadeBody) dealRandomDamage(5);
    }

    private void triggerRottenMiasma() {
        if (rottenMiasma) {
            List<Enemy> alive = enemies.stream().filter(e -> e.getHp() > 0).collect(Collectors.toList());
            if (!alive.isEmpty()) {
                Enemy target = alive.get(new Random().nextInt(alive.size()));
                target.addStatus("poison", 3);
                elementalAppliedThisTurn = true;
                triggerInvadeBody();
            }
        }
    }

    private List<Card> combinePiles() {
        List<Card> all = new ArrayList<>();
        all.addAll(hand);
        all.addAll(drawPile);
        all.addAll(discardPile);
        all.addAll(exhaustPile);
        return all;
    }

    public int getCardDynamicDamage(Card card) {
        int damage = card.getDamage();
        if (card.hasEffectType("skill_count_damage")) {
            int skillCount = 0;
            for (Card c : combinePiles()) {
                if (c.getType() == CardType.SKILL) skillCount++;
            }
            damage += skillCount * card.getEffectValue("skill_count_damage", 2);
        }
        if (card.hasEffectType("judgment_damage")) {
            int n = energy;
            damage = 3 + (5 + n) * n;
        }
        if (card.hasEffectType("gold_damage")) {
            int goldPerDmg = card.getEffectValue("gold_damage", 100);
            if (goldPerDmg > 0) damage += gold / goldPerDmg;
        }
        if (!card.hasEffectType("judgment_damage")) {
            damage += strength + tempStrength;
        }
        return damage;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("char_class", charClass.getValue());
        d.put("hp", hp);
        d.put("max_hp", maxHp);
        d.put("gold", gold);
        d.put("level", level);
        d.put("block", block);
        d.put("strength", strength);
        d.put("guard", guard);
        d.put("dexterity", dexterity);
        d.put("hand", hand.stream().map(Card::toMap).collect(Collectors.toList()));
        d.put("draw_pile", drawPile.stream().map(Card::toMap).collect(Collectors.toList()));
        d.put("discard_pile", discardPile.stream().map(Card::toMap).collect(Collectors.toList()));
        d.put("exhaust_pile", exhaustPile.stream().map(Card::toMap).collect(Collectors.toList()));
        d.put("relics", relics.stream().map(Relic::toMap).collect(Collectors.toList()));
        d.put("items", items.stream().map(Item::toMap).collect(Collectors.toList()));
        d.put("max_hand_size", maxHandSize);
        d.put("draw_count", drawCount);
        d.put("energy", energy);
        d.put("max_energy", maxEnergy);
        d.put("status_effects", statusEffects);
        d.put("start_debuff", startDebuff);
        d.put("first_card_played", firstCardPlayed);
        d.put("strength_per_turn", strengthPerTurn);
        d.put("frostbite_per_turn", frostbitePerTurn);
        d.put("next_turn_block", nextTurnBlock);
        d.put("base_strength", baseStrength);
        d.put("base_dexterity", baseDexterity);
        d.put("base_guard", baseGuard);
        d.put("base_draw_count", baseDrawCount);
        d.put("base_max_energy", baseMaxEnergy);
        d.put("strength_on_hp_loss", strengthOnHpLoss);
        d.put("poison_on_unblocked_damage", poisonOnUnblockedDamage);
        d.put("hp_change_pierce", hpChangePierce);
        d.put("block_per_turn", blockPerTurn);
        d.put("blood_burn_round", bloodBurnRound);
        d.put("pending_exhaust", pendingExhaust);
        d.put("holy_energy_per_turn", holyEnergyPerTurn);
        d.put("burn_per_turn", burnPerTurn);
        d.put("frostbite_self_per_turn", frostbiteSelfPerTurn);
        d.put("poison_transfer_on_death", poisonTransferOnDeath);
        d.put("burn_transfer_on_death", burnTransferOnDeath);
        d.put("paralysis_no_decay", paralysisNoDecay);
        d.put("paralysis_per_turn", paralysisPerTurn);
        d.put("draw_on_paralysis", drawOnParalysis);
        return d;
    }

    @SuppressWarnings("unchecked")
    public static Player fromMap(Map<String, Object> data) {
        Player player = new Player(
            CharacterClass.fromValue((String) data.get("char_class")),
            ((Number) data.get("hp")).intValue(),
            ((Number) data.get("max_hp")).intValue(),
            ((Number) data.get("gold")).intValue(),
            ((Number) data.getOrDefault("level", 1)).intValue()
        );
        player.block = ((Number) data.getOrDefault("block", 0)).intValue();
        player.strength = ((Number) data.getOrDefault("strength", 0)).intValue();
        player.guard = ((Number) data.getOrDefault("guard", 0)).intValue();
        player.dexterity = ((Number) data.getOrDefault("dexterity", 0)).intValue();

        List<Map<String, Object>> handList = (List<Map<String, Object>>) data.getOrDefault("hand", new ArrayList<>());
        player.hand = handList.stream().map(Card::fromMap).collect(Collectors.toList());
        List<Map<String, Object>> drawList = (List<Map<String, Object>>) data.getOrDefault("draw_pile", new ArrayList<>());
        player.drawPile = drawList.stream().map(Card::fromMap).collect(Collectors.toList());
        List<Map<String, Object>> discardList = (List<Map<String, Object>>) data.getOrDefault("discard_pile", new ArrayList<>());
        player.discardPile = discardList.stream().map(Card::fromMap).collect(Collectors.toList());
        List<Map<String, Object>> exhaustList = (List<Map<String, Object>>) data.getOrDefault("exhaust_pile", new ArrayList<>());
        player.exhaustPile = exhaustList.stream().map(Card::fromMap).collect(Collectors.toList());
        List<Map<String, Object>> relicList = (List<Map<String, Object>>) data.getOrDefault("relics", new ArrayList<>());
        player.relics = relicList.stream().map(Relic::fromMap).collect(Collectors.toList());
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) data.getOrDefault("items", new ArrayList<>());
        player.items = itemList.stream().map(Item::fromMap).collect(Collectors.toList());

        player.maxHandSize = ((Number) data.getOrDefault("max_hand_size", 10)).intValue();
        player.drawCount = ((Number) data.getOrDefault("draw_count", 5)).intValue();
        player.energy = ((Number) data.getOrDefault("energy", 3)).intValue();
        player.maxEnergy = ((Number) data.getOrDefault("max_energy", 3)).intValue();
        player.statusEffects = (List<Map<String, Object>>) data.getOrDefault("status_effects", new ArrayList<>());
        player.startDebuff = (Boolean) data.getOrDefault("start_debuff", false);
        player.firstCardPlayed = (Boolean) data.getOrDefault("first_card_played", false);
        player.strengthPerTurn = ((Number) data.getOrDefault("strength_per_turn", 0)).intValue();
        player.frostbitePerTurn = ((Number) data.getOrDefault("frostbite_per_turn", 0)).intValue();
        player.nextTurnBlock = ((Number) data.getOrDefault("next_turn_block", 0)).intValue();
        player.baseStrength = ((Number) data.getOrDefault("base_strength", 0)).intValue();
        player.baseDexterity = ((Number) data.getOrDefault("base_dexterity", 0)).intValue();
        player.baseGuard = ((Number) data.getOrDefault("base_guard", 0)).intValue();
        player.baseDrawCount = ((Number) data.getOrDefault("base_draw_count", 0)).intValue();
        player.baseMaxEnergy = ((Number) data.getOrDefault("base_max_energy", 0)).intValue();
        player.strengthOnHpLoss = ((Number) data.getOrDefault("strength_on_hp_loss", 0)).intValue();
        player.poisonOnUnblockedDamage = ((Number) data.getOrDefault("poison_on_unblocked_damage", 0)).intValue();
        player.hpChangePierce = ((Number) data.getOrDefault("hp_change_pierce", 0)).intValue();
        player.blockPerTurn = ((Number) data.getOrDefault("block_per_turn", 0)).intValue();
        player.bloodBurnRound = ((Number) data.getOrDefault("blood_burn_round", 0)).intValue();
        player.pendingExhaust = ((Number) data.getOrDefault("pending_exhaust", 0)).intValue();
        player.holyEnergyPerTurn = ((Number) data.getOrDefault("holy_energy_per_turn", 0)).intValue();
        player.burnPerTurn = ((Number) data.getOrDefault("burn_per_turn", 0)).intValue();
        player.frostbiteSelfPerTurn = ((Number) data.getOrDefault("frostbite_self_per_turn", 0)).intValue();
        player.poisonTransferOnDeath = (Boolean) data.getOrDefault("poison_transfer_on_death", false);
        player.burnTransferOnDeath = (Boolean) data.getOrDefault("burn_transfer_on_death", false);
        player.paralysisNoDecay = (Boolean) data.getOrDefault("paralysis_no_decay", false);
        player.paralysisPerTurn = ((Number) data.getOrDefault("paralysis_per_turn", 0)).intValue();
        player.drawOnParalysis = (Boolean) data.getOrDefault("draw_on_paralysis", false);
        return player;
    }

    // ============== 工具方法（由任务要求添加） ==============

    public void addCard(Card card) {
        drawPile.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
        drawPile.remove(card);
        discardPile.remove(card);
        exhaustPile.remove(card);
    }

    public void shuffleDrawPile() {
        Collections.shuffle(drawPile);
    }

    public void applyBuildStats(Map<String, Integer> alloc) {
        baseStrength += alloc.getOrDefault("strength", 0);
        baseDexterity += alloc.getOrDefault("dexterity", 0);
        baseGuard += alloc.getOrDefault("guard", 0);
        baseDrawCount += alloc.getOrDefault("draw", 0);
        baseMaxEnergy += alloc.getOrDefault("energy", 0);
    }
}