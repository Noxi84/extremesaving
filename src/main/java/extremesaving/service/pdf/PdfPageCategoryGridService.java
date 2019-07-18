package extremesaving.service.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.dto.CategoryDto;
import extremesaving.service.CategoryService;
import extremesaving.service.DataService;
import extremesaving.service.pdf.enums.PdfGridTimeEnum;
import extremesaving.service.pdf.enums.PdfGridTypeEnum;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;
import extremesaving.util.PropertiesValueHolder;
import extremesaving.util.PropertyValueENum;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PdfPageCategoryGridService implements PdfPageService {

    private DataService dataService;
    private CategoryService categoryService;

    @Override
    public void generate(Document document) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(getCategorySection(document, PdfGridTypeEnum.RESULT));
        document.add(getCategorySection(document, PdfGridTypeEnum.PROFITS));
        document.add(getCategorySection(document, PdfGridTypeEnum.EXPENSES));
    }

    private Table getCategorySection(Document document, PdfGridTypeEnum pdfGridTypeEnum) {
        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            document.add(getTitleParagraph("Most profitable categories"));
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            document.add(getTitleParagraph("Most expensive categories"));
        } else if (PdfGridTypeEnum.RESULT.equals(pdfGridTypeEnum)) {
            document.add(getTitleParagraph("Result"));
        }

        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));

        List<CategoryDto> overallResults = new ArrayList<>();
        List<CategoryDto> yearResults = new ArrayList<>();
        List<CategoryDto> monthResults = new ArrayList<>();

        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            overallResults = categoryService.getMostProfitableCategories(dataService.findAll());
            yearResults = categoryService.getMostProfitableCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            monthResults = categoryService.getMostProfitableCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            overallResults = categoryService.getMostExpensiveCategories(dataService.findAll());
            yearResults = categoryService.getMostExpensiveCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            monthResults = categoryService.getMostExpensiveCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        } else if (PdfGridTypeEnum.RESULT.equals(pdfGridTypeEnum)) {
            overallResults = categoryService.getCategories(dataService.findAll());
            yearResults = categoryService.getCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            monthResults = categoryService.getCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        }

        table.addCell(getCategoryCell("Overall", overallResults, PdfGridTimeEnum.OVERALL, pdfGridTypeEnum));
        table.addCell(getCategoryCell("This year", yearResults, PdfGridTimeEnum.YEAR, pdfGridTypeEnum));
        table.addCell(getCategoryCell("This month", monthResults, PdfGridTimeEnum.MONTH, pdfGridTypeEnum));
        return table;
    }

    private Paragraph getTitleParagraph(String title) {
        Paragraph summaryTitle = new Paragraph(title);
        summaryTitle.setBold();
        try {
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.COURIER);
            summaryTitle.setFont(regular);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return summaryTitle;
    }

    private Cell getCategoryCell(String title, List<CategoryDto> categoryDtos, PdfGridTimeEnum pdfGridTimeEnum, PdfGridTypeEnum pdfGridTypeEnum) {
        Cell cell = new Cell();
//        cell.setBorder(Border.NO_BORDER);

        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum) || PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            Paragraph cellTitle = getItemParagraph(title);
            cellTitle.setTextAlignment(TextAlignment.CENTER);
            cellTitle.setBold();
            cell.add(cellTitle);
        }

        Table alignmentTable = new Table(2);

        // Create left cell
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setWidth(300);

        // Create right cell
        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight.setWidth(100);

        // Add categoryDtos
        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum) || PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            for (CategoryDto categoryDto : categoryDtos) {
                alignmentTableLeft.add(getItemParagraph(categoryDto.getName()));
                alignmentTableRight.add(getItemParagraph(NumberUtils.formatNumber(categoryDto.getTotalResults().getResult())));
            }
        }

        // Add total amount
        Paragraph totalTitle = getItemParagraph("Total");
        totalTitle.setBold();
        alignmentTableLeft.add(totalTitle);
        BigDecimal totalAmount = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        Paragraph totalAmountParagraph = getItemParagraph(NumberUtils.formatNumber(totalAmount));
        totalAmountParagraph.setBold();
        alignmentTableRight.add(totalAmountParagraph);

        if (PdfGridTypeEnum.RESULT.equals(pdfGridTypeEnum)) {

            addSavingRatio(alignmentTableLeft, alignmentTableRight, pdfGridTimeEnum);

        }

        // Add left and right cell
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);

        cell.add(alignmentTable);

        return cell;
    }

    private void addSavingRatio(Cell alignmentTableLeft, Cell alignmentTableRight, PdfGridTimeEnum pdfGridTimeEnum) {
        Paragraph savingRatioTitle = getItemParagraph("Saving ratio");
        savingRatioTitle.setBold();
        alignmentTableLeft.add(savingRatioTitle);

        // Calculate saving ratio
        List<CategoryDto> profitResults = new ArrayList<>();
        List<CategoryDto> expensesResults = new ArrayList<>();

        if (pdfGridTimeEnum.equals(PdfGridTimeEnum.OVERALL)) {
            profitResults = categoryService.getMostProfitableCategories(dataService.findAll());
            expensesResults = categoryService.getMostExpensiveCategories(dataService.findAll());
        }
        if (pdfGridTimeEnum.equals(PdfGridTimeEnum.YEAR)) {
            profitResults = categoryService.getMostProfitableCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            expensesResults = categoryService.getMostExpensiveCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        }
        if (pdfGridTimeEnum.equals(PdfGridTimeEnum.MONTH)) {
            profitResults = categoryService.getMostProfitableCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            expensesResults = categoryService.getMostExpensiveCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
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
            savingRatio = BigDecimal.valueOf(100).subtract(expensesAmountReversed.divide(profitAmount, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100)));
        }

        // Create saving ratio table
        Table savingRatiotable = new Table(2);
        savingRatiotable.setBorder(Border.NO_BORDER);
        savingRatiotable.setTextAlignment(TextAlignment.RIGHT);
        savingRatiotable.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        // Saving rate image
        Cell savingRateLeftCell = new Cell();
        savingRateLeftCell.setWidth(20);
        savingRateLeftCell.setBorder(Border.NO_BORDER);
        savingRateLeftCell.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        savingRateLeftCell.setTextAlignment(TextAlignment.RIGHT);
        savingRateLeftCell.setPaddingLeft(0);
        savingRateLeftCell.setPaddingRight(10);
        savingRateLeftCell.setMarginLeft(0);
        savingRateLeftCell.setMarginRight(0);
        savingRateLeftCell.add(getSavingRatioImage(savingRatio));

        // Saving rate percentage
        Cell savingRateRightCell = new Cell();
        savingRateRightCell.setBorder(Border.NO_BORDER);
        savingRateLeftCell.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        savingRateLeftCell.setTextAlignment(TextAlignment.RIGHT);
        savingRateRightCell.setWidth(10);
        savingRateRightCell.setPaddingLeft(0);
        savingRateRightCell.setPaddingRight(0);
        savingRateRightCell.setMarginLeft(0);
        savingRateRightCell.setMarginRight(0);

        Paragraph savingRatioParagraph = getItemParagraph(NumberUtils.formatPercentage(savingRatio));
        savingRatioParagraph.setBold();
        savingRateRightCell.add(savingRatioParagraph);

        // Add cells to table
        savingRatiotable.addCell(savingRateLeftCell);
        savingRatiotable.addCell(savingRateRightCell);

        alignmentTableRight.add(savingRatiotable);
    }

    private Image getSavingRatioImage(BigDecimal savingRatio) {
        Image savingRateIcon = null;
        try {
            if (savingRatio.compareTo(BigDecimal.valueOf(90)) >= 0) {
                savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.SAVING_RATE_ICON9)));
            } else if (savingRatio.compareTo(BigDecimal.valueOf(80)) >= 0) {
                savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.SAVING_RATE_ICON9)));
            } else if (savingRatio.compareTo(BigDecimal.valueOf(70)) >= 0) {
                savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.SAVING_RATE_ICON8)));
            } else if (savingRatio.compareTo(BigDecimal.valueOf(60)) >= 0) {
                savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.SAVING_RATE_ICON7)));
            } else if (savingRatio.compareTo(BigDecimal.valueOf(50)) >= 0) {
                savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.SAVING_RATE_ICON6)));
            } else if (savingRatio.compareTo(BigDecimal.valueOf(40)) >= 0) {
                savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.SAVING_RATE_ICON5)));
            } else if (savingRatio.compareTo(BigDecimal.valueOf(30)) >= 0) {
                savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.SAVING_RATE_ICON4)));
            } else if (savingRatio.compareTo(BigDecimal.valueOf(20)) >= 0) {
                savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.SAVING_RATE_ICON3)));
            } else if (savingRatio.compareTo(BigDecimal.valueOf(10)) >= 0) {
                savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.SAVING_RATE_ICON2)));
            } else {
                savingRateIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.SAVING_RATE_ICON1)));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        savingRateIcon.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        savingRateIcon.setTextAlignment(TextAlignment.RIGHT);
        savingRateIcon.setWidth(12);
        savingRateIcon.setHeight(12);
        return savingRateIcon;
    }

    private Paragraph getItemParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(9);
        try {
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.COURIER);
            paragraph.setFont(regular);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paragraph;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}