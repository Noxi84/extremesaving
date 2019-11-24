package extremesaving.pdf.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.charts.facade.ChartFacade;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.chart.YearBarChartImageComponent;
import extremesaving.pdf.component.itemgrid.TableComponent;
import extremesaving.pdf.util.PdfUtils;

import java.util.List;

public class OverallItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 10;
    private static final int TEXT_MAX_CHARACTERS = 200;

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;
    private ChartFacade chartFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating OverallItemsPage");

        document.add(buildYearBarChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(PdfUtils.getTitleParagraph("Most profitable items overall", TextAlignment.LEFT));
        document.add(buildProfitsTable());
        document.add(PdfUtils.getTitleParagraph("Most expensive items overall", TextAlignment.LEFT));
        document.add(buildExpensesTable());
    }

    protected Image buildYearBarChartImage() {
        chartFacade.generateYearBarChart();
        return new YearBarChartImageComponent().build();
    }

    protected Table buildProfitsTable() {
        List<ResultDto> results = calculationFacade.getMostProfitableItems(dataFacade.findAll());
        return new TableComponent()
                .withResults(results)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build();
    }

    protected Table buildExpensesTable() {
        List<ResultDto> overallResults = calculationFacade.getMostExpensiveItems(dataFacade.findAll());
        return new TableComponent()
                .withResults(overallResults)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build();
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }

    public void setChartFacade(ChartFacade chartFacade) {
        this.chartFacade = chartFacade;
    }
}