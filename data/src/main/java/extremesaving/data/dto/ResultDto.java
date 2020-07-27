package extremesaving.data.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ResultDto {

    private Set<DataDto> data = new HashSet<>();
    private BigDecimal result = BigDecimal.ZERO;
    private BigDecimal totalIncomes = BigDecimal.ZERO;
    private long numberOfItems = 0;
    private Date firstDate;
    private Date lastDate;
    private BigDecimal savingRatio;

    public Set<DataDto> getData() {
        return data;
    }

    public void setData(Set<DataDto> data) {
        this.data = data;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public BigDecimal getTotalIncomes() {
        return totalIncomes;
    }

    public void setTotalIncomes(final BigDecimal totalIncomes) {
        this.totalIncomes = totalIncomes;
    }

    public long getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(long numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public BigDecimal getSavingRatio() {
        return savingRatio;
    }

    public void setSavingRatio(final BigDecimal savingRatio) {
        this.savingRatio = savingRatio;
    }
}