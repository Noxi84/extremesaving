package extremesaving.pdf.component.itemgrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import extremesaving.common.ExtremeSavingConstants;
import extremesaving.common.util.NumberUtils;
import extremesaving.data.dto.CategoryDto;
import extremesaving.pdf.component.paragraph.CategoryParagraphComponent;

/**
 * Abstract Component builder containing the table with category results.
 * Using .build() will return the component which can be added to the PDF-page..
 */
public abstract class AbstractCategoryTableComponent {

    Map<String, List<CategoryDto>> results;
    private int displayMaxItems;
    private int displayMaxTextCharacters;
    private int numberOfColumns;
    private boolean printTotalsColumn;
    private List<String> categoryNames;

    /**
     * Add the results to the AbstractCategoryTableComponent Builder.
     *
     * @param results Map<String, List<CategoryDto>> containing the CategoryDto's for each key value (month or year).
     * @return AbstractCategoryTableComponent
     */
    public AbstractCategoryTableComponent withResults(Map<String, List<CategoryDto>> results) {
        this.results = results;
        return this;
    }

    /**
     * Set the maximum number of rows (category names) that can be displayed.
     *
     * @param displayMaxItems The maximum number of category rows.
     * @return AbstractCategoryTableComponent
     */
    public AbstractCategoryTableComponent withDisplayMaxItems(int displayMaxItems) {
        this.displayMaxItems = displayMaxItems;
        return this;
    }

