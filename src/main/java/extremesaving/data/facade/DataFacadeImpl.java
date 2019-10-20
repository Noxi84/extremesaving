package extremesaving.data.facade;

import extremesaving.calculation.dto.MiniResultDto;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.data.dto.DataDto;
import extremesaving.data.model.DataModel;
import extremesaving.data.model.TipOfTheDayModel;
import extremesaving.data.service.DataService;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_DATE_ENABLED;
import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_DATE_RANGE;
import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_OUTLINER_ENABLED;

public class DataFacadeImpl implements DataFacade {

    private CalculationFacade calculationFacade;
    private DataService dataService;

    @Override
    public List<DataDto> findAll() {
        List<DataModel> dataModels = dataService.findAll();
        return dataModels.stream().map(dataModel -> new DataDto(dataModel)).collect(Collectors.toList());
    }

    @Override
    public Date getLastItemAdded() {
        List<DataDto> dataDtos = findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);
        return resultDto.getLastDate();
    }

    @Override
    public BigDecimal getTotalBalance() {
        List<DataDto> dataDtos = findAll();
        ResultDto resultDto = calculationFacade.getResults(dataDtos);
        return resultDto.getResult();
    }

    protected Integer getResult(Map<Integer, MiniResultDto> results, boolean reverse) {
        Integer highestMonth = null;
        MiniResultDto highestResultDto = null;

        Map<Integer, MiniResultDto> monthResults = results;
        for (Map.Entry<Integer, MiniResultDto> resultEntry : monthResults.entrySet()) {

            if (highestMonth == null || (!reverse && resultEntry.getValue().getResult().compareTo(highestResultDto.getResult()) > 0) || (reverse && resultEntry.getValue().getResult().compareTo(highestResultDto.getResult()) < 0)) {
                highestMonth = resultEntry.getKey();
                highestResultDto = resultEntry.getValue();
            }
        }
        return highestMonth;
    }

    @Override
    public Date getBestMonth() {
        Map<Integer, MiniResultDto> monthResults = getMonthlyResults(findAll());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, getResult(monthResults, false));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public Date getWorstMonth() {
        Map<Integer, MiniResultDto> monthResults = getMonthlyResults(findAll());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, getResult(monthResults, true));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public Date getBestYear() {
        Map<Integer, MiniResultDto> yearlyResults = getYearlyResults(findAll());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.YEAR, getResult(yearlyResults, false));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public Date getWorstYear() {
        Map<Integer, MiniResultDto> yearlyResults = getYearlyResults(findAll());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.YEAR, getResult(yearlyResults, true));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public List<ResultDto> getMostProfitableItems(Collection<DataDto> dataDtos) {
        List<ResultDto> categoryDescriptionGrouped = createCategoryDescriptionMap(dataDtos);
        return categoryDescriptionGrouped.stream()
                .filter(resultDto -> NumberUtils.isIncome(resultDto.getResult()))
                .sorted((o1, o2) -> o2.getResult().compareTo(o1.getResult()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultDto> getMostExpensiveItems(Collection<DataDto> dataDtos) {
        List<ResultDto> categoryDescriptionGrouped = createCategoryDescriptionMap(dataDtos);
        return categoryDescriptionGrouped.stream()
                .filter(resultDto -> NumberUtils.isExpense(resultDto.getResult()))
                .sorted(Comparator.comparing(ResultDto::getResult))
                .collect(Collectors.toList());
    }

    protected List<ResultDto> createCategoryDescriptionMap(Collection<DataDto> dataDtos) {
        // Group datamodels for each category + description
        Map<String, List<DataDto>> categoryDescriptionDtos = new HashMap<>();
        for (DataDto dataDto : dataDtos) {
            if (StringUtils.isNotBlank(dataDto.getDescription())) {
                String categoryDescription = dataDto.getCategory().toLowerCase() + "_" + dataDto.getDescription().toLowerCase();
                List<DataDto> dataDtosForCategoryDescription = categoryDescriptionDtos.get(categoryDescription);
                if (dataDtosForCategoryDescription == null) {
                    dataDtosForCategoryDescription = new ArrayList<>();
                }
                dataDtosForCategoryDescription.add(dataDto);
                categoryDescriptionDtos.put(categoryDescription, dataDtosForCategoryDescription);
            }
        }

        // Create ResultDto map
        List<ResultDto> results = new ArrayList<>();
        for (Map.Entry<String, List<DataDto>> categoryDescriptionEntry : categoryDescriptionDtos.entrySet()) {
            results.add(calculationFacade.getResults(categoryDescriptionEntry.getValue()));
        }
        return results;
    }

    @Override
    public Map<Integer, MiniResultDto> getMonthlyResults(Collection<DataDto> dataDtos) {
        Map<Integer, MiniResultDto> monthlyResults = new HashMap<>();
        monthlyResults.put(Calendar.JANUARY, new MiniResultDto());
        monthlyResults.put(Calendar.FEBRUARY, new MiniResultDto());
        monthlyResults.put(Calendar.MARCH, new MiniResultDto());
        monthlyResults.put(Calendar.APRIL, new MiniResultDto());
        monthlyResults.put(Calendar.MAY, new MiniResultDto());
        monthlyResults.put(Calendar.JUNE, new MiniResultDto());
        monthlyResults.put(Calendar.JULY, new MiniResultDto());
        monthlyResults.put(Calendar.AUGUST, new MiniResultDto());
        monthlyResults.put(Calendar.SEPTEMBER, new MiniResultDto());
        monthlyResults.put(Calendar.OCTOBER, new MiniResultDto());
        monthlyResults.put(Calendar.NOVEMBER, new MiniResultDto());
        monthlyResults.put(Calendar.DECEMBER, new MiniResultDto());

        List<DataDto> filteredDataDtos = dataDtos.stream()
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .filter(dataModel -> !dataModel.getCategory().equalsIgnoreCase("..."))
                .collect(Collectors.toList());

        for (DataDto dataDto : filteredDataDtos) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataDto.getDate());

            MiniResultDto resultDtoForThisMonth = monthlyResults.get(cal.get(Calendar.MONTH));
            resultDtoForThisMonth.setResult(resultDtoForThisMonth.getResult().add(dataDto.getValue()));

            if (NumberUtils.isExpense(dataDto.getValue())) {
                resultDtoForThisMonth.setExpenses(resultDtoForThisMonth.getExpenses().add(dataDto.getValue()));
            } else {
                resultDtoForThisMonth.setIncomes(resultDtoForThisMonth.getIncomes().add(dataDto.getValue()));
            }
        }

        return monthlyResults;
    }

    @Override
    public Map<Integer, MiniResultDto> getYearlyResults(Collection<DataDto> dataDtos) {
        Map<Integer, MiniResultDto> yearlyResults = new HashMap<>();
        List<DataDto> filteredDataDtos = dataDtos.stream().filter(dataModel -> !dataModel.getCategory().equalsIgnoreCase("...")).collect(Collectors.toList());

        for (DataDto dataDto : filteredDataDtos) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataDto.getDate());
            int year = cal.get(Calendar.YEAR);

            MiniResultDto resultDtoForThisYear = yearlyResults.get(year);
            if (resultDtoForThisYear == null) {
                resultDtoForThisYear = new MiniResultDto();
            }
            resultDtoForThisYear.setResult(resultDtoForThisYear.getResult().add(dataDto.getValue()));

            if (NumberUtils.isExpense(dataDto.getValue())) {
                resultDtoForThisYear.setExpenses(resultDtoForThisYear.getExpenses().add(dataDto.getValue()));
            } else {
                resultDtoForThisYear.setIncomes(resultDtoForThisYear.getIncomes().add(dataDto.getValue()));
            }
            yearlyResults.put(year, resultDtoForThisYear);
        }
        return yearlyResults;
    }

    @Override
    public String getTipOfTheDay() {
        List<TipOfTheDayModel> tipOfTheDayModels = dataService.findTypeOfTheDays();
        return tipOfTheDayModels.get(NumberUtils.getRandom(0, tipOfTheDayModels.size() - 1)).getText();
    }

    @Override
    public List<DataDto> removeOutliners(Collection<DataDto> dataDtos) {
        if (Boolean.TRUE.equals(PropertiesValueHolder.getBoolean(CHART_GOALS_ESTIMATION_OUTLINER_ENABLED))) {
            List<DataDto> expenses = dataService.filterOutliners(dataDtos.stream().filter(dataDto -> NumberUtils.isExpense(dataDto.getValue())).collect(Collectors.toList()));
            List<DataDto> incomes = dataService.filterOutliners(dataDtos.stream().filter(dataDto -> NumberUtils.isIncome(dataDto.getValue())).collect(Collectors.toList()));
            return dataDtos.stream().filter(dataDto -> expenses.contains(dataDto) || incomes.contains(dataDto)).collect(Collectors.toList());
        }
        return new ArrayList<>(dataDtos);
    }

    @Override
    public List<DataDto> filterEstimatedDateRange(Collection<DataDto> dataDtos) {
        if (Boolean.TRUE.equals(PropertiesValueHolder.getBoolean(CHART_GOALS_ESTIMATION_DATE_ENABLED))) {
            ResultDto resultDto = calculationFacade.getResultDto(dataDtos);
            long rangeValue = resultDto.getLastDate().getTime() - resultDto.getFirstDate().getTime();
            long pieceValue = rangeValue / 10;
            long estimationRangeValue = PropertiesValueHolder.getLong(CHART_GOALS_ESTIMATION_DATE_RANGE) * pieceValue;
            Date startDate = new Date(resultDto.getLastDate().getTime() - estimationRangeValue);
            return dataDtos.stream().filter(dataDto -> dataDto.getDate().after(startDate)).collect(Collectors.toList());
        }
        return new ArrayList<>(dataDtos);
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }
}