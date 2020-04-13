package extremesaving.pdf.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.calculation.facade.CategoryFacade;
import extremesaving.calculation.facade.EstimationFacade;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.charts.facade.ChartFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.chart.MonthBarChartImageComponent;
import extremesaving.pdf.component.itemgrid.MonthCategoryTableComponent;
import extremesaving.pdf.component.summary.SummaryTableComponent;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;

public class MonthItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 20;
    private static final int TEXT_MAX_CHARACTERS = 200;
    public static final int NUMBER_OF_MONTHS = 12;

    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;
    private CalculationFacade calculationFacade;
    private ChartFacade chartFacade;
    private EstimationFacade estimationFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating Monthly Analysis Report");
        document.add(PdfUtils.getTitleParagraph("Extreme-Saving Report", TextAlignment.LEFT));
        document.add(buildSummaryTable());
        document.add(buildMonthBarChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(PdfUtils.getTitleParagraph("Most profitable items", TextAlignment.LEFT));
        document.add(buildCategoryProfitsTable());
        document.add(PdfUtils.getTitleParagraph("Most expensive items", TextAlignment.LEFT));
        document.add(buildCategoryExpensesTable());
    }

    protected Table buildSummaryTable() {
        List<DataDto> dataDtos = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<CategoryDto> results = categoryFacade.getCategories(dataDtos);
        return new SummaryTableComponent()
                .withResults(results)
                .withSavingRatio(getSavingRatio())
                .withGoalRatio(estimationFacade.calculateGoalRatio())
                .withTipOfTheDay(dataFacade.getTipOfTheDay())
                .build();
    }

    protected BigDecimal getSavingRatio() {
        List<DataDto> dataDtos = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<CategoryDto> profitResults = categoryFacade.getMostProfitableCategories(dataDtos);
        List<CategoryDto> expensesResults = categoryFacade.getMostExpensiveCategories(dataDtos);
        return calculationFacade.calculateSavingRatio(profitResults, expensesResults);
    }

    protected Image buildMonthBarChartImage() {
        chartFacade.generateMonthBarChart();
        return new MonthBarChartImageComponent().build();
    }

    protected Table buildCategoryProfitsTable() {
        Map<String, List<CategoryDto>> monthResults = new HashMap<>();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        Calendar cal = Calendar.getInstance();

        List<CategoryDto> overallCategoryResults = categoryFacade.getCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toSet())).stream()
                .filter(categoryDto -> NumberUtils.isIncome(categoryDto.getTotalResults().getResult()))
                .sorted((o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()))
                .collect(Collectors.toList());

        for (int monthCounter = cal.get(Calendar.MONTH); monthCounter > cal.get(Calendar.MONTH) - NUMBER_OF_MONTHS; monthCounter--) {
            Calendar monthDate = Calendar.getInstance();
            monthDate.set(Calendar.MONTH, monthCounter);
            List<DataDto> dataDtos = dataFacade.findAll().stream()
                    .filter(dataDto -> DateUtils.equalYearAndMonths(monthDate.getTime(), dataDto.getDate()))
                    .collect(Collectors.toList());
            List<CategoryDto> categoryResults = categoryFacade.getCategories(dataDtos);

            List<CategoryDto> results;
            if (monthCounter == currentMonth) {
                results = categoryResults.stream()
                        .filter(categoryDto -> overallCategoryResults.stream().filter(overallCategory -> overallCategory.getName().equals(categoryDto.getName())).count() > 0)
                        .sorted((o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()))
                        .collect(Collectors.toList());
            } else {
                results = categoryResults.stream()
                        .sorted((o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()))
                        .collect(Collectors.toList());
            }
            monthResults.put(String.valueOf(monthCounter), results);
        }

        monthResults.put("Total", overallCategoryResults);

        return new MonthCategoryTableComponent()
                .withResults(monthResults)
                .withNumberOfColumns(6)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .withPrintTotalsColumn(false)
                .build();
    }

    protected Table buildCategoryExpensesTable() {
        Map<String, List<CategoryDto>> monthResults = new HashMap<>();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        Calendar cal = Calendar.getInstance();

        List<CategoryDto> overallCategoryResults = categoryFacade.getCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toSet())).stream()
                .filter(categoryDto -> NumberUtils.isExpense(categoryDto.getTotalResults().getResult()))
                .sorted(Comparator.comparing(o -> o.getTotalResults().getResult()))
                .collect(Collectors.toList());

        for (int monthCounter = cal.get(Calendar.MONTH); monthCounter > cal.get(Calendar.MONTH) - NUMBER_OF_MONTHS; monthCounter--) {
            Calendar monthDate = Calendar.getInstance();
            monthDate.set(Calendar.MONTH, monthCounter);
            List<DataDto> dataDtos = dataFacade.findAll().stream()
                    .filter(dataDto -> DateUtils.equalYearAndMonths(monthDate.getTime(), dataDto.getDate()))
                    .collect(Collectors.toList());
            List<CategoryDto> categoryResults = categoryFacade.getCategories(dataDtos);
            List<CategoryDto> results;
            if (monthCounter == currentMonth) {
                results = categoryResults.stream()
                        .filter(categoryDto -> overallCategoryResults.stream().filter(overallCategory -> overallCategory.getName().equals(categoryDto.getName())).count() > 0)
                        .sorted(Comparator.comparing(o -> o.getTotalResults().getResult()))
                        .collect(Collectors.toList());
            } else {
                results = categoryResults.stream()
                        .sorted(Comparator.comparing(o -> o.getTotalResults().getResult()))
                        .collect(Collectors.toList());
            }
            monthResults.put(String.valueOf(monthCounter), results);
        }

        monthResults.put("Total", overallCategoryResults);

        return new MonthCategoryTableComponent()
                .withResults(monthResults)
                .withNumberOfColumns(6)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .withPrintTotalsColumn(false)
                .build();
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setCategoryFacade(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }

    public void setChartFacade(ChartFacade chartFacade) {
        this.chartFacade = chartFacade;
    }

    public void setEstimationFacade(EstimationFacade estimationFacade) {
        this.estimationFacade = estimationFacade;
    }
}