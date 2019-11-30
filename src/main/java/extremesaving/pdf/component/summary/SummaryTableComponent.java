package extremesaving.pdf.component.summary;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.pdf.util.PdfUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SummaryTableComponent {

    private List<CategoryDto> results;
    private BigDecimal savingRatio;
    private BigDecimal goalRatio;
    private String tipOfTheDayMessage;

    private BigDecimal previousGoal;
    private BigDecimal currentGoal;

    public SummaryTableComponent withResults(List<CategoryDto> results) {
        this.results = results;
        return this;
    }

    public SummaryTableComponent withSavingRatio(BigDecimal savingRatio) {
        this.savingRatio = savingRatio;
        return this;
    }

    public SummaryTableComponent withGoalRatio(BigDecimal goalRatio) {
        this.goalRatio = goalRatio;
        return this;
    }

    public SummaryTableComponent withTipOfTheDay(String tipOfTheDayMessage) {
        this.tipOfTheDayMessage = tipOfTheDayMessage;
        return this;
    }

    public SummaryTableComponent withPreviousGoal(BigDecimal previousGoal) {
        this.previousGoal = previousGoal;
        return this;
    }

    public SummaryTableComponent withCurrentGoal(BigDecimal currentGoal) {
        this.currentGoal = currentGoal;
        return this;
    }

    public Table build() {
        return createResultCategoryTable(results, savingRatio);
    }


    protected Table createResultCategoryTable(List<CategoryDto> categoryDtos, BigDecimal savingRatio) {
        Table alignmentTable = new Table(4);
        alignmentTable.setWidth(UnitValue.createPercentValue(100));

        // Saving ratio cell
        Cell savingRatioCell = createTropheeImageCell(savingRatio);

        // Create left cell
        Cell alignmentTableLeft = createLeftValuesCell();

        // Create right cell
        Cell alignmentTableRight = createRightValuesCell();

        // Add total amount
        BigDecimal totalAmount = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total", true));
        alignmentTableRight.add(PdfUtils.getItemParagraph(PdfUtils.formatNumber(totalAmount), true));

        // Add saving ratio
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Saving ratio", false));
        alignmentTableRight.add(PdfUtils.getItemParagraph(PdfUtils.formatPercentage(savingRatio), false));

        // Add total items
        long totalItems = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getNumberOfItems()).mapToLong(i -> i).sum();
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total items"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(String.valueOf(totalItems)));

        // Add goal
        if (currentGoal != null) {
            alignmentTableLeft.add(PdfUtils.getItemParagraph("Current goal" + " (" + PdfUtils.formatPercentage(getGoalPercentage()) + ")", false));
            alignmentTableRight.add(PdfUtils.getItemParagraph(PdfUtils.formatNumber(currentGoal, false), false));
        }

        // Tip of the day cell
        Cell tipOfTheDayCell = createTipOfTheDayCell();

        // Add cells to table
        alignmentTable.addCell(savingRatioCell);
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);
        if (tipOfTheDayCell != null) {
            alignmentTable.addCell(tipOfTheDayCell);
        }

        return alignmentTable;
    }

    protected BigDecimal getGoalPercentage() {
        BigDecimal totalAmount = results.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal goalPercentageAmount = currentGoal.subtract(previousGoal);
        BigDecimal currentGoalAmount = totalAmount.subtract(previousGoal);
        if (NumberUtils.isIncome(currentGoalAmount)) {
            return currentGoalAmount.divide(goalPercentageAmount, 2, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    private Cell createTipOfTheDayCell() {
        if (StringUtils.isNotBlank(tipOfTheDayMessage)) {
            Cell tipOfTheDayCell = new Cell();
            tipOfTheDayCell.setBorder(Border.NO_BORDER);
            tipOfTheDayCell.setWidth(600);
            tipOfTheDayCell.add(PdfUtils.getItemParagraph(tipOfTheDayMessage));
            tipOfTheDayCell.setPaddingLeft(20);
            return tipOfTheDayCell;
        }
        return null;
    }

    private Cell createRightValuesCell() {
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.RIGHT);
        cell.setWidth(120);
        cell.setPaddingRight(20);
        return cell;
    }

    private Cell createLeftValuesCell() {
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setBorder(Border.NO_BORDER);
        cell.setWidth(280);
        cell.setPaddingLeft(20);
        return cell;
    }

    private Cell createTropheeImageCell(BigDecimal savingRatio) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(new TropheeImageComponent()
                .withSavingRatio(savingRatio)
                .withGoalRatio(goalRatio)
                .build());
        cell.setPaddingRight(20);
        return cell;
    }
}