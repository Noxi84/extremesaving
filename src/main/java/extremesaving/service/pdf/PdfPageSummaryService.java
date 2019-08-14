package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.service.DataService;
import extremesaving.util.ChartUtils;
import extremesaving.util.PropertiesValueHolder;

import java.net.MalformedURLException;

import static extremesaving.util.PropertyValueENum.*;

public class PdfPageSummaryService implements PdfPageService {

    public static float MONTHCHART_WIDTH = 550;
    public static float MONTHCHART_HEIGHT = 240;

    public static float YEARCHART_WIDTH = 550;
    public static float YEARCHART_HEIGHT = 255;

    public static float ACCOUNTCHART_WIDTH = 200;
    public static float ACCOUNTCHART_HEIGHT = 225;


    private DataService dataService;

    @Override
    public void generate(Document document) {
        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getMonthChartCell());
        document.add(table);

        Table table2 = new Table(1);
        table2.setWidth(UnitValue.createPercentValue(100));
        table2.addCell(getYearChartCell());
        document.add(table2);
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

    private Cell getAccountChartCell() throws MalformedURLException {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        chartCell.setTextAlignment(TextAlignment.CENTER);
        chartCell.setWidth(300);

        Image accountPieImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(ACCOUNT_PIE_CHART_IMAGE_FILE)));
        accountPieImage.setWidth(ACCOUNTCHART_WIDTH);
        accountPieImage.setHeight(ACCOUNTCHART_HEIGHT);
        chartCell.add(accountPieImage);
        chartCell.add(ChartUtils.getItemParagraph("\n"));
        chartCell.add(ChartUtils.getItemParagraph("\n"));
        return chartCell;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}