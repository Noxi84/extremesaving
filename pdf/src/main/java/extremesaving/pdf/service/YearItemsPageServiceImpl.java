package extremesaving.pdf.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import extremesaving.data.dto.CategoryDto;
import extremesaving.data.facade.CategoryFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.chart.GoalLineChartImageComponent;
import extremesaving.pdf.component.itemgrid.YearCategoryTableComponent;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.common.util.DateUtils;

public class YearItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 20;
    private static final int TEXT_MAX_CHARACTERS = 200;
    public static final int NUMBER_OF_YEARS = 12;

    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating Yearly Analysis Report");
        document.add(PdfUtils.getTitleParagraph("Financial Report generated on " + new SimpleDateFormat("dd MMM yyyy").format(new Date()), TextAlignment.RIGHT));
        document.add(buildGoalLineChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(buildCategoryTable());
    }

    protected Image buildGoalLineChartImage() {
        return new GoalLineChartImageComponent().build();
    }

    protected Table buildCategoryTable() {
        List<CategoryDto> overallCategoryResults = categoryFacade.getCategories(dataFacade.findAll()).stream()
                .collect(Collectors.toList());

        Map<String, List<CategoryDto>> yearResults = new HashMap<>();

        Date lastDate = overallCategoryResults.stream().map(categoryDto -> categoryDto.getTotalResults().getLastDate()).max(Date::compareTo).get();
        Calendar lastDateCal = Calendar.getInstance();
        lastDateCal.setTime(lastDate);
        int lastYear = lastDateCal.get(Calendar.YEAR);

        Map<String, List<CategoryDto>> categoryResultsPerYear = getCategoryResultsPerYear(lastYear);
        List<String> sortedCategories = getSortedCategories(categoryResultsPerYear, lastYear);

        for (int yearCounter = lastYear; yearCounter > lastYear - NUMBER_OF_YEARS; yearCounter--) {
            List<CategoryDto> categoryResults = categoryResultsPerYear.get(String.valueOf(yearCounter));
            yearResults.put(String.valueOf(yearCounter), categoryResults);
        }

        yearResults.put("Total", overallCategoryResults);

        return new YearCategoryTableComponent()
                .withCategoryNames(sortedCategories)
                .withResults(yearResults)
                .withNumberOfColumns(10)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .withPrintTotalsColumn(true)
                .build();
    }

    protected Map<String, List<CategoryDto>> getCategoryResultsPerYear(int lastYear) {
        Map<String, List<CategoryDto>> yearResults = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, lastYear);

        for (int yearCounter = cal.get(Calendar.YEAR); yearCounter > cal.get(Calendar.YEAR) - NUMBER_OF_YEARS; yearCounter--) {
            Calendar yearDate = Calendar.getInstance();
            yearDate.set(Calendar.YEAR, yearCounter);
            List<DataDto> dataDtos = dataFacade.findAll().stream()
                    .filter(dataDto -> DateUtils.isEqualYear(yearDate.getTime(), dataDto.getDate()))
                    .collect(Collectors.toList());
            List<CategoryDto> categoryResults = categoryFacade.getCategories(dataDtos);
            if (categoryResults.size() == 1) {
                // categoryResults only contains total and no other categories. So we set the lastDate manually for displaying purpose.
                categoryResults.get(0).getTotalResults().setLastDate(yearDate.getTime());
            }
            yearResults.put(String.valueOf(yearCounter), categoryResults);
        }
        return yearResults;
    }

    protected List<String> getSortedCategories(Map<String, List<CategoryDto>> categoriesPerYear, int lastYear) {
        List<String> categoryNames = new ArrayList<>();
        for (int yearCounter = lastYear; yearCounter > lastYear - NUMBER_OF_YEARS; yearCounter--) {
            List<String> categories = categoriesPerYear.get(String.valueOf(yearCounter))
                    .stream()
                    .sorted((o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()))
                    .map(CategoryDto::getName)
                    .filter(categoryName -> !categoryNames.contains(categoryName))
                    .collect(Collectors.toList());
            categoryNames.addAll(categories);
        }

        categoryNames.remove("Total");
        categoryNames.add("Total"); // Make sure total is the last one in the list
        return categoryNames;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setCategoryFacade(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }
}