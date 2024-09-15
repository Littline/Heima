package com.hmdp;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class MethodReference {
    @Test
    public void instanceMethod(){
        Arrays.asList("a", "b", "c").forEach(String::toUpperCase);
        Arrays.asList("a", "b", "c").forEach(s -> System.out.println(s.toUpperCase()));
    }

    @Test
    public void testLRUCache() {
        // 构造LRU缓存对象
        LRUCache<Integer, String> lruCache = new LRUCache<>(5);

        // 添加数据
        lruCache.put(1, "One");
        lruCache.put(2, "Two");
        lruCache.put(3, "Three");
        lruCache.put(4, "Four");
        lruCache.put(5, "Five");

        // 打印初始缓存
        System.out.println("Initial Cache: " + lruCache);

        // 访问元素，将其移到最近使用的位置
        lruCache.get(3);
        System.out.println("Cache after accessing 3: " + lruCache);

        // 添加新元素，可能导致淘汰最旧的元素
        lruCache.put(6, "Six");
        System.out.println("Cache after adding 6: " + lruCache);
    }

    // 定义LRU缓存类
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private static final int MAX_ENTRIES = 5; // 设置缓存的最大容量

        public LRUCache(int initialCapacity) {
            super(initialCapacity, 0.75f, true); // true表示按访问顺序排序
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > MAX_ENTRIES; // 超过最大容量时移除最旧的元素
        }
    }
}
