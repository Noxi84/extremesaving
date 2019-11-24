package extremesaving.pdf.component.tipoftheday;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GoalAndAwardsCellComponent {

    private ResultDto resultDto;
    private BigDecimal previousGoal;
    private Date previousGoalReachDate;
    private BigDecimal currentGoal;
    private int goalIndex;
    private Long goalTime;
    private Long survivalDays;

    public GoalAndAwardsCellComponent withResultDto(ResultDto resultDto) {
        this.resultDto = resultDto;
        return this;
    }

    public GoalAndAwardsCellComponent withPreviousGoal(BigDecimal previousGoal) {
        this.previousGoal = previousGoal;
        return this;
    }

    public GoalAndAwardsCellComponent withPreviousGoalReachDate(Date previousGoalReachDate) {
        this.previousGoalReachDate = previousGoalReachDate;
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

    public GoalAndAwardsCellComponent withGoalTime(Long goalTime) {
        this.goalTime = goalTime;
        return this;
    }

    public GoalAndAwardsCellComponent withSurvivalDays(Long survivalDays) {
        this.survivalDays = survivalDays;
        return this;
    }

    public Cell build() {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setWidth(UnitValue.createPercentValue(50));

        if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
            cell.add(new TrophyImageComponent().withGoalIndex(goalIndex).build());

            BigDecimal goalPercentage = getGoalPercentage();
            cell.add(PdfUtils.getItemParagraph("Save " + PdfUtils.formatNumber(resultDto.getResult(), false) + " / " + PdfUtils.formatNumber(currentGoal, false) + " (" + PdfUtils.formatPercentage(goalPercentage) + ")", true));
            cell.add(PdfUtils.getItemParagraph("Estimated time: " + DateUtils.formatTimeLeft(goalTime), false));
            cell.add(PdfUtils.getItemParagraph("Previous goal " + PdfUtils.formatNumber(previousGoal, false) + " reached: " + getPreviousGoalReached()));
            cell.add(PdfUtils.getItemParagraph("Estimated survival time without incomes: " + DateUtils.formatTimeLeft(survivalDays), false));
            cell.add(PdfUtils.getItemParagraph("\n"));
        }
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

    protected String getPreviousGoalReached() {
        if (previousGoalReachDate != null) {
            return new SimpleDateFormat("d MMMM yyyy").format(previousGoalReachDate);
        }
        return "Never";
    }
}