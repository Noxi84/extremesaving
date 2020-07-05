package extremesaving.pdf.service;

import com.itextpdf.layout.Document;

/**
 * Service to generate a PDF-page.
 */
public interface PdfPageService {

    /**
     * Generate PDF Page
     *
     * @param document Document to add the page.
     */
    void generate(Document document);
}