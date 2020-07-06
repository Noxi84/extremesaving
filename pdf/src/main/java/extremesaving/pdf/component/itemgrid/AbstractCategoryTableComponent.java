package extremesaving.pdf.component.itemgrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
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

        List<List<Paragraph>> valuesColumnData = getValuesColumnData();
        List<Paragraph> totalsColumnData = getTotalsColumnData();
        List<Paragraph> categoryNamesColumnData = getCategoryNamesColumnData();

        // Print values column
        for (List<Paragraph> p1 : valuesColumnData) {
            table.addCell(getItemCell(getAmountCell(p1)));
        }

        // Print total column
        table.addCell(getItemCell(getAmountCell(totalsColumnData)));

        // Print category names column
        table.addCell(getItemCell(getCategoryNamesCell(categoryNamesColumnData)));

        return table;
    }

    private List<List<Paragraph>> getValuesColumnData() {
        List<List<Paragraph>> valuesColumnData = new ArrayList<>();
        int currentMonthOrYear = getLastMonthOrYear();
        for (int counter = currentMonthOrYear - numberOfColumns; counter <= currentMonthOrYear; counter++) {
            List<CategoryDto> categoryDtos = results.get(String.valueOf(counter));
            if (categoryDtos != null) {
                List<CategoryDto> sortedCategories = new ArrayList<>();
                for (String category : categoryNames) {
                    sortedCategories.add(categoryDtos.stream().filter(categoryDto -> category.equals(categoryDto.getName())).findFirst().orElse(null));
                }
                String title = getColumnTitle(sortedCategories);
                List<Paragraph> paragraphs = getAmountCellParagraphs(title, sortedCategories);
                valuesColumnData.add(paragraphs);
            }
        }
        return valuesColumnData;
    }

    private List<Paragraph> getTotalsColumnData() {
        List<Paragraph> totalsColumnData = new ArrayList<>();
        if (printTotalsColumn) {
            List<CategoryDto> overallCategoryDtos = results.get(ExtremeSavingConstants.TOTAL_COLUMN);
            if (overallCategoryDtos != null) {
                List<CategoryDto> overallSortedCategories = new ArrayList<>();
                for (String category : categoryNames) {
                    Optional<CategoryDto> optCategoryDto = overallCategoryDtos.stream().filter(categoryDto -> category.equals(categoryDto.getName())).findFirst();
                    overallSortedCategories.add(optCategoryDto.orElse(null));
                }
                totalsColumnData = getAmountCellParagraphs(ExtremeSavingConstants.TOTAL_COLUMN, overallSortedCategories);
            }
        }
        return totalsColumnData;
    }

    private List<Paragraph> getCategoryNamesColumnData() {
        List<Paragraph> paragraphs = new ArrayList<>();

        // Display column title
        paragraphs.add(PdfUtils.getItemParagraph("Category", true, TextAlignment.LEFT));
        paragraphs.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.LEFT));

        // Display column category names
        int counter = 0;
        for (String categoryName : categoryNames) {
            if (!ExtremeSavingConstants.TOTAL_COLUMN.equals(categoryName)) {
                counter++;
                if (counter > displayMaxItems) {
                    break;
                }
                String trimmedCategoryName = categoryName.length() > displayMaxTextCharacters ? categoryName.substring(0, displayMaxTextCharacters - 3).trim() + "..." : categoryName;
                paragraphs.add(PdfUtils.getItemParagraph(trimmedCategoryName));
            }
        }

        // Display column total-titles
        paragraphs.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.CENTER));
        paragraphs.add(PdfUtils.getItemParagraph("Total Items", true));
        paragraphs.add(PdfUtils.getItemParagraph("Saving Ratio", true));
        paragraphs.add(PdfUtils.getItemParagraph(ExtremeSavingConstants.TOTAL_COLUMN, true));

        return paragraphs;
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

    protected Cell getCategoryNamesCell(List<Paragraph> paragraphs) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setWidth(650);
        cell.setPaddingLeft(20);
        cell.setMarginLeft(0);
        cell.setPaddingRight(0);
        cell.setMarginRight(0);

        for (Paragraph paragraph : paragraphs) {
            cell.add(paragraph);
        }
        return cell;
    }

    protected Cell getAmountCell(List<Paragraph> paragraphs) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setWidth(160);
        cell.setPaddingLeft(0);
        cell.setMarginLeft(0);
        cell.setPaddingRight(0);
        cell.setMarginRight(0);

        for (Paragraph paragraph : paragraphs) {
            cell.add(paragraph);
        }
        return cell;
    }

    protected List<Paragraph> getAmountCellParagraphs(String title, List<CategoryDto> results) {
        List<Paragraph> paragraphs = new ArrayList<>();

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
            paragraphs.add(PdfUtils.getItemParagraph(title, true, TextAlignment.CENTER));
            paragraphs.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.CENTER));

            // Display column category values
            int counter = 0;
            for (CategoryDto categoryDto : results) {
                counter++;
                if (counter > displayMaxItems) {
                    break;
                }
                if (categoryDto == null) {
                    paragraphs.add(PdfUtils.getItemParagraph("\n", false, TextAlignment.RIGHT));
                } else {
                    paragraphs.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(categoryDto.getTotalResults().getResult())));
                }
            }

            // Display column total values
            paragraphs.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.RIGHT));
            paragraphs.add(PdfUtils.getItemParagraph(String.valueOf(totalsCategory.getTotalResults().getNumberOfItems())));
            paragraphs.add(PdfUtils.getItemParagraph(NumberUtils.formatPercentage(totalsCategory.getTotalResults().getSavingRatio())));
            paragraphs.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(totalsCategory.getTotalResults().getResult()), true));
        }
        return paragraphs;
    }
}