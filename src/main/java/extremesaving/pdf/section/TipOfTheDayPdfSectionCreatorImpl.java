package extremesaving.pdf.section;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.util.PdfUtils;

public class TipOfTheDayPdfSectionCreatorImpl implements TipOfTheDayPdfSectionCreator{

    private DataFacade dataFacade;

    @Override
    public Cell getTipOfTheDayCell() {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setWidth(UnitValue.createPercentValue(35));

        chartCell.add(PdfUtils.getTitleParagraph("Tip of the day", TextAlignment.CENTER));
        chartCell.add(PdfUtils.getItemParagraph("\n"));
        Paragraph tipOfTheDay = PdfUtils.getItemParagraph(dataFacade.getTipOfTheDay());
        tipOfTheDay.setTextAlignment(TextAlignment.CENTER);

        chartCell.add(tipOfTheDay);
        chartCell.add(PdfUtils.getItemParagraph("\n"));

        return chartCell;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }
}