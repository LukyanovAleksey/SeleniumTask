import org.apache.commons.io.FileUtils;
import org.example.driver.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class BaseTest {
    private static final ThreadLocal<WebDriver> DRIVER_CONTAINER = new ThreadLocal<>();
    private final String REPORTS_PATH = "target/reports";

    public static WebDriver getDriver() {
        return DRIVER_CONTAINER.get();
    }

    @BeforeClass
    public void classSetup() {
        cleanReportFolder();
    }

    @BeforeMethod
    public void methodSetup() {
        DRIVER_CONTAINER.set(DriverFactory.createWebDriver());
    }

    @AfterMethod
    public void methodTeardown() {
        if (getDriver() != null) {
            getDriver().quit();
        }
    }

    @AfterClass
    public void classTeardown() {
        DRIVER_CONTAINER.remove();
    }

    public void captureScreenshot(String fileName) {
        try {
            new File(REPORTS_PATH).mkdirs();
            File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            Date d = new Date();
            String timeStamp = d.toString().replace(":", "_").replace(" ", "_");
            FileUtils.copyFile(srcFile, new File(REPORTS_PATH + File.separator + "screenshot-" + fileName + "_" + timeStamp + ".png"));
        } catch (IOException | WebDriverException e) {
            System.out.println("screenshot failed: " + e.getMessage());
        }
    }

    public void cleanReportFolder() {
        if (Files.exists(Path.of(REPORTS_PATH))) {
            try {
                FileUtils.cleanDirectory(new File(REPORTS_PATH));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}