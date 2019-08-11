package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.dto.AccountDto;
import extremesaving.service.AccountService;
import extremesaving.service.DataService;
import extremesaving.util.ChartUtils;
import extremesaving.util.NumberUtils;
import extremesaving.util.PropertiesValueHolder;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static extremesaving.util.PropertyValueENum.ACCOUNT_PIE_CHART_IMAGE_FILE;

public class PdfPageSummaryService implements PdfPageService {

    private AccountService accountService;
    private DataService dataService;

    @Override
    public void generate(Document document) {
        try {
            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));
            table.addCell(getAccountsCell());
            table.addCell(getChartCell());

            document.add(table);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Cell getChartCell() throws MalformedURLException {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        chartCell.setTextAlignment(TextAlignment.CENTER.CENTER);
        Image accountPieImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(ACCOUNT_PIE_CHART_IMAGE_FILE)));
        accountPieImage.setWidth(350);
        accountPieImage.setHeight(200);
        chartCell.add(accountPieImage);
        return chartCell;
    }

    private Cell getAccountsCell() {
        Cell accountsCell = new Cell();
        accountsCell.setBorder(Border.NO_BORDER);
        accountsCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        accountsCell.setTextAlignment(TextAlignment.CENTER);
        accountsCell.setWidth(400);
        accountsCell.add(ChartUtils.getTitleParagraph("Accounts"));
        accountsCell.add(ChartUtils.getItemParagraph("\n"));

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
        alignmentTableRight.setPaddingRight(20);

        List<AccountDto> accounts = accountService.getAccounts();

        // Sort by name
        Collections.sort(accounts, Comparator.comparing(AccountDto::getName));

        // Add positive accounts
        for (AccountDto accountDto : accounts.stream().filter(accountDto -> accountDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList())) {
            alignmentTableLeft.add(ChartUtils.getItemParagraph(accountDto.getName()));
            alignmentTableCenter.add(ChartUtils.getItemParagraph(" : "));
            alignmentTableRight.add(ChartUtils.getItemParagraph(NumberUtils.formatNumber(accountDto.getTotalResults().getResult())));
        }

//        // Add zero accounts
//        for (AccountDto accountDto : accounts.stream().filter(accountDto -> accountDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList())) {
//            alignmentTableLeft.add(getItemParagraph(accountDto.getName()));
//            alignmentTableCenter.add(getItemParagraph(" : "));
//            alignmentTableRight.add(getItemParagraph(NumberUtils.formatNumber(accountDto.getTotalResults().getResult())));
//        }

        // Add negative accounts
        for (AccountDto accountDto : accounts.stream().filter(accountDto -> accountDto.getTotalResults().getResult().compareTo(BigDecimal.ZERO) < 0).collect(Collectors.toList())) {
            alignmentTableLeft.add(ChartUtils.getItemParagraph(accountDto.getName()));
            alignmentTableCenter.add(ChartUtils.getItemParagraph(" : "));
            alignmentTableRight.add(ChartUtils.getItemParagraph(NumberUtils.formatNumber(accountDto.getTotalResults().getResult())));
        }
        alignmentTableLeft.add(ChartUtils.getItemParagraph("\n"));
        alignmentTableLeft.add(ChartUtils.getItemParagraph("Total result", true));

        alignmentTableCenter.add(ChartUtils.getItemParagraph("\n"));
        alignmentTableCenter.add(ChartUtils.getItemParagraph(":", true));

        alignmentTableRight.add(ChartUtils.getItemParagraph("\n"));
        alignmentTableRight.add(ChartUtils.getItemParagraph(NumberUtils.formatNumber(dataService.getTotalBalance()), true));

        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableCenter);
        alignmentTable.addCell(alignmentTableRight);
        accountsCell.add(alignmentTable);

        return accountsCell;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}