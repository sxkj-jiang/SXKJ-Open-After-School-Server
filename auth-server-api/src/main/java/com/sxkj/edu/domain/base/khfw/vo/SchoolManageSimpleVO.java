package com.sxkj.edu.domain.base.khfw.vo;

import com.sxkj.edu.domain.dto.SchoolManageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@ApiModel(value = "SchoolManageVO对象", description = "学校信息表")
public class SchoolManageSimpleVO {

    private Long id;

    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    @ApiModelProperty(value = "学校类型")
    private String schoolType;


    public static SchoolManageSimpleVO convert(SchoolManageDTO dto) {
        if (dto == null) return null;
        SchoolManageSimpleVO vo = new SchoolManageSimpleVO();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    public static List<SchoolManageSimpleVO> convert(List<SchoolManageDTO> dtos) {
        if (dtos == null) return Collections.emptyList();
        List<SchoolManageSimpleVO> vos = new ArrayList<>();
        for (SchoolManageDTO dto : dtos) {
            vos.add(convert(dto));
        }
        return vos;
    }

}