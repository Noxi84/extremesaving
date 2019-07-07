package extremesaving.facade;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.service.pdf.PdfPageGenerator;

import java.io.FileNotFoundException;

public class PdfFacadeImpl implements PdfFacade {

    private PdfPageGenerator pdfPageSummaryGenerator;
    private PdfPageGenerator pdfPageMonthYearGenerator;
    private PdfPageGenerator pdfPageCategoryGridGenerator;
    private PdfPageGenerator pdfPageItemGridGenerator;
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
            pdfPageCategoryGridGenerator.generate(document);
            pdfPageItemGridGenerator.generate(document);
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

    public void setPdfPageCategoryGridGenerator(PdfPageGenerator pdfPageCategoryGridGenerator) {
        this.pdfPageCategoryGridGenerator = pdfPageCategoryGridGenerator;
    }

    public void setPdfPageItemGridGenerator(PdfPageGenerator pdfPageItemGridGenerator) {
        this.pdfPageItemGridGenerator = pdfPageItemGridGenerator;
    }

    public void setPdfPagePredictionsGenerator(PdfPageGenerator pdfPagePredictionsGenerator) {
        this.pdfPagePredictionsGenerator = pdfPagePredictionsGenerator;
    }
}