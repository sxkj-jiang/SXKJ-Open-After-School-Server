package com.sxkj.edu.module.afterschool.mapper;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 报名统计 Mapper SQL 契约测试。
 */
public class AsEnrollmentMapperSqlTest {

    private static final String NAMESPACE = "com.sxkj.edu.module.afterschool.mapper.AsEnrollmentMapper.";

    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        try (InputStream input = Resources.getResourceAsStream("mapper/khfw/AsEnrollmentMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    input,
                    configuration,
                    "mapper/khfw/AsEnrollmentMapper.xml",
                    configuration.getSqlFragments()
            );
            builder.parse();
        }
    }

    /**
     * 已存在记录判断只返回去重学生编号，不能加载完整报名实体。
     */
    @Test
    public void existingEnrollmentStudentsShouldUseDistinctIdQuery() {
        Map<String, Object> params = new HashMap<>();
        params.put("schoolId", 1L);
        params.put("serviceId", 2L);
        params.put("studentIds", Arrays.asList(10L, 11L));

        String sql = sql("listExistingEnrollmentStudentIds", params);

        assertTrue(sql.contains("SELECT DISTINCT student_id"));
        assertTrue(sql.contains("school_id = ?"));
        assertTrue(sql.contains("service_id = ?"));
        assertFalse(sql.contains("SELECT *"));
    }

    /**
     * 状态统计占用时段查询应同时覆盖有效课程和非课程去向，并只返回轻量字段。
     */
    @Test
    public void occupiedSlotsShouldKeepOriginalBusinessFilters() {
        Map<String, Object> params = new HashMap<>();
        params.put("schoolIds", Arrays.asList(1L, 2L));
        params.put("termIds", Arrays.asList(100L, 101L));
        params.put("studentIds", Arrays.asList(10L, 11L));

        String sql = sql("listStudentTermOccupiedSlots", params);

        assertTrue(sql.contains("e.enroll_status IN (1, 3)"));
        assertTrue(sql.contains("d.destination_type IN (2, 3, 4)"));
        assertTrue(sql.contains("cs.session_status != 3"));
        assertTrue(sql.contains("UNION ALL"));
        assertTrue(sql.contains("SELECT DISTINCT occupied.schoolId"));
        assertFalse(sql.contains("e.*"));
        assertFalse(sql.contains("d.*"));
    }

    private String sql(String statementId, Map<String, Object> params) {
        BoundSql boundSql = configuration.getMappedStatement(NAMESPACE + statementId).getBoundSql(params);
        return boundSql.getSql().replaceAll("\\s+", " ").trim();
    }
}
