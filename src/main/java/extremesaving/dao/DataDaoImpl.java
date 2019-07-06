package extremesaving.dao;

import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.model.DataModel;

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

            br = new BufferedReader(new FileReader(ExtremeSavingConstants.DATA_CSV));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }

                String[] lineSplit = line.split(ExtremeSavingConstants.CSV_SPLIT_BY);
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
        DataModel dataModel = new DataModel();
        String date = lineSplit[0];
        String account = lineSplit[1];
        String value = lineSplit[2];
        String category = lineSplit[3];
        String description = lineSplit.length >= 5 ? lineSplit[4] : "";

        Date dateResult = null;
        try {
            dateResult = new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            // Ignore
        }
        try {
            dateResult = new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT2).parse(date);
        } catch (ParseException e) {
            // Ignore
        }
        if (dateResult == null) {
            throw new IllegalStateException("Date " + date + " is invalid. Required format is '" + ExtremeSavingConstants.DATA_CSV_DATE_FORMAT + "'.");
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
    }
}