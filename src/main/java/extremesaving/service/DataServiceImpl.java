package extremesaving.service;

import extremesaving.dao.DataDao;
import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class DataServiceImpl implements DataService {

    private DataDao dataDao;
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

    private Integer getResult(Map<Integer, ResultDto> results, boolean reverse) {
        Integer highestMonth = null;
        ResultDto highestResultDto = null;

        Map<Integer, ResultDto> monthResults = results;
        for (Map.Entry<Integer, ResultDto> resultEntry : monthResults.entrySet()) {

            if (highestMonth == null || (!reverse && resultEntry.getValue().getResult().compareTo(highestResultDto.getResult()) > 0) || (reverse && resultEntry.getValue().getResult().compareTo(highestResultDto.getResult()) < 0)) {
                highestMonth = resultEntry.getKey();
                highestResultDto = resultEntry.getValue();
            }
        }
        return highestMonth;
    }

    @Override
    public Date getBestMonth() {
        Map<Integer, ResultDto> monthResults = getMonthlyResults(findAll());
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
        Map<Integer, ResultDto> monthResults = getMonthlyResults(findAll());
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
        Map<Integer, ResultDto> yearlyResults = getYearlyResults(findAll());
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
        Map<Integer, ResultDto> yearlyResults = getYearlyResults(findAll());
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
    public List<CategoryDto> getMostProfitableItems(Collection<DataModel> dataModels) {
        return null;
    }

    @Override
    public List<CategoryDto> getMostExpensiveItems(Collection<DataModel> dataModels) {
        return null;
    }

    @Override
    public Map<Integer, ResultDto> getMonthlyResults(Collection<DataModel> dataModels) {
        Map<Integer, ResultDto> monthlyResults = new HashMap<>();
        monthlyResults.put(Calendar.JANUARY, new ResultDto());
        monthlyResults.put(Calendar.FEBRUARY, new ResultDto());
        monthlyResults.put(Calendar.MARCH, new ResultDto());
        monthlyResults.put(Calendar.APRIL, new ResultDto());
        monthlyResults.put(Calendar.MAY, new ResultDto());
        monthlyResults.put(Calendar.JUNE, new ResultDto());
        monthlyResults.put(Calendar.JULY, new ResultDto());
        monthlyResults.put(Calendar.AUGUST, new ResultDto());
        monthlyResults.put(Calendar.SEPTEMBER, new ResultDto());
        monthlyResults.put(Calendar.OCTOBER, new ResultDto());
        monthlyResults.put(Calendar.NOVEMBER, new ResultDto());
        monthlyResults.put(Calendar.DECEMBER, new ResultDto());

        List<DataModel> filteredDataModels = dataModels.stream()
                .filter(dataModel -> !dataModel.isTransfer())
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .collect(Collectors.toList());

        for (DataModel dataModel : filteredDataModels) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataModel.getDate());

            ResultDto resultDtoForThisMonth = monthlyResults.get(cal.get(Calendar.MONTH));
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
    public Map<Integer, ResultDto> getYearlyResults(Collection<DataModel> dataModels) {
        Calendar calendar = Calendar.getInstance();
        Map<Integer, ResultDto> yearlyResults = new HashMap<>();

        List<DataModel> filteredDataModels = dataModels.stream()
                .filter(dataModel -> !dataModel.isTransfer())
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .collect(Collectors.toList());

        for (DataModel dataModel : filteredDataModels) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataModel.getDate());

            ResultDto resultDtoForThisYear = yearlyResults.get(cal.get(Calendar.YEAR));
            if (resultDtoForThisYear == null) {
                resultDtoForThisYear = new ResultDto();
                yearlyResults.put(cal.get(Calendar.YEAR), resultDtoForThisYear);
            }
            resultDtoForThisYear.setResult(resultDtoForThisYear.getResult().add(dataModel.getValue()));

            if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
                resultDtoForThisYear.setExpenses(resultDtoForThisYear.getExpenses().add(dataModel.getValue()));
            } else {
                resultDtoForThisYear.setIncomes(resultDtoForThisYear.getIncomes().add(dataModel.getValue()));
            }
        }
        return yearlyResults;
    }

    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}