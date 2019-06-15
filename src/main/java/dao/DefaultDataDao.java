package dao;

import constant.FinanceConstants;
import model.AccountModel;
import model.CategoryModel;
import model.DataModel;
import util.csvparser.DataCsvParser;
import util.csvparser.data.CsvModel;
import util.csvparser.data.DataCsv;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultDataDao implements DataDao {

    private DataCsvParser dataCsvParser = new DataCsvParser();
    private AccountDao accountDao = new DefaultAccountDao();
    private CategoryDao categoryDao = new DefaultCategoryDao();

    @Override
    public List<DataModel> findAll() {
        List<CsvModel> csvModels = dataCsvParser.parseCsv();
        List<DataModel> dataModels = new ArrayList<>();
        for (CsvModel csvModel : csvModels) {
            dataModels.add(convert((DataCsv) csvModel));
        }
        return dataModels;
    }

    private DataModel convert(DataCsv dataCsv) {
        Optional<AccountModel> accountModel = accountDao.getAccountById(dataCsv.getAccount());
        if (!accountModel.isPresent()) {
            throw new IllegalStateException("Account with id " + dataCsv.getAccount() + " does not exist.");
        }
        Optional<CategoryModel> categoryModel = categoryDao.getCategoryById(dataCsv.getCategory());
        if (!categoryModel.isPresent()) {
            throw new IllegalStateException("Category with id " + dataCsv.getCategory() + " does not exist.");
        }

        SimpleDateFormat sf = new SimpleDateFormat(FinanceConstants.DATA_CSV_DATE_FORMAT);

        DataModel dataModel = new DataModel();
        try {
            dataModel.setDate(sf.parse(dataCsv.getDate()));
        } catch (ParseException e) {
            throw new IllegalStateException("Date " + dataCsv.getDate() + " is invalid. Required format is '" + FinanceConstants.DATA_CSV_DATE_FORMAT + "'.");
        }
        dataModel.setCategory(categoryModel.get());
        dataModel.setAccount(accountModel.get());
        dataModel.setValue(new BigDecimal(dataCsv.getValue()));
        dataModel.setDescription(dataCsv.getDescription());
        return dataModel;
    }

    @Override
    public List<DataModel> findByAccount(String accountId) {
        return findAll().stream().filter(dataModel -> accountId.equals(dataModel.getAccount().getId())).collect(Collectors.toList());
    }

    @Override
    public List<DataModel> findBCategory(String categoryId) {
        return findAll().stream().filter(dataModel -> categoryId.equals(dataModel.getCategory().getId())).collect(Collectors.toList());
    }

}
