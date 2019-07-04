package extremesaving.service.pdf;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.AreaBreakType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfPageYearGenerator implements PdfPageGenerator {

    @Override
    public void generate(Document document) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(new Paragraph("Year report"));


        document.add(new Paragraph("The five most profitable categories overall are: "));
        document.add(new Paragraph("..."));

        document.add(new Paragraph("The five most expensive categories overall are: "));
        document.add(new Paragraph("..."));

        document.add(new Paragraph("The five most profitable items overall are: "));
        document.add(new Paragraph("..."));

        document.add(new Paragraph("The five most expensive items overall are: "));
        document.add(new Paragraph("..."));


        document.add(new Paragraph("Month report"));

        document.add(new Paragraph("Your saving rate for " + new SimpleDateFormat("MMMM yyyy").format(new Date()) + " is:"));

        document.add(new Paragraph("The month with highest income this year is xxx and the month highest expense this year is xxx"));

        document.add(new Paragraph("The five most profitable categories this month are: "));
        document.add(new Paragraph("..."));

        document.add(new Paragraph("The five most expensive categories this month are: "));
        document.add(new Paragraph("..."));

        document.add(new Paragraph("The five most profitable items this month are: "));
        document.add(new Paragraph("..."));

        document.add(new Paragraph("The five most expensive items this month are: "));
        document.add(new Paragraph("..."));





        document.add(new Paragraph("Your best year is xxxx with a result of xxxx EURO and your worst year is with a result of xxxx EURO"));
        //        document.add(Image.getInstance(ExtremeSavingConstants.MONTHLY_METER_CHART_IMAGE_FILE));

        document.add(new Paragraph("The five most profitable categories this year are: "));
        document.add(new Paragraph("..."));

        document.add(new Paragraph("The five most expensive categories this year are: "));
        document.add(new Paragraph("..."));

        document.add(new Paragraph("The five most profitable items this year are: "));
        document.add(new Paragraph("..."));

        document.add(new Paragraph("The five most expensive items this year are: "));
        document.add(new Paragraph("..."));
    }
}