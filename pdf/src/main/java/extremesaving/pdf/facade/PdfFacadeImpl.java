package extremesaving.pdf.facade;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.property.AreaBreakType;

import extremesaving.pdf.service.PdfPageService;

public class PdfFacadeImpl implements PdfFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfFacadeImpl.class);

    private PdfPageService yearItemsPageService;
    private PdfPageService monthItemsPageService;

    @Override
    public void generatePdf() {
        try {
            PdfWriter writer = new PdfWriter(Paths.get("").toFile().getAbsolutePath() + File.separator + "FinancialReport.pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4.rotate());
            yearItemsPageService.generate(document);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            monthItemsPageService.generate(document);
            document.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("PDF file could not be found.", e);
        }
    }

    public void setYearItemsPageService(PdfPageService yearItemsPageService) {
        this.yearItemsPageService = yearItemsPageService;
    }

    public void setMonthItemsPageService(PdfPageService monthItemsPageService) {
        this.monthItemsPageService = monthItemsPageService;
    }
}