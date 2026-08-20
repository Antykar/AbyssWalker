package com.abyss.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多语言管理器（单例）。
 * <p>
 * 使用 Jackson 的 ObjectMapper 从类路径加载 lang.json 文件，
 * 提供 {@link #getText(String)} 方法以点分键路径访问文案，
 * 例如 {@code getText("cards.strike")} 返回 "打击"。
 * <p>
 * 设计为单例以避免反复读取与解析 lang.json。
 */
public class LangManager {

    private static volatile LangManager instance;

    private final ObjectMapper objectMapper;
    private Map<String, Object> data;

    /**
     * 私有构造方法，初始化 ObjectMapper 并加载语言数据。
     */
    private LangManager() {
        this.objectMapper = new ObjectMapper();
        this.data = new ConcurrentHashMap<>();
        load();
    }

    /**
     * 获取单例实例。
     * <p>
     * 首次调用时自动创建实例并加载语言文件，后续调用直接返回已有实例。
     *
     * @return LangManager 单例
     */
    public static LangManager getInstance() {
        if (instance == null) {
            synchronized (LangManager.class) {
                if (instance == null) {
                    instance = new LangManager();
                }
            }
        }
        return instance;
    }

    /**
     * 从类路径加载 lang.json 数据。
     * <p>
     * 若文件不存在或解析失败，data 会被置为空 Map，
     * getText 将返回默认值。
     */
    @SuppressWarnings("unchecked")
    public void load() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("lang.json")) {
            if (inputStream == null) {
                System.err.println("语言文件 lang.json 未在类路径中找到");
                this.data = new ConcurrentHashMap<>();
                return;
            }
            Map<String, Object> rawData = objectMapper.readValue(
                inputStream, new TypeReference<Map<String, Object>>() {});
            this.data = new ConcurrentHashMap<>(rawData);
        } catch (Exception e) {
            System.err.println("语言文件加载失败: " + e.getMessage());
            this.data = new ConcurrentHashMap<>();
        }
    }

    /**
     * 以点分键路径获取文案。
     *
     * @param key 点分路径键，例如 "menu.title" 或 "cards.strike"
     * @return 查到的字符串，若键不存在返回空字符串
     */
    @SuppressWarnings("unchecked")
    public String getText(String key) {
        return getText(key, "");
    }

    /**
     * 以点分键路径获取文案，支持默认值。
     *
     * @param key      点分路径键，例如 "menu.title" 或 "cards.strike"
     * @param default  键不存在时返回的默认值
     * @return 查到的字符串或 default
     */
    @SuppressWarnings("unchecked")
    public String getText(String key, String defaultText) {
        String[] keys = key.split("\\.");
        Object value = data;
        for (String k : keys) {
            if (value instanceof Map) {
                value = ((Map<String, Object>) value).get(k);
            } else {
                return defaultText;
            }
        }
        return value instanceof String ? (String) value : defaultText;
    }

    /**
     * 重新加载语言数据（例如切换语言后调用）。
     */
    public void reload() {
        load();
    }
}