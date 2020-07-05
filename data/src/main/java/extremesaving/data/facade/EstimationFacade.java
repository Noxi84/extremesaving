package extremesaving.data.facade;

import java.util.Collection;

import extremesaving.data.dto.DataDto;
import extremesaving.data.dto.EstimationResultDto;

public interface EstimationFacade {

    EstimationResultDto getEstimationResultDto(Collection<DataDto> dataDtos);
}