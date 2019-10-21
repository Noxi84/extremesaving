package extremesaving.pdf.component.tipoftheday;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import static extremesaving.property.PropertyValueEnum.GOAL_LINE_CHART_IMAGE_FILE;

public class GoalLineChartImageComponent {

    public static float GOAL_LINE_CHART_WIDTH = 530;
    public static float GOAL_LINE_CHART_HEIGHT = 240;

    private Image image;

    public GoalLineChartImageComponent build() {
        try {
            image = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(GOAL_LINE_CHART_IMAGE_FILE)));
            image.setWidth(GOAL_LINE_CHART_WIDTH);
            image.setHeight(GOAL_LINE_CHART_HEIGHT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public Image getImage() {
        return image;
    }
}