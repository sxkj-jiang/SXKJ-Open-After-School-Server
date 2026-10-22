package com.sxkj.edu.module.afterschool.mapper;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/**
 * 家长学生关系查询语句契约测试。
 */
public class AsGuardianStudentMapperSqlTest {

    /**
     * 学生详情中的当前监护人必须排除已解绑关系。
     */
    @Test
    public void studentGuardianDetailShouldFilterBindStatus() throws Exception {
        String xml = new String(Files.readAllBytes(Paths.get(
                "src/main/resources/mapper/khfw/AsGuardianStudentMapper.xml")),
                StandardCharsets.UTF_8);
        int start = xml.indexOf("id=\"getStuParentDetailByStudentId\"");
        int end = xml.indexOf("</select>", start);
        String statement = xml.substring(start, end);

        assertTrue(statement.contains("g.bind_status = 1"));
        assertTrue(statement.contains("s.id = #{studentId}"));
    }

    /**
     * 评价任务查询家长绑定学生时必须限定到任务所属学期。
     */
    @Test
    public void evaluationStudentShouldFilterTerm() throws Exception {
        String xml = new String(Files.readAllBytes(Paths.get(
                "src/main/resources/mapper/khfw/AsGuardianStudentMapper.xml")),
                StandardCharsets.UTF_8);
        int start = xml.indexOf("id=\"queryBindParentTaskByStuParentIds\"");
        int end = xml.indexOf("</select>", start);
        String statement = xml.substring(start, end);

        assertTrue(statement.contains("s.student_identity_id = g.student_identity_id"));
        assertTrue(statement.contains("s.term_id = #{termId}"));
    }
}
