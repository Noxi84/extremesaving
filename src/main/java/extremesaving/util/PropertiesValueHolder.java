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

    public Boolean getBoolean(PropertyValueEnum propertyValueENum) {
        return Boolean.valueOf(getPropValue(propertyValueENum));
    }

    public String getPropValue(PropertyValueEnum propertyValueENum) {
        try {
            return getPropValues().get(propertyValueENum.getValue());
        } catch (Exception ex) {
            throw new IllegalStateException("Error retrieving property " + propertyValueENum.getValue() + " from config.properties");
        }
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
            throw new IllegalStateException("Unable to read properties. Exception: " + e.getMessage(), e);
        } finally {
            inputStream.close();
        }

        return results;
    }
}