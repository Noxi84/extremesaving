package extremesaving.calculation.facade;

import java.math.BigDecimal;
import java.util.Date;

public interface EstimationFacade {

    Long getSurvivalDays();

    BigDecimal getPreviousGoal();

    BigDecimal getCurrentGoal();

    int getGoalIndex(BigDecimal goalAmount);

    /**
     * @return The next goal based on the index. Default index use should be 1 for the next upcoming goal.
     */
    BigDecimal getNextGoal(int index);

    Long getGoalTime(BigDecimal goal);

    Date getGoalReachedDate(BigDecimal goal);
}
