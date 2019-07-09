package extremesaving.service.chart;

import extremesaving.service.ChartDataService;
import extremesaving.util.PropertiesValueHolder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;

import static extremesaving.util.PropertyValueENum.ACCOUNT_PIE_CHART_IMAGE_FILE;

public class AccountPieChartService implements ChartService {

    private ChartDataService chartDataService;

    @Override
    public void generateChartPng() {
        try {
            Map<String, BigDecimal> accounts = chartDataService.getAccountResults();

            DefaultPieDataset dataSet = new DefaultPieDataset();
            for (Map.Entry<String, BigDecimal> account : accounts.entrySet()) {
                dataSet.setValue(account.getKey(), account.getValue());
            }

            JFreeChart chart = ChartFactory.createPieChart("Accounts", dataSet, true, false, false);
            chart.setTitle("");
            BufferedImage objBufferedImage = chart.createBufferedImage(550, 300);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ImageIO.write(objBufferedImage, "png", bas);
            byte[] byteArray = bas.toByteArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(in);
            File outputfile = new File(PropertiesValueHolder.getInstance().getPropValue(ACCOUNT_PIE_CHART_IMAGE_FILE));
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setChartDataService(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }
}