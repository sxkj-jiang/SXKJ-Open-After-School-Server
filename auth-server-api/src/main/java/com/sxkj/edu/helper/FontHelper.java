package com.sxkj.edu.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.InputStream;

@Component
@Slf4j
public class FontHelper {

    private static Font alibabaRegularFont;
    private static Font alibabaBoldFont;
    private static Font sourceHanSerifCNBoldFont;

    @PostConstruct
    public void init() {
        try (InputStream alibabaRegularFontStream = this.getClass().getClassLoader().getResourceAsStream("Alibaba-PuHuiTi-Regular.ttf");
             InputStream alibabaBoldFontStream = this.getClass().getClassLoader().getResourceAsStream("Alibaba-PuHuiTi-Bold.ttf");
             InputStream sourceHanSerifCNBoldFontStream = this.getClass().getClassLoader().getResourceAsStream("SourceHanSerifCN-Bold.ttf");
        ) {
            if (alibabaRegularFontStream != null) {
                alibabaRegularFont = Font.createFont(Font.TRUETYPE_FONT, alibabaRegularFontStream);
            }
            if (alibabaBoldFontStream != null) {
                alibabaBoldFont = Font.createFont(Font.TRUETYPE_FONT, alibabaBoldFontStream);
            }
            if (sourceHanSerifCNBoldFontStream != null) {
                sourceHanSerifCNBoldFont = Font.createFont(Font.TRUETYPE_FONT, sourceHanSerifCNBoldFontStream);
            }
        } catch (Exception e) {
            log.error("字体加载失败：", e);
            // 字体加载失败，使用默认字体
            alibabaRegularFont = new Font("雅黑", Font.BOLD, 40);
            alibabaBoldFont = new Font("雅黑", Font.BOLD, 40);
            sourceHanSerifCNBoldFont = new Font("Source Han Serif CN-Bold", Font.BOLD, 40);
        }
    }

    public Font getAlibabaRegularFont() {
        return alibabaRegularFont;
    }

    public Font getAlibabaBoldFont() {
        return alibabaBoldFont;
    }

    public Font getSourceHanSerifCNBoldFont() {
        return sourceHanSerifCNBoldFont;
    }
}
