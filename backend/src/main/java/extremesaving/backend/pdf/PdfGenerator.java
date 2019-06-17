package extremesaving.backend.pdf;

import extremesaving.backend.dto.TotalsDto;

public interface PdfGenerator {

    void generatePdf(TotalsDto totalsDto);
}
