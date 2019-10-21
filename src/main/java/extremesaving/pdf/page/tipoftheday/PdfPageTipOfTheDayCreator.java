package extremesaving.pdf.page.tipoftheday;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.AccountFacade;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.calculation.facade.EstimationFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.page.PdfPageCreator;
import extremesaving.pdf.page.tipoftheday.section.AccountsPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.GoalAndAwardsPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.GoalLineChartPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.MonthBarChartPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.StatisticsPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.TipOfTheDayPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.YearLineChartPdfSectionCreator;
import extremesaving.pdf.util.PdfUtils;

import java.math.BigDecimal;
import java.util.List;

public class PdfPageTipOfTheDayCreator implements PdfPageCreator {

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;
    private EstimationFacade estimationFacade;
    private AccountFacade accountFacade;

    @Override
    public void generate(Document document) {
        List<DataDto> dataDtos = dataFacade.findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);

        Table table = new Table(2);
        table.setWidth(UnitValue.createPercentValue(100));

        BigDecimal previousGoal = estimationFacade.getPreviousGoal();
        BigDecimal currentGoal = estimationFacade.getCurrentGoal();
        table.addCell(new GoalAndAwardsPdfSectionCreator()
                .withResultDto(resultDto)
                .withPreviousGoal(previousGoal)
                .withPreviousGoalReachDate(estimationFacade.getGoalReachedDate(previousGoal))
                .withCurrentGoal(currentGoal)
                .withGoalIndex(estimationFacade.getGoalIndex(currentGoal))
                .withGoalTime(estimationFacade.getGoalTime(currentGoal))
                .withSurvivalDays(estimationFacade.getSurvivalDays())
                .build()
                .getCell());
        table.addCell(new StatisticsPdfSectionCreator()
                .withLastItemAdded(calculationFacade.getLastItemAdded())
                .withBestMonth(calculationFacade.getBestMonth())
                .withBestYear(calculationFacade.getBestYear())
                .withWorstMonth(calculationFacade.getWorstMonth())
                .withWorstYear(calculationFacade.getWorstYear())
                .build()
                .getBalanceCell());
        document.add(table);

        document.add(new GoalLineChartPdfSectionCreator().build().getChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));

        Table table2 = new Table(2);
        table2.setWidth(UnitValue.createPercentValue(100));
        table2.addCell(new AccountsPdfSectionCreator().withAccounts(accountFacade.getAccounts()).withTotalBalance(calculationFacade.getTotalBalance()).build().getAccountsCell());
        table2.addCell(new TipOfTheDayPdfSectionCreator().withMessage(dataFacade.getTipOfTheDay()).build().getChartCell());
        document.add(table2);

        document.add(new MonthBarChartPdfSectionCreator().build().getChartImage());
        document.add(new YearLineChartPdfSectionCreator().build().getChartImage());
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setEstimationFacade(EstimationFacade estimationFacade) {
        this.estimationFacade = estimationFacade;
    }

    public void setAccountFacade(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }
}