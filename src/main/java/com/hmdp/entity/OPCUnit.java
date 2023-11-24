package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("r21_opcunit")
public class OPCUnit {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("test_point_id")
    private Long testPointId;

    @TableField("part_id")
    private Long partId;


    @TableField("test_point_name")
    private String testPointName;

    private Long status;

    private LocalDateTime updateTime;

    private String chName;

    private Double up;
    private Double low;
    private Integer flag;
    private String unit;
    private String address;
    private String alarmRule;

}
