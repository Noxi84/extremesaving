package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import extremesaving.constant.ExtremeSavingConstants;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfPagePredictionsGenerator implements PdfPageGenerator {

    @Override
    public void generate(Document document) {
        try {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            document.add(getSavingRateTable());
            document.add(getItemParagraph(""));
            document.add(getPredictionsTable());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Table getSavingRateTable() throws MalformedURLException {
        Table table = new Table(2);

        Cell cell1 = new Cell();
        cell1.setBorder(Border.NO_BORDER);


        Image monthlyMeterChart = new Image(ImageDataFactory.create(ExtremeSavingConstants.MONTHLY_METER_CHART_IMAGE_FILE));
        monthlyMeterChart.setWidth(150);
        monthlyMeterChart.setHeight(100);
        cell1.add(monthlyMeterChart);

        Cell cell2 = new Cell();
        cell2.setBorder(Border.NO_BORDER);
        cell2.add(getItemParagraph("Your saving rate for " + new SimpleDateFormat("MMMM yyyy").format(new Date()) + " is: € 1785.30 EURO"));
        cell2.add(getItemParagraph(""));
        cell2.add(getItemParagraph("Your best month is xxxx with a result of xxxx EURO and your worst month is with a result of xxxx EURO."));
        cell2.add(getItemParagraph("Your best year is xxxx with a result of xxxx EURO and your worst year is with a result of xxxx EURO."));
        cell2.add(getItemParagraph(""));
        cell2.add(getItemParagraph("If you reduce the highest expense category xxxx with 20% you will save xxx EUR after 5 years."));
        cell2.add(getItemParagraph("If you increase the highest income category xxxx with 20% you will save xxx EUR after 5 years."));

        table.addCell(cell1);
        table.addCell(cell2);

        return table;
    }

    private Table getPredictionsTable() throws MalformedURLException {
        Table table = new Table(2);

        Cell cell1 = new Cell();
        cell1.setBorder(Border.NO_BORDER);

        Image monthlyMeterChart = new Image(ImageDataFactory.create(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE));
        monthlyMeterChart.setWidth(350);
        monthlyMeterChart.setHeight(216);
        cell1.add(monthlyMeterChart);

        Cell cell2 = new Cell();
        cell2.setBorder(Border.NO_BORDER);
        cell2.add(getItemParagraph("With a current total budget of xxxx EURO, an average income of xxxxx EURO per day and, an average expense of xxxx EURO per day and inflation of 3% :"));
        cell2.add(getItemParagraph("    You can live financially free, without any income for x years, x months and days .On 1 january 2024 you will have xxxx EURO."));

        table.addCell(cell1);
        table.addCell(cell2);

        return table;
    }

    private Paragraph getItemParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(9);
        return paragraph;
    }
}