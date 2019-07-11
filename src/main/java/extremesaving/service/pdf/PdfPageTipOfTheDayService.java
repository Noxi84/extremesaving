package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.service.CalculationService;
import extremesaving.service.CategoryService;
import extremesaving.service.DataService;
import extremesaving.service.PredictionService;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;
import extremesaving.util.PropertiesValueHolder;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import static extremesaving.util.PropertyValueENum.GOAL_LINE_CHART_IMAGE_FILE;
import static extremesaving.util.PropertyValueENum.HISTORY_LINE_CHART_IMAGE_FILE;

public class PdfPageTipOfTheDayService implements PdfPageService {

    private DataService dataService;
    private CalculationService calculationService;
    private PredictionService predictionService;
    private CategoryService categoryService;

    @Override
    public void generate(Document document) {
        try {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            List<DataModel> dataModels = dataService.findAll();
            ResultDto resultDto = calculationService.getResults(dataModels);

            Paragraph titleParagraph = new Paragraph("Tip of the day");
            titleParagraph.setBold();
            titleParagraph.setTextAlignment(TextAlignment.CENTER);
            document.add(titleParagraph);
            Paragraph tipOfTheDay = getItemParagraph(predictionService.getTipOfTheDay());
            tipOfTheDay.setTextAlignment(TextAlignment.CENTER);
            document.add(tipOfTheDay);

            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));
            Cell chartCell1 = getChartcell1(resultDto);
            Cell chartCell2 = getChartCell2(resultDto);
            table.addCell(chartCell1);
            table.addCell(chartCell2);

            document.add(table);

            document.add(getCategoryPredictionParagraph());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Paragraph getCategoryPredictionParagraph() {
        CategoryDto expensiveCategoryDto = predictionService.getRandomExpensiveCategory();
        CategoryDto profitableCategoryDto = predictionService.getRandomProfitCategory();

        List<BigDecimal> categoryPercentages = Arrays.asList(BigDecimal.valueOf(5), BigDecimal.valueOf(10), BigDecimal.valueOf(15), BigDecimal.valueOf(20), BigDecimal.valueOf(25));
        BigDecimal expensiveCategoryPercentage = categoryPercentages.get(NumberUtils.getRandom(0, categoryPercentages.size() - 1));
        BigDecimal profitableCategoryPercentage = categoryPercentages.get(NumberUtils.getRandom(0, categoryPercentages.size() - 1));

        List<Integer> years = Arrays.asList(1, 2, 3, 4, 5, 10, 15, 20);
        Integer mostProfitableCategoryYears = years.get(NumberUtils.getRandom(0, years.size() - 1));
        Integer mostExpensiveCategoryYears = years.get(NumberUtils.getRandom(0, years.size() - 1));

        StringBuilder text = new StringBuilder();
        text.append("Reduce expenses '")
                .append(expensiveCategoryDto.getName())
                .append("' with ")
                .append(NumberUtils.formatPercentage(expensiveCategoryPercentage))
                .append(" to save ")
                .append(NumberUtils.formatNumber(categoryService.calculateSavings(expensiveCategoryDto.getName(), expensiveCategoryPercentage, mostExpensiveCategoryYears * 365)))
                .append(" in ")
                .append(DateUtils.formatTimeLeft(Long.valueOf(mostProfitableCategoryYears) * 365))
                .append(".")
                .append("\n");
        text.append("Increase incomes '")
                .append(profitableCategoryDto.getName())
                .append("' with ")
                .append(NumberUtils.formatPercentage(profitableCategoryPercentage))
                .append(" to save ")
                .append(NumberUtils.formatNumber(categoryService.calculateSavings(profitableCategoryDto.getName(), profitableCategoryPercentage, mostProfitableCategoryYears * 365)))
                .append(" in ")
                .append(DateUtils.formatTimeLeft(Long.valueOf(mostExpensiveCategoryYears) * 365))
                .append(".");
        text.append("Get rid of repeating occurring expenses. Item 'LUMINUS' occurred 52 times in the past 5 years.");

        Paragraph categoryParagraph = getItemParagraph(text.toString());
        categoryParagraph.setTextAlignment(TextAlignment.CENTER);
        return categoryParagraph;
    }

    private Cell getChartCell2(ResultDto resultDto) throws MalformedURLException {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setTextAlignment(TextAlignment.CENTER);
        Image monthlyBarChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(HISTORY_LINE_CHART_IMAGE_FILE)));
        monthlyBarChartImage.setWidth(380);
        monthlyBarChartImage.setHeight(300);
        chartCell.add(getItemParagraph("Without income and 3% inflation you will run out of money in..."));
        chartCell.add(getItemParagraph(DateUtils.formatTimeLeft(predictionService.getSurvivalDays()), true));
        chartCell.add(getItemParagraph("Average daily expense: " + NumberUtils.formatNumber(resultDto.getAverageDailyExpense())));
        chartCell.add(getItemParagraph("\n"));
        chartCell.add(monthlyBarChartImage);
        return chartCell;
    }

    private Cell getChartcell1(ResultDto resultDto) throws MalformedURLException {
        Cell chartCell = new Cell();
        chartCell.setBorder(Border.NO_BORDER);
        chartCell.setTextAlignment(TextAlignment.CENTER);

        if (resultDto.getAverageDailyResult().compareTo(BigDecimal.ZERO) > 0) {
            // Prediction goal 1 (higher priority if possible)
            BigDecimal goalAmount = predictionService.getNextGoal();
            chartCell.add(getItemParagraph("Your next goal is: " + NumberUtils.formatNumber(resultDto.getResult(), false) + " / " + NumberUtils.formatNumber(goalAmount, false)));
            chartCell.add(getItemParagraph("Estimated time: " + DateUtils.formatTimeLeft(predictionService.getGoalTime(goalAmount)), true));
            chartCell.add(getItemParagraph("Average daily result: " + NumberUtils.formatNumber(resultDto.getAverageDailyResult())));
            chartCell.add(getItemParagraph("\n"));

            Image futureLineChartImage = new Image(ImageDataFactory.create(PropertiesValueHolder.getInstance().getPropValue(GOAL_LINE_CHART_IMAGE_FILE)));
            futureLineChartImage.setWidth(380);
            futureLineChartImage.setHeight(300);
            chartCell.add(futureLineChartImage);
        }
        return chartCell;
    }

    private Paragraph getItemParagraph(String text) {
        return getItemParagraph(text, false);
    }

    private Paragraph getItemParagraph(String text, boolean bold) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(9);
        if (bold) {
            paragraph.setBold();
        }
        return paragraph;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public void setPredictionService(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}