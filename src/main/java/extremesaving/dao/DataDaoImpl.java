package extremesaving.dao;

import extremesaving.model.DataModel;
import extremesaving.util.PropertiesValueHolder;

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

import static extremesaving.util.PropertyValueENum.*;

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
            String date = lineSplit[0];
            String account = lineSplit[1];
            String value = lineSplit[2];
            String category = lineSplit[3];
            String description = lineSplit.length >= 5 ? lineSplit[4] : "";

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
            dataModel.setAccount(account);
            try {
                BigDecimal newValue = new BigDecimal(value);
                dataModel.setValue(newValue);
            } catch (Exception ex) {
                System.out.println("Unable to parse " + value + " for date " + dateResult + " and description " + description);
                ex.printStackTrace();
                throw ex;
            }

            dataModel.setCategory(category);
            dataModel.setDescription(description);

            return dataModel;
        } catch (Exception ex) {
            System.out.println("Unable to process line " + lineSplit[0] + " " + lineSplit[1] + " " + lineSplit[2]);
            throw ex;
        }
    }
}