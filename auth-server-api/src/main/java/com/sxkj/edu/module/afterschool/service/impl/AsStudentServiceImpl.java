package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import com.sxkj.edu.module.afterschool.domain.ao.AsStudentNoByGradeClassAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsStudentQueryListAO;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.excel.AsLearningAchievementExportExcel;
import com.sxkj.edu.module.afterschool.domain.excel.AsStudentExcel;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.enums.RegisterStatusEnum;
import com.sxkj.edu.module.afterschool.mapper.*;
import com.sxkj.edu.module.afterschool.service.*;
import com.sxkj.edu.module.afterschool.support.StudentBusinessGuard;
import com.sxkj.edu.module.afterschool.support.StudentIdentitySupport;
import com.sxkj.edu.module.afterschool.support.ServiceEnrollmentNoticeSupport;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.module.system.employee.TbaseUserService;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDataVO;
import com.sxkj.edu.service.BaseFileService;
import com.sxkj.edu.service.SchoolManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生档案 Service 实现类
 *
 * @author system
 * @date 2026-05-19
 */
@Service
@Slf4j
public class AsStudentServiceImpl extends ServiceImpl<AsStudentMapper, AsStudentEntity> implements AsStudentService {

    @Resource
    private EduclGradeService educlGradeService;
    @Resource
    private EduclClassService educlClassService;
    @Resource
    private SchoolManageService schoolManageService;
    @Resource
    private AsGuardianStudentService asGuardianStudentService;
    @Resource
    private AsCourseSessionMapper asCourseSessionMapper;
    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;
    @Resource
    private AsCourseClassMapper asCourseClassMapper;

    @Resource
    private BaseFileService baseFileService;
    @Resource
    private TbaseUserService tbaseUserService;

    @Resource
    private EduclTermService educlTermService;
    @Resource
    private AsStudentIdentityService asStudentIdentityService;
    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;
    @Resource
    private StudentIdentitySupport studentIdentitySupport;
    @Resource
    private StudentBusinessGuard studentBusinessGuard;
    @Resource
    private AsStudentTermChangeLogMapper asStudentTermChangeLogMapper;
    @Resource
    private ServiceEnrollmentNoticeSupport serviceEnrollmentNoticeSupport;


    // ======================== 通用私有方法 ========================

