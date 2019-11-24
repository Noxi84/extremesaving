package extremesaving.pdf.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.charts.facade.ChartFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.itemgrid.TableComponent;
import extremesaving.pdf.component.tipoftheday.MonthBarChartImageComponent;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MonthItemsPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 39;
    private static final int TEXT_MAX_CHARACTERS = 200;

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;
    private ChartFacade chartFacade;

    @Override
    public void generate(Document document) {
        System.out.println("Generating MonthItemsPageServiceImpl");

        document.add(buildMonthBarChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));
        document.add(PdfUtils.getTitleParagraph("Most profitable items this month", TextAlignment.LEFT));
        document.add(buildProfitsTable());
        document.add(PdfUtils.getTitleParagraph("Most expensive items this month", TextAlignment.LEFT));
        document.add(buildExpensesTable());
    }

    protected Image buildMonthBarChartImage() {
        chartFacade.generateMonthBarChart();
        return new MonthBarChartImageComponent().build();
    }

    protected Table buildProfitsTable() {
        List<DataDto> monthResults = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<ResultDto> results = calculationFacade.getMostProfitableItems(monthResults);
        return new TableComponent()
                .withResults(results)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build();
    }

    protected Table buildExpensesTable() {
        List<DataDto> monthResults = dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList());
        List<ResultDto> results = calculationFacade.getMostExpensiveItems(monthResults);
        return new TableComponent()
                .withResults(results)
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