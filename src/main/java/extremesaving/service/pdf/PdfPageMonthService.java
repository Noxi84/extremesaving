package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.dto.CategoryDto;
import extremesaving.service.CategoryService;
import extremesaving.service.DataService;
import extremesaving.service.pdf.enums.PdfGridTimeEnum;
import extremesaving.service.pdf.enums.PdfGridTypeEnum;
import extremesaving.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
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
        Table table = new Table(2);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getSavingRatioCell());
        table.addCell(getStatisticsCell());
        document.add(table);

        document.add(getMonthChart());

        document.add(getCategorySection(document, PdfGridTypeEnum.PROFITS));
        document.add(getCategorySection(document, PdfGridTypeEnum.EXPENSES));
    }

    private Cell getStatisticsCell() {
        Cell cell = new Cell();
        cell.setWidth(UnitValue.createPercentValue(50));
        cell.setBorder(Border.NO_BORDER);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setTextAlignment(TextAlignment.CENTER);

        Table alignmentTable = new Table(3);

        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setTextAlignment(TextAlignment.LEFT);
        alignmentTableLeft.setPaddingLeft(20);

        Cell alignmentTableCenter = new Cell();
        alignmentTableCenter.setBorder(Border.NO_BORDER);
        alignmentTableCenter.setTextAlignment(TextAlignment.CENTER);

        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);


        alignmentTableLeft.add(PdfUtils.getItemParagraph("Items added"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(String.valueOf(dataService.getTotalItems())));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("\n"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph("\n"));
        alignmentTableRight.add(PdfUtils.getItemParagraph("\n"));

        SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM");

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Best day"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(monthDateFormat.format(dataService.getBestMonth())));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Worst day"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(monthDateFormat.format(dataService.getWorstMonth())));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Best month"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(monthDateFormat.format(dataService.getBestMonth())));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Worst month"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(monthDateFormat.format(dataService.getWorstMonth())));


        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableCenter);
        alignmentTable.addCell(alignmentTableRight);

        cell.add(alignmentTable);
        return cell;
    }

    public Image getMonthChart() {
        try {
            Image monthlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(MONTHLY_BAR_CHART_IMAGE_FILE)));
            monthlyBarChartImage.setWidth(MONTHCHART_WIDTH);
            monthlyBarChartImage.setHeight(MONTHCHART_HEIGHT);
            return monthlyBarChartImage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Table getCategorySection(Document document, PdfGridTypeEnum pdfGridTypeEnum) {
        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            document.add(PdfUtils.getTitleParagraph("Most profitable categories", TextAlignment.LEFT));
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            document.add(PdfUtils.getTitleParagraph("Most expensive categories", TextAlignment.LEFT));
        } else if (PdfGridTypeEnum.RESULT.equals(pdfGridTypeEnum)) {
            document.add(PdfUtils.getTitleParagraph("Result", TextAlignment.LEFT));
        }

        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));

        List<CategoryDto> monthResults = new ArrayList<>();

        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            monthResults = categoryService.getMostProfitableCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            monthResults = categoryService.getMostExpensiveCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        } else if (PdfGridTypeEnum.RESULT.equals(pdfGridTypeEnum)) {
            monthResults = categoryService.getCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        }

        table.addCell(PdfUtils.getCategoryCell("This month", monthResults, PdfGridTimeEnum.MONTH, pdfGridTypeEnum));
        return table;
    }


    private Cell getSavingRatioCell() {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setWidth(UnitValue.createPercentValue(50));

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

        cell.add(getSavingRatioImage(savingRatio));
        cell.add(PdfUtils.getItemParagraph("\n"));
        cell.add(PdfUtils.getItemParagraph("Result: € -392.57", true));
        cell.add(PdfUtils.getItemParagraph("Saving ratio " + NumberUtils.formatPercentage(savingRatio)));

        return cell;
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
        savingRateIcon.setHorizontalAlignment(HorizontalAlignment.CENTER);
        savingRateIcon.setTextAlignment(TextAlignment.CENTER);
        savingRateIcon.setWidth(30);
        savingRateIcon.setHeight(30);
        return savingRateIcon;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}