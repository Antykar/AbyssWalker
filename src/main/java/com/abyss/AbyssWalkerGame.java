package com.abyss;

import com.abyss.state.GameState;
import com.abyss.system.ResourceManager;
import com.abyss.ui.GameInputHandler;
import com.abyss.ui.GameLoop;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * 深渊行者-阿先出品 —— JavaFX 主入口。
 * <p>
 * 负责初始化窗口、画布、场景、事件监听和游戏主循环。
 * 对应 Python 版 roguelike_card_game.py 的 main() 入口。
 */
public class AbyssWalkerGame extends Application {

    /** 基准窗口宽度。 */
    private static final double BASE_WIDTH = 1200;
    /** 基准窗口高度。 */
    private static final double BASE_HEIGHT = 800;

    private Canvas canvas;
    private GameLoop gameLoop;
    private GameState gameState;

    @Override
    public void start(Stage primaryStage) {
        // ── 初始化资源管理器（加载字体、图片等） ──
        ResourceManager.get().loadCustomFont();

        // ── 设置窗口图标 ──
        Image icon = ResourceManager.get().getAppIcon();
        if (icon != null) {
            primaryStage.getIcons().add(icon);
        }

        // ── 创建画布（基准尺寸） ──
        canvas = new Canvas(BASE_WIDTH, BASE_HEIGHT);

        // ── 创建场景 ──
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, BASE_WIDTH, BASE_HEIGHT);

        // ── 初始化游戏状态 ──
        gameState = new GameState();

        // ── 创建游戏主循环 ──
        gameLoop = new GameLoop(gameState, canvas);
        gameLoop.setStage(primaryStage);

        // ── 注册鼠标事件监听（坐标转换为 canvas 本地坐标，以兼容窗口缩放居中） ──
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            Point2D local = canvas.sceneToLocal(e.getSceneX(), e.getSceneY());
            gameLoop.onMouseMoved(local.getX(), local.getY());
        });

        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            Point2D local = canvas.sceneToLocal(e.getSceneX(), e.getSceneY());
            gameLoop.onMouseClicked(local.getX(), local.getY(), e.getButton());
        });

        // ── 注册键盘事件监听 ──
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            gameLoop.onKeyPressed(e.getCode());
        });

        // ── 处理窗口大小变化（保持宽高比） ──
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            onWindowResize(scene.getWidth(), scene.getHeight());
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            onWindowResize(scene.getWidth(), scene.getHeight());
        });

        // ── 设置窗口 ──
        primaryStage.setTitle("深渊行者-阿先出品");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(BASE_WIDTH / 2);
        primaryStage.setMinHeight(BASE_HEIGHT / 2);
        primaryStage.show();

        // ── 启动游戏主循环 ──
        gameLoop.start();
    }

    /**
     * 处理窗口大小变化：更新 Canvas 尺寸和常量缩放比例。
     *
     * @param w 新宽度
     * @param h 新高度
     */
    private void onWindowResize(double w, double h) {
        // 保持宽高比（以基准比例为基准）
        double aspectRatio = BASE_WIDTH / BASE_HEIGHT;
        double newW = w;
        double newH = h;

        if (w / h > aspectRatio) {
            // 窗口过宽，以高度为基准
            newW = h * aspectRatio;
        } else {
            // 窗口过高，以宽度为基准
            newH = w / aspectRatio;
        }

        canvas.setWidth(newW);
        canvas.setHeight(newH);

        // 更新常量缩放比例
        com.abyss.constants.Constants.updateScreenSize(newW, newH);
    }

    /**
     * 程序主入口。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        launch(args);
    }
}