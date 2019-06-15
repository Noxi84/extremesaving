package util.csvparser;

import constant.FinanceConstants;
import util.csvparser.data.CsvModel;
import util.csvparser.data.DataCsv;

public class DataCsvParser extends AbstractCsvParser {

    @Override
    protected String getCsvFile() {
        return FinanceConstants.DATA_CSV;
    }

    @Override
    protected String getCsvSplitBy() {
        return FinanceConstants.CSV_SPLIT_BY;
    }

    @Override
    protected CsvModel handeLines(String[] lineSplit) {
        DataCsv csv = new DataCsv();
        String date = lineSplit[0];
        String account = lineSplit[1];
        String value = lineSplit[2];
        String category = lineSplit[3];
        String description = lineSplit.length >= 5 ? lineSplit[4] : "";

        csv.setDate(date);
        csv.setAccount(account);
        csv.setValue(value);
        csv.setCategory(category);
        csv.setDescription(description);

        return csv;
    }
}