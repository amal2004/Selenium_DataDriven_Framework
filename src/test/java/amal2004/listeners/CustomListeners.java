package amal2004.listeners;

import java.io.IOException;
import java.util.Date;

import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import amal2004.base.TestBase;
import amal2004.utilities.TestUtil;


public class CustomListeners extends TestBase implements ITestListener,ISuiteListener {
	
	static Date d = new Date();
	static String fileName = "Extent_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";
	static String messageBody;
	
	//private static ExtentReports extent = ExtentManager.createInstance(System.getProperty("user.dir")+"\\reports\\"+fileName);
    //	static ExtentHtmlReporter exreport = new ExtentHtmlReporter("./report/Report.html");
	//static ExtentReports extent = new ExtentReports();
	
	//static ExtentTest logger;
	
	
	
	public void onTestStart(ITestResult result) {
		
		ExtentTest test = extent.createTest(result.getTestClass().getName()+"  @TestCase : " + result.getMethod().getMethodName());
		testReport.set(test);
	}

	public void onTestSuccess(ITestResult result) {
		
		String methodName = result.getMethod().getMethodName();
		String logText="<b>"+"TEST CASE:- "+ methodName.toUpperCase()+ " PASSED"+"</b>";
		Markup m=MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		testReport.get().pass(m);
	}
	
	
	public void onTestFailure(ITestResult result) {
				
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

	public void onTestSkipped(ITestResult result) {
		
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
		
	}

	public void onStart(ITestContext context) {
		
		
	}

	public void onFinish(ITestContext context) {
		
		if (extent != null) {
			extent.flush();
		}
		
	}

	
	
}
