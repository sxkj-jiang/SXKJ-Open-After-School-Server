package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("t_wp_file_video")
public class WpFileVideoPo extends BaseEntity {

    private Long fileId;
    private String fileName;
    private String requestId;
    private Long fileSize;
    private Integer fileLength;
    private Integer courseType;
    private Integer transformStatus;
    private String videoUrl;
    private String transformVideoUrl;
    private String remark;

}
