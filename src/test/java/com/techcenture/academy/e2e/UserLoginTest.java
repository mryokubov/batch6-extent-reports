package com.techcenture.academy.e2e;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class UserLoginTest {

    private WebDriver driver;
    private ExtentReports extentReports;
    private ExtentTest extentTest;


    @BeforeTest
    public void beforeTest(){

        extentReports = new ExtentReports(  System.getProperty("user.dir") + "/test-output/ExtentReports.html", true);
        extentReports.addSystemInfo( "OS NAME" , System.getProperty("os.name"));
        extentReports.addSystemInfo("ENGINEER", System.getProperty("user.name"));
        extentReports.addSystemInfo("ENVIRONMENT", "QA");
        extentReports.addSystemInfo("JAVA VERSION", System.getProperty("java.version"));

    }


    String browserName;
    String browserVersion;
    @BeforeMethod
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        Capabilities browserCap = ((RemoteWebDriver) driver).getCapabilities();
        browserName = browserCap.getBrowserName();
        browserVersion = browserCap.getBrowserVersion();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.get("https://automationpractice.com");

        extentReports.addSystemInfo("BROWSER NAME", browserName);
        extentReports.addSystemInfo("BROWSER VERSION", browserVersion);
    }

    @AfterTest
    public void afterTest(){
        extentReports.flush();
        extentReports.close();
    }


    @Test
    public void loginPositiveTest(){

        extentTest = extentReports.startTest("User Login Positive Functionality");

        extentTest.log(LogStatus.INFO, "Verifying Title of the home page - should be My Store");
        Assert.assertTrue(driver.getTitle().equals("My Store"), "Title should be My Store");
        extentTest.log(LogStatus.PASS, "Title was correct");

        driver.findElement(By.partialLinkText("Sign in")).click();
        extentTest.log(LogStatus.INFO, "Clicked on Sign in Link");

        driver.findElement(By.id("email")).sendKeys("kevinlee1234@gmail.com");
        extentTest.log(LogStatus.INFO, "Entered kevinlee1234@gmail.com for the usename");

        driver.findElement(By.id("passwd")).sendKeys("Kevin124");
        extentTest.log(LogStatus.INFO, "Entered password for the password input");

        WebElement loginBtn = driver.findElement(By.id("SubmitLogin"));

        Assert.assertTrue(loginBtn.isEnabled(), "Login button was not enabled");
        extentTest.log(LogStatus.PASS, "Login button was enabled");

        loginBtn.click();
        extentTest.log(LogStatus.INFO, "Login button was clicked");

        extentTest.log(LogStatus.INFO, "Waiting for the title to be My account - My Store");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.titleIs("My account - My Store")); //exception will be thrown from here

        Assert.assertTrue(driver.getTitle().equals("My account - My Store"), "Title should My Store");
        extentTest.log(LogStatus.PASS, "Title is My account - My Store which is correct");

        WebElement sign_out = driver.findElement(By.partialLinkText("Sign out"));

        Assert.assertTrue(sign_out.isDisplayed(), "Sign out link is not displayed");
        extentTest.log(LogStatus.PASS, "Sign out link was displayed" );

        sign_out.click();
        extentTest.log(LogStatus.INFO, "Clicked on sing out link");

        extentTest.log(LogStatus.PASS, "Login end to end passed successfully");

    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {

        if(result.getStatus()==ITestResult.FAILURE){
            extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getName());
            extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getThrowable());

            String screenshotPath = getScreenshot(driver, result.getName());
            extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath));

        }
        else if(result.getStatus()==ITestResult.SKIP){
            extentTest.log(LogStatus.SKIP, "Test Case SKIPPED IS " + result.getName());
        }
        else if(result.getStatus()==ITestResult.SUCCESS){
            extentTest.log(LogStatus.PASS, "Test Case PASSED IS " + result.getName());

        }

        extentReports.endTest(extentTest);

        if (driver != null){
            driver.quit();
        }
    }



    public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination =  System.getProperty("user.dir") + "/test-output/" + screenshotName + dateName
                + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        return destination;
    }




}
