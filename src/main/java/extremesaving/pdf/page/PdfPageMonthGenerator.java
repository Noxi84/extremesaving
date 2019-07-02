package extremesaving.pdf.page;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.AreaBreakType;
import extremesaving.constant.ExtremeSavingConstants;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfPageMonthGenerator implements PdfPageGenerator {

    @Override
    public void generate(Document document) {
        try {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            document.add(new Paragraph("Month report"));

            document.add(new Image(ImageDataFactory.create(ExtremeSavingConstants.MONTHLY_BAR_CHART_IMAGE_FILE)));

            document.add(new Paragraph("Your saving rate for " + new SimpleDateFormat("MMMM yyyy").format(new Date()) + " is:"));
            //        document.add(Image.getInstance(ExtremeSavingConstants.MONTHLY_METER_CHART_IMAGE_FILE));

            document.add(new Paragraph("The month with highest income this year is xxx and the month highest expense this year is xxx"));

            document.add(new Paragraph("The five most profitable categories this month are: "));
            document.add(new Paragraph("..."));

            document.add(new Paragraph("The five most expensive categories this month are: "));
            document.add(new Paragraph("..."));

            document.add(new Paragraph("The five most profitable items this month are: "));
            document.add(new Paragraph("..."));

            document.add(new Paragraph("The five most expensive items this month are: "));
            document.add(new Paragraph("..."));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}