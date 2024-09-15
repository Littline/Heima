package com.hmdp.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NodeDTO implements Serializable {
    private static final long serialVersionUID = 2L;
    private Long id;
    private String name;
    private String number;
    private Integer lastTrue;
    private Integer lastFalse;
    private Integer lastWarn;
    private Integer lastBox;//集装箱个数
    private Integer todayTrue;
    private Integer todayFalse;
    private Integer todayWarn;
    private Integer todayBox;//集装箱个数
    private Double weight;//实时重量
    private Double defaultWeight;//重量阈值
    private LocalDateTime updateTime;
    private List<Integer> last7True;
    private List<Integer> last7False;
    private List<Integer> last7Warn;
    private List<Integer> last7Box;

}
