package extremesaving.pdf.page.tipoftheday.component;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.pdf.util.PdfUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatisticsPdfSectionCreator {

    private Cell balanceCell;
    private Date lastItemAdded;
    private Date bestMonth;
    private Date bestYear;
    private Date worstMonth;
    private Date worstYear;

    public StatisticsPdfSectionCreator withLastItemAdded(Date lastItemAdded) {
        this.lastItemAdded = lastItemAdded;
        return this;
    }

    public StatisticsPdfSectionCreator withBestMonth(Date bestMonth) {
        this.bestMonth = bestMonth;
        return this;
    }

    public StatisticsPdfSectionCreator withBestYear(Date bestYear) {
        this.bestYear = bestYear;
        return this;
    }

    public StatisticsPdfSectionCreator withWorstMonth(Date worstMonth) {
        this.worstMonth = worstMonth;
        return this;
    }

    public StatisticsPdfSectionCreator withWorstYear(Date worstYear) {
        this.worstYear = worstYear;
        return this;
    }

    public StatisticsPdfSectionCreator build() {
        balanceCell = new Cell();
        balanceCell.setWidth(UnitValue.createPercentValue(25));
        balanceCell.setBorder(Border.NO_BORDER);
        balanceCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        balanceCell.setTextAlignment(TextAlignment.CENTER);
        balanceCell.setWidth(500);
        balanceCell.add(PdfUtils.getTitleParagraph("Statistics", TextAlignment.CENTER));
        balanceCell.add(PdfUtils.getItemParagraph("\n"));

        Table alignmentTable = new Table(3);

        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setTextAlignment(TextAlignment.LEFT);
        alignmentTableLeft.setPaddingLeft(20);

        Cell alignmentTableCenter = new Cell();
        alignmentTableCenter.setBorder(Border.NO_BORDER);
        alignmentTableCenter.setTextAlignment(TextAlignment.CENTER);

        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);

        SimpleDateFormat sf = new SimpleDateFormat(" d MMMM yyyy");
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Last update"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(sf.format(new Date())));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Last item added"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(sf.format(lastItemAdded)));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("\n"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph("\n"));
        alignmentTableRight.add(PdfUtils.getItemParagraph("\n"));

        SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM");

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Best month"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(monthDateFormat.format(bestMonth)));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Worst month"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(monthDateFormat.format(worstMonth)));

        SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Best year"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(yearDateFormat.format(bestYear)));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Worst year"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(yearDateFormat.format(worstYear)));

        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableCenter);
        alignmentTable.addCell(alignmentTableRight);

        balanceCell.add(alignmentTable);

        return this;
    }

    public Cell getBalanceCell() {
        return balanceCell;
    }
}