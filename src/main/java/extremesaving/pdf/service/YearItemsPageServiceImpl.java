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
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.itemgrid.CategoryTableComponent;
import extremesaving.pdf.component.itemgrid.ItemTableComponent;
import extremesaving.pdf.component.chart.YearLineChartImageComponent;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class YearItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 10;
    private static final int TEXT_MAX_CHARACTERS = 200;

    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;
    private CalculationFacade calculationFacade;
    private ChartFacade chartFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating YearItemsPageServiceImpl");

        document.add(PdfUtils.getTitleParagraph("Year Report", TextAlignment.LEFT));
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(buildYearLineChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(PdfUtils.getTitleParagraph("Most profitable items", TextAlignment.LEFT));
        document.add(buildCategoryProfitsTable());
        document.add(buildItemProfitsTable());
        document.add(PdfUtils.getTitleParagraph("Most expensive items", TextAlignment.LEFT));
        document.add(buildCategoryExpensesTable());
        document.add(buildItemExpensesTable());
    }

    protected Image buildYearLineChartImage() {
        chartFacade.generateYearLineChart();
        return new YearLineChartImageComponent().build();
    }

    protected Table buildCategoryProfitsTable() {
        List<DataDto> dataDtos = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<CategoryDto> categoryResults = categoryFacade.getCategories(dataDtos);
        List<CategoryDto> results = categoryResults.stream().filter(categoryDto -> NumberUtils.isIncome(categoryDto.getTotalResults().getResult())).collect(Collectors.toList());
        return new CategoryTableComponent()
                .withResults(results)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build();
    }

    protected Table buildItemProfitsTable() {
        List<DataDto> yearResults = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<ResultDto> results = calculationFacade.getMostProfitableItems(yearResults);
        return new ItemTableComponent()
                .withResults(results)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build();
    }

    protected Table buildCategoryExpensesTable() {
        List<DataDto> dataDtos = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<CategoryDto> categoryResults = categoryFacade.getCategories(dataDtos);
        List<CategoryDto> results = categoryResults.stream().filter(categoryDto -> NumberUtils.isExpense(categoryDto.getTotalResults().getResult())).collect(Collectors.toList());
        return new CategoryTableComponent()
                .withResults(results)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build();
    }

    protected Table buildItemExpensesTable() {
        List<DataDto> yearResults = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<ResultDto> results = calculationFacade.getMostExpensiveItems(yearResults);
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
}