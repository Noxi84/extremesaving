package extremesaving.pdf.component.chart;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import extremesaving.common.ExtremeSavingConstants;

/**
 * Component builder containing the overall line chart image.
 * Using .build() will return the component which can be added to the PDF-page..
 */
public class OverallLineChartImageComponent {

    /**
     * Build the image to be added to the PDF-page.
     *
     * @return Image
     */
    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(ExtremeSavingConstants.GOAL_LINE_CHART_FILENAME));
            image.setWidth(ExtremeSavingConstants.GOAL_LINE_CHART_WIDTH);
            image.setHeight(ExtremeSavingConstants.GOAL_LINE_CHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to build OverallLineChartImageComponent.", ex);
        }
    }
}