package com.hmdp.controller;

import com.hmdp.dto.NodeDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.CljInfo;
import com.hmdp.entity.HistoryRecord;
import com.hmdp.entity.UserDYG;
import com.hmdp.service.ICljInfoService;
import com.hmdp.service.IUserDYGService;
import com.hmdp.service.impl.HistoryRecordService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.hmdp.controller.QueryNodeController.getList;

@RestController
@RequestMapping("/send")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class NodeController {

    @Resource
    private ICljInfoService cljInfoService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IUserDYGService userDYGService;

    @Resource
    private HistoryRecordService historyRecordService;

    @CrossOrigin(origins = "*", methods = {RequestMethod.OPTIONS})
    @GetMapping
    public ResponseEntity<Void> handleOptions() {
        System.out.println("options");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateNodeInfo")
    public String updateNodeInfo(@RequestBody Map<String, Object> params) {
        // 输出传入的参数
        System.out.println("received params is: " + params.toString());
        String token = (String) params.get("token");
        if (!"clj168168".equals(token)) {
            return "Invalid token.";
        }
        NodeDTO nodeDTO = createNode(params);
        nodeDTO.setLastTrue((Integer) params.get("lastTrue"));
        nodeDTO.setLastFalse((Integer) params.get("lastFalse"));
        nodeDTO.setLastWarn((Integer) params.get("lastWarn"));
        nodeDTO.setLastBox((Integer) params.get("lastBox"));
        nodeDTO.setTodayTrue((Integer) params.get("todayTrue"));
        nodeDTO.setTodayFalse((Integer) params.get("todayFalse"));
        nodeDTO.setTodayWarn((Integer) params.get("todayWarn"));
        nodeDTO.setTodayBox((Integer) params.get("todayBox"));

        String updateTimeStr = (String) params.get("updateTime");
        Instant instant = Instant.parse(updateTimeStr);
        LocalDateTime updateTime = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(8));
        nodeDTO.setUpdateTime(updateTime);

        boolean b = cljInfoService.writeInfo2Redis(nodeDTO);
        System.out.println("write redis successful? :" + b);
        cljInfoService.getNodeDTOFromRedis(1L);
        return nodeDTO.toString();
    }

    @PostMapping("/queryNodeInfo")
    public List<NodeDTO> queryNodeInfo(@RequestBody Map<String, Object> params) {
        List<CljInfo> cljInfoList = cljInfoService.list();
        if (cljInfoList.isEmpty()) {
            return new ArrayList<>();
        }
        List<NodeDTO> nodeDTOList = new ArrayList<>();
        for (CljInfo cljInfo : cljInfoList) {
            NodeDTO nodeDTOFromRedis = cljInfoService.getNodeDTOFromRedis(cljInfo.getId());
            if (nodeDTOFromRedis != null) {
                nodeDTOList.add(nodeDTOFromRedis);
            }
        }
        System.out.println(nodeDTOList);
        return nodeDTOList;
    }

    @PostMapping("/queryNodeInfoWeek")
    public List<NodeDTO> queryNodeInfoWeek(@RequestBody Map<String, Object> params) {
        System.out.println(params.toString());
        List<CljInfo> cljInfoList = cljInfoService.list();
        if (cljInfoList.isEmpty()) {
            return new ArrayList<>();
        }
        List<NodeDTO> nodeDTOList = new ArrayList<>();
        for (CljInfo cljInfo : cljInfoList) {
            NodeDTO nodeDTOFromRedis = cljInfoService.getNodeWeekFromRedis(cljInfo.getId());
            if (nodeDTOFromRedis != null) {
                nodeDTOList.add(nodeDTOFromRedis);
            }
        }
        System.out.println(nodeDTOList);
        return nodeDTOList;
    }

    @PostMapping("/queryNodeInfoWeekToken")
    public Result queryNodeInfoWeekToken(@RequestBody Map<String, Object> params) {
        System.out.println(params.toString());
        String account = (String) params.get("account");
        String token = (String) params.get("token");
        System.out.println(account);
        if (token != null && account != null) {
            HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
            Map<String, String> map = hashOperations.entries("token");
            stringRedisTemplate.expire("token", 30, TimeUnit.MINUTES);
            if (!map.containsKey(token)) {
                return Result.fail("未携带有效token");
            }
            UserDYG userInfo = userDYGService.getUserByUsername(account);
            List userCljRelationship = convertStringToIntArray(userInfo.getCljIds());
            System.out.println("Request from " + account + " " + userCljRelationship);
            List<CljInfo> cljInfoList = cljInfoService.list();
            if (cljInfoList.isEmpty()) {
                return Result.ok();
            }
            List<NodeDTO> nodeDTOList = new ArrayList<>();
            for (CljInfo cljInfo : cljInfoList) {
                if (userCljRelationship.contains(cljInfo.getId())) {
                    NodeDTO nodeDTOFromRedis = cljInfoService.getNodeWeekFromRedis(cljInfo.getId());
                    if (nodeDTOFromRedis != null) {
                        nodeDTOList.add(nodeDTOFromRedis);
                        System.out.println("add node: " + nodeDTOFromRedis);
                    }
                }
            }
            return Result.ok(nodeDTOList);
        } else {
            return Result.fail("未验证请求");
        }
    }

    @PostMapping("/update7NodeInfo")
    public String update7NodeInfo(@RequestBody Map<String, Object> params) {
        // 输出传入的参数
        System.out.println("received params is: " + params.toString());
        String token = (String) params.get("token");
        if (!"clj168168".equals(token)) {
            return "Invalid token.";
        }
        String type = (String) params.get("type");
        if (!"lastWeek".equals(type)) {
            return "Invalid type";
        }

        NodeDTO nodeDTO = createNode(params);
        nodeDTO.setLast7True((List<Integer>) params.get("last7True"));
        nodeDTO.setLast7False((List<Integer>) params.get("last7False"));
        nodeDTO.setLast7Warn((List<Integer>) params.get("last7Warn"));
        nodeDTO.setLast7Box((List<Integer>) params.get("last7Box"));
        String updateTimeStr = (String) params.get("updateTime");
        Instant instant = Instant.parse(updateTimeStr);
        LocalDateTime updateTime = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(8));
        nodeDTO.setUpdateTime(updateTime);

        boolean b = cljInfoService.writeWeekInfo2Redis(nodeDTO);
        System.out.println("write redis successful? :" + b);
        cljInfoService.getNodeWeekFromRedis(1L);
        return nodeDTO.toString();
    }

    private NodeDTO createNode(@RequestBody Map<String, Object> params) {
        CljInfo cljInfo = cljInfoService.findByNameAndNumber((String) params.get("name"), (String) params.get("number"));
        NodeDTO nodeDTO = new NodeDTO();
        nodeDTO.setId(cljInfo.getId());
        nodeDTO.setName(cljInfo.getName());
        nodeDTO.setNumber(cljInfo.getNumber());
        nodeDTO.setDefaultWeight(cljInfo.getWeightThreshold());
        nodeDTO.setWeight((Double) params.get("weight"));
        return nodeDTO;
    }

    private List convertStringToIntArray(String cljIdsString) {
        // 按逗号分隔字符串
        return getList(cljIdsString);
    }

    @PostMapping("/insertHistoryRecord")
    public Result insertHistoryRecord(@RequestBody Map<String, Object> params) {
        HistoryRecord historyRecord = new HistoryRecord();
        CljInfo cljNode = cljInfoService.findByNameAndNumber((String)params.get("name"), (String)params.get("number"));
        if(cljNode==null|| cljNode.getId()==null){
            return Result.fail("节点代号错误，请检查节点信息");
        }
        historyRecord.setNodeId(cljNode.getId());
        historyRecord.setMotorspeed2((List<Double>) params.get("motor_speed2"));
        historyRecord.setWeight2((List<Double>) params.get("weight2"));
        historyRecord.setTime((String) params.get("time"));
        historyRecord.setLength((Integer) params.get("length"));
        if (historyRecord != null) {
            historyRecord.setUpdatetime(LocalDateTime.now());
            int result = historyRecordService.createHistoryRecord(historyRecord);
            return Result.ok(result);
        } else {
            System.out.println("error happens at:"+cljNode.getId()+params.get("time")+params.get("length"));
            return Result.fail("未验证请求");
        }
    }


}

