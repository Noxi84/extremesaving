package extremesaving.pdf.component.itemgrid;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import extremesaving.pdf.component.paragraph.CategoryParagraphComponent;

public class CategoryTableComponent {

    private List<List<CategoryParagraphComponent>> columnsData = new ArrayList<>();
    private Color rowColor1;
    private Color rowColor2;

    public CategoryTableComponent withRowColor1(Color rowColor1) {
        this.rowColor1 = rowColor1;
        return this;
    }

    public CategoryTableComponent withRowColor2(Color rowColor2) {
        this.rowColor2 = rowColor2;
        return this;
    }

    public CategoryTableComponent addColumnData(List<CategoryParagraphComponent> paragraphs) {
        columnsData.add(paragraphs);
        return this;
    }

    /**
     * Build the table to be added to the PDF-page.
     *
     * @return Table
     */
    public Table build() {
        Table table = new Table(columnsData.size());
        table.setBorder(Border.NO_BORDER);
        table.setWidth(UnitValue.createPercentValue(100));

        int numberOfRows = columnsData.stream().map(paragraphs -> paragraphs.size()).mapToInt(v -> v).max().orElse(0);

        for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
            for (List<CategoryParagraphComponent> paragraphs : columnsData) {
                Cell cell = new Cell();
                cell.setBorder(Border.NO_BORDER);
                cell.setTextAlignment(TextAlignment.CENTER);

                CategoryParagraphComponent paragraph;
                if (paragraphs.size() > rowNumber) {
                    paragraph = paragraphs.get(rowNumber);

                } else {
                    paragraph = new CategoryParagraphComponent("\n", true);
                }

                if (paragraph.getCellBackgroundColor() != null) {
                    cell.setBackgroundColor(paragraph.getCellBackgroundColor());
                }

                cell.add(paragraph);
                table.addCell(cell);
            }
        }

        return table;
    }
}