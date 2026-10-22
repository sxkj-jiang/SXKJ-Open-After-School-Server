package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.module.afterschool.domain.dto.EduclCalendarEventAO;
import com.sxkj.edu.module.afterschool.domain.entity.EduclCalendarEventEntity;

import java.util.List;

public interface EduclCalendarEventService extends IService<EduclCalendarEventEntity> {

     Boolean logicDeleteCalendarEvents(Long termId);

     Boolean updateCalendarEvents(Long termId, Long schoolId, List<EduclCalendarEventAO> eventList);
}
