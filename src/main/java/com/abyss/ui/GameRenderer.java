package com.abyss.ui;

import com.abyss.constants.Constants;
import com.abyss.model.*;
import com.abyss.state.GamePhase;
import com.abyss.state.GameState;
import com.abyss.state.MapNode;
import com.abyss.state.MapNodeType;
import com.abyss.system.CardFactory;
import com.abyss.system.LangManager;
import com.abyss.system.ResourceManager;
import com.abyss.system.SaveSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.*;
import java.util.List;

/**
 * JavaFX UI 渲染类 —— 游戏全部 UI 绘制函数。
 * <p>
 * 使用 GraphicsContext 进行绘制，所有坐标/尺寸通过 Constants.rx/ry/rs 从基准分辨率缩放。
 * 颜色使用 Constants 中的颜色常量。
 * <p>
 * 对应 Python 版 ui_render.py、shop.py、encyclopedia.py、tutorial.py 的绘制函数。
 */
public final class GameRenderer {

    private GameRenderer() {
        // 工具类，禁止实例化
    }

    // ================================================================
    //  卡牌稀有度边框颜色
    // ================================================================
    private static final Map<CardRarity, Color> RARITY_BORDER_COLORS = Map.of(
            CardRarity.COMMON, Constants.WHITE,
            CardRarity.UNCOMMON, Constants.BLUE,
            CardRarity.RARE, Constants.GOLD,
            CardRarity.LEGENDARY, Constants.RED
    );

    // ================================================================
    //  卡牌类型颜色
    // ================================================================
    private static final Map<CardType, Color> TYPE_COLORS = Map.of(
            CardType.ATTACK, Constants.RED,
            CardType.SKILL, Constants.BLUE,
            CardType.POWER, Constants.PURPLE,
            CardType.CURSE, Color.rgb(100, 0, 100)
    );

    // ================================================================
    //  卡牌类型中文名
    // ================================================================
    private static final Map<CardType, String> TYPE_NAMES = Map.of(
            CardType.ATTACK, "攻击",
            CardType.SKILL, "技能",
            CardType.POWER, "能力",
            CardType.CURSE, "诅咒"
    );

    // ================================================================
    //  敌人状态效果中文名与颜色
    // ================================================================
    private static final Map<String, Object[]> ENEMY_STATUS_NAMES = Map.ofEntries(
            Map.entry("vulnerable", new Object[]{"易伤", Color.rgb(255, 100, 100)}),
            Map.entry("weak", new Object[]{"虚弱", Color.rgb(150, 150, 150)}),
            Map.entry("poison", new Object[]{"中毒", Color.rgb(100, 200, 100)}),
            Map.entry("burn", new Object[]{"灼烧", Constants.RED}),
            Map.entry("phasing", new Object[]{"虚化", Constants.GOLD}),
            Map.entry("fragile", new Object[]{"脆弱", Color.rgb(200, 150, 100)}),
            Map.entry("frostbite", new Object[]{"冻伤", Color.rgb(100, 200, 255)}),
            Map.entry("paralysis", new Object[]{"麻痹", Color.rgb(255, 255, 100)}),
            Map.entry("slow", new Object[]{"减速", Constants.PURPLE}),
            Map.entry("bleed", new Object[]{"流血", Color.rgb(220, 50, 50)})
    );

    // ================================================================
    //  敌人意图背景色
    // ================================================================
    private static final Map<EnemyIntent, Color> ENEMY_INTENT_COLORS = Map.of(
            EnemyIntent.ATTACK, Constants.RED,
            EnemyIntent.DEFEND, Constants.BLUE,
            EnemyIntent.BUFF, Color.rgb(120, 90, 30),
            EnemyIntent.DEBUFF, Constants.PURPLE
    );

    // ================================================================
    //  敌人意图文字色
    // ================================================================
    private static final Map<EnemyIntent, Color> ENEMY_INTENT_TEXT_COLORS = Map.of(
            EnemyIntent.ATTACK, Constants.WHITE,
            EnemyIntent.DEFEND, Constants.WHITE,
            EnemyIntent.BUFF, Color.rgb(255, 240, 180),
            EnemyIntent.DEBUFF, Constants.WHITE
    );

    // ================================================================
    //  敌人 buff 属性列表
    // ================================================================
    private static final List<Object[]> ENEMY_BUFF_ATTRS = List.of(
            new Object[]{"strength", "力量", Constants.RED},
            new Object[]{"guard", "守护", Constants.BLUE},
            new Object[]{"dexterity", "敏捷", Constants.GREEN},
            new Object[]{"increaseDamage", "增伤", Constants.GOLD}
    );

    // ================================================================
    //  玩家状态标签
    // ================================================================
    private static final Map<String, Object[]> PLAYER_STATUS_LABELS = Map.ofEntries(
            Map.entry("phasing", new Object[]{"虚化", Constants.GOLD}),
            Map.entry("dodge", new Object[]{"闪避", Constants.BLUE}),
            Map.entry("increaseDamage", new Object[]{"增伤", Constants.GOLD}),
            Map.entry("vulnerable", new Object[]{"易伤", Constants.RED}),
            Map.entry("weak", new Object[]{"虚弱", Constants.GRAY}),
            Map.entry("paralysis", new Object[]{"麻痹", Color.rgb(255, 255, 100)}),
            Map.entry("frostbite", new Object[]{"冻伤", Color.rgb(100, 200, 255)}),
            Map.entry("skip_turn", new Object[]{"跳过", Constants.GRAY}),
            Map.entry("lurk", new Object[]{"潜伏", Color.rgb(100, 200, 150)}),
            Map.entry("assassinate", new Object[]{"刺杀", Color.rgb(220, 50, 50)})
    );

    // ================================================================
    //  Boss 特性提示文本
    // ================================================================
    private static final Map<String, String> BOSS_ABILITY_TEXT = Map.of(
            "dragon_breathe", "Boss 特性: 龙息 - 每2回合 -1 力量，3穿甲伤害",
            "lich_drain", "Boss 特性: 巫妖 - 每回合恢复5 HP，每2回合+1力量",
            "dark_curse", "Boss 特性: 暗黑诅咒 - 能量上限 -1，持续虚弱(伤害减半)",
            "machine_suppress", "Boss 特性: 机关压制 - 抽牌-2，持续易伤"
    );

    // ================================================================
    //  地图节点颜色
    // ================================================================
    private static final Map<MapNodeType, Color> NODE_COLORS = Map.of(
            MapNodeType.COMBAT, Constants.RED,
            MapNodeType.ELITE, Color.rgb(255, 165, 0),
            MapNodeType.SHOP, Constants.GOLD,
            MapNodeType.REST, Constants.GREEN,
            MapNodeType.EVENT, Constants.PURPLE,
            MapNodeType.BOSS, Color.rgb(150, 0, 0),
            MapNodeType.TREASURE, Constants.BLUE,
            MapNodeType.OPPORTUNITY, Color.rgb(150, 50, 255)
    );

    // ================================================================
    //  地图节点图标
    // ================================================================
    private static final Map<MapNodeType, String> NODE_ICONS = Map.of(
            MapNodeType.COMBAT, "战",
            MapNodeType.ELITE, "★",
            MapNodeType.SHOP, "$",
            MapNodeType.REST, "心",
            MapNodeType.EVENT, "?",
            MapNodeType.BOSS, "王",
            MapNodeType.TREASURE, "宝",
            MapNodeType.OPPORTUNITY, "?"
    );

    // ================================================================
    //  地图节点名称
    // ================================================================
    private static final Map<MapNodeType, String> NODE_NAMES = Map.of(
            MapNodeType.COMBAT, "战斗",
            MapNodeType.ELITE, "精英战",
            MapNodeType.SHOP, "商店",
            MapNodeType.REST, "休息",
            MapNodeType.EVENT, "事件",
            MapNodeType.BOSS, "Boss战",
            MapNodeType.TREASURE, "宝箱",
            MapNodeType.OPPORTUNITY, "机遇"
    );

    // ================================================================
    //  地图节点描述
    // ================================================================
    private static final Map<MapNodeType, String> NODE_DESC = Map.of(
            MapNodeType.COMBAT, "击败敌人获得金币和卡牌",
            MapNodeType.ELITE, "高难度战斗，奖励更丰厚",
            MapNodeType.SHOP, "花费金币购买卡牌和遗物",
            MapNodeType.REST, "恢复50%最大生命值",
            MapNodeType.EVENT, "随机事件，机遇与挑战并存",
            MapNodeType.BOSS, "强大的Boss战，通关关键",
            MapNodeType.TREASURE, "获得额外奖励",
            MapNodeType.OPPORTUNITY, "随机事件，机遇与挑战并存"
    );

    // ================================================================
    //  角色选择数据
    // ================================================================
    private static final CharacterClass[] SELECTABLE_CLASSES = {
            CharacterClass.WARRIOR, CharacterClass.MAGE, CharacterClass.ROGUE, CharacterClass.PRIEST
    };

    // ================================================================
    //  绘制工具方法
    // ================================================================

    /**
     * 设置字体到 GraphicsContext。
     */
    private static void setFont(GraphicsContext gc, double fontSize) {
        gc.setFont(Font.font(ResourceManager.CUSTOM_FONT_NAME, FontWeight.NORMAL, Constants.rs(fontSize)));
    }

    /**
     * 绘制半透明填充矩形。
     */
    private static void fillAlphaRect(GraphicsContext gc, double x, double y, double w, double h, Color color) {
        gc.setGlobalAlpha(1.0);
        gc.setFill(color);
        gc.fillRect(Constants.rx(x), Constants.ry(y), Constants.rs(w), Constants.rs(h));
    }

    /**
     * 绘制填充矩形（带边框）。
     */
    private static void fillRoundRect(GraphicsContext gc, double x, double y, double w, double h,
                                      Color fill, Color border, double borderWidth, double radius) {
        double sx = Constants.rx(x);
        double sy = Constants.ry(y);
        double sw = Constants.rs(w);
        double sh = Constants.rs(h);
        double sr = Constants.rs(radius);
        if (fill != null) {
            gc.setFill(fill);
            gc.fillRoundRect(sx, sy, sw, sh, sr, sr);
        }
        if (border != null && borderWidth > 0) {
            gc.setStroke(border);
            gc.setLineWidth(Constants.rs(borderWidth));
            gc.strokeRoundRect(sx, sy, sw, sh, sr, sr);
        }
    }

    /**
     * 绘制填充矩形。
     */
    private static void fillRect(GraphicsContext gc, double x, double y, double w, double h, Color color) {
        gc.setFill(color);
        gc.fillRect(Constants.rx(x), Constants.ry(y), Constants.rs(w), Constants.rs(h));
    }

    /**
     * 绘制矩形边框。
     */
    private static void strokeRect(GraphicsContext gc, double x, double y, double w, double h,
                                   Color color, double lineWidth) {
        gc.setStroke(color);
        gc.setLineWidth(Constants.rs(lineWidth));
        gc.strokeRect(Constants.rx(x), Constants.ry(y), Constants.rs(w), Constants.rs(h));
    }

    // ================================================================
    //  1. drawText — 绘制文本
    // ================================================================

    /**
     * 获取卡牌中文名称（通过 LangManager 翻译）。
     */
    private static String getCardName(Card card) {
        return LangManager.getInstance().getText("cards." + card.getNameKey(), card.getNameKey());
    }

    /**
     * 获取敌人中文名称（通过 LangManager 翻译）。
     */
    private static String getEnemyName(Enemy enemy) {
        return LangManager.getInstance().getText("enemies." + enemy.getNameKey(), enemy.getNameKey());
    }

    /**
     * 获取遗物中文名称（通过 LangManager 翻译）。
     */
    private static String getRelicName(Relic relic) {
        return LangManager.getInstance().getText("relics." + relic.getNameKey(), relic.getNameKey());
    }

    /**
     * 获取遗物中文描述（通过 LangManager 翻译）。
     */
    private static String getRelicDesc(Relic relic) {
        return LangManager.getInstance().getText("relics." + relic.getNameKey() + ".desc", relic.getDescKey());
    }

    /**
     * 绘制单行文本。
     *
     * @param gc       GraphicsContext
     * @param text     文本内容
     * @param x        基准 x 坐标（左上角或中心）
     * @param y        基准 y 坐标（左上角或中心）
     * @param color    文字颜色
     * @param fontSize 字号（基准字号，内部自动缩放）
     */
    public static void drawText(GraphicsContext gc, String text, double x, double y,
                                Color color, double fontSize) {
        drawText(gc, text, x, y, color, fontSize, false);
    }

    /** 字体度量缓存：fontSize → [baselineOffset, textHeight] 的缩放后值 */
    private static final Map<Double, double[]> fontMetricsCache = new HashMap<>();

    /**
     * 获取缩放后字体的度量（baselineOffset, textHeight）。
     * <p>
     * JavaFX fillText 的 y 坐标是基线位置，而调用方通常传入的是文字顶部(y=top)。
     * 此方法测量实际字体的基线偏移和总高度，用于正确计算填充位置。
     * 使用包含中文的字符串测量，保证黑体中文字符的度量准确。
     */
    private static double[] getFontMetrics(double actualSize) {
        double[] cached = fontMetricsCache.get(actualSize);
        if (cached != null) return cached;
        // 用包含中文的 Text 节点测量实际字体度量（SimHei 是中文字体，中文测量更准确）
        javafx.scene.text.Text measurer = new javafx.scene.text.Text("黑Ay");
        measurer.setFont(Font.font(ResourceManager.CUSTOM_FONT_NAME, actualSize));
        double baselineOffset = measurer.getBaselineOffset();
        double textHeight = measurer.getLayoutBounds().getHeight();
        double[] metrics = new double[]{baselineOffset, textHeight};
        fontMetricsCache.put(actualSize, metrics);
        return metrics;
    }

    /**
     * 绘制单行文本（支持居中）。
     * <p>
     * 注：JavaFX fillText 的 y 坐标对应基线(baseline)，而非文字顶部。
     * 语义与 Python 版本一致：
     * <ul>
     * <li>{@code center=false}: (x, y) 是文字左上角，x 左对齐，y 文字顶部</li>
     * <li>{@code center=true}: x 是文字水平中心点（水平居中），y 仍然是文字顶部</li>
     * </ul>
     * 垂直居中需要调用方手动计算 y = 容器顶部 + 容器高度/2 - 文字高度/2。
     *
     * @param gc       GraphicsContext
     * @param text     文本内容
     * @param x        基准 x 坐标（左对齐：文字左边缘；center=true：文字水平中心）
     * @param y        基准 y 坐标（文字顶部，无论 center 是否为 true）
     * @param color    文字颜色
     * @param fontSize 字号（基准字号，内部自动缩放）
     * @param center   是否水平居中对齐（x 为中心点），y 始终为文字顶部
     */
    public static void drawText(GraphicsContext gc, String text, double x, double y,
                                Color color, double fontSize, boolean center) {
        double actualSize = Constants.rs(fontSize);
        gc.setFont(Font.font(ResourceManager.CUSTOM_FONT_NAME, actualSize));
        gc.setFill(color);
        double sx = Constants.rx(x);
        double sy = Constants.ry(y);
        double[] metrics = getFontMetrics(actualSize);
        double baselineOffset = metrics[0];
        if (center) {
            // 水平居中：x 是文字中心点，y 仍然是文字顶部
            gc.setTextAlign(TextAlignment.CENTER);
            // 文字顶部在 sy → 基线在 sy + baselineOffset
            gc.fillText(text, sx, sy + baselineOffset);
            gc.setTextAlign(TextAlignment.LEFT);
        } else {
            // 左对齐：文字顶部在 sy → 基线在 sy + baselineOffset
            gc.fillText(text, sx, sy + baselineOffset);
        }
    }

    // ================================================================
    //  2. drawCard — 绘制单张卡牌
    // ================================================================

    /**
     * 绘制一张卡牌。
     * <p>
     * 包含：费用标签、名称、类型色条、伤害/格挡/治疗数值、效果文本、稀有度边框、选中/禁用状态。
     *
     * @param gc       GraphicsContext
     * @param card     Card 实例
     * @param x        基准 x 坐标（左上角）
     * @param y        基准 y 坐标（左上角）
     * @param width    卡牌宽度
     * @param height   卡牌高度
     * @param selected 是否选中
     * @param disabled 是否禁用
     */
    public static void drawCard(GraphicsContext gc, Card card, double x, double y,
                                double width, double height, boolean selected, boolean disabled) {
        double sx = Constants.rx(x);
        double sy = Constants.ry(y);
        double sw = Constants.rs(width);
        double sh = Constants.rs(height);

        // 选中状态：外围加粗稀有度色框
        if (selected) {
            Color borderColor = RARITY_BORDER_COLORS.getOrDefault(card.getRarity(), Constants.WHITE);
            gc.setStroke(borderColor);
            gc.setLineWidth(Constants.rs(3));
            gc.strokeRect(sx - Constants.rs(5), sy - Constants.rs(5),
                    sw + Constants.rs(10), sh + Constants.rs(10));
        }

        // 绘制卡牌主体
        drawCardCommon(gc, card, x, y, width, height, true);

        // 禁用状态：半透明灰色遮罩
        if (disabled) {
            gc.setGlobalAlpha(0.5);
            gc.setFill(Color.rgb(60, 60, 60));
            gc.fillRect(sx, sy, sw, sh);
            gc.setGlobalAlpha(1.0);
        }
    }

