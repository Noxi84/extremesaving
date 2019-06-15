package pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import constant.ExtremeSavingConstants;
import dto.TotalsDto;
import pdf.page.MonthReportGenerator;
import pdf.page.PredictionsReportGenerator;
import pdf.page.SummaryReportGenerator;
import pdf.page.YearReportGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DefaultPdfGenerator implements PdfGenerator {

    private SummaryReportGenerator summaryReportGenerator = new SummaryReportGenerator();
    private MonthReportGenerator monthReportGenerator = new MonthReportGenerator();
    private YearReportGenerator yearReportGenerator = new YearReportGenerator();
    private PredictionsReportGenerator predictionsReportGenerator = new PredictionsReportGenerator();

    @Override
    public void generatePdf(TotalsDto totalsDto) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(ExtremeSavingConstants.PDF_FILE_NAME)));
            document.open();

            summaryReportGenerator.addSummaryReport(totalsDto, document);
            monthReportGenerator.addMonthReport(totalsDto, document);
            yearReportGenerator.addYearReport(totalsDto, document);
            predictionsReportGenerator.addPredictionsReport(totalsDto, document);

            document.close();

            System.out.println("Done");

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}