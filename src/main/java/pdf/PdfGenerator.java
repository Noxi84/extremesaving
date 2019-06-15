package pdf;

import dto.TotalsDto;

public interface PdfGenerator {

    void generatePdf(TotalsDto totalsDto);
}
