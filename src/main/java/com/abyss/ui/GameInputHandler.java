package com.abyss.ui;

import com.abyss.constants.Constants;
import com.abyss.model.*;
import com.abyss.state.GamePhase;
import com.abyss.state.GameState;
import com.abyss.state.MapNode;
import com.abyss.state.MapNodeType;
import com.abyss.system.CardFactory;
import com.abyss.system.EnemyData;
import com.abyss.system.LangManager;
import com.abyss.system.RelicPool;
import com.abyss.system.SaveSystem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.*;

/**
 * 游戏输入事件处理器 —— 根据 GameState.phase 将鼠标/键盘事件分发到对应的处理逻辑。
 * <p>
 * 对应 Python 版 event_handlers.py 中各 handle_*_events 函数。
 * 所有鼠标坐标均通过 Constants.invScalePos() 从窗口坐标转换为基准坐标。
 */
public final class GameInputHandler {

    private GameInputHandler() {
        // 工具类，禁止实例化
    }

    // ================================================================
    //  对外入口
    // ================================================================

    /**
     * 处理鼠标点击事件，根据 state.phase 分发到对应的处理逻辑。
     *
     * @param state  游戏状态
     * @param x      窗口坐标 x
     * @param y      窗口坐标 y
     * @param button 鼠标按钮 (1=左键, 2=中键, 3=右键)
     */
    public static void handleMouseClick(GameState state, double x, double y, int button) {
        if (button != 1) return; // 仅处理左键点击
        double[] logical = Constants.invScalePos(x, y);
        double lx = logical[0];
        double ly = logical[1];

        // 教程覆盖层优先处理（在所有阶段之上）
        if (state.tutorialActive) {
            handleTutorialEvents(state, lx, ly);
            return;
        }

        switch (state.phase) {
            case TITLE -> handleTitleEvents(state, lx, ly);
            case MODE_SELECT -> handleModeSelectEvents(state, lx, ly);
            case CHARACTER_SELECT -> handleCharacterSelectEvents(state, lx, ly);
            case BUILD -> handleCharacterBuildEvents(state, lx, ly);
            case MAP -> handleMapEvents(state, lx, ly);
            case COMBAT -> handleCombatEvents(state, lx, ly);
            case SHOP -> handleShopEvents(state, lx, ly);
            case REWARD -> handleRewardEvents(state, lx, ly);
            case ENCYCLOPEDIA -> handleEncyclopediaEvents(state, lx, ly);
            case DECK_VIEW -> handleDeckViewEvents(state, lx, ly);
            case RELIC_VIEW -> handleRelicViewEvents(state, lx, ly);
            case GAME_OVER -> handleGameOverEvents(state, lx, ly);
            case SETTINGS -> handleSettingsEvents(state, lx, ly);
            default -> {
                // 其他阶段暂不处理
            }
        }
    }

    /**
     * 处理鼠标移动事件，更新鼠标位置用于悬停检测。
     *
     * @param state 游戏状态
     * @param x     窗口坐标 x
     * @param y     窗口坐标 y
     */
    public static void handleMouseMove(GameState state, double x, double y) {
        double[] logical = Constants.invScalePos(x, y);
        // 更新悬停卡牌检测（战斗阶段）
        if (state.phase == GamePhase.COMBAT) {
            updateHoveredCard(state, logical[0], logical[1]);
        }
    }

    /**
     * 处理键盘事件。
     *
     * @param state   游戏状态
     * @param keyCode 按键代码
     */
    public static void handleKeyPress(GameState state, KeyCode keyCode) {
        // ── F11: 切换全屏模式（在任何阶段都可用） ──
        if (keyCode == KeyCode.F11) {
            state.isFullscreen = !state.isFullscreen;
            state.fullscreenRequested = true;
            return;
        }

        // ── ESC: 全屏模式下先退出全屏，不执行其他导航 ──
        if (keyCode == KeyCode.ESCAPE) {
            if (state.isFullscreen) {
                state.isFullscreen = false;
                state.fullscreenRequested = true;
                return;
            }
            switch (state.phase) {
                case TITLE -> {
                    // ESC 退出游戏 — 由 AbyssWalkerGame 处理
                }
                case MODE_SELECT, CHARACTER_SELECT -> state.phase = GamePhase.TITLE;
                case BUILD -> state.phase = GamePhase.CHARACTER_SELECT;
                case MAP -> state.phase = GamePhase.TITLE;
                case COMBAT -> {
                    if (state.combatMenuOpen) {
                        state.combatMenuOpen = false;
                    } else if (state.powersMenuOpen) {
                        state.powersMenuOpen = false;
                    } else {
                        state.phase = GamePhase.TITLE;
                    }
                }
                case ENCYCLOPEDIA, DECK_VIEW, RELIC_VIEW -> {
                    if (state.prevPhase != null) {
                        state.phase = state.prevPhase;
                        state.prevPhase = null;
                    } else {
                        state.phase = GamePhase.TITLE;
                    }
                }
                default -> {
                }
            }
        }
    }

    // ================================================================
    //  标题界面事件
    // ================================================================

    /**
     * 处理标题界面（主菜单）的点击事件。
     * 按钮布局：
     * - 有存档时：开始(400-460)、继续(480-540)、图鉴(560-620)、退出(640-700)
     * - 无存档时：开始(400-460)、图鉴(480-540)、退出(560-620)
     */
    private static void handleTitleEvents(GameState state, double x, double y) {
        // 设置弹窗优先处理
        if (state.settingsOpen) {
            handleSettingsPopupClick(state, x, y);
            return;
        }

        // 右上角设置齿轮按钮
        double settingsBtnSize = 50;
        double settingsBtnX = Constants.BASE_WIDTH - settingsBtnSize - 15;
        double settingsBtnY = 15;
        if (x >= settingsBtnX && x <= settingsBtnX + settingsBtnSize &&
                y >= settingsBtnY && y <= settingsBtnY + settingsBtnSize) {
            state.settingsOpen = true;
            return;
        }

        double buttonW = 300;
        double buttonH = 60;
        double buttonX = Constants.BASE_WIDTH / 2 - buttonW / 2;
        double buttonY = 400;

        // "开始游戏" 按钮
        if (x >= buttonX && x <= buttonX + buttonW && y >= buttonY && y <= buttonY + buttonH) {
            state.reset();
            state.phase = GamePhase.MODE_SELECT;
            return;
        }

        boolean hasSave = SaveSystem.hasSave();

        // "继续游戏" 按钮（仅存档存在时显示）
        if (hasSave && x >= buttonX && x <= buttonX + buttonW &&
                y >= buttonY + 80 && y <= buttonY + 80 + buttonH) {
            Map<String, Object> saveData = SaveSystem.loadGame();
            if (saveData != null) {
                GameState loaded = GameState.fromMap(saveData);
                state.phase = loaded.phase;
                state.turn = loaded.turn;
                state.currentFloor = loaded.currentFloor;
                state.maxFloors = loaded.maxFloors;
                state.gameMode = loaded.gameMode;
                state.combatLog = loaded.combatLog;
                state.mapNodes = loaded.mapNodes;
                state.rewardGold = loaded.rewardGold;
                state.rewardSelectedCard = loaded.rewardSelectedCard;
                state.rewardRelicTaken = loaded.rewardRelicTaken;
                state.elitesUsed = loaded.elitesUsed;
                // 玩家数据从存档恢复
                if (saveData.containsKey("player") && saveData.get("player") instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> playerData = (Map<String, Object>) saveData.get("player");
                    state.player = Player.fromMap(playerData);
                }
            }
            return;
        }

        double encY = hasSave ? buttonY + 160 : buttonY + 80;
        double exitY = hasSave ? buttonY + 240 : buttonY + 160;

        // "图鉴" 按钮
        if (x >= buttonX && x <= buttonX + buttonW && y >= encY && y <= encY + buttonH) {
            state.encyclopediaModule = "cards";
            state.encyclopediaPage = 0;
            state.phase = GamePhase.ENCYCLOPEDIA;
            return;
        }

        // "退出" 按钮
        if (x >= buttonX && x <= buttonX + buttonW && y >= exitY && y <= exitY + buttonH) {
            // 退出由 AbyssWalkerGame 处理
            System.exit(0);
        }
    }

