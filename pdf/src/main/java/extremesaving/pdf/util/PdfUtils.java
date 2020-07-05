package extremesaving.pdf.util;

import java.io.IOException;
import java.math.BigDecimal;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

public final class PdfUtils {

    private PdfUtils() {
    }

    public static Paragraph getItemParagraph(String text) {
        return getItemParagraph(text, false);
    }

    public static Paragraph getItemParagraph(String text, boolean bold) {
        return getItemParagraph(text, bold, null);
    }

    public static Paragraph getItemParagraph(String text, boolean bold, TextAlignment textAlignment) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(7);
        if (bold) {
            paragraph.setBold();
        }
        try {
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.COURIER);
            paragraph.setFont(regular);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (textAlignment != null) {
            paragraph.setTextAlignment(textAlignment);
        }
        return paragraph;
    }

    public static Paragraph getTitleParagraph(String text, TextAlignment textAlignment) {
        Paragraph titleParagraph = new Paragraph(text);
        titleParagraph.setBold();
        titleParagraph.setFontSize(6);
        titleParagraph.setTextAlignment(textAlignment);
        try {
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.COURIER);
            titleParagraph.setFont(regular);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titleParagraph;
    }

    public static String formatNumber(BigDecimal val) {
        return formatNumber(val, true);
    }

    public static String formatNumber(BigDecimal val, boolean decimals) {
        if (decimals) {
            return "€ " +  String.format("%.2f", val);
        }
        return "€ " + val.intValue();
    }

    public static String formatPercentage(BigDecimal val) {
        return val.intValue() + "%";
    }
}