    /**
     * 通用卡牌绘制函数（内部调用）。
     */
    private static void drawCardCommon(GraphicsContext gc, Card card, double x, double y,
                                       double width, double height, boolean showCost) {
        double sx = Constants.rx(x);
        double sy = Constants.ry(y);
        double sw = Constants.rs(width);
        double sh = Constants.rs(height);

        // 卡牌图片作为背景（覆盖整个卡牌区域）
        String cardImgName = card.getNameKey() + ".png";
        Image cardImg = ResourceManager.get().loadImage(cardImgName);
        if (cardImg == null) {
            // 尝试加载 jpg 格式
            cardImg = ResourceManager.get().loadImage(card.getNameKey() + ".jpg");
        }
        if (cardImg == null) {
            // 使用默认卡牌背景
            cardImg = ResourceManager.get().loadImage("card_back.jpg");
        }
        if (cardImg != null) {
            gc.drawImage(cardImg, sx, sy, sw, sh);
        } else {
            // 最终备用：纯色背景
            gc.setFill(Constants.DARK_PURPLE);
            gc.fillRect(sx, sy, sw, sh);
        }

        // 顶部信息：费用圆
        double yOffset = sy + Constants.rs(3);
        if (showCost && card.getType() != CardType.CURSE) {
            double cx = sx + Constants.rs(14);
            double cy = yOffset + Constants.rs(11);
            double cr = Constants.rs(11);
            gc.setFill(Constants.BLACK);
            gc.fillOval(cx - cr, cy - cr, cr * 2, cr * 2);
            gc.setStroke(Constants.GOLD);
            gc.setLineWidth(Constants.rs(2));
            gc.strokeOval(cx - cr, cy - cr, cr * 2, cr * 2);
            String costText = card.getCost() == 0 ? "X" : String.valueOf(card.getCost());
            drawText(gc, costText, x + 14, y + 4, Constants.GOLD, 15, true);
        }

        // 右上角：类型小字
        String typeText = TYPE_NAMES.getOrDefault(card.getType(), "");
        double typeX = x + width - 18;
        // 类型背景条
        fillRect(gc, typeX - 14, y + 3, 28, 16, Constants.DARK_PURPLE);
        drawText(gc, typeText, typeX, y + 5, Color.rgb(230, 230, 230), 11, true);

        yOffset += Constants.rs(25);

        // 名称行
        fillRect(gc, x + 3, y + 25, width - 6, 24, Color.rgb(30, 20, 40));
        Color nameColor = RARITY_BORDER_COLORS.getOrDefault(card.getRarity(), Constants.WHITE);
        drawText(gc, getCardName(card), x + width / 2, y + 30, nameColor, 16, true);
        yOffset += Constants.rs(28);

        // 类型色条
        Color typeColor = TYPE_COLORS.getOrDefault(card.getType(), Constants.PURPLE);
        fillRect(gc, x + 2, y + 53, width - 4, 3, typeColor);
        yOffset += Constants.rs(8);

        // 属性数值（动态偏移）
        int statLine = 0;
        if (card.getDamage() > 0) {
            drawText(gc, card.getDamage() + " 伤害", x + 10, y + 61 + statLine * 20, Constants.RED, 14);
            statLine++;
        }
        if (card.getPenetratingDamage() > 0) {
            drawText(gc, card.getPenetratingDamage() + " 穿透", x + 10, y + 61 + statLine * 20,
                    Color.rgb(255, 140, 0), 14);
            statLine++;
        }
        if (card.getBlock() > 0) {
            drawText(gc, card.getBlock() + " 格挡", x + 10, y + 61 + statLine * 20,
                    Constants.BLUE, 14);
            statLine++;
        }
        if (card.getHeal() > 0) {
            drawText(gc, card.getHeal() + " 治疗", x + 10, y + 61 + statLine * 20,
                    Constants.GREEN, 14);
            statLine++;
        }

        // 效果文本（紧跟在属性数值下方）
        if (card.getEffect() != null) {
            String effectText = getEffectText(card.getEffect());
            if (effectText != null && !effectText.isEmpty()) {
                drawText(gc, effectText, x + 10, y + 61 + statLine * 20, Constants.GOLD, 12);
            }
        }

        // 边框
        Color borderColor = RARITY_BORDER_COLORS.getOrDefault(card.getRarity(), Constants.WHITE);
        gc.setStroke(borderColor);
        gc.setLineWidth(Constants.rs(2));
        gc.strokeRect(sx, sy, sw, sh);
    }

