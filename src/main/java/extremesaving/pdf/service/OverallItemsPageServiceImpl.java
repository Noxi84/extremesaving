package extremesaving.pdf.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.calculation.facade.CategoryFacade;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.charts.facade.ChartFacade;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.chart.YearBarChartImageComponent;
import extremesaving.pdf.component.itemgrid.CategoryTableComponent;
import extremesaving.pdf.component.itemgrid.ItemTableComponent;
import extremesaving.pdf.util.PdfUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OverallItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 10;
    private static final int TEXT_MAX_CHARACTERS = 200;

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;
    private CategoryFacade categoryFacade;
    private ChartFacade chartFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating OverallItemsPage");

        document.add(PdfUtils.getTitleParagraph("Overall Report", TextAlignment.LEFT));
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(buildYearBarChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(PdfUtils.getTitleParagraph("Most profitable items", TextAlignment.LEFT));
        document.add(buildCategoryProfitsTable());
        document.add(buildItemProfitsTable());
        document.add(PdfUtils.getTitleParagraph("Most expensive items", TextAlignment.LEFT));
        document.add(buildCategoryExpensesTable());
        document.add(buildItemExpensesTable());
    }

    protected Image buildYearBarChartImage() {
        chartFacade.generateYearBarChart();
        return new YearBarChartImageComponent().build();
    }

    protected Table buildCategoryProfitsTable() {
        List<CategoryDto> results = categoryFacade.getCategories(dataFacade.findAll()).stream().filter(categoryDto -> NumberUtils.isIncome(categoryDto.getTotalResults().getResult())).collect(Collectors.toList());
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
        List<CategoryDto> results = categoryFacade.getCategories(dataFacade.findAll()).stream().filter(categoryDto -> NumberUtils.isExpense(categoryDto.getTotalResults().getResult())).collect(Collectors.toList());
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
}