package com.hmdp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRefused {
    public static void main(String[] args) {
        int numTasks = 100;
        int numThreads = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numTasks; i++) {
            int finalI = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Simulate task with sleep
                        long sleepTime = 3000 + (int) (Math.random() * 2000);
                        Thread.sleep(sleepTime);
                        System.out.println(finalI + " Task completed by " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }


}
