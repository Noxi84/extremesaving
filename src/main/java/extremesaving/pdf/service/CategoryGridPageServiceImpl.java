package extremesaving.pdf.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.facade.CategoryFacade;
import extremesaving.charts.facade.ChartFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.categorygrid.CategoryExpensesTableComponent;
import extremesaving.pdf.component.categorygrid.CategoryOverallTableComponent;
import extremesaving.pdf.component.categorygrid.CategoryProfitsTableComponent;
import extremesaving.pdf.component.categorygrid.YearBarChartImageComponent;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryGridPageServiceImpl implements PdfPageService {

    private ChartFacade chartFacade;
    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating CategoryGridPage");

        document.add(buildYearBarChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));

        document.add(PdfUtils.getTitleParagraph("Result", TextAlignment.LEFT));
        document.add(buildCategoryOverallTable());
        document.add(PdfUtils.getItemParagraph("\n"));

        document.add(PdfUtils.getTitleParagraph("Most profitable categories", TextAlignment.LEFT));
        document.add(buildCategoryProfitsTable());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(PdfUtils.getTitleParagraph("Most expensive categories", TextAlignment.LEFT));
        document.add(buildCategoryExpensesTable());
    }

    protected Image buildYearBarChartImage() {
        chartFacade.generateYearBarChart();
        return new YearBarChartImageComponent().build();
    }

    protected Table buildCategoryOverallTable() {
        List<CategoryDto> overallResults = categoryFacade.getCategories(dataFacade.findAll());
        List<CategoryDto> yearResults = categoryFacade.getCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        List<CategoryDto> monthResults = categoryFacade.getCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        return new CategoryOverallTableComponent()
                .withOverallResults(overallResults)
                .withYearResults(monthResults)
                .withMontResults(yearResults)
                .withOverallSavingRatio(getOverallSavingRatio())
                .withYearSavingRatio(getYearSavingRatio())
                .withMonthSavingRatio(getMonthSavingRatio())
                .build();
    }

    protected Table buildCategoryProfitsTable() {
        List<CategoryDto> overallResults = categoryFacade.getMostProfitableCategories(dataFacade.findAll());
        List<CategoryDto> yearResults = categoryFacade.getMostProfitableCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        List<CategoryDto> monthResults = categoryFacade.getMostProfitableCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        return new CategoryProfitsTableComponent()
                .withOverallResults(overallResults)
                .withYearResults(yearResults)
                .withMonthResults(monthResults)
                .build();
    }

    protected Table buildCategoryExpensesTable() {
        List<CategoryDto> overallResults = categoryFacade.getMostExpensiveCategories(dataFacade.findAll());
        List<CategoryDto> yearResults = categoryFacade.getMostExpensiveCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        List<CategoryDto> monthResults = categoryFacade.getMostExpensiveCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        return new CategoryExpensesTableComponent()
                .withOverallResults(overallResults)
                .withYearResults(yearResults)
                .withMontResults(monthResults)
                .build();
    }

    protected BigDecimal getOverallSavingRatio() {
        List<DataDto> dataDtos = dataFacade.findAll();
        List<CategoryDto> profitResults = categoryFacade.getMostProfitableCategories(dataDtos);
        List<CategoryDto> expensesResults = categoryFacade.getMostExpensiveCategories(dataDtos);
        return calculateSavingRatio(profitResults, expensesResults);
    }

    protected BigDecimal getYearSavingRatio() {
        List<DataDto> dataDtos = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<CategoryDto> profitResults = categoryFacade.getMostProfitableCategories(dataDtos);
        List<CategoryDto> expensesResults = categoryFacade.getMostExpensiveCategories(dataDtos);
        return calculateSavingRatio(profitResults, expensesResults);
    }

    protected BigDecimal getMonthSavingRatio() {
        List<DataDto> dataDtos = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<CategoryDto> profitResults = categoryFacade.getMostProfitableCategories(dataDtos);
        List<CategoryDto> expensesResults = categoryFacade.getMostExpensiveCategories(dataDtos);
        return calculateSavingRatio(profitResults, expensesResults);
    }

    protected BigDecimal calculateSavingRatio(List<CategoryDto> profitResults, List<CategoryDto> expensesResults) {
        BigDecimal profitAmount = profitResults.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expensesAmount = expensesResults.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expensesAmountReversed = expensesAmount.multiply(BigDecimal.valueOf(-1));
        if (BigDecimal.ZERO.compareTo(expensesAmountReversed) == 0) {
            return BigDecimal.valueOf(100);
        } else if (profitAmount.compareTo(expensesAmountReversed) > 0) {
            return BigDecimal.valueOf(100).subtract(expensesAmountReversed.divide(profitAmount, 2, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100)));
        }
        return BigDecimal.ZERO;
    }

    public void setChartFacade(ChartFacade chartFacade) {
        this.chartFacade = chartFacade;
    }

    public void setCategoryFacade(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }
}