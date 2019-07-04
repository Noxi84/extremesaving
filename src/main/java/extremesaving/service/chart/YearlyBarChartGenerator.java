package extremesaving.service.chart;

import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.dto.ResultDto;
import extremesaving.service.ChartDataService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

public class YearlyBarChartGenerator implements ChartGenerator {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        try {
            Map<Integer, ResultDto> yearlyResults = chartDataService.getYearlyResults();

            JFreeChart barChart = ChartFactory.createBarChart("", "", "", createDataset(yearlyResults), PlotOrientation.VERTICAL, true, false, false);
            BufferedImage objBufferedImage = barChart.createBufferedImage(600, 370);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ImageIO.write(objBufferedImage, "png", bas);
            byte[] byteArray = bas.toByteArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(in);
            File outputfile = new File(ExtremeSavingConstants.YEARLY_BAR_CHART_IMAGE_FILE);
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CategoryDataset createDataset(Map<Integer, ResultDto> yearlyResults) {
        final String incomes = "Incomes";
        final String result = "Result";
        final String expenses = "Expenses";

        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        final String year1 = String.valueOf(currentYear);
        final String year2 = String.valueOf(currentYear - 1);
        final String year3 = String.valueOf(currentYear - 2);
        final String year4 = String.valueOf(currentYear - 3);
        final String year5 = String.valueOf(currentYear - 4);
        final String year6 = String.valueOf(currentYear - 5);
        final String year7 = String.valueOf(currentYear - 6);
        final String year8 = String.valueOf(currentYear - 7);
        final String year9 = String.valueOf(currentYear - 8);
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        ResultDto year9Results = yearlyResults.get(Integer.valueOf(year9));
        ResultDto year8Results = yearlyResults.get(Integer.valueOf(year8));
        ResultDto year7Results = yearlyResults.get(Integer.valueOf(year7));
        ResultDto year6Results = yearlyResults.get(Integer.valueOf(year6));
        ResultDto year5Results = yearlyResults.get(Integer.valueOf(year5));
        ResultDto year4Results = yearlyResults.get(Integer.valueOf(year4));
        ResultDto year3Results = yearlyResults.get(Integer.valueOf(year3));
        ResultDto year2Results = yearlyResults.get(Integer.valueOf(year2));
        ResultDto year1Results = yearlyResults.get(Integer.valueOf(year1));

        dataset.addValue(year9Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year9);
        dataset.addValue(year8Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year8);
        dataset.addValue(year7Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year7);
        dataset.addValue(year6Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year6);
        dataset.addValue(year5Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year5);
        dataset.addValue(year4Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year4);
        dataset.addValue(year3Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year3);
        dataset.addValue(year2Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year2);
        dataset.addValue(year1Results.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, year1);

        dataset.addValue(year9Results.getResult(), result, year9);
        dataset.addValue(year8Results.getResult(), result, year8);
        dataset.addValue(year7Results.getResult(), result, year7);
        dataset.addValue(year6Results.getResult(), result, year6);
        dataset.addValue(year5Results.getResult(), result, year5);
        dataset.addValue(year4Results.getResult(), result, year4);
        dataset.addValue(year3Results.getResult(), result, year3);
        dataset.addValue(year2Results.getResult(), result, year2);
        dataset.addValue(year1Results.getResult(), result, year1);

        dataset.addValue(year9Results.getIncomes(), incomes, year9);
        dataset.addValue(year8Results.getIncomes(), incomes, year8);
        dataset.addValue(year7Results.getIncomes(), incomes, year7);
        dataset.addValue(year6Results.getIncomes(), incomes, year6);
        dataset.addValue(year5Results.getIncomes(), incomes, year5);
        dataset.addValue(year4Results.getIncomes(), incomes, year4);
        dataset.addValue(year3Results.getIncomes(), incomes, year3);
        dataset.addValue(year2Results.getIncomes(), incomes, year2);
        dataset.addValue(year1Results.getIncomes(), incomes, year1);

        return dataset;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}