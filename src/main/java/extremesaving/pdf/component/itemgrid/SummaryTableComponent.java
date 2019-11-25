package extremesaving.pdf.component.itemgrid;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.pdf.util.PdfUtils;

import java.math.BigDecimal;
import java.util.List;

public class SummaryTableComponent {

    private List<CategoryDto> results;
    private BigDecimal savingRatio;

    public SummaryTableComponent withResults(List<CategoryDto> results) {
        this.results = results;
        return this;
    }

    public SummaryTableComponent withSavingRatio(BigDecimal overallSavingRatio) {
        this.savingRatio = overallSavingRatio;
        return this;
    }

    public Table build() {
        return createResultCategoryTable(results, savingRatio);
    }


    protected Table createResultCategoryTable(List<CategoryDto> categoryDtos, BigDecimal savingRatio) {
        Table alignmentTable = new Table(3);
        alignmentTable.setWidth(UnitValue.createPercentValue(100));

        // Create Saving ratio cell
        Cell savingRatioCell = new Cell();
        savingRatioCell.setBorder(Border.NO_BORDER);
        savingRatioCell.add(new SavingRatioImageComponent().withSavingRatio(savingRatio).build());

        // Create left cell
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setVerticalAlignment(VerticalAlignment.MIDDLE);
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setWidth(280);

        // Create right cell
        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setVerticalAlignment(VerticalAlignment.MIDDLE);
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
        alignmentTable.addCell(savingRatioCell);
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);

        return alignmentTable;
    }
}