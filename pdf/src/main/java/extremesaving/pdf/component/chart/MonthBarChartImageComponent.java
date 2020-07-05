package extremesaving.pdf.component.chart;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import extremesaving.common.ExtremeSavingConstants;

/**
 * Component builder containing the month-bar chart image.
 * Using .build() will return the component which can be added to the PDF-page..
 */
public class MonthBarChartImageComponent {

    /**
     * Build the image to be added to the PDF-page.
     *
     * @return Image
     */
    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(ExtremeSavingConstants.MONTHCHART_FILENAME));
            image.setWidth(ExtremeSavingConstants.MONTHCHART_WIDTH);
            image.setHeight(ExtremeSavingConstants.MONTHCHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to build MonthBarChartImageComponent.", ex);
        }
    }
}