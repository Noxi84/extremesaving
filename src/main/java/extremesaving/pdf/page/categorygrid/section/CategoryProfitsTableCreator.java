package extremesaving.pdf.page.categorygrid.section;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.util.List;

public class CategoryProfitsTableCreator {

    private List<CategoryDto> overallResults;
    private List<CategoryDto> yearResults;
    private List<CategoryDto> monthResults;
    private Table table;

    public CategoryProfitsTableCreator withOverallResults(List<CategoryDto> overallResults) {
        this.overallResults = overallResults;
        return this;
    }

    public CategoryProfitsTableCreator withYearResults(List<CategoryDto> yearResults) {
        this.yearResults = yearResults;
        return this;
    }

    public CategoryProfitsTableCreator withMontResults(List<CategoryDto> monthResults) {
        this.monthResults = monthResults;
        return this;
    }

    public CategoryProfitsTableCreator build() {
        table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getCategoryCell("Overall", overallResults));
        table.addCell(getCategoryCell("This year", yearResults));
        table.addCell(getCategoryCell("This month", monthResults));
        return this;
    }

    protected Cell getCategoryCell(String title, List<CategoryDto> categoryDtos) {
        Cell cell = new Cell();

        cell.add(PdfUtils.getItemParagraph(title, true, TextAlignment.CENTER));

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
        BigDecimal totalAmount = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(totalAmount), true));

        // Add cells to table
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);

        cell.add(alignmentTable);

        return cell;
    }

    public Table getTable() {
        return table;
    }
}