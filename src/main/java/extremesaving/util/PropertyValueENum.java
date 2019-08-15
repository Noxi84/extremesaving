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
    MONTH_LINE_CHART_IMAGE_FILE("chart.monthLine.location"),
    GOAL_LINE_CHART_IMAGE_FILE("chart.goalLine.location"),
    YEARLY_BAR_CHART_IMAGE_FILE("chart.yearlyBar.location"),

    SAVING_RATE_ICON1("savingRate.icon1"),
    SAVING_RATE_ICON2("savingRate.icon2"),
    SAVING_RATE_ICON3("savingRate.icon3"),
    SAVING_RATE_ICON4("savingRate.icon4"),
    SAVING_RATE_ICON5("savingRate.icon5"),
    SAVING_RATE_ICON6("savingRate.icon6"),
    SAVING_RATE_ICON7("savingRate.icon7"),
    SAVING_RATE_ICON8("savingRate.icon8"),
    SAVING_RATE_ICON9("savingRate.icon9"),

    TROPHY_ICON1("trophy.icon1"),
    TROPHY_ICON2("trophy.icon2"),
    TROPHY_ICON3("trophy.icon3"),
    TROPHY_ICON4("trophy.icon4"),
    TROPHY_ICON5("trophy.icon5"),
    TROPHY_ICON6("trophy.icon6"),
    TROPHY_ICON7("trophy.icon7"),
    TROPHY_ICON8("trophy.icon8"),
    TROPHY_ICON9("trophy.icon9"),
    TROPHY_ICON10("trophy.icon10"),
    TROPHY_ICON11("trophy.icon11"),
    TROPHY_ICON12("trophy.icon12"),
    TROPHY_ICON13("trophy.icon13"),
    TROPHY_ICON14("trophy.icon14"),
    TROPHY_ICON15("trophy.icon15"),
    TROPHY_ICON16("trophy.icon16"),
    TROPHY_ICON17("trophy.icon17"),
    TROPHY_ICON18("trophy.icon18"),

    CHART_GOALS_SURVIVAL("chart.goals.survival"),
    CHART_GOALS_YEAR_SPENDINGS("chart.goals.yearSpendings"),
    CHART_GOALS_SAVINGS("chart.goals.savings"),
    GOAL_LINE_BAR_CHART_INFLATION_PERCENTAGE("chart.goalLine.inflationPercentage");

    private String value;

    PropertyValueENum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}