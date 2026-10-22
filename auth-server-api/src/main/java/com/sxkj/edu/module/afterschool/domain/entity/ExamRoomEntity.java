package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 教室信息实体
 *
 * @author system
 * @date 2026-05-25
 */
@Data
@TableName("t_exam_room")
public class ExamRoomEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 教室名
     */
    private String roomName;

    /**
     * 教室位置（教学楼）
     */
    private String roomAddress;

    /**
     * 教室编码（楼层）
     */
    private String roomNumber;

    /**
     * 备注
     */
    private String remark;
}
