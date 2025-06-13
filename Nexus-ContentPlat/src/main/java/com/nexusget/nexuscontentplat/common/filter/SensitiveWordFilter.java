package com.nexusget.nexuscontentplat.common.filter;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 敏感词过滤器（线程安全）
 */
@Component
public class SensitiveWordFilter {
    private final Map<String, Object> sensitiveWordMap = new ConcurrentHashMap<>();
    private final Set<String> stopWords = Set.of(" ", "*", "-"); // 忽略的间隔符

    /**
     * 初始化敏感词库
     * @param words 敏感词集合
     */
    @SuppressWarnings("unchecked")
    public void init(Collection<String> words) {
        for (String word : words) {
            Map<String, Object> currentMap = sensitiveWordMap;
            for (char c : word.toCharArray()) {
                String charStr = String.valueOf(c);
                if (stopWords.contains(charStr)) continue;
                currentMap = (Map<String, Object>)
                        currentMap.computeIfAbsent(charStr, k -> new ConcurrentHashMap<>());
            }
            currentMap.put("isEnd", true);
        }
    }

    /**
     * 检查是否包含敏感词
     */
    public boolean containsSensitive(String text) {
        return Optional.ofNullable(text)
                .filter(t -> !t.isEmpty())
                .map(t -> {
                    for (int i = 0; i < t.length(); i++) {
                        if (checkWord(t, i) > 0) return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    /**
     * 替换敏感词（Lambda风格）
     */
    public String filter(String text, Function<String, String> replacementFunc) {
        return Optional.ofNullable(text)
                .map(t -> {
                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i < t.length(); ) {
                        int len = checkWord(t, i);
                        if (len > 0) {
                            result.append(replacementFunc.apply(t.substring(i, i + len)));
                            i += len;
                        } else {
                            result.append(t.charAt(i));
                            i++;
                        }
                    }
                    return result.toString();
                })
                .orElse("");
    }
    @SuppressWarnings("unchecked")
    private int checkWord(String text, int startIndex) {
        Map<String, Object> currentMap = sensitiveWordMap;
        int matchLength = 0;

        for (int i = startIndex; i < text.length(); i++) {
            String c = String.valueOf(text.charAt(i));
            if (stopWords.contains(c)) continue;

            currentMap = (Map<String, Object>) currentMap.get(c);
            if (currentMap == null) break;

            matchLength++;
            if (Boolean.TRUE.equals(currentMap.get("isEnd"))) {
                return matchLength;
            }
        }
        return 0;
    }
}