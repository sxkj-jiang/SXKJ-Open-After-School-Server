package com.sxkj.edu.common.base.constants;

import lombok.Getter;

/**
 * @Description 批量导入模块枚举类
 * @Author xull
 * @Date 2022/9/15 22:09
 */
@Getter
public enum ImportModule {

    /**
     * 批量导入模块
     */
    TEACH_DESIGN("1", "教学设计", "教学设计.docx"),
    COURSEWARE("2", "辅导拓展", "辅导拓展.pptx"),
    STUDY_TASK("3", "学习任务单", "学习任务单.docx"),
    HOMEWORK_PRACTICE("4", "课后练习", "课后练习.docx"),
    ;


    /**
     * 编码
     */
    private final String code;
    /**
     * 描述
     */
    private final String desc;
    /**
     * 模板名称
     */
    private final String templateName;

    ImportModule(String code, String desc, String templateName) {
        this.code = code;
        this.desc = desc;
        this.templateName = templateName;
    }

    /**
     * 解析模块
     *
     * @param code 模块编码
     * @return 模块详情
     */
    public static ImportModule getByCode(String code) {
        for (ImportModule importModule : ImportModule.values()) {
            if (importModule.code.equals(code)) {
                return importModule;
            }
        }
        return null;
    }

}
