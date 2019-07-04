package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.dto.AccountDto;
import extremesaving.service.AccountService;
import extremesaving.service.DataService;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PdfPageSummaryGenerator implements PdfPageGenerator {

    private AccountService accountService;
    private DataService dataService;

    @Override
    public void generate(Document document) {
        try {
            Table table = new Table(3);
            table.setWidth(UnitValue.createPercentValue(100));
            table.addCell(getBalanceCell());
            table.addCell(getChartCell());
            table.addCell(getAccountsCell());

            document.add(table);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Cell getBalanceCell() {


        Cell balanceCell = new Cell();
        balanceCell.setBorder(Border.NO_BORDER);
        Paragraph summaryTitle = new Paragraph("Summary");
        summaryTitle.setBold();
        balanceCell.add(summaryTitle);

        Table alignmentTable1 = new Table(2);
        Cell alignmentTableLeft1 = new Cell();
        alignmentTableLeft1.setBorder(Border.NO_BORDER);

        SimpleDateFormat sf = new SimpleDateFormat("MMM d yyyy");

        Cell alignmentTableRight1 = new Cell();
        alignmentTableRight1.setBorder(Border.NO_BORDER);
        alignmentTableLeft1.add(getItemParagraph("Last update"));
        alignmentTableRight1.add(getItemParagraph(": " + sf.format(new Date())));
        alignmentTableLeft1.add(getItemParagraph("Last item added"));
        alignmentTableRight1.add(getItemParagraph(": " + sf.format(dataService.getLastItemAdded())));
        alignmentTableLeft1.add(getItemParagraph("\n"));
        alignmentTableRight1.add(getItemParagraph("\n"));
        alignmentTableLeft1.add(getItemParagraph("Total balance"));
        alignmentTableRight1.add(getItemParagraph(": " + NumberUtils.formatNumber(dataService.getTotalBalance(), true)));
        alignmentTableLeft1.add(getItemParagraph("Total items"));
        alignmentTableRight1.add(getItemParagraph(": " + dataService.getTotalItems()));
        alignmentTableLeft1.add(getItemParagraph("\n"));
        alignmentTableRight1.add(getItemParagraph("\n"));
        alignmentTableLeft1.add(getItemParagraph("Best month"));
        alignmentTableRight1.add(getItemParagraph(": January 2019 (€ 3 956.41)"));
        alignmentTableLeft1.add(getItemParagraph("Worst month"));
        alignmentTableRight1.add(getItemParagraph(": July 2019 (€ 3 956.40)"));
        alignmentTableLeft1.add(getItemParagraph("Best year"));
        alignmentTableRight1.add(getItemParagraph(": 2019 (€ 20 000.85)"));
        alignmentTableLeft1.add(getItemParagraph("Worst year"));
        alignmentTableRight1.add(getItemParagraph(": 2019 (€ 35 000.45)"));
        alignmentTable1.addCell(alignmentTableLeft1);
        alignmentTable1.addCell(alignmentTableRight1);
        balanceCell.add(alignmentTable1);
        return balanceCell;
    }

    private Cell getChartCell() throws MalformedURLException {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        Image accountPieImage = new Image(ImageDataFactory.create(ExtremeSavingConstants.ACCOUNT_PIE_CHART_IMAGE_FILE));
        accountPieImage.setWidth(350);
        accountPieImage.setHeight(216);
        chartCell.add(accountPieImage);
        return chartCell;
    }

    private Cell getAccountsCell() {
        Cell accountsCell = new Cell();
        accountsCell.setBorder(Border.NO_BORDER);
        Paragraph accountsTitle = new Paragraph("Accounts");
        accountsTitle.setBold();
        accountsCell.add(accountsTitle);

        Table alignmentTable = new Table(3);
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

        List<AccountDto> accounts = accountService.getAccounts();
        for (AccountDto accountDto : accounts) {
            alignmentTableLeft.add(getItemParagraph(accountDto.getName()));
            alignmentTableCenter.add(getItemParagraph(" : "));
            alignmentTableRight.add(getItemParagraph(NumberUtils.formatNumber(accountDto.getTotalResults().getResult(), true)));
        }
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableCenter);
        alignmentTable.addCell(alignmentTableRight);
        accountsCell.add(alignmentTable);

        return accountsCell;
    }

    private Paragraph getItemParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(10);
        return paragraph;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}