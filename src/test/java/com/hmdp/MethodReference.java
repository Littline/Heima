package com.hmdp;

import org.junit.Test;

import java.util.*;

public class MethodReference {
    @Test
    public  void MaxConflicts(){
            int n = 10; // 示例值，可以根据需要修改
            int result = maxNonConflictingCount(n);
            System.out.println("Maximum number of non-conflicting numbers: " + result);
    }

    @Test
    public  void bigDui(){
        int[] data={5,13,24,35,22,79,86,75,64,53,29};
        int DEFAULT_INITIAL_CAPACITY = data.length;
        PriorityQueue<Integer> maxHeap=new PriorityQueue<Integer>(DEFAULT_INITIAL_CAPACITY, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        for( int i=0;i<data.length;i++){
            maxHeap.add(data[i]);
            System.out.println(maxHeap);
        }
        System.out.println(maxHeap);
    }



        public  int maxNonConflictingCount(int n) {
            if (n < 2) return 0;

            // 1. 生成小于n的素数
            List<Integer> primes = sieveOfEratosthenes(n);

            // 2. 构建冲突关系图
            List<List<Integer>> graph = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for (int i = 1; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (gcd(i, j) != 1 && isPrime(gcd(i, j), primes)) {
                        graph.get(i).add(j);
                        graph.get(j).add(i);
                    }
                }
            }

            // 3. 使用 DFS 或 BFS 找到最大独立集
            boolean[] visited = new boolean[n];
            int count = 0;

            for (int i = 1; i < n; i++) {
                if (!visited[i]) {
                    count += dfs(graph, visited, i);
                }
            }

            return count;
        }

        // 使用埃拉托斯特尼筛法生成素数
        public  List<Integer> sieveOfEratosthenes(int n) {
            boolean[] isPrime = new boolean[n];
            Arrays.fill(isPrime, true);
            List<Integer> primes = new ArrayList<>();
            isPrime[0] = isPrime[1] = false; // 0和1不是素数

            for (int p = 2; p < n; p++) {
                if (isPrime[p]) {
                    primes.add(p);
                    for (int i = p * 2; i < n; i += p) {
                        isPrime[i] = false;
                    }
                }
            }
            return primes;
        }

        // 计算GCD
        public  int gcd(int a, int b) {
            while (b != 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }

        // 检查一个数是否是素数
        public  boolean isPrime(int num, List<Integer> primes) {
            return primes.contains(num);
        }

        // DFS 遍历图，计算最大独立集
        private  int dfs(List<List<Integer>> graph, boolean[] visited, int node) {
            visited[node] = true;
            int size = 1; // 选择当前节点
            for (int neighbor : graph.get(node)) {
                if (!visited[neighbor]) {
                    size += dfs(graph, visited, neighbor);
                }
            }
            return size;
        }


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
