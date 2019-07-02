package extremesaving.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.pdf.page.MonthReportGenerator;
import extremesaving.pdf.page.PredictionsReportGenerator;
import extremesaving.pdf.page.SummaryReportGenerator;
import extremesaving.pdf.page.YearReportGenerator;

import java.io.FileNotFoundException;

public class DefaultPdfGenerator implements PdfGenerator {

    private SummaryReportGenerator summaryReportGenerator = new SummaryReportGenerator();
    private MonthReportGenerator monthReportGenerator = new MonthReportGenerator();
    private YearReportGenerator yearReportGenerator = new YearReportGenerator();
    private PredictionsReportGenerator predictionsReportGenerator = new PredictionsReportGenerator();

    @Override
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(ExtremeSavingConstants.PDF_FILE_NAME);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph("Hello World!"));
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}