    /** 设置弹窗内的点击处理。 */
    private static void handleSettingsPopupClick(GameState state, double x, double y) {
        double popupW = 400;
        double popupH = 200;
        double popupX = Constants.BASE_WIDTH / 2 - popupW / 2;
        double popupY = Constants.BASE_HEIGHT / 2 - popupH / 2;

        // 点击弹窗外部 → 关闭弹窗
        if (!(x >= popupX && x <= popupX + popupW && y >= popupY && y <= popupY + popupH)) {
            state.settingsOpen = false;
            return;
        }

        // 关闭按钮
        double closeBtnX = popupX + popupW - 50;
        double closeBtnY = popupY + 10;
        double closeBtnW = 35;
        double closeBtnH = 25;
        if (x >= closeBtnX && x <= closeBtnX + closeBtnW && y >= closeBtnY && y <= closeBtnY + closeBtnH) {
            state.settingsOpen = false;
            return;
        }

        // 全屏复选框点击
        double checkboxSize = 20;
        double checkboxX = popupX + 40;
        double checkboxY = popupY + 65;
        if (x >= checkboxX && x <= checkboxX + checkboxSize && y >= checkboxY && y <= checkboxY + checkboxSize) {
            state.isFullscreen = !state.isFullscreen;
            state.fullscreenRequested = true;
            state.settingsOpen = false;
            return;
        }
    }

    // ================================================================
    //  模式选择界面事件
    // ================================================================

    /**
     * 处理游戏模式选择界面的事件。
     */
    private static void handleModeSelectEvents(GameState state, double x, double y) {
        double btnW = 300;
        double btnH = 60;
        double btnX = Constants.BASE_WIDTH / 2 - btnW / 2;

        // "普通模式" 按钮
        double normalBtnY = 300;
        if (x >= btnX && x <= btnX + btnW && y >= normalBtnY && y <= normalBtnY + btnH) {
            state.gameMode = "normal";
            state.phase = GamePhase.CHARACTER_SELECT;
            return;
        }

        // "BOSS连战" 按钮
        double bossRushBtnY = 400;
        if (x >= btnX && x <= btnX + btnW && y >= bossRushBtnY && y <= bossRushBtnY + btnH) {
            state.gameMode = "boss_rush";
            state.phase = GamePhase.CHARACTER_SELECT;
            return;
        }

        // "自由模式" 按钮
        double freeBtnY = 500;
        if (x >= btnX && x <= btnX + btnW && y >= freeBtnY && y <= freeBtnY + btnH) {
            state.gameMode = "free_mode";
            state.phase = GamePhase.CHARACTER_SELECT;
            return;
        }

        // "返回" 按钮
        double backBtnY = 600;
        double backBtnW = 150;
        double backBtnX = Constants.BASE_WIDTH / 2 - backBtnW / 2;
        if (x >= backBtnX && x <= backBtnX + backBtnW && y >= backBtnY && y <= backBtnY + 40) {
            state.phase = GamePhase.TITLE;
        }
    }

    // ================================================================
    //  角色选择界面事件
    // ================================================================

    /**
     * 处理角色选择界面的事件。
     */
    private static void handleCharacterSelectEvents(GameState state, double x, double y) {
        // 底部按钮
        double btnY = Constants.BASE_HEIGHT - 60;
        double btnW = 120;
        double btnH = 40;
        double btnGap = 80;

        // 返回按钮
        double backBtnX = Constants.BASE_WIDTH / 2 - btnGap / 2 - btnW;
        if (x >= backBtnX && x <= backBtnX + btnW && y >= btnY && y <= btnY + btnH) {
            state.selectedCharacter = null;
            state.phase = GamePhase.TITLE;
            return;
        }

        // 确定按钮
        double confirmBtnX = Constants.BASE_WIDTH / 2 + btnGap / 2;
        if (x >= confirmBtnX && x <= confirmBtnX + btnW && y >= btnY && y <= btnY + btnH) {
            if (state.selectedCharacter != null) {
                // 初始化玩家角色
                initPlayer(state);
                state.buildPoints = 2;
                state.buildAlloc.put("strength", 0);
                state.buildAlloc.put("dexterity", 0);
                state.buildAlloc.put("guard", 0);
                state.buildAlloc.put("draw", 0);
                state.buildAlloc.put("energy", 0);
                state.phase = GamePhase.BUILD;
            }
            return;
        }

        // 角色卡片点击检测（2×2 网格）
        double cardWidth = 520;
        double cardHeight = 220;
        double gapX = 40;
        double gapY = 20;
        double totalW = cardWidth * 2 + gapX;
        double startX = (Constants.BASE_WIDTH - totalW) / 2;
        double startY = 80;

        String[] charClasses = {"warrior", "mage", "rogue", "priest"};
        for (int i = 0; i < 4; i++) {
            int row = i / 2;
            int col = i % 2;
            double cx = startX + col * (cardWidth + gapX);
            double cy = startY + row * (cardHeight + gapY);
            if (x >= cx && x <= cx + cardWidth && y >= cy && y <= cy + cardHeight) {
                state.selectedCharacter = charClasses[i];
                break;
            }
        }
    }

    // ================================================================
    //  角色加点界面事件
    // ================================================================

