package extremesaving.pdf.page.component.categorygrid;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class CategoryExpensesTableComponent {

    private List<CategoryDto> overallResults;
    private List<CategoryDto> yearResults;
    private List<CategoryDto> monthResults;
    private Table table;

    public CategoryExpensesTableComponent withOverallResults(List<CategoryDto> overallResults) {
        this.overallResults = overallResults;
        return this;
    }

    public CategoryExpensesTableComponent withYearResults(List<CategoryDto> yearResults) {
        this.yearResults = yearResults;
        return this;
    }

    public CategoryExpensesTableComponent withMontResults(List<CategoryDto> monthResults) {
        this.monthResults = monthResults;
        return this;
    }

    public CategoryExpensesTableComponent build() {
        table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(createOverallCategoryCell());
        table.addCell(createYearCategoryCell());
        table.addCell(createMonthCategoryCell());
        return this;
    }

    protected Cell createOverallCategoryCell() {
        Cell cell = new Cell();
        cell.add(PdfUtils.getItemParagraph("Overall", true, TextAlignment.CENTER));
        cell.add(getTable(overallResults));
        return cell;
    }

    protected Cell createYearCategoryCell() {
        Cell cell = new Cell();
        cell.add(PdfUtils.getItemParagraph("This year", true, TextAlignment.CENTER));
        cell.add(getTable(yearResults));
        return cell;
    }

    protected Cell createMonthCategoryCell() {
        Cell cell = new Cell();
        cell.add(PdfUtils.getItemParagraph("This month", true, TextAlignment.CENTER));
        cell.add(getTable(monthResults));
        return cell;
    }

    protected Table getTable(List<CategoryDto> categoryDtos) {
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

        // Add categoryDtos
        for (CategoryDto categoryDto : categoryDtos) {
            alignmentTableLeft.add(PdfUtils.getItemParagraph(categoryDto.getName()));
            alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(categoryDto.getTotalResults().getResult())));
        }

        // Add total amount
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total", true));
        alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(getTotalAmount(categoryDtos)), true));

        // Add cells to table
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);
        return alignmentTable;
    }

    protected BigDecimal getTotalAmount(Collection<CategoryDto> categoryDtos) {
        return categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Table getTable() {
        return table;
    }
}