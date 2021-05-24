package github.enplee.Utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesFileUtil {
    private PropertiesFileUtil() {
    }

    public static Properties readPropertiesFile(String fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String propertiesPath = "";
        if(url != null){
            propertiesPath = url.getPath()+fileName;
        }
        Properties properties = null;
        try(InputStreamReader inputStreamReader = new InputStreamReader(
                new FileInputStream(propertiesPath), StandardCharsets.UTF_8)){

            properties = new Properties();
            properties.load(inputStreamReader);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
