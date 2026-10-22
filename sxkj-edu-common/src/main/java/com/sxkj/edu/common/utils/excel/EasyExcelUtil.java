package com.sxkj.edu.common.utils.excel;




import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.sxkj.edu.common.factory.EasyExcelWriterFactory;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author:         yangjie
 * Datetime:       2022年11月16日 10:57
 * Description:    注：如果方法中无以下参数，则使用默认值
 * Integer sheetNo       默认0读取第一张sheet
 * Integer headRowNum    默认1不读取首行，从第二行开始读取
 * 返回值return：List<Map<Integer,String>>: Integer:列数  String:列数对应的value
 */
public class EasyExcelUtil {

    /**
     * 同步无模型读（默认读取sheet0,从第2行开始读）
     *
     * @param filePath excel文件的绝对路径
     */
    public static List<Map<Integer, String>> syncRead(String filePath) {
        return EasyExcelFactory.read(filePath).sheet().doReadSync();
    }

    /**
     * 同步无模型读（自定义读取sheetX，从第2行开始读）
     *
     * @param filePath excel文件的绝对路径
     * @param sheetNo  sheet页号，从0开始
     */
    public static List<Map<Integer, String>> syncRead(String filePath, Integer sheetNo) {
        return EasyExcelFactory.read(filePath).sheet(sheetNo).doReadSync();
    }

    /**
     * 同步无模型读（指定sheet和表头占的行数）
     *
     * @param filePath
     * @param sheetNo    sheet页号，从0开始
     * @param headRowNum 表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static List<Map<Integer, String>> syncRead(String filePath, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(filePath).sheet(sheetNo).headRowNumber(headRowNum).doReadSync();
    }

    /**
     * 同步无模型读（指定sheet和表头占的行数）
     *
     * @param inputStream
     * @param sheetNo     sheet页号，从0开始
     * @param headRowNum  表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static List<Map<Integer, String>> syncRead(InputStream inputStream, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(inputStream).sheet(sheetNo).headRowNumber(headRowNum).doReadSync();
    }

    /**
     * 同步无模型读（指定sheet和表头占的行数）
     *
     * @param file
     * @param sheetNo    sheet页号，从0开始
     * @param headRowNum 表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static List<Map<Integer, String>> syncRead(File file, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(file).sheet(sheetNo).headRowNumber(headRowNum).doReadSync();
    }
//====================================================无JAVA模型读取excel数据===============================================================

//====================================================将excel数据同步到JAVA模型属性里===============================================================

    /**
     * 同步按模型读（默认读取sheet0,从第2行开始读）
     *
     * @param filePath
     * @param clazz    模型的类类型（excel数据会按该类型转换成对象）
     */
    public static List<T> syncReadModel(String filePath, Class clazz) {
        return EasyExcelFactory.read(filePath).sheet().head(clazz).doReadSync();
    }

    /**
     * 同步按模型读（默认表头占一行，从第2行开始读）
     *
     * @param filePath
     * @param clazz    模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo  sheet页号，从0开始
     */
    public static List<T> syncReadModel(String filePath, Class clazz, Integer sheetNo) {
        return EasyExcelFactory.read(filePath).sheet(sheetNo).head(clazz).doReadSync();
    }

    /**
     * 同步按模型读（指定sheet和表头占的行数）
     *
     * @param inputStream
     * @param clazz       模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo     sheet页号，从0开始
     * @param headRowNum  表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static List<T> syncReadModel(InputStream inputStream, Class clazz, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(inputStream).sheet(sheetNo).headRowNumber(headRowNum).head(clazz).doReadSync();
    }

    /**
     * 同步按模型读（指定sheet和表头占的行数）
     *
     * @param file
     * @param clazz      模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo    sheet页号，从0开始
     * @param headRowNum 表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static List<T> syncReadModel(File file, Class clazz, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(file).sheet(sheetNo).headRowNumber(headRowNum).head(clazz).doReadSync();
    }

    /**
     * 同步按模型读（指定sheet和表头占的行数）
     *
     * @param filePath
     * @param clazz      模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo    sheet页号，从0开始
     * @param headRowNum 表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static List<T> syncReadModel(String filePath, Class clazz, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(filePath).sheet(sheetNo).headRowNumber(headRowNum).head(clazz).doReadSync();
    }

    /**
     * 异步无模型读（默认读取sheet0,从第2行开始读）
     *
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param filePath      表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncRead(String filePath, AnalysisEventListener<T> excelListener) {
        EasyExcelFactory.read(filePath, excelListener).sheet().doRead();
    }

    /**
     * 异步无模型读（默认表头占一行，从第2行开始读）
     *
     * @param filePath      表头占的行数，从0开始（如果要连表头一起读出来则传0）
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param sheetNo       sheet页号，从0开始
     */
    public static void asyncRead(String filePath, AnalysisEventListener<T> excelListener, Integer sheetNo) {
        EasyExcelFactory.read(filePath, excelListener).sheet(sheetNo).doRead();
    }

