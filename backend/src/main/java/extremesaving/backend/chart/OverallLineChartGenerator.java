package extremesaving.backend.chart;

import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.frontend.dto.TotalsDto;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.Calendar;

public class OverallLineChartGenerator implements ChartGenerator {

    @Override
    public void generateChartPng(TotalsDto totalsDto) throws IOException {
        JFreeChart lineChart = ChartFactory.createLineChart("", "", "", createDataset(totalsDto), PlotOrientation.VERTICAL, false, false, false);

        BufferedImage objBufferedImage = lineChart.createBufferedImage(500, 309);
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try {
            ImageIO.write(objBufferedImage, "png", bas);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteArray = bas.toByteArray();
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage image = ImageIO.read(in);
        File outputfile = new File(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE);
        ImageIO.write(image, "png", outputfile);
    }

    private DefaultCategoryDataset createDataset(TotalsDto totalsDto) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Calendar cal = Calendar.getInstance();

        int year1 = cal.get(Calendar.YEAR);
        int year2 = cal.get(Calendar.YEAR) - 1;
        int year3 = cal.get(Calendar.YEAR) - 2;
        int year4 = cal.get(Calendar.YEAR) - 3;
        int year5 = cal.get(Calendar.YEAR) - 4;
        int year6 = cal.get(Calendar.YEAR) - 5;
        int year7 = cal.get(Calendar.YEAR) - 6;
        int year8 = cal.get(Calendar.YEAR) - 7;

        BigDecimal year8Expenses = totalsDto.getYearlyResults().get(year8).getExpenses();
        BigDecimal year7Expenses = totalsDto.getYearlyResults().get(year7).getExpenses().add(year8Expenses);
        BigDecimal year6Expenses = totalsDto.getYearlyResults().get(year6).getExpenses().add(year7Expenses);
        BigDecimal year5Expenses = totalsDto.getYearlyResults().get(year5).getExpenses().add(year6Expenses);
        BigDecimal year4Expenses = totalsDto.getYearlyResults().get(year4).getExpenses().add(year5Expenses);
        BigDecimal year3Expenses = totalsDto.getYearlyResults().get(year3).getExpenses().add(year4Expenses);
        BigDecimal year2Expenses = totalsDto.getYearlyResults().get(year2).getExpenses().add(year3Expenses);
        BigDecimal year1Expenses = totalsDto.getYearlyResults().get(year1).getExpenses().add(year2Expenses);

        BigDecimal year8Incomes = totalsDto.getYearlyResults().get(year8).getIncomes();
        BigDecimal year7Incomes = totalsDto.getYearlyResults().get(year7).getIncomes().add(year8Incomes);
        BigDecimal year6Incomes = totalsDto.getYearlyResults().get(year6).getIncomes().add(year7Incomes);
        BigDecimal year5Incomes = totalsDto.getYearlyResults().get(year5).getIncomes().add(year6Incomes);
        BigDecimal year4Incomes = totalsDto.getYearlyResults().get(year4).getIncomes().add(year5Incomes);
        BigDecimal year3Incomes = totalsDto.getYearlyResults().get(year3).getIncomes().add(year4Incomes);
        BigDecimal year2Incomes = totalsDto.getYearlyResults().get(year2).getIncomes().add(year3Incomes);
        BigDecimal year1Incomes = totalsDto.getYearlyResults().get(year1).getIncomes().add(year2Incomes);

        BigDecimal year8Result = totalsDto.getYearlyResults().get(year8).getResult();
        BigDecimal year7Result = totalsDto.getYearlyResults().get(year7).getResult().add(year8Result);
        BigDecimal year6Result = totalsDto.getYearlyResults().get(year6).getResult().add(year7Result);
        BigDecimal year5Result = totalsDto.getYearlyResults().get(year5).getResult().add(year6Result);
        BigDecimal year4Result = totalsDto.getYearlyResults().get(year4).getResult().add(year5Result);
        BigDecimal year3Result = totalsDto.getYearlyResults().get(year3).getResult().add(year4Result);
        BigDecimal year2Result = totalsDto.getYearlyResults().get(year2).getResult().add(year3Result);
        BigDecimal year1Result = totalsDto.getYearlyResults().get(year1).getResult().add(year2Result);

        dataset.addValue(year2Expenses.multiply(BigDecimal.valueOf(-1)), "expenses", String.valueOf(year8));
        dataset.addValue(year2Expenses.multiply(BigDecimal.valueOf(-1)), "expenses", String.valueOf(year7));
        dataset.addValue(year2Expenses.multiply(BigDecimal.valueOf(-1)), "expenses", String.valueOf(year6));
        dataset.addValue(year2Expenses.multiply(BigDecimal.valueOf(-1)), "expenses", String.valueOf(year5));
        dataset.addValue(year2Expenses.multiply(BigDecimal.valueOf(-1)), "expenses", String.valueOf(year4));
        dataset.addValue(year2Expenses.multiply(BigDecimal.valueOf(-1)), "expenses", String.valueOf(year3));
        dataset.addValue(year2Expenses.multiply(BigDecimal.valueOf(-1)), "expenses", String.valueOf(year2));
        dataset.addValue(year1Expenses.multiply(BigDecimal.valueOf(-1)), "expenses", String.valueOf(year1));

        dataset.addValue(year2Result, "result", String.valueOf(year8));
        dataset.addValue(year2Result, "result", String.valueOf(year7));
        dataset.addValue(year2Result, "result", String.valueOf(year6));
        dataset.addValue(year2Result, "result", String.valueOf(year5));
        dataset.addValue(year2Result, "result", String.valueOf(year4));
        dataset.addValue(year2Result, "result", String.valueOf(year3));
        dataset.addValue(year2Result, "result", String.valueOf(year2));
        dataset.addValue(year1Result, "result", String.valueOf(year1));

        dataset.addValue(year2Incomes, "incomes", String.valueOf(year8));
        dataset.addValue(year2Incomes, "incomes", String.valueOf(year7));
        dataset.addValue(year2Incomes, "incomes", String.valueOf(year6));
        dataset.addValue(year2Incomes, "incomes", String.valueOf(year5));
        dataset.addValue(year2Incomes, "incomes", String.valueOf(year4));
        dataset.addValue(year2Incomes, "incomes", String.valueOf(year3));
        dataset.addValue(year2Incomes, "incomes", String.valueOf(year2));
        dataset.addValue(year1Incomes, "incomes", String.valueOf(year1));
        return dataset;
    }
}
