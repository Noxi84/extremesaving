package extremesaving.dto;

public class CategoryDto {

    private String name;
    private ResultDto totalResults;
    private ResultDto nonHiddenResults;

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

    public ResultDto getNonHiddenResults() {
        return nonHiddenResults;
    }

    public void setNonHiddenResults(ResultDto nonHiddenResults) {
        this.nonHiddenResults = nonHiddenResults;
    }
}