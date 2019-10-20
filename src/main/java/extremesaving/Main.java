package extremesaving;

import extremesaving.charts.facade.ChartFacade;
import extremesaving.pdf.facade.PdfFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private PdfFacade pdfFacade;
    private ChartFacade chartFacade;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/applicationContext*.xml");
        Main p = context.getBean(Main.class);
        p.start();
    }

    private void start() {
        System.out.println("Generating charts...");
        chartFacade.generateCharts();
        System.out.println("Generating pdf...");
        pdfFacade.generatePdf();
        System.out.println("Done.");
    }

    public void setPdfFacade(PdfFacade pdfFacade) {
        this.pdfFacade = pdfFacade;
    }

    public void setChartFacade(ChartFacade chartFacade) {
        this.chartFacade = chartFacade;
    }
}