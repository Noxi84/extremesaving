package extremesaving.service;

import extremesaving.dto.ResultDto;
import extremesaving.data.model.DataModel;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;
import extremesaving.property.PropertiesValueHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_DATE_ENABLED;
import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_DATE_RANGE;
import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_OUTLINER_ENABLED;
import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_OUTLINER_RANGE;

public class CalculationServiceImpl implements CalculationService {

    private static Map<Integer, ResultDto> calculationCash = new HashMap<>();

    @Override
    public ResultDto getResults(Collection<DataModel> dataModels) {
        int hashCode = Objects.hash(dataModels.toArray());
        ResultDto result = calculationCash.get(hashCode);
        if (result == null) {
            result = getResultDto(dataModels);
            calculationCash.put(hashCode, result);
        }
        return result;
    }

    protected ResultDto getResultDto(Collection<DataModel> dataModels) {
        ResultDto resultDto = new ResultDto();
        resultDto.setData(new HashSet<>(dataModels));

        for (DataModel dataModel : dataModels) {
            resultDto.setResult(resultDto.getResult().add(dataModel.getValue()));
            resultDto.setNumberOfItems(resultDto.getNumberOfItems() + 1);
            if (resultDto.getHighestResult().compareTo(dataModel.getValue()) > 0) {
                resultDto.setHighestResult(dataModel.getValue());
            }
            if (NumberUtils.isExpense(dataModel.getValue())) {
                resultDto.setNumberOfExpenses(resultDto.getNumberOfExpenses() + 1);
                resultDto.setExpenses(resultDto.getExpenses().add(dataModel.getValue()));
                if (resultDto.getHighestExpense().compareTo(dataModel.getValue()) > 0) {
                    resultDto.setHighestExpense(dataModel.getValue());
                }
            } else {
                resultDto.setNumberOfIncomes(resultDto.getNumberOfIncomes() + 1);
                resultDto.setIncomes(resultDto.getIncomes().add(dataModel.getValue()));
                if (resultDto.getHighestIncome().compareTo(dataModel.getValue()) < 0) {
                    resultDto.setHighestIncome(dataModel.getValue());
                }
            }

            if (resultDto.getFirstDate() == null || dataModel.getDate().before(resultDto.getFirstDate())) {
                resultDto.setFirstDate(dataModel.getDate());
            }
            if (resultDto.getLastDate() == null || dataModel.getDate().after(resultDto.getLastDate())) {
                resultDto.setLastDate(dataModel.getDate());
            }
        }
        if (resultDto.getFirstDate() != null) {
            resultDto.setDaysSinceLastUpdate(DateUtils.daysBetween(new Date(), resultDto.getFirstDate()));
        }

        resultDto.setAverageDailyIncome(calculateAverageDaily(resultDto, CalculationEnum.INCOME));
        resultDto.setAverageDailyExpense(calculateAverageDaily(resultDto, CalculationEnum.EXPENSE));
        resultDto.setAverageDailyResult(calculateAverageDaily(resultDto, CalculationEnum.RESULT));

        return resultDto;
    }

    protected BigDecimal calculateAverageDaily(ResultDto resultDto, CalculationEnum calculationEnum) {
        try {
            if (resultDto.getFirstDate() != null) {
                BigDecimal amount = null;
                if (CalculationEnum.INCOME.equals(calculationEnum)) {
                    amount = resultDto.getIncomes();
                } else if (CalculationEnum.EXPENSE.equals(calculationEnum)) {
                    amount = resultDto.getExpenses();
                } else if (CalculationEnum.RESULT.equals(calculationEnum)) {
                    amount = resultDto.getResult();
                }

                long daysBetween = DateUtils.daysBetween(new Date(), resultDto.getFirstDate());
                return amount.divide(BigDecimal.valueOf(daysBetween), 2, RoundingMode.HALF_DOWN);
            }
            return BigDecimal.ZERO;
        } catch (ArithmeticException ex) {
            return null;
        }
    }

    @Override
    public List<DataModel> removeOutliners(Collection<DataModel> dataModels) {
        if (Boolean.TRUE.equals(PropertiesValueHolder.getInstance().getBoolean(CHART_GOALS_ESTIMATION_OUTLINER_ENABLED))) {
            List<DataModel> expenses = filterOutliners(dataModels.stream().filter(dataModel -> NumberUtils.isExpense(dataModel.getValue())).collect(Collectors.toList()));
            List<DataModel> incomes = filterOutliners(dataModels.stream().filter(dataModel -> NumberUtils.isIncome(dataModel.getValue())).collect(Collectors.toList()));
            return dataModels.stream().filter(dataModel -> expenses.contains(dataModel) || incomes.contains(dataModel)).collect(Collectors.toList());
        }
        return new ArrayList<>(dataModels);
    }

    private List<DataModel> filterOutliners(Collection<DataModel> dataModels) {
        int outlinerRangeValue = Integer.valueOf(PropertiesValueHolder.getInstance().getPropValue(CHART_GOALS_ESTIMATION_OUTLINER_RANGE));
        List<DataModel> sortedDataModels = dataModels.stream()
                .sorted(Comparator.comparing(DataModel::getValue))
                .collect(Collectors.toList());
        if (sortedDataModels.size() > 0) {
            int meridianIndex = sortedDataModels.size() / 2;
            int outlineValue = (sortedDataModels.size() - meridianIndex) / outlinerRangeValue;
            int belowOutlineIndex = outlineValue;
            int aboveOutlineIndex = sortedDataModels.size() - outlineValue;

            BigDecimal belowOutlineValue = sortedDataModels.get(belowOutlineIndex).getValue();
            BigDecimal aboveOutlineValue = sortedDataModels.get(aboveOutlineIndex).getValue();

            List<DataModel> results = new ArrayList<>();
            for (DataModel dataModel : dataModels) {
                if ((belowOutlineValue.compareTo(dataModel.getValue()) <= 0) && aboveOutlineValue.compareTo(dataModel.getValue()) >= 0) {
                    results.add(dataModel);
                } else {
                    //System.out.println("Removing outliner: " + dataModel.getDate() + " " + dataModel.getCategory() + " " + dataModel.getDescription() + " " + dataModel.getAccount() + " " + dataModel.getValue() + " (min: " + belowOutlineValue + ", max: " + aboveOutlineValue + ")");
                }
            }
            return results;
        }
        return dataModels.stream().collect(Collectors.toList());
    }

    @Override
    public List<DataModel> filterEstimatedDateRange(Collection<DataModel> dataModels) {
        if (Boolean.TRUE.equals(PropertiesValueHolder.getInstance().getBoolean(CHART_GOALS_ESTIMATION_DATE_ENABLED))) {
            ResultDto resultDto = getResultDto(dataModels);
            long rangeValue = resultDto.getLastDate().getTime() - resultDto.getFirstDate().getTime();
            long pieceValue = rangeValue / 10;
            long estimationRangeValue = Long.valueOf(PropertiesValueHolder.getInstance().getPropValue(CHART_GOALS_ESTIMATION_DATE_RANGE)) * pieceValue;
            Date startDate = new Date(resultDto.getLastDate().getTime() - estimationRangeValue);
            return dataModels.stream().filter(dataModel -> dataModel.getDate().after(startDate)).collect(Collectors.toList());
        }
        return new ArrayList<>(dataModels);
    }
}