package extremesaving.pdf.component.categorygrid;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import java.net.MalformedURLException;

import static extremesaving.property.PropertyValueEnum.YEARLY_BAR_CHART_IMAGE_FILE;

public class YearBarChartImageComponent {

    public static float CHART_WIDTH = 530;
    public static float CHART_HEIGHT = 240;

    private Image image;

    public YearBarChartImageComponent build() {
        try {
            image = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(YEARLY_BAR_CHART_IMAGE_FILE)));
            image.setWidth(CHART_WIDTH);
            image.setHeight(CHART_HEIGHT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Image getImage() {
        return image;
    }
}