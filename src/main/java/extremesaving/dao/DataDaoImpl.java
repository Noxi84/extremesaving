package extremesaving.dao;

import extremesaving.model.DataModel;
import extremesaving.util.PropertiesValueHolder;
import extremesaving.util.PropertyValueENum;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static extremesaving.util.PropertyValueENum.CSV_SPLIT_BY;
import static extremesaving.util.PropertyValueENum.DATA_CSV;
import static extremesaving.util.PropertyValueENum.DATA_CSV_DATE_FORMAT1;
import static extremesaving.util.PropertyValueENum.DATA_CSV_DATE_FORMAT2;

public class DataDaoImpl implements DataDao {

    private static List<DataModel> results;

    @Override
    public List<DataModel> findAll() {
        if (results == null) {
            results = getResultFromCSV();
        }
        return results;
    }

    private List<DataModel> getResultFromCSV() {
        BufferedReader br = null;
        String line = "";
        List<DataModel> dataModels = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(PropertiesValueHolder.getInstance().getPropValue(DATA_CSV)));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }

                String[] lineSplit = line.split(PropertiesValueHolder.getInstance().getPropValue(CSV_SPLIT_BY));
                DataModel dataModel = handeLines(lineSplit);
                dataModels.add(dataModel);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataModels;
    }

    private DataModel handeLines(String[] lineSplit) {
        try {
            DataModel dataModel = new DataModel();
            int dateColumn = Integer.valueOf(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.DATA_CSV_COLUMN_DATE));
            int accountColumn = Integer.valueOf(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.DATA_CSV_COLUMN_ACCOUNT));
            int valueColumn = Integer.valueOf(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.DATA_CSV_COLUMN_VALUE));
            int categoryColumn = Integer.valueOf(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.DATA_CSV_COLUMN_CATEGORY));
            int descriptionColumn = Integer.valueOf(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.DATA_CSV_COLUMN_DESCRIPTION));

            String date = lineSplit.length > dateColumn ? lineSplit[dateColumn] : "";
            String account = lineSplit.length > accountColumn ? lineSplit[accountColumn] : "";
            String value = lineSplit.length > valueColumn ? lineSplit[valueColumn] : "";
            String category = lineSplit.length > categoryColumn ? lineSplit[categoryColumn] : "";
            String description = lineSplit.length > descriptionColumn ? lineSplit[descriptionColumn] : "";

            Date dateResult = null;
            try {
                dateResult = new SimpleDateFormat(PropertiesValueHolder.getInstance().getPropValue(DATA_CSV_DATE_FORMAT1)).parse(date);
            } catch (ParseException e) {
                // Ignore
            }
            try {
                dateResult = new SimpleDateFormat(PropertiesValueHolder.getInstance().getPropValue(DATA_CSV_DATE_FORMAT2)).parse(date);
            } catch (ParseException e) {
                // Ignore
            }
            if (dateResult == null) {
                throw new IllegalStateException("Date " + date + " is invalid. Required format is '" + PropertiesValueHolder.getInstance().getPropValue(DATA_CSV_DATE_FORMAT1) + "'.");
            }
            dataModel.setDate(dateResult);
            if (StringUtils.isBlank(account)) {
                throw new IllegalStateException("Account is empty.");
            }
            dataModel.setAccount(account);
            try {
                BigDecimal newValue = new BigDecimal(value);
                dataModel.setValue(newValue);
            } catch (Exception ex) {
                System.out.println("Unable to parse " + value + " for date " + dateResult + " and description " + description);
                ex.printStackTrace();
                throw ex;
            }

            if (StringUtils.isBlank(category)) {
                throw new IllegalStateException("Category is empty.");
            }
            dataModel.setCategory(category);
            dataModel.setDescription(description);

            return dataModel;
        } catch (Exception ex) {
            StringBuilder line = new StringBuilder();
            for (String field : lineSplit) {
                line.append(field).append(' ');
            }

            System.out.println("Unable to process line " + line.toString());
            throw ex;
        }
    }
}