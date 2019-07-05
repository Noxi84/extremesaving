package extremesaving.service.pdf;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.model.DataModel;
import extremesaving.service.DataService;
import extremesaving.service.pdf.enums.PdfGridTypeEnum;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PdfPageItemGridGenerator implements PdfPageGenerator {

    private DataService dataService;

    @Override
    public void generate(Document document) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(getItemsSection(document, PdfGridTypeEnum.PROFITS));
        document.add(getItemsSection(document, PdfGridTypeEnum.EXPENSES));
    }

    private Table getItemsSection(Document document, PdfGridTypeEnum pdfGridTypeEnum) {
        String title = "";
        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            title = "Most profitable items";
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            title = "Most expensive items";
        }

        Paragraph summaryTitle = new Paragraph(title);
        summaryTitle.setBold();
        document.add(summaryTitle);

        List<DataModel> overallResults = new ArrayList<>();
        List<DataModel> yearResults = new ArrayList<>();
        List<DataModel> monthResults = new ArrayList<>();

        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            overallResults = dataService.getMostProfitableItems(dataService.findAll());
            yearResults = dataService.getMostProfitableItems(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            monthResults = dataService.getMostProfitableItems(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            overallResults = dataService.getMostExpensiveItems(dataService.findAll());
            yearResults = dataService.getMostExpensiveItems(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            monthResults = dataService.getMostExpensiveItems(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        }

        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getItemCell("Overall", overallResults));
        table.addCell(getItemCell("This year", yearResults));
        table.addCell(getItemCell("This month", monthResults));
        return table;
    }

    private Cell getItemCell(String title, List<DataModel> results) {
        Cell cell1 = new Cell();
        cell1.setWidth(UnitValue.createPercentValue(33));
        cell1.setBorder(Border.NO_BORDER);
        Paragraph cell1Title = getItemParagraph(title);
        cell1Title.setBold();
        cell1.add(cell1Title);

        Table alignmentTable1 = new Table(2);
        Cell alignmentTableLeft1 = new Cell();
        alignmentTableLeft1.setBorder(Border.NO_BORDER);
        alignmentTableLeft1.setWidth(300);

        Cell alignmentTableRight1 = new Cell();
        alignmentTableRight1.setBorder(Border.NO_BORDER);
        alignmentTableRight1.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight1.setWidth(100);

        int maxCount = 16;
        int counter = 0;
        for (DataModel dataModel : results) {
            counter++;
            if (counter >= maxCount) {
                break;
            }
            alignmentTableLeft1.add(getItemParagraph(DateUtils.formatDate(dataModel.getDate()) + " " + StringUtils.abbreviate(dataModel.getDescription(), 33)));
            alignmentTableRight1.add(getItemParagraph(NumberUtils.formatNumber(dataModel.getValue())));
        }

        alignmentTable1.addCell(alignmentTableLeft1);
        alignmentTable1.addCell(alignmentTableRight1);

        cell1.add(alignmentTable1);

        return cell1;
    }

    private Paragraph getItemParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(9);
        return paragraph;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}