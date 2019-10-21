package extremesaving.pdf.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.itemgrid.ExpensesTablePdfSectionComponent;
import extremesaving.pdf.component.itemgrid.ProfitsTablePdfSectionComponent;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ItemGridPageServiceImpl implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 39;
    private static final int TEXT_MAX_CHARACTERS = 26;

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;

    @Override
    public void generate(Document document) {
        document.add(PdfUtils.getTitleParagraph("Most profitable items", TextAlignment.LEFT));
        document.add(buildProfitsTable());
        document.add(PdfUtils.getTitleParagraph("Most expensive items", TextAlignment.LEFT));
        document.add(buildExpensesTable());
    }

    protected Table buildProfitsTable() {
        List<ResultDto> overallResults = calculationFacade.getMostProfitableItems(dataFacade.findAll());
        List<ResultDto> yearResults = calculationFacade.getMostProfitableItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        List<ResultDto> monthResults = calculationFacade.getMostProfitableItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        return new ProfitsTablePdfSectionComponent()
                .withOverallResults(overallResults)
                .withYearResults(yearResults)
                .withMonthResults(monthResults)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build()
                .getTable();
    }

    protected Table buildExpensesTable() {
        List<ResultDto> overallResults = calculationFacade.getMostExpensiveItems(dataFacade.findAll());
        List<ResultDto> yearResults = calculationFacade.getMostExpensiveItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        List<ResultDto> monthResults = calculationFacade.getMostExpensiveItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        return new ExpensesTablePdfSectionComponent()
                .withOverallResults(overallResults)
                .withYearResults(yearResults)
                .withMonthResults(monthResults)
                .withDisplayMaxItems(DISPLAY_MAX_ITEMS)
                .withDisplayMaxTextCharacters(TEXT_MAX_CHARACTERS)
                .build()
                .getTable();
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }
}