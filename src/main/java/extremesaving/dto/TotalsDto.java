package extremesaving.dto;

import extremesaving.model.DataModel;

import java.math.BigDecimal;
import java.util.*;

public class TotalsDto {

    private Set<DataModel> data = new HashSet<>();
    private List<AccountDto> accountDtoList = new ArrayList<>();
    private List<CategoryDto> categoryDtoList = new ArrayList<>();
    private Map<Integer, ResultDto> monthlyResults;
    private Map<Integer, ResultDto> yearlyResults;
    private Map<Integer, ResultDto> yearlyNonTransferResults;
    private Map<Integer, BigDecimal> yearPredictions;
    private ResultDto totalResults;
    private ResultDto noTransferResults;
    private long survivalDays;
    private CategoryDto mostExpensiveCategory;
    private CategoryDto mostProfitableCategory;
    private AccountDto mostExpensiveAccount;
    private AccountDto mostProfitableAccount;

    public Set<DataModel> getData() {
        return data;
    }

    public void setData(Set<DataModel> data) {
        this.data = data;
    }

    public Map<Integer, BigDecimal> getYearPredictions() {
        return yearPredictions;
    }

    public void setYearPredictions(Map<Integer, BigDecimal> yearPredictions) {
        this.yearPredictions = yearPredictions;
    }

    public List<AccountDto> getAccountDtoList() {
        return accountDtoList;
    }

    public void setAccountDtoList(List<AccountDto> accountDtoList) {
        this.accountDtoList = accountDtoList;
    }

    public List<CategoryDto> getCategoryDtoList() {
        return categoryDtoList;
    }

    public void setCategoryDtoList(List<CategoryDto> categoryDtoList) {
        this.categoryDtoList = categoryDtoList;
    }

    public Map<Integer, ResultDto> getMonthlyResults() {
        return monthlyResults;
    }

    public void setMonthlyResults(Map<Integer, ResultDto> monthlyResults) {
        this.monthlyResults = monthlyResults;
    }

    public Map<Integer, ResultDto> getYearlyResults() {
        return yearlyResults;
    }

    public void setYearlyResults(Map<Integer, ResultDto> yearlyResults) {
        this.yearlyResults = yearlyResults;
    }

    public Map<Integer, ResultDto> getYearlyNonTransferResults() {
        return yearlyNonTransferResults;
    }

    public void setYearlyNonTransferResults(Map<Integer, ResultDto> yearlyNonTransferResults) {
        this.yearlyNonTransferResults = yearlyNonTransferResults;
    }

    public ResultDto getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(ResultDto totalResults) {
        this.totalResults = totalResults;
    }

    public ResultDto getNoTransferResults() {
        return noTransferResults;
    }

    public void setNoTransferResults(ResultDto noTransferResults) {
        this.noTransferResults = noTransferResults;
    }

    public long getSurvivalDays() {
        return survivalDays;
    }

    public void setSurvivalDays(long survivalDays) {
        this.survivalDays = survivalDays;
    }

    public CategoryDto getMostExpensiveCategory() {
        return mostExpensiveCategory;
    }

    public void setMostExpensiveCategory(CategoryDto mostExpensiveCategory) {
        this.mostExpensiveCategory = mostExpensiveCategory;
    }

    public CategoryDto getMostProfitableCategory() {
        return mostProfitableCategory;
    }

    public void setMostProfitableCategory(CategoryDto mostProfitableCategory) {
        this.mostProfitableCategory = mostProfitableCategory;
    }

    public AccountDto getMostExpensiveAccount() {
        return mostExpensiveAccount;
    }

    public void setMostExpensiveAccount(AccountDto mostExpensiveAccount) {
        this.mostExpensiveAccount = mostExpensiveAccount;
    }

    public AccountDto getMostProfitableAccount() {
        return mostProfitableAccount;
    }

    public void setMostProfitableAccount(AccountDto mostProfitableAccount) {
        this.mostProfitableAccount = mostProfitableAccount;
    }
}
