package com.hmdp;
import java.util.concurrent.*;
public class TaskExecutorRefused {
    public static void main(String[] args) {
        int numTasks = 100;
        int numThreads = 10;
        int queueSize = 10;

        ExecutorService executorService = new ThreadPoolExecutor(
                numThreads,
                numThreads,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize),
                new ThreadPoolExecutor.AbortPolicy()
        );

        for (int i = 0; i < numTasks; i++) {
            int finalI = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        long sleepTime = 3000 + (int) (Math.random() * 2000);
                        for (long t = 0; t < sleepTime / 100; t++) {
                            Thread.sleep(100);
                            if (Thread.currentThread().isInterrupted()) {
                                System.out.println(finalI + " Task was interrupted.");
                                return;
                            }
                        }
                        System.out.println(finalI + " Task completed by " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        System.out.println(finalI + " Task was interrupted.");
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("Tasks are still running, attempting to shut down now...");
                executorService.shutdownNow();
                if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
