package extremesaving.data.dao;

import static extremesaving.common.property.PropertyValueEnum.DATA_CSV_FOLDER;

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

import org.apache.commons.lang3.StringUtils;

import extremesaving.common.property.PropertiesValueHolder;
import extremesaving.common.property.PropertyValueEnum;
import extremesaving.data.model.DataModel;

public class DataDaoImpl implements DataDao {

    private static List<DataModel> results;

    @Override
    public List<DataModel> findAll() {
        if (results == null) {
            File f = new File(PropertiesValueHolder.getString(DATA_CSV_FOLDER));
            if (f.isFile() && f.getName().endsWith(".csv")) {
                results = getResultFromCSV(f.getAbsolutePath());
            } else if (f.isDirectory()) {
                results = new ArrayList<>();
                for (File file : f.listFiles()) {
                    if (file.isFile() && file.getName().endsWith(".csv")) {
                        results.addAll(getResultFromCSV(file.getAbsolutePath()));
                    }
                }
            } else if (!f.exists()) {
                System.out.println(f.getAbsolutePath() + " could not be found.");
            }
        }
        return results;
    }

    protected List<DataModel> getResultFromCSV(String csvFile) {
        BufferedReader br = null;
        String line;
        List<DataModel> dataModels = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(csvFile));

            int dateColumn = getColumnNumber(csvFile, PropertiesValueHolder.getString(PropertyValueEnum.DATA_CSV_HEADER_DATE));
            int value = getColumnNumber(csvFile, PropertiesValueHolder.getString(PropertyValueEnum.DATA_CSV_HEADER_VALUE));
            int category = getColumnNumber(csvFile, PropertiesValueHolder.getString(PropertyValueEnum.DATA_CSV_HEADER_CATEGORY));
            int description = getColumnNumber(csvFile, PropertiesValueHolder.getString(PropertyValueEnum.DATA_CSV_HEADER_DESCRIPTION));

            int lineCounter = 0;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlank(line) && !line.startsWith("#") && lineCounter > 0) {
                    String[] lineSplit = splitCsvLine(line);

                    DataModel dataModel = handleLines(lineSplit, dateColumn, value, category, description);
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

    protected int getColumnNumber(String csvFile, String columnName) {
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

    protected DataModel handleLines(String[] lineSplit, int dateColumn, int valueColumn, int categoryColumn, int descriptionColumn) {
        try {
            DataModel dataModel = new DataModel();

            String date = getQuoteSafeValue(lineSplit, dateColumn);
            String value = getQuoteSafeValue(lineSplit, valueColumn);
            String category = getQuoteSafeValue(lineSplit, categoryColumn);
            String description = getQuoteSafeValue(lineSplit, descriptionColumn);

            // Date
            Date dateResult = null;
            try {
                dateResult = new SimpleDateFormat("d/M/yyyy").parse(date);
            } catch (ParseException e) {
                // Ignore
            }
            if (dateResult == null) {
                throw new IllegalStateException("Date " + date + " is invalid. Required format is d/M/yyyy.");
            }
            dataModel.setDate(dateResult);

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

    protected String[] splitCsvLine(String line) {
        return line.split("," + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    protected String getQuoteSafeValue(String[] lineSplit, int columnNumber) {
        String fieldValue = lineSplit.length > columnNumber ? lineSplit[columnNumber] : "";
        fieldValue = StringUtils.remove(fieldValue, "\"");
        return StringUtils.trim(fieldValue);
    }
}