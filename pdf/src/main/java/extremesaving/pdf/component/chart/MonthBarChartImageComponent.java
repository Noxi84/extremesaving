package extremesaving.pdf.component.chart;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

public class MonthBarChartImageComponent {

    public static float MONTHCHART_WIDTH = 780;
    public static float MONTHCHART_HEIGHT = 250;
    public static String MONTHCHART_FILENAME = "month-barchart.png";

    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(MONTHCHART_FILENAME));
            image.setWidth(MONTHCHART_WIDTH);
            image.setHeight(MONTHCHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create MonthBarChartImageComponent.", ex);
        }
    }
}