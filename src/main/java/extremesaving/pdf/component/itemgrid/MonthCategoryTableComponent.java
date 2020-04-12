package extremesaving.pdf.component.itemgrid;

import static extremesaving.pdf.service.YearItemsPageServiceImpl.NUMBER_OF_YEARS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class MonthCategoryTableComponent {

    private Map<Integer, List<CategoryDto>> results;
    private int displayMaxItems;
    private int displayMaxTextCharacters;

    public MonthCategoryTableComponent withResults(Map<Integer, List<CategoryDto>> results) {
        this.results = results;
        return this;
    }

    public MonthCategoryTableComponent withDisplayMaxItems(int displayMaxItems) {
        this.displayMaxItems = displayMaxItems;
        return this;
    }

    public MonthCategoryTableComponent withDisplayMaxTextCharacters(int displayMaxTextCharacters) {
        this.displayMaxTextCharacters = displayMaxTextCharacters;
        return this;
    }

    public Table build() {
        Table table = new Table(NUMBER_OF_YEARS + 3);
        table.setBorder(Border.NO_BORDER);
        table.setWidth(UnitValue.createPercentValue(100));

        int currentYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));
        List<CategoryDto> categoryDtos = results.get(currentYear);
        List<String> categories = new ArrayList<>();
        if (categoryDtos != null) {
            categories.addAll(results.get(currentYear).stream().map(categoryDto -> categoryDto.getName()).collect(Collectors.toList()));

        }
        for (int yearCounter = currentYear - NUMBER_OF_YEARS; yearCounter <= currentYear; yearCounter++) {
            List<CategoryDto> categoryDtosForYear = results.get(yearCounter);
            if (categoryDtosForYear != null) {
                List<CategoryDto> sortedCategories = new ArrayList<>();
                for (String category : categories) {
                    Optional<CategoryDto> optCategoryDto = categoryDtosForYear.stream().filter(categoryDto -> category.equals(categoryDto.getName())).findFirst();
                    if (optCategoryDto.isPresent()) {
                        sortedCategories.add(optCategoryDto.get());
                    } else {
                        sortedCategories.add(null);
                    }
                }
                boolean printDescription = yearCounter == currentYear;
                table.addCell(getItemCell(sortedCategories, printDescription));
            }
        }

        return table;
    }

    protected Cell getItemCell(List<CategoryDto> results, boolean printDescriptionCell) {
        Table alignmentTable = new Table(printDescriptionCell ? 3 : 1);
        alignmentTable.setBorder(Border.NO_BORDER);
        alignmentTable.setPaddingLeft(0);
        alignmentTable.setMarginLeft(0);
        alignmentTable.setPaddingRight(0);
        alignmentTable.setMarginRight(0);

        alignmentTable.addCell(getAmountCell(results));
        if (printDescriptionCell) {
            alignmentTable.addCell(getDescriptionCell(results));
        }

        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(alignmentTable);
        return cell;
    }

    protected Cell getDescriptionCell(List<CategoryDto> results) {
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
        for (CategoryDto categoryDto : results) {
            counter++;
            if (counter > displayMaxItems) {
                break;
            }
            cell.add(PdfUtils.getItemParagraph(StringUtils.abbreviate(categoryDto.getName(), displayMaxTextCharacters)));
        }
        return cell;
    }

    protected Cell getAmountCell(List<CategoryDto> results) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.RIGHT);
        cell.setWidth(160);
        cell.setPaddingLeft(0);
        cell.setMarginLeft(0);
        cell.setPaddingRight(0);
        cell.setMarginRight(0);

        cell.add(PdfUtils.getItemParagraph("" + new SimpleDateFormat("MMM yyy").format(results.get(0).getTotalResults().getLastDate()), true, TextAlignment.RIGHT));
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
        return cell;
    }
}