package amal2004.base;

import static amal2004.base.TestBase.extent;
import static amal2004.base.TestBase.logger;
import static amal2004.base.TestBase.report;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import amal2004.listeners.CustomListeners;
import amal2004.utilities.ExcelReader;
import amal2004.utilities.TestUtil;

public class TestBase {

	public static WebDriver driver;
	public static Properties configProp = new Properties();
	public static Properties or = new Properties();
	public static FileInputStream fis;
	public static Logger log = LogManager.getLogger();
	public static ExcelReader excel = new ExcelReader(System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\testdata.xlsx");
	public static WebDriverWait wait;
	static WebElement dropdown;
	public static String browser;
	
	public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();
	public static ExtentHtmlReporter report=new ExtentHtmlReporter("./Report/Report.html");
	public static ExtentReports extent=new ExtentReports();
	public static ExtentTest logger;

	@BeforeClass
	public static void launch(){    	
		 extent.attachReporter(report);
	}
	
	@BeforeMethod
	public void setup(Method method) {
    	logger=extent.createTest(method.getName());
	}
	
	@AfterMethod
	public void checkStatus(ITestResult result) {
		try {
			if(result.getStatus()== ITestResult.FAILURE) {
				String path = TestUtil.captureScreenshot(driver);
				logger.fail(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		extent.flush();
	}

	@BeforeSuite
	public void setUp() throws InterruptedException {
		if (driver == null) {

			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\Config.properties");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				configProp.load(fis);
				log.debug("Config file Loaded!!!");
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\OR.properties");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				or.load(fis);
				log.debug("OR file Loaded!!!");
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			if (System.getenv("browser") != null && !System.getenv("browser").isEmpty()) {
				browser = System.getenv("browser");
			} else {
				browser = configProp.getProperty("browser");
			}
			configProp.setProperty("browser", browser);
			System.out.println(configProp.getProperty("browser"));
			
			
			if (configProp.getProperty("browser").equals("chrome")) {
				driver = new ChromeDriver();
				log.debug("Chrome Launched!!!");
				
			} else if (configProp.getProperty("browser").equals("edge")) {
				driver = new FirefoxDriver();
			}

		} else if (configProp.getProperty("browser").equals("ie")) {
			driver = new InternetExplorerDriver();
		}
		
		driver.get(configProp.getProperty("testsiteurl"));
		log.debug("Navigated to :" + configProp.getProperty("testsiteurl"));
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(configProp.getProperty("implicit.wait"))));
		
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		

	}

	public void click(String locator) {

		System.out.println("locator : " + locator);

		if (locator.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(or.getProperty(locator))).click();
		} else if (locator.endsWith("_XPATH")) {
			driver.findElement(By.xpath(or.getProperty(locator))).click();
		} else if (locator.endsWith("_ID")) {
			driver.findElement(By.id(or.getProperty(locator))).click();
		}
		CustomListeners.testReport.get().log(Status.INFO, "Clicking on : " + locator);
	}

	public void type(String locator, String value) {
		
		if (locator.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(or.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_XPATH")) {
			driver.findElement(By.xpath(or.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_ID")) {
			driver.findElement(By.id(or.getProperty(locator))).sendKeys(value);
		}

		CustomListeners.testReport.get().log(Status.INFO, "Typing in : " + locator + " entered value as " + value);

	}
	
	public void select(String locator, String value) {

		if (locator.endsWith("_CSS")) {
			dropdown = driver.findElement(By.cssSelector(or.getProperty(locator)));
		} else if (locator.endsWith("_XPATH")) {
			dropdown = driver.findElement(By.xpath(or.getProperty(locator)));
		} else if (locator.endsWith("_ID")) {
			dropdown = driver.findElement(By.id(or.getProperty(locator)));
		}
		
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);

		CustomListeners.testReport.get().log(Status.INFO, "Selecting from dropdown : " + locator + " value as " + value);

	}
	
	public static void verifyEquals(String expected, String actual) throws IOException {

		try {
			Assert.assertEquals(actual, expected);
		} catch (Throwable t) {

			TestUtil.captureScreenshot(driver);
			// ReportNG
			Reporter.log("<br>" + "Verification failure : " + t.getMessage() + "<br>");
			Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + "><img src=" + TestUtil.screenshotName
					+ " height=200 width=200></img></a>");
			Reporter.log("<br>");
			Reporter.log("<br>");
			// Extent Reports
			CustomListeners.testReport.get().log(Status.FAIL, " Verification failed with exception : " + t.getMessage());
			//CustomListeners.testReport.get().log(Status.FAIL, CustomListeners.testReport.get().addScreenCaptureFromPath(TestUtil.screenshotName));

		}

	}
	
	
	public boolean isElementPresent(By by) {
		
		try {
			
			driver.findElement(by);
			return true;
			
		} catch (NoSuchElementException e) {
			return false;
		}
		
	}
	
	
	@AfterSuite
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
		
		log.debug("Test execution completed!!!");
	}
}
