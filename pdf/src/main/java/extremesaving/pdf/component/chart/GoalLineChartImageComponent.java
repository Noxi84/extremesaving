package extremesaving.pdf.component.chart;

import static extremesaving.common.property.PropertyValueEnum.GOAL_LINE_CHART_IMAGE_FILE;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import extremesaving.common.property.PropertiesValueHolder;

public class GoalLineChartImageComponent {

    public static float GOAL_LINE_CHART_WIDTH = 780;
    public static float GOAL_LINE_CHART_HEIGHT = 250;

    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(GOAL_LINE_CHART_IMAGE_FILE)));
            image.setWidth(GOAL_LINE_CHART_WIDTH);
            image.setHeight(GOAL_LINE_CHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create GoalLineChartImageComponent.", ex);
        }
    }
}