package extremesaving.pdf.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.AccountFacade;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.calculation.facade.EstimationFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.component.tipoftheday.AccountsPdfSectionComponent;
import extremesaving.pdf.component.tipoftheday.GoalAndAwardsPdfSectionComponent;
import extremesaving.pdf.component.tipoftheday.GoalLineChartPdfSectionComponent;
import extremesaving.pdf.component.tipoftheday.MonthBarChartPdfSectionComponent;
import extremesaving.pdf.component.tipoftheday.StatisticsPdfSectionComponent;
import extremesaving.pdf.component.tipoftheday.TipOfTheDayPdfSectionComponent;
import extremesaving.pdf.component.tipoftheday.YearLineChartPdfSectionComponent;
import extremesaving.pdf.util.PdfUtils;

import java.math.BigDecimal;
import java.util.List;

public class TipOfTheDayServiceImpl implements PdfPageService {

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;
    private EstimationFacade estimationFacade;
    private AccountFacade accountFacade;

    @Override
    public void generate(Document document) {
        Table table = new Table(2);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(buildGoalAndAwardsCell());
        table.addCell(buildStatisticsCell());
        document.add(table);

        document.add(buildGoalLineChartImage());
        document.add(PdfUtils.getItemParagraph("\n"));

        Table table2 = new Table(2);
        table2.setWidth(UnitValue.createPercentValue(100));
        table2.addCell(buildAccountsCell());
        table2.addCell(buildTipOfTheDayCell());
        document.add(table2);

        document.add(buildMonthBarChartImage());
        document.add(buildYearLineChartImage());
    }

    protected Cell buildGoalAndAwardsCell() {
        List<DataDto> dataDtos = dataFacade.findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);

        BigDecimal previousGoal = estimationFacade.getPreviousGoal();
        BigDecimal currentGoal = estimationFacade.getCurrentGoal();

        return new GoalAndAwardsPdfSectionComponent()
                .withResultDto(resultDto)
                .withPreviousGoal(previousGoal)
                .withPreviousGoalReachDate(estimationFacade.getGoalReachedDate(previousGoal))
                .withCurrentGoal(currentGoal)
                .withGoalIndex(estimationFacade.getGoalIndex(currentGoal))
                .withGoalTime(estimationFacade.getGoalTime(currentGoal))
                .withSurvivalDays(estimationFacade.getSurvivalDays())
                .build()
                .getCell();
    }

    protected Cell buildStatisticsCell() {
        return new StatisticsPdfSectionComponent()
                .withLastItemAdded(calculationFacade.getLastItemAdded())
                .withBestMonth(calculationFacade.getBestMonth())
                .withBestYear(calculationFacade.getBestYear())
                .withWorstMonth(calculationFacade.getWorstMonth())
                .withWorstYear(calculationFacade.getWorstYear())
                .build()
                .getBalanceCell();
    }

    protected Image buildGoalLineChartImage() {
        return new GoalLineChartPdfSectionComponent()
                .build()
                .getChartImage();
    }

    protected Cell buildAccountsCell() {
        return new AccountsPdfSectionComponent()
                .withAccounts(accountFacade.getAccounts())
                .withTotalBalance(calculationFacade.getTotalBalance())
                .build()
                .getAccountsCell();
    }

    protected Cell buildTipOfTheDayCell() {
        return new TipOfTheDayPdfSectionComponent()
                .withMessage(dataFacade.getTipOfTheDay())
                .build()
                .getCell();
    }

    protected Image buildMonthBarChartImage() {
        return new MonthBarChartPdfSectionComponent()
                .build()
                .getChartImage();
    }

    protected Image buildYearLineChartImage() {
        return new YearLineChartPdfSectionComponent()
                .build()
                .getChartImage();
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