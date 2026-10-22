package com.sxkj.edu.config.handle.business;

import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsGuardianScheduleItemVO;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description : 家长可报名服务及其目标学期学生快照,家长端选课报名上课时间视图
 * @Author : jiangjj$
 * @Date : 2026$ $
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
public class GuardianServiceCandidate {

    public  AsServiceEntity service;
    public  AsStudentEntity student;
    public final Map<Long, List<AsGuardianScheduleItemVO>> itemMap = new HashMap<>();
    public final Map<Long, String> textMap = new HashMap<>();


    public GuardianServiceCandidate(AsServiceEntity service, AsStudentEntity student) {
        this.service = service;
        this.student = student;
    }

    public GuardianServiceCandidate(){}

    public void put(Long courseClassId, List<AsGuardianScheduleItemVO> items, String text) {
        itemMap.put(courseClassId, items == null ? Collections.emptyList() : items);
        if (StringUtils.hasText(text)) {
            textMap.put(courseClassId, text);
        }
    }

    public List<AsGuardianScheduleItemVO> getScheduleItems(Long courseClassId) {
        return itemMap.getOrDefault(courseClassId, Collections.emptyList());
    }

    public String getScheduleText(Long courseClassId) {
        return textMap.get(courseClassId);
    }
    
}
