package extremesaving.pdf.component.itemgrid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import extremesaving.data.dto.CategoryDto;
import extremesaving.common.util.NumberUtils;

public class MonthCategoryTableComponent extends AbstractCategoryTableComponent {

    @Override
    int getLastMonthOrYear() {
        return results.entrySet().stream()
                .filter(v -> NumberUtils.isNumber(v.getKey()))
                .map(v -> Integer.valueOf(v.getKey()))
                .mapToInt(v -> v).max()
                .orElse(Calendar.getInstance().get(Calendar.MONTH));
    }

    @Override
    String getColumnTitle(final List<CategoryDto> sortedCategories) {
        List<CategoryDto> filteredCategories = sortedCategories.stream().filter(Objects::nonNull).filter(categoryDto -> categoryDto.getTotalResults() != null).collect(Collectors.toList());
        if (filteredCategories.size() > 0) {
            return "" + new SimpleDateFormat("MMM yyyy").format(filteredCategories.get(0).getTotalResults().getLastDate());
        }
        return "";
    }
}