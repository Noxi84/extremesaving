package extremesaving.service;

import java.math.BigDecimal;
import java.util.Date;

public interface PredictionService {

    Long getSurvivalDays();

    BigDecimal getPreviousGoal();

    BigDecimal getCurrentGoal();

    int getGoalIndex(BigDecimal goalAmount);

    BigDecimal getNextGoal();

    Long getGoalTime(BigDecimal goal);

    Date getGoalReachedDate(BigDecimal goal);

    String getTipOfTheDay();
}