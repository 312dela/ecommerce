package delarosa.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("reports/extent-report.html");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setDocumentTitle("Automation Test Report");
        htmlReporter.config().setReportName("E-Commerce Test Report");
        htmlReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", "Dela Rosa");
    }

    @BeforeMethod
    public void setupDriver(Method method, Object[] testData) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://rahulshettyacademy.com/client");
        String testDescription;

        if (testData.length > 0 && testData[testData.length - 1] instanceof String) {
            testDescription = (String) testData[testData.length - 1];
        } else {
            testDescription = method.getAnnotation(Test.class).description();
        }

        test = extent.createTest(testDescription);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "Test passed");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = takeScreenshot(result.getName());
            test.log(Status.FAIL, result.getThrowable());
            test.addScreenCaptureFromPath("../" + screenshotPath);
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "Test skipped");
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    public String takeScreenshot(String testName) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String dest = "reports/screenshots/" + testName + ".png";
        try {
            FileUtils.copyFile(src, new File(dest));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }
}