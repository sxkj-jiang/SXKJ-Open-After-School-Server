package com.sxkj.edu.common.page;

import java.io.Serializable;
import java.util.List;

/**
 * 场景：对查询结果进行自定义分页
 * @描述: 分页组件.
 * @版本: 1.0 .
 */
public class PageCommonBean<T> implements Serializable {
    private static final long serialVersionUID = 8470697978259453214L;
    private Integer num;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    // 指定的或是页面参数
    private long currentPage; // 当前页
    private long numPerPage; // 每页显示多少条

    // 查询数据库
    private long totalCount; // 总记录数
    private List<T> recordList; // 本页的数据列表

    // 计算
    private long pageCount; // 总页数
    private long beginPageIndex; // 页码列表的开始索引（包含）
    private long endPageIndex; // 页码列表的结束索引（包含）

    private T countResultMap; // 当前分页条件下的统计结果

    public PageCommonBean() {
    }

    /**
     * 只接受前4个必要的属性，会自动的计算出其他3个属生的值
     *
     * @param currentPage
     * @param numPerPage
     * @param totalCount
     * @param recordList
     */
    public PageCommonBean(long currentPage, long numPerPage, long totalCount, List<T> recordList) {
        this.currentPage = currentPage;
        this.numPerPage = numPerPage;
        this.totalCount = totalCount;
        this.recordList = recordList;

        // 计算总页码
        pageCount = (totalCount + numPerPage - 1) / numPerPage;

        // 计算 beginPageIndex 和 endPageIndex
        // >> 总页数不多于10页，则全部显示
        if (pageCount <= 10) {
            beginPageIndex = 1;
            endPageIndex = pageCount;
        }
        // >> 总页数多于10页，则显示当前页附近的共10个页码
        else {
            // 当前页附近的共10个页码（前4个 + 当前页 + 后5个）
            beginPageIndex = currentPage - 4;
            endPageIndex = currentPage + 5;
            // 当前面的页码不足4个时，则显示前10个页码
            if (beginPageIndex < 1) {
                beginPageIndex = 1;
                endPageIndex = 10;
            }
            // 当后面的页码不足5个时，则显示后10个页码
            if (endPageIndex > pageCount) {
                endPageIndex = pageCount;
                beginPageIndex = pageCount - 10 + 1;
            }
        }
    }

    /**
     * 只接受前5个必要的属性，会自动的计算出其他3个属生的值
     *
     * @param currentPage
     * @param totalCount
     * @param recordList
     */
    public PageCommonBean(int currentPage, int numPerPage, int totalCount, List<T> recordList, T countResultMap) {
        this.currentPage = currentPage;
        this.numPerPage = numPerPage;
        this.totalCount = totalCount;
        this.recordList = recordList;
        this.countResultMap = countResultMap;

        // 计算总页码
        pageCount = (totalCount + numPerPage - 1) / numPerPage;

        // 计算 beginPageIndex 和 endPageIndex
        // >> 总页数不多于10页，则全部显示
        if (pageCount <= 10) {
            beginPageIndex = 1;
            endPageIndex = pageCount;
        }
        // >> 总页数多于10页，则显示当前页附近的共10个页码
        else {
            // 当前页附近的共10个页码（前4个 + 当前页 + 后5个）
            beginPageIndex = currentPage - 4;
            endPageIndex = currentPage + 5;
            // 当前面的页码不足4个时，则显示前10个页码
            if (beginPageIndex < 1) {
                beginPageIndex = 1;
                endPageIndex = 10;
            }
            // 当后面的页码不足5个时，则显示后10个页码
            if (endPageIndex > pageCount) {
                endPageIndex = pageCount;
                beginPageIndex = pageCount - 10 + 1;
            }
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public long getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(int numPerPage) {
        this.numPerPage = numPerPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public long getBeginPageIndex() {
        return beginPageIndex;
    }

    public void setBeginPageIndex(int beginPageIndex) {
        this.beginPageIndex = beginPageIndex;
    }

    public long getEndPageIndex() {
        return endPageIndex;
    }

    public void setEndPageIndex(int endPageIndex) {
        this.endPageIndex = endPageIndex;
    }

    public T getCountResultMap() {
        return countResultMap;
    }

    public void setCountResultMap(T countResultMap) {
        this.countResultMap = countResultMap;
    }

}
