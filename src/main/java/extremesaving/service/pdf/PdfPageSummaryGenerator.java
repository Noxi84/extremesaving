package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.dto.AccountDto;
import extremesaving.service.AccountService;
import extremesaving.util.NumberUtils;

import java.net.MalformedURLException;
import java.util.List;

public class PdfPageSummaryGenerator implements PdfPageGenerator {

    private AccountService accountService;

    @Override
    public void generate(Document document) {
        try {
            Table table = new Table(3);
            table.setWidth(UnitValue.createPercentValue(100));

            Cell balanceCell = new Cell();
            balanceCell.setBorder(Border.NO_BORDER);
            Paragraph summaryTitle = new Paragraph("Summary");
            summaryTitle.setBold();
            balanceCell.add(summaryTitle);
            balanceCell.add(getItemParagraph("Total balance: € 115 230.59"));
            balanceCell.add(getItemParagraph("Last update: 10 february 1984"));
            balanceCell.add(getItemParagraph("Last item added: 10 february 1984"));
            balanceCell.add(getItemParagraph("Total items: 75"));
            balanceCell.add(getItemParagraph("Best month: january 2019: xxxx EURO"));
            balanceCell.add(getItemParagraph("Worst month: july 2019: xxxx EURO"));
            balanceCell.add(getItemParagraph(" "));
            balanceCell.add(getItemParagraph("Best year: january 2019: xxxx EURO"));
            balanceCell.add(getItemParagraph("Worst year: july 2019: xxxx EURO"));

            Cell accountsCell = new Cell();
            accountsCell.setBorder(Border.NO_BORDER);
            Paragraph accountsTitle = getItemParagraph("Accounts");
            accountsTitle.setBold();
            accountsCell.add(accountsTitle);
            List<AccountDto> accounts = accountService.getAccounts();
            for (AccountDto accountDto : accounts) {
                accountsCell.add(getItemParagraph(accountDto.getName() + " : " + NumberUtils.formatNumber(accountDto.getTotalResults().getResult(), true)));
            }

            Cell chartCell = new Cell();
            chartCell.setBorder(Border.NO_BORDER);
            Image accountPieImage = new Image(ImageDataFactory.create(ExtremeSavingConstants.ACCOUNT_PIE_CHART_IMAGE_FILE));
            accountPieImage.setWidth(350);
            accountPieImage.setHeight(216);
            chartCell.add(accountPieImage);

            table.addCell(balanceCell);
            table.addCell(chartCell);
            table.addCell(accountsCell);

            document.add(table);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Paragraph getItemParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(10);
        return paragraph;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}