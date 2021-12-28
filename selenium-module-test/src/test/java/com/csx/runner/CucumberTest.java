package com.csx.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
//do not change the name of report file. CI tool can only recognize result.html
@CucumberOptions(plugin = {"html:target/cucumber-reports/result.html", "pretty"}, monochrome = true,
    glue = {"com.csx.stepdefinitions"}, features = {"classpath:featurefiles"})

public class CucumberTest {
}
