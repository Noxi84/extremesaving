package extremesaving.pdf.page;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.dto.AccountDto;
import extremesaving.service.AccountService;

import java.net.MalformedURLException;
import java.util.List;

public class PdfPageSummaryGenerator implements PdfPageGenerator {

    private AccountService accountService;

    @Override
    public void generate(Document document) {
        try {
            document.add(new Paragraph("Accounts"));

            List<AccountDto> accounts = accountService.getAccounts();

            for (AccountDto accountDto : accounts) {
                document.add(new Paragraph(accountDto.getName() + " : " + accountDto.getTotalResults().getResult()));
            }

            document.add(new Image(ImageDataFactory.create(ExtremeSavingConstants.ACCOUNT_PIE_CHART_IMAGE_FILE)));

            document.add(new Paragraph("Last update: xx"));
            document.add(new Paragraph("Last item added: xx"));
            document.add(new Paragraph("Total items: xx "));

            //        document.add(Image.getInstance(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE));

            document.add(new Paragraph("The five most profitable categories overall are: "));
            document.add(new Paragraph("..."));

            document.add(new Paragraph("The five most expensive categories overall are: "));
            document.add(new Paragraph("..."));

            document.add(new Paragraph("The five most profitable items overall are: "));
            document.add(new Paragraph("..."));

            document.add(new Paragraph("The five most expensive items overall are: "));
            document.add(new Paragraph("..."));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}