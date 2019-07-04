package extremesaving.service.pdf;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;

public class PdfPageGridGenerator implements PdfPageGenerator {

    @Override
    public void generate(Document document) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(getProfitableCategorySection(document));
        document.add(getExpensiveCategorySection((document)));
        document.add(getProfitableItemsSection((document)));
        document.add(getExpensiveItemsSection((document)));
    }

    private Table getProfitableCategorySection(Document document) {
        Paragraph summaryTitle = new Paragraph("The five most profitable categories are: ");
        summaryTitle.setBold();
        document.add(summaryTitle);

        Table table = new Table(3);

        Cell cell1 = new Cell();
        cell1.setBorder(Border.NO_BORDER);
        Paragraph cell1Title = getItemParagraph("Overall");
        cell1Title.setBold();
        cell1.add(cell1Title);
        cell1.add(getItemParagraph("Werk: € 18 900.00"));
        cell1.add(getItemParagraph("Mama: € 18 900.00"));
        cell1.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell1.add(getItemParagraph("2dehands: € 18 900.00"));
        cell1.add(getItemParagraph("..."));

        Cell cell2 = new Cell();
        cell2.setBorder(Border.NO_BORDER);
        Paragraph cell2Title = getItemParagraph("This month");
        cell2Title.setBold();
        cell2.add(cell2Title);
        cell2.add(getItemParagraph("Werk: € 18 900.00"));
        cell2.add(getItemParagraph("Mama: € 18 900.00"));
        cell2.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell2.add(getItemParagraph("2dehands: € 18 900.00"));
        cell2.add(getItemParagraph("..."));

        Cell cell3 = new Cell();
        cell3.setBorder(Border.NO_BORDER);
        Paragraph cell3Title = getItemParagraph("This year");
        cell3Title.setBold();
        cell3.add(cell3Title);
        cell3.add(getItemParagraph("Werk: € 18 900.00"));
        cell3.add(getItemParagraph("Mama: € 18 900.00"));
        cell3.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell3.add(getItemParagraph("2dehands: € 18 900.00"));
        cell3.add(getItemParagraph("..."));

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        return table;
    }

    private Table getExpensiveCategorySection(Document document) {
        Paragraph summaryTitle = new Paragraph("The five most expensive categories are: ");
        summaryTitle.setBold();
        document.add(summaryTitle);

        Table table = new Table(3);

        Cell cell1 = new Cell();
        cell1.setBorder(Border.NO_BORDER);
        Paragraph cell1Title = getItemParagraph("Overall");
        cell1Title.setBold();
        cell1.add(cell1Title);
        cell1.add(getItemParagraph("Werk: € 18 900.00"));
        cell1.add(getItemParagraph("Mama: € 18 900.00"));
        cell1.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell1.add(getItemParagraph("2dehands: € 18 900.00"));
        cell1.add(getItemParagraph("..."));

        Cell cell2 = new Cell();
        cell2.setBorder(Border.NO_BORDER);
        Paragraph cell2Title = getItemParagraph("This month");
        cell2Title.setBold();
        cell2.add(cell2Title);
        cell2.add(getItemParagraph("Werk: € 18 900.00"));
        cell2.add(getItemParagraph("Mama: € 18 900.00"));
        cell2.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell2.add(getItemParagraph("2dehands: € 18 900.00"));
        cell2.add(getItemParagraph("..."));

        Cell cell3 = new Cell();
        cell3.setBorder(Border.NO_BORDER);
        Paragraph cell3Title = getItemParagraph("This year");
        cell3Title.setBold();
        cell3.add(cell3Title);
        cell3.add(getItemParagraph("Werk: € 18 900.00"));
        cell3.add(getItemParagraph("Mama: € 18 900.00"));
        cell3.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell3.add(getItemParagraph("2dehands: € 18 900.00"));
        cell3.add(getItemParagraph("..."));

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        return table;
    }

    private Table getProfitableItemsSection(Document document) {
        Paragraph summaryTitle = new Paragraph("The five most profitable items are: ");
        summaryTitle.setBold();
        document.add(summaryTitle);

        Table table = new Table(3);

        Cell cell1 = new Cell();
        cell1.setBorder(Border.NO_BORDER);
        Paragraph cell1Title = getItemParagraph("Overall");
        cell1Title.setBold();
        cell1.add(cell1Title);
        cell1.add(getItemParagraph("Werk: Loon € 18 900.00"));
        cell1.add(getItemParagraph("Voeding: Waterbron € 18 900.00"));
        cell1.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell1.add(getItemParagraph("2dehands: Verkoop Pioneer set € 18 900.00"));
        cell1.add(getItemParagraph("..."));

        Cell cell2 = new Cell();
        cell2.setBorder(Border.NO_BORDER);
        Paragraph cell2Title = getItemParagraph("This month");
        cell2Title.setBold();
        cell2.add(cell2Title);
        cell2.add(getItemParagraph("Werk: Loon € 18 900.00"));
        cell2.add(getItemParagraph("Voeding: Waterbron € 18 900.00"));
        cell2.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell2.add(getItemParagraph("2dehands: Verkoop Pioneer set € 18 900.00"));
        cell2.add(getItemParagraph("..."));

        Cell cell3 = new Cell();
        cell3.setBorder(Border.NO_BORDER);
        Paragraph cell3Title = getItemParagraph("This year");
        cell3Title.setBold();
        cell3.add(cell3Title);
        cell3.add(getItemParagraph("Werk: Loon € 18 900.00"));
        cell3.add(getItemParagraph("Voeding: Waterbron € 18 900.00"));
        cell3.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell3.add(getItemParagraph("2dehands: Verkoop Pioneer set € 18 900.00"));
        cell3.add(getItemParagraph("..."));

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        return table;
    }

    private Table getExpensiveItemsSection(Document document) {
        Paragraph summaryTitle = new Paragraph("The five most expensive items are: ");
        summaryTitle.setBold();
        document.add(summaryTitle);

        Table table = new Table(3);

        Cell cell1 = new Cell();
        cell1.setBorder(Border.NO_BORDER);
        Paragraph cell1Title = getItemParagraph("Overall");
        cell1Title.setBold();
        cell1.add(cell1Title);
        cell1.add(getItemParagraph("Werk: € 18 900.00"));
        cell1.add(getItemParagraph("Mama: € 18 900.00"));
        cell1.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell1.add(getItemParagraph("2dehands: € 18 900.00"));
        cell1.add(getItemParagraph("..."));

        Cell cell2 = new Cell();
        cell2.setBorder(Border.NO_BORDER);
        Paragraph cell2Title = getItemParagraph("This month");
        cell2Title.setBold();
        cell2.add(cell2Title);
        cell2.add(getItemParagraph("Werk: € 18 900.00"));
        cell2.add(getItemParagraph("Mama: € 18 900.00"));
        cell2.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell2.add(getItemParagraph("2dehands: € 18 900.00"));
        cell2.add(getItemParagraph("..."));

        Cell cell3 = new Cell();
        cell3.setBorder(Border.NO_BORDER);
        Paragraph cell3Title = getItemParagraph("This year");
        cell3Title.setBold();
        cell3.add(cell3Title);
        cell3.add(getItemParagraph("Werk: € 18 900.00"));
        cell3.add(getItemParagraph("Mama: € 18 900.00"));
        cell3.add(getItemParagraph("Speelgoed & electronica: € 18 900.00"));
        cell3.add(getItemParagraph("2dehands: € 18 900.00"));
        cell3.add(getItemParagraph("..."));

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        return table;
    }

    private Paragraph getItemParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(9);
        return paragraph;
    }
}