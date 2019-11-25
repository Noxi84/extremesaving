package extremesaving.pdf.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.calculation.facade.CategoryFacade;
import extremesaving.calculation.facade.EstimationFacade;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.charts.facade.ChartFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.itemgrid.SummaryTableComponent;
import extremesaving.pdf.component.chart.MonthBarChartImageComponent;
import extremesaving.pdf.component.itemgrid.CategoryTableComponent;
import extremesaving.pdf.component.itemgrid.ItemTableComponent;
import extremesaving.pdf.component.tipoftheday.GoalAndAwardsCellComponent;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MonthItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 10;
    private static final int TEXT_MAX_CHARACTERS = 200;

    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;
    private CalculationFacade calculationFacade;
    private ChartFacade chartFacade;
    private EstimationFacade estimationFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating MonthItemsPageServiceImpl");

        document.add(PdfUtils.getTitleParagraph("Monthly Analysis Report", TextAlignment.LEFT));
        document.add(buildSummaryTable());
        document.add(buildMonthBarChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(PdfUtils.getTitleParagraph("Most profitable items", TextAlignment.LEFT));
        document.add(buildCategoryProfitsTable());
        document.add(buildItemProfitsTable());
        document.add(PdfUtils.getTitleParagraph("Most expensive items", TextAlignment.LEFT));
        document.add(buildCategoryExpensesTable());
        document.add(buildItemExpensesTable());
    }

    protected Table buildSummaryTable() {
        List<CategoryDto> results = categoryFacade.getCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        return new SummaryTableComponent()
                .withResults(results)
                .withSavingRatio(getSavingRatio())
                .withGoalAndAwardsCell(buildGoalAndAwardsCell())
                .build();
    }

    protected Cell buildGoalAndAwardsCell() {
        List<DataDto> dataDtos = dataFacade.findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);

        BigDecimal previousGoal = estimationFacade.getPreviousGoal();
        BigDecimal currentGoal = estimationFacade.getCurrentGoal();

        return new GoalAndAwardsCellComponent()
                .withResultDto(resultDto)
                .withPreviousGoal(previousGoal)
                .withPreviousGoalReachDate(estimationFacade.getGoalReachedDate(previousGoal))
                .withCurrentGoal(currentGoal)
                .withGoalIndex(estimationFacade.getGoalIndex(currentGoal))
                .withGoalTime(estimationFacade.getGoalTime(currentGoal))
                .withSurvivalDays(estimationFacade.getSurvivalDays())
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
        List<DataDto> dataDtos = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<CategoryDto> categoryResults = categoryFacade.getCategories(dataDtos);
        List<CategoryDto> results = categoryResults.stream()
                .filter(categoryDto -> NumberUtils.isIncome(categoryDto.getTotalResults().getResult()))
                .sorted((o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()))
                .collect(Collectors.toList());
        return new CategoryTableComponent()
                .withResults(results)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build();
    }

    protected Table buildItemProfitsTable() {
        List<DataDto> monthResults = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<ResultDto> results = calculationFacade.getMostProfitableItems(monthResults);
        return new ItemTableComponent()
                .withResults(results)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build();
    }

    protected Table buildCategoryExpensesTable() {
        List<DataDto> dataDtos = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<CategoryDto> categoryResults = categoryFacade.getCategories(dataDtos);
        List<CategoryDto> results = categoryResults.stream()
                .filter(categoryDto -> NumberUtils.isExpense(categoryDto.getTotalResults().getResult()))
                .sorted(Comparator.comparing(o -> o.getTotalResults().getResult()))
                .collect(Collectors.toList());
        return new CategoryTableComponent()
                .withResults(results)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build();
    }

    protected Table buildItemExpensesTable() {
        List<DataDto> monthResults = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<ResultDto> results = calculationFacade.getMostExpensiveItems(monthResults);
        return new ItemTableComponent()
                .withResults(results)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
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