package extremesaving.model;

public enum DataHideEnum {

    HIDE_ITEMSGRID_CATEGORIES("itemsGrid.categories.hide"),
    HIDE_TIPOFTHEDAY_REDUCEINCREASE_CATEGORIES("tipOfTheDay.reduceincrease.categories.hide");

    private String key;

    DataHideEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}