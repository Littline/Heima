package com.hmdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.entity.HistoryRecord;
import com.hmdp.mapper.HistoryRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryRecordService {

    @Resource
    private HistoryRecordMapper historyRecordMapper;

    public int createHistoryRecord(HistoryRecord record) {
        return historyRecordMapper.insert(record);
    }

    public List<HistoryRecord> queryByNodeIdAndTimeRange(Long nodeId, String beginTime, String endTime) {
        QueryWrapper<HistoryRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("node_id", nodeId)
                .ge("DATE(time)", beginTime)
                .le("DATE(time)", endTime);
        return historyRecordMapper.selectList(queryWrapper);
    }

    public HistoryRecord getHistoryRecordById(Long id) {
        return historyRecordMapper.selectById(id);
    }

    // 其他 CRUD 方法
}
