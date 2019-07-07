package extremesaving.dao;

import extremesaving.constant.ExtremeSavingConstants;
import extremesaving.model.TipOfTheDayModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TipOfTheDayDaoImpl implements TipOfTheDayDao {

    private static List<TipOfTheDayModel> results;

    @Override
    public List<TipOfTheDayModel> findAll() {
        if (results == null) {
            results = getResultFromCSV();
        }
        return results;
    }

    private List<TipOfTheDayModel> getResultFromCSV() {
        BufferedReader br = null;
        String line = "";
        List<TipOfTheDayModel> dataModels = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(ExtremeSavingConstants.TIPOFTHEDAY_CSV));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }

                TipOfTheDayModel dataModel = handeLines(line);
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

    private TipOfTheDayModel handeLines(String line) {
        TipOfTheDayModel dataModel = new TipOfTheDayModel();
        dataModel.setText(line);
        return dataModel;
    }
}