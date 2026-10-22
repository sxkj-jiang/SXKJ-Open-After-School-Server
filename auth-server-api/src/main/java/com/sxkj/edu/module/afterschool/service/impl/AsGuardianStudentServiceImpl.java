package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.enums.RoleEnum;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.config.GrobProperties;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.common.utils.*;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.domain.vo.khfw.QueryStudentGuardVO;
import com.sxkj.edu.module.afterschool.domain.ao.AsParentPersonEditAO;
import com.sxkj.edu.module.afterschool.domain.dto.AddParentRegisterAO;
import com.sxkj.edu.module.afterschool.domain.dto.ParentRegisterAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsGuardianStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.domain.excel.AsStudentExcel;
import com.sxkj.edu.module.afterschool.domain.vo.AsParentPersonDetailVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsGuardianStudentOptionVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentVO;
import com.sxkj.edu.module.afterschool.mapper.AsGuardianStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.service.*;
import com.sxkj.edu.module.afterschool.support.StudentIdentitySupport;
import com.sxkj.edu.module.afterschool.support.ServiceEnrollmentNoticeSupport;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import com.sxkj.edu.module.afterschool.support.StudentTermScope;
import com.sxkj.edu.module.system.employee.TbaseUserService;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDataVO;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDetailVO;
import com.sxkj.edu.module.system.login.domain.LoginStuParentTaskVO;
import com.sxkj.edu.module.system.role.roleemployee.RoleUserService;
import com.sxkj.edu.service.BaseFileService;
import com.sxkj.edu.service.CacheServiceImpl;
import com.sxkj.edu.service.SchoolManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AsGuardianStudentServiceImpl extends ServiceImpl<AsGuardianStudentMapper, AsGuardianStudentEntity>
        implements AsGuardianStudentService {

    @Resource
    private AsStudentService asStudentService;
    @Resource
    private TbaseUserService tbaseUserService;

    @Value("${base.defaultPassword}")
    private String defaultPassword;

    @Resource
    private RoleUserService roleUserService;


    @Resource
    private BaseFileService baseFileService;

    @Resource
    private EduclClassService educlClassService;

    @Resource
    private EduclGradeService educlGradeService;
    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;
    @Resource
    private SchoolManageService schoolManageService;
    @Resource
    private ServiceEnrollmentNoticeSupport serviceEnrollmentNoticeSupport;
    @Resource
    private AsServiceMapper asServiceMapper;

    @Resource
    private CacheServiceImpl cacheService;


    @Override
    public Boolean createStudentGuardImport(Long schoolId, Long student, String studentName, AsStudentExcel excel, String phone) {
        Long studentIdentityId = requireStudentIdentityId(student, schoolId);
        AsGuardianStudentEntity existing = findGuardianRelation(
                schoolId, studentIdentityId, excel.getParentPhone());
        if (existing != null && Integer.valueOf(1).equals(existing.getBindStatus())) {
            log.warn("call createStudentGuardImport in exist");
            return true;
        }
        AsGuardianStudentEntity asGuardianStudentEntity = new AsGuardianStudentEntity();
        // 为空时设置系统默认家长姓名和关系
        if(StringUtils.isBlank(excel.getParentName())){
            excel.setParentName(studentName+"家长");
        }
        if(StringUtils.isBlank(excel.getRelationName())){
            excel.setRelationName("家长");
        }

        if (existing != null) {
            existing.setRelationName(excel.getRelationName());
            existing.setParentName(excel.getParentName());
            existing.setPhone(phone);
            existing.setBindStatus(1);
            existing.setIsValid(1);
            existing.setPhoneInfo(excel.getParentPhone());
            existing.setUpdateTime(new Date());
            existing.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            Boolean updateResult = this.updateById(existing);
            log.info("call createStudentGuardImport reactivate result:{}", updateResult);
            return updateResult;
        }
        asGuardianStudentEntity.setSchoolId(schoolId);
        asGuardianStudentEntity.setPhoneInfo(excel.getParentPhone());
        asGuardianStudentEntity.setStudentIdentityId(studentIdentityId);
        asGuardianStudentEntity.setRelationName(excel.getRelationName());
        asGuardianStudentEntity.setParentName(excel.getParentName());
        asGuardianStudentEntity.setPhone(phone);
        asGuardianStudentEntity.setIsValid(1);
        asGuardianStudentEntity.setBindStatus(1);
        asGuardianStudentEntity.setCreateTime(new Date());
        asGuardianStudentEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        Boolean uptRow = this.save(asGuardianStudentEntity);
        log.info("call createStudentGuardImport in exec result:{}",uptRow);
        return uptRow;
    }


    private Boolean checkExistEntenry(Long studentId,String parentName,Long schoolId) {
        Boolean exist = false;
        Long studentIdentityId = requireStudentIdentityId(studentId, schoolId);
        LambdaQueryWrapper<AsGuardianStudentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AsGuardianStudentEntity::getSchoolId, schoolId);
        queryWrapper.eq(AsGuardianStudentEntity::getStudentIdentityId, studentIdentityId);
        queryWrapper.eq(AsGuardianStudentEntity::getParentName, parentName);
        queryWrapper.eq(AsGuardianStudentEntity::getIsValid, 1);
        queryWrapper.eq(AsGuardianStudentEntity::getBindStatus, 1);
        List<AsGuardianStudentEntity> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            exist = true;
        }
        return exist;
    }

    /**
     * 查询稳定学生身份下同一监护人的最近一条关系记录。
     */
    private AsGuardianStudentEntity findGuardianRelation(Long schoolId,
                                                         Long studentIdentityId,
                                                         String phone) {
        LambdaQueryWrapper<AsGuardianStudentEntity> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(AsGuardianStudentEntity::getSchoolId, schoolId)
                .eq(AsGuardianStudentEntity::getStudentIdentityId, studentIdentityId)
                .eq(AsGuardianStudentEntity::getPhone, phone)
                .eq(AsGuardianStudentEntity::getIsValid, 1)
                .orderByDesc(AsGuardianStudentEntity::getId)
                .last("LIMIT 1");
        return this.getOne(queryWrapper, false);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult<String> registerParent(ParentRegisterAO ao) {
        // 校验密码
        if (!ao.getPassword().equals(ao.getConfirmPassword())) {
            return JsonResult.buildError("两次输入的密码不一致");
        }
        String studentNo = ao.getStudentNo();
        studentNo = studentNo.replace(" ","").toUpperCase(Locale.ROOT);
        ao.setStudentNo(studentNo);
        Long registerTermId = resolveRegisterTermId(ao);
        // 验证/保存学校
        if(!CheckParamUtil.checkLongIdParam(ao.getSchoolId()) && StringUtils.isNotBlank(ao.getSchoolName())){
            Long schoolId = schoolManageService.registSchoolManageByName(ao.getSchoolName());
            if(schoolId == null){
                return JsonResult.buildError("学校不存在");
            }
            ao.setSchoolId(schoolId);
        }
        // 查询业务学期学生是否处于已注册或审核中
        AsStudentEntity asStudentEntity =
                asStudentService.registerParentStudentCheckExist(
                        ao.getSchoolId(), ao.getStudentName(),
                        ao.getStudentNo(), registerTermId);
        if(asStudentEntity != null && (asStudentEntity.getRegisterStatus() == 1 || asStudentEntity.getRegisterStatus() == 3)){
            return JsonResult.buildError("学生处于已注册或审核中，请勿重复操作");
        }


        // 1. 查询学生是否存在，存在更新学生以及学生身份信息
        AsStudentEntity student =
                asStudentService.saveOrUpdateStudent(ao, registerTermId);
        boolean studentExists = false;
        if(null != student && student.getRegisterStatus() == 1){
            studentExists = true;
        }

        // 2. 保存或更新家长-学生监护关系
        Long studentIdentityId = requireStudentIdentityId(student.getId(), ao.getSchoolId());
        if(!CheckParamUtil.checkLongIdParam(studentIdentityId)){
            throw businessError("学生身份信息不存在");
        }
        LambdaQueryWrapper<AsGuardianStudentEntity> guardianWrapper = new LambdaQueryWrapper<>();
        guardianWrapper.eq(AsGuardianStudentEntity::getSchoolId, ao.getSchoolId())
                .eq(AsGuardianStudentEntity::getStudentIdentityId, studentIdentityId)
                .eq(AsGuardianStudentEntity::getPhone, ao.getPhone())
                .eq(AsGuardianStudentEntity::getIsValid, 1);
        AsGuardianStudentEntity guardian = this.getOne(guardianWrapper);
        Boolean guardExist = false;
        if (guardian != null) {
            log.info("call registerParent update guardian");
            // 更新监护关系
            guardian.setRelationName(ao.getRelationName());
            guardian.setBindStatus(1);
            guardExist = true;
        } else {
            log.info("call registerParent insert guardian");
            // 新增监护关系
            guardian = new AsGuardianStudentEntity();
            guardian.setSchoolId(ao.getSchoolId());
            guardian.setStudentIdentityId(studentIdentityId);
            guardian.setRelationName(ao.getRelationName());
            guardian.setParentName(ao.getParentName());
            guardian.setPhone(ao.getPhone());
            guardian.setBindStatus(1);
            guardian.setIsValid(1);
        }

        // 3.保存用户信息
        Long userId = tbaseUserService.createBaseUserByParent(ao, studentExists,0);
        log.info("call registerParent in userId:{}", userId);
        if (!CheckParamUtil.checkLongIdParam(userId)) {
            throw businessError("家长账号创建失败");
        }
        guardian.setGuardianUserId(userId);
        Boolean guardianSaved;
        if(guardExist && CheckParamUtil.checkLongIdParam(guardian.getId())){
            guardianSaved = this.updateById(guardian);
        }else{
            guardianSaved = this.save(guardian);
        }
        log.info("call registerParent：schoolId={}, teacherName={}, phone={}, teacherExists={}, result={}",
                ao.getSchoolId(), ao.getParentName(), ao.getPhone(), studentExists, guardianSaved);
        if (!Boolean.TRUE.equals(guardianSaved)) {
            throw businessError("家长学生关系保存失败");
        }
        serviceEnrollmentNoticeSupport.notifyOpenServicesForIdentity(
                ao.getSchoolId(), studentIdentityId);
        return JsonResult.buildSuccess("家长注册成功");
    }

    /**
     * 解析家长注册所属学期，普通注册走日期当前学期，服务入口走服务业务学期。
     */
    private Long resolveRegisterTermId(ParentRegisterAO ao) {
        if (ao.getServiceId() == null) {
            return studentTermScopeSupport.requireDefaultTerm(
                    ao.getSchoolId()).getId();
        }
        AsServiceEntity service = asServiceMapper.selectById(ao.getServiceId());
        Date now = new Date();
        if (service == null
                || !Integer.valueOf(1).equals(service.getIsValid())
                || !Objects.equals(service.getSchoolId(), ao.getSchoolId())
                || !Objects.equals(service.getServiceStatus(),
                CoreConstant.SERVICE_STATUS_ENROLLING)
                || (service.getEnrollEndTime() != null
                && now.after(service.getEnrollEndTime()))) {
            throw new BaseBusinessException(
                    ResultCode.PARA_ERROR.code(),
                    "来源课后服务不存在或已不允许注册");
        }
        StudentTermScope scope =
                studentTermScopeSupport.requireServiceScope(service.getId());
        studentTermScopeSupport.requireTermInSchool(
                ao.getSchoolId(), scope.getTermId());
        return scope.getTermId();
    }


    @Override
    public Long queryStudentIdByParent(Long parentId, Long schoolId) {
        EduclTermEntity term = studentTermScopeSupport.resolveReadableTerm(schoolId);
        Long mainId = null;
        List<Long> studentIds = this.baseMapper.queryPassStudentIdByParentUserId(parentId, schoolId, term.getId(),null);
        if(CollectionUtil.isNotEmpty(studentIds)){
            mainId = studentIds.get(0);
            String parentPhone = SmartRequestTokenUtil.getRequestUser().getEmployeeBO().getPhone();
            Long currStuId = cacheService.setLoginParentStuIdCache(parentPhone, mainId);
            if(CheckParamUtil.checkLongIdParam(currStuId)) return currStuId;
        }
        return mainId;
    }


    /**
     * 返回学生信息，上游业务应使用studentId
     * @param parentId
     * @param schoolId
     * @return
     */
    @Override
    public LoginStuParentDetailVO queryStudentByParent(Long parentId, Long schoolId) {
        EduclTermEntity term = studentTermScopeSupport.resolveReadableTerm(schoolId);
        LoginStuParentDetailVO loginStuParentDetailVO = this.baseMapper.getStuParentDetail(
                parentId, schoolId, term.getId());
        if (loginStuParentDetailVO != null) {
            loginStuParentDetailVO.setTermId(term.getId());
            loginStuParentDetailVO.setTermName(term.getTermName());

            String parentPhone = SmartRequestTokenUtil.getRequestUser().getEmployeeBO().getPhone();
            // 获取缓存中的学生id
            Long currStuId = cacheService.getLoginParentStuIdCache(parentPhone);
            if(CheckParamUtil.checkLongIdParam(currStuId)){
                loginStuParentDetailVO.setStudentId(currStuId);
            }

            // 查询多个孩子id集合
            List<Long> studentIdList = this.baseMapper.queryPassStudentIdByParentUserId(parentId, schoolId, term.getId(), null);
            loginStuParentDetailVO.setStudentIdList(studentIdList);
        }
        return loginStuParentDetailVO;
    }

    @Override
    public List<LoginStuParentDetailVO> queryAllStudentByParent(String parentPhone,Long parentId, Long schoolId) {
        EduclTermEntity term = studentTermScopeSupport.resolveReadableTerm(schoolId);
        List<LoginStuParentDetailVO> loginStuParentList = this.baseMapper.getStuListParentData(
                parentId, schoolId, term.getId());
        Long currStudId = cacheService.getLoginParentStuIdCache(parentPhone);
        for(int s = 0;s<loginStuParentList.size();s++){
            LoginStuParentDetailVO item = loginStuParentList.get(s);
            if(loginStuParentList.size() > 1){
                if(currStudId != null && currStudId.equals(item.getStudentId())){
                    item.setCurrentStudent(true);
                }else{
                    if(s == 0){
                        item.setCurrentStudent(true);
                        // 设置到缓存
                        cacheService.setLoginParentStuIdCache(parentPhone, item.getStudentId());
                    }else{
                        item.setCurrentStudent(false);
                    }
                }
            }else if(loginStuParentList.size() == 1){
                item.setCurrentStudent(true);
                // 设置到缓存
                cacheService.setLoginParentStuIdCache(parentPhone, item.getStudentId());
            }else{
                item.setCurrentStudent(false);
            }
            item.setTermId(term.getId());
            item.setTermName(term.getTermName());
        }
        log.info("call queryAllStudentByParent result:{}", loginStuParentList);
        return loginStuParentList;
    }


    @Override
    public LoginStuParentDataVO queryParentByStudentId(Long studentId, Long schoolId) {
        List<LoginStuParentDataVO> loginStuParentDataVOList = this.baseMapper.getStuParentDetailByStudentId(studentId,schoolId);
        if(CollectionUtil.isNotEmpty(loginStuParentDataVOList)){
            // 学生有多个家长数据时，只返回已经注册的家长
            if(loginStuParentDataVOList.size() > 1){
                loginStuParentDataVOList = loginStuParentDataVOList.stream().filter(item -> item.getGuardianId() != null).collect(Collectors.toList());
                return loginStuParentDataVOList.get(0);
            }else{
                return loginStuParentDataVOList.get(0);
            }
        }
        return null;
    }

    @Override
    public Boolean updateParentInfoByStudent(Long schoolId, Long guardianId, Long studentId,
                                             String relationName, String phone, String parentName) {
        Boolean uptRow = false;
        Long studentIdentityId = requireStudentIdentityId(studentId, schoolId);
        //  修改时新的手机号+学校+学生身份ID在家长表中要确保唯一性
        LambdaQueryWrapper<AsGuardianStudentEntity> uniqueWrapper = new LambdaQueryWrapper<>();
        uniqueWrapper.eq(AsGuardianStudentEntity::getIsValid, 1)
                .eq(AsGuardianStudentEntity::getSchoolId, schoolId)
                .eq(AsGuardianStudentEntity::getStudentIdentityId,studentIdentityId)
                .eq(AsGuardianStudentEntity::getPhone, phone);
        if(ObjectUtils.isNotEmpty(guardianId)){
            uniqueWrapper.ne(AsGuardianStudentEntity::getId, guardianId);
        }

        if (this.count(uniqueWrapper) > 0) {
            log.info("call updateParentInfoByStudent guardianId in exist");
            return false;
        }
        Long userId = 0L;
        if(CheckParamUtil.checkLongIdParam(guardianId)){
            AsGuardianStudentEntity asGuardianStudentEntity = this.getById(guardianId);
            if(null == asGuardianStudentEntity){
                log.warn("call updateParentInfoByStudent guardianId not exist");
                return false;
            }
            asGuardianStudentEntity.setRelationName(relationName);
            asGuardianStudentEntity.setPhone(phone);
            asGuardianStudentEntity.setParentName(parentName);
            asGuardianStudentEntity.setSchoolId(schoolId);
            // 存在已经注册了的用户
            if(ObjectUtils.isNotEmpty(asGuardianStudentEntity.getGuardianUserId())) userId = asGuardianStudentEntity.getGuardianUserId();
            uptRow = this.updateById(asGuardianStudentEntity);
            log.info("call updateParentInfoByStudent update guardianId:{}, result:{}", guardianId, uptRow);
        }else{
            // 新增
            if(StringUtils.isBlank(parentName) || StringUtils.isBlank(phone)) return false;
            AsGuardianStudentEntity asGuardianStudentEntity = new AsGuardianStudentEntity();
            asGuardianStudentEntity.setStudentIdentityId(studentIdentityId);
            asGuardianStudentEntity.setRelationName(relationName);
            asGuardianStudentEntity.setPhone(phone);
            asGuardianStudentEntity.setParentName(parentName);
            asGuardianStudentEntity.setSchoolId(schoolId);
            if(checkExistEntenry(studentId, parentName, schoolId)){
                log.warn("call updateParentInfoByStudent guardianId in exist");
            }else{
                uptRow = this.save(asGuardianStudentEntity);
            }
        }

        if(uptRow && userId != 0L){
            // 查询用户信息
            LambdaQueryWrapper<TBaseUserEntity> uw = new LambdaQueryWrapper<>();
            uw.eq(TBaseUserEntity::getId, userId)
                    .eq(TBaseUserEntity::getIsValid, 1);
            TBaseUserEntity user = tbaseUserService.getOne(uw);

            // 更新用户的手机号和登录名为新的手机号
            user.setPhone(phone);
            user.setLoginName(phone);
            user.setActualName(parentName);
            user.setUpdateTime(new Date());
            user.setUpdateUser(userId);
            boolean userUpdated = tbaseUserService.updateById(user);
            if (!userUpdated) {
                return true;
            }

            log.info("家长个人信息编辑成功，userId:{}, guardianId:{}, phone:{}", userId, guardianId, phone);
        }
        return uptRow;
    }

    // ======================== 家长个人信息查询/编辑 ========================


    /**
     * 查询家长指定稳定学生身份的个人信息详情，并返回同学期全部孩子选项。
     */
    @Override
    public AsParentPersonDetailVO queryParentPersonDetail(Long schoolId,
                                                          Long userId,
                                                          Long studentIdentityId) {
        // 1. 家长关系绑定稳定身份，个人页映射到当前可读学期快照
        EduclTermEntity defaultTerm = studentTermScopeSupport.resolveReadableTerm(schoolId);
        LambdaQueryWrapper<AsGuardianStudentEntity> gw = new LambdaQueryWrapper<>();
        gw.eq(AsGuardianStudentEntity::getGuardianUserId, userId)
                .eq(AsGuardianStudentEntity::getSchoolId, schoolId)
                .eq(AsGuardianStudentEntity::getBindStatus, 1)
                .eq(Args.isNotNull(studentIdentityId), AsGuardianStudentEntity::getStudentIdentityId, studentIdentityId)
                .eq(AsGuardianStudentEntity::getIsValid, 1)
                .orderByDesc(AsGuardianStudentEntity::getCreateTime);
        List<AsGuardianStudentEntity> relations = this.list(gw);
        List<Long> identIds = new ArrayList<>();
        AsGuardianStudentEntity guardian = null;
        String phone = SmartRequestTokenUtil.getRequestUser().getEmployeeBO().getPhone();
        Long currStuId = cacheService.getLoginParentStuIdCache(phone);
        if (CollectionUtil.isNotEmpty(relations)) {
            identIds = relations.stream().map(AsGuardianStudentEntity::getStudentIdentityId).collect(Collectors.toList());
            guardian = relations.get(0);
            if(!CheckParamUtil.checkLongIdParam(currStuId)){
                // 一个家长有多个学生时，获取当前学生
                currStuId = cacheService.getLoginParentStuIdCache(guardian.getPhone());
            }
        }else if(CollectionUtil.isEmpty(relations)){
            return null;
        }

        LambdaQueryWrapper<AsStudentEntity> sw = new LambdaQueryWrapper<>();
        sw.in(AsStudentEntity::getStudentIdentityId, identIds)
                .eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, defaultTerm.getId())
                .eq(AsStudentEntity::getIsValid, 1)
                .orderByAsc(AsStudentEntity::getId);
        List<AsStudentEntity> students = asStudentService.list(sw);
        if (CollectionUtil.isEmpty(students)) {
            return null;
        }

        // 3. 查询用户信息
        LambdaQueryWrapper<TBaseUserEntity> uw = new LambdaQueryWrapper<>();
        uw.eq(TBaseUserEntity::getId, userId)
                .eq(TBaseUserEntity::getIsValid, 1);
        TBaseUserEntity user = tbaseUserService.getOne(uw);


        Map<Long,AsParentPersonDetailVO> studentMap = new HashMap<>();
        // 学生信息
        // 4. 组装结果
        AsParentPersonDetailVO resVo = new AsParentPersonDetailVO();
        List<AsGuardianStudentOptionVO> optList = new ArrayList<>();
        for(AsStudentEntity student: students){
            // 4. 组装结果
            AsParentPersonDetailVO vo = new AsParentPersonDetailVO();
            AsGuardianStudentOptionVO option = new AsGuardianStudentOptionVO();

            // 家长信息
            vo.setGuardianId(guardian.getId());
            vo.setParentName(guardian.getParentName());
            vo.setRelationName(guardian.getRelationName());
            vo.setPhone(guardian.getPhone());

            // 用户性别
            if (user != null) {
                vo.setGender(user.getSex());
                // 登录用户名脱密
                vo.setLoginName(TuominUtil.tuomingName(user.getLoginName()));
            }

            vo.setStudentId(student.getId());
            vo.setStudentIdentityId(student.getStudentIdentityId());
            vo.setTermId(student.getTermId());
            vo.setTermName(defaultTerm.getTermName());
            vo.setStudentName(student.getStudentName());
            vo.setStudentNo(student.getStudentNo());
            vo.setStudentGender(student.getGender());
            // 身份证脱密
            vo.setIdCard(TuominUtil.tuomingIdCard(student.getIdCard()));

            vo.setTermId(defaultTerm.getId());
            vo.setTermName(defaultTerm.getTermName());
            vo.setStudentIdentityId(student.getStudentIdentityId());
            // 学生头像
            if (CheckParamUtil.checkLongIdParam(student.getAvatarFileId())) {
                String avatarUrl = baseFileService.getFileUrlById(student.getAvatarFileId());
                vo.setImgUrl(avatarUrl);
            }

            // 班级名称
            if (CheckParamUtil.checkLongIdParam(student.getClassId())) {
                TEduclClass educlClass = educlClassService.getById(student.getClassId());
                if (educlClass != null && educlClass.getIsValid() != null && educlClass.getIsValid() == 1) {
                    vo.setClassName(educlClass.getClassName());

                    // 年级名称
                    if (CheckParamUtil.checkLongIdParam(student.getGradeId())) {
                        EduclGradeEntity grade = educlGradeService.getById(student.getGradeId());
                        if (grade != null && grade.getIsValid() != null && grade.getIsValid() == 1) {
                            vo.setGradeName(grade.getGradeName());
                        }
                    }
                }
            }
            SmartBeanUtil.copyProperties(vo, option);
            option.setEnrollmentStatus(student.getSchoolRollStatus());
            if(CheckParamUtil.checkLongIdParam(currStuId) && currStuId.equals(student.getId())) {
                option.setCurrentStudent(true);
            }else{
                option.setCurrentStudent(false);
            }
            if(StringUtils.isNotBlank(option.getImgUrl())){
                option.setImgUrl(option.getImgUrl());
            }
            optList.add(option);
            studentMap.put(student.getId(), vo);
        }
        if(CollectionUtil.isNotEmpty(studentMap) && CheckParamUtil.checkLongIdParam(currStuId)){
            resVo = studentMap.get(currStuId);
            resVo.setStudentOptions(optList);
        }

        return resVo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateParentPerson(AsParentPersonEditAO ao) {
        Long userId = ao.getUserId();

        // a. 校验用户id是否同时存在于家长表和用户表
        LambdaQueryWrapper<AsGuardianStudentEntity> gw = new LambdaQueryWrapper<>();
        gw.eq(AsGuardianStudentEntity::getGuardianUserId, userId)
                .eq(AsGuardianStudentEntity::getBindStatus, 1)
                .eq(AsGuardianStudentEntity::getIsValid, 1)
                .last("limit 1");
        AsGuardianStudentEntity guardian = this.getOne(gw, false);
        if (guardian == null) {
            return JsonResult.buildError("家长信息不存在");
        }

        LambdaQueryWrapper<TBaseUserEntity> uw = new LambdaQueryWrapper<>();
        uw.eq(TBaseUserEntity::getId, userId)
                .eq(TBaseUserEntity::getIsValid, 1);
        TBaseUserEntity user = tbaseUserService.getOne(uw);
        if (user == null) {
            return JsonResult.buildError("用户信息不存在");
        }

        // b. 修改时新的手机号+家长姓名在家长表中要确保唯一性
        LambdaQueryWrapper<AsGuardianStudentEntity> uniqueWrapper = new LambdaQueryWrapper<>();
        uniqueWrapper.eq(AsGuardianStudentEntity::getIsValid, 1)
                .eq(AsGuardianStudentEntity::getSchoolId, guardian.getSchoolId())
                .eq(AsGuardianStudentEntity::getParentName, ao.getParentName())
                .eq(AsGuardianStudentEntity::getPhone, ao.getPhone())
                .ne(AsGuardianStudentEntity::getId, guardian.getId());
        if (this.count(uniqueWrapper) > 0) {
            return JsonResult.buildError("该学校下已存在相同姓名和手机号的家长");
        }

        // c. 同时更新家长表和用户表
        // 更新家长表
        guardian.setPhone(ao.getPhone());
        guardian.setParentName(ao.getParentName());
        guardian.setRelationName(ao.getRelationName());
        guardian.setUpdateTime(new Date());
        guardian.setUpdateUser(userId);
        boolean guardianUpdated = this.updateById(guardian);
        if (!guardianUpdated) {
            return JsonResult.buildError("更新家长信息失败");
        }

        // 更新用户的手机号和登录名为新的手机号
        user.setPhone(ao.getPhone());
        user.setLoginName(ao.getPhone());
        user.setActualName(ao.getParentName()+ao.getRelationName());
        if (ao.getGender() != null) {
            user.setSex(ao.getGender());
        }
        if (StrUtil.isNotBlank(ao.getIdCard())) {
            user.setIdCard(ao.getIdCard());
        }
        user.setUpdateTime(new Date());
        user.setUpdateUser(userId);
        boolean userUpdated = tbaseUserService.updateById(user);
        if (!userUpdated) {
            return JsonResult.buildError("更新用户信息失败");
        }

        log.info("家长个人信息编辑成功，userId:{}, guardianId:{}, phone:{}", userId, guardian.getId(), ao.getPhone());
        return JsonResult.buildSuccess("操作成功");
    }



    /**
     * 根据学期学生快照解析稳定身份，兼容尚未补齐稳定身份的开发数据。
     */
    private Long requireStudentIdentityId(Long studentId, Long schoolId) {
        if (!CheckParamUtil.checkLongIdParam(studentId)) {
            throw businessError("学生编号不能为空");
        }
        AsStudentEntity student = asStudentService.getById(studentId);
        if (student == null || !Integer.valueOf(1).equals(student.getIsValid())
                || (schoolId != null && !schoolId.equals(student.getSchoolId()))) {
            throw businessError("学生不存在或不属于当前学校");
        }
        if (student.getStudentIdentityId() == null) {
            log.error("call studentIdentitySupport.resolveOrCreateIdentity studentNo:{},is identId null",student.getStudentNo());
//            studentIdentitySupport.resolveOrCreateIdentity(
//                    student, SmartRequestTokenUtil.getRequestUserId());
//            if (!asStudentService.updateById(student)) {
//                throw businessError("学生稳定身份关联失败");
//            }
        }
        return student.getStudentIdentityId();
    }

    /**
     * 构造统一业务异常。
     */
    private BaseBusinessException businessError(String message) {
        return new BaseBusinessException(ResultCode.PARA_ERROR.code(), message);
    }
}
