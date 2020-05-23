package helper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigPropertyValues {

    public String getPropValueByKey(String key) throws IOException {

        String result = "";
        InputStream inputStream = null;

        try {
            Properties properties = new Properties();
            String propertiesFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

            if (inputStream == null) {
                throw new FileNotFoundException("Property file \"" + propertiesFileName + "\" not found in the classpath");
            }

            properties.load(inputStream);

            result = properties.getProperty(key);

        } catch (Exception e) {
            // TODO : Slf4j implementation for logging
            System.out.println("Exception " + e);
        } finally {
            inputStream.close();
        }

        return result;
    }
}
