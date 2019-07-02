package extremesaving.pdf.page;

import com.itextpdf.text.*;
import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.dto.CategoryDto;
import extremesaving.dto.TotalsDto;
import extremesaving.model.DataModel;
import extremesaving.util.DateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class YearReportGenerator {

    public void addYearReport(TotalsDto totalsDto, Document document) throws DocumentException, IOException {
//        Font f = new Font();
//        f.setStyle(Font.BOLD);
//        f.setSize(8);
//
//        document.newPage();
//        document.add(getParagraph("Year report"));
//        document.add(new Paragraph("Your best year is xxxx with a result of xxxx EURO and your worst year is with a result of xxxx EURO", f));
//        document.add(Image.getInstance(ExtremeSavingConstants.YEARLY_BAR_CHART_IMAGE_FILE));
//
//        document.add(getParagraph("Categories"));
//        document.add(new Paragraph("This year's highest income category is xxx and the highest expense category is xxx", f));
//        for (CategoryDto categoryDto : totalsDto.getCategoryDtoList()) {
//            if (BigDecimal.ZERO.compareTo(categoryDto.getNonTransferResults().getResult()) != 0) {
//                document.add(new Paragraph(categoryDto.getCategoryName() + ": € " + categoryDto.getNonTransferResults().getResult(), f));
//            }
//        }
//        Set<DataModel> nonTransferDataModels = totalsDto.getData().stream().filter(dataModel -> !dataModel.getCategory().isTransfer()).filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toSet());
//        List<DataModel> sortedItems = new ArrayList<>(nonTransferDataModels);
//        Collections.sort(sortedItems, Comparator.comparing(DataModel::getValue));
//
//        document.add(new Paragraph("The five most profitable items this year are ...<list>", f));
//        for (int i = 0; i < Math.min(5, sortedItems.size() - 1); i++) {
//            DataModel dataModel = sortedItems.get(sortedItems.size() - i - 1);
//
//            if (dataModel.getValue().compareTo(BigDecimal.ZERO) > 0) {
//                document.add(new Paragraph(new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).format(dataModel.getDate()) + " - " + dataModel.getCategory().getName() + " - " + dataModel.getValue() + " - " + dataModel.getDescription(), f));
//            }
//        }
//        document.add(new Paragraph("The five most expensive items this year are ...<list>", f));
//        for (int i = 0; i < Math.min(5, sortedItems.size() - 1); i++) {
//            DataModel dataModel = sortedItems.get(i);
//            if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
//                document.add(new Paragraph(new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).format(dataModel.getDate()) + " - " + dataModel.getCategory().getName() + " - " + dataModel.getValue() + " - " + dataModel.getDescription(), f));
//            }
//        }
    }

    private Paragraph getParagraph(String categories) {
        Paragraph categoriesParagraph = new Paragraph();
        categoriesParagraph.add(categories);
        categoriesParagraph.setAlignment(Element.ALIGN_LEFT);
        return categoriesParagraph;
    }
}