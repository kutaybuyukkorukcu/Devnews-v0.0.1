package helper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigPropertyValues {

    public String getPropValueByKey(String key) throws IOException {

        String result;
        InputStream inputStream;

        Properties properties = new Properties();
        String propertiesFileName = "config.properties";

        inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

        if (inputStream == null) {
            throw new FileNotFoundException("Property file \"" + propertiesFileName + "\" not found in the classpath");
        }

        properties.load(inputStream);

        result = properties.getProperty(key);

        inputStream.close();

        return result;
    }
}
