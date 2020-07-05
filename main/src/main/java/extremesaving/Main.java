package extremesaving;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import extremesaving.charts.facade.ChartFacade;
import extremesaving.pdf.facade.PdfFacade;

/**
 * Main class.
 */
public class Main {

    private PdfFacade pdfFacade;
    private ChartFacade chartFacade;

    /**
     * Main method.
     *
     * @param args java args
     */
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath*:/applicationContext/applicationContext*.xml")
                .getBean(Main.class)
                .start();
    }

    private void start() {
        System.out.println("Generating Financial Report.");
        chartFacade.generateMonthBarChart();
        chartFacade.generateOverallLineChart();
        pdfFacade.generatePdf();
        chartFacade.removeMonthBarChartFile();
        chartFacade.removeOverallLineChartFile();
        System.out.println("Done.");
    }

    public void setPdfFacade(PdfFacade pdfFacade) {
        this.pdfFacade = pdfFacade;
    }

    public void setChartFacade(final ChartFacade chartFacade) {
        this.chartFacade = chartFacade;
    }
}