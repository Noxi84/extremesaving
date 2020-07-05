package extremesaving;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import extremesaving.charts.facade.ChartFacade;
import extremesaving.pdf.facade.PdfFacade;

/**
 * Main class.
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private PdfFacade pdfFacade;
    private ChartFacade chartFacade;

    /**
     * Main method.
     *
     * @param args java args
     */
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath*:/applicationContext*.xml")
                .getBean(Main.class)
                .start();
    }

    private void start() {
        LOGGER.info("Generating Financial Report.");
        chartFacade.generateMonthBarChart();
        chartFacade.generateOverallLineChart();
        pdfFacade.generatePdf();
        chartFacade.removeMonthBarChartFile();
        chartFacade.removeOverallLineChartFile();
        LOGGER.info("Done.");
    }

    public void setPdfFacade(PdfFacade pdfFacade) {
        this.pdfFacade = pdfFacade;
    }

    public void setChartFacade(final ChartFacade chartFacade) {
        this.chartFacade = chartFacade;
    }
}