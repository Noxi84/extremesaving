package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.service.CalculationService;
import extremesaving.service.DataService;
import extremesaving.service.PredictionService;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class PdfPagePredictionsGenerator implements PdfPageGenerator {

    private DataService dataService;
    private CalculationService calculationService;
    private PredictionService predictionService;

    @Override
    public void generate(Document document) {
        try {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            Paragraph title = new Paragraph("Prediction report");
            title.setBold();
            document.add(title);

            List<DataModel> dataModels = dataService.findAll();
            ResultDto resultDto = calculationService.getResults(dataModels);
            ResultDto nonTransferResultDto = calculationService.getResults(dataModels.stream().filter(dataModel -> !dataModel.isTransfer()).collect(Collectors.toList()));

            int predictionNumberOfDays = 5 * 365;
            Calendar predictionEndDate = Calendar.getInstance();
            predictionEndDate.add(Calendar.DAY_OF_MONTH, predictionNumberOfDays);
            predictionEndDate.set(Calendar.DAY_OF_MONTH, 1);
            predictionEndDate.set(Calendar.MONTH, Calendar.JANUARY);
            BigDecimal predictionAmount = predictionService.getPredictionAmount(predictionEndDate.getTime());

            document.add(getItemParagraph("Tip of the day", true));
            document.add(getItemParagraph("Do you really need a fridge? Having no fridge makes you really conscious of what you need & what you really don’t..& what a great way to live I say. Is the convenience REAL, or just habitual? Remember that this was one of the selling points drummed into us to get us to buy fridges. Keep in mind that lettuces, broccoli, cauliflower, and herbs (basically anything with a stem) will store incredibly well if the end of the stem is placed in water (just the tip of the stem should be submerged). You can also create a cool space in the basement to store your food."));

            StringBuilder text = new StringBuilder();
            text.append("If you reduce category ")
                    .append("[random expense category]")
                    .append(" expenses with ")
                    .append("[random between 1%,2%,3%,4%,5%,10%,15%20%,25%]")
                    .append(" you should save about ")
                    .append("€ 5 000.00")
                    .append(" in ")
                    .append("[random between 5,10,15,20] ")
                    .append("years.")
                    .append("\n");
            text.append("If you increase category ")
                    .append("[random income category]")
                    .append(" incomes  with ")
                    .append("[random between 1%,2%,3%,4%,5%,10%,15%20%,25%] ")
                    .append("you should save ")
                    .append("€ 5 000.00 EUR")
                    .append(" in 5,10,15,20] years.");

            document.add(getItemParagraph(text.toString()));

            Paragraph itemParagraph = getItemParagraph(new StringBuilder().append("With a current total budget of ")
                    .append(NumberUtils.formatNumber(resultDto.getResult()))
                    .append(", an average income of ")
                    .append(NumberUtils.formatNumber(nonTransferResultDto.getAverageDailyIncome()))
                    .append(" per day and, an average expense of ")
                    .append(NumberUtils.formatNumber(nonTransferResultDto.getAverageDailyExpense()))
                    .append(" per day and an inflation of 3% :").toString());
            itemParagraph.setTextAlignment(TextAlignment.CENTER);
            itemParagraph.setBold();
            document.add(itemParagraph);

            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));

            Cell chartCell1 = new Cell();
            chartCell1.setBorder(Border.NO_BORDER);
            chartCell1.setTextAlignment(TextAlignment.CENTER);
            Image monthlyBarChartImage = new Image(ImageDataFactory.create(ExtremeSavingConstants.HISTORY_LINE_CHART_IMAGE_FILE));
            monthlyBarChartImage.setWidth(380);
            monthlyBarChartImage.setHeight(300);
            chartCell1.add(getItemParagraph("You could live financially free, without any income for..."));
            chartCell1.add(getItemParagraph(DateUtils.formatSurvivalDays(predictionService.getSurvivalDays()), true));
            chartCell1.add(getItemParagraph("\n"));
            chartCell1.add(monthlyBarChartImage);

            Cell chartCell2 = new Cell();
            chartCell2.setBorder(Border.NO_BORDER);
            chartCell2.setTextAlignment(TextAlignment.CENTER);
            Image yearlyBarChartImage = new Image(ImageDataFactory.create(ExtremeSavingConstants.FUTURE_LINE_CHART_IMAGE_FILE));
            yearlyBarChartImage.setWidth(380);
            yearlyBarChartImage.setHeight(300);
            chartCell2.add(getItemParagraph("If you keep up your average daily result, you should have about..."));
            chartCell2.add(getItemParagraph(NumberUtils.formatNumber(predictionAmount) + " on " + DateUtils.formatDate(predictionEndDate.getTime()), true));
            chartCell2.add(getItemParagraph("\n"));
            chartCell2.add(yearlyBarChartImage);

            table.addCell(chartCell1);
            table.addCell(chartCell2);

            document.add(table);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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