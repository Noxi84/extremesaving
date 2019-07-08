package extremesaving.facade;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import extremesaving.service.pdf.PdfPageService;
import extremesaving.util.PropertiesValueHolder;

import java.io.FileNotFoundException;

import static extremesaving.util.PropertyValueENum.PDF_FILE_NAME;

public class PdfFacadeImpl implements PdfFacade {

    private PdfPageService pdfPageSummaryGenerator;
    private PdfPageService pdfPageMonthYearGenerator;
    private PdfPageService pdfPageCategoryGridGenerator;
    private PdfPageService pdfPageItemGridGenerator;
    private PdfPageService pdfPagePredictionsGenerator;

    @Override
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(PropertiesValueHolder.getInstance().getPropValue(PDF_FILE_NAME));
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

    public void setPdfPageSummaryGenerator(PdfPageService pdfPageSummaryGenerator) {
        this.pdfPageSummaryGenerator = pdfPageSummaryGenerator;
    }

    public void setPdfPageMonthYearGenerator(PdfPageService pdfPageMonthYearGenerator) {
        this.pdfPageMonthYearGenerator = pdfPageMonthYearGenerator;
    }

    public void setPdfPageCategoryGridGenerator(PdfPageService pdfPageCategoryGridGenerator) {
        this.pdfPageCategoryGridGenerator = pdfPageCategoryGridGenerator;
    }

    public void setPdfPageItemGridGenerator(PdfPageService pdfPageItemGridGenerator) {
        this.pdfPageItemGridGenerator = pdfPageItemGridGenerator;
    }

    public void setPdfPagePredictionsGenerator(PdfPageService pdfPagePredictionsGenerator) {
        this.pdfPagePredictionsGenerator = pdfPagePredictionsGenerator;
    }
}