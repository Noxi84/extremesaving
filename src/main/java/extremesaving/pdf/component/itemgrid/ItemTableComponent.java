package extremesaving.pdf.component.itemgrid;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.calculation.dto.ResultDto;
import extremesaving.data.dto.DataDto;
import extremesaving.pdf.util.PdfUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ItemTableComponent {

    private List<ResultDto> results;
    private int displayMaxItems;
    private int displayMaxTextCharacters;

    public ItemTableComponent withResults(List<ResultDto> results) {
        this.results = results;
        return this;
    }

    public ItemTableComponent withDisplayMaxItems(int displayMaxItems) {
        this.displayMaxItems = displayMaxItems;
        return this;
    }

    public ItemTableComponent withDisplayMaxTextCharacters(int displayMaxTextCharacters) {
        this.displayMaxTextCharacters = displayMaxTextCharacters;
        return this;
    }

    public Table build() {
        Table table = new Table(3);
        table.setBorder(Border.NO_BORDER);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getItemCell(results));
        return table;
    }

    protected Cell getItemCell(List<ResultDto> results) {
        Table alignmentTable = new Table(3);
        alignmentTable.setBorder(Border.NO_BORDER);
        alignmentTable.setPaddingLeft(0);
        alignmentTable.setMarginLeft(0);
        alignmentTable.setPaddingRight(0);
        alignmentTable.setMarginRight(0);
        alignmentTable.addCell(getAmountCell(results));
        alignmentTable.addCell(getDescriptionCell(results));
        alignmentTable.addCell(getOccurrencesCell(results));

        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(alignmentTable);
        return cell;
    }

    protected Cell getDescriptionCell(List<ResultDto> results) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setWidth(600);
        cell.setPaddingLeft(20);
        cell.setMarginLeft(0);
        cell.setPaddingRight(0);
        cell.setMarginRight(0);

        cell.add(PdfUtils.getItemParagraph("Description", true, TextAlignment.LEFT));
        cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.LEFT));
        int counter = 0;
        for (ResultDto resultDto : results) {
            counter++;
            if (counter >= displayMaxItems) {
                break;
            }
            DataDto dataDto = resultDto.getData().iterator().next();
            cell.add(PdfUtils.getItemParagraph(StringUtils.abbreviate(dataDto.getDescription(), displayMaxTextCharacters)));
        }
        return cell;
    }

    protected Cell getOccurrencesCell(List<ResultDto> results) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setWidth(100);
        cell.setPaddingLeft(0);
        cell.setMarginLeft(0);
        cell.setPaddingRight(0);
        cell.setMarginRight(0);

        cell.add(PdfUtils.getItemParagraph("# Items", true, TextAlignment.CENTER));
        cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.CENTER));

        int counter = 0;
        for (ResultDto resultDto : results) {
            counter++;
            if (counter >= displayMaxItems) {
                break;
            }
            cell.add(PdfUtils.getItemParagraph(StringUtils.abbreviate(String.valueOf(resultDto.getNumberOfItems()), displayMaxTextCharacters), false, TextAlignment.CENTER));
        }
        return cell;
    }

    protected Cell getAmountCell(List<ResultDto> results) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.RIGHT);
        cell.setWidth(160);
        cell.setPaddingLeft(0);
        cell.setMarginLeft(0);
        cell.setPaddingRight(0);
        cell.setMarginRight(0);

        cell.add(PdfUtils.getItemParagraph("Amount", true, TextAlignment.RIGHT));
        cell.add(PdfUtils.getItemParagraph("\n", true, TextAlignment.RIGHT));

        int counter = 0;
        for (ResultDto resultDto : results) {
            counter++;
            if (counter >= displayMaxItems) {
                break;
            }
            cell.add(PdfUtils.getItemParagraph(PdfUtils.formatNumber(resultDto.getResult())));
        }
        return cell;
    }
}