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

import static extremesaving.util.PropertyValueENum.MONTHLY_BAR_CHART_IMAGE_FILE;

public class PdfPageMonthService implements PdfPageService {

    public static float MONTHCHART_WIDTH = 525;
    public static float MONTHCHART_HEIGHT = 400;

    private DataService dataService;

    @Override
    public void generate(Document document) {
        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getMonthChartCell());
        document.add(table);
    }

    public Cell getMonthChartCell() {
        try {
            Cell chartCell1 = new Cell();
            chartCell1.setBorder(Border.NO_BORDER);
            Image monthlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(MONTHLY_BAR_CHART_IMAGE_FILE)));
            monthlyBarChartImage.setWidth(MONTHCHART_WIDTH);
            monthlyBarChartImage.setHeight(MONTHCHART_HEIGHT);
            chartCell1.add(monthlyBarChartImage);
            return chartCell1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}