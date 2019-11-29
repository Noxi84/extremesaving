package extremesaving.pdf.service;

import com.itextpdf.layout.Document;
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
import extremesaving.pdf.component.chart.GoalLineChartImageComponent;
import extremesaving.pdf.component.itemgrid.CategoryTableComponent;
import extremesaving.pdf.component.itemgrid.ItemTableComponent;
import extremesaving.pdf.component.summary.SummaryTableComponent;
import extremesaving.pdf.util.PdfUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OverallItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 10;
    private static final int TEXT_MAX_CHARACTERS = 200;

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;
    private CategoryFacade categoryFacade;
    private ChartFacade chartFacade;
    private EstimationFacade estimationFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating Overall Analysis Report");

        document.add(PdfUtils.getTitleParagraph("Overall Analysis Report", TextAlignment.LEFT));

        document.add(buildSummaryTable());
        document.add(PdfUtils.getItemParagraph("\n"));

        document.add(buildGoalLineChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));

        document.add(PdfUtils.getTitleParagraph("Most profitable items", TextAlignment.LEFT));
        document.add(buildCategoryProfitsTable());
        document.add(buildItemProfitsTable());

        document.add(PdfUtils.getTitleParagraph("Most expensive items", TextAlignment.LEFT));
        document.add(buildCategoryExpensesTable());
        document.add(buildItemExpensesTable());
    }

    protected Table buildSummaryTable() {
        List<CategoryDto> results = categoryFacade.getCategories(dataFacade.findAll());
        BigDecimal previousGoal = estimationFacade.getPreviousGoal();
        BigDecimal currentGoal = estimationFacade.getCurrentGoal();

        return new SummaryTableComponent()
                .withResults(results)
                .withSavingRatio(getSavingRatio())
                .withPreviousGoal(previousGoal)
                .withCurrentGoal(currentGoal)
                .withGoalIndex(estimationFacade.getGoalIndex(currentGoal))
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
        List<CategoryDto> results = categoryFacade.getCategories(dataFacade.findAll()).stream()
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
        List<ResultDto> results = calculationFacade.getMostProfitableItems(dataFacade.findAll());
        return new ItemTableComponent()
                .withResults(results)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build();
    }

    protected Table buildCategoryExpensesTable() {
        List<CategoryDto> results = categoryFacade.getCategories(dataFacade.findAll()).stream()
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
        List<ResultDto> overallResults = calculationFacade.getMostExpensiveItems(dataFacade.findAll());
        return new ItemTableComponent()
                .withResults(overallResults)
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