package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.service.CategoryService;
import extremesaving.service.DataService;
import extremesaving.service.pdf.enums.PdfGridTypeEnum;
import extremesaving.util.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static extremesaving.util.PropertyValueENum.MONTHLY_BAR_CHART_IMAGE_FILE;
import static extremesaving.util.PropertyValueENum.MONTH_LINE_CHART_IMAGE_FILE;

public class PdfPageMonthService implements PdfPageService {

//    public static float MONTHCHART_WIDTH = 530;
//    public static float MONTHCHART_HEIGHT = 150;

    public static float MONTHLINECHART_WIDTH = 530;
    public static float MONTHLINECHART_HEIGHT = 150;

    private static final int DISPLAY_MAX_ITEMS = 30;
    private static final int TEXT_MAX_CHARACTERS = 14;

    private DataService dataService;
    private CategoryService categoryService;

    @Override
    public void generate(Document document) {
        Table table2 = new Table(4);
        table2.setWidth(UnitValue.createPercentValue(100));
        table2.setBorder(Border.NO_BORDER);

        BigDecimal savingRatio = getSavingRatio();
        table2.addCell(getSavingRatioImage(savingRatio));
        table2.addCell(getSavingRatioCell1());
        table2.addCell(getSavingRatioCell2(savingRatio));
        table2.addCell(getSavingRatioCell3());

        document.add(table2);
        document.add(getMonthLineChart());

        document.add(getItemsSection(document, PdfGridTypeEnum.PROFITS));
        document.add(getItemsSection(document, PdfGridTypeEnum.EXPENSES));
    }

    private Cell getSavingRatioCell1() {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setWidth(150);

        cell.add(PdfUtils.getItemParagraph("Month Report August 2019", true));
        cell.add(PdfUtils.getItemParagraph("€ -392.57"));

        return cell;
    }

    private Cell getSavingRatioCell2(BigDecimal savingRatio) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setWidth(150);

        cell.add(PdfUtils.getItemParagraph("Saving ratio", true));
        cell.add(PdfUtils.getItemParagraph(NumberUtils.formatPercentage(savingRatio)));

        return cell;
    }

    private Cell getSavingRatioCell3() {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setWidth(150);

        cell.add(PdfUtils.getItemParagraph("Estimated result", true));
        cell.add(PdfUtils.getItemParagraph("€ 982"));

        return cell;
    }

//    private Cell getStatisticsCell() {
//        Cell cell = new Cell();
//        cell.setWidth(UnitValue.createPercentValue(50));
//        cell.setBorder(Border.NO_BORDER);
//        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
//        cell.setTextAlignment(TextAlignment.CENTER);
//
//        cell.add(PdfUtils.getTitleParagraph("Month Report", TextAlignment.RIGHT));
//        cell.add(PdfUtils.getItemParagraph("\n"));
//
//        Table alignmentTable = new Table(3);
//        alignmentTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
//
//        Cell alignmentTableLeft = new Cell();
//        alignmentTableLeft.setBorder(Border.NO_BORDER);
//        alignmentTableLeft.setTextAlignment(TextAlignment.LEFT);
//        alignmentTableLeft.setPaddingLeft(20);
//
//        Cell alignmentTableCenter = new Cell();
//        alignmentTableCenter.setBorder(Border.NO_BORDER);
//        alignmentTableCenter.setTextAlignment(TextAlignment.CENTER);
//
//        Cell alignmentTableRight = new Cell();
//        alignmentTableRight.setBorder(Border.NO_BORDER);
//        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
//
//        alignmentTableLeft.add(PdfUtils.getItemParagraph("Start of month"));
//        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
//        alignmentTableRight.add(PdfUtils.getItemParagraph("€ 17300"));
//
//        alignmentTableLeft.add(PdfUtils.getItemParagraph("Current result"));
//        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
//        alignmentTableRight.add(PdfUtils.getItemParagraph("€ 17000.23"));
//
//        alignmentTableLeft.add(PdfUtils.getItemParagraph("Items added"));
//        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
//        alignmentTableRight.add(PdfUtils.getItemParagraph(String.valueOf(dataService.getTotalItems())));
//
//        alignmentTableLeft.add(PdfUtils.getItemParagraph("\n"));
//        alignmentTableCenter.add(PdfUtils.getItemParagraph("\n"));
//        alignmentTableRight.add(PdfUtils.getItemParagraph("\n"));
//
//        SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM");
//
//        alignmentTableLeft.add(PdfUtils.getItemParagraph("Best day"));
//        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
//        alignmentTableRight.add(PdfUtils.getItemParagraph("Tuesday"));
//
//        alignmentTableLeft.add(PdfUtils.getItemParagraph("Worst day"));
//        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
//        alignmentTableRight.add(PdfUtils.getItemParagraph("Monday"));
//
//        alignmentTableLeft.add(PdfUtils.getItemParagraph("Best month"));
//        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
//        alignmentTableRight.add(PdfUtils.getItemParagraph(monthDateFormat.format(dataService.getBestMonth())));
//
//        alignmentTableLeft.add(PdfUtils.getItemParagraph("Worst month"));
//        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
//        alignmentTableRight.add(PdfUtils.getItemParagraph(monthDateFormat.format(dataService.getWorstMonth())));
//
//
//        alignmentTable.addCell(alignmentTableLeft);
//        alignmentTable.addCell(alignmentTableCenter);
//        alignmentTable.addCell(alignmentTableRight);
//
//        cell.add(alignmentTable);
//        return cell;
//    }

