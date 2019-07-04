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

        try {
            dataModel.setDate(new SimpleDateFormat(ExtremeSavingConstants.DATA_CSV_DATE_FORMAT).parse(date));
        } catch (ParseException e) {
            throw new IllegalStateException("Date " + date + " is invalid. Required format is '" + ExtremeSavingConstants.DATA_CSV_DATE_FORMAT + "'.");
        }

        dataModel.setAccount(account);
        dataModel.setValue((new BigDecimal(value)));
        dataModel.setCategory(category);
        dataModel.setDescription(description);

        return dataModel;
    }
}