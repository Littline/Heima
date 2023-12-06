package com.hmdp.controller;

import com.hmdp.service.NioServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NioServerController {

    @Autowired
    private NioServerService nioServerService;

    @GetMapping("/startServer")
    public String startServer() {
        // 启动非阻塞 I/O 服务
        nioServerService.startServer();
        return "Server started";
    }
}

