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

public class PdfPageGridGenerator implements PdfPageGenerator {

    @Override
    public void generate(Document document) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(getCategorySection(document, "Most profitable categories"));
        document.add(getCategorySection(document, "Most expensive categories"));
        document.add(getItemsSection(document, "Most profitable items"));
        document.add(getItemsSection(document, "Most expensive items"));
    }

    private Table getCategorySection(Document document, String title) {
        Paragraph summaryTitle = new Paragraph(title);
        summaryTitle.setBold();
        document.add(summaryTitle);

        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getCategoryCell("Overall"));
        table.addCell(getCategoryCell("This year"));
        table.addCell(getCategoryCell("This month"));
        return table;
    }

    private Cell getCategoryCell(String title) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);

        Paragraph cellTitle = getItemParagraph(title);
        cellTitle.setBold();
        cell.add(cellTitle);

        Table alignmentTable = new Table(2);
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setWidth(300);

        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight.setWidth(100);

        alignmentTableLeft.add(getItemParagraph("Werk"));
        alignmentTableRight.add(getItemParagraph("€ 10.00"));

        alignmentTableLeft.add(getItemParagraph("Mama"));
        alignmentTableRight.add(getItemParagraph("€ 100.00"));

        alignmentTableLeft.add(getItemParagraph("Speelgoed & electronica"));
        alignmentTableRight.add(getItemParagraph("€ 1900.00"));

        alignmentTableLeft.add(getItemParagraph("2dehands"));
        alignmentTableRight.add(getItemParagraph("€ 18900.00"));

        alignmentTableLeft.add(getItemParagraph("2dehands"));
        alignmentTableRight.add(getItemParagraph("€ 18900.00"));

        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);

        cell.add(alignmentTable);

        return cell;
    }

    private Table getItemsSection(Document document, String title) {
        Paragraph summaryTitle = new Paragraph(title);
        summaryTitle.setBold();
        document.add(summaryTitle);

        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(getItemCell("Overall"));
        table.addCell(getItemCell("This year"));
        table.addCell(getItemCell("This month"));
        return table;
    }

    private Cell getItemCell(String title) {
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

        alignmentTableLeft1.add(getItemParagraph("1/1/2018 Loon"));
        alignmentTableRight1.add(getItemParagraph("€ 18 900.00"));

        alignmentTableLeft1.add(getItemParagraph("2/5/2019 Maandelijkse bijdage"));
        alignmentTableRight1.add(getItemParagraph("€ 18 900.00"));

        alignmentTableLeft1.add(getItemParagraph("9/5/2019 Bandcamp"));
        alignmentTableRight1.add(getItemParagraph("€ 18 900.00"));

        alignmentTableLeft1.add(getItemParagraph("12/7/2019 Verkoop pioneer set"));
        alignmentTableRight1.add(getItemParagraph("€ 18 900.00"));

        alignmentTableLeft1.add(getItemParagraph("..."));
        alignmentTableRight1.add(getItemParagraph("€ 1.20"));

        alignmentTable1.addCell(alignmentTableLeft1);
        alignmentTable1.addCell(alignmentTableRight1);

        cell1.add(alignmentTable1);

        return cell1;
    }

    private Paragraph getItemParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(10);
        return paragraph;
    }
}