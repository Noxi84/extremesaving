package extremesaving.pdf.component.chart;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

public class GoalLineChartImageComponent {

    public static float GOAL_LINE_CHART_WIDTH = 780;
    public static float GOAL_LINE_CHART_HEIGHT = 250;
    public static String GOAL_LINE_CHART_FILENAME = "goal-linechart.png";

    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(GOAL_LINE_CHART_FILENAME));
            image.setWidth(GOAL_LINE_CHART_WIDTH);
            image.setHeight(GOAL_LINE_CHART_HEIGHT);
            return image;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create GoalLineChartImageComponent.", ex);
        }
    }
}