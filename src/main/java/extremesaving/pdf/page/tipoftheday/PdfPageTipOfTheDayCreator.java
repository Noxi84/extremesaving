package extremesaving.pdf.page.tipoftheday;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.calculation.facade.EstimationFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.page.PdfPageCreator;
import extremesaving.pdf.page.tipoftheday.section.AccountsPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.TipOfTheDayPdfSectionCreator;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.property.PropertyValueEnum;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static extremesaving.property.PropertyValueEnum.GOAL_LINE_CHART_IMAGE_FILE;
import static extremesaving.property.PropertyValueEnum.MONTHLY_BAR_CHART_IMAGE_FILE;
import static extremesaving.property.PropertyValueEnum.YEAR_LINE_CHART_IMAGE_FILE;

public class PdfPageTipOfTheDayCreator implements PdfPageCreator {

    public static float GOAL_LINE_CHART_WIDTH = 530;
    public static float GOAL_LINE_CHART_HEIGHT = 170;

    public static float YEAR_LINE_CHART_WIDTH = 530;
    public static float YEAR_LINE_CHART_HEIGHT = 170;

    public static float MONTHCHART_WIDTH = 530;
    public static float MONTHCHART_HEIGHT = 170;

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;
    private EstimationFacade estimationFacade;
    private TipOfTheDayPdfSectionCreator tipOfTheDayPdfSectionCreator;
    private AccountsPdfSectionCreator accountsPdfSectionCreator;

    @Override
    public void generate(Document document) {
        List<DataDto> dataDtos = dataFacade.findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);

