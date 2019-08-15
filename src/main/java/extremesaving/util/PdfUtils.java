package extremesaving.util;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import extremesaving.dto.CategoryDto;
import extremesaving.dto.ResultDto;
import extremesaving.service.pdf.enums.PdfGridTimeEnum;
import extremesaving.service.pdf.enums.PdfGridTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.JFreeChart;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public final class PdfUtils {

    private static final int DISPLAY_MAX_ITEMS = 22;
    private static final int TEXT_MAX_CHARACTERS = 10;

    private PdfUtils() {
    }

    public static void writeChartPng(JFreeChart chart, String file, int width, int height) {
        try {
            BufferedImage objBufferedImage = chart.createBufferedImage(width, height);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ImageIO.write(objBufferedImage, "png", bas);
            byte[] byteArray = bas.toByteArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(in);
            File outputfile = new File(file);
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Paragraph getItemParagraph(String text) {
        return getItemParagraph(text, false);
    }

    public static Paragraph getItemParagraph(String text, boolean bold) {
        return getItemParagraph(text, bold, null);
    }

    public static Paragraph getItemParagraph(String text, boolean bold, TextAlignment textAlignment) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFontSize(8);
        if (bold) {
            paragraph.setBold();
        }
        try {
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.COURIER);
            paragraph.setFont(regular);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (textAlignment != null) {
            paragraph.setTextAlignment(textAlignment);
        }
        return paragraph;
    }

    public static Paragraph getTitleParagraph(String text, TextAlignment textAlignment) {
        Paragraph titleParagraph = new Paragraph(text);
        titleParagraph.setBold();
        titleParagraph.setTextAlignment(textAlignment);
        try {
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.COURIER);
            titleParagraph.setFont(regular);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return titleParagraph;
    }

    public static Cell getCategoryCell(String title, List<CategoryDto> categoryDtos, PdfGridTimeEnum pdfGridTimeEnum, PdfGridTypeEnum pdfGridTypeEnum) {
        Cell cell = new Cell();

        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum) || PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            Paragraph cellTitle = PdfUtils.getItemParagraph(title);
            cellTitle.setTextAlignment(TextAlignment.CENTER);
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
        if (PdfGridTypeEnum.PROFITS.equals(pdfGridTypeEnum) || PdfGridTypeEnum.EXPENSES.equals(pdfGridTypeEnum)) {
            for (CategoryDto categoryDto : categoryDtos) {
                alignmentTableLeft.add(PdfUtils.getItemParagraph(categoryDto.getName()));
                alignmentTableRight.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(categoryDto.getTotalResults().getResult())));
            }
        }

        // Add total amount
        Paragraph totalTitle = PdfUtils.getItemParagraph("Total");
        totalTitle.setBold();
        alignmentTableLeft.add(totalTitle);
        BigDecimal totalAmount = categoryDtos.stream().map(categoryDto -> categoryDto.getTotalResults().getResult()).reduce(BigDecimal.ZERO, BigDecimal::add);
        Paragraph totalAmountParagraph = PdfUtils.getItemParagraph(NumberUtils.formatNumber(totalAmount));
        totalAmountParagraph.setBold();
        alignmentTableRight.add(totalAmountParagraph);

//        if (PdfGridTypeEnum.RESULT.equals(pdfGridTypeEnum)) {
//            addSavingRatio(alignmentTableLeft, alignmentTableRight, pdfGridTimeEnum);
//        }

        // Add left and right cell
        alignmentTable.addCell(alignmentTableLeft);
        alignmentTable.addCell(alignmentTableRight);

        cell.add(alignmentTable);

        return cell;
    }

    public static Cell getItemCell(String title, List<ResultDto> results) {
        Cell cell = new Cell();
        cell.setWidth(UnitValue.createPercentValue(33));

        Paragraph cell1Title = PdfUtils.getItemParagraph(title);
        cell1Title.setTextAlignment(TextAlignment.CENTER);
        cell1Title.setBold();
        cell.add(cell1Title);

        Table alignmentTable1 = new Table(2);
        Cell alignmentTableLeft1 = new Cell();
        alignmentTableLeft1.setBorder(Border.NO_BORDER);
        alignmentTableLeft1.setWidth(300);

        Cell alignmentTableRight1 = new Cell();
        alignmentTableRight1.setBorder(Border.NO_BORDER);
        alignmentTableRight1.setTextAlignment(TextAlignment.RIGHT);
        alignmentTableRight1.setWidth(100);

        int counter = 0;
        for (ResultDto resultDto : results) {
            counter++;
            if (counter >= DISPLAY_MAX_ITEMS) {
                break;
            }
            alignmentTableLeft1.add(PdfUtils.getItemParagraph(new SimpleDateFormat("dd/MM/yyyy").format(resultDto.getLastDate()) + " " + StringUtils.abbreviate(resultDto.getData().iterator().next().getDescription(), TEXT_MAX_CHARACTERS)));
            alignmentTableRight1.add(PdfUtils.getItemParagraph(NumberUtils.formatNumber(resultDto.getResult())));
        }

        alignmentTable1.addCell(alignmentTableLeft1);
        alignmentTable1.addCell(alignmentTableRight1);

        cell.add(alignmentTable1);

        return cell;
    }
}