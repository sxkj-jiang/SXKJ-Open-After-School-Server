package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * 家长评价任务 Mapper 学生范围 SQL 契约测试。
 */
public class AsEvaluationTaskMapperSqlTest {

    private static final String NAMESPACE =
            "com.sxkj.edu.module.afterschool.mapper.AsEvaluationTaskMapper.";

    private Configuration configuration;

    /**
     * 加载评价任务 Mapper XML。
     */
    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        try (InputStream input = Resources.getResourceAsStream(
                "mapper/khfw/AsEvaluationTaskMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    input,
                    configuration,
                    "mapper/khfw/AsEvaluationTaskMapper.xml",
                    configuration.getSqlFragments());
            builder.parse();
        }
    }

    /**
     * 家长已评价状态必须按当前选择的学生快照隔离。
     */
    @Test
    public void guardianEvaluatedStatusShouldFilterSelectedStudent() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", new Page<>(1, 10));
        params.put("schoolId", 3L);
        params.put("studentId", 120L);
        params.put("userId", 100L);
        params.put("tabType", 1);
        params.put("termId", 8L);

        BoundSql boundSql = configuration
                .getMappedStatement(NAMESPACE + "queryGuardianEvaluationTaskPage")
                .getBoundSql(params);
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim();

        assertTrue(sql.contains("a.evaluator_user_id = ?"));
        assertTrue(sql.contains("a.target_student_id = ?"));
        assertTrue(sql.contains("e.student_id = ?"));
        assertTrue(sql.contains("srv.term_id = ?"));
    }
}
