package com.sxkj.edu.module.afterschool.mapper;

import com.sxkj.edu.module.afterschool.domain.dto.EduclTermQueryAO;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 学期分页 Mapper SQL 契约测试。
 */
public class EduclTermMapperSqlTest {

    private static final String NAMESPACE = "com.sxkj.edu.module.afterschool.mapper.EduclTermMapper.";

    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        try (InputStream input = Resources.getResourceAsStream("mapper/khfw/EduclTermMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    input,
                    configuration,
                    "mapper/khfw/EduclTermMapper.xml",
                    configuration.getSqlFragments()
            );
            builder.parse();
        }
    }

    @Test
    public void pageQueryShouldIncludeEndedTermsByDefault() {
        EduclTermQueryAO query = new EduclTermQueryAO();
        query.setSchoolId(6L);

        assertTrue(query.getIsAll());
        assertFalse(sql(query).contains("t.end_date >= CURRENT_DATE()"));
    }

    /**
     * 显式不查全部学期时，应过滤已结束学期。
     */
    @Test
    public void pageQueryShouldExcludeEndedTermsWhenIsAllIsFalse() {
        EduclTermQueryAO query = new EduclTermQueryAO();
        query.setSchoolId(6L);
        query.setIsAll(false);

        assertTrue(sql(query).contains("t.end_date >= CURRENT_DATE()"));
    }

    private String sql(EduclTermQueryAO query) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);
        BoundSql boundSql = configuration.getMappedStatement(NAMESPACE + "pageList").getBoundSql(params);
        return boundSql.getSql().replaceAll("\\s+", " ").trim();
    }
}
