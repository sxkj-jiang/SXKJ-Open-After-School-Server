package com.sxkj.edu.module.afterschool.mapper;

import com.sxkj.edu.module.afterschool.domain.dto.EduclClassQueryAO;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 行政班级分页 Mapper SQL 契约测试。
 */
public class EduclClassMapperSqlTest {

    private static final String NAMESPACE = "com.sxkj.edu.module.afterschool.mapper.EduclClassMapper.";

    private Configuration configuration;

    /**
     * 加载班级 Mapper XML，用于校验动态 SQL。
     */
    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        try (InputStream input = Resources.getResourceAsStream("mapper/khfw/EduclClassMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    input,
                    configuration,
                    "mapper/khfw/EduclClassMapper.xml",
                    configuration.getSqlFragments()
            );
            builder.parse();
        }
    }

    /**
     * 传入学期时按该学期有效学生关联班级，不传时保持原查询。
     */
    @Test
    public void pageQueryShouldFilterClassesByStudentTermOnlyWhenSpecified() {
        EduclClassQueryAO termQuery = new EduclClassQueryAO();
        termQuery.setSchoolId(6L);
        termQuery.setTermId(19L);
        assertTrue(sql(termQuery).contains("stu.term_id = ?"));

        EduclClassQueryAO defaultQuery = new EduclClassQueryAO();
        defaultQuery.setSchoolId(6L);
        assertFalse(sql(defaultQuery).contains("stu.term_id = ?"));
    }

    /**
     * 行政班通知查询学生时必须限定到通知所属学期。
     */
    @Test
    public void noticeStudentQueryShouldFilterTerm() {
        Map<String, Object> params = new HashMap<>();
        params.put("schoolId", 6L);
        params.put("termId", 19L);
        params.put("noticeObjIdList", Arrays.asList(1L, 2L));
        params.put("studentIds", null);

        BoundSql boundSql = configuration.getMappedStatement(
                NAMESPACE + "queryStudentsByClassIds").getBoundSql(params);
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim();

        assertTrue(sql.contains("stu.term_id = ?"));
    }

    /**
     * 生成指定查询对象对应的分页 SQL。
     */
    private String sql(EduclClassQueryAO query) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);
        BoundSql boundSql = configuration.getMappedStatement(NAMESPACE + "pageList").getBoundSql(params);
        return boundSql.getSql().replaceAll("\\s+", " ").trim();
    }
}
