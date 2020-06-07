package extremesaving.data.dto;

public class CategoryDto {

    private String name;
    private ResultDto totalResults;

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
}