package extremesaving.pdf.component.paragraph;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

public class CategoryParagraphComponent extends Paragraph {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryParagraphComponent.class);

    private boolean useRowColors;
    private Color cellBackgroundColor;

    public CategoryParagraphComponent(String text, boolean useRowColors) {
        this(text, false, null, useRowColors, null);
    }

    public CategoryParagraphComponent(String text, boolean useRowColors, Color cellBackgroundColor) {
        this(text, false, null, useRowColors, cellBackgroundColor);
    }

    public CategoryParagraphComponent(String text, boolean bold, boolean useRowColors, Color cellBackgroundColor) {
        this(text, bold, null, useRowColors, cellBackgroundColor);
    }

    public CategoryParagraphComponent(String text, boolean bold, TextAlignment textAlignment, boolean useRowColors, Color cellBackgroundColor) {
        this(text, bold, textAlignment, useRowColors, cellBackgroundColor, null);
    }

    public CategoryParagraphComponent(String text, boolean bold, TextAlignment textAlignment, boolean useRowColors, Color cellBackgroundColor, Color textColor) {
        this(text, bold, textAlignment, useRowColors, cellBackgroundColor, textColor, 0);
    }

    public CategoryParagraphComponent(String text, boolean bold, TextAlignment textAlignment, boolean useRowColors, Color cellBackgroundColor, Color textColor, Integer paddingRight) {
        super(text);
        if (paddingRight != null) {
            setPaddingRight(paddingRight);
        }
        if (cellBackgroundColor != null) {
            setCellBackgroundColor(cellBackgroundColor);
        }
        if (textColor != null) {
            setFontColor(textColor);
        }
        setFontSize(7);
        if (bold) {
            setBold();
        }
        try {
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.COURIER);
            setFont(regular);
        } catch (IOException e) {
            LOGGER.error("Unable to set font on PDF Paragraph.", e);
        }
        if (textAlignment != null) {
            setTextAlignment(textAlignment);
        }
        setUseRowColors(useRowColors);
    }

    public boolean isUseRowColors() {
        return useRowColors;
    }

    public void setUseRowColors(final boolean useRowColors) {
        this.useRowColors = useRowColors;
    }

    public Color getCellBackgroundColor() {
        return cellBackgroundColor;
    }

    public void setCellBackgroundColor(final Color cellBackgroundColor) {
        this.cellBackgroundColor = cellBackgroundColor;
    }
}