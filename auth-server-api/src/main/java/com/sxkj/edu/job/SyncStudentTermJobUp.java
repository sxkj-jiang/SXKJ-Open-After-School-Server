package com.sxkj.edu.job;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclGradeMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import com.sxkj.edu.module.afterschool.service.EduclClassService;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.service.SchoolManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *@Description : 同步学生学期信息
 *@Author : yh
 *@Date : 2026-8-6 15:37
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Component
@Slf4j
public class SyncStudentTermJobUp {
    @Autowired
    private SchoolManageService schoolManageService;

    @Autowired
    private EduclTermMapper educlTermMapper;

    @Autowired
    private AsStudentMapper asStudentMapper;
    @Autowired
    private EduclGradeMapper educlGradeMapper;
    @Autowired
    private EduclClassService educlClassService;

    @Scheduled(cron = "0 0 2 * 2,3,8,9 ?")
    public void syncStudentTerm() {
        log.info("同步学生学期信息-开始");
        // 1. 查询所有学校
        List<SchoolManagePo> schools = schoolManageService.list(
                new LambdaQueryWrapper<SchoolManagePo>().eq(SchoolManagePo::getIsValid, 1));
        if (CollectionUtil.isEmpty(schools)) return;
        for (SchoolManagePo school : schools) {
            try {
                processSchool(school);
            } catch (Exception e) {
                log.error("同步学生学期信息异常, schoolId={}", school.getId(), e);
            }
        }
        log.info("同步学生学期信息-结束");
    }

    private void processSchool(SchoolManagePo school) {
        Long schoolId = school.getId();
        Date now = new Date();
        // 判断当前时间是否有正在进行的学期
        LambdaQueryWrapper<EduclTermEntity> tw = new LambdaQueryWrapper<>();
        tw.eq(EduclTermEntity::getSchoolId, schoolId)
                .eq(EduclTermEntity::getIsValid, 1)
                .ge(EduclTermEntity::getStartDate, now)
                .le(EduclTermEntity::getEndDate, now)
                .eq(EduclTermEntity::getTermStatus, 1);
        if (!educlTermMapper.selectList(tw).isEmpty()) {
            return;
        }
        Date date = new Date(now.getTime() + TimeUnit.DAYS.toMillis(5));
        // 2. 查询学期开始时间与当前时间差值 <= 5天的学期（取第一条）
        LambdaQueryWrapper<EduclTermEntity> termWrapper = new LambdaQueryWrapper<>();
        termWrapper.eq(EduclTermEntity::getSchoolId, schoolId)
                .eq(EduclTermEntity::getIsValid, 1)
                .between(EduclTermEntity::getStartDate, now,date)
                .orderByAsc(EduclTermEntity::getStartDate)
                .last("LIMIT 1");
        EduclTermEntity term = educlTermMapper.selectOne(termWrapper);
        if (term == null) {
            log.info("学校[{}]未找到即将开始的学期", schoolId);
            return;
        }

        // 更新 term_status = 1
        if (term.getTermStatus() == null || term.getTermStatus() != 1) {
            // 根据学校id查询旧学期
            EduclTermEntity originTerm = educlTermMapper.selectOne(
                    new LambdaQueryWrapper<EduclTermEntity>()
                            .eq(EduclTermEntity::getSchoolId, schoolId)
                            .eq(EduclTermEntity::getIsValid, 1)
                            .eq(EduclTermEntity::getTermStatus, 1));
            if (originTerm != null) {
                // 将原学期的 term_status 设置为 0
                originTerm.setTermStatus(0);
                originTerm.setUpdateTime(now);
                originTerm.setUpdateUser(1L);
                educlTermMapper.updateById(originTerm);
            }

            // 启用新学期
            term.setId(term.getId());
            term.setTermStatus(1);
            term.setUpdateTime(now);
            term.setUpdateUser(1L);
            educlTermMapper.updateById(term);
        }
        Long termId = term.getId();
        log.info("学校[{}]匹配到学期: id={}, name={}", schoolId, termId, term.getTermName());

        // 3. 查询当前学校所有审核通过且在籍的学生
        LambdaQueryWrapper<AsStudentEntity> stuWrapper = new LambdaQueryWrapper<>();
        stuWrapper.eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getSchoolRollStatus, 1);
        List<AsStudentEntity> allStudentsData = asStudentMapper.selectList(stuWrapper);
        if (CollectionUtil.isEmpty(allStudentsData)) {
            log.info("学校[{}]无审核通过在籍学生", schoolId);
            return;
        }

        // 4. 按除 term_id 外的其他业务字段去重（保留最新一条）
        List<AsStudentEntity> dedupedStudents = new ArrayList<>();
        List<Long> identStuIds = allStudentsData.stream().map(AsStudentEntity::getStudentIdentityId).distinct().collect(Collectors.toList());
        for (Long identStuId : identStuIds) {
            List<AsStudentEntity> filterStus = allStudentsData.stream()
                    .filter(stu -> stu.getStudentIdentityId().equals(identStuId))
                    .sorted(Comparator.comparing(AsStudentEntity::getCreateTime).reversed())
                    .collect(Collectors.toList());
            dedupedStudents.add(filterStus.get(0));

        }
        log.info("学校[{}]去重前: {}, 去重后: {}", schoolId, allStudentsData.size(), dedupedStudents.size());

        // 按 studentNo+studentName termId是否为空 分组
        Map<String, AsStudentEntity> byNoName = new HashMap<>();
        Map<String, AsStudentEntity> byNoNameNullTerm = new HashMap<>();
        if (CollectionUtil.isNotEmpty(dedupedStudents)) {
            for (AsStudentEntity es : dedupedStudents) {
                String key = es.getStudentNo() + "|" + es.getStudentName();
                if (es.getTermId() != null) {
                    byNoName.putIfAbsent(key, es);
                } else {
                    byNoNameNullTerm.putIfAbsent(key, es);
                }
            }
        }

