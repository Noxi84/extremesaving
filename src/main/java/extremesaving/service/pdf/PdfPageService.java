package extremesaving.service.pdf;

import com.itextpdf.layout.Document;

public interface PdfPageService {

    void generate(Document document);
}