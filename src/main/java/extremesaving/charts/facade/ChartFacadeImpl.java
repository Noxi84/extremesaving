package extremesaving.charts.facade;

import extremesaving.charts.builder.GoalLineChart;
import extremesaving.charts.builder.MonthlyBarChart;
import extremesaving.charts.builder.YearLineChart;
import extremesaving.charts.builder.YearlyBarChart;
import extremesaving.charts.service.ChartDataService;
import extremesaving.pdf.component.categorygrid.YearBarChartImageComponent;
import extremesaving.pdf.component.tipoftheday.GoalLineChartImageComponent;
import extremesaving.pdf.component.tipoftheday.MonthBarChartImageComponent;
import extremesaving.pdf.component.tipoftheday.YearLineChartImageComponent;
import extremesaving.property.PropertiesValueHolder;
import org.jfree.chart.JFreeChart;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static extremesaving.property.PropertyValueEnum.GOAL_LINE_CHART_IMAGE_FILE;
import static extremesaving.property.PropertyValueEnum.MONTHLY_BAR_CHART_IMAGE_FILE;
import static extremesaving.property.PropertyValueEnum.YEARLY_BAR_CHART_IMAGE_FILE;
import static extremesaving.property.PropertyValueEnum.YEAR_LINE_CHART_IMAGE_FILE;

public class ChartFacadeImpl implements ChartFacade {

    private ChartDataService chartDataService;

    @Override
    public void generateMonthlyBarChart() {
        System.out.println("Generating MonthlyBarChart...");
        JFreeChart chart = new MonthlyBarChart()
                .withMonthlyResults(chartDataService.getMonthlyResults())
                .build();
        writeChartPng(chart, PropertiesValueHolder.getString(MONTHLY_BAR_CHART_IMAGE_FILE), (int) MonthBarChartImageComponent.MONTHCHART_WIDTH * 2, (int) MonthBarChartImageComponent.MONTHCHART_HEIGHT * 2);
    }

    @Override
    public void generateYearlyBarChart() {
        System.out.println("Generating YearlyBarChart...");
        JFreeChart chart = new YearlyBarChart()
                .withYearResults(chartDataService.getYearlyResults())
                .build();
        writeChartPng(chart, PropertiesValueHolder.getString(YEARLY_BAR_CHART_IMAGE_FILE), (int) YearBarChartImageComponent.CHART_WIDTH * 2, (int) YearBarChartImageComponent.CHART_HEIGHT * 2);
    }

    @Override
    public void generateYearLineChart() {
        System.out.println("Generating YearLineChart...");
        JFreeChart chart = new YearLineChart()
                .withHistoryResults(chartDataService.getGoalLineHistoryResults())
                .withFutureResults(chartDataService.getGoalLineFutureEstimationResults())
                .withSurvivalResults(chartDataService.getGoalLineSurvivalEstimationResults())
                .build();
        writeChartPng(chart, PropertiesValueHolder.getString(YEAR_LINE_CHART_IMAGE_FILE), (int) YearLineChartImageComponent.YEAR_LINE_CHART_WIDTH * 2, (int) YearLineChartImageComponent.YEAR_LINE_CHART_HEIGHT * 2);
    }

    @Override
    public void generateGoalLineChart() {
        System.out.println("Generating GoalLineChart...");
        JFreeChart chart = new GoalLineChart()
                .withFutureResults(chartDataService.getGoalLineFutureEstimationResults())
                .withHistoryResults(chartDataService.getGoalLineHistoryResults())
                .withSurvivalResults(chartDataService.getGoalLineSurvivalEstimationResults())
                .build();
        writeChartPng(chart, PropertiesValueHolder.getString(GOAL_LINE_CHART_IMAGE_FILE), (int) GoalLineChartImageComponent.GOAL_LINE_CHART_WIDTH * 2, (int) GoalLineChartImageComponent.GOAL_LINE_CHART_HEIGHT * 2);
    }

    protected void writeChartPng(JFreeChart chart, String file, int width, int height) {
        try {
            BufferedImage objBufferedImage = chart.createBufferedImage(width, height);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ImageIO.write(objBufferedImage, "png", bas);
            byte[] byteArray = bas.toByteArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(in);
            File outputfile = new File(file);
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}