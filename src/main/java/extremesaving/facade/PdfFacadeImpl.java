package extremesaving.facade;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.property.AreaBreakType;
import extremesaving.service.pdf.PdfPageService;
import extremesaving.util.PropertiesValueHolder;

import java.io.FileNotFoundException;

import static extremesaving.util.PropertyValueENum.PDF_FILE_NAME;

public class PdfFacadeImpl implements PdfFacade {

    private PdfPageService pdfPageOverallService;
    private PdfPageService pdfPageMonthService;
    private PdfPageService pdfPageYearService;
    private PdfPageService pdfPageCategoryGridService;
    private PdfPageService pdfPageItemGridService;
    private PdfPageService pdfPagePredictionsService;

    @Override
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(PropertiesValueHolder.getInstance().getPropValue(PDF_FILE_NAME));
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);

            pdfPagePredictionsService.generate(document);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

//            pdfPageOverallService.generate(document);
//            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            pdfPageMonthService.generate(document);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

//            pdfPageYearService.generate(document);
//            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            pdfPageCategoryGridService.generate(document);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            pdfPageItemGridService.generate(document);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPdfPageOverallService(PdfPageService pdfPageOverallService) {
        this.pdfPageOverallService = pdfPageOverallService;
    }

    public void setPdfPageMonthService(PdfPageService pdfPageMonthService) {
        this.pdfPageMonthService = pdfPageMonthService;
    }

    public void setPdfPageYearService(PdfPageService pdfPageYearService) {
        this.pdfPageYearService = pdfPageYearService;
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