package extremesaving.chart;

import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.dto.ResultDto;
import extremesaving.dto.TotalsDto;
import extremesaving.util.DateUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class MonthlyMeterChartGenerator implements ChartGenerator {

    @Override
    public void generateChartPng()  {
//        JFreeChart barChart = createChart(totalsDto);
//        BufferedImage objBufferedImage = barChart.createBufferedImage(220, 136);
//        ByteArrayOutputStream bas = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(objBufferedImage, "png", bas);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        byte[] byteArray = bas.toByteArray();
//        InputStream in = new ByteArrayInputStream(byteArray);
//        BufferedImage image = ImageIO.read(in);
//        File outputfile = new File(ExtremeSavingConstants.MONTHLY_METER_CHART_IMAGE_FILE);
//        ImageIO.write(image, "png", outputfile);
    }

//    private JFreeChart createChart(TotalsDto totalsDto) {
//        ResultDto highestRange = null;
//        for (Map.Entry<Integer, ResultDto> result : totalsDto.getMonthlyResults().entrySet()) {
//            if (highestRange == null || result.getValue().getResult().compareTo(highestRange.getResult()) > 0) {
//                highestRange = result.getValue();
//            }
//        }
//
//        long monthsBetween = DateUtils.monthsBetween(totalsDto.getTotalResults().getFirstDate(), new Date());
//        BigDecimal averageValue = totalsDto.getNoTransferResults().getResult().divide(BigDecimal.valueOf(monthsBetween));
//        BigDecimal restFromTotal = averageValue.multiply(BigDecimal.valueOf(60)).divide(BigDecimal.valueOf(100));
//        BigDecimal lowerAverage = averageValue.subtract(restFromTotal);
//        BigDecimal highestValue = highestRange.getResult();
//
//        averageValue.setScale(0, RoundingMode.HALF_DOWN);
//        lowerAverage.setScale(0, RoundingMode.HALF_DOWN);
//        highestValue.setScale(0, RoundingMode.HALF_DOWN);
//
//        averageValue = averageValue.round(new MathContext(3, RoundingMode.HALF_UP));
//        lowerAverage = lowerAverage.round(new MathContext(3, RoundingMode.HALF_UP));
//        highestValue = highestValue.round(new MathContext(3, RoundingMode.HALF_UP));
//
//
//        DefaultValueDataset valuedataset = new DefaultValueDataset(totalsDto.getMonthlyResults().get(Calendar.getInstance().get(Calendar.MONTH)).getResult());
//        MeterPlot meterplot = new MeterPlot(valuedataset);
//        //set minimum and maximum value
//        meterplot.setRange(new Range(0.0D, highestValue.doubleValue()));
//
//        meterplot.addInterval(new MeterInterval("Battery LOW", new Range(0.0D, lowerAverage.doubleValue()), Color.red, new BasicStroke(2.0F), new Color(255, 0, 0, 128)));
//
//        meterplot.addInterval(new MeterInterval("Moderate", new Range(lowerAverage.doubleValue() + 1, averageValue.doubleValue()), Color.yellow, new BasicStroke(2.0F), new Color(255, 255, 0, 64)));
//
//        meterplot.addInterval(new MeterInterval("Battery FULL", new Range(averageValue.doubleValue() + 1, highestValue.doubleValue()), Color.green, new BasicStroke(2.0F), new Color(0, 255, 0, 64)));
//
//        meterplot.setNeedlePaint(Color.darkGray);
//        meterplot.setDialBackgroundPaint(Color.white);
//        meterplot.setDialOutlinePaint(Color.black);
//        meterplot.setDialShape(DialShape.CHORD);
//        meterplot.setMeterAngle(180);
//        meterplot.setTickLabelsVisible(true);
//        meterplot.setTickLabelFont(new Font("Arial", 1, 12));
//        meterplot.setTickLabelPaint(Color.black);
//        meterplot.setTickSize(5D);
//        meterplot.setTickPaint(Color.gray);
//        meterplot.setValuePaint(Color.black);
//        meterplot.setValueFont(new Font("Arial", 1, 14));
//        JFreeChart jfreechart = new JFreeChart("",
//                JFreeChart.DEFAULT_TITLE_FONT, meterplot, false);
//        return jfreechart;
//    }
}