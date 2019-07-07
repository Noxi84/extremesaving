package extremesaving.service.chart;

import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.dto.MiniResultDto;
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

public class MonthlyBarChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        try {
            Map<Integer, MiniResultDto> monthlyResults = chartDataService.getMonthlyResults();

            JFreeChart barChart = ChartFactory.createBarChart("", "", "", createDataset(monthlyResults), PlotOrientation.VERTICAL, true, false, false);
            BufferedImage objBufferedImage = barChart.createBufferedImage(760, 600);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ImageIO.write(objBufferedImage, "png", bas);
            byte[] byteArray = bas.toByteArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(in);
            File outputfile = new File(ExtremeSavingConstants.MONTHLY_BAR_CHART_IMAGE_FILE);
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CategoryDataset createDataset(Map<Integer, MiniResultDto> monthlyResults) {
        final String incomes = "Incomes";
        final String result = "Result";
        final String expenses = "Expenses";
        final String january = "Jan";
        final String february = "Feb";
        final String march = "Mar";
        final String april = "Apr";
        final String may = "May";
        final String june = "Jun";
        final String july = "Jul";
        final String august = "Au";
        final String september = "Sep";
        final String october = "Oct";
        final String november = "Nov";
        final String december = "Dec";
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        MiniResultDto januaryResults = monthlyResults.get(Calendar.JANUARY);
        MiniResultDto februaryResults = monthlyResults.get(Calendar.FEBRUARY);
        MiniResultDto marchResults = monthlyResults.get(Calendar.MARCH);
        MiniResultDto aprilResults = monthlyResults.get(Calendar.APRIL);
        MiniResultDto mayResults = monthlyResults.get(Calendar.MAY);
        MiniResultDto juneResults = monthlyResults.get(Calendar.JUNE);
        MiniResultDto julyResults = monthlyResults.get(Calendar.JULY);
        MiniResultDto augustResults = monthlyResults.get(Calendar.AUGUST);
        MiniResultDto septemberResults = monthlyResults.get(Calendar.SEPTEMBER);
        MiniResultDto octoberResults = monthlyResults.get(Calendar.OCTOBER);
        MiniResultDto novemberResults = monthlyResults.get(Calendar.NOVEMBER);
        MiniResultDto decemberResults = monthlyResults.get(Calendar.DECEMBER);

        dataset.addValue(januaryResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, january);
        dataset.addValue(februaryResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, february);
        dataset.addValue(marchResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, march);
        dataset.addValue(aprilResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, april);
        dataset.addValue(mayResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, may);
        dataset.addValue(juneResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, june);
        dataset.addValue(julyResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, july);
        dataset.addValue(augustResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, august);
        dataset.addValue(septemberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, september);
        dataset.addValue(octoberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, october);
        dataset.addValue(novemberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, november);
        dataset.addValue(decemberResults.getExpenses().multiply(BigDecimal.valueOf(-1)), expenses, december);

        dataset.addValue(januaryResults.getResult(), result, january);
        dataset.addValue(februaryResults.getResult(), result, february);
        dataset.addValue(marchResults.getResult(), result, march);
        dataset.addValue(aprilResults.getResult(), result, april);
        dataset.addValue(mayResults.getResult(), result, may);
        dataset.addValue(juneResults.getResult(), result, june);
        dataset.addValue(julyResults.getResult(), result, july);
        dataset.addValue(augustResults.getResult(), result, august);
        dataset.addValue(septemberResults.getResult(), result, september);
        dataset.addValue(octoberResults.getResult(), result, october);
        dataset.addValue(novemberResults.getResult(), result, november);
        dataset.addValue(decemberResults.getResult(), result, december);

        dataset.addValue(januaryResults.getIncomes(), incomes, january);
        dataset.addValue(februaryResults.getIncomes(), incomes, february);
        dataset.addValue(marchResults.getIncomes(), incomes, march);
        dataset.addValue(aprilResults.getIncomes(), incomes, april);
        dataset.addValue(mayResults.getIncomes(), incomes, may);
        dataset.addValue(juneResults.getIncomes(), incomes, june);
        dataset.addValue(julyResults.getIncomes(), incomes, july);
        dataset.addValue(augustResults.getIncomes(), incomes, august);
        dataset.addValue(septemberResults.getIncomes(), incomes, september);
        dataset.addValue(octoberResults.getIncomes(), incomes, october);
        dataset.addValue(novemberResults.getIncomes(), incomes, november);
        dataset.addValue(decemberResults.getIncomes(), incomes, december);

        return dataset;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}