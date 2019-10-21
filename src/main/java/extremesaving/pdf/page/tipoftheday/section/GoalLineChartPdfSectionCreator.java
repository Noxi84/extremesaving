package extremesaving.pdf.page.tipoftheday.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import static extremesaving.property.PropertyValueEnum.GOAL_LINE_CHART_IMAGE_FILE;

public class GoalLineChartPdfSectionCreator {

    public static float GOAL_LINE_CHART_WIDTH = 530;
    public static float GOAL_LINE_CHART_HEIGHT = 240;

    private Image chartImage;

    public GoalLineChartPdfSectionCreator build() {
        try {
            chartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(GOAL_LINE_CHART_IMAGE_FILE)));
            chartImage.setWidth(GOAL_LINE_CHART_WIDTH);
            chartImage.setHeight(GOAL_LINE_CHART_HEIGHT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public Image getChartImage() {
        return chartImage;
    }
}