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

    private PdfPageService pdfPageSummaryService;
    private PdfPageService pdfPageMonthYearService;
    private PdfPageService pdfPageCategoryGridService;
    private PdfPageService pdfPageItemGridService;
    private PdfPageService pdfPagePredictionsService;

    @Override
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(PropertiesValueHolder.getInstance().getPropValue(PDF_FILE_NAME));
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);
            document.getPdfDocument().setDefaultPageSize(PageSize.A4.rotate());

            pdfPageSummaryService.generate(document);
            pdfPageMonthYearService.generate(document);
            pdfPageCategoryGridService.generate(document);
            pdfPageItemGridService.generate(document);
            pdfPagePredictionsService.generate(document);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPdfPageSummaryService(PdfPageService pdfPageSummaryService) {
        this.pdfPageSummaryService = pdfPageSummaryService;
    }

    public void setPdfPageMonthYearService(PdfPageService pdfPageMonthYearService) {
        this.pdfPageMonthYearService = pdfPageMonthYearService;
    }

    public void setPdfPageCategoryGridService(PdfPageService pdfPageCategoryGridService) {
        this.pdfPageCategoryGridService = pdfPageCategoryGridService;
    }

    public void setPdfPageItemGridService(PdfPageService pdfPageItemGridService) {
        this.pdfPageItemGridService = pdfPageItemGridService;
    }

    public void setPdfPagePredictionsService(PdfPageService pdfPagePredictionsService) {
        this.pdfPagePredictionsService = pdfPagePredictionsService;
    }
}