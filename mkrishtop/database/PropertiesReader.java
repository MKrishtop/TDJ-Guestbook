package mkrishtop.database;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


public class PropertiesReader {
	private Properties configProps = new Properties();
	static final Logger logger = Logger.getLogger(PropertiesReader.class);
	
	public PropertiesReader(String filepath) {
		BasicConfigurator.configure();
		
		File file = new File(filepath);
		try {
			InputStream inStream = new FileInputStream(file);
			configProps.load(inStream);
		}
		catch (IOException ex) {
			logger.error(ex.toString());
		}
	}
	
	public PropertiesReader(InputStream inStream) {
		BasicConfigurator.configure();
		
		try {
			configProps.load(inStream);
		}
		catch (IOException ex) {
			logger.error(ex.toString());
		}
	}
	
	public String getProperty(String propertyName) {
		return configProps.getProperty(propertyName);
	}
	
	public String getLogin() {
		return configProps.getProperty("db.username");
	}
	
	public String getPassword() {
		return configProps.getProperty("db.password");
	}
}
