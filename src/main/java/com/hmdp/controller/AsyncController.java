package com.hmdp.controller;

import com.hmdp.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/async")
    public String triggerAsyncMethod() throws ExecutionException, InterruptedException {
        // 调用异步方法
        Future<String> result = asyncService.asyncMethod();

        // 这里可以处理其他逻辑，不会被阻塞
        System.out.println("异步非阻塞，controller正在执行");

        // 等待异步操作完成
        String asyncResult = result.get();
        return "Result: " + asyncResult;
    }
}
