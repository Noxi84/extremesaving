package extremesaving.data;

public class SampleCategory {

    private String name;
    private boolean isExpense;
    private boolean isIncome;
    private int minAmount;
    private int maxAmount;
    private int occurrencesPerYear;

    public SampleCategory(final String name, final boolean isExpense, final boolean isIncome, final int minAmount, final int maxAmount, final int occurrencesPerYear) {
        this.name = name;
        this.isExpense = isExpense;
        this.isIncome = isIncome;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.occurrencesPerYear = occurrencesPerYear;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(final boolean expense) {
        isExpense = expense;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(final boolean income) {
        isIncome = income;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(final int minAmount) {
        this.minAmount = minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(final int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getOccurrencesPerYear() {
        return occurrencesPerYear;
    }

    public void setOccurrencesPerYear(final int occurrencesPerYear) {
        this.occurrencesPerYear = occurrencesPerYear;
    }
}
