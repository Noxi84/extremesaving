package extremesaving.pdf.page.tipoftheday.section;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.pdf.util.PdfUtils;

public class TipOfTheDayPdfSectionCreator {

    private Cell chartCell;
    private String tipOfTheDayMessage;

    public TipOfTheDayPdfSectionCreator withMessage(String message) {
        this.tipOfTheDayMessage = message;
        return this;
    }

    public TipOfTheDayPdfSectionCreator build() {
        chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setWidth(UnitValue.createPercentValue(35));

        chartCell.add(PdfUtils.getTitleParagraph("Tip of the day", TextAlignment.CENTER));
        chartCell.add(PdfUtils.getItemParagraph("\n"));
        Paragraph tipOfTheDay = PdfUtils.getItemParagraph(tipOfTheDayMessage);
        tipOfTheDay.setTextAlignment(TextAlignment.CENTER);

        chartCell.add(tipOfTheDay);
        chartCell.add(PdfUtils.getItemParagraph("\n"));
        return this;
    }

    public Cell getChartCell() {
        return chartCell;
    }
}