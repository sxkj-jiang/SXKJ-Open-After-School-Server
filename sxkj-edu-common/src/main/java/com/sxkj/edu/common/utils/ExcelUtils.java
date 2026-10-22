package com.sxkj.edu.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.sxkj.edu.common.utils.excel.ExcelListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从Excel中读取文件，读取的类必须继承BaseRowModel
 * inputStream 文件输入流
 * clazz       继承该类必须继承BaseRowModel的类
 *
 * @return 读取完成的list
 */
@Slf4j
public class ExcelUtils {
    public static <T extends BaseRowModel> List<T> readExcel(final InputStream inputStream, final Class<? extends BaseRowModel> clazz) {
        if (null == inputStream) {
            throw new NullPointerException("the inputStream is null!");
        }
        AnalysisEventListener listener = new ExcelListener();
        //读取xls 和 xlxs格式
        //如果POI版本为3.17，可以如下声明
        ExcelReader reader = new ExcelReader(inputStream, null, listener);
        //判断格式，针对POI版本低于3.17
        //ExcelTypeEnum excelTypeEnum = valueOf(inputStream);
        //ExcelReader reader = new ExcelReader(inputStream, excelTypeEnum, null, listener);
        reader.read(new com.alibaba.excel.metadata.Sheet(1, 1, clazz));

        return ((ExcelListener) listener).getData();
    }

    public static <T extends BaseRowModel> List<T> customerReadExcel(final InputStream inputStream, final Class<? extends BaseRowModel> clazz, final Integer lineNum) {
        if (null == inputStream) {
            throw new NullPointerException("the inputStream is null!");
        }
        AnalysisEventListener listener = new ExcelListener();
        //读取xls 和 xlxs格式
        //如果POI版本为3.17，可以如下声明
        ExcelReader reader = new ExcelReader(inputStream, null, listener);
        //判断格式，针对POI版本低于3.17
        //ExcelTypeEnum excelTypeEnum = valueOf(inputStream);
        //ExcelReader reader = new ExcelReader(inputStream, excelTypeEnum, null, listener);
        reader.read(new com.alibaba.excel.metadata.Sheet(1, lineNum, clazz));

        return ((ExcelListener) listener).getData();
    }

    public static <T extends BaseRowModel> List<T> customerReadExcel(final InputStream inputStream, final Class<? extends BaseRowModel> clazz, final Integer sheetNo, final Integer lineNum) {
        if (null == inputStream) {
            throw new NullPointerException("the inputStream is null!");
        }
        AnalysisEventListener listener = new ExcelListener();
        //读取xls 和 xlxs格式
        //如果POI版本为3.17，可以如下声明
        ExcelReader reader = new ExcelReader(inputStream, null, listener);
        //判断格式，针对POI版本低于3.17
        //ExcelTypeEnum excelTypeEnum = valueOf(inputStream);
        //ExcelReader reader = new ExcelReader(inputStream, excelTypeEnum, null, listener);
        reader.read(new com.alibaba.excel.metadata.Sheet(sheetNo, lineNum, clazz));

        return ((ExcelListener) listener).getData();
    }

    /**
     * 获取Excel中的图片及图片所在行数
     *
     * @param inputStream
     * @return
     */
    public static Map<Integer, XSSFPictureData> getPictures(final InputStream inputStream) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

        //获取sheet表格，及读取单元格内容
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        Map<Integer, XSSFPictureData> map = new HashMap<>();
        XSSFDrawing drawing = xssfSheet.getDrawingPatriarch();
        if (drawing == null) return map;
        List<XSSFShape> list = drawing.getShapes();

        for (XSSFShape shape : list) {

            XSSFPicture picture = (XSSFPicture) shape;
            XSSFClientAnchor xssfClientAnchor = (XSSFClientAnchor) picture.getAnchor();
            XSSFPictureData pdata = picture.getPictureData();
            // 行号-列号
            Integer key = xssfClientAnchor.getRow1();
            map.put(key, pdata);
        }
        return map;
    }

    /**
     * 获取图片文件名
     *
     * @param picture 图片文件
     * @return 文件名
     */
    public static String getPictureFileName(XSSFPictureData picture) {
        String mimeType = picture.getMimeType();
        String extension;

        // 根据MIME类型确定文件扩展名
        switch (mimeType) {
            case "image/jpeg":
                extension = ".jpg";
                break;
            case "image/png":
                extension = ".png";
                break;
            case "image/gif":
                extension = ".gif";
                break;
            // 其他类型...
            default:
                extension = ".dat";
        }

        return "image" + System.currentTimeMillis() + extension;
    }

    public static <T> void export(HttpServletResponse response, List<T> dataList, Class<T> clazz, String sheetName) {
        // 头的策略
        try {
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            WriteFont headWriteFont = new WriteFont();
            headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            headWriteCellStyle.setWriteFont(headWriteFont);
            headWriteFont.setFontHeightInPoints((short) 12);
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            // 设置单元格上下左右边框为细边框
            contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
            contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
            contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
            contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            String fileName = URLEncoder.encode(sheetName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), clazz).registerWriteHandler(horizontalCellStyleStrategy).sheet(sheetName).doWrite(dataList);
        } catch (IOException e) {
            log.error("call excelUtil export in error");
        }

//        // 设置响应头：告诉浏览器下载文件
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setCharacterEncoding("UTF-8");
//
//        response.setHeader("Content-Disposition", "attachment;filename=" + sheetName + ".xlsx");
//        try {
//            ExcelUtils.export(response.getOutputStream(), dataList, clazz, sheetName);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static <T> void export(OutputStream outputStream, List<T> dataList, Class<T> clazz, String sheetName) {
        EasyExcel.write(outputStream, clazz)
                .sheet(sheetName)
                .doWrite(dataList);
    }

    /**
     * 通用Excel导出（直接写入HttpServletResponse）
     * 封装响应头设置、样式策略和EasyExcel写出
     *
     * @param response  HTTP响应
     * @param dataList  导出数据列表
     * @param clazz     Excel映射类
     * @param fileName  下载文件名（不含后缀）
     * @param sheetName Sheet名称
     */
    public static <T> void exportExcel(HttpServletResponse response, List<T> dataList, Class<T> clazz, String fileName, String sheetName) {
        try {
            setExcelResponseHeaders(response, fileName);
            HorizontalCellStyleStrategy strategy = buildCommonCellStyle();
            EasyExcel.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(strategy)
                    .sheet(sheetName)
                    .doWrite(dataList);
        } catch (Exception e) {
            log.error("Excel导出异常", e);
        }
    }

    /**
     * 通用Excel导出（写入指定OutputStream，适用于大数量导出后上传文件等场景）
     *
     * @param outputStream 输出流
     * @param dataList     导出数据列表
     * @param clazz        Excel映射类
     * @param sheetName    Sheet名称
     */
    public static <T> void exportExcel(OutputStream outputStream, List<T> dataList, Class<T> clazz, String sheetName) {
        try {
            HorizontalCellStyleStrategy strategy = buildCommonCellStyle();
            EasyExcel.write(outputStream, clazz)
                    .registerWriteHandler(strategy)
                    .sheet(sheetName)
                    .doWrite(dataList);
        } catch (Exception e) {
            log.error("Excel导出异常", e);
        }
    }

    /**
     * 设置Excel下载响应头
     */
    public static void setExcelResponseHeaders(HttpServletResponse response, String fileName) throws Exception {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
    }

    private static HorizontalCellStyleStrategy buildCommonCellStyle() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headWriteCellStyle.setWriteFont(headWriteFont);
        headWriteFont.setFontHeightInPoints((short) 12);
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

}


