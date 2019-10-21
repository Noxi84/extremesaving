package extremesaving.pdf.page.categorygrid.component;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import java.net.MalformedURLException;

import static extremesaving.property.PropertyValueEnum.YEARLY_BAR_CHART_IMAGE_FILE;

public class YearBarChartPdfSectionComponent {

    public static float CHART_WIDTH = 530;
    public static float CHART_HEIGHT = 240;

    private Image chartImage;

    public YearBarChartPdfSectionComponent build() {
        try {
            chartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(YEARLY_BAR_CHART_IMAGE_FILE)));
            chartImage.setWidth(CHART_WIDTH);
            chartImage.setHeight(CHART_HEIGHT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Image getChartImage() {
        return chartImage;
    }
}