    /**
     * 从卡牌效果对象中提取中文效果文本（通过 LangManager 翻译）。
     */
    @SuppressWarnings("unchecked")
    private static String getEffectText(Object effect) {
        if (effect == null) return "";
        if (effect instanceof String s) return s;
        LangManager lang = LangManager.getInstance();
        if (effect instanceof Map<?, ?> m) {
            Map<String, Object> map = (Map<String, Object>) m;
            String type = (String) map.get("type");
            if (type == null) return "";
            // 从 lang.json 获取效果描述模板（card_effects.xxx）
            String template = lang.getText("card_effects." + type);
            if (template == null || template.isEmpty()) {
                // 没有翻译，返回英文类型名
                Object value = map.get("value");
                return value != null ? type + " " + value : type;
            }
            // 替换 {value} 占位符
            Object value = map.get("value");
            if (value != null && template.contains("{value}")) {
                String valueStr = String.valueOf(value);
                // 处理特殊格式如 {value:+d}（显示正负号）
                template = template.replace("{value:+d}", valueStr.startsWith("-") ? valueStr : "+" + valueStr);
                template = template.replace("{value}", valueStr);
            }
            // 替换其他特殊占位符（如 {base}, {multiplier}, {extra} 等）
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                if (!"type".equals(key) && !"value".equals(key)) {
                    String placeholder = "{" + key + "}";
                    if (template.contains(placeholder)) {
                        template = template.replace(placeholder, String.valueOf(entry.getValue()));
                    }
                }
            }
            return template;
        }
        if (effect instanceof List<?> list) {
            StringBuilder sb = new StringBuilder();
            for (Object item : list) {
                if (sb.length() > 0) sb.append("，");
                sb.append(getEffectText(item));
            }
            return sb.toString();
        }
        return effect.toString();
    }

    // ================================================================
    //  3. drawPlayerInfo — 绘制玩家信息面板
    // ================================================================

    /**
     * 绘制玩家信息面板。
     * <p>
     * 包含：角色名+等级、血条、格挡值、金币、状态增益（力量/守护/敏捷/虚化/闪避/增伤等）。
     *
     * @param gc     GraphicsContext
     * @param player Player 实例
     * @param x      面板左上角基准 x
     * @param y      面板左上角基准 y
     * @param width  面板宽度
     * @param height 面板高度
     */
    public static void drawPlayerInfo(GraphicsContext gc, Player player, double x, double y,
                                      double width, double height) {
        // 背景
        fillRect(gc, x, y, width, height, Constants.DARK_PURPLE);
        strokeRect(gc, x, y, width, height, Constants.WHITE, 2);

        // 角色名 + 等级
        String levelText = player.getLevel() < 10 ? "Lv." + player.getLevel() : "Lv.MAX";
        drawText(gc, player.getCharClass().getValue() + " [" + levelText + "]", x + width / 2, y + 10,
                Constants.WHITE, 18, true);

        // 血条
        double hpPercent = (double) player.getHp() / Math.max(1, player.getMaxHp());
        double hpBarWidth = width - 40;
        double hpBarHeight = 20;
        fillRect(gc, x + 20, y + 45, hpBarWidth, hpBarHeight, Constants.BLACK);
        Color hpColor = hpPercent > 0.3 ? Constants.GREEN : Constants.RED;
        fillRect(gc, x + 20, y + 45, hpBarWidth * hpPercent, hpBarHeight, hpColor);
        drawText(gc, player.getHp() + "/" + player.getMaxHp() + " 生命", x + width / 2, y + 70,
                Constants.WHITE, 14, true);

        // 格挡
        if (player.getBlock() > 0) {
            drawText(gc, "格挡: " + player.getBlock(), x + width / 2, y + 95, Constants.BLUE, 14, true);
        }

        // 金币
        drawText(gc, "金币: " + player.getGold(), x + width / 2, y + 120, Constants.GOLD, 14, true);

        // 状态增益（两列布局）
        double sy = y + 140;
        double sx = x + 10;

        if (player.getStrength() != 0) {
            drawText(gc, "力量:" + (player.getStrength() > 0 ? "+" : "") + player.getStrength(),
                    sx, sy, Constants.RED, 12);
            sx += 55;
        }
        if (player.getTempStrength() > 0) {
            drawText(gc, "临力:" + player.getTempStrength(), sx, sy, Color.rgb(255, 150, 80), 12);
            sx += 55;
        }
        if (player.getGuard() > 0) {
            drawText(gc, "守护:" + player.getGuard(), sx, sy, Constants.GREEN, 12);
            sx += 55;
        }
        if (player.getDexterity() > 0) {
            drawText(gc, "敏捷:" + player.getDexterity(), sx, sy, Constants.BLUE, 12);
            sx += 55;
        }

        // 状态效果
        if (player.getStatusEffects() != null) {
            for (Map<String, Object> status : player.getStatusEffects()) {
                String stype = (String) status.get("type");
                int value = ((Number) status.get("value")).intValue();
                if (value > 0 && PLAYER_STATUS_LABELS.containsKey(stype)) {
                    Object[] labelInfo = PLAYER_STATUS_LABELS.get(stype);
                    drawText(gc, labelInfo[0] + ":" + value, sx, sy, (Color) labelInfo[1], 12);
                    sx += 55;
                }
            }
        }
    }

    // ================================================================
    //  4. drawEnemy — 绘制敌人
    // ================================================================

    /**
     * 绘制单个敌人信息面板。
     * <p>
     * 包含：名称、血条、护盾条、意图、状态效果、buff 属性和金币掉落。
     *
     * @param gc     GraphicsContext
     * @param enemy  Enemy 实例
     * @param x      面板左上角基准 x
     * @param y      面板左上角基准 y
     * @param width  面板宽度
     * @param height 面板高度
     * @param hoverX 鼠标悬停 x（逻辑坐标），用于高亮
     * @param hoverY 鼠标悬停 y（逻辑坐标）
     */
    public static void drawEnemy(GraphicsContext gc, Enemy enemy, double x, double y,
                                 double width, double height, double hoverX, double hoverY) {
        double sx = Constants.rx(x);
        double sy = Constants.ry(y);
        double sw = Constants.rs(width);
        double sh = Constants.rs(height);

        // 背景色：Boss 深红 / 精英 橙 / 普通 暗紫
        Color bgColor;
        if (enemy.isBoss()) {
            bgColor = Color.rgb(150, 20, 20);
        } else if (enemy.isElite()) {
            bgColor = Color.rgb(150, 100, 20);
        } else {
            bgColor = Constants.DARK_PURPLE;
        }
        gc.setFill(bgColor);
        gc.fillRect(sx, sy, sw, sh);
        gc.setStroke(Constants.WHITE);
        gc.setLineWidth(Constants.rs(2));
        gc.strokeRect(sx, sy, sw, sh);

        // 名称
        drawText(gc, getEnemyName(enemy), x + width / 2, y + 10, Constants.WHITE, 16, true);

        // 血条
        double hpPercent = (double) enemy.getHp() / Math.max(1, enemy.getMaxHp());
        double hpBarWidth = width - 20;
        double hpBarHeight = 15;
        fillRect(gc, x + 10, y + 35, hpBarWidth, hpBarHeight, Constants.BLACK);
        Color hpColor = hpPercent > 0.3 ? Constants.GREEN : Constants.RED;
        fillRect(gc, x + 10, y + 35, hpBarWidth * hpPercent, hpBarHeight, hpColor);
        drawText(gc, enemy.getHp() + "/" + enemy.getMaxHp(), x + width / 2, y + 55,
                Constants.WHITE, 14, true);

        // 护盾条
        if (enemy.getBlock() > 0) {
            double shieldBarY = y + 72;
            double shieldBarH = 18;
            double shieldPct = Math.min(1.0, (double) enemy.getBlock() / Math.max(1, enemy.getMaxHp() / 5));
            fillRect(gc, x + 10, shieldBarY, hpBarWidth, shieldBarH, Color.rgb(30, 30, 60));
            fillRect(gc, x + 10, shieldBarY, hpBarWidth * shieldPct, shieldBarH, Constants.BLUE);
            strokeRect(gc, x + 10, shieldBarY, hpBarWidth, shieldBarH, Constants.WHITE, 1);
            drawText(gc, "盾 " + enemy.getBlock(), x + width / 2, shieldBarY + 2, Constants.WHITE, 14, true);
        }

        // 意图
        double intentY = y + 100;
        Color intentBg = ENEMY_INTENT_COLORS.getOrDefault(enemy.getIntent(), Constants.PURPLE);
        fillRect(gc, x + 10, intentY, width - 20, 30, intentBg);
        if (enemy.getIntent() == EnemyIntent.BUFF) {
            strokeRect(gc, x + 10, intentY, width - 20, 30, Constants.GOLD, 2);
        }
        String intentText = enemy.getIntent().getValue() + " " + enemy.getIntentValue();
        Color intentTextColor = ENEMY_INTENT_TEXT_COLORS.getOrDefault(enemy.getIntent(), Constants.WHITE);
        drawText(gc, intentText, x + width / 2, intentY + 10, intentTextColor, 14, true);

        // 状态效果（两列布局）
        double statusY = y + 135;
        int idx = 0;
        if (enemy.getStatusEffects() != null) {
            for (Map<String, Object> status : enemy.getStatusEffects()) {
                String stype = (String) status.get("type");
                int value = ((Number) status.get("value")).intValue();
                if (ENEMY_STATUS_NAMES.containsKey(stype) && value > 0) {
                    Object[] nameInfo = ENEMY_STATUS_NAMES.get(stype);
                    int col = idx % 2;
                    int row = idx / 2;
                    double px = x + 15 + col * 70;
                    double py = statusY + row * 16;
                    drawText(gc, nameInfo[0] + ":" + value, px, py, (Color) nameInfo[1], 12);
                    idx++;
                }
            }
        }

        // buff 属性
        for (Object[] attr : ENEMY_BUFF_ATTRS) {
            String attrName = (String) attr[0];
            String label = (String) attr[1];
            Color normalColor = (Color) attr[2];
            int val = 0;
            if ("strength".equals(attrName)) val = enemy.getStrength();
            else if ("guard".equals(attrName)) val = enemy.getGuard();
            else if ("dexterity".equals(attrName)) val = enemy.getDexterity();
            else if ("increaseDamage".equals(attrName)) val = enemy.getIncreaseDamage();
            if (val > 0) {
                Color color = enemy.isBoss() ? Color.rgb(255, 220, 150) : normalColor;
                int col = idx % 2;
                int row = idx / 2;
                double px = x + 15 + col * 70;
                double py = statusY + row * 16;
                drawText(gc, label + ":" + val, px, py, color, 12);
                idx++;
            }
        }

        // 金币掉落
        if (enemy.getGoldDrop() > 0) {
            drawText(gc, "金 " + enemy.getGoldDrop(), x + width / 2, y + height - 25,
                    Constants.GOLD, 14, true);
        }
    }

    // ================================================================
    //  5. drawCombat — 绘制战斗界面
    // ================================================================

    /**
     * 绘制战斗界面。
     * <p>
     * 布局：顶部回合数、中上部敌人（水平排列）、左下玩家信息面板、
     * 底部手牌、右侧能量/牌堆信息、结束回合按钮、战斗日志、Boss 特性提示。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawCombat(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.BLACK);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 顶部回合数
        drawText(gc, "回合 " + state.turn, Constants.BASE_WIDTH / 2, 20,
                Constants.WHITE, 32, true);

        // 玩家信息面板（左下）
        if (state.player instanceof Player player) {
            drawPlayerInfo(gc, player, 20, 480, 200, 175);
        }

        // 敌方区域（水平排列）
        if (state.enemies != null && !state.enemies.isEmpty()) {
            double enemyStartX = Constants.BASE_WIDTH / 2
                    - (state.enemies.size() * 160.0) / 2;
            for (int i = 0; i < state.enemies.size(); i++) {
                Object e = state.enemies.get(i);
                if (e instanceof Enemy enemy && enemy.getHp() > 0) {
                    drawEnemy(gc, enemy, enemyStartX + i * 160, 50, 150, 220, 0, 0);
                }
            }
        }

        // 手牌区（底部居中排列）
        if (state.player instanceof Player player && player.getHand() != null) {
            List<Card> hand = player.getHand();
            double handStartX = Constants.BASE_WIDTH / 2 - (hand.size() * 110.0) / 2;
            for (int i = 0; i < hand.size(); i++) {
                Card card = hand.get(i);
                boolean isSelected = card.equals(state.selectedCard) || card.equals(state.hoveredCard);
                boolean isDisabled = card.getRequireStatus() != null
                        && !playerHasStatus(player, card.getRequireStatus());
                // 悬停高光：卡牌向上偏移10px，放大至110×150
                double cardY = Constants.BASE_HEIGHT - 160;
                double cardH = 140;
                if (card.equals(state.hoveredCard) && !isDisabled) {
                    cardY -= 10;
                    cardH = 150;
                }
                drawCard(gc, card, handStartX + i * 110, cardY,
                        100, cardH, isSelected, isDisabled);
            }
        }

        // 能量/牌堆信息（右侧）
        if (state.player instanceof Player player) {
            drawText(gc, "能量: " + player.getEnergy() + "/" + player.getMaxEnergy(),
                    Constants.BASE_WIDTH - 150, 520, Constants.GOLD, 24);
            drawText(gc, "抽牌堆: " + (player.getDrawPile() != null ? player.getDrawPile().size() : 0),
                    Constants.BASE_WIDTH - 150, 560, Color.rgb(200, 200, 200), 18);
            drawText(gc, "弃牌堆: " + (player.getDiscardPile() != null ? player.getDiscardPile().size() : 0),
                    Constants.BASE_WIDTH - 150, 590, Color.rgb(200, 200, 200), 18);
        }

        // 结束回合按钮（右侧）
        fillRect(gc, Constants.BASE_WIDTH - 180, 460, 160, 40, Color.rgb(100, 50, 100));
        strokeRect(gc, Constants.BASE_WIDTH - 180, 460, 160, 40, Constants.GOLD, 2);
        drawText(gc, "结束回合", Constants.BASE_WIDTH - 100, 470, Constants.WHITE, 20, true);

        // 战斗日志（左上角）
        if (state.combatLog != null && !state.combatLog.isEmpty()) {
            int maxLines = 10;
            double lineH = 18;
            List<String> logLines = state.combatLog.subList(
                    Math.max(0, state.combatLog.size() - maxLines), state.combatLog.size());
            double logX = 20;
            double logTop = 300;
            double logW = 280;
            double logBgH = logLines.size() * lineH + 8;

            // 半透明背景
            gc.setGlobalAlpha(0.7);
            fillRect(gc, logX, logTop, logW, logBgH, Color.rgb(20, 10, 30));
            gc.setGlobalAlpha(1.0);
            strokeRect(gc, logX, logTop, logW, logBgH, Color.rgb(100, 70, 130), 1);

            for (int i = 0; i < logLines.size(); i++) {
                String log = logLines.get(i);
                Color logColor = Color.rgb(200, 200, 200);
                if (log.contains("造成") || log.contains("伤害") || log.contains("灼烧")) {
                    logColor = Color.rgb(255, 150, 150);
                } else if (log.contains("回复") || log.contains("恢复") || log.contains("治疗")) {
                    logColor = Color.rgb(150, 255, 150);
                }
                drawText(gc, log, logX + 8, logTop + 4 + i * lineH, logColor, 12);
            }
        }

        // 卡牌打出动画
        if (state.animCardPlaying instanceof Card && state.animCardTimer > 0) {
            Card animCard = (Card) state.animCardPlaying;
            double totalFrames = 20.0;
            double progress = 1.0 - (double) state.animCardTimer / totalFrames; // 0→1
            // 缓动：先快后慢
            double eased = 1.0 - Math.pow(1.0 - progress, 2);
            double ax = state.animCardSx + (state.animCardEx - state.animCardSx) * eased;
            double ay = state.animCardSy + (state.animCardEy - state.animCardSy) * eased;
            // 淡出效果
            double alpha = Math.max(0, 1.0 - progress * 0.6);
            gc.setGlobalAlpha(alpha);
            drawCard(gc, animCard, ax, ay, 100, 140, false, false);
            gc.setGlobalAlpha(1.0);
        }

        // Boss 特性提示
        double abilityY = 60;
        if (state.enemies != null) {
            for (Object e : state.enemies) {
                if (e instanceof Enemy enemy) {
                    String ability = enemy.getBossAbility();
                    if (ability != null && BOSS_ABILITY_TEXT.containsKey(ability)) {
                        String text = BOSS_ABILITY_TEXT.get(ability);
                        fillRect(gc, Constants.BASE_WIDTH - 380, abilityY, 370, 32, Color.rgb(40, 0, 0));
                        strokeRect(gc, Constants.BASE_WIDTH - 380, abilityY, 370, 32, Color.rgb(200, 50, 50), 2);
                        drawText(gc, text, Constants.BASE_WIDTH - 195, abilityY + 10,
                                Color.rgb(255, 200, 200), 14, true);
                        abilityY += 36;
                    }
                }
            }
        }

        // 操作提示
        drawText(gc, "点击卡牌使用", Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 30,
                Constants.WHITE, 18, true);
    }

    /**
     * 检查玩家是否有指定状态。
     */
    private static boolean playerHasStatus(Player player, String statusType) {
        if (player.getStatusEffects() == null) return false;
        for (Map<String, Object> s : player.getStatusEffects()) {
            if (statusType.equals(s.get("type")) && ((Number) s.get("value")).intValue() > 0) {
                return true;
            }
        }
        return false;
    }

    // ================================================================
    //  6. drawMap — 绘制地图
    // ================================================================

    /**
     * 绘制地图界面。
     * <p>
     * 包含：当前层数标题、所有地图节点（不同形状表示不同类型）、
     * 已完成节点标记、悬浮提示、图鉴/卡组按钮、新手教程提示。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawMap(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 标题
        drawText(gc, "第 " + state.currentFloor + " 层", Constants.BASE_WIDTH / 2, 30,
                Constants.GOLD, 32, true);

        // 绘制地图节点
        if (state.mapNodes != null) {
            for (MapNode node : state.mapNodes) {
                if (node.getFloor() <= state.currentFloor) {
                    Color color = NODE_COLORS.getOrDefault(node.getType(), Constants.GRAY);
                    double nx = Constants.rx(node.getX());
                    double ny = Constants.ry(node.getY());
                    boolean isCurrentFloor = node.getFloor() == state.currentFloor;
                    boolean isNotCompleted = !node.isCompleted();

                    if (isCurrentFloor && isNotCompleted) {
                        // 绘制当前层可交互节点
                        drawMapNode(gc, node.getType(), nx, ny, Constants.rs(30), color, false);
                    } else {
                        // 已完成或未解锁节点
                        if (node.isCompleted()) {
                            drawMapNode(gc, node.getType(), nx, ny, Constants.rs(20), color, true);
                        } else {
                            gc.setFill(Color.rgb(50, 50, 50));
                            gc.fillOval(nx - Constants.rs(20), ny - Constants.rs(20),
                                    Constants.rs(40), Constants.rs(40));
                        }
                    }

                    // 节点图标
                    String icon = NODE_ICONS.getOrDefault(node.getType(), "?");
                    drawText(gc, icon, node.getX(), node.getY() - 5, Constants.WHITE, 20, true);

                    // 当前层节点名称
                    if (isCurrentFloor && isNotCompleted) {
                        String name = NODE_NAMES.getOrDefault(node.getType(), "");
                        drawText(gc, name, node.getX(), node.getY() + 35, Constants.WHITE, 18, true);
                    }
                }
            }
        }

        // 图鉴/卡组/遗物按钮（左上角）
        fillRect(gc, 20, 20, 90, 35, Color.rgb(60, 60, 100));
        strokeRect(gc, 20, 20, 90, 35, Constants.GOLD, 2);
        drawText(gc, "图鉴", 65, 28, Constants.WHITE, 16, true);

        fillRect(gc, 120, 20, 90, 35, Color.rgb(60, 80, 60));
        strokeRect(gc, 120, 20, 90, 35, Constants.GOLD, 2);
        drawText(gc, "卡组", 165, 28, Constants.WHITE, 16, true);

        fillRect(gc, 220, 20, 90, 35, Color.rgb(80, 60, 30));
        strokeRect(gc, 220, 20, 90, 35, Constants.GOLD, 2);
        drawText(gc, "✦遗物", 265, 28, Constants.WHITE, 16, true);

        // 金币/生命值显示（右上角）
        if (state.player instanceof Player player) {
            drawText(gc, "金:" + player.getGold(), Constants.BASE_WIDTH - 30, 28,
                    Constants.GOLD, 18, true);
            Color hpColor = player.getHp() <= player.getMaxHp() * 0.3 ? Constants.RED : Constants.GREEN;
            drawText(gc, "HP:" + player.getHp() + "/" + player.getMaxHp(),
                    Constants.BASE_WIDTH - 130, 28, hpColor, 18, true);
        }

        // 滚动提示
        drawText(gc, "滚轮滚动地图 | 拖拽移动", Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 80,
                Color.rgb(180, 180, 180), 16, true);

        // 新手教程提示
        if (state.showTutorial) {
            drawText(gc, "点击任意节点开始探索", Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 80,
                    Constants.GOLD, 24, true);
            drawText(gc, "战斗获取金币和卡牌  |  休息恢复生命  |  $ 商店购买卡牌",
                    Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 40, Constants.WHITE, 18, true);
        }
    }

    /**
     * 绘制单个地图节点形状。
     */
    private static void drawMapNode(GraphicsContext gc, MapNodeType type,
                                    double cx, double cy, double size, Color color, boolean completed) {
        gc.setFill(color);
        switch (type) {
            case BOSS -> {
                // 菱形
                gc.fillPolygon(
                        new double[]{cx, cx + size, cx, cx - size},
                        new double[]{cy - size, cy, cy + size, cy},
                        4);
            }
            case ELITE -> {
                // 六边形
                gc.fillPolygon(
                        new double[]{cx, cx + size * 0.866, cx + size * 0.866, cx, cx - size * 0.866, cx - size * 0.866},
                        new double[]{cy - size, cy - size * 0.5, cy + size * 0.5, cy + size, cy + size * 0.5, cy - size * 0.5},
                        6);
            }
            case SHOP -> {
                // 方形
                gc.fillRect(cx - size, cy - size, size * 2, size * 2);
            }
            case REST -> {
                // 椭圆
                gc.fillOval(cx - size, cy - size * 0.8, size * 2, size * 1.6);
            }
            default -> {
                // 圆形
                gc.fillOval(cx - size, cy - size, size * 2, size * 2);
            }
        }
    }

    // ================================================================
    //  7. drawTitle — 绘制标题界面
    // ================================================================

    /**
     * 绘制标题画面。
     * <p>
     * 包含：游戏标题"深渊行者"、开始游戏按钮、继续游戏按钮、图鉴按钮、退出按钮。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawTitle(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 背景图片
        Image bgImage = ResourceManager.get().loadImage("shenyuan.png");
        if (bgImage != null) {
            gc.drawImage(bgImage, 0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());
        }

        // 右上角设置齿轮按钮
        double settingsBtnSize = 50;
        double settingsBtnX = Constants.BASE_WIDTH - settingsBtnSize - 15;
        double settingsBtnY = 15;
        fillRect(gc, settingsBtnX, settingsBtnY, settingsBtnSize, settingsBtnSize,
                Color.rgb(80, 50, 120, 0.7));
        strokeRect(gc, settingsBtnX, settingsBtnY, settingsBtnSize, settingsBtnSize,
                Constants.WHITE, 2);
        drawText(gc, "⚙", settingsBtnX + settingsBtnSize / 2, settingsBtnY + 8,
                Constants.WHITE, 32, true);

        // 按钮
        double buttonW = 300;
        double buttonH = 60;
        double buttonX = Constants.BASE_WIDTH / 2 - buttonW / 2;
        double buttonY = 400;

        // 开始游戏按钮
        fillAlphaRect(gc, buttonX, buttonY, buttonW, buttonH, Color.rgb(80, 50, 120, 0.78));
        strokeRect(gc, buttonX, buttonY, buttonW, buttonH, Constants.WHITE, 2);
        drawText(gc, "开始游戏", Constants.BASE_WIDTH / 2, buttonY + 15, Constants.WHITE, 32, true);

        // 图鉴按钮
        double encBtnY = buttonY + 80;
        fillAlphaRect(gc, buttonX, encBtnY, buttonW, buttonH, Color.rgb(90, 70, 120, 0.78));
        strokeRect(gc, buttonX, encBtnY, buttonW, buttonH, Constants.WHITE, 2);
        drawText(gc, "游戏图鉴", Constants.BASE_WIDTH / 2, encBtnY + 15, Constants.WHITE, 32, true);

        // 退出按钮
        double exitBtnY = encBtnY + 80;
        fillAlphaRect(gc, buttonX, exitBtnY, buttonW, buttonH, Color.rgb(140, 50, 50, 0.78));
        strokeRect(gc, buttonX, exitBtnY, buttonW, buttonH, Constants.WHITE, 2);
        drawText(gc, "退出游戏", Constants.BASE_WIDTH / 2, exitBtnY + 15, Constants.WHITE, 32, true);
    }

    // ================================================================
    //  8. drawShop — 绘制商店
    // ================================================================

    /**
     * 绘制商店界面。
     * <p>
     * 包含：标题"神秘商店"、金币数、卡牌商品（含购买按钮）、遗物商品、删卡按钮、离开按钮。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawShop(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 标题
        drawText(gc, "神秘商店", Constants.BASE_WIDTH / 2, 30, Constants.GOLD, 48, true);

        // 金币
        if (state.player instanceof Player player) {
            drawText(gc, "金币: " + player.getGold(), Constants.BASE_WIDTH / 2, 80,
                    Constants.GOLD, 24, true);
        }

        // 卡牌商品（5张）
        if (state.shopItems != null) {
            double cardStartX = Constants.BASE_WIDTH / 2 - 5 * 75;
            for (int i = 0; i < Math.min(5, state.shopItems.size()); i++) {
                Map<String, Object> item = state.shopItems.get(i);
                double cx = cardStartX + i * 150;
                boolean sold = item.get("sold") != null && (Boolean) item.get("sold");
                int price = item.get("price") instanceof Number ? ((Number) item.get("price")).intValue() : 0;

                // 绘制卡牌
                Object cardObj = item.get("card");
                if (cardObj instanceof Card card) {
                    drawCard(gc, card, cx, 120, 130, 180, false, sold);
                    Color rarityColor = RARITY_BORDER_COLORS.getOrDefault(card.getRarity(), Constants.WHITE);
                    strokeRect(gc, cx, 120, 130, 180, rarityColor, 2);
                } else {
                    // 占位符
                    fillRect(gc, cx, 120, 130, 180, Constants.DARK_PURPLE);
                    strokeRect(gc, cx, 120, 130, 180, Constants.GOLD, 2);
                    drawText(gc, "卡牌", cx + 65, 200, Constants.WHITE, 16, true);
                }

                // 价格标签
                drawText(gc, "￥" + price, cx + 65, 305, Constants.GOLD, 16, true);

                // 购买按钮
                if (sold) {
                    fillRect(gc, cx + 10, 310, 110, 36, Color.rgb(60, 60, 60));
                    strokeRect(gc, cx + 10, 310, 110, 36, Color.rgb(100, 100, 100), 2);
                    drawText(gc, "已售", cx + 65, 320, Constants.GRAY, 16, true);
                } else {
                    boolean canBuy = state.player instanceof Player p && p.getGold() >= price;
                    Color btnColor = canBuy ? Color.rgb(50, 150, 50) : Color.rgb(80, 80, 80);
                    fillRect(gc, cx + 10, 310, 110, 36, btnColor);
                    strokeRect(gc, cx + 10, 310, 110, 36, Constants.GOLD, 2);
                    drawText(gc, "购买", cx + 65, 320, Constants.WHITE, 16, true);
                }
            }
        }

        // 遗物商品（2个，从第5项之后取）
        if (state.shopItems != null && state.shopItems.size() > 5) {
            double relicStartX = Constants.BASE_WIDTH / 2 - 220;
            for (int i = 0; i < 2; i++) {
                int idx = 5 + i;
                if (idx >= state.shopItems.size()) break;
                Map<String, Object> item = state.shopItems.get(idx);
                double rx = relicStartX + i * 240;
                boolean sold = item.get("sold") != null && (Boolean) item.get("sold");
                int price = item.get("price") instanceof Number ? ((Number) item.get("price")).intValue() : 0;

                // 遗物卡片背景（紫色背景，金色边框）
                fillRect(gc, rx, 370, 200, 100, Color.rgb(50, 35, 70));
                strokeRect(gc, rx, 370, 200, 100, Constants.GOLD, 2);

                Object relicObj = item.get("relic");
                if (relicObj instanceof Relic relic) {
                    drawText(gc, "✦ " + getRelicName(relic), rx + 100, 390, Constants.GOLD, 16, true);
                    drawText(gc, getRelicDesc(relic), rx + 100, 415, Constants.WHITE, 11, true);
                } else {
                    drawText(gc, "✦ 遗物", rx + 100, 410, Constants.GOLD, 16, true);
                }

                // 价格标签
                drawText(gc, "￥" + price, rx + 100, 445, Constants.GOLD, 16, true);

                // 购买按钮
                if (sold) {
                    fillRect(gc, rx + 45, 480, 110, 36, Color.rgb(60, 60, 60));
                    strokeRect(gc, rx + 45, 480, 110, 36, Color.rgb(100, 100, 100), 2);
                    drawText(gc, "已售", rx + 100, 490, Constants.GRAY, 16, true);
                } else {
                    boolean canBuy = state.player instanceof Player p && p.getGold() >= price;
                    Color btnColor = canBuy ? Color.rgb(50, 150, 50) : Color.rgb(80, 80, 80);
                    fillRect(gc, rx + 45, 480, 110, 36, btnColor);
                    strokeRect(gc, rx + 45, 480, 110, 36, Constants.GOLD, 2);
                    drawText(gc, "购买", rx + 100, 490, Constants.WHITE, 16, true);
                }
            }
        } else {
            // 没有遗物数据时显示占位
            double relicStartX = Constants.BASE_WIDTH / 2 - 220;
            for (int i = 0; i < 2; i++) {
                double rx = relicStartX + i * 240;
                fillRect(gc, rx, 370, 200, 100, Color.rgb(50, 35, 70));
                strokeRect(gc, rx, 370, 200, 100, Constants.GOLD, 2);
                drawText(gc, "✦ 遗物", rx + 100, 410, Constants.GOLD, 16, true);
                drawText(gc, "￥--", rx + 100, 445, Constants.GOLD, 16, true);

                fillRect(gc, rx + 45, 480, 110, 36, Color.rgb(80, 80, 80));
                strokeRect(gc, rx + 45, 480, 110, 36, Constants.GOLD, 2);
                drawText(gc, "购买", rx + 100, 490, Constants.WHITE, 16, true);
            }
        }

        // 删卡模式UI
        if (state.shopDeleteMode) {
            // 删卡模式提示
            fillRect(gc, 0, 0, Constants.BASE_WIDTH, Constants.BASE_HEIGHT, Color.rgb(0, 0, 0, 0.3));
            drawText(gc, "选择要删除的卡牌（已删除 " + state.cardDeleteCount + " 张）",
                    Constants.BASE_WIDTH / 2, 120, Constants.RED, 24, true);

            // 显示玩家卡牌供选择
            if (state.player instanceof Player player) {
                List<Card> allCards = new ArrayList<>();
                if (player.getDrawPile() != null) allCards.addAll(player.getDrawPile());
                if (player.getHand() != null) allCards.addAll(player.getHand());
                if (player.getDiscardPile() != null) allCards.addAll(player.getDiscardPile());
                if (player.getExhaustPile() != null) allCards.addAll(player.getExhaustPile());

                if (!allCards.isEmpty()) {
                    double cardW = 100;
                    double cardH = 140;
                    double gapX = 15;
                    double totalW = Math.min(10, allCards.size()) * (cardW + gapX) - gapX;
                    double startX = Constants.BASE_WIDTH / 2 - totalW / 2;
                    double startY = 160;

                    for (int i = 0; i < Math.min(10, allCards.size()); i++) {
                        Card card = allCards.get(i);
                        double cx = startX + i % 10 * (cardW + gapX);
                        double cy = startY + (i / 10) * (cardH + 20);
                        boolean isSelected = card.equals(state.selectedShopDeleteCard);
                        drawCard(gc, card, cx, cy, cardW, cardH, isSelected, false);
                        if (isSelected) {
                            strokeRect(gc, cx - 3, cy - 3, cardW + 6, cardH + 6, Constants.RED, 3);
                        }
                    }
                }
            }

            // 取消删卡按钮
            fillRect(gc, Constants.BASE_WIDTH / 2 - 60, Constants.BASE_HEIGHT - 60, 120, 40, Color.rgb(80, 60, 60));
            strokeRect(gc, Constants.BASE_WIDTH / 2 - 60, Constants.BASE_HEIGHT - 60, 120, 40, Constants.WHITE, 2);
            drawText(gc, "取消", Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 50, Constants.WHITE, 18, true);
        } else {
            // 底部按钮（非删卡模式）
            fillRect(gc, Constants.BASE_WIDTH / 2 - 180, 550, 150, 45, Color.rgb(150, 50, 50));
            strokeRect(gc, Constants.BASE_WIDTH / 2 - 180, 550, 150, 45, Constants.GOLD, 2);
            drawText(gc, "删卡", Constants.BASE_WIDTH / 2 - 105, 562, Constants.WHITE, 20, true);

            fillRect(gc, Constants.BASE_WIDTH / 2 + 30, 550, 150, 45, Color.rgb(80, 60, 140));
            strokeRect(gc, Constants.BASE_WIDTH / 2 + 30, 550, 150, 45, Constants.GOLD, 2);
            drawText(gc, "离开", Constants.BASE_WIDTH / 2 + 105, 562, Constants.WHITE, 20, true);
        }

        // 图鉴/卡组/遗物按钮（左上角）
        fillRect(gc, 20, 20, 90, 35, Color.rgb(60, 60, 100));
        strokeRect(gc, 20, 20, 90, 35, Constants.GOLD, 2);
        drawText(gc, "图鉴", 65, 28, Constants.WHITE, 16, true);

        fillRect(gc, 120, 20, 90, 35, Color.rgb(60, 80, 60));
        strokeRect(gc, 120, 20, 90, 35, Constants.GOLD, 2);
        drawText(gc, "卡组", 165, 28, Constants.WHITE, 16, true);

        fillRect(gc, 220, 20, 90, 35, Color.rgb(80, 60, 30));
        strokeRect(gc, 220, 20, 90, 35, Constants.GOLD, 2);
        drawText(gc, "✦遗物", 265, 28, Constants.WHITE, 16, true);
    }

    // ================================================================
    //  9. drawReward — 绘制奖励界面
    // ================================================================

    /**
     * 绘制战斗奖励界面。
     * <p>
     * 包含：胜利标题、金币数、可选卡牌列表（点击选中/金色高亮）、
     * 精英怪掉落遗物、跳过/确认按钮。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawReward(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 标题
        drawText(gc, "战斗胜利!", Constants.BASE_WIDTH / 2, 50, Constants.GOLD, 48, true);

        // 金币
        drawText(gc, "金币 " + state.rewardGold, Constants.BASE_WIDTH / 2, 150,
                Constants.GOLD, 32, true);

        // 选择卡牌提示
        drawText(gc, "选择一张卡牌", Constants.BASE_WIDTH / 2, 220, Constants.WHITE, 24, true);

        // 卡牌列表（3张）
        if (state.rewardCards != null) {
            double cardStartX = Constants.BASE_WIDTH / 2 - 165;
            for (int i = 0; i < state.rewardCards.size(); i++) {
                Object cardObj = state.rewardCards.get(i);
                if (cardObj instanceof Card card) {
                    double cx = cardStartX + i * 110;
                    drawCard(gc, card, cx, 280, 100, 140,
                            state.rewardSelectedCard == i, false);
                    // 选中高亮
                    if (state.rewardSelectedCard == i) {
                        strokeRect(gc, cx - 3, 277, 106, 146, Constants.GOLD, 3);
                    }
                }
            }
        }

        // 遗物
        if (state.rewardRelic instanceof Relic relic) {
            double relicY = 450;
            if (state.rewardRelicTaken) {
                fillRect(gc, Constants.BASE_WIDTH / 2 - 200, relicY, 400, 60, Color.rgb(40, 40, 40));
                strokeRect(gc, Constants.BASE_WIDTH / 2 - 200, relicY, 400, 60, Color.rgb(100, 100, 100), 2);
                drawText(gc, "✓ 已获得遗物", Constants.BASE_WIDTH / 2, relicY + 5,
                        Color.rgb(150, 150, 150), 16, true);
                drawText(gc, "✦ " + getRelicName(relic), Constants.BASE_WIDTH / 2, relicY + 28,
                        Color.rgb(180, 180, 180), 20, true);
            } else {
                fillRect(gc, Constants.BASE_WIDTH / 2 - 200, relicY, 400, 60, Color.rgb(50, 30, 80));
                strokeRect(gc, Constants.BASE_WIDTH / 2 - 200, relicY, 400, 60, Constants.GOLD, 2);
                drawText(gc, "✦ 点击拾取遗物", Constants.BASE_WIDTH / 2, relicY + 5,
                        Constants.GOLD, 16, true);
                drawText(gc, "✦ " + getRelicName(relic), Constants.BASE_WIDTH / 2, relicY + 28,
                        Constants.WHITE, 20, true);
            }
        }

        // 底部按钮
        double buttonY = Constants.BASE_HEIGHT - 100;
        fillRect(gc, Constants.BASE_WIDTH / 2 - 200, buttonY, 150, 50, Constants.RED);
        drawText(gc, "跳过", Constants.BASE_WIDTH / 2 - 125, buttonY + 15, Constants.WHITE, 24, true);

        fillRect(gc, Constants.BASE_WIDTH / 2 + 50, buttonY, 150, 50, Constants.GREEN);
        drawText(gc, "确认", Constants.BASE_WIDTH / 2 + 125, buttonY + 15, Constants.WHITE, 24, true);

        // 图鉴/卡组/遗物按钮
        fillRect(gc, 20, 20, 90, 35, Color.rgb(60, 60, 100));
        strokeRect(gc, 20, 20, 90, 35, Constants.GOLD, 2);
        drawText(gc, "图鉴", 65, 28, Constants.WHITE, 16, true);

        fillRect(gc, 120, 20, 90, 35, Color.rgb(60, 80, 60));
        strokeRect(gc, 120, 20, 90, 35, Constants.GOLD, 2);
        drawText(gc, "卡组", 165, 28, Constants.WHITE, 16, true);

        fillRect(gc, 220, 20, 90, 35, Color.rgb(80, 60, 30));
        strokeRect(gc, 220, 20, 90, 35, Constants.GOLD, 2);
        drawText(gc, "✦遗物", 265, 28, Constants.WHITE, 16, true);
    }

    // ================================================================
    //  10. drawEncyclopedia — 绘制图鉴
    // ================================================================

    /**
     * 绘制图鉴界面。
     * <p>
     * 包含：标题"游戏图鉴"、模块切换按钮（卡牌/遗物/状态/怪物/道具）、
     * 内容网格（4列×2行）、翻页按钮、返回按钮。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawEncyclopedia(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 标题
        drawText(gc, "游戏图鉴", Constants.BASE_WIDTH / 2, 30, Constants.GOLD, 40, true);

        // 模块切换按钮
        String[] modules = {"cards", "relics", "statuses", "enemies", "items"};
        String[] moduleLabels = {"卡牌", "遗物", "状态", "怪物", "道具"};
        double btnWidth = 200;
        double btnHeight = 40;
        double startX = Constants.BASE_WIDTH / 2 - (modules.length * btnWidth) / 2;

        for (int i = 0; i < modules.length; i++) {
            double bx = startX + i * btnWidth;
            boolean isSelected = modules[i].equals(state.encyclopediaModule);
            Color bgColor = isSelected ? Color.rgb(80, 60, 120) : Color.rgb(50, 40, 70);
            fillRect(gc, bx, 80, btnWidth - 10, btnHeight, bgColor);
            strokeRect(gc, bx, 80, btnWidth - 10, btnHeight, isSelected ? Constants.WHITE : Constants.GRAY, 2);
            Color textColor = isSelected ? Constants.GOLD : Constants.WHITE;
            drawText(gc, moduleLabels[i], bx + (btnWidth - 10) / 2, 88, textColor, 20, true);
        }

        // 内容区域
        switch (state.encyclopediaModule) {
            case "cards" -> drawEncyclopediaCards(gc, state);
            case "relics" -> drawEncyclopediaRelics(gc, state);
            case "statuses" -> drawEncyclopediaStatuses(gc, state);
            case "enemies" -> drawEncyclopediaEnemies(gc, state);
            case "items" -> drawEncyclopediaItems(gc, state);
        }

        // 返回按钮
        fillRect(gc, Constants.BASE_WIDTH - 120, Constants.BASE_HEIGHT - 60, 100, 35,
                Color.rgb(100, 50, 50));
        strokeRect(gc, Constants.BASE_WIDTH - 120, Constants.BASE_HEIGHT - 60, 100, 35,
                Constants.GOLD, 2);
        drawText(gc, "◀ 返回", Constants.BASE_WIDTH - 70, Constants.BASE_HEIGHT - 52,
                Constants.WHITE, 16, true);
    }

    /**
     * 绘制图鉴 - 卡牌模块（与Python版一致：稀有度+角色筛选，左侧竖排按钮，5列×3行）。
     */
    private static void drawEncyclopediaCards(GraphicsContext gc, GameState state) {
        // ── 1. 稀有度筛选按钮（左侧竖排） ──
        String[] rarityLabels = {"全部", "普通", "罕见", "稀有", "传说", "诅咒"};
        String[] rarityKeys = {null, "common", "uncommon", "rare", "legendary", "curse"};
        Color[] rarityColors = {Constants.GOLD, Constants.WHITE, Constants.BLUE, Constants.GOLD, Constants.RED, Color.rgb(100, 0, 100)};
        double filterX = 30;
        double filterY = 150;
        double btnW = 110;
        double btnH = 38;
        double gap = 8;

        drawText(gc, "稀有度筛选", filterX + btnW / 2, filterY - 25, Constants.GOLD, 14, true);
        for (int i = 0; i < rarityLabels.length; i++) {
            double fy = filterY + i * (btnH + gap);
            boolean isSelected = (rarityKeys[i] == null && state.encyclopediaFilter == null)
                    || (rarityKeys[i] != null && rarityKeys[i].equals(state.encyclopediaFilter));
            Color bgColor = isSelected ? Color.rgb(80, 60, 120) : Color.rgb(50, 40, 70);
            fillRect(gc, filterX, fy, btnW, btnH, bgColor);
            Color borderColor = isSelected ? rarityColors[i] : Constants.GRAY;
            strokeRect(gc, filterX, fy, btnW, btnH, borderColor, 2);
            Color textColor = isSelected ? rarityColors[i] : Constants.WHITE;
            drawText(gc, rarityLabels[i], filterX + btnW / 2, fy + 11, textColor, 16, true);
        }

        // ── 2. 角色筛选按钮（左侧竖排） ──
        String[] classLabels = {"全部", "战士", "法师", "盗贼", "圣女", "公共"};
        String[] classKeys = {null, "warrior", "mage", "rogue", "priest", "public"};
        Color[] classColors = {Constants.GOLD, Color.rgb(220, 100, 100), Color.rgb(100, 150, 255),
                Color.rgb(200, 180, 100), Color.rgb(240, 200, 80), Constants.GRAY};
        double classFilterX = 30;
        double classFilterY = 440;
        double classBtnW = 110;
        double classBtnH = 38;
        double classGap = 6;

        drawText(gc, "角色筛选", classFilterX + classBtnW / 2, classFilterY - 25, Constants.GOLD, 14, true);
        for (int i = 0; i < classLabels.length; i++) {
            double fy = classFilterY + i * (classBtnH + classGap);
            boolean isSelected = (classKeys[i] == null && state.encyclopediaClassFilter == null)
                    || (classKeys[i] != null && classKeys[i].equals(state.encyclopediaClassFilter));
            Color bgColor = isSelected ? Color.rgb(80, 60, 120) : Color.rgb(50, 40, 70);
            fillRect(gc, classFilterX, fy, classBtnW, classBtnH, bgColor);
            Color borderColor = isSelected ? classColors[i] : Constants.GRAY;
            strokeRect(gc, classFilterX, fy, classBtnW, classBtnH, borderColor, 2);
            Color textColor = isSelected ? classColors[i] : Constants.WHITE;
            drawText(gc, classLabels[i], classFilterX + classBtnW / 2, fy + 11, textColor, 16, true);
        }

        // ── 3. 收集所有卡牌并应用筛选 ──
        // 计算公共卡集合（所有卡 - 所有专属卡）
        Set<String> allExclusive = new HashSet<>();
        for (Set<String> pool : GameState.CLASS_EXCLUSIVE_POOL.values()) {
            allExclusive.addAll(pool);
        }
        Set<String> allCardKeys = new HashSet<>();
        for (List<String> pool : GameState.FULL_CARD_POOL.values()) {
            allCardKeys.addAll(pool);
        }
        allCardKeys.addAll(GameState.CURSE_CARD_POOL);

        List<Card> filteredCards = new ArrayList<>();
        for (String cardKey : allCardKeys) {
            // 稀有度筛选
            if (state.encyclopediaFilter != null) {
                if ("curse".equals(state.encyclopediaFilter)) {
                    // 诅咒卡按类型筛选
                    Card testCard = CardFactory.createCard(cardKey);
                    if (testCard.getType() != CardType.CURSE) continue;
                } else {
                    Card testCard = CardFactory.createCard(cardKey);
                    if (!state.encyclopediaFilter.equals(testCard.getRarity().getValue())) continue;
                }
            }
            // 角色筛选
            if (state.encyclopediaClassFilter != null) {
                if ("public".equals(state.encyclopediaClassFilter)) {
                    if (allExclusive.contains(cardKey)) continue;
                } else {
                    Set<String> pool = GameState.CLASS_EXCLUSIVE_POOL.get(state.encyclopediaClassFilter);
                    if (pool == null || !pool.contains(cardKey)) continue;
                }
            }
            Card card = CardFactory.createCard(cardKey);
            filteredCards.add(card);
        }

        // ── 4. 分页（5列×3行） ──
        int cols = 5;
        int rows = 3;
        int perPage = cols * rows;
        int totalPages = Math.max(1, (filteredCards.size() + perPage - 1) / perPage);
        state.encyclopediaPage = Math.min(state.encyclopediaPage, totalPages - 1);

        int startIdx = state.encyclopediaPage * perPage;
        int endIdx = Math.min(startIdx + perPage, filteredCards.size());

        // ── 5. 绘制卡牌网格 ──
        double cardW = 120;
        double cardH = 170;
        double gapX = 20;
        double gapY = 20;
        double totalW = cols * (cardW + gapX) - gapX;
        double startX = Constants.BASE_WIDTH / 2 - totalW / 2;
        double startY = 140;

        for (int i = startIdx; i < endIdx; i++) {
            int idx = i - startIdx;
            int col = idx % cols;
            int row = idx / cols;
            double cx = startX + col * (cardW + gapX);
            double cy = startY + row * (cardH + gapY);
            Card card = filteredCards.get(i);
            // 用 getCardName 获取中文名
            drawCard(gc, card, cx, cy, cardW, cardH, false, false);
        }

        // ── 6. 页码和翻页 ──
        if (totalPages > 1) {
            drawText(gc, "第 " + (state.encyclopediaPage + 1) + " / " + totalPages + " 页",
                    Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 80, Constants.WHITE, 16, true);

            if (state.encyclopediaPage > 0) {
                fillRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                        Color.rgb(60, 80, 60));
                strokeRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                        Constants.GOLD, 2);
                drawText(gc, "◀ 上一页", Constants.BASE_WIDTH / 2 - 80, Constants.BASE_HEIGHT - 52,
                        Constants.WHITE, 14, true);
            }

            if (state.encyclopediaPage < totalPages - 1) {
                fillRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                        Color.rgb(60, 80, 60));
                strokeRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                        Constants.GOLD, 2);
                drawText(gc, "下一页 ▶", Constants.BASE_WIDTH / 2 + 80, Constants.BASE_HEIGHT - 52,
                        Constants.WHITE, 14, true);
            }
        }
    }

    /**
     * 绘制图鉴 - 遗物模块。
     */
    private static void drawEncyclopediaRelics(GraphicsContext gc, GameState state) {
        List<String> relicKeys = com.abyss.system.RelicPool.getAllRelicKeys();

        // 分页显示
        int cols = 4;
        int rows = 2;
        int perPage = cols * rows;
        int totalPages = Math.max(1, (relicKeys.size() + perPage - 1) / perPage);
        state.encyclopediaPage = Math.min(state.encyclopediaPage, totalPages - 1);

        int startIdx = state.encyclopediaPage * perPage;
        List<String> pageRelics = relicKeys.subList(startIdx, Math.min(startIdx + perPage, relicKeys.size()));

        // 绘制遗物网格
        double relicW = 240;
        double relicH = 90;
        double gapX = 25;
        double gapY = 20;
        double totalW = cols * (relicW + gapX) - gapX;
        double startX = Constants.BASE_WIDTH / 2 - totalW / 2;
        double startY = 140;

        for (int i = 0; i < pageRelics.size(); i++) {
            int col = i % cols;
            int row = i / cols;
            double rx = startX + col * (relicW + gapX);
            double ry = startY + row * (relicH + gapY);

            Relic relic = com.abyss.system.RelicPool.getRelic(pageRelics.get(i));
            if (relic != null) {
                // 遗物卡片背景
                fillRect(gc, rx, ry, relicW, relicH, Color.rgb(50, 30, 80));
                strokeRect(gc, rx, ry, relicW, relicH, Constants.GOLD, 2);

                // 遗物名称
                drawText(gc, "✦ " + getRelicName(relic), rx + 10, ry + 8, Constants.GOLD, 14);
                // 描述
                drawText(gc, getRelicDesc(relic), rx + 10, ry + 32, Constants.WHITE, 11);
                // 效果类型
                if (relic.getEffect() != null) {
                    String effectType = (String) relic.getEffect().get("type");
                    drawText(gc, "效果: " + effectType, rx + 10, ry + 55, Color.rgb(200, 200, 100), 10);
                }
            }
        }

        // 页码和翻页
        if (totalPages > 1) {
            drawText(gc, "第 " + (state.encyclopediaPage + 1) + " / " + totalPages + " 页",
                    Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 80, Constants.WHITE, 16, true);

            if (state.encyclopediaPage > 0) {
                fillRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                        Color.rgb(60, 80, 60));
                strokeRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                        Constants.GOLD, 2);
                drawText(gc, "◀ 上一页", Constants.BASE_WIDTH / 2 - 80, Constants.BASE_HEIGHT - 52,
                        Constants.WHITE, 14, true);
            }

            if (state.encyclopediaPage < totalPages - 1) {
                fillRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                        Color.rgb(60, 80, 60));
                strokeRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                        Constants.GOLD, 2);
                drawText(gc, "下一页 ▶", Constants.BASE_WIDTH / 2 + 80, Constants.BASE_HEIGHT - 52,
                        Constants.WHITE, 14, true);
            }
        }
    }

    /**
     * 绘制图鉴 - 状态效果模块。
     */
    private static void drawEncyclopediaStatuses(GraphicsContext gc, GameState state) {
        // 状态效果数据
        String[][] statusData = {
                {"易伤", "vulnerable", "受到的伤害提高50%", "#ff6464"},
                {"虚弱", "weak", "造成的伤害减半", "#969696"},
                {"中毒", "poison", "每回合受到层数伤害，层数-1", "#64c864"},
                {"灼烧", "burn", "每回合受到层数伤害，层数-1", "#ff0000"},
                {"虚化", "phasing", "免疫下一次受到的伤害", "#ffd700"},
                {"脆弱", "fragile", "格挡量减半", "#c89664"},
                {"冻伤", "frostbite", "层数≥HP时直接死亡，不掉层", "#64c8ff"},
                {"麻痹", "paralysis", "受到的伤害提高层数，层数-1", "#ffff64"},
                {"减速", "slow", "50%概率跳过行动", "#a855f7"},
                {"流血", "bleed", "每回合层数减半（向下取整）", "#dc3232"},
                {"眩晕", "stun", "跳过行动", "#ffd700"},
                {"闪避", "dodge", "闪避下一次攻击", "#6495ed"},
                {"潜伏", "lurk", "潜行状态，下回合可发动刺杀", "#64c896"},
                {"刺杀", "assassinate", "潜伏后的刺杀效果", "#dc3232"},
                {"力量", "strength", "增加攻击伤害", "#ff3333"},
                {"守护", "guard", "每点守护减少1点受到的伤害", "#32cd32"},
                {"敏捷", "dexterity", "增加格挡获取量", "#6495ed"},
                {"增伤", "increase_damage", "造成的伤害增加", "#ffd700"}
        };

        int cols = 3;
        int rows = 3;
        int perPage = cols * rows;
        int totalPages = Math.max(1, (statusData.length + perPage - 1) / perPage);
        state.encyclopediaPage = Math.min(state.encyclopediaPage, totalPages - 1);

        int startIdx = state.encyclopediaPage * perPage;
        int endIdx = Math.min(startIdx + perPage, statusData.length);

        double cardW = 320;
        double cardH = 70;
        double gapX = 30;
        double gapY = 15;
        double totalW = cols * (cardW + gapX) - gapX;
        double startX = Constants.BASE_WIDTH / 2 - totalW / 2;
        double startY = 140;

        for (int i = startIdx; i < endIdx; i++) {
            int idx = i - startIdx;
            int col = idx % cols;
            int row = idx / cols;
            double cx = startX + col * (cardW + gapX);
            double cy = startY + row * (cardH + gapY);

            String[] data = statusData[i];
            Color statusColor = Color.web(data[3]);

            fillRect(gc, cx, cy, cardW, cardH, Color.rgb(40, 30, 60));
            strokeRect(gc, cx, cy, cardW, cardH, statusColor, 2);

            drawText(gc, data[0] + " (" + data[1] + ")", cx + 15, cy + 10, statusColor, 16);
            drawText(gc, data[2], cx + 15, cy + 35, Constants.WHITE, 12);
        }

        // 页码和翻页
        if (totalPages > 1) {
            drawText(gc, "第 " + (state.encyclopediaPage + 1) + " / " + totalPages + " 页",
                    Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 80, Constants.WHITE, 16, true);

            if (state.encyclopediaPage > 0) {
                fillRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                        Color.rgb(60, 80, 60));
                strokeRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                        Constants.GOLD, 2);
                drawText(gc, "◀ 上一页", Constants.BASE_WIDTH / 2 - 80, Constants.BASE_HEIGHT - 52,
                        Constants.WHITE, 14, true);
            }

            if (state.encyclopediaPage < totalPages - 1) {
                fillRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                        Color.rgb(60, 80, 60));
                strokeRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                        Constants.GOLD, 2);
                drawText(gc, "下一页 ▶", Constants.BASE_WIDTH / 2 + 80, Constants.BASE_HEIGHT - 52,
                        Constants.WHITE, 14, true);
            }
        }
    }

    /**
     * 绘制图鉴 - 怪物模块（与Python版一致：类型筛选，左侧竖排按钮）。
     */
    private static void drawEncyclopediaEnemies(GraphicsContext gc, GameState state) {
        // ── 1. 类型筛选按钮（左侧竖排） ──
        String[] filterLabels = {"全部", "普通", "精英", "BOSS"};
        String[] filterTypes = {null, "普通", "精英", "Boss"};
        Color[] filterColors = {Constants.GOLD, Constants.WHITE, Color.rgb(255, 165, 0), Constants.RED};
        double filterX = 30;
        double filterY = 150;
        double btnW = 110;
        double btnH = 38;
        double gap = 8;

        drawText(gc, "类型筛选", filterX + btnW / 2, filterY - 25, Constants.GOLD, 14, true);
        for (int i = 0; i < filterLabels.length; i++) {
            double fy = filterY + i * (btnH + gap);
            boolean isSelected = (filterTypes[i] == null && state.encyclopediaFilter == null)
                    || (filterTypes[i] != null && filterTypes[i].equals(state.encyclopediaFilter));
            Color bgColor = isSelected ? Color.rgb(80, 60, 120) : Color.rgb(50, 40, 70);
            fillRect(gc, filterX, fy, btnW, btnH, bgColor);
            Color borderColor = isSelected ? filterColors[i] : Constants.GRAY;
            strokeRect(gc, filterX, fy, btnW, btnH, borderColor, 2);
            Color textColor = isSelected ? filterColors[i] : Constants.WHITE;
            drawText(gc, filterLabels[i], filterX + btnW / 2, fy + 11, textColor, 16, true);
        }

        // 从 EnemyData 获取怪物数据
        String[][] enemyData = {
                {"slime", "史莱姆", "普通", "24", "4", "普通的史莱姆，行动缓慢"},
                {"goblin", "哥布林", "普通", "20", "5", "狡猾的哥布林，喜欢偷袭"},
                {"skeleton", "骷髅", "普通", "28", "6", "不死骷髅兵，不知疲倦"},
                {"orc", "兽人", "普通", "32", "8", "强壮的兽人，攻击力较高"},
                {"vampire", "吸血鬼", "普通", "28", "7", "嗜血的吸血鬼，会吸取生命"},
                {"demon", "恶魔", "普通", "36", "9", "来自地狱的恶魔，十分危险"},
                {"mushroom", "蘑菇精", "普通", "22", "4", "蘑菇精，每回合获得3点格挡"},
                {"bat", "蝙蝠", "普通", "18", "5", "嗜血蝙蝠，攻击时回复2点生命"},
                {"gargoyle", "石像鬼", "普通", "40", "3", "石像鬼，战斗开始时获得8点格挡"},
                {"viper", "毒蛇", "普通", "20", "4", "毒蛇，攻击时施加1层中毒"},
                {"wraith", "幽灵", "普通", "25", "6", "幽灵，死亡时对玩家造成5点伤害"},
                {"goblin_shaman", "哥布林萨满", "普通", "22", "4", "哥布林萨满，每2回合增加1点力量"},
                {"werewolf", "狼人", "普通", "34", "7", "凶猛的狼人，月圆之夜力量倍增"},
                {"elf_archer", "精灵射手", "普通", "16", "9", "敏捷的精灵射手，高攻低血"},
                {"goblin_bomber", "哥布林炸弹兵", "普通", "14", "3", "疯狂的哥布林，死亡时造成6点伤害"},
                {"stone_golem", "石人", "普通", "50", "4", "坚硬的石人，每回合获得4点格挡"},
                {"frost_spider", "冰霜蜘蛛", "普通", "20", "4", "攻击时施加1层冻伤"},
                {"goblin_warlock", "哥布林术士", "普通", "24", "5", "攻击时施加1层虚弱"},
                {"imp", "小恶魔", "普通", "10", "10", "攻击力惊人但非常脆弱"},
                {"shadow_wraith", "暗影幽灵", "精英", "60", "10", "暗影幽灵，每回合获得1层虚化"},
                {"bandit", "强盗", "普通", "30", "7", "强盗，攻击时额外获得1金币"},
                {"cursed_statue", "诅咒雕像", "普通", "36", "5", "战斗开始时给玩家施加1层虚弱"},
                {"thunder_bird", "雷鸟", "普通", "20", "6", "攻击时施加1层麻痹"},
                {"healing_nymph", "治愈精灵", "普通", "24", "3", "每回合回复所有队友3点生命"},
                {"fire_imp", "火焰小鬼", "普通", "14", "8", "攻击时施加1层灼烧"},
                {"goblin_captain", "哥布林队长", "精英", "75", "16", "哥布林队长，统领小喽啰"},
                {"skeleton_knight", "骷髅骑士", "精英", "85", "17", "骷髅骑士，装备精良"},
                {"orc_warrior", "兽人战士", "精英", "100", "19", "兽人战士，凶猛残暴"},
                {"slime_ang", "史莱姆ang", "精英", "110", "12", "死亡时分裂成两个史莱姆"},
                {"terror_eye", "恐怖眼球", "精英", "240", "8", "血量很高的眼球"},
                {"chicken_hotpot_killer", "鸡煲杀手", "精英", "92", "15", "非攻击牌增加力量"},
                {"blood_monster", "血液怪", "精英", "105", "14", "回复等同造成的伤害的血量"},
                {"shadow_assassin", "暗影刺客", "精英", "55", "18", "每回合获得1层虚化"},
                {"lava_beast", "熔岩巨兽", "精英", "130", "10", "每回合获得5点格挡"},
                {"frost_mage", "冰霜法师", "精英", "70", "8", "每回合给玩家施加2层冻伤"},
                {"curse_priest", "诅咒祭司", "精英", "80", "9", "单数回合虚弱，双数回合易伤"},
                {"poison_witch", "毒术士", "精英", "85", "12", "未被格挡给予3层中毒"},
                {"snow_fairy", "雪妖精", "精英", "75", "10", "每回合给予2层冻伤，免疫冻伤"},
                {"thorn_ghost", "荆棘鬼", "精英", "95", "14", "每次受到攻击对玩家造成2点伤害"},
                {"killer_machine", "杀手机器", "Boss", "300", "21", "拥有机甲之力"},
                {"dragon", "远古巨龙", "Boss", "278", "20", "拥有龙息之力"},
                {"lich", "巫妖王", "Boss", "240", "19", "掌控死亡之力"},
                {"dark_lord", "暗黑领主", "Boss", "345", "25", "拥有诅咒之力"},
                {"disaster_left_hand", "灾祸左手", "Boss", "200", "14", "与诅咒右手成对出现"},
                {"curse_right_hand", "诅咒右手", "Boss", "200", "14", "与灾祸左手成对出现"},
                {"jiangwang_xiao_laodi", "僵王小老弟", "Boss", "320", "22", "召唤僵尸大军"}
        };

        // 应用类型筛选
        List<String[]> filteredData = new ArrayList<>();
        for (String[] data : enemyData) {
            String typeLabel = data[2];
            if (state.encyclopediaFilter == null || typeLabel.equals(state.encyclopediaFilter)) {
                filteredData.add(data);
            }
        }

        // 分页
        int cols = 4;
        int rows = 2;
        int perPage = cols * rows;
        int totalPages = Math.max(1, (filteredData.size() + perPage - 1) / perPage);
        state.encyclopediaPage = Math.min(state.encyclopediaPage, totalPages - 1);

        int startIdx = state.encyclopediaPage * perPage;
        int endIdx = Math.min(startIdx + perPage, filteredData.size());

        double cardW = 240;
        double cardH = 80;
        double gapX = 25;
        double gapY = 15;
        double totalW = cols * (cardW + gapX) - gapX;
        double startX = Constants.BASE_WIDTH / 2 - totalW / 2;
        double startY = 140;

        for (int i = startIdx; i < endIdx; i++) {
            int idx = i - startIdx;
            int col = idx % cols;
            int row = idx / cols;
            double cx = startX + col * (cardW + gapX);
            double cy = startY + row * (cardH + gapY);

            String[] data = filteredData.get(i);
            String type = data[2];
            Color bgColor;
            Color borderColor;
            if ("Boss".equals(type)) {
                bgColor = Color.rgb(80, 20, 20);
                borderColor = Color.rgb(200, 50, 50);
            } else if ("精英".equals(type)) {
                bgColor = Color.rgb(80, 60, 20);
                borderColor = Color.rgb(255, 165, 0);
            } else {
                bgColor = Color.rgb(40, 30, 60);
                borderColor = Constants.WHITE;
            }

            fillRect(gc, cx, cy, cardW, cardH, bgColor);
            strokeRect(gc, cx, cy, cardW, cardH, borderColor, 2);

            // 怪物名称
            drawText(gc, data[1] + " [" + type + "]", cx + 10, cy + 8, Constants.WHITE, 14);
            // 属性
            drawText(gc, "HP: " + data[3] + "  ATK: " + data[4], cx + 10, cy + 30, Color.rgb(200, 200, 200), 12);
            // 描述
            drawText(gc, data[5], cx + 10, cy + 50, Color.rgb(180, 180, 180), 11);
        }

        // 页码和翻页
        if (totalPages > 1) {
            drawText(gc, "第 " + (state.encyclopediaPage + 1) + " / " + totalPages + " 页",
                    Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 80, Constants.WHITE, 16, true);

            if (state.encyclopediaPage > 0) {
                fillRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                        Color.rgb(60, 80, 60));
                strokeRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                        Constants.GOLD, 2);
                drawText(gc, "◀ 上一页", Constants.BASE_WIDTH / 2 - 80, Constants.BASE_HEIGHT - 52,
                        Constants.WHITE, 14, true);
            }

            if (state.encyclopediaPage < totalPages - 1) {
                fillRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                        Color.rgb(60, 80, 60));
                strokeRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                        Constants.GOLD, 2);
                drawText(gc, "下一页 ▶", Constants.BASE_WIDTH / 2 + 80, Constants.BASE_HEIGHT - 52,
                        Constants.WHITE, 14, true);
            }
        }
    }

    /**
     * 绘制图鉴 - 道具模块。
     */
    private static void drawEncyclopediaItems(GraphicsContext gc, GameState state) {
        // 道具数据
        String[][] itemData = {
                {"hp_potion", "生命药水", "恢复20点生命值", "消耗品"},
                {"energy_potion", "能量药水", "获得2点能量", "消耗品"},
                {"block_potion", "护盾药水", "获得15点格挡", "消耗品"},
                {"strength_potion", "力量药水", "获得2点力量", "消耗品"},
                {"poison_potion", "毒药", "对敌人造成15点中毒", "消耗品"},
                {"fire_potion", "火焰药水", "对敌人造成20点灼烧", "消耗品"},
                {"explosive_potion", "爆炸药水", "对所有敌人造成10点伤害", "消耗品"},
                {"swift_potion", "敏捷药水", "获得2点敏捷", "消耗品"},
                {"guard_potion", "守护药水", "获得2点守护", "消耗品"},
                {"elixir", "万能药水", "移除所有负面效果", "消耗品"},
                {"smoke_bomb", "烟雾弹", "逃离战斗", "消耗品"},
                {"scroll", "力量卷轴", "永久获得1点力量", "永久"},
                {"armor_scroll", "护甲卷轴", "永久获得1点守护", "永久"},
                {"agility_scroll", "敏捷卷轴", "永久获得1点敏捷", "永久"},
                {"gold_coin", "幸运金币", "获得50金币", "金钱"},
                {"treasure_chest", "宝箱", "获得100金币", "金钱"},
                {"crystal_ball", "水晶球", "查看下一层地图", "特殊"},
                {"compass", "指南针", "选择下一层的节点类型", "特殊"}
        };

        // 分页
        int cols = 3;
        int rows = 3;
        int perPage = cols * rows;
        int totalPages = Math.max(1, (itemData.length + perPage - 1) / perPage);
        state.encyclopediaPage = Math.min(state.encyclopediaPage, totalPages - 1);

        int startIdx = state.encyclopediaPage * perPage;
        int endIdx = Math.min(startIdx + perPage, itemData.length);

        double cardW = 320;
        double cardH = 70;
        double gapX = 30;
        double gapY = 15;
        double totalW = cols * (cardW + gapX) - gapX;
        double startX = Constants.BASE_WIDTH / 2 - totalW / 2;
        double startY = 140;

        for (int i = startIdx; i < endIdx; i++) {
            int idx = i - startIdx;
            int col = idx % cols;
            int row = idx / cols;
            double cx = startX + col * (cardW + gapX);
            double cy = startY + row * (cardH + gapY);

            String[] data = itemData[i];
            String itemType = data[3];
            Color borderColor;
            switch (itemType) {
                case "消耗品" -> borderColor = Constants.GREEN;
                case "永久" -> borderColor = Constants.PURPLE;
                case "金钱" -> borderColor = Constants.GOLD;
                default -> borderColor = Constants.BLUE;
            }

            fillRect(gc, cx, cy, cardW, cardH, Color.rgb(40, 30, 60));
            strokeRect(gc, cx, cy, cardW, cardH, borderColor, 2);

            drawText(gc, data[1] + " [" + itemType + "]", cx + 15, cy + 10, borderColor, 16);
            drawText(gc, data[2], cx + 15, cy + 35, Constants.WHITE, 12);
        }

        // 页码和翻页
        if (totalPages > 1) {
            drawText(gc, "第 " + (state.encyclopediaPage + 1) + " / " + totalPages + " 页",
                    Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 80, Constants.WHITE, 16, true);

            if (state.encyclopediaPage > 0) {
                fillRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                        Color.rgb(60, 80, 60));
                strokeRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                        Constants.GOLD, 2);
                drawText(gc, "◀ 上一页", Constants.BASE_WIDTH / 2 - 80, Constants.BASE_HEIGHT - 52,
                        Constants.WHITE, 14, true);
            }

            if (state.encyclopediaPage < totalPages - 1) {
                fillRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                        Color.rgb(60, 80, 60));
                strokeRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                        Constants.GOLD, 2);
                drawText(gc, "下一页 ▶", Constants.BASE_WIDTH / 2 + 80, Constants.BASE_HEIGHT - 52,
                        Constants.WHITE, 14, true);
            }
        }
    }

    // ================================================================
    //  11. drawDeckView — 绘制卡组查看
    // ================================================================

    /**
     * 绘制卡组查看界面。
     * <p>
     * 显示玩家所有卡牌（抽牌堆+手牌+弃牌堆+消耗堆），每页 15 张（5列×3行）。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawDeckView(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 标题
        drawText(gc, "我的卡组", Constants.BASE_WIDTH / 2, 30, Constants.GOLD, 36, true);

        if (state.player instanceof Player player) {
            // 合并所有卡牌
            List<Card> allCards = new ArrayList<>();
            if (player.getDrawPile() != null) allCards.addAll(player.getDrawPile());
            if (player.getHand() != null) allCards.addAll(player.getHand());
            if (player.getDiscardPile() != null) allCards.addAll(player.getDiscardPile());
            if (player.getExhaustPile() != null) allCards.addAll(player.getExhaustPile());

            // 统计信息
            drawText(gc, "总计 " + allCards.size() + " 张", Constants.BASE_WIDTH / 2, 70,
                    Constants.WHITE, 20, true);
            String stats = "抽牌堆: " + (player.getDrawPile() != null ? player.getDrawPile().size() : 0)
                    + "  |  手牌: " + (player.getHand() != null ? player.getHand().size() : 0)
                    + "  |  弃牌堆: " + (player.getDiscardPile() != null ? player.getDiscardPile().size() : 0)
                    + "  |  消耗堆: " + (player.getExhaustPile() != null ? player.getExhaustPile().size() : 0);
            drawText(gc, stats, Constants.BASE_WIDTH / 2, 95, Color.rgb(200, 200, 200), 16, true);

            // 分页
            int cardsPerPage = 15;
            int cols = 5;
            int rows = 3;
            int totalPages = Math.max(1, (allCards.size() + cardsPerPage - 1) / cardsPerPage);
            state.deckViewPage = Math.min(state.deckViewPage, totalPages - 1);

            int startIdx = state.deckViewPage * cardsPerPage;
            List<Card> pageCards = allCards.subList(startIdx,
                    Math.min(startIdx + cardsPerPage, allCards.size()));

            // 绘制卡牌
            double cardW = 100;
            double cardH = 140;
            double gapX = 20;
            double gapY = 20;
            double startX = Constants.BASE_WIDTH / 2 - (cols * cardW + (cols - 1) * gapX) / 2;
            double startY = 130;

            for (int i = 0; i < pageCards.size(); i++) {
                int col = i % cols;
                int row = i / cols;
                double cx = startX + col * (cardW + gapX);
                double cy = startY + row * (cardH + gapY);
                drawCard(gc, pageCards.get(i), cx, cy, cardW, cardH, false, false);
            }

            // 页码
            if (totalPages > 1) {
                drawText(gc, (state.deckViewPage + 1) + " / " + totalPages,
                        Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 80,
                        Constants.WHITE, 20, true);

                if (state.deckViewPage > 0) {
                    fillRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                            Color.rgb(60, 80, 60));
                    strokeRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                            Constants.GOLD, 2);
                    drawText(gc, "◀ 上一页", Constants.BASE_WIDTH / 2 - 80, Constants.BASE_HEIGHT - 52,
                            Constants.WHITE, 14, true);
                }

                if (state.deckViewPage < totalPages - 1) {
                    fillRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                            Color.rgb(60, 80, 60));
                    strokeRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                            Constants.GOLD, 2);
                    drawText(gc, "下一页 ▶", Constants.BASE_WIDTH / 2 + 80, Constants.BASE_HEIGHT - 52,
                            Constants.WHITE, 14, true);
                }
            }
        }

        // 返回按钮
        fillRect(gc, Constants.BASE_WIDTH - 120, Constants.BASE_HEIGHT - 60, 100, 35,
                Color.rgb(100, 50, 50));
        strokeRect(gc, Constants.BASE_WIDTH - 120, Constants.BASE_HEIGHT - 60, 100, 35,
                Constants.GOLD, 2);
        drawText(gc, "◀ 返回", Constants.BASE_WIDTH - 70, Constants.BASE_HEIGHT - 52,
                Constants.WHITE, 16, true);
    }

    // ================================================================
    //  12. drawRelicView — 绘制遗物查看
    // ================================================================

    /**
     * 绘制遗物查看界面。
     * <p>
     * 显示玩家当前持有的所有遗物，每页 15 件（5列×3行）。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawRelicView(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 标题
        drawText(gc, "我的遗物", Constants.BASE_WIDTH / 2, 30, Constants.GOLD, 36, true);

        List<Relic> relics = state.player instanceof Player player
                ? (player.getRelics() != null ? player.getRelics() : List.of())
                : List.of();

        drawText(gc, "总计 " + relics.size() + " 件", Constants.BASE_WIDTH / 2, 70,
                Constants.WHITE, 20, true);

        if (relics.isEmpty()) {
            drawText(gc, "暂无遗物", Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT / 2,
                    Constants.GRAY, 28, true);
        } else {
            // 分页
            int relicsPerPage = 15;
            int cols = 5;
            int rows = 3;
            int totalPages = Math.max(1, (relics.size() + relicsPerPage - 1) / relicsPerPage);
            state.relicViewPage = Math.min(state.relicViewPage, totalPages - 1);

            int startIdx = state.relicViewPage * relicsPerPage;
            List<Relic> pageRelics = relics.subList(startIdx,
                    Math.min(startIdx + relicsPerPage, relics.size()));

            // 遗物卡片布局
            double relicW = 220;
            double relicH = 85;
            double gapX = 12;
            double gapY = 15;
            double startX = Constants.BASE_WIDTH / 2 - (cols * relicW + (cols - 1) * gapX) / 2;
            double startY = 110;

            for (int i = 0; i < pageRelics.size(); i++) {
                int col = i % cols;
                int row = i / cols;
                double rlx = startX + col * (relicW + gapX);
                double rly = startY + row * (relicH + gapY);

                // 遗物卡片背景
                fillRect(gc, rlx, rly, relicW, relicH, Color.rgb(50, 30, 80));
                strokeRect(gc, rlx, rly, relicW, relicH, Constants.GOLD, 2);

                // 遗物名称
                drawText(gc, "✦ " + getRelicName(pageRelics.get(i)), rlx + 10, rly + 8,
                        Constants.GOLD, 14);
                // 描述
                drawText(gc, pageRelics.get(i).getDescKey(), rlx + 10, rly + 32,
                        Constants.WHITE, 11);
            }

            // 页码
            if (totalPages > 1) {
                drawText(gc, (state.relicViewPage + 1) + " / " + totalPages,
                        Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 80,
                        Constants.WHITE, 20, true);

                if (state.relicViewPage > 0) {
                    fillRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                            Color.rgb(60, 80, 60));
                    strokeRect(gc, Constants.BASE_WIDTH / 2 - 120, Constants.BASE_HEIGHT - 60, 80, 35,
                            Constants.GOLD, 2);
                    drawText(gc, "◀ 上一页", Constants.BASE_WIDTH / 2 - 80, Constants.BASE_HEIGHT - 52,
                            Constants.WHITE, 14, true);
                }

                if (state.relicViewPage < totalPages - 1) {
                    fillRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                            Color.rgb(60, 80, 60));
                    strokeRect(gc, Constants.BASE_WIDTH / 2 + 40, Constants.BASE_HEIGHT - 60, 80, 35,
                            Constants.GOLD, 2);
                    drawText(gc, "下一页 ▶", Constants.BASE_WIDTH / 2 + 80, Constants.BASE_HEIGHT - 52,
                            Constants.WHITE, 14, true);
                }
            }
        }

        // 返回按钮
        fillRect(gc, Constants.BASE_WIDTH - 120, Constants.BASE_HEIGHT - 60, 100, 35,
                Color.rgb(100, 50, 50));
        strokeRect(gc, Constants.BASE_WIDTH - 120, Constants.BASE_HEIGHT - 60, 100, 35,
                Constants.GOLD, 2);
        drawText(gc, "◀ 返回", Constants.BASE_WIDTH - 70, Constants.BASE_HEIGHT - 52,
                Constants.WHITE, 16, true);
    }

    // ================================================================
    //  13. drawCharacterSelect — 绘制角色选择
    // ================================================================

    /**
     * 绘制角色选择界面。
     * <p>
     * 2x2 布局展示四个角色卡片（战士/法师/盗贼/圣女），
     * 每个卡片包含角色图片、名称、描述、等级、经验条、生命值、专属卡牌、初始遗物。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawCharacterSelect(GraphicsContext gc, GameState state) {
        // 清屏并绘制背景
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 绘制背景壁纸
        Image bgImage = ResourceManager.get().loadImage("shenyuan.png");
        if (bgImage != null) {
            gc.drawImage(bgImage, 0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());
        }

        // 顶部标题栏
        fillRect(gc, 0, 0, Constants.BASE_WIDTH, 60, Constants.PURPLE);
        drawText(gc, "选择你的英雄", Constants.BASE_WIDTH / 2, 15, Constants.GOLD, 32, true);

        // ── 角色数据 ──
        String[] charKeys = {"warrior", "mage", "rogue", "priest"};
        String[] charNames = {"战士", "法师", "盗贼", "圣女"};
        String[] charDescs = {"高生命·物理攻击·防御专精", "法术攻击·额外抽牌·元素精通",
                "低费连击·金币加成·致命一击", "神圣审判·回合增伤·群体压制"};
        int[] baseHp = {80, 70, 70, 70};
        // 专属卡牌数据: [nameKey, cost, type]
        String[][][] exclusiveCards = {
            {{"uppercut", "2", "攻击"}, {"bash", "2", "攻击"}},
            {{"fireball", "2", "攻击"}, {"ice_spike", "1", "攻击"}},
            {{"oppression", "0", "攻击"}, {"buy", "0", "技能"}},
            {{"judgment", "X", "攻击"}, {"holy_shield", "2", "技能"}}
        };
        String[] relicKeys = {"burning_blood", "ring_of_snake", "golden_fleece", "scepter"};
        Color[] charColors = {Color.rgb(220, 50, 50), Color.rgb(50, 100, 220),
                Color.rgb(50, 180, 80), Color.rgb(220, 200, 50)};
        Color[] charDarkColors = {Color.rgb(120, 30, 30), Color.rgb(30, 50, 120),
                Color.rgb(30, 100, 50), Color.rgb(120, 100, 20)};

        // 加载角色进度
        Map<String, Map<String, Integer>> progress = SaveSystem.loadCharacterProgress();
        LangManager lang = LangManager.getInstance();

        // 2x2 布局
        double cardWidth = 520;
        double cardHeight = 220;
        double gapX = 40;
        double gapY = 20;
        double totalW = cardWidth * 2 + gapX;
        double startX = (Constants.BASE_WIDTH - totalW) / 2;
        double startY = 80;

        for (int i = 0; i < 4; i++) {
            int col = i % 2;
            int row = i / 2;
            double cx = startX + col * (cardWidth + gapX);
            double cy = startY + row * (cardHeight + gapY);
            boolean isSelected = state.selectedCharacter != null
                    && state.selectedCharacter.equals(SELECTABLE_CLASSES[i].getValue());

            // ── 卡片背景 ──
            fillRect(gc, cx, cy, cardWidth, cardHeight, charDarkColors[i]);
            Color borderColor = isSelected ? Constants.GOLD : charColors[i];
            double borderW = isSelected ? 5 : 3;
            strokeRect(gc, cx, cy, cardWidth, cardHeight, borderColor, borderW);

            // ── 左栏：角色图片（130x182，5:7 比例） ──
            Image portrait = ResourceManager.get().getCharacterPortrait(charKeys[i]);
            if (portrait != null) {
                double imgW = 130;
                double imgH = 182;
                double imgX = cx + 20;
                double imgY = cy + (cardHeight - imgH) / 2;
                gc.drawImage(portrait, imgX, imgY, imgW, imgH);
            } else {
                drawText(gc, charNames[i].substring(0, 1), cx + 80, cy + cardHeight / 2,
                        Constants.WHITE, 48, true);
            }

            // 选中标记
            if (isSelected) {
                Image checkMark = ResourceManager.get().getCheckMark(charKeys[i]);
                if (checkMark != null) {
                    gc.drawImage(checkMark, cx + cardWidth - 40, cy + 10, 30, 30);
                }
            }

            // ── 右栏：角色信息 ──
            double rightX = cx + 160;
            double y = cy + 14;

            // 角色名称
            drawText(gc, charNames[i], rightX, y, Constants.WHITE, 20);
            y += 30;

            // 等级 + HP（同一行，等级左对齐，HP右对齐）
            Map<String, Integer> charProgress = progress.getOrDefault(charKeys[i],
                    new LinkedHashMap<>(Map.of("level", 1, "exp", 0)));
            int level = charProgress.getOrDefault("level", 1);
            int exp = charProgress.getOrDefault("exp", 0);
            int expNeeded = (level < 10) ? SaveSystem.getExpForLevel(level) : 0;

            String levelText = (level < 10) ? "Lv." + level : "Lv.MAX";
            drawText(gc, levelText, rightX, y, Constants.GOLD, 14);

            // HP 右对齐
            String hpText = "HP: " + (baseHp[i] + (level - 1) * 2);
            gc.setTextAlign(TextAlignment.RIGHT);
            drawText(gc, hpText, cx + cardWidth - 18, y, Constants.WHITE, 14);
            gc.setTextAlign(TextAlignment.LEFT);
            y += 22;

            // 经验条
            double barWidth = cardWidth - 180;
            double barHeight = 8;
            double barX = rightX;
            fillRect(gc, barX, y, barWidth, barHeight, Color.rgb(40, 40, 60));
            if (level < 10) {
                double expPercent = Math.min((double) exp / expNeeded, 1.0);
                if (expPercent > 0) {
                    fillRect(gc, barX, y, barWidth * expPercent, barHeight, Color.rgb(0, 200, 255));
                }
                drawText(gc, exp + "/" + expNeeded, barX + barWidth / 2, y, Constants.WHITE, 8, true);
            } else {
                fillRect(gc, barX, y, barWidth, barHeight, Constants.GOLD);
                drawText(gc, "MAX", barX + barWidth / 2, y, Constants.WHITE, 8, true);
            }
            y += 14;

            // 分隔线
            gc.setStroke(Color.rgb(255, 255, 255, 0.24));
            gc.setLineWidth(1);
            gc.strokeLine(Constants.rx(rightX), Constants.ry(y),
                    Constants.rx(cx + cardWidth - 18), Constants.ry(y));
            y += 8;

            // 专属卡牌标签
            drawText(gc, "专属卡牌", rightX, y, Color.rgb(255, 220, 120), 12);
            y += 16;

            // 两张专属卡牌横排
            double cardGap = 8;
            double miniW = (cardWidth - 180 - cardGap) / 2;
            double miniH = 32;
            for (int j = 0; j < 2; j++) {
                double miniCardX = rightX + j * (miniW + cardGap);
                String[] cardData = exclusiveCards[i][j];
                // 卡牌背景
                fillRect(gc, miniCardX, y, miniW, miniH, Constants.DARK_PURPLE);
                strokeRect(gc, miniCardX, y, miniW, miniH, charColors[i], 2);
                // 费用小圆
                fillRect(gc, miniCardX + 4, y + 4, 16, 16, Color.rgb(10, 10, 20));
                drawText(gc, cardData[1], miniCardX + 12, y + 3, Constants.GOLD, 12, true);
                // 卡牌名称
                String cardName = lang.getText("cards." + cardData[0], cardData[0]);
                drawText(gc, cardName, miniCardX + 26, y + 3, Constants.WHITE, 12);
                // 卡牌类型
                drawText(gc, cardData[2], miniCardX + 26, y + 19, Color.rgb(200, 200, 200), 10);
            }
            y += miniH + 6;

            // 分隔线
            gc.setStroke(Color.rgb(255, 255, 255, 0.24));
            gc.setLineWidth(1);
            gc.strokeLine(Constants.rx(rightX), Constants.ry(y),
                    Constants.rx(cx + cardWidth - 18), Constants.ry(y));
            y += 8;

            // 初始遗物标签
            drawText(gc, "初始遗物", rightX, y, Color.rgb(255, 220, 120), 12);
            y += 14;

            // 遗物框
            double relicW = cardWidth - 180;
            double relicH = 32;
            fillRect(gc, rightX, y, relicW, relicH, Color.rgb(40, 35, 55));
            strokeRect(gc, rightX, y, relicW, relicH, Constants.GOLD, 2);

            String relicName = lang.getText("relics." + relicKeys[i], relicKeys[i]);
            String relicDesc = lang.getText("relics_desc." + relicKeys[i], "");
            drawText(gc, "✦", rightX + 10, y + 3, Constants.GOLD, 16);
            drawText(gc, relicName, rightX + 28, y + 3, Constants.GOLD, 12);
            drawText(gc, relicDesc, rightX + 28, y + 18, Color.rgb(220, 220, 220), 10);
        }

        // ── 底部按钮 ──
        double btnY = Constants.BASE_HEIGHT - 60;
        double btnW = 120;
        double btnH = 40;
        double btnGap = 80;

        // 提示文字
        String hint = "点击角色卡片选择，然后点击确定开始冒险";
        if (state.selectedCharacter != null) {
            String selectedName = lang.getText("character_select." + state.selectedCharacter,
                    state.selectedCharacter);
            hint = "已选择: " + selectedName;
        }
        drawText(gc, hint, Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 20,
                state.selectedCharacter != null ? Constants.GOLD : Constants.GRAY, 16, true);

        // 返回按钮
        double backBtnX = Constants.BASE_WIDTH / 2 - btnGap / 2 - btnW;
        fillAlphaRect(gc, backBtnX, btnY, btnW, btnH, Color.rgb(80, 60, 60, 0.78));
        strokeRect(gc, backBtnX, btnY, btnW, btnH, Constants.WHITE, 2);
        drawText(gc, "返回", backBtnX + btnW / 2, btnY + 10, Constants.WHITE, 18, true);

        // 确定按钮
        boolean hasSelection = state.selectedCharacter != null;
        Color confirmColor = hasSelection ? Color.rgb(60, 120, 60, 0.78) : Color.rgb(80, 80, 80, 0.78);
        Color confirmBorder = hasSelection ? Constants.GOLD : Constants.GRAY;
        double confirmBtnX = Constants.BASE_WIDTH / 2 + btnGap / 2;
        fillAlphaRect(gc, confirmBtnX, btnY, btnW, btnH, confirmColor);
        strokeRect(gc, confirmBtnX, btnY, btnW, btnH, confirmBorder, 2);
        drawText(gc, "确定", confirmBtnX + btnW / 2, btnY + 10,
                hasSelection ? Constants.WHITE : Constants.GRAY, 18, true);
    }

    // ================================================================
    //  14. drawCharacterBuild — 绘制角色加点
    // ================================================================

    /**
     * 绘制角色加点界面。
     * <p>
     * 包含：可用加点数、四项属性（力量/敏捷/守护/抽牌）及其 +/- 按钮、开始冒险按钮。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawCharacterBuild(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 标题
        drawText(gc, "角色加点", Constants.BASE_WIDTH / 2, 50, Constants.GOLD, 48, true);
        drawText(gc, "可用加点: " + state.buildPoints, Constants.BASE_WIDTH / 2, 120,
                Constants.WHITE, 24, true);

        // 玩家信息面板
        if (state.player instanceof Player player) {
            drawPlayerInfo(gc, player, 50, Constants.BASE_HEIGHT - 250, 200, 175);
        }

        // 加点选项
        String[] keys = {"strength", "dexterity", "guard", "draw"};
        String[] names = {"力量+1", "敏捷+1", "守护+1", "回合初始抽牌+1"};
        String[] descs = {"攻击伤害+1", "格挡获取+1", "受到伤害-1", "每回合开始时抽牌+1"};
        int[] costs = {1, 1, 1, 2};
        Color[] colors = {Color.rgb(200, 50, 50), Color.rgb(50, 200, 100),
                Color.rgb(50, 150, 200), Color.rgb(150, 50, 200)};

        double optionY = 180;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            int cur = state.buildAlloc.getOrDefault(key, 0);
            boolean canAdd = state.buildPoints >= costs[i];
            boolean canSub = cur > 0;

            double optionX = Constants.BASE_WIDTH / 2 - 300;

            // 选项背景
            fillRect(gc, optionX, optionY, 600, 80, Color.rgb(50, 30, 80));
            Color borderColor = (canAdd || canSub) ? colors[i] : Color.rgb(80, 80, 80);
            strokeRect(gc, optionX, optionY, 600, 80, borderColor, 2);

            // 名称和描述
            drawText(gc, names[i], optionX + 20, optionY + 15, Constants.WHITE, 24);
            drawText(gc, descs[i] + " (消耗" + costs[i] + "点)", optionX + 20, optionY + 45,
                    Constants.GRAY, 16);

            // - 按钮
            double minusBtnX = optionX + 400;
            double minusBtnY = optionY + 20;
            Color minusColor = canSub ? Color.rgb(180, 50, 50) : Color.rgb(80, 50, 50);
            fillRect(gc, minusBtnX, minusBtnY, 40, 40, minusColor);
            strokeRect(gc, minusBtnX, minusBtnY, 40, 40, Constants.WHITE, 2);
            drawText(gc, "-", minusBtnX + 14, minusBtnY + 6, Constants.WHITE, 28);

            // 当前值
            drawText(gc, String.valueOf(cur), minusBtnX + 70, optionY + 22, Constants.GOLD, 28, true);

            // + 按钮
            double plusBtnX = optionX + 510;
            Color plusColor = canAdd ? Color.rgb(50, 150, 50) : Color.rgb(50, 80, 50);
            fillRect(gc, plusBtnX, minusBtnY, 40, 40, plusColor);
            strokeRect(gc, plusBtnX, minusBtnY, 40, 40, Constants.WHITE, 2);
            drawText(gc, "+", plusBtnX + 10, minusBtnY + 6, Constants.WHITE, 28);

            optionY += 100;
        }

        // 开始冒险按钮
        double startBtnX = Constants.BASE_WIDTH / 2 - 100;
        double startBtnY = Constants.BASE_HEIGHT - 80;
        fillRect(gc, startBtnX, startBtnY, 200, 50, Color.rgb(100, 50, 150));
        strokeRect(gc, startBtnX, startBtnY, 200, 50, Constants.GOLD, 2);
        drawText(gc, "开始冒险", Constants.BASE_WIDTH / 2, startBtnY + 15,
                Constants.WHITE, 24, true);
    }

    // ================================================================
    //  15. drawGameOver — 绘制游戏结束
    // ================================================================

    /**
     * 绘制游戏结束画面。
     * <p>
     * 包含：胜利/失败标题、最终得分、获得经验与升级信息、重新开始/退出按钮。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawGameOver(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.BLACK);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 结果标题
        if ("win".equals(state.result)) {
            drawText(gc, "恭喜通关!", Constants.BASE_WIDTH / 2, 150, Constants.GOLD, 72, true);
        } else {
            drawText(gc, "游戏结束", Constants.BASE_WIDTH / 2, 150, Constants.RED, 72, true);
        }

        // 得分
        int score = state.currentFloor * 100
                + (state.player instanceof Player player ? player.getGold() : 0);
        drawText(gc, "最终得分 " + score, Constants.BASE_WIDTH / 2, 250, Constants.WHITE, 32, true);

        // 按钮
        double btnW = 200;
        double btnH = 50;
        double btnY = 430;
        double gap = 40;

        // 重新开始按钮
        double restartX = Constants.BASE_WIDTH / 2 - btnW - gap / 2;
        fillRect(gc, restartX, btnY, btnW, btnH, Color.rgb(30, 90, 45));
        strokeRect(gc, restartX, btnY, btnW, btnH, Constants.GREEN, 2);
        drawText(gc, "重新开始", restartX + btnW / 2, btnY + 14, Constants.WHITE, 22, true);

        // 退出游戏按钮
        double quitX = Constants.BASE_WIDTH / 2 + gap / 2;
        fillRect(gc, quitX, btnY, btnW, btnH, Color.rgb(100, 30, 30));
        strokeRect(gc, quitX, btnY, btnW, btnH, Constants.RED, 2);
        drawText(gc, "退出游戏", quitX + btnW / 2, btnY + 14, Constants.WHITE, 22, true);
    }

    // ================================================================
    //  16. drawModeSelect — 绘制模式选择
    // ================================================================

    /**
     * 绘制模式选择界面。
     * <p>
     * 包含："选择游戏模式"标题、普通模式/BOSS连战/自由模式/返回 按钮。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawModeSelect(GraphicsContext gc, GameState state) {
        // 清屏并绘制背景
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 绘制背景壁纸
        Image bgImage = ResourceManager.get().loadImage("shenyuan.png");
        if (bgImage != null) {
            gc.drawImage(bgImage, 0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());
        }

        // 标题
        drawText(gc, "选择游戏模式", Constants.BASE_WIDTH / 2, 150, Constants.GOLD, 48, true);

        // 按钮
        double btnW = 300;
        double btnH = 80;
        double btnX = Constants.BASE_WIDTH / 2 - btnW / 2;

        // 普通模式
        double normalY = 300;
        fillAlphaRect(gc, btnX, normalY, btnW, btnH, Color.rgb(60, 40, 90, 0.78));
        strokeRect(gc, btnX, normalY, btnW, btnH, Constants.WHITE, 2);
        drawText(gc, "普通模式", Constants.BASE_WIDTH / 2, normalY + 18, Constants.WHITE, 28, true);
        drawText(gc, "体验完整的冒险", Constants.BASE_WIDTH / 2, normalY + 50,
                Color.rgb(220, 220, 220), 14, true);

        // BOSS连战
        double bossY = 400;
        fillAlphaRect(gc, btnX, bossY, btnW, btnH, Color.rgb(90, 30, 30, 0.78));
        strokeRect(gc, btnX, bossY, btnW, btnH, Constants.GOLD, 2);
        drawText(gc, "BOSS连战", Constants.BASE_WIDTH / 2, bossY + 18, Constants.GOLD, 28, true);
        drawText(gc, "连续挑战BOSS", Constants.BASE_WIDTH / 2, bossY + 50,
                Color.rgb(220, 220, 220), 14, true);

        // 自由模式
        double freeY = 500;
        fillAlphaRect(gc, btnX, freeY, btnW, btnH, Color.rgb(30, 70, 100, 0.78));
        strokeRect(gc, btnX, freeY, btnW, btnH, Color.rgb(100, 180, 220), 2);
        drawText(gc, "自由模式", Constants.BASE_WIDTH / 2, freeY + 18,
                Color.rgb(150, 220, 255), 28, true);
        drawText(gc, "任意选择卡牌和遗物", Constants.BASE_WIDTH / 2, freeY + 50,
                Color.rgb(220, 220, 220), 14, true);

        // 返回按钮
        double backBtnW = 150;
        double backBtnH = 40;
        double backBtnX = Constants.BASE_WIDTH / 2 - backBtnW / 2;
        double backBtnY = 600;
        fillAlphaRect(gc, backBtnX, backBtnY, backBtnW, backBtnH, Color.rgb(80, 40, 40, 0.78));
        strokeRect(gc, backBtnX, backBtnY, backBtnW, backBtnH, Constants.WHITE, 2);
        drawText(gc, "返回", Constants.BASE_WIDTH / 2, backBtnY + 10, Constants.WHITE, 18, true);
    }

    // ================================================================
    //  17. drawTutorial — 绘制新手指引覆盖层
    // ================================================================

    /**
     * 绘制新手指引覆盖层。
     * <p>
     * 半透明遮罩 + 高亮区域 + 文字说明框 + 进度指示/按钮。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawTutorial(GraphicsContext gc, GameState state) {
        // 全屏半透明遮罩
        gc.setGlobalAlpha(0.6);
        gc.setFill(Color.rgb(0, 0, 0));
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());
        gc.setGlobalAlpha(1.0);

        // 高亮区域和箭头指示（根据步骤）
        double highlightX = 0, highlightY = 0, highlightW = 0, highlightH = 0;
        String highlightLabel = "";

        // 根据步骤设置高亮区域
        switch (state.tutorialStep) {
            case 0 -> {
                // Step 0: 欢迎 — 高亮地图区域
                highlightX = 300; highlightY = 100; highlightW = 600; highlightH = 500;
                highlightLabel = "地图区域";
            }
            case 1 -> {
                // Step 1: 卡牌类型 — 高亮手牌区
                highlightX = 100; highlightY = 640; highlightW = 800; highlightH = 160;
                highlightLabel = "手牌区";
            }
            case 2 -> {
                // Step 2: 战斗基础 — 高亮敌人区域
                highlightX = 300; highlightY = 50; highlightW = 600; highlightH = 300;
                highlightLabel = "敌人区域";
            }
            case 3 -> {
                // Step 3: 能量和回合 — 高亮能量区
                highlightX = 1050; highlightY = 450; highlightW = 150; highlightH = 200;
                highlightLabel = "能量/牌堆信息";
            }
            case 4 -> {
                // Step 4: 遗物和道具 — 高亮玩家信息面板
                highlightX = 20; highlightY = 480; highlightW = 200; highlightH = 175;
                highlightLabel = "玩家信息";
            }
            case 5 -> {
                // Step 5: 地图导航 — 高亮地图节点
                highlightX = 200; highlightY = 100; highlightW = 800; highlightH = 500;
                highlightLabel = "地图节点";
            }
        }

        // 绘制高亮区域（在遮罩上挖空）
        if (highlightW > 0 && highlightH > 0) {
            // 清除高亮区域的遮罩（通过绘制透明区域实现）
            gc.setGlobalAlpha(1.0);
            gc.setFill(Color.rgb(0, 0, 0, 0.0));
            gc.setStroke(Constants.GOLD);
            gc.setLineWidth(Constants.rs(3));
            double sx = Constants.rx(highlightX);
            double sy = Constants.ry(highlightY);
            double sw = Constants.rs(highlightW);
            double sh = Constants.rs(highlightH);
            gc.clearRect(sx - Constants.rs(2), sy - Constants.rs(2),
                    sw + Constants.rs(4), sh + Constants.rs(4));
            gc.strokeRect(sx, sy, sw, sh);

            // 高亮标签
            drawText(gc, "▼ " + highlightLabel, highlightX + highlightW / 2,
                    highlightY - 10, Constants.GOLD, 18, true);
        }

        // 步骤说明文字框
        String[][] tutorialSteps = {
                {
                        "欢迎来到深渊行者",
                        "这是一款Roguelike卡牌游戏，你将扮演英雄深入深渊。",
                        "在每一层，你可以选择不同的路径推进。",
                        "击败敌人获取金币和卡牌，不断强化你的卡组。",
                        "最终击败Boss，通关深渊！"
                },
                {
                        "卡牌类型说明",
                        "攻击牌（红色）: 对敌人造成伤害",
                        "技能牌（蓝色）: 提供格挡、治疗等辅助效果",
                        "能力牌（紫色）: 提供持续整场战斗的增益效果",
                        "诅咒牌（暗紫色）: 负面效果，占据手牌位置"
                },
                {
                        "战斗基础",
                        "点击手牌中的卡牌选中它",
                        "然后点击敌人来使用攻击牌",
                        "技能牌通常点击自身或自动释放",
                        "击败所有敌人即可获得奖励"
                },
                {
                        "能量和回合",
                        "每回合开始获得3点能量",
                        "使用卡牌需要消耗对应的能量",
                        "能量用完后点击\"结束回合\"按钮",
                        "合理分配能量是战斗的关键"
                },
                {
                        "遗物和道具",
                        "遗物（✦）提供永久性的被动效果",
                        "击败精英怪和Boss可获得遗物",
                        "道具是消耗品，可在商店购买",
                        "合理搭配遗物和道具可以大幅提升战力"
                },
                {
                        "地图导航",
                        "地图上每个节点代表一个事件",
                        "战斗（红）: 获得金币和卡牌",
                        "商店（金）: 购买卡牌和遗物",
                        "休息（绿）: 恢复50%生命值"
                }
        };

        // 文字说明框（居中偏下，不遮挡高亮区域）
        double boxW = 500;
        double boxH = 220;
        double boxX = Constants.rx(Constants.BASE_WIDTH / 2) - Constants.rs(boxW) / 2;
        double boxY = Constants.ry(Constants.BASE_HEIGHT - 250);

        // 背景
        gc.setFill(Color.rgb(20, 15, 35, 0.9));
        gc.fillRoundRect(boxX, boxY, Constants.rs(boxW), Constants.rs(boxH),
                Constants.rs(12), Constants.rs(12));
        gc.setStroke(Constants.GOLD);
        gc.setLineWidth(Constants.rs(3));
        gc.strokeRoundRect(boxX, boxY, Constants.rs(boxW), Constants.rs(boxH),
                Constants.rs(12), Constants.rs(12));

        // 步骤标题
        int step = Math.min(state.tutorialStep, tutorialSteps.length - 1);
        String[] stepContent = tutorialSteps[step];
        drawText(gc, stepContent[0], Constants.BASE_WIDTH / 2,
                Constants.BASE_HEIGHT - 230, Constants.GOLD, 22, true);

        // 说明文字
        double textY = Constants.BASE_HEIGHT - 200;
        for (int i = 1; i < stepContent.length; i++) {
            drawText(gc, stepContent[i], Constants.BASE_WIDTH / 2, textY, Constants.WHITE, 14, true);
            textY += 22;
        }

        // 进度指示
        drawText(gc, "步骤 " + (state.tutorialStep + 1) + " / " + tutorialSteps.length,
                Constants.BASE_WIDTH / 2, Constants.BASE_HEIGHT - 40, Color.rgb(180, 180, 180), 13, true);

        // 底部按钮
        double btnW = 120;
        double btnH = 36;
        double btnY2 = Constants.BASE_HEIGHT - 70;

        // 下一步按钮
        if (state.tutorialStep < tutorialSteps.length - 1) {
            double nextBtnX = Constants.BASE_WIDTH / 2 + 10;
            fillRect(gc, nextBtnX, btnY2, btnW, btnH, Color.rgb(60, 100, 200));
            strokeRect(gc, nextBtnX, btnY2, btnW, btnH, Constants.WHITE, 1);
            drawText(gc, "下一步", nextBtnX + btnW / 2, btnY2 + 8, Constants.WHITE, 16, true);
        } else {
            // 最后一步显示"完成"
            double nextBtnX = Constants.BASE_WIDTH / 2 + 10;
            fillRect(gc, nextBtnX, btnY2, btnW, btnH, Color.rgb(60, 150, 60));
            strokeRect(gc, nextBtnX, btnY2, btnW, btnH, Constants.WHITE, 1);
            drawText(gc, "完成", nextBtnX + btnW / 2, btnY2 + 8, Constants.WHITE, 16, true);
        }

        // 跳过按钮
        double skipBtnX = Constants.BASE_WIDTH / 2 - btnW - 10;
        fillRect(gc, skipBtnX, btnY2, btnW, btnH, Color.rgb(80, 60, 80));
        strokeRect(gc, skipBtnX, btnY2, btnW, btnH, Constants.GOLD, 1);
        drawText(gc, "跳过", skipBtnX + btnW / 2, btnY2 + 8, Constants.WHITE, 16, true);
    }

    // ================================================================
    //  18. drawBossRushSetup — 绘制BOSS连战模式准备界面
    // ================================================================

    /**
     * 绘制BOSS连战模式准备界面。
     * 包含：候选卡牌池（6张选择3张）、候选遗物池（3个选择1个）、BOSS预览。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawBossRushSetup(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 标题
        drawText(gc, "BOSS连战 - 准备", Constants.BASE_WIDTH / 2, 30, Constants.GOLD, 40, true);

        // 选择卡牌提示
        drawText(gc, "选择3张初始卡牌", Constants.BASE_WIDTH / 2, 70, Constants.WHITE, 20, true);

        // 候选卡牌池（6张）
        if (state.bossRushCardPool != null && !state.bossRushCardPool.isEmpty()) {
            double cardW = 100;
            double cardH = 140;
            double gapX = 15;
            double totalW = Math.min(6, state.bossRushCardPool.size()) * (cardW + gapX) - gapX;
            double startX = Constants.BASE_WIDTH / 2 - totalW / 2;

            for (int i = 0; i < Math.min(6, state.bossRushCardPool.size()); i++) {
                Object obj = state.bossRushCardPool.get(i);
                double cx = startX + i * (cardW + gapX);
                if (obj instanceof Card card) {
                    boolean selected = state.bossRushSelectedCards.contains(card);
                    drawCard(gc, card, cx, 100, cardW, cardH, selected, false);
                    if (selected) {
                        strokeRect(gc, cx - 3, 97, cardW + 6, cardH + 6, Constants.GOLD, 3);
                    }
                } else {
                    fillRect(gc, cx, 100, cardW, cardH, Constants.DARK_PURPLE);
                    strokeRect(gc, cx, 100, cardW, cardH, Constants.WHITE, 2);
                    drawText(gc, "卡牌", cx + cardW / 2, 170, Constants.WHITE, 14, true);
                }
            }
        }

        // 选择遗物提示
        drawText(gc, "选择1个初始遗物", Constants.BASE_WIDTH / 2, 270, Constants.WHITE, 20, true);

        // 候选遗物池（3个）
        if (state.bossRushRelicPool != null && !state.bossRushRelicPool.isEmpty()) {
            double relicW = 220;
            double relicH = 70;
            double gapX = 20;
            double totalW = Math.min(3, state.bossRushRelicPool.size()) * (relicW + gapX) - gapX;
            double startX = Constants.BASE_WIDTH / 2 - totalW / 2;

            for (int i = 0; i < Math.min(3, state.bossRushRelicPool.size()); i++) {
                Object obj = state.bossRushRelicPool.get(i);
                double rx = startX + i * (relicW + gapX);
                boolean selected = obj == state.bossRushSelectedRelic;

                Color bg = selected ? Color.rgb(60, 40, 100) : Color.rgb(40, 25, 70);
                Color border = selected ? Constants.GOLD : Constants.WHITE;
                fillRect(gc, rx, 300, relicW, relicH, bg);
                strokeRect(gc, rx, 300, relicW, relicH, border, 2);

                if (obj instanceof Relic relic) {
                    drawText(gc, "✦ " + getRelicName(relic), rx + 10, 310, Constants.GOLD, 16);
                    drawText(gc, getRelicDesc(relic), rx + 10, 335, Constants.WHITE, 12);
                } else {
                    drawText(gc, "✦ 遗物", rx + relicW / 2, 335, Constants.GOLD, 16, true);
                }
            }
        }

        // BOSS顺序预览
        drawText(gc, "BOSS顺序", Constants.BASE_WIDTH / 2, 410, Constants.WHITE, 20, true);
        if (state.bossRushBossOrder != null) {
            double bossY = 430;
            double bossX = Constants.BASE_WIDTH / 2 - (state.bossRushBossOrder.size() * 120) / 2;
            for (int i = 0; i < state.bossRushBossOrder.size(); i++) {
                String bossName = state.bossRushBossOrder.get(i);
                fillRect(gc, bossX + i * 120, bossY, 110, 50, Color.rgb(80, 20, 20));
                strokeRect(gc, bossX + i * 120, bossY, 110, 50, Constants.GOLD, 2);
                drawText(gc, bossName, bossX + i * 120 + 55, bossY + 18, Constants.WHITE, 12, true);
            }
        }

        // 底部按钮
        double btnY = Constants.BASE_HEIGHT - 60;
        boolean canStart = state.bossRushSelectedCards.size() == 3 && state.bossRushSelectedRelic != null;

        fillRect(gc, Constants.BASE_WIDTH / 2 - 120, btnY, 120, 40, Color.rgb(80, 60, 60));
        strokeRect(gc, Constants.BASE_WIDTH / 2 - 120, btnY, 120, 40, Constants.WHITE, 2);
        drawText(gc, "返回", Constants.BASE_WIDTH / 2 - 60, btnY + 10, Constants.WHITE, 18, true);

        Color startColor = canStart ? Color.rgb(60, 120, 60) : Color.rgb(80, 80, 80);
        Color startBorder = canStart ? Constants.GOLD : Constants.GRAY;
        fillRect(gc, Constants.BASE_WIDTH / 2, btnY, 120, 40, startColor);
        strokeRect(gc, Constants.BASE_WIDTH / 2, btnY, 120, 40, startBorder, 2);
        drawText(gc, "开始", Constants.BASE_WIDTH / 2 + 60, btnY + 10,
                canStart ? Constants.WHITE : Constants.GRAY, 18, true);
    }

    // ================================================================
    //  19. drawFreeSetup — 绘制自由模式准备界面
    // ================================================================

    /**
     * 绘制自由模式准备界面。
     * 包含：卡牌选择（分页）、遗物选择（分页）、BOSS选择。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawFreeSetup(GraphicsContext gc, GameState state) {
        // 清屏
        gc.setFill(Constants.DARK_PURPLE);
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());

        // 标题
        drawText(gc, "自由模式 - 准备", Constants.BASE_WIDTH / 2, 30, Constants.GOLD, 40, true);

        // 卡牌选择区域
        drawText(gc, "选择卡牌（点击切换）", Constants.BASE_WIDTH / 2, 70, Constants.WHITE, 20, true);

        if (state.freeCardPool != null && !state.freeCardPool.isEmpty()) {
            int cols = 6;
            int rows = 3;
            int perPage = cols * rows;
            int totalPages = Math.max(1, (state.freeCardPool.size() + perPage - 1) / perPage);
            state.freeSetupPage = Math.min(state.freeSetupPage, totalPages - 1);

            int startIdx = state.freeSetupPage * perPage;
            List<Object> pageCards = state.freeCardPool.subList(startIdx,
                    Math.min(startIdx + perPage, state.freeCardPool.size()));

            double cardW = 90;
            double cardH = 125;
            double gapX = 10;
            double gapY = 10;
            double totalW = cols * (cardW + gapX) - gapX;
            double startX = Constants.BASE_WIDTH / 2 - totalW / 2;
            double startY = 100;

            for (int i = 0; i < pageCards.size(); i++) {
                int col = i % cols;
                int row = i / cols;
                double cx = startX + col * (cardW + gapX);
                double cy = startY + row * (cardH + gapY);

                Object obj = pageCards.get(i);
                if (obj instanceof Card card) {
                    boolean selected = state.freeSelectedCards.contains(card.getId());
                    drawCard(gc, card, cx, cy, cardW, cardH, selected, false);
                    if (selected) {
                        strokeRect(gc, cx - 3, cy - 3, cardW + 6, cardH + 6, Constants.GOLD, 3);
                    }
                }
            }

            // 翻页
            if (totalPages > 1) {
                drawText(gc, (state.freeSetupPage + 1) + " / " + totalPages,
                        Constants.BASE_WIDTH / 2, 500, Constants.WHITE, 16, true);
                if (state.freeSetupPage > 0) {
                    fillRect(gc, Constants.BASE_WIDTH / 2 - 120, 510, 80, 30, Color.rgb(60, 80, 60));
                    strokeRect(gc, Constants.BASE_WIDTH / 2 - 120, 510, 80, 30, Constants.GOLD, 2);
                    drawText(gc, "◀ 上一页", Constants.BASE_WIDTH / 2 - 80, 518, Constants.WHITE, 12, true);
                }
                if (state.freeSetupPage < totalPages - 1) {
                    fillRect(gc, Constants.BASE_WIDTH / 2 + 40, 510, 80, 30, Color.rgb(60, 80, 60));
                    strokeRect(gc, Constants.BASE_WIDTH / 2 + 40, 510, 80, 30, Constants.GOLD, 2);
                    drawText(gc, "下一页 ▶", Constants.BASE_WIDTH / 2 + 80, 518, Constants.WHITE, 12, true);
                }
            }
        }

        // 底部按钮
        double btnY = Constants.BASE_HEIGHT - 60;
        fillRect(gc, Constants.BASE_WIDTH / 2 - 120, btnY, 120, 40, Color.rgb(80, 60, 60));
        strokeRect(gc, Constants.BASE_WIDTH / 2 - 120, btnY, 120, 40, Constants.WHITE, 2);
        drawText(gc, "返回", Constants.BASE_WIDTH / 2 - 60, btnY + 10, Constants.WHITE, 18, true);

        fillRect(gc, Constants.BASE_WIDTH / 2, btnY, 120, 40, Color.rgb(60, 120, 60));
        strokeRect(gc, Constants.BASE_WIDTH / 2, btnY, 120, 40, Constants.GOLD, 2);
        drawText(gc, "开始", Constants.BASE_WIDTH / 2 + 60, btnY + 10, Constants.WHITE, 18, true);
    }

    // ================================================================
    //  20. drawOpportunityEvent — 绘制机遇房事件弹窗
    // ================================================================

    /**
     * 绘制机遇房事件弹窗。
     * 半透明遮罩 + 事件内容面板 + 选项按钮。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawOpportunityEvent(GraphicsContext gc, GameState state) {
        if (!state.opportunityEventActive) return;

        // 全屏半透明遮罩
        gc.setGlobalAlpha(0.7);
        gc.setFill(Color.rgb(0, 0, 0));
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());
        gc.setGlobalAlpha(1.0);

        // 弹窗背景
        double popupW = 500;
        double popupH = 350;
        double popupX = Constants.BASE_WIDTH / 2 - popupW / 2;
        double popupY = Constants.BASE_HEIGHT / 2 - popupH / 2;

        fillRect(gc, popupX, popupY, popupW, popupH, Color.rgb(40, 30, 60));
        strokeRect(gc, popupX, popupY, popupW, popupH, Constants.GOLD, 3);

        // 标题
        String title = "机遇事件";
        if (state.opportunityEventType != null && !state.opportunityEventType.isEmpty()) {
            title = state.opportunityEventType;
        }
        drawText(gc, title, Constants.BASE_WIDTH / 2, popupY + 25, Constants.GOLD, 28, true);

        // 事件描述
        String desc = state.opportunityPopupMessage;
        if (desc == null || desc.isEmpty()) {
            desc = "你遇到了一个神秘的事件...";
        }

        // 分行显示描述
        String[] descLines = desc.split("\n");
        double textY = popupY + 70;
        for (String line : descLines) {
            // 如果一行太长，按宽度截断
            if (line.length() > 30) {
                int idx = 0;
                while (idx < line.length()) {
                    int end = Math.min(idx + 30, line.length());
                    drawText(gc, line.substring(idx, end), Constants.BASE_WIDTH / 2, textY,
                            Constants.WHITE, 14, true);
                    textY += 20;
                    idx = end;
                }
            } else {
                drawText(gc, line, Constants.BASE_WIDTH / 2, textY, Constants.WHITE, 14, true);
                textY += 20;
            }
        }

        // 选项按钮
        double btnY = popupY + popupH - 80;
        double btnW = 150;
        double btnH = 40;

        // 确认/继续 按钮
        fillRect(gc, Constants.BASE_WIDTH / 2 - btnW / 2, btnY, btnW, btnH, Color.rgb(60, 100, 60));
        strokeRect(gc, Constants.BASE_WIDTH / 2 - btnW / 2, btnY, btnW, btnH, Constants.GOLD, 2);
        drawText(gc, "继续", Constants.BASE_WIDTH / 2, btnY + 10, Constants.WHITE, 18, true);

        // 关闭按钮
        double closeBtnX = popupX + popupW - 40;
        double closeBtnY = popupY + 10;
        fillRect(gc, closeBtnX, closeBtnY, 30, 25, Color.rgb(80, 40, 40));
        drawText(gc, "X", closeBtnX + 15, closeBtnY + 2, Constants.WHITE, 14, true);
    }

    // ================================================================
    //  21. drawSettings — 绘制设置弹窗
    // ================================================================

    /**
     * 绘制设置弹窗。
     * <p>
     * 半透明遮罩 + 弹窗面板（全屏复选框 + 关闭按钮）。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void drawSettings(GraphicsContext gc, GameState state) {
        if (!state.settingsOpen) return;

        // 全屏半透明遮罩
        gc.setGlobalAlpha(0.7);
        gc.setFill(Color.rgb(0, 0, 0));
        gc.fillRect(0, 0, Constants.getScreenWidth(), Constants.getScreenHeight());
        gc.setGlobalAlpha(1.0);

        // 弹窗背景
        double popupW = 400;
        double popupH = 200;
        double popupX = Constants.BASE_WIDTH / 2 - popupW / 2;
        double popupY = Constants.BASE_HEIGHT / 2 - popupH / 2;

        fillRect(gc, popupX, popupY, popupW, popupH, Color.rgb(40, 30, 50));
        strokeRect(gc, popupX, popupY, popupW, popupH, Constants.GOLD, 2);

        // 标题
        drawText(gc, "设置", Constants.BASE_WIDTH / 2, popupY + 20, Constants.GOLD, 26, true);

        // 全屏选项
        double optionY = popupY + 65;
        double checkboxSize = 20;
        double checkboxX = popupX + 40;

        // 复选框
        fillRect(gc, checkboxX, optionY, checkboxSize, checkboxSize, Color.rgb(50, 40, 60));
        strokeRect(gc, checkboxX, optionY, checkboxSize, checkboxSize, Color.rgb(100, 80, 60), 2);
        // 勾选标记
        if (state.isFullscreen) {
            drawText(gc, "✓", checkboxX + checkboxSize / 2, optionY + checkboxSize / 2 - 3, Constants.WHITE, 18, true);
        }
        drawText(gc, "全屏模式", checkboxX + checkboxSize + 15, optionY + 2,
                Constants.WHITE, 18);

        // 关闭按钮
        double closeBtnX = popupX + popupW - 50;
        double closeBtnY = popupY + 10;
        double closeBtnW = 35;
        double closeBtnH = 25;
        fillRect(gc, closeBtnX, closeBtnY, closeBtnW, closeBtnH, Color.rgb(80, 40, 40));
        drawText(gc, "X", closeBtnX + closeBtnW / 2, closeBtnY + 2, Constants.WHITE, 16, true);
    }

    // ================================================================
    //  19. render — 主渲染入口
    // ================================================================

    /**
     * 主渲染入口，根据 state.phase 调用对应绘制方法。
     *
     * @param gc    GraphicsContext
     * @param state GameState 实例
     */
    public static void render(GraphicsContext gc, GameState state) {
        if (state == null) return;

        try {
            switch (state.phase) {
                case TITLE -> drawTitle(gc, state);
                case MODE_SELECT -> drawModeSelect(gc, state);
                case CHARACTER_SELECT -> drawCharacterSelect(gc, state);
                case BUILD, CHARACTER_BUILD -> drawCharacterBuild(gc, state);
                case BOSS_RUSH_SETUP -> drawBossRushSetup(gc, state);
                case FREE_SETUP -> drawFreeSetup(gc, state);
                case MAP -> drawMap(gc, state);
                case COMBAT -> drawCombat(gc, state);
                case REWARD -> drawReward(gc, state);
                case SHOP -> drawShop(gc, state);
                case ENCYCLOPEDIA -> drawEncyclopedia(gc, state);
                case DECK_VIEW -> drawDeckView(gc, state);
                case RELIC_VIEW -> drawRelicView(gc, state);
                case GAME_OVER -> drawGameOver(gc, state);
                case SETTINGS -> drawSettings(gc, state);
                default -> {
                    drawTitle(gc, state);
                }
            }
        } catch (Exception e) {
            System.err.println("渲染错误 (" + state.phase + "): " + e.getMessage());
            e.printStackTrace();
            // 出错时回退到标题界面，避免游戏卡死
            state.phase = GamePhase.TITLE;
        }

        // 浮动提示（能量不足/圣能不足等，在所有阶段之上）
        if (state.floatingMessage != null && !state.floatingMessage.isEmpty() && state.floatingMessageTimer > 0) {
            drawFloatingMessage(gc, state);
        }

        // 机遇事件覆盖层（在所有阶段之上）
        if (state.opportunityEventActive) {
            drawOpportunityEvent(gc, state);
        }

        // 新手指引覆盖层（在所有阶段之上）
        if (state.tutorialActive) {
            drawTutorial(gc, state);
        }

        // 设置弹窗（在所有阶段之上）
        if (state.settingsOpen) {
            drawSettings(gc, state);
        }
    }

    /**
     * 绘制浮动提示（能量不足/圣能不足/金币不足等）。
     * 对应 Python 版 ui_render.py 的浮动提示渲染。
     */
    private static void drawFloatingMessage(GraphicsContext gc, GameState state) {
        if (state.floatingMessage == null || state.floatingMessage.isEmpty()) return;
        double alpha = Math.min(1.0, state.floatingMessageTimer / 60.0);
        double msgW = state.floatingMessage.length() * 30 + 40;
        double msgH = 50;
        double msgX = Constants.BASE_WIDTH / 2 - msgW / 2;
        double msgY = 260;

        // 半透明背景
        gc.setGlobalAlpha(alpha * 0.5);
        fillRect(gc, msgX, msgY, msgW, msgH, Color.rgb(0, 0, 0));
        gc.setGlobalAlpha(1.0);

        // 红色边框
        gc.setStroke(Color.rgb(255, (int)(alpha * 255), (int)(alpha * 255)));
        gc.setLineWidth(2);
        gc.strokeRect(Constants.rx(msgX), Constants.ry(msgY), Constants.rs(msgW), Constants.rs(msgH));

        // 淡红色文字
        Color textColor = Color.rgb(255, (int)(alpha * 255), (int)(alpha * 255));
        drawText(gc, state.floatingMessage, Constants.BASE_WIDTH / 2, msgY + msgH / 2 - 5, textColor, 28, true);
    }
}