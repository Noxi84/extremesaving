package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.constant.ExtremeSavingConstants;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfPagePredictionsGenerator implements PdfPageGenerator {

    @Override
    public void generate(Document document) {
        try {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            Paragraph title = new Paragraph("Prediction report");
            title.setBold();
            document.add(title);

//            document.add(getSavingRateTable());

            document.add(getItemParagraph("If you reduce category [random expense category] expenses with [random between 1%,2%,3%,4%,5%,10%,15%20%,25%] you should save about € 5 000.00 in [random between 5,10,15,20] years."));
            document.add(getItemParagraph("If you increase category [random income category] incomes  with [random between 1%,2%,3%,4%,5%,10%,15%20%,25%] you should save € 5 000.00 EUR in 5,10,15,20] years."));
            document.add(getItemParagraph("With a current total budget of xxxx EURO, an average income of xxxxx EURO per day and, an average expense of xxxx EURO per day and an inflation of 3% :"));
            document.add(getItemParagraph("You could live financially free, without any income for x years, x months and days. On 1 january 2024 you should have about xxxx EURO."));

            document.add(getItemParagraph("\n"));

            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));

            Cell chartCell1 = new Cell();
            chartCell1.setBorder(Border.NO_BORDER);
            Image monthlyBarChartImage = new Image(ImageDataFactory.create(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE));
            monthlyBarChartImage.setWidth(380);
            monthlyBarChartImage.setHeight(285);
            chartCell1.add(monthlyBarChartImage);

            Cell chartCell2 = new Cell();
            chartCell2.setBorder(Border.NO_BORDER);
            Image yearlyBarChartImage = new Image(ImageDataFactory.create(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE));
            yearlyBarChartImage.setWidth(380);
            yearlyBarChartImage.setHeight(285);
            chartCell2.add(yearlyBarChartImage);

            table.addCell(chartCell1);
            table.addCell(chartCell2);

            document.add(table);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Table getSavingRateTable() throws MalformedURLException {
        Table table = new Table(2);
        table.setWidth(UnitValue.createPercentValue(100));

        Cell cell1 = new Cell();
        cell1.setBorder(Border.NO_BORDER);

        Image monthlyMeterChart = new Image(ImageDataFactory.create(ExtremeSavingConstants.MONTHLY_METER_CHART_IMAGE_FILE));
        monthlyMeterChart.setWidth(180);
        monthlyMeterChart.setHeight(130);
        cell1.add(monthlyMeterChart);

        Cell cell2 = new Cell();
        cell2.setBorder(Border.NO_BORDER);
        cell2.add(getItemParagraph("Your saving rate for " + new SimpleDateFormat("MMMM yyyy").format(new Date()) + " is: € 1785.30 EURO"));
        cell2.add(getItemParagraph("If you reduce category [random expense category] expenses with [random between 1%,2%,3%,4%,5%,10%,15%20%,25%] you should save about € 5 000.00 after [random between 5,10,15,20] years."));
        cell2.add(getItemParagraph("If you increase category [random income category] incomes  with [random between 1%,2%,3%,4%,5%,10%,15%20%,25%] you should save € 5 000.00 EUR after 5,10,15,20] years."));
        cell2.add(getItemParagraph("With a current total budget of xxxx EURO, an average income of xxxxx EURO per day and, an average expense of xxxx EURO per day and an inflation of 3% :"));
        cell2.add(getItemParagraph("You can live financially free, without any income for x years, x months and days. On 1 january 2024 you should have about xxxx EURO."));

        table.addCell(cell1);
        table.addCell(cell2);

        return table;
    }

    private Paragraph getItemParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(10);
        return paragraph;
    }
}