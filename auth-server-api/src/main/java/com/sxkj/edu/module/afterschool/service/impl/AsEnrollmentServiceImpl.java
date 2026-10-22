package com.sxkj.edu.module.afterschool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentEntity;
import com.sxkj.edu.module.afterschool.mapper.AsEnrollmentMapper;
import com.sxkj.edu.module.afterschool.service.AsEnrollmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 13581
 * @version v1.0
 * @interfaceName AsEnrollmentService
 * @description 排课选课服务
 * @date 2026-9-17 18:44
 * @company SXKJ
 **/
@Service
@Slf4j
public class AsEnrollmentServiceImpl extends ServiceImpl<AsEnrollmentMapper, AsEnrollmentEntity> implements AsEnrollmentService {

}
