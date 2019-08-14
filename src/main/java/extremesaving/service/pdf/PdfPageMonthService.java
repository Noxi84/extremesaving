package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.service.CategoryService;
import extremesaving.service.DataService;
import extremesaving.service.pdf.enums.PdfGridTypeEnum;
import extremesaving.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static extremesaving.util.PropertyValueENum.MONTHLY_BAR_CHART_IMAGE_FILE;

public class PdfPageMonthService implements PdfPageService {

    public static float MONTHCHART_WIDTH = 525;
    public static float MONTHCHART_HEIGHT = 350;

    private DataService dataService;
    private CategoryService categoryService;

    @Override
    public void generate(Document document) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        addSavingRatio(document);

        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getMonthChartCell());
        document.add(table);

        document.add(getItemsSection(document, PdfGridTypeEnum.PROFITS));
        document.add(getItemsSection(document, PdfGridTypeEnum.EXPENSES));
    }

    public Cell getMonthChartCell() {
        try {
            Cell chartCell1 = new Cell();
            chartCell1.setBorder(Border.NO_BORDER);
            Image monthlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(MONTHLY_BAR_CHART_IMAGE_FILE)));
            monthlyBarChartImage.setWidth(MONTHCHART_WIDTH);
            monthlyBarChartImage.setHeight(MONTHCHART_HEIGHT);
            chartCell1.add(monthlyBarChartImage);
            return chartCell1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Table getItemsSection(Document document, PdfGridTypeEnum pdfGridTypeEnum) {
        String title = "";
        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            title = "Most profitable items";
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            title = "Most expensive items";
        }

        document.add(PdfUtils.getTitleParagraph(title, TextAlignment.LEFT));

        List<ResultDto> monthResults = new ArrayList<>();

        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            monthResults = dataService.getMostProfitableItems(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            monthResults = dataService.getMostExpensiveItems(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        }

        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(PdfUtils.getItemCell("This month", monthResults));
        return table;
    }

    private void addSavingRatio(Document document) {
        Paragraph savingRatioTitle = PdfUtils.getItemParagraph("Saving ratio");
        savingRatioTitle.setBold();
        document.add(savingRatioTitle);

        // Calculate saving ratio
        List<CategoryDto> profitResults = categoryService.getMostProfitableCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        List<CategoryDto> expensesResults = categoryService.getMostExpensiveCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));

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

        Paragraph savingRatioParagraph = PdfUtils.getItemParagraph(NumberUtils.formatPercentage(savingRatio));
        savingRatioParagraph.setBold();
        savingRateRightCell.add(savingRatioParagraph);

        // Add cells to table
        savingRatiotable.addCell(savingRateLeftCell);
        savingRatiotable.addCell(savingRateRightCell);

        document.add(savingRatiotable);
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
        savingRateIcon.setWidth(72);
        savingRateIcon.setHeight(72);
        return savingRateIcon;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}