package com.sxkj.edu.module.afterschool.mapper;

import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassCountQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassSessionDetailQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsSessionAttendancePageQueryAO;
import com.sxkj.edu.module.afterschool.enums.AttendanceStatusEnum;
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
 * 课程班统计 Mapper SQL 契约测试。
 */
public class AsCourseClassMapperSqlTest {

    private static final String NAMESPACE = "com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper.";

    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        try (InputStream input = Resources.getResourceAsStream("mapper/khfw/AsCourseClassMapper.xml")) {
            XMLMapperBuilder builder = new XMLMapperBuilder(
                    input,
                    configuration,
                    "mapper/khfw/AsCourseClassMapper.xml",
                    configuration.getSqlFragments()
            );
            builder.parse();
        }
    }

    @Test
    public void courseClassListAndDetailShouldUseUnifiedPresentStatuses() {
        AsCourseClassPageQueryAO query = new AsCourseClassPageQueryAO();
        query.setSchoolId(100L);

        Map<String, Object> pageParams = presentStatusParams();
        pageParams.put("query", query);

        assertUsesCompositePresentStatus(sql("pageQueryCourseClass", pageParams));

        Map<String, Object> detailParams = presentStatusParams();
        detailParams.put("courseClassId", 200L);
        assertUsesCompositePresentStatus(sql("queryCourseClassAttendanceRate", detailParams));
    }

    @Test
    public void sessionAttendanceShouldReturnCompositePresentAndEarlyLeaveCounts() {
        AsSessionAttendancePageQueryAO query = new AsSessionAttendancePageQueryAO();
        query.setSchoolId(100L);
        query.setCourseClassId(200L);

        Map<String, Object> params = presentStatusParams();
        params.put("query", query);
        params.put("earlyLeaveStatusCode", AttendanceStatusEnum.EARLY_LEAVE.getCode());

        String sql = sql("pageQuerySessionAttendance", params);
        assertUsesCompositePresentStatus(sql);
        assertTrue(sql.contains("ar.attendance_status = ? THEN 1 ELSE 0 END) AS earlyLeaveCount"));
        assertTrue(sql.contains("srv.term_id AS termId"));
        assertTrue(sql.contains("te.term_name AS termName"));
    }

    @Test
    public void courseClassPageAndCountShouldUseSameTermScope() {
        AsCourseClassPageQueryAO pageQuery = new AsCourseClassPageQueryAO();
        pageQuery.setSchoolId(100L);
        pageQuery.setTermId(200L);
        Map<String, Object> pageParams = presentStatusParams();
        pageParams.put("query", pageQuery);
        String pageSql = sql("pageQueryCourseClass", pageParams);
        assertTrue(pageSql.contains("srv.term_id = ?"));
        assertTrue(pageSql.contains("srv.term_id AS termId"));

        AsCourseClassCountQueryAO countQuery = new AsCourseClassCountQueryAO();
        countQuery.setSchoolId(100L);
        countQuery.setTermId(200L);
        Map<String, Object> countParams = new HashMap<>();
        countParams.put("query", countQuery);
        String countSql = sql("countCourseClass", countParams);
        assertTrue(countSql.contains("srv.term_id = ?"));
    }

    /**
     * 行政班课程的年级展示应根据适用行政班反查，不能依赖不入库的适用年级字段。
     */
    @Test
    public void courseClassPageShouldResolveAdministrativeGradesFromClasses() {
        AsCourseClassPageQueryAO query = new AsCourseClassPageQueryAO();
        query.setSchoolId(100L);
        query.setEnrollType(3);
        Map<String, Object> params = presentStatusParams();
        params.put("query", query);

        String sql = sql("pageQueryCourseClass", params);

        assertTrue(sql.contains("CASE WHEN cc.enroll_type = 3"));
        assertTrue(sql.contains("grade_scope.resolved_grade_ids"));
        assertTrue(sql.contains("FROM t_educl_class ec_scope"));
        assertTrue(sql.contains("ccg.applicable_class_ids"));
        assertTrue(sql.contains("CAST(ec_scope.id AS JSON)"));
        assertTrue(sql.contains("ec_scope.grade_id = g.id"));
    }

    /**
     * 年级筛选应按课程类型区分：行政班按具体班级所属年级，公开和邀请课程按适用年级字段。
     */
    @Test
    public void courseClassPageShouldFilterAdministrativeCoursesByClassGrade() {
        AsCourseClassPageQueryAO query = new AsCourseClassPageQueryAO();
        query.setSchoolId(100L);
        query.setGradeId(24L);
        Map<String, Object> params = presentStatusParams();
        params.put("query", query);

        String sql = sql("pageQueryCourseClass", params);

        assertTrue(sql.contains("cc.enroll_type = 3"));
        assertTrue(sql.contains("FROM t_educl_class ec_filter"));
        assertTrue(sql.contains("cc.applicable_class_ids"));
        assertTrue(sql.contains("CAST(ec_filter.id AS JSON)"));
        assertTrue(sql.contains("ec_filter.grade_id = ?"));
        assertTrue(sql.contains("cc.enroll_type IN (1, 2)"));
        assertTrue(sql.contains("cc.applicable_grade_ids"));
        assertTrue(sql.contains("CAST(? AS JSON)"));
    }

    /**
     * 教师调课课程课次详情应返回课程所属学期。
     */
    @Test
    public void courseSessionDetailShouldReturnTerm() {
        AsCourseClassSessionDetailQueryAO query = new AsCourseClassSessionDetailQueryAO();
        query.setSchoolId(100L);
        query.setCourseClassId(200L);
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        String sql = sql("listSessionDetailByCourseClassId", params);

        assertTrue(sql.contains("srv.term_id AS termId"));
        assertTrue(sql.contains("te.term_name AS termName"));
        assertTrue(sql.contains("LEFT JOIN t_as_service srv"));
        assertTrue(sql.contains("LEFT JOIN t_educl_term te"));
    }

    private Map<String, Object> presentStatusParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("presentStatusCodes", AttendanceStatusEnum.getPresentCodes());
        return params;
    }

    private String sql(String statementId, Map<String, Object> params) {
        BoundSql boundSql = configuration.getMappedStatement(NAMESPACE + statementId).getBoundSql(params);
        return boundSql.getSql().replaceAll("\\s+", " ").trim();
    }

    private void assertUsesCompositePresentStatus(String sql) {
        assertTrue(sql.contains("ar.attendance_status IN ( ? , ? , ? )"));
        assertFalse(sql.contains("ar.attendance_status = 1"));
    }
}
