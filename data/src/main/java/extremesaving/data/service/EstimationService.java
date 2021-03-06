package extremesaving.data.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import extremesaving.data.dto.DataDto;

public interface EstimationService {

    Map<Date, BigDecimal> combineDays(Collection<DataDto> dataDtos);

    Map<Date, BigDecimal> removeOutliners(Map<Date, BigDecimal> dataMap);

    Map<Date, BigDecimal> filterEstimatedDateRange(Map<Date, BigDecimal> dataMap);
}