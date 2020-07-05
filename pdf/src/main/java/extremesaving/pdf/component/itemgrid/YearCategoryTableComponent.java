package extremesaving.pdf.component.itemgrid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import extremesaving.common.ExtremeSavingConstants;
import extremesaving.data.dto.CategoryDto;

/**
 * Component builder containing the table with yearly category results.
 * Using .build() will return the component which can be added to the PDF-page..
 */
public class YearCategoryTableComponent extends AbstractCategoryTableComponent {

    @Override
    int getLastMonthOrYear() {
        return results.entrySet().stream()
                .filter(v -> !ExtremeSavingConstants.TOTAL_COLUMN.equals(v.getKey()))
                .map(v -> Integer.valueOf(v.getKey()))
                .mapToInt(v -> v).max()
                .orElse(Calendar.getInstance().get(Calendar.YEAR));
    }

    @Override
    String getColumnTitle(final List<CategoryDto> sortedCategories) {
        List<CategoryDto> filteredCategories = sortedCategories.stream().filter(Objects::nonNull).filter(categoryDto -> categoryDto.getTotalResults() != null).collect(Collectors.toList());
        if (filteredCategories.size() > 0) {
            return "" + new SimpleDateFormat("yyyy").format(filteredCategories.get(0).getTotalResults().getLastDate());
        }
        return "";
    }
}