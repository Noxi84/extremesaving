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

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        alignmentTableRight.add(getItemParagraph(sf.format(dataService.getLastItemAdded())));

        alignmentTableLeft.add(getItemParagraph("Total items"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph(String.valueOf(dataService.getTotalItems())));
        
        alignmentTableLeft.add(getItemParagraph("\n"));
        alignmentTableCenter.add(getItemParagraph("\n"));
        alignmentTableRight.add(getItemParagraph("\n"));

        SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM");

        alignmentTableLeft.add(getItemParagraph("Best month"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph(monthDateFormat.format(dataService.getBestMonth())));

        alignmentTableLeft.add(getItemParagraph("Worst month"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph(monthDateFormat.format(dataService.getWorstMonth())));

        SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");
        alignmentTableLeft.add(getItemParagraph("Best year"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph(yearDateFormat.format(dataService.getBestYear())));

        alignmentTableLeft.add(getItemParagraph("Worst year"));
        alignmentTableCenter.add(getItemParagraph(":"));
        alignmentTableRight.add(getItemParagraph(yearDateFormat.format(dataService.getWorstYear())));

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
        accountPieImage.setHeight(200);
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

        // Sort by name
        Collections.sort(accounts, Comparator.comparing(AccountDto::getName));

        // Add positive accounts
        for (AccountDto accountDto : accounts.stream().filter(accountDto -> accountDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList())) {
            alignmentTableLeft.add(getItemParagraph(accountDto.getName()));
            alignmentTableCenter.add(getItemParagraph(" : "));
            alignmentTableRight.add(getItemParagraph(NumberUtils.formatNumber(accountDto.getTotalResults().getResult())));
        }

        // Add zero accounts
        for (AccountDto accountDto : accounts.stream().filter(accountDto -> accountDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList())) {
            alignmentTableLeft.add(getItemParagraph(accountDto.getName()));
            alignmentTableCenter.add(getItemParagraph(" : "));
            alignmentTableRight.add(getItemParagraph(NumberUtils.formatNumber(accountDto.getTotalResults().getResult())));
        }

        // Add negative accounts
        for (AccountDto accountDto : accounts.stream().filter(accountDto -> accountDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) < 0).collect(Collectors.toList())) {
            alignmentTableLeft.add(getItemParagraph(accountDto.getName()));
            alignmentTableCenter.add(getItemParagraph(" : "));
            alignmentTableRight.add(getItemParagraph(NumberUtils.formatNumber(accountDto.getTotalResults().getResult())));
        }

        alignmentTableLeft.add(getItemParagraph("Total result", true));
        alignmentTableCenter.add(getItemParagraph(":", true));
        alignmentTableRight.add(getItemParagraph(NumberUtils.formatNumber(dataService.getTotalBalance()), true));

        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableCenter);
        alignmentTable.addCell(alignmentTableRight);
        accountsCell.add(alignmentTable);

        return accountsCell;
    }

    private Paragraph getItemParagraph(String text, boolean bold) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(9);
        if (bold) {
            paragraph.setBold();
        }
        return paragraph;
    }

    private Paragraph getItemParagraph(String text) {
        return getItemParagraph(text, false);
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}