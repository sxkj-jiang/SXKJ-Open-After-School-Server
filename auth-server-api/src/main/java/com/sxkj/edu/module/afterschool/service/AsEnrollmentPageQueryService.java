package com.sxkj.edu.module.afterschool.service;

import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.module.afterschool.domain.vo.AsAdminUnselectedSlotQueryData;
import com.sxkj.edu.module.afterschool.domain.vo.AsEnrollmentSelectedPageData;
import com.sxkj.edu.module.afterschool.mapper.AsEnrollmentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 管理端选课分页数据库查询服务。
 * <p>分页 SQL 与 FOUND_ROWS() 必须在同一只读事务、同一数据库连接内连续执行。</p>
 */
@Service
public class AsEnrollmentPageQueryService {

    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;

    /**
     * 查询已选课组班当前页数据及总数，不再额外执行一遍同口径 count 聚合。
     */
    @Transactional(readOnly = true)
    public PageResultDTO<AsEnrollmentSelectedPageData> pageSelectedData(
            Long schoolId, Collection<Long> serviceIds, Long gradeId, Long classId,
            String keyword, Integer enrollType, int pageIndex, int pageSize) {
        int offset = (pageIndex - 1) * pageSize;
        List<AsEnrollmentSelectedPageData> rows = asEnrollmentMapper.listSelectedPageData(
                schoolId, serviceIds, gradeId, classId, keyword, enrollType, offset, pageSize);
        long total = asEnrollmentMapper.selectFoundRows();
        return buildPageResult(pageIndex, pageSize, total, rows);
    }

    /**
     * 查询未选课组班当前页候选学生编号及总数，只把当前页编号返回给 Java。
     */
    @Transactional(readOnly = true)
    public PageResultDTO<Long> pageAdminUnselectedStudentIds(
            Long schoolId, Long termId, Collection<Long> serviceIds,
            Long gradeId, Long classId, String keyword,
            Collection<AsAdminUnselectedSlotQueryData> assignableSlots,
            int pageIndex, int pageSize) {
        int offset = (pageIndex - 1) * pageSize;
        List<Long> studentIds = asEnrollmentMapper.listAdminUnselectedStudentCandidatePageIds(
                schoolId, termId, serviceIds, gradeId, classId, keyword,
                assignableSlots, offset, pageSize);
        long total = asEnrollmentMapper.selectFoundRows();
        return buildPageResult(pageIndex, pageSize, total, studentIds);
    }

    /** 构造内部分页结果，返回结构与现有管理端分页接口保持一致。 */
    private <T> PageResultDTO<T> buildPageResult(
            int pageIndex, int pageSize, long total, List<T> rows) {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setPageIndex((long) pageIndex);
        result.setPageSize((long) pageSize);
        result.setTotal(total);
        result.setPages((long) Math.ceil(total * 1.0 / pageSize));
        result.setList(rows);
        return result;
    }
}
