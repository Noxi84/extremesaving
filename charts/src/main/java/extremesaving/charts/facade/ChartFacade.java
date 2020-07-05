package extremesaving.charts.facade;

/**
 * Facade to create chart object and write the PNG files.
 */
public interface ChartFacade {

    /**
     * Build the month bar chart and save the chart as a PNG file.
     */
    void generateMonthBarChart();

    /**
     * Build the overall line chart and save the chart as a PNG file.
     */
    void generateOverallLineChart();
}