package extremesaving.pdf.component.categorygrid;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.pdf.util.PdfUtils;

import java.math.BigDecimal;
import java.util.List;

public class CategoryOverallTableComponent {

    private List<CategoryDto> overallResults;
    private List<CategoryDto> yearResults;
    private List<CategoryDto> monthResults;
    private BigDecimal overallSavingRatio;
    private BigDecimal yearSavingRatio;
    private BigDecimal monthSavingRatio;

    public CategoryOverallTableComponent withOverallResults(List<CategoryDto> overallResults) {
        this.overallResults = overallResults;
        return this;
    }

    public CategoryOverallTableComponent withYearResults(List<CategoryDto> yearResults) {
        this.yearResults = yearResults;
        return this;
    }

    public CategoryOverallTableComponent withMontResults(List<CategoryDto> monthResults) {
        this.monthResults = monthResults;
        return this;
    }

    public CategoryOverallTableComponent withOverallSavingRatio(BigDecimal overallSavingRatio) {
        this.overallSavingRatio = overallSavingRatio;
        return this;
    }

    public CategoryOverallTableComponent withYearSavingRatio(BigDecimal yearSavingRatio) {
        this.yearSavingRatio = yearSavingRatio;
        return this;
    }

    public CategoryOverallTableComponent withMonthSavingRatio(BigDecimal monthSavingRatio) {
        this.monthSavingRatio = monthSavingRatio;
        return this;
    }

    public Table build() {
        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(createOverallResultsCell());
        table.addCell(createYearResultsCell());
        table.addCell(createMonthResultsCell());
        return table;
    }

    protected Cell createOverallResultsCell() {
        Cell cell = new Cell();
        cell.add(new SavingRatioImageComponent().withSavingRatio(overallSavingRatio).build());
        cell.add(createResultCategoryTable(overallResults, overallSavingRatio));
        return cell;
    }

    protected Cell createYearResultsCell() {
        Cell cell = new Cell();
        cell.add(new SavingRatioImageComponent().withSavingRatio(yearSavingRatio).build());
        cell.add(createResultCategoryTable(yearResults, yearSavingRatio));
        return cell;
    }

    protected Cell createMonthResultsCell() {
        Cell cell = new Cell();
        cell.add(new SavingRatioImageComponent().withSavingRatio(monthSavingRatio).build());
        cell.add(createResultCategoryTable(monthResults, monthSavingRatio));
        return cell;
    }

    protected Table createResultCategoryTable(List<CategoryDto> categoryDtos, BigDecimal savingRatio) {
        Table alignmentTable = new Table(2);
        alignmentTable.setWidth(UnitValue.createPercentValue(100));

        // Create left cell
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setWidth(280);

        // Create right cell
        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight.setWidth(120);

        // Add total amount
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total", true));
        BigDecimal totalAmount = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        alignmentTableRight.add(PdfUtils.getItemParagraph(PdfUtils.formatNumber(totalAmount), true));


        // Add saving ratio
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Saving ratio", true));
        alignmentTableRight.add(PdfUtils.getItemParagraph(PdfUtils.formatPercentage(savingRatio), true));

        // Add total items
        long totalItems = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getNumberOfItems()).mapToLong(i -> i).sum();
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total items"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(String.valueOf(totalItems)));

        // Add cells to table
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);

        return alignmentTable;
    }
}