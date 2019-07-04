package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import extremesaving.constant.ExtremeSavingConstants;

import java.net.MalformedURLException;

public class PdfPageMonthYearGenerator implements PdfPageGenerator {

    @Override
    public void generate(Document document) {
        try {
            Table table = new Table(2);

            Cell chartCell1 = new Cell();
            chartCell1.setBorder(Border.NO_BORDER);
            Image monthlyBarChartImage = new Image(ImageDataFactory.create(ExtremeSavingConstants.MONTHLY_BAR_CHART_IMAGE_FILE));
            monthlyBarChartImage.setWidth(350);
            monthlyBarChartImage.setHeight(216);
            chartCell1.add(monthlyBarChartImage);

            Cell chartCell2 = new Cell();
            chartCell2.setBorder(Border.NO_BORDER);
            Image yearlyBarChartImage = new Image(ImageDataFactory.create(ExtremeSavingConstants.YEARLY_BAR_CHART_IMAGE_FILE));
            yearlyBarChartImage.setWidth(350);
            yearlyBarChartImage.setHeight(216);
            chartCell2.add(yearlyBarChartImage);

            table.addCell(chartCell1);
            table.addCell(chartCell2);

            document.add(table);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}