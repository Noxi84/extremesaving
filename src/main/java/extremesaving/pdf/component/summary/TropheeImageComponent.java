package extremesaving.pdf.component.summary;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.property.PropertyValueEnum;

import java.math.BigDecimal;
import java.net.MalformedURLException;


public class TropheeImageComponent {

    private BigDecimal savingRatio;

    public TropheeImageComponent withSavingRatio(BigDecimal savingRatio) {
        this.savingRatio = savingRatio;
        return this;
    }

    public Image build() {
        PropertyValueEnum savingRatioImage = getSavingRatioImage();
        try {
            Image image = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(savingRatioImage)));
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            image.setTextAlignment(TextAlignment.CENTER);
            image.setWidth(45);
            image.setHeight(45);
            return image;
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Unable to create TropheeImageComponent.", e);
        }
    }

    protected PropertyValueEnum getSavingRatioImage() {
        if (savingRatio.compareTo(BigDecimal.valueOf(90)) >= 0) {
            return PropertyValueEnum.TROPHY_ICON9;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(80)) >= 0) {
            return PropertyValueEnum.TROPHY_ICON8;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(70)) >= 0) {
            return PropertyValueEnum.TROPHY_ICON7;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(60)) >= 0) {
            return PropertyValueEnum.TROPHY_ICON6;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(50)) >= 0) {
            return PropertyValueEnum.TROPHY_ICON5;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(40)) >= 0) {
            return PropertyValueEnum.TROPHY_ICON4;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(30)) >= 0) {
            return PropertyValueEnum.TROPHY_ICON3;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(20)) >= 0) {
            return PropertyValueEnum.TROPHY_ICON2;
        } else if (savingRatio.compareTo(BigDecimal.valueOf(10)) >= 0) {
            return PropertyValueEnum.TROPHY_ICON1;
        }
        return PropertyValueEnum.TROPHY_ICON0;
    }
}