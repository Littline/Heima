package com.hmdp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.entity.User;
import com.sangupta.murmur.Murmur3;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.UUID;

@SpringBootTest
@Slf4j
class HmDianPingApplicationTests {
    class Inner {
        public void display() {
            System.out.println("This is the Inner class.");
        }
    }


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    //JSON工具类ObjectMapper,或者可以用fastjson:JSON.toJSONString(), JSON.parseObject()
    private static final ObjectMapper mapper = new ObjectMapper();
    @Test
    public void IntegerTest() {
        new HmDianPingApplicationTests.Inner();
        String s=new String();
        Integer a = 42;
        Integer b = 42;

        // 打印对象的哈希码（不是内存地址）
        System.out.println(System.identityHashCode(a));
        System.out.println(System.identityHashCode(b));

        Integer c;  // 创建一个新的对象
        c = Integer.valueOf(42);
        Integer d= new Integer(42);

        System.out.println(System.identityHashCode(c));
        System.out.println(System.identityHashCode(d));
        byte bbb=(byte)129;
        System.out.println(bbb);
        System.out.println(Recurse(8,2012));
    }
    int Recurse(int a,int b){
        if(a>=b){
            if(a==b){
                return a;
            }else{
                return 0;
            }
        }else{
            return Recurse(a+1,b-1)+a+b;
        }
    }


    @Test
    void testHash(){
        stringRedisTemplate.opsForHash().put("user:200","name","张三");
        stringRedisTemplate.opsForHash().put("user:200","age","21");

        Object name = stringRedisTemplate.opsForHash().get("user:200", "name");
        System.out.println("属性name: " + name);
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("user:200");//获取全部的hashkey-hashvalue
        System.out.println("所有属性: " + entries);
    }
    @Test
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void fun() {


        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        long start = System.currentTimeMillis();

        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();

        for (int i = 0; i < 10; i++) {//100000
            String uuid = UUID.randomUUID().toString();

            redisTemplate.opsForSet().add("set10w_uuid", uuid);
            redisTemplate.opsForSet().add("set10w_incr", String.valueOf(i));
            valueOps.setBit("bitMap10w_hash", Murmur3.hash_x86_32(uuid.getBytes(),  uuid.length(), 0),true);
            valueOps.setBit("bitMap10w_hash_size", Math.abs(Murmur3.hash_x64_128(uuid.getBytes(),  uuid.length(), 0)[0] % 100000),true);
            valueOps.setBit("bitMap10w_incr", i,true);

            System.out.println("progress " + i);
        }

        System.out.println("执行耗时: " + (System.currentTimeMillis() - start));
    }
    @Test
    public void test() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        long start = System.currentTimeMillis();

        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();

        for (int i = 0; i < 2; i++) {
            String uuid = UUID.randomUUID().toString();
            log.info("Generated UUID: {}", uuid);
            redisTemplate.opsForSet().add("set10w_uuid", uuid);
            redisTemplate.opsForSet().add("set10w_incr", String.valueOf(i));
            valueOps.setBit("bitMap10w_hash", Murmur3.hash_x86_32(uuid.getBytes(), uuid.length(), 0), true);
            valueOps.setBit("bitMap10w_hash_size", Math.abs(Murmur3.hash_x64_128(uuid.getBytes(), uuid.length(), 0)[0] % 100000), true);
            valueOps.setBit("bitMap10w_incr", i, true);
            Long count = (Long) redisTemplate.execute((RedisCallback<Long>) connection ->
                    connection.bitCount("bitMap10w_incr".getBytes())
            );
            log.info("bitMap10w_incr is {}",count);
            log.info("value of bitMap10w_hash is {}",Murmur3.hash_x86_32(uuid.getBytes(), uuid.length(), 0));
            log.info("value of bitMap10w_hash_size is {}",Math.abs(Murmur3.hash_x64_128(uuid.getBytes(), uuid.length(), 0)[0] % 100000));
            log.info("value of bitMap10w_incr is {}",i);
            log.info("Progress: {}", i);
        }

        log.info("Execution time: {} ms", (System.currentTimeMillis() - start));
    }


}
    // 非静态的内部类
