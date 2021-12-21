package com.csx.stepdefinitions;

import com.csx.test.util.*;
import io.cucumber.java8.En;
import io.cucumber.java8.Scenario;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.BooleanUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


/**
 *
 * This Hooks class has to be present in the same package as step definitions
 *
 */
public class Hooks implements En {
	public static final String LOCAL_VIDEO_RECORD_FLAG = "localVideoRecord";
	@Inject
  private WebDriverProvider driverFactory;

  @Inject
  private ScenarioContext scenarioContext;

	@Inject
	VideoRecorder videoRecorder;

	public Hooks() {
		Before("@Chrome and not (@Headless or @IE or @Safari or @Firefox or @Edge)", (final Scenario scenario) -> {
			//initialize video recording on only local
			localVideoRecord();
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.CHROME);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("(@Chrome and @Headless) and not (@IE or @Safari or @Firefox or @Edge)", (final Scenario scenario) -> {
			//initialize video recording on only local
			localVideoRecord();
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.CHROME, true);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("@IE and not (@Chrome or @Safari or @Firefox or @Edge)", (final Scenario scenario) -> {
			//initialize video recording on only local
			localVideoRecord();
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.INTERNET_EXPLORER);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("@Safari and not (@Chrome or @IE or @Firefox or @Edge)", (final Scenario scenario) -> {
			//initialize video recording on only local
			localVideoRecord();
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.SAFARI);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("@Firefox and not (@Headless or @Chrome or @Safari or @IE or @Edge)", (final Scenario scenario) -> {
			//initialize video recording on only local
			localVideoRecord();
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.FIRE_FOX);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("(@Firefox and @Headless) and not (@Chrome or @Safari or @IE or @Edge)", (final Scenario scenario) -> {
			//initialize video recording on only local
			localVideoRecord();
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.FIRE_FOX, true);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("@Edge and not (@Chrome or @Safari or @IE or @Firefox)", (final Scenario scenario) -> {
			//initialize video recording on only local
			localVideoRecord();
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.EDGE, true);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		Before("not (@Chrome or @IE or @Safari or @Firefox or @Edge)", (final Scenario scenario) -> {
			//initialize video recording on only local
			localVideoRecord();
			driverFactory.generateWebDriver(WebDriverProvider.BrowserType.CHROME);
			// initialize context
			scenarioContext.setScenario(scenario);
		});

		After((final Scenario scenario) -> {
			// clear scenario context after each scenario
			scenarioContext = new ScenarioContext();
			scenarioContext.clearContextData();
			if (scenario.isFailed()) {
				try {
					captureScreenshot(driverFactory.getInstance(), scenario);
					if (BooleanUtils.toBoolean(System.getProperty(LOCAL_VIDEO_RECORD_FLAG))) {
						videoRecorder.stopRecording();
					}
				} catch (ClassCastException | IOException e) {
					throw new LoggingException(e);
				}
			} 
			else {
				if (BooleanUtils.toBoolean(System.getProperty(LOCAL_VIDEO_RECORD_FLAG))) {
					videoRecorder.stopRecording();
					String fileName = FileHandlingUtil.getTheNewestFile(SeleniumUtil.downloadPath, SeleniumUtil.videoFileType);
					FileHandlingUtil.deleteExistingFile(fileName);
				}
			}
			driverFactory.getInstance().close();
			driverFactory.getInstance().quit();
		});
	}

	private void localVideoRecord() throws Exception {
		if (BooleanUtils.toBoolean(System.getProperty(LOCAL_VIDEO_RECORD_FLAG))) {
			videoRecorder.startRecording();
		}
	}

	private String captureScreenshot(final WebDriver driver, final Scenario scenario) throws IOException {
	    //final Date now = new Date();
	    final String dateString = DateTimeUtil.todayDate("dd-MMM-yyy");
	    final String dateAndTimeString = DateTimeUtil.todayDate("dd-MMM-yyy");
	    final TakesScreenshot screenShot = (TakesScreenshot) driver;
	    final File source = screenShot.getScreenshotAs(OutputType.FILE);
		String screenshotName = screenshotName(scenario, dateString, dateAndTimeString);
		final String dest = ".." + File.separator + "target" + File.separator + "cucumber-html-reports" + File.separator +  screenshotName;
//	    final File destination = new File(dest);
//	    FileUtils.copyFile(source, destination);
		scenario.attach(Files.readAllBytes(source.toPath()), "image/png", screenshotName);
	    return dest;
	  }

	private String screenshotName(Scenario scenario, String dateString, String dateAndTimeString) {
		return dateString + File.separator
				+ "Error_" + scenario.getName().replaceAll(" ", "_") + dateAndTimeString.replaceAll(" ", "_") + ".png";
	}

}