    /**
     * 处理角色加点界面的事件。
     */
    private static void handleCharacterBuildEvents(GameState state, double x, double y) {
        // 属性分配选项
        String[] buildKeys = {"strength", "dexterity", "guard", "draw"};
        int[] buildCosts = {1, 1, 1, 2};

        double optionY = 180;
        for (int i = 0; i < buildKeys.length; i++) {
            String key = buildKeys[i];
            int cost = buildCosts[i];
            double optionX = Constants.BASE_WIDTH / 2 - 300;

            // "−" 按钮：减少分配
            double minusBtnX = optionX + 400;
            double minusBtnY = optionY + 20;
            if (x >= minusBtnX && x <= minusBtnX + 40 && y >= minusBtnY && y <= minusBtnY + 40) {
                int current = state.buildAlloc.getOrDefault(key, 0);
                if (current > 0) {
                    state.buildAlloc.put(key, current - 1);
                    state.buildPoints += cost;
                }
                return;
            }

            // "+" 按钮：增加分配
            double plusBtnX = optionX + 510;
            double plusBtnY = optionY + 20;
            if (x >= plusBtnX && x <= plusBtnX + 40 && y >= plusBtnY && y <= plusBtnY + 40) {
                if (state.buildPoints >= cost) {
                    int current = state.buildAlloc.getOrDefault(key, 0);
                    state.buildAlloc.put(key, current + 1);
                    state.buildPoints -= cost;
                }
                return;
            }

            optionY += 100;
        }

        // "开始冒险" 按钮
        double startBtnX = Constants.BASE_WIDTH / 2 - 100;
        double startBtnY = Constants.BASE_HEIGHT - 80;
        if (x >= startBtnX && x <= startBtnX + 200 && y >= startBtnY && y <= startBtnY + 50) {
            // 应用属性到角色
            if (state.player != null) {
                state.player.applyBuildStats(state.buildAlloc);
            }
            state.generateMap();
            state.phase = GamePhase.MAP;
        }
    }

    // ================================================================
    //  地图界面事件
    // ================================================================

    /**
     * 处理地图界面的事件：节点点击、拖拽、按钮。
     */
    private static void handleMapEvents(GameState state, double x, double y) {
        // 机遇房事件弹窗拦截
        if (state.opportunityEventActive) {
            handleOpportunityEventClick(state, x, y);
            return;
        }

        // 图鉴按钮（左上角 20,20 起，90×35）
        if (x >= 20 && x <= 110 && y >= 20 && y <= 55) {
            state.prevPhase = state.phase;
            state.phase = GamePhase.ENCYCLOPEDIA;
            return;
        }

        // 卡组按钮（左上角 120,20 起，90×35）
        if (x >= 120 && x <= 210 && y >= 20 && y <= 55) {
            state.prevPhase = state.phase;
            state.deckViewPage = 0;
            state.phase = GamePhase.DECK_VIEW;
            return;
        }

        // 遗物按钮（左上角 220,20 起，90×35）
        if (x >= 220 && x <= 310 && y >= 20 && y <= 55) {
            state.prevPhase = state.phase;
            state.relicViewPage = 0;
            state.phase = GamePhase.RELIC_VIEW;
            return;
        }

        // 地图节点点击检测（圆形区域，半径 30 像素）
        double scrollOff = state.mapScrollOffset;
        for (MapNode node : state.mapNodes) {
            if (node.getFloor() == state.currentFloor && !node.isCompleted()) {
                double ny = node.getY() + scrollOff;
                double dist = Math.sqrt((x - node.getX()) * (x - node.getX()) + (y - ny) * (y - ny));
                if (dist <= 30) {
                    state.selectedNode = node;
                    if (node.getType() == MapNodeType.COMBAT || node.getType() == MapNodeType.ELITE || node.getType() == MapNodeType.BOSS) {
                        state.generateEnemies(node.getType());
                        state.startCombat();
                        state.phase = GamePhase.COMBAT;
                    } else if (node.getType() == MapNodeType.SHOP) {
                        state.generateShop();
                        state.phase = GamePhase.SHOP;
                    } else if (node.getType() == MapNodeType.REST) {
                        // 回复30%最大生命值
                        if (state.player != null) {
                            int healAmount = (int)(state.player.getMaxHp() * 0.3);
                            state.player.setHp(Math.min(state.player.getHp() + healAmount, state.player.getMaxHp()));
                        }
                        node.setCompleted(true);
                        state.currentFloor++;
                    } else if (node.getType() == MapNodeType.OPPORTUNITY) {
                        state.opportunityEventActive = true;
                        state.opportunityEventType = "random";
                        state.opportunityEventStage = "choice";
                    } else if (node.getType() == MapNodeType.TREASURE) {
                        state.generateReward();
                        state.opportunityRewardActive = true;
                        state.phase = GamePhase.REWARD;
                    } else if (node.getType() == MapNodeType.EVENT) {
                        // 随机事件占位
                        if (state.player != null) {
                            int healAmount = (int)(state.player.getMaxHp() * 0.15);
                            state.player.setHp(Math.min(state.player.getHp() + healAmount, state.player.getMaxHp()));
                        }
                        node.setCompleted(true);
                        state.currentFloor++;
                    }
                    return;
                }
            }
        }
    }

    /** 处理机遇房事件的弹窗点击。 */
    private static void handleOpportunityEventClick(GameState state, double x, double y) {
        // 弹窗区域
        double popupW = 500;
        double popupH = 400;
        double popupX = Constants.BASE_WIDTH / 2 - popupW / 2;
        double popupY = Constants.BASE_HEIGHT / 2 - popupH / 2;

        // 点击外部关闭
        if (!(x >= popupX && x <= popupX + popupW && y >= popupY && y <= popupY + popupH)) {
            state.opportunityEventActive = false;
            if (state.selectedNode != null) {
                state.selectedNode.setCompleted(true);
            }
            state.currentFloor++;
            state.phase = GamePhase.MAP;
            return;
        }

        // 选择阶段
        if ("choice".equals(state.opportunityEventStage)) {
            // 两个选项按钮
            double btnW = 200;
            double btnH = 50;
            double btnY = popupY + popupH - 100;
            double option1X = popupX + 50;
            double option2X = popupX + popupW - 250;

            if (x >= option1X && x <= option1X + btnW && y >= btnY && y <= btnY + btnH) {
                // 选项1：获得金币
                if (state.player != null) {
                    state.player.setGold(state.player.getGold() + 50);
                }
                state.opportunityEventActive = false;
                if (state.selectedNode != null) {
                    state.selectedNode.setCompleted(true);
                }
                state.currentFloor++;
                state.phase = GamePhase.MAP;
                return;
            }
            if (x >= option2X && x <= option2X + btnW && y >= btnY && y <= btnY + btnH) {
                // 选项2：获得随机卡牌
                state.generateReward();
                state.opportunityRewardActive = true;
                state.opportunityEventActive = false;
                state.phase = GamePhase.REWARD;
                return;
            }
        }
    }

