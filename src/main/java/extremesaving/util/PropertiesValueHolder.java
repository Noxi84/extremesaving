package extremesaving.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesValueHolder {

    private static PropertiesValueHolder instance;
    private Map<String, String> results;

    public static PropertiesValueHolder getInstance() {
        if (instance == null) {
            instance = new PropertiesValueHolder();
        }
        return instance;
    }

    public String getPropValue(PropertyValueENum propertyValueENum) {
        try {
            return getPropValues().get(propertyValueENum.getValue());
        } catch (Exception ex) {
            System.out.println("Error retrieving property " + propertyValueENum.getValue() + " from config.properties");
            ex.printStackTrace();
        }
        return null;
    }

    public Map<String, String> getPropValues() throws IOException {
        if (results != null) {
            return results;
        }

        results = new HashMap<>();
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            for (Map.Entry<Object, Object> property : prop.entrySet()) {
                results.put((String) property.getKey(), (String) property.getValue());
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }

        return results;
    }
}