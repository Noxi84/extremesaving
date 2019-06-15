package model;

import java.math.BigDecimal;
import java.util.Date;

public class DataModel {

    private Date date;
    private BigDecimal value;
    private AccountModel account;
    private CategoryModel category;
    private String description;

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

    public AccountModel getAccount() {
        return account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "date=" + date +
                ", value=" + value +
                ", account=" + account +
                ", category=" + category +
                ", description='" + description + '\'' +
                '}';
    }
}
