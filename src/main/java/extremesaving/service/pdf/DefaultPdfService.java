package extremesaving.service.pdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import extremesaving.constant.ExtremeSavingConstants;

import java.io.FileNotFoundException;

public class DefaultPdfService implements PdfService {

    private PdfPageGenerator pdfPageSummaryGenerator;
    private PdfPageGenerator pdfPageMonthGenerator;
    private PdfPageGenerator pdfPageYearGenerator;
    private PredictionsReportGenerator predictionsReportGenerator;

    @Override
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(ExtremeSavingConstants.PDF_FILE_NAME);
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);
            document.getPdfDocument().setDefaultPageSize(PageSize.A4.rotate());

            pdfPageSummaryGenerator.generate(document);
            pdfPageMonthGenerator.generate(document);
            pdfPageYearGenerator.generate(document);
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPdfPageSummaryGenerator(PdfPageGenerator pdfPageSummaryGenerator) {
        this.pdfPageSummaryGenerator = pdfPageSummaryGenerator;
    }

    public void setPdfPageMonthGenerator(PdfPageGenerator pdfPageMonthGenerator) {
        this.pdfPageMonthGenerator = pdfPageMonthGenerator;
    }

    public void setPdfPageYearGenerator(PdfPageGenerator pdfPageYearGenerator) {
        this.pdfPageYearGenerator = pdfPageYearGenerator;
    }
}