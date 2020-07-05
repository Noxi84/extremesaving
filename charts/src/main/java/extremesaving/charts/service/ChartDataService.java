package extremesaving.charts.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import extremesaving.data.dto.MiniResultDto;

/**
 * Service for handling the chart-image data sets.
 */
public interface ChartDataService {

    /**
     * Get the month results for the month bar chart.
     *
     * @return Map<Date, MiniResultDto> containg the end result of each month.
     */
    Map<Date, MiniResultDto> getMonthResults();

    /**
     * Get the history results for the overall line-chart. This is the blue line.
     *
     * @return Map<Date, BigDecimal> containing the end result of each date.
     */
    Map<Date, BigDecimal> getOverallLineHistoryResults();

    /**
     * Get the future estimation results for the overall line-chart. This is the green line.
     *
     * @return Map<Date, BigDecimal> containing the end result of each date.
     */
    Map<Date, BigDecimal> getOverallLineFutureEstimationResults();

    /**
     * Get the survival results for the overall line-chart. This is the red line.
     *
     * @return Map<Date, BigDecimal> containing the end result of each date.
     */
    Map<Date, BigDecimal> getOverallLineSurvivalEstimationResults();
}