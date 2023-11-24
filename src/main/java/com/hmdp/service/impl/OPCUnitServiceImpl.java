package com.hmdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.OPCUnit;
import com.hmdp.mapper.OPCUnitMapper;
import com.hmdp.service.IOPCUnitService;
import org.springframework.stereotype.Service;

@Service
public class OPCUnitServiceImpl extends ServiceImpl<OPCUnitMapper, OPCUnit> implements IOPCUnitService {
}
