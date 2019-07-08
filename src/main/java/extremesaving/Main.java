package extremesaving;

import extremesaving.facade.ChartFacade;
import extremesaving.facade.PdfFacade;
import extremesaving.util.PropertiesValueHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Main {

    private PdfFacade pdfFacade;
    private ChartFacade chartFacade;

    public static void main(String[] args) throws IOException {
        PropertiesValueHolder.getInstance().getPropValues();
        System.out.println("o");


        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/applicationContext*.xml");
        Main p = context.getBean(Main.class);
        p.start();
    }

    private void start() {
        chartFacade.generateCharts();
        pdfFacade.generatePdf();
    }

    public void setPdfFacade(PdfFacade pdfFacade) {
        this.pdfFacade = pdfFacade;
    }

    public void setChartFacade(ChartFacade chartFacade) {
        this.chartFacade = chartFacade;
    }
}