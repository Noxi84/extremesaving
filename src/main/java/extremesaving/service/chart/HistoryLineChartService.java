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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static com.itextpdf.kernel.pdf.PdfName.Title;

public class HistoryLineChartService implements ChartService {

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
            File outputfile = new File(ExtremeSavingConstants.HISTORY_LINE_CHART_IMAGE_FILE);
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/YYYY");

        for (int i = 0; i < 9000; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 1);

            Random r = new Random();
            int low = 6000;
            int high = 30000;
            int result = r.nextInt(high - low) + low;

            dataset.addValue(result, "Incomes", sf.format(cal.getTime()));
            dataset.addValue(result, "Expenses", sf.format(cal.getTime()));
            dataset.addValue(result, "Result", sf.format(cal.getTime()));
        }


        return dataset;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}