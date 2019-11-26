package extremesaving.calculation.service;

import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.util.NumberUtils;
import extremesaving.data.dto.DataDto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

public class CalculationServiceImpl implements CalculationService {

    @Override
    public ResultDto getResultDto(Collection<DataDto> dataDtos) {
        ResultDto resultDto = new ResultDto();
        resultDto.setData(new HashSet<>(dataDtos));
        for (DataDto dataDto : dataDtos) {
            enrichResultDto(resultDto, dataDto.getDate(), dataDto.getValue());
        }
        return resultDto;
    }

    @Override
    public ResultDto getResultDto(Map<Date, BigDecimal> dataMap) {
        ResultDto resultDto = new ResultDto();
        for (Map.Entry<Date, BigDecimal> data : dataMap.entrySet()) {
            Date date = data.getKey();
            BigDecimal value = data.getValue();
            enrichResultDto(resultDto, date, value);
        }
        return resultDto;
    }

    private void enrichResultDto(ResultDto resultDto, Date date, BigDecimal value) {
        resultDto.setResult(resultDto.getResult().add(value));
        resultDto.setNumberOfItems(resultDto.getNumberOfItems() + 1);
        if (resultDto.getHighestResult().compareTo(value) > 0) {
            resultDto.setHighestResult(value);
        }
        if (NumberUtils.isExpense(value)) {
            resultDto.setNumberOfExpenses(resultDto.getNumberOfExpenses() + 1);
            resultDto.setExpenses(resultDto.getExpenses().add(value));
            if (resultDto.getHighestExpense().compareTo(value) > 0) {
                resultDto.setHighestExpense(value);
            }
        } else {
            resultDto.setNumberOfIncomes(resultDto.getNumberOfIncomes() + 1);
            resultDto.setIncomes(resultDto.getIncomes().add(value));
            if (resultDto.getHighestIncome().compareTo(value) < 0) {
                resultDto.setHighestIncome(value);
            }
        }

        if (resultDto.getFirstDate() == null || date.before(resultDto.getFirstDate())) {
            resultDto.setFirstDate(date);
        }
        if (resultDto.getLastDate() == null || date.after(resultDto.getLastDate())) {
            resultDto.setLastDate(date);
        }
    }
}