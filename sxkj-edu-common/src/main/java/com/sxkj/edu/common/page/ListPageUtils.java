package com.sxkj.edu.common.page;

import com.sxkj.edu.common.exception.BaseBusinessException;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 手动对list结果集分页
 */
@Data
public class ListPageUtils<E> implements Serializable {

    private static final long serialVersionUID = 6586051440945393618L;

    /**
     * 当前页面
     */
    private long pageIndex;

    /**
     * 显示多少行
     */
    private long pageSize;

    /**
     * 总记录条数
     */
    private long total;

    /**
     * 总页数
     */
    private long pages;


    /**
     * @param total the total to set
     */
    public void setTotal(long total) {
        this.total = total;
    }

    public ListPageUtils() {
        this.pageIndex = 0;
        this.pageSize = 0;
        this.total = 0;
        this.pages = 0;
    }

    public ListPageUtils(long index, long page, long total) {
        this.pageIndex = index;
        this.pageSize = page;
        this.total = total;
    }

    public ListPageUtils(long index, long page, long total, long pageTotal) {
        this.pageIndex = index;
        this.pageSize = page;
        this.total = total;
        this.pages = pageTotal;
    }


    /**
     * 对list集合进行分页处理
     *
     * @return
     */
    public List<E> setListSplitForPage(List<E> list, ListPageUtils sub) {
        List<E> newList = new ArrayList<>();
        try {
            total = list.size();
            if (pageSize > 0) {
                long currTotal = pageSize * (pageIndex);
                if (currTotal <= total) {
                    String formIndex = pageSize * (pageIndex - 1)+"";
                    String endIndex = ((pageSize * pageIndex) > total ? total : (pageSize * pageIndex))+"";
                    newList = list.subList(new Integer(formIndex), new Integer(endIndex));
                } else {
                    newList = list;
                }
                Double result = new BigDecimal((float) total / pageSize).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                Double resDb = Math.ceil(result);
                int pTotal = resDb.intValue();
                sub.setPages(pTotal);
            }
        } catch (Exception e) {
            throw new BaseBusinessException("call ListPageUtils setListSplit in error,", e);
        }
        return newList;
    }

    /**
     * 适用于 只计算总页数，list无需截取
     * @param list
     * @param sub
     * @return
     */
    public List<E> setListSplitForSizePage(List<E> list, ListPageUtils sub) {
        List<E> newList = new ArrayList<>();
        try {
            total = sub.getTotal();
            if (pageSize > 0) {
                newList = list;
                Double result = new BigDecimal((float) total / pageSize).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                Double resDb = Math.ceil(result);
                int pTotal = resDb.intValue();
                sub.setPages(pTotal);
            }
        } catch (Exception e) {
            throw new BaseBusinessException("call ListPageUtils setListSplit in error,", e);
        }
        return newList;
    }

    @Override
    public String toString() {
        return "ListPageUtils{" +
                "page=" + pageIndex +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", pageTotal=" + pages +
                '}';
    }

}
