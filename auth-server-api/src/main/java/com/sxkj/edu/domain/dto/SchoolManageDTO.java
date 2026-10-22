package com.sxkj.edu.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class SchoolManageDTO extends SchoolManagePo {

    @ApiModelProperty(value = "校友数量")
    private Integer schoolMateCount;

    public static SchoolManagePo convert(SchoolManageDTO dto) {
        if (dto == null) return null;
        SchoolManagePo po = new SchoolManagePo();
        BeanUtils.copyProperties(dto, po);
        return po;
    }

    public static SchoolManageDTO convert(SchoolManagePo po) {
        if (po == null) return null;
        SchoolManageDTO dto = new SchoolManageDTO();
        BeanUtils.copyProperties(po, dto);
        return dto;
    }

    public static List<SchoolManageDTO> convert(List<SchoolManagePo> pos) {
        if (pos == null) return Collections.emptyList();
        List<SchoolManageDTO> dtos = new ArrayList<>();
        for (SchoolManagePo po : pos) {
            dtos.add(convert(po));
        }
        return dtos;
    }

}