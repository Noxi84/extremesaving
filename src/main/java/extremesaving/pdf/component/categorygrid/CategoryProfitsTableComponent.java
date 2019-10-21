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

public class CategoryProfitsTableComponent {

    private List<CategoryDto> overallResults;
    private List<CategoryDto> yearResults;
    private List<CategoryDto> monthResults;

    public CategoryProfitsTableComponent withOverallResults(List<CategoryDto> overallResults) {
        this.overallResults = overallResults;
        return this;
    }

    public CategoryProfitsTableComponent withYearResults(List<CategoryDto> yearResults) {
        this.yearResults = yearResults;
        return this;
    }

    public CategoryProfitsTableComponent withMonthResults(List<CategoryDto> monthResults) {
        this.monthResults = monthResults;
        return this;
    }

    public Table build() {
        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(createOverallCategory());
        table.addCell(createYearCategory());
        table.addCell(createMonthCategory());
        return table;
    }

    protected Cell createOverallCategory() {
        Cell cell = new Cell();
        cell.add(PdfUtils.getItemParagraph("Overall", true, TextAlignment.CENTER));
        cell.add(getCategoryTable(overallResults));
        return cell;
    }

    protected Cell createYearCategory() {
        Cell cell = new Cell();
        cell.add(PdfUtils.getItemParagraph("This year", true, TextAlignment.CENTER));
        cell.add(getCategoryTable(yearResults));
        return cell;
    }

    protected Cell createMonthCategory() {
        Cell cell = new Cell();
        cell.add(PdfUtils.getItemParagraph("This month", true, TextAlignment.CENTER));
        cell.add(getCategoryTable(monthResults));
        return cell;
    }

    protected Table getCategoryTable(List<CategoryDto> categoryDtos) {
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
            alignmentTableRight.add(PdfUtils.getItemParagraph(PdfUtils.formatNumber(categoryDto.getTotalResults().getResult())));
        }

        // Add total amount
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total", true));
        BigDecimal totalAmount = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        alignmentTableRight.add(PdfUtils.getItemParagraph(PdfUtils.formatNumber(totalAmount), true));

        // Add cells to table
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);

        return alignmentTable;
    }
}