package extremesaving;

import extremesaving.pdf.PdfService;
import extremesaving.service.chart.ChartService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private PdfService pdfService;
    private ChartService chartService;


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/applicationContext*.xml");
        Main p = context.getBean(Main.class);
        p.start();
    }

    private void start() {
        chartService.generateCharts();
        pdfService.generatePdf();
    }

    public void setPdfService(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    public void setChartService(ChartService chartService) {
        this.chartService = chartService;
    }
}