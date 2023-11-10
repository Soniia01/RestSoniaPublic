package common;

import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.Properties;

@Singleton
public class Configuration {

    private static Configuration instance;
        private Properties p;
        private Configuration() {
            try {
                p= new Properties();
                p.load(Configuration.class.getClassLoader().getResourceAsStream(Constantes.RESOURCES_CONFIG_FILES_PROPERTIES));
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        public static Configuration getInstance() {
            if (instance==null) {
                instance=new Configuration();
            }
            return instance;
        }
        public String getProperty(String key) {
            return p.getProperty(key);
        }

}
