package com.sxkj.edu.module.afterschool.mapper;

import com.sxkj.edu.module.afterschool.domain.dto.AsAttendanceStatisticsQueryAO;
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
 * 考勤统计 Mapper 学期 SQL 契约测试。
 */
public class AsAttendanceRecordMapperSqlTest {

    private static final String NAMESPACE =
            "com.sxkj.edu.module.afterschool.mapper.AsAttendanceRecordMapper.";

    private Configuration configuration;

    /**
     * 加载考勤 Mapper XML。
     */
    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        try (InputStream input = Resources.getResourceAsStream(
                "mapper/khfw/AsAttendanceRecordMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    input,
                    configuration,
                    "mapper/khfw/AsAttendanceRecordMapper.xml",
                    configuration.getSqlFragments());
            builder.parse();
        }
    }

    /**
     * 管理端考勤统计必须同时限制服务学期和学生学期。
     */
    @Test
    public void attendanceStatisticsShouldFilterServiceAndStudentTerm() {
        AsAttendanceStatisticsQueryAO query = new AsAttendanceStatisticsQueryAO();
        query.setSchoolId(6L);
        query.setTermId(20L);
        query.setSessionDate("2026-07-24");
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        String sql = sql("queryAttendanceStatistics", params);

        assertTrue(sql.contains("INNER JOIN t_as_course_class cc"));
        assertTrue(sql.contains("INNER JOIN t_as_service srv"));
        assertTrue(sql.contains("INNER JOIN t_as_student stu"));
        assertTrue(sql.contains("srv.term_id = ?"));
        assertTrue(sql.contains("stu.term_id = srv.term_id"));
    }

    private String sql(String statement, Object params) {
        BoundSql boundSql = configuration.getMappedStatement(NAMESPACE + statement)
                .getBoundSql(params);
        return boundSql.getSql().replaceAll("\\s+", " ").trim();
    }
}
