package extremesaving.pdf.component.categorygrid;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import java.net.MalformedURLException;

import static extremesaving.property.PropertyValueEnum.YEAR_BAR_CHART_IMAGE_FILE;

public class YearBarChartImageComponent {

    public static float CHART_WIDTH = 530;
    public static float CHART_HEIGHT = 240;

    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(YEAR_BAR_CHART_IMAGE_FILE)));
            image.setWidth(CHART_WIDTH);
            image.setHeight(CHART_HEIGHT);
            return image;
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Unable to create YearBarChartImageComponent", e);
        }
    }
}