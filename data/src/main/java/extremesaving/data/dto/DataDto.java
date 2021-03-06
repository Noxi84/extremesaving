package extremesaving.data.dto;

import java.math.BigDecimal;
import java.util.Date;

import extremesaving.data.model.DataModel;

public class DataDto {

    private Date date;
    private BigDecimal value;
    private String category;

    public DataDto(DataModel dataModel) {
        this.date = dataModel.getDate();
        this.value = dataModel.getValue();
        this.category = dataModel.getCategory().trim().replaceAll("\\s{2,}", " ");
    }

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