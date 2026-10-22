package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.base.enums.RoleEnum;
import com.sxkj.edu.common.base.enums.UserTypeEnum;
import com.sxkj.edu.common.config.GrobProperties;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.*;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.ao.AsTeacherStudentQueryAO;
import com.sxkj.edu.domain.vo.AsTeacherCourseStudentVO;
import com.sxkj.edu.module.afterschool.domain.ao.AsTeacherListQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsTeacherPersonEditAO;
import com.sxkj.edu.module.afterschool.domain.dto.AddTeacherRegisterAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherAuditAO;
import com.sxkj.edu.module.afterschool.domain.dto.TeacherRegisterAO;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.excel.AsTeacherExportExcel;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.enums.ApproveStatusEnum;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsEnrollmentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsTeacherMapper;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherQueryAO;
import com.sxkj.edu.module.afterschool.domain.excel.AsTeacherExcel;
import com.sxkj.edu.module.afterschool.service.AsTeacherClassService;
import com.sxkj.edu.module.afterschool.service.AsTeacherService;
import com.sxkj.edu.module.afterschool.service.EduclClassService;
import com.sxkj.edu.module.afterschool.service.EduclGradeService;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.system.employee.TbaseUserService;
import com.sxkj.edu.module.system.employee.domain.bo.EmployeeBO;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import com.sxkj.edu.module.system.role.roleemployee.RoleUserService;
import com.sxkj.edu.service.AsTeacherSubjectService;
import com.sxkj.edu.service.BaseFileService;
import com.sxkj.edu.service.SchoolManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 教师档案 Service 实现类
 *
 * @author system
 * @date 2026-05-19
 */
@Service
@Slf4j
public class AsTeacherServiceImpl extends ServiceImpl<AsTeacherMapper, AsTeacherEntity> implements AsTeacherService {

    @Resource
    private EduclGradeService educlGradeService;

    @Resource
    private SchoolManageService schoolManageService;

    @Resource
    private AsTeacherClassService asTeacherClassService;
    @Resource
    private EduclClassService educlClassService;
    @Resource
    private TbaseUserService tbaseUserService;
    @Resource
    private BaseFileService baseFileService;
    @Resource
    private AsCourseClassMapper asCourseClassMapper;

    @Resource
    private AsTeacherSubjectService asTeacherSubjectService;

    @Resource
    private AsTeacherService asTeacherService;

    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;

    @Resource
    private AsStudentMapper asStudentMapper;

    @Resource
    private RoleUserService roleUserService;

    @Value("${base.defaultPassword}")
    private String defaultPassword;

    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;


    // ======================== 通用私有方法 ========================

    /**
     * 校验同一学校下教师姓名+手机号是否已存在
     */
    private boolean existsByNameAndPhone(Long schoolId, String teacherName, String phone, Long excludeId) {
        LambdaQueryWrapper<AsTeacherEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsTeacherEntity::getIsValid, 1)
         .eq(AsTeacherEntity::getSchoolId, schoolId)
         .eq(AsTeacherEntity::getPhone, phone)
                .ne(Args.isNotNull(excludeId),AsTeacherEntity::getId, excludeId);

