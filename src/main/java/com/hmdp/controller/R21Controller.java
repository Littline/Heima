package com.hmdp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.OPCUnit;
import com.hmdp.service.IBlogService;
import com.hmdp.service.IOPCUnitService;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.LOGIN_USER_KEY;

@RestController
@RequestMapping("/r21")
@CrossOrigin(origins = "http://192.168.46.188:3000", allowCredentials = "true", allowedHeaders = {"content-type"})
public class R21Controller {
    @Resource
    private IOPCUnitService opcUnitService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @CrossOrigin(origins = "http://192.168.46.188:81", methods = {RequestMethod.OPTIONS})
    @GetMapping
    public ResponseEntity<Void> handleOptions() {
        System.out.println("options");
        return ResponseEntity.ok().build();
    }
    @GetMapping("/all")
    public Result queryOPCUnitAll(@RequestHeader(name = "Authorization", required = false) String authorization, HttpSession session) {
        System.out.println("Authorization: " + authorization);
        if(authorization!=null){
            HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
            Map<String,String> map= hashOperations.entries("token");
            stringRedisTemplate.expire("token", 30, TimeUnit.MINUTES);
            if(!map.containsKey(authorization)){
                return Result.fail("未验证请求");
            }
            List<OPCUnit> allOpcUnits = opcUnitService.list();
            return Result.ok(allOpcUnits);
        }else{
            return Result.fail("未验证请求");
        }


    }
    @GetMapping("/part/{id}")
    public Result queryOPCUnitByPart(@PathVariable("id") Long id,@RequestParam(value = "current", defaultValue = "1") Integer current) {
        Page<OPCUnit> page = opcUnitService.query()
                .eq("part_id", id)
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<OPCUnit> records = page.getRecords();
        return Result.ok(records);
    }
    @GetMapping("/unit/{id}")
    public Result queryOPCUnitById(@PathVariable("id") Long id,@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return Result.ok(opcUnitService.getById(id));
    }
    @PutMapping("/{id}")
    public Result updateUnitById(@PathVariable("id") Long id, @RequestBody OPCUnit unit) {
        boolean updated = opcUnitService.update(unit, new QueryWrapper<OPCUnit>().eq("id", id));
        System.out.println(unit.toString());
        if (updated) {
            return Result.ok("Unit updated successfully");
        } else {
            return Result.fail("Failed to update unit");
        }
    }
}
