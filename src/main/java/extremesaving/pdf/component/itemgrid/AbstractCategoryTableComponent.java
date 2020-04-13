package extremesaving.pdf.component.itemgrid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import extremesaving.calculation.dto.CategoryDto;
import extremesaving.pdf.util.PdfUtils;

public abstract class AbstractCategoryTableComponent {

    private Map<String, List<CategoryDto>> results;
    private int displayMaxItems;
    private int displayMaxTextCharacters;
    private int numberOfColumns;
    private boolean printTotalsColumn;

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

    public Table build() {
        Table table = new Table(numberOfColumns + 3);
        table.setBorder(Border.NO_BORDER);
        table.setWidth(UnitValue.createPercentValue(100));

        int currentMonthOrYear = getCurrentMonthOrYear();
        List<String> categories = getCategoryNames();

        for (int counter = currentMonthOrYear - numberOfColumns; counter <= currentMonthOrYear; counter++) {
            List<CategoryDto> categoryDtos = results.get(String.valueOf(counter));
            if (categoryDtos != null) {
                List<CategoryDto> sortedCategories = new ArrayList<>();
                for (String category : categories) {
                    Optional<CategoryDto> optCategoryDto = categoryDtos.stream().filter(categoryDto -> category.equals(categoryDto.getName())).findFirst();
                    if (optCategoryDto.isPresent()) {
                        sortedCategories.add(optCategoryDto.get());
                    } else {
                        sortedCategories.add(null);
                    }
                }
                String title = getTitle(sortedCategories);
                table.addCell(getItemCell(getAmountCell(title, sortedCategories)));
            }
        }

        // Print total cell
        if (printTotalsColumn) {
            List<CategoryDto> overallCategoryDtos = results.get("Total");
            if (overallCategoryDtos != null) {
                List<CategoryDto> overallSortedCategories = new ArrayList<>();
                for (String category : categories) {
                    Optional<CategoryDto> optCategoryDto = overallCategoryDtos.stream().filter(categoryDto -> category.equals(categoryDto.getName())).findFirst();
                    if (optCategoryDto.isPresent()) {
                        overallSortedCategories.add(optCategoryDto.get());
                    } else {
                        overallSortedCategories.add(null);
                    }
                }
                table.addCell(getItemCell(getAmountCell("Total", overallSortedCategories)));
            }
        }

        // Print category names
        table.addCell(getItemCell(getDescriptionCell(categories)));

        return table;
    }

    abstract int getCurrentMonthOrYear();

    abstract String getTitle(final List<CategoryDto> sortedCategories);

    private  List<String> getCategoryNames() {
        List<String> categories = new ArrayList<>();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        List<CategoryDto> categoryDtosThisYear = results.get(String.valueOf(currentMonth));
        if (categoryDtosThisYear != null) {
            categories.addAll(categoryDtosThisYear.stream().map(categoryDto -> categoryDto.getName()).collect(Collectors.toList()));
        }
        List<CategoryDto> categoryDtosOverall = results.get("Total");
        if (categoryDtosOverall != null) {
            for (CategoryDto categoryDto : categoryDtosOverall) {
                if (!categories.contains(categoryDto.getName())) {
                    categories.add(categoryDto.getName());
                }
            }
        }
        return categories;
    }

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

    protected Cell getDescriptionCell(List<String> categories) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setWidth(600);
        cell.setPaddingLeft(20);
        cell.setMarginLeft(0);
        cell.setPaddingRight(0);
        cell.setMarginRight(0);

        cell.add(PdfUtils.getItemParagraph("Category", true, TextAlignment.LEFT));
        cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.LEFT));
        int counter = 0;
        for (String category : categories) {
            counter++;
            if (counter > displayMaxItems) {
                break;
            }
            cell.add(PdfUtils.getItemParagraph(StringUtils.abbreviate(category, displayMaxTextCharacters)));
        }
        return cell;
    }

    protected Cell getAmountCell(String title, List<CategoryDto> results) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.RIGHT);
        cell.setWidth(160);
        cell.setPaddingLeft(0);
        cell.setMarginLeft(0);
        cell.setPaddingRight(0);
        cell.setMarginRight(0);
        if (results.size() > 0) {
            cell.add(PdfUtils.getItemParagraph(title, true, TextAlignment.RIGHT));
            cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.RIGHT));

            int counter = 0;
            for (CategoryDto categoryDto : results) {
                counter++;
                if (counter > displayMaxItems) {
                    break;
                }
                if (categoryDto == null) {
                    cell.add(PdfUtils.getItemParagraph("\n", false, TextAlignment.RIGHT));
                } else {
                    cell.add(PdfUtils.getItemParagraph(PdfUtils.formatNumber(categoryDto.getTotalResults().getResult())));
                }
            }
        }
        return cell;
    }
}