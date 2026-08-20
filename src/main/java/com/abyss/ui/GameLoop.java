package com.abyss.ui;

import com.abyss.state.GameState;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.Screen;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

/**
 * 游戏主循环 —— 基于 javafx.animation.AnimationTimer 的帧循环。
 * <p>
 * 每帧执行：处理输入事件队列 → 更新游戏状态 → 调用渲染器绘制。
 * 对应 Python 版 roguelike_card_game.py 的主循环。
 */
public class GameLoop extends AnimationTimer {

    /** 游戏全局状态。 */
    private final GameState state;

    /** 渲染画布。 */
    private final Canvas canvas;

    /** 主窗口 Stage（用于全屏切换）。 */
    private Stage stage;

    // ── 输入状态 ──

    /** 当前鼠标位置（窗口坐标）。 */
    private double mouseX;
    private double mouseY;

    /** 鼠标点击事件队列（用于在帧循环中安全处理事件）。 */
    private final Queue<ClickEvent> clickQueue = new ArrayDeque<>();

    /**
     * 创建一个新的游戏主循环。
     *
     * @param state  游戏状态实例
     * @param canvas 渲染画布
     */
    public GameLoop(GameState state, Canvas canvas) {
        this.state = state;
        this.canvas = canvas;
    }

    /**
     * 设置主窗口 Stage（用于全屏切换）。
     *
     * @param stage 主窗口
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // ================================================================
    //  输入事件注册（由 AbyssWalkerGame 调用）
    // ================================================================

    /**
     * 记录鼠标位置更新（由鼠标移动事件触发）。
     *
     * @param x 窗口坐标 x
     * @param y 窗口坐标 y
     */
    public void onMouseMoved(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    /**
     * 将鼠标点击事件加入队列（由鼠标点击事件触发）。
     *
     * @param x      窗口坐标 x
     * @param y      窗口坐标 y
     * @param button 鼠标按钮
     */
    public void onMouseClicked(double x, double y, MouseButton button) {
        int btn = switch (button) {
            case PRIMARY -> 1;
            case SECONDARY -> 3;
            case MIDDLE -> 2;
            default -> 0;
        };
        clickQueue.add(new ClickEvent(x, y, btn));
    }

    /**
     * 处理键盘按下事件。
     *
     * @param keyCode 按键代码
     */
    public void onKeyPressed(KeyCode keyCode) {
        GameInputHandler.handleKeyPress(state, keyCode);
    }

    // ================================================================
    //  AnimationTimer 回调
    // ================================================================

    @Override
    public void handle(long now) {
        // 0. 处理全屏切换请求
        if (state.fullscreenRequested && stage != null) {
            stage.setFullScreen(state.isFullscreen);
            state.fullscreenRequested = false;
        }

        // 1. 处理输入事件队列
        processInputQueue();

        // 2. 更新游戏状态（动画计时器等）
        updateState();

        // 3. 调用渲染器绘制
        render();
    }

    // ================================================================
    //  内部方法
    // ================================================================

    /**
     * 处理输入事件队列中的所有待处理事件。
     */
    private void processInputQueue() {
        // 处理鼠标移动（悬停检测）
        GameInputHandler.handleMouseMove(state, mouseX, mouseY);

        // 处理鼠标点击队列
        ClickEvent event;
        while ((event = clickQueue.poll()) != null) {
            GameInputHandler.handleMouseClick(state, event.x, event.y, event.button);
        }

        // 处理教程覆盖层（如果激活）
        if (state.tutorialActive) {
            // 教程事件由 GameInputHandler 在处理点击时一并处理
        }
    }

    /**
     * 更新游戏状态（动画计时器递减、敌人行动队列处理等）。
     */
    private void updateState() {
        // 动画计时器递减
        if (state.animCardTimer > 0) {
            state.animCardTimer--;
            if (state.animCardTimer <= 0) {
                state.animCardPlaying = null;
            }
        }
        if (state.animDamageTimer > 0) {
            state.animDamageTimer--;
        }
        if (state.animAttackTimer > 0) {
            state.animAttackTimer--;
        }
        if (state.animEnemyDamageTimer > 0) {
            state.animEnemyDamageTimer--;
        }
        if (state.animEnemyActionTimer > 0) {
            state.animEnemyActionTimer--;
        }
        if (state.floatingMessageTimer > 0) {
            state.floatingMessageTimer--;
        }

        // 敌人行动队列处理（逐帧驱动）
        if (state.enemyActionState.equals("acting")) {
            processEnemyActionQueue();
        }

        // 战斗结束过渡
        if (state.combatEnding && state.animCardTimer <= 0) {
            state.combatEnding = false;
            if (state.selectedNode != null) {
                state.selectedNode.setCompleted(true);
            }
            state.phase = com.abyss.state.GamePhase.REWARD;
        }
    }

    /**
     * 逐帧处理敌人行动队列。
     * <p>
     * 状态机：idle → acting（执行中）→ cleanup（后处理）→ idle。
     * 每帧仅处理一个行动，通过 enemyActionDelay 控制帧间隔。
     */
    private void processEnemyActionQueue() {
        // 延迟计数控制帧间隔
        if (state.enemyActionDelay > 0) {
            state.enemyActionDelay--;
            return;
        }

        // 当前索引超出队列，进入清理阶段
        if (state.animEnemyActionIdx >= state.enemyActionQueue.size() - 1) {
            // 清理阶段：队列中的行动已全部处理完毕
            state.enemyActionState = "idle";
            state.enemyActionQueue.clear();
            state.animEnemyActionIdx = -1;
            state.animEnemyActionTimer = 0;
            state.animEnemyActionType = null;

            // 处理敌人死亡效果
            if (state.player != null) {
                for (Object e : state.enemies) {
                    if (e instanceof com.abyss.model.Enemy) {
                        ((com.abyss.model.Enemy) e).tickStatus(state.player);
                    }
                }
            }
            state.processEnemyDeaths();
            return;
        }

        // 处理当前行动
        state.animEnemyActionIdx++;
        if (state.animEnemyActionIdx < state.enemyActionQueue.size()) {
            Map<String, Object> action = state.enemyActionQueue.get(state.animEnemyActionIdx);
            String actionType = (String) action.getOrDefault("type", "attack");
            int enemyIdx = action.get("enemy_idx") instanceof Number ? ((Number) action.get("enemy_idx")).intValue() : -1;
            int value = action.get("value") instanceof Number ? ((Number) action.get("value")).intValue() : 0;
            String message = (String) action.getOrDefault("message", "");

            state.animEnemyActionType = actionType;
            state.animEnemyActionTimer = 20; // 约 20 帧显示动画

            // 记录行动日志
            if (message != null && !message.isEmpty()) {
                state.combatLog.add(message);
                // 保持日志不超过 10 条
                while (state.combatLog.size() > 10) {
                    state.combatLog.remove(0);
                }
            }

            // 设置延迟，使行动之间有间隔（约 20 帧 ≈ 333ms）
            state.enemyActionDelay = 20;
        }
    }

    /**
     * 渲染当前帧 —— 委托给 GameRenderer 根据 game phase 绘制。
     */
    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // 清空画布由 GameRenderer 在每个绘制方法开头重新填充背景色完成
        GameRenderer.render(gc, state);
    }

    // ================================================================
    //  内部类型
    // ================================================================

    /** 鼠标点击事件记录。 */
    private record ClickEvent(double x, double y, int button) {
    }
}