    // ================================================================
    //  战斗界面事件
    // ================================================================

    /**
     * 处理战斗界面的事件 —— 卡牌选择、目标选择、结束回合等。
     */
    private static void handleCombatEvents(GameState state, double x, double y) {
        // 战斗结束动画中不处理事件
        if (state.combatEnding) return;

        // 主菜单弹出
        if (state.combatMenuOpen) {
            handleCombatMenuClick(state, x, y);
            return;
        }

        // 能力弹出
        if (state.powersMenuOpen) {
            // 点击外部关闭
            double panelW = 420;
            double panelH = 500;
            double panelX = Constants.BASE_WIDTH / 2 - panelW / 2;
            double panelY = Constants.BASE_HEIGHT / 2 - panelH / 2;
            if (!(x >= panelX && x <= panelX + panelW && y >= panelY && y <= panelY + panelH)) {
                state.powersMenuOpen = false;
            }
            return;
        }

        // 道具选择面板
        if (state.itemSelectMode) {
            double panelW = 400;
            double panelH = 300;
            double panelX = Constants.BASE_WIDTH / 2 - panelW / 2;
            double panelY = Constants.BASE_HEIGHT / 2 - panelH / 2;
            if (!(x >= panelX && x <= panelX + panelW && y >= panelY && y <= panelY + panelH)) {
                state.itemSelectMode = false;
            }
            return;
        }

        // 主菜单按钮（左上角）
        if (x >= 20 && x <= 110 && y >= 20 && y <= 55) {
            state.combatMenuOpen = true;
            return;
        }

        // 能力按钮
        if (x >= 225 && x <= 305 && y >= 510 && y <= 545) {
            state.powersMenuOpen = true;
            return;
        }

        // 道具按钮
        if (x >= 225 && x <= 305 && y >= 555 && y <= 590) {
            state.itemSelectMode = true;
            return;
        }

        // "结束回合" 按钮
        double endTurnX = Constants.BASE_WIDTH - 180;
        double endTurnY = 460;
        if (x >= endTurnX && x <= endTurnX + 160 && y >= endTurnY && y <= endTurnY + 40) {
            // 执行回合结束流程
            if (state.player != null) {
                // 1. 手牌入弃牌堆
                List<Card> hand = state.player.getHand();
                List<Card> toDiscard = new ArrayList<>();
                for (Card card : hand) {
                    if (!card.isRetain()) {
                        toDiscard.add(card);
                    }
                }
                hand.removeAll(toDiscard);
                state.player.getDiscardPile().addAll(toDiscard);

                // 2. 敌人中毒等回合结束效果
                for (Object e : state.enemies) {
                    if (e instanceof Enemy) {
                        Enemy enemy = (Enemy) e;
                        if (enemy.getHp() > 0) {
                            enemy.tickStatus(state.player);
                            enemy.tickPoison();
                            enemy.tickBleed();
                        }
                    }
                }

                // 3. 敌人行动
                for (Object e : new ArrayList<>(state.enemies)) {
                    if (e instanceof Enemy) {
                        Enemy enemy = (Enemy) e;
                        if (enemy.getHp() > 0) {
                            enemy.act(state.player);
                            if (enemy.getIntent() == EnemyIntent.ATTACK) {
                                int dmg = enemy.getDisplayIntentValue(state.player);
                                if (dmg > 0) {
                                    state.player.takeDamage(dmg);
                                    state.combatLog.add(getEnemyName(enemy) + " 攻击，造成 " + dmg + " 点伤害");
                                }
                            }
                        }
                    }
                }

                // 4. 处理敌人死亡
                state.processEnemyDeaths();

                // 5. 检查玩家是否死亡
                if (state.player.getHp() <= 0) {
                    state.result = "lose";
                    state.phase = GamePhase.GAME_OVER;
                    state.turn++;
                    return;
                }

                // 6. 检查是否所有敌人死亡
                boolean allDead = true;
                for (Object e : state.enemies) {
                    if (e instanceof Enemy && ((Enemy) e).getHp() > 0) {
                        allDead = false;
                        break;
                    }
                }
                if (allDead) {
                    state.generateReward();
                    state.phase = GamePhase.REWARD;
                    state.turn++;
                    return;
                }

                // 7. 玩家回合重置
                state.player.resetTurn();
                state.turn++;
            }
            return;
        }

        // 手牌点击检测
        if (state.player != null) {
            List<Card> hand = state.player.getHand();
            double handX = Constants.BASE_WIDTH / 2 - (hand.size() * 110) / 2.0;
            double cardY = Constants.BASE_HEIGHT - 160;
            for (int i = 0; i < hand.size(); i++) {
                double cardX = handX + i * 110;
                if (x >= cardX && x <= cardX + 100 && y >= cardY && y <= cardY + 140) {
                    Card clickedCard = hand.get(i);
                    if (state.selectedCard == null) {
                        // 选择卡牌
                        if (state.player.getEnergy() >= clickedCard.getCost() || clickedCard.getCost() == 0) {
                            state.selectedCard = clickedCard;
                            // 如果目标是自己或全体，直接出牌
                            String target = clickedCard.getTarget();
                            if ("self".equals(target) || target == null) {
                                playCard(state, clickedCard, null);
                                state.selectedCard = null;
                            }
                            // 如果目标是全体敌人，直接出牌
                            if ("all_enemies".equals(target)) {
                                playCard(state, clickedCard, null);
                                state.selectedCard = null;
                            }
                        }
                    } else {
                        // 取消选中
                        state.selectedCard = null;
                    }
                    return;
                }
            }
        }

        // 敌人点击检测（目标选择）
        if (state.selectedCard != null && state.selectedCard instanceof Card) {
            Card selCard = (Card) state.selectedCard;
            double enemyStartX = 50;
            double enemyY = 100;
            double enemyW = 160;
            double enemyH = 140;
            double enemyGap = 30;
            int idx = 0;
            for (Object e : state.enemies) {
                if (e instanceof Enemy) {
                    Enemy enemy = (Enemy) e;
                    if (enemy.getHp() > 0) {
                        double ex = enemyStartX + idx * (enemyW + enemyGap);
                        if (x >= ex && x <= ex + enemyW && y >= enemyY && y <= enemyY + enemyH) {
                            playCard(state, selCard, enemy);
                            state.selectedCard = null;
                            return;
                        }
                        idx++;
                    }
                }
            }
        }

        // 点击空白取消选中
        state.selectedCard = null;
    }

