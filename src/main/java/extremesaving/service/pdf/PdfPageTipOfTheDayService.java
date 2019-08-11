package extremesaving.service.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.service.CalculationService;
import extremesaving.service.DataService;
import extremesaving.service.PredictionService;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;
import extremesaving.util.PropertiesValueHolder;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static extremesaving.util.PropertyValueENum.GOAL_LINE_CHART_IMAGE_FILE;

public class PdfPageTipOfTheDayService implements PdfPageService {

    public static float CHART_WIDTH = 760;
    public static float CHART_HEIGHT = 390;

    private DataService dataService;
    private CalculationService calculationService;
    private PredictionService predictionService;

    @Override
    public void generate(Document document) {
        try {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            List<DataModel> dataModels = dataService.findAll();
            ResultDto resultDto = calculationService.getResults(dataModels);

            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));
            table.addCell(getChartCell2());
            table.addCell(getChartCell1(resultDto));

            document.add(table);

            Image futureLineChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(GOAL_LINE_CHART_IMAGE_FILE)));
            futureLineChartImage.setWidth(CHART_WIDTH);
            futureLineChartImage.setHeight(CHART_HEIGHT);
            document.add(futureLineChartImage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Cell getChartCell1(ResultDto resultDto) {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setTextAlignment(TextAlignment.CENTER);
        chartCell.setWidth(UnitValue.createPercentValue(50));

        chartCell.add(getTitleParagraph("Goals & Awards"));
        chartCell.add(getItemParagraph("\n"));

        if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
            if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal previousGoalAmount = predictionService.getPreviousGoal();
                Date goalReachedDate = predictionService.getGoalReachedDate(previousGoalAmount);
                BigDecimal goalAmount = predictionService.getCurrentGoal();

                BigDecimal goalPercentage = resultDto.getResult().divide(goalAmount, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100));
                chartCell.add(getItemParagraph("Save " + NumberUtils.formatNumber(resultDto.getResult(), false) + " / " + NumberUtils.formatNumber(goalAmount, false) + " (" + NumberUtils.formatPercentage(goalPercentage) + ")", true));
                chartCell.add(getItemParagraph("Estimated time: " + DateUtils.formatTimeLeft(predictionService.getGoalTime(goalAmount)), false));
                chartCell.add(getItemParagraph("Previous goal " + NumberUtils.formatNumber(previousGoalAmount, false) + " reached on " + new SimpleDateFormat("d MMMM yyyy").format(goalReachedDate)));
                chartCell.add(getItemParagraph("\n"));
            }

            chartCell.add(getItemParagraph("Survive 3 years without incomes.", true));
            chartCell.add(getItemParagraph("Estimated time: " + DateUtils.formatTimeLeft(predictionService.getSurvivalDays()), false));
            chartCell.add(getItemParagraph("\n"));

            chartCell.add(getItemParagraph("Spend less than € 6000 this year.", true));
            chartCell.add(getItemParagraph("Current expenses: € 5692 "));
            chartCell.add(getItemParagraph("\n"));
        }
        return chartCell;
    }

    private Cell getChartCell2() throws MalformedURLException {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setWidth(UnitValue.createPercentValue(50));

        chartCell.add(getTitleParagraph("Tip of the day"));
        chartCell.add(getItemParagraph("\n"));
        Paragraph tipOfTheDay = getItemParagraph(predictionService.getTipOfTheDay());
        tipOfTheDay.setTextAlignment(TextAlignment.CENTER);

        chartCell.add(tipOfTheDay);
        chartCell.add(getItemParagraph("\n"));

        return chartCell;
    }

    private Paragraph getTitleParagraph(String text) {
        Paragraph titleParagraph = new Paragraph(text);
        titleParagraph.setBold();
        titleParagraph.setTextAlignment(TextAlignment.CENTER);
        try {
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.COURIER);
            titleParagraph.setFont(regular);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titleParagraph;
    }

    private Paragraph getItemParagraph(String text) {
        return getItemParagraph(text, false);
    }

    private Paragraph getItemParagraph(String text, boolean bold) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(8);
        if (bold) {
            paragraph.setBold();
        }
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

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public void setPredictionService(PredictionService predictionService) {
        this.predictionService = predictionService;
    }
}