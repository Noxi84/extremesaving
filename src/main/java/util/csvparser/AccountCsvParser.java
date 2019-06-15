package util.csvparser;

import constant.FinanceConstants;
import util.csvparser.data.AccountCsv;

public class AccountCsvParser extends AbstractCsvParser<AccountCsv> {

    @Override
    protected String getCsvFile() {
        return FinanceConstants.ACCOUNT_CSV;
    }

    @Override
    protected String getCsvSplitBy() {
        return FinanceConstants.CSV_SPLIT_BY;
    }

    @Override
    protected AccountCsv handeLines(String[] lineSplit) {
        AccountCsv csv = new AccountCsv();
        String id = lineSplit[0];
        String name = lineSplit[2];

        csv.setId(id);
        csv.setName(name);

        return csv;
    }
}