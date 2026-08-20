package com.abyss;

import com.abyss.system.LangManager;
import com.abyss.system.ResourceManager;

/**
 * 资源加载测试 —— 验证 lang.json、图片、字体是否能正常加载。
 * <p>
 * 运行方式：mvn test -q 或直接在 IDE 中运行此文件。
 */
public class ResourceTest {

    public static void main(String[] args) {
        System.out.println("===== 资源加载测试 =====");

        // 1. 测试 lang.json 加载
        testLangJson();

        // 2. 测试图片加载
        testImageLoading();

        // 3. 测试字体文件是否存在
        testFontExistence();

        System.out.println("===== 测试完成 =====");
    }

    private static void testLangJson() {
        System.out.print("[测试] lang.json 加载... ");
        try {
            // 测试类路径根目录下的 lang.json
            var url = ResourceTest.class.getClassLoader().getResource("lang.json");
            if (url != null) {
                System.out.println("OK - 找到文件: " + url);
            } else {
                System.out.println("失败 - 未找到 lang.json");
                return;
            }

            // 测试 LangManager 是否能正常读取
            LangManager lang = LangManager.getInstance();
            String title = lang.getText("menu.title", "");
            if (!title.isEmpty()) {
                System.out.println("  -> LangManager 读取成功: menu.title = '" + title + "'");
            } else {
                System.out.println("  -> LangManager 读取失败: 返回空字符串");
            }
        } catch (Exception e) {
            System.out.println("失败 - " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testImageLoading() {
        System.out.println("[测试] 图片资源加载...");
        String[] testImages = {
            "warrior.png", "mage.png", "rogue.png", "priest.png",
            "shenyuan.png", "strike.png", "defend.png",
            "check_warrior.png", "check_mage.png", "check_rogue.png", "check_priest.png",
            "tubiao.png"
        };
        int successCount = 0;
        int failCount = 0;
        for (String imgName : testImages) {
            var url = ResourceTest.class.getClassLoader().getResource("images/" + imgName);
            if (url != null) {
                successCount++;
            } else {
                System.out.println("  失败 - 未找到: images/" + imgName);
                failCount++;
            }
        }
        System.out.println("  结果: " + successCount + " 成功, " + failCount + " 失败 (共 " + testImages.length + ")");
    }

    private static void testFontExistence() {
        System.out.print("[测试] 字体文件... ");
        var url = ResourceTest.class.getClassLoader().getResource("fonts/simhei.ttf");
        if (url != null) {
            System.out.println("OK - 找到: " + url);
        } else {
            System.out.println("失败 - 未找到 fonts/simhei.ttf");
        }
    }
}