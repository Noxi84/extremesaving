package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.util.PropertiesValueHolder;

import java.net.MalformedURLException;

import static extremesaving.util.PropertyValueENum.MONTHLY_BAR_CHART_IMAGE_FILE;
import static extremesaving.util.PropertyValueENum.YEARLY_BAR_CHART_IMAGE_FILE;

public class PdfPageMonthYearService implements PdfPageService {

    @Override
    public void generate(Document document) {
        try {
            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));

            Cell chartCell1 = new Cell();
            chartCell1.setBorder(Border.NO_BORDER);
            Image monthlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(MONTHLY_BAR_CHART_IMAGE_FILE)));
            monthlyBarChartImage.setWidth(380);
            monthlyBarChartImage.setHeight(300);
            chartCell1.add(monthlyBarChartImage);

            Cell chartCell2 = new Cell();
            chartCell2.setBorder(Border.NO_BORDER);
            Image yearlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(YEARLY_BAR_CHART_IMAGE_FILE)));
            yearlyBarChartImage.setWidth(380);
            yearlyBarChartImage.setHeight(300);
            chartCell2.add(yearlyBarChartImage);

            table.addCell(chartCell1);
            table.addCell(chartCell2);

            document.add(table);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}