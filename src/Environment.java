
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

public class Environment {

    private static final String filename = ".config";
    private Properties prop = new Properties();

    public Environment() {
        load();
    }

    private void load() {

        FileInputStream envFile = null;

        try {

            String filename = "config.properties";
            envFile = new FileInputStream(filename);

            if(envFile==null){
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(envFile);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(envFile!=null){
                try {
                    envFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String get(String propertyName) {
        return prop.getProperty(propertyName);
    }

    public String get(String propertyName, String propertyNameDefault) {
        return prop.getProperty(propertyName, propertyNameDefault);
    }
}