    /** 战斗内主菜单弹出点击处理。 */
    private static void handleCombatMenuClick(GameState state, double x, double y) {
        double panelW = 320;
        double panelH = 360;
        double panelX = Constants.BASE_WIDTH / 2 - panelW / 2;
        double panelY = Constants.BASE_HEIGHT / 2 - panelH / 2;

        // 点击外部关闭
        if (!(x >= panelX && x <= panelX + panelW && y >= panelY && y <= panelY + panelH)) {
            state.combatMenuOpen = false;
            return;
        }

        double btnW = 250;
        double btnH = 45;
        double btnX = panelX + (panelW - btnW) / 2;
        double startBtnY = panelY + 80;

        // 图鉴
        if (x >= btnX && x <= btnX + btnW && y >= startBtnY && y <= startBtnY + btnH) {
            state.combatMenuOpen = false;
            state.prevPhase = state.phase;
            state.phase = GamePhase.ENCYCLOPEDIA;
            return;
        }
        // 我的卡牌
        double cardsBtnY = startBtnY + (btnH + 10);
        if (x >= btnX && x <= btnX + btnW && y >= cardsBtnY && y <= cardsBtnY + btnH) {
            state.combatMenuOpen = false;
            state.prevPhase = state.phase;
            state.deckViewPage = 0;
            state.phase = GamePhase.DECK_VIEW;
            return;
        }
        // 我的遗物
        double relicsBtnY = startBtnY + 2 * (btnH + 10);
        if (x >= btnX && x <= btnX + btnW && y >= relicsBtnY && y <= relicsBtnY + btnH) {
            state.combatMenuOpen = false;
            state.prevPhase = state.phase;
            state.relicViewPage = 0;
            state.phase = GamePhase.RELIC_VIEW;
            return;
        }
        // 返回主界面
        double mainMenuBtnY = startBtnY + 3 * (btnH + 10);
        if (x >= btnX && x <= btnX + btnW && y >= mainMenuBtnY && y <= mainMenuBtnY + btnH) {
            state.combatMenuOpen = false;
            state.phase = GamePhase.TITLE;
            return;
        }
        // 关闭菜单
        double closeBtnY = startBtnY + 4 * (btnH + 10);
        if (x >= btnX && x <= btnX + btnW && y >= closeBtnY && y <= closeBtnY + btnH) {
            state.combatMenuOpen = false;
        }
    }

    // ================================================================
    //  商店界面事件
    // ================================================================

    /**
     * 处理商店界面的事件：购买商品、删牌、离开。
     */
    private static void handleShopEvents(GameState state, double x, double y) {
        // 菜单按钮
        if (x >= 20 && x <= 110 && y >= 15 && y <= 50) {
            state.prevPhase = state.phase;
            state.phase = GamePhase.ENCYCLOPEDIA;
            return;
        }
        if (x >= 120 && x <= 210 && y >= 15 && y <= 50) {
            state.prevPhase = state.phase;
            state.deckViewPage = 0;
            state.phase = GamePhase.DECK_VIEW;
            return;
        }
        if (x >= 220 && x <= 310 && y >= 15 && y <= 50) {
            state.prevPhase = state.phase;
            state.relicViewPage = 0;
            state.phase = GamePhase.RELIC_VIEW;
            return;
        }

        // 删牌模式
        if (state.shopDeleteMode) {
            double backBtnX = Constants.BASE_WIDTH / 2 - 75;
            double backBtnY = Constants.BASE_HEIGHT - 60;
            if (x >= backBtnX && x <= backBtnX + 150 && y >= backBtnY && y <= backBtnY + 45) {
                state.shopDeleteMode = false;
                state.selectedShopDeleteCard = null;
                return;
            }
            // 卡牌点击检测删除（遍历玩家全部卡牌）
            if (state.player != null) {
                List<Card> allCards = new ArrayList<>();
                allCards.addAll(state.player.getHand());
                allCards.addAll(state.player.getDrawPile());
                allCards.addAll(state.player.getDiscardPile());
                allCards.addAll(state.player.getExhaustPile());
                double cardStartX = Constants.BASE_WIDTH / 2 - (Math.min(allCards.size(), 10) * 110) / 2.0;
                double cardY = 200;
                for (int i = 0; i < allCards.size(); i++) {
                    double cx = cardStartX + i * 110;
                    if (i >= 10) break; // 最多显示10张
                    if (x >= cx && x <= cx + 100 && y >= cardY && y <= cardY + 140) {
                        Card clicked = allCards.get(i);
                        if (state.selectedShopDeleteCard == clicked) {
                            // 第二次点击确认删除
                            state.player.removeCard(clicked);
                            state.cardDeleteCount++;
                            state.shopDeleteMode = false;
                            state.selectedShopDeleteCard = null;
                        } else {
                            state.selectedShopDeleteCard = clicked;
                        }
                        return;
                    }
                }
                // 点击空白取消选中
                state.selectedShopDeleteCard = null;
            }
            return;
        }

        // 离开按钮
        double leaveBtnX = Constants.BASE_WIDTH / 2 - 75;
        double btnY = Constants.BASE_HEIGHT - 65;
        if (x >= leaveBtnX && x <= leaveBtnX + 150 && y >= btnY && y <= btnY + 45) {
            if (state.selectedNode != null) {
                state.selectedNode.setCompleted(true);
            }
            state.currentFloor++;
            state.phase = GamePhase.MAP;
        }

        // 删卡按钮
        double deleteBtnX = Constants.BASE_WIDTH / 2 - 240;
        if (x >= deleteBtnX && x <= deleteBtnX + 150 && y >= btnY && y <= btnY + 45) {
            state.shopDeleteMode = true;
        }

        // 商品购买按钮检测
        double shopStartX = 50;
        double shopItemW = 180;
        double shopItemH = 200;
        double shopStartY = 120;
        double shopGap = 30;
        for (int i = 0; i < state.shopItems.size(); i++) {
            Map<String, Object> item = state.shopItems.get(i);
            if (Boolean.TRUE.equals(item.get("sold"))) continue;
            double sx = shopStartX + i * (shopItemW + shopGap);
            double sy = shopStartY;
            if (x >= sx && x <= sx + shopItemW && y >= sy && y <= sy + shopItemH) {
                int price = ((Number) item.getOrDefault("price", 0)).intValue();
                if (state.player != null && state.player.getGold() >= price) {
                    state.player.setGold(state.player.getGold() - price);
                    item.put("sold", true);
                    String type = (String) item.get("type");
                    if ("card".equals(type)) {
                        Card card = (Card) item.get("card");
                        if (card != null) state.player.addCard(card);
                    } else if ("relic".equals(type)) {
                        Relic relic = (Relic) item.get("relic");
                        if (relic != null) state.player.getRelics().add(relic);
                    } else if ("item".equals(type)) {
                        Item itm = (Item) item.get("item");
                        if (itm != null) state.player.getItems().add(itm);
                    }
                    state.combatLog.add("购买商品: " + type + " 价格: " + price);
                }
                return;
            }
        }
    }

