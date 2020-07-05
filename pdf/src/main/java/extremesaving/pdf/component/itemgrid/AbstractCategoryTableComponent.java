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

public abstract class AbstractCategoryTableComponent {

    Map<String, List<CategoryDto>> results;
    private int displayMaxItems;
    private int displayMaxTextCharacters;
    private int numberOfColumns;
    private boolean printTotalsColumn;
    private List<String> categoryNames;

    public AbstractCategoryTableComponent withResults(Map<String, List<CategoryDto>> results) {
        this.results = results;
        return this;
    }

    public AbstractCategoryTableComponent withDisplayMaxItems(int displayMaxItems) {
        this.displayMaxItems = displayMaxItems;
        return this;
    }

    public AbstractCategoryTableComponent withNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
        return this;
    }

    public AbstractCategoryTableComponent withDisplayMaxTextCharacters(int displayMaxTextCharacters) {
        this.displayMaxTextCharacters = displayMaxTextCharacters;
        return this;
    }

    public AbstractCategoryTableComponent withPrintTotalsColumn(boolean printTotalsColumn) {
        this.printTotalsColumn = printTotalsColumn;
        return this;
    }

    public AbstractCategoryTableComponent withCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
        return this;
    }

    public Table build() {
        Table table = new Table(numberOfColumns + 3);
        table.setBorder(Border.NO_BORDER);
        table.setWidth(UnitValue.createPercentValue(100));

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

        // Print total cell
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

        // Print category names
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

        cell.add(PdfUtils.getItemParagraph("Category", true, TextAlignment.LEFT));
        cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.LEFT));
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
        cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.CENTER));
        cell.add(PdfUtils.getItemParagraph(ExtremeSavingConstants.TOTAL_COLUMN, true));
        cell.add(PdfUtils.getItemParagraph("Saving Ratio", true));
        cell.add(PdfUtils.getItemParagraph("Total Items", true));
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
            cell.add(PdfUtils.getItemParagraph(title, true, TextAlignment.CENTER));
            cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.CENTER));
            CategoryDto totalsCategory = results.stream().filter(Objects::nonNull).filter(categoryDto -> ExtremeSavingConstants.TOTAL_COLUMN.equals(categoryDto.getName())).findFirst().get();
            results.remove(totalsCategory);
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
            cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.RIGHT));
            cell.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(totalsCategory.getTotalResults().getResult()), true));
            cell.add(PdfUtils.getItemParagraph(NumberUtils.formatPercentage(totalsCategory.getTotalResults().getSavingRatio())));
            cell.add(PdfUtils.getItemParagraph(String.valueOf(totalsCategory.getTotalResults().getNumberOfItems())));
        }
        return cell;
    }
}