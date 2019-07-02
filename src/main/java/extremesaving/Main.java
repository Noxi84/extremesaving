package extremesaving;

import extremesaving.chart.ChartGenerator;
import extremesaving.facade.TotalsFacade;
import extremesaving.pdf.PdfGenerator;
import extremesaving.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private AccountService accountService;
    private TotalsFacade totalsFacade;
    private PdfGenerator pdfGenerator;

    private ChartGenerator accountPieChartGenerator;
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
//        TotalsDto totalsDto = totalsFacade.getTotals();
        accountPieChartGenerator.generateChartPng();
        monthlyBarChartGenerator.generateChartPng();
        yearlyBarChartGenerator.generateChartPng();
//        overallLineChartGenerator.generateChartPng(totalsDto);
//        monthlyMeterChartGenerator.generateChartPng(totalsDto);
        pdfGenerator.generatePdf();
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

    public void setAccountPieChartGenerator(ChartGenerator accountPieChartGenerator) {
        this.accountPieChartGenerator = accountPieChartGenerator;
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