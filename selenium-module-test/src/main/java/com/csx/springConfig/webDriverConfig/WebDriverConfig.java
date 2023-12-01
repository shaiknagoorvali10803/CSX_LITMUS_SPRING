package com.csx.springConfig.webDriverConfig;

import com.csx.springConfig.annotation.WebDriverScopeBean;
import com.csx.springConfig.annotation.LazyConfiguration;
import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

@LazyConfiguration
public class WebDriverConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverConfig.class);

    @Value("${buildToolRun}")
    private String BUILD_TOOL_RUN;

    @Value("${selenium.grid.url}")
    private String SELENIUM_GRID_URL;

    @Value("${headless}")
    private String headless;

    @WebDriverScopeBean
    @ConditionalOnProperty(name = "browser", havingValue = "firefox")
    public WebDriver firefoxDriver() {
        String downloadFilepath = System.getProperty("user.dir");
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(false);
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", downloadFilepath);
        profile.setPreference("browser.download.useDownloadDir", true);
        profile.setPreference("browser.download.viewableInternally.enabledTypes", "");
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/xlsx;application/msword;application/ms-doc;application/doc;application/pdf;text/plain;application/text;text/xml;application/xml");
        profile.setPreference("pdfjs.disabled", true);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(profile);
        firefoxOptions.addArguments("--disable-web-security");
        firefoxOptions.addArguments("--allow-running-insecure-content");
        firefoxOptions.addArguments("--whitelist-ip *");
        firefoxOptions.addArguments("--ignore-certificate-errors", "--ignore-ssl-errors");
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        firefoxOptions.addArguments("start-maximized");
        firefoxOptions.addArguments("--private");
        if (BooleanUtils.toBoolean(headless)) {
            firefoxOptions.addArguments("--headless");
        }
        if (BooleanUtils.toBoolean(BUILD_TOOL_RUN)) {
            try {
                firefoxOptions.addArguments("--headless");
                firefoxOptions.addArguments("--window-size=1920,1080");
                return new RemoteWebDriver(new URL(SELENIUM_GRID_URL), firefoxOptions);
            } catch (MalformedURLException e) {
                LOGGER.info("Given remote web driver url is wrong");
            }
        }
        return new FirefoxDriver(firefoxOptions);
    }


    @WebDriverScopeBean
    @ConditionalOnProperty(name = "browser", havingValue = "chrome")
    public WebDriver chromeDriver(){
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.setHeadless(BooleanUtils.toBoolean(headless));
        chromeOptions.addArguments("--proxy-server='direct://'");
        chromeOptions.addArguments("--proxy-bypass-list=*");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        // to launch chrome in incognito mode
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--incognito");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        // to set default download path
        String downloadFilepath = System.getProperty("user.dir");
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
        if (BooleanUtils.toBoolean(headless)) {
            chromeOptions.addArguments("--headless");
        }
        if (BooleanUtils.toBoolean(BUILD_TOOL_RUN)) {
            try {
                chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("--window-size=1920,1080");
                return new RemoteWebDriver(new URL(SELENIUM_GRID_URL), chromeOptions);
            } catch (MalformedURLException e) {
                LOGGER.info("Given remote web driver url is wrong");
            }
        }
        return new ChromeDriver(chromeOptions);
    }

    @WebDriverScopeBean
    @ConditionalOnProperty(name = "browser", havingValue = "edge")
    public WebDriver edgeDriver() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--proxy-server='direct://'");
        edgeOptions.addArguments("--proxy-bypass-list=*");
        edgeOptions.addArguments("--ignore-certificate-errors");
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        edgeOptions.addArguments("--remote-allow-origins=*");
        edgeOptions.addArguments("start-maximized");
        edgeOptions.addArguments("-inprivate");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, edgeOptions);
        String downloadFilepath = System.getProperty("user.dir");
        HashMap<String, Object> edgePrefs = new HashMap<>();
        edgePrefs.put("profile.default_content_settings.popups", 0);
        edgePrefs.put("download.default_directory", downloadFilepath);
        edgeOptions.setExperimentalOption("prefs", edgePrefs);
        if (BooleanUtils.toBoolean(headless)) {
            edgeOptions.addArguments("--headless");
        }
        if (BooleanUtils.toBoolean(BUILD_TOOL_RUN)) {
            try {
                edgeOptions.addArguments("--headless");
                edgeOptions.addArguments("--window-size=1920,1080");
                return new RemoteWebDriver(new URL(SELENIUM_GRID_URL), edgeOptions);
            } catch (MalformedURLException e) {
                LOGGER.info("Given remote web driver url is wrong");
            }
        }
        return new EdgeDriver(edgeOptions);
    }
}
