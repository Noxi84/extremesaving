package extremesaving.calculation.facade;

import extremesaving.calculation.dto.EstimationResultDto;
import extremesaving.data.dto.DataDto;

import java.math.BigDecimal;
import java.util.Collection;

public interface EstimationFacade {

    BigDecimal getPreviousGoal();

    BigDecimal getCurrentGoal();

    int getGoalIndex(BigDecimal goalAmount);

    /**
     * @return The next goal based on the index. Default index use should be 1 for the next upcoming goal.
     */
    BigDecimal getNextGoal(int index);

    EstimationResultDto getEstimationResultDto(Collection<DataDto> dataDtos);

    BigDecimal calculateGoalRatio();
}