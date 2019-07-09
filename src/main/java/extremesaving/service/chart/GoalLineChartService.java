package extremesaving.service.chart;

import extremesaving.service.ChartDataService;
import extremesaving.util.ChartUtils;
import extremesaving.util.PropertiesValueHolder;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import static com.itextpdf.kernel.pdf.PdfName.Title;
import static extremesaving.util.PropertyValueENum.FUTURE_LINE_CHART_IMAGE_FILE;

public class GoalLineChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        JFreeChart chart = new JFreeChart(createDataset());
        ChartUtils.writeChartPng(chart, PropertiesValueHolder.getInstance().getPropValue(FUTURE_LINE_CHART_IMAGE_FILE), 760, 600);
    }

    private XYPlot createDataset() {
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

        return xyplot;
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}