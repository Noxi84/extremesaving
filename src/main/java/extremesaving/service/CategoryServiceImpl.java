package extremesaving.service;

import extremesaving.dto.CategoryDto;
import extremesaving.model.DataHideEnum;
import extremesaving.model.DataModel;

import java.math.BigDecimal;
import java.util.*;
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
            categoryDto.setNonTransferResults(calculationService.getResults(dataModels.stream().filter(dataModel -> dataModel.getCategory().equals(category)).filter(dataModel -> !dataModel.getHide().get(DataHideEnum.HIDE_CATEGORYGRID_CATEGORIES)).collect(Collectors.toList())));
            categoryDtos.add(categoryDto);
        }

        Collections.sort(categoryDtos, (o1, o2) -> o2.getNonTransferResults().getResult().compareTo(o1.getNonTransferResults().getResult()));
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
            categoryDto.setNonTransferResults(calculationService.getResults(dataModels.stream().filter(dataModel -> dataModel.getCategory().equals(category)).filter(dataModel -> !dataModel.getHide().get(DataHideEnum.HIDE_CATEGORYGRID_CATEGORIES)).collect(Collectors.toList())));
            if (BigDecimal.ZERO.compareTo(categoryDto.getTotalResults().getResult()) < 0) {
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
            categoryDto.setNonTransferResults(calculationService.getResults(dataModels.stream().filter(dataModel -> dataModel.getCategory().equals(category)).filter(dataModel -> !dataModel.getHide().get(DataHideEnum.HIDE_CATEGORYGRID_CATEGORIES)).collect(Collectors.toList())));
            if (BigDecimal.ZERO.compareTo(categoryDto.getTotalResults().getResult()) > 0) {
                categoryDtos.add(categoryDto);
            }
        }
        Collections.sort(categoryDtos, Comparator.comparing(o -> o.getTotalResults().getResult()));
        return categoryDtos;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}