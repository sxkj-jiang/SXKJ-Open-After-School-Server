package com.sxkj.edu.module.afterschool.mapper;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * 数据看板 Mapper 学期查询契约测试。
 */
public class StatisticsMapperTermSqlTest {

    private static final String NAMESPACE =
            "com.sxkj.edu.module.afterschool.mapper.StatisticsMapper.";

    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        try (InputStream input = Resources.getResourceAsStream(
                "mapper/khfw/StatisticsMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    input,
                    configuration,
                    "mapper/khfw/StatisticsMapper.xml",
                    configuration.getSqlFragments());
            builder.parse();
        }
    }

    /**
     * 数据概览的学生总数应限定到所选学期。
     */
    @Test
    public void overviewShouldFilterStudentTerm() {
        Map<String, Object> params = new HashMap<>();
        params.put("schoolId", 1L);
        params.put("termId", 30L);

        String sql = sql("getDataBoardOverview", params);

        assertTrue(sql.contains("s.term_id = ?"));
    }

    /**
     * 年级人数分布应限定到所选学期。
     */
    @Test
    public void gradeDistributionShouldFilterStudentTerm() {
        Map<String, Object> params = new HashMap<>();
        params.put("schoolId", 1L);
        params.put("termId", 30L);

        String sql = sql("getNjrsData", params);

        assertTrue(sql.contains("s.school_id = ?"));
        assertTrue(sql.contains("s.term_id = ?"));
    }

    /**
     * 获取并压缩指定 Mapper SQL。
     */
    private String sql(String statementId, Map<String, Object> params) {
        BoundSql boundSql = configuration.getMappedStatement(
                NAMESPACE + statementId).getBoundSql(params);
        return boundSql.getSql().replaceAll("\\s+", " ").trim();
    }
}
