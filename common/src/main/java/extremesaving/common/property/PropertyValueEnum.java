package extremesaving.common.property;

public enum PropertyValueEnum {

    DATA_CSV_FOLDER("data.csv.dataFolder");

    private String value;

    PropertyValueEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}