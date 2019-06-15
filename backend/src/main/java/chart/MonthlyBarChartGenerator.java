package chart;

import constant.ExtremeSavingConstants;
import dto.ResultDto;
import dto.TotalsDto;
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

public class MonthlyBarChartGenerator implements ChartGenerator {

    @Override
    public void generateChartPng(TotalsDto totalsDto) throws IOException {
        JFreeChart barChart = ChartFactory.createBarChart("", "", "", createDataset(totalsDto), PlotOrientation.VERTICAL, false, false, false);
        BufferedImage objBufferedImage = barChart.createBufferedImage(500, 309);
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try {
            ImageIO.write(objBufferedImage, "png", bas);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteArray = bas.toByteArray();
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage image = ImageIO.read(in);
        File outputfile = new File(ExtremeSavingConstants.MONTHLY_BAR_CHART_IMAGE_FILE);
        ImageIO.write(image, "png", outputfile);
    }

    private static CategoryDataset createDataset(TotalsDto totalsDto) {
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

        ResultDto januaryResults = totalsDto.getMonthlyResults().get(Calendar.JANUARY);
        ResultDto februaryResults = totalsDto.getMonthlyResults().get(Calendar.FEBRUARY);
        ResultDto marchResults = totalsDto.getMonthlyResults().get(Calendar.MARCH);
        ResultDto aprilResults = totalsDto.getMonthlyResults().get(Calendar.APRIL);
        ResultDto mayResults = totalsDto.getMonthlyResults().get(Calendar.MAY);
        ResultDto juneResults = totalsDto.getMonthlyResults().get(Calendar.JUNE);
        ResultDto julyResults = totalsDto.getMonthlyResults().get(Calendar.JULY);
        ResultDto augustResults = totalsDto.getMonthlyResults().get(Calendar.AUGUST);
        ResultDto septemberResults = totalsDto.getMonthlyResults().get(Calendar.SEPTEMBER);
        ResultDto octoberResults = totalsDto.getMonthlyResults().get(Calendar.OCTOBER);
        ResultDto novemberResults = totalsDto.getMonthlyResults().get(Calendar.NOVEMBER);
        ResultDto decemberResults = totalsDto.getMonthlyResults().get(Calendar.DECEMBER);

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
}
