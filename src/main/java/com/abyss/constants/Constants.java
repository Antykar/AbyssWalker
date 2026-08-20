package com.abyss.constants;

import javafx.scene.paint.Color;

/**
 * 全局常量与坐标转换工具类。
 *
 * 颜色常量对应 Python 版 constants.py 中的调色板；
 * rx/ry/rs 系列函数用于将基准分辨率下的坐标/尺寸
 * 按比例缩放到当前窗口坐标，供 JavaFX GraphicsContext 绘制时使用。
 */
public final class Constants {

    private Constants() {
        // 工具类，禁止实例化
    }

    // ============== 基准分辨率 ==============
    /** 基准宽度（逻辑像素）。 */
    public static final double BASE_WIDTH = 1200;
    /** 基准高度（逻辑像素）。 */
    public static final double BASE_HEIGHT = 800;

    // ============== 当前窗口尺寸（动态更新） ==============
    private static double screenWidth = BASE_WIDTH;
    private static double screenHeight = BASE_HEIGHT;

    /**
     * 更新当前窗口尺寸（由窗口 resize 事件触发时调用）。
     *
     * @param width  当前窗口实际宽度
     * @param height 当前窗口实际高度
     */
    public static void updateScreenSize(double width, double height) {
        screenWidth = Math.max(width, BASE_WIDTH / 2);
        screenHeight = Math.max(height, BASE_HEIGHT / 2);
    }

    /**
     * 获取当前窗口宽度。
     *
     * @return 当前窗口实际宽度
     */
    public static double getScreenWidth() {
        return screenWidth;
    }

    /**
     * 获取当前窗口高度。
     *
     * @return 当前窗口实际高度
     */
    public static double getScreenHeight() {
        return screenHeight;
    }

    // ============== 颜色常量 ==============
    /** 主背景色：深黑偏蓝 (10, 10, 20)。 */
    public static final Color BLACK = Color.rgb(10, 10, 20);
    /** 次背景色：深紫 (30, 20, 50)，营造地牢氛围。 */
    public static final Color DARK_PURPLE = Color.rgb(30, 20, 50);
    /** 强调色：紫色 (60, 40, 100)。 */
    public static final Color PURPLE = Color.rgb(60, 40, 100);
    /** 高亮色：浅紫 (100, 80, 150)。 */
    public static final Color LIGHT_PURPLE = Color.rgb(100, 80, 150);
    /** 金币/高级卡/增益色：金色 (255, 215, 0)。 */
    public static final Color GOLD = Color.rgb(255, 215, 0);
    /** 攻击/受伤/危险色：红色 (200, 50, 50)。 */
    public static final Color RED = Color.rgb(200, 50, 50);
    /** 治疗/格挡/增益色：绿色 (50, 200, 50)。 */
    public static final Color GREEN = Color.rgb(50, 200, 50);
    /** 防御/技能色：蓝色 (50, 100, 200)。 */
    public static final Color BLUE = Color.rgb(50, 100, 200);
    /** 主文字色：暖白 (240, 240, 255)。 */
    public static final Color WHITE = Color.rgb(240, 240, 255);
    /** 次要文字色：灰 (100, 100, 120)。 */
    public static final Color GRAY = Color.rgb(100, 100, 120);

    // ============== 坐标转换工具 ==============

    /**
     * 将基准 x 坐标按比例缩放为当前窗口坐标。
     *
     * @param x 基准分辨率（BASE_WIDTH）下的 x 坐标
     * @return 当前窗口下的实际 x 坐标
     */
    public static double rx(double x) {
        return x * screenWidth / BASE_WIDTH;
    }

    /**
     * 将基准 y 坐标按比例缩放为当前窗口坐标。
     *
     * @param y 基准分辨率（BASE_HEIGHT）下的 y 坐标
     * @return 当前窗口下的实际 y 坐标
     */
    public static double ry(double y) {
        return y * screenHeight / BASE_HEIGHT;
    }

    /**
     * 将基准坐标 (x, y) 按比例缩放到当前窗口坐标。
     *
     * @param x 基准分辨率下的 x 坐标
     * @param y 基准分辨率下的 y 坐标
     * @return 包含缩放后 x, y 的数组 [scaledX, scaledY]
     */
    public static double[] scalePos(double x, double y) {
        return new double[]{rx(x), ry(y)};
    }

    /**
     * 将基准尺寸按比例缩放为当前窗口尺寸。
     * 取宽高中较小的缩放比例，保持宽高比。
     *
     * @param size 基准分辨率下的尺寸值
     * @return 当前窗口下的实际尺寸
     */
    public static double rs(double size) {
        double ratio = Math.min(screenWidth / BASE_WIDTH, screenHeight / BASE_HEIGHT);
        return size * ratio;
    }

    /**
     * 将当前窗口 x 坐标反向映射回基准坐标（用于鼠标事件处理）。
     *
     * @param x 当前窗口下的实际 x 坐标
     * @return 基准分辨率下的逻辑 x 坐标
     */
    public static double invRx(double x) {
        return x * BASE_WIDTH / screenWidth;
    }

    /**
     * 将当前窗口 y 坐标反向映射回基准坐标（用于鼠标事件处理）。
     *
     * @param y 当前窗口下的实际 y 坐标
     * @return 基准分辨率下的逻辑 y 坐标
     */
    public static double invRy(double y) {
        return y * BASE_HEIGHT / screenHeight;
    }

    /**
     * 将鼠标坐标反向映射回基准坐标。
     *
     * @param x 当前窗口下的实际 x 坐标
     * @param y 当前窗口下的实际 y 坐标
     * @return 包含逻辑 x, y 的数组 [logicalX, logicalY]
     */
    public static double[] invScalePos(double x, double y) {
        return new double[]{invRx(x), invRy(y)};
    }
}