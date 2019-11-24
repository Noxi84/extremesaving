package extremesaving.pdf.component.chart;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import static extremesaving.property.PropertyValueEnum.YEAR_LINE_CHART_IMAGE_FILE;

public class YearLineChartImageComponent {

    public static float YEAR_LINE_CHART_WIDTH = 530;
    public static float YEAR_LINE_CHART_HEIGHT = 220;

    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(YEAR_LINE_CHART_IMAGE_FILE)));
            image.setWidth(YEAR_LINE_CHART_WIDTH);
            image.setHeight(YEAR_LINE_CHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create YearLineChartImageComponent", ex);
        }
    }
}