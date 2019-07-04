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
        balanceCell.setTextAlignment(TextAlignment.CENTER);
        Paragraph summaryTitle = new Paragraph("Summary");
        summaryTitle.setBold();
        balanceCell.add(summaryTitle);

        Table alignmentTable = new Table(3);

        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setTextAlignment(TextAlignment.LEFT);

        Cell alignmentTableCenter = new Cell();
        alignmentTableCenter.setBorder(Border.NO_BORDER);
        alignmentTableCenter.setTextAlignment(TextAlignment.CENTER);

        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);

        SimpleDateFormat sf = new SimpleDateFormat(" d MMMM yyyy");
        alignmentTableLeft.add(getItemParagraph("Last update"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph(sf.format(new Date())));

        alignmentTableLeft.add(getItemParagraph("Last item added"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph( sf.format(dataService.getLastItemAdded())));

        alignmentTableLeft.add(getItemParagraph("\n"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph("\n"));

        alignmentTableLeft.add(getItemParagraph("Total balance"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph( NumberUtils.formatNumber(dataService.getTotalBalance(), true)));

        alignmentTableLeft.add(getItemParagraph("Total items"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph(String.valueOf(dataService.getTotalItems())));

        alignmentTableLeft.add(getItemParagraph("\n"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph("\n"));

        alignmentTableLeft.add(getItemParagraph("Best month"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph("January 2019 (€ 3 956.41)"));

        alignmentTableLeft.add(getItemParagraph("Worst month"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph("July 2019 (€ 3 956.40)"));

        alignmentTableLeft.add(getItemParagraph("Best year"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph("2019 (€ 20 000.85)"));

        alignmentTableLeft.add(getItemParagraph("Worst year"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph("2019 (€ 35 000.45)"));

        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableCenter);
        alignmentTable.addCell(alignmentTableRight);

        balanceCell.add(alignmentTable);
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
        accountsCell.setTextAlignment(TextAlignment.CENTER);
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