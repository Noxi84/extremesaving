package extremesaving.pdf.component.tipoftheday;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;

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
    private Cell cell;

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

    public GoalAndAwardsCellComponent build() {
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setWidth(UnitValue.createPercentValue(50));

        if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal goalPercentageAmount = currentGoal.subtract(previousGoal);
            BigDecimal currentGoalAmount = resultDto.getResult().subtract(previousGoal);
            BigDecimal goalPercentage = currentGoalAmount.divide(goalPercentageAmount, 2, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100));

            cell.add(new TrophyImageComponent().withGoalIndex(goalIndex).build().getImage());
            cell.add(PdfUtils.getItemParagraph("Save " + NumberUtils.formatNumber(resultDto.getResult(), false) + " / " + NumberUtils.formatNumber(currentGoal, false) + " (" + NumberUtils.formatPercentage(goalPercentage) + ")", true));
            cell.add(PdfUtils.getItemParagraph("Estimated time: " + DateUtils.formatTimeLeft(goalTime), false));
            cell.add(PdfUtils.getItemParagraph("Previous goal " + NumberUtils.formatNumber(previousGoal, false) + " reached on " + new SimpleDateFormat("d MMMM yyyy").format(previousGoalReachDate)));
            cell.add(PdfUtils.getItemParagraph("Estimated survival time without incomes: " + DateUtils.formatTimeLeft(survivalDays), false));
            cell.add(PdfUtils.getItemParagraph("\n"));
        }
        return this;
    }

    public Cell getCell() {
        return cell;
    }
}