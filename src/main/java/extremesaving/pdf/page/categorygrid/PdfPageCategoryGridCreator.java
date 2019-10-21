package extremesaving.pdf.page.categorygrid;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.facade.CategoryFacade;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.enums.PdfGridTimeEnum;
import extremesaving.pdf.page.PdfPageCreator;
import extremesaving.pdf.page.categorygrid.section.CategoryExpensesTableCreator;
import extremesaving.pdf.page.categorygrid.section.CategoryOverallTableCreator;
import extremesaving.pdf.page.categorygrid.section.CategoryProfitsTableCreator;
import extremesaving.pdf.page.categorygrid.section.YearBarChartPdfSectionCreator;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PdfPageCategoryGridCreator implements PdfPageCreator {

    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;

    @Override
    public void generate(Document document) {
        document.add(new YearBarChartPdfSectionCreator().build().getChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));

        document.add(PdfUtils.getTitleParagraph("Result", TextAlignment.LEFT));
        document.add(new CategoryOverallTableCreator()
                .withOverallResults(categoryFacade.getCategories(dataFacade.findAll()))
                .withYearResults(categoryFacade.getCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList())))
                .withMontResults(categoryFacade.getCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList())))
                .withOverallSavingRatio(getSavingRatio(PdfGridTimeEnum.OVERALL))
                .withYearSavingRatio(getSavingRatio(PdfGridTimeEnum.YEAR))
                .withMonthSavingRatio(getSavingRatio(PdfGridTimeEnum.MONTH))
                .build()
                .getTable());
        document.add(PdfUtils.getItemParagraph("\n"));

        document.add(PdfUtils.getTitleParagraph("Most profitable categories", TextAlignment.LEFT));
        document.add(new CategoryProfitsTableCreator()
                .withOverallResults(categoryFacade.getMostProfitableCategories(dataFacade.findAll()))
                .withYearResults(categoryFacade.getMostProfitableCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList())))
                .withMontResults(categoryFacade.getMostProfitableCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList())))
                .build()
                .getTable());
        document.add(PdfUtils.getItemParagraph("\n"));

        document.add(PdfUtils.getTitleParagraph("Most expensive categories", TextAlignment.LEFT));
        document.add(new CategoryExpensesTableCreator()
                .withOverallResults(categoryFacade.getMostExpensiveCategories(dataFacade.findAll()))
                .withYearResults(categoryFacade.getMostExpensiveCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList())))
                .withMontResults(categoryFacade.getMostExpensiveCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList())))
                .build()
                .getTable());
    }

    /**
     * Calculate saving ratio
     */
    protected BigDecimal getSavingRatio(PdfGridTimeEnum pdfGridTimeEnum) {
        List<CategoryDto> profitResults = new ArrayList<>();
        List<CategoryDto> expensesResults = new ArrayList<>();

        if (pdfGridTimeEnum.equals(PdfGridTimeEnum.OVERALL)) {
            profitResults = categoryFacade.getMostProfitableCategories(dataFacade.findAll());
            expensesResults = categoryFacade.getMostExpensiveCategories(dataFacade.findAll());
        }
        if (pdfGridTimeEnum.equals(PdfGridTimeEnum.YEAR)) {
            profitResults = categoryFacade.getMostProfitableCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
            expensesResults = categoryFacade.getMostExpensiveCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        }
        if (pdfGridTimeEnum.equals(PdfGridTimeEnum.MONTH)) {
            profitResults = categoryFacade.getMostProfitableCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
            expensesResults = categoryFacade.getMostExpensiveCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        }

        BigDecimal savingRatio = BigDecimal.ZERO;
        BigDecimal profitAmount = profitResults.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expensesAmount = expensesResults.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expensesAmountReversed = expensesAmount.multiply(BigDecimal.valueOf(-1));

        if (BigDecimal.ZERO.compareTo(expensesAmountReversed) == 0) {
            savingRatio = BigDecimal.valueOf(100);
        } else if (profitAmount.compareTo(expensesAmountReversed) < 0) {
            savingRatio = BigDecimal.ZERO;
        } else if (profitAmount.compareTo(expensesAmountReversed) > 0) {
            savingRatio = BigDecimal.valueOf(100).subtract(expensesAmountReversed.divide(profitAmount, 2, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100)));
        }
        return savingRatio;
    }

    public void setCategoryFacade(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }
}