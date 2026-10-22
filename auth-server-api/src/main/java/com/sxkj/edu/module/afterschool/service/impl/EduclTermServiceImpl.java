package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.excel.EduclTermExportExcel;
import com.sxkj.edu.module.afterschool.mapper.EduclCalendarEventMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import com.sxkj.edu.module.afterschool.domain.dto.EduclCalendarEventAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclTermAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclTermQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.DateHolidayEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclCalendarEventEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.domain.excel.EduclTermExcel;
import com.sxkj.edu.module.afterschool.domain.vo.EduclCalendarEventVO;
import com.sxkj.edu.module.afterschool.domain.vo.EduclTermVO;
import com.sxkj.edu.module.afterschool.domain.vo.TermWeekMonthDropdownVO;
import com.sxkj.edu.module.afterschool.service.DateHolidayService;
import com.sxkj.edu.module.afterschool.service.EduclCalendarEventService;
import com.sxkj.edu.module.afterschool.service.EduclTermService;
import com.sxkj.edu.module.afterschool.support.StudentBusinessGuard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 学期 Service 实现类
 *
 * @author system
 * @date 2026-05-19
 */
@Service
@Slf4j
public class EduclTermServiceImpl extends ServiceImpl<EduclTermMapper, EduclTermEntity> implements EduclTermService {

    @Autowired
    private EduclCalendarEventMapper educlCalendarEventMapper;

    @Autowired
    private EduclCalendarEventService educlCalendarEventService;

    @Resource
    private RedisCache redisCache;
    @Autowired
    private DateHolidayService dateHolidayService;
    @Resource
    private StudentBusinessGuard studentBusinessGuard;


    // ======================== 通用私有方法 ========================

    /**
     * 校验同一学校下学期名称是否已存在
     */
    private boolean existsByName(Long schoolId, String termName, Long excludeId) {
        LambdaQueryWrapper<EduclTermEntity> w = new LambdaQueryWrapper<>();
        w.eq(EduclTermEntity::getIsValid, 1)
         .eq(EduclTermEntity::getSchoolId, schoolId)
         .eq(EduclTermEntity::getTermName, termName);
        if (excludeId != null) {
            w.ne(EduclTermEntity::getId, excludeId);
        }
        return this.count(w) > 0;
    }

