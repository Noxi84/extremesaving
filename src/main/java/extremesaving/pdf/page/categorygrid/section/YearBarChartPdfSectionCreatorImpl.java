package extremesaving.pdf.page.categorygrid.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import extremesaving.property.PropertiesValueHolder;

import java.net.MalformedURLException;

import static extremesaving.property.PropertyValueEnum.YEARLY_BAR_CHART_IMAGE_FILE;

public class YearBarChartPdfSectionCreatorImpl implements YearBarChartPdfSectionCreator {

    public static float CHART_WIDTH = 530;
    public static float CHART_HEIGHT = 250;

    @Override
    public Image getYearChart() {
        try {
            Image yearlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(YEARLY_BAR_CHART_IMAGE_FILE)));
            yearlyBarChartImage.setWidth(CHART_WIDTH);
            yearlyBarChartImage.setHeight(CHART_HEIGHT);
            return yearlyBarChartImage;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}