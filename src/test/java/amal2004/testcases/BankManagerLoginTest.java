package amal2004.testcases;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import amal2004.base.TestBase;

public class BankManagerLoginTest extends TestBase {

	@Test
	public void bankManagerLoginTest() throws InterruptedException, IOException {
		
		//verifyEquals("abc", "xyz");
		Thread.sleep(3000);
		
		log.debug("Inside Login Test");
		click("bmlBtn_CSS");
		//driver.findElement(By.cssSelector(or.getProperty("bmlButton"))).click();
		Thread.sleep(2000);
		
		Assert.assertTrue(isElementPresent(By.cssSelector(or.getProperty("addCustBtn_CSS"))), "Login Not Successful!");
		log.debug("Login executed!!!");
		Reporter.log("Login Successfully Executed!");
		//Assert.fail("Login NOT Successfull");
	}
	
}
