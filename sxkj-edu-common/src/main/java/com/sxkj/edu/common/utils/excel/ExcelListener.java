package com.sxkj.edu.common.utils.excel;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 处理easyExcel，将读取到数据保存为对象并输出
 */
@Slf4j
public class ExcelListener<T> extends AnalysisEventListener<T> {

    /**
     * 自定义用于暂时存储data。
     * 可以通过实例获取该值
     */
    private final List<T> data = new ArrayList<>();

    @Override
    public void invoke(T object, AnalysisContext context) {
        log.info("数据："+object.toString());
        //数据存储
        data.add(object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    public List<T> getData() {
        return data;
    }

}

