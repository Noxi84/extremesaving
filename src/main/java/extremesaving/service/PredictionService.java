package extremesaving.service;

import extremesaving.dto.CategoryDto;

import java.math.BigDecimal;
import java.util.Date;

public interface PredictionService {

    CategoryDto getRandomExpensiveCategory();

    CategoryDto getRandomProfitCategory();

    Long getSurvivalDays();

    BigDecimal getNextGoal();

    Long getGoalTime(BigDecimal goal);

    BigDecimal getPredictionAmount(Date endDate);

    String getTipOfTheDay();
}