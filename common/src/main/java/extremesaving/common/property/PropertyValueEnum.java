package extremesaving.common.property;

public enum PropertyValueEnum {

    DATA_CSV_FOLDER("data.csv.dataFolder"),
    PDF_FILE_NAME("pdf.location"),
    MONTH_BAR_CHART_IMAGE_FILE("chart.monthBar.location"),
    GOAL_LINE_CHART_IMAGE_FILE("chart.goalLine.location"),

    DATA_CSV_HEADER_DATE("data.csv.header.date"),
    DATA_CSV_HEADER_VALUE("data.csv.header.value"),
    DATA_CSV_HEADER_CATEGORY("data.csv.header.category"),
    DATA_CSV_HEADER_DESCRIPTION("data.csv.header.description");

    private String value;

    PropertyValueEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}