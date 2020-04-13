package extremesaving.pdf.component.itemgrid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import extremesaving.calculation.dto.CategoryDto;

public class MonthCategoryTableComponent extends AbstractCategoryTableComponent {

    @Override
    int getCurrentMonthOrYear() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    @Override
    String getTitle(final List<CategoryDto> sortedCategories) {
        return "" + new SimpleDateFormat("MMM yyyy").format(sortedCategories.get(0).getTotalResults().getLastDate());
    }
}