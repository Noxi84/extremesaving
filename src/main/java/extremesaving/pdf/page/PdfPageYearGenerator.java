package extremesaving.pdf.page;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.AreaBreakType;
import extremesaving.constant.ExtremeSavingConstants;

import java.net.MalformedURLException;

public class PdfPageYearGenerator implements PdfPageGenerator {

    @Override
    public void generate(Document document) {
        try {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            document.add(new Paragraph("Year report"));

            document.add(new Image(ImageDataFactory.create(ExtremeSavingConstants.YEARLY_BAR_CHART_IMAGE_FILE)));

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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}