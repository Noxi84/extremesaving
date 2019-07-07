package extremesaving.service.chart;

import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.service.ChartDataService;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static com.itextpdf.kernel.pdf.PdfName.Title;

public class FutureLineChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        try {
//            JFreeChart lineChart = ChartFactory.createLineChart(
//                    "title ...",
//                    "Years", "Number of Schools",
//                    createDataset(),
//                    PlotOrientation.VERTICAL,
//                    true, true, false);

            JFreeChart lineChart = lala();

            BufferedImage objBufferedImage = lineChart.createBufferedImage(760, 600);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ImageIO.write(objBufferedImage, "png", bas);
            byte[] byteArray = bas.toByteArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(in);
            File outputfile = new File(ExtremeSavingConstants.FUTURE_LINE_CHART_IMAGE_FILE);
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(Math.random(), "Incomes", "2016");
        dataset.addValue(240, "Expenses", "2016");
        dataset.addValue(300, "Result", "2016");

        dataset.addValue(120, "Incomes", "2017");
        dataset.addValue(240, "Expenses", "2017");
        dataset.addValue(300, "Result", "2017");

        dataset.addValue(120, "Incomes", "2018");
        dataset.addValue(240, "Expenses", "2018");
        dataset.addValue(300, "Result", "2018");

        dataset.addValue(120, "Incomes", "2019");
        dataset.addValue(240, "Expenses", "2019");
        dataset.addValue(300, "Result", "2019");
        return dataset;
    }

    private JFreeChart lala() {
        XYSeries series = new XYSeries(Title);
        for (int i = 0; i <= 10; i++) {
            series.add(i, Math.pow(2, i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        NumberAxis domain = new NumberAxis("x");
        NumberAxis range = new NumberAxis("f(x)");
        XYSplineRenderer r = new XYSplineRenderer(3);
        XYPlot xyplot = new XYPlot(dataset, domain, range, r);
        JFreeChart chart = new JFreeChart(xyplot);
        return chart;
    }


    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}