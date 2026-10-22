package com.sxkj.edu.module.afterschool.mapper;

import com.sxkj.edu.domain.base.khfw.ao.AsStudentBindParentAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentQueryAO;
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
 * 学生档案 Mapper 学期 SQL 契约测试。
 */
public class AsStudentMapperSqlTest {

    private static final String NAMESPACE =
            "com.sxkj.edu.module.afterschool.mapper.AsStudentMapper.";

    private Configuration configuration;

    /**
     * 加载学生 Mapper XML。
     */
    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        try (InputStream input = Resources.getResourceAsStream("mapper/khfw/AsStudentMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    input,
                    configuration,
                    "mapper/khfw/AsStudentMapper.xml",
                    configuration.getSqlFragments());
            builder.parse();
        }
    }

    /**
     * 学生分页传入学期时必须按学生快照学期过滤。
     */
    @Test
    public void pageStudentShouldFilterByTerm() {
        AsStudentQueryAO query = new AsStudentQueryAO();
        query.setSchoolId(1L);
        query.setTermId(100L);
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        String sql = sql("pageStudent", params);

        assertTrue(sql.contains("term_id = ?"));
        assertTrue(sql.contains("te.term_name AS termName"));
        assertTrue(sql.contains("GROUP BY term_id, student_no, student_name"));
    }

    /**
     * 未来学期服务发布通知家长时只允许查询目标学期学生快照。
     */
    @Test
    public void bindParentQueryShouldFilterByTargetTerm() {
        AsStudentBindParentAO query = new AsStudentBindParentAO();
        query.setSchoolId(1L);
        query.setTermId(100L);
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        String sql = sql("querytBindParentByGradeOrClass", params);

        assertTrue(sql.contains("stu.term_id = ?"));
    }

    /**
     * 审核统计与学生列表必须复用同一学期条件。
     */
    @Test
    public void auditSummaryShouldFilterByTerm() {
        Map<String, Object> params = new HashMap<>();
        params.put("schoolId", 1L);
        params.put("termId", 100L);

        String sql = sql("selectStudentAuditSummary", params);

        assertTrue(sql.contains("t.term_id = ?"));
    }

    private String sql(String statement, Object params) {
        BoundSql boundSql = configuration.getMappedStatement(NAMESPACE + statement).getBoundSql(params);
        return boundSql.getSql().replaceAll("\\s+", " ").trim();
    }
}
