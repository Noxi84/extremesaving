package util.csvparser;

import constant.FinanceConstants;
import util.csvparser.data.CategoryCsv;

public class CategoryCsvParser extends AbstractCsvParser<CategoryCsv> {

    @Override
    protected String getCsvFile() {
        return FinanceConstants.CATEGORY_CSV;
    }

    @Override
    protected String getCsvSplitBy() {
        return FinanceConstants.CSV_SPLIT_BY;
    }

    @Override
    protected CategoryCsv handeLines(String[] lineSplit) {
        CategoryCsv csv = new CategoryCsv();
        String id = lineSplit[0];
        String name = lineSplit[1];
        String transfer = lineSplit[2];

        csv.setId(id);
        csv.setName(name);
        csv.setTransfer(transfer);

        return csv;
    }
}