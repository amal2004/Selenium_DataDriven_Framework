package amal2004.rough;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TestProperties {
	
	public static void main(String[] args) throws IOException {
		
		Properties config = new Properties();
		Properties or = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\Config.properties");
		config.load(fis);
		System.out.println(config.getProperty("browser"));
		
		FileInputStream ois = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\OR.properties");
		or.load(ois);
		
		//driver.findElement(By.cssSelector(OR.getProperty("bmlButton"))).click();
		System.out.println(or.getProperty("bmlButton"));
		
	}

}
