package extremesaving;

import extremesaving.chart.ChartGenerator;
import extremesaving.dto.AccountDto;
import extremesaving.facade.TotalsFacade;
import extremesaving.pdf.PdfGenerator;
import extremesaving.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.List;

public class Main {

    private AccountService accountService;
    private TotalsFacade totalsFacade;
    private PdfGenerator pdfGenerator;
    private ChartGenerator monthlyBarChartGenerator;
    private ChartGenerator yearlyBarChartGenerator;
    private ChartGenerator overallLineChartGenerator;
    private ChartGenerator monthlyMeterChartGenerator;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/applicationContext*.xml");
        Main p = context.getBean(Main.class);
        p.start();
    }

    private void start() {

        List<AccountDto> accounts = accountService.getAccounts();

        for (AccountDto accountDto : accounts) {
            System.out.println(accountDto.getName() + " : " + accountDto.getTotalResults().getResult());
        }

//        TotalsDto totalsDto = totalsFacade.getTotals();
//        monthlyBarChartGenerator.generateChartPng(totalsDto);
//        yearlyBarChartGenerator.generateChartPng(totalsDto);
//        overallLineChartGenerator.generateChartPng(totalsDto);
//        monthlyMeterChartGenerator.generateChartPng(totalsDto);
//        pdfGenerator.generatePdf(totalsDto);
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setTotalsFacade(TotalsFacade totalsFacade) {
        this.totalsFacade = totalsFacade;
    }

    public void setPdfGenerator(PdfGenerator pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    public void setMonthlyBarChartGenerator(ChartGenerator monthlyBarChartGenerator) {
        this.monthlyBarChartGenerator = monthlyBarChartGenerator;
    }

    public void setYearlyBarChartGenerator(ChartGenerator yearlyBarChartGenerator) {
        this.yearlyBarChartGenerator = yearlyBarChartGenerator;
    }

    public void setOverallLineChartGenerator(ChartGenerator overallLineChartGenerator) {
        this.overallLineChartGenerator = overallLineChartGenerator;
    }

    public void setMonthlyMeterChartGenerator(ChartGenerator monthlyMeterChartGenerator) {
        this.monthlyMeterChartGenerator = monthlyMeterChartGenerator;
    }
}