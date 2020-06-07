package extremesaving.pdf.component.chart;

import static extremesaving.common.property.PropertyValueEnum.MONTH_BAR_CHART_IMAGE_FILE;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import extremesaving.common.property.PropertiesValueHolder;

public class MonthBarChartImageComponent {

    public static float MONTHCHART_WIDTH = 780;
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