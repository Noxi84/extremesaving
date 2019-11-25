package extremesaving.pdf.component.itemgrid;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import extremesaving.calculation.dto.AccountDto;
import extremesaving.calculation.dto.CategoryDto;
import extremesaving.pdf.component.tipoftheday.AccountsCellComponent;
import extremesaving.pdf.util.PdfUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

public class SummaryTableComponent {

    private List<CategoryDto> results;
    private BigDecimal savingRatio;
    private String tipOfTheDayMessage;
    private List<AccountDto> accounts;
    private Cell goalAndAwardsCell;

    public SummaryTableComponent withResults(List<CategoryDto> results) {
        this.results = results;
        return this;
    }

    public SummaryTableComponent withSavingRatio(BigDecimal overallSavingRatio) {
        this.savingRatio = overallSavingRatio;
        return this;
    }

    public SummaryTableComponent withTipOfTheDay(String tipOfTheDayMessage) {
        this.tipOfTheDayMessage = tipOfTheDayMessage;
        return this;
    }

    public SummaryTableComponent withAccounts(List<AccountDto> accounts) {
        this.accounts = accounts;
        return this;
    }

    public SummaryTableComponent withGoalAndAwardsCell(Cell goalAndAwardsCell) {
        this.goalAndAwardsCell = goalAndAwardsCell;
        return this;
    }

    public Table build() {
        return createResultCategoryTable(results, savingRatio);
    }


    protected Table createResultCategoryTable(List<CategoryDto> categoryDtos, BigDecimal savingRatio) {
        Table alignmentTable = new Table(4);
        alignmentTable.setWidth(UnitValue.createPercentValue(100));

        // Saving ratio cell
        Cell savingRatioCell = createSavingRatioCell(savingRatio);

        // Create left cell
        Cell alignmentTableLeft = createLeftValuesCell();

        // Create right cell
        Cell alignmentTableRight = createRightValuesCell();

        // Add total amount
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total", true));
        BigDecimal totalAmount = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        alignmentTableRight.add(PdfUtils.getItemParagraph(PdfUtils.formatNumber(totalAmount), true));


        // Add saving ratio
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Saving ratio", true));
        alignmentTableRight.add(PdfUtils.getItemParagraph(PdfUtils.formatPercentage(savingRatio), true));

        // Add total items
        long totalItems = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getNumberOfItems()).mapToLong(i -> i).sum();
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total items"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(String.valueOf(totalItems)));

        // Tip of the day cell
        Cell tipOfTheDayCell = createTipOfTheDayCell();
        Cell accountsCell = buildAccountsCell();

        // Add cells to table
        alignmentTable.addCell(savingRatioCell);
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);
        if (tipOfTheDayCell != null) {
            alignmentTable.addCell(tipOfTheDayCell);
        }
        if (accountsCell != null) {
            alignmentTable.addCell(accountsCell);
        }
        if (goalAndAwardsCell != null) {
            alignmentTable.addCell(goalAndAwardsCell);
        }

        return alignmentTable;
    }


    protected Cell buildAccountsCell() {
        if (accounts != null) {
            return new AccountsCellComponent()
                    .withAccounts(accounts)
                    .build();
        }
        return null;
    }

    private Cell createTipOfTheDayCell() {
        if (StringUtils.isNotBlank(tipOfTheDayMessage)) {
            Cell tipOfTheDayCell = new Cell();
            tipOfTheDayCell.setBorder(Border.NO_BORDER);
            tipOfTheDayCell.setWidth(600);
            tipOfTheDayCell.add(PdfUtils.getItemParagraph(tipOfTheDayMessage));
            tipOfTheDayCell.setPaddingLeft(20);
            return tipOfTheDayCell;
        }
        return null;
    }

    private Cell createRightValuesCell() {
        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setVerticalAlignment(VerticalAlignment.MIDDLE);
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight.setWidth(120);
        alignmentTableRight.setPaddingRight(20);
        return alignmentTableRight;
    }

    private Cell createLeftValuesCell() {
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setVerticalAlignment(VerticalAlignment.MIDDLE);
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setWidth(280);
        alignmentTableLeft.setPaddingLeft(20);
        return alignmentTableLeft;
    }

    private Cell createSavingRatioCell(BigDecimal savingRatio) {
        Cell savingRatioCell = new Cell();
        savingRatioCell.setBorder(Border.NO_BORDER);
        savingRatioCell.add(new SavingRatioImageComponent().withSavingRatio(savingRatio).build());
        savingRatioCell.setPaddingRight(20);
        return savingRatioCell;
    }
}