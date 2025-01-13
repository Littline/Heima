package com.hmdp.controller;

import com.hmdp.dto.NodeDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.CljInfo;
import com.hmdp.entity.HistoryRecord;
import com.hmdp.entity.UserDYG;
import com.hmdp.service.ICljInfoService;
import com.hmdp.service.IUserDYGService;
import com.hmdp.service.impl.HistoryRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/query")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class QueryNodeController {
    @Resource
    private ICljInfoService cljInfoService;
    @Resource
    private IUserDYGService userDYGService;
    @Resource
    private HistoryRecordService historyRecordService;

    @PostMapping("/queryOfflineNode")
    public List<NodeDTO> queryOfflineNode(@RequestBody Map<String, Object> params) {
        String account = (String) params.get("account");
        String token = (String) params.get("token");
        System.out.println(account + "-" + token);
        // Get the list of CljInfo from the database (MySQL)
        List<CljInfo> cljInfoList = cljInfoService.list();
        System.out.println(cljInfoList);

        List<NodeDTO> nodeDTOList = new ArrayList<>();
        if (account != null && token != null) {
            UserDYG userInfo = userDYGService.getUserByUsername(account);
            List userCljRelationship = convertStringToIntArray(userInfo.getCljIds());
            // Iterate through the CljInfo list and check if the corresponding NodeDTO exists in Redis
            for (CljInfo cljInfo : cljInfoList) {
                if (userCljRelationship.contains(cljInfo.getId())) {
                    NodeDTO nodeDTOFromRedis = cljInfoService.getNodeWeekFromRedis(cljInfo.getId());

                    // If the node does not exist in Redis, add it to the result list
                    if (nodeDTOFromRedis != null) {
                        System.out.println("redis--contains" + nodeDTOFromRedis.toString());
                        continue;
                    }
                    NodeDTO nodeDTOFromDb = new NodeDTO();
                    nodeDTOFromDb.setId(cljInfo.getId());
                    nodeDTOFromDb.setName(cljInfo.getName());
                    nodeDTOFromDb.setNumber(cljInfo.getNumber());
                    // Add other necessary properties from CljInfo to NodeDTO if needed

                    nodeDTOList.add(nodeDTOFromDb);
                }

            }
        }

        // Return the list of nodes that exist in MySQL but not in Redis
        return nodeDTOList;
    }

    @PostMapping("/queryHistoryRecord")
    public Result queryHistoryRecord(@RequestBody Map<String, Object> params) {
        String account = (String) params.get("account");
        String token = (String) params.get("token");
        Long nodeId=getId(params);
        String beginTime = (String) params.get("beginTime");
        String endTime = (String) params.get("endTime");
        System.out.println(params.toString());
        // 1.根据用户信息判断是否具有查看id的权限
        if (!hasPermission(account, token, nodeId)) {
            return Result.fail("User does not have permission to view this node.");
        }

        // 2.根据输入的时间进行查询，并返回结果
        List<HistoryRecord> records = queryHistoryRecords(nodeId, beginTime, endTime);

        if (records.isEmpty()) {
            return Result.ok("No records found for the specified time range.");
        }

        return Result.ok(records,Long.valueOf(records.size()));
    }

    @PostMapping("/queryRecordName")
    public Result queryRecordName(@RequestBody Map<String, Object> params) {
        Long nodeId=getId(params);
        CljInfo cljInfo = cljInfoService.getById(nodeId);

        if (cljInfo==null) {
            return Result.fail("No records found for the specified time range.");
        }

        return Result.ok(cljInfo.getName()+cljInfo.getNumber());
    }

    private Long getId(@RequestBody Map<String, Object> params) {
        Object idObject = params.get("id");
        Long nodeId = null;
        if (idObject instanceof String) {
            nodeId = Long.valueOf((String) idObject);
        } else if (idObject instanceof Integer) {
            nodeId = Long.valueOf((Integer) idObject);
        } else {
            throw new IllegalArgumentException("Invalid id type");
        }
        return nodeId;
    }

    private List convertStringToIntArray(String cljIdsString) {
        return getList(cljIdsString);
    }

    // Helper method for permission check (this is a basic example; adjust as per your authentication logic)
    private boolean hasPermission(String account, String token, Long nodeId) {
        // Implement your logic to verify the user permission based on account and token
        // For example, check if the user is authorized to access the `nodeId` resource.
        return true; // Assuming always has permission, replace with actual logic
    }

    // Helper method to query history records based on the time range and nodeId
    private List<HistoryRecord> queryHistoryRecords(Long nodeId, String beginTime, String endTime) {
        // Here, you should call your service layer or repository to perform the query.
        // I'll assume there's a `HistoryRecordService` to handle this logic.
        return historyRecordService.queryByNodeIdAndTimeRange(nodeId, beginTime, endTime);
    }

    public static List getList(String cljIdsString) {
        String[] cljIdsArray1 = cljIdsString.split(",");
        List<Long> cljIds = new ArrayList<>(cljIdsArray1.length);
        for (int i = 0; i < cljIdsArray1.length; i++) {
            cljIds.add(Long.valueOf(cljIdsArray1[i].trim()));  // 使用 trim() 去掉空格
        }
        return cljIds;
    }
}
