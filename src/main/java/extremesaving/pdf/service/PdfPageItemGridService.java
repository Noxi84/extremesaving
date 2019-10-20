package extremesaving.pdf.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.enums.PdfGridTypeEnum;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PdfPageItemGridService implements PdfPageService {

    private static final int DISPLAY_MAX_ITEMS = 39;
    private static final int TEXT_MAX_CHARACTERS = 18;

    private DataFacade dataFacade;

    @Override
    public void generate(Document document) {
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

        document.add(PdfUtils.getTitleParagraph(title, TextAlignment.LEFT));

        List<ResultDto> overallResults = new ArrayList<>();
        List<ResultDto> yearResults = new ArrayList<>();
        List<ResultDto> monthResults = new ArrayList<>();

        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            overallResults = dataFacade.getMostProfitableItems(dataFacade.findAll());
            yearResults = dataFacade.getMostProfitableItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
            monthResults = dataFacade.getMostProfitableItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            overallResults = dataFacade.getMostExpensiveItems(dataFacade.findAll());
            yearResults = dataFacade.getMostExpensiveItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
            monthResults = dataFacade.getMostExpensiveItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        }

        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getItemCell("Overall", overallResults));
        table.addCell(getItemCell("This year", yearResults));
        table.addCell(getItemCell("This month", monthResults));
        return table;
    }

    private Cell getItemCell(String title, List<ResultDto> results) {
        Cell cell = new Cell();
        cell.setWidth(UnitValue.createPercentValue(33));

        cell.add(PdfUtils.getItemParagraph(title, true, TextAlignment.CENTER));

        Table alignmentTable1 = new Table(2);
        Cell alignmentTableLeft1 = new Cell();
        alignmentTableLeft1.setBorder(Border.NO_BORDER);
        alignmentTableLeft1.setWidth(400);
        alignmentTableLeft1.setPaddingLeft(0);
        alignmentTableLeft1.setMarginLeft(0);
        alignmentTableLeft1.setPaddingRight(0);
        alignmentTableLeft1.setMarginRight(0);

        Cell alignmentTableRight1 = new Cell();
        alignmentTableRight1.setBorder(Border.NO_BORDER);
        alignmentTableRight1.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight1.setWidth(120);
        alignmentTableRight1.setPaddingLeft(0);
        alignmentTableRight1.setMarginLeft(0);
        alignmentTableRight1.setPaddingRight(0);
        alignmentTableRight1.setMarginRight(0);

        int counter = 0;
        for (ResultDto resultDto : results) {
            counter++;
            if (counter >= DISPLAY_MAX_ITEMS) {
                break;
            }
            alignmentTableLeft1.add(PdfUtils.getItemParagraph(StringUtils.abbreviate(resultDto.getData().iterator().next().getDescription(), TEXT_MAX_CHARACTERS)));
            alignmentTableRight1.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(resultDto.getResult())));
        }

        alignmentTable1.addCell(alignmentTableLeft1);
        alignmentTable1.addCell(alignmentTableRight1);

        cell.add(alignmentTable1);

        return cell;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }
}