package com.abyss.system;

import com.abyss.state.GameState;
import com.abyss.state.MapNode;
import com.abyss.state.MapNodeType;
import com.abyss.state.GamePhase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * 游戏存档系统 —— 负责 GameState 的 JSON 序列化/反序列化，
 * 以及角色跨 run 进度管理。
 *
 * <p>使用 Jackson ObjectMapper 进行 JSON 序列化，保存到本地文件系统。</p>
 */
public class SaveSystem {

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    /** 运行时存档文件名（当前 run 存档） */
    private static final String SAVE_FILE_NAME = "save_data.json";

    /** 角色进度文件名（跨 run 持久化） */
    private static final String CHARACTER_PROGRESS_FILE = "character_progress.json";

    /** 存档目录，默认在当前工作目录下 */
    private static String saveDirectory = ".";

    /**
     * 设置存档目录。
     *
     * @param dir 存档目录路径
     */
    public static void setSaveDirectory(String dir) {
        saveDirectory = dir;
    }

    /**
     * 获取存档目录。
     *
     * @return 存档目录路径
     */
    public static String getSaveDirectory() {
        return saveDirectory;
    }

    /**
     * 获取运行时存档文件路径。
     *
     * @return 存档文件绝对路径
     */
    private static String getSaveFilePath() {
        return Paths.get(saveDirectory, SAVE_FILE_NAME).toString();
    }

    /**
     * 获取角色进度文件路径。
     *
     * @return 角色进度文件绝对路径
     */
    private static String getCharacterProgressPath() {
        return Paths.get(saveDirectory, CHARACTER_PROGRESS_FILE).toString();
    }

    // ================================================================
    //  当前 run 存档：save / load / has_save
    // ================================================================

