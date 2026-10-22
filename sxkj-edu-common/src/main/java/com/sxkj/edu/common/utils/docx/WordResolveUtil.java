package com.sxkj.edu.common.utils.docx;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 *@Description : 
 *@Author : jjj$
 *@Date : 2024$ 0929$
 *@Company : SXKJ
 *@Version : 1.0
 **/
public class WordResolveUtil {

    public static void resolveDocx(){
        // 读取Word文档
        XWPFDocument document = null;
        try {
            FileInputStream fis = new FileInputStream("D:\\003_项目\\sxjypt\\模板\\录取审核申请模板.docx");
            document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            // 遍历并打印每个段落
            for (XWPFParagraph para : paragraphs) {
                System.out.println(para.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        resolveDocx();
    }
}
