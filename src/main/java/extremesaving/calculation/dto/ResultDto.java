package extremesaving.calculation.dto;

import extremesaving.data.dto.DataDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ResultDto {

    private Set<DataDto> data = new HashSet<>();
    private BigDecimal incomes = BigDecimal.ZERO;
    private BigDecimal expenses = BigDecimal.ZERO;
    private BigDecimal result = BigDecimal.ZERO;
    private long numberOfIncomes = 0;
    private long numberOfExpenses = 0;
    private long numberOfItems = 0;
    private BigDecimal highestIncome = BigDecimal.ZERO;
    private BigDecimal highestExpense = BigDecimal.ZERO;
    private BigDecimal highestResult = BigDecimal.ZERO;
    private Date firstDate;
    private Date lastDate;

    public Set<DataDto> getData() {
        return data;
    }

    public void setData(Set<DataDto> data) {
        this.data = data;
    }

    public BigDecimal getIncomes() {
        return incomes;
    }

    public void setIncomes(BigDecimal incomes) {
        this.incomes = incomes;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public long getNumberOfIncomes() {
        return numberOfIncomes;
    }

    public void setNumberOfIncomes(long numberOfIncomes) {
        this.numberOfIncomes = numberOfIncomes;
    }

    public long getNumberOfExpenses() {
        return numberOfExpenses;
    }

    public void setNumberOfExpenses(long numberOfExpenses) {
        this.numberOfExpenses = numberOfExpenses;
    }

    public long getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(long numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public BigDecimal getHighestIncome() {
        return highestIncome;
    }

    public void setHighestIncome(BigDecimal highestIncome) {
        this.highestIncome = highestIncome;
    }

    public BigDecimal getHighestExpense() {
        return highestExpense;
    }

    public void setHighestExpense(BigDecimal highestExpense) {
        this.highestExpense = highestExpense;
    }

    public BigDecimal getHighestResult() {
        return highestResult;
    }

    public void setHighestResult(BigDecimal highestResult) {
        this.highestResult = highestResult;
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
}