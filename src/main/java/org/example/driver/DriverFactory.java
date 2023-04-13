package org.example.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {
    public static WebDriver createWebDriver() {
        //java.util.concurrent.TimeoutException thrown at random netty read timeouts with RemoteWebDriver
        //https://github.com/SeleniumHQ/selenium/issues/9528
        System.setProperty("webdriver.http.factory", "jdk-http-client");

        String driver = System.getProperty("driver", "chrome");
        Capabilities capabilities = null;

        if ("chrome".equals(driver)) {
            capabilities = new ChromeOptions();
            ((ChromeOptions) capabilities).addArguments(
                    "--no-sandbox",
                    "--disable-dev-shm-usage",
                    "--disable-browser-side-navigation",
                    "--disable-gpu"
            );
        } else if ("firefox".equals(driver)) {
            capabilities = new FirefoxOptions();
        } else if ("edge".equals(driver)) {
            capabilities = new EdgeOptions();
        }

        String url = System.getProperty("seleniumServerUrl", "http://localhost:4444/");
        if (url == null) {
            throw new IllegalStateException("No 'seleniumServerUrl' system property set");
        }

        String target = System.getProperty("target", "remote");
        WebDriver webDriver;
        switch (target) {
            case "remote":
                try {
                    webDriver = new RemoteWebDriver(new URL(url), capabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                break;
            case "local":
                webDriver = new ChromeDriver();
                break;
            default:
                throw new IllegalStateException("Incorrect system property 'target': " + target);
        }

        webDriver.manage().timeouts().scriptTimeout(Duration.ofMillis(5000));
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(30000));
        webDriver.manage().window().maximize();

        return webDriver;
    }
}