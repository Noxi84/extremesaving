package extremesaving.pdf.component.tipoftheday;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import static extremesaving.property.PropertyValueEnum.YEAR_LINE_CHART_IMAGE_FILE;

public class YearLineChartImageComponent {

    public static float YEAR_LINE_CHART_WIDTH = 530;
    public static float YEAR_LINE_CHART_HEIGHT = 240;

    private Image image;

    public YearLineChartImageComponent build() {
        try {
            image = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(YEAR_LINE_CHART_IMAGE_FILE)));
            image.setWidth(YEAR_LINE_CHART_WIDTH);
            image.setHeight(YEAR_LINE_CHART_HEIGHT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public Image getImage() {
        return image;
    }
}