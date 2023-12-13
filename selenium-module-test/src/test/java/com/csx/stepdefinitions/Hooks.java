package com.csx.stepdefinitions;

import com.csx.springConfig.annotation.LazyAutowired;
import com.csx.test.util.FileHandlingUtil;
import com.csx.test.util.LoggingException;
import com.csx.test.util.VideoRecorder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class Hooks {
    @LazyAutowired
    private ApplicationContext applicationContext;

    @Autowired
    private ScenarioContext scenarioContext;

    @Before
    public void before(final Scenario scenario){
        scenarioContext.setScenario(scenario);
    }

    @After
    public void afterScenario(Scenario scenario) throws Exception {
        if (scenario.isFailed()) {
            scenario.attach(((TakesScreenshot) this.applicationContext.getBean(WebDriver.class)).getScreenshotAs(OutputType.BYTES), "image/png", scenario.getName());
        }
        this.applicationContext.getBean(WebDriver.class).quit();
    }

    private void localVideoRecord() throws Exception {
        if (!BooleanUtils.toBoolean(System.getProperty("buildToolRun"))) {
            VideoRecorder.startRecording();
        }
    }

}


