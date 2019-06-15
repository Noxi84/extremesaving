package extremesaving.frontend.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class CategoryDto {

    private String categoryId;
    private String categoryName;
    private ResultDto totalResults;
    private ResultDto nonTransferResults;


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ResultDto getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(ResultDto totalResults) {
        this.totalResults = totalResults;
    }

    public ResultDto getNonTransferResults() {
        return nonTransferResults;
    }

    public void setNonTransferResults(ResultDto nonTransferResults) {
        this.nonTransferResults = nonTransferResults;
    }

    public BigDecimal getTotalIncomes() {
        return nonTransferResults.getIncomes();
    }

    public BigDecimal getTotalExpenses() {
        return nonTransferResults.getExpenses();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDto that = (CategoryDto) o;
        return categoryId.equals(that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }

    @Override
    public String toString() {
        return "CategoryDto{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", totalResults=" + totalResults +
                ", nonTransferResults=" + nonTransferResults +
                '}';
    }
}