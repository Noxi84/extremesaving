package extremesaving.pdf.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.facade.CategoryFacade;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.enums.PdfGridTimeEnum;
import extremesaving.pdf.section.YearBarChartPdfSectionCreator;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.property.PropertyValueEnum;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static extremesaving.property.PropertyValueEnum.SAVING_RATE_ICON1;

public class PdfPageCategoryGridService implements PdfPageService {

    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;
    private YearBarChartPdfSectionCreator yearBarChartPdfSectionCreator;

    @Override
    public void generate(Document document) {
        document.add(yearBarChartPdfSectionCreator.getYearChart());
        document.add(PdfUtils.getItemParagraph("\n"));

        document.add(PdfUtils.getTitleParagraph("Result", TextAlignment.LEFT));
        document.add(getOverallCategoryTable());
        document.add(PdfUtils.getItemParagraph("\n"));

        document.add(PdfUtils.getTitleParagraph("Most profitable categories", TextAlignment.LEFT));
        document.add(getProfitsTable());
        document.add(PdfUtils.getItemParagraph("\n"));

        document.add(PdfUtils.getTitleParagraph("Most expensive categories", TextAlignment.LEFT));
        document.add(getExpensesTable());
    }

    protected Table getOverallCategoryTable() {
        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        List<CategoryDto> overallResults = categoryFacade.getCategories(dataFacade.findAll());
        List<CategoryDto> yearResults = categoryFacade.getCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        List<CategoryDto> monthResults = categoryFacade.getCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        table.addCell(getResultCategoryCell(overallResults, PdfGridTimeEnum.OVERALL));
        table.addCell(getResultCategoryCell(yearResults, PdfGridTimeEnum.YEAR));
        table.addCell(getResultCategoryCell(monthResults, PdfGridTimeEnum.MONTH));
        return table;
    }

    protected Table getProfitsTable() {
        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        List<CategoryDto> overallResults = categoryFacade.getMostProfitableCategories(dataFacade.findAll());
        List<CategoryDto> yearResults = categoryFacade.getMostProfitableCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        List<CategoryDto> monthResults = categoryFacade.getMostProfitableCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        table.addCell(getCategoryCell("Overall", overallResults));
        table.addCell(getCategoryCell("This year", yearResults));
        table.addCell(getCategoryCell("This month", monthResults));
        return table;
    }

    protected Table getExpensesTable() {
        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        List<CategoryDto> overallResults = categoryFacade.getMostExpensiveCategories(dataFacade.findAll());
        List<CategoryDto> yearResults = categoryFacade.getMostExpensiveCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        List<CategoryDto> monthResults = categoryFacade.getMostExpensiveCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        table.addCell(getCategoryCell("Overall", overallResults));
        table.addCell(getCategoryCell("This year", yearResults));
        table.addCell(getCategoryCell("This month", monthResults));
        return table;
    }

    protected Cell getCategoryCell(String title, List<CategoryDto> categoryDtos) {
        Cell cell = new Cell();

        cell.add(PdfUtils.getItemParagraph(title, true, TextAlignment.CENTER));

        Table alignmentTable = new Table(2);
        alignmentTable.setWidth(UnitValue.createPercentValue(100));

        // Create left cell
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setWidth(280);

        // Create right cell
        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight.setWidth(120);

        // Add categoryDtos
        for (CategoryDto categoryDto : categoryDtos) {
            alignmentTableLeft.add(PdfUtils.getItemParagraph(categoryDto.getName()));
            alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(categoryDto.getTotalResults().getResult())));
        }

        // Add total amount
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total", true));
        BigDecimal totalAmount = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(totalAmount), true));

        // Add cells to table
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);

        cell.add(alignmentTable);

        return cell;
    }

    protected Cell getResultCategoryCell(List<CategoryDto> categoryDtos, PdfGridTimeEnum pdfGridTimeEnum) {
        Cell cell = new Cell();

        BigDecimal savingRatio = getSavingRatio(pdfGridTimeEnum);
        cell.add(getSavingRatioImage(savingRatio));

        Table alignmentTable = new Table(2);
        alignmentTable.setWidth(UnitValue.createPercentValue(100));

        // Create left cell
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setWidth(280);

        // Create right cell
        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight.setWidth(120);

        // Add total amount
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total", true));
        BigDecimal totalAmount = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(totalAmount), true));


        // Add saving ratio
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Saving ratio", true));
        alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatPercentage(savingRatio), true));

        // Add total items
        long totalItems = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getNumberOfItems()).mapToLong(i -> i).sum();
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total items"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(String.valueOf(totalItems)));

        // Add cells to table
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);

        cell.add(alignmentTable);

        return cell;
    }

    protected BigDecimal getSavingRatio(PdfGridTimeEnum pdfGridTimeEnum) {
        // Calculate saving ratio
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

    protected Image getSavingRatioImage(BigDecimal savingRatio) {
        PropertyValueEnum savingRatioImage = SAVING_RATE_ICON1;
        if (savingRatio.compareTo(BigDecimal.valueOf(90)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON9;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(80)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON9;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(70)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON8;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(60)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON7;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(50)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON6;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(40)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON5;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(30)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON4;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(20)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON3;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(10)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON2;
        }
        try {
            Image savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(savingRatioImage)));
            savingRateIcon.setHorizontalAlignment(HorizontalAlignment.CENTER);
            savingRateIcon.setTextAlignment(TextAlignment.CENTER);
            savingRateIcon.setWidth(45);
            savingRateIcon.setHeight(45);
            return savingRateIcon;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCategoryFacade(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setYearBarChartPdfSectionCreator(YearBarChartPdfSectionCreator yearBarChartPdfSectionCreator) {
        this.yearBarChartPdfSectionCreator = yearBarChartPdfSectionCreator;
    }
}