package extremesaving.pdf.component.tipoftheday;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import static extremesaving.property.PropertyValueEnum.MONTHLY_BAR_CHART_IMAGE_FILE;

public class MonthBarChartImageComponent {

    public static float MONTHCHART_WIDTH = 530;
    public static float MONTHCHART_HEIGHT = 240;

    private Image image;

    public MonthBarChartImageComponent build() {
        try {
            image = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(MONTHLY_BAR_CHART_IMAGE_FILE)));
            image.setWidth(MONTHCHART_WIDTH);
            image.setHeight(MONTHCHART_HEIGHT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public Image getImage() {
        return image;
    }
}