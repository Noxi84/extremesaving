package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.service.CalculationService;
import extremesaving.service.DataService;
import extremesaving.service.PredictionService;
import extremesaving.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static extremesaving.util.PropertyValueENum.GOAL_LINE_CHART_IMAGE_FILE;

public class PdfPageTipOfTheDayService implements PdfPageService {

    public static float CHART_WIDTH = 770;
    public static float CHART_HEIGHT = 370;

    private DataService dataService;
    private CalculationService calculationService;
    private PredictionService predictionService;

    @Override
    public void generate(Document document) {
        try {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            List<DataModel> dataModels = dataService.findAll();
            ResultDto resultDto = calculationService.getResults(dataModels);

            Table table = new Table(3);
            table.setWidth(UnitValue.createPercentValue(100));
            table.addCell(getBalanceCell());
            table.addCell(getGoalAndAwardsCell(resultDto));
            table.addCell(getTipOfTheDayCell());

            document.add(table);

            Image futureLineChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(GOAL_LINE_CHART_IMAGE_FILE)));
            futureLineChartImage.setWidth(CHART_WIDTH);
            futureLineChartImage.setHeight(CHART_HEIGHT);
            document.add(futureLineChartImage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Cell getBalanceCell() {
        Cell balanceCell = new Cell();
        balanceCell.setWidth(UnitValue.createPercentValue(25));
        balanceCell.setBorder(Border.NO_BORDER);
        balanceCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        balanceCell.setTextAlignment(TextAlignment.CENTER);
        balanceCell.setWidth(500);
        balanceCell.add(ChartUtils.getTitleParagraph("Statistics", TextAlignment.CENTER));
        balanceCell.add(ChartUtils.getItemParagraph("\n"));

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
        alignmentTableLeft.add(ChartUtils.getItemParagraph("Last update"));
        alignmentTableCenter.add(ChartUtils.getItemParagraph(":"));
        alignmentTableRight.add(ChartUtils.getItemParagraph(sf.format(new Date())));

        alignmentTableLeft.add(ChartUtils.getItemParagraph("Last item added"));
        alignmentTableCenter.add(ChartUtils.getItemParagraph(":"));
        alignmentTableRight.add(ChartUtils.getItemParagraph(sf.format(dataService.getLastItemAdded())));

        alignmentTableLeft.add(ChartUtils.getItemParagraph("Total items"));
        alignmentTableCenter.add(ChartUtils.getItemParagraph(":"));
        alignmentTableRight.add(ChartUtils.getItemParagraph(String.valueOf(dataService.getTotalItems())));

        alignmentTableLeft.add(ChartUtils.getItemParagraph("\n"));
        alignmentTableCenter.add(ChartUtils.getItemParagraph("\n"));
        alignmentTableRight.add(ChartUtils.getItemParagraph("\n"));

        SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM");

        alignmentTableLeft.add(ChartUtils.getItemParagraph("Best month"));
        alignmentTableCenter.add(ChartUtils.getItemParagraph(":"));
        alignmentTableRight.add(ChartUtils.getItemParagraph(monthDateFormat.format(dataService.getBestMonth())));

        alignmentTableLeft.add(ChartUtils.getItemParagraph("Worst month"));
        alignmentTableCenter.add(ChartUtils.getItemParagraph(":"));
        alignmentTableRight.add(ChartUtils.getItemParagraph(monthDateFormat.format(dataService.getWorstMonth())));

        SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");
        alignmentTableLeft.add(ChartUtils.getItemParagraph("Best year"));
        alignmentTableCenter.add(ChartUtils.getItemParagraph(":"));
        alignmentTableRight.add(ChartUtils.getItemParagraph(yearDateFormat.format(dataService.getBestYear())));

        alignmentTableLeft.add(ChartUtils.getItemParagraph("Worst year"));
        alignmentTableCenter.add(ChartUtils.getItemParagraph(":"));
        alignmentTableRight.add(ChartUtils.getItemParagraph(yearDateFormat.format(dataService.getWorstYear())));

        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableCenter);
        alignmentTable.addCell(alignmentTableRight);

        balanceCell.add(alignmentTable);
        return balanceCell;
    }