    // ================================================================
    //  奖励界面事件
    // ================================================================

    /**
     * 处理战斗奖励界面的事件：选择卡牌、拾取遗物、跳过/确认。
     */
    private static void handleRewardEvents(GameState state, double x, double y) {
        // 菜单按钮
        if (x >= 20 && x <= 110 && y >= 20 && y <= 55) {
            state.prevPhase = state.phase;
            state.phase = GamePhase.ENCYCLOPEDIA;
            return;
        }
        if (x >= 120 && x <= 210 && y >= 20 && y <= 55) {
            state.prevPhase = state.phase;
            state.deckViewPage = 0;
            state.phase = GamePhase.DECK_VIEW;
            return;
        }
        if (x >= 220 && x <= 310 && y >= 20 && y <= 55) {
            state.prevPhase = state.phase;
            state.relicViewPage = 0;
            state.phase = GamePhase.RELIC_VIEW;
            return;
        }

        // 奖励卡牌选中
        double cardYMin = state.opportunityRewardActive ? 220 : 280;
        double cardYMax = state.opportunityRewardActive ? 360 : 420;
        double cardSpacing = state.opportunityRewardActive ? 105 : 110;
        double cardX = Constants.BASE_WIDTH / 2 - (state.opportunityRewardActive ? 5 * cardSpacing : 165) / 2;
        for (int i = 0; i < state.rewardCards.size(); i++) {
            if (x >= cardX && x <= cardX + 100 && y >= cardYMin && y <= cardYMax) {
                if (state.rewardSelectedCard == i) {
                    state.rewardSelectedCard = -1;
                } else {
                    state.rewardSelectedCard = i;
                }
                return;
            }
            cardX += cardSpacing;
        }

        // 遗物拾取
        if (state.rewardRelic != null && !state.rewardRelicTaken) {
            double relicY = 450;
            if (x >= Constants.BASE_WIDTH / 2 - 200 && x <= Constants.BASE_WIDTH / 2 + 200 &&
                    y >= relicY && y <= relicY + 60) {
                state.rewardRelicTaken = true;
                return;
            }
        }

        // 跳过 / 确认按钮
        double buttonY = Constants.BASE_HEIGHT - 100;
        // 跳过
        if (x >= Constants.BASE_WIDTH / 2 - 200 && x <= Constants.BASE_WIDTH / 2 - 50 &&
                y >= buttonY && y <= buttonY + 50) {
            state.currentFloor++;
            if (state.currentFloor > state.maxFloors) {
                state.result = "win";
                state.phase = GamePhase.GAME_OVER;
            } else {
                state.phase = GamePhase.MAP;
            }
            return;
        }
        // 确认
        if (x >= Constants.BASE_WIDTH / 2 + 50 && x <= Constants.BASE_WIDTH / 2 + 200 &&
                y >= buttonY && y <= buttonY + 50) {
            state.currentFloor++;
            if (state.currentFloor > state.maxFloors) {
                state.result = "win";
                state.phase = GamePhase.GAME_OVER;
            } else {
                state.phase = GamePhase.MAP;
            }
        }
    }

    // ================================================================
    //  图鉴界面事件
    // ================================================================

    /**
     * 处理图鉴界面的交互事件。
     */
    private static void handleEncyclopediaEvents(GameState state, double x, double y) {
        // ── 稀有度筛选按钮（仅卡牌模块） ──
        if ("cards".equals(state.encyclopediaModule)) {
            String[] rarityKeys = {null, "common", "uncommon", "rare", "legendary", "curse"};
            double filterX = 30;
            double filterY = 150;
            double btnW = 110;
            double btnH = 38;
            double gap = 8;
            for (int i = 0; i < rarityKeys.length; i++) {
                double fy = filterY + i * (btnH + gap);
                if (x >= filterX && x <= filterX + btnW && y >= fy && y <= fy + btnH) {
                    state.encyclopediaFilter = rarityKeys[i];
                    state.encyclopediaPage = 0;
                    return;
                }
            }

            // 角色筛选按钮（仅卡牌模块）
            String[] classKeys = {null, "warrior", "mage", "rogue", "priest", "public"};
            double classFilterX = 30;
            double classFilterY = 440;
            double classBtnW = 110;
            double classBtnH = 38;
            double classGap = 6;
            for (int i = 0; i < classKeys.length; i++) {
                double fy = classFilterY + i * (classBtnH + classGap);
                if (x >= classFilterX && x <= classFilterX + classBtnW && y >= fy && y <= fy + classBtnH) {
                    state.encyclopediaClassFilter = classKeys[i];
                    state.encyclopediaPage = 0;
                    return;
                }
            }
        }

        // ── 类型筛选按钮（仅怪物模块） ──
        if ("enemies".equals(state.encyclopediaModule)) {
            String[] filterTypes = {null, "普通", "精英", "Boss"};
            double filterX = 30;
            double filterY = 150;
            double btnW = 110;
            double btnH = 38;
            double gap = 8;
            for (int i = 0; i < filterTypes.length; i++) {
                double fy = filterY + i * (btnH + gap);
                if (x >= filterX && x <= filterX + btnW && y >= fy && y <= fy + btnH) {
                    state.encyclopediaFilter = filterTypes[i];
                    state.encyclopediaPage = 0;
                    return;
                }
            }
        }

        // 模块切换
        String[] modules = {"cards", "relics", "statuses", "enemies", "items"};
        double btnWidth = 200;
        double startX = Constants.BASE_WIDTH / 2 - (modules.length * btnWidth) / 2.0;
        double moduleY = 80;
        for (int i = 0; i < modules.length; i++) {
            double bx = startX + i * btnWidth;
            if (x >= bx && x <= bx + btnWidth - 10 && y >= moduleY && y <= moduleY + 40) {
                state.encyclopediaModule = modules[i];
                state.encyclopediaPage = 0;
                if (!"cards".equals(modules[i]) && !"enemies".equals(modules[i])) {
                    state.encyclopediaFilter = null;
                    state.encyclopediaClassFilter = null;
                } else if ("cards".equals(modules[i])) {
                    state.encyclopediaClassFilter = null;
                }
                return;
            }
        }

        // 翻页按钮
        double prevX = Constants.BASE_WIDTH / 2 - 120;
        double prevY = Constants.BASE_HEIGHT - 60;
        if (x >= prevX && x <= prevX + 80 && y >= prevY && y <= prevY + 35) {
            state.encyclopediaPage = Math.max(0, state.encyclopediaPage - 1);
            return;
        }
        double nextX = Constants.BASE_WIDTH / 2 + 40;
        double nextY = Constants.BASE_HEIGHT - 60;
        if (x >= nextX && x <= nextX + 80 && y >= nextY && y <= nextY + 35) {
            state.encyclopediaPage++;
            return;
        }

        // 返回按钮（右下角，与渲染位置一致）
        double backX = Constants.BASE_WIDTH - 120;
        double backY = Constants.BASE_HEIGHT - 60;
        double backW = 100;
        double backH = 35;
        if (x >= backX && x <= backX + backW && y >= backY && y <= backY + backH) {
            if (state.prevPhase != null) {
                state.phase = state.prevPhase;
                state.prevPhase = null;
            } else {
                state.phase = GamePhase.TITLE;
            }
        }
    }

