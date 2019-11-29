package extremesaving.data.dto;

import extremesaving.data.model.DataModel;

import java.math.BigDecimal;
import java.util.Date;

public class DataDto {

    private Date date;
    private BigDecimal value;
    private String category;
    private String description;

    public DataDto(DataModel dataModel) {
        this.date = dataModel.getDate();
        this.value = dataModel.getValue();
        this.category = dataModel.getCategory();
        this.description = dataModel.getDescription();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}