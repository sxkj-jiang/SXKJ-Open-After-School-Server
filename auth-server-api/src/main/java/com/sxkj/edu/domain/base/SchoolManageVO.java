package com.sxkj.edu.domain.base;

import com.sxkj.edu.domain.dto.SchoolManageDTO;
import com.sxkj.edu.domain.vo.FileVO;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@ApiModel(value = "SchoolManageVO对象", description = "学校信息表")
public class SchoolManageVO extends SchoolManagePo {

    @ApiModelProperty(value = "返回原始文件路径")
    private List<FileVO> fileUrlList;
    @ApiModelProperty(value = "校友数量")
    private Integer schoolMateCount;


    public static SchoolManageVO convert(SchoolManageDTO dto) {
        if (dto == null) return null;
        SchoolManageVO vo = new SchoolManageVO();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    public static List<SchoolManageVO> convert(List<SchoolManageDTO> dtos) {
        if (dtos == null) return Collections.emptyList();
        List<SchoolManageVO> vos = new ArrayList<>();
        for (SchoolManageDTO dto : dtos) {
            vos.add(convert(dto));
        }
        return vos;
    }

}