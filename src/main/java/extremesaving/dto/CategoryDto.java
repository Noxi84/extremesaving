package extremesaving.dto;

public class CategoryDto {

    private String name;
    private ResultDto totalResults;
    private ResultDto nonTransferResults;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}