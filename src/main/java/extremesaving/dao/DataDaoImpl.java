package extremesaving.dao;

import extremesaving.model.DataModel;
import extremesaving.property.PropertiesValueHolder;
import extremesaving.property.PropertyValueEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static extremesaving.property.PropertyValueEnum.CSV_SPLIT_BY;
import static extremesaving.property.PropertyValueEnum.DATA_CSV_DATE_FORMAT1;
import static extremesaving.property.PropertyValueEnum.DATA_CSV_DATE_FORMAT2;
import static extremesaving.property.PropertyValueEnum.DATA_CSV_FOLDER;

public class DataDaoImpl implements DataDao {

    private static List<DataModel> results;

    @Override
    public List<DataModel> findAll() {
        if (results == null) {
            File f = new File(PropertiesValueHolder.getInstance().getPropValue(DATA_CSV_FOLDER));
            if (f.isFile()) {
                results = getResultFromCSV(f.getAbsolutePath());
            } else if (f.isDirectory()) {
                results = new ArrayList<>();
                for (File file : f.listFiles()) {
                    if (file.isFile()) {
                        results.addAll(getResultFromCSV(file.getAbsolutePath()));
                    }
                }
            } else if (!f.exists()) {
                System.out.println(f.getAbsolutePath() + " could not be found.");
            }
        }
        return results;
    }

    private int getColumnNumber(String csvFile, String columnName) {
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            int lineCounter = 0;
            while ((line = br.readLine()) != null) {
                lineCounter++;
                if (lineCounter == 1) {
                    String[] lineSplit = splitCsvLine(line);

                    int columnCounter = 0;
                    for (String columnHeader : lineSplit) {
                        if (columnName.equalsIgnoreCase(columnHeader)) {
                            return columnCounter;
                        }
                        columnCounter++;
                    }
                    continue;
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
        return 0;
    }

    private List<DataModel> getResultFromCSV(String csvFile) {
        BufferedReader br = null;
        String line;
        List<DataModel> dataModels = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(csvFile));

            int dateColumn = getColumnNumber(csvFile, PropertiesValueHolder.getInstance().getPropValue(PropertyValueEnum.DATA_CSV_HEADER_DATE));
            int account = getColumnNumber(csvFile, PropertiesValueHolder.getInstance().getPropValue(PropertyValueEnum.DATA_CSV_HEADER_ACCOUNT));
            int value = getColumnNumber(csvFile, PropertiesValueHolder.getInstance().getPropValue(PropertyValueEnum.DATA_CSV_HEADER_VALUE));
            int category = getColumnNumber(csvFile, PropertiesValueHolder.getInstance().getPropValue(PropertyValueEnum.DATA_CSV_HEADER_CATEGORY));
            int description = getColumnNumber(csvFile, PropertiesValueHolder.getInstance().getPropValue(PropertyValueEnum.DATA_CSV_HEADER_DESCRIPTION));

            int lineCounter = 0;
            while ((line = br.readLine()) != null) {
                if (lineCounter > 0) {
                    String[] lineSplit = splitCsvLine(line);

                    DataModel dataModel = handleLines(lineSplit, dateColumn, account, value, category, description);
                    if (dataModel != null) {
                        dataModels.add(dataModel);
                    }
                }
                lineCounter++;
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

    private DataModel handleLines(String[] lineSplit, int dateColumn, int accountColumn, int valueColumn, int categoryColumn, int descriptionColumn) {
        try {
            DataModel dataModel = new DataModel();

            String date = getQuoteSafeValue(lineSplit, dateColumn);
            String account = getQuoteSafeValue(lineSplit, accountColumn);
            String value = getQuoteSafeValue(lineSplit, valueColumn);
            String category = getQuoteSafeValue(lineSplit, categoryColumn);
            String description = getQuoteSafeValue(lineSplit, descriptionColumn);

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
                account = "...";
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
                category = "...";
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

    private String[] splitCsvLine(String line) {
        String csvSpliyBy = PropertiesValueHolder.getInstance().getPropValue(CSV_SPLIT_BY);
        String[] lineSplit = line.split(csvSpliyBy + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        return lineSplit;
    }

    private String getQuoteSafeValue(String[] lineSplit, int columnNumber) {
        String fieldValue = lineSplit.length > columnNumber ? lineSplit[columnNumber] : "";
        fieldValue = StringUtils.remove(fieldValue, "\"");
        return StringUtils.trim(fieldValue);
    }
}