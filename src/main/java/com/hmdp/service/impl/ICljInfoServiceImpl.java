package com.hmdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.NodeDTO;
import com.hmdp.entity.CljInfo;
import com.hmdp.mapper.CljInfoMapper;
import com.hmdp.service.ICljInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class ICljInfoServiceImpl extends ServiceImpl<CljInfoMapper, CljInfo> implements ICljInfoService {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean writeInfo2Redis(NodeDTO nodeDTO) {
        try {
            String key =  "clj_node_"+nodeDTO.getId();
            List<String> existingKeys = redisTemplate.opsForList().range("NodeDTOKeys", 0, -1);
            if (existingKeys != null && existingKeys.contains(key)) {
                // update node directly
                redisTemplate.opsForValue().set(key, nodeDTO, Duration.ofMinutes(60));
                return true;
            }
            redisTemplate.opsForList().rightPush("NodeDTOKeys", key);
            redisTemplate.opsForValue().set(key, nodeDTO, Duration.ofMinutes(60));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean writeWeekInfo2Redis(NodeDTO nodeDTO){
        try {
            String key =  "clj_node_week_"+nodeDTO.getId();
            List<String> existingKeys = redisTemplate.opsForList().range("NodeDTOKeys_Week", 0, -1);
            if (existingKeys != null && existingKeys.contains(key)) {
                redisTemplate.opsForValue().set(key, nodeDTO, Duration.ofMinutes(60));
                return true;
            }
            redisTemplate.opsForList().rightPush("NodeDTOKeys_Week", key);
            redisTemplate.opsForValue().set(key, nodeDTO, Duration.ofMinutes(60));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public NodeDTO getNodeDTOFromRedis(Long id) {
        String key =  "clj_node_"+id;
        NodeDTO nodeDTO = (NodeDTO) redisTemplate.opsForValue().get(key);
        List nodeDTOKeys = redisTemplate.opsForList().range("NodeDTOKeys", 0, -1);
        if(id==1L){
            System.out.println("test is: "+nodeDTOKeys);
        }
        return nodeDTO;
    }

    @Override
    public NodeDTO getNodeWeekFromRedis(Long id){
        String key =  "clj_node_week_"+id;
        NodeDTO nodeDTO = (NodeDTO) redisTemplate.opsForValue().get(key);
        List nodeDTOKeys = redisTemplate.opsForList().range("NodeDTOKeys_Week", 0, -1);
        if(id==1L){
            System.out.println("week test is: "+nodeDTOKeys);
        }
        return nodeDTO;
    }
    @Override
    public CljInfo findByNameAndNumber(String name, String number) {
        if (name == null || number == null) {
            throw new IllegalArgumentException("Name and number must not be null");
        }
        QueryWrapper<CljInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("number", number)
                .eq("name", name);
        return this.getOne(queryWrapper);
    }
}
