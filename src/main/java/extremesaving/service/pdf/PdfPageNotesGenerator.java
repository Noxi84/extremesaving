package extremesaving.service.pdf;

import com.itextpdf.layout.Document;

public class PdfPageNotesGenerator implements PdfPageGenerator {

    @Override
    public void generate(Document document) {

    }

//    public void addPredictionsReport(TotalsDto totalsDto, Document document) throws DocumentException, IOException {
//        Font f = new Font();
//        f.setStyle(Font.BOLD);
//        f.setSize(8);


//   document.add(new Paragraph("Your saving rate for " + new SimpleDateFormat("MMMM yyyy").format(new Date()) + " is:"));
//
//        document.add(new Paragraph("The month with highest income this year is xxx and the month highest expense this year is xxx"));
//
//        document.add(new Paragraph("Your best year is xxxx with a result of xxxx EURO and your worst year is with a result of xxxx EURO"));
//    //        document.add(Image.getInstance(ExtremeSavingConstants.MONTHLY_METER_CHART_IMAGE_FILE));

    //        document.add(Image.getInstance(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE));
//
//        document.newPage();
//        document.add(getParagraph("Predictions"));
//        document.add(new Paragraph("With a current total budget of " + totalsDto.getTotalResults().getResult() + " EURO, an average income of " + totalsDto.getNoTransferResults().getAverageDailyIncome() + " EURO per day and an average expense of " + totalsDto.getNoTransferResults().getAverageDailyExpense() + " EURO per day ", f));
//        document.add(new Paragraph("you can have " + totalsDto.getYearPredictions().get(2024) + " EURO after 7 years with an inflation of 3%.", f));
//        document.add(Image.getInstance(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE));
//
//        document.add(new Paragraph("You can live financially free, without any income for " + totalsDto.getSurvivalDays() + " x years, x months and days .", f));
//        document.add(Image.getInstance(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE));
//
//        document.add(new Paragraph("If you reduce the highest expense category xxxx with 20% you will save xxx EUR after 5 years.", f));
//        document.add(new Paragraph("If you increase the highest income category xxxx with 20% you will save xxx EUR after 5 years.", f));
//    }
//
//    private Paragraph getParagraph(String categories) {
//        Paragraph categoriesParagraph = new Paragraph();
//        categoriesParagraph.add(categories);
//        categoriesParagraph.setAlignment(Element.ALIGN_LEFT);
//        return categoriesParagraph;
//    }
}