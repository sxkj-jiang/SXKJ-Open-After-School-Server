package com.sxkj.edu.module.afterschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.dto.EduclCalendarEventAO;
import com.sxkj.edu.module.afterschool.mapper.EduclCalendarEventMapper;
import com.sxkj.edu.module.afterschool.domain.entity.EduclCalendarEventEntity;
import com.sxkj.edu.module.afterschool.service.EduclCalendarEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class EduclCalendarEventServiceImpl extends ServiceImpl<EduclCalendarEventMapper, EduclCalendarEventEntity> implements EduclCalendarEventService {

    /**
     * 逻辑删除学期下所有校历事件
     */
    public Boolean logicDeleteCalendarEvents(Long termId) {
        LambdaQueryWrapper<EduclCalendarEventEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclCalendarEventEntity::getTermId, termId)
                .eq(EduclCalendarEventEntity::getIsValid, 1);
        List<EduclCalendarEventEntity> events = this.list(wrapper);
        if (CollectionUtils.isEmpty(events)) return true;
        for (EduclCalendarEventEntity event : events) {
            event.setIsValid(0);
            event.setUpdateTime(new Date());
            this.updateById(event);
        }
        return true;
    }

    /**
     * 先逻辑删除再全量新增校历事件子表
     */
    public Boolean updateCalendarEvents(Long termId, Long schoolId, List<EduclCalendarEventAO> eventList) {
        // 1. 先逻辑删除该学期下所有有效的校历事件
        logicDeleteCalendarEvents(termId);
        // 2. 再全量新增前端传入的校历事件
        if (CollectionUtils.isEmpty(eventList)) return true;
        for (EduclCalendarEventAO eventAO : eventList) {
            EduclCalendarEventEntity insertEntity = new EduclCalendarEventEntity();
            BeanUtils.copyProperties(eventAO, insertEntity);
            insertEntity.setId(null);
            insertEntity.setTermId(termId);
            insertEntity.setSchoolId(schoolId);
            insertEntity.setIsValid(1);
            insertEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            insertEntity.setCreateTime(new Date());
            this.save(insertEntity);
        }
        return true;
    }

}
