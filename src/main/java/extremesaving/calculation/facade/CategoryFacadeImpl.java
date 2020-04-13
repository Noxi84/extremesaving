package extremesaving.calculation.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import extremesaving.calculation.dto.CategoryDto;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.data.dto.DataDto;

public class CategoryFacadeImpl implements CategoryFacade {

    private CalculationFacade calculationFacade;

    @Override
    public List<CategoryDto> getCategories(Collection<DataDto> dataDtos) {
        List<String> categories = new ArrayList<>(dataDtos.stream().map(dataDto -> dataDto.getCategory()).collect(Collectors.toSet()));
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (String category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category);
            categoryDto.setTotalResults(calculationFacade.getResults(dataDtos.stream().filter(dataDto -> dataDto.getCategory().equals(category)).collect(Collectors.toList())));
            categoryDtos.add(categoryDto);
        }
        Collections.sort(categoryDtos, (o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()));
        CategoryDto total = new CategoryDto();
        total.setName("Total");
        total.setTotalResults(calculationFacade.getResults(dataDtos));
        categoryDtos.add(total);
        return categoryDtos;
    }

    @Override
    public List<CategoryDto> getMostProfitableCategories(Collection<DataDto> dataDtos) {
        List<String> categories = new ArrayList<>(dataDtos.stream().map(dataDto -> dataDto.getCategory()).collect(Collectors.toSet()));
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (String category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category);
            categoryDto.setTotalResults(calculationFacade.getResults(dataDtos.stream().filter(dataDto -> dataDto.getCategory().equals(category)).collect(Collectors.toList())));
            if (NumberUtils.isIncome(categoryDto.getTotalResults().getResult())) {
                categoryDtos.add(categoryDto);
            }
        }
        Collections.sort(categoryDtos, (o1, o2) -> o2.getTotalResults().getResult().compareTo(o1.getTotalResults().getResult()));
        return categoryDtos;
    }

    @Override
    public List<CategoryDto> getMostExpensiveCategories(Collection<DataDto> dataDtos) {
        List<String> categories = new ArrayList<>(dataDtos.stream().map(dataDto -> dataDto.getCategory()).collect(Collectors.toSet()));
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (String category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category);
            categoryDto.setTotalResults(calculationFacade.getResults(dataDtos.stream().filter(dataDto -> dataDto.getCategory().equals(category)).collect(Collectors.toList())));
            if (NumberUtils.isExpense(categoryDto.getTotalResults().getResult())) {
                categoryDtos.add(categoryDto);
            }
        }
        Collections.sort(categoryDtos, Comparator.comparing(o -> o.getTotalResults().getResult()));
        return categoryDtos;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }
}