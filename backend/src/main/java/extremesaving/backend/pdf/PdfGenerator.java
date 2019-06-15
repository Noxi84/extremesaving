package extremesaving.backend.pdf;

import extremesaving.frontend.dto.TotalsDto;

public interface PdfGenerator {

    void generatePdf(TotalsDto totalsDto);
}
