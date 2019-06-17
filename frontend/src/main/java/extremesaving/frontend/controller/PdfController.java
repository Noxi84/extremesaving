package extremesaving.frontend.controller;

import extremesaving.backend.chart.ChartGenerator;
import extremesaving.backend.pdf.PdfGenerator;
import extremesaving.backend.dto.TotalsDto;
import extremesaving.frontend.facade.TotalsFacade;
import extremesaving.sampledata.SampleDataGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;

@Controller
public class PdfController {

    @Resource(name = "defaultTotalsFacade")
    private TotalsFacade totalsFacade;

    @Resource(name = "defaultPdfGenerator")
    private PdfGenerator pdfGenerator;

    @Resource(name = "monthlyBarChartGenerator")
    private ChartGenerator monthlyBarChartGenerator;

    @Resource(name = "yearlyBarChartGenerator")
    private ChartGenerator yearlyBarChartGenerator;
    @Resource(name = "overallLineChartGenerator")
    private ChartGenerator overallLineChartGenerator;

    @Resource(name = "monthlyMeterChartGenerator")
    private ChartGenerator monthlyMeterChartGenerator;

    @Resource(name = "defaultSampleDataGenerator")
    private SampleDataGenerator sampleDataGenerator;

    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public String pdf(ModelMap model) throws IOException, ParseException {
        sampleDataGenerator.generateData();

        TotalsDto totalsDto = totalsFacade.getTotals();
        monthlyBarChartGenerator.generateChartPng(totalsDto);
        yearlyBarChartGenerator.generateChartPng(totalsDto);
        overallLineChartGenerator.generateChartPng(totalsDto);
        monthlyMeterChartGenerator.generateChartPng(totalsDto);
        pdfGenerator.generatePdf(totalsDto);

        return "index";
    }

}