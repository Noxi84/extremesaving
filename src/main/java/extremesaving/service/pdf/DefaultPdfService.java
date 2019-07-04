package extremesaving.service.pdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import extremesaving.constant.ExtremeSavingConstants;

import java.io.FileNotFoundException;

public class DefaultPdfService implements PdfService {

    private PdfPageGenerator pdfPageSummaryGenerator;
    private PdfPageGenerator pdfPageMonthYearGenerator;
    private PdfPageGenerator pdfPageGridGenerator;
    private PdfPageGenerator pdfPagePredictionsGenerator;

    @Override
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(ExtremeSavingConstants.PDF_FILE_NAME);
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);
            document.getPdfDocument().setDefaultPageSize(PageSize.A4.rotate());

            pdfPageSummaryGenerator.generate(document);
            pdfPageMonthYearGenerator.generate(document);
            pdfPageGridGenerator.generate(document);
            pdfPagePredictionsGenerator.generate(document);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPdfPageSummaryGenerator(PdfPageGenerator pdfPageSummaryGenerator) {
        this.pdfPageSummaryGenerator = pdfPageSummaryGenerator;
    }

    public void setPdfPageMonthYearGenerator(PdfPageGenerator pdfPageMonthYearGenerator) {
        this.pdfPageMonthYearGenerator = pdfPageMonthYearGenerator;
    }

    public void setPdfPageGridGenerator(PdfPageGenerator pdfPageGridGenerator) {
        this.pdfPageGridGenerator = pdfPageGridGenerator;
    }

    public void setPdfPagePredictionsGenerator(PdfPageGenerator pdfPagePredictionsGenerator) {
        this.pdfPagePredictionsGenerator = pdfPagePredictionsGenerator;
    }
}