    /**
     * 校验同一学校、同一学期下学号是否已存在。
     */
    private boolean existsByTermAndNo(Long schoolId, Long termId, String studentNo, Long excludeId) {
        LambdaQueryWrapper<AsStudentEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsStudentEntity::getIsValid, 1)
         .eq(schoolId != null, AsStudentEntity::getSchoolId, schoolId)
         .eq(AsStudentEntity::getTermId, termId)
         .eq(AsStudentEntity::getStudentNo, studentNo);
        if (excludeId != null) {
            w.ne(AsStudentEntity::getId, excludeId);
        }
        return this.count(w) > 0;
    }

    /**
     * 校验同一学校、同一学期下稳定身份是否已有有效快照。
     */
    private boolean existsByTermAndIdentity(Long schoolId, Long termId,
                                            Long studentIdentityId, Long excludeId) {
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, termId)
                .eq(AsStudentEntity::getStudentIdentityId, studentIdentityId);
        if (excludeId != null) {
            wrapper.ne(AsStudentEntity::getId, excludeId);
        }
        return this.count(wrapper) > 0;
    }

    /**
     * 查询同一稳定身份在指定学期被逻辑删除且可恢复的快照。
     */
    private AsStudentEntity findDeletedSnapshot(Long schoolId, Long termId,
                                                Long studentIdentityId) {
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, termId)
                .eq(AsStudentEntity::getStudentIdentityId, studentIdentityId)
                .eq(AsStudentEntity::getIsValid, 0)
                .orderByDesc(AsStudentEntity::getId)
                .last("LIMIT 1");
        List<AsStudentEntity> students = this.list(wrapper);
        return CollectionUtil.isEmpty(students) ? null : students.get(0);
    }

    /**
     * 恢复同一稳定身份在目标学期被逻辑删除的学生快照。
     *
     * <p>恢复时复用原学生编号并保留原创建信息，只更新本次提交的基础资料。</p>
     */
    private boolean restoreDeletedSnapshot(AsStudentEntity deleted,
                                           AsStudentEntity submitted,
                                           Long operatorId) {
        Long deletedId = deleted.getId();
        Date createTime = deleted.getCreateTime();
        Long createUser = deleted.getCreateUser();
        BeanUtils.copyProperties(submitted, deleted,
                "id", "createTime", "createUser");
        deleted.setId(deletedId);
        deleted.setCreateTime(createTime);
        deleted.setCreateUser(createUser);
        deleted.setIsValid(1);
        deleted.setUpdateUser(operatorId);
        deleted.setUpdateTime(new Date());
        return this.updateById(deleted);
    }

    /**
     * 学生转学、休学或毕业时处理尚未产生业务的未来学期快照。
     */
    private JsonResult<String> disableFutureSnapshots(AsStudentEntity current,
                                                      Integer newSchoolRollStatus,
                                                      Long operatorId) {
        return handleFutureSnapshots(
                current, newSchoolRollStatus, operatorId, true);
    }

    /**
     * 校验未来学期快照是否允许在学籍变更生效时停用。
     */
    private JsonResult<String> validateFutureSnapshotsCanDisable(
            AsStudentEntity current, Integer newSchoolRollStatus) {
        return handleFutureSnapshots(
                current, newSchoolRollStatus, null, false);
    }

    /**
     * 校验并按需停用尚未产生业务的未来学期快照。
     */
    private JsonResult<String> handleFutureSnapshots(
            AsStudentEntity current,
            Integer newSchoolRollStatus,
            Long operatorId,
            boolean disableSnapshot) {
        EduclTermEntity currentTerm = educlTermService.getById(current.getTermId());
        if (currentTerm == null || currentTerm.getStartDate() == null) {
            return JsonResult.buildError("学生所属学期不存在，不能变更学籍状态");
        }
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getSchoolId, current.getSchoolId())
                .eq(AsStudentEntity::getStudentIdentityId,
                        current.getStudentIdentityId())
                .ne(AsStudentEntity::getId, current.getId())
                .eq(AsStudentEntity::getIsValid, 1);
        List<AsStudentEntity> snapshots = this.list(wrapper);
        if (CollectionUtil.isEmpty(snapshots)) {
            return null;
        }
        List<AsStudentEntity> futureSnapshots = new ArrayList<>();
        for (AsStudentEntity snapshot : snapshots) {
            EduclTermEntity term = educlTermService.getById(snapshot.getTermId());
            if (term != null && term.getStartDate() != null
                    && term.getStartDate().after(currentTerm.getStartDate())) {
                if (studentBusinessGuard.hasStudentBusiness(snapshot.getId())) {
                    return JsonResult.buildError(
                            "未来学期学生档案已产生业务，请先处理未来学期报名或排课");
                }
                futureSnapshots.add(snapshot);
            }
        }
        if (!disableSnapshot) {
            return null;
        }
        Date now = new Date();
        for (AsStudentEntity snapshot : futureSnapshots) {
            snapshot.setSchoolRollStatus(newSchoolRollStatus);
            snapshot.setIsValid(0);
            snapshot.setUpdateUser(operatorId);
            snapshot.setUpdateTime(now);
            if (!this.updateById(snapshot)) {
                throw new BaseBusinessException(
                        ResultCode.STU_MANAGER_ERROR.code(), "未来学期学生档案停用失败");
            }
        }
        return null;
    }

    /**
     * 记录学生学期内年级、班级或学籍状态的变更历史。
     */
    private void saveTermChangeLog(AsStudentEntity before,
                                   AsStudentEntity after,
                                   AsStudentAddAO ao,
                                   Long operatorId,
                                   boolean pending) {
        AsStudentTermChangeLogEntity logEntity = new AsStudentTermChangeLogEntity();
        logEntity.setSchoolId(before.getSchoolId());
        logEntity.setTermId(before.getTermId());
        logEntity.setStudentId(before.getId());
        logEntity.setStudentIdentityId(before.getStudentIdentityId());
        logEntity.setEffectiveTime(ao.getEffectiveTime() == null
                ? new Date() : ao.getEffectiveTime());
        logEntity.setOldGradeId(before.getGradeId());
        logEntity.setNewGradeId(after.getGradeId());
        logEntity.setOldClassId(before.getClassId());
        logEntity.setNewClassId(after.getClassId());
        logEntity.setOldSchoolRollStatus(before.getSchoolRollStatus());
        logEntity.setNewSchoolRollStatus(after.getSchoolRollStatus());
        logEntity.setChangeReason(StringUtils.isBlank(ao.getChangeReason())
                ? "学生信息维护" : StringUtils.trim(ao.getChangeReason()));
        logEntity.setApplyStatus(pending
                ? CoreConstant.AS_STUDENT_TERM_CHANGE_PENDING
                : CoreConstant.AS_STUDENT_TERM_CHANGE_APPLIED);
        logEntity.setApplyTime(pending ? null : new Date());
        logEntity.setCreateUser(operatorId);
        logEntity.setCreateTime(new Date());
        logEntity.setIsValid(1);
        if (asStudentTermChangeLogMapper.insert(logEntity) != 1) {
            throw new BaseBusinessException(
                    ResultCode.STU_MANAGER_ERROR.code(), "学生学期资料变更记录保存失败");
        }
    }

    /**
     * 校验资料变更生效时间属于学生当前学期。
     */
    private JsonResult<String> validateEffectiveTimeInTerm(
            AsStudentEntity student, Date effectiveTime) {
        EduclTermEntity term = educlTermService.getById(student.getTermId());
        if (term == null || term.getStartDate() == null
                || term.getEndDate() == null) {
            return JsonResult.buildError("学生所属学期周期不完整，不能登记资料变更");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(term.getEndDate());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        if (effectiveTime.before(term.getStartDate())
                || !effectiveTime.before(calendar.getTime())) {
            return JsonResult.buildError("学生资料变更生效时间必须在所属学期周期内");
        }
        return null;
    }

    /**
     * 判断学生是否已有尚未生效的资料变更。
     */
    private boolean hasPendingTermChange(Long studentId) {
        LambdaQueryWrapper<AsStudentTermChangeLogEntity> wrapper =
                new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentTermChangeLogEntity::getStudentId, studentId)
                .in(AsStudentTermChangeLogEntity::getApplyStatus,
                        CoreConstant.AS_STUDENT_TERM_CHANGE_PENDING,
                        CoreConstant.AS_STUDENT_TERM_CHANGE_PROCESSING)
                .eq(AsStudentTermChangeLogEntity::getIsValid, 1);
        return asStudentTermChangeLogMapper.selectCount(wrapper) > 0;
    }

    /**
     * Entity -> VO
     */
    private AsStudentVO toVO(AsStudentEntity entity) {
        if (entity == null) return null;
        AsStudentVO vo = new AsStudentVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    // ======================== 对外方法 ========================

    @Override
    public PageResultDTO<AsStudentVO> queryAsStudentPage(AsStudentQueryAO qo) {
        // 设置默认学期
        normalizeStudentQueryScope(qo);
        Page<AsStudentVO> page = baseMapper.pageStudent(new Page<>(qo.getPageIndex(), qo.getPageSize()), qo);
        PageResultDTO<AsStudentVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        List<AsStudentVO> list = page.getRecords();
        if(CollectionUtil.isNotEmpty(list)){
            list.forEach(vo -> {
                if(CheckParamUtil.checkLongIdParam(vo.getGradeId())){
                    EduclGradeEntity gradeEntity = educlGradeService.getById(vo.getGradeId());
                    vo.setGradeName(gradeEntity != null ? gradeEntity.getGradeName():"");
                }
                if(CheckParamUtil.checkLongIdParam(vo.getClassId())){
                    TEduclClass classEntity = educlClassService.getById(vo.getClassId());
                    vo.setClassName(classEntity != null ? classEntity.getClassName():"");
                }
                // 查询学生家长
                LoginStuParentDataVO parentDataVO = asGuardianStudentService.queryParentByStudentId(vo.getId(), vo.getSchoolId());
                if(null != parentDataVO){
                    vo.setParentName(parentDataVO.getParentName());
                    vo.setParentPhone(parentDataVO.getPhone());
                    vo.setRelationName(parentDataVO.getRelationName());
                }
                if(CheckParamUtil.checkLongIdParam(vo.getSchoolId())){
                    SchoolManagePo schoolEntity = schoolManageService.getById(vo.getSchoolId());
                    vo.setSchoolName(schoolEntity != null ? schoolEntity.getSchoolName():"");
                }
            });
        }
        result.setList(list);
        return result;
    }

    @Override
    public JsonResult<String> updateAsStudent(AsStudentAddAO ao) {
        JsonResult<String> result = JsonResult.buildError(ResultCode.STU_MANAGER_ERROR.code(),"学生管理更新失败");
        if (ao.getId() == null) return JsonResult.buildError("学生ID不能为空");
        AsStudentEntity exist = this.getById(ao.getId());
        if(ObjectUtil.isNull(ao.getSchoolId())) ao.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
        studentTermScopeSupport.requireTermInSchool(ao.getSchoolId(), ao.getTermId());

        if (exist == null || exist.getIsValid() != 1) return JsonResult.buildError("学生记录不存在");
        if (existsByNameAndNo(ao.getSchoolId(), ao.getStudentName(), ao.getStudentNo(), ao.getId())) {
            return JsonResult.buildError("该学校下已存在相同姓名和学号的学生");
        }
        AsStudentEntity entity = new AsStudentEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());

//        boolean termProfileChanged = !Objects.equals(exist.getGradeId(), ao.getGradeId())
//                || !Objects.equals(exist.getClassId(), ao.getClassId())
//                || !Objects.equals(exist.getSchoolRollStatus(),
//                ao.getSchoolRollStatus() == null ? exist.getSchoolRollStatus()
//                        : ao.getSchoolRollStatus());
//        boolean hasBusiness = studentBusinessGuard.hasStudentBusiness(exist.getId());
//        if (termProfileChanged && hasBusiness
//                && (ao.getEffectiveTime() == null
//                || StringUtils.isBlank(ao.getChangeReason()))) {
//            return JsonResult.buildError("学生已产生业务无法修改，请联系管理员");
//        }
        Boolean uptRow = this.updateById(entity);
        if(uptRow){
            // 更新学生家长信息：根据家长姓名、学生ID、手机号和学校查询是否存在
            asGuardianStudentService.updateParentInfoByStudent(ao.getSchoolId(),ao.getGuardianId(),ao.getId(),ao.getRelationName(),ao.getParentPhone(),
                    ao.getParentName());
            Boolean uptStuId = asStudentIdentityService.updateStudentIdentityStudent(entity,ao.getEnrollmentYear());
            if(uptStuId){
                result = JsonResult.buildSuccess("学生管理更新成功");
            }
        }
        log.info("call AsStudentService updateAsStudent is request:{},exec result:{}", ao, uptRow);
        return result;
    }

    /**
     * 校验同一学校下学生姓名+学号是否已存在
     */
    private boolean existsByNameAndNo(Long schoolId, String studentName, String studentNo, Long termId) {
        LambdaQueryWrapper<AsStudentEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, termId)
                .eq(AsStudentEntity::getStudentName, studentName)
                .eq(AsStudentEntity::getStudentNo, studentNo);
        return this.count(w) > 0;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> deleteAsStudent(Long id) {
        JsonResult<String> result = JsonResult.buildError(ResultCode.STU_MANAGER_ERROR.code(),"学生管理删除失败");
        AsStudentEntity entity = this.getById(id);
        if (entity == null || entity.getIsValid() != 1) return JsonResult.buildError("学生记录不存在");
        if (studentBusinessGuard.hasStudentBusiness(id)) {
            return JsonResult.buildError("学生已产生业务数据，不能删除");
        }
        entity.setIsValid(0);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        Boolean uptRow = this.updateById(entity);
        if(uptRow){
            result = JsonResult.buildSuccess("操作成功");
        }
        log.info("call AsStudentService deleteAsStudent is request:{},exec result:{}", id, uptRow);
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> importAsStudentData(MultipartFile file, Long schoolId) {
        log.info("call AsStudentService importAsStudentData is schoolId:{}", schoolId);
        try (InputStream is = file.getInputStream()) {
            List<AsStudentExcel> list = ExcelUtils.customerReadExcel(is, AsStudentExcel.class,1);
            if (list == null || list.isEmpty()) return JsonResult.buildError("导入数据为空");
            List<String> errors = new ArrayList<>();
            int row = 1;
            for (AsStudentExcel excel : list) {
                String phone = excel.getParentPhone();
                if (StrUtil.isBlank(excel.getStudentName()) || StrUtil.isBlank(excel.getStudentNo())) {
                    errors.add("第" + row + "行: 学生姓名或学号不能为空");
                    row++;
                    continue;
                }
                if (StrUtil.isBlank(excel.getParentName()) || StrUtil.isBlank(excel.getParentPhone())
                        || StrUtil.isBlank(excel.getClassName()) || StrUtil.isBlank(excel.getGradeName())) {
                    errors.add("第" + row + "行: 学生家长姓名、手机号、年级班级不能为空");
                    row++;
                    continue;
                }
                if (StringUtils.isNotBlank(excel.getParentPhone()) && excel.getParentPhone().length() > 48) {
                    errors.add("第" + row + "行: 手机号太长了");
                    row++;
                    continue;
                }
                List<String> phoneList = AssembleUtil.spitNameForList(phone, ";");
                if(CollectionUtil.isNotEmpty(phoneList)){
                    phone = phoneList.get(0);
                }
                AsStudentEntity entity = new AsStudentEntity();
                entity.setSchoolId(schoolId);
                entity.setStudentName(excel.getStudentName());
                entity.setStudentNo(excel.getStudentNo());
                Long termId = educlTermService.getEduclTermExistForId(excel.getTermName(),schoolId);
                if(!CheckParamUtil.checkLongIdParam(termId)){
                    errors.add("第" + row + "行: 学期[" + excel.getTermName() + "]不存在，跳过");
                    row++;
                    continue;
                }
                if(checkExistEntenry(excel.getStudentNo(), schoolId,termId)){
                    errors.add("第" + row + "行: 学生[" + excel.getStudentName() + "/" + excel.getStudentNo() + "]已存在，跳过");
                    row++;
                    continue;
                }
                entity.setGender(AssembleUtil.getStuSexDetail(excel.getGender()));
                entity.setTermId(termId);
                entity.setIdCard(excel.getIdCard());
                // 设置学段
                Integer stageNum = AssembleUtil.operStudentStage(excel.getStageName());
                // 保存或获取年级id
                Long gradeId = educlGradeService.createEduclGrade(schoolId, stageNum, excel.getGradeName());
                // 保存或获取班级
                if(!CheckParamUtil.checkLongIdParam(gradeId)){
                    errors.add("第" + row + "行: 学生[" + excel.getStudentName() + "/" + excel.getStudentNo() + "]获取年级失败，跳过");
                    row++;
                    continue;
                }
                Long classId = educlClassService.createStudentClassImport(schoolId, gradeId, excel.getClassName(),termId);
                if(!CheckParamUtil.checkLongIdParam(classId)){
                    errors.add("第" + row + "行: 学生[" + excel.getStudentName() + "/" + excel.getStudentNo() + "]获取班级失败，跳过");
                    row++;
                    continue;
                }
                entity.setGradeId(gradeId);
                entity.setClassId(classId);
                entity.setSchoolRollStatus(1);
                entity.setRegisterStatus(0);
                // 默认头像id
                if(1 == entity.getGender()){
                    entity.setAvatarFileId(GrobProperties.getInstance().womanDefUrlId);
                }else{
                    entity.setAvatarFileId(GrobProperties.getInstance().manDefUrlId);
                }
                entity.setIsValid(1);
                entity.setCreateTime(new Date());
                entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                Boolean uptRow = this.save(entity);
                // 创建学生家长关系表
                Long studentId = entity.getId();
                if(uptRow && CheckParamUtil.checkLongIdParam(studentId)){

                    // 创建学生身份信息
                    Long originStudentId = asStudentIdentityService.createStudentIdentityStudent(entity,excel.getEnrollmentYear());
                    if(!CheckParamUtil.checkLongIdParam(originStudentId)){
                        errors.add("第" + row + "行: 学生[" + excel.getStudentName() + "/" + excel.getStudentNo() + "]导入失败");
                        row++;
                        continue;
                    }
                    // 最后更新学生表的身份id
                    asGuardianStudentService.createStudentGuardImport(schoolId, studentId, entity.getStudentName(), excel,phone);
                    Boolean uptStuRes = updateStudentForStudentIdentId(studentId, originStudentId);
                    if(!uptStuRes){
                        errors.add("第" + row + "行: 学生[" + excel.getStudentName() + "/" + excel.getStudentNo() + "]导入失败");
                        row++;
                        continue;
                    }
                }

                row++;
            }
            if (!errors.isEmpty()) {
                return JsonResult.buildError("部分数据导入失败：" + String.join("；", errors));
            }
            return JsonResult.buildSuccess("导入成功");
        } catch (Exception e) {
            log.error("学生导入异常", e);
            markCurrentTransactionRollback();
            return JsonResult.buildError("学生导入失败");
        }
    }

    /**
     * 导入出现不可恢复异常时标记当前事务回滚，避免返回错误后仍提交部分数据。
     */
    private void markCurrentTransactionRollback() {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }


    @Override
    public void exportAsStudent(AsStudentQueryAO qo, HttpServletResponse response) {
        List<AsStudentVO> studentList = queryAsStudentPage(qo).getList();
        if(CollectionUtil.isEmpty(studentList)) return;

        List<AsStudentExcel> excelList = studentList.stream().map(e -> {
            AsStudentExcel ex = new AsStudentExcel();
            BeanUtils.copyProperties(e, ex);
            ex.setGender(AssembleUtil.getStuSexForInteger(e.getGender()));
            return ex;
        }).collect(Collectors.toList());
        try {
            ExcelUtils.exportExcel(response, excelList, AsStudentExcel.class, "学生档案导出", "学生档案导出");
        } catch (Exception e) {
            log.error("学生导出异常", e);
        }
    }


    /**
     * 在指定业务学期保存或更新家长注册学生。
     */
    @Override
    public AsStudentEntity saveOrUpdateStudent(ParentRegisterAO ao, Long termId) {
        EduclTermEntity targetTerm = studentTermScopeSupport.requireTermInSchool(
                ao.getSchoolId(), termId);
        // 家长注册只在本次业务所属学期内定位学生快照。
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getSchoolId, ao.getSchoolId())
                .eq(AsStudentEntity::getTermId, targetTerm.getId())
                .eq(AsStudentEntity::getStudentName, ao.getStudentName())
                .eq(AsStudentEntity::getStudentNo, ao.getStudentNo())
                .eq(AsStudentEntity::getIsValid, 1);
        AsStudentEntity student = this.getOne(wrapper);
        log.info("call saveOrUpdateStudent get student:{}", student);
        if (student != null) {
            studentIdentitySupport.resolveOrCreateIdentity(
                    student,
                    SmartRequestTokenUtil.getRequestUserId(),
                    resolveStudentEnrollmentYear(
                            null, student.getClassId(), ao.getSchoolId()));
            // 更新学生信息
            // 未来服务发布时已升级目标快照年级和班级，注册不能再被来源页面旧值覆盖。
            if (ao.getServiceId() == null) {
                student.setGradeId(ao.getGradeId());
                student.setClassId(ao.getClassId());
            }
            student.setGender(ao.getStudentGender());
            student.setAvatarFileId(ao.getAvatarFileId());
            student.setIdCard(ao.getIdCard());
            if(student.getRegisterStatus() == 2){// 已驳回
                student.setRegisterStatus(3);// 待审核
            }else if(student.getRegisterStatus() == 0){// 待注册
                student.setRegisterStatus(1);// 已通过
            }
            student.setUpdateTime(new Date());
            Boolean uptRow = this.updateById(student);
            log.info("call saveOrUpdateStudent：schoolId={}, studentName={}, studentNo={},result:{}", ao.getSchoolId(), ao.getStudentName(), ao.getStudentNo(),uptRow);
            if (!uptRow) {
                throw new BaseBusinessException(
                        ResultCode.STU_MANAGER_ERROR.code(), "学生信息更新失败");
            }
            studentIdentitySupport.updateIdentityProfile(
                    student, SmartRequestTokenUtil.getRequestUserId());
            log.info("学生信息更新：schoolId={}, studentName={}, studentNo={}", ao.getSchoolId(), ao.getStudentName(), ao.getStudentNo());
        }else{
            // 新增学生
            student = new AsStudentEntity();
            SmartBeanUtil.copyProperties(ao,student);
            student.setGender(ao.getStudentGender());
            student.setSchoolRollStatus(1); // 在籍
            student.setRegisterStatus(3); // 待审核
            student.setTermId(targetTerm.getId());
            student.setIsValid(1);
            student.setCreateTime(new Date());
            student.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            studentIdentitySupport.resolveOrCreateIdentity(
                    student,
                    SmartRequestTokenUtil.getRequestUserId(),
                    resolveStudentEnrollmentYear(
                            null, student.getClassId(), ao.getSchoolId()));
            if (existsByTermAndIdentity(ao.getSchoolId(), targetTerm.getId(),
                    student.getStudentIdentityId(), null)) {
                throw new BaseBusinessException(
                        ResultCode.STU_MANAGER_ERROR.code(),
                        CoreConstant.AS_STUDENT_TERM_DUPLICATED_MESSAGE);
            }
            if (!this.save(student)) {
                throw new BaseBusinessException(
                        ResultCode.STU_MANAGER_ERROR.code(), "学生信息新增失败");
            }
            log.info("学生信息新增：schoolId={}, studentName={}, studentNo={}", ao.getSchoolId(), ao.getStudentName(), ao.getStudentNo());
        }

        return student;
    }

    private Boolean checkExistEntenry(String stuNo,Long schoolId,Long termId) {
        Boolean exist = false;
        LambdaQueryWrapper<AsStudentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AsStudentEntity::getSchoolId, schoolId);
        queryWrapper.eq(AsStudentEntity::getTermId, termId);
        queryWrapper.eq(AsStudentEntity::getStudentNo, stuNo);
        queryWrapper.eq(AsStudentEntity::getIsValid, 1);
        List<AsStudentEntity> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            exist = true;
        }
        return exist;
    }



    @Override
    public AsStudentMergedRecordVO queryStudentMergedRecord(Long schoolId, Long studentId) {
        AsStudentMergedRecordVO vo = new AsStudentMergedRecordVO();

        // 查询学生信息
        AsStudentEntity student = this.getById(studentId);
        if (student == null || student.getIsValid() != 1) {
            return vo;
        }
        String studentName = student.getStudentName();
        String studentNo = student.getStudentNo();
        vo.setTermId(student.getTermId());
        if (CheckParamUtil.checkLongIdParam(student.getTermId())) {
            EduclTermEntity term = educlTermService.getById(student.getTermId());
            vo.setTermName(term == null ? null : term.getTermName());
        }
        // 2. 报名记录
        vo.setEnrollmentRecords(buildEnrollmentRecords(schoolId, studentId, studentName, studentNo,student.getTermId()));

        return vo;
    }



    /**
     * 构建报名记录
     */
    private List<AsStudentMergedRecordVO.EnrollmentItem> buildEnrollmentRecords(Long schoolId, Long studentId,
                                                                                 String studentName, String studentNo,
                                                                                Long termId) {

        List<AsStudentEnrollmentRegistTermVO> enrollments = asEnrollmentMapper.queryStudentEnrollmentForList(studentId,schoolId,termId);
        if (CollectionUtil.isEmpty(enrollments)) {
            return Collections.emptyList();
        }

        // 批量查询课程班信息
        List<Long> courseClassIds = enrollments.stream()
                .map(AsStudentEnrollmentRegistTermVO::getCourseClassId)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, AsCourseClassEntity> courseClassMap = batchQueryCourseClasses(courseClassIds);

        // 批量查询课次信息（用于上课时间）
        Map<Long, List<AsCourseSessionEntity>> sessionByClassMap = batchQuerySessionsByClassIds(courseClassIds);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        List<AsStudentMergedRecordVO.EnrollmentItem> items = new ArrayList<>();
        for (AsStudentEnrollmentRegistTermVO enrollment : enrollments) {
            AsStudentMergedRecordVO.EnrollmentItem item = new AsStudentMergedRecordVO.EnrollmentItem();
            item.setStudentName(studentName);
            item.setStudentNo(studentNo);
            item.setEnrollTime(enrollment.getEnrollmentTime());
            item.setEnrollStatus(enrollment.getEnrollStatus());
            item.setEnrollStatusDesc(getEnrollStatusName(enrollment.getEnrollStatus()));

            AsCourseClassEntity courseClass = courseClassMap.get(enrollment.getCourseClassId());
            if (courseClass == null) {
                continue;
            }
            item.setCourseClassName(courseClass.getCourseClassName());
            item.setEnrollType(courseClass.getEnrollType());
            item.setEnrollTypeDesc(getEnrollTypeName(courseClass.getEnrollType()));

            // 拼合上课时间
            List<AsCourseSessionEntity> sessions = sessionByClassMap.getOrDefault(enrollment.getCourseClassId(), Collections.emptyList());
            if (!sessions.isEmpty()) {
                AsCourseSessionEntity firstSession = sessions.get(0);
                String startTime = firstSession.getStartTime() != null ? timeFormat.format(firstSession.getStartTime()) : "";
                String endTime = firstSession.getEndTime() != null ? timeFormat.format(firstSession.getEndTime()) : "";
                item.setClassTime(startTime + "-" + endTime);
            }

            items.add(item);
        }
        return items;
    }


    // ======================== 批量查询辅助方法 ========================


    private Map<Long, AsCourseClassEntity> batchQueryCourseClasses(List<Long> courseClassIds) {
        if (CollectionUtil.isEmpty(courseClassIds)) {
            return Collections.emptyMap();
        }
        List<AsCourseClassEntity> classes = asCourseClassMapper.selectBatchIds(courseClassIds);
        return classes.stream().collect(Collectors.toMap(AsCourseClassEntity::getId, c -> c, (a, b) -> a));
    }

    private Map<Long, List<AsCourseSessionEntity>> batchQuerySessionsByClassIds(List<Long> courseClassIds) {
        if (CollectionUtil.isEmpty(courseClassIds)) {
            return Collections.emptyMap();
        }
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        LambdaQueryWrapper<AsCourseSessionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseSessionEntity::getIsValid, 1)
                .in(AsCourseSessionEntity::getCourseClassId, courseClassIds)
                .ne(AsCourseSessionEntity::getSessionStatus, 3)
                .eq(AsCourseSessionEntity::getSchoolId, schoolId)
                .orderByAsc(AsCourseSessionEntity::getSessionDate);
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(wrapper);
        return sessions.stream().collect(Collectors.groupingBy(AsCourseSessionEntity::getCourseClassId));
    }

    /**
     * 优先使用请求中的入学年份，未传时兼容读取班级入学年份。
     */
    private Integer resolveStudentEnrollmentYear(Integer requestedEnrollmentYear,
                                                 Long classId,
                                                 Long schoolId) {
        if (classId == null) {
            return requestedEnrollmentYear;
        }
        TEduclClass classEntity = educlClassService.getById(classId);
        if (classEntity == null
                || !Integer.valueOf(1).equals(classEntity.getIsValid())
                || !Objects.equals(classEntity.getSchoolId(), schoolId)) {
            throw new BaseBusinessException(
                    ResultCode.PARA_ERROR.code(),
                    "班级不存在、已失效或不属于当前学校");
        }
        return requestedEnrollmentYear == null
                ? classEntity.getEnrollmentYear()
                : requestedEnrollmentYear;
    }

    private String getEnrollTypeName(Integer value) {
        if (value == null) return null;
        switch (value) {
            case 1: return "公开报名";
            default: return "未知";
        }
    }

    private String getEnrollStatusName(Integer value) {
        if (value == null) return null;
        switch (value) {
            case 0: return "待确认";
            case 1: return "已报名";
            case 2: return "已取消";
            case 3: return "已分配";
            case 4: return "未分配";
            default: return "未知";
        }
    }


    // ======================== 学生详情查询 ========================

    @Override
    public AsStudentVO getStudentDetail(Long schoolId, Long studentId) {
        LambdaQueryWrapper<AsStudentEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getId, studentId);
        w.eq(AsStudentEntity::getSchoolId, schoolId);
        AsStudentEntity entity = this.getOne(w, false);
        if (entity == null) {
            return null;
        }
        AsStudentVO vo = toVO(entity);
        // 年级名称
        if (CheckParamUtil.checkLongIdParam(vo.getGradeId())) {
            EduclGradeEntity gradeEntity = educlGradeService.getById(vo.getGradeId());
            vo.setGradeName(gradeEntity != null ? gradeEntity.getGradeName() : "");
        }
        // 班级名称
        if (CheckParamUtil.checkLongIdParam(vo.getClassId())) {
            TEduclClass classEntity = educlClassService.getById(vo.getClassId());
            vo.setClassName(classEntity != null ? classEntity.getClassName() : "");
        }
        // 家长信息
        LoginStuParentDataVO parentDataVO = asGuardianStudentService.queryParentByStudentId(vo.getId(), vo.getSchoolId());
        if (parentDataVO != null) {
            vo.setParentName(parentDataVO.getParentName());
            vo.setParentPhone(parentDataVO.getPhone());
            vo.setGuardianId(parentDataVO.getGuardianId());
            vo.setRelationName(parentDataVO.getRelationName());
            vo.setParentId(parentDataVO.getParentId());
        }
        // 学校名称
        if (CheckParamUtil.checkLongIdParam(vo.getSchoolId())) {
            SchoolManagePo schoolEntity = schoolManageService.getById(vo.getSchoolId());
            vo.setSchoolName(schoolEntity != null ? schoolEntity.getSchoolName() : "");
        }
        if(CheckParamUtil.checkLongIdParam(vo.getTermId())){
            EduclTermEntity termEntity = educlTermService.getById(vo.getTermId());
            vo.setTermName(termEntity != null ? termEntity.getTermName():"");
        }
        // 头像地址
        if (CheckParamUtil.checkLongIdParam(entity.getAvatarFileId())) {
            String avatarUrl = baseFileService.getFileUrlById(entity.getAvatarFileId());
            if(StringUtils.isNotBlank(avatarUrl)){
                vo.setImgUrl(avatarUrl);
            }
        }
        return vo;
    }

    @Override
    public Long getStudentId(Long schoolId, String studentName, String studentNo) {
        EduclTermEntity defaultTerm = studentTermScopeSupport.requireDefaultTerm(schoolId);
        LambdaQueryWrapper<AsStudentEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, defaultTerm.getId())
                .eq(AsStudentEntity::getStudentName, studentName)
                .eq(AsStudentEntity::getStudentNo, studentNo)
                .eq(AsStudentEntity::getIsValid, 1);
        AsStudentEntity entity = this.getOne(w, false);
        return entity != null ? entity.getId() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> auditStudentParent(AsStudentAuditAO ao) {
        Long studentId = ao.getStudentId();
        String parentPhone = ao.getParentPhone();
        Integer registerStatus = ao.getRegisterStatus();
        String auditRemark = ao.getAuditRemark();

        // 驳回时审核意见必填
        if (Integer.valueOf(2).equals(registerStatus) && StrUtil.isBlank(auditRemark)) {
            return JsonResult.buildError("驳回时审核意见必填");
        }

        // 查询学生信息
        AsStudentEntity student = this.getById(studentId);
        if (student == null || !Integer.valueOf(1).equals(student.getIsValid())) {
            return JsonResult.buildError("学生信息不存在");
        }

        // 校验家长绑定关系：稳定身份跨学期保持不变
        if (student.getStudentIdentityId() == null) {
            return JsonResult.buildError("学生稳定身份不存在");
        }
        LambdaQueryWrapper<AsGuardianStudentEntity> gsWrapper = new LambdaQueryWrapper<>();
        gsWrapper.eq(AsGuardianStudentEntity::getStudentIdentityId,
                        student.getStudentIdentityId())
                .eq(AsGuardianStudentEntity::getPhone, parentPhone)
                .eq(AsGuardianStudentEntity::getIsValid, 1)
                .eq(AsGuardianStudentEntity::getBindStatus, 1);
        AsGuardianStudentEntity guardianStudent = asGuardianStudentService.getOne(gsWrapper);
        if (guardianStudent == null) {
            return JsonResult.buildError("未找到该学生与家长的绑定关系");
        }

        // 校验家长用户账号是否存在
        Long guardianUserId = guardianStudent.getGuardianUserId();
        if (guardianUserId == null) {
            return JsonResult.buildError("家长未关联用户账号");
        }
        LambdaQueryWrapper<TBaseUserEntity> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(TBaseUserEntity::getId, guardianUserId)
                .eq(TBaseUserEntity::getIsValid, 1);
        TBaseUserEntity user = tbaseUserService.getOne(userWrapper);
        if (user == null) {
            return JsonResult.buildError("家长关联的用户账号不存在");
        }

        // 2. 校验审核状态是否已通过
        if (Integer.valueOf(1).equals(student.getRegisterStatus()) && Integer.valueOf(1).equals(user.getApproveStatus())) {
            return JsonResult.buildError("已审核通过");
        }else if (!Integer.valueOf(3).equals(student.getRegisterStatus()) && !Integer.valueOf(0).equals(user.getApproveStatus())) {
            return JsonResult.buildError("状态未处于审核中，无法进行审核");
        }

        // 3. 先更新学生表 registerStatus
        student.setRegisterStatus(registerStatus);
        student.setAuditRemark(auditRemark);
        student.setUpdateTime(new Date());
        student.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean studentUpdated = this.updateById(student);
        if (!studentUpdated) {
            return JsonResult.buildError("更新学生审核状态失败");
        }

        // 再更新 t_base_user 审批状态
        user.setApproveStatus(registerStatus);
        user.setUpdateTime(new Date());
        user.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean userUpdated = tbaseUserService.updateById(user);
        log.info("call tbaseUserService.updateById(user) result:{}", userUpdated);

        syncPendingReviewSnapshots(student, registerStatus, auditRemark);
        if (RegisterStatusEnum.APPROVED.getCode().equals(registerStatus)) {
            serviceEnrollmentNoticeSupport.notifyOpenServicesForIdentity(
                    student.getSchoolId(), student.getStudentIdentityId());
        }
        log.info("学生家长审核完成，studentId:{}, parentPhone:{}, registerStatus:{}, auditRemark:{}",
                studentId, parentPhone, registerStatus, auditRemark);
        return JsonResult.buildSuccess("审核成功");
    }

    /**
     * 将审核结果同步到同一稳定身份尚未产生业务的未来学期待审核快照。
     */
    private void syncPendingReviewSnapshots(AsStudentEntity auditedStudent,
                                            Integer registerStatus,
                                            String auditRemark) {
        if (auditedStudent.getStudentIdentityId() == null
                || auditedStudent.getTermId() == null) {
            return;
        }
        EduclTermEntity sourceTerm = educlTermService.getById(auditedStudent.getTermId());
        if (sourceTerm == null || sourceTerm.getStartDate() == null) {
            return;
        }
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getSchoolId, auditedStudent.getSchoolId())
                .eq(AsStudentEntity::getStudentIdentityId,
                        auditedStudent.getStudentIdentityId())
                .eq(AsStudentEntity::getRegisterStatus,
                        RegisterStatusEnum.APR_ING.getCode())
                .eq(AsStudentEntity::getIsValid, 1)
                .ne(AsStudentEntity::getId, auditedStudent.getId());
        List<AsStudentEntity> candidates = this.list(wrapper);
        if (CollectionUtil.isEmpty(candidates)) {
            return;
        }
        Long operatorId = SmartRequestTokenUtil.getRequestUserId();
        for (AsStudentEntity candidate : candidates) {
            EduclTermEntity candidateTerm = educlTermService.getById(candidate.getTermId());
            if (candidateTerm == null || candidateTerm.getStartDate() == null
                    || !candidateTerm.getStartDate().after(sourceTerm.getStartDate())
                    || studentBusinessGuard.hasStudentBusiness(candidate.getId())) {
                continue;
            }
            AsStudentEntity update = new AsStudentEntity();
            update.setId(candidate.getId());
            update.setRegisterStatus(registerStatus);
            update.setAuditRemark(auditRemark);
            update.setUpdateUser(operatorId);
            update.setUpdateTime(new Date());
            if (!this.updateById(update)) {
                throw new BaseBusinessException(
                        ResultCode.STU_MANAGER_ERROR.code(), "未来学期学生审核状态同步失败");
            }
        }
    }

    @Override
    public AsTeacherAuditSummaryVO queryStudentAuditSummary(Long schoolId, Long termId) {
        AsTeacherAuditSummaryVO summary = new AsTeacherAuditSummaryVO();

        List<Map<String, Object>> rows = baseMapper.selectStudentAuditSummary(schoolId, termId);
        long allCount = 0L;
        if (CollectionUtil.isNotEmpty(rows)) {
            for (Map<String, Object> row : rows) {
                Integer status = row.get("registerStatus") != null
                        ? Integer.parseInt(row.get("registerStatus").toString()) : null;
                Long cnt = row.get("cnt") != null
                        ? Long.parseLong(row.get("cnt").toString()) : 0L;
                if (status == null) continue;
                allCount += cnt;
                switch (status) {
                    case 0: summary.setPendingRegisterCount(cnt); break;
                    case 1: summary.setApprovedCount(cnt);        break;
                    case 2: summary.setRejectedCount(cnt);        break;
                    case 3: summary.setPendingAuditCount(cnt);    break;
                    default: break;
                }
            }
        }
        summary.setAllCount(allCount);

        log.info("查询教师审核状态汇总，schoolId:{}, summary:{}", schoolId, summary);
        return summary;
    }


    @Override
    public PageResultDTO<AsStudentLearningAchievementVO> pageLearningAchievement(AsStudentLearningAchievementQueryAO qo) {
        Page<Object> pageParam = new Page<>(qo.getPageIndex(), qo.getPageSize());
        Page<AsStudentLearningAchievementVO> page = baseMapper.pageLearningAchievement(pageParam, qo);

        if(CollectionUtil.isNotEmpty(page.getRecords())){
            Map<Long, String> termNameMap = studentTermScopeSupport.listTermNames(
                    page.getRecords().stream()
                            .map(AsStudentLearningAchievementVO::getTermId)
                            .collect(Collectors.toList()));
            page.getRecords().forEach(vo -> {
                // 查询年级和班级
                if(CheckParamUtil.checkLongIdParam(vo.getGradeId())){
                    EduclGradeEntity gradeEntity = educlGradeService.getById(vo.getGradeId());
                    vo.setGradeName(gradeEntity != null ? gradeEntity.getGradeName():"");
                }
                if(CheckParamUtil.checkLongIdParam(vo.getClassId())){
                    TEduclClass classEntity = educlClassService.getById(vo.getClassId());
                    vo.setClassName(classEntity != null ? classEntity.getClassName():"");
                }
                vo.setTermName(termNameMap.get(vo.getTermId()));
                // 拼接日期
                if(null != vo.getSessionDate() && null != vo.getStartTime()){
                    vo.setClassResTime(DateUtils.format(vo.getSessionDate(), DateUtils.YMD_DASH_WITH_DATE) + " " + vo.getStartTime()+"-"+vo.getEndTime());
                }

            });
        }
        PageResultDTO<AsStudentLearningAchievementVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    @Override
    public void exportLearningAchievement(AsStudentLearningAchievementQueryAO qo, HttpServletResponse response) {
        try {
            AsStudentLearningAchievementQueryAO exportQuery = new AsStudentLearningAchievementQueryAO();
            SmartBeanUtil.copyProperties(qo,exportQuery);
            exportQuery.setStudentKeyword(qo.getStudentKeyword());

            Page<Object> pageParam = new Page<>(qo.getPageIndex(), qo.getPageSize());
            Page<AsStudentLearningAchievementVO> page = baseMapper.pageLearningAchievement(pageParam, exportQuery);

            if (CollectionUtil.isEmpty(page.getRecords())) {
                return;
            }

            // 转换为导出 Excel 模型
            List<AsLearningAchievementExportExcel> excelList = new ArrayList<>();
            Map<Long, String> termNameMap = studentTermScopeSupport.listTermNames(
                    page.getRecords().stream()
                            .map(AsStudentLearningAchievementVO::getTermId)
                            .collect(Collectors.toList()));
            int index = 1;
            for (AsStudentLearningAchievementVO vo : page.getRecords()) {
                AsLearningAchievementExportExcel excel = new AsLearningAchievementExportExcel();
                excel.setSequence(String.valueOf(index++));
                excel.setServiceName(vo.getServiceName());
                excel.setCourseClassName(vo.getCourseClassName());
                excel.setStudentNo(vo.getStudentNo());
                excel.setStudentName(vo.getStudentName());
                excel.setSessionDate(vo.getSessionDate());
                excel.setTeacherName(vo.getTeacherName());
                excel.setIsCheckInDesc(vo.getIsCheckIn() != null && vo.getIsCheckIn() == 1 ? "是" : "否");
                excel.setTermName(termNameMap.get(vo.getTermId()));
                excelList.add(excel);
            }

            // 写出 Excel
            ExcelUtils.exportExcel(response, excelList, AsLearningAchievementExportExcel.class, "学生学习成果", "学生学习成果");
        } catch (Exception e) {
            log.error("导出学生学习成果异常", e);
        }
    }

    /**
     * 在指定业务学期校验家长注册学生状态。
     */
    @Override
    public AsStudentEntity registerParentStudentCheckExist(
            Long schoolId, String studentName, String studentNo, Long termId) {
        studentTermScopeSupport.requireTermInSchool(schoolId, termId);
        LambdaQueryWrapper<AsStudentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, termId)
                .eq(AsStudentEntity::getStudentName, studentName)
                .eq(AsStudentEntity::getStudentNo, studentNo)
                .eq(AsStudentEntity::getIsValid, 1)
                .last("limit 1");
        List<AsStudentEntity> studentList = baseMapper.selectList(queryWrapper);
        return studentList.isEmpty() ? null : studentList.get(0);
    }

    @Override
    public List<AsStudentVO> queryAsStudentList(AsStudentQueryListAO qo) {
        Long schoolId = qo.getSchoolId();
        if (!CheckParamUtil.checkLongIdParam(schoolId)) {
            schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
            qo.setSchoolId(schoolId);
        }
        EduclTermEntity term = qo.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(schoolId)
                : studentTermScopeSupport.requireTermInSchool(schoolId, qo.getTermId());
        qo.setTermId(term.getId());
        List<AsStudentVO> asStudentVOList = new ArrayList<>();
        LambdaQueryWrapper<AsStudentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AsStudentEntity::getSchoolId, qo.getSchoolId())
                .eq(qo.getTermId() != null, AsStudentEntity::getTermId, qo.getTermId())
                .eq(BaseEntity::getIsValid,1)
                .eq(AsStudentEntity::getRegisterStatus,1)
                .eq(AsStudentEntity::getSchoolRollStatus,1);
        List<AsStudentEntity> studentList = baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(studentList)) {
            Map<Long, String> termNameMap = studentTermScopeSupport.listTermNames(
                    studentList.stream().map(AsStudentEntity::getTermId).collect(Collectors.toSet()));
            studentList.forEach(student -> {
                AsStudentVO asStudentVO = new AsStudentVO();
                SmartBeanUtil.copyProperties(student, asStudentVO);
                asStudentVO.setTermName(termNameMap.get(student.getTermId()));
                asStudentVOList.add(asStudentVO);
            });
        }
        return asStudentVOList;
    }

    /**
     * 统一学生列表和导出的学校、学期查询范围。
     */
    private void normalizeStudentQueryScope(AsStudentQueryAO query) {
        Long schoolId = query.getSchoolId();
        if (!CheckParamUtil.checkLongIdParam(schoolId)) {
            schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
            query.setSchoolId(schoolId);
        }
        EduclTermEntity term = query.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(schoolId)
                : studentTermScopeSupport.requireTermInSchool(schoolId, query.getTermId());
        if(null != term)query.setTermId(term.getId());
    }


    @Override
    public Long getStuIdByTermAndIdentifyId(Long termId,Long schoolId, Long studentIdentityId) {
        LambdaQueryWrapper<AsStudentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AsStudentEntity::getStudentIdentityId, studentIdentityId)
                .eq(AsStudentEntity::getRegisterStatus, 1)
                .eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getStudentIdentityId, studentIdentityId)
                .eq(AsStudentEntity::getTermId, termId).last("limit 1");
        List<AsStudentEntity> studentEntities = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(studentEntities)){
            return studentEntities.get(0).getId();
        }
        return null;
    }


    private Boolean updateStudentForStudentIdentId(Long studentId,Long stuentIdentId){
        LambdaUpdateWrapper<AsStudentEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AsStudentEntity::getId, studentId)
                .set(AsStudentEntity::getStudentIdentityId, stuentIdentId);
        Boolean uptRow = this.update(updateWrapper);
        log.info("call updateStudentForStudentIdentId in studentId:{}, stuentIdentId:{}, exec result:{}", studentId,stuentIdentId, uptRow);
        return uptRow;
    }

    @Override
    public List<AsQueryStudentNoByGradeClassVO> queryStudentNoByGradeClass(AsStudentNoByGradeClassAO qo) {
        LambdaQueryWrapper<AsStudentEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<AsQueryStudentNoByGradeClassVO> asQueryStudentNoByGradeClassVOList = new ArrayList<>();
        String stuNoQuery = "";
        String gradeNo = AssembleUtil.getGradeNoToName(qo.getGradeName());
        String classNo = "";
        if(qo.getSchoolId() == 6){// 育新小学
            classNo = qo.getClassName();
            if(StringUtils.isNotBlank(qo.getStudentNoKeyword())){
                stuNoQuery = gradeNo + classNo + qo.getStudentNoKeyword();
            }else{
                stuNoQuery = gradeNo + classNo;
            }
        }else{
            // 2601班截取为01
            classNo = qo.getClassName().replaceAll("班","").substring(2,4);
            String year = qo.getClassName().substring(0,2);
            if(StringUtils.isNotBlank(qo.getStudentNoKeyword())){
                stuNoQuery = "20" + year + classNo + qo.getStudentNoKeyword();
            }else{
                stuNoQuery = "20" + year + classNo;
            }
        }
        queryWrapper.eq(AsStudentEntity::getSchoolId, qo.getSchoolId())
                .like(AsStudentEntity::getStudentNo, stuNoQuery)
                .eq(AsStudentEntity::getIsValid, 1)
                .orderByDesc(AsStudentEntity::getStudentNo).last("limit 50");
        List<AsStudentEntity> studentEntities = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(studentEntities)){
            asQueryStudentNoByGradeClassVOList = SmartBeanUtil.copyList(studentEntities, AsQueryStudentNoByGradeClassVO.class);
        }
        return asQueryStudentNoByGradeClassVOList;
    }
}
