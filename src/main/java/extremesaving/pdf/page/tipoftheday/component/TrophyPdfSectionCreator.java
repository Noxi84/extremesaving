package extremesaving.pdf.page.tipoftheday.component;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.property.PropertyValueEnum;

import java.net.MalformedURLException;

public class TrophyPdfSectionCreator {

    private static float IMAGE_WIDTH = 72;
    private static float IMAGE_HEIGHT = 72;

    private Image trophyImage;
    private int goalIndex;

    public TrophyPdfSectionCreator withGoalIndex(int goalIndex) {
        this.goalIndex = goalIndex;
        return this;
    }

    public TrophyPdfSectionCreator build() {
        try {
            trophyImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(getTrophyLocation())));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        trophyImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
        trophyImage.setTextAlignment(TextAlignment.CENTER);
        trophyImage.setWidth(IMAGE_WIDTH);
        trophyImage.setHeight(IMAGE_HEIGHT);
        return this;
    }

    protected PropertyValueEnum getTrophyLocation() {
        if (goalIndex == 0) {
            return PropertyValueEnum.TROPHY_ICON1;
        } else if (goalIndex == 1) {
            return PropertyValueEnum.TROPHY_ICON2;
        } else if (goalIndex == 2) {
            return PropertyValueEnum.TROPHY_ICON3;
        } else if (goalIndex == 3) {
            return PropertyValueEnum.TROPHY_ICON4;
        } else if (goalIndex == 4) {
            return PropertyValueEnum.TROPHY_ICON5;
        } else if (goalIndex == 5) {
            return PropertyValueEnum.TROPHY_ICON6;
        } else if (goalIndex == 6) {
            return PropertyValueEnum.TROPHY_ICON7;
        } else if (goalIndex == 7) {
            return PropertyValueEnum.TROPHY_ICON8;
        } else if (goalIndex == 8) {
            return PropertyValueEnum.TROPHY_ICON9;
        } else if (goalIndex == 9) {
            return PropertyValueEnum.TROPHY_ICON10;
        } else if (goalIndex == 10) {
            return PropertyValueEnum.TROPHY_ICON11;
        } else if (goalIndex == 11) {
            return PropertyValueEnum.TROPHY_ICON12;
        } else if (goalIndex == 12) {
            return PropertyValueEnum.TROPHY_ICON13;
        } else if (goalIndex == 13) {
            return PropertyValueEnum.TROPHY_ICON14;
        } else if (goalIndex == 14) {
            return PropertyValueEnum.TROPHY_ICON15;
        } else if (goalIndex == 15) {
            return PropertyValueEnum.TROPHY_ICON16;
        } else if (goalIndex == 16) {
            return PropertyValueEnum.TROPHY_ICON17;
        }
        return PropertyValueEnum.TROPHY_ICON18;
    }

    public Image getTrophyImage() {
        return trophyImage;
    }
}