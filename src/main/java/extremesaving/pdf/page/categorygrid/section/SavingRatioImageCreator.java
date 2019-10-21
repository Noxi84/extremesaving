package extremesaving.pdf.page.categorygrid.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.property.PropertyValueEnum;

import java.math.BigDecimal;
import java.net.MalformedURLException;

import static extremesaving.property.PropertyValueEnum.SAVING_RATE_ICON1;

public class SavingRatioImageCreator {

    private BigDecimal savingRatio;
    private Image image;

    public SavingRatioImageCreator withSavingRatio(BigDecimal savingRatio) {
        this.savingRatio = savingRatio;
        return this;
    }

    public SavingRatioImageCreator build() {
        PropertyValueEnum savingRatioImage = SAVING_RATE_ICON1;
        if (savingRatio.compareTo(BigDecimal.valueOf(90)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON9;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(80)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON9;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(70)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON8;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(60)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON7;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(50)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON6;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(40)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON5;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(30)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON4;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(20)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON3;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(10)) >= 0) {
            savingRatioImage = PropertyValueEnum.SAVING_RATE_ICON2;
        }
        try {
            image = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(savingRatioImage)));
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            image.setTextAlignment(TextAlignment.CENTER);
            image.setWidth(45);
            image.setHeight(45);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Image getImage() {
        return image;
    }
}