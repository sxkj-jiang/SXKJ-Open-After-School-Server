package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.config.GrobProperties;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.DateUtils;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.ao.AsStudentQueryListAO;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.excel.AsLearningAchievementExportExcel;
import com.sxkj.edu.module.afterschool.domain.excel.AsStudentExcel;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.enums.RegisterStatusEnum;
import com.sxkj.edu.module.afterschool.mapper.*;
import com.sxkj.edu.module.afterschool.service.*;
import com.sxkj.edu.module.afterschool.support.*;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.module.system.employee.TbaseUserService;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDataVO;
import com.sxkj.edu.service.BaseFileService;
import com.sxkj.edu.service.SchoolManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AsStudentIdentityServiceImpl extends ServiceImpl<AsStudentIdentityMapper, AsStudentIdentityEntity>
        implements AsStudentIdentityService {



    private AsStudentIdentityEntity checkStudentIdentityByStuNoName(Long schoolId, String studentNo, String studentName) {
        LambdaQueryWrapper<AsStudentIdentityEntity> w = new LambdaQueryWrapper<>();
            w.eq(AsStudentIdentityEntity::getIsValid, 1)
            .eq(AsStudentIdentityEntity::getSchoolId, schoolId)
                    .eq(AsStudentIdentityEntity::getStudentNo, studentNo);
            List<AsStudentIdentityEntity> list = this.list(w);
            return list != null && list.size() > 0 ? list.get(0) : null;
    }



    @Override
    public Long createStudentIdentityStudent(AsStudentEntity asStudentEntity,Integer enrollmentYear) {
        AsStudentIdentityEntity studentIdentity = new AsStudentIdentityEntity();
        BeanUtils.copyProperties(asStudentEntity, studentIdentity);
        AsStudentIdentityEntity existStudent = checkStudentIdentityByStuNoName(studentIdentity.getSchoolId(), studentIdentity.getStudentNo(),
                studentIdentity.getStudentName());
        if(existStudent != null){
            log.warn("call createStudentIdentityStudent in exist,studentNo:{},studentName:{}",studentIdentity.getStudentNo(),studentIdentity.getStudentName());
            return existStudent.getId();
        }
        studentIdentity.setEnrollmentYear(enrollmentYear);
        Boolean uptSave = this.save(studentIdentity);
        log.info("call createStudentIdentityStudent in exec result:{}",uptSave);
        return studentIdentity.getId();
    }

    @Override
    public Boolean updateStudentIdentityStudent(AsStudentEntity asStudentEntity, Integer enrollmentYear) {
        AsStudentIdentityEntity studentIdentity = new AsStudentIdentityEntity();
        BeanUtils.copyProperties(asStudentEntity, studentIdentity);
        studentIdentity.setId(null);

        Boolean uptRow = false;
        AsStudentIdentityEntity origin = checkStudentIdentityByStuNoNameForPo(asStudentEntity.getSchoolId(), studentIdentity.getStudentNo(), studentIdentity.getStudentName());
        if(null != origin){
            Long originId = origin.getId();
            BeanUtils.copyProperties(studentIdentity, origin);
            origin.setId(originId);
            origin.setUpdateTime(new Date());
            origin.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            uptRow = this.updateById(origin);
        }else{
            studentIdentity.setEnrollmentYear(enrollmentYear);
            uptRow = this.save(studentIdentity);
        }
        log.info("call createStudentIdentityStudent in exec result:{}",uptRow);
        return uptRow;
    }

    /**
     * 按学校+学号查询唯一身份记录（与唯一索引 uk_as_student_identity_school_no 对齐）。
     */
    private AsStudentIdentityEntity checkStudentIdentityByStuNoNameForPo(Long schoolId, String studentNo, String studentName) {
        LambdaQueryWrapper<AsStudentIdentityEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsStudentIdentityEntity::getIsValid, 1)
                .eq(AsStudentIdentityEntity::getSchoolId, schoolId)
                .eq(AsStudentIdentityEntity::getStudentNo, studentNo);
        List<AsStudentIdentityEntity> list = this.list(w);
        if(CollectionUtil.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }
}