//    public Image getMonthBarChart() {
//        try {
//            Image monthlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(MONTHLY_BAR_CHART_IMAGE_FILE)));
//            monthlyBarChartImage.setWidth(MONTHCHART_WIDTH);
//            monthlyBarChartImage.setHeight(MONTHCHART_HEIGHT);
//            return monthlyBarChartImage;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }

    public Image getMonthLineChart() {
        try {
            Image monthlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(MONTH_LINE_CHART_IMAGE_FILE)));
            monthlyBarChartImage.setWidth(MONTHLINECHART_WIDTH);
            monthlyBarChartImage.setHeight(MONTHLINECHART_HEIGHT);
            return monthlyBarChartImage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private BigDecimal getSavingRatio() {
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
        return savingRatio;
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
        savingRateIcon.setWidth(45);
        savingRateIcon.setHeight(45);
        return savingRateIcon;
    }

    private Table getItemsSection(Document document, PdfGridTypeEnum pdfGridTypeEnum) {
        String title = "";
        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            title = "Most profitable items";
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            title = "Most expensive items";
        }

        document.add(PdfUtils.getTitleParagraph(title, TextAlignment.LEFT));

//        List<ResultDto> overallResults = new ArrayList<>();
//        List<ResultDto> yearResults = new ArrayList<>();
        List<ResultDto> monthResults = new ArrayList<>();

        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
//            overallResults = dataService.getMostProfitableItems(dataService.findAll());
//            yearResults = dataService.getMostProfitableItems(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            monthResults = dataService.getMostProfitableItems(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
//            overallResults = dataService.getMostExpensiveItems(dataService.findAll());
//            yearResults = dataService.getMostExpensiveItems(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            monthResults = dataService.getMostExpensiveItems(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        }

        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));
//        table.addCell(getItemCell("Overall", overallResults));
//        table.addCell(getItemCell("This year", yearResults));
        table.addCell(getItemCell("This month", monthResults));


        return table;
    }

    private Cell getItemCell(String title, List<ResultDto> results) {
        Cell cell = new Cell();
        cell.setWidth(UnitValue.createPercentValue(33));

        Paragraph cell1Title = PdfUtils.getItemParagraph(title);
        cell1Title.setTextAlignment(TextAlignment.CENTER);
        cell1Title.setBold();
        cell.add(cell1Title);

        Table alignmentTable1 = new Table(2);
        Cell alignmentTableLeft1 = new Cell();
        alignmentTableLeft1.setBorder(Border.NO_BORDER);
        alignmentTableLeft1.setWidth(300);

        Cell alignmentTableRight1 = new Cell();
        alignmentTableRight1.setBorder(Border.NO_BORDER);
        alignmentTableRight1.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight1.setWidth(110);

        int counter = 0;
        for (ResultDto resultDto : results) {
            counter++;
            if (counter >= DISPLAY_MAX_ITEMS) {
                break;
            }
            alignmentTableLeft1.add(PdfUtils.getItemParagraph(StringUtils.abbreviate(resultDto.getData().iterator().next().getDescription(), TEXT_MAX_CHARACTERS)));
            alignmentTableRight1.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(resultDto.getResult())));
        }

        alignmentTable1.addCell(alignmentTableLeft1);
        alignmentTable1.addCell(alignmentTableRight1);

        cell.add(alignmentTable1);

        return cell;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}