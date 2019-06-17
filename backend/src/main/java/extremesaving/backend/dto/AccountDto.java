package extremesaving.backend.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountDto {

    private String accountId;
    private String accountName;
    private ResultDto totalResults;
    private ResultDto nonTransferResults;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDto that = (AccountDto) o;
        return accountId.equals(that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    public BigDecimal getTotalIncomes() {
        return nonTransferResults.getIncomes();
    }

    public BigDecimal getTotalExpenses() {
        return nonTransferResults.getExpenses();
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "accountId='" + accountId + '\'' +
                ", accountName='" + accountName + '\'' +
                ", totalResults=" + totalResults +
                ", nonTransferResults=" + nonTransferResults +
                '}';
    }
}
