package extremesaving.pdf.page;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.calculation.facade.CalculationFacade;
import extremesaving.data.facade.DataFacade;
import extremesaving.pdf.util.PdfUtils;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PdfPageItemGridCreator implements PdfPageCreator {

    private static final int DISPLAY_MAX_ITEMS = 39;
    private static final int TEXT_MAX_CHARACTERS = 26;

    private DataFacade dataFacade;
    private CalculationFacade calculationFacade;

    @Override
    public void generate(Document document) {
        document.add(PdfUtils.getTitleParagraph("Most profitable items", TextAlignment.LEFT));
        document.add(getProfitsTable());
        document.add(PdfUtils.getTitleParagraph("Most expensive items", TextAlignment.LEFT));
        document.add(getExpensesTable());
    }

    protected Table getProfitsTable() {
        List<ResultDto> overallResults = calculationFacade.getMostProfitableItems(dataFacade.findAll());
        List<ResultDto> yearResults = calculationFacade.getMostProfitableItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        List<ResultDto> monthResults = calculationFacade.getMostProfitableItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));

        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getItemCell("Overall", overallResults));
        table.addCell(getItemCell("This year", yearResults));
        table.addCell(getItemCell("This month", monthResults));
        return table;
    }

    protected Table getExpensesTable() {
        List<ResultDto> overallResults = calculationFacade.getMostExpensiveItems(dataFacade.findAll());
        List<ResultDto> yearResults = calculationFacade.getMostExpensiveItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYears(new Date(), dataDto.getDate())).collect(Collectors.toList()));
        List<ResultDto> monthResults = calculationFacade.getMostExpensiveItems(dataFacade.findAll().stream().filter(dataDto -> DateUtils.equalYearAndMonths(new Date(), dataDto.getDate())).collect(Collectors.toList()));

        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getItemCell("Overall", overallResults));
        table.addCell(getItemCell("This year", yearResults));
        table.addCell(getItemCell("This month", monthResults));
        return table;
    }

    protected Cell getItemCell(String title, List<ResultDto> results) {
        Table alignmentTable = new Table(2);
        alignmentTable.setPaddingLeft(0);
        alignmentTable.setMarginLeft(0);
        alignmentTable.setPaddingRight(0);
        alignmentTable.setMarginRight(0);
        alignmentTable.addCell(getLeftCell(results));
        alignmentTable.addCell(getRightCell(results));

        Cell cell = new Cell();
        cell.add(PdfUtils.getItemParagraph(title, true, TextAlignment.CENTER));
        cell.add(alignmentTable);
        return cell;
    }

    protected Cell getLeftCell(List<ResultDto> results) {
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setWidth(400);
        alignmentTableLeft.setPaddingLeft(0);
        alignmentTableLeft.setMarginLeft(0);
        alignmentTableLeft.setPaddingRight(0);
        alignmentTableLeft.setMarginRight(0);

        int counter = 0;
        for (ResultDto resultDto : results) {
            counter++;
            if (counter >= DISPLAY_MAX_ITEMS) {
                break;
            }
            alignmentTableLeft.add(PdfUtils.getItemParagraph(StringUtils.abbreviate(resultDto.getData().iterator().next().getDescription(), TEXT_MAX_CHARACTERS)));
        }
        return alignmentTableLeft;
    }

    protected Cell getRightCell(List<ResultDto> results) {
        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight.setWidth(130);
        alignmentTableRight.setPaddingLeft(0);
        alignmentTableRight.setMarginLeft(0);
        alignmentTableRight.setPaddingRight(0);
        alignmentTableRight.setMarginRight(0);

        int counter = 0;
        for (ResultDto resultDto : results) {
            counter++;
            if (counter >= DISPLAY_MAX_ITEMS) {
                break;
            }
            alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(resultDto.getResult())));
        }
        return alignmentTableRight;
    }

    public void setDataFacade(DataFacade dataFacade) {
        this.dataFacade = dataFacade;
    }

    public void setCalculationFacade(CalculationFacade calculationFacade) {
        this.calculationFacade = calculationFacade;
    }
}