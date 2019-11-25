package extremesaving.pdf.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.facade.CategoryFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.categorygrid.CategoryOverallTableComponent;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryGridPageServiceImpl implements PdfPageService {

    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating CategoryGridPage");

        document.add(PdfUtils.getTitleParagraph("Result", TextAlignment.LEFT));
        document.add(buildCategoryOverallTable());
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

    public void setCategoryFacade(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }
}