package extremesaving.pdf.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import extremesaving.pdf.component.chart.GoalLineChartImageComponent;
import extremesaving.pdf.component.itemgrid.YearCategoryTableComponent;
import extremesaving.pdf.component.summary.SummaryTableComponent;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;

public class YearItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 20;
    private static final int TEXT_MAX_CHARACTERS = 200;
    public static final int NUMBER_OF_YEARS = 12;

    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;
    private CalculationFacade calculationFacade;
    private ChartFacade chartFacade;
    private EstimationFacade estimationFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating Yearly Analysis Report");
        document.add(PdfUtils.getTitleParagraph("Extreme-Saving Report", TextAlignment.LEFT));
        document.add(buildSummaryTable());
        document.add(buildGoalLineChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(PdfUtils.getTitleParagraph("Most profitable items", TextAlignment.LEFT));
        document.add(buildCategoryProfitsTable());
        document.add(PdfUtils.getTitleParagraph("Most expensive items", TextAlignment.LEFT));
        document.add(buildCategoryExpensesTable());
    }

    protected Table buildSummaryTable() {
        List<DataDto> dataDtos = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<CategoryDto> results = categoryFacade.getCategories(dataDtos);
        return new SummaryTableComponent()
                .withResults(results)
                .withSavingRatio(getSavingRatio())
                .withGoalRatio(estimationFacade.calculateGoalRatio())
                .withTipOfTheDay(dataFacade.getTipOfTheDay())
                .build();
    }

    protected BigDecimal getSavingRatio() {
        List<DataDto> dataDtos = dataFacade.findAll();
        List<CategoryDto> profitResults = categoryFacade.getMostProfitableCategories(dataDtos);
        List<CategoryDto> expensesResults = categoryFacade.getMostExpensiveCategories(dataDtos);
        return calculationFacade.calculateSavingRatio(profitResults, expensesResults);
    }

    protected Image buildGoalLineChartImage() {
        chartFacade.generateGoalLineChart();
        return new GoalLineChartImageComponent().build();
    }

    protected Table buildCategoryProfitsTable() {
        List<CategoryDto> overallCategoryResults = categoryFacade.getCategories(dataFacade.findAll()).stream()
                .filter(categoryDto -> NumberUtils.isIncome(categoryDto.getTotalResults().getResult()))
                .sorted((o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()))
                .collect(Collectors.toList());
        return buildCategoryTable(overallCategoryResults);
    }

    protected Table buildCategoryExpensesTable() {
        List<CategoryDto> overallCategoryResults = categoryFacade.getCategories(dataFacade.findAll()).stream()
                .filter(categoryDto -> NumberUtils.isExpense(categoryDto.getTotalResults().getResult()))
                .sorted(Comparator.comparing(o -> o.getTotalResults().getResult()))
                .collect(Collectors.toList());
        return buildCategoryTable(overallCategoryResults);
    }

    protected Table buildCategoryTable(List<CategoryDto> overallCategoryResults) {
        Map<String, List<CategoryDto>> yearResults = new HashMap<>();
        int currentYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));
        Calendar cal = Calendar.getInstance();

        for (int yearCounter = cal.get(Calendar.YEAR); yearCounter > cal.get(Calendar.YEAR) - NUMBER_OF_YEARS; yearCounter--) {
            Calendar yearDate = Calendar.getInstance();
            yearDate.set(Calendar.YEAR, yearCounter);
            List<DataDto> dataDtos = dataFacade.findAll().stream()
                    .filter(dataDto -> DateUtils.equalYears(yearDate.getTime(), dataDto.getDate()))
                    .collect(Collectors.toList());
            List<CategoryDto> categoryResults = categoryFacade.getCategories(dataDtos);
            List<CategoryDto> results;
            if (yearCounter == currentYear) {
                results = categoryResults.stream()
                        .filter(categoryDto -> overallCategoryResults.stream().filter(overallCategory -> overallCategory.getName().equals(categoryDto.getName())).count() > 0)
                        .sorted(Comparator.comparing(o -> o.getTotalResults().getResult()))
                        .collect(Collectors.toList());
            } else {
                results = categoryResults.stream()
                        .sorted(Comparator.comparing(o -> o.getTotalResults().getResult()))
                        .collect(Collectors.toList());
            }
            yearResults.put(String.valueOf(yearCounter), results);
        }

        yearResults.put("Total", overallCategoryResults);

        return new YearCategoryTableComponent()
                .withResults(yearResults)
                .withNumberOfColumns(11)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .withPrintTotalsColumn(true)
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