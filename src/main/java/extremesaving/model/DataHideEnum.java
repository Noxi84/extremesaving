package extremesaving.model;

public enum DataHideEnum {

    HIDE_SUMMARY_WORSTBEST_MONTHYEAR("summary.worstbestMonthYear.categories.hide"),
    HIDE_SUMMARY_WORST_BEST_YEAR("summary.worstbestMonthYear.years.hide"),
    HIDE_ACCOUNTS("accounts.hide"),
    HIDE_MONTHCHART_CATEGORIES("monthChart.categories.hide"),
    HIDE_YEARCHART_CATEGORIES("yearChart.categories.hide"),
    HIDE_CATEGORYGRID_CATEGORIES("categoryGrid.categories.hide"),
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