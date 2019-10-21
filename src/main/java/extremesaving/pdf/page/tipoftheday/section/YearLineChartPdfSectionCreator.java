package extremesaving.pdf.page.tipoftheday.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import static extremesaving.property.PropertyValueEnum.YEAR_LINE_CHART_IMAGE_FILE;

public class YearLineChartPdfSectionCreator {

    public static float YEAR_LINE_CHART_WIDTH = 530;
    public static float YEAR_LINE_CHART_HEIGHT = 170;

    private Image chartImage;

    public YearLineChartPdfSectionCreator build() {
        try {
            chartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(YEAR_LINE_CHART_IMAGE_FILE)));
            chartImage.setWidth(YEAR_LINE_CHART_WIDTH);
            chartImage.setHeight(YEAR_LINE_CHART_HEIGHT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public Image getChartImage() {
        return chartImage;
    }
}