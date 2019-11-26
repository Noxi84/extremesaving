package extremesaving.pdf.component.summary;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.property.PropertyValueEnum;

import java.net.MalformedURLException;

public class TrophyImageComponent {

    private static float IMAGE_WIDTH = 72;
    private static float IMAGE_HEIGHT = 72;

    private int goalIndex;

    public TrophyImageComponent withGoalIndex(int goalIndex) {
        this.goalIndex = goalIndex;
        return this;
    }

    public Image build() {
        try {
            Image image = new Image(ImageDataFactory.create(PropertiesValueHolder.getString(getTrophyLocation())));
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            image.setTextAlignment(TextAlignment.CENTER);
            image.setWidth(IMAGE_WIDTH);
            image.setHeight(IMAGE_HEIGHT);
            return image;
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Unable to create TophyImageComponent.", e);
        }
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
}