package extremesaving.pdf.facade;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.property.AreaBreakType;
import extremesaving.pdf.service.PdfPageCreator;
import extremesaving.property.PropertiesValueHolder;

import java.io.FileNotFoundException;

import static extremesaving.property.PropertyValueEnum.PDF_FILE_NAME;

public class PdfFacadeImpl implements PdfFacade {

    private PdfPageCreator pdfPageCategoryGridCreator;
    private PdfPageCreator pdfPageItemGridCreator;
    private PdfPageCreator pdfPageTipOfTheDayCreator;

    @Override
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(PropertiesValueHolder.getString(PDF_FILE_NAME));
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);

            pdfPageTipOfTheDayCreator.generate(document);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            pdfPageCategoryGridCreator.generate(document);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            pdfPageItemGridCreator.generate(document);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPdfPageCategoryGridCreator(PdfPageCreator pdfPageCategoryGridCreator) {
        this.pdfPageCategoryGridCreator = pdfPageCategoryGridCreator;
    }

    public void setPdfPageItemGridCreator(PdfPageCreator pdfPageItemGridCreator) {
        this.pdfPageItemGridCreator = pdfPageItemGridCreator;
    }

    public void setPdfPageTipOfTheDayCreator(PdfPageCreator pdfPageTipOfTheDayCreator) {
        this.pdfPageTipOfTheDayCreator = pdfPageTipOfTheDayCreator;
    }
}