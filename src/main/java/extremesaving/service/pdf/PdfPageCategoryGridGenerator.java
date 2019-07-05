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
import extremesaving.dto.CategoryDto;
import extremesaving.service.CategoryService;
import extremesaving.service.DataService;
import extremesaving.service.pdf.enums.PdfGridTypeEnum;
import extremesaving.util.DateUtils;
import extremesaving.util.NumberUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PdfPageCategoryGridGenerator implements PdfPageGenerator {

    private DataService dataService;
    private CategoryService categoryService;

    @Override
    public void generate(Document document) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(getCategorySection(document, PdfGridTypeEnum.PROFITS));
        document.add(getCategorySection(document, PdfGridTypeEnum.EXPENSES));
        document.add(getCategorySection(document, PdfGridTypeEnum.RESULT));
    }

    private Table getCategorySection(Document document, PdfGridTypeEnum pdfGridTypeEnum) {
        String title = "";
        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            title = "Most profitable categories";
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            title = "Most expensive categories";
        } else if (PdfGridTypeEnum.RESULT.equals(pdfGridTypeEnum)) {
            title = "Result";
        }
        Paragraph summaryTitle = new Paragraph(title);
        summaryTitle.setBold();
        document.add(summaryTitle);

        Table table = new Table(3);
        table.setWidth(UnitValue.createPercentValue(100));

        List<CategoryDto> overallResults = new ArrayList<>();
        List<CategoryDto> yearResults = new ArrayList<>();
        List<CategoryDto> monthResults = new ArrayList<>();

        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum)) {
            overallResults = categoryService.getMostProfitableCategories(dataService.findAll());
            yearResults = categoryService.getMostProfitableCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            monthResults = categoryService.getMostProfitableCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        } else if (PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            overallResults = categoryService.getMostExpensiveCategories(dataService.findAll());
            yearResults = categoryService.getMostExpensiveCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            monthResults = categoryService.getMostExpensiveCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        } else if (PdfGridTypeEnum.RESULT.equals(pdfGridTypeEnum)) {
            overallResults = categoryService.getCategories(dataService.findAll());
            yearResults = categoryService.getCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYears(new Date(), dataModel.getDate())).collect(Collectors.toList()));
            monthResults = categoryService.getCategories(dataService.findAll().stream().filter(dataModel -> DateUtils.equalYearAndMonths(new Date(), dataModel.getDate())).collect(Collectors.toList()));
        }

        table.addCell(getCategoryCell("Overall", overallResults, pdfGridTypeEnum));
        table.addCell(getCategoryCell("This year", yearResults, pdfGridTypeEnum));
        table.addCell(getCategoryCell("This month", monthResults, pdfGridTypeEnum));
        return table;
    }

    private Cell getCategoryCell(String title, List<CategoryDto> categoryDtos, PdfGridTypeEnum pdfGridEnum) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);

        if (PdfGridTypeEnum.PROFITS.equals(pdfGridEnum) || PdfGridTypeEnum.EXPENSES.equals(pdfGridEnum)) {
            Paragraph cellTitle = getItemParagraph(title);
            cellTitle.setBold();
            cell.add(cellTitle);
        }

        Table alignmentTable = new Table(2);

        // Create left cell
        Cell alignmentTableLeft = new Cell();
        alignmentTableLeft.setBorder(Border.NO_BORDER);
        alignmentTableLeft.setWidth(300);

        // Create right cell
        Cell alignmentTableRight = new Cell();
        alignmentTableRight.setBorder(Border.NO_BORDER);
        alignmentTableRight.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight.setWidth(100);

        // Add categoryDtos
        if (PdfGridTypeEnum.PROFITS.equals(pdfGridEnum) || PdfGridTypeEnum.EXPENSES.equals(pdfGridEnum)) {
            for (CategoryDto categoryDto : categoryDtos) {
                alignmentTableLeft.add(getItemParagraph(categoryDto.getName()));
                alignmentTableRight.add(getItemParagraph(NumberUtils.formatNumber(categoryDto.getNonTransferResults().getResult())));
            }
        }

        // Add total amount
        Paragraph totalTitle = getItemParagraph("Total");
        totalTitle.setBold();
        alignmentTableLeft.add(totalTitle);
        BigDecimal totalAmount = categoryDtos.stream().map(categoryDto -> categoryDto.getNonTransferResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        Paragraph totalAmountParagraph = getItemParagraph(NumberUtils.formatNumber(totalAmount));
        totalAmountParagraph.setBold();
        alignmentTableRight.add(totalAmountParagraph);

        // Add left and right cell
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);

        cell.add(alignmentTable);

        return cell;
    }

    private Paragraph getItemParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(9);
        return paragraph;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}