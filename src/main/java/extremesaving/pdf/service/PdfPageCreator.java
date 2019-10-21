package extremesaving.pdf.service;

import com.itextpdf.layout.Document;

public interface PdfPageCreator {

    void generate(Document document);
}