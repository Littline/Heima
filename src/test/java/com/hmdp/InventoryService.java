package com.hmdp;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class InventoryService {
    private static RedissonClient redissonClient;

    static {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        redissonClient = Redisson.create(config);
    }

    public static void main(String[] args) {
        redissonClient.getBucket("product_stock").set(100);
        System.out.println("product_stock is: " + redissonClient.getBucket("product_stock").get());
        int[] largeArray = new int[10000000];
//        for(int i=0;i<largeArray.length;i++){
//            largeArray[i]=i+1;
//            System.out.println(i+Thread.currentThread().getName());
//        }
        for (int i = 0; i < 105; i++) {
            new Thread(InventoryService::purchaseProduct).start();
        }
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> lists = groupAnagrams(strs);
        System.out.println(lists);
    }

    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] s = str.toCharArray();
            Arrays.sort(s);
            String sortedStr = new String(s);
            if (!map.containsKey(sortedStr)) {
                map.put(sortedStr, new ArrayList<>());
            }
            map.get(sortedStr).add(str);
        }
        return new ArrayList<>(map.values());
    }


    public static void purchaseProduct() {
        RLock lock = redissonClient.getLock("product_lock");
        try {
            if (lock.tryLock(10, 10, TimeUnit.SECONDS)) {
                try {
                    int stock = (int) redissonClient.getBucket("product_stock").get();
                    System.out.println("current stock is: " + stock);
                    if (stock > 0) {
                        redissonClient.getBucket("product_stock").set(stock - 1);
                        System.out.println("Purchase successful! Remaining stock: " + (stock - 1));
                    } else {
                        System.out.println("Purchase failed! Out of stock.");
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

