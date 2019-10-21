package extremesaving.charts.service;

import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.pdf.component.categorygrid.YearBarChartPdfSectionComponent;
import extremesaving.property.PropertiesValueHolder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.Calendar;
import java.util.Map;

import static extremesaving.property.PropertyValueEnum.YEARLY_BAR_CHART_IMAGE_FILE;

public class YearlyBarChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        JFreeChart barChart = ChartFactory.createBarChart("", "", "", createDataset(), PlotOrientation.VERTICAL, false, false, false);
        chartDataService.writeChartPng(barChart, PropertiesValueHolder.getString(YEARLY_BAR_CHART_IMAGE_FILE), (int) YearBarChartPdfSectionComponent.CHART_WIDTH * 2, (int) YearBarChartPdfSectionComponent.CHART_HEIGHT * 2);
    }

    protected CategoryDataset createDataset() {
        Map<Integer, MiniResultDto> yearlyResults = chartDataService.getYearlyResults();

        final String result = "Result";

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

        MiniResultDto year9Results = yearlyResults.get(Integer.valueOf(year9));
        MiniResultDto year8Results = yearlyResults.get(Integer.valueOf(year8));
        MiniResultDto year7Results = yearlyResults.get(Integer.valueOf(year7));
        MiniResultDto year6Results = yearlyResults.get(Integer.valueOf(year6));
        MiniResultDto year5Results = yearlyResults.get(Integer.valueOf(year5));
        MiniResultDto year4Results = yearlyResults.get(Integer.valueOf(year4));
        MiniResultDto year3Results = yearlyResults.get(Integer.valueOf(year3));
        MiniResultDto year2Results = yearlyResults.get(Integer.valueOf(year2));
        MiniResultDto year1Results = yearlyResults.get(Integer.valueOf(year1));

        dataset.addValue(year9Results.getResult(), result, year9);
        dataset.addValue(year8Results.getResult(), result, year8);
        dataset.addValue(year7Results.getResult(), result, year7);
        dataset.addValue(year6Results.getResult(), result, year6);
        dataset.addValue(year5Results.getResult(), result, year5);
        dataset.addValue(year4Results.getResult(), result, year4);
        dataset.addValue(year3Results.getResult(), result, year3);
        dataset.addValue(year2Results.getResult(), result, year2);
        dataset.addValue(year1Results.getResult(), result, year1);

        return dataset;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}