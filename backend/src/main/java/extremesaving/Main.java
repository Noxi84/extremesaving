package extremesaving;

import extremesaving.backend.chart.*;
import extremesaving.backend.pdf.DefaultPdfGenerator;
import extremesaving.backend.pdf.PdfGenerator;
import extremesaving.frontend.dto.TotalsDto;
import extremesaving.frontend.facade.DefaultTotalsFacade;
import extremesaving.frontend.facade.TotalsFacade;
import extremesaving.sampledata.DefaultSampleDataGenerator;
import extremesaving.sampledata.SampleDataGenerator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    private static TotalsFacade totalsFacade = new DefaultTotalsFacade();
    private static PdfGenerator pdfGenerator = new DefaultPdfGenerator();
    private static ChartGenerator monthlyBarChartGenerator = new MonthlyBarChartGenerator();
    private static ChartGenerator yearlyBarChartGenerator = new YearlyBarChartGenerator();
    private static ChartGenerator overallLineChartGenerator = new OverallLineChartGenerator();
    private static ChartGenerator monthlyMeterChartGenerator = new MonthlyMeterChartGenerator();
    private static SampleDataGenerator sampleDataGenerator = new DefaultSampleDataGenerator();

    public static void main(String[] args) throws IOException, ParseException {
        sampleDataGenerator.generateData();

        TotalsDto totalsDto = totalsFacade.getTotals();
        monthlyBarChartGenerator.generateChartPng(totalsDto);
        yearlyBarChartGenerator.generateChartPng(totalsDto);
        overallLineChartGenerator.generateChartPng(totalsDto);
        monthlyMeterChartGenerator.generateChartPng(totalsDto);
        pdfGenerator.generatePdf(totalsDto);
    }
}