        return this.count(w) > 0;
    }

    /**
     * Entity -> VO
     */
    private AsTeacherVO toVO(AsTeacherEntity entity) {
        if (entity == null) {
            return null;
        }
        AsTeacherVO vo = new AsTeacherVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    // ======================== 对外方法 ========================
    /**
     * 构建分页结果DTO（提取公共逻辑，避免重复代码）
     */
    private <T> PageResultDTO<T> buildPageResultDTO(Page<?> page) {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        return result;
    }

    /**
     * 批量填充学校名称（复用SchoolManageService已有的批量查询方法）
     * 将N次单条查询优化为1次批量查询
     */
    private void fillSchoolNames(List<AsTeacherVO> list) {
        List<Long> schoolIds = list.stream()
                .map(AsTeacherVO::getSchoolId)
                .filter(CheckParamUtil::checkLongIdParam)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(schoolIds)) {
            return;
        }
        Map<Long, String> schoolNameMap = schoolManageService.getSchoolNameById(schoolIds);
        list.forEach(vo -> {
            if (CheckParamUtil.checkLongIdParam(vo.getSchoolId())) {
                vo.setSchoolName(schoolNameMap.getOrDefault(vo.getSchoolId(), ""));
            }
        });
    }

    /**
     * 批量校验教师审核状态：当教师registerStatus=已通过时，
     * 检查其关联的用户账号是否存在待审核(approve_status=0)的情况，
     * 若存在则将registerStatus覆盖为待审核状态
     */
    private void batchCheckAuditStatus(List<AsTeacherVO> list) {
        List<String> approvedPhones = list.stream()
                .filter(vo -> Objects.equals(vo.getRegisterStatus(), ApproveStatusEnum.APPROVED.getCode()))
                .map(AsTeacherVO::getPhone)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(approvedPhones)) {
            return;
        }
        Long schoolId = list.get(0).getSchoolId();
        Set<String> pendingPhones = new HashSet<>(baseMapper.selectPendingAuditPhones(schoolId, approvedPhones));
        if (CollectionUtil.isNotEmpty(pendingPhones)) {
            list.forEach(vo -> {
                if (Objects.equals(vo.getRegisterStatus(), ApproveStatusEnum.APPROVED.getCode())
                        && pendingPhones.contains(vo.getPhone())) {
                    vo.setRegisterStatus(ApproveStatusEnum.PENDING.getCode());
                }
            });
        }
    }

    /**
     * 批量填充教师教学科目（将N次单条查询优化为1次批量查询）
     */
    private void fillTeacherSubjects(List<AsTeacherVO> list) {
        List<Long> teacherIds = list.stream()
                .map(AsTeacherVO::getId)
                .filter(CheckParamUtil::checkLongIdParam)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(teacherIds)) {
            return;
        }
        Map<Long, List<AsSubjectVO>> subjectMap = asTeacherSubjectService.listSubjectsByTeacherIds(teacherIds);
        list.forEach(vo -> {
            List<AsSubjectVO> subjectList = subjectMap.get(vo.getId());
            if (CollectionUtil.isNotEmpty(subjectList)) {
                vo.setSubjectList(subjectList);
                String subjectNames = subjectList.stream()
                        .map(s -> {
                            String gradeName = s.getGradeName() != null ? s.getGradeName() : "";
                            String subjectName = s.getSubjectName() != null ? s.getSubjectName() : "";
                            return gradeName + subjectName;
                        })
                        .filter(name -> !name.isEmpty())
                        .distinct()
                        .collect(Collectors.joining(","));
                vo.setSubjectNames(subjectNames);
            }
        });
    }

    // ======================== 对外方法 ========================

    @Override
    public PageResultDTO<AsTeacherVO> queryAsTeacherPage(AsTeacherQueryAO qo) {
        // 1. 分页主查询（XML多表关联，已包含user/class/grade信息）
        Page<Object> pageParam = new Page<>(qo.getPageIndex(), qo.getPageSize());
        Integer userStatus = AssembleUtil.getUserStatusByRegistStatus(qo.getRegisterStatus());
        qo.setUserStatus(userStatus);
        Page<AsTeacherVO> page = baseMapper.pageTeacherWithUser(pageParam, qo);

        PageResultDTO<AsTeacherVO> result = buildPageResultDTO(page);
        List<AsTeacherVO> list = page.getRecords();
        if (CollectionUtil.isEmpty(list)) {
            return result;
        }

        // 2. 批量填充学校名称（1次DB查询代替N次）
        fillSchoolNames(list);

        // 3. 批量校验教师审核状态（1次DB查询代替N次）
        batchCheckAuditStatus(list);

        // 4. 批量填充教师教学科目（1次DB查询代替N次）
        fillTeacherSubjects(list);

        result.setList(list);
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateAsTeacher(AsTeacherAddAO ao) {
        JsonResult<String> result = JsonResult.buildError(ResultCode.STU_MANAGER_ERROR.code(),"教师管理更新失败");

        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        if (ao.getId() == null) {
            return JsonResult.buildError("教师ID不能为空");
        }

        AsTeacherEntity exist = this.getById(ao.getId());
        if (exist == null || exist.getIsValid() != 1) {
            return JsonResult.buildError("教师记录不存在");
        }

        if (existsByNameAndPhone(schoolId, ao.getTeacherName(), ao.getPhone(), ao.getId())) {
            return JsonResult.buildError("该学校下已存在相同姓名和手机号的教师");
        }
        AsTeacherEntity entity = new AsTeacherEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setUpdateTime(new Date());
        Boolean uptRow = this.updateById(entity);
        if(uptRow){
            // 同步教师与学科的关联关系
            asTeacherSubjectService.saveTeacherSubjects(schoolId, ao.getId(), ao.getSubjectIds());
            // 教师任课班级关系绑定
            if(CollectionUtil.isNotEmpty(ao.getClassIdList())){
                asTeacherClassService.createTeacherBindClass(schoolId, ao.getClassIdList(),entity.getId(),2);
                log.info("教师任课班级关系绑定成功，教师ID：{}", entity.getId());
            }
            result = JsonResult.buildSuccess("操作成功");
        }
        return result;
    }

    @Override
    public JsonResult<String> deleteAsTeacher(Long id) {
        JsonResult<String> result = JsonResult.buildError(ResultCode.STU_MANAGER_ERROR.code(),"删除教师失败");
        AsTeacherEntity entity = this.getById(id);
        if (entity == null || entity.getIsValid() != 1) {
            return JsonResult.buildError("教师记录不存在");
        }

        entity.setIsValid(0);
        entity.setUpdateTime(new Date());
        Boolean uptRow = this.updateById(entity);
        if(uptRow){
            result = JsonResult.buildSuccess("操作成功");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> importAsTeacher(Long schoolId, MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            List<AsTeacherExcel> list = ExcelUtils.readExcel(is, AsTeacherExcel.class);
            if (list == null || list.isEmpty()) {
                return JsonResult.buildError("导入数据为空");
            }
            List<String> errors = new ArrayList<>();
            int row = 1;
            Long termId = null;
            EduclTermEntity termEntity = studentTermScopeSupport.resolveReadableTerm(schoolId);
            if(null != termEntity){
                termId = termEntity.getId();
            }
            for (AsTeacherExcel excel : list) {
                String phone = excel.getPhone();
                Integer isClassHead = excel.getIsHeadTeacher();
                if (StrUtil.isBlank(excel.getTeacherName())) {
                    errors.add("第" + row + "行: 教师姓名不能为空");
                    row++;
                    continue;
                }
                if (StrUtil.isBlank(excel.getPhone()) || null == excel.getIsHeadTeacher()) {
                    errors.add("第" + row + "行: 手机号不能为空或教师必填信息未填写");
                    row++;
                    continue;
                }

                if (existsByNameAndPhone(schoolId, excel.getTeacherName(), phone, null)) {
                    errors.add("第" + row + "行: 教师[" + excel.getTeacherName() + "/" + phone + "]已存在，跳过");
                    row++;
                    continue;
                }
                AsTeacherEntity entity = new AsTeacherEntity();
                entity.setSchoolId(schoolId);
                entity.setTeacherName(excel.getTeacherName());
                entity.setPhone(phone);
                entity.setTeacherNo(excel.getTeacherNo());
                entity.setGender(AssembleUtil.getStuSexDetail(excel.getGender()));
                entity.setEmployeeStatus(1);
                entity.setIsHeadTeacher(excel.getIsHeadTeacher() != null ? excel.getIsHeadTeacher() : 0);
                entity.setRegisterStatus(0);
                entity.setIsValid(1);
                entity.setCreateTime(new Date());
                entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                Boolean uptRow = this.save(entity);
                if(uptRow){
                    log.info("call saveAsTeacherImport schoolId:{},teacherName:{},phone:{},result:{}", schoolId, excel.getTeacherName(), excel.getPhone(), uptRow);
                    // 保存教师班级
//                    asTeacherClassService.createTeacherBindClass(schoolId, classId,entity.getId(),excel.getIsHeadTeacher());
                    // 最后在更新班级的班主任id
//                    if(isClassHead != null && isClassHead == 1){
//                        educlClassService.updateClassTeacherId(classId,entity.getId());
//                    }
                }
                row++;
            }
            if (!errors.isEmpty()) {
                return JsonResult.buildError("部分数据导入失败：" + String.join("；", errors));
            }
            return JsonResult.buildSuccess("导入成功");
        } catch (Exception e) {
            log.error("教师导入异常", e);
            return JsonResult.buildError("导入异常：" + e.getMessage());
        }
    }

    @Override
    public void exportAsTeacher(AsTeacherQueryAO qo, HttpServletResponse response) {
        List<AsTeacherVO> teacherList = queryAsTeacherPage(qo).getList();
        if(CollectionUtil.isEmpty(teacherList)) return;

        List<AsTeacherExportExcel> excelList = teacherList.stream().map(e -> {
            AsTeacherExportExcel ex = new AsTeacherExportExcel();
            BeanUtils.copyProperties(e, ex);
            ex.setGender(AssembleUtil.getStuSexForInteger(e.getGender()));
            ex.setIsHeadTeacher(e.getIsHeadTeacher() != null && e.getIsHeadTeacher() == 1 ? "是" : "否");
            ex.setStatus(e.getEmployeeStatus() != null && e.getEmployeeStatus() == 1 ? "在职" : "离职");
            ex.setAccountStatus(e.getRegisterStatus() != null && e.getRegisterStatus() == 1 ? "已注册" : "待注册");
            return ex;
        }).collect(Collectors.toList());
        ExcelUtils.exportExcel(response, excelList, AsTeacherExportExcel.class, "教师档案导出", "教师档案导出");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> registerTeacher(TeacherRegisterAO ao) {
        log.info("call registerTeacher in request:{}", ao);
        // 校验密码
        if (!ao.getPassword().equals(ao.getConfirmPassword())) {
            return JsonResult.buildError("两次输入的密码不一致");
        }
        if(ao.getIsHeadTeacher() != null && ao.getIsHeadTeacher() == 1) {
            if(!CheckParamUtil.checkLongIdParam(ao.getGradeId()) || !CheckParamUtil.checkLongIdParam(ao.getClassId())){
                return JsonResult.buildError("班主任身份时，请选择年级和班级");
            }
        }
        // 验证/保存学校
        if(!CheckParamUtil.checkLongIdParam(ao.getSchoolId()) && StringUtils.isNotBlank(ao.getSchoolName())){
            Long schoolId = schoolManageService.registSchoolManageByName(ao.getSchoolName());
            if(schoolId == null){
                return JsonResult.buildError("学校不存在");
            }
            ao.setSchoolId(schoolId);
        }
        // 查询教师用户是否已注册
        EmployeeDTO user = tbaseUserService.queryTeacherUserByPhoneForRegister(ao.getPhone(), ao.getSchoolId());
        if (user != null && (user.getApproveStatus() == 1 || user.getApproveStatus() == 0)) {
            return JsonResult.buildError("用户已注册或已处于审核中，请勿重复注册");
        }

        // 查询教师是否已存在（根据学校ID、姓名、手机号）
        LambdaQueryWrapper<AsTeacherEntity> teacherWrapper = new LambdaQueryWrapper<>();
        teacherWrapper.eq(AsTeacherEntity::getSchoolId, ao.getSchoolId())
                .eq(AsTeacherEntity::getTeacherName, ao.getTeacherName())
                .eq(AsTeacherEntity::getPhone, ao.getPhone())
                .eq(AsTeacherEntity::getIsValid, 1);
        AsTeacherEntity teacher = this.getOne(teacherWrapper);
        boolean teacherExists = false;
        boolean teacherDataExists = false;
        if(null != teacher && (teacher.getRegisterStatus() == 1 || teacher.getRegisterStatus() == 3)) {
            return JsonResult.buildError("教师处于已注册或待审核，请勿重复操作");
        }else if(teacher != null && teacher.getRegisterStatus() == 1){
            teacherExists = true;
        }


        // 保存或更新教师信息
        if (null != teacher) {
            teacherDataExists = true;
            // 更新现有教师信息
            teacher.setGender(ao.getGender());
            teacher.setIsHeadTeacher(ao.getIsHeadTeacher());
            teacher.setPositionName(ao.getPositionName());
            teacher.setIdCard(ao.getIdCard());
            teacher.setTeacherNo(ao.getTeacherNo());
            if(teacher.getRegisterStatus() == 0){// 待注册
                teacher.setRegisterStatus(1); // 已注册
                teacherExists = true;
            }else if(teacher.getRegisterStatus() == 2){// 已驳回
                teacher.setRegisterStatus(3); // 待审核
            }
            teacher.setUpdateUser(SmartRequestTokenUtil.getRequestUserId() == null ? 1L : SmartRequestTokenUtil.getRequestUserId());
            teacher.setUpdateTime(new Date());
            if(CheckParamUtil.checkLongIdParam(ao.getAvatarFileId())){
                teacher.setAvatarFileId(ao.getAvatarFileId());
            }else{
                teacher.setAvatarFileId(GrobProperties.getInstance().womanDefUrlId);
            }
            teacher.setPoliticalStatus(ao.getPoliticalStatus());
        } else {
            // 新增教师
            teacher = new AsTeacherEntity();
            teacher.setSchoolId(ao.getSchoolId());
            SmartBeanUtil.copyProperties(ao,teacher);

            teacher.setRegisterStatus(3);
            teacher.setEmployeeStatus(1); // 在职
            teacher.setIsValid(1);
            teacher.setCreateTime(new Date());
            teacher.setCreateUser(SmartRequestTokenUtil.getRequestUserId() == null ? 1L : SmartRequestTokenUtil.getRequestUserId());
            if(CheckParamUtil.checkLongIdParam(ao.getAvatarFileId())){
                teacher.setAvatarFileId(ao.getAvatarFileId());
            }else{
                teacher.setAvatarFileId(GrobProperties.getInstance().womanDefUrlId);
            }
        }

        // 保存用户信息
        Long userId = tbaseUserService.createBaseUserByTeacher(ao, teacherExists,0);
        if(CheckParamUtil.checkLongIdParam(userId)){
            // 更新教师的userId
            teacher.setUserId(userId);
            Boolean uptTea = false;
            if(teacherDataExists){
                uptTea = this.updateById(teacher);
            }else{
                uptTea = this.save(teacher);
            }
            // 如果是班主任，同步更新教师班级信息（班级存在时才更新班主任id）
            if (ao.getIsHeadTeacher() != null && ao.getIsHeadTeacher() == 1 && CheckParamUtil.checkLongIdParam(ao.getClassId())) {
                TEduclClass clazz = educlClassService.getById(ao.getClassId());
                if (clazz != null && clazz.getIsValid() == 1) {
                    educlClassService.updateClassTeacherId(ao.getClassId(), teacher.getId(),ao.getSchoolId());
                    asTeacherClassService.syncHeadTeacherClass(ao.getClassId(), teacher.getId(), ao.getSchoolId());
                    log.info("班主任注册，已更新班级班主任：classId={}, teacherId={}", ao.getClassId(), teacher.getId());
                }
            }

            log.info("教师注册成功：schoolId={}, teacherName={}, phone={}, uptTea={}",
                    ao.getSchoolId(), ao.getTeacherName(), ao.getPhone(), uptTea);
            return JsonResult.buildSuccess("注册成功");
        }
        return JsonResult.buildSuccess("教师注册失败");
    }


    @Override
    public List<AsTeacherVO> queryList(AsTeacherListQueryAO qo) {
        LambdaQueryWrapper<AsTeacherEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsTeacherEntity::getIsValid, 1);
        w.eq(qo.getSchoolId() != null, AsTeacherEntity::getSchoolId, qo.getSchoolId());
        w.eq(AsTeacherEntity::getRegisterStatus, 1);
        w.like(StringUtils.isNotBlank(qo.getTeacherName()), AsTeacherEntity::getTeacherName, qo.getTeacherName());
        List<AsTeacherEntity> entities = this.list(w);
        return entities.stream().map(e -> {
            AsTeacherVO vo = new AsTeacherVO();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
    }


    @Override
    public Long queryTeacherIdByPhone(String teacherPhone,Long schoolId) {
        String phone = teacherPhone;
        if(StringUtils.isBlank(teacherPhone)){
            phone = SmartRequestTokenUtil.getRequestUser().getEmployeeBO().getPhone();
        }
        Long schId = schoolId;
        if(!CheckParamUtil.checkLongIdParam(schId)){
            schId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        }
        LambdaQueryWrapper<AsTeacherEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsTeacherEntity::getPhone, phone);
        w.eq(AsTeacherEntity::getSchoolId, schId);
        w.eq(AsTeacherEntity::getIsValid, 1);
        w.orderByDesc(AsTeacherEntity::getCreateTime).last("limit 1");
        List<AsTeacherEntity> entityList = this.list(w);
        return entityList != null && !entityList.isEmpty() ? entityList.get(0).getId() : 0L;
    }

    @Override
    public AsTeacherEntity queryTeacherIdByPhoneForEntity(String teacherPhone,Long schoolId) {
        LambdaQueryWrapper<AsTeacherEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsTeacherEntity::getPhone, teacherPhone);
        w.eq(AsTeacherEntity::getSchoolId, schoolId);
        w.eq(AsTeacherEntity::getIsValid, 1);
        w.orderByDesc(AsTeacherEntity::getCreateTime).last("limit 1");
        List<AsTeacherEntity> entityList = this.list(w);
        return entityList != null && !entityList.isEmpty() ? entityList.get(0) : null;
    }


    // ======================== 教师详情查询 ========================

    @Override
    public AsTeacherVO getTeacherDetail(Long schoolId, Long teacherId) {
        LambdaQueryWrapper<AsTeacherEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsTeacherEntity::getIsValid, 1)
                .eq(AsTeacherEntity::getId, teacherId);
        w.eq(AsTeacherEntity::getSchoolId, schoolId);
        AsTeacherEntity entity = this.getOne(w, false);
        if (entity == null) {
            return null;
        }
        AsTeacherVO vo = toVO(entity);
//        vo.setUserId(teacherId);
        // 学校名称
        if (CheckParamUtil.checkLongIdParam(vo.getSchoolId())) {
            SchoolManagePo schoolEntity = schoolManageService.getById(vo.getSchoolId());
            vo.setSchoolName(schoolEntity != null ? schoolEntity.getSchoolName() : "");
        }
        // 头像地址
        if (CheckParamUtil.checkLongIdParam(entity.getAvatarFileId())) {
            String avatarUrl = baseFileService.getFileUrlById(entity.getAvatarFileId());
            vo.setImgUrl(avatarUrl);
        }
        // 查询教师课程班名称集合
        List<String> courseClassNames = queryCourseClassNamesByTeacherId(teacherId);
        if (CollectionUtil.isNotEmpty(courseClassNames)) {
            vo.setCourseClassNames(String.join("、", courseClassNames));
        }

        // 根据教师id查询教学的科目
        List<AsSubjectVO> subjectList = asTeacherSubjectService.listSubjectByTeacherId(vo.getId());
        if (CollectionUtil.isNotEmpty(subjectList)) {
            vo.setSubjectList(subjectList);
            String subjectNames = subjectList.stream()
                    .map(s -> {
                        String gradeName = s.getGradeName() != null ? s.getGradeName() : "";
                        String subjectName = s.getSubjectName() != null ? s.getSubjectName() : "";
                        return gradeName + subjectName;
                    })
                    .filter(name -> !name.isEmpty())
                    .distinct()
                    .collect(Collectors.joining(","));
            vo.setSubjectNames(subjectNames);
        }

        // 通过任课教师绑定查询任课班级
        List<AsTeacherBindClassVO> teacherBindClassList = asTeacherClassService.queryTeacherBindClassList(teacherId, schoolId);
        if (CollectionUtil.isNotEmpty(teacherBindClassList)) {
            String classNames = teacherBindClassList.stream()
                    .map(AsTeacherBindClassVO::getClassName)
                    .filter(StrUtil::isNotBlank)
                    .distinct()
                    .collect(Collectors.joining(","));
            List<Long> classIdList = teacherBindClassList.stream()
                    .map(AsTeacherBindClassVO::getClassId)
                    .filter(CheckParamUtil::checkLongIdParam)
                    .distinct()
                    .collect(Collectors.toList());
            vo.setClassIdListStr(classNames);
            vo.setClassIdList(classIdList);
        }

        return vo;
    }

    @Override
    public List<String> queryCourseClassNamesByTeacherId(Long teacherId) {
        if (!CheckParamUtil.checkLongIdParam(teacherId)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsCourseClassEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsCourseClassEntity::getIsValid, 1)
                .eq(AsCourseClassEntity::getTeacherId, teacherId);
        List<AsCourseClassEntity> list = asCourseClassMapper.selectList(w);
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(AsCourseClassEntity::getCourseClassName)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> auditTeacher(AsTeacherAuditAO ao) {
        Long teacherId = ao.getId();
        Integer registerStatus = ao.getRegisterStatus();
        String auditRemark = ao.getAuditRemark();
        Integer userType = null;

        // 驳回时审核意见必填
        if (Integer.valueOf(2).equals(registerStatus) && StrUtil.isBlank(auditRemark)) {
            return JsonResult.buildError("驳回时审核意见必填");
        }

        // 1. 查询教师信息
        AsTeacherEntity teacher = this.getById(teacherId);
        if (teacher == null || !Integer.valueOf(1).equals(teacher.getIsValid())) {
            return JsonResult.buildError("教师信息不存在");
        }
        if(teacher.getIsHeadTeacher() == 1){
            userType = UserTypeEnum.LEADER.getValue();
        }else{
            userType = UserTypeEnum.TEACH.getValue();
        }

        // 校验教师用户id关联查询t_base_user是否已存在
        Long userId = teacher.getUserId();
        if (userId == null) {
            return JsonResult.buildError("教师未关联用户账号");
        }
        Boolean userCheck = tbaseUserService.getUserFinishPass(teacher.getSchoolId(),teacher.getPhone(),userType);

        // 2. 校验审核状态是否为待审核
        if (!Integer.valueOf(3).equals(teacher.getRegisterStatus()) && !userCheck) {
            return JsonResult.buildError("状态未处于审核中，无法进行审核");
        }

        // 3. 更新教师表 registerStatus
        teacher.setUpdateTime(new Date());
        teacher.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        teacher.setAuditRemark(auditRemark);
        if(teacher.getRegisterStatus() != 1){
            teacher.setRegisterStatus(registerStatus);
            this.updateById(teacher);
        }

        // 更新 t_base_user 审批状态（approve_status: 0-待审批 1-已通过 2-已驳回）
        boolean userUpdated = tbaseUserService.updateUserAuditStatusFinishPass(teacher.getSchoolId(),teacher.getPhone(),
                ao.getRegisterStatus(), userType);
        if (!userUpdated) {
            return JsonResult.buildError("更新用户审核状态失败");
        }

        log.info("教师审核完成，teacherId:{}, registerStatus:{}, auditRemark:{}", teacherId, registerStatus, auditRemark);
        return JsonResult.buildSuccess("审核成功");
    }

    // ======================== 教师个人信息查询/编辑 ========================

    @Override
    public AsTeacherPersonDetailVO queryTeacherPersonDetail(Long schoolId, Long userId) {
        // 1. 查询教师信息
        EmployeeBO userBo = SmartRequestTokenUtil.getRequestUser().getEmployeeBO();
        AsTeacherEntity teacher = queryTeacherIdByPhoneForEntity(userBo.getPhone(),schoolId);
        if (teacher == null) {
            return null;
        }

        // 2. 查询用户信息
        LambdaQueryWrapper<TBaseUserEntity> uw = new LambdaQueryWrapper<>();
        uw.eq(TBaseUserEntity::getId, userId)
                .eq(TBaseUserEntity::getIsValid, 1);
        TBaseUserEntity user = tbaseUserService.getOne(uw);

        // 3. 组装基本信息
        AsTeacherPersonDetailVO vo = new AsTeacherPersonDetailVO();
        vo.setTeacherId(teacher.getId());
        vo.setTeacherName(teacher.getTeacherName());
        vo.setPhone(teacher.getPhone());
        vo.setTeacherNo(teacher.getTeacherNo());
        vo.setGender(teacher.getGender());
        vo.setIsHeadTeacher(teacher.getIsHeadTeacher());
        vo.setPosition(teacher.getPositionName());
        vo.setSchoolStage(teacher.getSchoolStage());


        // 登录用户名
        if (user != null) {
            vo.setLoginName(user.getLoginName());
        }

        // 头像
        if (CheckParamUtil.checkLongIdParam(teacher.getAvatarFileId())) {
            String avatarUrl = baseFileService.getFileUrlById(teacher.getAvatarFileId());
            vo.setImgUrl(avatarUrl);
        } else if (user != null && StrUtil.isNotBlank(user.getLogoUrl())) {
            vo.setImgUrl(user.getLogoUrl());
        }

        // 学校名称
        if (CheckParamUtil.checkLongIdParam(schoolId)) {
            SchoolManagePo school = schoolManageService.getById(schoolId);
            vo.setSchoolName(school != null ? school.getSchoolName() : "");
        }

        // 4. 查询教师班级绑定关系
        LambdaQueryWrapper<AsTeacherClassEntity> tcw = new LambdaQueryWrapper<>();
        tcw.eq(AsTeacherClassEntity::getTeacherId, teacher.getId())
                .eq(AsTeacherClassEntity::getSchoolId, schoolId)
                .eq(AsTeacherClassEntity::getIsValid, 1);
        List<AsTeacherClassEntity> teacherClassList = asTeacherClassService.list(tcw);

        if (CollectionUtil.isNotEmpty(teacherClassList)) {
            List<Long> classIds = teacherClassList.stream()
                    .map(AsTeacherClassEntity::getClassId)
                    .filter(CheckParamUtil::checkLongIdParam)
                    .distinct()
                    .collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(classIds)) {
                // 查询班级信息
                LambdaQueryWrapper<TEduclClass> cw = new LambdaQueryWrapper<>();
                cw.in(TEduclClass::getId, classIds)
                        .eq(TEduclClass::getIsValid, 1);
                List<TEduclClass> classList = educlClassService.list(cw);

                if (CollectionUtil.isNotEmpty(classList)) {
                    // 班级名称
                    List<String> classNames = classList.stream()
                            .map(TEduclClass::getClassName)
                            .filter(StrUtil::isNotBlank)
                            .distinct()
                            .collect(Collectors.toList());
                    vo.setClassNames(classNames);

                    // 年级名称（通过班级的gradeId查询）
                    List<Long> gradeIds = classList.stream()
                            .map(TEduclClass::getGradeId)
                            .filter(CheckParamUtil::checkLongIdParam)
                            .distinct()
                            .collect(Collectors.toList());

                    if (CollectionUtil.isNotEmpty(gradeIds)) {
                        LambdaQueryWrapper<EduclGradeEntity> gw = new LambdaQueryWrapper<>();
                        gw.in(EduclGradeEntity::getId, gradeIds)
                                .eq(EduclGradeEntity::getIsValid, 1);
                        List<EduclGradeEntity> gradeList = educlGradeService.list(gw);
                        if (CollectionUtil.isNotEmpty(gradeList)) {
                            List<String> gradeNames = gradeList.stream()
                                    .map(EduclGradeEntity::getGradeName)
                                    .filter(StrUtil::isNotBlank)
                                    .distinct()
                                    .collect(Collectors.toList());
                            vo.setGradeNames(gradeNames);
                        }
                    }
                }
            }
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateTeacherPerson(AsTeacherPersonEditAO ao) {
        Long userId = SmartRequestTokenUtil.getRequestUserId();

        // a. 校验用户id是否同时存在于教师表和用户表
        LambdaQueryWrapper<AsTeacherEntity> tw = new LambdaQueryWrapper<>();
        tw.eq(AsTeacherEntity::getUserId, userId)
                .eq(AsTeacherEntity::getIsValid, 1);
        AsTeacherEntity teacher = this.getOne(tw, false);
        if (teacher == null) {
            return JsonResult.buildError("教师信息不存在");
        }

        LambdaQueryWrapper<TBaseUserEntity> uw = new LambdaQueryWrapper<>();
        uw.eq(TBaseUserEntity::getId, userId)
                .eq(TBaseUserEntity::getIsValid, 1);
        TBaseUserEntity user = tbaseUserService.getOne(uw);
        if (user == null) {
            return JsonResult.buildError("用户信息不存在");
        }

        // b. 修改时新的手机号+姓名在教师表中要确保唯一性
        LambdaQueryWrapper<AsTeacherEntity> uniqueWrapper = new LambdaQueryWrapper<>();
        uniqueWrapper.eq(AsTeacherEntity::getIsValid, 1)
                .eq(AsTeacherEntity::getSchoolId, teacher.getSchoolId())
                .eq(AsTeacherEntity::getTeacherName, teacher.getTeacherName())
                .eq(AsTeacherEntity::getPhone, ao.getPhone())
                .ne(AsTeacherEntity::getId, teacher.getId());
        if (this.count(uniqueWrapper) > 0) {
            return JsonResult.buildError("该学校下已存在相同姓名和手机号的教师");
        }

        // c. 同时更新教师表和用户表
        // 更新教师表
        teacher.setPhone(ao.getPhone());
        teacher.setIsHeadTeacher(ao.getIsHeadTeacher());
        teacher.setPositionName(ao.getPositionName());
        teacher.setAvatarFileId(ao.getAvatarFileId());
        teacher.setUpdateTime(new Date());
        teacher.setUpdateUser(userId);
        boolean teacherUpdated = this.updateById(teacher);
        if (!teacherUpdated) {
            return JsonResult.buildError("更新教师信息失败");
        }

        // 更新用户表
        user.setPhone(ao.getPhone());
        user.setUpdateTime(new Date());
        user.setUpdateUser(userId);
        boolean userUpdated = tbaseUserService.updateById(user);
        if (!userUpdated) {
            return JsonResult.buildError("更新用户信息失败");
        }

        log.info("教师个人信息编辑成功，userId:{}, teacherId:{}, phone:{}", userId, teacher.getId(), ao.getPhone());
        return JsonResult.buildSuccess("操作成功");
    }

    // ======================== 教师审核状态汇总 ========================

    @Override
    public AsTeacherAuditSummaryVO queryAuditSummary(Long schoolId) {
        AsTeacherAuditSummaryVO summary = new AsTeacherAuditSummaryVO();

        List<Map<String, Object>> rows = baseMapper.selectAuditSummary(schoolId);
        long allCount = 0L;
        if (CollectionUtil.isNotEmpty(rows)) {
            for (Map<String, Object> row : rows) {
                Integer status = row.get("registerStatus") != null
                        ? Integer.parseInt(row.get("registerStatus").toString()) : null;
                Long cnt = row.get("cnt") != null
                        ? Long.parseLong(row.get("cnt").toString()) : 0L;
                if (status == null) {
                    continue;
                }
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
    public List<AsTeacherCourseStudentVO> listTeacherCourseStudents(AsTeacherStudentQueryAO ao) {
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        Long teacherId = ao != null && ao.getTeacherId() != null
                ? ao.getTeacherId()
                : asTeacherService.queryTeacherIdByPhone(null, schoolId);
        if (teacherId == null) {
            return Collections.emptyList();
        }
        ao.setSchoolId(schoolId);
        return baseMapper.listTeacherCourseStudents(ao);
    }

}
