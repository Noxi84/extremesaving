package extremesaving.pdf.section;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.AccountDto;
import extremesaving.calculation.facade.AccountFacade;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AccountsPdfSectionCreatorImpl implements AccountsPdfSectionCreator {

    private AccountFacade accountFacade;
    private CalculationFacade calculationFacade;

    @Override
    public Cell getAccountsCell() {
        Cell accountsCell = new Cell();
        accountsCell.setBorder(Border.NO_BORDER);
        accountsCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        accountsCell.setTextAlignment(TextAlignment.CENTER);
        accountsCell.setWidth(UnitValue.createPercentValue(50));
        accountsCell.add(PdfUtils.getTitleParagraph("Accounts", TextAlignment.CENTER));
        accountsCell.add(PdfUtils.getItemParagraph("\n"));

        Table alignmentTable = new Table(3);
        alignmentTable.setWidth(UnitValue.createPercentValue(100));
        alignmentTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setTextAlignment(TextAlignment.RIGHT);

        Cell alignmentTableCenter = new Cell();
        alignmentTableCenter.setWidth(10);
        alignmentTableCenter.setBorder(Border.NO_BORDER);
        alignmentTableCenter.setTextAlignment(TextAlignment.LEFT);

        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight.setPaddingRight(20);

        List<AccountDto> accounts = accountFacade.getAccounts();

        // Sort by name
        Collections.sort(accounts, Comparator.comparing(AccountDto::getName));

        // Add positive accounts
        for (AccountDto accountDto : accounts.stream().filter(accountDto -> accountDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList())) {
            alignmentTableLeft.add(PdfUtils.getItemParagraph(accountDto.getName()));
            alignmentTableCenter.add(PdfUtils.getItemParagraph(" : "));
            alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(accountDto.getTotalResults().getResult())));
        }

        // Add negative accounts
        for (AccountDto accountDto : accounts.stream().filter(accountDto -> accountDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) < 0).collect(Collectors.toList())) {
            alignmentTableLeft.add(PdfUtils.getItemParagraph(accountDto.getName()));
            alignmentTableCenter.add(PdfUtils.getItemParagraph(" : "));
            alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(accountDto.getTotalResults().getResult())));
        }
        alignmentTableLeft.add(PdfUtils.getItemParagraph("\n"));
        alignmentTableLeft.add(PdfUtils.getItemParagraph("Total result", true));

        alignmentTableCenter.add(PdfUtils.getItemParagraph("\n"));
        alignmentTableCenter.add(PdfUtils.getItemParagraph(":", true));

        alignmentTableRight.add(PdfUtils.getItemParagraph("\n"));
        alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(calculationFacade.getTotalBalance()), true));

        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableCenter);
        alignmentTable.addCell(alignmentTableRight);
        accountsCell.add(alignmentTable);

        return accountsCell;
    }

    public void setAccountFacade(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }
}