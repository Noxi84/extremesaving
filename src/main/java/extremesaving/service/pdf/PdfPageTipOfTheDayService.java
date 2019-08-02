package extremesaving.service.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
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
import java.net.MalformedURLException;
import java.util.List;

import static extremesaving.util.PropertyValueENum.GOAL_LINE_CHART_IMAGE_FILE;

public class PdfPageTipOfTheDayService implements PdfPageService {

    private DataService dataService;
    private CalculationService calculationService;
    private PredictionService predictionService;

    @Override
    public void generate(Document document) {
        try {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            List<DataModel> dataModels = dataService.findAll();
            ResultDto resultDto = calculationService.getResults(dataModels);

            document.add(getTitleParagraph());
            Paragraph tipOfTheDay = getItemParagraph(predictionService.getTipOfTheDay());
            tipOfTheDay.setTextAlignment(TextAlignment.CENTER);
            document.add(tipOfTheDay);

            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));
            table.addCell(getChartcell1(resultDto));
            table.addCell(getChartCell2(resultDto));

            document.add(table);

            Image futureLineChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(GOAL_LINE_CHART_IMAGE_FILE)));
            futureLineChartImage.setWidth(760);
            futureLineChartImage.setHeight(380);
            document.add(futureLineChartImage);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Paragraph getTitleParagraph() {
        Paragraph titleParagraph = new Paragraph("Tip of the day");
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

    private Cell getChartcell1(ResultDto resultDto) {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setTextAlignment(TextAlignment.CENTER);

        if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal goalAmount = predictionService.getNextGoal();
            chartCell.add(getItemParagraph("Your previous goal was: € 12000"));
            chartCell.add(getItemParagraph("Reached goal on 1 february 2019", true));
            chartCell.add(getItemParagraph("\n"));
            chartCell.add(getItemParagraph("Your next goal is: " + NumberUtils.formatNumber(resultDto.getResult(), false) + " / " + NumberUtils.formatNumber(goalAmount, false)));
            chartCell.add(getItemParagraph("Estimated time: " + DateUtils.formatTimeLeft(predictionService.getGoalTime(goalAmount)), true));
        }
        return chartCell;
    }

    private Cell getChartCell2(ResultDto resultDto) {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setTextAlignment(TextAlignment.CENTER);
        chartCell.add(getItemParagraph("Without income and 3% inflation you will run out of money in..."));
        chartCell.add(getItemParagraph(DateUtils.formatTimeLeft(predictionService.getSurvivalDays()), true));
        chartCell.add(getItemParagraph("\n"));
        chartCell.add(getItemParagraph("Average daily expense: " + NumberUtils.formatNumber(resultDto.getAverageDailyExpense())));
        chartCell.add(getItemParagraph("Average daily result: " + NumberUtils.formatNumber(resultDto.getAverageDailyResult())));
        return chartCell;
    }

    private Paragraph getItemParagraph(String text) {
        return getItemParagraph(text, false);
    }

    private Paragraph getItemParagraph(String text, boolean bold) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(9);
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