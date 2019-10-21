package extremesaving.pdf.page.tipoftheday;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.AccountFacade;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.calculation.facade.EstimationFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.page.PdfPageCreator;
import extremesaving.pdf.page.tipoftheday.section.AccountsPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.GoalLineChartPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.MonthBarChartPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.StatisticsPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.TipOfTheDayPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.TrophyPdfSectionCreator;
import extremesaving.pdf.page.tipoftheday.section.YearLineChartPdfSectionCreator;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        table.addCell(getGoalAndAwardsCell(resultDto));
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

    protected Cell getGoalAndAwardsCell(ResultDto resultDto) {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setTextAlignment(TextAlignment.CENTER);
        chartCell.setWidth(UnitValue.createPercentValue(50));

        if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
            if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal previousGoalAmount = estimationFacade.getPreviousGoal();
                Date goalReachedDate = estimationFacade.getGoalReachedDate(previousGoalAmount);
                BigDecimal goalAmount = estimationFacade.getCurrentGoal();

                BigDecimal goalPercentageAmount = goalAmount.subtract(previousGoalAmount);
                BigDecimal currentGoalAmount = resultDto.getResult().subtract(previousGoalAmount);
                BigDecimal goalPercentage = currentGoalAmount.divide(goalPercentageAmount, 2, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100));

                chartCell.add(new TrophyPdfSectionCreator().withGoalIndex(estimationFacade.getGoalIndex(goalAmount)).build().getTrophyImage());
                chartCell.add(PdfUtils.getItemParagraph("Save " + NumberUtils.formatNumber(resultDto.getResult(), false) + " / " + NumberUtils.formatNumber(goalAmount, false) + " (" + NumberUtils.formatPercentage(goalPercentage) + ")", true));
                chartCell.add(PdfUtils.getItemParagraph("Estimated time: " + DateUtils.formatTimeLeft(estimationFacade.getGoalTime(goalAmount)), false));
                chartCell.add(PdfUtils.getItemParagraph("Previous goal " + NumberUtils.formatNumber(previousGoalAmount, false) + " reached on " + new SimpleDateFormat("d MMMM yyyy").format(goalReachedDate)));
            }
            chartCell.add(PdfUtils.getItemParagraph("Estimated survival time without incomes: " + DateUtils.formatTimeLeft(estimationFacade.getSurvivalDays()), false));
            chartCell.add(PdfUtils.getItemParagraph("\n"));
        }
        return chartCell;
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