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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/r21")
@CrossOrigin(origins = "http://192.168.46.188:3000", allowCredentials = "true", allowedHeaders = {"content-type"})
//@CrossOrigin(origins = "*")//主要作用
public class R21Controller {
    @Resource
    private IOPCUnitService opcUnitService;

//    @CrossOrigin(origins = "http://192.168.46.190:3000", methods = {RequestMethod.OPTIONS})
    @CrossOrigin(origins = "http://192.168.46.188:81", methods = {RequestMethod.OPTIONS})
    @GetMapping
    //跨域请求//基本没什么用了
    public ResponseEntity<Void> handleOptions() {
        System.out.println("options");
        return ResponseEntity.ok().build();
    }
    @GetMapping("/all")
//    @CrossOrigin(origins = "http://192.168.46.190:3000")
    @CrossOrigin(origins = "http://192.168.46.188:81")
    public Result queryOPCUnitAll() {
        List<OPCUnit> allOpcUnits = opcUnitService.list();
        return Result.ok(allOpcUnits);
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
