package extremesaving.util;

public enum PropertyValueENum {

    TIPOFTHEDAY_CSV_LOCATION("tipOfTheDay.csv.location"),

    DATA_CSV_FOLDER("data.csv.dataFolder"),
    DATA_CSV_DATE_FORMAT1("data.csv.dateFormat1"),
    DATA_CSV_DATE_FORMAT2("data.csv.dateFormat2"),

    DATA_CSV_HEADER_DATE("data.csv.header.date"),
    DATA_CSV_HEADER_VALUE("data.csv.header.value"),
    DATA_CSV_HEADER_ACCOUNT("data.csv.header.account"),
    DATA_CSV_HEADER_CATEGORY("data.csv.header.category"),
    DATA_CSV_HEADER_DESCRIPTION("data.csv.header.description"),

    CSV_SPLIT_BY("data.csv.splitBy"),
    PDF_FILE_NAME("pdf.location"),

    ACCOUNT_PIE_CHART_IMAGE_FILE("chart.accountPie.location"),
    MONTHLY_BAR_CHART_IMAGE_FILE("chart.monthlyBar.location"),
    HISTORY_LINE_CHART_IMAGE_FILE("chart.historyLine.location"),
    GOAL_LINE_CHART_IMAGE_FILE("chart.goalLine.location"),
    YEARLY_BAR_CHART_IMAGE_FILE("chart.yearlyBar.location");

    private String value;

    PropertyValueENum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}