    // ================================================================
    //  卡组查看界面事件
    // ================================================================

    /**
     * 处理卡组查看界面的事件：翻页、返回。
     */
    private static void handleDeckViewEvents(GameState state, double x, double y) {
        // "返回" 按钮（右下角）
        double backBtnX = Constants.BASE_WIDTH - 120;
        double backBtnY = Constants.BASE_HEIGHT - 60;
        if (x >= backBtnX && x <= backBtnX + 100 && y >= backBtnY && y <= backBtnY + 35) {
            state.phase = state.prevPhase != null ? state.prevPhase : GamePhase.COMBAT;
            return;
        }

        // 翻页按钮
        if (state.deckViewPage > 0 && x >= Constants.BASE_WIDTH / 2 - 120 &&
                x <= Constants.BASE_WIDTH / 2 - 40 && y >= Constants.BASE_HEIGHT - 60 &&
                y <= Constants.BASE_HEIGHT - 25) {
            state.deckViewPage--;
            return;
        }
        if (x >= Constants.BASE_WIDTH / 2 + 40 && x <= Constants.BASE_WIDTH / 2 + 120 &&
                y >= Constants.BASE_HEIGHT - 60 && y <= Constants.BASE_HEIGHT - 25) {
            state.deckViewPage++;
        }
    }

    // ================================================================
    //  遗物查看界面事件
    // ================================================================

    /**
     * 处理遗物查看界面的事件：翻页、返回。
     */
    private static void handleRelicViewEvents(GameState state, double x, double y) {
        // "返回" 按钮
        double backBtnX = Constants.BASE_WIDTH - 120;
        double backBtnY = Constants.BASE_HEIGHT - 60;
        if (x >= backBtnX && x <= backBtnX + 100 && y >= backBtnY && y <= backBtnY + 35) {
            state.phase = state.prevPhase != null ? state.prevPhase : GamePhase.COMBAT;
            return;
        }

        // 翻页按钮
        if (state.relicViewPage > 0 && x >= Constants.BASE_WIDTH / 2 - 120 &&
                x <= Constants.BASE_WIDTH / 2 - 40 && y >= Constants.BASE_HEIGHT - 60 &&
                y <= Constants.BASE_HEIGHT - 25) {
            state.relicViewPage--;
            return;
        }
        if (x >= Constants.BASE_WIDTH / 2 + 40 && x <= Constants.BASE_WIDTH / 2 + 120 &&
                y >= Constants.BASE_HEIGHT - 60 && y <= Constants.BASE_HEIGHT - 25) {
            state.relicViewPage++;
        }
    }

    // ================================================================
    //  游戏结束界面事件
    // ================================================================

    /**
     * 处理游戏结束界面的事件：重新开始、返回菜单。
     */
    private static void handleGameOverEvents(GameState state, double x, double y) {
        double btnW = 200;
        double btnH = 50;
        double btnY = 430;
        double gap = 40;

        // 重新开始按钮
        double restartX = Constants.BASE_WIDTH / 2 - btnW - gap / 2;
        if (x >= restartX && x <= restartX + btnW && y >= btnY && y <= btnY + btnH) {
            state.reset();
            state.phase = GamePhase.TITLE;
            return;
        }

        // 返回菜单按钮
        double quitX = Constants.BASE_WIDTH / 2 + gap / 2;
        if (x >= quitX && x <= quitX + btnW && y >= btnY && y <= btnY + btnH) {
            state.reset();
            state.phase = GamePhase.TITLE;
        }
    }

    // ================================================================
    //  角色加点界面事件（设置弹窗）
    // ================================================================

    /**
     * 处理设置弹窗的点击事件。
     */
    private static void handleSettingsEvents(GameState state, double x, double y) {
        // 设置弹窗处理逻辑与 handleTitleEvents 中的设置弹窗一致
        handleSettingsPopupClick(state, x, y);
    }

    // ================================================================
    //  教程点击事件
    // ================================================================

    /**
     * 处理教程覆盖层的点击事件。
     */
    public static void handleTutorialEvents(GameState state, double x, double y) {
        if (state.tutorialStep < 0) return;

        // 教程总步骤数，与 GameRenderer.drawTutorial 中的 tutorialSteps 长度一致
        final int TOTAL_STEPS = 6;

        double btnW = 120;
        double btnH = 36;
        double btnY = Constants.BASE_HEIGHT - 70; // 与 render 中的 btnY2 一致

        // 下一步/完成按钮（与 render 位置一致：nextBtnX = BASE_WIDTH / 2 + 10）
        double nextBtnX = Constants.BASE_WIDTH / 2 + 10;
        if (x >= nextBtnX && x <= nextBtnX + btnW && y >= btnY && y <= btnY + btnH) {
            state.tutorialStep++;
            if (state.tutorialStep >= TOTAL_STEPS) {
                state.tutorialActive = false;
                state.tutorialStep = -1;
            }
            return;
        }

        // 跳过按钮（与 render 位置一致：skipBtnX = BASE_WIDTH / 2 - btnW - 10）
        double skipBtnX = Constants.BASE_WIDTH / 2 - btnW - 10;
        if (x >= skipBtnX && x <= skipBtnX + btnW && y >= btnY && y <= btnY + btnH) {
            state.tutorialActive = false;
            state.tutorialStep = -1;
            return;
        }

        // 点击教程内容区域也前进（只在地图/战斗阶段适用）
        double tutorialContentY = 150;
        double tutorialContentH = 300;
        if (y >= tutorialContentY && y <= tutorialContentY + tutorialContentH) {
            state.tutorialStep++;
            if (state.tutorialStep >= TOTAL_STEPS) {
                state.tutorialActive = false;
                state.tutorialStep = -1;
            }
        }
    }

    // ================================================================
    //  辅助方法
    // ================================================================

