package extremesaving.pdf.facade;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.property.AreaBreakType;
import extremesaving.pdf.service.PdfPageService;
import extremesaving.property.PropertiesValueHolder;

import java.io.FileNotFoundException;

import static extremesaving.property.PropertyValueEnum.PDF_FILE_NAME;

public class PdfFacadeImpl implements PdfFacade {

    private PdfPageService categoryGridPageService;
    private PdfPageService itemGridPageService;
    private PdfPageService tipOfTheDayPageService;

    @Override
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(PropertiesValueHolder.getString(PDF_FILE_NAME));
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);

            tipOfTheDayPageService.generate(document);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            categoryGridPageService.generate(document);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            itemGridPageService.generate(document);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setCategoryGridPageService(PdfPageService categoryGridPageService) {
        this.categoryGridPageService = categoryGridPageService;
    }

    public void setItemGridPageService(PdfPageService itemGridPageService) {
        this.itemGridPageService = itemGridPageService;
    }

    public void setTipOfTheDayPageService(PdfPageService tipOfTheDayPageService) {
        this.tipOfTheDayPageService = tipOfTheDayPageService;
    }
}