package extremesaving.pdf.facade;

import static extremesaving.property.PropertyValueEnum.PDF_FILE_NAME;

import java.io.FileNotFoundException;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.property.AreaBreakType;

import extremesaving.pdf.service.PdfPageService;
import extremesaving.property.PropertiesValueHolder;

public class PdfFacadeImpl implements PdfFacade {

    private PdfPageService yearItemsPageService;
    private PdfPageService monthItemsPageService;

    @Override
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(PropertiesValueHolder.getString(PDF_FILE_NAME));
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);

            yearItemsPageService.generate(document);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            monthItemsPageService.generate(document);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setYearItemsPageService(PdfPageService yearItemsPageService) {
        this.yearItemsPageService = yearItemsPageService;
    }

    public void setMonthItemsPageService(PdfPageService monthItemsPageService) {
        this.monthItemsPageService = monthItemsPageService;
    }
}