package com.sxkj.edu.common.base.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class DirAllFileVO {

    // 所有文件
    private List<String> fileList;

    // 所有文件夹
    private List<String> dirFileList;

}
