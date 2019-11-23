package extremesaving.calculation.dto;

import java.math.BigDecimal;

public class EstimationResultDto {

    private BigDecimal averageDailyExpense = BigDecimal.ZERO;
    private BigDecimal averageDailyResult = BigDecimal.ZERO;

    public BigDecimal getAverageDailyExpense() {
        return averageDailyExpense;
    }

    public void setAverageDailyExpense(BigDecimal averageDailyExpense) {
        this.averageDailyExpense = averageDailyExpense;
    }

    public BigDecimal getAverageDailyResult() {
        return averageDailyResult;
    }

    public void setAverageDailyResult(BigDecimal averageDailyResult) {
        this.averageDailyResult = averageDailyResult;
    }
}