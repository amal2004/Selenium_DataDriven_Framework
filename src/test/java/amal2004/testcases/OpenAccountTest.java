package amal2004.testcases;

import java.util.Hashtable;
import java.util.Iterator;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import amal2004.base.TestBase;
import amal2004.utilities.TestUtil;

public class OpenAccountTest extends TestBase {

	@Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void openAccountTest(Hashtable<String,String> data) throws InterruptedException {
		
		
			if(!(TestUtil.isTestRunnable("openAccountTest", excel))){
				
				throw new SkipException("Skipping the test "+"openAccountTest".toUpperCase()+ "as the Run mode is NO");
			}
			
			click("openaccount_CSS");
			select("customer_CSS", data.get("customer"));
			select("currency_CSS", data.get("currency"));
			click("process_CSS");
			Thread.sleep(2000);
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			alert.accept();
		
	}


	   

}
