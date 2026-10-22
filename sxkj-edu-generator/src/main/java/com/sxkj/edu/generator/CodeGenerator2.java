package com.sxkj.edu.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Scanner;


public class CodeGenerator2 {

    public static void generator(CodeGenerator2Config config) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(config.getOutput());
        gc.setAuthor("zhengqn");
        gc.setOpen(false); //生成后是否打开资源管理器
        gc.setFileOverride(true);
        gc.setBaseResultMap(false);
        gc.setBaseColumnList(false);
        gc.setDateType(DateType.ONLY_DATE);
        //gc.setEntityName("%sEntity");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setIdType(config.getIdType());
        gc.setSwagger2(true);//开启Swagger2模式
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(config.getDbUrl());
        dsc.setDriverName(config.getDbDriverName());
        dsc.setUsername(config.getDbUser());
        dsc.setPassword(config.getDbPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(config.getBasePackage());
        pc.setController("controller." + config.getModuleName());
        pc.setEntity("entity." + config.getModuleName());
        pc.setMapper("mapper." + config.getModuleName());
        pc.setXml("mapper." + config.getModuleName() + ".xml");
        pc.setService("service." + config.getModuleName());
        pc.setServiceImpl("service." + config.getModuleName() + ".impl");
        mpg.setPackageInfo(pc);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        //templateConfig.setController(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setTablePrefix("t_educl_");
//        strategy.csetSuperEntityClass(onfig.getSuperEntityClass());
        strategy.setEntityLombokModel(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setInclude(scanner("表名, 多个英文逗号分割").split(","));
        strategy.setEntitySerialVersionUID(false);
        strategy.setRestControllerStyle(true); //restful api风格控制器
        strategy.setControllerMappingHyphenStyle(false); //url中驼峰转连字符
        mpg.setStrategy(strategy);

        mpg.execute();
    }

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + ": ");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        CodeGenerator2Config config = new CodeGenerator2Config();
        config.setBasePackage("com.sxkj.edu");
        config.setDbUrl("jdbc:mysql://49.235.240.173:3306/edu_sys?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false");
        config.setDbUser("root");
        config.setDbPassword("Sxkj@.20231122");
        config.setModuleName("teach");
        CodeGenerator2.generator(config);
    }
}