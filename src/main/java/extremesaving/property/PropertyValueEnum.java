package extremesaving.property;

public enum PropertyValueEnum {

    TIPOFTHEDAY_CSV_LOCATION("tipOfTheDay.csv.location"),

    DATA_CSV_FOLDER("data.csv.dataFolder"),
    DATA_CSV_DATE_FORMAT1("data.csv.dateFormat1"),
    DATA_CSV_DATE_FORMAT2("data.csv.dateFormat2"),

    DATA_CSV_HEADER_DATE("data.csv.header.date"),
    DATA_CSV_HEADER_VALUE("data.csv.header.value"),
    DATA_CSV_HEADER_CATEGORY("data.csv.header.category"),
    DATA_CSV_HEADER_DESCRIPTION("data.csv.header.description"),

    CSV_SPLIT_BY("data.csv.splitBy"),
    PDF_FILE_NAME("pdf.location"),

    MONTH_BAR_CHART_IMAGE_FILE("chart.monthBar.location"),
    GOAL_LINE_CHART_IMAGE_FILE("chart.goalLine.location"),
    YEAR_BAR_CHART_IMAGE_FILE("chart.yearBar.location"),

    TROPHY_ICON0("trophy.icon0"),
    TROPHY_ICON1("trophy.icon1"),
    TROPHY_ICON2("trophy.icon2"),
    TROPHY_ICON3("trophy.icon3"),
    TROPHY_ICON4("trophy.icon4"),
    TROPHY_ICON5("trophy.icon5"),
    TROPHY_ICON6("trophy.icon6"),
    TROPHY_ICON7("trophy.icon7"),
    TROPHY_ICON8("trophy.icon8"),
    TROPHY_ICON9("trophy.icon9"),

    CHART_GOALS_SAVINGS("chart.goals.savings"),
    CHART_GOALS_ESTIMATION_OUTLINER_ENABLED("chart.goalLine.estimation.removeOutliners.enabled"),
    CHART_GOALS_ESTIMATION_DATE_ENABLED("chart.goalLine.estimation.filterDates.enabled");

    private String value;

    PropertyValueEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}