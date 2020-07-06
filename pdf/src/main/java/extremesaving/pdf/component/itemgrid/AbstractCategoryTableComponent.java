package extremesaving.pdf.component.itemgrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import extremesaving.common.ExtremeSavingConstants;
import extremesaving.common.util.NumberUtils;
import extremesaving.data.dto.CategoryDto;
import extremesaving.pdf.util.PdfUtils;

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
        Table table = new Table(numberOfColumns + 3);
        table.setBorder(Border.NO_BORDER);
        table.setWidth(UnitValue.createPercentValue(100));

        // Print values column
        int currentMonthOrYear = getLastMonthOrYear();
        for (int counter = currentMonthOrYear - numberOfColumns; counter <= currentMonthOrYear; counter++) {
            List<CategoryDto> categoryDtos = results.get(String.valueOf(counter));
            if (categoryDtos != null) {
                List<CategoryDto> sortedCategories = new ArrayList<>();
                for (String category : categoryNames) {
                    sortedCategories.add(categoryDtos.stream().filter(categoryDto -> category.equals(categoryDto.getName())).findFirst().orElse(null));
                }
                String title = getColumnTitle(sortedCategories);
                table.addCell(getItemCell(getAmountCell(title, sortedCategories)));
            }
        }

        // Print total column
        if (printTotalsColumn) {
            List<CategoryDto> overallCategoryDtos = results.get(ExtremeSavingConstants.TOTAL_COLUMN);
            if (overallCategoryDtos != null) {
                List<CategoryDto> overallSortedCategories = new ArrayList<>();
                for (String category : categoryNames) {
                    Optional<CategoryDto> optCategoryDto = overallCategoryDtos.stream().filter(categoryDto -> category.equals(categoryDto.getName())).findFirst();
                    overallSortedCategories.add(optCategoryDto.orElse(null));
                }
                table.addCell(getItemCell(getAmountCell(ExtremeSavingConstants.TOTAL_COLUMN, overallSortedCategories)));
            }
        }

        // Print category names column
        table.addCell(getItemCell(getCategoryNamesCell(categoryNames)));

        return table;
    }

    abstract int getLastMonthOrYear();

    abstract String getColumnTitle(final List<CategoryDto> sortedCategories);

    protected Cell getItemCell(Cell amountCell) {
        Table alignmentTable = new Table(1);
        alignmentTable.setBorder(Border.NO_BORDER);
        alignmentTable.setPaddingLeft(0);
        alignmentTable.setMarginLeft(0);
        alignmentTable.setPaddingRight(0);
        alignmentTable.setMarginRight(0);
        alignmentTable.addCell(amountCell);

        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(alignmentTable);
        return cell;
    }

    protected Cell getCategoryNamesCell(List<String> categories) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setWidth(650);
        cell.setPaddingLeft(20);
        cell.setMarginLeft(0);
        cell.setPaddingRight(0);
        cell.setMarginRight(0);

        // Display column title
        cell.add(PdfUtils.getItemParagraph("Category", true, TextAlignment.LEFT));
        cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.LEFT));

        // Display column category names
        int counter = 0;
        for (String categoryName : categories) {
            if (!ExtremeSavingConstants.TOTAL_COLUMN.equals(categoryName)) {
                counter++;
                if (counter > displayMaxItems) {
                    break;
                }
                String trimmedCategoryName = categoryName.length() > displayMaxTextCharacters ? categoryName.substring(0, displayMaxTextCharacters - 3).trim() + "..." : categoryName;
                cell.add(PdfUtils.getItemParagraph(trimmedCategoryName));
            }
        }

        // Display column total-titles
        cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.CENTER));
        cell.add(PdfUtils.getItemParagraph("Total Items", true));
        cell.add(PdfUtils.getItemParagraph("Saving Ratio", true));
        cell.add(PdfUtils.getItemParagraph(ExtremeSavingConstants.TOTAL_COLUMN, true));

        return cell;
    }

    protected Cell getAmountCell(String title, List<CategoryDto> results) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setWidth(160);
        cell.setPaddingLeft(0);
        cell.setMarginLeft(0);
        cell.setPaddingRight(0);
        cell.setMarginRight(0);

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
            cell.add(PdfUtils.getItemParagraph(title, true, TextAlignment.CENTER));
            cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.CENTER));

            // Display column category values
            int counter = 0;
            for (CategoryDto categoryDto : results) {
                counter++;
                if (counter > displayMaxItems) {
                    break;
                }
                if (categoryDto == null) {
                    cell.add(PdfUtils.getItemParagraph("\n", false, TextAlignment.RIGHT));
                } else {
                    cell.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(categoryDto.getTotalResults().getResult())));
                }
            }

            // Display column total values
            cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.RIGHT));
            cell.add(PdfUtils.getItemParagraph(String.valueOf(totalsCategory.getTotalResults().getNumberOfItems())));
            cell.add(PdfUtils.getItemParagraph(NumberUtils.formatPercentage(totalsCategory.getTotalResults().getSavingRatio())));
            cell.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(totalsCategory.getTotalResults().getResult()), true));
        }
        return cell;
    }
}