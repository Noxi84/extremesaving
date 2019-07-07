package extremesaving.service;

import java.math.BigDecimal;
import java.util.Date;

public interface PredictionService {

    Long getSurvivalDays();

    BigDecimal getPredictionAmount(Date endDate);

    String getTipOfTheDay();
}