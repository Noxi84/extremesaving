package extremesaving.pdf.component.itemgrid;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.pdf.util.PdfUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ExpensesTableComponent {

    private List<ResultDto> overallResults;
    private List<ResultDto> yearResults;
    private List<ResultDto> monthResults;
    private int displayMaxItems;
    private int displayMaxTextCharacters;

    public ExpensesTableComponent withOverallResults(List<ResultDto> overallResults) {
        this.overallResults = overallResults;
        return this;
    }

    public ExpensesTableComponent withYearResults(List<ResultDto> yearResults) {
        this.yearResults = yearResults;
        return this;
    }

    public ExpensesTableComponent withMonthResults(List<ResultDto> monthResults) {
        this.monthResults = monthResults;
        return this;
    }

    public ExpensesTableComponent withDisplayMaxItems(int displayMaxItems) {
        this.displayMaxItems = displayMaxItems;
        return this;
    }

    public ExpensesTableComponent withDisplayMaxTextCharacters(int displayMaxTextCharacters) {
        this.displayMaxTextCharacters = displayMaxTextCharacters;
        return this;
    }

    public Table build() {
        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getItemCell("Overall", overallResults));
        table.addCell(getItemCell("This year", yearResults));
        table.addCell(getItemCell("This month", monthResults));
        return table;
    }

    protected Cell getItemCell(String title, List<ResultDto> results) {
        Table alignmentTable = new Table(2);
        alignmentTable.setPaddingLeft(0);
        alignmentTable.setMarginLeft(0);
        alignmentTable.setPaddingRight(0);
        alignmentTable.setMarginRight(0);
        alignmentTable.addCell(getLeftCell(results));
        alignmentTable.addCell(getRightCell(results));

        Cell cell = new Cell();
        cell.add(PdfUtils.getItemParagraph(title, true, TextAlignment.CENTER));
        cell.add(alignmentTable);
        return cell;
    }

    protected Cell getLeftCell(List<ResultDto> results) {
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setWidth(400);
        alignmentTableLeft.setPaddingLeft(0);
        alignmentTableLeft.setMarginLeft(0);
        alignmentTableLeft.setPaddingRight(0);
        alignmentTableLeft.setMarginRight(0);

        int counter = 0;
        for (ResultDto resultDto : results) {
            counter++;
            if (counter >= displayMaxItems) {
                break;
            }
            alignmentTableLeft.add(PdfUtils.getItemParagraph(StringUtils.abbreviate(resultDto.getData().iterator().next().getDescription(), displayMaxTextCharacters)));
        }
        return alignmentTableLeft;
    }

    protected Cell getRightCell(List<ResultDto> results) {
        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight.setWidth(130);
        alignmentTableRight.setPaddingLeft(0);
        alignmentTableRight.setMarginLeft(0);
        alignmentTableRight.setPaddingRight(0);
        alignmentTableRight.setMarginRight(0);

        int counter = 0;
        for (ResultDto resultDto : results) {
            counter++;
            if (counter >= displayMaxItems) {
                break;
            }
            alignmentTableRight.add(PdfUtils.getItemParagraph(PdfUtils.formatNumber(resultDto.getResult())));
        }
        return alignmentTableRight;
    }
}