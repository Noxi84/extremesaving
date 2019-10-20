package extremesaving.service.pdf;

import com.itextpdf.layout.Document;
import extremesaving.service.DataService;

public class PdfPageYearService implements PdfPageService {

//    public static float YEARCHART_WIDTH = 530;
//    public static float YEARCHART_HEIGHT = 160;

    private DataService dataService;

    @Override
    public void generate(Document document) {
//        Table table = new Table(1);
//        table.setWidth(UnitValue.createPercentValue(100));
//        table.addCell(getYearChartCell());
//        document.add(table);
    }

//    public Cell getYearChartCell() {
//        try {
//            Cell chartCell2 = new Cell();
//            chartCell2.setBorder(Border.NO_BORDER);
//            Image yearlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(YEARLY_BAR_CHART_IMAGE_FILE)));
//            yearlyBarChartImage.setWidth(YEARCHART_WIDTH);
//            yearlyBarChartImage.setHeight(YEARCHART_HEIGHT);
//            chartCell2.add(yearlyBarChartImage);
//            return chartCell2;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}