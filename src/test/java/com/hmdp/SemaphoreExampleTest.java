package com.hmdp;

import org.junit.Test;

import java.util.concurrent.Semaphore;

public class SemaphoreExampleTest {

    @Test
    public void testSemaphoreExample() {
        // 创建一个计数信号量，初始许可证数量为3
        Semaphore semaphore = new Semaphore(3);

        // 创建多个线程并启动
        for (int i = 1; i <= 5; i++) {
            Thread thread = new Thread(new WorkerThread(semaphore, i));
            thread.start();
        }

        // 等待所有线程执行完成
        try {
            Thread.sleep(10000); // 等待足够的时间，确保所有线程都有足够的时间执行完
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class WorkerThread implements Runnable {
        private final Semaphore semaphore;
        private final int workerId;

        public WorkerThread(Semaphore semaphore, int workerId) {
            this.semaphore = semaphore;
            this.workerId = workerId;
        }

        @Override
        public void run() {
            try {
                System.out.println("Worker " + workerId + " is waiting for a permit.");
                // 获得许可证，如果没有可用的许可证，线程将阻塞直到有许可证可用
                semaphore.acquire();
                System.out.println("Worker " + workerId + " has acquired a permit.");
                // 模拟工作
                Thread.sleep(2000);
                System.out.println("Worker " + workerId + " is releasing a permit.");
                // 释放许可证，使其可供其他等待线程使用
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
