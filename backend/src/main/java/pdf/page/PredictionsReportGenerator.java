package pdf.page;

import com.itextpdf.text.*;
import constant.ExtremeSavingConstants;
import dto.TotalsDto;

import java.io.IOException;

public class PredictionsReportGenerator {

    public void addPredictionsReport(TotalsDto totalsDto, Document document) throws DocumentException, IOException {
        Font f = new Font();
        f.setStyle(Font.BOLD);
        f.setSize(8);

        document.newPage();
        document.add(getParagraph("Predictions"));
        document.add(new Paragraph("With a current total budget of " + totalsDto.getTotalResults().getResult() + " EURO, an average income of " + totalsDto.getNoTransferResults().getAverageDailyIncome() + " EURO per day and an average expense of " + totalsDto.getNoTransferResults().getAverageDailyExpense() + " EURO per day ", f));
        document.add(new Paragraph("ou can have " + totalsDto.getYearPredictions().get(2024) + " EURO after 7 years.", f));
        document.add(Image.getInstance(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE));

        document.add(new Paragraph("You can live financially free, without any income for " + totalsDto.getSurvivalDays() + " x years, x months and days .", f));
        document.add(Image.getInstance(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE));

        document.add(new Paragraph("If you reduce the highest expense category xxxx with 20% you will save xxx EUR after 5 years.", f));
        document.add(new Paragraph("If you increase the highest income category xxxx with 20% you will save xxx EUR after 5 years.", f));
    }

    private Paragraph getParagraph(String categories) {
        Paragraph categoriesParagraph = new Paragraph();
        categoriesParagraph.add(categories);
        categoriesParagraph.setAlignment(Element.ALIGN_LEFT);
        return categoriesParagraph;
    }
}