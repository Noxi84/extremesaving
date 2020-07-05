package extremesaving;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import extremesaving.charts.facade.ChartFacade;
import extremesaving.pdf.facade.PdfFacade;

public class Main {

    private PdfFacade pdfFacade;
    private ChartFacade chartFacade;

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath*:/applicationContext/applicationContext*.xml")
                .getBean(Main.class)
                .start();
    }

    private void start() {
        System.out.println("Generating Financial Report.");
        chartFacade.generateMonthBarChart();
        chartFacade.generateGoalLineChart();
        pdfFacade.generatePdf();
        System.out.println("Done.");
    }

    public void setPdfFacade(PdfFacade pdfFacade) {
        this.pdfFacade = pdfFacade;
    }

    public void setChartFacade(final ChartFacade chartFacade) {
        this.chartFacade = chartFacade;
    }
}