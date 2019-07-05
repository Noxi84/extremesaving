package extremesaving.service.chart;

import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.service.ChartDataService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FutureLineChartGenerator implements ChartGenerator {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        try {
            JFreeChart lineChart = ChartFactory.createLineChart(
                    "title ...",
                    "Years", "Number of Schools",
                    createDataset(),
                    PlotOrientation.VERTICAL,
                    true, true, false);

            BufferedImage objBufferedImage = lineChart.createBufferedImage(600, 370);
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

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}