        Table table = new Table(2);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getGoalAndAwardsCell(resultDto));
        table.addCell(getStatisticsCell());
        document.add(table);

        document.add(getGoalLineChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));

        Table table2 = new Table(2);
        table2.setWidth(UnitValue.createPercentValue(100));
        table2.addCell(accountsPdfSectionCreator.getAccountsCell());
        table2.addCell(tipOfTheDayPdfSectionCreator.getTipOfTheDayCell());
        document.add(table2);

        document.add(getMonthBarChartImage());
        document.add(getYearLineChartImage());
    }

    protected Image getGoalLineChartImage() {
        try {
            Image image = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(GOAL_LINE_CHART_IMAGE_FILE)));
            image.setWidth(GOAL_LINE_CHART_WIDTH);
            image.setHeight(GOAL_LINE_CHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected Image getMonthBarChartImage() {
        try {
            Image monthlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(MONTHLY_BAR_CHART_IMAGE_FILE)));
            monthlyBarChartImage.setWidth(MONTHCHART_WIDTH);
            monthlyBarChartImage.setHeight(MONTHCHART_HEIGHT);
            return monthlyBarChartImage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected Image getYearLineChartImage() {
        try {
            Image image = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(YEAR_LINE_CHART_IMAGE_FILE)));
            image.setWidth(YEAR_LINE_CHART_WIDTH);
            image.setHeight(YEAR_LINE_CHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected Cell getStatisticsCell() {
        Cell balanceCell = new Cell();
        balanceCell.setWidth(UnitValue.createPercentValue(25));
        balanceCell.setBorder(Border.NO_BORDER);
        balanceCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        balanceCell.setTextAlignment(TextAlignment.CENTER);
        balanceCell.setWidth(500);
        balanceCell.add(PdfUtils.getTitleParagraph("Statistics", TextAlignment.CENTER));
        balanceCell.add(PdfUtils.getItemParagraph("\n"));

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

        SimpleDateFormat sf = new SimpleDateFormat(" d MMMM yyyy");
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Last update"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(sf.format(new Date())));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Last item added"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(sf.format(calculationFacade.getLastItemAdded())));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("\n"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph("\n"));
        alignmentTableRight.add(PdfUtils.getItemParagraph("\n"));

        SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM");

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Best month"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(monthDateFormat.format(calculationFacade.getBestMonth())));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Worst month"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(monthDateFormat.format(calculationFacade.getWorstMonth())));

        SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Best year"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(yearDateFormat.format(calculationFacade.getBestYear())));

        alignmentTableLeft.add(PdfUtils.getItemParagraph("Worst year"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(yearDateFormat.format(calculationFacade.getWorstYear())));

        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableCenter);
        alignmentTable.addCell(alignmentTableRight);

        balanceCell.add(alignmentTable);
        return balanceCell;
    }

    protected Cell getGoalAndAwardsCell(ResultDto resultDto) {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setTextAlignment(TextAlignment.CENTER);
        chartCell.setWidth(UnitValue.createPercentValue(50));

        if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
            if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal previousGoalAmount = estimationFacade.getPreviousGoal();
                Date goalReachedDate = estimationFacade.getGoalReachedDate(previousGoalAmount);
                BigDecimal goalAmount = estimationFacade.getCurrentGoal();

                BigDecimal goalPercentageAmount = goalAmount.subtract(previousGoalAmount);
                BigDecimal currentGoalAmount = resultDto.getResult().subtract(previousGoalAmount);
                BigDecimal goalPercentage = currentGoalAmount.divide(goalPercentageAmount, 2, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100));

                chartCell.add(getGoalTropheeImage(goalAmount));
                chartCell.add(PdfUtils.getItemParagraph("Save " + NumberUtils.formatNumber(resultDto.getResult(), false) + " / " + NumberUtils.formatNumber(goalAmount, false) + " (" + NumberUtils.formatPercentage(goalPercentage) + ")", true));
                chartCell.add(PdfUtils.getItemParagraph("Estimated time: " + DateUtils.formatTimeLeft(estimationFacade.getGoalTime(goalAmount)), false));
                chartCell.add(PdfUtils.getItemParagraph("Previous goal " + NumberUtils.formatNumber(previousGoalAmount, false) + " reached on " + new SimpleDateFormat("d MMMM yyyy").format(goalReachedDate)));
            }
            chartCell.add(PdfUtils.getItemParagraph("Estimated survival time without incomes: " + DateUtils.formatTimeLeft(estimationFacade.getSurvivalDays()), false));
            chartCell.add(PdfUtils.getItemParagraph("\n"));
        }
        return chartCell;
    }

    protected Image getGoalTropheeImage(BigDecimal goal) {
        int goalIndex = estimationFacade.getGoalIndex(goal);
        Image trophyIcon = null;
        try {
            if (goalIndex == 0) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON1)));
            } else if (goalIndex == 1) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON2)));
            } else if (goalIndex == 2) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON3)));
            } else if (goalIndex == 3) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON4)));
            } else if (goalIndex == 4) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON5)));
            } else if (goalIndex == 5) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON6)));
            } else if (goalIndex == 6) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON7)));
            } else if (goalIndex == 7) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON8)));
            } else if (goalIndex == 8) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON9)));
            } else if (goalIndex == 9) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON10)));
            } else if (goalIndex == 10) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON11)));
            } else if (goalIndex == 11) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON12)));
            } else if (goalIndex == 12) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON13)));
            } else if (goalIndex == 13) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON14)));
            } else if (goalIndex == 14) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON15)));
            } else if (goalIndex == 15) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON16)));
            } else if (goalIndex == 16) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON17)));
            } else {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(PropertyValueEnum.TROPHY_ICON18)));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        trophyIcon.setHorizontalAlignment(HorizontalAlignment.CENTER);
        trophyIcon.setTextAlignment(TextAlignment.CENTER);
        trophyIcon.setWidth(72);
        trophyIcon.setHeight(72);
        return trophyIcon;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setEstimationFacade(EstimationFacade estimationFacade) {
        this.estimationFacade = estimationFacade;
    }

    public void setTipOfTheDayPdfSectionCreator(TipOfTheDayPdfSectionCreator tipOfTheDayPdfSectionCreator) {
        this.tipOfTheDayPdfSectionCreator = tipOfTheDayPdfSectionCreator;
    }

    public void setAccountsPdfSectionCreator(AccountsPdfSectionCreator accountsPdfSectionCreator) {
        this.accountsPdfSectionCreator = accountsPdfSectionCreator;
    }
}