    /**
     * Set the number of columns the Table will have.
     *
     * @param numberOfColumns Number of columns.
     * @return AbstractCategoryTableComponent
     */
    public AbstractCategoryTableComponent withNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
        return this;
    }

    /**
     * Set the number of max text characters a category can have. Category names exceeding this value will be abbreviated with a "..."-postfix.
     *
     * @param displayMaxTextCharacters Number of max characters a category name can have before it is abbreviated.
     * @return AbstractCategoryTableComponent
     */
    public AbstractCategoryTableComponent withDisplayMaxTextCharacters(int displayMaxTextCharacters) {
        this.displayMaxTextCharacters = displayMaxTextCharacters;
        return this;
    }

    /**
     * Determines of the totals-column must be added to the table.
     *
     * @param printTotalsColumn True of false to determine of the totals-column must be added.
     * @return AbstractCategoryTableComponent
     */
    public AbstractCategoryTableComponent withPrintTotalsColumn(boolean printTotalsColumn) {
        this.printTotalsColumn = printTotalsColumn;
        return this;
    }

    /**
     * Add the category names to the AbstractCategoryTableComponent Builder.
     *
     * @param categoryNames List of all category names to be displayed.
     * @return AbstractCategoryTableComponent
     */
    public AbstractCategoryTableComponent withCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
        return this;
    }

    /**
     * Build the table to be added to the PDF-page.
     *
     * @return Table
     */
    public Table build() {
        List<List<CategoryParagraphComponent>> valuesColumnData = getValuesColumnData();
        List<CategoryParagraphComponent> totalsColumnData = getTotalsColumnData();
        List<CategoryParagraphComponent> categoryNamesColumnData = getCategoryNamesColumnData();

        CategoryTableComponent categoryTableComponent = new CategoryTableComponent();
        for (List<CategoryParagraphComponent> paragraphs : valuesColumnData) {
            categoryTableComponent.withColumnData(paragraphs);
        }
        if (printTotalsColumn) {
            categoryTableComponent.withColumnData(totalsColumnData);
        }
        return categoryTableComponent
                .withColumnData(categoryNamesColumnData)
                .withRowColor1(new DeviceRgb(224, 224, 224))
                .withRowColor2(new DeviceRgb(204, 229, 255))
                .build();
    }

    private List<List<CategoryParagraphComponent>> getValuesColumnData() {
        List<List<CategoryParagraphComponent>> valuesColumnData = new ArrayList<>();
        int currentMonthOrYear = getLastMonthOrYear();
        for (int counter = currentMonthOrYear - numberOfColumns; counter <= currentMonthOrYear; counter++) {
            List<CategoryDto> categoryDtos = results.get(String.valueOf(counter));
            if (categoryDtos != null) {
                List<CategoryDto> sortedCategories = new ArrayList<>();
                for (String category : categoryNames) {
                    sortedCategories.add(categoryDtos.stream().filter(categoryDto -> category.equals(categoryDto.getName())).findFirst().orElse(null));
                }
                String title = getColumnTitle(sortedCategories);
                List<CategoryParagraphComponent> paragraphs = getAmountParagraphs(title, sortedCategories);
                valuesColumnData.add(paragraphs);
            }
        }
        return valuesColumnData;
    }

    private List<CategoryParagraphComponent> getTotalsColumnData() {
        List<CategoryParagraphComponent> totalsColumnData = new ArrayList<>();
        if (printTotalsColumn) {
            List<CategoryDto> overallCategoryDtos = results.get(ExtremeSavingConstants.TOTAL_COLUMN);
            if (overallCategoryDtos != null) {
                List<CategoryDto> overallSortedCategories = new ArrayList<>();
                for (String category : categoryNames) {
                    Optional<CategoryDto> optCategoryDto = overallCategoryDtos.stream().filter(categoryDto -> category.equals(categoryDto.getName())).findFirst();
                    overallSortedCategories.add(optCategoryDto.orElse(null));
                }
                totalsColumnData = getAmountParagraphs(ExtremeSavingConstants.TOTAL_COLUMN, overallSortedCategories);
            }
        }
        return totalsColumnData;
    }

    private List<CategoryParagraphComponent> getCategoryNamesColumnData() {
        List<CategoryParagraphComponent> paragraphs = new ArrayList<>();

        // Display column title
        paragraphs.add(new CategoryParagraphComponent("Category", true, TextAlignment.CENTER, false, null));
        paragraphs.add(new CategoryParagraphComponent("\n", true, TextAlignment.CENTER, false, null));

        // Display column category names
        int counter = 0;
        for (String categoryName : categoryNames) {
            if (!ExtremeSavingConstants.TOTAL_COLUMN.equals(categoryName)) {
                counter++;
                if (counter > displayMaxItems) {
                    break;
                }
                String trimmedCategoryName = categoryName.length() > displayMaxTextCharacters ? categoryName.substring(0, displayMaxTextCharacters - 3).trim() + "..." : categoryName;
                paragraphs.add(new CategoryParagraphComponent(trimmedCategoryName, false));
            }
        }

        // Display column total-titles
        paragraphs.add(new CategoryParagraphComponent("\n", true, TextAlignment.CENTER, false, null));
        paragraphs.add(new CategoryParagraphComponent("Total Items", true, false, null));
        paragraphs.add(new CategoryParagraphComponent("Saving Ratio", true, false, null));
        paragraphs.add(new CategoryParagraphComponent(ExtremeSavingConstants.TOTAL_COLUMN, true, false, null));

        return paragraphs;
    }

    abstract int getLastMonthOrYear();

    abstract String getColumnTitle(final List<CategoryDto> sortedCategories);

    protected List<CategoryParagraphComponent> getAmountParagraphs(String title, List<CategoryDto> results) {
        List<CategoryParagraphComponent> paragraphs = new ArrayList<>();

        boolean hasData = !results.stream()
                .filter(Objects::nonNull)
                .filter(cat -> ExtremeSavingConstants.TOTAL_COLUMN.equals(cat.getName()))
                .findFirst()
                .get()
                .getTotalResults().getData().isEmpty();
        if (hasData) {
            CategoryDto totalsCategory = results.stream().filter(Objects::nonNull).filter(categoryDto -> ExtremeSavingConstants.TOTAL_COLUMN.equals(categoryDto.getName())).findFirst().get();
            results.remove(totalsCategory);

            // Display column title (usually the month or year)
            paragraphs.add(new CategoryParagraphComponent(title, true, TextAlignment.CENTER, false, new DeviceRgb(0, 25, 51), new DeviceRgb(255, 255, 255)));
            paragraphs.add(new CategoryParagraphComponent("\n", true, TextAlignment.CENTER, false, null));

            // Display column category values
            int counter = 0;
            for (CategoryDto categoryDto : results) {
                counter++;
                if (counter > displayMaxItems) {
                    break;
                }
                if (categoryDto == null) {
                    paragraphs.add(new CategoryParagraphComponent("\n", false, TextAlignment.RIGHT, true, null));
                } else {
                    paragraphs.add(new CategoryParagraphComponent(NumberUtils.formatNumber(categoryDto.getTotalResults().getResult()), true));
                }
            }

            // Display column total values
            paragraphs.add(new CategoryParagraphComponent("\n", true, TextAlignment.RIGHT, false, null));
            paragraphs.add(new CategoryParagraphComponent(String.valueOf(totalsCategory.getTotalResults().getNumberOfItems()), false, new DeviceRgb(204, 229, 255)));
            paragraphs.add(new CategoryParagraphComponent(NumberUtils.formatPercentage(totalsCategory.getTotalResults().getSavingRatio()), false, new DeviceRgb(204, 229, 255)));
            paragraphs.add(new CategoryParagraphComponent(NumberUtils.formatNumber(totalsCategory.getTotalResults().getResult()), true, false, new DeviceRgb(204, 229, 255)));
        }
        return paragraphs;
    }
}