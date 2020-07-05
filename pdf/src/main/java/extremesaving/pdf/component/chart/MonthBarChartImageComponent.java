package extremesaving.pdf.component.chart;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import extremesaving.common.ExtremeSavingConstants;

public class MonthBarChartImageComponent {

    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(ExtremeSavingConstants.MONTHCHART_FILENAME));
            image.setWidth(ExtremeSavingConstants.MONTHCHART_WIDTH);
            image.setHeight(ExtremeSavingConstants.MONTHCHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create MonthBarChartImageComponent.", ex);
        }
    }
}