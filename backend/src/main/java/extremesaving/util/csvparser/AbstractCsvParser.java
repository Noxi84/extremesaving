package extremesaving.util.csvparser;

import extremesaving.util.csvparser.data.CsvModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCsvParser<T extends CsvModel> {

    protected abstract String getCsvFile();

    protected abstract String getCsvSplitBy();

    protected abstract CsvModel handeLines(String[] lineSplit);

    public List<T> parseCsv() {
        BufferedReader br = null;
        String line = "";
        List<T> csvList = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(getCsvFile()));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }

                String[] lineSplit = line.split(getCsvSplitBy());
                CsvModel csv = handeLines(lineSplit);
                csvList.add((T) csv);
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
        return csvList;
    }
}