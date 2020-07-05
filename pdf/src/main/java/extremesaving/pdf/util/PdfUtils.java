package extremesaving.pdf.util;

import java.io.IOException;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

/**
 * Utility class for handling the PDF.
 */
public final class PdfUtils {

    private PdfUtils() {
    }

    /**
     * Get a Paragraph for normal text.
     *
     * @param text Text to be set on the Paragraph.
     * @return Paragraph to be used on a PDF-file.
     */
    public static Paragraph getItemParagraph(String text) {
        return getItemParagraph(text, false);
    }

    /**
     * Get a Paragraph for normal bold text.
     *
     * @param text Text to be set on the Paragraph.
     * @param bold True if the text has to be bold.
     * @return Paragraph to be used on a PDF-file.
     */
    public static Paragraph getItemParagraph(String text, boolean bold) {
        return getItemParagraph(text, bold, null);
    }

    /**
     * Get a Paragraph for normal text.
     *
     * @param text Text to be set on the Paragraph.
     * @param bold True if the text has to be bold.
     * @param textAlignment TextAlignment of the Paragraph.
     * @return Paragraph to be used on a PDF-file.
     */
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

    /**
     * Get a Paragraph for titles.
     *
     * @param text Text to be set on the Paragraph.
     * @param textAlignment TextAlignment of the Paragraph.
     * @return Paragraph to be used on a PDF-file.
     */
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
}