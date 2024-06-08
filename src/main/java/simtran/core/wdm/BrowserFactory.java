package simtran.core.wdm;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import static simtran.core.config.ConfigManager.config;

/**
 * This enum provides a factory pattern for creating WebDriver instances with common browser configurations.
 * It supports Chrome, Firefox, Edge, and Safari browsers.
 *
 * @author simtran
 */
public enum BrowserFactory {
    /**
     * Represents the Chrome browser.
     */
    CHROME {
        @Override
        public ChromeOptions getOptions(String target) {
            ChromeOptions options = new ChromeOptions();

            options.addArguments(BrowserArguments.START_MAXIMIZED);
            options.addArguments(BrowserArguments.DISABLE_NOTIFICATIONS);
            options.addArguments(BrowserArguments.DISABLE_POPUP_BLOCKING);

            if (config(target).headless())
                options.addArguments(BrowserArguments.CHROME_HEADLESS);
            return options;
        }

        @Override
        public WebDriver initDriver(String target) {
            return new ChromeDriver(getOptions(target));
        }
    },

    /**
     * Represents the Firefox browser.
     */
    FIREFOX {
        @Override
        public FirefoxOptions getOptions(String target) {
            FirefoxOptions options = new FirefoxOptions();

            if (config(target).headless())
                options.addArguments(BrowserArguments.HEADLESS);
            return options;
        }

        @Override
        public WebDriver initDriver(String target) {
            FirefoxDriver driver = new FirefoxDriver(getOptions(target));
            driver.manage().window().maximize();
            return driver;
        }
    },

    /**
     * Represents the Microsoft Edge browser.
     */
    EDGE {
        @Override
        public EdgeOptions getOptions(String target) {
            EdgeOptions options = new EdgeOptions();

            options.addArguments(BrowserArguments.START_MAXIMIZED);

            if (config(target).headless())
                options.addArguments(BrowserArguments.HEADLESS);
            return options;
        }

        @Override
        public WebDriver initDriver(String target) {
            return new EdgeDriver(getOptions(target));
        }
    },

    /**
     * Represents the Safari browser.
     */
    SAFARI {
        @Override
        public SafariOptions getOptions(String target) {
            SafariOptions options = new SafariOptions();
            options.setAutomaticInspection(false);

            return options;
        }

        @Override
        public WebDriver initDriver(String target) {
            if (config(target).headless())
                throw new IllegalStateException("Headless not supported for Safari!");

            return new SafariDriver(getOptions(target));
        }
    };

    /**
     * Method to retrieve browser-specific options for WebDriver initialization.
     *
     * @return An instance of the appropriate `AbstractDriverOptions` subclass configured for the specific browser.
     */
    public abstract AbstractDriverOptions<?> getOptions(String target);

    /**
     * Method to initialize a WebDriver instance with the browser-specific options.
     *
     * @return A new WebDriver instance configured for the chosen browser.
     */
    public abstract WebDriver initDriver(String target);
}
