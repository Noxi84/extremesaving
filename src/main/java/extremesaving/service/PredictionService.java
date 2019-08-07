package extremesaving.service;

import java.math.BigDecimal;

public interface PredictionService {

    Long getSurvivalDays();

    BigDecimal getPreviousGoal();

    BigDecimal getNextGoal();

    Long getGoalTime(BigDecimal goal);

    String getTipOfTheDay();
}