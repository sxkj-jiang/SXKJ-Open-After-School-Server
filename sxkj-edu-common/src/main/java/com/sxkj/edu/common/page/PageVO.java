package com.sxkj.edu.common.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应对象.
 * 配合 mybatis plus 分页插件 IPage 使用. 转成 Pager 输出给前端, 避免传递过多无效响应字段.
 */
@ApiModel("分页通用对象")
@Getter
@Setter
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Long pageIndex;

    /**
     * 每页的数量
     */
    @ApiModelProperty(value = "每页的数量")
    private Long pageSize;

    /**
     * 结果集
     */
    @ApiModelProperty(value = "结果集")
    private List<T> list;

    @ApiModelProperty("总记录数")
    private int total;

    @ApiModelProperty("总页数")
    private int pages;


    public PageVO(int total, int pages, List<T> list) {
        this.total = total;
        this.pages = pages;
        this.list = list;
    }

    /**
     * 适配 mybatis plus 的分页插件 IPage
     */
    public PageVO(long total, long pages, List<T> list) {
        this.total = (int) total;
        this.pages = (int) pages;
        this.list = list;
    }

    public PageVO(long pageIndex, long pageSize, long total, long pages, List<T> list) {
        this.total = (int) total;
        this.pages = (int) pages;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.list = list;
    }
}
