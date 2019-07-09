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
            int lineCounter = 0;
            Integer skipLines = Integer.valueOf(PropertiesValueHolder.getInstance().getPropValue(PropertyValueENum.DATA_CSV_SKIP_LINES));
            while ((line = br.readLine()) != null) {
                lineCounter++;
                if (lineCounter <= skipLines) {
                    continue;
                }
                String csvSpliyBy = PropertiesValueHolder.getInstance().getPropValue(CSV_SPLIT_BY);
                String[] lineSplit = line.split(csvSpliyBy + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                DataModel dataModel = handeLines(lineSplit);
                if (dataModel != null) {
                    dataModels.add(dataModel);
                }
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

    private String getQuoteSafeValue(String[] lineSplit, PropertyValueENum columnNumber) {
        int column = Integer.valueOf(PropertiesValueHolder.getInstance().getPropValue(columnNumber));
        String fieldValue = lineSplit.length > column ? lineSplit[column] : "";
        fieldValue = StringUtils.remove(fieldValue, "\"");
        return StringUtils.trim(fieldValue);
    }

    private DataModel handeLines(String[] lineSplit) {
        try {
            DataModel dataModel = new DataModel();

            String date = getQuoteSafeValue(lineSplit, PropertyValueENum.DATA_CSV_COLUMN_DATE);
            String account = getQuoteSafeValue(lineSplit, PropertyValueENum.DATA_CSV_COLUMN_ACCOUNT);
            String value = getQuoteSafeValue(lineSplit, PropertyValueENum.DATA_CSV_COLUMN_VALUE);
            String category = getQuoteSafeValue(lineSplit, PropertyValueENum.DATA_CSV_COLUMN_CATEGORY);
            String description = getQuoteSafeValue(lineSplit, PropertyValueENum.DATA_CSV_COLUMN_DESCRIPTION);

            // Date
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

            // Account
            if (StringUtils.isBlank(account)) {
                throw new IllegalStateException("Account is empty.");
            }
            dataModel.setAccount(account);

            // Value
            try {
                value = StringUtils.remove(value, ",");
                BigDecimal newValue = new BigDecimal(value);
                dataModel.setValue(newValue);
            } catch (Exception ex) {
                System.out.println("Unable to parse value " + value + ".");
                ex.printStackTrace();
                throw ex;
            }

            // Category
            if (StringUtils.isBlank(category)) {
//                throw new IllegalStateException("Category is empty.");
                return null;
            }
            dataModel.setCategory(category);

            // Description
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