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
import extremesaving.util.PdfUtils;
import extremesaving.util.PropertiesValueHolder;

import java.net.MalformedURLException;

import static extremesaving.util.PropertyValueEnum.ACCOUNT_PIE_CHART_IMAGE_FILE;

public class PdfPageOverallService implements PdfPageService {

    public static float ACCOUNTCHART_WIDTH = 200;
    public static float ACCOUNTCHART_HEIGHT = 225;

    private DataService dataService;

    @Override
    public void generate(Document document) {
        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));
        try {
            table.addCell(getAccountChartCell());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        document.add(table);
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
        chartCell.add(PdfUtils.getItemParagraph("\n"));
        chartCell.add(PdfUtils.getItemParagraph("\n"));
        return chartCell;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}