    private Cell getGoalAndAwardsCell(ResultDto resultDto) {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setTextAlignment(TextAlignment.CENTER);
        chartCell.setWidth(UnitValue.createPercentValue(40));

        chartCell.add(ChartUtils.getTitleParagraph("Goals & Awards", TextAlignment.CENTER));
        chartCell.add(ChartUtils.getItemParagraph("\n"));

        if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
            if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal previousGoalAmount = predictionService.getPreviousGoal();
                Date goalReachedDate = predictionService.getGoalReachedDate(previousGoalAmount);
                BigDecimal goalAmount = predictionService.getCurrentGoal();

                BigDecimal goalPercentage = resultDto.getResult().divide(goalAmount, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100));
                chartCell.add(getGoalTropheeImage(goalAmount));
                chartCell.add(ChartUtils.getItemParagraph("Save " + NumberUtils.formatNumber(resultDto.getResult(), false) + " / " + NumberUtils.formatNumber(goalAmount, false) + " (" + NumberUtils.formatPercentage(goalPercentage) + ")", true));
                chartCell.add(ChartUtils.getItemParagraph("Estimated time: " + DateUtils.formatTimeLeft(predictionService.getGoalTime(goalAmount)), false));
                chartCell.add(ChartUtils.getItemParagraph("Previous goal " + NumberUtils.formatNumber(previousGoalAmount, false) + " reached on " + new SimpleDateFormat("d MMMM yyyy").format(goalReachedDate)));
                chartCell.add(ChartUtils.getItemParagraph("\n"));
            }

            chartCell.add(ChartUtils.getItemParagraph("Survive 3 years without incomes.", true));
            chartCell.add(ChartUtils.getItemParagraph("Estimated time: " + DateUtils.formatTimeLeft(predictionService.getSurvivalDays()), false));
            chartCell.add(ChartUtils.getItemParagraph("\n"));

            chartCell.add(ChartUtils.getItemParagraph("Spend less than € 6000 this year.", true));
            chartCell.add(ChartUtils.getItemParagraph("Current expenses: € 5692 "));
            chartCell.add(ChartUtils.getItemParagraph("\n"));
        }
        return chartCell;
    }

    private Image getGoalTropheeImage(BigDecimal goal) {
        int goalIndex = predictionService.getGoalIndex(goal);
        Image trophyIcon = null;
        try {
            if (goalIndex == 0) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON1)));
            } else if (goalIndex == 1) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON2)));
            } else if (goalIndex == 2) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON3)));
            } else if (goalIndex == 3) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON4)));
            } else if (goalIndex == 4) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON5)));
            } else if (goalIndex == 5) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON6)));
            } else if (goalIndex == 6) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON7)));
            } else if (goalIndex == 7) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON8)));
            } else if (goalIndex == 8) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON9)));
            } else if (goalIndex == 9) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON10)));
            } else if (goalIndex == 10) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON11)));
            } else if (goalIndex == 11) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON12)));
            } else if (goalIndex == 12) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON13)));
            } else if (goalIndex == 13) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON14)));
            } else if (goalIndex == 14) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON15)));
            } else if (goalIndex == 15) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON16)));
            } else if (goalIndex == 16) {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON17)));
            } else {
                trophyIcon = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.TROPHY_ICON18)));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        trophyIcon.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        trophyIcon.setTextAlignment(TextAlignment.RIGHT);
        trophyIcon.setWidth(72);
        trophyIcon.setHeight(72);
        return trophyIcon;
    }

    private Cell getTipOfTheDayCell() {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setWidth(UnitValue.createPercentValue(35));

        chartCell.add(ChartUtils.getTitleParagraph("Tip of the day", TextAlignment.CENTER));
        chartCell.add(ChartUtils.getItemParagraph("\n"));
        Paragraph tipOfTheDay = ChartUtils.getItemParagraph(predictionService.getTipOfTheDay());
        tipOfTheDay.setTextAlignment(TextAlignment.CENTER);

        chartCell.add(tipOfTheDay);
        chartCell.add(ChartUtils.getItemParagraph("\n"));

        return chartCell;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public void setPredictionService(PredictionService predictionService) {
        this.predictionService = predictionService;
    }
}