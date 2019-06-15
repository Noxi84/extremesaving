import chart.*;
import dto.TotalsDto;
import facade.DefaultTotalsFacade;
import facade.TotalsFacade;
import org.apache.log4j.Logger;
import pdf.DefaultPdfGenerator;
import pdf.PdfGenerator;

import java.io.IOException;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    private static TotalsFacade totalsFacade = new DefaultTotalsFacade();
    private static PdfGenerator pdfGenerator = new DefaultPdfGenerator();
    private static ChartGenerator monthlyBarChartGenerator = new MonthlyBarChartGenerator();
    private static ChartGenerator yearlyBarChartGenerator = new YearlyBarChartGenerator();
    private static ChartGenerator overallLineChartGenerator = new OverallLineChartGenerator();
    private static ChartGenerator monthlyMeterChartGenerator = new MonthlyMeterChartGenerator();

    public static void main(String[] args) throws IOException {
        TotalsDto totalsDto = totalsFacade.getTotals();
        monthlyBarChartGenerator.generateChartPng(totalsDto);
        yearlyBarChartGenerator.generateChartPng(totalsDto);
        overallLineChartGenerator.generateChartPng(totalsDto);
        monthlyMeterChartGenerator.generateChartPng(totalsDto);
        pdfGenerator.generatePdf(totalsDto);
    }
}