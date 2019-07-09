package extremesaving.util;

public enum PropertyValueENum {

    TIPOFTHEDAY_CSV_LOCATION("tipOfTheDay.csv.location"),

    DATA_CSV("data.csv.location"),
    DATA_CSV_DATE_FORMAT1("data.csv.dateFormat1"),
    DATA_CSV_DATE_FORMAT2("data.csv.dateFormat2"),
    DATA_CSV_COLUMN_DATE("data.csv.column.date"),
    DATA_CSV_COLUMN_VALUE("data.csv.column.value"),
    DATA_CSV_COLUMN_ACCOUNT("data.csv.column.account"),
    DATA_CSV_COLUMN_CATEGORY("data.csv.column.category"),
    DATA_CSV_COLUMN_DESCRIPTION("data.csv.column.description"),

    CSV_SPLIT_BY("data.csv.splitBy"),
    PDF_FILE_NAME("pdf.location"),

    ACCOUNT_PIE_CHART_IMAGE_FILE("chart.accountPie.location"),
    MONTHLY_BAR_CHART_IMAGE_FILE("chart.monthlyBar.location"),
    HISTORY_LINE_CHART_IMAGE_FILE("chart.historyLine.location"),
    FUTURE_LINE_CHART_IMAGE_FILE("chart.futureLine.location"),
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