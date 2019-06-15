package pdf.page;

import com.itextpdf.text.*;
import constant.ExtremeSavingConstants;
import dto.CategoryDto;
import dto.TotalsDto;
import model.DataModel;
import util.DateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class MonthReportGenerator {

    public void addMonthReport(TotalsDto totalsDto, Document document) throws DocumentException, IOException {
        Font f = new Font();
        f.setStyle(Font.BOLD);
        f.setSize(8);

        document.newPage();
        document.add(getParagraph("Month report"));
        document.add(new Paragraph("The month with highest income this year is xxx and the month highest expense this year is xxx", f));
        document.add(Image.getInstance(ExtremeSavingConstants.MONTHLY_BAR_CHART_IMAGE_FILE));
        document.add(new Paragraph("Your saving rate for " + new SimpleDateFormat("MMMM yyyy").format(new Date()) + " is:", f));
        document.add(Image.getInstance(ExtremeSavingConstants.MONTHLY_METER_CHART_IMAGE_FILE));

        document.add(getParagraph("Categories"));
        document.add(new Paragraph("This month's highest income category is xxx and the highest expense category is xxx", f));
        for (CategoryDto categoryDto : totalsDto.getCategoryDtoList()) {
            if (BigDecimal.ZERO.compareTo(categoryDto.getNonTransferResults().getResult()) != 0) {
                document.add(new Paragraph(categoryDto.getCategoryName() + ": € " + categoryDto.getNonTransferResults().getResult(), f));
            }
        }

        Set<DataModel> nonTransferDataModels = totalsDto.getData().stream().filter(dataModel -> !dataModel.getCategory().isTransfer()).filter(dataModel -> DateUtils.equalMonths(new Date(), dataModel.getDate())).collect(Collectors.toSet());
        List<DataModel> sortedItems = new ArrayList<>(nonTransferDataModels);
        Collections.sort(sortedItems, Comparator.comparing(DataModel::getValue));

        document.add(new Paragraph("The five most profitable items this month are ...", f));
        for (int i = 0; i < Math.min(5, sortedItems.size() - 1); i++) {
            DataModel dataModel = sortedItems.get(sortedItems.size() - i - 1);

            if (dataModel.getValue().compareTo(BigDecimal.ZERO) > 0) {
                document.add(new Paragraph(new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).format(dataModel.getDate()) + " - " + dataModel.getCategory().getName() + " - " + dataModel.getValue() + " - " + dataModel.getDescription(), f));
            }
        }
        document.add(new Paragraph("The five most expensive items this month are ...", f));
        for (int i = 0; i < Math.min(5, sortedItems.size() - 1); i++) {
            DataModel dataModel = sortedItems.get(i);
            if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
                document.add(new Paragraph(new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).format(dataModel.getDate()) + " - " + dataModel.getCategory().getName() + " - " + dataModel.getValue() + " - " + dataModel.getDescription(), f));
            }
        }
    }

    private Paragraph getParagraph(String categories) {
        Paragraph categoriesParagraph = new Paragraph();
        categoriesParagraph.add(categories);
        categoriesParagraph.setAlignment(Element.ALIGN_LEFT);
        return categoriesParagraph;
    }
}