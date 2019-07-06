package extremesaving.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.service.CalculationService;
import extremesaving.service.CategoryService;
import extremesaving.service.DataService;
import extremesaving.service.PredictionService;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class PdfPagePredictionsGenerator implements PdfPageGenerator {

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
            ResultDto nonTransferResultDto = calculationService.getResults(dataModels.stream().filter(dataModel -> !dataModel.isTransfer()).collect(Collectors.toList()));

            int predictionNumberOfDays = 5 * 365;
            Calendar predictionEndDate = Calendar.getInstance();
            predictionEndDate.add(Calendar.DAY_OF_MONTH, predictionNumberOfDays);
            predictionEndDate.set(Calendar.DAY_OF_MONTH, 1);
            predictionEndDate.set(Calendar.MONTH, Calendar.JANUARY);
            BigDecimal predictionAmount = predictionService.getPredictionAmount(predictionEndDate.getTime());

            Paragraph titleParagraph = new Paragraph("Tip of the day");
            titleParagraph.setBold();
            titleParagraph.setTextAlignment(TextAlignment.CENTER);
            document.add(titleParagraph);
            Paragraph tipOfTheDay = getItemParagraph("You get to be more creative!: When you have set limits on how much you can spend you’ll be more creative about how you go out and have fun.\nInstead of an expensive dinner and movie out, why not a bike ride and a picnic at the park!");
            tipOfTheDay.setTextAlignment(TextAlignment.CENTER);
            document.add(tipOfTheDay);

            List<CategoryDto> categoryDtos = categoryService.getCategories(dataModels);
            List<CategoryDto> expensiveCategoryDtos = categoryDtos.stream().filter(categoryDto -> categoryDto.getNonTransferResults().getResult().compareTo(BigDecimal.ZERO) < 0).collect(Collectors.toList());
            List<CategoryDto> profitableCategoryDtos = categoryDtos.stream().filter(categoryDto -> categoryDto.getNonTransferResults().getResult().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            CategoryDto expensiveCategoryDto = expensiveCategoryDtos.get(NumberUtils.getRandom(0, expensiveCategoryDtos.size() - 1));
            CategoryDto profitableCategoryDto = profitableCategoryDtos.get(NumberUtils.getRandom(0, profitableCategoryDtos.size() - 1));

            List<BigDecimal> categoryPercentages = Arrays.asList(BigDecimal.ONE, BigDecimal.valueOf(2), BigDecimal.valueOf(3), BigDecimal.valueOf(4), BigDecimal.valueOf(5), BigDecimal.valueOf(10), BigDecimal.valueOf(15), BigDecimal.valueOf(20), BigDecimal.valueOf(25));
            BigDecimal expensiveCategoryPercentage = categoryPercentages.get(NumberUtils.getRandom(0, categoryPercentages.size() - 1));
            BigDecimal profitableCategoryPercentage = categoryPercentages.get(NumberUtils.getRandom(0, categoryPercentages.size() - 1));

            List<Integer> years = Arrays.asList(1, 1, 2, 3, 4, 5, 10, 15, 20);
            Integer mostProfitableCategoryYears = years.get(NumberUtils.getRandom(0, years.size() - 1));
            Integer mostExpensiveCategoryYears = years.get(NumberUtils.getRandom(0, years.size() - 1));

            StringBuilder text = new StringBuilder();
            text.append("If you reduce category expenses '")
                    .append(expensiveCategoryDto.getName())
                    .append("' with ")
                    .append(NumberUtils.formatPercentage(expensiveCategoryPercentage))
                    .append(" you should save about ")
                    .append("€ 5 000.00")
                    .append(" in ")
                    .append(mostExpensiveCategoryYears)
                    .append(" years.")
                    .append("\n");
            text.append("If you increase category incomes '")
                    .append(profitableCategoryDto.getName())
                    .append("' with ")
                    .append(NumberUtils.formatPercentage(profitableCategoryPercentage))
                    .append(" you should save about ")
                    .append("€ 5 000.00 EUR")
                    .append(" in ")
                    .append(mostProfitableCategoryYears)
                    .append(" years.");

            Paragraph categoryParagraph = getItemParagraph(text.toString());
            categoryParagraph.setTextAlignment(TextAlignment.CENTER);
            document.add(categoryParagraph);
            document.add(getItemParagraph("\n"));

            Paragraph itemParagraph = getItemParagraph(new StringBuilder().append("With a current total budget of ")
                    .append(NumberUtils.formatNumber(resultDto.getResult()))
                    .append(", an average income of ")
                    .append(NumberUtils.formatNumber(nonTransferResultDto.getAverageDailyIncome()))
                    .append(" per day and, an average expense of ")
                    .append(NumberUtils.formatNumber(nonTransferResultDto.getAverageDailyExpense()))
                    .append(" per day and an inflation of 3% :").toString());
            itemParagraph.setTextAlignment(TextAlignment.CENTER);
            itemParagraph.setBold();
            document.add(itemParagraph);

            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));

            Cell chartCell1 = new Cell();
            chartCell1.setBorder(Border.NO_BORDER);
            chartCell1.setTextAlignment(TextAlignment.CENTER);
            Image monthlyBarChartImage = new Image(ImageDataFactory.create(ExtremeSavingConstants.HISTORY_LINE_CHART_IMAGE_FILE));
            monthlyBarChartImage.setWidth(380);
            monthlyBarChartImage.setHeight(300);
            chartCell1.add(getItemParagraph("You could live financially free, without any income for..."));
            chartCell1.add(getItemParagraph(DateUtils.formatSurvivalDays(predictionService.getSurvivalDays()), true));
            chartCell1.add(getItemParagraph("\n"));
            chartCell1.add(monthlyBarChartImage);

            Cell chartCell2 = new Cell();
            chartCell2.setBorder(Border.NO_BORDER);
            chartCell2.setTextAlignment(TextAlignment.CENTER);
            Image yearlyBarChartImage = new Image(ImageDataFactory.create(ExtremeSavingConstants.FUTURE_LINE_CHART_IMAGE_FILE));
            yearlyBarChartImage.setWidth(380);
            yearlyBarChartImage.setHeight(300);
            chartCell2.add(getItemParagraph("If you keep up your average daily result, you should have about..."));
            chartCell2.add(getItemParagraph(NumberUtils.formatNumber(predictionAmount) + " on " + DateUtils.formatDate(predictionEndDate.getTime()), true));
            chartCell2.add(getItemParagraph("\n"));
            chartCell2.add(yearlyBarChartImage);

            table.addCell(chartCell1);
            table.addCell(chartCell2);

            document.add(table);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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