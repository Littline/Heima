package com.hmdp.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/chat")
public class ProxyController {

    private final RestTemplate restTemplate;

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @CrossOrigin(origins = "http://192.168.46.190:3000", methods = {RequestMethod.OPTIONS})
    @GetMapping
    public ResponseEntity<Void> handleOptions() {
        System.out.println("options");
        return ResponseEntity.ok().build();
    }
    @CrossOrigin(origins = "http://192.168.46.190:3000")
    @PostMapping
    public ResponseEntity<String> proxyChatApi(@RequestBody String message) {
        String targetUrl = "http://192.168.46.15:8000"; // Assuming target URL
        System.out.println(message);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(message, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, request, String.class);
        System.out.println(response);
        return response;
    }
}


