package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.service.DataService;
import extremesaving.util.PropertiesValueHolder;

import java.net.MalformedURLException;

import static extremesaving.util.PropertyValueENum.YEARLY_BAR_CHART_IMAGE_FILE;

public class PdfPageYearService implements PdfPageService {

    public static float YEARCHART_WIDTH = 525;
    public static float YEARCHART_HEIGHT = 350;

    private DataService dataService;

    @Override
    public void generate(Document document) {
        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getYearChartCell());
        document.add(table);
    }

    public Cell getYearChartCell() {
        try {
            Cell chartCell2 = new Cell();
            chartCell2.setBorder(Border.NO_BORDER);
            Image yearlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(YEARLY_BAR_CHART_IMAGE_FILE)));
            yearlyBarChartImage.setWidth(YEARCHART_WIDTH);
            yearlyBarChartImage.setHeight(YEARCHART_HEIGHT);
            chartCell2.add(yearlyBarChartImage);
            return chartCell2;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}