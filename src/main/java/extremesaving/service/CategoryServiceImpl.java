package extremesaving.service;

import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements CategoryService {

    private CalculationService calculationService;
    private DataService dataService;

    @Override
    public List<CategoryDto> getCategories(Collection<DataModel> dataModels) {
        List<String> categories = new ArrayList<>(dataModels.stream().map(dataModel -> dataModel.getCategory()).collect(Collectors.toSet()));

        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (String category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category);
            categoryDto.setTotalResults(calculationService.getResults(dataModels.stream().filter(dataModel -> dataModel.getCategory().equals(category)).collect(Collectors.toList())));
            categoryDtos.add(categoryDto);
        }

        Collections.sort(categoryDtos, (o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()));
        return categoryDtos;
    }

    @Override
    public List<CategoryDto> getMostProfitableCategories(Collection<DataModel> dataModels) {
        List<String> categories = new ArrayList<>(dataModels.stream().map(dataModel -> dataModel.getCategory()).collect(Collectors.toSet()));

        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (String category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category);
            categoryDto.setTotalResults(calculationService.getResults(dataModels.stream().filter(dataModel -> dataModel.getCategory().equals(category)).collect(Collectors.toList())));
            if (NumberUtils.isIncome(categoryDto.getTotalResults().getResult())) {
                categoryDtos.add(categoryDto);
            }
        }

        Collections.sort(categoryDtos, (o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()));
        return categoryDtos;
    }

    @Override
    public List<CategoryDto> getMostExpensiveCategories(Collection<DataModel> dataModels) {
        List<String> categories = new ArrayList<>(dataModels.stream().map(dataModel -> dataModel.getCategory()).collect(Collectors.toSet()));

        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (String category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category);
            categoryDto.setTotalResults(calculationService.getResults(dataModels.stream().filter(dataModel -> dataModel.getCategory().equals(category)).collect(Collectors.toList())));
            if (NumberUtils.isExpense(categoryDto.getTotalResults().getResult())) {
                categoryDtos.add(categoryDto);
            }
        }
        Collections.sort(categoryDtos, Comparator.comparing(o -> o.getTotalResults().getResult()));
        return categoryDtos;
    }

    @Override
    public BigDecimal calculateSavings(String categoryName, BigDecimal percentage, long numberOfDays) {
        Set<DataModel> categoryData = dataService.findAll().stream()
                .filter(dataModel -> dataModel.getCategory().equals(categoryName))
                .collect(Collectors.toSet());
        ResultDto resultDto = calculationService.getResults(categoryData);
        BigDecimal averageDailyResult = resultDto.getAverageDailyResult();
        BigDecimal averageDailyPredictionResult = averageDailyResult.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN);
        BigDecimal predictionResult = averageDailyPredictionResult.multiply(BigDecimal.valueOf(numberOfDays));
        if (predictionResult.compareTo(BigDecimal.ZERO) < 0) {
            return predictionResult.multiply(BigDecimal.valueOf(-1));
        }
        return predictionResult;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}