package extremesaving.data.service;

import extremesaving.data.dao.DataDao;
import extremesaving.data.dao.TipOfTheDayDao;
import extremesaving.data.dto.DataDto;
import extremesaving.data.model.DataModel;
import extremesaving.data.model.TipOfTheDayModel;
import extremesaving.property.PropertiesValueHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static extremesaving.property.PropertyValueEnum.CHART_GOALS_ESTIMATION_OUTLINER_RANGE;

public class DataServiceImpl implements DataService {

    private DataDao dataDao;
    private TipOfTheDayDao tipOfTheDayDao;

    @Override
    public List<DataModel> findAll() {
        List<DataModel> dataModels = dataDao.findAll();
        Collections.sort(dataModels, Comparator.comparing(DataModel::getDate));
        return dataModels;
    }

    @Override
    public List<DataDto> filterOutliners(Collection<DataDto> dataDtos) {
        int outlinerRangeValue = PropertiesValueHolder.getInteger(CHART_GOALS_ESTIMATION_OUTLINER_RANGE);
        List<DataDto> sortedDataDtos = dataDtos.stream()
                .sorted(Comparator.comparing(DataDto::getValue))
                .collect(Collectors.toList());
        if (sortedDataDtos.size() > 0) {
            int meridianIndex = sortedDataDtos.size() / 2;
            int outlineValue = (sortedDataDtos.size() - meridianIndex) / outlinerRangeValue;
            int belowOutlineIndex = outlineValue;
            int aboveOutlineIndex = sortedDataDtos.size() - outlineValue;

            BigDecimal belowOutlineValue = sortedDataDtos.get(belowOutlineIndex).getValue();
            BigDecimal aboveOutlineValue = sortedDataDtos.get(aboveOutlineIndex).getValue();

            List<DataDto> results = new ArrayList<>();
            for (DataDto dataDto : dataDtos) {
                if ((belowOutlineValue.compareTo(dataDto.getValue()) <= 0) && aboveOutlineValue.compareTo(dataDto.getValue()) >= 0) {
                    results.add(dataDto);
                } else {
                    //System.out.println("Removing outliner: " + dataModel.getDate() + " " + dataModel.getCategory() + " " + dataModel.getDescription() + " " + dataModel.getAccount() + " " + dataModel.getValue() + " (min: " + belowOutlineValue + ", max: " + aboveOutlineValue + ")");
                }
            }
            return results;
        }
        return dataDtos.stream().collect(Collectors.toList());
    }

    @Override
    public List<TipOfTheDayModel> findTypeOfTheDays() {
        return tipOfTheDayDao.findAll();
    }

    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public void setTipOfTheDayDao(TipOfTheDayDao tipOfTheDayDao) {
        this.tipOfTheDayDao = tipOfTheDayDao;
    }
}