    /**
     * 异步无模型读（指定sheet和表头占的行数）
     *
     * @param inputStream
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncRead(InputStream inputStream, AnalysisEventListener<T> excelListener, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(inputStream, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 异步无模型读（指定sheet和表头占的行数）
     *
     * @param file
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncRead(File file, AnalysisEventListener<T> excelListener, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(file, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 异步无模型读（指定sheet和表头占的行数）
     *
     * @param filePath
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     * @return
     */
    public static void asyncRead(String filePath, AnalysisEventListener<T> excelListener, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(filePath, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 异步按模型读取（默认读取sheet0,从第2行开始读）
     *
     * @param filePath
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param clazz         模型的类类型（excel数据会按该类型转换成对象）
     */
    public static void asyncReadModel(String filePath, AnalysisEventListener<T> excelListener, Class clazz) {
        EasyExcelFactory.read(filePath, clazz, excelListener).sheet().doRead();
    }

    /**
     * 异步按模型读取（默认表头占一行，从第2行开始读）
     *
     * @param filePath
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param clazz         模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo       sheet页号，从0开始
     */
    public static void asyncReadModel(String filePath, AnalysisEventListener<T> excelListener, Class clazz, Integer sheetNo) {
        EasyExcelFactory.read(filePath, clazz, excelListener).sheet(sheetNo).doRead();
    }

    /**
     * 异步按模型读取
     *
     * @param inputStream
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param clazz         模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncReadModel(InputStream inputStream, AnalysisEventListener<T> excelListener, Class clazz, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(inputStream, clazz, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 异步按模型读取
     *
     * @param file
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param clazz         模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncReadModel(File file, AnalysisEventListener<T> excelListener, Class clazz, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(file, clazz, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 异步按模型读取
     *
     * @param filePath
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param clazz         模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncReadModel(String filePath, AnalysisEventListener<T> excelListener, Class clazz, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(filePath, clazz, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 无模板写文件
     *
     * @param filePath
     * @param head     表头数据
     * @param data     表内容数据
     */
    public static void write(String filePath, List<List<String>> head, List<List<Object>> data) {
        EasyExcel.write(filePath).head(head).sheet().doWrite(data);
    }

    /**
     * 无模板写文件
     *
     * @param filePath
     * @param head      表头数据
     * @param data      表内容数据
     * @param sheetNo   sheet页号，从0开始
     * @param sheetName sheet名称
     */
    public static void write(String filePath, List<List<String>> head, List<List<Object>> data, Integer sheetNo, String sheetName) {
        EasyExcel.write(filePath).head(head).sheet(sheetNo, sheetName).doWrite(data);
    }

    /**
     * 根据excel模板文件写入文件
     *
     * @param filePath
     * @param templateFileName
     * @param headClazz
     * @param data
     */
    public static void writeTemplate(String filePath, String templateFileName, Class headClazz, List data) {
        EasyExcel.write(filePath, headClazz).withTemplate(templateFileName).sheet().doWrite(data);
    }

    /**
     * 根据excel模板文件写入文件
     *
     * @param filePath
     * @param templateFileName
     * @param data
     */
    public static void writeTemplate(String filePath, String templateFileName, List data) {
        EasyExcel.write(filePath).withTemplate(templateFileName).sheet().doWrite(data);
    }

    /**
     * 按模板写文件
     *
     * @param filePath
     * @param headClazz 表头模板
     * @param data      数据
     */
    public static void write(String filePath, Class headClazz, List data) {
        EasyExcel.write(filePath, headClazz).sheet().doWrite(data);
    }

    /**
     * 按模板写文件
     *
     * @param filePath
     * @param headClazz 表头模板
     * @param data      数据
     * @param sheetNo   sheet页号，从0开始
     * @param sheetName sheet名称
     */
    public static void write(String filePath, Class headClazz, List data, Integer sheetNo, String sheetName) {
        EasyExcel.write(filePath, headClazz).sheet(sheetNo, sheetName).doWrite(data);
    }

    /**
     * 按模板写文件
     *
     * @param filePath
     * @param headClazz    表头模板
     * @param data         数据
     * @param writeHandler 自定义的处理器，比如设置table样式，设置超链接、单元格下拉框等等功能都可以通过这个实现（需要注册多个则自己通过链式去调用）
     * @param sheetNo      sheet页号，从0开始
     * @param sheetName    sheet名称
     */
    public static void write(String filePath, Class headClazz, List data, WriteHandler writeHandler, Integer sheetNo, String sheetName) {
        EasyExcel.write(filePath, headClazz).registerWriteHandler(writeHandler).sheet(sheetNo, sheetName).doWrite(data);
    }

    /**
     * 按模板写文件（包含某些字段）
     *
     * @param filePath
     * @param headClazz   表头模板
     * @param data        数据
     * @param includeCols 包含字段集合，根据字段名称显示
     * @param sheetNo     sheet页号，从0开始
     * @param sheetName   sheet名称
     */
    public static void writeInclude(String filePath, Class headClazz, List data, Set<String> includeCols, Integer sheetNo, String sheetName) {
        EasyExcel.write(filePath, headClazz).includeColumnFiledNames(includeCols).sheet(sheetNo, sheetName).doWrite(data);
    }

    /**
     * 按模板写文件（排除某些字段）
     *
     * @param filePath
     * @param headClazz   表头模板
     * @param data        数据
     * @param excludeCols 过滤排除的字段，根据字段名称过滤
     * @param sheetNo     sheet页号，从0开始
     * @param sheetName   sheet名称
     */
    public static void writeExclude(String filePath, Class headClazz, List data, Set<String> excludeCols, Integer sheetNo, String sheetName) {
        EasyExcel.write(filePath, headClazz).excludeColumnFiledNames(excludeCols).sheet(sheetNo, sheetName).doWrite(data);
    }

    /**
     * 多个sheet页的数据链式写入
     * .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     * .write(headData, data,"sheetName2")
     * .finish();
     *
     * @param outputStream
     */
    public static EasyExcelWriterFactory writeWithSheets(OutputStream outputStream) {
        EasyExcelWriterFactory excelWriter = new EasyExcelWriterFactory(outputStream);
        return excelWriter;
    }

    /**
     * 多个sheet页的数据链式写入
     * .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     * .write(headData, data,"sheetName2")
     * .finish();
     *
     * @param file
     */
    public static EasyExcelWriterFactory writeWithSheets(File file) {
        EasyExcelWriterFactory excelWriter = new EasyExcelWriterFactory(file);
        return excelWriter;
    }

    /**
     * 多个sheet页的数据链式写入
     * .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     * .write(headData, data,"sheetName2")
     * .finish();
     *
     * @param filePath
     */
    public static EasyExcelWriterFactory writeWithSheets(String filePath) {
        EasyExcelWriterFactory excelWriter = new EasyExcelWriterFactory(filePath);
        return excelWriter;
    }

    /**
     * 多个sheet页的数据链式写入（失败了会返回一个有部分数据的Excel）
     * .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     * .write(headData, data,"sheetName2")
     * .finish();
     *
     * @param response
     * @param exportFileName 导出的文件名称
     */
    public static EasyExcelWriterFactory writeWithSheetsWeb(HttpServletResponse response, String exportFileName) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode(exportFileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcelWriterFactory excelWriter = new EasyExcelWriterFactory(response.getOutputStream());
        return excelWriter;
    }

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response 响应
     * @param fileName 文件名
     * @param sheetName Excel sheet 名
     * @param head Excel head 头
     * @param data 数据列表哦
     * @param <T> 泛型，保证 head 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String fileName, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {

        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        String fileNameURL = URLEncoder.encode(fileName, "UTF-8");

        response.setHeader("Content-disposition", "attachment;fileName="+fileNameURL+";"+"filename*=utf-8''"+fileNameURL);
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
                .autoCloseStream(false)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(sheetName).doWrite(data);

    }
}
