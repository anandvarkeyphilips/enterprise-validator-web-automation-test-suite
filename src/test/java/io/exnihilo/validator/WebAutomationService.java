package io.exnihilo.validator;

import ch.qos.logback.classic.Logger;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Selenium driver automation setup for Enterprise Validator
 *
 * @author Anand Varkey Philips
 * @since 1.0.0
 */
@Slf4j
    public class WebAutomationService {

    private static final String CHROME_BIN = "CHROME_BIN";

    public WebDriver setUp() {

        if (null == System.getenv(CHROME_BIN)) {
            log.warn("Using Web Driver Manager to download and setup chrome driver");
            Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            root.setLevel(ch.qos.logback.classic.Level.INFO);
            WebDriverManager.chromedriver().setup();
        }

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless"); //useful for running without opening chrome
        chromeOptions.addArguments("disable-gpu");
        chromeOptions.addArguments("disable-dev-shm-usage");
        chromeOptions.addArguments("no-sandbox");
        // chromeOptions.merge(getCapabilities()); //useful for debugging

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        return driver;
    }

    private static DesiredCapabilities getCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        LoggingPreferences loggingPreferences = new LoggingPreferences();
        loggingPreferences.enable(LogType.BROWSER, Level.ALL);
        loggingPreferences.enable(LogType.DRIVER, Level.ALL);
        loggingPreferences.enable(LogType.PERFORMANCE, Level.INFO);
        desiredCapabilities.setCapability("goog:loggingPrefs", loggingPreferences);
        return desiredCapabilities;
    }

    public static void getLogs(WebDriver driver) {
        LogEntries logEntries = driver.manage().logs().get(LogType.DRIVER);
        for (LogEntry logEntry : logEntries) {
            log.info(logEntry.getTimestamp() + "::" + logEntry.getMessage());
        }
        logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry logEntry : logEntries) {
            log.info(logEntry.getTimestamp() + "::" + logEntry.getMessage());
        }
        logEntries = driver.manage().logs().get(LogType.PERFORMANCE);
        for (LogEntry logEntry : logEntries) {
            log.info(logEntry.getTimestamp() + "::" + logEntry.getMessage());
        }
    }
}
