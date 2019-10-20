package extremesaving.pdf.util;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.IOException;

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
        titleParagraph.setTextAlignment(textAlignment);
        try {
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.COURIER);
            titleParagraph.setFont(regular);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titleParagraph;
    }
}