package extremesaving.pdf.page;

public class SummaryReportGenerator {

//    public void addSummaryReport(TotalsDto totalsDto, Document document) throws DocumentException, IOException {
//        Font f = new Font();
//        f.setStyle(Font.BOLD);
//        f.setSize(8);
//
//        document.add(getParagraph("Summary"));
//        document.add(new Paragraph("The account with the highest current balance is xxx with a total value of xxxx EUR and the account with the lowest value is xxxx with a total value of xxx EUR.", f));
//        document.add(new Paragraph("There are " + totalsDto.getTotalResults().getNumberOfIncomes() + " income items " + "and " + totalsDto.getTotalResults().getNumberOfExpenses() + " expense items since " + new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).format(totalsDto.getTotalResults().getFirstDate()) + ". The last item was added on " + new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).format(totalsDto.getTotalResults().getLastDate()) + ".", f));
//        document.add(Image.getInstance(ExtremeSavingConstants.OVERALL_LINE_CHART_IMAGE_FILE));
//
//        document.add(getParagraph("Accounts"));
//        for (AccountDto accountDto : totalsDto.getAccountDtoList()) {
//            document.add(new Paragraph(accountDto.getAccountName() + ": € " + accountDto.getTotalResults().getResult(), f));
//        }
//
//        document.add(getParagraph("Categories"));
//
//        PdfPTable table = new PdfPTable(2);
//        table.setWidthPercentage(100);
//
//        table.addCell(new PdfPCell(new Paragraph("Profitable category", f)));
//        table.addCell(new PdfPCell(new Paragraph("Expensive category", f)));
//
//        List<CategoryDto> profitableCategories = totalsDto.getCategoryDtoList().stream().filter(categoryDto -> BigDecimal.ZERO.compareTo(categoryDto.getTotalResults().getResult()) < 0).collect(Collectors.toList());
//        Collections.sort(profitableCategories, Comparator.comparing(o -> o.getTotalResults().getResult()));
//        Collections.reverse(profitableCategories);
//        List<CategoryDto> expensiveCategories = totalsDto.getCategoryDtoList().stream().filter(categoryDto -> BigDecimal.ZERO.compareTo(categoryDto.getTotalResults().getResult()) > 0).collect(Collectors.toList());
//        Collections.sort(expensiveCategories, Comparator.comparing(o -> o.getTotalResults().getResult()));
//
//        for (int i = 0; i < Math.max(profitableCategories.size(), expensiveCategories.size()); i++) {
//            if (i < profitableCategories.size()) {
//                CategoryDto categoryDto = profitableCategories.get(i);
//                table.addCell(new PdfPCell(new Paragraph(categoryDto.getCategoryName() + ": € " + categoryDto.getNonTransferResults().getResult(), f)));
//            } else {
//                table.addCell(new PdfPCell(new Paragraph()));
//            }
//            if (i < expensiveCategories.size()) {
//                CategoryDto categoryDto = expensiveCategories.get(i);
//                table.addCell(new PdfPCell(new Paragraph(categoryDto.getCategoryName() + ": € " + categoryDto.getNonTransferResults().getResult(), f)));
//            } else {
//                table.addCell(new PdfPCell(new Paragraph()));
//            }
//        }
//        document.add(table);
//
//        Set<DataModel> nonTransferDataModels = totalsDto.getData().stream().filter(dataModel -> !dataModel.getCategory().isTransfer()).collect(Collectors.toSet());
//        List<DataModel> sortedItems = new ArrayList<>(nonTransferDataModels);
//        Collections.sort(sortedItems, Comparator.comparing(DataModel::getValue));
//
//        document.add(new Paragraph("The five most profitable items overall are ...<list>", f));
//        for (int i = 0; i < Math.min(5, sortedItems.size() - 1); i++) {
//            DataModel dataModel = sortedItems.get(sortedItems.size() - i - 1);
//
//            if (dataModel.getValue().compareTo(BigDecimal.ZERO) > 0) {
//                document.add(new Paragraph(new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).format(dataModel.getDate()) + " - " + dataModel.getCategory().getName() + " - " + dataModel.getValue() + " - " + dataModel.getDescription(), f));
//            }
//        }
//        document.add(new Paragraph("The five most expensive items overall are ...<list>", f));
//        for (int i = 0; i < Math.min(5, sortedItems.size() - 1); i++) {
//            DataModel dataModel = sortedItems.get(i);
//            if (BigDecimal.ZERO.compareTo(dataModel.getValue()) > 0) {
//                document.add(new Paragraph(new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).format(dataModel.getDate()) + " - " + dataModel.getCategory().getName() + " - " + dataModel.getValue() + " - " + dataModel.getDescription(), f));
//            }
//        }
//    }
//
//    private Paragraph getParagraph(String categories) {
//        Paragraph categoriesParagraph = new Paragraph();
//        categoriesParagraph.add(categories);
//        categoriesParagraph.setAlignment(Element.ALIGN_LEFT);
//        return categoriesParagraph;
//    }
}