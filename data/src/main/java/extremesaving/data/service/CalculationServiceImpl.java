package extremesaving.data.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import extremesaving.common.util.NumberUtils;
import extremesaving.data.dto.DataDto;
import extremesaving.data.dto.ResultDto;

public class CalculationServiceImpl implements CalculationService {

    @Override
    public ResultDto getResultDto(Collection<DataDto> dataDtos) {
        ResultDto resultDto = new ResultDto();
        resultDto.setData(new HashSet<>(dataDtos));
        for (DataDto dataDto : dataDtos) {
            enrichResultDto(resultDto, dataDto.getDate(), dataDto.getValue());
        }
        resultDto.setSavingRatio(getSavingRatio(resultDto));
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

    protected void enrichResultDto(ResultDto resultDto, Date date, BigDecimal value) {
        resultDto.setResult(resultDto.getResult().add(value));
        if (NumberUtils.isIncome(value)) {
            resultDto.setTotalIncomes(resultDto.getTotalIncomes().add(value));
        }
        resultDto.setNumberOfItems(resultDto.getNumberOfItems() + 1);
        if (resultDto.getFirstDate() == null || date.before(resultDto.getFirstDate())) {
            resultDto.setFirstDate(date);
        }
        if (resultDto.getLastDate() == null || date.after(resultDto.getLastDate())) {
            resultDto.setLastDate(date);
        }
    }

    protected BigDecimal getSavingRatio(ResultDto resultDto) {
        if (resultDto.getResult().compareTo(BigDecimal.ZERO) > 0) {
            return resultDto.getResult().divide(resultDto.getTotalIncomes(), RoundingMode.DOWN).multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }
}