    /**
     * 检测鼠标是否悬浮在手牌上方，更新悬停卡牌信息。
     * 对应 Python 版 _update_hovered_card。
     */
    private static void updateHoveredCard(GameState state, double mouseX, double mouseY) {
        if (state.player == null) {
            state.hoveredCard = null;
            return;
        }

        List<Card> hand = state.player.getHand();
        if (hand.isEmpty()) {
            state.hoveredCard = null;
            return;
        }

        double handX = Constants.BASE_WIDTH / 2 - (hand.size() * 110) / 2.0;
        double cardY = Constants.BASE_HEIGHT - 160;
        state.hoveredCard = null;

        for (int i = 0; i < hand.size(); i++) {
            double cardX = handX + i * 110;
            if (mouseX >= cardX && mouseX <= cardX + 100 && mouseY >= cardY && mouseY <= cardY + 140) {
                state.hoveredCard = hand.get(i);
                state.hoveredCardX = (int) cardX;
                state.hoveredCardY = (int) cardY;
                break;
            }
        }
    }

    // ================================================================
    //  辅助方法：初始化玩家角色
    // ================================================================

    /**
     * 根据选择的职业创建玩家实例，添加初始卡牌和遗物。
     */
    private static void initPlayer(GameState state) {
        String charClass = state.selectedCharacter;
        CharacterClass cc = CharacterClass.fromValue(charClass);

        // 根据职业设置初始属性（与Python版一致）
        int hp;
        int maxHp;
        String starterRelic;
        String[] classCards;
        switch (charClass) {
            case "warrior":
                hp = 80; maxHp = 80;
                starterRelic = "burning_blood";
                classCards = new String[]{"uppercut", "bash"};
                break;
            case "mage":
                hp = 70; maxHp = 70;
                starterRelic = "ring_of_snake";
                classCards = new String[]{"fireball", "ice_spike"};
                break;
            case "rogue":
                hp = 65; maxHp = 65;
                starterRelic = "golden_fleece";
                classCards = new String[]{"oppression", "buy"};
                break;
            case "priest":
                hp = 70; maxHp = 70;
                starterRelic = "scepter";
                classCards = new String[]{"judgment", "holy_shield"};
                break;
            default:
                hp = 80; maxHp = 80;
                starterRelic = "burning_blood";
                classCards = new String[]{"uppercut", "bash"};
                break;
        }

        Player player = new Player(cc, hp, maxHp, 99, 1);

        // 添加初始卡牌：4 strike + 4 defend
        for (int i = 0; i < 4; i++) {
            player.addCard(CardFactory.createCard("strike"));
            player.addCard(CardFactory.createCard("defend"));
        }

        // 添加2张职业专属卡
        for (String cardKey : classCards) {
            player.addCard(CardFactory.createCard(cardKey));
        }

        // 添加初始遗物
        Relic relic = RelicPool.getRelic(starterRelic);
        if (relic != null) {
            player.getRelics().add(relic);
        }

        state.player = player;
    }

    // ================================================================
    //  辅助方法：翻译名称
    // ================================================================

    private static String getCardName(Card card) {
        return LangManager.getInstance().getText("cards." + card.getNameKey(), card.getNameKey());
    }

    private static String getEnemyName(Enemy enemy) {
        return LangManager.getInstance().getText("enemies." + enemy.getNameKey(), enemy.getNameKey());
    }

    // ================================================================
    //  辅助方法：出牌
    // ================================================================

    /**
     * 执行出牌逻辑。
     */
    private static void playCard(GameState state, Card card, Enemy target) {
        if (state.player == null) return;

        // ── 圣能检查 ──
        if (card.hasEffectType("consume_holy_energy")) {
            int holyNeeded = 2; // 默认消耗2层圣能
            if (card.getEffect() instanceof Map) {
                // focused_ray消耗2层, bodyguard消耗2层, 光能波动消耗全部
                for (Map.Entry<String, Object> entry : ((Map<String, Object>) card.getEffect()).entrySet()) {
                    if ("value".equals(entry.getKey())) {
                        holyNeeded = ((Number) entry.getValue()).intValue();
                    }
                }
            }
            int currentHoly = 0;
            for (Map<String, Object> s : state.player.getStatusEffects()) {
                if ("holy_energy".equals(s.get("type"))) {
                    currentHoly = ((Number) s.getOrDefault("value", 0)).intValue();
                    break;
                }
            }
            if (currentHoly < holyNeeded) {
                state.floatingMessage = "圣能不足";
                state.floatingMessageTimer = 180; // 3秒
                return;
            }
        }

        // ── 能量检查 ──
        if (state.player.getEnergy() < card.getCost() && card.getCost() != 0) {
            state.floatingMessage = "能量不足";
            state.floatingMessageTimer = 180;
            return;
        }

        // ── 金币检查 ──
        if (card.hasEffectType("future_strike_gold")) {
            int needGold = 10;
            if (card.getEffect() instanceof Map) {
                Object val = ((Map<String, Object>) card.getEffect()).get("value");
                if (val instanceof Number) needGold = ((Number) val).intValue();
            }
            if (state.player.getGold() < needGold) {
                state.floatingMessage = "金币不足";
                state.floatingMessageTimer = 180;
                return;
            }
        }

        // 计算卡牌在手牌中的位置（用于动画起始位置）
        List<Card> hand = state.player.getHand();
        double handX = Constants.BASE_WIDTH / 2 - (hand.size() * 110) / 2.0;
        double cardStartX = handX;
        double cardStartY = Constants.BASE_HEIGHT - 160;
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i) == card) {
                cardStartX = handX + i * 110;
                break;
            }
        }

        // 获取所有敌人
        List<Enemy> allEnemies = new ArrayList<>();
        for (Object e : state.enemies) {
            if (e instanceof Enemy && ((Enemy) e).getHp() > 0) {
                allEnemies.add((Enemy) e);
            }
        }

        // 调用 Player.playCard
        boolean success = state.player.playCard(card, target, allEnemies);
        if (success) {
            state.combatLog.add("打出 " + getCardName(card));

            // 设置卡牌打出动画
            state.animCardPlaying = card;
            state.animCardTimer = 20;
            state.animCardSx = (int) cardStartX;
            state.animCardSy = (int) cardStartY;
            state.animCardEx = (int) (Constants.BASE_WIDTH / 2);
            state.animCardEy = (int) (Constants.BASE_HEIGHT / 2 - 50);

            // 处理敌人死亡
            state.processEnemyDeaths();

            // 检查是否所有敌人死亡
            boolean allDead = true;
            for (Object e : state.enemies) {
                if (e instanceof Enemy && ((Enemy) e).getHp() > 0) {
                    allDead = false;
                    break;
                }
            }
            if (allDead) {
                state.generateReward();
                state.phase = GamePhase.REWARD;
            }

            // 检查玩家是否死亡
            if (state.player.getHp() <= 0) {
                state.result = "lose";
                state.phase = GamePhase.GAME_OVER;
            }
        }
    }
}