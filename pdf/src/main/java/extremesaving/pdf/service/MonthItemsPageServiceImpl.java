package extremesaving.pdf.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import extremesaving.common.ExtremeSavingConstants;
import extremesaving.common.util.DateUtils;
import extremesaving.common.util.NumberUtils;
import extremesaving.data.dto.CategoryDto;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.CategoryFacade;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.chart.MonthBarChartImageComponent;
import extremesaving.pdf.component.itemgrid.MonthCategoryTableComponent;
import extremesaving.pdf.util.PdfUtils;

/**
 * Implementation of PdfPageService to generate the PDF-page which contains the monthly results.
 */
public class MonthItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 12;
    private static final int DISPLAY_MAX_TEXT_CHARACTERS = 20;
    public static final int NUMBER_OF_MONTHS = 12;

    private DataFacade dataFacade;
    private CategoryFacade categoryFacade;

    @Override
    public void generate(Document document) {
        document.add(PdfUtils.getTitleParagraph("Financial Report generated on " + new SimpleDateFormat("d MMMM yyyy").format(new Date()), TextAlignment.RIGHT));
        document.add(buildMonthBarChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(buildCategoryTable());
    }

    protected Image buildMonthBarChartImage() {
        return new MonthBarChartImageComponent().build();
    }

    protected Table buildCategoryTable() {
        List<CategoryDto> overallCategoryResults = categoryFacade.getCategories(dataFacade.findAll()).stream().collect(Collectors.toList());

        Map<String, List<CategoryDto>> monthResults = new HashMap<>();

        Calendar lastDateCal = Calendar.getInstance();
        lastDateCal.setTime(new Date());
        int lastMonth = lastDateCal.get(Calendar.MONTH);
        int lastYear = lastDateCal.get(Calendar.YEAR);

        Map<String, List<CategoryDto>> categoryResultsPerMonth = getCategoryResultsPerMonth(lastMonth, lastYear);
        List<String> sortedCategories = getSortedCategories(categoryResultsPerMonth, lastMonth);

        for (int monthCounter = lastMonth; monthCounter > lastMonth - NUMBER_OF_MONTHS; monthCounter--) {
            List<CategoryDto> categoryResults = categoryResultsPerMonth.get(String.valueOf(monthCounter));
            monthResults.put(String.valueOf(monthCounter), categoryResults);
        }

        monthResults.put(ExtremeSavingConstants.TOTAL_COLUMN, overallCategoryResults);

        return new MonthCategoryTableComponent()
                .withCategoryNames(sortedCategories)
                .withResults(monthResults)
                .withNumberOfColumns(12)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(DISPLAY_MAX_TEXT_CHARACTERS)
                .withPrintTotalsColumn(false)
                .build();
    }

    protected Map<String, List<CategoryDto>> getCategoryResultsPerMonth(int lastMonth, int lastYear) {
        Map<String, List<CategoryDto>> categoryResultsPerMonth = new HashMap<>();
        for (int monthCounter = lastMonth; monthCounter > lastMonth - NUMBER_OF_MONTHS; monthCounter--) {
            Calendar monthDate = Calendar.getInstance();
            monthDate.set(Calendar.DAY_OF_MONTH, 1); // day must be set to 1 because setting the month can go 1 month further if current day is 31.
            monthDate.set(Calendar.MONTH, monthCounter);
            monthDate.set(Calendar.YEAR, lastYear);
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
            List<String> incomeCategories = categoriesPerMonth.get(String.valueOf(monthCounter))
                    .stream()
                    .filter(categoryDto -> !categoryDto.getTotalResults().getData().isEmpty())
                    .filter(categoryDto -> NumberUtils.isIncome(categoryDto.getTotalResults().getResult()))
                    .sorted((o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()))
                    .map(CategoryDto::getName)
                    .filter(categoryName -> !categoryNames.contains(categoryName))
                    .collect(Collectors.toList());

            List<String> expenseCategories = categoriesPerMonth.get(String.valueOf(monthCounter))
                    .stream()
                    .filter(categoryDto -> !categoryDto.getTotalResults().getData().isEmpty())
                    .filter(categoryDto -> NumberUtils.isExpense(categoryDto.getTotalResults().getResult()))
                    .sorted(Comparator.comparing(o2 -> o2.getTotalResults().getResult()))
                    .map(CategoryDto::getName)
                    .filter(categoryName -> !categoryNames.contains(categoryName))
                    .collect(Collectors.toList());
            categoryNames.addAll(incomeCategories);
            categoryNames.addAll(expenseCategories);
        }

        categoryNames.remove(ExtremeSavingConstants.TOTAL_COLUMN);
        categoryNames.add(ExtremeSavingConstants.TOTAL_COLUMN); // Make sure total is the last one in the list
        return categoryNames;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setCategoryFacade(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }
}