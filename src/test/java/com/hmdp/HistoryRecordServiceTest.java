package com.hmdp;

import com.hmdp.entity.HistoryRecord;
import com.hmdp.service.IHistoryRecordService;
import com.hmdp.service.impl.HistoryRecordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// 如果使用 H2 数据库，可以导入以下注解以确保 schema.sql 被执行
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test") // 指定使用 application-test.properties 配置
public class HistoryRecordServiceTest {

    @Autowired
    private HistoryRecordService historyRecordService;

    @Test
    public void testCreateHistoryRecord() {
        // 创建一个新的 HistoryRecord 实例
//        HistoryRecord record = new HistoryRecord();
//        record.setNodeId(12333L);
//
//        record.setLength(3);
//        record.setTime(LocalDateTime.now().toString());
//        record.setUpdatetime(LocalDateTime.now());
//
//        // 调用服务类插入数据
//        int rows = historyRecordService.createHistoryRecord(record);
//
//        // 断言插入成功
//        Assertions.assertEquals(1, rows, "插入数据应该影响1行");
//
//        // 进一步验证数据是否正确插入
//        Long insertedId = record.getId();
//        Assertions.assertNotNull(insertedId, "插入后ID应该不为空");
//
//        HistoryRecord fetchedRecord = historyRecordService.getHistoryRecordById(insertedId);
//        Assertions.assertNotNull(fetchedRecord, "通过ID查询的数据应该存在");
//        Assertions.assertEquals(12333L, fetchedRecord.getNodeId(), "nodeId 应该匹配");
//        Assertions.assertEquals(3, fetchedRecord.getLength(), 0.001, "length 应该匹配");
    }
}
