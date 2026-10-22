package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 教师巡课任务-登记详情返回VO
 *
 * @author system
 * @date 2026-06-03
 */
@Data
@ApiModel("教师巡课任务-登记详情返回VO")
public class AsPatrolRegistrationDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("地点ID")
    private String placeId;

    @ApiModelProperty("地点名称")
    private String placeName;

    @ApiModelProperty("教师id")
    private Long teacherId;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("课次集合")
    private List<SessionItem> sessionItems;

    @ApiModelProperty("问题整改单列表")
    private List<AsRectificationOrderAppVO> rectificationOrders;

    @Data
    @ApiModel("课次项")
    public static class SessionItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("课次ID")
        private Long sessionId;

        @ApiModelProperty("上课时间，格式：HH:mm-HH:mm")
        private String classTime;
    }
}
