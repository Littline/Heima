package com.hmdp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Data
public class HistoryRecordDTO {
    private String number;
    private String name;
    private List<Double> motor_speed2;
    private List<Double> weight2;
    private String time;
    private Integer length;
    /**
     * 将时间字符串解析为 LocalDateTime 对象
     * @return LocalDateTime 对象
     * @throws DateTimeParseException 如果格式不正确
     */
    public LocalDateTime getParsedTime() throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(this.time, formatter);
    }
}
