package com.abyss.system;

import javafx.scene.image.Image;
import javafx.scene.text.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源管理器，负责加载和缓存游戏中的图片、字体等资源。
 * <p>
 * 所有资源文件存放在 src/main/resources/ 目录下。
 * 图片存放在 images/ 子目录，字体存放在 fonts/ 子目录。
 */
public class ResourceManager {

    private static ResourceManager instance;
    private final Map<String, Image> imageCache = new HashMap<>();
    private boolean fontLoaded = false;
    /** 自定义字体名称（从 simhei.ttf 读取的实际字体族名）。 */
    public static String CUSTOM_FONT_NAME = "SimHei"; // 默认值，加载后会被覆盖

    private ResourceManager() {}

    public static synchronized ResourceManager get() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    /**
     * 加载指定名称的图片（从 images/ 目录）。
     *
     * @param name 图片文件名（如 "warrior.png"）
     * @return Image 对象，如果加载失败返回 null
     */
    public Image loadImage(String name) {
        if (imageCache.containsKey(name)) {
            return imageCache.get(name);
        }
        try {
            String path = "images/" + name;
            var url = getClass().getClassLoader().getResource(path);
            if (url == null) {
                imageCache.put(name, null);
                return null;
            }
            Image img = new Image(url.toExternalForm());
            imageCache.put(name, img);
            return img;
        } catch (Exception e) {
            System.err.println("加载图片失败: " + name + " - " + e.getMessage());
            imageCache.put(name, null);
            return null;
        }
    }

    /**
     * 加载自定义字体 simhei.ttf。
     * <p>
     * 先后尝试 classpath InputStream、URL 和文件系统路径，确保字体被正确注册到 JavaFX 字体系统。
     * 加载成功后读取实际字体族名，用于后续创建不同字号的字体。
     */
    public void loadCustomFont() {
        if (fontLoaded) return;
        Exception lastError = null;

        // 尝试1：classpath InputStream 加载（最可靠）
        try (var is = getClass().getClassLoader().getResourceAsStream("fonts/simhei.ttf")) {
            if (is != null) {
                Font loaded = Font.loadFont(is, 14);
                if (loaded != null) {
                    CUSTOM_FONT_NAME = loaded.getFamily();
                }
                fontLoaded = true;
                System.out.println("自定义字体 " + CUSTOM_FONT_NAME + " 加载成功 (InputStream)");
                return;
            }
        } catch (Exception e) {
            lastError = e;
        }

        // 尝试2：classpath URL 加载
        try {
            var url = getClass().getClassLoader().getResource("fonts/simhei.ttf");
            if (url != null) {
                Font loaded = Font.loadFont(url.toExternalForm(), 14);
                if (loaded != null) {
                    CUSTOM_FONT_NAME = loaded.getFamily();
                }
                fontLoaded = true;
                System.out.println("自定义字体 " + CUSTOM_FONT_NAME + " 加载成功 (URL)");
                return;
            }
        } catch (Exception e) {
            lastError = e;
        }

        // 尝试3：绝对路径加载
        try {
            var url = getClass().getResource("/fonts/simhei.ttf");
            if (url != null) {
                Font loaded = Font.loadFont(url.toExternalForm(), 14);
                if (loaded != null) {
                    CUSTOM_FONT_NAME = loaded.getFamily();
                }
                fontLoaded = true;
                System.out.println("自定义字体 " + CUSTOM_FONT_NAME + " 加载成功 (absolute path)");
                return;
            }
        } catch (Exception e) {
            lastError = e;
        }

        System.err.println("字体文件未找到: fonts/simhei.ttf - " +
                (lastError != null ? lastError.getMessage() : "所有路径均未找到"));
    }

    /**
     * 获取应用图标。
     */
    public Image getAppIcon() {
        return loadImage("tubiao.png");
    }

    /**
     * 获取角色头像。
     */
    public Image getCharacterPortrait(String className) {
        return loadImage(className + ".png");
    }

    /**
     * 获取角色选中标记。
     */
    public Image getCheckMark(String className) {
        return loadImage("check_" + className + ".png");
    }

    /**
     * 清空缓存（用于重新加载资源）。
     */
    public void clearCache() {
        imageCache.clear();
        fontLoaded = false;
    }
}