    /**
     * 校验同一学校内学期周期不重叠。
     */
    private String validateTermRange(Long schoolId, Date startDate,
                                     Date endDate, Long excludeId) {
        if (startDate == null || endDate == null) {
            return "学期开始日期和结束日期不能为空";
        }
        if (endDate.before(startDate)) {
            return "学期结束日期不能早于开始日期";
        }
        LambdaQueryWrapper<EduclTermEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclTermEntity::getSchoolId, schoolId)
                .eq(EduclTermEntity::getIsValid, 1)
                .le(EduclTermEntity::getStartDate, endDate)
                .ge(EduclTermEntity::getEndDate, startDate);
        if (excludeId != null) {
            wrapper.ne(EduclTermEntity::getId, excludeId);
        }
        return this.count(wrapper) > 0 ? "同一学校的学期周期不能重叠" : null;
    }

    /**
     * Entity -> VO
     */
    private EduclTermVO toVO(EduclTermEntity entity) {
        if (entity == null) return null;
        EduclTermVO vo = new EduclTermVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    // ======================== 对外方法 ========================

    @Override
    public PageResultDTO<EduclTermVO> queryEduclTermPage(EduclTermQueryAO qo) {
        Page<EduclTermVO> page = baseMapper.pageList(new Page<>(qo.getPageIndex(), qo.getPageSize()), qo);
        PageResultDTO<EduclTermVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> addEduclTerm(EduclTermAddAO ao) {
        if (existsByName(ao.getSchoolId(), ao.getTermName(), null)) {
            return JsonResult.buildError("学期名称在该学校下已存在");
        }
        String rangeError = validateTermRange(
                ao.getSchoolId(), ao.getStartDate(), ao.getEndDate(), null);
        if (rangeError != null) {
            return JsonResult.buildError(rangeError);
        }
        Boolean flag = false;
        EduclTermEntity entity = new EduclTermEntity();
        BeanUtils.copyProperties(ao, entity);
        // 根据开始/结束日期计算总周数
        entity.setTotalWeeks(calcTotalWeeks(ao.getStartDate(), ao.getEndDate()));
        //生成学期编码
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        StringBuilder stringBuilder = new StringBuilder().append(dateStr).
                append(String.format("%03d", redisCache.increment(String.format("%s:%s", CoreConstant.edu_base_xlgl, "EDUCL_TERM_CODE"), 1L)));
        entity.setTermCode(stringBuilder.toString());
        entity.setIsValid(1);
        entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        entity.setCreateTime(new Date());
        flag = this.save(entity);
        if (!flag) {
            return JsonResult.buildError("操作失败");
        }
        // 保存子表：校历事件
        flag = educlCalendarEventService.updateCalendarEvents(entity.getId(), ao.getSchoolId(), ao.getCalendarEventList());
        if (!flag) {
            throw businessError("校历事件保存失败，学期新增已回滚");
        }
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateEduclTerm(EduclTermAddAO ao) {
        if (ao.getId() == null) return JsonResult.buildError("学期ID不能为空");
        EduclTermEntity exist = this.getById(ao.getId());
        if (exist == null || exist.getIsValid() != 1) return JsonResult.buildError("学期记录不存在");
        if (!Objects.equals(exist.getSchoolId(), ao.getSchoolId())) {
            return JsonResult.buildError("学期所属学校不允许修改");
        }
        if (existsByName(ao.getSchoolId(), ao.getTermName(), ao.getId())) {
            return JsonResult.buildError("学期名称在该学校下已存在");
        }
        String rangeError = validateTermRange(
                ao.getSchoolId(), ao.getStartDate(), ao.getEndDate(), ao.getId());
        if (rangeError != null) {
            return JsonResult.buildError(rangeError);
        }
        if (studentBusinessGuard.hasTermBusiness(exist.getSchoolId(), exist.getId())
                && (!Objects.equals(exist.getTermName(), ao.getTermName())
                || !Objects.equals(exist.getSchoolYear(), ao.getSchoolYear())
                || !Objects.equals(exist.getTermType(), ao.getTermType())
                || !Objects.equals(exist.getStartDate(), ao.getStartDate())
                || !Objects.equals(exist.getEndDate(), ao.getEndDate()))) {
            return JsonResult.buildError(
                    "学期已产生学生或课后服务业务，名称、学年、类型和周期不允许修改");
        }
        EduclTermEntity entity = new EduclTermEntity();
        BeanUtils.copyProperties(ao, entity);
        // 根据开始/结束日期计算总周数
        entity.setTotalWeeks(calcTotalWeeks(ao.getStartDate(), ao.getEndDate()));
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        entity.setUpdateTime(new Date());
        if (!this.updateById(entity)) {
            return JsonResult.buildError("操作失败");
        }
        // 先逻辑删除旧子表，再全量新增
        if (!educlCalendarEventService.updateCalendarEvents(
                ao.getId(), ao.getSchoolId(), ao.getCalendarEventList())) {
            throw businessError("校历事件保存失败，学期修改已回滚");
        }
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> deleteEduclTerm(Long id) {
        EduclTermEntity entity = this.getById(id);
        if (entity == null || entity.getIsValid() != 1) return JsonResult.buildError("学期记录不存在");
        if (studentBusinessGuard.hasTermBusiness(entity.getSchoolId(), id)) {
            return JsonResult.buildError("学期已产生业务，不能删除，请通过结束日期保留历史数据");
        }
        entity.setIsValid(0);
        entity.setUpdateTime(new Date());
        if (!this.updateById(entity)) {
            return JsonResult.buildError("学期删除失败");
        }
        // 同时逻辑删除子表
        educlCalendarEventService.logicDeleteCalendarEvents(id);
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<EduclTermVO> getEduclTermDetail(Long id) {
        EduclTermVO vo = baseMapper.selectDetail(id);
        if (vo == null) return JsonResult.buildError("学期记录不存在");
        // 查询子表：校历事件列表
        LambdaQueryWrapper<EduclCalendarEventEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclCalendarEventEntity::getTermId, id)
                .eq(EduclCalendarEventEntity::getIsValid, 1)
                .orderByAsc(EduclCalendarEventEntity::getEventDateStart);
        List<EduclCalendarEventEntity> eventEntities = educlCalendarEventMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(eventEntities)) {
            List<EduclCalendarEventVO> eventVOs = eventEntities.stream().map(e -> {
                EduclCalendarEventVO eventVO = new EduclCalendarEventVO();
                BeanUtils.copyProperties(e, eventVO);
                return eventVO;
            }).collect(Collectors.toList());
            vo.setCalendarEventList(eventVOs);
        }
        return JsonResult.buildSuccess(vo);
    }

    /**
     * 根据学期开始日期和结束日期计算总周数（向上取整）
     */
    private Integer calcTotalWeeks(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || endDate.before(startDate)) return null;
        long diffMillis = endDate.getTime() - startDate.getTime();
        long days = diffMillis / (1000 * 60 * 60 * 24);
        return (int) Math.ceil(days / 7.0);
    }

    @Override
    public JsonResult<String> importEduclTerm(Long schoolId, MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            List<EduclTermExcel> list = ExcelUtils.readExcel(is, EduclTermExcel.class);
            if (list == null || list.isEmpty()) return JsonResult.buildError("导入数据为空");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<String> errors = new ArrayList<>();
            int row = 2;
            for (EduclTermExcel excel : list) {
                if (StrUtil.isBlank(excel.getTermName())) {
                    errors.add("第" + row + "行: 学期名称不能为空");
                    row++;
                    continue;
                }
                if (existsByName(schoolId, excel.getTermName(), null)) {
                    errors.add("第" + row + "行: 学期名称[" + excel.getTermName() + "]已存在，跳过");
                    row++;
                    continue;
                }
                EduclTermEntity entity = new EduclTermEntity();
                entity.setSchoolId(schoolId);
                entity.setTermName(excel.getTermName());
                entity.setTermCode(excel.getTermCode());
                entity.setSchoolYear(excel.getSchoolYear());
                entity.setTermType(excel.getTermType());
                entity.setTotalWeeks(excel.getTotalWeeks());
                entity.setTermStatus(excel.getTermStatus() != null ? excel.getTermStatus() : 1);
                if (StrUtil.isBlank(excel.getStartDate())
                        || StrUtil.isBlank(excel.getEndDate())) {
                    errors.add("第" + row + "行: 学期开始日期和结束日期不能为空");
                    row++;
                    continue;
                }
                try {
                    sdf.setLenient(false);
                    entity.setStartDate(sdf.parse(excel.getStartDate()));
                    entity.setEndDate(sdf.parse(excel.getEndDate()));
                } catch (Exception exception) {
                    errors.add("第" + row + "行: 学期日期格式不正确，应为yyyy-MM-dd");
                    row++;
                    continue;
                }
                String rangeError = validateTermRange(
                        schoolId, entity.getStartDate(), entity.getEndDate(), null);
                if (rangeError != null) {
                    errors.add("第" + row + "行: " + rangeError);
                    row++;
                    continue;
                }
                entity.setIsValid(1);
                entity.setCreateTime(new Date());
                this.save(entity);
                row++;
            }
            if (!errors.isEmpty()) {
                return JsonResult.buildError("部分数据导入失败：" + String.join("；", errors));
            }
            return JsonResult.buildSuccess("导入成功");
        } catch (Exception e) {
            log.error("学期导入异常", e);
            return JsonResult.buildError("导入异常：" + e.getMessage());
        }
    }

    @Override
    public void exportEduclTerm(EduclTermQueryAO qo, HttpServletResponse response) {
        PageResultDTO<EduclTermVO> page = this.queryEduclTermPage(qo);
        List<EduclTermVO> records = page.getList();
        if (CollectionUtil.isEmpty(records)) {
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<EduclTermExportExcel> excelList = records.stream().map(e -> {
            EduclTermExportExcel ex = new EduclTermExportExcel();
            BeanUtils.copyProperties(e, ex);
            ex.setSequence(String.valueOf(records.indexOf(e) + 1));
            String startDate = e.getStartDate() != null ? sdf.format(e.getStartDate()) : "";
            String endDate = e.getEndDate() != null ? sdf.format(e.getEndDate()) : "";
            ex.setTermRange(startDate + "～" + endDate);
            ex.setTotalWeeks(e.getTotalWeeks()+"周");
            return ex;
        }).collect(Collectors.toList());
        try {
            ExcelUtils.exportExcel(response, excelList, EduclTermExportExcel.class, "学期数据导出", "学期数据");
        } catch (Exception e) {
            log.error("学期导出异常", e);
        }
    }

    @Override
    public Integer getTermTypeById(Long id) {
        EduclTermEntity entity = this.getById(id);
        if (entity != null) return entity.getTermType();
        return 1;
    }

    @Override
    public List<EduclCalendarEventVO> queryHolidayEvents(Date startDate, Date endDate) {
        List<EduclCalendarEventVO> result = new ArrayList<>();
        if (startDate == null || endDate == null || endDate.before(startDate)) {
            return result;
        }

        // 查询 t_date_holiday 表中与日期范围有交集的节假日
        LambdaQueryWrapper<DateHolidayEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DateHolidayEntity::getIsValid, 1)
                .le(DateHolidayEntity::getStartDate, endDate)    // 节假日开始日期 <= 查询结束日期
                .ge(DateHolidayEntity::getEndDate, startDate)    // 节假日结束日期 >= 查询开始日期
                .orderByAsc(DateHolidayEntity::getStartDate);

        List<DateHolidayEntity> holidayList = dateHolidayService.list(wrapper);

        // 转换为 EduclCalendarEventVO
        for (DateHolidayEntity holiday : holidayList) {
            EduclCalendarEventVO vo = new EduclCalendarEventVO();
            vo.setEventName(holiday.getHolidayName());
            vo.setEventDateStart(holiday.getStartDate());
            vo.setEventDateEnd(holiday.getEndDate());
            vo.setEventType(4); // 4-节假日
            result.add(vo);
        }

        return result;
    }

    @Override
    public List<TermWeekMonthDropdownVO> getCurrentTermWeekMonthDropdown() {
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        Date now = new Date();

        // 查询当前日期所处的学期：start_date <= now <= end_date
        LambdaQueryWrapper<EduclTermEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclTermEntity::getIsValid, 1)
                .eq(EduclTermEntity::getSchoolId, schoolId)
                .le(EduclTermEntity::getStartDate, now)
                .ge(EduclTermEntity::getEndDate, now)
                .last("LIMIT 1");
        EduclTermEntity term = this.getOne(wrapper);

        List<TermWeekMonthDropdownVO> result = new ArrayList<>();
        if (term == null) {
            return result;
        }

        // 1. 生成周数下拉
        TermWeekMonthDropdownVO weekVO = new TermWeekMonthDropdownVO();
        weekVO.setType("周数");
        int totalWeeks = term.getTotalWeeks() != null ? term.getTotalWeeks() : 0;

        List<String> weekItems = new ArrayList<>();
        for (int i = 0;  i < totalWeeks; i++) {
            weekItems.add("第" + (i + 1) + "周");
        }
        weekVO.setItems(weekItems);
        result.add(weekVO);

        // 2. 生成月份下拉：根据学期开始和结束日期之间的年月
        TermWeekMonthDropdownVO monthVO = new TermWeekMonthDropdownVO();
        monthVO.setType("月份");
        List<String> monthItems = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(term.getStartDate());
        // 对齐到月初
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(term.getEndDate());
        while (!cal.after(endCal)) {
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            monthItems.add(year + "年" + month + "月");
            cal.add(Calendar.MONTH, 1);
        }
        monthVO.setItems(monthItems);
        result.add(monthVO);

        return result;
    }

    @Override
    public Long getEduclTermExistForId(String termName,Long schoolId) {

        LambdaQueryWrapper<EduclTermEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclTermEntity::getIsValid, 1)
                .eq(EduclTermEntity::getSchoolId, schoolId)
                .eq(EduclTermEntity::getTermName, termName);
        List<EduclTermEntity> termList = this.list(wrapper);
        return termList.size() > 0 ? termList.get(0).getId() : null;
    }

    /**
     * 获取学校最新启用状态的学期
     * @param schoolId
     * @return
     */
    @Override
    public EduclTermEntity getCurrentTermBySchoolId(Long schoolId) {
        LambdaQueryWrapper<EduclTermEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclTermEntity::getSchoolId, schoolId)
                .eq(EduclTermEntity::getIsValid, 1)
                .eq(EduclTermEntity::getTermStatus, 1)
                .orderByDesc(EduclTermEntity::getStartDate);
        List<EduclTermEntity> terms = this.list(wrapper);
        if(CollectionUtil.isNotEmpty(terms)){
            return terms.get(0);
        }
        return null;
    }

    /**
     * 构造统一的业务异常，确保事务内的主表和校历事件同时回滚。
     */
    private BaseBusinessException businessError(String message) {
        return new BaseBusinessException(ResultCode.PARA_ERROR.code(), message);
    }

}
