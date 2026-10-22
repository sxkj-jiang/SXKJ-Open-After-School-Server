package com.sxkj.edu.common.utils.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;

/**
 * @Author Administrator
 * @Date 2024/12/3 18:01
 */
@Slf4j
public class ExcelReadImageUtil {

    public static <T> void readImage(InputStream inputStream, List<T> list) {
        try {
            Workbook sheets = WorkbookFactory.create(inputStream);
            // 默认读取第一页  
            XSSFSheet sheet = (XSSFSheet) sheets.getSheetAt(0);
            List<POIXMLDocumentPart> relations = sheet.getRelations();
            for (POIXMLDocumentPart poixmlDocumentPart : relations) {
                if (poixmlDocumentPart instanceof XSSFDrawing) {
                    XSSFDrawing drawing = (XSSFDrawing) poixmlDocumentPart;
                    List<XSSFShape> shapes = drawing.getShapes();
                    for (int i = 0; i < shapes.size(); i++) {
                        XSSFPicture picture = (XSSFPicture) shapes.get(i);
                        PictureData pictureData = picture.getPictureData();
                        String extension = pictureData.suggestFileExtension();
                        byte[] bytes = pictureData.getData();
                        T item = list.get(i);
                        Class clazz = item.getClass();
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field field : fields) {
                            if (field.isAnnotationPresent(ExcelImageProperty.class)) {
                                field.setAccessible(true);
                                String s = Base64.getEncoder().encodeToString(bytes);
                                field.set(item, s);
                                break;
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            log.error("解析失败：{}", e);
        }
    }
}
