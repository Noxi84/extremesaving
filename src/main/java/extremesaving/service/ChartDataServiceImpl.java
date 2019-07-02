package extremesaving.service;

import extremesaving.dao.DataDao;
import extremesaving.dto.ResultDto;
import extremesaving.model.DataModel;
import extremesaving.util.DateUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ChartDataServiceImpl implements ChartDataService {

    private DataDao dataDao;
    private CalculationService calculationService;

    @Override
    public Map<Integer, ResultDto> getMonthlyResults() {
        List<DataModel> dataModels = dataDao.findAll().stream()
                .filter(dataModel -> !dataModel.isTransfer())
                .filter(dataModel -> DateUtils.equalYears(dataModel.getDate(), new Date()))
                .collect(Collectors.toList());

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

        for (DataModel dataModel : dataModels) {
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

    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public void setCalculationService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
}
