package extremesaving.pdf.component.chart;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import extremesaving.common.ExtremeSavingConstants;

public class GoalLineChartImageComponent {

    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(ExtremeSavingConstants.GOAL_LINE_CHART_FILENAME));
            image.setWidth(ExtremeSavingConstants.GOAL_LINE_CHART_WIDTH);
            image.setHeight(ExtremeSavingConstants.GOAL_LINE_CHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create GoalLineChartImageComponent.", ex);
        }
    }
}