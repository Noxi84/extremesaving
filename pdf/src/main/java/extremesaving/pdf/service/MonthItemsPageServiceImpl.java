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

import extremesaving.common.util.DateUtils;
import extremesaving.data.dto.CategoryDto;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.CategoryFacade;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.chart.MonthBarChartImageComponent;
import extremesaving.pdf.component.itemgrid.MonthCategoryTableComponent;
import extremesaving.pdf.util.PdfUtils;

public class MonthItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 20;
    private static final int TEXT_MAX_CHARACTERS = 200;
    public static final int NUMBER_OF_MONTHS = 12;

    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating Monthly Analysis PDF Report");
        document.add(PdfUtils.getTitleParagraph("Financial Report generated on " + new SimpleDateFormat("d MMMM yyyy").format(new Date()), TextAlignment.RIGHT));
        document.add(buildMonthBarChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(buildCategoryTable());
    }

    protected Image buildMonthBarChartImage() {
        return new MonthBarChartImageComponent().build();
    }

    protected Table buildCategoryTable() {
        List<CategoryDto> overallCategoryResults = categoryFacade.getCategories(dataFacade.findAll().stream().filter(dataDto -> DateUtils.isEqualYear(new Date(), dataDto.getDate())).collect(Collectors.toSet())).stream()
                .collect(Collectors.toList());

        Map<String, List<CategoryDto>> monthResults = new HashMap<>();

        Date lastDate = overallCategoryResults.stream().map(categoryDto -> categoryDto.getTotalResults().getLastDate()).max(Date::compareTo).get();
        Calendar lastDateCal = Calendar.getInstance();
        lastDateCal.setTime(lastDate);
        int lastMonth = lastDateCal.get(Calendar.MONTH);

        Map<String, List<CategoryDto>> categoryResultsPerMonth = getCategoryResultsPerMonth(lastMonth);
        List<String> sortedCategories = getSortedCategories(categoryResultsPerMonth, lastMonth);

        for (int monthCounter = lastMonth; monthCounter > lastMonth - NUMBER_OF_MONTHS; monthCounter--) {
            List<CategoryDto> categoryResults = categoryResultsPerMonth.get(String.valueOf(monthCounter));
            monthResults.put(String.valueOf(monthCounter), categoryResults);
        }

        monthResults.put("Total", overallCategoryResults);

        return new MonthCategoryTableComponent()
                .withCategoryNames(sortedCategories)
                .withResults(monthResults)
                .withNumberOfColumns(12)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .withPrintTotalsColumn(false)
                .build();
    }

    protected Map<String, List<CategoryDto>> getCategoryResultsPerMonth(int lastMonth) {
        Map<String, List<CategoryDto>> categoryResultsPerMonth = new HashMap<>();
        for (int monthCounter = lastMonth; monthCounter > lastMonth - NUMBER_OF_MONTHS; monthCounter--) {
            Calendar monthDate = Calendar.getInstance();
            monthDate.set(Calendar.DAY_OF_MONTH, 1); // day must be set to 1 because setting the month can go 1 month further if current day is 31.
            monthDate.set(Calendar.MONTH, monthCounter);
            List<DataDto> dataDtos = dataFacade.findAll().stream()
                    .filter(dataDto -> DateUtils.isEqualYearAndMonth(monthDate.getTime(), dataDto.getDate()))
                    .collect(Collectors.toList());
            List<CategoryDto> categoryResults = categoryFacade.getCategories(dataDtos);
            categoryResultsPerMonth.put(String.valueOf(monthCounter), categoryResults);
        }
        return categoryResultsPerMonth;
    }

    protected List<String> getSortedCategories(Map<String, List<CategoryDto>> categoriesPerMonth, int lastMonth) {
        List<String> categoryNames = new ArrayList<>();
        for (int monthCounter = lastMonth; monthCounter > lastMonth - NUMBER_OF_MONTHS; monthCounter--) {
            List<String> categories = categoriesPerMonth.get(String.valueOf(monthCounter))
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