    /**
     * 把当前 GameState 序列化到 save_data.json。
     *
     * @param state GameState 实例
     * @return true 保存成功，false 失败
     */
    public static boolean saveGame(GameState state) {
        try {
            // 构建可序列化的数据结构
            Map<String, Object> saveData = new LinkedHashMap<>();
            saveData.put("phase", state.phase.getValue());
            saveData.put("turn", state.turn);
            saveData.put("currentFloor", state.currentFloor);
            saveData.put("maxFloors", state.maxFloors);
            saveData.put("gameMode", state.gameMode);
            saveData.put("combatLog", state.combatLog);
            saveData.put("rewardGold", state.rewardGold);
            saveData.put("rewardSelectedCard", state.rewardSelectedCard);
            saveData.put("rewardRelicTaken", state.rewardRelicTaken);
            saveData.put("elitesUsed", state.elitesUsed);
            saveData.put("shopDeleteMode", state.shopDeleteMode);
            saveData.put("cardDeleteCount", state.cardDeleteCount);

            // 地图节点序列化
            saveData.put("mapNodes", state.mapNodes.stream().map(MapNode::toMap).toList());

            // 玩家序列化
            saveData.put("player", state.player != null ? state.player.toMap() : null);

            // 敌人序列化
            List<Map<String, Object>> enemiesData = new ArrayList<>();
            for (Object e : state.enemies) {
                if (e instanceof Map) {
                    enemiesData.add((Map<String, Object>) e);
                } else {
                    // 待 Enemy 类实现 toMap
                    Map<String, Object> ed = new LinkedHashMap<>();
                    ed.put("serialized", e != null ? e.toString() : null);
                    enemiesData.add(ed);
                }
            }
            saveData.put("enemies", enemiesData);

            // 奖励卡牌序列化
            List<Map<String, Object>> rewardCardsData = new ArrayList<>();
            for (Object c : state.rewardCards) {
                if (c instanceof Map) {
                    rewardCardsData.add((Map<String, Object>) c);
                } else {
                    Map<String, Object> cd = new LinkedHashMap<>();
                    cd.put("serialized", c != null ? c.toString() : null);
                    rewardCardsData.add(cd);
                }
            }
            saveData.put("rewardCards", rewardCardsData);

            // 奖励遗物序列化
            saveData.put("rewardRelic", state.rewardRelic != null ? state.rewardRelic : null);

            // 商店物品序列化
            List<Map<String, Object>> shopItemsData = new ArrayList<>();
            for (Map<String, Object> item : state.shopItems) {
                shopItemsData.add(serializeShopItem(item));
            }
            saveData.put("shopItems", shopItemsData);

            // 写入文件
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(
                new File(getSaveFilePath()), saveData);
            return true;
        } catch (Exception e) {
            System.err.println("存档保存失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 从 save_data.json 读取存档。
     *
     * @return 字典结构（与 saveGame 写入的结构一致），失败时返回 null
     */
    public static Map<String, Object> loadGame() {
        try {
            File file = new File(getSaveFilePath());
            if (!file.exists()) {
                return null;
            }
            return MAPPER.readValue(file, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            System.err.println("存档加载失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 从 save_data.json 读取存档并还原为 GameState 实例。
     *
     * @return GameState 实例，失败时返回 null
     */
    public static GameState loadGameState() {
        Map<String, Object> data = loadGame();
        if (data == null) {
            return null;
        }
        return GameState.fromMap(data);
    }

    /**
     * 是否存在存档文件。
     *
     * @return true 存在存档，false 不存在
     */
    public static boolean hasSave() {
        return new File(getSaveFilePath()).exists();
    }

    // ================================================================
    //  角色进度（跨 run 持久化）
    // ================================================================

    /**
     * 读取所有角色的等级与经验。
     *
     * <p>不存在或解析失败时返回三个职业的默认初始数据（Lv.1, 0 exp）。</p>
     *
     * @return 角色进度字典，格式：{ "warrior": {"level": 1, "exp": 0}, ... }
     */
    public static Map<String, Map<String, Integer>> loadCharacterProgress() {
        try {
            File file = new File(getCharacterProgressPath());
            if (!file.exists()) {
                return getDefaultCharacterProgress();
            }
            return MAPPER.readValue(file, new TypeReference<Map<String, Map<String, Integer>>>() {});
        } catch (Exception e) {
            System.err.println("角色进度加载失败: " + e.getMessage());
            return getDefaultCharacterProgress();
        }
    }

    /**
     * 保存所有角色的等级与经验。
     *
     * @param progress 角色进度字典，键为职业 value，值为 {"level": int, "exp": int}
     * @return true 保存成功，false 失败
     */
    public static boolean saveCharacterProgress(Map<String, Map<String, Integer>> progress) {
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(
                new File(getCharacterProgressPath()), progress);
            return true;
        } catch (Exception e) {
            System.err.println("角色进度保存失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取默认角色进度数据。
     *
     * @return 默认角色进度字典
     */
    private static Map<String, Map<String, Integer>> getDefaultCharacterProgress() {
        Map<String, Map<String, Integer>> defaults = new LinkedHashMap<>();
        for (String cls : new String[]{"warrior", "mage", "rogue"}) {
            Map<String, Integer> data = new LinkedHashMap<>();
            data.put("level", 1);
            data.put("exp", 0);
            defaults.put(cls, data);
        }
        return defaults;
    }

    /**
     * 为指定职业累加经验并处理升级（最高 10 级）。
     *
     * @param charClass 职业 value，例如 "warrior"、"mage"、"rogue"
     * @param gainedExp 本次获得的经验（正整数）
     * @return 包含 leveledUp, newLevel, remainingExp 的结果 Map
     */
    public static Map<String, Object> addExpToCharacter(String charClass, int gainedExp) {
        Map<String, Map<String, Integer>> progress = loadCharacterProgress();
        Map<String, Integer> current = progress.getOrDefault(charClass,
            new LinkedHashMap<>(Map.of("level", 1, "exp", 0)));

        int currentLevel = current.getOrDefault("level", 1);
        int currentExp = current.getOrDefault("exp", 0) + gainedExp;

        boolean leveledUp = false;
        while (currentLevel < 10) {
            int needed = getExpForLevel(currentLevel);
            if (currentExp >= needed) {
                currentExp -= needed;
                currentLevel++;
                leveledUp = true;
            } else {
                break;
            }
        }

        Map<String, Integer> updated = new LinkedHashMap<>();
        updated.put("level", currentLevel);
        updated.put("exp", currentExp);
        progress.put(charClass, updated);
        saveCharacterProgress(progress);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("leveledUp", leveledUp);
        result.put("newLevel", currentLevel);
        result.put("remainingExp", currentExp);
        return result;
    }

    /**
     * 获取升到下一级所需经验。
     *
     * <p>经验公式：2000 + 当前等级 × 200</p>
     *
     * @param level 当前等级
     * @return 升到 level+1 所需经验值
     */
    public static int getExpForLevel(int level) {
        return 2000 + level * 200;
    }

    // ================================================================
    //  商店物品序列化辅助方法
    // ================================================================

    /**
     * 序列化单个商店商品（卡牌/遗物/道具）。
     *
     * @param item 商店商品字典
     * @return 可 JSON 序列化的字典
     */
    private static Map<String, Object> serializeShopItem(Map<String, Object> item) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("price", item.getOrDefault("price", 0));
        result.put("sold", item.getOrDefault("sold", false));

        if (item.containsKey("card")) {
            result.put("type", "card");
            Object card = item.get("card");
            if (card instanceof Map) {
                result.put("card", card);
            } else {
                Map<String, Object> cardData = new LinkedHashMap<>();
                cardData.put("serialized", card != null ? card.toString() : null);
                result.put("card", cardData);
            }
        } else if (item.containsKey("relic")) {
            result.put("type", "relic");
            Object relic = item.get("relic");
            if (relic instanceof Map) {
                result.put("relic", relic);
            } else {
                Map<String, Object> relicData = new LinkedHashMap<>();
                relicData.put("serialized", relic != null ? relic.toString() : null);
                result.put("relic", relicData);
            }
        } else if (item.containsKey("item")) {
            result.put("type", "item");
            Object itm = item.get("item");
            if (itm instanceof Map) {
                result.put("item", itm);
            } else {
                Map<String, Object> itemData = new LinkedHashMap<>();
                itemData.put("serialized", itm != null ? itm.toString() : null);
                result.put("item", itemData);
            }
        }
        return result;
    }
}