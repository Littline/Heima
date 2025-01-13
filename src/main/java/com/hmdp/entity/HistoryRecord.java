package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmdp.config.DoubleListTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
@TableName(value = "history_record", autoResultMap = true)
public class HistoryRecord extends Model<HistoryRecord> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("node_id")
    private Long nodeId;

    @TableField("time")
    private String time;

    @TableField("length")
    private Integer length;

    @TableField(value = "weight2", typeHandler = DoubleListTypeHandler.class)
    private String weight2;

    @TableField(value = "motorspeed2", typeHandler = DoubleListTypeHandler.class)
    private String motorspeed2;

    @TableField("updatetime")
    private LocalDateTime updatetime;

    // 设置 motorspeed2 字段：List<Double> 转换为 JSON 字符串
    public void setMotorspeed2(List<Double> motor_speed2) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.motorspeed2 = objectMapper.writeValueAsString(motor_speed2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert motor_speed2 to JSON", e);
        }
    }

    // 设置 weight2 字段：List<Double> 转换为 JSON 字符串
    public void setWeight2(List<Double> weight2) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.weight2 = objectMapper.writeValueAsString(weight2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert weight2 to JSON", e);
        }
    }

    // 获取 motorspeed2 字段：将 JSON 字符串转换回 List<Double>
    public List<Double> getMotorspeed2() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.motorspeed2, new TypeReference<List<Double>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to List<Double>", e);
        }
    }

    // 获取 weight2 字段：将 JSON 字符串转换回 List<Double>
    public List<Double> getWeight2() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.weight2, new TypeReference<List<Double>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to List<Double>", e);
        }
    }
}

