package extremesaving.data.model;

import java.math.BigDecimal;
import java.util.Date;

public class DataModel {

    private Date date;
    private BigDecimal value;
    private String category;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}