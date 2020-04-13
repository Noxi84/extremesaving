package extremesaving.pdf.component.chart;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import static extremesaving.property.PropertyValueEnum.MONTH_BAR_CHART_IMAGE_FILE;

public class MonthBarChartImageComponent {

    public static float MONTHCHART_WIDTH = 750;
    public static float MONTHCHART_HEIGHT = 250;

    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(MONTH_BAR_CHART_IMAGE_FILE)));
            image.setWidth(MONTHCHART_WIDTH);
            image.setHeight(MONTHCHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create MonthBarChartImageComponent.", ex);
        }
    }
}