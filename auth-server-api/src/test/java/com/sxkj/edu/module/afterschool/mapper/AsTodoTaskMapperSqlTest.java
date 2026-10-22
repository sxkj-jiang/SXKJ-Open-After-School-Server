package com.sxkj.edu.module.afterschool.mapper;

import com.sxkj.edu.module.afterschool.domain.dto.AsTodoTaskAdminPageQueryAO;
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
 * 待办任务 Mapper 学期查询契约测试。
 */
public class AsTodoTaskMapperSqlTest {

    private static final String NAMESPACE =
            "com.sxkj.edu.module.afterschool.mapper.AsTodoTaskMapper.";

    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        try (InputStream input = Resources.getResourceAsStream(
                "mapper/khfw/AsTodoTaskMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    input,
                    configuration,
                    "mapper/khfw/AsTodoTaskMapper.xml",
                    configuration.getSqlFragments());
            builder.parse();
        }
    }

    /**
     * 管理端待办分页应优先按固化学期查询，并兼容三类历史学期待办。
     */
    @Test
    public void pageAdminListShouldFilterStoredAndLegacySourceTerms() {
        AsTodoTaskAdminPageQueryAO query = new AsTodoTaskAdminPageQueryAO();
        query.setSchoolId(1L);
        query.setTermId(30L);
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        String sql = sql("pageAdminList", params);

        assertTrue(sql.contains("t.term_id AS termId"));
        assertTrue(sql.contains("term.term_name AS termName"));
        assertTrue(sql.contains("t.task_status = 0 OR"));
        assertTrue(sql.contains("t.term_id = ?"));
        assertTrue(sql.contains("t.source_biz_type = 'TEACHER_ADJUST'"));
        assertTrue(sql.contains("t.source_biz_type = 'INVITE_COURSE'"));
        assertTrue(sql.contains("t.source_biz_type = 'EVALUATE_COURSE'"));
        assertTrue(sql.contains("evaluation_service.term_id = ?"));
    }

    /**
     * 首页和详情也应直接返回待办固化的学期信息。
     */
    @Test
    public void homeAndDetailShouldSelectStoredTerm() {
        Map<String, Object> homeParams = new HashMap<>();
        homeParams.put("ao", new com.sxkj.edu.module.afterschool.domain.ao.AsTodoTaskHomePageAO());

        String homeSql = sql("listHomePageTasks", homeParams);
        String detailSql = sql("getTodoTaskDetail",
                java.util.Collections.<String, Object>singletonMap("id", 1L));

        assertTrue(homeSql.contains("t.term_id AS termId"));
        assertTrue(homeSql.contains("term.term_name AS termName"));
        assertTrue(detailSql.contains("t.term_id AS termId"));
        assertTrue(detailSql.contains("term.term_name AS termName"));
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