        // 判断当前学期的开始时间月份是不是9月
        boolean isSeptember = term.getStartDate().getMonth() == Calendar.SEPTEMBER;

        // 6. 分类为新增列表和更新列表
        List<AsStudentEntity> toInsert = new ArrayList<>();
        List<AsStudentEntity> toUpdate = new ArrayList<>();

        for (AsStudentEntity stu : dedupedStudents) {
            String key = stu.getStudentNo() + "|" + stu.getStudentName();
            AsStudentEntity matched = byNoName.get(key);
            if (matched != null && termId.equals(matched.getTermId())) {
                // 学期ID、学号、姓名已存在 -> 无需操作
                continue;
            }
            if (matched == null) {
                matched = byNoNameNullTerm.get(key);
                if (matched != null) {
                    // 学号姓名存在但term_id为null，或需要更新term_id -> 编辑
                    AsStudentEntity updateEntity = new AsStudentEntity();
                    updateEntity.setId(matched.getId());
                    updateEntity.setTermId(termId);
                    updateEntity.setUpdateTime(now);
                    updateEntity.setUpdateUser(1L);
                    toUpdate.add(updateEntity);
                }
            } else {
                // 其他情况 -> 新增
                Long nextGradeId = getNextGrade(stu.getGradeId(), schoolId);
                if (isSeptember && nextGradeId != null ){ // 9月份为下学期，更新学生年级
                    if (nextGradeId != 0L) {
                        stu.setGradeId(nextGradeId);
                    }else {
                        // 无下一个年级，标记为已毕业，不再执行后续新增逻辑
                        Calendar cal = Calendar.getInstance();
                        stu.setGraduationYear(cal.get(Calendar.YEAR) + 1);
                        stu.setSchoolRollStatus(2);
                        stu.setUpdateTime(now);
                        stu.setUpdateUser(1L);
                        asStudentMapper.updateById(stu);
                        log.info("学生[{}{}]已毕业，更新学籍状态", stu.getStudentNo(), stu.getStudentName());
                        continue;
                    }
                }
                stu.setId(null);
                stu.setTermId(termId);
                stu.setCreateTime(now);
                stu.setCreateUser(1L);
                toInsert.add(stu);
            }
        }

        // 7. 批量执行新增与编辑
        if (CollectionUtil.isNotEmpty(toInsert)) {
            // 按班级ID分组 处理班级升年级
            Map<Long, List<AsStudentEntity>> classStudentMap = toInsert.stream()
                    .collect(Collectors.groupingBy(AsStudentEntity::getClassId));
            for (Map.Entry<Long, List<AsStudentEntity>> entry : classStudentMap.entrySet()) {
                Long classId = entry.getKey();
                AsStudentEntity classStudent = entry.getValue().get(0);
                TEduclClass educlClass = educlClassService.getById(classId);
                if(null != educlClass){
                    if (isSeptember){
                        educlClass.setGradeId(classStudent.getGradeId());
                    }
                    educlClass.setTermId(classStudent.getTermId());
                    educlClass.setUpdateTime(now);
                    educlClass.setUpdateUser(1L);
                    educlClassService.updateById(educlClass);
                }
            }


            for (AsStudentEntity s : toInsert) {
                asStudentMapper.insert(s);
            }
            log.info("学校[{}]批量新增学生: {}条", schoolId, toInsert.size());
        }
        if (CollectionUtil.isNotEmpty(toUpdate)) {
            for (AsStudentEntity s : toUpdate) {
                asStudentMapper.updateById(s);
            }
            log.info("学校[{}]批量更新学生学期: {}条", schoolId, toUpdate.size());
        }
    }

    /**
     * 根据年级名称和学段类型，返回下一个年级名称
     */
    private Long getNextGrade(Long gradeId, Long schoolId) {

        EduclGradeEntity grade = educlGradeMapper.selectById(gradeId);
        if (grade == null || StringUtils.isBlank(grade.getGradeName())) {
            return null;
        }
        String gradeName = grade.getGradeName();
        // 数字映射
        Map<String, Integer> cnNumMap = new HashMap<>();
        cnNumMap.put("一", 1);
        cnNumMap.put("二", 2);
        cnNumMap.put("三", 3);
        cnNumMap.put("四", 4);
        cnNumMap.put("五", 5);
        cnNumMap.put("六", 6);
        cnNumMap.put("七", 7);
        cnNumMap.put("八", 8);
        cnNumMap.put("九", 9);

        // 提取年级名称中的中文数字
        String numStr = gradeName.replace("年级", "").trim();
        Integer currentNum = cnNumMap.get(numStr);
        if (currentNum == null) {
            log.warn("无法解析年级名称: {}", gradeName);
            return null;
        }

        int nextNum = currentNum + 1;
        // 数字映射
        String[] cnNums = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        String newGradeName = cnNums[nextNum] + "年级";

        EduclGradeEntity nextGrade = educlGradeMapper.selectOne(
                new LambdaQueryWrapper<EduclGradeEntity>()
                        .eq(EduclGradeEntity::getSchoolId, schoolId)
                        .eq(EduclGradeEntity::getGradeName, newGradeName)
                        .eq(EduclGradeEntity::getIsValid, 1));
        if (nextGrade == null && (7 == nextNum || 10 == nextNum)) {
            return 0L;
        }
        return nextGrade != null ? nextGrade.getId() : null;
    }
}
