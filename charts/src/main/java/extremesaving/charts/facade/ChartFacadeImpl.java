package extremesaving.charts.facade;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;

import extremesaving.charts.builder.MonthBarChartBuilder;
import extremesaving.charts.builder.OverallLineChartBuilder;
import extremesaving.charts.service.ChartDataService;
import extremesaving.common.ExtremeSavingConstants;

/**
 * Implementation of ChartFacade.
 * Facade to create chart object and write the PNG files.
 */
public class ChartFacadeImpl implements ChartFacade {

    private ChartDataService chartDataService;

    @Override
    public void generateMonthBarChart() {
        JFreeChart chart = new MonthBarChartBuilder()
                .withMonthResults(chartDataService.getMonthResults())
                .build();
        int width = (int) ExtremeSavingConstants.MONTHCHART_WIDTH * 2;
        int height = (int) ExtremeSavingConstants.MONTHCHART_HEIGHT * 2;
        writeChartPng(chart, ExtremeSavingConstants.MONTHCHART_FILENAME, width, height);
    }

    @Override
    public void generateOverallLineChart() {
        JFreeChart chart = new OverallLineChartBuilder()
                .withFutureResults(chartDataService.getOverallLineFutureEstimationResults())
                .withHistoryResults(chartDataService.getOverallLineHistoryResults())
                .withSurvivalResults(chartDataService.getOverallLineSurvivalEstimationResults())
                .build();
        int width = (int) ExtremeSavingConstants.GOAL_LINE_CHART_WIDTH * 2;
        int height = (int) ExtremeSavingConstants.GOAL_LINE_CHART_HEIGHT * 2;
        writeChartPng(chart, ExtremeSavingConstants.OVERALL_LINE_CHART_FILENAME, width, height);
    }

    @Override
    public void removeMonthBarChartFile() {
        new File(Paths.get("").toFile().getAbsolutePath() + File.separator + ExtremeSavingConstants.MONTHCHART_FILENAME).delete();
    }

    @Override
    public void removeOverallLineChartFile() {
        new File(Paths.get("").toFile().getAbsolutePath() + File.separator + ExtremeSavingConstants.OVERALL_LINE_CHART_FILENAME).delete();
    }

    protected void writeChartPng(JFreeChart chart, String file, int width, int height) {
        try {
            BufferedImage objBufferedImage = chart.createBufferedImage(width, height);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ImageIO.write(objBufferedImage, "png", bas);
            byte[] byteArray = bas.toByteArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(in);
            File outputFile = new File(file);
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}