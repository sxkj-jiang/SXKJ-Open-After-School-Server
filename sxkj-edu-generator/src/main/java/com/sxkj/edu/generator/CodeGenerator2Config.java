package com.sxkj.edu.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

/**
 * 代码生成器配置
 */
@Data
public class CodeGenerator2Config {

    /**
     * 代码生成的目录. 默认 /tmp/mybatis-plus-generator
     */
    private String output = System.getProperty("user.dir") + "/sxkj-edu-service/src/main/java";

    private String dbUrl;

    private String dbUser;

    private String dbPassword;

    /**
     * 代码包. entity 和 mapper 的 java 父包
     */
    private String basePackage;

    /**
     *
     */
    private String moduleName;

    /**
     * jdbc 驱动名. 默认 mysql
     */
    private String dbDriverName = "com.mysql.cj.jdbc.Driver";

    /**
     * 默认的 id 生成类型. 默认 UUID
     */
    private IdType idType = IdType.ASSIGN_ID;

    /**
     * entity 的基类. 如：com.sxkj.edu.common.bean.BasePOJO 暂时不用
     */
    private String superEntityClass = "";

}
