package extremesaving.service;

import extremesaving.dao.DataDao;
import extremesaving.dao.TipOfTheDayDao;
import extremesaving.dto.MiniResultDto;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.model.TipOfTheDayModel;
import extremesaving.util.DateUtils;
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

public class DataServiceImpl implements DataService {

    private DataDao dataDao;
    private TipOfTheDayDao tipOfTheDayDao;
    private CalculationService calculationService;

    @Override
    public List<DataModel> findAll() {
        return dataDao.findAll();
    }

    @Override
    public Date getLastItemAdded() {
        List<DataModel> dataModels = dataDao.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);
        return resultDto.getLastDate();
    }

    @Override
    public long getTotalItems() {
        List<DataModel> dataModels = dataDao.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);
        return resultDto.getNumberOfItems();
    }

    @Override
    public BigDecimal getTotalBalance() {
        List<DataModel> dataModels = dataDao.findAll();
        ResultDto resultDto = calculationService.getResults(dataModels);
        return resultDto.getResult();
    }

    private Integer getResult(Map<Integer, MiniResultDto> results, boolean reverse) {
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
    public List<ResultDto> getMostProfitableItems(Collection<DataModel> dataModels) {
        List<ResultDto> categoryDescriptionGrouped = createCategoryDescriptionMap(dataModels);
        return categoryDescriptionGrouped.stream()
                .filter(resultDto -> BigDecimal.ZERO.compareTo(resultDto.getResult()) < 0)
                .sorted((o1, o2) -> o2.getResult().compareTo(o1.getResult()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultDto> getMostExpensiveItems(Collection<DataModel> dataModels) {
        List<ResultDto> categoryDescriptionGrouped = createCategoryDescriptionMap(dataModels);
        return categoryDescriptionGrouped.stream()
                .filter(resultDto -> BigDecimal.ZERO.compareTo(resultDto.getResult()) > 0)
                .sorted(Comparator.comparing(ResultDto::getResult))
                .collect(Collectors.toList());
    }

    private List<ResultDto> createCategoryDescriptionMap(Collection<DataModel> filteredDataModels) {
        // Group datamodels for each category + description
        Map<String, List<DataModel>> categoryDescriptionModels = new HashMap<>();
        for (DataModel dataModel : filteredDataModels) {
            if (StringUtils.isNotBlank(dataModel.getDescription())) {
                String categoryDescription = dataModel.getCategory() + "_" + dataModel.getDescription();
                List<DataModel> dataModelsForCategoryDescription = categoryDescriptionModels.get(categoryDescription);
                if (dataModelsForCategoryDescription == null) {
                    dataModelsForCategoryDescription = new ArrayList<>();
                }
                dataModelsForCategoryDescription.add(dataModel);
                categoryDescriptionModels.put(categoryDescription, dataModelsForCategoryDescription);
            }
        }

        // Create ResultDto map
        List<ResultDto> results = new ArrayList<>();
        for (Map.Entry<String, List<DataModel>> categoryDescriptionEntry : categoryDescriptionModels.entrySet()) {
            results.add(calculationService.getResults(categoryDescriptionEntry.getValue()));
        }
        return results;
    }

    @Override
    public Map<Integer, MiniResultDto> getMonthlyResults(Collection<DataModel> dataModels) {
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

        List<DataModel> filteredDataModels = dataModels.stream()
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .collect(Collectors.toList());

        for (DataModel dataModel : filteredDataModels) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataModel.getDate());

            MiniResultDto resultDtoForThisMonth = monthlyResults.get(cal.get(Calendar.MONTH));
            resultDtoForThisMonth.setResult(resultDtoForThisMonth.getResult().add(dataModel.getValue()));

            if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
                resultDtoForThisMonth.setExpenses(resultDtoForThisMonth.getExpenses().add(dataModel.getValue()));
            } else {
                resultDtoForThisMonth.setIncomes(resultDtoForThisMonth.getIncomes().add(dataModel.getValue()));
            }
        }

        return monthlyResults;
    }

    @Override
    public Map<Integer, MiniResultDto> getYearlyResults(Collection<DataModel> dataModels) {
        Map<Integer, MiniResultDto> yearlyResults = new HashMap<>();

        for (DataModel dataModel : dataModels) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataModel.getDate());
            int year = cal.get(Calendar.YEAR);

            MiniResultDto resultDtoForThisYear = yearlyResults.get(year);
            if (resultDtoForThisYear == null) {
                resultDtoForThisYear = new MiniResultDto();
            }
            resultDtoForThisYear.setResult(resultDtoForThisYear.getResult().add(dataModel.getValue()));

            if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
                resultDtoForThisYear.setExpenses(resultDtoForThisYear.getExpenses().add(dataModel.getValue()));
            } else {
                resultDtoForThisYear.setIncomes(resultDtoForThisYear.getIncomes().add(dataModel.getValue()));
            }
            yearlyResults.put(year, resultDtoForThisYear);
        }
        return yearlyResults;
    }

    @Override
    public List<TipOfTheDayModel> getTipOfTheDays() {
        return tipOfTheDayDao.findAll();
    }

    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public void setTipOfTheDayDao(TipOfTheDayDao tipOfTheDayDao) {
        this.tipOfTheDayDao = tipOfTheDayDao;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}