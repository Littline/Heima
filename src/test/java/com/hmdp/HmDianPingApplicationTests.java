package com.hmdp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

@SpringBootTest
class HmDianPingApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //JSON工具类ObjectMapper,或者可以用fastjson:JSON.toJSONString(), JSON.parseObject()
    private static final ObjectMapper mapper = new ObjectMapper();


    @Test
    void testHash(){
        stringRedisTemplate.opsForHash().put("user:200","name","张三");
        stringRedisTemplate.opsForHash().put("user:200","age","21");

        Object name = stringRedisTemplate.opsForHash().get("user:200", "name");
        System.out.println("属性name: " + name);
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("user:200");//获取全部的hashkey-hashvalue
        System.out.println("所有属性: " + entries);
    }


}
