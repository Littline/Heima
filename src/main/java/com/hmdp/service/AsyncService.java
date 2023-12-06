package com.hmdp.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    @Async
    public CompletableFuture<String> asyncMethod() {
        // 模拟一个异步操作
        try {
            System.out.println("阻塞开始");
            Thread.sleep(2000);
            System.out.println("阻塞结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture("Async operation completed");
    }
}

