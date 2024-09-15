package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.NodeDTO;
import com.hmdp.entity.CljInfo;

import java.time.LocalDateTime;

public interface ICljInfoService extends IService<CljInfo> {
    boolean writeInfo2Redis(NodeDTO nodeDTO);
    NodeDTO getNodeDTOFromRedis(Long id);
    CljInfo findByNameAndNumber(String name, String number);
}
