package extremesaving.pdf.page.tipoftheday.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import static extremesaving.property.PropertyValueEnum.MONTHLY_BAR_CHART_IMAGE_FILE;

public class MonthBarChartPdfSectionCreator {

    public static float MONTHCHART_WIDTH = 530;
    public static float MONTHCHART_HEIGHT = 170;

    private Image chartImage;

    public MonthBarChartPdfSectionCreator build() {
        try {
            chartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(MONTHLY_BAR_CHART_IMAGE_FILE)));
            chartImage.setWidth(MONTHCHART_WIDTH);
            chartImage.setHeight(MONTHCHART_HEIGHT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public Image getChartImage() {
        return chartImage;
    }
}