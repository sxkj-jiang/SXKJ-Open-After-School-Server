package com.sxkj.edu.common.utils;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PDFUtil {

    public static void main(String[] args) throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("name","张三哈");
        map.put("sex","男");
        map.put("birthYear","1994");
        map.put("birthMonth","2");
        map.put("birthDay","20");
        map.put("startYear","2013");
        map.put("startMonth","7");
        map.put("endYear","2017");
        map.put("endMonth","7");
        map.put("majorName","计算机");
        map.put("yearLevel","2.5年制");
        map.put("studyLevel","专科");
        map.put("schoolName","北京大学");
        map.put("schoolMaster","李四");
        map.put("approveNumber","43118800000001");
        map.put("certNumber","431123888800001236");
        map.put("year","2023");
        map.put("month","02");
        map.put("day","21");
        FileInputStream fileInputStream = new FileInputStream("D:/data/opt/format/certTemplate3.pdf");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedImage bufferedImage = ImageIO.read(new File("D:/data/opt/format/test.png"));
        ImageIO.write(bufferedImage, "png", bos);
//        generateCert(fileInputStream, map, bos.toByteArray());
        ByteArrayOutputStream byteArrayOutputStream = generateCert(fileInputStream, map, bos.toByteArray());
        FileOutputStream os = new FileOutputStream(new File("D:/data/opt/format/certTemplate-result.pdf"));
        os.write(byteArrayOutputStream.toByteArray());
//        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
//        PdfReader pdfReader=new PdfReader("D:\\Project\\certTemplate.pdf");
//        PdfStamper pdfStamper=new PdfStamper(pdfReader, new FileOutputStream("D:\\Project\\aa.pdf"));
//        AcroFields form = pdfStamper.getAcroFields();
//        form.addSubstitutionFont(baseFont);
//        for(String key:map.keySet()){
//            String value=map.get(key);
//            //key对应模板数据域的名称
//            form.setField(key,value);
//        }
//        int pageNo = form.getFieldPositions("photo").get(0).page;
//        Rectangle signRect = form.getFieldPositions("photo").get(0).position;
//        float x = signRect.getLeft();
//        float y = signRect.getBottom();
//        Image image = Image.getInstance("D:\\Project\\studentPicture.png");
//        PdfContentByte under = pdfStamper.getOverContent(pageNo);
//        //设置图片大小
//        image.scaleAbsolute(signRect.getWidth(), signRect.getHeight());
//        //设置图片位置
//        image.setAbsolutePosition(x, y);
//        under.addImage(image);
//        pdfStamper.setFormFlattening(true);
//        pdfStamper.flush();
    }

    /**
     * certTemplateInputStream 证书模板文件流
     * map 填充的数据
     * stuPhoto 学生头像文件流
     * @return
     */
    public static ByteArrayOutputStream generateCert(InputStream certTemplateInputStream, Map<String,String> map,byte[] stuPhoto) throws Exception{
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);

        PdfReader pdfReader=new PdfReader(certTemplateInputStream);
        //输出文件字节流
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfStamper pdfStamper=new PdfStamper(pdfReader, os);
        AcroFields form = pdfStamper.getAcroFields();
        form.addSubstitutionFont(baseFont);
        for(String key:map.keySet()){
            String value=map.get(key);
//            form.setFieldProperty(key, "textsize", 13.0f, null);
            //key对应模板数据域的名称
            form.setField(key,value);
        }
        //设置图片
        Image image = Image.getInstance(stuPhoto);
        PushbuttonField pushbuttonField = form.getNewPushbuttonFromField("photo_af_image");
        pushbuttonField.setImage(image);
        PdfFormField pdfFormField = pushbuttonField.getField();
        form.replacePushbuttonField("photo_af_image", pdfFormField);
        pdfStamper.setFormFlattening(true);
        pdfStamper.close();
        return os;
    }

}
