package extremesaving.pdf.component.tipoftheday;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.pdf.util.PdfUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GoalAndAwardsCellComponent {

    private ResultDto resultDto;
    private BigDecimal previousGoal;
    private BigDecimal currentGoal;
    private int goalIndex;

    public GoalAndAwardsCellComponent withResultDto(ResultDto resultDto) {
        this.resultDto = resultDto;
        return this;
    }

    public GoalAndAwardsCellComponent withPreviousGoal(BigDecimal previousGoal) {
        this.previousGoal = previousGoal;
        return this;
    }

    public GoalAndAwardsCellComponent withCurrentGoal(BigDecimal currentGoal) {
        this.currentGoal = currentGoal;
        return this;
    }

    public GoalAndAwardsCellComponent withGoalIndex(int goalIndex) {
        this.goalIndex = goalIndex;
        return this;
    }

    public Cell build() {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setWidth(UnitValue.createPercentValue(50));


        Table alignmentTable = new Table(3);
        alignmentTable.setWidth(UnitValue.createPercentValue(100));


        if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
            // Trophy cell
            Cell trophyCell = new Cell();
            trophyCell.setBorder(Border.NO_BORDER);
            trophyCell.setTextAlignment(TextAlignment.CENTER);
            trophyCell.setWidth(100);
            trophyCell.add(new TrophyImageComponent().withGoalIndex(goalIndex).build());

            // Text cell
            Cell textCell = new Cell();
            textCell.setBorder(Border.NO_BORDER);
            textCell.setTextAlignment(TextAlignment.LEFT);
            textCell.setWidth(600);

            BigDecimal goalPercentage = getGoalPercentage();
            textCell.add(PdfUtils.getItemParagraph("Save " + PdfUtils.formatNumber(resultDto.getResult(), false) + " / " + PdfUtils.formatNumber(currentGoal, false) + " (" + PdfUtils.formatPercentage(goalPercentage) + ")", true));

            alignmentTable.addCell(trophyCell);
            alignmentTable.addCell(textCell);
        }

        cell.add(alignmentTable);
        return cell;
    }

    protected BigDecimal getGoalPercentage() {
        BigDecimal goalPercentageAmount = currentGoal.subtract(previousGoal);
        BigDecimal currentGoalAmount = resultDto.getResult().subtract(previousGoal);
        if (NumberUtils.isIncome(currentGoalAmount)) {
            return currentGoalAmount.divide(goalPercentageAmount, 2, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }
}