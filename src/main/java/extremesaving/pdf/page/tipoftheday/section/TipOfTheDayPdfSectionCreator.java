package extremesaving.pdf.page.tipoftheday.section;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.pdf.util.PdfUtils;

public class TipOfTheDayPdfSectionCreator {

    private Cell cell;
    private String tipOfTheDayMessage;

    public TipOfTheDayPdfSectionCreator withMessage(String message) {
        this.tipOfTheDayMessage = message;
        return this;
    }

    public TipOfTheDayPdfSectionCreator build() {
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setWidth(UnitValue.createPercentValue(35));

        cell.add(PdfUtils.getTitleParagraph("Tip of the day", TextAlignment.CENTER));
        cell.add(PdfUtils.getItemParagraph("\n"));
        Paragraph tipOfTheDay = PdfUtils.getItemParagraph(tipOfTheDayMessage);
        tipOfTheDay.setTextAlignment(TextAlignment.CENTER);

        cell.add(tipOfTheDay);
        cell.add(PdfUtils.getItemParagraph("\n"));
        return this;
    }

    public Cell getCell() {
        return cell;
    }
}